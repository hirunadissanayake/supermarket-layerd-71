package lk.ijse.gdse71.supermarketfx.bo.custom;

import lk.ijse.gdse71.supermarketfx.bo.SuperBO;
import lk.ijse.gdse71.supermarketfx.dto.CustomerDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    ArrayList<CustomerDto> getAllCustomers() throws SQLException;
    boolean saveCustomer(CustomerDto customerDto) throws SQLException;
    String getNextCustomerId() throws SQLException;
    boolean deleteCustomer(String customerId) throws SQLException;
    boolean updateCustomer(CustomerDto customerDto) throws SQLException;

    ArrayList<String> getAllCustomerIds() throws SQLException;
    CustomerDto findById(String selectedCustId) throws SQLException;

}
