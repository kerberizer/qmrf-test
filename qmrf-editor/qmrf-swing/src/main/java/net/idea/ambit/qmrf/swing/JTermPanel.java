package net.idea.ambit.qmrf.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class JTermPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8537299245944387426L;

	/**
	 * 
	 */
	protected Font font = new Font("serif", Font.BOLD, 18);
	protected static String query;
	protected TermSearchAction searchAction;
	protected final JEditorPane ft = new JEditorPane();
	protected final JFormattedTextField term = new JFormattedTextField("");

	public JTermPanel(TermSearchAction searchAction) {
		super(new BorderLayout());
		this.searchAction = searchAction;
		addWidgets();
	}

	protected void hidePanel() {
		setVisible(false);
	}

	protected void addWidgets() {
		JLabel l = new JLabel("<html><b>Ontology</b></html>") {
			@Override
			public boolean isFocusable() {
				return false;
			}
		};

		l.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		l.setOpaque(true);
		l.setBackground(Color.DARK_GRAY);
		l.setForeground(Color.WHITE);
		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		l.setToolTipText("Ontology query");
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// hidePanel();
			}
		});
		add(l, BorderLayout.PAGE_START);

		ft.setContentType("text/html");
		ft.setEditable(false);
		ft.setEnabled(true);
		ft.addHyperlinkListener(new HTMLListener());
		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("body {color:#F5F2EB; font-family:verdana; margin: 4px; }");
		styleSheet.addRule("h1 {color: ##8C0305;}");
		styleSheet.addRule("h2 {color: ##8C0305;}");
		styleSheet.addRule("h3 {color: ##8C0305; font: 11px; font-weight:bold;}");
		styleSheet.addRule("p {font : 10px verdana; color : black; background-color : #FFFFFF; }");
		styleSheet.addRule("td {font : 10px verdana; color : black; background-color : #FFFFFF; }");

		ft.setEditorKit(kit);
		Document doc = kit.createDefaultDocument();
		ft.setDocument(doc);

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.setBackground(Color.white);
		bar.setForeground(Color.DARK_GRAY);

		term.setEnabled(true);
		bar.add(term);

		JButton searchButton = new JButton("Search");
		searchButton.setMnemonic(KeyEvent.VK_S);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				term.setText(query);
				doSearch(term.getText(), ft);
			}
		});
		bar.add(searchButton);

		JPanel p = new JPanel(new BorderLayout());
		p.setPreferredSize(new Dimension(16 * 32, 6 * 32));
		p.add(bar, BorderLayout.PAGE_START);
		p.add(new JScrollPane(ft, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		add(p, BorderLayout.CENTER);
		setPreferredSize(new Dimension(16 * 32, 6 * 32));

		term.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSearch(term.getText(), ft);
			}
		});
	}

	private class HTMLListener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				try {
					if (e.getDescription().startsWith("#")) {
						query = e.getDescription().replaceAll("#", "");
						term.setText(query);
						doSearch(query, ft);
					} else {
						String url = e.getURL() == null ? String.format("http://aber-owl.net/#!%s", e.getDescription())
								: e.getURL().toString();
						searchAction.copyTerm(url);
						try {
							if (Desktop.isDesktopSupported()) {
								Desktop.getDesktop().browse(new URI(url));
							} else
								openURL(url);
						} catch (Exception x) {
							x.printStackTrace();
						}
					}
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		}
	}

	protected void doSearch(String query, JEditorPane f) {
		try {
			setEnabled(false);
			term.setEnabled(false);
			ft.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			searchAction.actionPerformed(new ActionEvent(f, ActionEvent.ACTION_FIRST, query));
		} catch (Exception x) {

		} finally {
			term.setEnabled(true);
			ft.setEnabled(true);
			setEnabled(true);
			setCursor(Cursor.getDefaultCursor());
		}
	}

	public static void openURL(String url) throws Exception {
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else { // assume Unix or Linux
				String[] browsers = { "chrome", "firefox", "opera", "konqueror", "epiphany", "mozilla" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
						browser = browsers[count];
				if (browser == null)
					throw new Exception(browsers.toString());
				else
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
		} catch (Exception e) {
			throw new Exception(
					String.format("Error connecting to %s: %s:\n%s", url, e.getMessage(), e.getLocalizedMessage()));
		}

	}
}
