package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import models.RawMaterial;

@ApplicationScoped
public class RawMaterialRepository implements PanacheRepository<RawMaterial> {
}
