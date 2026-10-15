package frc.lib.wrappers;

import com.revrobotics.*;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkLowLevel.PeriodicFrame;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkMaxConfigAccessor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import frc.lib.MMRobot;
import frc.robot.Robot;

public class NEO {
  private final SparkMax motor;
  private final RelativeEncoder encoder;
  private final SparkClosedLoopController controlLoopController;

  private ControlLoopType controlType = ControlLoopType.PERCENT;
  private double targetPosition = 0;
  private double targetVelocity = 0;

  private boolean positionPIDWrappingEnabled = false;
  private double positionPIDWrappingMaxInput = 0;
  private double positionPIDWrappingMinInput = 0;

  private double kS = 0;

  private boolean hasAbsoluteEncoder;

  /**
   * Creates a new NEO motor
   *
   * @param id CANID of the SparkMax the NEO is connected to.
   */
  public NEO(int id) {
    this(id, false);
  }

  /**
   * Creates a new NEO motor
   *
   * @param id   CANID of the SparkMax the NEO is connected to.
   * @param mode The idle mode of the motor. If true, the motor will brake when
   *             not powered. If
   *             false, the motor will coast when not powered.
   */
  public NEO(int id, IdleMode mode) {
    this(id, false, mode);
  }

  /**
   * Creates a new NEO motor
   *
   * @param id       CANID of the SparkMax the NEO is connected to.
   * @param reversed Whether the motor is reversed or not.
   * @param mode     The idle mode of the motor. If true, the motor will brake
   *                 when not powered. If
   *                 false, the motor will coast when not powered.
   */
  public NEO(int id, boolean reversed, IdleMode mode) {
    motor = new SparkMax(id, MotorType.kBrushless);
    Timer.delay(0.050);

    // If a parameter set fails, this will add more time to alleviate any bus
    // traffic default is 20ms
    motor.setCANTimeout(50);

    register();

    SparkMaxConfig config = new SparkMaxConfig();

    config.inverted(reversed);
    config.idleMode(mode);

    motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    encoder = motor.getEncoder();
    controlLoopController = motor.getClosedLoopController();

    hasAbsoluteEncoder = false;
  }

  /**
   * Creates a new NEO motor
   *
   * @param id       CANID of the SparkMax the NEO is connected to.
   * @param reversed Whether the motor is reversed or not.
   */
  public NEO(int id, boolean reversed) {
    this(id, reversed, IdleMode.kBrake);
  }

  public void setHasAbsoluteEncoder(boolean hasAbsoluteEncoder) {
    this.hasAbsoluteEncoder = hasAbsoluteEncoder;
  }

