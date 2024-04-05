
import javax.swing.*;
import java.awt.*;


public class GUI {

    public GUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton enterNewProductButton = new JButton("Add new product to inventory");
        enterNewProductButton.addActionListener(e -> ButtonActions.showEnterNewProductScreen());

        JButton viewInventoryButton = new JButton("View Inventory");
        viewInventoryButton.addActionListener(e -> ButtonActions.showInventoryScreen());

        mainPanel.add(enterNewProductButton);
        mainPanel.add(viewInventoryButton);

        frame.getContentPane().add(mainPanel);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
