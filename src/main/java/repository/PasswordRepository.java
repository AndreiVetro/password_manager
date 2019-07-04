package repository;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import domain.Password;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@org.springframework.stereotype.Repository
public class PasswordRepository implements Repository<Password>
{

    @Autowired
    private Session session;


    @Transactional
    @Override
    public boolean add(Password entity)
    {
        session.beginTransaction();
        try
        {
            session.persist(entity);
        }
        finally
        {
            session.getTransaction().commit();
        }

        return session.contains(entity);
    }


    public boolean remove(Password entity)
    {
        session.beginTransaction();
        try
        {
            session.remove(entity);

        }
        finally
        {
            session.getTransaction().commit();
        }

        return !session.contains(entity);
    }

    @Override
    public boolean update(Password entity)
    {
        int id  = entity.getId();
        Password oldPassword = session.get(Password.class, id);

        session.beginTransaction();
        try
        {
            session.update(entity);
        }
        finally
        {
            session.getTransaction().commit();

        }

        return !session.get(Password.class, id).equals(oldPassword);
    }

    @Override
    public List<Password> getAll()
    {
        return session.createQuery("from Password").list();
    }

    public List<Password> getAllForUser(int id)
    {
        return session.createQuery("from Password where user_id = ?").setParameter(0, id).list();
    }
}
