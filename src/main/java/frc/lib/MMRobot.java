package frc.lib;

import edu.wpi.first.wpilibj.PowerDistribution; 
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import frc.lib.wrappers.NEO;

import java.util.ArrayList; 
import java.util.List; 
import java.util.function.Consumer; 
import java.util.function.Supplier; 

/* * Some of this is adapted from 3005's 2022 Code * Original source published at https://github.com/FRC3005/Rapid-React-2022-Public/tree/d499655448ed592c85f9cfbbd78336d8841f46e2 */ 

public class MMRobot { 
private static List<NEO> neos = new ArrayList<>(); 
// private static List<PhotonPoseCamera> cameras = new ArrayList<>(); 
private static List<DoubleTuner> 
tuners = new ArrayList<>(); 
private static PowerDistribution powerDistribution = new PowerDistribution(20, ModuleType.kRev); 


public static void registerNEO(NEO neo) { neos.add(neo); }
 

public static void registerTuner(DoubleTuner tuner) { tuners.add(tuner); }

public static void tuneDouble(
	String path, Supplier<Double> doubleSupplier, Consumer<Double> doubleConsumer) { new DoubleTuner(path, doubleSupplier, doubleConsumer); } 

public static void outputTick() { neos.forEach(NEO::tick); tuners.forEach(DoubleTuner::tick); } 

public static void simulationTick() {

	} 
}