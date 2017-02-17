package string;

import sort.QuickSorter;

public class BurrowsWheelerTransform {
   
    public static String transform(String p) {
        Rotation[] rotations = new Rotation[p.length()];
        
        for (int i = 0; i < p.length(); i++) {
            rotations[i] = new Rotation(p, i);
        }
        QuickSorter sorter = new QuickSorter();
        sorter.sort(rotations);
        
        StringBuilder bwText = new StringBuilder(p.length());
        for (int i = 0; i < rotations.length; i++) {
            bwText.append(rotations[i].charAt(p.length() - 1));
        }
        
        return bwText.toString();
    }
}
