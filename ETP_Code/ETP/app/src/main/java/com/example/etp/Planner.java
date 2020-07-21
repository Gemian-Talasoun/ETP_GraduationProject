
package com.example.etp;

/* Imports */
import android.widget.Toast;
import java.util.*;
import java.util.Random;
// *---------------------------------*  Planner used to get plan   *---------------------------------*
public class Planner {
    public static double intialPlanValue;
    public static double finalPlanValue;

    public Planner() {
    }

    // *---------------------------------*  Get plan algorithm   *---------------------------------*
    public static FullPlan getPlan(int MT,int TabuFreq,double SFW,double TTW,double WTW,int MIWoutI,Data data){
        
        /*Get Data (User, Trip, POIs)*/
        User user=data.userData();
        Trip trip=data.tripData();
        ArrayList<POI> POIs = Data.POIsData(user.getFavorite(), user.getPreferences());
        
        /*Prepare Data*/
        //1- Remove forbidden list
        //2- Split POIs depending on type
        //3- Make initial MPlan and NPlan
        //4- Make Tabulist
        Prepare pre = new Prepare(POIs, trip, user);
        TabuList tabuList = new TabuList();
        
        
        /*Algorithm*/
        // Make Intial Plan as current and best plan
        FullPlan currentPlan = new FullPlan(pre.MinitialPlan, pre.NinitialPlan);
      //  currentPlan.showPlanD();
        FullPlan bestPlan = new FullPlan(currentPlan);
        double bestValue = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
       // Log.d("myTag","");
       // String f = "Intial Plan value: "+bestValue;
      //  System.out.println(f);
        intialPlanValue = bestValue;
        int IWoutI=0;
        
        //Loop MT times
        while(MT>0){
            MT--;
            // Update tabu list
            tabuList.update(pre.MPOIs, pre.NPOIs,pre.APOIs);
            //1)Make Neighboors
            
            //Make candidate list
            ArrayList<CandidatePair> candidateList = new ArrayList<CandidatePair>();
            
            //1-Swap Neighboors
            
            //Swap each outMPOI and outAPOI with each inMPOI
            Node index = currentPlan.MPlan.getTrip().next;
            while(index!=null){
                for(POI Mpoi:pre.MPOIs){
                    //Do
                    POI temp = index.poi;
                    index.poi=Mpoi;
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        //Opreation planType poiType
                        String name = "SMM";
                        name+=index.poi.getId();
                        name+="-";
                        name+=temp.getId();
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    index.poi = temp;
                }
                for(POI Apoi:pre.APOIs){
                    //Do
                    POI temp = index.poi;
                    index.poi=Apoi;
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "SMA";
                        name+=index.poi.getId();
                        name+="-";
                        name+=temp.getId();
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    index.poi = temp;
                }
                index = index.next;
            }
            
            //Swap each outNPOI and outAPOI with each inNPOI
            index = currentPlan.NPlan.getTrip().next;
            while(index!=null){
                for(POI Npoi:pre.NPOIs){
                    //Do
                    POI temp = index.poi;
                    index.poi=Npoi;
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "SNN";
                        name+=index.poi.getId();
                        name+="-";
                        name+=temp.getId();
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    index.poi = temp;
                }
                for(POI Apoi:pre.APOIs){
                    //Do
                    POI temp = index.poi;
                    index.poi=Apoi;
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "SNA";
                        name+=index.poi.getId();
                        name+="-";
                        name+=temp.getId();
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    index.poi = temp;
                }
                index = index.next;
            }
            
            //2- Insert Neighboors
            
            //Insert each MPOI and APOI in each poistion in MPlan
            for(POI Mpoi:pre.MPOIs){
                for(int i=0;i<=currentPlan.MPlan.getNOV();i++){
                    //Do 
                    currentPlan.MPlan.insert(Mpoi, i, new Time(0,0), new Time(0,0),currentPlan.planData);
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "IMM";
                        name+=Mpoi.getId();
                        name+="-";
                        name+=i;
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    currentPlan.MPlan.delete(i,null,0,currentPlan.planData);
                } 
            }
            for(POI Apoi:pre.APOIs){
                for(int i=0;i<=currentPlan.MPlan.getNOV();i++){
                    //Do 
                    currentPlan.MPlan.insert(Apoi, i, new Time(0,0), new Time(0,0),currentPlan.planData);
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "IMA";
                        name+=Apoi.getId();
                        name+="-";
                        name+=i;
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    currentPlan.MPlan.delete(i,null,0,currentPlan.planData);
                } 
            }
            //Insert each NPOI and APOI in each poistion in NPlan
            for(POI Npoi:pre.NPOIs){
                for(int i=0;i<=currentPlan.NPlan.getNOV();i++){
                    //Do 
                    currentPlan.NPlan.insert(Npoi, i, new Time(0,0), new Time(0,0),currentPlan.planData);
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "INN";
                        name+=Npoi.getId();
                        name+="-";
                        name+=i;
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    currentPlan.NPlan.delete(i,null,0,currentPlan.planData);
                }
            }
            for(POI Apoi:pre.APOIs){
                for(int i=0;i<=currentPlan.NPlan.getNOV();i++){
                    //Do 
                    currentPlan.NPlan.insert(Apoi, i, new Time(0,0), new Time(0,0),currentPlan.planData);
                    //Evaluate
                    double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    if(result>0){
                        //Add in candidate List
                        String name = "INA";
                        name+=Apoi.getId();
                        name+="-";
                        name+=i;
                        candidateList.add(new CandidatePair(name, result));
                    }
                    //UnDo
                    currentPlan.NPlan.delete(i,null,0,currentPlan.planData);
                }
            }
            
            //Sort candidateList by value
            Collections.sort(candidateList);
            
            //Check for a solution
            boolean solutionExist=false;
            for(int i=0;i<candidateList.size();i++){
                if(candidateList.get(i).value>bestValue){
                    IWoutI=0;
                    DoSolution(candidateList.get(i).name,currentPlan,pre,tabuList,TabuFreq);
                    //3-change best value and plan
                    bestValue = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                    bestPlan = new FullPlan(currentPlan);
                    solutionExist=true;
                    break;
                }
                else{
                    //Check if opration in tabu
                    if(MT==1)
                        System.out.println();
                    String name = candidateList.get(i).name;
                    boolean inTabu=false;
                    if(name.charAt(0)=='S'){
                        int outid = Integer.parseInt(name.substring(3,name.indexOf("-")));
                        int inid = Integer.parseInt(name.substring(name.indexOf("-")+1));
                        if(tabuList.Ssearch(outid, inid)>0)
                            inTabu = true;
                    }
                    else{
                        int outid = Integer.parseInt(name.substring(3,name.indexOf("-")));
                        int indx = Integer.parseInt(name.substring(name.indexOf("-")+1));
                        if(tabuList.Isearch(outid, indx)>0)
                            inTabu = true;
                    }
                    if(!inTabu){
                        IWoutI++;
                        DoSolution(candidateList.get(i).name,currentPlan,pre,tabuList,TabuFreq);
                        solutionExist=true;
                        break;
                    }
                }
            }
            
            //If no solution exist or IWoutI > MIWoutI Delete random poi from currentplan
            if(!solutionExist||IWoutI>MIWoutI){
                Random rand = new Random();
                int indx;
                if(currentPlan.planData.NOVs>0)
                    indx = rand.nextInt(currentPlan.planData.NOVs);
                else
                    indx=0;
                if(!currentPlan.MPlan.delete(indx,tabuList,TabuFreq,currentPlan.planData))
                    currentPlan.NPlan.delete(indx-currentPlan.MPlan.getNOV(),tabuList,TabuFreq,currentPlan.planData);
                IWoutI=0;
                double result = currentPlan.evaluatePlan(SFW, TTW, WTW, trip);
                if(result>bestValue){
                    bestValue = result;
                    bestPlan = new FullPlan(currentPlan);
                }
            }
        }
       // Log.d("myTag","");
       // f = "-------------------------------------\nBest plan value: "+bestPlan.evaluatePlan(SFW, TTW, WTW, trip);
       // System.out.println(f);
        finalPlanValue = bestPlan.evaluatePlan(SFW, TTW, WTW, trip);
    //    bestPlan.showPlanD();
        return bestPlan;
    }

