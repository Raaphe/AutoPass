package backend.autopass.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author Raphael Paquin
 * @version 01
 * Represents a log for transit history.
 * 2024-04-12
 * AutoPass
 */
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class TransitLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double date;
    private Boolean authorized;
    private String resourceType;
    private int busNumber;
    private String busName;
    @ManyToOne
    private User user;
}
