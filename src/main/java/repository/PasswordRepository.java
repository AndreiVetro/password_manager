package repository;

import domain.Password;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Repository
public class PasswordRepository implements Repository<Password>
{

    @Autowired
    private Session session;

    @Override
    public boolean add(Password entity)
    {
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        return session.contains(entity);
    }

    @Override
    public boolean remove(Password entity)
    {
        session.beginTransaction();
        session.remove(entity);
        session.getTransaction().commit();

        return !session.contains(entity);
    }

    @Override
    public boolean update(Password entity)
    {
        int id  = entity.getId();
        Password oldPassword = session.get(Password.class, id);

        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();

        return !session.get(Password.class, id).equals(oldPassword);
    }

    @Override
    public List<Password> getAll()
    {
        return session.createQuery("from Password").list();
    }
}
