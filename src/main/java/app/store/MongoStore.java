package app.store;

import com.mongodb.client.*;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        client = MongoClients.create("mongodb+srv://bircansen:VCDnanJZcgdYQQxG@cluster0.nyuewj6.mongodb.net/");
        collection = client.getDatabase("nosqllab").getCollection("ogrenciler");

        long count = collection.countDocuments();
        if (count >= 10000) {
            System.out.println("MongoDB zaten dolu, veri eklenmeyecek.");
            return;
        }

        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            collection.insertOne(Document.parse(gson.toJson(s)));
        }

        System.out.println("MongoDB'ye 10.000 kayıt eklendi.");
    }

    public static Student get(String id) {
        Document doc = collection.find(new Document("ogrenciNo", id)).first();
        return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
    }

    public void save(Student student) {
        Document doc = new Document("student_no", student.getOgrenciNo())
                .append("name", student.getAdSoyad())
                .append("department", student.getBolum());
        collection.insertOne(doc);
    }
}
