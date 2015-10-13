/**
 * Copyright (c) 2014-2015 Digi International Inc.,
 * All rights not expressly granted are reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Digi International Inc. 11001 Bren Road East, Minnetonka, MN 55343
 * =======================================================================
 */

package com.digi.android.ADC;

import android.adc.ADC;
import android.adc.ADCListener;
import android.adc.ADCSample;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * ADC sample application.
 *
 * <p>This application demonstrates the usage of the ADC API by monitoring the sample
 * conversion of a selectable ADC channel.</p>
 *
 * <p>For a complete description on the example, refer to the 'README.md' file
 * included in the example directory.</p>
 */
public class ADCSampleApp extends Activity
		implements OnCheckedChangeListener, OnClickListener, ADCListener {

	// Constants.
	private final static int ACTION_DRAW_SAMPLE = 0;
	private final static String START_SAMPLING = "START SAMPLING";
	private final static String STOP_SAMPLING = "STOP SAMPLING";

	// Variables.
	private EditText channel, numSamples, samplePeriod, readSample, receivedSamples;
	private CheckBox mCheckBox;
	private Editable samples;
	private Button button;
	private ADC adc;
	private boolean isSampling = false;
	private boolean hasSubscribed = false;
	private int samplesCount = 0;
	private ADCSample receivedSample;

	private IncomingHandler handler = new IncomingHandler(this);

	/**
	 * Handler to manage UI calls from different threads.
	 */
	static class IncomingHandler extends Handler {
		private final WeakReference<ADCSampleApp> wActivity;

		IncomingHandler(ADCSampleApp activity) {
			wActivity = new WeakReference<ADCSampleApp>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			ADCSampleApp adcSampleApp = wActivity.get();

			if (adcSampleApp == null)
				return;

			switch (msg.what) {
				case ACTION_DRAW_SAMPLE:
					// Update sample count.
					adcSampleApp.samplesCount = adcSampleApp.samplesCount + 1;

					// Show received sample.
					adcSampleApp.readSample.setText(String.format("%d", adcSampleApp.receivedSample.sample));
					adcSampleApp.receivedSamples.setText(String.format("%d", adcSampleApp.samplesCount));

					int totalSamples;
					if (adcSampleApp.numSamples.getText() != null) {
						totalSamples = adcSampleApp.editTextToInt(adcSampleApp.numSamples);
					} else {
						totalSamples = 0;
					}

					if (adcSampleApp.samplesCount == totalSamples) {
						adcSampleApp.isSampling = false;
						adcSampleApp.channel.setEnabled(true);
						adcSampleApp.numSamples.setEnabled(true);
						adcSampleApp.samplePeriod.setEnabled(true);
						adcSampleApp.button.setText(START_SAMPLING);
					}
					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Instantiate the elements from layout.
		channel = (EditText)findViewById(R.id.channel);
		mCheckBox = (CheckBox)findViewById(R.id.checkbox);
		numSamples = (EditText)findViewById(R.id.numSamples);
		samplePeriod = (EditText)findViewById(R.id.samplePeriod);
		button = (Button)findViewById(R.id.sample_button);
		readSample = (EditText)findViewById(R.id.readSample);
		receivedSamples = (EditText)findViewById(R.id.receivedSamples);

		// Show initial values.
		mCheckBox.setChecked(false);
		button.setText(START_SAMPLING);
		readSample.setEnabled(false);
		receivedSamples.setText("0");
		receivedSamples.setEnabled(false);

		// Set event listeners for layout elements.
		mCheckBox.setOnCheckedChangeListener(this);
		button.setOnClickListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (isSampling) {
			adc.stopSampling();
		}

		if (hasSubscribed) {
			adc.unsubscribeListener(this);
		}
	}

	@Override
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

	@Override
	public void onClick(View v) {
		if (!isSampling) {
			Toast toast;
			// Check entered values.
			if (channel.getText().length() == 0 || channel.getText().toString().contains(".")) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid channel number.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			if (!mCheckBox.isChecked() && (numSamples.getText().length() == 0 || numSamples.getText().toString().contains("."))) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid number of samples.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			if (samplePeriod.getText().length() == 0 || samplePeriod.getText().toString().contains(".")) {
				toast = Toast.makeText(getApplicationContext(), "Enter a valid sample period.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}

			// Create ADC object and subscribe listener.
			int mChannel = editTextToInt(channel);
			if (mChannel < 0 || mChannel > 6) {
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

			// Start sampling.
			samplesCount = 0;
			isSampling = true;
			if (!mCheckBox.isChecked()) {
				adc.startSampling(editTextToInt(numSamples), editTextToInt(samplePeriod));
			} else {
				adc.startSampling(editTextToInt(samplePeriod));
			}
		} else {
			// Stop sampling.
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

	@Override
	public void sampleReceived (ADCSample sample) {
		receivedSample = sample;
		handler.sendEmptyMessage(ACTION_DRAW_SAMPLE);
	}

	private int editTextToInt(EditText text) {
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
}