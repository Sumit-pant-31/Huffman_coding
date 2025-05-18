import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class CompressorGUI extends JFrame 
{

    private JButton selectFileButton, compressButton, decompressButton;
    private JTextArea resultTextArea;
    private JLabel statusLabel;
    private File selectedFile;

    public CompressorGUI() {
        setTitle("Huffman File Compressor / Decompressor");
        setSize(700, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        selectFileButton = new JButton("Select File");
        compressButton = new JButton("Compress");
        decompressButton = new JButton("Decompress");

        resultTextArea = new JTextArea(15, 55);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        statusLabel = new JLabel("No file selected");

        add(selectFileButton);
        add(compressButton);
        add(decompressButton);
        add(scrollPane);
        add(statusLabel);

        selectFileButton.addActionListener(e -> chooseFile());
        compressButton.addActionListener(e -> compressFile());
        decompressButton.addActionListener(e -> decompressFile());
    }

    private void chooseFile() 
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text and Image Files", "txt", "jpg", "jpeg", "png", "huff"));
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Selected: " + selectedFile.getName());
        }
    }

    private void compressFile() 
    {
    if (selectedFile == null) 
    {
        statusLabel.setText("Please select a file first.");
        return;
    }
    try {
        byte[] input = FileHandler.readFile(selectedFile);
        int originalSize = input.length;

        byte[] compressed = HuffmanAlgorithm.compress(input);
        int compressedSize = compressed.length;

        File outFile = new File(selectedFile.getParent(), selectedFile.getName() + ".huff");
        FileHandler.writeFile(outFile, compressed);

        StringBuilder binaryBuilder = new StringBuilder();
        for (byte b : compressed) {
            // Convert byte to 8-bit binary string
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryBuilder.append(binaryString).append(' ');
        }

        double ratio = (double) compressedSize / originalSize * 100;

        resultTextArea.setText(
            "Compression Summary:\n" +
            "------------------------------\n" +
            " File: " + selectedFile.getName() + "\n" +
            "Original Size: " + originalSize + " bytes\n" +
            "Compressed Size: " + compressedSize + " bytes\n" +
            "Compression Ratio: " + String.format("%.2f", ratio) + " %\n\n" +
            "Compressed Data (Binary View):\n" + binaryBuilder.toString()
        );

        statusLabel.setText("Compression successful: " + outFile.getName());
    } catch (Exception ex) {
        ex.printStackTrace();
        statusLabel.setText("Compression failed: " + ex.getMessage());
    }
}
private void decompressFile() 
{
        if (selectedFile == null) {
            statusLabel.setText("Please select a file first.");
            return;
        }
        try {
            byte[] input = FileHandler.readFile(selectedFile);
            byte[] decompressed = HuffmanAlgorithm.decompress(input);
            File outFile = new File(selectedFile.getParent(), selectedFile.getName() + "_decompressed");
            FileHandler.writeFile(outFile, decompressed);
            String outputText = new String(decompressed);
            resultTextArea.setText("Decompressed Content:\n" + outputText);
            statusLabel.setText("Decompression successful.");
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Decompression failed: " + ex.getMessage());
        }
    }
}
