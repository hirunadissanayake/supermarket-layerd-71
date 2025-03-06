package lk.ijse.gdse71.supermarketfx.bo.custom;

import lk.ijse.gdse71.supermarketfx.bo.SuperBO;
import lk.ijse.gdse71.supermarketfx.dto.CustomerDto;
import lk.ijse.gdse71.supermarketfx.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CustomerBO extends SuperBO {
    ArrayList<CustomerDto> getAllCustomers() throws SQLException;
    boolean saveCustomer(CustomerDto customerDto) throws SQLException;
    String getNextCustomerId() throws SQLException;
    boolean deleteCustomer(String customerId) throws SQLException;
    boolean updateCustomer(CustomerDto customerDto) throws SQLException;

    List<String> getAllCustomerIds() throws SQLException;
    Optional<Customer> findById(String selectedCustId) throws SQLException;
    String generateCustomerId() throws SQLException;

}
