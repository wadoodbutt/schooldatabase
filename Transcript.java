import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Transcript {

    JFrame frame;
    JPanel panel;
    JTable table;

    public Transcript() {
        frame = new JFrame("Transcript");
        panel = new JPanel(null);
    }

    public void showTranscript(Student student) {
        frame.getContentPane().add(panel);

        JLabel fullName = new JLabel("Name:");
        fullName.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        fullName.setSize(50, 30);
        fullName.setLocation(20, 20);
        panel.add(fullName);

        String name = student.getFullName().toUpperCase(Locale.ROOT);
        JLabel displayName = new JLabel(name);
        displayName.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayName.setSize(200, 30);
        displayName.setLocation(100, 20);
        panel.add(displayName);
        // ID
        JLabel ID = new JLabel("ID:");
        ID.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        ID.setSize(100, 30);
        ID.setLocation(20, 70);
        panel.add(ID);

        String strID = String.valueOf(student.getStudentID());
        JLabel displayID = new JLabel(strID);
        displayID.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayID.setSize(100, 30);
        displayID.setLocation(100, 70);
        panel.add(displayID);
        // Age
        JLabel age = new JLabel("Age:");
        age.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        age.setSize(100, 30);
        age.setLocation(20, 120);
        panel.add(age);

        String strAge = String.valueOf(student.getAge());
        JLabel displayAge = new JLabel(strAge);
        displayAge.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayAge.setSize(100, 30);
        displayAge.setLocation(100, 120);
        panel.add(displayAge);
        // Grade
        JLabel grade = new JLabel("Grade:");
        grade.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        grade.setSize(100, 30);
        grade.setLocation(20, 170);
        panel.add(grade);

        String strGrade = student.getGradeLevel().toUpperCase(Locale.ROOT);
        JLabel displayGrade = new JLabel(strGrade);
        displayGrade.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayGrade.setSize(100, 30);
        displayGrade.setLocation(100, 170);
        panel.add(displayGrade);
        // D.O.B
        JLabel dob = new JLabel("D.O.B:");
        dob.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        dob.setSize(50, 30);
        dob.setLocation(20, 220);
        panel.add(dob);

        String strDOB = student.getDOB().toUpperCase(Locale.ROOT);
        JLabel displayDOB = new JLabel(strDOB);
        displayDOB.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayDOB.setSize(200, 30);
        displayDOB.setLocation(100, 220);
        panel.add(displayDOB);
        // Sex
        JLabel sex = new JLabel("Sex:");
        sex.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        sex.setSize(100, 30);
        sex.setLocation(20, 270);
        panel.add(sex);

        String strSex = student.getSex().toUpperCase(Locale.ROOT);
        JLabel displaySex = new JLabel(strSex);
        displaySex.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displaySex.setSize(100, 30);
        displaySex.setLocation(100, 270);
        panel.add(displaySex);
        // Major
        JLabel major = new JLabel("Major:");
        major.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        major.setSize(100, 30);
        major.setLocation(20, 320);
        panel.add(major);

        // Makes the student's the first major appear in the list of all majors
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < Department.numOfDepartments; i++) {
            temp.add(Department.listOfDepartments[i]);
        }
        System.out.println(temp.size());
        temp.remove(student.getMajor());
        temp.add(0, student.getMajor());
        Object[] temp2 = temp.toArray();
        String[] listOfMajors = new String[temp2.length];
        for (int i = 0; i < temp2.length; i++) {
            listOfMajors[i] = temp2[i].toString();
        }

        JComboBox<String> displayMajor = new JComboBox<>(listOfMajors);
        displayMajor.setSize(100, 30);
        displayMajor.setLocation(100, 320);
        displayMajor.setEnabled(false);
        panel.add(displayMajor);
        // Classes
        String[] columns = {"Class", "Credits", "Teacher", "Grade"};
        Object[][] data = new Object[student.getNumOfClasses()][columns.length];
        for (Class classes : student.classes) {
            int classIndex  = 0;
            for (int index = 0; index < columns.length; index++) {
                switch (index) {
                    case 0:
                        data[classIndex][index] = classes.getClassName();
                        break;
                    case 1:
                        data[classIndex][index] = classes.getCredits();
                        break;
                    case 2:
                        data[classIndex][index] = classes.getTeacher();
                        break;
                    case 3:
                        data[classIndex][index] = student.getGradeForClass(classes);
                        break;
                }
            }
            classIndex++;
        }
        DefaultTableModel model = new DefaultTableModel(data, columns);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table = new JTable(model);
        table.setRowSorter(sorter);
        table.setBackground(Color.WHITE);
        JScrollPane scroller = new JScrollPane(table);
        scroller.setSize(400, 400);
        scroller.setLocation(300,20);
        panel.add(scroller);

        // Action Buttons
        JButton changeMajor = new JButton("Change Major");
        JButton showSchedule = new JButton("Schedule");
        JButton setMajor = new JButton("Set Major");

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == changeMajor) {
                    displayMajor.setEnabled(true);
                    changeMajor.setVisible(false);
                    changeMajor.setEnabled(false);
                    setMajor.setVisible(true);
                    setMajor.setEnabled(true);
                } else if (e.getSource() == setMajor) {
                    student.setMajor(displayMajor.getItemAt(displayMajor.getSelectedIndex()));
                    int index = 0;
                    for (int i = 0; i < Student.numOfStudents; i++) {
                        if (Student.directoryOfStudents[i].equals(student)) {
                            break;
                        }
                        index++;
                    }
                    Student.listOfStudents[index][6] = displayMajor.getItemAt(displayMajor.getSelectedIndex());
                    setMajor.setVisible(false);
                    setMajor.setEnabled(false);
                    changeMajor.setVisible(true);
                    changeMajor.setEnabled(true);
                    displayMajor.setEnabled(false);
                    JOptionPane.showMessageDialog(null,"Major Changed!");
                }
            }
        };
        setMajor.setSize(120, 25);
        setMajor.setLocation(15, 370);
        setMajor.setVisible(false);
        setMajor.setEnabled(false);
        setMajor.addActionListener(input);

        changeMajor.setSize(120, 25);
        showSchedule.setSize(120, 25);
        changeMajor.setLocation(15, 370);
        showSchedule.setLocation(155, 370);
        changeMajor.addActionListener(input);
        showSchedule.addActionListener(input);
        panel.add(changeMajor);
        panel.add(showSchedule);
        panel.add(setMajor);

        panel.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(750, 475);
    }


    public void showOverview(Teacher teacher) {
        frame.getContentPane().add(panel);

        JLabel fullName = new JLabel("Name:");
        fullName.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        fullName.setSize(50, 30);
        fullName.setLocation(20, 20);
        panel.add(fullName);

        String name = teacher.fullName.toUpperCase(Locale.ROOT);
        JLabel displayName = new JLabel(name);
        displayName.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayName.setSize(200, 30);
        displayName.setLocation(100, 20);
        panel.add(displayName);
        // ID
        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        email.setSize(100, 30);
        email.setLocation(20, 70);
        panel.add(email);

        String strEmail = teacher.email;
        JLabel displayEmail = new JLabel(strEmail);
        displayEmail.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayEmail.setSize(100, 30);
        displayEmail.setLocation(100, 70);
        panel.add(displayEmail);
        // Age
        JLabel age = new JLabel("Age:");
        age.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        age.setSize(100, 30);
        age.setLocation(20, 120);
        panel.add(age);

        String strAge = String.valueOf(teacher.getAge());
        JLabel displayAge = new JLabel(strAge);
        displayAge.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayAge.setSize(100, 30);
        displayAge.setLocation(100, 120);
        panel.add(displayAge);

        // Phone Number
        JLabel contactNumber = new JLabel("Contact Number:");
        contactNumber.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        contactNumber.setSize(50, 30);
        contactNumber.setLocation(20, 170);
        panel.add(contactNumber);

        String strContactNumber = teacher.phoneNumber;
        JLabel displayContactNumber = new JLabel(strContactNumber);
        displayContactNumber.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displayContactNumber.setSize(200, 30);
        displayContactNumber.setLocation(100, 170);
        panel.add(displayContactNumber);
        // Sex
        JLabel sex = new JLabel("Sex:");
        sex.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        sex.setSize(100, 30);
        sex.setLocation(20, 220);
        panel.add(sex);

        String strSex = teacher.gender.toUpperCase(Locale.ROOT);
        JLabel displaySex = new JLabel(strSex);
        displaySex.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
        displaySex.setSize(100, 30);
        displaySex.setLocation(100, 220);
        panel.add(displaySex);
        // Department
        JLabel major = new JLabel("Department:");
        major.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        major.setSize(100, 30);
        major.setLocation(20, 270);
        panel.add(major);

        // Makes the teacher's department appear in the list of all majors
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < Department.numOfDepartments; i++) {
            temp.add(Department.listOfDepartments[i]);
        }
        System.out.println(temp.size());
        temp.remove(teacher.department);
        temp.add(0, teacher.department);
        Object[] temp2 = temp.toArray();
        String[] listOfDepartments = new String[temp2.length];
        for (int i = 0; i < temp2.length; i++) {
            listOfDepartments[i] = temp2[i].toString();
        }

        JComboBox<String> displayDepartment = new JComboBox<>(listOfDepartments);
        displayDepartment.setSize(100, 30);
        displayDepartment.setLocation(100, 320);
        displayDepartment.setEnabled(false);
        panel.add(displayDepartment);
        // Classes Taught
        String[] columns = {"Class", "Credits"};
        Object[][] data = new Object
                [teacher.classesTaught.get(teacher.fullName).size()][columns.length];
        for (String teacherName : teacher.classesTaught.keySet()) {
            int classIndex  = 0;
            for (int index = 0; index < columns.length; index++) {
                switch (index) {
                    case 0:
                        data[classIndex][index] = Class.getClassViaName(teacherName).getClassName();
                        break;
                    case 1:
                        System.out.println(teacherName);
                        data[classIndex][index] = Class.getClassViaName(teacherName).getCredits();
                        break;
                }
            }
            classIndex++;
        }
        DefaultTableModel model = new DefaultTableModel(data, columns);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table = new JTable(model);
        table.setRowSorter(sorter);
        table.setBackground(Color.WHITE);
        JScrollPane scroller = new JScrollPane(table);
        scroller.setSize(400, 400);
        scroller.setLocation(300,20);
        panel.add(scroller);

        // Action Buttons
        JButton changeDepartment = new JButton("Change Department");
        JButton setDepartment = new JButton("Set Department");

        ActionListener input = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == changeDepartment) {
                    displayDepartment.setEnabled(true);
                    changeDepartment.setVisible(false);
                    changeDepartment.setEnabled(false);
                    setDepartment.setVisible(true);
                    setDepartment.setEnabled(true);
                } else if (e.getSource() == setDepartment) {
                    teacher.setDepartment(displayDepartment.getItemAt(displayDepartment.getSelectedIndex()));
                    int index = 0;
                    for (int i = 0; i < Student.numOfStudents; i++) {
                        if (Teacher.directoryOfTeachers[i].equals(teacher)) {
                            break;
                        }
                        index++;
                    }
                    Teacher.listOfTeachers[index][4] = displayDepartment.getItemAt(displayDepartment.getSelectedIndex());
                    setDepartment.setVisible(false);
                    setDepartment.setEnabled(false);
                    changeDepartment.setVisible(true);
                    changeDepartment.setEnabled(true);
                    displayDepartment.setEnabled(false);
                    JOptionPane.showMessageDialog(null,"Department Changed!");
                }
            }
        };
        setDepartment.setSize(150, 25);
        setDepartment.setLocation(30, 370);
        setDepartment.setVisible(false);
        setDepartment.setEnabled(false);
        setDepartment.addActionListener(input);

        changeDepartment.setSize(150, 25);
        changeDepartment.setLocation(30, 370);
        changeDepartment.addActionListener(input);
        panel.add(changeDepartment);
        panel.add(setDepartment);

        panel.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(750, 475);
    }
}
