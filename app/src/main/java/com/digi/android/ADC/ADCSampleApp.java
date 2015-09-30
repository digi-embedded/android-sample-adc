package com.example.android.ADC;

import android.adc.ADC;
import android.adc.ADCListener;
import android.adc.ADCSample;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class ADCSampleApp extends Activity implements OnCheckedChangeListener, OnClickListener, ADCListener {

	// Constants
	private final static String TAG = "ADCSample";
	private final static int ACTION_DRAW_SAMPLE = 0;
	private final static String START_SAMPLING = "START SAMPLING";
	private final static String STOP_SAMPLING = "STOP SAMPLING";

	// Variables
	private EditText channel, numSamples, samplePeriod, readSample, receivedSamples;
	private CheckBox mCheckBox;
	private Editable samples;
	private Button button;
	private ADC adc;
	private int mChannel;
	private boolean isSampling = false;
	private boolean hasSubscribed = false;
	private int samplesCount = 0;
	private ADCSample receivedSample;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_DRAW_SAMPLE:
				// Update sample count
				samplesCount = samplesCount + 1;

				// Show received sample
				readSample.setText(Integer.toString(receivedSample.sample));
				receivedSamples.setText(Integer.toString(samplesCount));

				int totalSamples;
				if (numSamples.getText() != null) {
					totalSamples = editTextToInt(numSamples);
				} else {
					totalSamples = 0;
				}

				if (samplesCount == totalSamples) {
					isSampling = false;

					channel.setEnabled(true);
					numSamples.setEnabled(true);
					samplePeriod.setEnabled(true);
					button.setText(START_SAMPLING);
				}
				break;
			}
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Instance the elements from layout
        channel = (EditText)findViewById(R.id.channel);
        mCheckBox = (CheckBox)findViewById(R.id.checkbox);
        numSamples = (EditText)findViewById(R.id.numSamples);
        samplePeriod = (EditText)findViewById(R.id.samplePeriod);
        button = (Button)findViewById(R.id.sample_button);
        readSample = (EditText)findViewById(R.id.readSample);
        receivedSamples = (EditText)findViewById(R.id.receivedSamples);

        // Show initial values
        mCheckBox.setChecked(false);
        button.setText(START_SAMPLING);
        readSample.setEnabled(false);
        receivedSamples.setText("0");
        receivedSamples.setEnabled(false);

        // Set event listeners for layout elements
        mCheckBox.setOnCheckedChangeListener(this);
        button.setOnClickListener(this);
    }

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			samples = numSamples.getText();
			numSamples.setEnabled(false);
			numSamples.setText("");
		} else {
			numSamples.setEnabled(true);
			numSamples.setText(samples);
		}
	}

	public void onClick(View v) {
		if (!isSampling) {
			Toast toast;
			// Check entered values
			if ((channel.getText().length() == 0) || (channel.getText().toString().indexOf(".") != -1)) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid channel number.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			if (!mCheckBox.isChecked() && ((numSamples.getText().length() == 0)  || (numSamples.getText().toString().indexOf(".") != -1))) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid number of samples.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			if ((samplePeriod.getText().length() == 0)  || (samplePeriod.getText().toString().indexOf(".") != -1)) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid sample period.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			// Create ADC object and subscribe listener
			mChannel = editTextToInt(channel);
			if ((mChannel < 0) || (mChannel > 6)) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid channel number.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}
			adc = new ADC(mChannel);
			adc.subscribeListener(this);
			hasSubscribed = true;

			channel.setEnabled(false);
			numSamples.setEnabled(false);
			samplePeriod.setEnabled(false);
			button.setText(STOP_SAMPLING);

			// Start sampling
			samplesCount = 0;
			isSampling = true;
			if (!mCheckBox.isChecked()) {
				adc.startSampling(editTextToInt(numSamples), editTextToInt(samplePeriod));
			} else {
				adc.startSampling(editTextToInt(samplePeriod));
			}
		} else {
			// Stop sampling
			isSampling = false;
			adc.stopSampling();

			channel.setEnabled(true);
			if (!mCheckBox.isChecked()) {
				numSamples.setEnabled(true);
			}
			samplePeriod.setEnabled(true);
			button.setText(START_SAMPLING);
		}
	}

	public void sampleReceived (ADCSample sample) {
		receivedSample = sample;
		handler.sendEmptyMessage(ACTION_DRAW_SAMPLE);
	}

	private int editTextToInt (EditText text) {
		if (text.getText().length() == 0) {
			return -1;
		} else {
			try {
				return Integer.parseInt(text.getText().toString());
			} catch (NumberFormatException e) {
				return -1;
			}
		}
	}

    public void onPause() {
	super.onPause();

	if (isSampling) {
		adc.stopSampling();
	}

	if (hasSubscribed) {
		adc.unsubscribeListener(this);
	}
    }
}