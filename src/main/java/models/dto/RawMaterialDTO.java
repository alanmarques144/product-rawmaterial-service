package models.dto;

import java.util.UUID;

public record RawMaterialDTO(
    UUID code, 
    String name, 
    Integer stockQuantity
) {
    
}
