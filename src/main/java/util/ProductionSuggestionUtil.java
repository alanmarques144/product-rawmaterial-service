package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Product;
import models.ProductRawMaterial;
import models.dto.ProductSuggestionDTO;
import models.dto.SuggestionResultDTO;

public class ProductionSuggestionUtil {
    
    public static SuggestionResultDTO calculateOptimalProduction(List<Product> availableProducts, Map<Long, Integer> currentStock) {
        
        availableProducts.sort((p1, p2) -> p2.getPriceValue().compareTo(p1.getPriceValue()));

        List<ProductSuggestionDTO> suggestions = new ArrayList<>();
        BigDecimal totalExpectedValue = BigDecimal.ZERO;

        for (Product product : availableProducts) {
            int maxProduceable = Integer.MAX_VALUE;

            for (ProductRawMaterial requirement : product.getRequiredMaterials()) {
                Long materialId = requirement.getRawMaterial().getId();
                int available = currentStock.getOrDefault(materialId, 0);
                int needed = requirement.getQuantityNeeded();

                if (needed > 0) {
                    int possibleWithThisMaterial = available / needed;
                    maxProduceable = Math.min(maxProduceable, possibleWithThisMaterial);
                }
            }

            if (maxProduceable > 0 && maxProduceable != Integer.MAX_VALUE) {
                suggestions.add(new ProductSuggestionDTO(product.getName(), maxProduceable));
                totalExpectedValue = totalExpectedValue.add(
                        product.getPriceValue().multiply(BigDecimal.valueOf(maxProduceable))
                );

                for (ProductRawMaterial requirement : product.getRequiredMaterials()) {
                    Long materialId = requirement.getRawMaterial().getId();
                    int used = requirement.getQuantityNeeded() * maxProduceable;
                    currentStock.put(materialId, currentStock.get(materialId) - used);
                }
            }
        }
    return new SuggestionResultDTO(suggestions, totalExpectedValue);
    }
}
