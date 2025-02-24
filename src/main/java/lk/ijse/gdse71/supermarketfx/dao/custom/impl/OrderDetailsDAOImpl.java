package lk.ijse.gdse71.supermarketfx.dao.custom.impl;

import lk.ijse.gdse71.supermarketfx.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse71.supermarketfx.dto.OrderDetailsDto;
import lk.ijse.gdse71.supermarketfx.dao.SQLUtil;
import lk.ijse.gdse71.supermarketfx.entity.OrderDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public ArrayList<OrderDetails> getAll() throws SQLException {
        return null;
    }

    public boolean save(OrderDetails orderDetailsEntity) throws SQLException {
         return SQLUtil.execute(
                 "insert into OrderDetails values (?,?,?,?)",
                 orderDetailsEntity.getOrderId(),
                 orderDetailsEntity.getItemId(),
                 orderDetailsEntity.getQtyOnHand(),
                 orderDetailsEntity.getPrice()
         );
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public boolean delete(String Id) throws SQLException {
        return false;
    }

    @Override
    public boolean update(OrderDetails dto) throws SQLException {
        return false;
    }
}
