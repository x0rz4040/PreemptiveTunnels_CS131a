package cs131.pa5.CarsTunnels;

import cs131.pa5.Abstract.Direction;
import cs131.pa5.Abstract.Vehicle;

/**
 * An ambulance is a high priority vehicle that can enter any tunnel, as long as
 * there is no other ambulance currently in.
 * 
 * @author cs131a
 * 
 */
public class Ambulance extends Vehicle{

	/**
	 * Creates a new instance of the class Ambulance with the given name and direction
	 * @param name the name of the ambulance to create
	 * @param direction the direction of the ambulance to create
	 */
	public Ambulance (String name, Direction direction) {
		super(name, direction);
		this.setPriority(4);
	}

	@Override
	protected int getDefaultSpeed() {
		return 9;
	}
	
	@Override
    public String toString() {
        return String.format("%s AMBULANCE %s", super.getDirection(), super.getName());
    }
}
