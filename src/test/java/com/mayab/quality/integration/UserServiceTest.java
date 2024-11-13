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
	/*@Test
    public void testUpdateUser() throws Exception {
        // Setup: Create an initial user in the database
        User user = new User("OriginalName", "testupdate@example.com", "Password");
        User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());
        User user2 = new User("newName", "testupdate@example.com", "newPassword");

        // Execute the update method
        User updatedUser = userService.updateuser(user2);

        // Verify the user was updated
        assertNotNull(updatedUser);
        assertEquals("newName", updatedUser.getName());
        assertEquals("newPassword", updatedUser.getPassword());

        // Set up expected database state from XML
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/test4.xml"));
        ITable expectedTable = expectedDataSet.getTable("usuarios");

        // Capture actual database state
        IDatabaseConnection connection = getConnection();
        IDataSet databaseDataSet = connection.createDataSet();
        ITable actualTable = databaseDataSet.getTable("usuarios");

        // Compare expected vs. actual
        Assertion.assertEquals(expectedTable, actualTable);
    }*/
	@Test
	public void testDeleteUser() throws Exception {
	    // Setup: Create an initial user in the database
	    User user = new User("DeleteUser", "deleteuser@example.com", "password123");
	    User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());

	    // Execute delete method
	    boolean isDeleted = userService.deleteUser(createdUser.getId());


	    // Set up expected database state from XML
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Capture actual database state
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Compare expected vs. actual
	    Assertion.assertEquals(expectedTable, actualTable);
	}
	@Test
	public void testFindUserByEmail() throws Exception {
	    
	
	    User user = new User("NewUser", "newuser@example.com", "password");
		
	    User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());

	    // Ejecutar la búsqueda del usuario por correo electrónico
	    User foundUser = userService.findUserByEmail("newuser@example.com");

	    // Verificar que el usuario encontrado no es null y tiene los datos correctos
	    assertNotNull(foundUser);
	    assertEquals("NewUser", foundUser.getName());
	    assertEquals("newuser@example.com", foundUser.getEmail());

	    // Configurar el conjunto de datos esperado a partir del archivo XML
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/test1.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Capturar el estado real de la base de datos
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Comparar el estado esperado con el real
	    Assertion.assertEquals(expectedTable, actualTable);
	}
	@Test
	public void testFindUserById() throws Exception {
		User user = new User("NewUser", "newuser@example.com", "password");
		
	    User createdUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());

	    

	    // Obtener el ID del usuario creado
	    int userId = createdUser.getId();

	    // Ejecutar la búsqueda del usuario por ID
	    User foundUser = userService.findUserById(createdUser.getId());

	    // Verificar que el usuario encontrado no es null y tiene los datos correctos
	    assertNotNull(foundUser);
	    assertEquals("NewUser", foundUser.getName());
	    assertEquals("newuser@example.com", foundUser.getEmail());

	    // Configurar el conjunto de datos esperado a partir del archivo XML
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/test1.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Capturar el estado real de la base de datos
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Comparar el estado esperado con el real
	    Assertion.assertEquals(expectedTable, actualTable);
	}
	
	
	@Test
	public void testFindAllUsers() throws Exception {
	    // Setup: Crear usuarios en la base de datos
	    User user1 = new User("User1", "user1@example.com", "password123");
	    User user2 = new User("User2", "user2@example.com", "password456");
	    User user3 = new User("User3", "user3@example.com", "password789");

	    // Crear los usuarios usando el servicio
	    userService.createUser(user1.getName(), user1.getEmail(), user1.getPassword());
	    userService.createUser(user2.getName(), user2.getEmail(), user2.getPassword());
	    userService.createUser(user3.getName(), user3.getEmail(), user3.getPassword());

	    // Ejecutar la búsqueda de todos los usuarios
	    List<User> users = userService.findAllUsers();

	    // Verificar que la lista de usuarios no sea nula y contenga los usuarios esperados
	    assertNotNull(users);
	    assertEquals(3, users.size()); // Verificar que se crearon 3 usuarios

	    // Verificar que los usuarios están correctos
	    assertEquals("User1", users.get(0).getName());
	    assertEquals("user1@example.com", users.get(0).getEmail());

	    assertEquals("User2", users.get(1).getName());
	    assertEquals("user2@example.com", users.get(1).getEmail());

	    assertEquals("User3", users.get(2).getName());
	    assertEquals("user3@example.com", users.get(2).getEmail());

	    // Configurar el conjunto de datos esperado a partir del archivo XML
	    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/testFindAllUsers.xml"));
	    ITable expectedTable = expectedDataSet.getTable("usuarios");

	    // Capturar el estado real de la base de datos
	    IDatabaseConnection connection = getConnection();
	    IDataSet databaseDataSet = connection.createDataSet();
	    ITable actualTable = databaseDataSet.getTable("usuarios");

	    // Comparar el estado esperado con el real
	    Assertion.assertEquals(expectedTable, actualTable);
	}







	
	
	
	
	

	




	


}
