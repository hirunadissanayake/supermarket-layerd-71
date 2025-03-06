package lk.ijse.gdse71.supermarketfx.config;


import lk.ijse.gdse71.supermarketfx.entity.Customer;
import lk.ijse.gdse71.supermarketfx.entity.Item;
import lk.ijse.gdse71.supermarketfx.entity.Order;
import lk.ijse.gdse71.supermarketfx.entity.OrderDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private SessionFactory sessionFactory;

    private static FactoryConfiguration factoryConfiguration;

    private FactoryConfiguration() {
        org.hibernate.cfg.Configuration config = new Configuration().configure();

        config.addAnnotatedClass(Customer.class);
        config.addAnnotatedClass(Item.class);
        config.addAnnotatedClass(Order.class);
        config.addAnnotatedClass(OrderDetails.class);

        sessionFactory = config.buildSessionFactory();
    }
    public static FactoryConfiguration getInstance() {
        /*if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;*/
        return (factoryConfiguration == null) ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }
    public Session getSession() {
        return sessionFactory.openSession();
    }

}
