package com.example.budgetboss.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetboss.domain.models.User;
import com.example.budgetboss.domain.repository.AuthRepository;
import com.example.budgetboss.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<android.util.Pair<String, String>> _loginTrigger = new MutableLiveData<>();
    public final LiveData<Resource<User>> loginResult;

    private final MutableLiveData<String[]> _registerTrigger = new MutableLiveData<>();
    public final LiveData<Resource<User>> registerResult;

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;

        loginResult = androidx.lifecycle.Transformations.switchMap(_loginTrigger,
                pair -> authRepository.login(pair.first, pair.second));

        registerResult = androidx.lifecycle.Transformations.switchMap(_registerTrigger,
                data -> authRepository.register(data[0], data[1], data[2]));
    }

    public void login(String email, String password) {
        _loginTrigger.setValue(new android.util.Pair<>(email, password));
    }

    public void register(String username, String email, String password) {
        _registerTrigger.setValue(new String[] { username, email, password });
    }

    public LiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }
}
