// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.core.CoreTalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.DriveConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//might not need these ^

@SuppressWarnings("unused")

public class Shooter extends SubsystemBase {

  //================Defining==============
  //TalonFX Motor;     //Kraken provids all the power/force

  SparkMax rotationMotor; //Neo 1.1 rotates the base of turret
  SparkMax archMotor;     //Neo 550 comed out to controll the arch the balls follow
 
  CANcoder shooterPowerCancoder; 
  CANcoder rotationCancoder;

  PIDController powerMotorPID;
  PIDController rotationMotorPID;
  PIDController archMotorPID;
  
  //the reference code has velocity voltage for a flywheel and digital imput for limit switches
  //here

  static double CANRotatedDegrees = 0.0; //I have no clue where they got these numbers from
  double currentDegree;
  double currentRotationPower;
  double previousDegree;
  double shooterDegree;

  //Christinas Code
  private final TalonFX shooterMotor;
  
  //creates a new shooter
  public Shooter() {
  
  //=============Constructors===========
    shooterMotor = new TalonFX(DriveConstants.SHOOTER_CAN_ID);
  
  }
  public void useShooter(double shooterSpeed) {
    shooterMotor.set(shooterSpeed);
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
