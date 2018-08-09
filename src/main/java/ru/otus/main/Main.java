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
    public static CacheEngine cache;

    public static void main(String[] args) throws InterruptedException {
        int size = 3;
        //cache = new CacheEngineImpl<>(size, 0, 0, true);
        //cache = new CacheEngineImpl<>(size, 1006, 0, false);
        cache = new CacheEngineImpl<>(size, 0, 1020, false);
        DataSet u1 = new UserDataSet(1,
                "Даша", 25, new AddressDataSet("Ленина ул."),
                Arrays.asList(new PhoneDataSet("45-48-44"), new PhoneDataSet("11-11-11"))
        );

        DataSet u2 = new UserDataSet(2, "Маша", 23, new AddressDataSet("Куйбышева ул."),
                Arrays.asList(new PhoneDataSet("22-22-22"), new PhoneDataSet("xx-xx-xx")));

        DataSet u3 = new UserDataSet(3, "Паша", 43, new AddressDataSet("Арбат ул."),
                Arrays.asList(new PhoneDataSet("33-33-33"), new PhoneDataSet("yy-yy-yy")));

        DBService db = new DBServiceImpl();
       /*db.save(u1);
       db.save(u2);
       db.save(u3);*/
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
       /*
       DataSet u4=db.loadUser(2,UserDataSet.class);
       System.out.println("Прочитали по id: " +u4);
       DataSet u5= db.readByName("Даша",UserDataSet.class);
       System.out.println("Прочитали по имени: " + u5);
       List<UserDataSet> dataSets = db.readAll(UserDataSet.class);
       for (UserDataSet userDataSet : dataSets) {
           System.out.println(userDataSet);
       }*/

        db.shutdown();
        System.out.println("OKeY");
        cache.dispose();
    }
}