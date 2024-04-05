import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                    String sql = "INSERT INTO invmgmt (product_name, supplier, quantity, price) VALUES (?, ?, ?, ?)";

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

    // create a method to show the inventory screen
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

        Double totalSum = (double) 0;
        for (Product product : productInventory) {
            totalSum += product.getPrice() * product.getQuantity();
        }
        String formattedTotalSum = String.format("%.2f", totalSum);

        for (Product product : productInventory) {
            String formattedPrice = String.format("%.2f", product.getPrice());
            double totalPrice = product.getPrice() * product.getQuantity();

            String formattedTotalPrice = String.format("%.2f", totalPrice);

            tableModel.addRow(new Object[] {
                    product.getProduct(),
                    formattedPrice,
                    product.getSupplier(),
                    product.getQuantity(),
                    formattedTotalPrice
            });
        }

        // Add the table to a JScrollPane for scrollability
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();

        // Add a button to delete the selected entry

        JButton deleteButton = new JButton("Delete Selected Entry");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Remove the row from the table model
                    tableModel.removeRow(selectedRow);

                    // Remove the product from the product inventory list
                    Product removedProduct = productInventory.remove(selectedRow);

                    // Delete the product from the SQLite database
                    try {

                        Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/carliarbon/infosys.db");

                        String sql = "DELETE FROM invmgmt  WHERE product_name = ?";

                        // Create a prepared statement with the SQL statement
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, removedProduct.getProduct());

                        // Execute the statement
                        pstmt.executeUpdate();

                        // Close the resources
                        pstmt.close();
                        conn.close();

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

        // Add a button to save changes
        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Changes Saved!");
            }
        });

        // Add a button to save the inventory as a CSV file
        // EDIT to write to sqlite here
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
                        writeInventoryToCSV(fileToSave, productInventory);
                        JOptionPane.showMessageDialog(null, "Inventory saved as CSV!");

                        // Save inventory data to SQLite database
                        saveInventoryToDatabase(productInventory);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving CSV file: " + ex.getMessage());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error saving inventory to database: " + ex.getMessage());
                    }
                }
            }
        });

        // Add buttons to buttonPanel
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveChangesButton);
        buttonPanel.add(saveCsvButton);
        // buttonPanel.add(backButton);
        JLabel totalSumLabel = new JLabel("Total Sum of Inventory: $" + formattedTotalSum);
        buttonPanel.add(totalSumLabel);

        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);
        inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);

        productInventoryFrame.getContentPane().add(inventoryPanel);
        productInventoryFrame.setSize(800, 400);
        productInventoryFrame.setLocationRelativeTo(null);
        productInventoryFrame.setVisible(true);

    }
    // create a method to write the inventory to a CSV file

    private static void writeInventoryToCSV(File file, List<Product> inventory) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            for (Product product : inventory) {
                writer.write(product.getProduct() + "," +
                        product.getSupplier() + "," +
                        product.getQuantity() + "," +
                        product.getPrice() + "\n");
            }
        }
    }

    // Method to save inventory data to SQLite database
    private static void saveInventoryToDatabase(List<Product> inventory) throws SQLException {
        // Establish a connection to the SQLite database
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/carliarbon/infosys.db")) {
            // Clear existing data
            try (PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM invmgmt ")) {
                clearStmt.executeUpdate();
            }

            // Insert new inventory data using prepared statements
            String sql = "INSERT INTO infosys (product_name, supplier, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Product product : inventory) {
                    pstmt.setString(1, product.getProduct());
                    pstmt.setString(2, product.getSupplier());
                    pstmt.setFloat(3, product.getQuantity());
                    pstmt.setDouble(4, product.getPrice());
                    pstmt.executeUpdate();
                }
            }
        }
    }
}