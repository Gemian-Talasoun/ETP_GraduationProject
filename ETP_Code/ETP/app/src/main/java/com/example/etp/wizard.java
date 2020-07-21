package com.example.etp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class wizard extends AppCompatActivity {

    ViewPager vp ;
    LinearLayout bs;
    LinearLayout Ldots;
    TextView skp ;

    TextView[] dots;

    SliderAdapter sliderAdapter = new SliderAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        vp = (ViewPager) findViewById(R.id.slid_vew_pager);
//        bs =  (LinearLayout) findViewById(R.id.btns_slid);
        Ldots = (LinearLayout) findViewById(R.id.dotsLay);
        skp = (TextView) findViewById(R.id.skip);

        vp.setAdapter(sliderAdapter);

        dotsIndicator(0);
        vp.addOnPageChangeListener(viewListner);

        skp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                homeAct();
            }
        });

    }


    public void dotsIndicator(int position){
        dots = new TextView[3];
        for(int i =0 ; i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.yellow));

            Ldots.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.whit));
        }
    }


    public void homeAct(){
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            dotsIndicator(i);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}