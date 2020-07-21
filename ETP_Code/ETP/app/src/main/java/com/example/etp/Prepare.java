
package com.example.etp;

import java.util.*;

// *---------------------------------*  Prepare Class used to prepare my data and get POIs divided by type and make the initial soultion   *---------------------------------*

public class Prepare {
    public ArrayList<POI> MPOIs;        // Morning POIs
    public ArrayList<POI> NPOIs;        // Night POIs
    public ArrayList<POI> APOIs;        // All time POIs
    public Plan MinitialPlan;           // Morning initial plan
    public Plan NinitialPlan;           // Night initial plan
    public Time MTimeEnd;               // Morning end time
    public Time NTimeEnd;               // Night end time
    
    public Prepare(ArrayList<POI> POIs,Trip trip,User user){
        // When morning part ends
        MTimeEnd= new Time(15,0);
        // When night part ends
        NTimeEnd= new Time(23,59);
        MPOIs=new ArrayList<POI>();
        NPOIs=new ArrayList<POI>();
        APOIs=new ArrayList<POI>();
        
        // Remove Forbidden list from out POIs
        for(int index:user.getForbidden()){
            POIs.remove(POIs.get(index-1));
        }
        
        // Divide POIs depending on type
        for(POI poi:POIs){
            if(poi.getType().equalsIgnoreCase("M"))
                MPOIs.add(poi);
            else if(poi.getType().equalsIgnoreCase("N"))
                NPOIs.add(poi);
            else
                APOIs.add(poi);
        }
        
        // Sort POIs depending on Budget
        //Collections.sort(MPOIs);
        //Collections.sort(NPOIs);
        //Collections.sort(APOIs);

        // Reorder POIs
        Reorder(MPOIs);
        Reorder(NPOIs);
        Reorder(APOIs);

        /* Make Morning Intial plan */
        MinitialPlan=new Plan(trip.getStartTime(),trip.getEndTime());
        Time currentTime = new Time(trip.getStartTime().hour,trip.getStartTime().min);
        makeSubPlan(MinitialPlan, currentTime, MPOIs, MTimeEnd, trip,null,false);
    
        // Check if current time passed morning end time
        if(currentTime.compare(MTimeEnd)){
            makeSubPlan(MinitialPlan, currentTime, APOIs, MTimeEnd, trip,null,false);
            if(currentTime.compare(MTimeEnd))
               makeSubPlan(MinitialPlan, currentTime, MPOIs, MTimeEnd, trip,null,true); 
        }

        
        /* Make Night plan */
        NinitialPlan=new Plan(trip.getStartTime(),trip.getEndTime());
        makeSubPlan(NinitialPlan, currentTime, NPOIs, NTimeEnd, trip,MinitialPlan,false);

        // Check if current time passed night end time
        if(currentTime.compare(NTimeEnd)){
            makeSubPlan(NinitialPlan, currentTime, APOIs, NTimeEnd, trip,MinitialPlan,false);
            
            if(currentTime.compare(NTimeEnd))
               makeSubPlan(NinitialPlan, currentTime, NPOIs, NTimeEnd, trip,MinitialPlan,true); 
        }

    }

    // *---------------------------------*  Reorder POIs   *---------------------------------*
    private void Reorder(ArrayList<POI> list){
        if(list.size()<=2)
            return;
        for(int i=0;i<100;i++){
            int x = new Random().nextInt(list.size());
            x = (x>=list.size())? 0:x;
            int y = new Random().nextInt(list.size())+1;
            y = (y>=list.size())? 0:y;
            POI temp = list.get(x);
            list.set(x,list.get(y));
            list.set(y,temp);
        }
    }


