package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import models.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}
