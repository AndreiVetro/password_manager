package domain;

import javax.persistence.*;

@Entity
public class Password
{
    private int id;
    private char[] text;
    private char[] service;
    private User user;

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
}
