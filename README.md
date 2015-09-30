ADC Sample Application
======================

This application demonstrates the usage of the ADC API by monitoring the sample 
conversion of a selectable ADC channel.

The ConnectCore 6 SBC v2 platform has 3 available ADC channels located in the
CPIO expansion connector as follows:

* Pin 1: PMIC_ADCIN1 / PMIC_GPIO0
* Pin 2: PMIC_ADCIN2 / PMIC_GPIO1
* Pin 3: PMIC_ADCIN3 / PMIC_GPIO2

**\*\* NOTE**: The maximum voltage level of the analog input is 2.5V.

Demo requirements
-----------------

To run this example you need:

* One compatible device to host the application.
* Network connection between the device and the host PC in order to transfer
  and launch the application.
* Establish remote target connection to your Digi hardware before running this
  application.

Demo setup
----------

Make sure the hardware is set up correctly:

1. The device is powered on.
2. The device is connected directly to the PC or to the Local Area Network (LAN)
   by the Ethernet cable.
3. The desired ADC channel is enabled.

Demo run
--------

The example is already configured, so all you need to do now is to build and
launch the project.

While it is running, the application displays some boxes to select the ADC
channel to monitor, the desired number of samples, and the sampling period
in ms.

Once configured, click **START SAMPLING** to start the conversion. Both sample
values and number of samples are shown by the application.

The sample conversion automatically stops when the configured number of samples
is reach.

If no number of samples is set, the sample conversion continues indefinitely.
Click the button again to stop the sample conversion.

Tested on
---------

* ConnectCore Wi-i.MX53
* ConnectCard for i.MX28
* ConnectCore 6 Adapter Board
* ConnectCore 6 SBC v2