    // *---------------------------------*  Return legal sub plan   *---------------------------------*
    private void makeSubPlan(Plan plan,Time currentTime,ArrayList<POI> POIs,Time timeEnd,Trip trip,Plan mPlan,boolean skip){
        if(mPlan!=null) 
            plan.makeCalculations(trip.getStartTime(),trip.getEndTime(),mPlan.getFullCost(),mPlan);
        else
            plan.makeCalculations(trip.getStartTime(),trip.getEndTime(),0,mPlan);
        POI last=plan.getLastPOI();
        for(int i=0; i< POIs.size();i++){
            // Check if iam in Time range or not
            if(!currentTime.compare(timeEnd))
                break;
            else{
                if(canInsertLast(plan, POIs.get(i), trip, currentTime,mPlan,skip)){
                    // update current time & plan
                    Time from = new Time (0,0);
                    Time to = new Time (0,0);
                    if(last!=null){
                        // cal travel time
                        currentTime.add(last.getShortestPath(POIs.get(i).getId()));
                        // cal waste time
                        int x = Time.substract(currentTime, POIs.get(i).getOpenTime());
                        if(skip&&x!=-1)
                            currentTime.add(x);
                        
                        from.hour = currentTime.hour;
                        from.min = currentTime.min;
                        // cal poi duration 
                        currentTime.add(POIs.get(i).getDuration());
                        to.hour = currentTime.hour;
                        to.min = currentTime.min;
                    }
                    else{
                        if(mPlan==null){
                            int x = Time.substract(currentTime, POIs.get(i).getOpenTime());
                            if(skip&&x!=-1)
                                currentTime.add(x);
                            from.hour = currentTime.hour;
                            from.min = currentTime.min;
                            currentTime.add(POIs.get(i).getDuration());
                            to.hour = currentTime.hour;
                            to.min = currentTime.min;
                        }
                        else{
                            POI mLast = mPlan.getLastPOI();
                            if(mLast!=null)
                                currentTime.add(mLast.getShortestPath(POIs.get(i).getId()));
                            int x = Time.substract(currentTime, POIs.get(i).getOpenTime());
                            if(skip&&x!=-1)
                                currentTime.add(x);
                            from.hour = currentTime.hour;
                            from.min = currentTime.min;
                            currentTime.add(POIs.get(i).getDuration());
                            to.hour = currentTime.hour;
                            to.min = currentTime.min;
                        }
                    }
                    plan.insert(POIs.get(i), plan.getNOV(),from,to,null);
                    if(mPlan!=null)
                        plan.makeCalculations(trip.getStartTime(), trip.getEndTime(),mPlan.getFullCost(),mPlan);
                    else
                        plan.makeCalculations(trip.getStartTime(),trip.getEndTime(),0,mPlan);
                    last=POIs.get(i);
                    // Remove poi from POIs
                    POIs.remove(i);
                    i--;
                }
            }
        }
    }
    // *---------------------------------*  Check if can insert specific POI in the last poistion in current plan   *---------------------------------*
    private boolean canInsertLast(Plan plan,POI poi,Trip trip,Time currentTime,Plan mPlan,boolean skip){
        // if poi cost and time addtion to plan full cost and time satisify the constrains: 
        // 1- full cost <= budget
        // 2- fulltime-poiTime >=poi.startTime if skip is false only
        // 3- fulltime <= trip.endTime
        // 4- fulltime <= poi.colseTime
        // insert and make Calculations and change last

        // Check 1
        if((plan.getFullCost()+poi.getCost())<=trip.getBudget()){
            // Check 2
            POI last=plan.getLastPOI();
            
            if(hfun(currentTime, last, poi, mPlan, skip)){
                // Check3
                // cal poi duration
                int fullduration=poi.getDuration();
                //cal waste time
                if(skip){
                    Time currTime = new Time(currentTime.hour,currentTime.min);
                    if(last!=null)
                        currTime.add(last.getShortestPath(poi.getId()));
                    else if(mPlan!=null&&mPlan.getLastPOI()!=null)
                        currTime.add(mPlan.getLastPOI().getShortestPath(poi.getId()));
                    int x = Time.substract(currTime, poi.getOpenTime());
                    fullduration+=(x==-1)?0:x;
                }
                //cal travel time
                if(last!=null)
                    fullduration+=last.getShortestPath(poi.getId());
                else if(mPlan!=null){
                    POI mLast=mPlan.getLastPOI();
                    if(mLast!=null)
                        fullduration+= mLast.getShortestPath(poi.getId());
                }
                if(Time.isIn(currentTime, fullduration, trip.getEndTime())){
                    // Check4
                    if(Time.isIn(currentTime, fullduration, poi.getCloseTime())){
                        return true;
                    }
                }
        }
        }
        return false;
    }

    // *---------------------------------*  Helper function used for check 2 in canInsertLast   *---------------------------------*
    private boolean hfun(Time currentTime,POI last,POI poi,Plan mPlan,boolean skip){
        Time arriveAt=new Time(currentTime.hour,currentTime.min);
        if(last!=null){
            if(arriveAt.add(last.getShortestPath(poi.getId()))){
                if(poi.getOpenTime().compare(arriveAt)||skip){
                    return true;
                }
            }
        }
        else{
            
            if(mPlan!=null){
                POI mLast = mPlan.getLastPOI();
                if(mLast!=null)
                    arriveAt.add(mPlan.getLastPOI().getShortestPath(poi.getId()));
            }
            if(poi.getOpenTime().compare(arriveAt)||skip){
                    return true;
            }
        }
        return false;
    }
}
