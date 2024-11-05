package com.mayab.quality.loginunittest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.doubles.Dependency;
import com.mayab.quality.doubles.SubDependency;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class LoginServiceTest {
	private User user;
	private LoginService logService;
	private IDAOUser dao;

	@BeforeEach
	void setUp() throws Exception{
		IDAOUser dao = mock(IDAOUser.class);
		User user = mock(User.class);
		LoginService logService = new LoginService(dao);
	}
	@Test
	void loginTest() {
		
		when(user.getPassword()).thenReturn("123");
		when(dao.findByUserName("name")).thenReturn(user);
		
		
		//Exercise
		boolean res= logService.login("name", "123");
		
		
		assertThat(res, is(true));
		
	}

}
