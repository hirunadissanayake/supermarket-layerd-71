package lk.ijse.gdse71.supermarketfx.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Customer")
@Entity
public class Customer {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    private String customerName;
    private String nic;
    private String email;
    private String phone;

//    @OneToMany(mappedBy = "Customer",cascade = CascadeType.ALL)
//    private List<Order> orders;
}
