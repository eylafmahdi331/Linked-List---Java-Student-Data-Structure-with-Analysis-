import java.io.*;


abstract class Person {
    String name; // inherited by Student

    Person(String name) {
        this.name = name;
    }

    // used by subclasses
    abstract void displayInfo();
}

// Student class
class Student extends Person {
    String course;
    int credit;
    double grade;
    double cgpa;
    Student next; 

    // constructor
    Student(String name, String course, int credit, double grade, double cgpa) {
        super(name);
        this.course = course;
        this.credit = credit;
        this.grade = grade;
        this.cgpa = cgpa;
        this.next = null;
    }

    // uses polymorphism: each Student knows how to display itself
    void displayInfo() {
        System.out.printf("%-20s with CGPA: %.2f\n", name, cgpa);
    }
}

// info about the list 
class StudentLinkedList {
    private Student head;
    private Student tail; 
    private int size;

    StudentLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // First method add at the start
    public void addFirst(String name, String course, int credit, double grade, double cgpa) {
        Student newNode = new Student(name, course, credit, grade, cgpa);
        if (head == null) { 
            head = tail = newNode; 
        } else { 
            newNode.next = head; 
            head = newNode; 
        } 
        size++;
    }

    // Add at the end
    public void addLast(String name, String course, int credit, double grade, double cgpa) {
        Student newNode = new Student(name, course, credit, grade, cgpa);
              if (head == null) { 
            head = tail = newNode; 
        } else { 
            tail.next = newNode; 
            tail = newNode; 
        } 
        size++;
    }

