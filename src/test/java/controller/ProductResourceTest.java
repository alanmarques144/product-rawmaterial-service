package controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import models.dto.ProductDTO;
import models.dto.ProductSuggestionDTO;
import models.dto.SuggestionResultDTO;
import service.ProductService;

@QuarkusTest
class ProductResourceTest {

    @InjectMock
    ProductService productService;

    @Test
    void testGetAllProducts() {
        UUID mockId = UUID.randomUUID();
        ProductDTO mockDto = new ProductDTO(mockId, "Plastic Chair", new BigDecimal("45.00"), Collections.emptyList());
        Mockito.when(productService.listAll()).thenReturn(List.of(mockDto));

        given()
          .when().get("/api/products")
          .then()
             .statusCode(200)
             .body("$.size()", is(1))
             .body("[0].name", is("Plastic Chair"))
             .body("[0].priceValue", is(45.00f)); 
    }

    @Test
    void testDeleteProduct() {
        UUID mockId = UUID.randomUUID();
        
        Mockito.doNothing().when(productService).delete(mockId);

        given()
          .when().delete("/api/products/" + mockId)
          .then()
             .statusCode(204);
    }

    @Test
    void testGetProductionSuggestions() {

        ProductSuggestionDTO suggestion1 = new ProductSuggestionDTO("Plastic Chair", 50);
        ProductSuggestionDTO suggestion2 = new ProductSuggestionDTO("Heavy Bucket", 25);
        
        SuggestionResultDTO resultDTO = new SuggestionResultDTO(
                List.of(suggestion1, suggestion2), 
                new BigDecimal("2625.00")
        );

        Mockito.when(productService.calculateProductionSuggestion()).thenReturn(resultDTO);


        given()
          .when().get("/api/products/suggestions")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("totalValue", is(2625.00f))
             .body("suggestions.size()", is(2))
             .body("suggestions[0].productName", is("Plastic Chair"))
             .body("suggestions[0].quantityToProduce", is(50));
    }
}