package com.wallet.backend.controller;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.service.BankerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankers")
@CrossOrigin(origins = "*")
public class BankerController {
    private final BankerService bankerService;

    public BankerController(BankerService bankerService) {
        this.bankerService = bankerService;
    }

    @GetMapping
    public List<Banker> getAllBankers() {
        return bankerService.getAllBankers();
    }

    @GetMapping("/{id}")
    public Banker getBankerById(@PathVariable Long id) {
        return bankerService.getBankerById(id);
    }

    @PostMapping
    public Banker createBanker(@RequestBody Banker banker) {
        return bankerService.createBanker(banker);
    }

    @PutMapping("/{id}")
    public Banker updateBanker(@PathVariable Long id, @RequestBody Banker banker) {
        return bankerService.updateBanker(id, banker);
    }

    @DeleteMapping("/{id}")
    public void deleteBanker(@PathVariable Long id) {
        bankerService.deleteBanker(id);
    }
}
