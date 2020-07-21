
package com.example.etp;
// *---------------------------------*  Pair structure used in tabu list  *---------------------------------*
public class TabuPair {
    public int id1;
    public int id2;
    public POI poi;
    public TabuPair() {
    }

    public TabuPair(int id1, int id2, POI poi) {
        this.id1 = id1;
        this.id2 = id2;
        this.poi = poi;
    }

    
    
}
