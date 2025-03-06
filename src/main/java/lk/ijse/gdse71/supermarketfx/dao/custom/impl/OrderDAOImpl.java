package lk.ijse.gdse71.supermarketfx.dao.custom.impl;

import lk.ijse.gdse71.supermarketfx.config.FactoryConfiguration;
import lk.ijse.gdse71.supermarketfx.dao.custom.OrdersDAO;
import lk.ijse.gdse71.supermarketfx.entity.Order;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class OrderDAOImpl implements OrdersDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public ArrayList<Order> getAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(Order dto) throws SQLException {
        return false;
    }

    /*public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("SELECT MAX(order_id) FROM Orders");

        if (rst.next()) {
            String lastId = rst.getString(1);
            if (lastId != null && lastId.startsWith("O")) {
                int newIndex = Integer.parseInt(lastId.substring(1)) + 1;
                return String.format("O%03d", newIndex);
            }
        }
        return "O001";  // First order
    }
*/
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()){
            Query<String> query = session.createQuery("select o.orderId FROM Order o ORDER BY o.orderId DESC", String.class);
            query.getMaxResults();
            return query.uniqueResult();
        }catch (Exception e){
            throw new SQLException("Error` while executing query",e);
        }
    }
    @Override
    public boolean delete(String Id) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Order dto) throws SQLException {
        return false;
    }


    @Override
    public Optional<Order> findById(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return Optional.empty();
        }
        try(Session session = factoryConfiguration.getSession()) {
            Order order = session.get( Order.class, orderId);
            return Optional.ofNullable(order);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public boolean saveOrderWithOrderDetails(Session session, Order order) {
        try {
            session.merge(order);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
