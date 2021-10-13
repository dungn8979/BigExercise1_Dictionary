package Solution;

import java.sql.*;

public class AccessSQL {
    private Connection connection;

    // Getter and Setter
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Constructor.
     * Connect DataBase.
     *
     * @param url String Ex: "jdbc:mysql://localhost/"
     * @param database_name String
     * @param username String
     * @param password String
     */
    public AccessSQL(String url, String database_name, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url + database_name, username, password);
            if (connection != null) {
                System.out.println("Connect Database successfully");
            }
        }
        catch (Exception exp) {
            System.out.println("Connect Database fail");
            exp.printStackTrace();
        }
    }

    /**
     * Constructor.
     * Connect DataBase.
     * @param database_name String
     * @param username String
     * @param password String
     */
    public AccessSQL(String database_name, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database_name, username, password);
            if (connection != null) {
                System.out.println("Connect Database successfully");
            }
        }
        catch (Exception exp) {
            System.out.println("Connect Database fail");
            exp.printStackTrace();
        }
    }

    public void Close() throws SQLException {
        connection.close();
    }

    public void setDataBase(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("setDataBase success");
        } catch (Exception exp) {
            System.out.println("setDataBase fail");
            exp.printStackTrace();
        }
    }

    /**
     * lấy dữ liệu ResultSet luôn phải có resultSet.next() để không bị lỗi.
     *
     * @param sql
     * @return resultSet
     */
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
}
