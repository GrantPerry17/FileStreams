
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame
{
    // File used to store Product records
    private static final String FILE_NAME = "RandProductData.dat";

    // Each Java character uses 2 bytes.
    // The double uses 8 bytes.
    private static final int RECORD_SIZE =
            (Product.NAME_LENGTH
            + Product.DESCRIPTION_LENGTH
            + Product.ID_LENGTH) * 2
            + Double.BYTES;

    // GUI input fields
    private JTextField idField;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField costField;
    private JTextField countField;

    private int recordCount;

    public RandProductMaker()
    {
        setTitle("Random Access Product Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);

        createGUI();
        loadRecordCount();

        setVisible(true);
    }

    private void createGUI()
    {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Product ID
        idField = new JTextField(25);
        addRow(mainPanel, gbc, 0, "Product ID:", idField);

        // Product Name
        nameField = new JTextField(25);
        addRow(mainPanel, gbc, 1, "Product Name:", nameField);

        // Product Description
        descriptionField = new JTextField(25);
        addRow(mainPanel, gbc, 2, "Description:", descriptionField);

        // Product Cost
        costField = new JTextField(25);
        addRow(mainPanel, gbc, 3, "Product Cost:", costField);

        // Record Count
        countField = new JTextField(10);
        countField.setEditable(false);
        addRow(mainPanel, gbc, 4, "Record Count:", countField);

        // Buttons
        JButton addButton = new JButton("Add Product");
        JButton quitButton = new JButton("Quit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(quitButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        addButton.addActionListener(e -> addProduct());
        quitButton.addActionListener(e -> System.exit(0));

        add(mainPanel);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int row, String label, JTextField field)
    {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(field, gbc);
    }

    private void loadRecordCount()
    {
        File file = new File(FILE_NAME);

        if(file.exists())
        {
            long fileLength = file.length();

            if(fileLength % RECORD_SIZE != 0)
            {
                JOptionPane.showMessageDialog(this,
                        "The existing data file has an invalid size.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }

            recordCount = (int)(fileLength / RECORD_SIZE);
        }
        else
        {
            recordCount = 0;
        }

        countField.setText(String.valueOf(recordCount));
    }

    private void addProduct()
    {
        // Retrieve and trim input
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String costText = costField.getText().trim();

        // Validate empty fields
        if(id.isEmpty() || name.isEmpty()
                || description.isEmpty() || costText.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Please enter information in all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        // Validate maximum field lengths
        if(id.length() > Product.ID_LENGTH)
        {
            showError("Product ID cannot exceed 6 characters.");
            return;
        }

        if(name.length() > Product.NAME_LENGTH)
        {
            showError("Product Name cannot exceed 35 characters.");
            return;
        }

        if(description.length() > Product.DESCRIPTION_LENGTH)
        {
            showError("Description cannot exceed 75 characters.");
            return;
        }

        // Validate product cost
        double cost;

        try
        {
            cost = Double.parseDouble(costText);

            if(!Double.isFinite(cost) || cost < 0)
            {
                showError("Please enter a valid non-negative cost.");
                return;
            }
        }
        catch(NumberFormatException e)
        {
            showError("Product Cost must be a valid number.");
            return;
        }

        // Create Product object
        Product product = new Product(name, description, id, cost);

        // Write Product to RandomAccessFile
        try(RandomAccessFile file =
                    new RandomAccessFile(FILE_NAME, "rw"))
        {
            file.seek(file.length());

            file.writeChars(product.getRandomName());
            file.writeChars(product.getRandomDescription());
            file.writeChars(product.getRandomID());
            file.writeDouble(product.getCost());

            recordCount++;

            countField.setText(String.valueOf(recordCount));

            JOptionPane.showMessageDialog(this,
                    "Product successfully added!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear fields for next Product
            idField.setText("");
            nameField.setText("");
            descriptionField.setText("");
            costField.setText("");

            idField.requestFocus();

        }
        catch(IOException e)
        {
            showError("Error writing Product file: " + e.getMessage());
        }
    }

    private void showError(String message)
    {
        JOptionPane.showMessageDialog(this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new RandProductMaker());
    }
}
