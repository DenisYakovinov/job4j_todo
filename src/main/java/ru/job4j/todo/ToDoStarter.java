package ru.job4j.todo;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
@PropertySource("datasource.properties")
public class ToDoStarter {

    @Value("${jdbc.driver}")
    private String className;

    @Value ("${jdbc.url}")
    private String url;

    @Value ("${jdbc.username}")
    private String login;

    @Value ("${jdbc.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public SessionFactory sf(BasicDataSource basicDataSource) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting(AvailableSettings.DATASOURCE, basicDataSource)
                .configure()
                .build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Bean(destroyMethod = "close")
    public BasicDataSource basicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(className);
        dataSource.setUrl(url);
        dataSource.setUsername(login);
        dataSource.setPassword(password);
        return dataSource;
    }

    @GetMapping("/")
    String home() {
        return "redirect:/index";
    }

    public static void main(String[] args) {

        SpringApplication.run(ToDoStarter.class, args);
    }
}
