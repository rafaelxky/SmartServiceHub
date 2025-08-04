package org.example.services.persistance;


import org.example.models.AppService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppServiceDbService {

    private final AppServiceRepository serviceRepository;

    public AppServiceDbService(AppServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public AppService saveService(AppService service) {
        return serviceRepository.save(service);
    }

    public Optional<AppService> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<AppService> getAllServices() {
        return serviceRepository.findAll();
    }

    public void deleteServiceById(Long id) {
        serviceRepository.deleteById(id);
    }
}
