package com.example.budgetboss.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetboss.domain.repository.VaultRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class VaultViewModel extends ViewModel {

    private final VaultRepository vaultRepository;
    
    // State
    private final MutableLiveData<Boolean> _isPinSet = new MutableLiveData<>();
    public LiveData<Boolean> isPinSet() { return _isPinSet; }

    private final MutableLiveData<Boolean> _isUnlocked = new MutableLiveData<>(false);
    public LiveData<Boolean> isUnlocked() { return _isUnlocked; }
    
    private final MutableLiveData<Double> _balance = new MutableLiveData<>();
    public LiveData<Double> getBalance() { return _balance; }

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> getError() { return _error; }

    @Inject
    public VaultViewModel(VaultRepository vaultRepository) {
        this.vaultRepository = vaultRepository;
        checkPinStatus();
    }

    public void checkPinStatus() {
        _isPinSet.setValue(vaultRepository.hasPin());
    }

    public void setPin(String pin) {
        if (pin.length() != 6) {
            _error.setValue("PIN must be 6 digits");
            return;
        }
        vaultRepository.setPin(pin);
        _isPinSet.setValue(true);
        _isUnlocked.setValue(true); // Auto unlock after setting
        loadBalance();
    }

    public void validatePin(String pin) {
        if (vaultRepository.validatePin(pin)) {
            _isUnlocked.setValue(true);
            loadBalance();
        } else {
            _error.setValue("Incorrect PIN");
        }
    }

    public void loadBalance() {
        if (_isUnlocked.getValue() != Boolean.TRUE) return;
        _balance.setValue(vaultRepository.getVaultBalance());
    }

    public void addToVault(double amount) {
        vaultRepository.addToVault(amount);
        loadBalance();
    }
    
    public void withdrawFromVault(double amount) {
        if (_balance.getValue() != null && _balance.getValue() >= amount) {
             vaultRepository.withdrawFromVault(amount);
             loadBalance();
        } else {
            _error.setValue("Insufficient funds in vault");
        }
    }
}
