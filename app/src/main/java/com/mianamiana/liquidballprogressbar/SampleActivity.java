package com.mianamiana.liquidballprogressbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mianamiana.library.LiquidBallProgressBar;

public class SampleActivity extends AppCompatActivity implements LiquidBallProgressBar.OnProgressChangeListener {

    private LiquidBallProgressBar mLpb;
    private LiquidBallProgressBar mLpbCode;
    private LiquidBallProgressBar mLpbXml;
    private int value = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mLpb = (LiquidBallProgressBar) findViewById(R.id.lpb);
        mLpb.setProgress(50);
        mLpbCode = (LiquidBallProgressBar) findViewById(R.id.lpbCode);
        mLpbXml = (LiquidBallProgressBar) findViewById(R.id.lpbXml);


        mLpbCode.setOnProgressChangeListener(this);
        mLpbXml.setOnProgressChangeListener(this);

        customByCode(mLpbCode);

    }

    private void customByCode(LiquidBallProgressBar Lpb) {
        Lpb.setTextColor(0xffff5678);
        Lpb.setTextSize(18);
        Lpb.setWaveColor(0xffff5678);
        Lpb.setUnfilledWaveColor(0xfff0f0f0);
        Lpb.setTextOverlapColor(Color.WHITE);
        Lpb.setProgress(110);
        Lpb.setMaxProgress(200);
        Lpb.setBorderColor(0xffaeaeae);
        Lpb.setBorderWidth((int) getResources().getDimension(R.dimen.border_default_width));
    }


    @Override
    public void onProgressChange(View view, int progress) {
        switch (view.getId())
        {
            case R.id.lpbCode:
                ((LiquidBallProgressBar)view).setText("code" + "\n" + progress);
                break;
            case R.id.lpbXml:
                ((LiquidBallProgressBar)view).setText("xml" + "\n" + progress);
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mLpb.startWave();
        mLpbCode.startWave();
        mLpbXml.startWave();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLpb.endWave();
        mLpbCode.endWave();
        mLpbXml.endWave();
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnIncrease:
                mLpb.setProgressWithAnimation(mLpb.getProgress()+value);
                mLpbCode.setProgressWithAnimation(mLpbCode.getProgress()+value);
                mLpbXml.setProgressWithAnimation(mLpbXml.getProgress()+value);
                break;
            case R.id.btnDecrease:
                mLpb.setProgressWithAnimation(mLpb.getProgress()-value);
                mLpbCode.setProgressWithAnimation(mLpbCode.getProgress()-value);
                mLpbXml.setProgressWithAnimation(mLpbXml.getProgress()-value);
                break;
        }
    }

}