  public void setAbsoluteEncoderInverted(boolean invert) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.absoluteEncoder.inverted(invert);
    configure(config);
  }

  public void configure(SparkMaxConfig config) {
    motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  public SparkMaxConfigAccessor getConfigAccessor() {
    return motor.configAccessor;
  }

  public void setIdleMode(SparkBaseConfig.IdleMode a) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.idleMode(a);
    configure(config);
  }

  public IdleMode getIdleMode() {
    return motor.configAccessor.getIdleMode();
  }

  public void setAbsoluteInverted(boolean invert) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.absoluteEncoder.inverted(invert);
    configure(config);
  }

  public double getAbsolutePosition() {
    return motor.getAbsoluteEncoder().getPosition();
  }

  public double getAbsoluteVelocity() {
    return motor.getAbsoluteEncoder().getVelocity();
  }

  /**
   * Sets the target position for the NEO.
   *
   * @param position             Position to set the NEO to in rotations.
   * @param arbitraryFeedForward Arbitrary feed forward to add to the motor
   *                             output.
   */
  public void setTargetPosition(double position, double arbitraryFeedForward, int slot) {
    if (Robot.isReal()) {
      controlLoopController.setReference(
          position,
          ControlType.kPosition,
          ClosedLoopSlot.valueOf("kSlot" + slot),
          arbitraryFeedForward + ((Math.signum(position - getPosition()) * kS)),
          ArbFFUnits.kVoltage);
    }
    targetPosition = position;
    controlType = ControlLoopType.POSITION;
  }

  public void setTargetPosition(double position, double arbitraryFeedForward) {
    setTargetPosition(position, arbitraryFeedForward, 0);
  }

  public void setTargetPosition(double position) {
    setTargetPosition(position, 0, 0);
  }

  /**
   * Sets the target velocity for the NEO.
   *
   * @param velocity Velocity to set the NEO to in rotations per minute.
   */
  public void setTargetVelocity(double velocity) {
    setTargetVelocity(velocity, 0, 0);
  }

  /**
   * Sets the target velocity for the NEO.
   *
   * @param velocity             Velocity to set the NEO to in rotations per
   *                             minute.
   * @param arbitraryFeedForward Arbitrary feed forward to add to the motor
   *                             output.
   */
  public void setTargetVelocity(double velocity, double arbitraryFeedForward, int slot) {
    if (velocity == 0) {
      setVoltage(0);
    } else {
      controlLoopController.setReference(
          velocity,
          ControlType.kVelocity,
          ClosedLoopSlot.valueOf("kSlot" + slot),
          arbitraryFeedForward + (Math.signum(velocity) * kS),
          ArbFFUnits.kVoltage);
    }
    targetVelocity = velocity;
    controlType = ControlLoopType.VELOCITY;
  }

  public void setVoltage(double voltage) {
    motor.setVoltage(voltage);
    controlLoopController.setReference(voltage, ControlType.kVoltage);
  }

  public void setVoltage(Voltage voltage) {
    motor.setVoltage(voltage);
  }

  public void set(double percent) {
    setVoltage(percent * RobotController.getBatteryVoltage());
    controlType = ControlLoopType.PERCENT;
  }

  /**
   * Sets the NEO to follow another NEO.
   *
   * @param host The host NEO to follow. This NEO will mirror the host NEO's
   *             voltage outputs.
   */
  public void follow(NEO host) {
    follow(host, false);
  }

  /**
   * Sets the NEO to follow another NEO.
   *
   * @param host     The host NEO to follow. This NEO will mirror the host NEO's
   *                 voltage outputs.
   * @param inverted Sets the NEO to be inverted.
   */
  public void follow(NEO host, boolean inverted) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.follow(host.getID(), inverted);
    configure(config);
  }

  public void setPositionConversionFactor(double ratio) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.encoder.positionConversionFactor(ratio);
    config.absoluteEncoder.positionConversionFactor(ratio);
    configure(config);
  }

  public void setVelocityConversionFactor(double ratio) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.encoder.velocityConversionFactor(ratio);
    config.absoluteEncoder.velocityConversionFactor(ratio);
    configure(config);
  }

  public void setAbsolutePositionConversionFactor(double ratio) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.absoluteEncoder.positionConversionFactor(ratio);
    configure(config);
  }

  public void setAbsoluteVelocityConversionFactor(double ratio) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.absoluteEncoder.velocityConversionFactor(ratio);
    configure(config);
  }

  public void setMeasurementPeriod(int periodMs) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.encoder.uvwMeasurementPeriod(periodMs);

    configure(config);
  }

  public void setAverageDepth(int depth) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.encoder.uvwAverageDepth(depth);

    configure(config);
  }

  public void tick() {
    if (Robot.isSimulation() && controlType == ControlLoopType.POSITION) {
      double currentPosition = getPosition();

      if (hasAbsoluteEncoder) {
        currentPosition = motor.getAbsoluteEncoder().getPosition();
      }

      double error = targetPosition - currentPosition;

      if (positionPIDWrappingEnabled) {
        double errorBound = (positionPIDWrappingMaxInput - positionPIDWrappingMinInput) / 2.0;
        error = MathUtil.inputModulus(error, -errorBound, errorBound);
      }

      encoder.setPosition(encoder.getPosition() + getConfigAccessor().closedLoop.getP() * error);
    } else if (Robot.isSimulation() && DriverStation.isDisabled()) {
      setVoltage(0);
    }
  }

  public void register() {
    MMRobot.registerNEO(this);
  }

  /**
   * Gets the position of the NEO in rotations.
   *
   * @return The position of the NEO in rotations relative to the last 0 position.
   */
  public double getPosition() {
    double pos = encoder.getPosition();

    if (Robot.isSimulation() && controlType == ControlLoopType.VELOCITY) {
      pos /= getConfigAccessor().encoder.getVelocityConversionFactor();
    }

    return pos;
  }

  /**
   * Gets the velocity of the NEO in rotations per minute.
   *
   * @return The instantaneous velocity of the NEO in rotations per minute.
   */
  public double getVelocity() {
    return encoder.getVelocity();
  }

  /**
   * Gets the target position of the NEO in rotations.
   *
   * @return The target position of the NEO in rotations.
   */
  public double getTargetPosition() {
    return targetPosition;
  }

  /**
   * Gets the target velocity of the NEO in rotations per minute.
   *
   * @return The target velocity of the NEO in rotations per minute.
   */
  public double getTargetVelocity() {
    return targetVelocity;
  }

  /**
   * Gets the current of the NEO in amps.
   *
   * @return The current of the NEO in amps.
   */
  public double getCurrent() {
    return motor.getOutputCurrent();
  }

  /**
   * Sets the current encoder position to whatever is specified in rotations.
   *
   * @param position The position to set the encoder to in rotations.
   */
  public void setEncoderPosition(double position) {
    encoder.setPosition(position);
  }

  public void setInverted(boolean inverted) {
    if (Robot.isSimulation()) {
      return;
    }

    SparkMaxConfig config = new SparkMaxConfig();
    config.inverted(inverted);
    configure(config);
  }

  public void setPositionPIDWrappingRange(double minInput, double maxInput) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoop.positionWrappingEnabled(positionPIDWrappingEnabled);
    config.closedLoop.positionWrappingInputRange(minInput, maxInput);
    configure(config);
    positionPIDWrappingEnabled = true;
    positionPIDWrappingMaxInput = maxInput;
    positionPIDWrappingMinInput = minInput;
  }

  public void disablePositionPIDWrapping() {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoop.positionWrappingEnabled(false);
    configure(config);

    positionPIDWrappingEnabled = false;
  }

  /**
   * Sets the current limit.
   *
   * @param limit The current limit the NEO will follow. The NEO will not pull
   *              more than this amount
   *              of current.
   */
  public void setCurrentLimit(int limit) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.smartCurrentLimit(limit);
    configure(config);
  }

  public void setClosedLoopRampRate(double time) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoopRampRate(time);
    configure(config);
  }

  public void setOpenLoopRampRate(double time) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.openLoopRampRate(time);
    configure(config);
  }

  /** Burns all configured settings to the NEO's flash memory. */
  public void burn() {
    SparkMaxConfig config = new SparkMaxConfig();
    REVLibError error = motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    for (int i = 0; i <= 4; i++) {
      if (error == REVLibError.kOk)
        break;
      error = motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }
  }

  /**
   * Sets PID values.
   *
   * @param kP  The proportional gain constant for PIDFF control.
   * @param kI  The integral gain constant for PIDFF control.
   * @param kD  The derivative gain constant for PIDFF control.
   * @param kIZ The I-Zone constant for PIDFF control. The I term will not start
   *            accumulating until
   *            the error is less than this value.
   * @param kFF The feedforward gain constant for PIDFF control.
   */
  public void configurePIDFF(double kP, double kI, double kD, double kIZ, double kFF, int slot) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoop.pidf(kP, kIZ, kD, kFF, ClosedLoopSlot.valueOf("kSlot" + slot));

    configure(config);
  }

  public void configurePIDFF(double kP, double kI, double kD, double kIZ, double kFF) {
    configurePIDFF(kP, kI, kD, kIZ, kFF, 0);
  }

  public void configurePIDFF(double kP, double kI, double kD, double kFF) {
    configurePIDFF(kP, kI, kD, 0, kFF, 0);
  }

  public void configurePIDFF(double kP, double kI, double kD) {
    configurePIDFF(kP, kI, kD, 0, 0, 0);
  }

  public void setP(double kP) {
    setP(kP, 0);
  }

  public void setP(double kP, int slot) {
    configurePIDFF(kP, getI(), getD(), getIZ(), getFF(), slot);
  }

  public void setI(double kI) {
    setI(kI, 0);
  }

  public void setI(double kI, int slot) {
    configurePIDFF(getP(), kI, getD(), getIZ(), getFF(), slot);
  }

  public void setD(double kD) {
    setD(kD, 0);
  }

  public void setD(double kD, int slot) {
    configurePIDFF(getP(), getI(), kD, getIZ(), getFF(), slot);
  }

  public void setIZone(double kIZone) {
    setIZone(kIZone, 0);
  }

  public void setIZone(double kIZone, int slot) {
    configurePIDFF(getP(), getI(), getD(), kIZone, getFF(), slot);
  }

  public void setFF(double kFF) {
    setFF(kFF, 0);
  }

  public void setFF(double kFF, int slot) {
    configurePIDFF(getP(), getI(), getD(), getIZ(), kFF, slot);
  }

  public void setKS(double kS) {
    this.kS = kS;
  }

  public double getKS() {
    return this.kS;
  }

  /**
   * Gets the current applied % output from the motor.
   *
   * @return The current applied % output from the motor.
   */
  public double getOutput() {
    if (Robot.isReal()) {
      return motor.getAppliedOutput();
    } else {
      return motor.getAppliedOutput();
    }
  }

  public double getVoltage() {
    return motor.get() * RobotController.getBatteryVoltage();
  }

  /**
   * Clamps the applied power output for the NEO to this range.
   *
   * @param min Minimum power to output
   * @param max Maximum power to output.
   */
  public void setOutputRange(double min, double max) {
    SparkMaxConfig config = new SparkMaxConfig();

    config.closedLoop.outputRange(min, max);
    configure(config);
  }

  /**
   * Gets the proportional gain constant for PIDFF controller.
   *
   * @return The proportional gain constant for PIDFF controller.
   */
  public double getP() {
    return getConfigAccessor().closedLoop.getP();
  }

  /**
   * Gets the integral gain constant for PIDFF controller.
   *
   * @return The integral gain constant for PIDFF controller.
   */
  public double getI() {
    return getConfigAccessor().closedLoop.getI();
  }

  /**
   * Gets the derivative gain constant for PIDFF controller.
   *
   * @return The derivative gain constant for PIDFF controller.
   */
  public double getD() {
    return getConfigAccessor().closedLoop.getD();
  }

  /**
   * Gets the I-Zone constant for PIDFF controller.
   *
   * @return The I-Zone constant for PIDFF control.
   */
  public double getIZ() {
    return getConfigAccessor().closedLoop.getIZone();
  }

  /**
   * Gets the feedforward gain constant for PIDFF controller.
   *
   * @return The feedforward gain constant for PIDFF controller.
   */
  public double getFF() {
    return getConfigAccessor().closedLoop.getFF();
  }

  /**
   * Gets the CANID of the SparkMax running this motor.
   *
   * @return The CANID of the SparkMax running this motor.
   */
  public int getID() {
    return motor.getDeviceId();
  }

  /**
   * Gets the CANSparkMax object behind this motor.
   *
   * @return The CANSparkMax object behind this motor.
   */
  public SparkMax getMotor() {
    return motor;
  }

  public RelativeEncoder getEncoder() {
    return encoder;
  }

  public SparkAbsoluteEncoder getAbsoluteEncoder() {
    return motor.getAbsoluteEncoder();
  }

  public SparkClosedLoopController getPID() {
    return controlLoopController;
  }

  // TODO setup status frames n shit
  // // Documentation:
  // //
  // https://docs.revrobotics.com/sparkmax/operating-modes/control-interfaces#periodic-status-frames
  // public enum StatusFrame {
  // APPLIED_FAULTS_FOLLOWER(PeriodicFrame.kStatus0, 10),
  // VELO_TEMP_VOLTAGE_CURRENT(PeriodicFrame.kStatus1, 20),
  // POSITION(PeriodicFrame.kStatus2, 20),
  // ANALOG_VOLTAGE_VELO_POS(PeriodicFrame.kStatus3, 50),
  // ALTERNATE_VELO_POS(PeriodicFrame.kStatus4, 20),
  // ABSOLUTE_ENCODER_POS(PeriodicFrame.kStatus5, 200),
  // ABSOLUTE_ENCODER_VELO(PeriodicFrame.kStatus6, 200);

  // private final PeriodicFrame frame;
  // private final int defaultPeriod; // ms

  // StatusFrame(PeriodicFrame frame, int defaultPeriod) {
  // this.frame = frame;
  // this.defaultPeriod = defaultPeriod;
  // }

  // public PeriodicFrame getFrame() {
  // return frame;
  // }

  // public int getDefaultPeriod() {
  // return defaultPeriod;
  // }
  // }

  public enum ControlLoopType {
    POSITION,
    VELOCITY,
    PERCENT;
  }
}