package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import program.Database;
import program.Symbol;

/**
 * AddAsset Window allows user to interact with the program in regard to
 * populating the database with users favourite symbols
 * 
 * @author Patrik Turcani
 *
 */
public class AddAsset {

	// private variables
	private JFrame frame;
	private JTextField txtSymbol;
	private JComboBox cmbType;
	private JTextField txtComment;
	private JTextArea txtDescription = new JTextArea();

	/**
	 * Create the application.
	 */
	public AddAsset() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		String[] types = { "Technology", "Crypto", "Commodities", "Healthcare", "Energy", "Index ", "Entertainment",
				"Automotive", "Aviation", "Utility", "Transport", "Shipping", "Retail", "E-Commerce", "Gambling",
				"E-Sports", "Other " };

		frame = new JFrame("Add Asset");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainWindow.GetFrame().setEnabled(true);
			}
		});
		frame.setBounds(100, 100, 561, 273);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.getContentPane().setBackground(Color.DARK_GRAY);

		// ______GUI TEXT FIELDS______//

		// Text field where user inputs the symbol they wish to add to the database
		txtSymbol = new JTextField();
		txtSymbol.setBounds(10, 45, 95, 20);
		txtSymbol.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(txtSymbol);
		txtSymbol.setColumns(10);

		// Text field where user inputs the description to the symbol currently being
		// added
		txtDescription.setBounds(320, 45, 220, 180);
		txtDescription.setLineWrap(true);
		JScrollPane spDesc = new JScrollPane(txtDescription);
		spDesc.setBounds(320, 45, 220, 180);
		spDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		txtDescription.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(spDesc);

		// Text field where user inputs the comment for the symbol currently being added
		txtComment = new JTextField();
		txtComment.setColumns(10);
		txtComment.setBounds(210, 45, 95, 20);
		txtComment.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(txtComment);

		// Combo box that contains array of Asset types
		cmbType = new JComboBox(types);
		cmbType.setBounds(110, 45, 95, 20);
		cmbType.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		cmbType.setForeground(new Color(0, 0, 205));
		cmbType.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(cmbType);

		// ______GUI BUTTONS______//

		// Button that provides detailed explanation on how to find a symbol of asset
		JButton btnHowToFindSymbol = new JButton("How to find Symbol?");
		btnHowToFindSymbol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
						"Insert a name of a publicly traded company or asset such as Apple or Bitcoin into Yahoo Finance search.\n"
								+ "When entering information in Yahoo Finance search bar,\n"
								+ "the results for a variety of symbols for that asset are suggested.\n"
								+ "For example when searching for Apple, AAPL is shown, when searching for Bitcoin, BTC-USD is shown.\n"
								+ "Enter a copy of this symbol to add the asset to the database.\n"
								+ "Use of capital letters is not necessary.");
			}
		});
		btnHowToFindSymbol.setBounds(10, 200, 150, 23);
		btnHowToFindSymbol.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnHowToFindSymbol.setForeground(new Color(0, 0, 205));
		btnHowToFindSymbol.setBackground(new Color(152, 251, 152));
		frame.getContentPane().add(btnHowToFindSymbol);

		// Button that attempts to add the asset to the database
		JButton btnAddAsset = new JButton("Add asset");
		btnAddAsset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Add(txtSymbol.getText());
			}
		});
		btnAddAsset.setBounds(10, 165, 150, 23);
		btnAddAsset.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnAddAsset.setForeground(new Color(0, 0, 205));
		btnAddAsset.setBackground(new Color(152, 251, 152));
		frame.getContentPane().add(btnAddAsset);

		// ______GUI LABELS______//
		JLabel lblNewLabel = new JLabel(
				"Please fill in ticker symbol. Feel free to add other non-mandatory information");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(10, 0, 542, 34);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Symbol(*)");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(10, 30, 86, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Industry/Type");
		lblNewLabel_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1.setBounds(110, 30, 86, 14);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_2 = new JLabel("Additional information");
		lblNewLabel_1_1_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1_2.setBounds(320, 30, 147, 14);
		frame.getContentPane().add(lblNewLabel_1_1_2);

		JLabel lblNewLabel_1_1_1 = new JLabel("Comment");
		lblNewLabel_1_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1_1.setBounds(210, 30, 86, 14);
		frame.getContentPane().add(lblNewLabel_1_1_1);
	}

	/**
	 * Method that adds symbols to the database
	 * 
	 * @param name Insert name of the symbol to add to the Database
	 */
	public void Add(String name) {

		boolean exists = false;
		if (Database.GetMySymbols().Count() == 0) {
			Symbol s = new Symbol(txtSymbol.getText(), cmbType.getSelectedItem().toString(), txtComment.getText(),
					false);
			s.SetDescription(txtDescription.getText());
		} else {
			for (Symbol sym : Database.GetMySymbols()) {
				if (sym.GetName().equalsIgnoreCase(txtSymbol.getText())) {
					JOptionPane.showMessageDialog(frame, "Sorry, this symbol is already in the database");
					exists = true;

				}
			}
			if (!exists) {
				Symbol s = new Symbol(txtSymbol.getText(), cmbType.getSelectedItem().toString(), txtComment.getText(),
						false);
				// Symbol.Add(txtSymbol.getText(), cmbType.getSelectedItem().toString(),
				// txtComment.getText());
				s.SetDescription(txtDescription.getText());
			}
		}
	}
}
