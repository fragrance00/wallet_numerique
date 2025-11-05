package com.wallet.backend.service;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Client;
import com.wallet.backend.repository.BankerRepository;
import com.wallet.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BankerRepository bankerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Chercher d'abord dans Banker
        Banker banker = bankerRepository.findByUsername(username).orElse(null);
        if (banker != null) {
            return User.builder()
                    .username(banker.getUsername())
                    .password(banker.getPassword()) // ⚠️ CHANGEMENT : passwordHash → password
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + banker.getRole())))
                    .build();
        }

        // Si pas banker, chercher dans Client
        Client client = clientRepository.findByEmail(username).orElse(null);
        if (client != null) {
            return User.builder()
                    .username(client.getEmail())
                    .password(client.getPassword()) // ⚠️ CHANGEMENT : passwordHash → password
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")))
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}