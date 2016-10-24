package net.idea.ambit.swing.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

public class JTermPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8537299245944387426L;
	/**
	 * 
	 */
	protected Font font = new Font("serif", Font.BOLD, 18);
	protected static String term;

	public static String getTerm() {
		return term;
	}

	public static void setTerm(String aterm) {
		term = aterm;
	}

	public JTermPanel() {
		super(new BorderLayout());
		addWidgets();
	}

	protected void addWidgets() {
		final JTextPane ft = new JTextPane();
		ft.setEnabled(true);

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		final JFormattedTextField f = new JFormattedTextField("");
		f.setEnabled(true);
		bar.add(f);

		JButton searchButton = new JButton("Search ontology");
		searchButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
			 	ft.setText("blabla");
			 }
			});
		bar.add(searchButton);
		add(bar, BorderLayout.NORTH);
		
		// model = new UnicodeTable((char) (0x20)); //(char)(0 * 0x100));
		add(new JScrollPane(ft), BorderLayout.CENTER);
		setPreferredSize(new Dimension(16 * 32, 6 * 32));
	}


}