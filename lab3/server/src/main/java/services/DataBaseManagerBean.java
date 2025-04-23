package services;

import data.RequestData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

@ManagedBean(name = "dataBase", eager = true)
@RequestScoped
public class DataBaseManagerBean {

    private String result;
    private SessionFactory sessionFactory;
    private List<RequestData> requests;

    public DataBaseManagerBean() {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            getAllRequests();
        } catch (Throwable ex) {
            System.err.println("Инициализация SessionFactory завершилась неудачей: " + ex);
            throw new RuntimeException(ex);
        }
    }

    public void clearAndReloadRequests() {
        clearAllRequests();
        getAllRequests();
    }

    public String addRequestData(RequestData requestData) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(requestData);
            transaction.commit();
            return "Данные успешно добавлены: " + requestData;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return "Ошибка";
        }
    }

    public String getAllRequests() {
        try (Session session = sessionFactory.openSession()) {
            requests = session.createQuery("FROM RequestData", RequestData.class).getResultList();
            result = "goToTablePage";
            return result;
        }
    }

    public void clearAllRequests() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM RequestData").executeUpdate();
            session.getTransaction().commit();
        }
    }

    public String dropAllRequests(){
        try (Session session = sessionFactory.openSession()){
            session.createQuery("DELETE FROM RequestData").executeUpdate();
            result = "clear";
            return result;
        }
    }

    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public List<RequestData> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestData> requests) {
        this.requests = requests;
    }
}
