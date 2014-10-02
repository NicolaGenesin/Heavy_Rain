package com.doomsdaybunny.rain;

import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;


public class MainActivity extends ActionBarActivity  {

    private static MediaPlayer mediaPlayer;
    private AlphaAnimation aPlus;
    private AlphaAnimation aMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Parse.initialize(this, "uFqs4svGdikeRrJJprjuFT9RHBCF2zkoFaVLK4Jd", "SJUjZOw7r3asOMihW96edwHnOW557ZSieuxuolSY");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseAnalytics.trackAppOpened(getIntent());
        getActionBar().hide();

        if (mediaPlayer !=null && mediaPlayer.isPlaying())
            findViewById(R.id.background).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.background).setVisibility(View.INVISIBLE);

        TextView title = (TextView) findViewById(R.id.titleMaster);
        TextView titleSlave = (TextView) findViewById(R.id.titleSlave);
        title.setTypeface(Typeface.createFromAsset(this.getAssets(), "raleway.ttf"), Typeface.BOLD);
        titleSlave.setTypeface(Typeface.createFromAsset(this.getAssets(), "raleway.ttf"), Typeface.BOLD);

        aPlus = new AlphaAnimation(0.0f, 1.0f);
        aMinus = new AlphaAnimation(1.0f, 0.0f);
        aPlus.setDuration(2000);
        aMinus.setDuration(300);
        aPlus.setFillAfter(true);
        aMinus.setFillAfter(true);

        findViewById(R.id.clickableLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.background).setVisibility(View.VISIBLE);
                findViewById(R.id.background).clearAnimation();

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    findViewById(R.id.background).startAnimation(aMinus);
                    findViewById(R.id.titleSlave).startAnimation(aPlus);
                    findViewById(R.id.titleMaster).startAnimation(aPlus);
                } else {
                    findViewById(R.id.titleMaster).startAnimation(aMinus);
                    findViewById(R.id.titleSlave).startAnimation(aMinus);
                    findViewById(R.id.background).startAnimation(aPlus);
                }
                playBeep();
            }
        });
    }

    public void playBeep() {
        try {
            if (mediaPlayer == null)
                mediaPlayer = new MediaPlayer();

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                return;
            }
            AssetFileDescriptor descriptor = getAssets().openFd("rain.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
