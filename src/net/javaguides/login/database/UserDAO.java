package net.javaguides.login.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javaguides.login.bean.User;
import net.javaguides.login.bean.Product;

/**
 * AbstractDAO.java This DAO class provides CRUD database operations for the
 * table users in the database.
 * 
 * @author Ramesh Fadatare
 *
 */
public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/amigos?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +
        " (?, ?, ?);";
    private static final String INSERT_PRODUCTS_SQL =  "INSERT INTO products" + "  (item, user_id) VALUES " +
            " (?, ?);";

    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_PRODUCTS = "select products.item, products.user_id from products where user_id= ?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users, products where users.id=products.user_id  group by users.id";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

    public UserDAO() {}

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

    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
        	
        	
        	PreparedStatement preparedStatement1 = connection.prepareStatement(INSERT_PRODUCTS_SQL);
        	
 
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            int   p = preparedStatement.executeUpdate();
         
	         ResultSet rs = preparedStatement.getGeneratedKeys();
	         int pp =0;
	         if (rs.next()){
	             pp=rs.getInt(1);
	         }
         
	         System.out.println("------INSERTED QUERY ROW ID-----" + pp);
	         
	         
	         System.out.println("------INSERTED QUERY ITEM-----" + user.getList().get(0).getItem());
         
        
            
            
            
            preparedStatement1.setString(1, user.getList().get(0).getItem());
            preparedStatement1.setInt(2, pp);
            
            preparedStatement1.executeUpdate();
            
		
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                String list = rs.getString("country");
  //              user = new User(id, name, email, country, list);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public List <User> selectAllUsers() {

        // using try-with-resources to avoid closing resources (boiler plate code)
        List <User> users = new ArrayList <> ();
     
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            PreparedStatement preparedStatement1 = connection.prepareStatement(SELECT_ALL_PRODUCTS);
        		
        	) {
            System.out.println(preparedStatement);
            System.out.println(preparedStatement1);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
        

            System.out.println(rs);
            
       
            // Step 4: Process the ResultSet object.
            while (rs.next()) {
            	List <Product> products = new ArrayList <> ();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                
                
                preparedStatement1.setInt(1, id);
                ResultSet rs1 = preparedStatement1.executeQuery();
              
                while (rs1.next()) {
                   
                	 String item = rs1.getString("item");
                     String userId = rs1.getString("user_id");
                     products.add(new Product(item, userId));
                }
                
				/*
				 * String item = rs.getString("item"); String userId = rs.getString("user_id");
				 * products.add(new Product(item, userId));
				 */
                
              //  List<Product> pro = rs.getObject();
                System.out.println(users);
         //       System.out.println(country);
          //      System.out.println(item);
                users.add(new User(id, name, email, country, products));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
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