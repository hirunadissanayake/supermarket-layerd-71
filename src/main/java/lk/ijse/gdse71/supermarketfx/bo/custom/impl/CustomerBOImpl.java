package lk.ijse.gdse71.supermarketfx.bo.custom.impl;

import lk.ijse.gdse71.supermarketfx.bo.custom.CustomerBO;
import lk.ijse.gdse71.supermarketfx.bo.custom.ItemBO;
import lk.ijse.gdse71.supermarketfx.dao.DAOFactory;
import lk.ijse.gdse71.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.gdse71.supermarketfx.dto.CustomerDto;
import lk.ijse.gdse71.supermarketfx.entity.Customer;


import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDto> getAllCustomers() throws SQLException {
        ArrayList<Customer> customer = customerDAO.getAll();
        ArrayList<CustomerDto> customerDto = new ArrayList<>();

        for (Customer c : customer) {
            CustomerDto customerDto1 = new CustomerDto(
                    c.getCustomerId(),
                    c.getCustomerName(),
                    c.getNic(),
                    c.getEmail(),
                    c.getPhone()
            );
            customerDto.add(customerDto1);
        }
        return customerDto;
    }


    @Override
    public boolean saveCustomer(CustomerDto customerDto) throws SQLException {
        return customerDAO.save(new Customer(customerDto.getCustomerId(),customerDto.getCustomerName(),customerDto.getNic(),customerDto.getEmail(),customerDto.getPhone()));
    }

    @Override
    public String getNextCustomerId() throws SQLException {
        return customerDAO.getNextId();
    }

    @Override
    public boolean deleteCustomer(String customerId) throws SQLException {
        return customerDAO.delete(customerId);
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) throws SQLException {
        return customerDAO.update(new Customer(customerDto.getCustomerId(),customerDto.getCustomerName(),customerDto.getNic(),customerDto.getEmail(),customerDto.getPhone()));
    }

    @Override
    public ArrayList<String> getAllCustomerIds() throws SQLException {
        return customerDAO.getAllCustomerIds();
    }

    @Override
    public CustomerDto findById(String selectedCustId) throws SQLException {
        return customerDAO.findById(selectedCustId);
    }
}
