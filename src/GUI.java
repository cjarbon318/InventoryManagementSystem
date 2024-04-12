
import javax.swing.*;
import java.awt.*;


public class GUI {

    public GUI() {
        createAndShowGUI();
    }
    // Create the GUI and show it using the Event Dispatch Thread

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        // Create the buttons to add new product and view inventory and add them to the main panel
        JButton enterNewProductButton = new JButton("Add new product to inventory");
        enterNewProductButton.addActionListener(e -> ButtonActions.showEnterNewProductScreen());

        JButton viewInventoryButton = new JButton("View Inventory");
        viewInventoryButton.addActionListener(e -> ButtonActions.showInventoryScreen());

        mainPanel.add(enterNewProductButton);
        mainPanel.add(viewInventoryButton);

        // Add the main panel to the frame
        frame.getContentPane().add(mainPanel);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
