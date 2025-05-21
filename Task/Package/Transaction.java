package Task.Package;

import java.time.LocalDate;

public class Transaction {
	String type;
    String category;
    String subcategory;
    double amount;
    LocalDate date;

    public Transaction(String type, String category, String subcategory, double amount, LocalDate date) {
        this.type = type;
        this.category = category;
        this.subcategory = subcategory;
        this.amount = amount;
        this.date = date;
    }

    public String toCSV() {
        return type + "," + category + "," + subcategory + "," + amount + "," + date;
    }
}

