package controller;

import app.Main;
import domain.Password;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import repository.PasswordRepository;
import repository.UserRepository;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
class ParentController
{
    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;



    List<Password> getPasswordsCurrentUser()
    {
        return passwordRepository.getAllForUser(Main.user.getId());
    }

    List<User> getUsers()
    {
        return userRepository.getAll();
    }

    void addUser(User user)
    {
        userRepository.add(user);
    }

    void addPassword(Password password)
    {
        passwordRepository.add(password);
    }

    void removePassword(Password password)
    {
        passwordRepository.remove(password);
    }

    void updatePassword(Password password)
    {
        passwordRepository.update(password);
    }
}
