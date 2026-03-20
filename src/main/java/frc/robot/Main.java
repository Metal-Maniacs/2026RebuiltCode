// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * Do NOT add any static variables to this class, or any initialization at all.
 * Unless you know what
 * you are doing, do not modify this file except to change the parameter class
 * to the startRobot
 * call.
 */
public final class Main {
 // public static void main(String[] args) {
  /**
   * Main initialization function. Do not perform any initialization here.
   *
   * <p>
   * If you change your main robot class, change the parameter type.
   */

// took out the error 

   /* Error at edu.wpi.first.wpilibj.RobotBase.runRobot(RobotBase.java:457): 
   The startCompetition() method (or methods called by it) should have handled the exception above.    
   See https://wpilib.org/stacktrace for more information.    
   The above stacktrace can help determine where the error occurred.  
   Warning at edu.wpi.first.wpilibj.RobotBase.runRobot(RobotBase.java:450): 
   The robot program quit unexpectedly. 
   This is usually due to a code error.    	
   at frc.robot.Main.main(Main.java:54)  	
  
   at frc.robot.Robot.robotInit(Robot.java:129)  	
   at frc.robot.RobotContainer.<init>(RobotContainer.java:57)  	
   at frc.robot.subsystems.Hopper.<init>(Hopper.java:26)  	
   
   ERROR  1  The startCompetition() method (or methods called by it) 
   should have handled the exception above.  
   edu.wpi.first.wpilibj.RobotBase.runRobot(RobotBase.java:457)  	
   at com.revrobotics.spark.SparkBase.<init>(SparkBase.java:174)  	
   at com.revrobotics.spark.SparkLowLevel.<init>(SparkLowLevel.java:233) 
   Warning  1  The robot program quit unexpectedly. This is usually due to a code error.
  The above stacktrace can help determine where the error occurred.
  See https://wpilib.org/stacktrace for more information.  
  edu.wpi.first.wpilibj.RobotBase.runRobot(RobotBase.java:450)  
  Error at com.revrobotics.spark.SparkLowLevel.<init>(SparkLowLevel.java:233): 
  Unhandled exception: java.lang.IllegalStateException: 
  A CANSparkMax instance has already been created with this device ID: 9 
 */
public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}
