package com.mayab.quality.functional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class FacebookTest {

    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void test() {
        // Exercise
       
        driver.get("https://www.facebook.com/");
        pause(5000);
        
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("puppies");
        
        driver.findElement(By.id("pass")).clear();
        driver.findElement(By.id("pass")).sendKeys("password");

        driver.findElement(By.name("login")).click();
        
        pause(3000);

        String actualResult = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div[2]/div[2]/form/div/div[1]/div[2]/a")).getText();
        
        //Comparación 
        System.out.println(actualResult);
        assertThat(actualResult, is("Encuentra tu cuenta e inicia sesión."));
    
    }
    private void pause(long mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
