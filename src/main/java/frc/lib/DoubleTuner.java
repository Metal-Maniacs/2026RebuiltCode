package frc.lib;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.MMRobot;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleTuner{
    private final String path;
    private final Supplier<Double> doubleSupplier;
    private final Consumer<Double> doubleConsumer;

    public DoubleTuner(String path, Supplier<Double> doubleSupplier, Consumer<Double> doubleConsumer){
        this.path = "Tuners/" + path;
        this.doubleSupplier = doubleSupplier;
        this.doubleConsumer = doubleConsumer;

        SmartDashboard.putNumber(this.path, doubleSupplier.get());

        MMRobot.registerTuner(this);
    }

    public void tick(){
        double lastValue = doubleSupplier.get();
        double tunedValue = SmartDashboard.getNumber(path, lastValue);
        if(tunedValue != lastValue){
            doubleConsumer.accept(tunedValue);
        }
    }
}