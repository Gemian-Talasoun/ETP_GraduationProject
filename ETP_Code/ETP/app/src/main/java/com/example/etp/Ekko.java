package com.example.etp;
/* Imports */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// *---------------------------------*  Ekko class used to run the chat bot and provide all his services    *---------------------------------*
public class Ekko {
    /* Vars */
    private String message;         // Used to hold user or ekko message text
    Context context;
    LinearLayout layout;
    MediaPlayer sound;              // Used to hold message sound
    ArrayList<POI> POIs;            // Used to get all POIs in the system
    boolean ismute;                 // Used to mute/unmute message sound
    // Start
    public Ekko(Context context,LinearLayout layout) {
        this.context = context;
        this.layout = layout;
        sound =  MediaPlayer.create(context,R.raw.message);
        POIs = POIsData(new ArrayList<Integer>(),new ArrayList<String>());
        ismute = false;
        start(0);
    }

    // Get all POIs
    public ArrayList<POI> POIsData(ArrayList<Integer>favorite,ArrayList<String>userPreferences){
        ArrayList<POI> list = new ArrayList<POI>();
        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset(context));
            for (int i = 0; i < jArray.length(); ++i) {
                int id=Integer.parseInt(jArray.getJSONObject(i).getString("PoiID"));
                String name=jArray.getJSONObject(i).getString("PoiName");
                String start=jArray.getJSONObject(i).getString("OpenTime");
                int startHour=Integer.parseInt(start.substring(0,start.indexOf(":")));
                int startMin=Integer.parseInt(start.substring(start.indexOf(":")+1));
                String end=jArray.getJSONObject(i).getString("CloseTime");
                int endHour=Integer.parseInt(end.substring(0,end.indexOf(":")));
                int endMin=Integer.parseInt(end.substring(end.indexOf(":")+1));
                double cost=Double.parseDouble(jArray.getJSONObject(i).getString("PoiPrice"));
                int dur=Integer.parseInt(jArray.getJSONObject(i).getString("VisitTime"));
                String type=jArray.getJSONObject(i).getString("WorkingTime");
                String image=jArray.getJSONObject(i).getString("PoiURLImage");
                ArrayList<String>p = new ArrayList<String>();
                ArrayList<Integer>da= new ArrayList<Integer>();
                String perf=jArray.getJSONObject(i).getString("PoiPreferences");
                int n=perf.length();
                for(int j=0;j<n;j++)
                {
                    String ans="";
                    if(perf.charAt(j)=='"')
                    {
                        j++;
                        while ((perf.charAt(j)!='"')) {
                            ans += perf.charAt(j);
                            j++;
                        }
                        p.add(ans);
                        j++;

                    }
                }
                String daur=jArray.getJSONObject(i).getString("Duration");
                int x=daur.length();
                x=daur.length();
                for(int j=0;j<x;j++)
                {
                    String ans="";
                    if(Character.isDigit(daur.charAt(j)))
                    {
                        ans+=daur.charAt(j);
                        j++;
                        while (Character.isDigit(daur.charAt(j)))
                        {
                            ans+=daur.charAt(j);
                            j++;
                        }
                        da.add(Integer.parseInt(ans));
                    }
                    else if(daur.charAt(j)=='n')
                    {
                        da.add(0);
                    }
                }
                Map<Integer,Integer> mp = new HashMap<Integer,Integer>();
                for(int u=0;u<da.size();u++) {
                    mp.put(u + 1, da.get(u));
                }

                POI poi = new POI(id,name,cost,p,mp,type,dur,favorite,userPreferences,new Time(startHour,startMin),new Time(endHour,endMin));
                poi.photoURL = jArray.getJSONObject(i).getString("PoiURLImage");
                poi.desc = jArray.getJSONObject(i).getString("PoiDescription");

                list.add(poi);





            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return list;
    }
    public  String readJSONFromAsset(Context c) {
        String json = null;
        try {
            InputStream is = c.getAssets().open("pois.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /*
        // Function-name Function-id
        fun(){
            1- Ekko Message text
            2- List of next options
            3- make the view
        }
    */

    // start 0
    public void start(int i){
        if(i==0)
            message = "Hello i'm Ekko i am your helper to use ETP application.\n To know information about " +
                    "ETP application and how to use it please choose About ETP option.\n and to use one of " +
                    "my services please choose Ekko Services\n\nHow can i help you?";
        else
            message = "Hello again.\n\nHow can i help you?";

        ArrayList<String> options = new ArrayList<String>();
        // About
        options.add("About ETP-1");
        // Services
        options.add("Ekko Services-25");

        reply(options,message,0);
    }
    // about 1
    public void about(){
        message = "Here i can tell you important information about the app and how can you use it well.\n\nWhich part in the application you want to know about?";

        ArrayList<String> options = new ArrayList<String>();
        // back
        options.add("Back-0-1");
        // home
        options.add("Home-11");
        // profile
        options.add("Profile-2");
        // dashboard
        options.add("Dashboard-7");
        // poi info
        options.add("Places Information-9");
        // constrains
        options.add("Plan Constrains-14");
        // showplan
        options.add("Plan Review-17");
        reply(options,message,0);
    }
    // profilePage 2
    public void profilePage(){
        message = "In profile you can see your personal information and edit it Also you can share our app from (Share), Rate our app from (Rate), Know abstract information about ETP from (About Us) and if you faced a problem or bug in the app or you want to send your recommendation ,from (Connect Us), Also you can delete your account by (Delete Account).\n\nWhich part you want to know about?";
        ArrayList<String> options = new ArrayList<String>();
        // back
        options.add("Back-1");
        // settings
        options.add("Settings Part-3");
        reply(options,message,2);
    }
    // settings 3
    public void settings(){
        message = "In settings page you can see your personal information such as (full name, email, password,...etc) and you can change any of them except your email." +
                "\n\nWhich part you want to know about in settings page?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-2");
        //FavList
        options.add("Favorite Places List-4-2");
        //ForList
        options.add("Forbidden Places List-5-2");
        //PrefList
        options.add("Preferences-6-1");
        reply(options,message,3);
    }
    // favoriteList 4
    public void favoriteList(int op){
        message = "Favorite list is a list of places that you interest to visit so that we make them as high priority places and our planner try to do his best effort to add them in your plans." +
                "\nNote in case of a place in your favorite list not exist in your specific plan that mean planner couldn't put it because of some reason such as" +
                "(this place not working on this time, travelling time between it and other places will exceed your trip's end time,...etc)";
        ArrayList<String> options = new ArrayList<String>();
        if(op==0)
            options.add("Back-14");
        else if (op==1)
            options.add("Back-9");
        else
            options.add("Back-3");
        reply(options,message,0);
    }
    // forbiddenList 5
    public void forbiddenList(int op){
        message = "Forbidden list is a list of places that you don't need to visit so that we delete them from your available places to be visited and our planner will not add them in your plans.";
        ArrayList<String> options = new ArrayList<String>();
        if(op==0)
            options.add("Back-14");
        else if (op==1)
            options.add("Back-9");
        else
            options.add("Back-3");
        reply(options,message,0);
    }
    // preferences 6
    public void preferences(int op){
        message = "Preferences is a list of your interests and hobbies our planner use them to decide which places will satisfy your personality and add them in your plans";
        ArrayList<String> options = new ArrayList<String>();
        if(op==0)
            options.add("Back-14");
        else
            options.add("Back-3");
        reply(options,message,0);
    }
    // dashboardPage 7
    public void dashboardPage(){
        message = "Dashboard is a store of old user's shared plans instead of making a new plan you can use an old user's shared plan.\n" +
                "Also in search field by writing place name you will get all plans in dashboard that has this place.\n\nWhich part you want to know about?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-1");
        // post
        options.add("Dashboard Post-29");
        // post detail
        options.add("Post details-8");
        reply(options,message,7);
    }
    // Dashboard Post 29
    public void post(){
        message = "Dashboard post is an old plan shared by user and each post has these information:\n" +
                "- Plan name\n" +
                "- Plan place\n" +
                "- Plan full duration time\n" +
                "- Plan full cost\n" +
                "- Post number of comments\n" +
                "- Number of users that added this plan in their list" ;
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-7");
        reply(options,message,0);
    }
    // Post details 8
    public void dashboardmore(){
        message =  "By clicking on any dashboard post you will find these information:\n" +
                "- User who shared that plan\n"+
                "- Plan name\n" +
                "- Plan place\n" +
                "- Plan full duration time\n" +
                "- Plan full cost\n" +
                "- Post comments\n"+
                "- Plan's places and in each place has these information:\n"+
                "-- Place name\n"+
                "-- Place start and end times\n"+
                "-- Place minimum cost\n"+
                "Do you like the plan? Great from here you can add this plan to your list to be executed by add button that exist in the top right corner of the page ";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-7");
        reply(options,message,8);
    }
    // poiInfoPage 9
    public void poiInfoPage(){
        message = "Places Information is a list of all places stored in app to be visited.\n\nWhich part you want to know about in Places Information?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-1");
        // poi info
        options.add("Places More Information-10");
        // for
        options.add("Forbidden places list-5-1");
        // fav
        options.add("Favorite places list-4-1");
        reply(options,message,9);
    }
    // poiInfo 10
    public void poiInfo(){
        message = "By clicking on any place you will see place all information\n";
        message+="- Place name\n";
        message+="- Place position\n";
        message+="- Place information\n";
        message+="- Place rate (depending on google rate)\n";
        message+="- Place description\n";
        message+="- Place minimum cost\n";
        message+="- Minimum time taken in this place\n";
        message+="- Place phone number\n";
        message+="Also you can add this place in favorite or forbidden list from here";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-9");
        reply(options,message,10);
    }
    // homePage 11
    public void home(){
        message = "Home contains a list of your plans.\n\nWhich part you want to know about?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-1");
        //plan info
        options.add("Plan information-12");
        reply(options,message,11);
    }
    // planInfo 12
    public void planInfo(){
        message= "Each item in list show plan's information:\n";
        message+= "- Plan name\n";
        message+= "- Plan position\n";
        message+= "- Plan start and end times\n";
        message+= "- Plan cost\n";
        message+= "- A button allows you to share or unshare this plan on the dashboard. Note this service allowed only on plans you saved but not working on plans you imported from the dashboard\n";
        message+="and by clicking on specific plan you will see the places where be visited in this plan.\n\nDo you need to know about the information viewed in each place?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-11");
        //plan more info
        options.add("Yes-13");
        reply(options,message,12);
    }
    // planMoreInfo 13
    public void planMoreInfo(){
        message = "Each place in plan list has:\n" +
                "- Photo/name/brief description\n"+
                "- Place start time, when you be in this place\n"+
                "- Place end time, when you leave this place\n"+
                "- Time spent in that place\n"+
                "- The minimum cost paid in that place\n";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-12");
        reply(options,message,13);
    }
    // constrainsPage 14
    public void constrainsPage(){
        message = "Constrains you filled planner will use them to make a plan that satisfy your constrains.\n\nWhich constrain you want to know about?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-1");
        //sttime & entime
        options.add("Start time & end time-15-0");
        //budget
        options.add("Budget-16");
        //pref
        options.add("Preferences-6-0");
        //for
        options.add("Forbidden places list-5-0");
        //fav
        options.add("Favorite places list-4-0");
        reply(options,message,14);
    }
    // st_enTime 15
    public void st_enTime(int i){
        ArrayList<String> options = new ArrayList<String>();
        if (i==0) {
            message = "Start and End time fields used to set when your trip start and end, these fields filled depending on 12 hours format that start from 1 and end with 12, start time have to be before end time";
            options.add("Back-14");
        }
        else {
            message = "Start and End time fields used to set when you will visit that new place, these fields filled depending on 12 hours format that start from 1 and end with 12, start time have to be before end time";
            options.add("Back-22");
        }
        reply(options,message,0);
    }
    // budget 16
    public void budget(){
        message = "By this field you set the maximum cost you could pay during plan (This cost not contain travel cost it only contain the Basic fees such as ticket or entrance fees)";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-14");
        reply(options,message,0);
    }
    // showplanPage 17
    public void showplanPage(){
        message="In plan review you see our suggested plan that satisfy your constrains also you can modify it." +
                "Also you can know the minimum time needed to travel between 2 places in minutes by using the 2 buttons above the plan" +
                " (note this times based on google maps shortest path)." +
                "\nand when you finish and this plan is suitable please don't forget to save it." +
                "\nImportant note: If you will modify the output plan the app does not test the correctness of the plan so that any changes on your own responsibility" +
                "\n\nWhich part you want to know about?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-1");
        //plan properties
        options.add("Plan properties-18");
        // poi info
        options.add("Each place in plan information-19");
        //modify
        options.add("Modify plan-20");
        reply(options,message,17);
    }
    // PlanProperties 18
    public void PlanProperties(){
        message =  "Our planner's plan has these properties:\n" +
                "- Places showed in plan sorted.\n"+
                "- Plan's full cost not exceed your budget.\n"+
                "- Plan's full duration not exceed your time range(start time, end time).\n"+
                "- The time spent in any place is on the official working hours of this place.\n"+
                "- Time between each two places is equal or bigger than the minimum time taken to travel between these places.\n"+
                "- Planner did his best effort to choose places that satisfy your preferences and favorite list.\n"+
                "- Your forbidden list not in plan.";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-17");
        reply(options,message,0);
    }
    // planPoiInfo 19
    public void planPoiInfo(){
        message = "Each place in plan list has:\n" +
                "- Photo/name/brief description\n"+
                "- Place start time, when you be in this place\n"+
                "- Place end time, when you leave this place\n"+
                "- Time spent in that place\n"+
                "- The minimum cost paid in that place\n";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-17");
        reply(options,message,19);
    }
    // modify 20
    public void modify(){
        /* Part 1 */
        message= "You can modify output plan by:\n"+
                "- Delete place from plan\n"+
                "- Add new place in plan\n"+
                "Important note: If you will modify the output plan the application does not test the correctness of the plan " +
                "so that any changes on your own responsibility\n\n"+
                "Which modify process you want to know about?";

        /* Part 2 */
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-17");
        // add
        options.add("Adding new place in plan-22");
        //delete
        options.add("Deleting place from plan-21");

        /* Part 3 */
        reply(options,message,0);
    }
    // deletePoi 21
    public void deletePoi(){
        message =  "You can delete any place by hold on that place.\nImportant note you can't save empty plan";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-20");
        reply(options,message,0);
    }
    // addPoi 22
    public void addPoi(){
        message = "Choose which field you want to know about";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-20");
        // start&end time
        options.add("Start time & end time-15-1");
        // place
        options.add("Choose new place-23");
        // add place number
        options.add("Choose place number-24");
        reply(options,message,22);
    }
    // choosePlace 23
    public void choosePlace(){
        message = "From dialog showed you have to choose the new place you want to add in plan, dialog show all place's name inclusive places in forbidden list, you can visit same place one or more times";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-22");
        reply(options,message,0);
    }
    // choosePlaceN 24
    public void choosePlaceN(){
        message = "You have to choose where that new place be inserted in plan";
        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-22");
        reply(options,message,0);
    }
    // EkkoServices 25
    public void EkkoServices(){
        message= "Welcome in Ekko services, how can i help you?";
        ArrayList<String> options = new ArrayList<String>();
        //back
        options.add("Back-0-1");
        // SPs
        options.add("Minimum time between 2 places-26");
        //plan info
        options.add("Mute/Un Mute Ekko sound-27");
        //poi info
        options.add("Place information-28");
        reply(options,message,0);
    }
    // SPs 26
    public void SPs(){
        // Get all POIs names ordered by id
        final String [] arr = new String[POIs.size()];
        for(int i=0;i<POIs.size();i++)
            arr[i] = POIs.get(i).getName();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose First Place ");
        final int[] index1 = new int[1];
        final int[] index2 = new int[1];
        builder.setSingleChoiceItems(arr, -1,
                new DialogInterface.OnClickListener() {
                    // Choose first place
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        index1[0] = which;
                        dialog.dismiss();
                        userMessage(arr[which]);
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setTitle("Choose Second Place ");
                        builder.setSingleChoiceItems(arr, -1,
                                new DialogInterface.OnClickListener() {
                                    // Choose second place
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        index2[0] = which;
                                        dialog.dismiss();
                                        userMessage(arr[which]);
                                        // Min time message reply
                                        message= "Minimum time to travel from "+POIs.get(index1[0]).getName().toString()+" To "+POIs.get(index2[0]).getName().toString()+" is "
                                                + POIs.get(index1[0]).getShortestPath(POIs.get(index2[0]).getId())+" Minutes";
                                        ArrayList<String> options = new ArrayList<String>();
                                        options.add("Back-25");
                                        if(!ismute)
                                            sound.start();
                                        reply(options,message,0);
                                    }
                                });
                        AlertDialog mdialog = builder.create();
                        mdialog.show();

                    }
                });
        AlertDialog mdialog = builder.create();
        mdialog.show();

    }
    // EplanInfo 27
    public void Esound(){
        if(ismute){
            ismute = false;
            message= "Ekko sound on";
        }
        else{
            ismute = true;
            message= "Ekko sound off";
        }

        ArrayList<String> options = new ArrayList<String>();
        options.add("Back-25");
        reply(options,message,0);
    }
    // EpoiInfo 28
    public void EpoiInfo(){
        // Get all POIs names ordered by id
        final String [] arr = new String[POIs.size()];
        for(int i=0;i<POIs.size();i++)
            arr[i] = POIs.get(i).getName();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose Place ");
        final int[] index1 = new int[1];
        builder.setSingleChoiceItems(arr, -1,
                new DialogInterface.OnClickListener() {
                    // Choose the place
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        index1[0] = which;
                        dialog.dismiss();
                        userMessage(arr[which]);
                        // Get place data and reply
                        String pref="";
                        for(int i=0;i<POIs.get(index1[0]).getPreferences().size();i++)
                            pref+= POIs.get(index1[0]).getPreferences().get(i)+"   ";
                        message = POIs.get(index1[0]).photoURL;
                        message+= "<- Place Name:\n "+POIs.get(index1[0]).getName().toString()
                                +"\n- Minimum Time in that Place (Just Suggestion):\n"+POIs.get(index1[0]).getDuration() + "Minutes"
                                +"\n- Minimum Cost in that Place (Basic Fees such as ticket or entrance fees):\n"+POIs.get(index1[0]).getCost() +" $"
                                +"\n- Open Time (24 Time Format):\n"+POIs.get(index1[0]).getOpenTime().toString()
                                +"\n- Close Time (24 Time Format):\n"+POIs.get(index1[0]).getCloseTime().toString()
                                +"\n- Place Preferences:\n"+pref
                                +"\n- Place Description:\n"+POIs.get(index1[0]).desc.toString();
                        ArrayList<String> options = new ArrayList<String>();
                        options.add("Back-25");
                        if(!ismute)
                            sound.start();
                        reply(options,message,28);
                    }
                });
        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    // *---------------------------------*  Make Ekko button    *---------------------------------*
    public TextView EkkoButton(){
        View buttonLayout = LayoutInflater.from(context).inflate(R.layout.ekko_button,null);
        TextView b =(TextView) buttonLayout.findViewById(R.id.ekko_button);
        ((ViewGroup)b.getParent()).removeView(b);
        return b;
    }

    // *---------------------------------*  Make user message and add it to chat layout    *---------------------------------*
    public void userMessage(String message){
        TextView item = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        params.setMargins(30, 10, 30, 70);
        item.setLayoutParams(params);
//        item.setBackgroundColor(Color.parseColor("#00184a"));
        item.setTextColor(Color.parseColor("#F8C330"));
        item.setPadding(30,30,30,30);
        item.setText(message);
        item.setBackgroundResource(R.drawable.user_msg_bg);
        layout.addView(item);
    }
    // *---------------------------------*  Make Ekko message and add it to chat layout    *---------------------------------*
    public void reply(final ArrayList<String> options,String message,int imgurl){
        /* Part 1 */
        // Get message layout
        View view = LayoutInflater.from(context).inflate(R.layout.ekko_message,null);
        LinearLayout messageLayout =(LinearLayout)view.findViewById(R.id.ekko_message_layout);
        TextView text = (TextView) messageLayout.findViewById(R.id.ekko_text);
        LinearLayout optionsLayout =(LinearLayout)messageLayout.findViewById(R.id.ekko_options);
        /*
        if(imgurl>0){
            ImageView image = new ImageView(context);
            switch (imgurl){
                case 2:
                    image.setImageResource(R.drawable.e2);
                    break;
                case 3:
                    image.setImageResource(R.drawable.e3);
                    break;
                case 7:
                    image.setImageResource(R.drawable.e7);
                    break;
                case 8:
                    image.setImageResource(R.drawable.e8);
                    break;
                case 9:
                    image.setImageResource(R.drawable.e9);
                    break;
                case 10:
                    image.setImageResource(R.drawable.e10);
                    break;
                case 11:
                    image.setImageResource(R.drawable.e11);
                    break;
                case 12:
                    image.setImageResource(R.drawable.e12);
                    break;
                case 13:
                    image.setImageResource(R.drawable.e13);
                    break;
                case 14:
                    image.setImageResource(R.drawable.e14);
                    break;
                case 17:
                    image.setImageResource(R.drawable.e17);
                    break;
                case 19:
                    image.setImageResource(R.drawable.e19);
                    break;

                case 28:
                {
                    String url = message.substring(0,message.indexOf("1-"));
                    message = message.substring(message.indexOf("1-"));
                    Ekko.LoadImage loadImage = new Ekko.LoadImage(image);
                    loadImage.execute(url);
                    break;
                }
                default:
                    image.setImageResource(R.drawable.e22);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
            params.setMargins(0, 0, 0, 0);
            image.setLayoutParams(params);
            messageLayout.addView(image,0);
        }
        */
        if(imgurl==28){
            ImageView image = new ImageView(context);
            String url = message.substring(0,message.indexOf("<"));
            message = message.substring(message.indexOf("<")+1);
            Ekko.LoadImage loadImage = new Ekko.LoadImage(image);
            loadImage.execute(url);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
            params.setMargins(0, 0, 0, 0);
            image.setLayoutParams(params);
            messageLayout.addView(image,0);
        }
        /* Part 2 */
        for(int i=0;i<options.size();i++) {
            // Divide list item string into the option text and option function id
            final String s = options.get(i).substring(0,options.get(i).indexOf("-"));
            options.set(i,options.get(i).substring(options.get(i).indexOf("-")+1));
            final int x,op;
            if(options.get(i).indexOf("-")!=-1) {
                x = Integer.parseInt(options.get(i).substring(0,options.get(i).indexOf("-")));
                op = Integer.parseInt(options.get(i).substring(options.get(i).indexOf("-")+1));
            }
            else{
                x = Integer.parseInt(options.get(i));
                op = -1;
            }
            final TextView button = EkkoButton();
            button.setText(s);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ismute)
                        sound.start();
                    userMessage(s);
                    button.setClickable(false);
                    switch (x){
                        case 0:
                            start(op);
                            break;
                        case 1:
                            about();
                            break;

                        case 2:
                            profilePage();
                            break;

                        case 3:
                            settings();
                            break;

                        case 4:
                            favoriteList(op);
                            break;

                        case 5:
                            forbiddenList(op);
                            break;

                        case 6:
                            preferences(op);
                            break;

                        case 7:
                            dashboardPage();
                            break;

                        case 8:
                            dashboardmore();
                            break;

                        case 9:
                            poiInfoPage();
                            break;

                        case 10:
                            poiInfo();
                            break;

                        case 11:
                            home();
                            break;

                        case 12:
                            planInfo();
                            break;

                        case 13:
                            planMoreInfo();
                            break;

                        case 14:
                            constrainsPage();
                            break;

                        case 15:
                            st_enTime(op);
                            break;

                        case 16:
                            budget();
                            break;

                        case 17:
                            showplanPage();
                            break;

                        case 18:
                            PlanProperties();
                            break;

                        case 19:
                            planPoiInfo();
                            break;

                        case 20:
                            modify();
                            break;

                        case 21:
                            deletePoi();
                            break;

                        case 22:
                            addPoi();
                            break;

                        case 23:
                            choosePlace();
                            break;

                        case 24:
                            choosePlaceN();
                            break;

                        case 25:
                            EkkoServices();
                            break;

                        case 26:
                            SPs();
                            break;

                        case 27:
                            Esound();
                            break;

                        case 28:
                            EpoiInfo();
                            break;
                        case 29:
                            post();
                            break;
                    }
                }
            });
            optionsLayout.addView(button);
        }
        text.setText(message);
        layout.addView(view);
    }
    // *---------------------------------*  Load image from internet    *---------------------------------*
    private class LoadImage extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;
        public LoadImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap  = null;
            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap  = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
