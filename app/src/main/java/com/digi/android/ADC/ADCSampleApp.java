/**
 * Copyright (c) 2014-2016 Digi International Inc.,
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
import android.adc.ADCManager;
import android.adc.ADCSample;
import android.adc.IADCListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
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
public class ADCSampleApp extends Activity implements OnClickListener, IADCListener {

	// Constants.
	private final static int ACTION_DRAW_SAMPLE = 0;
	private final static String START_SAMPLING = "START SAMPLING";
	private final static String STOP_SAMPLING = "STOP SAMPLING";

	// Variables.
	private EditText channel, samplePeriod, readSample, receivedSamples;
	private Button button;

	private ADCManager manager;
	private ADC adc;
	private ADCSample receivedSample;

	private boolean isSampling = false;
	private boolean hasSubscribed = false;
	private int samplesCount = 0;

	private IncomingHandler handler = new IncomingHandler(this);

	/**
	 * Handler to manage UI calls from different threads.
	 */
	static class IncomingHandler extends Handler {
		private final WeakReference<ADCSampleApp> wActivity;

		IncomingHandler(ADCSampleApp activity) {
			wActivity = new WeakReference<>(activity);
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
					adcSampleApp.readSample.setText(String.format("%d", adcSampleApp.receivedSample.getSample()));
					adcSampleApp.receivedSamples.setText(String.format("%d", adcSampleApp.samplesCount));

					break;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		// Instantiate the elements from layout.
		channel = (EditText)findViewById(R.id.channel);
		samplePeriod = (EditText)findViewById(R.id.samplePeriod);
		button = (Button)findViewById(R.id.sample_button);
		readSample = (EditText)findViewById(R.id.readSample);
		receivedSamples = (EditText)findViewById(R.id.receivedSamples);

		// Show initial values.
		button.setText(START_SAMPLING);
		readSample.setEnabled(false);
		receivedSamples.setText("0");
		receivedSamples.setEnabled(false);

		// Set event listeners for layout elements.
		button.setOnClickListener(this);

		// Get the ADC manager.
		manager = (ADCManager)getSystemService(ADC_SERVICE);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (isSampling) {
			adc.stopSampling();
		}

		if (hasSubscribed) {
			adc.unregisterListener(this);
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
			adc = manager.createADC(mChannel);
			adc.registerListener(this);
			hasSubscribed = true;

			channel.setEnabled(false);
			samplePeriod.setEnabled(false);
			button.setText(STOP_SAMPLING);

			// Start sampling.
			samplesCount = 0;
			isSampling = true;
			adc.startSampling(editTextToInt(samplePeriod));
		} else {
			// Stop sampling.
			isSampling = false;
			adc.stopSampling();

			channel.setEnabled(true);
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