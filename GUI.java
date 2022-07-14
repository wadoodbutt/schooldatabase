import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.util.Locale;

/* This class creates the GUI for each interface. Each interface has a frame with a
 * button that leads to that frame. For example, the JFrame 'studentMenu' is accessible
 * through the 'studentDatabase' button.
 */
public class GUI {
    // Frames for the numerous menus
    JFrame mainMenu, studentMenu, teacherMenu, registerStudent, registerClass,
            addTeacher, editCourses, classTeacherRegistration;
    // Buttons to access those frames
    JButton studentDatabase, enrollStudent, registration, teacherDatabase,
            enrollTeacher, addOrRemoveCourse, exportData, classTeacher;
    // Tables that display data of students, teachers, classes
    JTable tableOfTeachers, tableOfStudents, tableOfClasses;

    GridBagConstraints c;

    public GUI () {
        mainMenu = new JFrame("School Database");
        studentMenu = new JFrame("Student Database");
        registerStudent = new JFrame("Add Student");
        registerClass = new JFrame("Student Registration");
        teacherMenu = new JFrame("Teacher Database");
        addTeacher = new JFrame("Add Teacher");
        editCourses = new JFrame("Create Course");
        classTeacherRegistration = new JFrame("Assign Class's Teacher");

        studentDatabase = new JButton("Student Database");
        teacherDatabase = new JButton("Teacher Database");
        registration = new JButton("Registration (Drop/Add)");
        enrollStudent = new JButton("Enroll Student");
        enrollTeacher = new JButton("Enroll Teacher");
        addOrRemoveCourse = new JButton("Add/Remove Course");
        exportData = new JButton("Save Data");
        classTeacher = new JButton("Assign Teacher to Class");

        tableOfStudents = new JTable();

        c = new GridBagConstraints();
    }

    // The interface for students that showcases their features (name, email, id, etc.)
    public void studentDatabaseInterface() {
        c = new GridBagConstraints();
        studentMenu.setSize(900, 405);

        JPanel tablePanel = new JPanel(new FlowLayout());
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setPreferredSize(new Dimension(200, 400));

        String[] columnNames = {"ID", "Name", "Age", "Grade/Year", "Date of Birth", "Gender", "Major"};
        Object[][] data = new Object[100][columnNames.length];
        for (int i = 0; i < Student.numOfStudents; i++) {
            for (int j = 0; j < columnNames.length; j++) {
                data[i][j] = Student.listOfStudents[i][j];
                }
            }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tableOfStudents = new JTable(model);

        tableOfStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableOfStudents.setPreferredSize(new Dimension(700, 350));
        tableOfStudents.setBackground(Color.WHITE);

        JScrollPane scroller = new JScrollPane(tableOfStudents);
        tablePanel.add(scroller);

        JButton overview = new JButton("Overview of Student");
        JButton remove = new JButton("Remove Student");
        JButton back = new JButton("<---");

        if (Student.numOfStudents == 0) {
            overview.setEnabled(false);
            remove.setEnabled(false);
        }

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == remove) {
                    if (tableOfStudents.getSelectedRow() != -1) {
                        // remove selected row from the model
                        model.removeRow(tableOfStudents.getSelectedRow());
                        // remove student from directory/list of students
                        int index = tableOfStudents.getSelectedRow();

                        int updatedLength = Student.directoryOfStudents.length - 1;
                        Student[] tempArr = new Student[updatedLength];
                        System.arraycopy(Student.directoryOfStudents, 0, tempArr, index, index);
                        System.arraycopy(Student.directoryOfStudents, index + 1, tempArr,
                                Student.directoryOfStudents.length - 1, updatedLength);

                        updatedLength = Student.listOfStudents.length - 1;
                        Object[][] temp2dArr = new Object[updatedLength][];
                        // Copying everything before the index (deleted element)
                        for (int i = 0; i < index; i++) {
                            System.arraycopy(Student.listOfStudents[i], 0, temp2dArr[i],
                                    Student.listOfStudents[i].length, Student.listOfStudents[i].length);
                        }
                        // Copying everything after the index (deleted element)
                        for (int i = index + 1; i < updatedLength; i++) {
                            System.arraycopy(Student.listOfStudents[i], 0, temp2dArr[i],
                                    Student.listOfStudents[i].length, Student.listOfStudents[i].length);
                        }

                        // Deep Copy of new, temp arrays on to the listOfStudents and directoryOfStudents
                        for (int i = 0; i < Student.numOfStudents; i++) {
                            Student.directoryOfStudents[i] = tempArr[i];
                            for (int j = 0; j < Student.listOfStudents[i].length; j++) {
                                Student.listOfStudents[i][j] = temp2dArr[i][j];
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Student Removed!");
                    }
                } else if (e.getSource() == overview) {
                    // Create pop up that shows all the student's details
                    // Transcript?
                    // Change major here
                    int index = tableOfStudents.getSelectedRow();
                    Transcript transcript = new Transcript();
                    transcript.showTranscript(Student.directoryOfStudents[index]);
                } else {
                    studentMenu.setVisible(false);
                    Driver.main(new String[]{});
                }
            }
        };

