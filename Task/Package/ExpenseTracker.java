package Task.Package;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExpenseTracker {
    static Scanner sc = new Scanner(System.in);
    static List<Transaction> transactions = new ArrayList<>();
    static final String FILE_NAME = "C:\\Users\\kunre\\eclipse-workspace\\Task\\transactions.csv";

    public static void main(String[] args) {
        loadFromFile(FILE_NAME);

        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load from file");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addTransaction("income");
                    break;
                case 2:
                    addTransaction("expense");
                    break;
                case 3:
                    viewMonthlySummary();
                    break;
                case 4:
                    loadFromFile(FILE_NAME);
                    break;
                case 5:
                    saveToFile(FILE_NAME);
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void addTransaction(String type) {
        System.out.println("\nEnter main category (e.g., salary, food, travel): ");
        String category = sc.nextLine();

        System.out.print("Enter subcategory (e.g., bonus, groceries, taxi): ");
        String subcategory = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Try again.");
            return;
        }

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date;
        try {
            date = LocalDate.parse(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        Transaction t = new Transaction(type, category, subcategory, amount, date);
        transactions.add(t);
        saveToFile(FILE_NAME);
        System.out.println(type + " added successfully.");
    }

    public static void viewMonthlySummary() {
        System.out.print("Enter month (YYYY-MM): ");
        String inputMonth = sc.nextLine();

        double incomeTotal = 0;
        double expenseTotal = 0;

        for (Transaction t : transactions) {
            String month = t.date.toString().substring(0, 7);
            if (month.equals(inputMonth)) {
                if (t.type.equals("income")) {
                    incomeTotal += t.amount;
                } else if (t.type.equals("expense")) {
                    expenseTotal += t.amount;
                }
            }
        }

        System.out.println("\nSummary for " + inputMonth);
        System.out.println("Total Income: $" + incomeTotal);
        System.out.println("Total Expenses: $" + expenseTotal);
        System.out.println("Balance: $" + (incomeTotal - expenseTotal));
    }

    public static void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No existing file found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            transactions.clear();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("type")) continue;
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    transactions.add(new Transaction(
                            parts[0],
                            parts[1],
                            parts[2],
                            Double.parseDouble(parts[3]),
                            LocalDate.parse(parts[4])
                    ));
                }
            }
            System.out.println("File loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public static void saveToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
        	 
            bw.write("type,category,subcategory,amount,date\n");
            for (Transaction t : transactions) {
                bw.write(t.toCSV());
                bw.newLine();
            }
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}