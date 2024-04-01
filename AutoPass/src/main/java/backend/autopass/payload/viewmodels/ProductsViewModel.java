package backend.autopass.payload.viewmodels;

import backend.autopass.model.entities.Membership;
import backend.autopass.model.entities.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @author : Lam Nguyen
 * @version : 01
 * Products View Model
 * View model of all products description for the page of products.
 * 30/03/24
 * AutoPass
 */
@Builder
@Data
@Schema(description = "Products page view model." )
public class ProductsViewModel implements Serializable {
    @Schema(description = "List of the tickets")
    List<Ticket> ticketsList;

    @Schema(description = "List of the memberships")
    List<Membership> membershipList;
}
