import java.util.*;
import java.io.*;

public class HuffmanAlgorithm {

    static class Node implements Comparable<Node> {
        byte data;
        int freq;
        Node left, right;

        Node(byte data, int freq) {
            this.data = data;
            this.freq = freq;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }

    public static byte[] compress(byte[] data) throws IOException {
        Map<Byte, Integer> freqMap = new HashMap<>();
        for (byte b : data) {
            freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);
        }

        Node root = buildTree(freqMap);
        Map<Byte, String> codeMap = new HashMap<>();
        buildCodeMap(root, "", codeMap);

        StringBuilder encoded = new StringBuilder();
        for (byte b : data) {
            encoded.append(codeMap.get(b));
        }

        // Convert binary string to byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(codeMap);
        writeBinaryString(objOut, encoded.toString());
        objOut.close();

        return out.toByteArray();
    }

    // Decompress method
    public static byte[] decompress(byte[] compressedData) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(compressedData);
        ObjectInputStream objIn = new ObjectInputStream(in);

        Map<Byte, String> codeMap = (Map<Byte, String>) objIn.readObject();
        Map<String, Byte> reverseMap = new HashMap<>();
        for (Map.Entry<Byte, String> entry : codeMap.entrySet()) {
            reverseMap.put(entry.getValue(), entry.getKey());
        }

        String bitString = readBinaryString(objIn);
        objIn.close();

        List<Byte> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (char c : bitString.toCharArray()) {
            current.append(c);
            if (reverseMap.containsKey(current.toString())) {
                result.add(reverseMap.get(current.toString()));
                current.setLength(0);
            }
        }

        byte[] output = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            output[i] = result.get(i);
        }

        return output;
    }

    private static Node buildTree(Map<Byte, Integer> freqMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node((byte) 0, left.freq + right.freq);
            parent.left = left;
            parent.right = right;
            pq.add(parent);
        }

        return pq.poll();
    }

    private static void buildCodeMap(Node node, String code, Map<Byte, String> map) {
        if (node == null) return;
        if (node.isLeaf()) {
            map.put(node.data, code.length() > 0 ? code : "0"); // Edge case: 1 unique symbol
        }
        buildCodeMap(node.left, code + "0", map);
        buildCodeMap(node.right, code + "1", map);
    }

    private static void writeBinaryString(ObjectOutputStream out, String bitString) throws IOException {
        out.writeInt(bitString.length());
        byte[] bytes = new byte[(bitString.length() + 7) / 8];
        for (int i = 0; i < bitString.length(); i++) 
        {
            if (bitString.charAt(i) == '1') 
            {
                bytes[i / 8] |= (1 << (7 - (i % 8)));
            }
        }
        out.write(bytes);
    }

    private static String readBinaryString(ObjectInputStream in) throws IOException {
        int bitLength = in.readInt();
        int byteLength = (bitLength + 7) / 8;
        byte[] bytes = new byte[byteLength];
        in.readFully(bytes);

        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < bitLength; i++) 
        {
            int b = (bytes[i / 8] >> (7 - (i % 8))) & 1;
            bitString.append(b);
        }
        return bitString.toString();
    }
}
