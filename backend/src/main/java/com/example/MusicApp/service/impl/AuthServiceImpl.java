package com.example.MusicApp.service.impl;

import com.example.MusicApp.model.Account;
import com.example.MusicApp.repository.AccountRepository;
import com.example.MusicApp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;

    public boolean login(String email, String password) {
        Account currentAccount = accountRepository.findByEmail(email);
        if (currentAccount != null) {
            return currentAccount.getPassword().equals(password);
        }
        return false;
    }

//    public TokenResponseDTO getToken()


}
