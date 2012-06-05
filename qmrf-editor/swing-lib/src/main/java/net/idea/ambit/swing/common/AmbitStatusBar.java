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

package net.idea.ambit.swing.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;

import net.idea.ambit.swing.interfaces.JobStatus;
import ambit2.base.interfaces.IBatchStatistics;

/**
 * Status bar. See example for {@link ambit.database.data.AmbitDatabaseToolsData}.
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Aug 31, 2006
 */
public class AmbitStatusBar extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6138277810700697834L;
	protected JLabel label;
	protected JLabel batchlabel;
    protected JProgressBar progressBar= null;
    protected LabelErrorViewer errorViewer;
	protected Exception theError;    
    
	public AmbitStatusBar(Dimension d) {
		super();
		addWidgets(d);
	}

	public AmbitStatusBar(Dimension d,boolean arg0) {
		super(arg0);
		addWidgets(d);
	}

	public AmbitStatusBar(Dimension d,LayoutManager arg0) {
		super(arg0);
		addWidgets(d);
	}

	public AmbitStatusBar(Dimension d,LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		addWidgets(d);
	}
	protected void addWidgets(Dimension d) {
		errorViewer = new LabelErrorViewer();
		theError = null;
		setVisible(true);
		setEnabled(true);
        setLayout(new BorderLayout());
		label = new JLabel();
		label.setText("");
		label.setMinimumSize(new Dimension(100,d.height));
		label.setPreferredSize(new Dimension(200,d.height));
		add(label,BorderLayout.WEST);
		
		batchlabel = new JLabel();
		batchlabel.setText("");
		batchlabel.setMinimumSize(new Dimension(100,d.height));
		batchlabel.setPreferredSize(new Dimension(200,d.height));
		batchlabel.setForeground(Color.blue);
		batchlabel.setOpaque(true);
		add(batchlabel,BorderLayout.CENTER);
		//batchlabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		batchlabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

	    progressBar = new JProgressBar();
	    progressBar.setPreferredSize(new Dimension(100,d.height));
	    progressBar.setMaximum(100);
	    progressBar.setMinimum(0);
	    progressBar.setStringPainted(true);
	    progressBar.setString("");
	    progressBar.setIndeterminate(false);

	    progressBar.setVisible(true);
	    progressBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	    

        JPanel p = new JPanel(new BorderLayout());
        p.add(progressBar,BorderLayout.CENTER);
        p.add(errorViewer.getView(),BorderLayout.EAST);
	    add(p,BorderLayout.EAST);        
		setPreferredSize(d);
		
	}
	public void update(Observable arg0, Object arg1) {

		if (arg0 instanceof JobStatus) {
			JobStatus job = (JobStatus) arg0;
			errorViewer.setError(job.getError());
			label.setText(job.getMessage());
			if (job.isRunning()) {
				if (!progressBar.isVisible()) {
	                progressBar.setIndeterminate(job.isIndeterminated());
					progressBar.setVisible(true);
				} 
				progressBar.setString("in progress");
                progressBar.setValue(job.getPersentage());
			} else { 
				progressBar.setIndeterminate(false);
				progressBar.setVisible(false);
			}
		} else if (arg0 instanceof IBatchStatistics) {
		    String s = arg0.toString();
		    if (!s.equals(""))
		        batchlabel.setText("<html>" + s + "</html>");
		}
		
	}
}
