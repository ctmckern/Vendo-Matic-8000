package com.techelevator.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.techelevator.view.Menu;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MenuTest {

	private final String [] testSnack = new String[] {"A1","Potato Crisps","3.05","Chip","5"};
	private List<String []> testList = new ArrayList<>();
	private ByteArrayOutputStream output;

	@Before
	public void setup() {
		output = new ByteArrayOutputStream();
	}

	@Test
	public void displays_a_list_of_menu_options_and_prompts_user_to_make_a_choice() {
		Object[] options = new Object[] { Integer.valueOf(3), "Blind", "Mice" };
		Menu menu = getMenuForTesting();

		menu.getChoiceFromOptions(options);

		String expected = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";
		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void returns_object_corresponding_to_user_choice() {
		Integer expected = Integer.valueOf(456);
		Integer[] options = new Integer[] { Integer.valueOf(123), expected, Integer.valueOf(789) };
		Menu menu = getMenuForTestingWithUserInput("2" + System.lineSeparator());

		Integer result = (Integer) menu.getChoiceFromOptions(options);

		Assert.assertEquals(expected, result);
	}

	@Test
	public void redisplays_menu_if_user_does_not_choose_valid_option() {
		Object[] options = new Object[] { "Larry", "Curly", "Moe" };
		Menu menu = getMenuForTestingWithUserInput("4" + System.lineSeparator() + "1" + System.lineSeparator());

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator() + "*** 4 is not a valid option ***" + System.lineSeparator() + System.lineSeparator() + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void redisplays_menu_if_user_chooses_option_less_than_1() {
		Object[] options = new Object[] { "Larry", "Curly", "Moe" };
		Menu menu = getMenuForTestingWithUserInput("0" + System.lineSeparator() + "1" + System.lineSeparator());

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator() + "*** 0 is not a valid option ***" + System.lineSeparator() + System.lineSeparator() + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void redisplays_menu_if_user_enters_garbage() {
		Object[] options = new Object[] { "Larry", "Curly", "Moe" };
		Menu menu = getMenuForTestingWithUserInput("Mickey Mouse" + System.lineSeparator() + "1" + System.lineSeparator());

		menu.getChoiceFromOptions(options);

		String menuDisplay = System.lineSeparator() + "1) " + options[0].toString() + System.lineSeparator() + "2) " + options[1].toString() + System.lineSeparator() + "3) "
				+ options[2].toString() + System.lineSeparator() + System.lineSeparator() + "Please choose an option >>> ";

		String expected = menuDisplay + System.lineSeparator() + "*** Mickey Mouse is not a valid option ***" + System.lineSeparator() + System.lineSeparator() + menuDisplay;

		Assert.assertEquals(expected, output.toString());
	}

	@Test
	public void testAddMoneyWithValidInput(){
		Menu menu = getMenuForTestingWithUserInput("2" +System.lineSeparator());
		menu.addMoney();
		double expectedResult = 2.0;
		double actualResult = menu.getAccount().getBalance();
		Assert.assertEquals(expectedResult,actualResult, 0);
	}
	@Test
	public void testAddMoneyWithInvalidInput(){
		Menu menu = getMenuForTestingWithUserInput("3" + System.lineSeparator());
		menu.addMoney();
		double expectedResult = 0.0;
		double actualResult = menu.getAccount().getBalance();
		Assert.assertEquals(expectedResult,actualResult,0);
	}
	@Test
	public void testAddMoneyWithInvalidFormat(){
		Menu menu = getMenuForTestingWithUserInput("two" + System.lineSeparator());
		menu.addMoney();
		double expectedResult = 0.0;
		double actualResult = menu.getAccount().getBalance();
		Assert.assertEquals(expectedResult, actualResult, 0);
	}
	@Test
	public void testGetSnack(){
		Menu menu = getMenuForTestingWithUserInput("A1");
		String[] testSnack2 = new String[]{"C1", "Cola",".25", "Drink", "5"};
		testList.add(testSnack);
		testList.add(testSnack2);
		Assert.assertArrayEquals(testSnack, menu.getSnack(testList));
	}
	@Test
	public void testGetSnackChecksForInvalidInput(){
		Menu menu = getMenuForTestingWithUserInput("1");
		String[] testSnack2 = new String[]{"C1", "Cola",".25", "Drink", "5"};
		testList.add(testSnack);
		testList.add(testSnack2);
		Assert.assertArrayEquals(null, menu.getSnack(testList));
	}

	@Test
	public void testMakeFileCreatesAFile(){
		Menu menu = getMenuForTesting();
		menu.makeFile();
		File testFile = new File("src/AuditLog.txt");
		Assert.assertTrue("File wasn't made.", testFile.exists());
	}

	@Test
	public void testMakeFileLeavesAnEmptyFile(){
		Menu menu = getMenuForTesting();
		menu.makeFile();
		File testFile = new File("src/AuditLog.txt");
		Assert.assertEquals(0, testFile.length());
	}

	@Test
	public void testVerifyChoiceWorksWithCorrectArray(){
		Menu menu = getMenuForTesting();
		Assert.assertTrue(menu.verifyChoice(testSnack));
	}

	@Test
	public void testVerifyChoiceChecksForNull(){
		Menu menu = getMenuForTesting();
		String message = "Method should return false if array is null.";
		Assert.assertFalse(message, menu.verifyChoice(null));
	}

	@Test
	public void testVerifyChoiceChecksForArraysThatWouldGenerateIndexOutOfBoundsErrors(){
		Menu menu = getMenuForTesting();
		String [] faultyArray = new String[4];
		String message = "Method allowed an array that would generate errors further on.";
		Assert.assertFalse(message, menu.verifyChoice(faultyArray));
	}

	@Test
	public void testDispenseSnackAdjustsAccount(){
		Menu menu = getMenuForTesting();
		menu.getAccount().setBalance(5.0);
		double expectedResult = 1.95;
		menu.dispenseSnack(testSnack);
		double actualResult = menu.getAccount().getBalance();
		Assert.assertEquals(expectedResult, actualResult, 0);
	}

	@Test
	public void testDispenseSnackWithInvalidArray(){
		Menu menu = getMenuForTesting();
		menu.getAccount().setBalance(5.0);
		double expectedResult = 5.0;
		menu.dispenseSnack(null);
		double actualResult = menu.getAccount().getBalance();
		Assert.assertEquals(expectedResult, actualResult, 0);
	}

	@Test
	public void testWriteToSalesReport() throws FileNotFoundException {
		Menu menu = getMenuForTesting();
		menu.getStock().makeInventory();
		menu.writeToSalesReport();
		String expectedResult = "Potato Crisps       |0";
		String actualResult = "";
		File file = new File("src/SalesReport.txt");
		try(Scanner reader = new Scanner(file)){
			if(reader.hasNextLine()){
				actualResult = reader.nextLine();
			}
		}
		Assert.assertEquals(expectedResult,actualResult);
	}

	@Test
	public void testWriteToSalesReportTracksASale() throws FileNotFoundException {
		Menu menu = getMenuForTesting();
		menu.getStock().makeInventory();
		menu.getStock().reduceStock(menu.getStock().getInventory().get(0));
		menu.writeToSalesReport();
		String expectedResult = "Potato Crisps       |1";
		String actualResult = "";
		File file = new File("src/SalesReport.txt");
		try(Scanner reader = new Scanner(file)){
			if(reader.hasNextLine()){
				actualResult = reader.nextLine();
			}
		}
		Assert.assertEquals(expectedResult,actualResult);
	}

	private Menu getMenuForTestingWithUserInput(String userInput) {
		ByteArrayInputStream input = new ByteArrayInputStream(String.valueOf(userInput).getBytes());
		return new Menu(input, output);
	}

	private Menu getMenuForTesting() {
		return getMenuForTestingWithUserInput("1" + System.lineSeparator());
	}
}
