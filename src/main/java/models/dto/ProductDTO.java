package models.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductDTO(
    UUID code, 
    String name, 
    BigDecimal priceValue, 
    List<ProductRawMaterialDTO> requiredMaterials
) {
    
}
