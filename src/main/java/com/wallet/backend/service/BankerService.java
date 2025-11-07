package com.wallet.backend.service;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.repository.BankerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankerService {
    private final BankerRepository bankerRepository;

    public BankerService(BankerRepository bankerRepository) {
        this.bankerRepository = bankerRepository;
    }

    public List<Banker> getAllBankers() {
        return bankerRepository.findAll();
    }

    public Banker getBankerById(Long id) {
        return bankerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banquier introuvable avec l'id: " + id));
    }

    public Banker updatePassword(Long bankerId, String newPassword) {
        Banker banker = bankerRepository.findById(bankerId)
                .orElseThrow(() -> new RuntimeException("Banquier introuvable avec l'ID : " + bankerId));

        // ⚠️ Ici, tu peux ajouter du hachage si tu veux sécuriser le mot de passe
        banker.setPassword(newPassword);

        return bankerRepository.save(banker);
    }

    public Banker createBanker(Banker banker) {
        return bankerRepository.save(banker);
    }

    public Banker updateBanker(Long id, Banker updatedBanker) {
        Banker existing = getBankerById(id);
        existing.setFirstName(updatedBanker.getFirstName());
        existing.setLastName(updatedBanker.getLastName());
        return bankerRepository.save(existing);
    }

    public void deleteBanker(Long id) {
        if (!bankerRepository.existsById(id)) {
            throw new RuntimeException("Banquier introuvable avec l'id: " + id);
        }
        bankerRepository.deleteById(id);
    }
}