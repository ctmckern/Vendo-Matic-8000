package com.techelevator;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import com.techelevator.Inventory;

public class InventoryTest {

    Inventory stock = new Inventory();
    private final String [] testSnack = new String[] {"A1","Potato Crisps","3.05","Chip","5"};
    private List<String []> testList = new ArrayList<>();

    @Test
    public void testMakeInventorySetsAmountToFive() {
        stock.makeInventory();
        String expectedResult = "5";
        String actualResult = stock.getInventory().get(0)[4];
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMakeItPretty() {
        String actualResult = stock.makeItPretty(0, testSnack[1]);
        String expectedResult = "Potato Crisps       ";
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testMakeItPrettyWillCatchInvalidChoice(){
        String actualResult = stock.makeItPretty(72, testSnack[2]);
        String expectedResult = testSnack[2]+"...What were you hoping for?";
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCheckAvailability() {
        Assert.assertTrue("Method returned false when amount more than 0", stock.checkAvailability(testSnack));
    }

    @Test
    public void testCheckAvailabilityWithZero(){
        Assert.assertTrue("Returned true when amount is 0", stock.checkAvailability(testSnack));
    }

    @Test
    public void testReduceStock() {
        stock.makeInventory();
        stock.reduceStock(stock.getInventory().get(0));
        String expectedResult = "4";
        String actualResult = stock.getInventory().get(0)[4];
        Assert.assertEquals(expectedResult, actualResult);
    }
}