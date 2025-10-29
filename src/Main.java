import java.io.*;
import java.util.*;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Household> households = new HashMap<>();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println("\n=== Eco-Points Recycling Tracker ===");
            System.out.println("1. Register Household");
            System.out.println("2. Log Recycling Event");
            System.out.println("3. Display Households");
            System.out.println("4. Display Household Recycling Events");
            System.out.println("5. Generate Reports");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerHousehold();
                    break;
                case "2":
                    logRecyclingEvent();
                    break;
                case "3":
                    displayHouseholds();
                    break;
                case "4":
                    displayHouseholdEvents();
                    break;
                case "5":
                    generateReports();
                    break;
                case "6":
                    saveHouseholdsToFile();
                    running = false;
                    System.out.println("Data saved. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }

        }

    }

    public static void registerHousehold() {

        System.out.println("Enter Household ID: ");
        String householdID = scanner.nextLine().trim();

        if(households.containsKey(householdID)) {
            System.out.println("Household ID is already in use");
            return;
        }

        System.out.println("Enter Household Name: ");
        String householdName = scanner.nextLine().trim();

        System.out.println("Enter Household Address: ");
        String householdAddress = scanner.nextLine().trim();

        Household household = new Household(householdID, householdName, householdAddress);
        households.put(householdID, household);

        System.out.println("Household registered successfully on " + household.getAddress());

    }

    public static void logRecyclingEvent() {
        System.out.println("Enter Household ID: ");
        String householdID = scanner.nextLine().trim();

        Household household = households.get(householdID);

        if(household == null) {
            System.out.println("Household does not exist");
            return;
        }

        System.out.println("Enter material type (plastic/glass/metal/paper): ");
        String materialType = scanner.nextLine().trim();

        double weight = 0.0;

        while (true){
            try{
                System.out.println("Enter weight ing kg");
                weight = Double.parseDouble(scanner.nextLine().trim());

                if(weight <= 0){ throw new IllegalArgumentException("Weight must be a positive number"); }
                break;
            } catch(IllegalArgumentException e){
                System.out.println("Weight must be a positive number");
            }
        }

        RecyclingEvent event = new RecyclingEvent(materialType, weight);
        household.addEvent(event);

        System.out.println("Event logged! Total points earned: " + event.getEcoPoints() );

    }

    private static void displayHouseholds() {
        if (households.isEmpty()) {
            System.out.println("No households registered.");
            return;
        }
        System.out.println("\nRegistered Households:");
        for (Household h : households.values()) {
            System.out.println("ID: " + h.getId() +
                    ", Name: " + h.getName() +
                    ", Address: " + h.getAddress() +
                    ", Joined: " + h.getJoinDate());
        }
    }

    private static void displayHouseholdEvents() {
        System.out.print("Enter household ID: ");
        String id = scanner.nextLine().trim();

        Household household = households.get(id);

        if (household == null) {
            System.out.println("Household not found.");
            return;
        }

        System.out.println("\nRecycling Events for " + household.getName() + ":");

        if (household.getEvents().isEmpty()) {
            System.out.println("No events logged.");
        } else {
            for (RecyclingEvent e : household.getEvents()) {
                System.out.println(e);
            }

            System.out.println("Total Weight: " + household.getTotalWeight() + " kg");

            System.out.println("Total Points: " + household.getTotalPoints() + " pts");
        }
    }

    private static void generateReports() {
        if (households.isEmpty()) {
            System.out.println("No households registered.");
            return;
        }

        Household top = null; // Start with no top household
        for (Household h : households.values()) {
            if (top == null || h.getTotalPoints() > top.getTotalPoints()) {
                top = h;
            }
        }

        System.out.println("\nHousehold with Highest Points:");
        System.out.println("ID: " + top.getId() +
                ", Name: " + top.getName() +
                ", Points: " + top.getTotalPoints());

        double totalWeight = 0.0;

        for (Household h : households.values()) {
            totalWeight += h.getTotalWeight();
        }

        System.out.println("Total Community Recycling Weight: " + totalWeight + " kg");
    }

    private static void saveHouseholdsToFile() {
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("households.ser"));
            out.writeObject(households);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadHouseholdsFromFile() {

        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("households.ser"));
            households = (Map<String, Household>) in.readObject();
            System.out.println("Households loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No households file found.");
        }  catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }

    }

}