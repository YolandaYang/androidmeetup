package com.shafernotes.lifebeat.android;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.media.SoundPool;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity {
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundsMap;
    int loaded = 0;

    int C1=1;
    int E1=2;
    int G1=3;
    int C2=4;
    int E2=5;
    int G2=6;
    int C3=7;
    int E3=8;
    int G3=9;
    int C4=10;
    int E4=11;
    int G4=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 100);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded ++;
            }
        });

        soundsMap = new HashMap<Integer, Integer>();
        soundsMap.put(C1, soundPool.load(this, R.raw.cone, 1));
        soundsMap.put(E1, soundPool.load(this, R.raw.eone, 1));
        soundsMap.put(G1, soundPool.load(this, R.raw.gone, 1));
        soundsMap.put(C2, soundPool.load(this, R.raw.ctwo, 1));
        soundsMap.put(E2, soundPool.load(this, R.raw.etwo, 1));
        soundsMap.put(G2, soundPool.load(this, R.raw.gtwo, 1));
        soundsMap.put(C3, soundPool.load(this, R.raw.cthree, 1));
        soundsMap.put(E3, soundPool.load(this, R.raw.ethree, 1));
        soundsMap.put(G3, soundPool.load(this, R.raw.gthree, 1));
        soundsMap.put(C4, soundPool.load(this, R.raw.cfour, 1));
        soundsMap.put(E4, soundPool.load(this, R.raw.efour, 1));
        soundsMap.put(G4, soundPool.load(this, R.raw.gfour, 1));



    }

    public void onButtonPlayClick(View view) {
        if (loaded > 11){
            AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = streamVolumeCurrent / streamVolumeMax;

            CheckBox checkBox = (CheckBox) findViewById(R.id.c1);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(C1), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.e1);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(E1), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.g1);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(G1), volume, volume, 1, 0, 1.0f);
            }

            checkBox = (CheckBox) findViewById(R.id.c2);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(C2), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.e2);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(E2), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.g2);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(G2), volume, volume, 1, 0, 1.0f);
            }


            checkBox = (CheckBox) findViewById(R.id.c3);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(C3), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.e3);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(E3), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.g3);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(G3), volume, volume, 1, 0, 1.0f);
            }


            checkBox = (CheckBox) findViewById(R.id.c4);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(C4), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.e4);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(E4), volume, volume, 1, 0, 1.0f);
            }
            checkBox = (CheckBox) findViewById(R.id.g4);
            if (checkBox.isChecked()) {
                soundPool.play(soundsMap.get(G4), volume, volume, 1, 0, 1.0f);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Still Loading", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
