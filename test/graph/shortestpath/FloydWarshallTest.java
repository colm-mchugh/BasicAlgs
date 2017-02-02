/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.shortestpath;

import graph.WGraphExps;
import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import graph.shortestpath.FloydWarshall;
import graph.shortestpath.Path;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author colm_mchugh
 */
public class FloydWarshallTest {
    
    @Test
    public void floydWarshall() {
        WeightedGraph<Integer> g = new WeightedGraphDirected<>();
        g.link(1, 2, 3);
        g.link(1, 3, 8);
        g.link(1, 5, -4);
        g.link(2, 5, 7);
        g.link(2, 4, 1);
        g.link(3, 2, 4);
        g.link(4, 1, 2);
        g.link(4, 3, -5);
        g.link(5, 4, 6);

        FloydWarshall<Integer> sper = new FloydWarshall<>();
        List<Path<Integer>> resList = sper.sp(g);
        assert resList.size() == 25;
        for (Path<Integer> res : resList) {
            if (res.u == 1 && res.v == 5) {
                assert res.d == -4;
            }
            if (res.u == 1 && res.v == 3) {
                assert res.d == -3;
            }
        }
    }

    @Test
    public void floydWarshallExt1() {
        WeightedGraph<Integer> g = new WGraphExps<>();
        g.link(1, 2, 1);
        g.link(1, 3, 1);
        g.link(1, 5, 1);
        g.link(2, 5, 1);
        g.link(2, 4, 1);
        g.link(3, 2, 1);
        g.link(4, 1, 1);
        g.link(4, 3, 1);
        g.link(5, 4, 1);
        FloydWarshall<Integer> sper = new FloydWarshall<>();
        List<Path<Integer>> resList = sper.sp(g);
        assert resList.size() == 25;
        for (Path<Integer> res : resList) {
            System.out.println(res);
        }
    }

    @Test
    public void floydWarshallNegCycle() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
        g.link('s', 'a', 1);
        g.link('s', 'c', 1);
        g.link('s', 'e', 1);
        g.link('a', 'b', 1);
        g.link('c', 'd', 1);
        g.link('d', 'c', 1);
        g.link('e', 'f', 1);
        g.link('f', 'e', 1);
        g.link('b', 'g', 1);
        g.link('d', 'g', 1);
        g.link('f', 'g', 1);
        FloydWarshall<Character> sper = new FloydWarshall<>();
        List<Path<Character>> resList = sper.sp(g);
        for (Path<Character> res : resList) {
            System.out.println(res);
        }
    }

    
    
}
