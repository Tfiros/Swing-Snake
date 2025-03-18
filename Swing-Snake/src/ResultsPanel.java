import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsPanel
extends JPanel
implements FoodListnener, GameEndsListnener {
	private List<Snake> gamesPlayed;
	private List<Point> listToPrint;
	private String nameOfCurrentPlayer;
	private Board board;
	private int score;
	private Border border;
	public ResultsPanel(Board board) {
		gamesPlayed = new ArrayList<>();
		listToPrint = new ArrayList<>();
		this.board = board;
		this.nameOfCurrentPlayer = JOptionPane.showInputDialog(this, "Input your name");
		if ( this.nameOfCurrentPlayer == null) {
			nameOfCurrentPlayer = "Anonymous";
		}
		board.getSnake().setName(nameOfCurrentPlayer);
		gamesPlayed.add(board.getSnake());
		listToPrint.add(new Point(0,20));
		this.border = BorderFactory.createLineBorder(Color.BLACK);
		this.setPreferredSize(new Dimension(300,300));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.board.foodListeners.add(this);
		this.board.gameEndsListneners.add(this);
	}
	boolean flag = false;

	public List<Snake> getGamesPlayed() {
		return gamesPlayed;
	}

	@Override
	public void gameRestart(GameEndsEvent evt) {
		board.foodListeners.add(this);
		if(flag) {
			gamesPlayed.add(evt.getSnake());
			y+=10;
		}
		flag=true;
		listToPrint.add(new Point(x,y));
		this.nameOfCurrentPlayer = JOptionPane.showInputDialog(this, "Input your name");
		if (this.nameOfCurrentPlayer == null) {
			this.nameOfCurrentPlayer = "Anonymous";
		}
		Collections.sort(gamesPlayed);
		Collections.reverse(gamesPlayed);
		board.getSnake().setName(nameOfCurrentPlayer);
		repaint();

	}

	@Override
	public void foodEaten(FoodEvent evt) {
		this.score = evt.getPoints();
		repaint();
	}
	int x = 0;
	int y =30 ;
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i =0 ; i < gamesPlayed.size();i++) {
			g.drawString(gamesPlayed.get(i).getName()+" score: "+gamesPlayed.get(i).getScore(),listToPrint.get(i).x,listToPrint.get(i).y);
		}
		if (flag) {
			g.drawString(board.getSnake().getName() + " score: " + board.getSnake().getScore(), x, y);
		}
	}
}
