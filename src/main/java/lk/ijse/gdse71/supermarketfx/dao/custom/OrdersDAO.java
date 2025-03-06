package lk.ijse.gdse71.supermarketfx.dao.custom;

import lk.ijse.gdse71.supermarketfx.dao.CrudDAO;
import lk.ijse.gdse71.supermarketfx.entity.Order;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.Optional;

public interface OrdersDAO extends CrudDAO<Order> {
    Optional<Order> findById(String orderId);

    boolean saveOrderWithOrderDetails(Session session, Order order);
//     String getNextOrderId() throws SQLException;

}
