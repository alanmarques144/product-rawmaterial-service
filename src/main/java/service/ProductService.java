package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import models.Product;
import models.ProductRawMaterial;
import models.RawMaterial;
import models.dto.ProductDTO;
import models.dto.ProductRawMaterialDTO;
import models.dto.SuggestionResultDTO;
import repository.ProductRepository;
import repository.RawMaterialRepository;
import util.ProductionSuggestionUtil;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    public List<ProductDTO> listAll() {
        return productRepository.listAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setPriceValue(dto.priceValue());
        
        List<ProductRawMaterial> materials = mapRequiredMaterials(dto.requiredMaterials(), product);
        product.setRequiredMaterials(materials);
        
        productRepository.persist(product);
        return toDTO(product);
    }

    @Transactional
    public ProductDTO update(UUID code, ProductDTO dto) {
        Product product = productRepository.find("code", code).firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setName(dto.name());
        product.setPriceValue(dto.priceValue());
        
        product.getRequiredMaterials().clear();
        product.getRequiredMaterials().addAll(mapRequiredMaterials(dto.requiredMaterials(), product));

        return toDTO(product);
    }

    @Transactional
    public void delete(UUID code) {
        Product product = productRepository.find("code", code).firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
    }

    private List<ProductRawMaterial> mapRequiredMaterials(List<ProductRawMaterialDTO> dtos, Product product) {
        List<ProductRawMaterial> materials = new ArrayList<>();
        if (dtos != null) {
            for (ProductRawMaterialDTO reqDto : dtos) {
                RawMaterial rm = rawMaterialRepository.find("code", reqDto.rawMaterialCode()).firstResultOptional()
                        .orElseThrow(() -> new NotFoundException("Raw material not found: " + reqDto.rawMaterialCode()));
                
                ProductRawMaterial prm = new ProductRawMaterial();
                prm.setProduct(product);
                prm.setRawMaterial(rm);
                prm.setQuantityNeeded(reqDto.quantityNeeded());
                materials.add(prm);
            }
        }
        return materials;
    }

    public SuggestionResultDTO calculateProductionSuggestion() {
        // Busca todos os produtos e seus relacionamentos
        List<Product> availableProducts = productRepository.listAll();
        
        // Busca todo o estoque e converte para um Mapa (ID -> Quantidade) para o algoritmo
        Map<Long, Integer> currentStock = rawMaterialRepository.listAll().stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));

        // Chama o algoritmo guloso que construímos anteriormente
        return ProductionSuggestionUtil.calculateOptimalProduction(availableProducts, currentStock);
    }

    private ProductDTO toDTO(Product product) {
        List<ProductRawMaterialDTO> materials = product.getRequiredMaterials().stream()
                .map(m -> new ProductRawMaterialDTO(m.getRawMaterial().getCode(), m.getQuantityNeeded()))
                .collect(Collectors.toList());
        return new ProductDTO(product.getCode(), product.getName(), product.getPriceValue(), materials);
    }
}