package com.example.budgetboss.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetboss.data.local.dao.UserDao;
import com.example.budgetboss.data.local.entity.UserEntity;
import com.example.budgetboss.domain.models.User;
import com.example.budgetboss.domain.repository.AuthRepository;
import com.example.budgetboss.utils.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseDatabase firebaseDatabase;
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public AuthRepositoryImpl(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase, UserDao userDao) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
        this.userDao = userDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public LiveData<Resource<User>> login(String email, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        fetchUserFromRealtimeDb(firebaseUser.getUid(), result);
                    }
                })
                .addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage(), null)));

        return result;
    }

    @Override
    public LiveData<Resource<User>> register(String username, String email, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        User newUser = new User(
                                firebaseUser.getUid(),
                                username,
                                email,
                                0.0, 0.0, 0.0,
                                System.currentTimeMillis()
                        );
                        saveUserToRealtimeDb(newUser, result);
                    }
                })
                .addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage(), null)));

        return result;
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
        executor.execute(userDao::clearUser);
    }

    @Override
    public LiveData<User> getCurrentUser() {
        // Mapping Entity to Domain Model is required here strictly speaking, 
        // but for simplicity I'll skip transformation for now or assume observing Fragment can handle it
        // Or better, let's just return what we have in DB.
        // Since LiveData transformation is tricky without proper setup, I will return null for now.
        // Wait, I should implement a transformation. 
        // For MVP, assume UserEntity and User are compatible or I'll just map it in ViewModel.
        return androidx.lifecycle.Transformations.map(userDao.getUser(), entity -> {
             if (entity == null) return null;
             return new User(entity.id, entity.username, entity.email, entity.upiBalance, entity.cashBalance, entity.vaultBalance, entity.createdAt);
        });
    }

    private void fetchUserFromRealtimeDb(String userId, MutableLiveData<Resource<User>> result) {
        DatabaseReference userRef = firebaseDatabase.getReference("users").child(userId);
        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.getValue(User.class);
            if (user != null) {
                saveUserToLocalDb(user);
                result.setValue(Resource.success(user));
            } else {
                result.setValue(Resource.error("User data not found", null));
            }
        }).addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage(), null)));
    }

    private void saveUserToRealtimeDb(User user, MutableLiveData<Resource<User>> result) {
        DatabaseReference userRef = firebaseDatabase.getReference("users").child(user.getId());
        userRef.setValue(user)
                .addOnSuccessListener(aVoid -> {
                    saveUserToLocalDb(user);
                    result.setValue(Resource.success(user));
                })
                .addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage(), null)));
    }

    private void saveUserToLocalDb(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUpiBalance(),
                user.getCashBalance(),
                user.getVaultBalance(),
                null, // Pin not synced for security usually, or handled separately
                user.getCreatedAt()
        );
        executor.execute(() -> userDao.insertUser(entity));
    }
}
