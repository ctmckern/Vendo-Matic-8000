package com.techelevator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/*This class was built around handling monetary transactions. It's basically the class that handles account
based things like change due.
 */
public class MoneyHandler {
    private static final DecimalFormat digits = new DecimalFormat("0.00");
    private double balance;
    private final double quarter = 25;
    private final double dime = 10;
    private final double nickel = 5;
    private double totalSales;


    public MoneyHandler(){
    }

    /*needChange first checks if there is any money in the account. If not then it returns the no change line.
    Otherwise it will then call the getChangeDue method which will create a StringBuilder and go through the
    steps to create the string. Using a string builder we reduce the number of strings created. Also is easier to
    test since we can only plug in so many numbers.
     */
    //The argument passed is confusing, it doesn't actually take balance automatically
    public String needChange(double balance){
        if (balance == 0.0){
            return System.lineSeparator()+"You have no change to return.";
        }
        if (balance < 0.0){
            return System.lineSeparator()+"Somehow your balance is negative, look into that.";
        }
        else return System.lineSeparator()+getChangeDue(balance);
    }

    private String getChangeDue (double coinChange) {
        boolean firstCoin = true;
        coinChange = coinChange*100;
        StringBuilder changeDue = new StringBuilder("");

        int quarters = 0;
        int dimes = 0;
        int nickels = 0;
        while (coinChange >= quarter) {
            quarters++;
            coinChange -= quarter;
        }
        while (coinChange >= dime) {
            dimes++;
            coinChange -= dime;
        }
        while (coinChange >= nickel) {
            nickels++;
            coinChange -= nickel;
        }
        if (quarters > 0){
            firstCoin = false;
        }
        setBalance(coinChange/100);
        changeDue.append(getSecondPrintableChange(quarters,"Quarters",true));
        changeDue.append(getSecondPrintableChange(dimes, "dimes", firstCoin));
        if (dimes > 0){
            firstCoin = false;
        }
        changeDue.append(getSecondPrintableChange(nickels, "nickels", firstCoin));

        return "Keep the change, you filthy animal: "+changeDue;
    }

    private String getSecondPrintableChange(int coins, String coinType, boolean first) {
        String coinPrintOut = "";
        if (first)
        {
            if (coins <= 0) {
                return "";
            }
            if (coins == 1) {
                coinType = coinType.substring(0, coinType.length() - 1);
            }
            coinPrintOut = coins + " " + coinType;
            return coinPrintOut;
        }
        else
        {
            if (coins <= 0) {
                return "";
            }
            if (coins == 1) {
                coinType = coinType.substring(0, coinType.length() - 1);
            }
            coinPrintOut = " and "+coins+" "+coinType;
        }
        return coinPrintOut;
    }
    //This and needChange will need tests.
    public boolean fundsAvailable(double cost){
        return !(this.balance - cost < 0);
    }

    public void subtractMoney(double cost){
        this.balance = (((balance*100) - (cost*100))/100);
        this.totalSales += cost;
    }
    public String getPrintableBalance(){
        return "$"+digits.format(this.balance);
    }
    public void setBalance(double balance){
        this.balance = balance;
    }

    public double getBalance(){
        return this.balance;
    }

    public String getPrintableTotalSales(){
        return "$"+digits.format(this.totalSales);
    }


    //This is just a main method to run spot checks
    public static void main(String[] args){
        MoneyHandler in = new MoneyHandler();
        System.out.println(in.getChangeDue(.68));
    }
}
