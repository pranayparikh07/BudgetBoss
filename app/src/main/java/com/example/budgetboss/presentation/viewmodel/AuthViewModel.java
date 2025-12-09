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

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Resource<User>> login(String email, String password) {
        return authRepository.login(email, password);
    }

    public LiveData<Resource<User>> register(String username, String email, String password) {
        return authRepository.register(username, email, password);
    }
    
    public LiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }
}
