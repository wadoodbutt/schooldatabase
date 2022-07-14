import java.util.Locale;

public class Department {

    public String department;
    public String abbreviation;
    public String fullTitle;

    public static String listOfDepartments[] = new String[100];
    public static int numOfDepartments = 0;

    static {
        listOfDepartments[0] = "MATH - MATH";
        listOfDepartments[1] = "PHYSICS - PHYS";
        listOfDepartments[2] = "ENGINEERING - ENGR";
        listOfDepartments[3] = "COMPUTER SCIENCE - CMSC";
        listOfDepartments[4] = "SOCIAL DATA SCIENCE - SDSC";
        listOfDepartments[5] = "CHEMISTRY - CHEM";
        listOfDepartments[6] = "PSYCHOLOGY - PSYC";
        listOfDepartments[7] = "JOURNALISM - JOUR";
        listOfDepartments[8] = "ART - ART";
        listOfDepartments[9] = "UNDECIDED - NONE";
        numOfDepartments += 9;
    }
    public Department (String department) {
        this.department = department.toUpperCase(Locale.ROOT);
        this.fullTitle = generateFullTitle();

        listOfDepartments[numOfDepartments] = fullTitle;
        numOfDepartments++;
    }
    public String generateFullTitle() {
        return abbreviation + " - " + department;
    }
}

