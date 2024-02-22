package backend.autopass.model.repositories;

import backend.autopass.model.entities.Sale;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISaleRepository {
    Optional<Sale> findSaleById(long id);

}
