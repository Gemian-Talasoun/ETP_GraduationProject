
package com.example.etp;

// *---------------------------------*  Used to present POI in plan   *---------------------------------*
public class Node {
    public Node before;         // point to pervious node
    public Node next;           // point to next node
    public POI poi;             // this node POI
    public Time from;           // start time in this place
    public Time to;             // end time in this place
    
    public Node() {
    }

    public Node(Node before, Node next, POI poi,Time from,Time to) {
        this.before = before;
        this.next = next;
        this.poi = poi;
        this.from=from;
        this.to=to;
    }
    
}
