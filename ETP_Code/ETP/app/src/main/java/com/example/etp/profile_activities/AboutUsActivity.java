package com.example.etp.profile_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.etp.R;

public class AboutUsActivity extends AppCompatActivity {
    private ImageView mBackToBrofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mBackToBrofile = findViewById(R.id.about_back_to_profile);
        mBackToBrofile.setOnClickListener(new View.OnClickListener() {  // Go Back to Profile Activity.
            @Override
            public void onClick(View v) {
                finish(); // Built in function to finish this activity.
            }
        });
    }
}