package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/*This class is based around the stock/inventory of the machine. It makes a list of arrays from the file and uses
that as the stock. This class is where things like reducing inventory and checking availability happens.
 */

//Currently have four public methods. Need a test for them.
public class Inventory {
    private File vending = new File("../capstone/vendingmachine.csv");
    private List<String []> inventory = new ArrayList<>();
    private List<String []> reportInventory = new ArrayList<>();


    public Inventory(){
    }
    public List<String []> getInventory(){
        return this.inventory;
    }

    public List<String []> getReportInventory(){
        return this.reportInventory;
        }

    public void makeInventory(){
        try{Scanner read = new Scanner(vending);
            while(read.hasNextLine()){
                String line = read.nextLine();
                String [] snack = line.split("\\|");
                inventory.add(snack);
            }
            makeReportInventory(inventory);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void makeReportInventory(List<String []> inventory){
        if(reportInventory.isEmpty()) {
            if (inventory.isEmpty()) {
                System.out.println("Issue trying to make the inventory.");
            } else for (int i = 0; i < inventory.size(); i++) {
                String[] temporary = new String[]{inventory.get(i)[1], "0"};
                reportInventory.add(temporary);
            }
        }
    }
    public String makeItPretty(int choice, String contents) {
        //This is mainly meant to create a pretty format for report inventory.
        //However, it is designed in a way we could add in all sorts of choices.
        if(choice == 0){
            return prettyFoodName(contents);
        }
        //This line should never print but if it does it means the wrong choice was passed.
        else return contents + "...What were you hoping for?";
    }
    //A simple method to print out a formatted inventory. If you want to style the inventory list
    //Check out the pretty methods.
    public void printInventory(int selection, String balance){
            for (int i = 0; i < inventory.size(); i++) {
                String foodSlot = prettyFoodSlot(inventory.get(i)[0]);
                String foodName = prettyFoodName(inventory.get(i)[1]);
                String foodCost = prettyFoodCost(inventory.get(i)[2]);
                String foodRemaining;
                if (Objects.equals(inventory.get(i)[4], "0")) {
                    foodRemaining = "Out of Stock";
                } else foodRemaining = inventory.get(i)[4] + " remaining";
                System.out.println(foodSlot + "|" + foodName + "|" + foodCost + "| " + foodRemaining);
            }
            //This line enables you to decide whether or not printing the inventory asks for a user prompt
        //and prints out balance or if it just prints the full inventory to look at.
        System.out.print(printInventoryFromPurchase(selection, balance));
        }

    private String printInventoryFromPurchase(int selection, String balance){
        if(selection == 0){
            return "";
        }
        String provided = System.lineSeparator()+ "Current Money Provided: " + balance;
        String prompt = System.lineSeparator()+"Please select your product >>> ";
        return provided+prompt;
    }
    /*The following three methods are what I use to print a pretty inventory. Note that they get called
    from printInventory. This way the functional List doesn't get altered while maintaining a pretty output.
    Lastly I used Stringbuilder so it's less work appending to it instead of creating new Strings for each
    concatenation.
     */
    private String prettyFoodName(String string){
        StringBuilder foodName = new StringBuilder(string);
        int suggestedLength = 20;
        while (foodName.length() <suggestedLength){
            foodName.append(" ");
        }
        return foodName.toString();
    }
    private String prettyFoodCost(String string){
        StringBuilder foodCost = new StringBuilder(string);
        foodCost.insert(0, "$");
        foodCost.insert(0, " ");
        int suggestedLength = 7;
        while (foodCost.length() <suggestedLength){
            foodCost.append(" ");
        }
        return foodCost.toString();
    }
    private String prettyFoodSlot(String string){
        StringBuilder foodSlot = new StringBuilder(string);
        foodSlot.append(" ");
        return foodSlot.toString();
    }


    public boolean checkAvailability(String [] snack){
        return !snack[4].equalsIgnoreCase("0");
    }

    public void reduceStock(String [] selectedSnack){
        for(String[] availableSnacks : this.inventory) {
            if(availableSnacks == selectedSnack) {
                String snackBeforePurchase = selectedSnack[4];
                if(Objects.equals(snackBeforePurchase, "0"))
                {
                    break;
                }
                Integer quantity = Integer.parseInt(snackBeforePurchase);
                quantity--;
                String updatedQuantity = quantity.toString();
                availableSnacks[4] = updatedQuantity;
                reportSale(availableSnacks);
                break;
            }
        }
    }

    private void reportSale(String [] selectedSnack){
        /*The array snack is the array from inventory so the snacks name is index 1 or snackFromInventory[1]
        snacks[0] should be the name of the snack. snacks[1] should be the number sold.
         */
        for(String[] snacks : this.reportInventory){
            if(Objects.equals(snacks[0], selectedSnack[1])){
                String snacksSold = snacks[1];
                Integer numberSold = Integer.parseInt(snacksSold);
                numberSold++;
                String updatedNumberSold = numberSold.toString();
                snacks[1] = updatedNumberSold;
                break;
            }
        }
    }



    public static void main(String[] args){
        Inventory in = new Inventory();
        in.makeInventory();
        final String [] testSnack = new String[] {"A1","Potato Crisps","3.05","Chip","5"};
        System.out.println(Arrays.toString(in.getInventory().get(0)));
        System.out.println(Arrays.toString(testSnack));

    }
}
