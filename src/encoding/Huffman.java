package encoding;

import heap.MinHeap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

/**
 * Create a Huffman encoding given an alphabet and frequencies of each character
 * in that alphabet.
 * 
 * @author colm_mchugh
 */
public class Huffman<T> {
    
    static final boolean ZERO = Boolean.FALSE;
    static final boolean ONE = Boolean.TRUE;
    static final Boolean UNSET = null;
    
    // A Node is an element in a Huffman tree. 
    // All nodes have a frequency and a code, which is 1 or 0.
    public abstract class Node implements Comparable<Node> {
        double frequency;
        Boolean code;

        @Override
        public int compareTo(Node o) {
           if (this.frequency > o.frequency) {
               return 1;
           }
           if (this.frequency < o.frequency) {
               return -1;
           }
           return 0;
        }

        public Node(double frequency) {
            this.frequency = frequency;
            this.code = UNSET;
        }
        
        protected abstract Node accept(NodeVisitor visitor);
    }
    
    // A Leaf is a node that represents a single character in the 
    // alphabet being encoded.
    // All terminal nodes in a Huffman tree are Leafs.
    public class Leaf<T> extends Node {
        T letter;

        public Leaf(T letter, double frequency) {
            super(frequency);
            this.letter = letter;
        }
        
        @Override
        public Node accept(NodeVisitor visitor) {
            visitor.visitLeaf(this);
            return this;
        }

        @Override
        public String toString() {
            return "leaf:" + this.letter + ", " + this.frequency;
        }
        
        
    }
    
    // A Branch is a node in a Huffman tree that has two nodes, left and right.
    // All inner nodes in a Huffman tree are Branches.
    public class Branch extends Node {
        Node left;
        Node right;

        public Branch(Node left, Node right, double frequency) {
            super(frequency);
            this.left = left;
            this.right = right;
        }
        
        @Override
        public Node accept(NodeVisitor visitor) {
            visitor.visitBranch(this);
            return this;
        }

        @Override
        public String toString() {
            return "node:" + this.frequency;
        }     
        
    }
    
    // base class for visiting Huffman Tree
    public abstract class NodeVisitor {
        public abstract Node visitBranch(Branch node);
        
        public abstract Node visitLeaf(Leaf leaf);
    }
    
    private final List<Huffman.Node> alphabet;
    private final Map<T, BitSet> bitsetEncoding = new HashMap<>();
    private final Map<T, String> stringEncoding = new HashMap<>();
        
    public Huffman() {
        alphabet = new ArrayList<>();
    }
    
    public void addSymbol(T symbol, double freq) {
        alphabet.add(new Leaf<>(symbol, freq));
    }
    
    public Map<T, BitSet> getBitSetEncoding() {
        if (bitsetEncoding.isEmpty()) {
            this.makeEncoding();
        }
        return bitsetEncoding;
    }
    
    public Map<T, String> getStringEncoding() {
        if (stringEncoding.isEmpty()) {
            this.makeEncoding();
        }
        return stringEncoding;
    }
    
    private void makeEncoding() {
        MinHeap<Node> encodingQueue = new MinHeap<>();
        for (Node letter : alphabet) {
            encodingQueue.Insert(letter);
        }
        while (encodingQueue.size() > 1) {
            Node el1 = encodingQueue.Delete();
            Node el2 = encodingQueue.Delete();
            Branch n = new Branch(el1, el2, el1.frequency + el2.frequency);
            encodingQueue.Insert(n);
        }
        // invariant: 
        assert encodingQueue.size() == 1;
        
        Node root = encodingQueue.Delete();
        Stack<Boolean> codeSuffix = new Stack<>();
        root.accept(new NodeVisitor() {
            @Override
            public Node visitBranch(Branch node) {
                if (!Objects.equals(node.code, UNSET)) {
                    codeSuffix.push(node.code);
                }
                node.left.code = ZERO;
                node.right.code = ONE;
                node.left.accept(this);
                node.right.accept(this);
                if (!Objects.equals(node.code, UNSET)) {
                    codeSuffix.pop();
                }
                return node;
            }
            
            @Override
            public Node visitLeaf(Leaf leaf) {
                BitSet encoding = new BitSet(1 + codeSuffix.size());
                StringBuilder sb = new StringBuilder(1 + codeSuffix.size());
                int bitIndex = 0; // for indexing into encoding; 0-based indexing
                for (Boolean code : codeSuffix) {
                    encoding.set(bitIndex++, code);
                    sb.append(code? '1' : '0');
                }
                encoding.set(bitIndex++, leaf.code);
                sb.append(leaf.code? '1' : '0');
                bitsetEncoding.put((T) leaf.letter, encoding);
                stringEncoding.put((T) leaf.letter, sb.toString());
                return leaf;
            }
        });
    }
    
}
