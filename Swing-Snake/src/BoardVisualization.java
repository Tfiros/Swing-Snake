import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class BoardVisualization
		extends JTable
		implements BoardChange{
	private Board board;
	private SnakeTable snakeTable;
	public BoardVisualization(Board board) {
		this.board = board;
		this.board.boardObservers.add(this);
		Object[][] data = new Object[16][25];
		String[] columnNames = new String[25];

		snakeTable = new SnakeTable(data, columnNames);
		this.setModel(snakeTable);
		this.setDefaultRenderer(Object.class, new CellRenderer());
		this.setRowHeight(25);

		this.setEnabled(false);
		setUpCol();
	}

	@Override
	public void updateBoardObserver() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 25; j++) {
				snakeTable.setValueAt(board.getBoard()[i][j], i, j);
			}
		}
	}

	void setUpCol() {
		TableColumnModel tcm = this.getColumnModel();
		TableColumn column;
		for ( int i = 0 ; i < tcm.getColumnCount() ; i++) {
			column=tcm.getColumn(i);
			column.setPreferredWidth(25);
		}
		this.updateUI();
	}



	private class SnakeTable extends DefaultTableModel {
		public SnakeTable(Object[][] data, Object[] columnNames) {
			super(data, columnNames);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

	}
	private class CellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value != null) {
				int cellValue = (int) value;
				if (cellValue > 0) {
					if (board.getSnake().getSnakeBody().get(0).x == column && board.getSnake().getSnakeBody().get(0).y == row) {
						c.setBackground(Color.BLUE);
					} else {
						c.setBackground(Color.GREEN);
					}
				} else if (cellValue == -1) {
					c.setBackground(Color.RED);
				} else {
					c.setBackground(Color.WHITE);
				}
			} else {
				c.setBackground(Color.WHITE);
			}

			return c;
		}
	}
}
