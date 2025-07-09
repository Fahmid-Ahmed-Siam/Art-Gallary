package GUI;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Entity.ArtItem;
import Entity.Painting;
import File.UserData;
import java.util.ArrayList;
import java.util.List;

public class ArtFrame extends JFrame implements ActionListener {
    private JPanel panel;
    private JLabel[] imageLabels, priceLabels, quantityLabels;
    private JComboBox<Integer>[] quantityBoxes;
    private JButton saveButton, confirmButton;
    private JLabel totalLabel;
    private JTextArea orderHistoryArea;
    private ImageIcon[] icons;
    private List<ArtItem> paintings;

    public ArtFrame() {
        super("Art Gallery");
        setBounds(20, 20, 1200, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize painting data
        paintings = new ArrayList<>();
        paintings.add(new Painting("Marchs", "Harris", 5000.0));
        paintings.add(new Painting("Steps", "Gregg", 7000.0));
        paintings.add(new Painting("Skies", "lewis", 4500.0));
        paintings.add(new Painting("Hymns", "Stacy", 6000.0));
        paintings.add(new Painting("break", "Adrei", 5500.0));

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(118, 181, 49)); // Avocado green
        add(panel);

        // Initialize arrays
        imageLabels = new JLabel[5];
        priceLabels = new JLabel[5];
        quantityLabels = new JLabel[5];
        quantityBoxes = new JComboBox[5];
        icons = new ImageIcon[5];

        // Add 5 panels for images, prices, and quantities
        for (int i = 0; i < 5; i++) {
            int x = 65 + (210 * i);
            icons[i] = new ImageIcon("Pic/P" + (i + 1) + ".jpg");
            JPanel imagePanel = new JPanel();
            imagePanel.setBounds(x, 50, 200, 200);
            imagePanel.setBackground(new Color(255, 230, 230));
            imageLabels[i] = new JLabel(icons[i]);
            imagePanel.add(imageLabels[i]);
            panel.add(imagePanel);

            // Price Label
            JPanel pricePanel = new JPanel();
            pricePanel.setBounds(x, 260, 200, 25);
            pricePanel.setBackground(Color.WHITE);
            priceLabels[i] = new JLabel(paintings.get(i).getDisplayText());
            pricePanel.add(priceLabels[i]);
            panel.add(pricePanel);

            // Quantity Label
            quantityLabels[i] = new JLabel("Quantity");
            quantityLabels[i].setBounds(x, 290, 80, 25);
            quantityLabels[i].setBackground(Color.WHITE);
            quantityLabels[i].setForeground(Color.RED);
            quantityLabels[i].setOpaque(true);
            panel.add(quantityLabels[i]);

            // Quantity ComboBox
            quantityBoxes[i] = new JComboBox<Integer>();
            for (int j = 0; j <= 10; j++) {
                quantityBoxes[i].addItem(j);
            }
            quantityBoxes[i].setBounds(x + 100, 290, 80, 25);
            quantityBoxes[i].setForeground(Color.RED);
            panel.add(quantityBoxes[i]);
        }

        // Total Price Label
        totalLabel = new JLabel("Total Price: 0");
        totalLabel.setBounds(65, 360, 150, 30);
        totalLabel.setBackground(Color.WHITE);
        totalLabel.setForeground(Color.RED);
        totalLabel.setOpaque(true);
        panel.add(totalLabel);

        // Save Button
        saveButton = new JButton("Purchase");
        saveButton.setBounds(65, 410, 100, 30);
        saveButton.addActionListener(this); // Use ActionListener
        panel.add(saveButton);

        // Confirm Button
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(200, 410, 100, 30);
        confirmButton.addActionListener(this); // Use ActionListener
        panel.add(confirmButton);

        // Order History Text Area
        orderHistoryArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(orderHistoryArea);
        scrollPane.setBounds(365, 360, 500, 100);
        panel.add(scrollPane);

        // Load order history
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        try {
            File file = new File("Painting_purchase_history.txt");
            if (file.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                orderHistoryArea.setText("");
                while ((line = br.readLine()) != null) {
                    orderHistoryArea.append(line + "\n");
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveButton) {
            double totalPrice = 0;
            for (int i = 0; i < 5; i++) {
                int quantity = (Integer) quantityBoxes[i].getSelectedItem();
                totalPrice += quantity * paintings.get(i).getPrice();
            }
            totalLabel.setText("Total Price: " + totalPrice);
        }

        if (ae.getSource() == confirmButton) {
            try {
                List<ArtItem> selectedPaintings = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                double totalPrice = 0;

                // Process selected paintings
                for (int i = 0; i < 5; i++) {
                    int quantity = (Integer) quantityBoxes[i].getSelectedItem();
                    if (quantity > 0) {
                        ArtItem painting = paintings.get(i);
                        selectedPaintings.add(painting);
                        quantities.add(quantity);
                        totalPrice += quantity * painting.getPrice();
                    }
                }

                // Save to file using UserData.saveData
                if (!selectedPaintings.isEmpty()) {
                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm a, dd/MM/yyyy");
                    String timeAndDate = myDateObj.format(myFormatObj);
                    UserData.saveData("Purchase at " + timeAndDate, selectedPaintings, quantities, totalPrice);

                    // Show success message
                    JOptionPane.showMessageDialog(this, "Order saved successfully!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);

                    // Update total price label
                    totalLabel.setText("Total Price: " + totalPrice);

                    // Reload order history
                    loadOrderHistory();
                } else {
                    JOptionPane.showMessageDialog(this, "No paintings selected!", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving order. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}