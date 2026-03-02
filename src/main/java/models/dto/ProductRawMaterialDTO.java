package models.dto;

import java.util.UUID;

public record ProductRawMaterialDTO(
    UUID rawMaterialCode, 
    Integer quantityNeeded
) {
    
}
