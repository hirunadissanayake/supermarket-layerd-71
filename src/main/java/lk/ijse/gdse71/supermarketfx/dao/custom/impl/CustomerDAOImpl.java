package lk.ijse.gdse71.supermarketfx.dao.custom.impl;

import lk.ijse.gdse71.supermarketfx.config.FactoryConfiguration;
import lk.ijse.gdse71.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.gdse71.supermarketfx.entity.Customer;
import lk.ijse.gdse71.supermarketfx.exception.DuplicateException;
import lk.ijse.gdse71.supermarketfx.exception.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    public List<Customer> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        Query<Customer> fromCustomer = session.createQuery("from Customer", Customer.class);
        List<Customer> list = fromCustomer.list();
        return list;


//        ResultSet rst = SQLUtil.execute("select * from Customer");
//        ArrayList<Customer> customerEntity = new ArrayList<>();
//
//        while (rst.next()) {
//            Customer customerDto = new Customer(
//                    rst.getString(1),
//                    rst.getString(2),
//                    rst.getString(3),
//                    rst.getString(4),
//                    rst.getString(5)
//            );
//            customerEntity.add(customerDto);
//        }
//        return customerEntity;

    }

    public boolean save(Customer customerEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (customerEntity == null || customerEntity.getCustomerId() == null){
                throw new IllegalArgumentException("Invalid customer entity or ID is null");
            }
            Customer customer = session.get(Customer.class, customerEntity.getCustomerId());
            if (customer != null){
                throw new DuplicateException("Customer ID duplicated");
            }
            session.persist(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if(session != null){
                session.close();
            }
        }
        /*return SQLUtil.execute("insert into Customer values(?,?,?,?,?)",
                customerEntity.getCustomerId(),
                customerEntity.getCustomerName(),
                customerEntity.getNic(),
                customerEntity.getEmail(),
                customerEntity.getPhone()
        );*/
    }
   /* public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from Customer order by customer_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("C%03d", newIndex);
        }
        return "C001";
    }*/
    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery("SELECT c.customerId FROM Customer c ORDER BY c.customerId DESC", String.class);
            query.setMaxResults(1);
            return query.uniqueResult();
        }catch (Exception e){
            throw new SQLException("Error` while executing query",e);
        }
    }
    public List<String> getAllCustomerIds() throws SQLException{
        Session session = factoryConfiguration.getSession();
        Query<String> query = session.createQuery("select c.customerId from Customer c", String.class);
        List<String> list = query.list();
        return list;
       /* ResultSet rst = SQLUtil.execute("select customer_id from Customer");
        ArrayList<String> customerIds = new ArrayList<>();

        while (rst.next()) {
            customerIds.add(rst.getString(1));
        }
        return customerIds;*/
    }
    public Optional<Customer> findById(String selectedCustId) throws SQLException {
        if (selectedCustId == null || selectedCustId.isEmpty()){
            System.err.println("Selected Customer ID is null or empty");
            return Optional.empty();
        }
        try (Session session = factoryConfiguration.getSession()) {
            Customer customer = session.get(Customer.class, selectedCustId);
            return Optional.ofNullable(customer);
        }catch (Exception e){
            return Optional.empty();
        }
        /*ResultSet rst = SQLUtil.execute("select * from Customer where customer_id=?", selectedCustId);

        if (rst.next()) {
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5));
        }
        return null;*/
    }

    public boolean delete(String customerId) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Customer customer = session.get(Customer.class, customerId);
            if (customer == null){
                throw new NotFoundException("No Customer Found To Delete");
            }
            session.remove(customer);
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
//        return SQLUtil.execute("delete from Customer where customer_id = ?",customerId);
    }

    public boolean update(Customer customerEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (customerEntity == null || customerEntity.getCustomerId() == null){
                throw new IllegalArgumentException("Invalid customer entity or ID is null");
            }
            Customer customer = session.get(Customer.class, customerEntity.getCustomerId());
            if (customer != null){
                throw new DuplicateException("Customer ID duplicated");
            }
            session.merge(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if(session != null){
                session.close();
            }
        }
      /*  return  SQLUtil.execute("update Customer set name=?, nic =?, email=?, phone=? where customer_id =?",
                customerEntity.getCustomerName(),
                customerEntity.getNic(),
                customerEntity.getEmail(),
                customerEntity.getPhone(),
                customerEntity.getCustomerId()
        );*/
    }
}
