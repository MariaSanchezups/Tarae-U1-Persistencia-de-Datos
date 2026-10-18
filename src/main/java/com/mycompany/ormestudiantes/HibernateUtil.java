package com.mycompany.ormestudiantes;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory sessionFactory =
            construirSessionFactory();

    private static SessionFactory construirSessionFactory() {
        StandardServiceRegistry registro =
                new StandardServiceRegistryBuilder()
                        .applySetting(
                            "hibernate.connection.driver_class",
                            "org.h2.Driver")
                        .applySetting(
                            "hibernate.connection.url",
                            "jdbc:h2:./tutidb")
                        .applySetting(
                            "hibernate.connection.username", "sa")
                        .applySetting(
                            "hibernate.connection.password", "")
                        .applySetting(
                            "hibernate.dialect",
                            "org.hibernate.dialect.H2Dialect")
                        .applySetting(
                            "hibernate.hbm2ddl.auto", "update")
                        .applySetting(
                            "hibernate.show_sql", "true")
                        .applySetting(
                            "hibernate.format_sql", "true")
                        .build();

        try {
            return new MetadataSources(registro)
                    .addAnnotatedClass(Producto.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registro);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void cerrar() {
        if (sessionFactory != null
                && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}

