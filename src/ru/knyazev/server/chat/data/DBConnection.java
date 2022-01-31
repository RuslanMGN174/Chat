package ru.knyazev.server.chat.data;

import java.sql.*;

public class DBConnection {
//    private static DBConnection instance;

    private ResultSet resultSet;
    private String userName;

    public String getUserNameFromDB(String userLogin, String userPassword, Statement statement) {
        try {
            String query = String.format("select username from Users where login = '%s' and password = '%s'", userLogin , userPassword);
            resultSet = statement.executeQuery(query);
            userName = resultSet.getString("username");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userName;
    }

    public Statement connect(String dbUrl) throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl);
        return connection.createStatement();
    }
}
