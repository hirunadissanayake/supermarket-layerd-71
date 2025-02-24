package lk.ijse.gdse71.supermarketfx.dao.custom.impl;

import lk.ijse.gdse71.supermarketfx.dao.custom.OrdersDAO;
import lk.ijse.gdse71.supermarketfx.db.DBConnection;
import lk.ijse.gdse71.supermarketfx.dto.OrderDto;
import lk.ijse.gdse71.supermarketfx.dao.SQLUtil;
import lk.ijse.gdse71.supermarketfx.entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrdersDAO {

      OrderDetailsDAOImpl orderDetailsDAOImpl = new OrderDetailsDAOImpl();

    @Override
    public ArrayList<Order> getAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(Order dto) throws SQLException {
        return false;
    }

    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select order_id from Orders order by order_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("0%03d", newIndex);
        }
        return "O001";
    }

    @Override
    public boolean delete(String Id) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Order dto) throws SQLException {
        return false;
    }


}