        overview.addActionListener(input);
        remove.addActionListener(input);
        back.addActionListener(input);

        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 10;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,20,20,30);
        buttonPanel.add(overview, c);
        c.gridy = 10;
        buttonPanel.add(remove, c);
        c.gridy = 19;
        buttonPanel.add(back, c);

        studentMenu.add(scroller);
        studentMenu.add(buttonPanel, BorderLayout.WEST);
        studentMenu.add(tablePanel, BorderLayout.EAST);
        studentMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentMenu.setResizable(false);
        studentMenu.setVisible(true);
    }

    // Interface that asks question (name, date of birth, age, etc.) to create a new student
    public void studentEnrollmentInterface() {
        JPanel panel = new JPanel(null);
        registerStudent.setSize(400,500);

        // Name
        JLabel firstName = new JLabel("First Name:");
        firstName.setSize(100, 20);
        firstName.setLocation(20, 20);
        panel.add(firstName);

        JTextField enterFirstName = new JTextField();
        enterFirstName.setSize(100, 30);
        enterFirstName.setLocation(120, 20);
        panel.add(enterFirstName);

        JLabel lastName = new JLabel("Last Name:");
        lastName.setSize(100, 20);
        lastName.setLocation(20, 70);
        panel.add(lastName);

        JTextField enterLastName = new JTextField();
        enterLastName.setSize(100, 30);
        enterLastName.setLocation(120, 70);
        panel.add(enterLastName);

        JLabel middleInitial = new JLabel("Middle Name:");
        middleInitial.setSize(100, 20);
        middleInitial.setLocation(20, 120);
        panel.add(middleInitial);

        JTextField enterMiddleInitial = new JTextField();
        enterMiddleInitial.setSize(100, 30);
        enterMiddleInitial.setLocation(120, 120);
        panel.add(enterMiddleInitial);

        // Age
        JLabel age = new JLabel("Age:");
        age.setSize(100, 20);
        age.setLocation(20, 170);
        panel.add(age);

        Integer[] ages = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
        21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,
        43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,
        65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,
        87,88,89,90,91,92,93,94,95,96,97,98,99,100};
        JComboBox<Integer> enterAge = new JComboBox<>(ages);
        enterAge.setSize(50,30);
        enterAge.setLocation(120, 170);
        panel.add(enterAge);

        // Grade
        JLabel grade = new JLabel("Grade:");
        grade.setSize(100, 20);
        grade.setLocation(20, 220);
        panel.add(grade);

        JComboBox<String> enterGrade = new JComboBox<>(new String[]{ "Freshman",
                "Sophomore", "Junior", "Senior"});
        enterGrade.setSize(100, 30);
        enterGrade.setLocation(120, 220);
        panel.add(enterGrade);

        // Date of Birth
        JLabel dob = new JLabel("Date of Birth:");
        dob.setSize(100, 20);
        dob.setLocation(20, 270);
        panel.add(dob);

        JComboBox<String> enterMonth = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"
        });
        enterMonth.setSize(50,30);
        enterMonth.setLocation(120, 270);
        panel.add(enterMonth);

        JComboBox<Integer> enterDay = new JComboBox<>(new Integer[]{
                1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,31
        });
        enterDay.setSize(50,30);
        enterDay.setLocation(190, 270);
        panel.add(enterDay);

        JComboBox<Integer> enterYear = new JComboBox<>(new Integer[]{
                2022,2021,2020,2019,2018,2017,2016,2015,2014,2013,
                2012,2011,2010,2009,2008,2007,2006,2005,2004,2003,2002,2001,
                2000,1999,1998,1997,1996,1995,1994,1993,1992,1991,
                1990,1989,1988,1987,1986,1985,1984,1983,1982,1981,
                1980,1979,1978,1977,1976,1975,1974,1973,1972,1971,
                1970,1969,1968,1967,1966,1965,1964,1963,1962,1961,
                1960,1959,1958,1957,1956,1955,1954,1953,1952,1951,
                1950,1949,1948,1947,1946,1945,1944,1943,1942,1941,
                1940,1939,1938,1937,1936,1935,1934,1933,1932,1931,
                1930,1929,1928,1927,1926,1925,1924,1923,1922
        });
        enterYear.setSize(50,30);
        enterYear.setLocation(260, 270);
        panel.add(enterYear);

        // Gender
        JLabel sex = new JLabel("Sex:");
        sex.setSize(50, 30);
        sex.setLocation(20, 315);
        panel.add(sex);

        ButtonGroup bg = new ButtonGroup();

        JRadioButton enterMale = new JRadioButton("Male");
        enterMale.setSelected(false);
        enterMale.setSize(75, 20);
        enterMale.setLocation(120,320);

        JRadioButton enterFemale = new JRadioButton("Female");
        enterFemale.setSelected(false);
        enterFemale.setSize(75, 20);
        enterFemale.setLocation(210,320);

        bg.add(enterMale);
        bg.add(enterFemale);
        panel.add(enterMale);
        panel.add(enterFemale);

        // Major
        JLabel major = new JLabel("Major:");
        major.setSize(100,30);
        major.setLocation(20, 350);

        JComboBox<String> enterMajor = new JComboBox<>(Department.listOfDepartments);
        enterMajor.setSize(150, 30);
        enterMajor.setLocation(100, 350);

        panel.add(major);
        panel.add(enterMajor);

        // Action Buttons
        JButton addStudent = new JButton("Add");
        addStudent.setSize(60, 30);
        addStudent.setLocation(130, 400);
        JButton back = new JButton("<---");
        back.setSize(60, 30);
        back.setLocation(210, 400);
        panel.add(addStudent);
        panel.add(back);


        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addStudent) {
                    if (enterFirstName.getText().toUpperCase(Locale.ROOT).equals("") ||
                    enterLastName.getText().toUpperCase(Locale.ROOT).equals("") ||
                            enterMiddleInitial.getText().toUpperCase(Locale.ROOT).equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields!");
                    } else {
                        String dob = enterMonth.getItemAt(enterMonth.getSelectedIndex())
                                + " " + enterDay.getItemAt(enterDay.getSelectedIndex()) + ", " +
                                enterYear.getItemAt(enterYear.getSelectedIndex());
                        dob = dob.toUpperCase(Locale.ROOT);
                        String sex = "";
                        if (enterMale.isSelected()) {
                            sex = "MALE";
                        } else {
                            sex = "FEMALE";
                        }
                        Student student = new Student(enterFirstName.getText().toUpperCase(Locale.ROOT),
                                enterLastName.getText().toUpperCase(Locale.ROOT),
                                enterMiddleInitial.getText().toUpperCase(Locale.ROOT),
                                enterAge.getItemAt(enterAge.getSelectedIndex()),
                                enterGrade.getItemAt(enterGrade.getSelectedIndex()),
                                dob,
                                sex,
                                enterMajor.getItemAt(enterMajor.getSelectedIndex()).toUpperCase(Locale.ROOT));
                        JOptionPane.showMessageDialog(null, "Student Added!");
                        enterFirstName.setText("");
                        enterLastName.setText("");
                        enterMiddleInitial.setText("");
                    }
                } else {
                    registerStudent.setVisible(false);
                    mainMenu.setVisible(true);
                }
            }
        };
        back.addActionListener(input);
        addStudent.addActionListener(input);

        registerStudent.add(panel);
        registerStudent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerStudent.setResizable(false);
        registerStudent.setVisible(true);
    }
    // The interface that showcases teachers' features (name, email, classes taught, etc.)
    public void teacherDatabaseInterface() {
        c = new GridBagConstraints();
        teacherMenu.setSize(900, 405);

        JPanel tablePanel = new JPanel(new FlowLayout());
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setPreferredSize(new Dimension(200, 400));

        String[] columnNames = {"Name", "Email", "Age", "Contact Number", "Department", "Gender"};
        Object[][] data = new Object[100][6];
        for (int i = 0; i < Teacher.numOfTeachers; i++) {
            for (int j = 0; j < 6; j++) {
                data[i][j] = Teacher.listOfTeachers[i][j];
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tableOfTeachers = new JTable(model);
        tableOfTeachers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableOfTeachers.setPreferredSize(new Dimension(700, 350));
        tableOfTeachers.setBackground(Color.WHITE);

        JScrollPane scroller = new JScrollPane(tableOfTeachers);
        tablePanel.add(scroller);

        JButton overview = new JButton("Overview of Teacher");
        JButton remove = new JButton("Remove Teacher");
        JButton back = new JButton("<---");

        if (Teacher.numOfTeachers == 0) {
            overview.setEnabled(false);
            remove.setEnabled(false);
        }

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == remove) {
                    int index = tableOfTeachers.getSelectedRow();
                    Teacher teacher = Teacher.directoryOfTeachers[index];
                    if (tableOfTeachers.getSelectedRow() != -1) {
                        // remove selected row from the model
                        model.removeRow(index);
                        JOptionPane.showMessageDialog(null, "Student Removed!");
                    }
                } else if (e.getSource() == overview) {
                    int index = tableOfTeachers.getSelectedRow();
                    Teacher teacher = Teacher.directoryOfTeachers[index];
                    Transcript transcript = new Transcript();
                    transcript.showOverview(teacher);
                } else {
                    teacherMenu.setVisible(false);
                    Driver.main(new String[]{});
                }
            }
        };

        overview.addActionListener(input);
        remove.addActionListener(input);
        back.addActionListener(input);

        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 10;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,20,20,30);
        buttonPanel.add(overview, c);
        c.gridy = 10;
        buttonPanel.add(remove, c);
        c.gridy = 19;
        buttonPanel.add(back, c);

        teacherMenu.add(scroller);
        teacherMenu.add(buttonPanel, BorderLayout.WEST);
        teacherMenu.add(tablePanel, BorderLayout.EAST);
        teacherMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        teacherMenu.setResizable(false);
        teacherMenu.setVisible(true);
    }
    // Interface that asks question (name, date of birth, age, etc.) to create a new teacher
    public void teacherEnrollmentInterface() {
        JPanel panel = new JPanel(null);
        addTeacher.setSize(400,425);

        // Name
        JLabel firstName = new JLabel("First Name:");
        firstName.setSize(100, 20);
        firstName.setLocation(20, 20);
        panel.add(firstName);

        JTextField enterFirstName = new JTextField();
        enterFirstName.setSize(100, 30);
        enterFirstName.setLocation(120, 20);
        panel.add(enterFirstName);

        JLabel lastName = new JLabel("Last Name:");
        lastName.setSize(100, 20);
        lastName.setLocation(20, 70);
        panel.add(lastName);

        JTextField enterLastName = new JTextField();
        enterLastName.setSize(100, 30);
        enterLastName.setLocation(120, 70);
        panel.add(enterLastName);

        JLabel middleInitial = new JLabel("Middle Name:");
        middleInitial.setSize(100, 20);
        middleInitial.setLocation(20, 120);
        panel.add(middleInitial);

        JTextField enterMiddleInitial = new JTextField();
        enterMiddleInitial.setSize(100, 30);
        enterMiddleInitial.setLocation(120, 120);
        panel.add(enterMiddleInitial);

        // Age
        JLabel age = new JLabel("Age:");
        age.setSize(100, 20);
        age.setLocation(20, 170);
        panel.add(age);

        Integer[] ages = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,
                43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,
                65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,
                87,88,89,90,91,92,93,94,95,96,97,98,99,100};
        JComboBox<Integer> enterAge = new JComboBox<>(ages);
        enterAge.setSize(50,30);
        enterAge.setLocation(120, 170);
        panel.add(enterAge);

        // Phone Number
        JLabel number = new JLabel("Contact Number (XXX-XXX-XXXX):");
        number.setSize(200, 20);
        number.setLocation(20, 220);
        panel.add(number);

        JTextField enterPhoneNumber = new JTextField();
        enterPhoneNumber.setSize(125, 30);
        enterPhoneNumber.setLocation(225, 220);
        panel.add(enterPhoneNumber);

        // Department
        JLabel department = new JLabel("Department:");
        department.setSize(100, 20);
        department.setLocation(20, 270);
        panel.add(department);

        JComboBox<String> enterDepartment = new JComboBox<>(Department.listOfDepartments);
        enterDepartment.setSize(125, 30);
        enterDepartment.setLocation(140, 270);
        panel.add(enterDepartment);

        // Gender
        JLabel sex = new JLabel("Sex:");
        sex.setSize(50, 30);
        sex.setLocation(20, 315);
        panel.add(sex);

        ButtonGroup bg = new ButtonGroup();

        JRadioButton enterMale = new JRadioButton("Male");
        enterMale.setSelected(false);
        enterMale.setSize(75, 20);
        enterMale.setLocation(120,320);

        JRadioButton enterFemale = new JRadioButton("Female");
        enterFemale.setSelected(false);
        enterFemale.setSize(75, 20);
        enterFemale.setLocation(210,320);

        bg.add(enterMale);
        bg.add(enterFemale);
        panel.add(enterMale);
        panel.add(enterFemale);

        // Action Buttons
        JButton addTeacherButton = new JButton("Add");
        addTeacherButton.setSize(60, 30);
        addTeacherButton.setLocation(130, 350);
        JButton back = new JButton("<---");
        back.setSize(60, 30);
        back.setLocation(210, 350);
        panel.add(addTeacherButton);
        panel.add(back);

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addTeacherButton) {
                    if (enterFirstName.getText().toUpperCase(Locale.ROOT).equals("") ||
                            enterLastName.getText().toUpperCase(Locale.ROOT).equals("") ||
                            enterMiddleInitial.getText().toUpperCase(Locale.ROOT).equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields!");
                    } else {
                        String sex = "";
                        if (enterMale.isSelected()) {
                            sex = "MALE";
                        } else {
                            sex = "FEMALE";
                        }
                        Teacher teacher = new Teacher(enterFirstName.getText().toUpperCase(Locale.ROOT),
                                enterLastName.getText().toUpperCase(Locale.ROOT),
                                enterMiddleInitial.getText().toUpperCase(Locale.ROOT),
                                enterAge.getItemAt(enterAge.getSelectedIndex()),
                                enterPhoneNumber.getText(),
                                enterDepartment.getItemAt(enterDepartment.getSelectedIndex()),
                                sex);
                        JOptionPane.showMessageDialog(null, "Teacher Added!");
                        enterFirstName.setText("");
                        enterLastName.setText("");
                        enterMiddleInitial.setText("");
                        enterPhoneNumber.setText("");
                    }
                } else {
                    addTeacher.setVisible(false);
                    mainMenu.setVisible(true);
                }
            }
        };
        back.addActionListener(input);
        addTeacherButton.addActionListener(input);

        addTeacher.add(panel);
        addTeacher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addTeacher.setResizable(false);
        addTeacher.setVisible(true);
    }
    // Interface that asks question (class name, class dates, credits, and time) to create a new class
    public void editCourseInterface() {
        JPanel formPanel = new JPanel(null);
        editCourses.getContentPane().add(formPanel);
        String[] labels = {"Course Name: ", "Number of Credits: ",
        "Time (XX:XX AM/PM): ", "Class Dates (i.e. MWF = Monday Wednesday Friday): "};
        JTextField entry[] = new JTextField[labels.length];
        // Course Name
        JLabel name = new JLabel("Course Name: ");
        name.setSize(100, 20);
        name.setLocation(50, 50);
        formPanel.add(name);

        JTextField enterName = new JTextField();
        enterName.setSize(190, 20);
        enterName.setLocation(150, 50);
        formPanel.add(enterName);

        // Credits
        JLabel credits = new JLabel("Credits");
        credits.setSize(100, 20);
        credits.setLocation(50, 100);
        formPanel.add(credits);

        JComboBox<String> selectCredits = new JComboBox<>(new String[]{"--", "0", "1",
        "2", "3", "4"});
        selectCredits.setSize(50,30);
        selectCredits.setLocation(150, 100);
        formPanel.add(selectCredits);

        // Time
        JLabel time = new JLabel("Time: ");
        time.setSize(100, 20);
        time.setLocation(50, 150);
        JLabel colonTime = new JLabel(":");
        colonTime.setSize(10,20);
        colonTime.setLocation(235, 150);
        formPanel.add(time);
        formPanel.add(colonTime);

        String[] hours = {"1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10", "11", "12"};
        String[] minutes = {"00", "05", "10", "15", "20",
                "25", "30", "35", "40", "45", "50", "55"};
        String[] timeOfDay = {"AM", "PM"};
        JComboBox<String> enterHour = new JComboBox<>(hours);
        JComboBox<String> enterMinute = new JComboBox<>(minutes);
        JComboBox<String> enterTimeOfDay = new JComboBox<>(timeOfDay);
        enterHour.setSize(70, 30);
        enterHour.setLocation(150, 150);
        enterMinute.setSize(70, 30);
        enterMinute.setLocation(250, 150);
        enterTimeOfDay.setSize(70,30);
        enterTimeOfDay.setLocation(350, 150);
        formPanel.add(enterHour);
        formPanel.add(enterMinute);
        formPanel.add(enterTimeOfDay);

        JLabel lengthOfClass = new JLabel("to");
        JLabel colonTimeTo = new JLabel(":");
        colonTimeTo.setSize(30,30);
        colonTimeTo.setLocation(535, 150);
        lengthOfClass.setSize(50, 30);
        lengthOfClass.setLocation( 427, 150);
        formPanel.add(lengthOfClass);
        formPanel.add(colonTimeTo);

        JComboBox<String> enterHourTo = new JComboBox<>(hours);
        JComboBox<String> enterMinuteTo = new JComboBox<>(minutes);
        JComboBox<String> enterTimeOfDayTo = new JComboBox<>(timeOfDay);
        enterHourTo.setSize(70, 30);
        enterHourTo.setLocation(450, 150);
        enterMinuteTo.setSize(70, 30);
        enterMinuteTo.setLocation(550, 150);
        enterTimeOfDayTo.setSize(70,30);
        enterTimeOfDayTo.setLocation(650, 150);
        formPanel.add(enterHourTo);
        formPanel.add(enterMinuteTo);
        formPanel.add(enterTimeOfDayTo);


        // Day
        JLabel day = new JLabel("Day(s): ");
        day.setSize(100, 20);
        day.setLocation(50, 200);
        formPanel.add(day);

        JRadioButton monday = new JRadioButton("Monday");
        monday.setSize(60, 20);
        monday.setLocation(150, 200);
        monday.setSelected(false);
        formPanel.add(monday);

        JRadioButton tuesday = new JRadioButton("Tuesday");
        tuesday.setSize(60, 20);
        tuesday.setLocation(225, 200);
        tuesday.setSelected(false);
        formPanel.add(tuesday);

        JRadioButton wednesday = new JRadioButton("Wednesday");
        wednesday.setSize(60, 20);
        wednesday.setLocation(300, 200);
        wednesday.setSelected(false);
        formPanel.add(wednesday);

        JRadioButton thursday = new JRadioButton("Thursday");
        thursday.setSize(60, 20);
        thursday.setLocation(375, 200);
        thursday.setSelected(false);
        formPanel.add(thursday);

        JRadioButton friday = new JRadioButton("Friday");
        friday.setSize(60, 20);
        friday.setLocation(450, 200);
        friday.setSelected(false);
        formPanel.add(friday);

        // Department
        JLabel department = new JLabel("Department: ");
        department.setSize(100, 20);
        department.setLocation(50, 250);
        formPanel.add(department);

        JComboBox<String> enterDepartment =
                new JComboBox<>(Department.listOfDepartments);
        enterDepartment.setSize(150, 30);
        enterDepartment.setLocation(150, 250);
        formPanel.add(enterDepartment);

        // Action Buttons
        JButton add = new JButton("Add Course");
        JButton back = new JButton("<---");
        add.setSize(100, 25);
        add.setLocation(180,300);
        back.setSize(100, 25);
        back.setLocation(290, 300);
        formPanel.add(add);
        formPanel.add(back);

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == add) {
                    // Check for valid input -- implement later
                    String courseName = enterName.getText().toUpperCase(Locale.ROOT);
                    String credits = selectCredits.getItemAt(selectCredits.getSelectedIndex());
                    String time = enterHour.getItemAt(enterHour.getSelectedIndex()) +
                            ":" + enterMinute.getItemAt(enterMinute.getSelectedIndex()) +
                            " " + enterTimeOfDay.getItemAt(enterTimeOfDay.getSelectedIndex()) + "->" +
                            enterHourTo.getItemAt(enterHourTo.getSelectedIndex()) +
                            ":" + enterMinuteTo.getItemAt(enterMinuteTo.getSelectedIndex()) +
                            " " + enterTimeOfDayTo.getItemAt(enterTimeOfDayTo.getSelectedIndex());
                    String day = "";
                    if (monday.isSelected()) {
                        day += "M ";
                    }
                    if (tuesday.isSelected()) {
                        day += "Tu ";
                    }
                    if (wednesday.isSelected()) {
                        day += "W ";
                    }
                    if (thursday.isSelected()) {
                        day += "Th ";
                    }
                    if (friday.isSelected()) {
                        day += "F ";
                    }
                    String department = enterDepartment.getItemAt(enterDepartment.getSelectedIndex());
                    Class addClass = new Class(courseName, credits, time, day, department); // Class is added
                    // Resets Response
                    JOptionPane.showMessageDialog(null, "Class Added!");
                    enterName.setText("");
                } else {
                    editCourses.setVisible(false);
                    mainMenu.setVisible(true);
                }
            }
        };
        add.addActionListener(input);
        back.addActionListener(input);
        formPanel.setBackground(Color.LIGHT_GRAY);
        editCourses.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editCourses.setResizable(false);
        editCourses.setSize(800,400);
        editCourses.setVisible(true);
    }
    // Allows for student to register to available classes
    public void registrationInterface() {
        JPanel panel = new JPanel(null);
        registerClass.getContentPane().add(panel);
        // Students
        JLabel student = new JLabel("Student: ");
        student.setSize(100, 20);
        student.setLocation(50, 50);
        panel.add(student);

        String[] listOfStudentNames = new String[Student.listOfStudents.length];
        for (int i = 0; i < Student.numOfStudents; i++) {
            listOfStudentNames[i] = ((String) Student.listOfStudents[i][1]);
        }
        JComboBox<String> enterStudent = new JComboBox<>(listOfStudentNames);
        enterStudent.setSize(100, 30);

        enterStudent.setSize(100, 30);
        enterStudent.setLocation(125, 45);
        panel.add(enterStudent);

        // Class List
        String[] columnNames = {"Department", "Class", "Section", "Day", "Time", "Teacher","Credits"};
        Object[][] data = new Object[100][columnNames.length];
        for (int i = 0; i < Class.numOfClasses; i++) {
            for (int j = 0; j < columnNames.length; j++) {
                data[i][j] = Class.listOfClasses[i][j];
            }
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        tableOfClasses = new JTable(model);
        tableOfClasses.setRowSorter(sorter);
        tableOfClasses.setBackground(Color.WHITE);
        JScrollPane scroller = new JScrollPane(tableOfClasses);
        scroller.setSize(450,400);
        scroller.setLocation(450, 50);
        panel.add(scroller);

        // Filters
        JLabel border = new JLabel();
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        border.setSize(375,345);
        border.setLocation(35,110); // 300, 260
        panel.add(border);

        JLabel filter = new JLabel("Filters: ");
        filter.setSize(100, 20);
        filter.setLocation(50, 130);
        panel.add(filter);
            // By Credits
        JLabel byCredits = new JLabel("Credits: ");
        byCredits.setSize(100, 20);
        byCredits.setLocation(75, 175);
        panel.add(byCredits);
        JComboBox<String> filterByCredits = new JComboBox<>(new String[]{
                "--", "1", "2", "3", "4"
        });
        filterByCredits.setSize(50,30);
        filterByCredits.setLocation(150, 175);
        panel.add(filterByCredits);
            // By Department
        JLabel byDepartment = new JLabel("Department: ");
        byDepartment.setSize(100, 20);
        byDepartment.setLocation(75, 250);
        panel.add(byDepartment);
        JComboBox<String> filterByDepartment = new JComboBox<>(new String[]{
               "--", "MATH", "PHYS", "ENGR", "CMSC", "SCDC", "CHEM", "PSYC",
                "JOUR", "ART"
        });
        filterByDepartment.setSize(100,30);
        filterByDepartment.setLocation(175, 250);
        panel.add(filterByDepartment);

            // By Days
        JLabel byDays = new JLabel("Days: ");
        byDays.setSize(100, 20);
        byDays.setLocation(75, 325);
        panel.add(byDays);

        JRadioButton monday = new JRadioButton("Monday");
        monday.setSize(75, 20);
        monday.setLocation(125, 325);
        monday.setSelected(false);
        panel.add(monday);

        JRadioButton tuesday = new JRadioButton("Tuesday");
        tuesday.setSize(75, 20);
        tuesday.setLocation(210, 325);
        tuesday.setSelected(false);
        panel.add(tuesday);

        JRadioButton wednesday = new JRadioButton("Wednesday");
        wednesday.setSize(75, 20);
        wednesday.setLocation(295, 325);
        wednesday.setSelected(false);
        panel.add(wednesday);

        JRadioButton thursday = new JRadioButton("Thursday");
        thursday.setSize(75, 20);
        thursday.setLocation(125, 350);
        thursday.setSelected(false);
        panel.add(thursday);

        JRadioButton friday = new JRadioButton("Friday");
        friday.setSize(75, 20);
        friday.setLocation(210, 350);
        friday.setSelected(false);
        panel.add(friday);

        JButton applyFilter = new JButton("Apply Filter(s)");
        applyFilter.setSize(130, 30);
        applyFilter.setLocation(125,400);
        panel.add(applyFilter);

        // Action Buttons
        JButton add = new JButton("Enroll");
        JButton remove = new JButton("Withdraw");
        JButton back = new JButton("<---");
        add.setSize(100, 25);
        add.setLocation(165,475);
        back.setSize(100,25);
        back.setLocation(40, 475);
        remove.setSize(100, 25);
        remove.setLocation(290, 475);
        panel.add(add);
        panel.add(back);
        panel.add(remove);

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == add) {
                    Student.getStudent(enterStudent.getItemAt
                                    (enterStudent.getSelectedIndex())).addClass
                            (Class.directoryOfClasses[tableOfClasses.getSelectedRow()]);
                    JOptionPane.showMessageDialog(null, "Student Enrolled!");
                } else if (e.getSource() == back) {
                    registerClass.setVisible(false);
                    Driver.main(new String[]{});
                } else if (e.getSource() == applyFilter) {
                    if (!(filterByDepartment.getItemAt(filterByDepartment.getSelectedIndex()).equals("--"))) {
                        sorter.setRowFilter(RowFilter.regexFilter
                                (filterByDepartment.getItemAt(filterByDepartment.getSelectedIndex())));
                    }
                    if (!(filterByCredits.getItemAt(filterByDepartment.getSelectedIndex()).equals("--"))) {
                        sorter.setRowFilter(RowFilter.regexFilter
                                (filterByCredits.getItemAt(filterByDepartment.getSelectedIndex())));
                    }
                    if (monday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Monday"));
                    }
                    if (tuesday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Tuesday"));
                    }
                    if (wednesday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Wednesday"));
                    }
                    if (thursday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Thursday"));
                    }
                    if (friday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Friday"));
                    }
                } else { // e.getSource == remove
                    Student.getStudent(enterStudent.getItemAt
                            (enterStudent.getSelectedIndex())).removeClass
                            ((Class.directoryOfClasses[tableOfClasses.getSelectedRow()]));
                    JOptionPane.showMessageDialog(null, "Student Withdrawn!");
                }
            }
        };
        add.addActionListener(input);
        back.addActionListener(input);
        remove.addActionListener(input);
        panel.setBackground(Color.LIGHT_GRAY);
        registerClass.setSize(950,550);
        registerClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerClass.setResizable(false);
        registerClass.setVisible(true);
    }
    // Main menu
    public void mainInterface() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel background = new JLabel(
                new ImageIcon(getClass().getResource("logoschool.png")));
        background.setSize(850, 245);
        mainMenu.add(background, BorderLayout.NORTH);
        mainMenu.setSize(850, 600);
        // Switches window by making main menu invisible and the selected entry visible
        ActionListener switchWindows = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == studentDatabase) {
                    mainMenu.setVisible(false);
                    studentDatabaseInterface();
                } else if (e.getSource() == enrollStudent) {
                    mainMenu.setVisible(false);
                    studentEnrollmentInterface();
                } else if (e.getSource() == registration) {
                    mainMenu.setVisible(false);
                    registrationInterface();
                } else if (e.getSource() == teacherDatabase) {
                    mainMenu.setVisible(false);
                    teacherDatabaseInterface();
                } else if (e.getSource() == enrollTeacher) {
                    mainMenu.setVisible(false);
                    teacherEnrollmentInterface();
                } else if (e.getSource() == exportData) {
                    try {
                        saveUserData();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (e.getSource() == classTeacher) {
                    mainMenu.setVisible(false);
                    classTeacherRegistrationInterface();
                } else { // e.getSource() == addOrRemoveCourse
                    mainMenu.setVisible(false);
                    editCourseInterface();
                }
            }
        };

        c.gridx = 2;
        c.gridy = 6;
        c.ipady = 15;
        c.insets = new Insets(30,0,0,100);
        c.fill = GridBagConstraints.HORIZONTAL;
        studentDatabase.addActionListener(switchWindows);
        panel.add(studentDatabase, c);

        c.gridx = 2;
        c.gridy = 7;
        c.ipady = 15;
        c.insets = new Insets(30,0,0,100);
        c.fill = GridBagConstraints.HORIZONTAL;
        enrollStudent.addActionListener(switchWindows);
        panel.add(enrollStudent, c);

        c.gridx = 2;
        c.gridy = 8;
        c.ipady = 15;
        c.insets = new Insets(30,0,0,100);
        c.fill = GridBagConstraints.HORIZONTAL;
        registration.addActionListener(switchWindows);
        panel.add(registration, c);

        c.gridx = 6;
        c.gridy = 6;
        c.ipady = 15;
        c.insets = new Insets(30,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        teacherDatabase.addActionListener(switchWindows);
        panel.add(teacherDatabase, c);

        c.gridx = 6;
        c.gridy = 7;
        c.ipady = 15;
        c.insets = new Insets(30,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        enrollTeacher.addActionListener(switchWindows);
        panel.add(enrollTeacher, c);

        c.gridx = 6;
        c.gridy = 8;
        c.ipady = 15;
        c.insets = new Insets(30,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        addOrRemoveCourse.addActionListener(switchWindows);
        panel.add(addOrRemoveCourse, c);

        c.gridx = 2;
        c.gridy = 9;
        c.insets = new Insets(30,0,0,100);
        exportData.addActionListener(switchWindows);
        panel.add(exportData, c);

        c.gridx = 6;
        c.gridy = 9;
        c.insets = new Insets(30,0,0,0);
        classTeacher.addActionListener(switchWindows);
        panel.add(classTeacher, c);


        panel.setPreferredSize(new Dimension(850, 355));
        panel.setBackground(Color.WHITE);
        mainMenu.add(panel, BorderLayout.SOUTH);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true);
    }
    // Registers teacher to class
    public void classTeacherRegistrationInterface() {
        JPanel panel = new JPanel(null);
        classTeacherRegistration.getContentPane().add(panel);
        // Teachers
        JLabel student = new JLabel("Teacher: ");
        student.setSize(100, 20);
        student.setLocation(50, 50);
        panel.add(student);

        String[] listOfTeacherNames = new String[Teacher.listOfTeachers.length];
        for (int i = 0; i < Teacher.numOfTeachers; i++) {
            listOfTeacherNames[i] = ((String) Teacher.listOfTeachers[i][0]);
        }
        JComboBox<String> enterTeacher = new JComboBox<>(listOfTeacherNames);
        enterTeacher.setSize(100, 30);

        enterTeacher.setSize(100, 30);
        enterTeacher.setLocation(125, 45);
        panel.add(enterTeacher);

        // Class List
        String[] columnNames = {"Department", "Class", "Section", "Day", "Time", "Teacher", "Credits"};
        Object[][] data = new Object[100][columnNames.length];
        for (int i = 0; i < Class.numOfClasses; i++) {
            for (int j = 0; j < columnNames.length; j++) {
                data[i][j] = Class.listOfClasses[i][j];
            }
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        tableOfClasses = new JTable(model);
        tableOfClasses.setRowSorter(sorter);
        tableOfClasses.setBackground(Color.WHITE);
        JScrollPane scroller = new JScrollPane(tableOfClasses);
        scroller.setSize(450,400);
        scroller.setLocation(450, 50);
        panel.add(scroller);

        // Filters
        JLabel border = new JLabel();
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        border.setSize(375,345);
        border.setLocation(35,110); // 300, 260
        panel.add(border);

        JLabel filter = new JLabel("Filters: ");
        filter.setSize(100, 20);
        filter.setLocation(50, 130);
        panel.add(filter);
        // By Credits
        JLabel byCredits = new JLabel("Credits: ");
        byCredits.setSize(100, 20);
        byCredits.setLocation(75, 175);
        panel.add(byCredits);
        JComboBox<String> filterByCredits = new JComboBox<>(new String[]{
                "--", "1", "2", "3", "4"
        });
        filterByCredits.setSize(50,30);
        filterByCredits.setLocation(150, 175);
        panel.add(filterByCredits);
        // By Department
        JLabel byDepartment = new JLabel("Department: ");
        byDepartment.setSize(100, 20);
        byDepartment.setLocation(75, 250);
        panel.add(byDepartment);
        JComboBox<String> filterByDepartment = new JComboBox<>(new String[]{
                "--", "MATH", "PHYS", "ENGR", "CMSC", "SCDC", "CHEM", "PSYC",
                "JOUR", "ART"
        });
        filterByDepartment.setSize(100,30);
        filterByDepartment.setLocation(175, 250);
        panel.add(filterByDepartment);

        // By Days
        JLabel byDays = new JLabel("Days: ");
        byDays.setSize(100, 20);
        byDays.setLocation(75, 325);
        panel.add(byDays);

        JRadioButton monday = new JRadioButton("Monday");
        monday.setSize(75, 20);
        monday.setLocation(125, 325);
        monday.setSelected(false);
        panel.add(monday);

        JRadioButton tuesday = new JRadioButton("Tuesday");
        tuesday.setSize(75, 20);
        tuesday.setLocation(210, 325);
        tuesday.setSelected(false);
        panel.add(tuesday);

        JRadioButton wednesday = new JRadioButton("Wednesday");
        wednesday.setSize(75, 20);
        wednesday.setLocation(295, 325);
        wednesday.setSelected(false);
        panel.add(wednesday);

        JRadioButton thursday = new JRadioButton("Thursday");
        thursday.setSize(75, 20);
        thursday.setLocation(125, 350);
        thursday.setSelected(false);
        panel.add(thursday);

        JRadioButton friday = new JRadioButton("Friday");
        friday.setSize(75, 20);
        friday.setLocation(210, 350);
        friday.setSelected(false);
        panel.add(friday);

        JButton applyFilter = new JButton("Apply Filter(s)");
        applyFilter.setSize(130, 30);
        applyFilter.setLocation(125,400);
        panel.add(applyFilter);

        // Action Buttons
        JButton add = new JButton("Enroll");
        JButton remove = new JButton("Withdraw");
        JButton back = new JButton("<---");
        add.setSize(100, 25);
        add.setLocation(165,475);
        back.setSize(100,25);
        back.setLocation(40, 475);
        remove.setSize(100, 25);
        remove.setLocation(290, 475);
        panel.add(add);
        panel.add(back);
        panel.add(remove);

        if (Class.numOfClasses == 0) {
            add.setEnabled(false);
            remove.setEnabled(false);
        }

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == add) {
                    int index = enterTeacher.getSelectedIndex();
                    Class classTaught = Class.directoryOfClasses[tableOfClasses.getSelectedRow()];
                    Teacher.directoryOfTeachers[index].addClassTaught(classTaught,
                            Teacher.directoryOfTeachers[index].fullName);
                    JOptionPane.showMessageDialog(null, "Teacher Enrolled!");
                } else if (e.getSource() == back) {
                    classTeacherRegistration.setVisible(false);
                    Driver.main(new String[]{});
                } else if (e.getSource() == applyFilter) {
                    if (!(filterByDepartment.getItemAt(filterByDepartment.getSelectedIndex()).equals("--"))) {
                        sorter.setRowFilter(RowFilter.regexFilter
                                (filterByDepartment.getItemAt(filterByDepartment.getSelectedIndex())));
                    }
                    if (!(filterByCredits.getItemAt(filterByDepartment.getSelectedIndex()).equals("--"))) {
                        sorter.setRowFilter(RowFilter.regexFilter
                                (filterByCredits.getItemAt(filterByDepartment.getSelectedIndex())));
                    }
                    if (monday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Monday"));
                    }
                    if (tuesday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Tuesday"));
                    }
                    if (wednesday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Wednesday"));
                    }
                    if (thursday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Thursday"));
                    }
                    if (friday.isSelected()) {
                        sorter.setRowFilter(RowFilter.regexFilter("Friday"));
                    }
                } else { // e.getSource == remove
                    int index = enterTeacher.getSelectedIndex();
                    Class classTaught = Class.directoryOfClasses[tableOfClasses.getSelectedRow()];
                    Teacher.directoryOfTeachers[index].removeClassTaught(classTaught,
                            Teacher.directoryOfTeachers[index].fullName);
                    JOptionPane.showMessageDialog(null, "Teacher Withdrawn!");
                }
            }
        };
        add.addActionListener(input);
        back.addActionListener(input);
        remove.addActionListener(input);
        panel.setBackground(Color.LIGHT_GRAY);
        classTeacherRegistration.setSize(950,550);
        classTeacherRegistration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        classTeacherRegistration.setResizable(false);
        classTeacherRegistration.setVisible(true);
    }
    // Saves all data into a text file
    public void saveUserData() throws IOException {
        File studentData = new File("schoolstudentdata.txt");
        FileWriter fw = new FileWriter(studentData, false);
        PrintWriter pw = new PrintWriter(fw, true);

        pw.println("Students:");
        for (int index = 0; index < Student.numOfStudents; index++) {
            for (int info = 0; info < 7; info++) {
                pw.println(Student.listOfStudents[index][info]);
            }
        }
        pw.close();
        File teacherData = new File("schoolteacherdata.txt");
        FileWriter fw2 = new FileWriter(teacherData, false);
        PrintWriter pw2 = new PrintWriter(fw2, true);

        pw2.println("Teachers:");
        for (int index = 0; index < Teacher.numOfTeachers; index++) {
            for (int info = 0; info < 6; info++) {
                pw2.println(Teacher.listOfTeachers[index][info]);
            }
        }
        pw2.close();
        File classData = new File("schoolclassdata.txt");
        FileWriter fw3 = new FileWriter(classData, false);
        PrintWriter pw3 = new PrintWriter(fw3, true);

        pw3.println("Classes:");
        for (int index = 0; index < Class.numOfClasses; index++) {
            for (int info = 0; info < 7; info++) {
                pw3.println(Class.listOfClasses[index][info]);
            }
        }
        pw3.close();
        JOptionPane.showMessageDialog(null, "Data Saved!");
    }
}
