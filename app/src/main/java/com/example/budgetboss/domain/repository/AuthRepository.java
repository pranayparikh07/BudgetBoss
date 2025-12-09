package com.example.budgetboss.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.budgetboss.domain.models.User;
import com.example.budgetboss.utils.Resource;

public interface AuthRepository {
    LiveData<Resource<User>> login(String email, String password);
    LiveData<Resource<User>> register(String username, String email, String password);
    void logout();
    LiveData<User> getCurrentUser();
}
