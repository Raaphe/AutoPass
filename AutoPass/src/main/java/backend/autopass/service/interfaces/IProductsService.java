package backend.autopass.service.interfaces;

import backend.autopass.payload.viewmodels.ProductsViewModel;
/**
 * @author : Lam Nguyen
 * @version : 01
 * Products Service interface
 * products service interface to be implemented as a class.
 * 30/03/24
 * AutoPass
 */
public interface IProductsService {
    /**
     * Gets all the products.
     * @return ProductsViewModel The view model for products of the application.
     */
    ProductsViewModel getAllProducts();
}
