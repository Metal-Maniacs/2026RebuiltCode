package frc.robot;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PIDValues {
	public double kP=0;
	public double kI=0;
	public double kD=0;
	
	public PIDValues(double kp, double ki, double kd) {
		this.kP=kp;
		this.kI=ki;
		this.kD=kd;
	}
	
	public String toString() {
		NumberFormat formatter=new DecimalFormat("#0.00000");
		return "<kp: "+formatter.format(kP)+", ki: "+formatter.format(kI)+", kd: "+formatter.format(kD)+">";
	}
}
