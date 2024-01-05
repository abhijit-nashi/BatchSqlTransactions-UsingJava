package batchSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchProcessingWithRollbackExample {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db";
        String user = "********";
        String password = "*****";

        try  {
            // Disable auto-commit to start a transaction
        	Connection connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

            try  {
            	PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO book (id, name,cost) VALUES (?, ?, ?)");
                // Add statements to the batch
                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, "Timelapse");
                preparedStatement.setInt(3, 150);
                preparedStatement.addBatch();

                preparedStatement.setInt(1, 2);
                preparedStatement.setString(2, "Potrait");
                preparedStatement.setInt(3, 200);
                preparedStatement.addBatch();

                // Execute the batch
                int[] affectedRows = preparedStatement.executeBatch();
                System.out.println(affectedRows);
                // Check if all statements were executed successfully
                for (int rows : affectedRows) {
                    if (rows <= 0) {
                        // Rollback the transaction if any statement failed
                        connection.rollback();
                        System.out.println("Rollback executed. Some statements failed.");
                        return;
                    }
                }

                // Commit the transaction if all statements were executed successfully
                connection.commit();
                System.out.println("Transaction committed successfully.");
            } catch (SQLException e) {
                // Rollback the transaction in case of an exception
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
