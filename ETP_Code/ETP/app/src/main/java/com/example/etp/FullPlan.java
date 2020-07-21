
package com.example.etp;

import java.util.ArrayList;

// *---------------------------------*  FullPlan class used to represent the full plan that contains Mplan and Nplan subplans    *---------------------------------*
public class FullPlan {
    public Plan MPlan;          // Morning sub plan
    public Plan NPlan;          // Night sub plan
    public PlanStructure planData;  //full plan data
    
    public FullPlan(Plan MPlan, Plan NPlan) {
        this.MPlan = MPlan;
        this.NPlan = NPlan;
        this.planData = new PlanStructure();
    }

    // *---------------------------------*  Copy this plan    *---------------------------------*
    public FullPlan (FullPlan plan){
        this.planData = new PlanStructure();
        this.planData.NOVs=plan.planData.NOVs;
        this.planData.SFs=plan.planData.SFs;
        this.planData.fullCost=plan.planData.fullCost;
        this.planData.travelTime=plan.planData.travelTime;
        this.planData.wasteTime=plan.planData.wasteTime;
        this.MPlan = new Plan(plan.MPlan,null);
        this.NPlan = new Plan(plan.NPlan,null);
    }

    // *---------------------------------*  Check if plan legal return plan's value if legal otherwise return -1   *---------------------------------*
    public double evaluatePlan (double SFW,double TTW,double WTW,Trip trip){
        // Initialize data
        double fullTime=MPlan.getFullTime();
        Time currentTime = new Time(trip.getStartTime().hour,trip.getStartTime().min);
        this.planData.NOVs=this.MPlan.getNOV()+this.NPlan.getNOV();
        this.planData.SFs=0;
        this.planData.fullCost=0;
        this.planData.travelTime=0;
        this.planData.wasteTime=0;
        //loop in MPlan
        if(!evaluateSubPlan(MPlan,currentTime,trip))
            return -1;
        //Cal range between Mplan and Nplan
        POI Mlast=MPlan.getLastPOI();
        Node Nfirst=NPlan.getTrip().next;
        if(Mlast!=null&&Nfirst!=null){
            int x = Mlast.getShortestPath(Nfirst.poi.getId());
            planData.travelTime+=x;
            currentTime.add(x);
        }
        //loop in NPlan
        if(!evaluateSubPlan(NPlan,currentTime,trip))
            return -1;
        //Cal range between last Nplan and trip.endtime
        int x = Time.substract(currentTime, trip.getEndTime());
        if(x==-1)
            return -1;
        planData.wasteTime+=x;

        // Evaluate plan value
        double TT=1-(planData.travelTime/fullTime);
        double WT=1-(planData.wasteTime/fullTime);
        return (SFW*planData.SFs+TTW*TT+WTW*WT);
    }

    // *---------------------------------*  Check if sub plan legal return true if legal otherwise return false   *---------------------------------*
    private boolean evaluateSubPlan(Plan plan,Time currentTime,Trip trip){
        Node index=plan.getTrip().next;
        Time currTime=new Time(currentTime.hour,currentTime.min);
        while(index!=null){
            // before
            int x = Time.substract(currentTime, index.poi.getOpenTime());
            x = (x==-1)?0:x;
            planData.wasteTime+=x;
            currTime.add(x);
            index.from.hour=currTime.hour;
            index.from.min=currTime.min;
            // in
            planData.fullCost+=index.poi.getCost();
            planData.SFs+=index.poi.getValue();
            x+=index.poi.getDuration();
            if(planData.fullCost>trip.getBudget())
                return false;
            if(!Time.isIn(currentTime, x, index.poi.getCloseTime()))
                return false;
            currTime.add(index.poi.getDuration());
            index.to.hour=currTime.hour;
            index.to.min=currTime.min;
            //after
            if(index.next!=null){
                POI next=index.next.poi;
                x+= index.poi.getShortestPath(next.getId());
                currTime.add(index.poi.getShortestPath(next.getId()));
                planData.travelTime+= index.poi.getShortestPath(next.getId());
            }
            if(!Time.isIn(currentTime, x, trip.getEndTime()))
                return false;
            
            currentTime.add(x);
            index=index.next;
        }
        if (Time.isIn(currentTime, 0, trip.getEndTime()))
            return true;
        else
            return false;
    }

