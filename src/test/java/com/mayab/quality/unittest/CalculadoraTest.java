package com.mayab.quality.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CalculadoraTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}
	@Test
	void test2() {
		System.out.println("Test 2");
	}
	@Test
	void testSuma() {
		Calculadora cal= new Calculadora();
		int num1= 10;
		int num2=5;
		double expRes =15;
		
		double res=cal.suma(num1, num2);
		
		assertEquals(res, expRes);
	}

}
