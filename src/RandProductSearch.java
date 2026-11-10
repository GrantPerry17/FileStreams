
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductSearch extends JFrame
{
    // Binary Product data file
    private static final String FILE_NAME = "RandProductData.dat";

    // Fixed record size in bytes
    private static final int RECORD_SIZE =
            (Product.NAME_LENGTH
            + Product.DESCRIPTION_LENGTH
            + Product.ID_LENGTH) * 2
            + Double.BYTES;

    // GUI components
    private JTextField searchField;
    private JTextArea resultsArea;

    public RandProductSearch()
    {
        setTitle("Random Access Product Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        createGUI();

        setVisible(true);
    }

    private void createGUI()
    {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search input panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel searchLabel = new JLabel("Partial Product Name:");

        searchField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(quitButton);

        // Results display area
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(resultsArea);

        // Add components to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button actions
        searchButton.addActionListener(e -> searchProducts());

        searchField.addActionListener(e -> searchProducts());

        quitButton.addActionListener(e -> System.exit(0));

        add(mainPanel);
    }

    private void searchProducts()
    {
        String searchTerm = searchField.getText().trim();

        // Validate search input
        if(searchTerm.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Please enter a product name to search.",
                    "Search Required",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        File dataFile = new File(FILE_NAME);

        // Check whether the data file exists
        if(!dataFile.exists())
        {
            resultsArea.setText("Product data file was not found.");
            return;
        }

        int matchCount = 0;

        resultsArea.setText("");

        // Read Product records
        try(RandomAccessFile file =
                    new RandomAccessFile(dataFile, "r"))
        {
            if(file.length() % RECORD_SIZE != 0)
            {
                resultsArea.setText(
                        "Error: Product data file has an invalid size.");
                return;
            }

            long totalRecords = file.length() / RECORD_SIZE;

            for(int i = 0; i < totalRecords; i++)
            {
                // Move to the beginning of the next record
                file.seek((long)i * RECORD_SIZE);

                // Read fixed-length Product fields
                String name = readFixedString(
                        file, Product.NAME_LENGTH);

                String description = readFixedString(
                        file, Product.DESCRIPTION_LENGTH);

                String id = readFixedString(
                        file, Product.ID_LENGTH);

                double cost = file.readDouble();

                // Check for partial name match
                if(name.toLowerCase().contains(
                        searchTerm.toLowerCase()))
                {
                    matchCount++;

                    resultsArea.append(
                            "Product " + matchCount + "\n");

                    resultsArea.append(
                            "----------------------------------------\n");

                    resultsArea.append("Product ID: " + id + "\n");
                    resultsArea.append("Name: " + name + "\n");
                    resultsArea.append(
                            "Description: " + description + "\n");

                    resultsArea.append(
                            String.format("Cost: $%.2f%n", cost));

                    resultsArea.append("\n");
                }
            }

            // Display search summary
            if(matchCount == 0)
            {
                resultsArea.setText(
                        "No products found matching: " + searchTerm);
            }
            else
            {
                resultsArea.append(
                        "Search completed. Total matches: "
                                + matchCount);
            }
        }
        catch(IOException e)
        {
            resultsArea.setText(
                    "Error reading Product data file: "
                            + e.getMessage());
        }
    }

    /**
     * Reads a fixed-length String from a RandomAccessFile.
     * @param file the file being read
     * @param length the number of characters to read
     * @return the String without padding spaces
     * @throws IOException if a file reading error occurs
     */
    private String readFixedString(RandomAccessFile file, int length)
            throws IOException
    {
        StringBuilder value = new StringBuilder();

        for(int i = 0; i < length; i++)
        {
            value.append(file.readChar());
        }

        return value.toString().trim();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(
                () -> new RandProductSearch());
    }
}
