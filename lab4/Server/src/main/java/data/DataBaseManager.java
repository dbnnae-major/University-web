package data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DataBaseManager {
    private static SessionFactory sessionFactory;
    private static List<RequestData> requests;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Инициализация SessionFactory завершилась неудачей: " + ex);
            throw new RuntimeException(ex);
        }
    }

    //RequestData
    public static List<RequestData> getRequestsByOwner(String owner) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM RequestData WHERE owner = :owner", RequestData.class)
                    .setParameter("owner", owner)
                    .getResultList();
        }
    }

    public static void deleteRequestByIdAndOwner(Long id, String owner) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM RequestData WHERE id = :id AND owner = :owner")
                    .setParameter("id", id)
                    .setParameter("owner", owner)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static List<RequestData> getRequests() {
        return requests;
    }

    public static void setRequests(List<RequestData> requests) {
        DataBaseManager.requests = requests;
    }

    //users
    public static boolean doesUserExist(String login) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(u) FROM UserData u WHERE u.login = :login";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("login", login)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getPasswordByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT u.password FROM UserData u WHERE u.login = :login";
            return session.createQuery(hql, String.class)
                    .setParameter("login", login)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //mixed
    public static void addData(Object o) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(o);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}