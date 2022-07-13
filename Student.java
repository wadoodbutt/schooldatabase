import java.io.*;
import java.util.*;

public class Student implements Serializable {

    public double GPA;
    public String firstName;
    public String lastName;
    public String middleName;
    public int ID;
    public int age;
    public String year;
    public String DOB;
    public int creditsEnrolled;
    public Schedule studentSchedule;
    public String email;
    public String major;
    public int numOfClasses;
    public String gender;
    public String address;

    protected Set<String> listOfEmails;
    protected String[] listOfIDs = new String[100];
    public Map<Class, Double> gradeForClass;
    public static Set<Class> classes;

    public static Object listOfStudents[][] = new Object[100][7];
    public static int numOfStudents = 0;
    public static Student[] directoryOfStudents = new Student[100];

    Random rng = new Random();

    static {
        try {
            int index = 0;
            BufferedReader reader = new BufferedReader(
                    new FileReader("C:/Users/wadoo/IdeaProjects/SchoolDatabase/schoolstudentdata.txt"));
            for (String student = reader.readLine(); student != null;
                student = reader.readLine()) {
                if (student.equals("Students:")) {
                    continue;
                }
                listOfStudents[numOfStudents][index % 7] = student;
                index++;
                if (index % 7 == 0) {
                   /* directoryOfStudents[numOfStudents] = new Student((String)listOfStudents[numOfStudents][1],
                            (Integer) listOfStudents[numOfStudents][2], (String)listOfStudents[numOfStudents][3],
                            (String)listOfStudents[numOfStudents][4], (String)listOfStudents[numOfStudents][5],
                            (String)listOfStudents[numOfStudents][6]);
                    //directoryOfStudents[numOfStudents].addClass();*/
                    numOfStudents++;
                }
            }
        } catch (IOException e) {}
    }

    public Student() {
        super();
    }

    public Student(String fullName, int age, String year, String DOB, String gender,
                   String major) {
        String firstName = "";
        String lastName = "";
        String middleName = "";
        boolean block = false;
        for (int i = 0; i < fullName.length(); i++) {
            if (!(fullName.charAt(i) == ',') && !block) {
                lastName += fullName.charAt(i);
                block = true;
            } else if (!(fullName.charAt(i) == ' ')) {
                firstName += fullName.charAt(i);
            } else {
                middleName += fullName.charAt(i);
            }
        }
        middleName = middleName.substring(1,middleName.length() - 1);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName.toUpperCase(Locale.ROOT);
        this.age = age;
        this.year = year;
        this.DOB = DOB;
        this.major = major;
        this.gradeForClass = new HashMap<>();
        GPA =  0.0;
        numOfClasses = 0;
        ID = generateUniqueID();
        creditsEnrolled = 0;
        classes = new HashSet<>(5);
        listOfEmails = new HashSet<>();
        email = generateEmail(firstName, lastName, address);
        studentSchedule = new Schedule();
        this.gender = gender.toUpperCase(Locale.ROOT);
    }

    public Student(String firstName, String lastName, String middleName, int age,
                    String year, String DOB, String gender, String major) {
        System.out.println(numOfStudents);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName.toUpperCase(Locale.ROOT);
        this.age = age;
        this.year = year;
        this.DOB = DOB;
        this.major = major;
        this.gradeForClass = new HashMap<>();
        GPA =  0.0;
        numOfClasses = 0;
        ID = generateUniqueID();
        creditsEnrolled = 0;
        classes = new HashSet<>(5);
        listOfEmails = new HashSet<>();
        email = generateEmail(firstName, lastName, address);
        studentSchedule = new Schedule();
        this.gender = gender.toUpperCase(Locale.ROOT);
        for (int i = 0; i < 7; i++) {
            listOfStudents[numOfStudents][i] = specificStudentInfo(i);
        }
        directoryOfStudents[numOfStudents] = this;
        numOfStudents++;
    }

    public double getGradeForClass(Class classes) {
        return gradeForClass.get(classes) == null? 0.00 : gradeForClass.get(classes);
    }

    public static Student getStudent(String name) {
        for (int i = 0; i < numOfStudents; i++) {
            if (name.toUpperCase(Locale.ROOT).equals(directoryOfStudents[i].getFullName())) {
                return directoryOfStudents[i];
            }
        }
        return null;
    }

    public int generateUniqueID() {
        String ID = "11";
        for (int i = 0; i < 5; i++) {
            ID += rng.nextInt(9);
        }
        for (int index = 0; index < listOfIDs.length; index++) {
            if (listOfIDs[index] != null) {
                if (listOfIDs[index].equals(ID)) {
                    return generateUniqueID();
                }
            }
        }
        return Integer.parseInt(ID);
    }

    public String generateEmail(String firstName, String lastName, String address) { // address = @gmail.com
        String email = firstName.charAt(0) + lastName + address;
        int number = 1;
        while (listOfEmails.contains(email)) {
            email = firstName.charAt(0) + number + lastName + address;
            number++;
        }
        return email;
    }

    public int getStudentID() {
        return ID;
    }

    public String getFullName() {
        return lastName.toUpperCase(Locale.ROOT) + ", " + firstName.toUpperCase(Locale.ROOT)
                + " " + middleName.toUpperCase(Locale.ROOT); // Last, First Middle
    }

    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGradeLevel() {
        return year;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Student)) {
            return false;
        }
        Student otherStudent = (Student)other;
        return this.ID == otherStudent.ID;
    }

    @Override
    public int hashCode() {
        return ID * 7;
    }


    public void addClass(Class e) {
        if (classes.contains(e)) {
            return;
        } else {
            classes.add(e);
            numOfClasses++;
        }
    }

    public void addClass(Set<Class> e) {
        int size = e.size();
        for (Class c : e) {
            if (!(classes.contains(c))) {
                classes.add(c);
            } else {
                size--;
            }
        }
        numOfClasses += size;
    }

    public void removeClass(Class e) {
        if (classes.contains(e)) {
            classes.remove(e);
            numOfClasses--;
        }
    }

    public int getNumOfClasses() {
        return this.numOfClasses;
    }

    public String getDOB() {
        return this.DOB;
    }

    public String getSex() {
        return this.gender;
    }

    public Object specificStudentInfo(int index) {
        switch (index) {
            case 0:
                return this.ID;
            case 1:
                return this.getFullName();
            case 2:
                return this.age;
            case 3:
                return this.year;
            case 4:
                return this.DOB;
            case 5:
                return this.gender;
            case 6:
                return this.major;
            case 7:
                return this.classes;
        }
        return null;
    }
}
