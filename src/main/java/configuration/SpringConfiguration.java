package configuration;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.cryptonode.jncryptor.AES256JNCryptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import util.WordUtil;

@Configuration
@ComponentScan({"repository", "controller"})
public class SpringConfiguration
{
    @Bean
    Session session()
    {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory.openSession();
    }

    @Bean
    WordUtil wordUtil()
    {
        return new WordUtil();
    }

    @Bean
    Argon2 argon2()
    {
        return Argon2Factory.create();
    }

    @Bean
    AES256JNCryptor aes256JNCryptor()
    {
        return new AES256JNCryptor();
    }

}
