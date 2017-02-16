package thinkwee.buptroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by think on 20162016/10/12 001220:47
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:
 */

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitle("个人首页");
        Intent intent=getIntent();
        int startcounts = intent.getIntExtra("StartCounts", 1);
        Button startcountsbt = (Button) findViewById(R.id.startcountsbt);
        startcountsbt.setText("使用次数\n"+String.valueOf(startcounts));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
