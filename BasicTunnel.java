package cs131.pa5.CarsTunnels;

import java.util.LinkedList;

import cs131.pa5.CarsTunnels.Car;
import cs131.pa5.CarsTunnels.Sled;
import cs131.pa5.Abstract.Tunnel;
import cs131.pa5.Abstract.Vehicle;

/**
 * 
 * The class for the Basic Tunnel, extending Tunnel.
 * @author cs131a
 *
 */
public class BasicTunnel extends Tunnel{

	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * @param name the name of the basic tunnel
	 */
	int numCar = 0;
	int numSled = 0;
	boolean hasAmbulance = false;
	
	LinkedList<Vehicle> vehicle_List = new LinkedList<Vehicle>();
	public BasicTunnel(String name) {
		super(name);
	}
	
	/*
	 * takes in a vehicle, determines what kind of vehicle it ensures that 
	 * certain restrictions are handled and that the vehicle is indeed allowed to safely enter the tunnel
	 * @param vehicle, the vehicle to be admitted
	 * returns boolean value
	 */
	@Override
	protected boolean tryToEnterInner(Vehicle vehicle) {
		
		if(vehicle instanceof Car){
			//First, conditions that ban entry
			if (numSled > 0) return false;
			if (numCar >= 3) return false;
			for (Vehicle v : vehicle_List) {
				if (vehicle.getDirection() != v.getDirection()) return false;
			}
			
			//Else, car can enter
			vehicle_List.add(vehicle);
			numCar++;
			return true;					
		}
		
		else if(vehicle instanceof Sled){
			
			if(vehicle_List.isEmpty()){
				numSled++;
				vehicle_List.add(vehicle);
				return true;
			}
			else{
				return false;
			}
		}
		else if(vehicle instanceof Ambulance) {
			if(hasAmbulance == true) {
				return false;
			}
			else {
				hasAmbulance = true;
				for(Vehicle v: vehicle_List) {
					v.pullOverForAmbulance();
				}
				vehicle_List.add(vehicle);

				return true;
				
					
				
			}
		}
		return false;

	}

	
	/*
	 * simply takes the  given vehicle out of the tunnel	
	 */
	@Override
	protected void exitTunnelInner(Vehicle vehicle) {

		if(vehicle instanceof Car){
			numCar -= 1;
			vehicle_List.remove(vehicle);
		}
		else if(vehicle instanceof Sled){
			numSled -=1;
			vehicle_List.remove(vehicle);
		}
		
		//if it's ambulance remove the vehicle before looping through the list of 
		// vehicles and tell them to keep driving
		else if(vehicle instanceof Ambulance) {
			hasAmbulance = false;
			vehicle_List.remove(vehicle);
			for(Vehicle v: vehicle_List) {
				v.continueDriving();
			}
		}
		
	}
	
}
