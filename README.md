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

Copyright (c) 2014-2016, Digi International Inc. <support@digi.com>

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.