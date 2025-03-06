package lk.ijse.gdse71.supermarketfx.entity;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "OrderDetails")
@Entity
public class OrderDetails {

    @EmbeddedId
    private OrderDetailsId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name= "order_id")
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name= "item_id")
    private Item item;

    private int qtyOnHand;

    @Column(name = "price")
    private double price;
}
