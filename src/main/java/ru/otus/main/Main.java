package ru.otus.main;

import ru.otus.cache.*;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.dataSets.AddressDataSet;
import ru.otus.dataSets.DataSet;
import ru.otus.dataSets.PhoneDataSet;
import ru.otus.dataSets.UserDataSet;
import ru.otus.dbService.DBService;
import ru.otus.dbService.DBServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    private CacheEngine cache;

    public static void main(String[] args) throws InterruptedException {
        int size = 3;
        //CacheEngineImpl cache = new CacheEngineImpl<>(size, 0, 0, true);
        //CacheEngineImpl cache = new CacheEngineImpl<>(size, 1006, 0, false);
        CacheEngineImpl cache = new CacheEngineImpl<>(size, 0, 900, false);
        DataSet u1 = new UserDataSet(1,
                "Даша", 25, new AddressDataSet("Ленина ул."),
                Arrays.asList(new PhoneDataSet("45-48-44"), new PhoneDataSet("11-11-11"))
        );

        DataSet u2 = new UserDataSet(2, "Маша", 23, new AddressDataSet("Куйбышева ул."),
                Arrays.asList(new PhoneDataSet("22-22-22"), new PhoneDataSet("xx-xx-xx")));

        DataSet u3 = new UserDataSet(3, "Паша", 43, new AddressDataSet("Арбат ул."),
                Arrays.asList(new PhoneDataSet("33-33-33"), new PhoneDataSet("yy-yy-yy")));

        DBService db = new DBServiceImpl(cache);

        List<DataSet> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        db.addUsers(users);
        for (int i = 1; i < 4; i++) {
            DataSet element = db.loadUser(i, UserDataSet.class);
            System.out.println("element " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(1000);

        for (int i = 1; i < 4; i++) {
            DataSet element = db.loadUser(i, UserDataSet.class);
            System.out.println("element " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        db.shutdown();
        System.out.println("OKeY");
        cache.dispose();
    }
}