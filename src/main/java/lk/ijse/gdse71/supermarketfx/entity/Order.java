package lk.ijse.gdse71.supermarketfx.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Orders")
@Entity
public class Order {
    @Id
    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private Date orderDate;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetailsList;
}
