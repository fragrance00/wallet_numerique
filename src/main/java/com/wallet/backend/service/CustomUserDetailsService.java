package com.wallet.backend.service;

import com.wallet.backend.entities.Admin;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Client;
import com.wallet.backend.repository.AdminRepository;
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
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BankerRepository bankerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Chercher dans toutes les tables
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return User.builder()
                    .username(admin.get().getUsername())
                    .password(admin.get().getPasswordHash())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }

        Optional<Banker> banker = bankerRepository.findByUsername(username);
        if (banker.isPresent()) {
            return User.builder()
                    .username(banker.get().getUsername())
                    .password(banker.get().getPasswordHash())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_BANKER")))
                    .build();
        }

        Optional<Client> client = clientRepository.findByEmail(username);
        if (client.isPresent()) {
            return User.builder()
                    .username(client.get().getEmail())
                    .password(client.get().getPasswordHash())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")))
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username/email: " + username);
    }
}