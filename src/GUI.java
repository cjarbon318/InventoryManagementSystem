import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    // create a method to show the main screen and add buttons to navigate to other screens
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton enterNewProductButton = new JButton("Add new product to inventory");
        enterNewProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonActions.showEnterNewProductScreen();
            }
        });

        JButton viewInventoryButton = new JButton("View Inventory");
        viewInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonActions.showInventoryScreen();
            }
        }); 

        mainPanel.add(enterNewProductButton);
        mainPanel.add(viewInventoryButton);

        frame.getContentPane().add(mainPanel);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
