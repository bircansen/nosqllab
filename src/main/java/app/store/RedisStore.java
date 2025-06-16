package app.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import app.model.Student;
import com.google.gson.Gson;

import java.util.Map;

public class RedisStore {
    static JedisPool pool;
    static Gson gson = new Gson();

    public static void init() {
        pool = new JedisPool("localhost", 6379);

        try (Jedis jedis = pool.getResource()) {
            if (jedis.dbSize() >= 10000) {
                System.out.println("Redis zaten dolu, veri eklenmeyecek.");
                return;
            }

            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
                jedis.set(id, gson.toJson(s));
            }
            System.out.println("Redis'e 10.000 kayıt eklendi.");
        }
    }

    public static Student get(String id) {
        try (Jedis jedis = pool.getResource()) {
            String json = jedis.get(id);
            return gson.fromJson(json, Student.class);
        }
    }

    public void save(Student student) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset("student:" + student.getOgrenciNo(), Map.of(
                    "name", student.getAdSoyad(),
                    "department", student.getBolum()
            ));
        }
    }
}