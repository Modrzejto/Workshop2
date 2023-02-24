package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String REMOVE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement stmt =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(READ_USER_QUERY);
            ResultSet rs = stmt.executeQuery();

            User readUser = new User();
            while (rs.next()) {
                if (rs.getInt("id") == userId) {
                    readUser.setId(rs.getInt("id"));
                    readUser.setEmail(rs.getString("email"));
                    readUser.setUserName(rs.getString("username"));
                    readUser.setPassword(rs.getString("password"));
                    return readUser;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.setInt(4, user.getId());
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(REMOVE_USER_QUERY);
            stmt.setString(1, String.valueOf(userId));
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(READ_USER_QUERY);
            ResultSet rs = stmt.executeQuery();

            User readUser;
            ArrayList<User> readUsersList = new ArrayList<>();

            while (rs.next()) {
                readUser = new User();
                readUser.setId(rs.getInt("id"));
                readUser.setUserName(rs.getString("username"));
                readUser.setEmail(rs.getString("email"));
                readUser.setPassword(hashPassword(rs.getString("password")));
                readUsersList.add(readUser);
            }

            return readUsersList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private int findUser(int userId) {
//
//    }

//    PreparedStatement preStmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//    This gets id from users
//ResultSet rs = preStmt.getGeneratedKeys();
//
//if (rs.next()) {
//        long id = rs.getLong(1);
//        System.out.println("Inserted ID: " + id);
//    }
}
