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

  // added contructs
  private final SparkMax m_HopperMotor;
  private final SparkMax m_HopperExtendMotor;
  //private final SparkMax m_HopperRollers;

// all the id are added
  public Hopper() {
    m_HopperMotor = new SparkMax(DriveConstants.kHopperCanId, MotorType.kBrushless);
    m_HopperExtendMotor = new SparkMax(DriveConstants.kExtendHopperCanId, MotorType.kBrushless);
    // no more rollers
     //m_HopperRollers = new SparkMax(DriveConstants.kRollerHopperCanId, MotorType.kBrushless);

  }

  // roll roll your fuel
/*public void rollers(double rollerspeed) {
m_HopperRollers.set(rollerspeed);
}*/
 
//for intake
public void intake(double hopperspeed){
  m_HopperMotor.set(hopperspeed);
}

// extemd and deextend fr the correct can id
    public void extend(double extendSpeed) {
    m_HopperExtendMotor.set(extendSpeed);
  }

    public void deextend(double deextendSpeed) {
    m_HopperExtendMotor.set(deextendSpeed);
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
