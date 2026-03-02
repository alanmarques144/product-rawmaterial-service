package controller;

import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.dto.ProductDTO;
import service.ProductService;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @GET
    public Response getAll() {
        return Response.ok(service.listAll()).build();
    }

    @GET
    public Response getSuggestions() {
        return Response.ok(service.calculateProductionSuggestion()).build();
    }

    @POST
    public Response create(ProductDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.create(dto)).build();
    }

    @PUT
    @Path("/{code}")
    public Response update(@PathParam("code") UUID code, ProductDTO dto) {
        return Response.ok(service.update(code, dto)).build();
    }

    @DELETE
    @Path("/{code}")
    public Response delete(@PathParam("code") UUID code) {
        service.delete(code);
        return Response.noContent().build();
    }
}