    // *---------------------------------*  Output plan data in detail in console   *---------------------------------*
    public void showPlanD(){
        System.out.println("NOVs: "+planData.NOVs);
        System.out.println("FullCost: "+planData.fullCost);
        System.out.println("FullTime: "+MPlan.getFullTime()+" TravelTime: "+planData.travelTime+" WasteTime: "+planData.wasteTime);
        Node index=MPlan.getTrip();
        while(index!=null){
            if(index.poi!=null)
                System.out.println("("+index.poi.getId()+") "+index.poi.getName()+" From: "+index.from+" To: "+index.to);
            index=index.next;
        }
        index=NPlan.getTrip();
        while(index!=null){
            if(index.poi!=null)
                System.out.println("("+index.poi.getId()+") "+index.poi.getName()+" From: "+index.from+" To: "+index.to);
            index=index.next;
        }
    }

    // *---------------------------------*  Return plan data in details   *---------------------------------*
    public void getPlanD(ArrayList<String> startTimeList, ArrayList<String> endTimeList, ArrayList<String> NamesList, ArrayList<String> costList
            , ArrayList<String> durationList, ArrayList<String> descList,ArrayList<String>IDsList,ArrayList<String> imageURLList){
        // Return Mplan
        Node index = this.MPlan.getTrip().next;
        while(index!=null){
            //idList.add(""+index.poi.getId());
            int H = index.from.hour;
            // Change format from 24 to 12
            if(H>=0&&H<12) {
                H = (H==0)?12:H;
                startTimeList.add("" + H + ":" + index.from.min+" AM");
            }
            else{
                H = (H==12)?12:H%12;
                startTimeList.add("" + H + ":" + index.from.min+" PM");
            }
            H = index.to.hour;
            // Change format from 24 to 12
            if(H>=0&&H<12){
                H = (H==0)?12:H;
                endTimeList.add(""+H+":"+index.to.min+" AM");
            }
            else{
                H = (H==12)?12:H%12;
                endTimeList.add(""+H+":"+index.to.min+" PM");
            }

            NamesList.add(""+index.poi.getName());
            costList.add(""+index.poi.getCost());
            durationList.add(""+index.poi.getDuration());
            descList.add(""+index.poi.desc);
            imageURLList.add(""+index.poi.photoURL);
            IDsList.add(""+index.poi.getId());
            index = index.next;
        }

        // Return Nplan
        index = this.NPlan.getTrip().next;
        while(index!=null){
            //idList.add(""+index.poi.getId());
            int H = index.from.hour;
            // Change format from 24 to 12
            if(H>=0&&H<12) {
                H = (H==0)?12:H;
                startTimeList.add("" + H + ":" + index.from.min+" AM");
            }
            else{
                H = (H==12)?12:H%12;
                startTimeList.add("" + H + ":" + index.from.min+" PM");
            }
            H = index.to.hour;
            // Change format from 24 to 12
            if(H>=0&&H<12){
                H = (H==0)?12:H;
                endTimeList.add(""+H+":"+index.to.min+" AM");
            }
            else{
                H = (H==12)?12:H%12;
                endTimeList.add(""+H+":"+index.to.min+" PM");
            }
            NamesList.add(""+index.poi.getName());
            costList.add(""+index.poi.getCost());
            durationList.add(""+index.poi.getDuration());
            descList.add(""+index.poi.desc);
            imageURLList.add(""+index.poi.photoURL);
            IDsList.add(""+index.poi.getId());
            index = index.next;
        }
    }

}
