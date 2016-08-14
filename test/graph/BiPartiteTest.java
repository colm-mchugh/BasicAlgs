package graph;


import org.junit.Test;

public class BiPartiteTest {

    @Test
    public void checkBiPartite() {
        Graph<Integer> biPartite = new UGraphMapImpl<>();
        Graph<Integer> NotbiPartite = new UGraphMapImpl<>();
        biPartite.add(1, 2);
        biPartite.add(1, 4);
        biPartite.add(1, 6);
        biPartite.add(3, 2);
        biPartite.add(3, 4);
        biPartite.add(5, 4);
        biPartite.add(5, 6);

        NotbiPartite.add(1, 2);
        NotbiPartite.add(1, 3);
        NotbiPartite.add(1, 4);
        NotbiPartite.add(2, 5);
        NotbiPartite.add(3, 5);
        NotbiPartite.add(3, 6);
        NotbiPartite.add(4, 6);
        NotbiPartite.add(5, 6);
        
        assert new BiPartiteChecker<Integer>(biPartite).isBiPartite();
        assert !(new BiPartiteChecker<Integer>(NotbiPartite).isBiPartite());

    }
}
