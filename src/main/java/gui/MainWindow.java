package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import program.Database;
import program.ReadExcelFile;
import program.Symbol;

/**
 * ___DESCRIPTION___ Stock Market Multi-tool application allows the user to
 * store data about various types of assets User can practically add any asset
 * that is included within the Yahoo Finance database It is possible to view a
 * detailed candlestick chart and Data table containing Candlestick data for the
 * past year. Variety of sorts are implemented for both symbols and doubles User
 * can use search field to iterate through the data structures and find
 * information about the sought for asset
 * 
 * ___ALGORITHMS AND DATA STRUCTURES USED___ Data structures used within the
 * application made by Patrik Turcani - Generic Singly Linked List, Double Array
 * Collection
 * 
 * Algorithms used within the application made by the author - Bubble Sort,
 * Selection Sort and iterative Binary Search
 * 
 * Algorithms implemented but not used - Quick Sort and recursive Binary Search
 * - Appeared to be much slower than bubble sort in every case the program could
 * use it Quick sort within the program could be upgraded when the application
 * becomes of larger scale with much larger amounts of data required to be
 * sorted
 * 
 * Data structures retrieved from Java libraries - List and array that assist in
 * generating a candlestick chart 2D Array used to produce a table showing
 * candlestick data. And array of symbols used to assist my array with sorting.
 * 
 * ___INSPIRATIONS FROM OTHER AUTHORS___ Certain parts in the program are
 * inspired by various Authors' works, all Links to other Authors work are
 * provided in the class that uses the specific code and referenced within
 * program documentation. Majority of code from inspirations is refined to
 * provide better user experience.
 * 
 * @author Patrik Turcani
 * @version 3.3
 */
public class MainWindow extends JPanel implements ListSelectionListener {

	// Private attributes
	private static JFrame frame;
	private TableModel assetData;
	private static DefaultListModel assetContent;
	private JLabel lblAssets;
	private JLabel lblSearchSymbol;
	private JLabel lblShowDetails;
	private JLabel lblExpandChart;
	private JLabel lblAddAsset;
	private JLabel lblRemoveAsset;
	private JLabel lblEditInformation;
	private JLabel lblUpdatePrices;
	private JList<String> listAssets;
	private JTextField txtSymToSearch;
	private long startTime = System.currentTimeMillis();

	// Public attributes
	public DefaultListModel getAssetContent() {
		return assetContent;
	}

	public JList<String> getListAssets() {
		return listAssets;
	}

	public static JFrame GetFrame() {
		return frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		assetContent = new DefaultListModel<String>();
		//
		frame = new JFrame("Stock Market Multi-Tool");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 1200, 500);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		// Populate Database - Only 6 symbols due to spam prevention on Yahoo Servers
		Symbol s = new Symbol("aapl", "Technology", "Blue chip stock", true);
		Symbol s1 = new Symbol("btc-usd", "Crypto", "Popular digital currency", true);
		Symbol s2 = new Symbol("vz", "Technology", "Dividend stock", true);
		Symbol s3 = new Symbol("bnb-usd", "Crypto", "Utility digital currency", true);
		Symbol s4 = new Symbol("spy", "Index ", "Major US Index", true);
		Symbol s5 = new Symbol("^vix", "Index ", "Volatility index", true);

		// ______GUI BUTTONS______//

