package com.vlath.keyboard;

/**
 * Created by todo on 01.12.2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
//import android.util.Log;

public class SeekPreference extends Preference implements OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private int mProgress;
    private int mMaxValue;

    public SeekPreference(Context context) {
        this(context, null, 0);
    }

    public SeekPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.seek_dialog);

        // Convoluted way of getting the android:max attribute of this
        // Preference
        int[] set = { android.R.attr.max };
        mMaxValue = context.obtainStyledAttributes(attrs, set).getInt(0, 20);
    }

    private void setValueLabel(View view, int value) {
        ((TextView) view.findViewById(R.id.seekbar_value))
                .setText(Integer.toString(mSeekBar.getProgress()));
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mSeekBar = view.findViewById(R.id.seekbar);
        mSeekBar.setProgress(mProgress);
        mSeekBar.setMax(mMaxValue);
        mSeekBar.setOnSeekBarChangeListener(this);

        // Initial setting of the value TextView
        setValueLabel(view, mSeekBar.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;

        setValue(progress);
        setValueLabel(seekBar.getRootView(), seekBar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // not used
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // not used
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mProgress) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != mProgress) {
            mProgress = value;
            notifyChanged();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }
}