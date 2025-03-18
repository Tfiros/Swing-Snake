import java.util.EventObject;

public class GameEndsEvent
extends EventObject {

	private Snake snake;

	public GameEndsEvent(Object source, Snake snake) {
		super(source);
		this.snake = snake;
	}

	public Snake getSnake() {
		return snake;
	}
}