		// ______Button to expand chart______//
		JButton btnExpandChart = new JButton("Expand Chart");
		btnExpandChart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblExpandChart.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblExpandChart.setVisible(false);
			}
		});
		btnExpandChart.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnExpandChart.setForeground(new Color(0, 0, 205));
		btnExpandChart.setBackground(new Color(152, 251, 152));
		btnExpandChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GetChart();
			}
		});
		btnExpandChart.setBounds(10, 125, 200, 20);
		frame.getContentPane().add(btnExpandChart);
		assetContent = new DefaultListModel<String>();

		// ______Button to Add Asset//
		JButton btnAddAsset = new JButton("Add Asset");
		btnAddAsset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblAddAsset.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblAddAsset.setVisible(false);
			}
		});
		btnAddAsset.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnAddAsset.setForeground(new Color(0, 0, 205));
		btnAddAsset.setBackground(new Color(152, 251, 152));
		btnAddAsset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddAsset aA = new AddAsset();
				frame.setEnabled(false);

			}
		});
		btnAddAsset.setBounds(10, 165, 200, 20);
		frame.getContentPane().add(btnAddAsset);

		// ______Button to Refresh______//
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnRefresh.setForeground(new Color(0, 0, 205));
		btnRefresh.setBackground(new Color(152, 251, 152));
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Refresh();
			}
		});
		btnRefresh.setBounds(990, 0, 194, 25);
		frame.getContentPane().add(btnRefresh);

		// ______Button to Show Details______//
		JButton btnShowDetails = new JButton("Show Details");
		btnShowDetails.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblShowDetails.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblShowDetails.setVisible(false);
			}
		});
		btnShowDetails.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnShowDetails.setForeground(new Color(0, 0, 205));
		btnShowDetails.setBackground(new Color(152, 251, 152));
		btnShowDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShowDetails();
			}
		});
		btnShowDetails.setBounds(10, 85, 200, 20);
		frame.getContentPane().add(btnShowDetails);

		// ______Button to Remove Asset______//
		JButton btnRemoveAsset = new JButton("Remove Asset");
		btnRemoveAsset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblRemoveAsset.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblRemoveAsset.setVisible(false);
			}
		});
		btnRemoveAsset.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnRemoveAsset.setForeground(new Color(0, 0, 205));
		btnRemoveAsset.setBackground(new Color(152, 251, 152));
		btnRemoveAsset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RemoveAsset();
				Refresh();
				listAssets.setSelectedIndex(0);
			}
		});
		btnRemoveAsset.setBounds(10, 205, 200, 20);
		frame.getContentPane().add(btnRemoveAsset);

		// ______Button to Sort High to Low______//
		JButton btnSortHighToLow = new JButton("Sort By Gain");
		btnSortHighToLow.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSortHighToLow.setForeground(new Color(0, 0, 205));
		btnSortHighToLow.setBackground(new Color(152, 251, 152));
		btnSortHighToLow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SortHighToLow();
				Refresh();
			}
		});
		btnSortHighToLow.setBounds(173, 395, 200, 20);
		frame.getContentPane().add(btnSortHighToLow);

		// ______Button to Sort Alphabetically______//
		JButton btnSortAlphabetically = new JButton("Sort Alphabetically");
		btnSortAlphabetically.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SortAlphabetically();
				Refresh();
			}
		});
		btnSortAlphabetically.setForeground(new Color(0, 0, 205));
		btnSortAlphabetically.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSortAlphabetically.setBackground(new Color(152, 251, 152));
		btnSortAlphabetically.setBounds(173, 360, 200, 20);
		frame.getContentPane().add(btnSortAlphabetically);

		// ______Button to Update Price______//
		int Delay = 300000;
		JButton btnUpdatePrices = new JButton("Update Prices");
		btnUpdatePrices.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblUpdatePrices.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblUpdatePrices.setVisible(false);
			}
		});
		btnUpdatePrices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long ClickTime = System.currentTimeMillis();
				if (ClickTime - startTime < Delay) {
					JOptionPane.showMessageDialog(frame,
							"Prices can only be updated every 5 minutes to prevent spamming Yahoo servers"
									+ ", please wait another " + (300 - ((ClickTime - startTime) / 1000))
									+ " Seconds before updating again");
					return;
				} else {
					startTime = ClickTime;
					UpdatePrices();
				}
			}
		});
		btnUpdatePrices.setForeground(new Color(0, 0, 205));
		btnUpdatePrices.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnUpdatePrices.setBackground(new Color(152, 251, 152));
		btnUpdatePrices.setBounds(10, 310, 200, 20);
		frame.getContentPane().add(btnUpdatePrices);

		// ______Button to sort Low to High______//
		JButton btnSortLowToHigh = new JButton("Sort By Loss");
		btnSortLowToHigh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SortLowToHigh();
				Refresh();
			}
		});
		btnSortLowToHigh.setForeground(new Color(0, 0, 205));
		btnSortLowToHigh.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSortLowToHigh.setBackground(new Color(152, 251, 152));
		btnSortLowToHigh.setBounds(173, 430, 200, 20);
		frame.getContentPane().add(btnSortLowToHigh);

		// ______Button to Search for Asset______//
		JButton btnSearchSymbol = new JButton("Search Symbol");
		btnSearchSymbol.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSearchSymbol.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblSearchSymbol.setVisible(false);
			}
		});
		btnSearchSymbol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchSymbol();
			}
		});
		btnSearchSymbol.setForeground(new Color(0, 0, 205));
		btnSearchSymbol.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSearchSymbol.setBackground(new Color(152, 251, 152));
		btnSearchSymbol.setBounds(10, 45, 153, 23);
		frame.getContentPane().add(btnSearchSymbol);

		// ______Button to Edit Information______//
		JButton btnEditInformation = new JButton("View/Edit Symbol Information");
		btnEditInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblEditInformation.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblEditInformation.setVisible(false);
			}
		});
		btnEditInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddDescription();
			}
		});
		btnEditInformation.setForeground(new Color(0, 0, 205));
		btnEditInformation.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnEditInformation.setBackground(new Color(152, 251, 152));
		btnEditInformation.setBounds(10, 245, 200, 20);
		frame.getContentPane().add(btnEditInformation);

		// ______GUI LABELS______//
		JLabel lblType = new JLabel("Type");
		lblType.setForeground(Color.WHITE);
		lblType.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblType.setBounds(784, 11, 123, 14);
		frame.getContentPane().add(lblType);

		JLabel lblComment = new JLabel("Comment");
		lblComment.setForeground(Color.WHITE);
		lblComment.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblComment.setBounds(884, 11, 123, 14);
		frame.getContentPane().add(lblComment);

		JLabel lblTodayChange = new JLabel("Last Recorded Change");
		lblTodayChange.setForeground(Color.WHITE);
		lblTodayChange.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblTodayChange.setBounds(634, 11, 123, 14);
		frame.getContentPane().add(lblTodayChange);

		JLabel lblTodayClose = new JLabel("Today Close");
		lblTodayClose.setForeground(Color.WHITE);
		lblTodayClose.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblTodayClose.setBounds(487, 11, 123, 14);
		frame.getContentPane().add(lblTodayClose);

		txtSymToSearch = new JTextField();
		txtSymToSearch.setBounds(171, 45, 190, 24);
		frame.getContentPane().add(txtSymToSearch);
		txtSymToSearch.setColumns(10);
		txtSymToSearch.setBackground(Color.LIGHT_GRAY);

		lblSearchSymbol = new JLabel(
				"<html>Search for a symbol that is within the database,<br/> e.g - aapl, $spy, $SPY</html>",
				SwingConstants.CENTER);
		lblSearchSymbol.setForeground(Color.WHITE);
		lblSearchSymbol.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblSearchSymbol.setBounds(10, 0, 257, 40);
		lblSearchSymbol.setVisible(false);
		frame.getContentPane().add(lblSearchSymbol);

		lblShowDetails = new JLabel("Display Candlestick data for the past year of the selected Asset");
		lblShowDetails.setForeground(Color.WHITE);
		lblShowDetails.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblShowDetails.setBounds(10, 70, 400, 15);
		lblShowDetails.setVisible(false);
		frame.getContentPane().add(lblShowDetails);

		lblExpandChart = new JLabel("Show Candlestick chart for the past year of the selected Asset");
		lblExpandChart.setForeground(Color.WHITE);
		lblExpandChart.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblExpandChart.setBounds(10, 110, 400, 15);
		lblExpandChart.setVisible(false);
		frame.getContentPane().add(lblExpandChart);

		lblAddAsset = new JLabel("Add a new symbol to the Database");
		lblAddAsset.setForeground(Color.WHITE);
		lblAddAsset.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblAddAsset.setBounds(10, 150, 250, 15);
		lblAddAsset.setVisible(false);
		frame.getContentPane().add(lblAddAsset);

		lblRemoveAsset = new JLabel("Remove Symbol From the Database");
		lblRemoveAsset.setForeground(Color.WHITE);
		lblRemoveAsset.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblRemoveAsset.setBounds(10, 190, 250, 15);
		lblRemoveAsset.setVisible(false);
		frame.getContentPane().add(lblRemoveAsset);

		lblEditInformation = new JLabel("Select Asset to Edit or View Description");
		lblEditInformation.setForeground(Color.WHITE);
		lblEditInformation.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblEditInformation.setBounds(10, 230, 250, 15);
		lblEditInformation.setVisible(false);
		frame.getContentPane().add(lblEditInformation);

		// ______List GUI______//
		listAssets = new JList<String>(assetContent);
		listAssets.setFont(new Font("Times New Roman", Font.BOLD, 13));
		listAssets.setBounds(373, 25, frame.getWidth() - 400, 425);
		listAssets.setBackground(Color.LIGHT_GRAY);

		lblAssets = new JLabel("Assets");
		lblAssets.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblAssets.setForeground(new Color(255, 255, 255));
		lblAssets.setBounds(375, 10, 120, 15);
		frame.getContentPane().add(lblAssets);

		lblUpdatePrices = new JLabel("<html>Update Prices - Cryptos can be updated every 60s,"
				+ "<br/> other assets at Midnight every day</html>");
		lblUpdatePrices.setForeground(Color.WHITE);
		lblUpdatePrices.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblUpdatePrices.setBounds(10, 270, 363, 40);
		lblUpdatePrices.setVisible(false);
		frame.getContentPane().add(lblUpdatePrices);

		// Utility
		JScrollPane spList = new JScrollPane(listAssets);
		spList.setBounds(373, 25, frame.getWidth() - 400, 425);
		spList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(spList);
		listAssets.setSelectedIndex(0);
		Refresh();

	}

	/**
	 * Searches for selected symbol
	 */
	private void SearchSymbol() {
		Symbol s = new Symbol();

		try {
			String toFind = txtSymToSearch.getText();
			if (toFind.substring(0, 1).equals("$"))
				toFind = toFind.substring(1, toFind.length());
			toFind = toFind.toLowerCase();
			final long current = System.currentTimeMillis();
			Symbol found = s.SearchSymbol(toFind);
			final long durationInMilliseconds = System.currentTimeMillis() - current;
			System.out.println("Binary search took " + durationInMilliseconds + "ms.");

			for (Symbol sym : Database.GetMySymbols()) {
				if (sym.GetName().equals(found.GetName())) {
					break;
				}
			}
			// Create frame for details
			JFrame frm = new JFrame("Information about : $" + found.GetName());
			frm.setBounds(100, 100, 1000, 100);
			JLabel lbl = new JLabel();
			lbl.setBounds(100, 100, 1500, 100);
			lbl.setText(found.Output().toString() + found.GetDescription());
			frm.getContentPane().add(lbl);
			frm.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Sorry, could not find: " + txtSymToSearch.getText()
					+ " in the database\n" + "Only assets already in the database can be searched for.");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Opens description window
	 */
	private void AddDescription() {
		String n = listAssets.getSelectedValue();
		for (Symbol sym : Database.GetMySymbols()) {
			if (sym.Output().toString().equals(n)) {
				AssetDescription aD = new AssetDescription(sym, sym.GetDescription());
				aD.setVisible(true);
				frame.setEnabled(false);
			}
		}
	}

	/**
	 * Updates prices
	 */
	private void UpdatePrices() {
		try {
			String url = "";
			for (Symbol sym : Database.GetMySymbols()) {
				url = sym.CreateURL(sym.GetName());
				sym.SetChartURL(url);
				sym.CalculateLastRecordedChange();
			}
			Refresh();
			JOptionPane.showMessageDialog(frame,
					"Prices successfully updated. Please wait 300 seconds before updating again");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Opens chart for the selected symbol
	 */
	private void GetChart() {
		String n = listAssets.getSelectedValue();
		for (Symbol sym : Database.GetMySymbols()) {
			if (sym.Output().toString().equals(n)) {
				sym.GetChart();
				ReadExcelFile r = new ReadExcelFile();
			}
		}
	}

	/**
	 * Opens the contents of a database type file in a new window using JTable
	 */
	private void ShowDetails() {
		String n = listAssets.getSelectedValue();
		for (Symbol sym : Database.GetMySymbols()) {
			if (sym.Output().toString().equals(n)) {
				// CandlestickChart lc = new CandlestickChart(sym.GetName(), sym.chartURL());
				ReadExcelFile r = new ReadExcelFile();
				String[] s = { "Date", "Open", "High", "Low", "Close", "AdjClose", "Volume" };
				DataTable lc = new DataTable(r.Read(sym.GetChartURL()), s);
			}
		}
	}

	/**
	 * Removes Selected Asset
	 */
	private void RemoveAsset() {
		String n = listAssets.getSelectedValue();
		// my list
		for (Symbol sym : Database.GetMySymbols()) {
			if (sym.Output().toString().equals(n)) {
				// CandlestickChart lc = new CandlestickChart(sym.GetName(), sym.chartURL());
				assetContent.removeElement(sym);
				sym.RemoveSymbol(sym);
				JOptionPane.showMessageDialog(frame,
						"Symbol: " + sym.GetName() + " Was Successfuly removed from the database");
				Refresh();
				break;
			}
		}
	}

	/**
	 * Sorts Assets Alphabetically
	 */
	private void SortAlphabetically() {
		Symbol s = new Symbol();
		s.SortAlphabetically();
		Refresh();
	}

	/**
	 * Sorts Assets from Highest gains to Largest loss based on the most recent
	 * price data
	 */
	private void SortHighToLow() {
		assetContent.clear();
		Symbol.SortByPrice(false);
		Refresh();
	}

	/**
	 * Sorts Assets from Largest loss to Highest gain based on the most recent price
	 * data
	 */
	private void SortLowToHigh() {
		assetContent.clear();
		Symbol.SortByPrice(true);
		Refresh();
	}

	/**
	 * UI Refresh
	 */
	public static void Refresh() {
		assetContent.clear();
		for (Symbol sym : Database.GetMySymbols()) {

			boolean match = false;
			for (Object s : assetContent.toArray()) {
				if (s.equals((sym).GetName())) {
					System.out.println("Found match");
					match = true;
				}
			}
			if (!match) {
				assetContent.addElement((sym).Output().toString());
			}
		}
	}

	/**
	 * Interface method
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
	}
}
