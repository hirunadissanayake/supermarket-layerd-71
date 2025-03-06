package lk.ijse.gdse71.supermarketfx.bo.custom.impl;

import lk.ijse.gdse71.supermarketfx.bo.custom.ItemBO;
import lk.ijse.gdse71.supermarketfx.dao.DAOFactory;
import lk.ijse.gdse71.supermarketfx.dao.custom.CustomerDAO;
import lk.ijse.gdse71.supermarketfx.dao.custom.ItemDAO;
import lk.ijse.gdse71.supermarketfx.dto.ItemDto;
import lk.ijse.gdse71.supermarketfx.dto.OrderDetailsDto;
import lk.ijse.gdse71.supermarketfx.entity.Item;
import lk.ijse.gdse71.supermarketfx.exception.DuplicateException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemBOImpl implements ItemBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public List<String> getAllItemIds() throws SQLException {
        return itemDAO.getAllItemIds();
    }


    @Override
    public Optional<Item> findById(String selecteItemId) throws SQLException {
        return itemDAO.findById(selecteItemId);
    }

    @Override
    public boolean reduceQty(OrderDetailsDto orderDetailsDto) throws SQLException {
        return itemDAO.reduceQty(orderDetailsDto);
    }

    @Override
    public ArrayList<ItemDto> getAllItems() throws SQLException {
        ArrayList<Item> items = (ArrayList<Item>) itemDAO.getAll();
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = new ItemDto(
                    item.getItemId(),
                    item.getItemName(),
                    item.getQuantity(),
                    item.getUnitPrice()
            );
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }
    @Override
    public String getNextItemId() throws SQLException {
        return generateNewItemId();
    }

    @Override
    public boolean saveItem(ItemDto itemDto) throws SQLException {
        try {
            if (itemDto.getItemId() == null || itemDto.getItemId().isEmpty()) {
                itemDto.setItemId(generateNewItemId());
            }
            Item item = new Item(
                    itemDto.getItemId(),
                    itemDto.getItemName(),
                    itemDto.getQuantity(),
                    itemDto.getUnitPrice()
            );
            itemDAO.save(item);
            return true;
        }catch (DuplicateException e){
            e.getMessage();
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return false;
//        return itemDAO.save(new Item(itemDto.getItemId(), itemDto.getItemName(), itemDto.getQuantity(), itemDto.getQuantity()));
    }

    @Override
    public boolean deleteItem(String itemId) throws SQLException {
        return itemDAO.delete(itemId);
    }

    @Override
    public boolean updateItem(ItemDto itemDto) throws SQLException {
        try {
            if (itemDto.getItemId() == null || itemDto.getItemId().isEmpty()) {
                itemDto.setItemId(generateNewItemId());
            }
            Item item = new Item(
                    itemDto.getItemId(),
                    itemDto.getItemName(),
                    itemDto.getQuantity(),
                    itemDto.getUnitPrice()
            );
            itemDAO.update(item);
            return true;
        }catch (DuplicateException e){
            e.getMessage();
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return false;
//        return itemDAO.update(new Item(itemDto.getItemId(), itemDto.getItemName(), itemDto.getQuantity(), itemDto.getQuantity()));
    }

    @Override
    public String generateNewItemId() throws SQLException {
        String lastId = itemDAO.getLastId();
        if (lastId == null || lastId.isEmpty()) {
            return "I001";
        }
        try {
            String substring = lastId.substring(0, 1);
            String substring1 = lastId.substring(1);

            int nextNumber = Integer.parseInt(substring1)+ 1;
            return String.format("%s%03d", substring, nextNumber);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            return "I001";
        }
    }
}
