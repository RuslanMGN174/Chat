package ru.knyazev.server.chat.data;

import java.sql.*;

public class DBConnection {
    private static DBConnection instance;

    private static final String DB_URL = "jdbc:sqlite:E:/Java/Chat/src/ru/knyazev/server/chat/data/Users.db";
    private ResultSet resultSet;
    private Statement statement;
    private String userName;

    public static DBConnection getInstance() {
        if(instance == null) instance = new DBConnection();
        return instance;
    }

    public String getUserNameFromDB(String userLogin, String userPassword) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            statement = connection.createStatement();
            String query = String.format("select username from Users where login = '%s' and password = '%s'", userLogin , userPassword);
            resultSet = statement.executeQuery(query);
            userName = resultSet.getString("username");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userName;
    }
}