    // Insert at a specific position
    public void insertAt(int index, String name, String course, int credit, double grade, double cgpa) {
        // check if position is invalid or bigger than the size
        if (index < 0 || index > size) {
            System.out.println("Index out of bounds or invalid.");
            return;
        }
        if (index == 0) {
            addFirst(name, course, credit, grade, cgpa);
            return;
        }
        if (index == size) { // insert at end
            addLast(name, course, credit, grade, cgpa);
            return;
        }
        Student newNode = new Student(name, course, credit, grade, cgpa);
        Student current = head;
        // moving until the current before the target position
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        // connecting the newNode to the next node and then connecting the current to the newNode
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    // Delete first node
    public void deleteFirst() {
        if (head == null) return;
        head = head.next;
        if (head == null) tail = null; // if list became empty
        size--;
    }

    // delete last node
    public void deleteLast() {
        if (head == null) return;
        // if only node exist
        if (head.next == null) {
            head = null;  // list is empty now
            tail = null;
        } else {
            Student current = head;
            while (current.next.next != null) {
                current = current.next;
            }
            current.next = null;
            tail = current;
        }
        size--;
    }

    // Delete at specific position
    public void deleteAt(int index) {
        if (index < 0 || index >= size || head == null) {
            System.out.println("Index out of bounds or list is empty.");
            return;
        }
        if (index == 0) {
            deleteFirst();
            return;
        }
        Student current = head;
        // move current until before the node we want to delete
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        if (current.next == tail) tail = current; // update tail if deleting last
        current.next = current.next.next;
        size--;
    }

    // Search by name
    public int search(String name) {
        Student current = head;
        int index = 0;
        while (current != null) {
            if (current.name.equalsIgnoreCase(name)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1; // not found
    }

    // Display all students
    public void display() {
        Student current = head;
        while (current != null) {
            current.displayInfo();
            current = current.next;
        }
    }

    // return the size
    public int getSize() {
        return size;
    }

    // generating dummy data for test 3
    public void generateStudentsTo(int target) {
        int start = size;
        for (int i = start; i < target; i++) {
            String name = "New_Student_" + (i + 1);
            String course = "CS" + (100 + (i % 100));
            int credit = 3 + (i % 3);
            double grade = 50.0 + (i % 50);
            double cgpa = Math.round((2.0 + (i % 200) * 0.01) * 100.0) / 100.0;
            addLast(name, course, credit, grade, cgpa);
        }
    }
}

// Main class to test everything
public class LinkedList {
    public static void main(String[] args) {
        

        StudentLinkedList list = new StudentLinkedList();
        String file = "assignment1.csv";

        // Read CSV and load list
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            if (line == null) System.out.println("CSV is empty!");

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] divided = line.split(",", -1);
                if (divided.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                String name = divided[0].trim();
                String course = divided[1].trim();
                int credit = Integer.parseInt(divided[2].trim());
                double grade = Double.parseDouble(divided[3].trim());
                double cgpa = Double.parseDouble(divided[4].trim());

                list.addLast(name, course, credit, grade, cgpa);
            }
        } catch (IOException error) {
            System.out.println("Error reading CSV: " + error.getMessage());
        }

        // Test Case 1
        System.out.println("\n Test Case 1: ");
        list.display();
        System.out.println("Total students: " + list.getSize());

        list.addFirst("Bob Brown", "CS203", 3, 84.0, 3.5);
        list.addLast("Eyman Adam", "CS202", 4, 92.0, 3.8);
        list.insertAt(2, "InsertedName", "CS203", 3, 75.0, 3.0);


        System.out.println("\nAfter adding students:");
        list.display();
        System.out.println("Total students: " + list.getSize());

        list.deleteFirst();
        list.deleteLast();
        list.deleteAt(1);

        System.out.println("\nAfter deleting students:");
        list.display();
        System.out.println("Total students: " + list.getSize());

        int index = list.search("Siti Nur");
        if (index != -1) System.out.println("\nSiti Nur found at index: " + index);
        else System.out.println("\nSiti Nur not found");

        // Test Case 2: Edge Cases
        System.out.println("\n Test Case 2: Edge Cases ");
        list.deleteAt(-1); // invalid index
        list.deleteAt(1000); // out of bounds
        int indx = list.search("NoStudent");

        if (indx == -1) {
            System.out.println("Student 'NoStudent' not found in the list.");
        } else {
            System.out.println("Student 'NoStudent' found at index: " + indx);
        }

        // Test Case 3: Performance
        System.out.println("\n Test Case 3: Performance Test (1000 students) ");

        // Generate 1000 students
        long startTime = System.nanoTime();
        list.generateStudentsTo(1000);
        long endTime = System.nanoTime();
        System.out.println("Time to generate 1000 students: " + (endTime - startTime) + " ns");

        // Measure addFirst
        startTime = System.nanoTime();
        list.addFirst("Test3Student", "CS999", 3, 90.0, 3.9);
        endTime = System.nanoTime();
        System.out.println("Time to addFirst: " + (endTime - startTime) + " ns");

        // Measure addLast
        startTime = System.nanoTime();
        list.addLast("PerfTestStudent2", "CS998", 4, 85.0, 3.6);
        endTime = System.nanoTime();
        System.out.println("Time to addLast: " + (endTime - startTime) + " ns");

        // Measure insertAt
        startTime = System.nanoTime();
        list.insertAt(500, "PerfInsert", "CS997", 3, 88.0, 3.7);
        endTime = System.nanoTime();
        System.out.println("Time to insertAt index 500: " + (endTime - startTime) + " ns");

        // Measure deleteFirst
        startTime = System.nanoTime();
        list.deleteFirst();
        endTime = System.nanoTime();
        System.out.println("Time to deleteFirst: " + (endTime - startTime) + " ns");

        // Measure deleteLast
        startTime = System.nanoTime();
        list.deleteLast();
        endTime = System.nanoTime();
        System.out.println("Time to deleteLast: " + (endTime - startTime) + " ns");

        // Measure deleteAt
        startTime = System.nanoTime();
        list.deleteAt(400);
        endTime = System.nanoTime();
        System.out.println("Time to deleteAt index 400: " + (endTime - startTime) + " ns");

        // Measure search
        startTime = System.nanoTime();
        int indx1 = list.search("New_Student_250"); // searching for an existing student
        endTime = System.nanoTime();
        System.out.println("Duration to search New_Student_250: " + (endTime - startTime) + " ns, found at index: " + indx1);

        startTime = System.nanoTime();
        int indx2 = list.search("NonExistentStudent"); // searching for a non-existent student
        endTime = System.nanoTime();
        System.out.println("Duration to search NonExistentStudent: " + (endTime - startTime) + " ns, found at index: " + indx2);

        System.out.println("\nFinal total students: " + list.getSize());
    }
}
