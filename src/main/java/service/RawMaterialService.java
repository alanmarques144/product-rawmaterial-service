package service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import models.RawMaterial;
import models.dto.RawMaterialDTO;
import repository.RawMaterialRepository;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;

    public List<RawMaterialDTO> listAll() {
        return repository.listAll().stream()
                .map(rm -> new RawMaterialDTO(rm.getCode(), rm.getName(), rm.getStockQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        RawMaterial rm = new RawMaterial();
        rm.setName(dto.name());
        rm.setStockQuantity(dto.stockQuantity());
        repository.persist(rm);
        return new RawMaterialDTO(rm.getCode(), rm.getName(), rm.getStockQuantity());
    }

    @Transactional
    public RawMaterialDTO update(UUID code, RawMaterialDTO dto) {
        RawMaterial rm = repository.find("code", code).firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Raw Material not found"));
        
        rm.setName(dto.name());
        rm.setStockQuantity(dto.stockQuantity());
        return new RawMaterialDTO(rm.getCode(), rm.getName(), rm.getStockQuantity());
    }

    @Transactional
    public void delete(UUID code) {
        RawMaterial rm = repository.find("code", code).firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Raw Material not found"));
        repository.delete(rm);
    }
}
