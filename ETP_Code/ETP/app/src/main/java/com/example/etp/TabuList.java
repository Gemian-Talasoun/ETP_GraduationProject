
package com.example.etp;
import java.util.*;
// *---------------------------------*  Tabu list structure  *---------------------------------*
public class TabuList {
    // id1-> poi id || id2 -> freq || poi
    private ArrayList<TabuPair> Dlist;          // Delete List
    // id1 -> poi id || id2 -> index || poi=null
    private ArrayList<TabuPair> Ilist;          // Insert List
    private int[][] Igraph;                     // Insert graph
    // id1 -> poi1 id || id2 -> poi2 id || poi=null
    private ArrayList<TabuPair> Slist;          // Swap List
    private int[][] Sgraph;                     // Swap graph
    
    public TabuList (){
        //maximum number of POIs
        int MNOPOIs = 101;
        //maximum number of visits
        int MNOVs=101;
        Dlist = new ArrayList<TabuPair>();
        Ilist = new ArrayList<TabuPair>();
        Igraph = new int[MNOPOIs][MNOVs]; 
        Slist = new ArrayList<TabuPair>();
        Sgraph = new int[MNOPOIs][MNOPOIs]; 
    }

    // Update tabu list
    public void update(ArrayList<POI> MPOIs,ArrayList<POI> NPOIs,ArrayList<POI> APOIs){
        //Update Delete List
        for(int i=0;i<Dlist.size();i++){
            Dlist.get(i).id2--;
            if(Dlist.get(i).id2==0){
                   POI poi = Dlist.get(i).poi;
                   if(poi.getType().equalsIgnoreCase("M"))
                       MPOIs.add(poi);
                   else if(poi.getType().equalsIgnoreCase("A"))
                       APOIs.add(poi);
                   else
                       NPOIs.add(poi);
                Dlist.remove(i);
                i--;
            }
        }
        
        //Update Insert List
        for(int i=0;i<Ilist.size();i++){
            Igraph[Ilist.get(i).id1][Ilist.get(i).id2]--;
            if(Igraph[Ilist.get(i).id1][Ilist.get(i).id2]==0){
                Ilist.remove(i);
                i--;
            }
        }
    
        //Update Swap List
        for(int i=0;i<Slist.size();i++){
            Sgraph[Slist.get(i).id1][Slist.get(i).id2]--;
            if(Sgraph[Slist.get(i).id1][Slist.get(i).id2]==0){
                Slist.remove(i);
                i--;
            }
        }
    }
    

    public int Isearch(int id,int index){
        return Igraph[id][index];
    }
    public void Iinsert (int id,int index,int freq){
        Igraph[id][index] = freq;
        Ilist.add(new TabuPair(id,index,null));
    }

    public int Ssearch(int id1,int id2){
        if(id1>id2){
            int temp=id1;
            id1=id2;
            id2=temp;
        }
        return Sgraph[id1][id2];
    }
    public void Sinsert (int id1,int id2,int freq){
        if(id1>id2){
            int temp=id1;
            id1=id2;
            id2=temp;
        }
        Sgraph[id1][id2] = freq;
        Slist.add(new TabuPair(id1,id2,null));
    }
   
    
    public void Dinsert (POI poi,int freq){
        Dlist.add(new TabuPair(poi.getId(),freq,poi));
    }


}
