package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import program.Symbol;

/**
 * Window that allows user to add information about assets
 * 
 * @author Patrik Turcani
 *
 */
public class AssetDescription extends JFrame {

	// private variables
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AssetDescription(Symbol sym, String description) {
		setTitle("Asset Description");
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.DARK_GRAY);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainWindow.GetFrame().setEnabled(true);
			}
		});

		JTextArea txtDescription = new JTextArea();
		txtDescription.append(description);
		txtDescription.setLineWrap(true);
		txtDescription.setBounds(0, 0, 434, 261);
		txtDescription.setBackground(Color.LIGHT_GRAY);
		JScrollPane spDesc = new JScrollPane(txtDescription);
		spDesc.setBounds(0, 0, 434, 261);
		spDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(spDesc);

		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sym.SetDescription(txtDescription.getText());
			}
		});
		btnSaveChanges.setBounds(444, 0, 132, 23);
		btnSaveChanges.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSaveChanges.setForeground(new Color(0, 0, 205));
		btnSaveChanges.setBackground(new Color(152, 251, 152));
		contentPane.add(btnSaveChanges);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow.GetFrame().setEnabled(true);
				dispose();
			}
		});
		btnClose.setBounds(444, 238, 132, 23);
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnClose.setForeground(new Color(0, 0, 205));
		btnClose.setBackground(new Color(152, 251, 152));
		contentPane.add(btnClose);
	}

}
