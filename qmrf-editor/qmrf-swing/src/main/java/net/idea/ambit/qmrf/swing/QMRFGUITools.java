/*
Copyright (C) 2005-2012  

Contact: www.ideaconsult.net

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

package net.idea.ambit.qmrf.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import net.idea.ambit.qmrf.QMRFData;
import net.idea.ambit.swing.common.AmbitStatusBar;
import net.idea.ambit.swing.common.UITools;

public class QMRFGUITools {

	protected QMRFGUITools() {

	}

	public static JMenuBar createMenuBar(JComponent toolBar, QMRFData qmrfData, Container mainFrame) {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(createMenuItem(toolBar, new QMRFNewAction(qmrfData, mainFrame)));
		fileMenu.add(createMenuItem(toolBar, new QMRFFileOpenAction(qmrfData, mainFrame)));
		fileMenu.add(createMenuItem(toolBar, new QMRFURIOpenAction(qmrfData, mainFrame)));
		fileMenu.add(createMenuItem(toolBar, new QMRFFileSaveAction(qmrfData, mainFrame)));
		fileMenu.add(createMenuItem(toolBar, new QMRFFileSaveAsAction(qmrfData, mainFrame)));
		fileMenu.add(createMenuItem(toolBar, new QMRFBatchAction(qmrfData, mainFrame)));
		menuBar.add(fileMenu);
		menuBar.add(UITools.createEditMenu(mainFrame));
		menuBar.add(UITools.createStyleMenu());
		menuBar.add(UITools.createOntologyMenu(mainFrame,
				new TermPanelAction("Ontology (show/hide)", qmrfData,false),
				new TermPanelAction("Search ontology (selected text)", qmrfData,true),
				new TermInsertAction("Insert ontology term URI", qmrfData)));
		return menuBar;
	}

	protected static JMenuItem createMenuItem(JComponent toolBar, Action action) {
		if (toolBar != null)
			toolBar.add(new JButton(action));
		return new JMenuItem(action);
	}

	public static JPanel createStatusBar(QMRFData qmrfData, int width, int height) {
		AmbitStatusBar statusBar = new AmbitStatusBar(new Dimension(width, height));
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		qmrfData.getJobStatus().addObserver((AmbitStatusBar) statusBar);
		if (qmrfData.getBatchStatistics() instanceof Observable)
			((Observable) qmrfData.getBatchStatistics()).addObserver((AmbitStatusBar) statusBar);

		return statusBar;

	}

}
