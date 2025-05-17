import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CompressorGUI extends JFrame {

    private JButton compressButton;
    private JButton decompressButton;
    private JButton selectFileButton;
    private JTextArea resultTextArea;
    private JLabel statusLabel;
    private JFileChooser fileChooser;
    private File selectedFile;

    public CompressorGUI() {
        // Set up the window
        setTitle("File Compressor/Decompressor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up components
        compressButton = new JButton("Compress");
        decompressButton = new JButton("Decompress");
        selectFileButton = new JButton("Select File");
        resultTextArea = new JTextArea(10, 40);
        statusLabel = new JLabel("Select a file and choose an action.");
        fileChooser = new JFileChooser();

        // Allow all types of files for compression and decompression
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text and Image Files", "txt", "jpg", "jpeg", "png"));

        // Layout the components
        setLayout(new FlowLayout());
        add(selectFileButton);
        add(compressButton);
        add(decompressButton);
        add(new JScrollPane(resultTextArea));
        add(statusLabel);

        // Action listener for the select file button
        selectFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        // Action listener for the compress button
        compressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    processFile(true); // true for compression
                }
            }
        });

        // Action listener for the decompress button
        decompressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    processFile(false); // false for decompression
                }
            }
        });
    }

    // Select a file using the file chooser
    private void selectFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Selected file: " + selectedFile.getName());
        }
    }

    // Process file for compression or decompression
    private void processFile(boolean isCompress) {
        try {
            byte[] data = FileHandler.readFile(selectedFile);

            if (isCompress) {
                byte[] compressedData = HuffmanAlgorithm.compress(data);
                String compressedText = new String(compressedData);
                resultTextArea.setText("Compressed Data:\n" + compressedText);
                // Save the compressed file
                File compressedFile = new File(selectedFile.getParent(), selectedFile.getName() + ".huff");
                FileHandler.writeFile(compressedFile, compressedData);
                statusLabel.setText("File compressed successfully: " + compressedFile.getName());
            } else {
                byte[] decompressedData = HuffmanAlgorithm.decompress(data);
                String decompressedText = new String(decompressedData);
                resultTextArea.setText("Decompressed Data:\n" + decompressedText);
                // Save the decompressed file
                File decompressedFile = new File(selectedFile.getParent(), selectedFile.getName() + "_decompressed.txt");
                FileHandler.writeFile(decompressedFile, decompressedData);
                statusLabel.setText("File decompressed successfully: " + decompressedFile.getName());
            }
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CompressorGUI().setVisible(true);
            }
        });
    }
}
