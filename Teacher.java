import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Teacher implements Serializable {

    public String firstName;
    public String lastName;
    public String middleName;
    public String fullName;
    public int age;
    public String gender;
    public String department;
    public String email;
    public String phoneNumber;
    public List<String> listOfEmails;

    public static Map<String, Set<Class>> classesTaught = new HashMap<>();

    public static Object listOfTeachers[][] = new Object[100][6];
    public static int numOfTeachers = 0;
    public static Teacher directoryOfTeachers[] = new Teacher[100];

    static {
        try {
            int index = 0;
            BufferedReader reader = new BufferedReader(
                    new FileReader("C:/Users/wadoo/IdeaProjects/SchoolDatabase/schoolteacherdata.txt"));
            for (String teacher = reader.readLine(); teacher != null;
                 teacher = reader.readLine()) {
                if (teacher.equals("Teachers:")) {
                    continue;
                }
                listOfTeachers[numOfTeachers][index % 6] = teacher;
                index++;
                if (index % 6 == 0) {
                    numOfTeachers++;
                }
            }
        } catch (IOException e) {}
    }

    public Teacher (String firstName, String lastName, String middleName, int age, String phoneNumber,
                    String department, String gender) {
        listOfEmails = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.fullName = lastName + ", " + firstName + " " + middleName;
        this.fullName = fullName.toUpperCase(Locale.ROOT);
        this.age = age;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.generateEmail();
        for (int i = 0; i < 6; i++) {
            listOfTeachers[numOfTeachers][i] = specificTeacherInfo(i);
        }
        directoryOfTeachers[numOfTeachers] = this;
        classesTaught.put(this.fullName, new HashSet<>());
        numOfTeachers++;
    }

    public Teacher() {
        super();
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClassesTaught(String teacherName) {
        String allClasses = "";
        for (Class taughtClass : classesTaught.get(teacherName)) {
            allClasses += taughtClass + "\n";
        }
        return allClasses;
    }

    public int getAge() {
        return age;
    }

    public void addClassTaught(Class e, String fullName) {
        if (!(classesTaught.get(fullName).contains(e))) {
            classesTaught.get(fullName).add(e);
        }
    }

    public void removeClassTaught(Class e, String fullName) {
        if ((classesTaught.get(fullName).contains(e))) {
            classesTaught.get(fullName).remove(e);
        }
    }

    public void generateEmail() {
        String email = firstName.charAt(0) + lastName + "@anya.edu";
        int number = 1;
        while (listOfEmails.contains(email)) {
            email = firstName.charAt(0) + number + lastName + "@anya.edu";
            number++;
        }
        this.email = email;
    }

    public void setEmail(String email, String address) {
        this.email = email + address;
    }

    public void setNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object specificTeacherInfo(int index) {
        switch (index) {
            case 0:
                return this.fullName.toUpperCase(Locale.ROOT);
            case 1:
                return this.email;
            case 2:
                return this.age;
            case 3:
                return this.phoneNumber;
            case 4:
                return this.department;
            case 5:
                return this.gender;
        }
        return null;
    }

}
