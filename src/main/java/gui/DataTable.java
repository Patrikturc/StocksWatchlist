package gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * Window that uses JTable to display contents of a Database type file
 * 
 * @author Patrik Turcani
 * @author Inspiration:
 *         https://www.roseindia.net/java/example/java/swing/ScrollableJTable.shtml
 */
public class DataTable {

	/**
	 * Constructor that sets up the table design and receivs data to populate
	 * 
	 * @param data to populate the table with
	 * @param cols amount of columns in the table
	 */
	public DataTable(String[][] data, String[] cols) {

		// initialize panel
		JPanel panel = new JPanel();
		// Initialise table and set design
		JTable table = new JTable(data, cols);
		table.setBackground(Color.cyan);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Initialise header set design
		JTableHeader topRow = table.getTableHeader();
		topRow.setBackground(Color.GREEN);
		JScrollPane pane = new JScrollPane(table);
		// Initialise frame and set it up
		JFrame frame = new JFrame("Asset Historical Data For Past Year");
		frame.add(panel);
		frame.setSize(485, 480);
		frame.setBackground(Color.DARK_GRAY);
		frame.setVisible(true);
		// Setup panel
		panel.add(pane);
		panel.setPreferredSize(frame.getSize());

	}
}
