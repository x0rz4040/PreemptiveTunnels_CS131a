package cs131.pa5.CarsTunnels;

import java.util.Collection;

import cs131.pa5.CarsTunnels.BasicTunnel;
import cs131.pa5.CarsTunnels.Car;
import cs131.pa5.CarsTunnels.PriorityScheduler;
import cs131.pa5.Abstract.Direction;
import cs131.pa5.Abstract.Factory;
import cs131.pa5.Abstract.Scheduler;
import cs131.pa5.Abstract.Tunnel;
import cs131.pa5.Abstract.Vehicle;

/**
 * The class implementing the Factory interface for creating instances of classes
 * @author cs131a
 *
 */
public class ConcreteFactory implements Factory {

    @Override
    public Tunnel createNewBasicTunnel(String name){
    		
    	Tunnel T = new BasicTunnel(name);
    	return T;
    }

    @Override
    public Vehicle createNewCar(String name, Direction direction){
    	Vehicle car =  new Car(name, direction);
    	return car;  
    }

    @Override
    public Vehicle createNewSled(String name, Direction direction){
    	Vehicle sled = new Sled(name, direction);
    	return sled;
    }

	@Override
	public Vehicle createNewAmbulance(String name, Direction direction) {
		Vehicle ambulance = new Ambulance(name, direction);
		return ambulance;
	}
	
    @Override
    public Scheduler createNewPriorityScheduler(String name, Collection<Tunnel> tunnels){
    	PriorityScheduler priority_Schedule = new PriorityScheduler(name,tunnels);
    	
    	return priority_Schedule;
    }

	@Override
	public Scheduler createNewPreemptivePriorityScheduler(String name, Collection<Tunnel> tunnels) {
		PreemptivePriorityScheduler preemptive_Schedule = new PreemptivePriorityScheduler(name, tunnels);
		return preemptive_Schedule;
	}
}
