import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class RegistrationForm extends JFrame implements ActionListener {

    // Declaring Swing components
    JTextField nameField, mobileField, dobField, addressField, contactField;
    JRadioButton maleButton, femaleButton;
    JButton submitButton, resetButton;
    JCheckBox termsCheckBox;

    // Database URL and credentials
    final String DB_URL = "jdbc:mysql://localhost:3306/registration_db";
    final String DB_USER = "root";
    final String DB_PASSWORD = "12345ewe";  // Replace with your MySQL password

    public RegistrationForm() {
        // Frame setup
        setTitle("Registration Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));

        // Adding components
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Mobile:"));
        mobileField = new JTextField();
        add(mobileField);

        add(new JLabel("Gender:"));
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        JPanel genderPanel = new JPanel(new FlowLayout());
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        add(genderPanel);

        add(new JLabel("DOB (YYYY-MM-DD):"));
        dobField = new JTextField();
        add(dobField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        add(new JLabel("Contact:"));
        contactField = new JTextField();
        add(contactField);

        termsCheckBox = new JCheckBox("Accept Terms And Conditions");
        add(termsCheckBox);

        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");
        submitButton.addActionListener(this);
        resetButton.addActionListener(this);
        add(submitButton);
        add(resetButton);

        setVisible(true);
    }

    // Action listener for button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (termsCheckBox.isSelected()) {
                submitData();
            } else {
                JOptionPane.showMessageDialog(this, "Please accept terms and conditions.");
            }
        } else if (e.getSource() == resetButton) {
            clearForm();
        }
    }

    // Method to clear form fields
    private void clearForm() {
        nameField.setText("");
        mobileField.setText("");
        dobField.setText("");
        addressField.setText("");
        contactField.setText("");
        genderGroup.clearSelection();
        termsCheckBox.setSelected(false);
    }

    // Method to submit data to the database
    private void submitData() {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = maleButton.isSelected() ? "Male" : "Female";
        String dob = dobField.getText();
        String address = addressField.getText();
        String contact = contactField.getText();

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to insert data
            String sql = "INSERT INTO registration (name, mobile, gender, dob, address, contact) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, mobile);
            stmt.setString(3, gender);
            stmt.setString(4, dob);
            stmt.setString(5, address);
            stmt.setString(6, contact);

            // Execute query
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed!");
            }

            // Close resources
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new RegistrationForm();
    }
}
