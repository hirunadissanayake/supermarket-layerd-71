package lk.ijse.gdse71.supermarketfx.dao.custom.impl;

import lk.ijse.gdse71.supermarketfx.config.FactoryConfiguration;
import lk.ijse.gdse71.supermarketfx.dao.SQLUtil;
import lk.ijse.gdse71.supermarketfx.dao.custom.ItemDAO;
import lk.ijse.gdse71.supermarketfx.dto.OrderDetailsDto;
import lk.ijse.gdse71.supermarketfx.entity.Item;
import lk.ijse.gdse71.supermarketfx.exception.DuplicateException;
import lk.ijse.gdse71.supermarketfx.exception.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    public List<String> getAllItemIds() throws SQLException {
        Session session = factoryConfiguration.getSession();
        Query<String> query = session.createQuery("select i.itemId from Item i", String.class);
        List<String> list = query.list();
        return list;
        /*ResultSet rst = SQLUtil.execute("select item_id from Item");

        ArrayList<String> itemIds = new ArrayList<>();

        while (rst.next()){
            itemIds.add(rst.getString(1));
        }
        return itemIds;*/
    }

    public Optional<Item> findById(String selecteItemId) throws SQLException {
        if(selecteItemId == null || selecteItemId.isEmpty()) {
            System.err.println("Selected Item ID is null or empty");
            return Optional.empty();
        }
        try (Session session = factoryConfiguration.getSession()){
            Item item = session.get(Item.class, selecteItemId);
            return Optional.ofNullable(item);
        }catch (Exception e){
            return Optional.empty();
        }

        /*ResultSet rst = SQLUtil.execute("select * from Item where item_id = ?",selecteItemId);

        if (rst.next()){
            return new ItemDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4)
            );
        }
        return null;*/
    }

    public boolean reduceQty(OrderDetailsDto orderDetailsDto) throws SQLException {
        return SQLUtil.execute(
                "update Item set quantity = quantity - ? where item_id = ?",
                orderDetailsDto.getQtyOnHand(),
                orderDetailsDto.getItemId()
        );
    }

    @Override
    public boolean updateItemWithOrder(Session session, Item item) {
        try {
            session.merge(item);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<Item> getAll() throws SQLException {
        Session session = factoryConfiguration.getSession();
        Query<Item> fromItem = session.createQuery("from Item ", Item.class);
        List<Item> list = fromItem.list();
        return list;

        /*ResultSet rst = SQLUtil.execute("select * from Item");

        ArrayList<Item> itemDtos = new ArrayList<>();
        while (rst.next()){
            Item itemEntity = new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4)
            );
            itemDtos.add(itemEntity);
        }
        return itemDtos;*/
    }


   /* public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select item_id from Item order by item_id desc limit 1");
        if (rst.next()){
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("I%03d", newIndex);
        }
        return "I001";
    }*/

    public String getLastId() throws SQLException {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery("SELECT i.itemId FROM Item i ORDER BY i.itemId DESC", String.class);
            query.getMaxResults();
            return query.uniqueResult();
        }catch (Exception e){
            throw new SQLException("Error` while executing query",e);
        }
    }

    public boolean save(Item itemEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if(itemEntity == null || itemEntity.getItemId() == null){
                throw new IllegalArgumentException("Invalid item entity or ID is null");
            }
            Item item = session.get(Item.class, itemEntity.getItemId());
            if(item == null){
                throw new DuplicateException("Item ID duplicated");
            }
            session.persist(item);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            if (session != null) {
                session.close();
            }
        }
        /*return SQLUtil.execute("insert into Item values (?,?,?,?)",
                itemEntity.getItemId(),
                itemEntity.getItemName(),
                itemEntity.getQuantity(),
                itemEntity.getUnitPrice()

        );*/
    }

    public boolean delete(String itemId) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Item item = session.get(Item.class, itemId);
            if (item == null){
                throw new NotFoundException("No Customer Found To Delete");
            }
            session.remove(item);
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
       // return SQLUtil.execute("delete from Item where item_id = ?",itemId);
    }

    public boolean update(Item itemEntity) throws SQLException {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            if(itemEntity == null || itemEntity.getItemId() == null){
                throw new IllegalArgumentException("Invalid item entity or ID is null");
            }
            Item item = session.get(Item.class, itemEntity.getItemId());
            if(item == null){
                throw new DuplicateException("Item ID duplicated");
            }
            session.merge(item);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            if (session != null) {
                session.close();
            }
        }
       /* return  SQLUtil.execute("update Item set name =?, quantity = ?, price = ? where item_id = ?",
                itemEntity.getItemName(),
                itemEntity.getQuantity(),
                itemEntity.getUnitPrice(),
                itemEntity.getItemId()
                );*/

    }
}
