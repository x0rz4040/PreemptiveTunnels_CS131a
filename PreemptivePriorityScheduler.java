package cs131.pa5.CarsTunnels;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import cs131.pa5.Abstract.Scheduler;
import cs131.pa5.Abstract.Tunnel;
import cs131.pa5.Abstract.Vehicle;


/**
 * The preemptive priority scheduler assigns vehicles to tunnels based on their priority and supports 
 * preemption with ambulances.
 * It extends the Scheduler class.
 * @author cs131a
 *
 */
public class PreemptivePriorityScheduler extends Scheduler{
	
	private Collection<Tunnel> tunnels;
	
	private PriorityQueue<Vehicle> waitQ;
	
	HashMap<Vehicle, Tunnel> listofVehicles = new HashMap<Vehicle,Tunnel>();
	
	private ReentrantLock lock = new ReentrantLock();
	private Condition tunnelIsOpen = lock.newCondition();
	private Condition lineHasShortened = lock.newCondition();
	
	
	
	
	// Only Ambulances get the highest priority of 4

	// An ambulance can share a tunnel with all other vehicles 
	// EXCEPT another ambulance

	// When an ambulance enters a tunnel, all other vehicles in the tunnel must “pull over” 
	//or wait until the ambulance has completely passed through the tunnel. Only then may 
	//the rest of the vehicles continue making progress



	/**
	 * Creates an instance of a preemptive priority scheduler with the given name and tunnels
	 * @param name the name of the preemptive priority scheduler
	 * @param tunnels the tunnels where the vehicles will be scheduled to
	 */
	public PreemptivePriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name, tunnels);
		this.tunnels = tunnels;
		this.waitQ = new PriorityQueue<>(new Comparator<Vehicle>(){
			@Override
			public int compare(Vehicle v1, Vehicle v2) {
				return v2.getPriority() - v1.getPriority();
			}
		});
	}

	
	/*
	 * admit method determines whether a vehicle can either enter the tunnel (and hashmap to
	 * keep track of which vehicle is in which tunnel or be put in the waitQ. The method will
	 * only return a specific tunnel if the current vehicle can be admitted which will happen
	 * eventually
	 */
	 
	@Override
	public Tunnel admit(Vehicle vehicle) {
		
		lock.lock();
		try {
			//Put the vehicle in line
			waitQ.add(vehicle);
			
			//Repeat the following until the vehicle gets into a tunnel,
			//at which point the function will return (and signal to other threads)
			while(true) {
				//Inner loop waits until the vehicle is at the front of the line
				//This waiting loop "awaits" a different car entering a tunnel which will shorten the line
				while (waitQ.peek() != vehicle) {
					lineHasShortened.await();
				}
				//Now check all tunnels, if any are open then enter the tunnel and return (loop ends)
				for (Tunnel tunnel: tunnels) {
					if(tunnel.tryToEnter(vehicle) == true) {	//and tunnel.HasNoAmbulance		
						listofVehicles.put(vehicle,tunnel);
						waitQ.poll();
						return tunnel;
						//signal all that a vehicle is gone from the queue...finally block
					}
				}
				//No tunnels were open, await for one to open then recheck the waitQ etc and keep going.
				tunnelIsOpen.await();
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		} finally {
			lineHasShortened.signalAll();
			lock.unlock();
		}

		return null;
	}

	@Override
	public void exit(Vehicle vehicle) {
		
		lock.lock();
		try {
			if(vehicle instanceof Ambulance) {
				listofVehicles.get(vehicle).hasAmbulance = false;
			//	Tunnel t.hasAmbulance = false;
				tunnelIsOpen.signalAll();
				listofVehicles.get(vehicle).exitTunnel(vehicle);
			}
			else {
				tunnelIsOpen.signalAll();
				listofVehicles.get(vehicle).exitTunnel(vehicle);
			}
		}	finally {
			lock.unlock();
		}
	}
		
		
//		try { 
//			locker.lock();
//			//clearEnter.signalAll();
//			if(vehicle instanceof Ambulance) {
//				clearEnter.signalAll();
//				Tunnel tunnelUsed = vehicleToTunnel.get(vehicle);
//				removeVehicle(vehicle);
//				tunnelUsed.exitTunnel(vehicle);
//			}else {
//				Tunnel tunnelUsed = vehicleToTunnel.get(vehicle); 
//				removeVehicle(vehicle); 
//				tunnelUsed.exitTunnel(vehicle);
//			}
//		}finally { 
//			clearEnter.signal();
//			locker.unlock(); 
//		}
	}
	
	


