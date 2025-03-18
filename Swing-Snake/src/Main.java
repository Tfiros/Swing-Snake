import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
extends JFrame {
	Board board;
	ResultsPanel resultsPanel;
	BoardVisualization boardVisualization;
	private int direction;
	private List<ChangePositionListener> positionListeners;
	public Main() {
		positionListeners= new ArrayList<>();
		board = new Board();
		resultsPanel = new ResultsPanel(board);
		boardVisualization = new BoardVisualization(board);
		positionListeners.add(board);
		this.getContentPane().add(resultsPanel, BorderLayout.SOUTH);
		this.add(boardVisualization);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
					direction = key;
				} else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
					direction = key;
				} else if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
					direction = key;
				} else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
					direction = key;
				}
				ChangePositionEvent evt = new ChangePositionEvent(this,direction);
				for (ChangePositionListener element : positionListeners) {
					element.changePosition(evt);
				}
			}
		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					FileOutputStream fileOutputStream = new FileOutputStream("snake.bin");
					for(int i = 0 ; i < resultsPanel.getGamesPlayed().size() ; i++) {
						if ( i == 10) break;
						fileOutputStream.write((byte)resultsPanel.getGamesPlayed().get(i).getName().length());
						fileOutputStream.write(toByteArray(resultsPanel.getGamesPlayed().get(i).getName()));
						fileOutputStream.write(toByteArray(resultsPanel.getGamesPlayed().get(i).getScore(),4));
					}
				}catch (IOException ex){
					ex.printStackTrace();
				}
			}
		});
		setSize(1200,800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static int bytesToInt(byte[] bytes) {
		if (bytes.length != 4) {
			throw new IllegalArgumentException("Tablica musi mieć długość 4");
		}

		int result = 0;
		for (int i = 0; i < 4; i++) {
			result |= (bytes[i] & 0xFF) << (24 - (8 * i));
		}

		return result;
	}

	public byte[] toByteArray(String name) {
			byte[] array = new byte[name.length()];
			char[] chars = name.toCharArray();
			for( int i = 0 ; i < chars.length ; i++) {
				array[i] = (byte)chars[i];
			}
			return array;
	}
	public static byte[] toByteArray(long value, int byteLength) {
		byte[] array = new byte[byteLength];
		int whichByte = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			array[i] = (byte) (value >>> whichByte * 8);
			whichByte++;
		}
		return array;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::new);
		String name;
		int score;
		File file = new File("snake.bin");
		if (file.exists()) {
			try {
				FileInputStream fin = new FileInputStream("snake.bin");
				while (fin.available() > 0) {
					int len = fin.read();
					byte[] nameBytes = new byte[len];
					fin.read(nameBytes);
					name = new String(nameBytes);
					byte[] scoreByteArray = new byte[4];
					fin.read(scoreByteArray);
					score = bytesToInt(scoreByteArray);
					System.out.println(name);
					System.out.println(score);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}