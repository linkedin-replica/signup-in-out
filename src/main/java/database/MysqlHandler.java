package database;

import model.User;
import org.javalite.activejdbc.Base;

public class MysqlHandler implements DatabaseHandler {


    public void connect() {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/linkedin", "root", "MOhamed2@");
    }

    public void disconnect() {
        Base.close();
    }

    public User getUser(String email) {
        User u = User.findFirst("email = ?", email);
        System.out.println(u);
        return u;
    }

    public void createUser(String email, String password) {
        User u = new User();
        u.set("email", email);
        u.set("password", password);
        u.saveIt();
    }

    public static void main(String[] args) {
        MysqlHandler db = new MysqlHandler();
        db.connect();

        //db.createUser("ab", "cd");
        User u = db.getUser("Esraa.Khaled@yahoo.com");
        System.out.println(u.get("id"));
        db.disconnect();
    }
}
