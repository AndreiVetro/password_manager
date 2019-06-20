package configuration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import util.WordUtil;

import java.io.IOException;

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
}
