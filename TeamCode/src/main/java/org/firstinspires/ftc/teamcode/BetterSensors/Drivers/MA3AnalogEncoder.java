/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.BetterSensors.Drivers;

import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;

@AnalogSensorType
@DeviceProperties(name = "gvjhgv", xmlTag = "opticaldist", builtIn = true, description = "jytcjn")
public class MA3AnalogEncoder implements AnalogSensor, HardwareDevice
  {
  private final AnalogInputController analogInputController;
  private final int physicalPort;

  protected static final double apiLevelMin = 0.0;
  protected static final double apiLevelMax = 1.0;

  public MA3AnalogEncoder(AnalogInputController analogInputController, int physicalPort) {
    this.analogInputController = analogInputController;
    this.physicalPort = physicalPort;
  }

  @Override
  public String toString() {
    return String.format("AnalogAbsoluteEncoder");
  }

  public double getMaxVoltage() {
    // The sensor itself is a 5v sensor, reporting analog values from 0v to 5v. However, depending
    // on the level conversion hardware that might be between us and the sensor, that may get shifted
    // to a different range. We'll assume that we only ever shift *down* in range, not up, so we
    // can take the min of the sensor's natural level and what the input controller can do.
    final double sensorMaxVoltage = 5.0;
    return Math.min(sensorMaxVoltage, analogInputController.getMaxAnalogInputVoltage());
  }

  @Override
  public double readRawVoltage() {
    return analogInputController.getAnalogInputVoltage(physicalPort);
  }

    @Override
    public Manufacturer getManufacturer() {
      return Manufacturer.Lego;
    }

    public String getDeviceName() {
    return "MA3 Absolute Encoder";
  }

  public String getConnectionInfo() {
    return analogInputController.getConnectionInfo() + "; analog port " + physicalPort;
  }

  public int getVersion() {
    return 0;
  }

  public void resetDeviceConfigurationForOpMode() {
  }

  public void close() {

  }
}
