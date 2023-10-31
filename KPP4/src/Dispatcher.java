import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator;

class Employee implements Serializable {
    String passportSeries;
    String passportNumber;
    BigDecimal salary;
//    List<CareerEntry> career;
    List<Characteristic> characteristics;

    public Employee(String passportSeries, String passportNumber, BigDecimal salary) {
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.salary = salary;
//        this.career = new ArrayList<>();
        this.characteristics = new ArrayList<>();
    }

    public void addCharacteristic(String property, double rating) {
        characteristics.add(new Characteristic(property, rating));
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    @Override
    public String toString() {
        return "Passport: " + passportSeries + "-" + passportNumber + ", Salary: " + salary;
    }
//    public void displayCareer() {
//        for (CareerEntry entry : career) {
//            System.out.println("Appointment Date: " + entry.appointmentDate +
//                    ", Position: " + entry.position +
//                    ", Department: " + entry.department);
//        }
//    }
}
    

//class CareerEntry implements Serializable {
//    Date appointmentDate;
//    String position;
//    String department;
//
//    public CareerEntry(Date appointmentDate, String position, String department) {
//        this.appointmentDate = appointmentDate;
//        this.position = position;
//        this.department = department;
//    }
//}

class Characteristic implements Serializable {
    private String property;
    private double rating;

    public Characteristic(String property, double rating) {
        this.property = property;
        this.rating = rating;
    }

    public String getProperty() {
        return property;
    }

    public double getRating() {
        return rating;
    }
}

class EmployeeContainer<T extends Employee> implements Iterable<T>, Serializable {
    private List<T> employees = new ArrayList<>();
    
    public List<T> sortEmployeesByPassport() {
        List<T> sortedList = new ArrayList<>(employees);
        sortedList.sort(Comparator.comparing(e -> e.passportSeries + e.passportNumber));
        return sortedList;
    }

    public List<T> sortEmployeesBySalary() {
        List<T> sortedList = new ArrayList<>(employees);
        sortedList.sort(Comparator.comparing(e -> e.salary));
        return sortedList;
    }

    public void addEmployee(T employee) {
        employees.add(employee);
    }

    public void serialize(String fileName) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Employee> EmployeeContainer<T> deserialize(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            return (EmployeeContainer<T>) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return employees.iterator();
    }
    
}

public class Dispatcher {
	public static void main(String[] args) {
        EmployeeContainer<Employee> container = new EmployeeContainer<>();
        Scanner scanner = new Scanner(System.in);
        
        boolean isAutoMode = false;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("auto")) {
                isAutoMode = true;
                break;
            }
        }

        if (isAutoMode) {
        	autoAddAndSaveData(container);
        }else {
	        while (true) {
	            System.out.println("Choose an action:");
	            System.out.println("1. Display employees");
	            System.out.println("2. Add employee");
	            System.out.println("3. Sort employees by passport");
	            System.out.println("4. Sort employees by salary");
	            System.out.println("5. Serialize or deserialize container");
	            System.out.println("6. Exit");
	
	            String choice = scanner.nextLine();
	
	            switch (choice) {
	                case "1":
	                    displayEmployees(container);
	                    break;
	                case "2":
	                    addEmployee(container, scanner);
	                    break;
	                case "3":
	                    displaySortedEmployees(container.sortEmployeesByPassport());
	                    break;
	                case "4":
	                    displaySortedEmployees(container.sortEmployeesBySalary());
	                    break;
	                case "5":
	                    System.out.print("Enter the file name for serialization or deserialization: ");
	                    String fileName = scanner.nextLine();
	                    if (new File(fileName).exists()) {
	                        container = EmployeeContainer.deserialize(fileName);
	                        System.out.println("Data loaded from file '" + fileName + "'.");
	                    } else {
	                        container.serialize(fileName);
	                        System.out.println("Data saved to file '" + fileName + "'.");
	                    }
	                    break;
	                case "6":
	                    System.exit(0);
	                    break;
	                default:
	                    System.out.println("Invalid choice. Please try again.");
	                    break;
	            }
	        }
        }
    }
	
	public static void autoAddAndSaveData(EmployeeContainer<Employee> container) {
	    // Створюємо нового працівника
	    Employee newEmployee = new Employee("XYZ", "98765", new BigDecimal("60000.00"));

	    // Додаємо характеристики (це лише приклад, можна додати більше характеристик)
	    newEmployee.addCharacteristic("Experience", 5.5);
	    

	    // Додаємо працівника до контейнера
	    container.addEmployee(newEmployee);

	    container.serialize("test");
	    
	    System.out.println("Data added and saved to 'test' file.");
	}
	
	private static void addEmployee(EmployeeContainer<Employee> container, Scanner scanner) {
	    System.out.print("Enter passport series: ");
	    String passportSeries = scanner.nextLine();

	    System.out.print("Enter passport number: ");
	    String passportNumber = scanner.nextLine();

	    System.out.print("Enter salary: ");
	    BigDecimal salary = new BigDecimal(scanner.nextLine());

	    Employee newEmployee = new Employee(passportSeries, passportNumber, salary);

	    while (true) {
	        System.out.print("Add a characteristic (Y/N): ");
	        String choice = scanner.nextLine().trim().toLowerCase();

	        if (choice.equals("y")) {
	            System.out.print("Enter characteristic property: ");
	            String property = scanner.nextLine();

	            System.out.print("Enter characteristic rating: ");
	            double rating = Double.parseDouble(scanner.nextLine());

	            newEmployee.addCharacteristic(property, rating);
	            break;
	        } else if (choice.equals("n")) {
	            break;
	        } else {
	            System.out.println("Invalid choice. Please enter Y or N.");
	        }
	    }

	    container.addEmployee(newEmployee);
	    System.out.println("Employee added.");
	}


    private static void displayEmployees(EmployeeContainer<Employee> container) {
        for (Employee employee : container) {
            System.out.println(employee);
            List<Characteristic> characteristics = employee.getCharacteristics();
            for (Characteristic characteristic : characteristics) {
                System.out.println("Characteristic: " + characteristic.getProperty() + ", Rating: " + characteristic.getRating());
            }
        }
    }

    private static void displaySortedEmployees(List<Employee> employees) {
        EmployeeContainer<Employee> sortedContainer = new EmployeeContainer<>();
        for (Employee employee : employees) {
            sortedContainer.addEmployee(employee);
        }
        displayEmployees(sortedContainer);
    }
    
}
