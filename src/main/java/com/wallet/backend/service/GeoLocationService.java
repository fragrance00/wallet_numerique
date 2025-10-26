package com.wallet.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeoLocationService {

    public String getClientLocation() {
        try {
            System.out.println("🔍 Début détection géolocalisation...");

            // Récupérer notre IP publique
            String publicIp = getPublicIPAddress();
            System.out.println("🌐 IP Publique du serveur: " + publicIp);

            // Si IP locale, on ne peut pas géolocaliser
            if (publicIp.equals("127.0.0.1")) {
                System.out.println("❌ IP locale détectée - géolocalisation impossible");
                return "Local Server";
            }

            // Essayer plusieurs APIs modernes
            String location = tryIpApiCom(publicIp);
            if (!location.equals("Online Transfer")) {
                return location;
            }

            location = tryIpInfoIo(publicIp);
            if (!location.equals("Online Transfer")) {
                return location;
            }

            System.out.println("❌ Aucune localisation trouvée");
            return "Online Transfer";

        } catch (Exception e) {
            System.err.println("💥 Erreur géolocalisation: " + e.getMessage());
            return "Online Transfer";
        }
    }

    /**
     * API ip-api.com (gratuite)
     */
    private String tryIpApiCom(String ipAddress) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://ip-api.com/json/" + ipAddress + "?fields=status,message,country,countryCode,region,regionName,city,district,zip,lat,lon,isp,org,as,query";
            System.out.println("📡 Appel ip-api.com: " + url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println("📨 Réponse ip-api.com: " + response);

            if (response != null && "success".equals(response.get("status"))) {
                String city = (String) response.get("city");
                String country = (String) response.get("country");
                String region = (String) response.get("regionName");
                String district = (String) response.get("district");

                System.out.println("📍 ip-api.com - Ville: " + city + ", District: " + district + ", Région: " + region + ", Pays: " + country);

                // Priorité au district (plus précis)
                if (district != null && !district.isEmpty() && country != null && !country.isEmpty()) {
                    String location = district + ", " + (region != null ? region : "") + ", " + country;
                    System.out.println("✅ Localisation déterminée (district): " + location);
                    return location.trim().replace(", ,", ",");
                }
                // Fallback: Ville, Région, Pays
                else if (city != null && !city.isEmpty() && country != null && !country.isEmpty()) {
                    String location = (region != null && !region.isEmpty())
                            ? city + ", " + region + ", " + country
                            : city + ", " + country;
                    System.out.println("✅ Localisation déterminée (ville): " + location);
                    return applyMoroccoRegionCorrection(location); // Correction régions Maroc
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur ip-api.com: " + e.getMessage());
        }
        return "Online Transfer";
    }

    /**
     * API ipinfo.io (plus moderne et précise)
     */
    private String tryIpInfoIo(String ipAddress) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://ipinfo.io/" + ipAddress + "/json?token=demo"; // Utilise le token demo gratuit
            System.out.println("📡 Appel ipinfo.io: " + url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println("📨 Réponse ipinfo.io: " + response);

            if (response != null && response.get("country") != null) {
                String city = (String) response.get("city");
                String country = (String) response.get("country");
                String region = (String) response.get("region");
                String loc = (String) response.get("loc"); // Coordonnées GPS

                System.out.println("📍 ipinfo.io - Ville: " + city + ", Région: " + region + ", Pays: " + country + ", GPS: " + loc);

                if (city != null && !city.isEmpty() && country != null && !country.isEmpty()) {
                    String location = (region != null && !region.isEmpty())
                            ? city + ", " + region + ", " + country
                            : city + ", " + country;
                    System.out.println("✅ Localisation déterminée (ipinfo.io): " + location);
                    return applyMoroccoRegionCorrection(location);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur ipinfo.io: " + e.getMessage());
        }
        return "Online Transfer";
    }

    /**
     * Correction des régions marocaines (ancienne -> nouvelle organisation)
     */
    private String applyMoroccoRegionCorrection(String location) {
        if (location == null || !location.contains("Morocco")) {
            return location;
        }

        // Corrections pour la nouvelle organisation territoriale marocaine
        String corrected = location
                // Région Tanger-Tétouan-Al Hoceima
                .replace("Fès-Meknès", "Tanger-Tétouan-Al Hoceima")
                .replace("Tanger-Tetouan", "Tanger-Tétouan-Al Hoceima")
                .replace("Tétouan", "Tanger-Tétouan-Al Hoceima")

                // Région Fès-Meknès (correcte)
                .replace("Fes-Meknes", "Fès-Meknès")

                // Région Casablanca-Settat
                .replace("Grand Casablanca", "Casablanca-Settat")

                // Région Rabat-Salé-Kénitra
                .replace("Rabat-Zemmour-Zaer", "Rabat-Salé-Kénitra")
                .replace("Rabat-Salé-Zemmour-Zaër", "Rabat-Salé-Kénitra")

                // Région Marrakech-Safi
                .replace("Marrakech-Tensift-El Haouz", "Marrakech-Safi")

                // Région Béni Mellal-Khénifra
                .replace("Tadla-Azilal", "Béni Mellal-Khénifra")

                // Région Draa-Tafilalet
                .replace("Souss-Massa-Draa", "Draa-Tafilalet")
                .replace("Guelmim-Es Semara", "Guelmim-Oued Noun");

        if (!corrected.equals(location)) {
            System.out.println("🔄 Correction région appliquée: " + location + " -> " + corrected);
        }

        return corrected;
    }

    private String getPublicIPAddress() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String[] ipServices = {
                    "https://api.ipify.org",
                    "https://checkip.amazonaws.com",
                    "https://icanhazip.com",
                    "https://ident.me"
            };

            for (String service : ipServices) {
                try {
                    String ip = restTemplate.getForObject(service, String.class);
                    if (ip != null && !ip.trim().isEmpty()) {
                        System.out.println("✅ IP publique récupérée depuis " + service + ": " + ip.trim());
                        return ip.trim();
                    }
                } catch (Exception e) {
                    System.err.println("❌ Échec service IP " + service + ": " + e.getMessage());
                }
            }

            return "127.0.0.1";

        } catch (Exception e) {
            System.err.println("💥 Erreur récupération IP publique: " + e.getMessage());
            return "127.0.0.1";
        }
    }
}