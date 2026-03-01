package models.dto;

import java.math.BigDecimal;
import java.util.List;

public record SuggestionResultDTO(
    List<ProductSuggestionDTO> suggestions, 
    BigDecimal totalValue) {
    
}
