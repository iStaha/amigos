package net.javaguides.login.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javaguides.login.bean.Product;
import net.javaguides.login.bean.User;

public class ProductDao {
	
	 private String jdbcURL = "jdbc:mysql://localhost:3306/amigos?useSSL=false";
	    private String jdbcUsername = "root";
	    private String jdbcPassword = "";

	    private static final String INSERT_USERS_SQL = "INSERT INTO products" + "  (item, user_id) VALUES " +
	        " (?, ?);";
	    
	    private static final String SELECT_ALL_PRODUCTS = "SELECT * from products";
	    
	    private static final String SELECT_PRODUCT_BY_ID = "select id,  item from products where id =?";
	    
	    
	    
	    public ProductDao() {}

	    protected Connection getConnection() {
	        Connection connection = null;
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return connection;
	    }
	    
	    
	    public void insertProduct(Product product) throws SQLException {
	        System.out.println(INSERT_USERS_SQL);
	        // try-with-resource statement will auto close the connection.
	        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
	            preparedStatement.setString(1, product.getItem());
	            preparedStatement.setString(2, product.getUser_id());
	            System.out.println(preparedStatement);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	    }
	    
	    
	    public Product selectProduct(int id) {
	        Product product = null;
	        // Step 1: Establishing a Connection
	        try (Connection connection = getConnection();
	            // Step 2:Create a statement using connection object
	            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID);) {
	            preparedStatement.setInt(1, id);
	            System.out.println(preparedStatement);
	            // Step 3: Execute the query or update query
	            ResultSet rs = preparedStatement.executeQuery();

	            // Step 4: Process the ResultSet object.
	            while (rs.next()) {
	            	int i = rs.getInt("id");
	                String item = rs.getString("item");
	                product = new Product(i, item);
	            }
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	        return product;
	    }

	    public List <Product> selectAllProducts() {
	        System.out.println("SELECT_ALL_USERS");
	        List <Product> products = new ArrayList <> ();
	        // try-with-resource statement will auto close the connection.
	        try (Connection connection = getConnection();
	        	  PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
	        	  System.out.println(preparedStatement);
	              // Step 3: Execute the query or update query
	              ResultSet rs = preparedStatement.executeQuery();
	              // Step 4: Process the ResultSet object.
	              while (rs.next()) {
	            	  int id = rs.getInt("id");
	                  String item = rs.getString("item");
	                  String userId = rs.getString("user_id");
	                
	                  products.add(new Product(id, item, userId));
	                
	        
	               
	              }
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	        System.out.println("Products in List "  +products);
	        
	        return products;
	    }
	    
	    
	    private void printSQLException(SQLException ex) {
	        for (Throwable e: ex) {
	            if (e instanceof SQLException) {
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = ex.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }


}
