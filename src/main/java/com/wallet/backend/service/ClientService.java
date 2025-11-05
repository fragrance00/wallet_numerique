package com.wallet.backend.service;

import com.wallet.backend.entities.Client;
import com.wallet.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("Email déjà utilisé !");
        }
        return clientRepository.save(client);
    }

    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setFirstName(updatedClient.getFirstName());
                    client.setLastName(updatedClient.getLastName());
                    client.setEmail(updatedClient.getEmail());
                    client.setPhone(updatedClient.getPhone());
                    client.setAddress(updatedClient.getAddress());
                    client.setPassword(updatedClient.getPassword()); // ⚠️ CHANGEMENT : ajoutez cette ligne
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}