package com.mm.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mm.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class EasyJCVideoPlayer extends JCVideoPlayerStandard {

    private LinearLayout llDuration;
    private TextView tvDuration;

    public EasyJCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyJCVideoPlayer(Context context) {
        super(context);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        llDuration = findViewById(R.id.llDuration);
        tvDuration =   findViewById(R.id.tvDuration);
        SeekBar seekBar =  findViewById(R.id.bottom_seek_progress);
        seekBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.video_seek_progess));
        ProgressBar progressBar=  findViewById(R.id.bottom_progress) ;
        progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.video_progress));
        TextView txtDuration =   findViewById(R.id.tvDuration);
        txtDuration.setVisibility(GONE);
    }

    @Override
    public void setUiWitStateAndScreen(int state) {
        super.setUiWitStateAndScreen(state);
        switch (currentState) {
            case CURRENT_STATE_PREPARING:
                //隐藏时长
                llDuration.setVisibility(View.GONE);
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
            case CURRENT_STATE_ERROR:
                //显示时长
                llDuration.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        //显示时长
        llDuration.setVisibility(View.VISIBLE);
    }

    public void setDurationText(String text) {
        tvDuration.setText(text);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_easy_video_player;
    }
}
