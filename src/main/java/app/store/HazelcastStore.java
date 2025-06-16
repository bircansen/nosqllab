package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import app.model.Student;
import com.hazelcast.map.IMap;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("hello-world");
        clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701"); //ip + port

        hz = HazelcastClient.newHazelcastClient(clientConfig);
        map = hz.getMap("ogrenciler");

        if (map.size() >= 10000) {
            System.out.println("Hazelcast zaten dolu, veri eklenmeyecek.");
            return;
        }
        hz = HazelcastClient.newHazelcastClient(clientConfig);
        map = hz.getMap("ogrenciler");

        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            map.put(id, s);
        }
    }

    public static Student get(String id) {
        return map.get(id);
    }

    public void save(Student student) {
        map.put(student.getOgrenciNo(), student);
    }
}