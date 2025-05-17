public class HuffmanNode {
    byte ch;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(byte ch, int frequency) {
        this.ch = ch;
        this.frequency = frequency;
        this.left = this.right = null;
    }
}
