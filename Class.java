import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Class implements Serializable {

    public Teacher teacher;

    private String credits;
    private String courseNumber;
    private String name;

    private String time;
    private String day; // (s)
    public String department;

    public static Object listOfClasses[][] = new Object[100][7];
    public static Class directoryOfClasses[] = new Class[100];
    public static int numOfClasses = 0;

    static {
        try {
            int index = 0;
            BufferedReader reader = new BufferedReader(
                    new FileReader("C:/Users/wadoo/IdeaProjects/SchoolDatabase/schoolclassdata.txt"));
            for (String classes = reader.readLine(); classes != null;
                 classes = reader.readLine()) {
                if (classes.equals("Classes:")) {
                    continue;
                }
                listOfClasses[numOfClasses][index % 7] = classes;
                index++;
                if (index % 7 == 0) {
                    numOfClasses++;
                }
            }
        } catch (IOException e) {}
    }

    public Class (String name, String credits, String time, String day, String department) {
        this.name = name;
        this.credits = credits;
        this.courseNumber = generateCourseNumber();
        this.time = time;
        this.day = day;
        this.department = department;
        for (int i = 0; i < 7; i++) {
            listOfClasses[numOfClasses][i] = specificClassInfo(i);
        }
        directoryOfClasses[numOfClasses] = this;
        numOfClasses++;
    }

    public Class() {
        super();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Class)) {
            return false;
        }
        Class otherClass = (Class)other;
        return this.getClassName().equals(otherClass.getClassName()) &&
                this.getCourseNumber().equals(otherClass.getCourseNumber());
    }

    public String getTeacher() {
        return this.teacher != null? this.teacher.fullName : "N/A";
    }

    public void setStudentGrade(Student student, double grade) {
        student.gradeForClass.put(this, grade);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String generateCourseNumber() {
        int difference = 0;
        for (int i = 0; i < numOfClasses; i++) {
            if (listOfClasses[i][0].equals(this.name)) {
                difference++;
            }
        }
        int suffix = 101 + (difference * 100);
        return "0" + suffix;
    }

    public static Class getClassViaName(String teacherName) {
        for (int i = 0; i < numOfClasses; i++) {
            for (Class classTaught : Teacher.classesTaught.get(teacherName)) {
                System.out.println(directoryOfClasses[i].name + "|" + classTaught.getClassName());
                if (directoryOfClasses[i].name.equals(classTaught.getClassName())) {
                    return directoryOfClasses[i];
                }
            }
        }
        return null;
    }

    public String getCredits() {
        return credits;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String toString() {
        return this.getClassName();
    }

    public String getClassName() {
        return name;
    }

    public Object specificClassInfo(int index) {
        switch (index) {
            case 0:
                return this.department.toUpperCase(Locale.ROOT);
            case 1:
                return this.getClassName().toUpperCase(Locale.ROOT);
            case 2:
                return this.courseNumber = generateCourseNumber();
            case 3:
                return this.day;
            case 4:
                return this.time;
            case 5:
                return this.teacher == null? "TBA" : this.teacher.fullName.toUpperCase(Locale.ROOT);
            case 6:
                return this.credits;
        }
        return null;
    }



}
