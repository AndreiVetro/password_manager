package repository;

import java.util.List;

public interface Repository<T>
{
    boolean add(T entity);
    boolean remove(T entity);
    boolean update(T entity);
    List<T> getAll();
}
