import java.sql.*;
import java.util.List;

public class SQLutils {
    private final String baseUrl = "jdbc:mysql://localhost/dictionary";
    private final String userName = "root";
    private final String passWord = "";
    public void getConnection(List<Word> wordList) {
        Connection connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(this.baseUrl, this.userName, this.passWord);
            if (connection != null) {
                System.out.println("Successfully");
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tbl_edict");
            String targetTemp;
            String explainTemp;
            while (resultSet.next()) {
                targetTemp = resultSet.getString(2);
                explainTemp = resultSet.getString(3);
                Word _word = new Word(targetTemp, explainTemp);
                wordList.add(_word);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Failure");
            e.printStackTrace();
        }
    }
}
