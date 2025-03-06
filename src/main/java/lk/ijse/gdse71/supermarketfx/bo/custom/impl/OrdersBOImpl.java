package lk.ijse.gdse71.supermarketfx.bo.custom.impl;

import lk.ijse.gdse71.supermarketfx.bo.custom.OrdersBO;
import lk.ijse.gdse71.supermarketfx.config.FactoryConfiguration;
import lk.ijse.gdse71.supermarketfx.dao.DAOFactory;
import lk.ijse.gdse71.supermarketfx.dao.SQLUtil;
import lk.ijse.gdse71.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.ItemDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.OrdersDAO;
import lk.ijse.gdse71.supermarketfx.db.DBConnection;
import lk.ijse.gdse71.supermarketfx.dto.OrderDetailsDto;
import lk.ijse.gdse71.supermarketfx.dto.OrderDto;
import lk.ijse.gdse71.supermarketfx.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersBOImpl implements OrdersBO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    CustomerDAO customerDAO =(CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    OrdersDAO ordersDAO = (OrdersDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERS);
    OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public String getNextOrderId() throws SQLException {
        return generateNewOrderId();
    }

    public boolean saveOrder(OrderDto orderDto) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            String orderId = orderDto.getOrderId();
            Optional<Order> byId = ordersDAO.findById(orderId);
            if (byId.isPresent()) {
                transaction.rollback();
                return false;
            }
            String customerId = orderDto.getCustomerId();
            Optional<Customer> byCustomerId = customerDAO.findById(customerId);
            if(customerId.isEmpty()){
                transaction.rollback();
                return false;
            }
            Customer customer = byCustomerId.get();
            Order order = new Order();
            order.setOrderId(orderId);
            order.setOrderDate(orderDto.getOrderDate());
            order.setCustomer(customer);

            List<OrderDetails> orderDetails = new ArrayList<>();
            ArrayList<OrderDetailsDto> orderDetailsDtos = orderDto.getOrderDetailsDtos();
            for (OrderDetailsDto orderDetailsDto : orderDetailsDtos) {
                String itemId = orderDetailsDto.getItemId();
                Optional<Item> byId1 = itemDAO.findById(itemId);

                if (byId1.isEmpty()) {
                    transaction.rollback();
                    return false;
                }
                Item item = byId1.get();
                OrderDetailsId orderDetailsId = new OrderDetailsId(orderId, itemId);

                OrderDetails orderDetails1 = new OrderDetails();
                orderDetails1.setId(orderDetailsId);
                orderDetails1.setOrder(order);
                orderDetails1.setItem(item);
                orderDetails1.setQtyOnHand(orderDetailsDto.getQtyOnHand());
                orderDetails1.setPrice(orderDetailsDto.getPrice());
                orderDetails.add(orderDetails1);
            }
            order.setOrderDetailsList(orderDetails);
            boolean isOrderSaved = ordersDAO.saveOrderWithOrderDetails(session,order);
            if (!isOrderSaved) {
                transaction.rollback();
                return false;
            }
            for (OrderDetails orderDetails1 : orderDetails) {
                String itemId = orderDetails1.getItem().getItemId();
                Optional<Item> byId1 = itemDAO.findById(itemId);
                if (byId1.isEmpty()) {
                    transaction.rollback();
                    return false;
                }
                Item item = byId1.get();
                if (item.getQuantity() < orderDetails1.getQtyOnHand()) {
                    transaction.rollback();
                    return false;
                }
                item.setQuantity(item.getQuantity() - orderDetails1.getQtyOnHand());
                boolean isItemUpdated = itemDAO.updateItemWithOrder(session,item);
                if (!isItemUpdated) {
                    transaction.rollback();
                    return false;
                }
            }
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            if(session != null){
                session.close();
            }
        }

       /* Connection connection = DBConnection.getInstance().getConnection();
        try{
            connection.setAutoCommit(false);
            boolean isOrderSaved = SQLUtil.execute("insert into Orders values (?,?,?)",
                    orderDto.getOrderId(),
                    orderDto.getCustomerId(),
                    orderDto.getOrderDate()
            );
            if(isOrderSaved){
                boolean isOrderDetailsSaved = saveOrderDetailsList(orderDto.getOrderDetailsDtos());
                if(isOrderDetailsSaved){
                    connection.commit();
                    return true;
                }

            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }*/
    }
    public boolean saveOrderDetailsList(ArrayList<OrderDetailsDto> orderDetailsDtos) throws SQLException {
        for (OrderDetailsDto orderDetailsDto : orderDetailsDtos) {
            boolean isOrderDetailsSaved = saveOrderDetail(orderDetailsDto);

            if (!isOrderDetailsSaved) {
                return false;
            }
            boolean isItemUpdated = itemDAO.reduceQty(orderDetailsDto);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveOrderDetail(OrderDetailsDto orderDetailsDto) throws SQLException {
        return orderDetailsDAO.save(new OrderDetails());
    }

    @Override
    public String generateNewOrderId() throws SQLException {
        String lastId = ordersDAO.getLastId();
        if (lastId == null || lastId.isEmpty()) {
            return "O001";
        }
        try {
            String substring = lastId.substring(0, 1);
            String substring1 = lastId.substring(1);

            int nextNumber = Integer.parseInt(substring1)+ 1;
            return String.format("%s%03d", substring, nextNumber);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            return "O001";
        }
    }
}
