package database.mysql;

import java.util.List;

public interface GenericDAO<T> {
    public List<T> getAll();
    public T getOneById(int id);
    // public T getOneByName(String name); // Added for QuestionDAO
    public void storeOne(T type);
}
