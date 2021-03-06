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
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * A Dialog
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 */
public abstract  class CoreDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8281042988604048591L;
	protected JButton cancelButton, okButton;
	protected JPanel buttonPane; 
	
	protected int result = JOptionPane.CANCEL_OPTION;
	/**
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog() throws HeadlessException {
		super();
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Dialog owner) throws HeadlessException {
		super(owner);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param modal
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Dialog owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Frame owner) throws HeadlessException {
		super(owner);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param modal
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param title
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Dialog owner, String title) throws HeadlessException {
		super(owner, title);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Dialog owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param title
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Frame owner, String title) throws HeadlessException {
		super(owner, title);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Frame owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		addWidgets();
		pack();

	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 * @throws java.awt.HeadlessException
	 */
	public CoreDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
		addWidgets();
		pack();
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 */
	public CoreDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		addWidgets();
		pack();

	}
	protected void cancelAction() {
		result = JOptionPane.CANCEL_OPTION;
		setVisible(false);
	}
	
	protected void okAction() {
		result = JOptionPane.OK_OPTION;
		setVisible(false);
	}
		
		
	protected void addWidgets() {

		cancelButton = new JButton("Cancel");
		okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		cancelButton.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
		 	cancelAction();
		 }
		});
		okButton.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
		     okAction();
		 }
		});
		getRootPane().setDefaultButton(okButton);

//		Lay out the buttons from left to right.
		buttonPane = new JPanel();
		
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(okButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);		
		buttonPane.setPreferredSize(new Dimension(400,48));
//		Put everything together, using the content pane's BorderLayout.
		Container contentPane = getContentPane();
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.setBackground(AmbitColors.BrightClr);
		buttonPane.setBackground(AmbitColors.BrightClr);
	}
	
	public void centerParent (Frame frame) {
		frame = JOptionPane.getFrameForComponent(null);
		Dimension dim = frame.getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
			      (dim.height - abounds.height) / 2);
	}
//		 centers the dialog within the parent container [1.1]
		public void centerParent() throws Exception  {
		    
		  int x;
		  int y;

		  // Find out our parent 
		  Container myParent = getParent();
		  if (myParent != null) {
			  Point topLeft = myParent.getLocationOnScreen();
			  Dimension parentSize = myParent.getSize();
	
			  Dimension mySize = getSize();
	
			  if (parentSize.width > mySize.width) 
			    x = ((parentSize.width - mySize.width)/2) + topLeft.x;
			  else 
			    x = topLeft.x;
			   
			  if (parentSize.height > mySize.height) 
			    y = ((parentSize.height - mySize.height)/2) + topLeft.y;
			  else 
			    y = topLeft.y;
			   
			  setLocation (x, y);
		  }
		  }  
		 
	
	public int getResult() {
		return result;
	}
}

