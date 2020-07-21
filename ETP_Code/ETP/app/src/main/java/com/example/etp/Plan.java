
package com.example.etp;

// *---------------------------------*  Used to present sub plan   *---------------------------------*
public class Plan {
    private Node trip;          // start pointer node
    private int NOV;            // number Of Visites
    private Integer fullTime;   // plan full time in minutes
    private double fullCost;    // plan full cost
    private Integer travelTime; // plan full travel time in minutes
    private int wasteTime;      // plan full waste time in minutes
    private double SFs;         // plan full satisfaction factor
    

    public Plan(Time startTime,Time endTime) {
        this.trip = new Node(null,null,null,new Time(0, 0),new Time(startTime.hour,startTime.min));
        this.NOV=0;
        this.fullCost=0;
        this.fullTime=Time.substract(startTime, endTime);
        this.travelTime=0;
        this.wasteTime=0;
        this.SFs=0;
    }

    // *---------------------------------*  Copy sub plan   *---------------------------------*
    public Plan(Plan plan,PlanStructure data){
        this.NOV = 0;
        this.SFs = plan.getSFs();
        this.fullCost = plan.getFullCost();
        this.fullTime = plan.getFullTime();
        this.travelTime = plan.getTravelTime();
        this.wasteTime = plan.getWasteTime();
        
        this.trip = new Node(null, null, null,new Time(plan.getTrip().from.hour,plan.getTrip().from.min)
                ,new Time(plan.getTrip().to.hour,plan.getTrip().to.min));
        
        Node index = plan.getTrip().next;
        while(index!=null){
            insert(index.poi,this.NOV,new Time (index.from.hour,index.from.min)
                    ,new Time (index.to.hour,index.to.min),data);
            index = index.next;
        }
    }

    public double getSFs() {
        return SFs;
    }

    public Node getTrip() {
        return trip;
    }
    
    public int getNOV() {
        return NOV;
    }

    public int getWasteTime() {
        return wasteTime;
    }
    
    
    public Integer getFullTime() {
        return this.fullTime;
    }

    public double getFullCost() {
        return fullCost;
    }
    
    public Integer getTravelTime() {
        return travelTime;
    }

    // *---------------------------------*  Return last poi in this sub plan   *---------------------------------*
    public POI getLastPOI(){
        Node index=this.trip;
        while(index.next!=null){
            index=index.next;
        }
        return index.poi;
    }

    // *---------------------------------*  Return last node in this sub plan   *---------------------------------*
    public Node getLastNode(){
        Node index=this.trip;
        while(index.next!=null){
            index=index.next;
        }
        return index;
    }

    // *---------------------------------*  Make all Calculations in current sub plan to get (fullCost, wasteTime, travelTime, value)   *---------------------------------*
    public void makeCalculations (Time startTime,Time endTime,double fullcost,Plan mPlan){
        // if this sub plan is mplan then fullcost=0 else fullcost=MPlan.cost
        this.fullCost=fullcost;
        this.travelTime=0;
        // in NPlan cal MPlan.lastPlace.endTime'x' - NPlan.firstPlace.startTime'y' + SPs(x,y) as waste time
        if(mPlan==null)
            this.wasteTime=0;
        else{
            if (this.trip.next!=null){
                Node last =mPlan.getLastNode();
                if(last.poi!=null){
                    Time currTime = new Time(last.to.hour,last.to.min);
                    currTime.add(last.poi.getShortestPath(this.trip.next.poi.getId()));
                    int x = Time.substract(currTime, this.trip.next.from);
                    if(x!=-1)
                        this.wasteTime=x;
                    else
                        this.wasteTime=0;
                }
                else
                    this.wasteTime=0;
            }
            else
                this.wasteTime=0;
        }
        this.SFs=0;
        Node index=this.trip;
        while (index!=null){
            //To reach next poi
            Node next=index.next;
            if(next!=null&&next.poi!=null&&index.poi!=null){
                Integer SP = index.poi.getShortestPath(next.poi.getId());
                this.travelTime+=SP;
                // sum of travel and waste time
                int x = Time.substract(index.to, next.from);
                x-=SP;
                this.wasteTime+=x;
            }
            else{
                // At start point in morning plan cal firstPlace.starttime - trip.starttime as waste time
                if(index.poi==null&&next!=null&&mPlan==null){
                    int x = Time.substract(startTime, next.from);
                    if(x!=-1)
                    {
                        this.wasteTime+=x;
                    }
                }
                // At end point in night plan cal trip.endtime - lastplace.endtime as waste time
                if(index.poi!=null&&next==null&&mPlan!=null){
                    int x = Time.substract(index.to, endTime);
                    if(x!=-1){
                        this.wasteTime+=x;
                    }
                }
            }
            //when i there
            if (index.poi!=null){
                this.fullCost+=index.poi.getCost();
                SFs+=index.poi.getValue();
            }
            index=index.next;
        }
        
        
        
        
    }

    // *---------------------------------*  insert new POI in plan in specific index    *---------------------------------*
    public void insert(POI poi,int index,Time from,Time to,PlanStructure data) {
        if(index<=NOV){
            // Get index
            Node x=this.trip;   
            for(int u=0;u<index;u++)
                x=x.next;
            // Insert
            Node y=x.next;   
            Node New = new Node(x,y,poi,from,to);  
            x.next=New;    
            if(y!=null)
                y.before=New;  
            NOV++;
            if(data!=null)
                data.NOVs++;
        }
      //  else
      //      System.out.println("Invalid insert in plan");
    }
   

    // *---------------------------------*  Swap poi with poi in plan by POI.id   *---------------------------------*
    public void swap (POI poi, int id,Time from,Time to){
        // Get Node with same id
        Node index = this.trip.next;
        while(index!=null&&index.poi.getId()!=id){
            index = index.next;
        }
        // Swap
        if(index!=null){
            index.poi=poi;
            index.from=from;
            index.to=to; 
        }
    //    else
    //        System.out.println("Invalid Swap");
    }

    // *---------------------------------*  Delete poi in plan by is index  *---------------------------------*
    public boolean delete (int index,TabuList tabuList,int freq,PlanStructure data){
        if(index<NOV){
            // Get Node with same index
            Node x=this.trip;   
            for(int u=0;u<=index;u++)
                x=x.next;
            // Delete
            x.before.next=x.next;
            if(x.next!=null)
                x.next.before=x.before;
            // if this fun used in planner algorithm insert this action in tabulist
            if(tabuList!=null)
                tabuList.Dinsert(x.poi, freq);
            x=null;
            NOV--;
            if(data!=null)
                data.NOVs--;
            return true;
        }
        else
            return false;
    }

    // *---------------------------------*  Show plan data in console   *---------------------------------*
    public void showPlan(){
        Node index=this.trip;
        while(index.next!=null){
            if(index.poi!=null)
                System.out.println(index.poi.getId()+index.poi.getName());
            index=index.next;
        }
    }

    // *---------------------------------*  Show plan data in console in detail  *---------------------------------*
    public void showPlanD(){
        System.out.println("NOVs: "+this.NOV);
        System.out.println("FullCost: "+this.fullCost);
        System.out.println("FullTime: "+this.fullTime+" TravelTime: "+this.travelTime+" WasteTime: "+this.wasteTime);
        Node index=this.trip;
        while(index!=null){
            if(index.poi!=null)
                System.out.println("("+index.poi.getId()+") "+index.poi.getName()+" From: "+index.from+" To: "+index.to);
            index=index.next;
        }
    }
    
}
