package lk.ijse.gdse71.supermarketfx.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{
    List<T> getAll() throws SQLException;
    boolean save(T dto) throws SQLException;
    String getLastId() throws SQLException;
    boolean delete(String Id) throws SQLException;
    boolean update(T dto) throws SQLException;
}
