package com.mayab.quality.unittest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;
import static org.hamcrest.Matchers.containsInAnyOrder;


class UserServiceTest {
	private UserService service;
	private IDAOUser dao;
	private HashMap<Integer, User> db;
	private User user;
	
	@BeforeEach
	public void setUp() throws Exception {
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db= new HashMap<Integer, User>();
		user = new User("name", "email", "password");
	}

	/*@Test
	public void whenPasswordShort() {
		String shortPass = "123";
		String name ="user1";
		String email ="user1@email.com";
		User user =null;
		
		when(dao.findByUserName(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(0);
		
		user= service.createUser(name, email, shortPass);
		
		assertThat(user, is(nullValue()));
		
	}*/
	
	//Primer test, Happy path Create user
	@Test
	public void whenAllDataCorrect_saveUserTest() {	
		//Se obtiene el tamaño de la bd antes de agregar usuario
		int sizeBefore = db.size();
		when(dao.findByUserName(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
	        public Integer answer(InvocationOnMock invocation) {
	            User user = (User) invocation.getArguments()[0];
	            //El ID es el tamaño de la db +1
	            int newId = db.size() + 1;
	            user.setId(newId); 
	            // Agregar el usuario a la base de datos simulada
	            db.put(newId, user); 
	            return newId; 
	        }
	    });
		
		//Se hace el ejercicio 
		User createdUser = service.createUser("name", "email", "password");
		
		//Se hace la comprobación de que el tamaño de la bd sea de uno más
		int sizeAfter = db.size();
	    assertThat(sizeAfter, is(sizeBefore + 1));
		
		
		
	}
	//Segundo test, cuando el email ya existe y se intenta crear el usuario, no se crea otro,
	//simplemente se regresa el que ya existe
	@Test
	public void duplicatedUser_whenCreating(){
		// Configurar un usuario existente en la bd
	    User existingUser = new User("name", "emailduplicado", "password");

	    // Configurar el mock para simular que el email ya está en uso
	    when(dao.findUserByEmail("emailduplicado")).thenReturn(existingUser);

	    // Intentar crear un nuevo usuario con el mismo email
	    User newUser = service.createUser("newName", "emailduplicado", "anotherpass");

	    // Verificar que no se creó el usuario, createUser retorna el mismo usuario que ya existía
	    assertThat(newUser, is(existingUser));
		
	}
	
	//Tercer test, actualizar password 
	@Test
	public void updatePassword_whenUserExists() {
	    // Configurar el usuario existente en la base de datos simulada
	    User existingUser = new User("oldName", "oldEmail", "oldPassword");
	    existingUser.setId(1);
	    db.put(1, existingUser); 

	    // Configurar el mock para devolver el usuario existente cuando se busque por ID
	    when(dao.findById(1)).thenReturn(existingUser);
	    when(dao.updateUser(any(User.class))).thenAnswer(invocation -> {
	        User updatedUser = (User) invocation.getArguments()[0];
	        db.replace(updatedUser.getId(), updatedUser);
	        return db.get(updatedUser.getId());
	    });

	    // Crear el nuevo usuario con la contraseña actualizada
	    User updatedUser = new User("oldName", "oldEmail", "newPassword");
	    updatedUser.setId(1);

	    // Ejercicio de actualizar la contraseña
	    User result = service.updateuser(updatedUser);

	    // Verificar que la contraseña se haya actualizado
	    assertThat(result.getPassword(), is("newPassword"));
	}
	
	//Cuarto test, borrar un usuario
	@Test
	public void deleteUser_whenUserExists() {
	    // Asignar un ID al usuario 
	    int newId = db.size() + 1;
	    User user = new User("name", "email", "password");
	    user.setId(newId);
	    // Agregar el usuario a la base de datos mock
	    db.put(user.getId(), user);  

	    // Tamaño de la base de datos antes de la eliminación
	    int sizeBefore = db.size();

	    // Configurar el mock para simular que la eliminación fue exitosa
	    when(dao.deleteById(user.getId())).thenAnswer(invocation -> {
	        db.remove(invocation.getArgument(0));
	        return true;
	    });

	    // Se hace el ejercicio de borrar usuario
	    boolean isDeleted = service.deleteUser(user.getId());

	    // Tamaño de la base de datos después de la eliminación
	    int sizeAfter = db.size();

	    // Verificaciones
	    assertThat(sizeAfter, is(sizeBefore - 1));
	    assertNull(db.get(user.getId())); 
	}
	
	//Quinto test, happy path, buscar a un usuario por email
	@Test
	public void whenUserExists_findUserByEmail() {
	    // Configurar un usuario que se espera encontrar
	    User expectedUser = new User("name", "email", "password");

	    // Configurar el mock para devolver el usuario cuando se busca por email
	    when(dao.findUserByEmail("email")).thenReturn(expectedUser);

	    // Ejercicio
	    User result = service.findUserByEmail("email");

	    // Verificar que el usuario encontrado es el esperado
	    assertThat(result, is(expectedUser));
	}
	//Sexto test, cuando se intenta buscar por email pero no existe en la bd
	@Test
	public void whenEmailNotFound_returnNull() {
	    //Agregar un usuario inicial a la base de datos mock
	    User existingUser = new User("existingUser", "existingemail", "password");
	    existingUser.setId(1);
	    db.put(existingUser.getId(), existingUser);
	    
	    //Buscar en db y devolver null si el email no existe
	    when(dao.findUserByEmail(anyString())).thenAnswer(invocation -> {
	        String email = invocation.getArgument(0);
	        // Verificar si el email existe en el HashMap, si no, devolver null
	        return db.values().stream()
	                 .filter(user -> user.getEmail().equals(email))
	                 .findFirst()
	                 .orElse(null);
	    });

	    //Ejercicio con un email que no esté en la base de datos
	    User result = service.findUserByEmail("nonexistentemail");

	    
	    assertThat(result, is(nullValue()));
	}
	//Séptimo test, se comprueba que findAllUsers devuelva todos los usuarios
	@Test
	public void whenFindAllUsers_returnAllUsers() {
	    //Agregar usuarios a la bd mock
	    User user1 = new User("user1", "email1", "password1");
	    User user2 = new User("user2", "email2", "password2");
	    user1.setId(1);
	    user2.setId(2);
	    
	    db.put(user1.getId(), user1);
	    db.put(user2.getId(), user2);
	    
	    //Devolver todos los usuarios en la lista cuando se llame findAll
	    when(dao.findAll()).thenAnswer(invocation -> new ArrayList<>(db.values()));

	    //Ejercicio para obtener todos los usuarios
	    List<User> users = service.findAllUsers();

	    //Comprobar que la lista contiene exactamente los usuarios configurados
	    assertThat(users.size(), is(2));
	    assertThat(users, containsInAnyOrder(user1, user2));
	}






	
	
}
