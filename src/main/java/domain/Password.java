package domain;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Password
{
    private int id;
    private char[] text;
    private char[] service;
    private User user;

    public Password(char[] text, char[] service, User user)
    {
        this.text = text;
        this.service = service;
        this.user = user;
    }

    public Password() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public char[] getText()
    {
        return text;
    }

    public void setText(char[] text)
    {
        this.text = text;
    }

    public char[] getService()
    {
        return service;
    }

    public void setService(char[] source)
    {
        this.service = source;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return String.valueOf(service);
    }
}
