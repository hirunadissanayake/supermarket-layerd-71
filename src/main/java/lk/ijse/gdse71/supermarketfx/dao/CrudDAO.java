package lk.ijse.gdse71.supermarketfx.dao;


import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{
    ArrayList<T> getAll() throws SQLException;
    boolean save(T dto) throws SQLException;
    String getNextId() throws SQLException;
    boolean delete(String Id) throws SQLException;
    boolean update(T dto) throws SQLException;
}
