package lk.ijse.gdse71.supermarketfx.dao;

import lk.ijse.gdse71.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {

    }
    public static DAOFactory getDaoFactory() {
        return (daoFactory==null)?daoFactory
                =new DAOFactory():daoFactory;
    }
    public enum DAOTypes{
        CUSTOMER,ORDERS,ITEM,ORDER_DETAILS,QUERY
    }
    public SuperDAO getDAO(DAOTypes daoType){
        switch (daoType){
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDERS:
                return new OrderDAOImpl();
            case ORDER_DETAILS:
                return new OrderDetailsDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            default:
                return null;
        }
    }
}
