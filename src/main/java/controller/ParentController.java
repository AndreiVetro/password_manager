package controller;

import app.Main;
import de.mkammerer.argon2.Argon2;
import domain.Password;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import repository.PasswordRepository;
import repository.UserRepository;

import java.util.List;

@Controller
class ParentController
{
    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Argon2 argon2;


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
