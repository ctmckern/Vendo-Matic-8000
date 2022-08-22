package com.techelevator;

import com.techelevator.view.Menu;

import java.io.IOException;

public class VendingMachineCLI {
	/* This is the class where our project basically occurs. When we want to see how we're doing we run this one.
	There's only one method here called run.
	 */

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT};

	private static final String PURCHASE_STEPS_FEED = "Feed Money";
	private static final String PURCHASE_STEPS_SELECT = "Select Product";
	private static final String PURCHASE_STEPS_FINISH = "Finish Transaction";
	private static final String[] PURCHASE_STEPS_OPTIONS = { PURCHASE_STEPS_FEED, PURCHASE_STEPS_SELECT, PURCHASE_STEPS_FINISH};

	private Menu menu;

	//consider adding custom exception messages.
	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
		menu.getStock().makeInventory();
		menu.makeFile();
		menu.makeSalesReport();
	}

	public void run() throws IOException {
		int selection = 0;
		boolean running = true;
		String priorBalance = "";
		while (running) {
			label:
			while (selection == 0) {
				String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

				switch (choice) {
					case MAIN_MENU_OPTION_DISPLAY_ITEMS:
						menu.getStock().printInventory(selection, menu.getAccount().getPrintableBalance());
						// display vending machine items
						break;
					case MAIN_MENU_OPTION_PURCHASE:
						// do purchase
						selection = 2;
						break;
					case MAIN_MENU_OPTION_EXIT:
						System.out.println("Enjoy!");
						running = false;
						break label;
					case MAIN_MENU_OPTION_SALES_REPORT:
						System.out.println("Printing sales report.");
						menu.writeToSalesReport();
						break;
				}
			}
			while (selection == 2) {
				//This is the Transaction menu, displays feed money, purchase, and finish
				String choice = (String) menu.getChoiceFromPurchaseMenu(PURCHASE_STEPS_OPTIONS);
				if (choice.equals(PURCHASE_STEPS_FEED)) {
					priorBalance = menu.getAccount().getPrintableBalance();
					menu.addMoney();
					menu.writeToAuditLog("FEED MONEY:", priorBalance);
				}
				if (choice.equals(PURCHASE_STEPS_SELECT)) {
					menu.getStock().printInventory(selection, menu.getAccount().getPrintableBalance());
					String[] snacks = menu.getSnack(menu.getStock().getInventory());
					if (menu.verifyChoice(snacks)) {
						priorBalance = menu.getAccount().getPrintableBalance();
						menu.dispenseSnack(snacks);
						menu.writeToAuditLog(snacks[1] + " " + snacks[0], priorBalance);
					}
				}
				if (choice.equals(PURCHASE_STEPS_FINISH)) {
					priorBalance = menu.getAccount().getPrintableBalance();
					System.out.println(menu.getAccount().needChange(menu.getAccount().getBalance()));
					menu.writeToAuditLog("GIVE CHANGE:", priorBalance);
					selection = 0;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
