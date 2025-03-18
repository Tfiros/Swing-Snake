import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Board
		extends Thread
		implements ChangePositionListener {
	List<FoodListnener> foodListeners;
	List<GameEndsListnener> gameEndsListneners;
	private int score;
	private int direction;
	private int[][] board;
	private int sleepTime;
	private Point food;
	private Snake snake;
	boolean active;
	private List<Snake> gamesPlayed;
	List<BoardChange> boardObservers;

	public Board() {
		gamesPlayed = new ArrayList<>();
		gameEndsListneners = new ArrayList<>();
		boardObservers = new ArrayList<>();
		foodListeners = new ArrayList<>();
		this.board = new int[16][25];
		this.snake = new Snake(this);
		sleepTime=300;
		generateFood();
		direction = KeyEvent.VK_RIGHT;
		foodListeners.add(snake);
		active = true;
		this.start();
	}

	@Override
	public void changePosition(ChangePositionEvent evt) {
		this.direction=evt.getDirection();
	}

	private void generateFood() {
		Random random = new Random();
		int x, y;
		do {
			x = random.nextInt(25);
			y = random.nextInt(16);
		} while (board[y][x] != 0);

		food = new Point(x, y);
		board[y][x] = -1;
	}

	private void moveSnake() {
		Point head = snake.getSnakeBody().get(0);
		int newSnakeX = head.x;
		int newSnakeY = head.y;

		switch (direction) {
			case KeyEvent.VK_UP -> newSnakeY--;
			case KeyEvent.VK_DOWN -> newSnakeY++;
			case KeyEvent.VK_LEFT -> newSnakeX--;
			case KeyEvent.VK_RIGHT -> newSnakeX++;
		}

		if (!isValidMove(newSnakeX, newSnakeY)) {
			gameOver();
			return;
		}
		if (newSnakeX == food.x && newSnakeY == food.y) {
			// Snake ate the food
			score++;
			sleepTime-=10;
			FoodEvent f = new FoodEvent(this, new Point(newSnakeX, newSnakeY), this.score);
			for (FoodListnener ls : foodListeners)
				ls.foodEaten(f);
			generateFood();
		} else {
			// Move the snake
			snake.getSnakeBody().add(0, new Point(newSnakeX, newSnakeY));
			board[newSnakeY][newSnakeX] = 1;
			Point tail = snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
			board[tail.y][tail.x] = 0;
		}
		for (BoardChange e : boardObservers) {
			e.updateBoardObserver();
		}
	}

	private boolean isValidMove(int x, int y) {
		if (x < 0 || x >= 25 || y < 0 || y >= 16) {
			// Hit the wall
			return false;
		}

		// Check if snake hits itself
		for (Point point : snake.getSnakeBody()) {
			if (point.x == x && point.y == y) {
				return false;
			}
		}

		return true;
	}

	private void gameOver() {
		active = false;
		resetGame();
	}

	private void resetGame() {
		sleepTime=300;
		GameEndsEvent event = new GameEndsEvent(this, snake);
		gamesPlayed.add(snake);
		this.board = new int[16][25];
		this.snake = new Snake(this);
		foodListeners.clear();
		foodListeners.add(snake);
		this.direction = KeyEvent.VK_RIGHT;
		generateFood();
		for (BoardChange e : boardObservers) {
			e.updateBoardObserver();
		}

		for (GameEndsListnener l : gameEndsListneners) {
			l.gameRestart(event);
		}
		active=true;
	}

	@Override
	public void run() {
		while (active) {
			moveSnake();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public int[][] getBoard() {
		return board;
	}

	public List<Snake> getGamesPlayed() {
		return gamesPlayed;
	}

	public Snake getSnake() {
		return snake;
	}

}
