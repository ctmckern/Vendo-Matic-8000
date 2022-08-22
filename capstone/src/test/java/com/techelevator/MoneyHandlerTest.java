package com.techelevator;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Assert;

public class MoneyHandlerTest {
    MoneyHandler inventory = new MoneyHandler();

    @Test
    public void testFundsAvailableWhenBalanceLessThanCost() {
        inventory.setBalance(5);
        boolean expectedValue = false;
        boolean actualValue = inventory.fundsAvailable(6);
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testFundsAvailableWhenBalanceMoreThanCost(){
        inventory.setBalance(5);
        boolean expectedValue = true;
        boolean actualValue = inventory.fundsAvailable(4);
        Assert.assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testFundsAvailableWhenBalanceEqualsCost(){
        inventory.setBalance(4);
        boolean expectedValue = true;
        boolean actualValue = inventory.fundsAvailable(4);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testNeedChangeWithZeroBalance() {
        String expectedResult = System.lineSeparator()+"You have no change to return.";
        String actualResult = inventory.needChange(0);
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testNeedChangeWithOnlyQuarters(){
        String expectedResult = System.lineSeparator()+"Keep the change, you filthy animal: 4 Quarters";
        String actualResult = inventory.needChange(1);
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testNeedChangeWithQuartersAndDimes(){
        String expectedResult = System.lineSeparator()+"Keep the change, you filthy animal: 4 Quarters and 2 dimes";
        String actualResult = inventory.needChange(1.20);
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testNeedChangeWithDimesAndNickels(){
        String expectedResult = System.lineSeparator()+"Keep the change, you filthy animal: 1 dime and 1 nickel";
        String actualResult = inventory.needChange(.15);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNeedChangeWithQuartersAndNickels(){
        String expectedResult = System.lineSeparator()+"Keep the change, you filthy animal: 1 Quarter and 1 nickel";
        String actualResult = inventory.needChange(.30);
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testNeedChangeWithOnlyNickels(){
        String expectedResult = System.lineSeparator()+"Keep the change, you filthy animal: 1 nickel";
        String actualResult = inventory.needChange(.05);
        Assert.assertEquals(expectedResult,actualResult);
    }
    @Test
    public void testNeedChangeWillSetBalanceToZero(){
        inventory.needChange(1);
        Assert.assertEquals(0, inventory.getBalance(), 0);
    }
    @Test
    public void testNeedChangeWontIgnorePennies(){
        inventory.needChange(1.04);
        Assert.assertEquals(0.04, inventory.getBalance(), 0);
    }
    @Test
    public void testNeedChangeWillAccountForNegativeBalance(){
        String expectedResult = System.lineSeparator()+"Somehow your balance is negative, look into that.";
        String actualResult = inventory.needChange(-.25);
        Assert.assertEquals(expectedResult,actualResult);
    }
    @Test
    public void testNeedChangeWontChangeBalanceIfBalanceIsNegative(){
        inventory.setBalance(-.25);
        inventory.needChange(-.25);
        Assert.assertEquals(-.25,inventory.getBalance(), 0);
    }

}