package com.example.etp;

// *---------------------------------*  Used to present time   *---------------------------------*
public class Time extends Object{
    public int hour;
    public int min;
    Time(int hour,int min){
        this.hour=hour;
        this.min=min;
    }

    public Time() {
    }

    // Check if start time + duration <= end time True if yes False if not
    public static boolean isIn(Time oStartTime,Integer duration,Time endTime){
        Time startTime=new Time(oStartTime.hour,oStartTime.min);
        startTime.min+=duration;
        startTime.hour+=(startTime.min/60);
        startTime.min%=60;
        return ((startTime.hour<endTime.hour)||(startTime.hour==endTime.hour&&startTime.min<=endTime.min));
    }
    
    // Check if this Time before or equal argument time true if yes otherwise false
    public boolean compare(Time time){
        return ((this.hour<time.hour)||(this.hour==time.hour&&this.min<=time.min));
    }
    
    // Add duration to current Time and if result passed that day (24) return false otherwise true
    public boolean add(Integer duration){
        this.min+=duration;
        this.hour+=(this.min/60);
        if(hour>=24){
            this.hour-=(this.min/60);
            this.min-=duration;
            return false;
        }
        this.min%=60;
        return true;
    }
    
    // Substract between 2 times and return -1 if time1>time2 otherwise the result
    public static int substract(Time small,Time big){
        int t1=small.hour*60+small.min;
        int t2=big.hour*60+big.min;
        return (t1>t2)?-1:(t2-t1);
    }
    
    // Get maximum time
    public static Time max(Time time1,Time time2){
        if(time1.hour>time2.hour)
            return time1;
        else if(time1.hour<time2.hour)
            return time2;
        else{
            if(time1.min>=time2.min)
                return time1;
            else
                return time2;
        }
    }

    // Get min time
    public static Time min(Time time1,Time time2){
        if(time1.hour>time2.hour)
            return time2;
        else if(time1.hour<time2.hour)
            return time1;
        else{
            if(time1.min>=time2.min)
                return time2;
            else
                return time1;
        }
    }
    @Override
    public String toString() {
        String h,m;
        if(hour<10)
            h = "0"+hour;
        else
            h = ""+hour;
        if(min<10)
            m= "0"+min;
        else
            m= ""+min;

        return  hour + ":" + min ;
    }
    
    
    
    
}
