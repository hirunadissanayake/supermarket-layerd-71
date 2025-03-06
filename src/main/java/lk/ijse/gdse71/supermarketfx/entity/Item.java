package lk.ijse.gdse71.supermarketfx.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Item")
@Entity
public class Item {
    @Id
    @Column(name = "item_id")
    private String itemId;

    @Column(length = 100)
    private String itemName;
    private int quantity;

    @Column(name = "price")
    private double unitPrice;

//    @OneToMany(mappedBy = "Item")
//    private List<OrderDetails> orderDetails;
}
