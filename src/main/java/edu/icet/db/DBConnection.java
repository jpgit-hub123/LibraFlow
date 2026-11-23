package edu.icet.db;

import edu.icet.model.entity.BookEntity;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import edu.icet.model.entity.UserEntity;
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
                    .addAnnotatedClass(UserEntity.class)
                    .addAnnotatedClass(MemberEntity.class)
                    .addAnnotatedClass(BookIssueEntity.class)
                    .addAnnotatedClass(BookEntity.class)
                    .buildSessionFactory();

    public static Session getSession() {
        return sessionFactory.openSession();
    }

}