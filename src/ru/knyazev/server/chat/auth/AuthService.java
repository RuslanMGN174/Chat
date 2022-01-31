package ru.knyazev.server.chat.auth;

import java.sql.*;
import java.util.Set;

public class AuthService {

//    private static final String DB_URL = "jdbc:sqlite:E:/Java/Chat/src/ru/knyazev/server/chat/data/Users.db";
//    private ResultSet resultSet;
//    private String userName;
//
//    public String getUserNameFromDB(String userLogin, String userPassword) {
//        try (Connection connection = DriverManager.getConnection(DB_URL)) {
//            Statement statement = connection.createStatement();
//            String query = String.format("select username from Users where login = '%s' and password = '%s'", userLogin , userPassword);
//            resultSet = statement.executeQuery(query);
//            userName = resultSet.getString("username");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return userName;
//    }

//    private static final Set<User> USERS = Set.of(
//            new User("login1", "pass1", "username1"),
//            new User("login2", "pass2", "username2"),
//            new User("login3", "pass3", "username3")
//    );

//    public String getUserNameByLoginAndPassword(String login, String password) {
//        User requiredUser = new User(login, password);
//        for (User user : USERS) {
//            if (requiredUser.equals(user)) {
//                return user.getUserName();
//            }
//        }
//
//        return null;
//    }
}
