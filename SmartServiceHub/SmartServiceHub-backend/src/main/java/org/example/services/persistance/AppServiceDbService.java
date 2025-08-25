package org.example.services.persistance;


import org.example.models.ServicePost;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppServiceDbService {

    private final AppServiceRepository serviceRepository;

    public AppServiceDbService(AppServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServicePost saveService(ServicePost service) {
        return serviceRepository.save(service);
    }

    public Optional<ServicePost> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<ServicePost> getAllServices() {
        return serviceRepository.findAll();
    }

    public void deleteServiceById(Long id) {
        serviceRepository.deleteById(id);
    }

    public List<ServicePost> getServiceWithLimit(int limit){
        return serviceRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    public List<ServicePost> getServicePostUnique(int limit, int offset){
        return serviceRepository.findUniqueServicePost(limit, offset);
    }
}