    // *---------------------------------*  Do best solution on currentplan   *---------------------------------*
    private static void DoSolution(String name,FullPlan currentPlan,Prepare pre,TabuList tabuList,int TabuFreq){
        //1-Do opration on currentplan
        Plan plan;
        ArrayList<POI> pois;
        POI outPOI=null;

        // Detect which plan will be changed
        if(name.charAt(1)=='M')
            plan = currentPlan.MPlan;
        else
            plan = currentPlan.NPlan;

        // Get POIs for that part
        if(name.charAt(2)=='M')
            pois = pre.MPOIs;
        else if(name.charAt(2)=='N')
            pois = pre.NPOIs;
        else
            pois = pre.APOIs;

        //Swap opration
        if(name.charAt(0)=='S'){
            int outid = Integer.parseInt(name.substring(3,name.indexOf("-")));
            int inid = Integer.parseInt(name.substring(name.indexOf("-")+1));
            for(POI poi :pois){
                if(poi.getId()==outid){
                    outPOI=poi;
                    break;
                }
            }
            plan.swap(outPOI, inid, new Time(0,0), new Time(0,0));
            //2-Add opration in tabu list
            tabuList.Sinsert(outid, inid, TabuFreq);
            
        }
        //Insert opration
        else{
            int outid = Integer.parseInt(name.substring(3,name.indexOf("-")));
            int indx = Integer.parseInt(name.substring(name.indexOf("-")+1));
            for(POI poi :pois){
                if(poi.getId()==outid){
                    outPOI=poi;
                    break;
                }
            }
            plan.insert(outPOI, indx, new Time(0,0), new Time(0,0),currentPlan.planData);
            //2-Add opration in tabu list
            tabuList.Iinsert(outid, indx, TabuFreq);
            
        }
        //3-Remove POI from POIs List
        pois.remove(outPOI);
    }
    
}
