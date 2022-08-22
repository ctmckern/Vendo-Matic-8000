package com.techelevator.view;

import com.techelevator.Inventory;
import com.techelevator.MoneyHandler;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Menu {

	private PrintWriter out;
	private Scanner in;
	private final int password = 6819;
	private final MoneyHandler account = new MoneyHandler();
	private final Inventory stock = new Inventory();
	private final Date date = new Date();
	private final SimpleDateFormat auditFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
	private final File auditLog = new File("src/AuditLog.txt");
	private File salesReport = new File("src/SalesReport.txt");
/*This class is where the bulk of things happens. I noticed it had a scanner already made so in order to avoid
having multiple scanners open I had the inventory and moneyhandler classes go through this menu class.
https://www.upgrad.com/blog/what-is-composition-in-java-with-examples/ this should go over it.
 */

	public Inventory getStock() {
		return this.stock;
	}

	public MoneyHandler getAccount() {
		return this.account;
	}

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.parseInt(userInput);
			if (selectedOption == password) {
					choice = options[options.length - 1];
			}
			if (selectedOption > 0 && selectedOption <= options.length - 1) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			/*this is a loop to go through the String[] MAIN_MENU_OPTIONS of vendingmachine
			 */
			int optionNum = i + 1;
			if (optionNum == 4) {
				continue;
			}
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public void addMoney() {
		out.print(System.lineSeparator() + "Using bills how much would you like to add? ");
		out.flush();
		String input = in.nextLine();
		double moneyAdded = 0.0;
		try {
			double moneyToAdd = Double.parseDouble(input);
			if ((moneyToAdd == 1 || moneyToAdd == 2) || moneyToAdd == 5 || moneyToAdd == 10) {
				moneyAdded = moneyToAdd;
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (moneyAdded == 0.0) {
			out.println(System.lineSeparator() + "*** There is no " + input + " dollar bill! ***" + System.lineSeparator());
		}
		account.setBalance(account.getBalance() + moneyAdded);
	}

	//These methods are for the purchase menu section of the run method.
	public Object getChoiceFromPurchaseMenu(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayPurchaseMenu(options);
			choice = getUserInputForPurchaseMenu(options);
		}
		return choice;
	}

	private void displayPurchaseMenu(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Current Money Provided: " + account.getPrintableBalance());
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	private Object getUserInputForPurchaseMenu(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.parseInt(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	public String[] getSnack(List<String[]> choices) {
		String[] choice = null;
		String chosen = in.nextLine();
		try {
			for (String[] string : choices) {
				if (chosen.equalsIgnoreCase(string[0])) {
					choice = string;
					break;
				}
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + chosen + " is not a valid option ***" + System.lineSeparator());
		}
		//If they're choice isn't valid the verify choice method will recieve this null, and skip the rest of
		//the block, letting us loop back around to the menu display
		return choice;
	}

	/* Just a means to prevent null pointer exceptions in the run method.
	If your snack choice is invalid it skips over the block where it would try and dispense snack
	 */
	public boolean verifyChoice(String[] choice) {
		if(!(choice == null)) {
			return choice.length >= 5;
		}
		else out.print(System.lineSeparator() + "Incorrect array to verify.");
		return false;
	}

	private String printForSnackType(String snackType) {
		if (snackType.equalsIgnoreCase("Chip")) {
			return "\n Crunch Crunch, Yum!";
		}
		if (snackType.equalsIgnoreCase("Candy")) {
			return "\n Munch Munch, Yum!";
		}
		if (snackType.equalsIgnoreCase("Drink")) {
			return "\n Glug Glug, Yum!";
		}
		if (snackType.equalsIgnoreCase("Gum")) {
			return "\n Chew Chew, Yum!";
		}
		//This line should never print but if it does it means either the line was edited in the file,
		//a new snack was added with a different type, or that the method dispenseSnack is referring to the
		//wrong spot in the array.
		else return "That's a weird snack";
	}

	public void dispenseSnack(String[] snack) {
		if(verifyChoice(snack)) {
			String snackName = snack[1];
			String writtenCost = snack[2];
			String snackType = snack[3];
			double cost = Double.parseDouble(writtenCost);
			if (!stock.checkAvailability(snack)) {
				System.out.println("\n Item is unfortunately out of stock, would you like something else?");
				out.flush();
				return;
			}
			if (account.fundsAvailable(cost)) {
				//Add adjust totalSales inside adjustAccount.
				account.subtractMoney(cost);
				String snackSpecificMessage = printForSnackType(snackType);
				out.print(System.lineSeparator() + snackName);
				out.print(System.lineSeparator() + "This item costs: $" + writtenCost);
				out.print(System.lineSeparator() + "You're remaining balance is now: " + account.getPrintableBalance());
				out.println(System.lineSeparator() + snackSpecificMessage);
				stock.reduceStock(snack);
				out.flush();
			} else System.out.println("\n *** Insufficient funds, please add more money. ***");
			out.flush();
		}
	}

	public void makeFile() {
		try {
			if (auditLog.exists()) {
				auditLog.delete();
			}
			auditLog.createNewFile();
		} catch (IOException ex) {
			ex.getMessage();
		}
	}
	//Is redundant, make test later
	public void makeSalesReport() {
		try {
			if (salesReport.exists()) {
				salesReport.delete();
			}
			salesReport.createNewFile();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	//Try adding filedescriptor to this too. Would be neat
	public void writeToAuditLog(String choice, String balanceBeforeTransaction) throws IOException {
		try (PrintWriter auditor = new PrintWriter((new FileOutputStream(auditLog, true)))) {
			String timeOfAudit = auditFormat.format(date);
			auditor.println(">" + timeOfAudit + " " + choice + " $" + balanceBeforeTransaction + " " + account.getPrintableBalance());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//https://docs.oracle.com/javase/7/docs/api/java/io/FileDescriptor.html this uses the filedescriptor
	//to enable its .synch method, using this we can allow the sales report to be viewable as soon as it's
	//written and not wait until we close the program

	public void writeToSalesReport() throws FileNotFoundException {
		try (FileOutputStream out = new FileOutputStream(salesReport);
			 PrintWriter salesRecorder = new PrintWriter(out)) {
			FileDescriptor fd = out.getFD();
			for (String[] snacks : stock.getReportInventory()) {
				salesRecorder.println(stock.makeItPretty(0, snacks[0]) + "|" + snacks[1]);
				out.flush();
			}
			salesRecorder.println(System.lineSeparator() + "Total sales: " + account.getPrintableTotalSales());
			salesRecorder.println(System.lineSeparator() + "Time of report: " + auditFormat.format(date));
			out.flush();
			fd.sync();
		} catch (IOException e) {
			e.getMessage();
		}
	}
}