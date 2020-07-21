package com.example.etp;

// *---------------------------------*  Used to list Candidate actions in plan algorithm    *---------------------------------*
public class CandidatePair implements Comparable{
    public String name;
    public double value;
    public CandidatePair(String name,double value){
        this.name = name;
        this.value = value;
    }
    // Used to sort candidate list
    @Override
    public int compareTo(Object pair) {
        return Double.compare(((CandidatePair)pair).value,this.value);
    }
}
