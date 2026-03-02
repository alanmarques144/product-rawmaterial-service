package controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import models.dto.RawMaterialDTO;
import service.RawMaterialService;

@QuarkusTest
class RawMaterialResourceTest {

    @InjectMock
    RawMaterialService rawMaterialService;

    @Test
    void testGetAllRawMaterials() {
        // 1. Prepara o Mock
        UUID mockId = UUID.randomUUID();
        RawMaterialDTO mockDto = new RawMaterialDTO(mockId, "HDPE Resin", 1000);
        Mockito.when(rawMaterialService.listAll()).thenReturn(List.of(mockDto));

        // 2. Executa a requisição HTTP e valida a resposta
        given()
          .when().get("/api/raw-materials")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("$.size()", is(1))
             .body("[0].name", is("HDPE Resin"))
             .body("[0].stockQuantity", is(1000));
    }

    @Test
    void testCreateRawMaterial() {
        UUID mockId = UUID.randomUUID();
        RawMaterialDTO inputDto = new RawMaterialDTO(null, "Blue Masterbatch", 500);
        RawMaterialDTO outputDto = new RawMaterialDTO(mockId, "Blue Masterbatch", 500);

        Mockito.when(rawMaterialService.create(Mockito.any(RawMaterialDTO.class))).thenReturn(outputDto);

        given()
          .contentType(ContentType.JSON)
          .body(inputDto)
          .when().post("/api/raw-materials")
          .then()
             .statusCode(201) // Valida status de "Created"
             .body("name", is("Blue Masterbatch"))
             .body("code", is(mockId.toString()));
    }
}