package com.example.etp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.etp.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter (Context context){
        this.context = context;
    }

    public int[] imgs_slid= {R.drawable.s1,R.drawable.s1,R.drawable.s1};
    public String[] head_slid = {"Step 1","Step 2","Step 3"};
    public String[] body_slid = {
            "implemented card view in my app, and want to put 2 different texts in it (one is 18dp, second is 9dp) in white space bellow image.",
            "implemented card view in my app, and want to put 2 different texts in it (one is 18dp, second is 9dp) in white space bellow image.",
            "implemented card view in my app, and want to put 2 different texts in it (one is 18dp, second is 9dp) in white space bellow image."
    };

    @Override
    public int getCount() {
        return head_slid.length;
    }

    @Override
    public boolean isViewFromObject( View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container, false);

        ImageView imgslid = (ImageView) view.findViewById(R.id.img_slid);
        TextView headslid = (TextView) view.findViewById(R.id.stp_num);
        TextView bodyslid = (TextView) view.findViewById(R.id.stp);

        imgslid.setImageResource(imgs_slid[position]);
        headslid.setText(head_slid[position]);
        bodyslid.setText(body_slid[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem( ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object );
    }
}
