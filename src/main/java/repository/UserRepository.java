package repository;

import de.mkammerer.argon2.Argon2;
import domain.Password;
import domain.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@org.springframework.stereotype.Repository
public class UserRepository implements Repository<User>
{
    @Autowired
    private Session session;

    @Autowired
    private Argon2 argon2;

    private void hashPasswordText(User user)
    {
        String hash = argon2.hash(10, 65536, 6, user.getMasterPassword());
        user.setMasterPassword(hash.toCharArray());
    }


    @Override
    public boolean add(User entity)
    {
        hashPasswordText(entity);
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        return session.contains(entity);
    }

    @Override
    public boolean remove(User entity)
    {
        session.beginTransaction();
        session.remove(entity);
        session.getTransaction().commit();

        return !session.contains(entity);
    }

    @Override
    public boolean update(User entity)
    {
        int id  = entity.getId();
        User oldUser = session.get(User.class, id);

        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();

        return !session.get(User.class, id).equals(oldUser);
    }

    @Override
    public List<User> getAll()
    {
        return session.createQuery("from User").list();
    }

    public User getByUsername(String username)
    {
        //session.createQuery("from User where username = ?").setParameter(0, username).getSingleResult(); throws exc if not found
        //forces exception handling, avoid like the plague

        List<User> users = session.createQuery("from User where username = ?").setParameter(0, username).list();

        if(users.isEmpty())
        {
            return null;
        }
        else
        {
            return users.get(0);
        }
    }
}
