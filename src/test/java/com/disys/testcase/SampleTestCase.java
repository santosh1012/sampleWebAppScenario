package com.disys.testcase;

import base.BaseClass;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class SampleTestCase extends BaseClass {


    @Test
    public void loginIntoApplication(){
        driver.findElement(By.id("userEmail")).sendKeys("gupta.santosh.1012@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("Password@123");
        driver.findElement(By.id("login")).click();
        driver.findElement(By.xpath("//button[text()=' Sign Out ']")).click();

    }
}
