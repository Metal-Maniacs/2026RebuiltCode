// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.DriveConstants;

@SuppressWarnings("unused")
public class Hopper extends SubsystemBase {

  double HopperMultRight = 1;
  double HopperMultLeft = 1;

  double ExtendMultRight = 1;
  double ExtendMultLeft = 1;

  private SparkMax m_HopperMotor;
  private SparkMax m_HopperExtendMotor;

  public Hopper() {
    m_HopperMotor = new SparkMax(DriveConstants.kHopperCanId, MotorType.kBrushed);
    m_HopperExtendMotor = new SparkMax(DriveConstants.kExtendHopperCanId, MotorType.kBrushed);
  }

  public void disableRight() {
    HopperMultRight = 0;
  }

  public void enableRight() {
    HopperMultRight = 1;
  }

  public void disableLeft() {
    HopperMultLeft = 0;
  }

  public void enableLeft() {
    HopperMultLeft = 1;
  }

  public void disableRightExtend() {
    ExtendMultRight = 0;
  }

  public void enableRightExtend() {
    ExtendMultRight = 1;
  }

  public void disableLeftExtend() {
    ExtendMultLeft = 0;
  }

  public void enableLeftExtend() {
    ExtendMultLeft = 1;
  }

  /*
   * public void elevate(double speed){
   * m_elevatorMotor.set(speed);
   * }
   */

  public void moveRight(double hopperSpeed) {
    m_HopperMotor.set(hopperSpeed * HopperMultRight);
  }

  public void moveLeft(double hopperSpeed) {
    m_HopperMotor.set(hopperSpeed * HopperMultLeft);
  }

  public void extendIn(double extendSpeed) {
    m_HopperMotor.set(extendSpeed * ExtendMultRight);
  }

  public void extendOut(double extendSpeed) {
    m_HopperMotor.set(extendSpeed * ExtendMultLeft);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
