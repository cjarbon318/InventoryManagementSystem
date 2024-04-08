import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.List;



public class ButtonActions {

    private static List<Product> productInventory = new ArrayList<>();

    // create a method to show the enter new product screen and add the product to
    // the inventory
    public static void showEnterNewProductScreen() {
        JFrame enterNewProductFrame = new JFrame("Enter New Product to Inventory");
        enterNewProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel newProductPanel = new JPanel();
        newProductPanel.setLayout(new BoxLayout(newProductPanel, BoxLayout.Y_AXIS));

        JTextField productNameField = new JTextField(20);
        JTextField supplierField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);

        newProductPanel.add(new JLabel("Product Name:"));
        newProductPanel.add(productNameField);
        newProductPanel.add(new JLabel("Supplier:"));
        newProductPanel.add(supplierField);
        newProductPanel.add(new JLabel("Price:"));
        newProductPanel.add(priceField);
        newProductPanel.add(new JLabel("Quantity:"));
        newProductPanel.add(quantityField);

        // Create a panel for the submit button
        JPanel submitButtonPanel = new JPanel(new BorderLayout());
        JButton submitButton = new JButton("Submit");
        submitButtonPanel.add(submitButton, BorderLayout.CENTER);

        // add an action listener to the submit button
        // EDIT to make to sqlite here
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product newProduct = new Product(
                        productNameField.getText(),
                        supplierField.getText(),
                        Float.parseFloat(quantityField.getText()),
                        Double.parseDouble(priceField.getText()));
                // System.out.println("I am here");
                try {
                    // Establish a connection to the SQLite database
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/carliarbon/infosys.db");

                    // Create a SQL statement to insert the new product into the database
                    String sql = "INSERT INTO invmgmt (product, supplier, quantity, price) VALUES (?, ?, ?, ?)";

                    // Create a prepared statement with the SQL statement
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newProduct.getProduct());
                    pstmt.setString(2, newProduct.getSupplier());
                    pstmt.setFloat(3, newProduct.getQuantity());
                    pstmt.setDouble(4, newProduct.getPrice());

                    // Execute the statement
                    pstmt.executeUpdate();

                    // Close the resources
                    pstmt.close();
                    conn.close();

                    JOptionPane.showMessageDialog(null, "Product Added!");

                    // Dispose the frame after adding the product
                    enterNewProductFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Unable to add product to database.");
                }
            }
        });

        // Add the submit button panel to the main panel
        newProductPanel.add(submitButtonPanel);

        // Add the main panel to the frame
        enterNewProductFrame.getContentPane().add(newProductPanel);
        enterNewProductFrame.pack();
        enterNewProductFrame.setLocationRelativeTo(null);
        enterNewProductFrame.setVisible(true);
    }
    private static void writeInventoryToCSV(File fileToSave) throws IOException, SQLException {
        System.out.println("Writing inventory data to CSV...");
        // SQLite database URL
        String url = "jdbc:sqlite:/Users/carliarbon/infosys.db";
    
        // SQL query to retrieve inventory data
        String query = "SELECT * FROM invmgmt";
    
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             FileWriter writer = new FileWriter(fileToSave)) {
    
            // Write CSV header
            writer.write("Product,Supplier,Quantity,Price\n");
    
            // Write inventory data to CSV
            while (resultSet.next()) {
                String product = resultSet.getString("product");
                String supplier = resultSet.getString("supplier");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
    
                // Write data row to CSV
                writer.write(product + "," + supplier + "," + quantity + "," + price + "\n");
            }
    
            System.out.println("Inventory data written to CSV successfully.");
        }
    }

    public static void showInventoryScreen() {
        JFrame productInventoryFrame = new JFrame("Inventory List for Company XYZ");
        productInventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        // Create a JTable with a DefaultTableModel
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable productTable = new JTable(tableModel);
    
        // Add columns to the table
        tableModel.addColumn("Product");
        tableModel.addColumn("Price");
        tableModel.addColumn("Supplier");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Total Price of Inventory");
    
        double totalSum = 0; // Declare and initialize totalSum here
    
        try {
            // Establishing a database connection
            Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/carliarbon/infosys.db");
    
            // Creating a statement
            Statement statement = connection.createStatement();
    
            // Executing a query to retrieve data
            String query = "SELECT * FROM invmgmt"; // Assuming table name is invmgmt
            ResultSet resultSet = statement.executeQuery(query);
    
            // Iterating through the result set and populating the table
            while (resultSet.next()) {
                String productName = resultSet.getString("product");
                double price = resultSet.getDouble("price");
                String supplier = resultSet.getString("supplier");
                int quantity = resultSet.getInt("quantity");
                double totalPrice = price * quantity;
                totalSum += totalPrice;
    
                String formattedPrice = String.format("%.2f", price);
                String formattedTotalPrice = String.format("%.2f", totalPrice);
    
                tableModel.addRow(new Object[]{
                        productName,
                        formattedPrice,
                        supplier,
                        quantity,
                        formattedTotalPrice
                });
            }
    
            // Closing resources
            resultSet.close();
            statement.close();
            connection.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to retrieve data from the database.");
            return; // Exit method if an exception occurs
        }
    
        // Add the total sum label
        double formattedTotalSum = Math.round(totalSum * 100.0) / 100.0;
        JLabel totalSumLabel = new JLabel("Total Sum of Inventory: $" + formattedTotalSum);
        productInventoryFrame.getContentPane().add(totalSumLabel, BorderLayout.SOUTH);
    
        // Add the table to the frame
        productInventoryFrame.add(new JScrollPane(productTable), BorderLayout.CENTER);
    
        // Add the button panel
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete Selected Entry");
        buttonPanel.add(deleteButton);
    
        // Add the delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productName = (String) productTable.getValueAt(selectedRow, 0);
    
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/carliarbon/infosys.db");
    
                        String sql = "DELETE FROM invmgmt WHERE product = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, productName);
                        pstmt.executeUpdate();
                        pstmt.close();
                        conn.close();
    
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Selected entry deleted.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error: Unable to delete entry from database.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an entry to delete.");
                }
            }
        });
    
        // Add a selection listener to the table
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Enable the delete button when a row is selected
                        deleteButton.setEnabled(true);
                    } else {
                        // Disable the delete button when no row is selected
                        deleteButton.setEnabled(false);
                    }
                }
            }
        });
    
        // Add a button to save changes
        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Changes Saved!");
            }
        });
        
        // Add a button to save inventory data as CSV
        JButton saveCsvButton = new JButton("Save as CSV");
        saveCsvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Invoke a file chooser dialog to select the destination to save the CSV file
                JFileChooser fileChooser = new JFileChooser();
                int userSelection = fileChooser.showSaveDialog(null);
        
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    try {
                        // Save inventory data to the selected file
                        writeInventoryToCSV(fileToSave);
                        JOptionPane.showMessageDialog(null, "Inventory saved as CSV!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving CSV file: " + ex.getMessage());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error retrieving inventory data from database: " + ex.getMessage());
                    }
                }
            }
        });
        

        // Add buttons to buttonPanel
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveChangesButton);
        buttonPanel.add(saveCsvButton);
        
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        productInventoryFrame.getContentPane().add(inventoryPanel);
        productInventoryFrame.setSize(800, 400);
        productInventoryFrame.setLocationRelativeTo(null);
        productInventoryFrame.setVisible(true);
    }
}

    