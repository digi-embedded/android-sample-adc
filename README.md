ADC Sample Application
======================

This application demonstrates the usage of the ADC API by monitoring the sample 
conversion of a selectable ADC channel.

The ConnectCore 6 SBC v3 platform has 3 available ADC channels located in the
GPIO expansion connector as follows:

* Pin 1: PMIC_ADCIN1 / PMIC_GPIO0
* Pin 2: PMIC_ADCIN2 / PMIC_GPIO1
* Pin 3: PMIC_ADCIN3 / PMIC_GPIO2

**\*\* NOTE**: The maximum voltage level of the analog input is 2.5V.

Demo requirements
-----------------

To run this example you need:

* One compatible device to host the application.
* A USB connection between the device and the host PC in order to transfer and
  launch the application.

Demo setup
----------

Make sure the hardware is set up correctly:

1. The device is powered on.
2. The device is connected directly to the PC by the micro USB cable.
3. The desired ADC channel is enabled.

Demo run
--------

The example is already configured, so all you need to do now is to build and
launch the project.

While it is running, the application displays some boxes to select the ADC
channel to monitor and the sampling period in milliseconds.

Once configured, click **START SAMPLING** to start the conversion. Both sample
values and number of samples are shown by the application.

Click **STOP SAMPLING** to stop the receiving ADC samples.

Compatible with
---------------

* ConnectCore 6 SBC v3

License
-------

This software is open-source software. Copyright Digi International, 2014-2016.

This Source Code Form is subject to the terms of the Mozilla Public License,
v. 2.0. If a copy of the MPL was not distributed with this file, you can obtain
one at http://mozilla.org/MPL/2.0/.