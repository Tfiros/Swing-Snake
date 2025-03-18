import java.awt.*;
import java.util.EventObject;

public class FoodEvent extends EventObject {
	private Point p;
	private int points;
	public FoodEvent(Object source, Point p, int points) {
		super(source);
		this.points=points;
		this.p = p;
	}

	public int getPoints() {
		return points;
	}

	public Point getP() {
		return p;
	}
}
