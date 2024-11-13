package com.mayab.quality.integration;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.UserDAOMySql;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;

class UserServiceTest extends DBTestCase{
	UserDAOMySql daoMysql;
	private UserService userService;
    
	
	public UserServiceTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3307/calidad");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "123456");
	}
	@BeforeEach
    protected void setUp() throws Exception {
        daoMysql = new UserDAOMySql();
        userService = new UserService(daoMysql);

        IDatabaseConnection connection = getConnection();
        DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
        connection.close();
    }
	protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
 }
	@Test
	public void testCreateUser_HappyPath() throws Exception {
	    // Execute the happy path: creating a new user with valid data
		User user = new User("NewUser", "newuser@example.com", "password");
		
	    User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());
	    //createdUser.setId(1);
	    // Verify the user was created
	    assertNotNull(createdUser);
	    assertEquals("NewUser", createdUser.getName());
	    assertEquals("newuser@example.com", createdUser.getEmail());

	    // Set up expected database state from XML
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/test1.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Capture actual database state
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Compare expected vs. actual
	    Assertion.assertEquals(expectedTable, actualTable);
	}
	@Test
	public void testCreateUser_DuplicateEmail() throws Exception {
	    // Setup: Create an initial user
		User user = new User("NewUser", "newuser@example.com", "password");
		
	    User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());
	    

	    // Attempt to create a new user with the same email
	    User duplicateUser = userService.createUser("NewUser2", "newuser@example.com", "password2");

	    // Verify that duplicateUser is null (indicating failure to create due to duplicate email)
	    //assertNull(duplicateUser);

	    // Load the expected dataset
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/test2.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Get actual database state
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Compare expected and actual data
	    Assertion.assertEquals(expectedTable, actualTable);
	}
	@Test
	public void testCreateUser_ValidData() throws Exception {
	    // Execute the creation of a new user with valid data
	    User createdUser = userService.createUser("User", "user@example.com", "pass");

	    // Verify the user was not created
	    assertNull(createdUser);
	    

	    // Load the expected dataset
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/test3.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Capture actual database state
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Compare expected and actual data
	    Assertion.assertEquals(expectedTable, actualTable);
	}
	
	
	
	
	@Test
	public void testDeleteUser() throws Exception {
	    

		// Setup: Create an initial user
		User user = new User("NewUser", "newuser@example.com", "password");
				
		User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());
	    

	    // Step 2: Delete the user
	    boolean isDeleted = userService.deleteUser(user.getId());

	    // Verify deletion was successful
	    assertTrue(isDeleted);

	    

	    // Load the expected XML for verification
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/init.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");
	 // Capture the actual database state
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");
	    
	   

	    // Compare the actual database state with the expected state
	    Assertion.assertEquals(expectedTable, actualTable);
	    connection.close();
	}

	




	


}
