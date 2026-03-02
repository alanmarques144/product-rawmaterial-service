package util;

import java.math.BigDecimal;
import java.util.Arrays;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import models.Product;
import models.ProductRawMaterial;
import models.RawMaterial;
import repository.ProductRepository;
import repository.RawMaterialRepository;

@ApplicationScoped
public class DataBaseSeeder {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public DataBaseSeeder(ProductRepository productRepository, RawMaterialRepository rawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (productRepository.count() > 0) {
            return;
        }


        RawMaterial hdpeResin = new RawMaterial();
        hdpeResin.setName("HDPE Resin Pellets");
        hdpeResin.setStockQuantity(100000); 

        RawMaterial petResin = new RawMaterial();
        petResin.setName("PET Resin Granules"); 
        petResin.setStockQuantity(50000); 

        RawMaterial blueMasterbatch = new RawMaterial();
        blueMasterbatch.setName("Blue Color Masterbatch"); 
        blueMasterbatch.setStockQuantity(5000); 

        RawMaterial uvStabilizer = new RawMaterial();
        uvStabilizer.setName("Anti-UV Stabilizer Additive"); 
        uvStabilizer.setStockQuantity(2000); 

        rawMaterialRepository.persist(Arrays.asList(hdpeResin, petResin, blueMasterbatch, uvStabilizer));


        Product gardenChair = new Product();
        gardenChair.setName("Premium Plastic Garden Chair");
        gardenChair.setPriceValue(new BigDecimal("45.00"));

        ProductRawMaterial chairReq1 = new ProductRawMaterial();
        chairReq1.setProduct(gardenChair);
        chairReq1.setRawMaterial(hdpeResin);
        chairReq1.setQuantityNeeded(2000); 

        ProductRawMaterial chairReq2 = new ProductRawMaterial();
        chairReq2.setProduct(gardenChair);
        chairReq2.setRawMaterial(blueMasterbatch);
        chairReq2.setQuantityNeeded(50); 

        ProductRawMaterial chairReq3 = new ProductRawMaterial();
        chairReq3.setProduct(gardenChair);
        chairReq3.setRawMaterial(uvStabilizer);
        chairReq3.setQuantityNeeded(20); 

        gardenChair.setRequiredMaterials(Arrays.asList(chairReq1, chairReq2, chairReq3));

        Product heavyBucket = new Product();
        heavyBucket.setName("Heavy-Duty Blue Bucket 10L");
        heavyBucket.setPriceValue(new BigDecimal("15.00"));

        ProductRawMaterial bucketReq1 = new ProductRawMaterial();
        bucketReq1.setProduct(heavyBucket);
        bucketReq1.setRawMaterial(hdpeResin);
        bucketReq1.setQuantityNeeded(800); 

        ProductRawMaterial bucketReq2 = new ProductRawMaterial();
        bucketReq2.setProduct(heavyBucket);
        bucketReq2.setRawMaterial(blueMasterbatch);
        bucketReq2.setQuantityNeeded(30); 

        heavyBucket.setRequiredMaterials(Arrays.asList(bucketReq1, bucketReq2));

        Product petBottle = new Product();
        petBottle.setName("Standard PET Water Bottle 500ml");
        petBottle.setPriceValue(new BigDecimal("1.50"));

        ProductRawMaterial bottleReq1 = new ProductRawMaterial();
        bottleReq1.setProduct(petBottle);
        bottleReq1.setRawMaterial(petResin);
        bottleReq1.setQuantityNeeded(25); 

        ProductRawMaterial bottleReq2 = new ProductRawMaterial();
        bottleReq2.setProduct(petBottle);
        bottleReq2.setRawMaterial(blueMasterbatch);
        bottleReq2.setQuantityNeeded(2); 

        petBottle.setRequiredMaterials(Arrays.asList(bottleReq1, bottleReq2));

        productRepository.persist(Arrays.asList(gardenChair, heavyBucket, petBottle));
        
        System.out.println("Database successfully seeded with Plastics Manufacturing data!");
    }
}
