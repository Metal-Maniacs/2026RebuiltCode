package frc.robot;

public class PIDController {

private double kP;
private double kI;
private double kD;
private double totalError;
private double lastError; 
private boolean resetOnOvershoot, daveyDTrick;

/**
 * A default constructor for a PIDController<br>
 * Must set pid values before ussing calculate
 */
public PIDController() {
    this(0, 0, 0, true, true);
}

public PIDController(PIDValues values) {
this(values.kP, values.kI, values.kD, true, true);
}

/**
 * a basic PIDController for random stuff (can be imporved greatly)
 * 
 * @param kP
 *              Proportional Constant
 * @param kI
 *              Intergral Constant
 * @param kD 
 *              Derivative COnstant
 * @param resetOnOverShoot
 *              Sets whether the I value should be reset when it is overshot 
 *              (Use if you aren't using D)
 * @param daveyDTrick
 */
public PIDController(double kP, double kI, double kD, boolean resetOnOvershoot, boolean daveyDTrick) {
    this.kP=kP;
    this.kI=kI;
    this.kD=kD;
    totalError=0;
    lastError=0;
    this.resetOnOvershoot=resetOnOvershoot;
    this.daveyDTrick=daveyDTrick;
}
    /**
 * Sets new values for the pid variables
 * 
 * @param kP
 *          the new p constnat
 * @param kI
 *          the new I constant
 * @param kD
 *          the new D constant
 */
    public void setPID(double kP, double kI, double kD) {
        this.kP=kP;
        this.kI=kI;
        this.kD=kD;
    }
    /**
     * calculates the output using the PIDController
     * 
     * @param targetValue
     *          The desired number 
     * @param currentValue
     *          the current value of whatever you're using (Gyro, Encoder)
     * @return
     *          the output using the p, i, and d values set in the constructor
     */
    public double calculate(double targetValue, double currentValue)  {
    
    double error=currentValue-targetValue;

    //reset if I should on overshoot, and I overshot
    if (resetOnOvershoot&&Math.signum(error)!=Math.signum(lastError)) {
        totalError=0;
    }

    totalError+=error;
    double pChange=-kP*error; //If my error is negative, spin moters faster
    double iChange=-kI*totalError;

    if (!daveyDTrick) {
        double dChange=-kD*(error-lastError); //(y2-y1)/(x2-x1)*constant
        double output=pChange+iChange+dChange;
        lastError=error;

        return output;
    }
    else {
        double defaultOutput=pChange+iChange;
        double output=defaultOutput+kD*Math.signum(defaultOutput);
        lastError=error;
        return output;
    }
}
    public void setValues(PIDValues value) {
        setPID(value.kP, value.kI, value.kD);
    }
    public void resetError() {
        totalError=0;
    }
}