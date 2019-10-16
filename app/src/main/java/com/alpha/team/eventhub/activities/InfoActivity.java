package com.alpha.team.eventhub.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.fragments.AboutFragment;
import com.alpha.team.eventhub.fragments.FeedbackFragment;
import com.alpha.team.eventhub.fragments.HelpFragment;

import static com.alpha.team.eventhub.utils.Constants.VIEW_PICKER;

/**
 * Created by clasence on 03,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 *
 */
public class InfoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        int viewId = bundle.getInt(VIEW_PICKER);
        switch (viewId){
            case 0:{
                setTitle(getString(R.string.about));
                AboutFragment aboutFragment = new AboutFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main, aboutFragment)
                        .commit();
                break;
            }
            case 1:{
                setTitle(getString(R.string.help));
                HelpFragment helpFragment = new HelpFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main, helpFragment)
                        .commit();
                break;

            }
            case 2:{
                setTitle(getString(R.string.feedback));
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main, feedbackFragment)
                        .commit();

                break;

            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
            }
            default: return super.onOptionsItemSelected(item);
        }
    }
}
