package graph;

import org.junit.Test;


public class BinaryTreeTest {
    
    
    @Test
    public void testIsSymmetric() {
        BinaryTree<Integer> symmTree = new BinaryTree<>(4);
        addNodes(symmTree, 5, 5);
        addNodes(symmTree.left, 6, 7);
        addNodes(symmTree.right, 7, 6);
        addNodes(symmTree.left.left, 39, 22);
        addNodes(symmTree.left.right, 11, null);
        addNodes(symmTree.right.left, null, 11);
        addNodes(symmTree.right.right, 22, 39);
        
        assert symmTree.isSymmetric() == 1;
        
        addNodes(symmTree.right.left, 23, 11);
        assert symmTree.isSymmetric() == 0;
    }

    private void addNodes(BinaryTree<Integer> t, Integer l, Integer r) {
        if (l != null) {
            t.left = new BinaryTree<>(l);            
        }
        if (r != null) {
            t.right = new BinaryTree<>(r);
        }
    }

    
    
}
