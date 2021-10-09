package Solution;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccessSQL {
    private Connection connection;

    public AccessSQL(String url, String database_name, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url + database_name, username, password);
            if (connection != null) {
                System.out.println("Successfully");
            }
        }
        catch (Exception exp) {
            System.out.println("Connect fail");
            exp.printStackTrace();
        }
    }

    public AccessSQL(String database_name, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database_name, username, password);
            if (connection != null) {
                System.out.println("Successfully");
            }
        }
        catch (Exception exp) {
            System.out.println("Connect fail");
            exp.printStackTrace();
        }
    }

    public void setDataBase(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            System.out.println("setDataBase success");
        } catch (Exception exp) {
            System.out.println("setDataBase fail");
            exp.printStackTrace();
        }
    }

    public ResultSet getDataBase(String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            System.out.println("getDataBase success");
        } catch (Exception exp) {
            System.out.println("getDataBase fail");
            exp.printStackTrace();
        }
        return resultSet;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
