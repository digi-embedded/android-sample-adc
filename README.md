ADC Sample Application
======================

This application demonstrates the usage of the ADC API by monitoring the sample 
conversion of a selectable ADC channel.

The ConnectCore 6 SBC v3 platform has 3 available ADC channels located in the
GPIO expansion connector as follows:

* Pin 1: PMIC_ADCIN1 / PMIC_GPIO0
* Pin 2: PMIC_ADCIN2 / PMIC_GPIO1
* Pin 3: PMIC_ADCIN3 / PMIC_GPIO2

The ConnectCore 8X SBC platform has several ADC-capable pins located in the
expansion connector:

* Pin 5:  MCA_IO6
* Pin 6:  MCA_IO7
* Pin 7:  MCA_IO8
* Pin 10: MCA_IO5
* Pin 11: MCA_IO14
* Pin 25: i.MX ADC channel 0
* Pin 26: i.MX ADC channel 4
* Pin 27: i.MX ADC channel 1
* Pin 28: i.MX ADC channel 5

**\*\* NOTE:** The MCA_IO lines need to be configure as ADC in order to be
usable for this example. Note that not all MCA_IO lines are ADC-capable, but
all the ones in the list above are ADC-capable.

**\*\* NOTE:** The maximum voltage level of the analog input should not exceed
the reference tension used by each ADC chip.

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
* ConnectCore 8X SBC Pro

License
-------

Copyright (c) 2014-2021, Digi International Inc. <support@digi.com>

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