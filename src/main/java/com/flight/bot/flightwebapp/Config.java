package com.flight.bot.flightwebapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Michael on 12/2/2017.
 */
@Configuration
public class Config {

    @Value("${db.url}")
    String databaseUrl;

    @Value("${db.password}")
    String databasePassword;

    @Value("${db.username}")
    String databaseUserName;

    @Value("${email.address}")
    String emailAddress;

    @Value("${email.password}")
    String emailPassword;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUserName);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean
    public Message message() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailAddress, emailPassword);
                    }
                });

            Message message = new MimeMessage(session);
           return message;

    }
}
