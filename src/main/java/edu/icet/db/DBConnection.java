package edu.icet.db;

import edu.icet.model.Book;
import edu.icet.model.BookIssue;
import edu.icet.model.Member;
import edu.icet.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConnection {

    private static final SessionFactory sessionFactory =
            new Configuration()
                    .setProperty("hibernate.connection.url",
                            "jdbc:mysql://localhost:3306/LibraFlow?createDatabaseIfNotExist=true")
                    .setProperty("hibernate.connection.username", "root")
                    .setProperty("hibernate.connection.password", "JP26@MySQL")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Member.class)
                    .addAnnotatedClass(BookIssue.class)
                    .addAnnotatedClass(Book.class)
                    .buildSessionFactory();

    public static Session getSession() {
        return sessionFactory.openSession();
    }

}