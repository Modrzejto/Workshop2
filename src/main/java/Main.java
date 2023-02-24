import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

public class Main {
    public static void main(String[] args) {
        UserDao userDao1 = new UserDao();

        for (User u : userDao1.findAll()) {
            System.out.println(u);
        }
    }
}
