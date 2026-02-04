package frc.robot.subsystems;

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

public PIDController(PIDValues   values) {
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
    public void setPID(double kP, double kI, double kD) {
        this.kP=kP;
        this.kI=kI;
        this.kD=kD;
    }
    public double calculate(double targetValue, double currentValue)  {
    
    double error=currentValue-targetValue;

    if (restOnOvershoot&&Math.signum(error) !=Math.signum(lastError)) {
        totalError=0;
    }

    totalError+=error;
    double pChange=-kP*error;
    double iChange=-kI*totalError;

    if (!daveyDTrick) {
        double dChange=-kd*(error-lastError);
        double output=pChange+iChnage+dChange;
        lastError=error;

        return output;
    }
    public void setValues(PIDValues vales) {
        setPID(value.kP, values.kI, values.kD);
    }
    public void resetError() {
        totalError=0;
    }
}


}

}