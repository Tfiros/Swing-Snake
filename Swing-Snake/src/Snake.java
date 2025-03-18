import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake implements FoodListnener, Comparable<Snake> {
	private int score;
	private String name;
	private Board board;
	private List<Point> snakeBody;

	public Snake(Board board) {
		this.board=board;
		snakeBody = new ArrayList<>();
		snakeBody.add(new Point(25/2,16/2));
	}

	public List<Point> getSnakeBody() {
		return snakeBody;
	}

	public int getScore() {
		return score;
	}

	@Override
	public int compareTo(Snake otherSnake) {
		return Integer.compare(this.score, otherSnake.getScore());
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void foodEaten(FoodEvent evt) {
		score++;
		snakeBody.add(0, new Point(evt.getP().x, evt.getP().y));
		board.getBoard()[evt.getP().y][evt.getP().x] = 1;

	}
}
