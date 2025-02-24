package lk.ijse.gdse71.supermarketfx.bo;

import lk.ijse.gdse71.supermarketfx.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse71.supermarketfx.bo.custom.impl.ItemBOImpl;
import lk.ijse.gdse71.supermarketfx.bo.custom.impl.OrdersBOImpl;
import lk.ijse.gdse71.supermarketfx.dao.custom.QueryDAO;

public class BOFactory {
    public static BOFactory boFactory;
    private BOFactory() {

    }
    public static BOFactory getInstance() {
        return  (boFactory == null)? boFactory =
                new BOFactory():boFactory;
    }
    public enum BOTypes {
        CUSTOMER,ORDER,ITEM
    }
    public SuperBO getBO(BOTypes boTypes) {
        switch (boTypes) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ORDER:
                return new OrdersBOImpl();
            case ITEM:
                return new ItemBOImpl();
            default:
                return null;

        }
    }
}
