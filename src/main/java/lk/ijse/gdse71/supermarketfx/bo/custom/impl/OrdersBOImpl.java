package lk.ijse.gdse71.supermarketfx.bo.custom.impl;

import lk.ijse.gdse71.supermarketfx.bo.custom.OrdersBO;
import lk.ijse.gdse71.supermarketfx.dao.DAOFactory;
import lk.ijse.gdse71.supermarketfx.dao.SQLUtil;
import lk.ijse.gdse71.supermarketfx.dao.custom.ItemDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.OrdersDAO;
import lk.ijse.gdse71.supermarketfx.db.DBConnection;
import lk.ijse.gdse71.supermarketfx.dto.OrderDetailsDto;
import lk.ijse.gdse71.supermarketfx.dto.OrderDto;
import lk.ijse.gdse71.supermarketfx.entity.OrderDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersBOImpl implements OrdersBO {
    OrdersDAO ordersDAO = (OrdersDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERS);
    OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public String getNextOrderId() throws SQLException {
        return ordersDAO.getNextId();
    }

    public boolean saveOrder(OrderDto orderDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
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
        }
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
}
