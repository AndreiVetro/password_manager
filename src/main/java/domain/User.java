package domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class User
{
    private String username; // this is encrypted using the master password's hash as a key
    private char[] masterPassword; //this is getting hashed
    private List<Password> passwords; //same as username

    private int id;

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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public char[] getMasterPassword()
    {
        return masterPassword;
    }

    public void setMasterPassword(char[] masterPassword)
    {
        this.masterPassword = masterPassword;
    }

    @OneToMany
    @JoinColumn(name = "user_id")
    public List<Password> getPasswords()
    {
        return passwords;
    }

    public void setPasswords(List<Password> passwords)
    {
        this.passwords = passwords;
    }
}
