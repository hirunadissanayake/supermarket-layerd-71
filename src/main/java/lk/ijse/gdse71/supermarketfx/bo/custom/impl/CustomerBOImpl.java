package lk.ijse.gdse71.supermarketfx.bo.custom.impl;

import lk.ijse.gdse71.supermarketfx.bo.custom.CustomerBO;
import lk.ijse.gdse71.supermarketfx.dao.DAOFactory;
import lk.ijse.gdse71.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.gdse71.supermarketfx.dto.CustomerDto;
import lk.ijse.gdse71.supermarketfx.entity.Customer;
import lk.ijse.gdse71.supermarketfx.exception.DuplicateException;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO =  DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDto> getAllCustomers() throws SQLException {
        ArrayList<Customer> customer = (ArrayList<Customer>) customerDAO.getAll();
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
        try {
            if (customerDto.getCustomerId() == null || customerDto.getCustomerId().isEmpty()) {
                customerDto.setCustomerId(generateCustomerId());
            }
            Customer customer = new Customer(
                    customerDto.getCustomerId(),
                    customerDto.getCustomerName(),
                    customerDto.getNic(),
                    customerDto.getEmail(),
                    customerDto.getPhone()
            );
            customerDAO.save(customer);
            return true;
        }catch (DuplicateException e){
            e.getMessage();
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return false;
//        return customerDAO.save(new Customer(customerDto.getCustomerId(),customerDto.getCustomerName(),customerDto.getNic(),customerDto.getEmail(),customerDto.getPhone()));
    }

    @Override
    public String getNextCustomerId() throws SQLException {
        return generateCustomerId();
    }

    @Override
    public boolean deleteCustomer(String customerId) throws SQLException {
        return customerDAO.delete(customerId);
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) throws SQLException {
        try {
            if (customerDto.getCustomerId() == null || customerDto.getCustomerId().isEmpty()) {
                customerDto.setCustomerId(generateCustomerId());
            }
            Customer customer = new Customer(
                    customerDto.getCustomerId(),
                    customerDto.getCustomerName(),
                    customerDto.getNic(),
                    customerDto.getEmail(),
                    customerDto.getPhone()
            );
            customerDAO.update(customer);
            return true;
        }catch (DuplicateException e){
            e.getMessage();
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return false;
//        return customerDAO.update(new Customer(customerDto.getCustomerId(),customerDto.getCustomerName(),customerDto.getNic(),customerDto.getEmail(),customerDto.getPhone()));
    }

    @Override
    public List<String> getAllCustomerIds() throws SQLException {
        return customerDAO.getAllCustomerIds();
    }

    @Override
    public Optional<Customer> findById(String selectedCustId) throws SQLException {
        return customerDAO.findById(selectedCustId);
    }

    @Override
    public String generateCustomerId() throws SQLException {
        String lastId = customerDAO.getLastId();
        if (lastId == null || lastId.isEmpty()) {
            return "C001";
        }
        try {
            String substring = lastId.substring(0, 1);
            String substring1 = lastId.substring(1);

            int nextNumber = Integer.parseInt(substring1)+ 1;
            return String.format("%s%03d", substring, nextNumber);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            return "C001";
        }
    }


}
