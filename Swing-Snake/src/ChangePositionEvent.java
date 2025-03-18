import java.util.EventObject;

public class ChangePositionEvent extends EventObject {
	private int direction;
	public ChangePositionEvent(Object source,int direction) {
		super(source);
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}
}
