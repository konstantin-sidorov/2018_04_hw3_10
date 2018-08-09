package ru.otus.dbService;


import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.cache.CacheElement;
import ru.otus.dataSets.DataSet;
import ru.otus.main.Main;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDataSetDAO {
    private Session session;

    public UserDataSetDAO(Session session) {
        this.session = session;
    }

    public <T extends DataSet> T read(long id, Class<T> clazz) {
        CacheElement<T> cacheElem = (CacheElement<T>) Main.cache.get(id);
        if (cacheElem != null) {
            //System.out.println("Прочитали из кеша!");
            return cacheElem.getObj();
        }
        T obj = session.load(clazz, id);
        CacheElement<T> elem = new CacheElement<>(obj);
        Main.cache.put(elem);
        return obj;
    }

    public <T extends DataSet> void save(T dataSet) {
        session.save(dataSet);
        //CacheElement<T> elem = new CacheElement<>(dataSet);
        //Main.cache.put(elem);
    }

    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> from = criteria.from(clazz);
        criteria.where(builder.equal(from.get("name"), name));
        Query<T> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).list();
    }
}
