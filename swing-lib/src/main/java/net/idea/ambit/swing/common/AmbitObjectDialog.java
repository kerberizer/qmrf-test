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
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import net.idea.ambit.swing.interfaces.AmbitObject;


/**
 * A dialog to display and edit arbitrary {@link ambit.data.AmbitObject} 
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 */
public class AmbitObjectDialog extends CoreDialog {
	AmbitObjectPanel qpanel;
	Dimension dim = null;
	/**
	 * 
	 * @param ao
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(AmbitObject ao) throws HeadlessException {
		super();
		qpanel.setAO(ao);
	}

	/**
	 * @param owner
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Dialog owner, AmbitObject ao) throws HeadlessException {
		super(owner);
		qpanel.setAO(ao);
	}

	/**
	 * @param owner
	 * @param modal
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Dialog owner, boolean modal,AmbitObject ao) throws HeadlessException {
		super(owner, modal);
		qpanel.setAO(ao);
	}

	/**
	 * @param owner
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Frame owner,AmbitObject ao) throws HeadlessException {
		super(owner);
		qpanel.setAO(ao);		
	}

	/**
	 * @param owner
	 * @param modal
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Frame owner, boolean modal,AmbitObject ao) throws HeadlessException {
		super(owner, modal);
	}

	/**
	 * @param owner
	 * @param title
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Dialog owner, String title,AmbitObject ao) throws HeadlessException {
		super(owner, title);
		qpanel.setAO(ao);		
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Dialog owner, String title, boolean modal,AmbitObject ao)
			throws HeadlessException {
		super(owner, title, modal);
		qpanel.setAO(ao);		
	}

	/**
	 * @param owner
	 * @param title
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Frame owner, String title,AmbitObject ao) throws HeadlessException {
		super(owner, title);
		qpanel.setAO(ao);
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Frame owner, String title, boolean modal,AmbitObject ao)
			throws HeadlessException {
		super(owner, title, modal);
		qpanel.setAO(ao);		
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 * @throws HeadlessException
	 */
	public AmbitObjectDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc,AmbitObject ao) throws HeadlessException {
		super(owner, title, modal, gc);
		qpanel.setAO(ao);		
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 */
	public AmbitObjectDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc,AmbitObject ao) {
		super(owner, title, modal, gc);
		qpanel.setAO(ao);		
	}
	protected void cancelAction() {
		super.cancelAction();
		setVisible(false);

	}

	/* (non-Javadoc)
	 * @see ambit.ui.CoreDialog#okAction()
	 */
	protected void okAction() {
		super.okAction();
		setVisible(false);

	}
	protected void addWidgets() {
		super.addWidgets();
		setTitle(getTitle());	
		//qpanel = new AmbitObjectPanel("",AmbitColors.BrightClr,AmbitColors.DarkClr,null);
		qpanel = new AmbitObjectPanel("Model",Color.white,AmbitColors.DarkClr,null);
		qpanel.setPreferredSize(new Dimension(600,500));
		qpanel.setMinimumSize(new Dimension(200,200));
		//qpanel.setEditable(true);
		Container contentPane = getContentPane();
		contentPane.add(qpanel, BorderLayout.CENTER);
		qpanel.setBorder(BorderFactory.createEtchedBorder(AmbitColors.DarkClr,AmbitColors.BrightClr));
	}
	
	public static AmbitObjectDialog createAndShow(
			boolean modal,
			String title,
			JFrame frame, 
			AmbitObject object, Dimension dim) {
		AmbitObjectDialog d = new AmbitObjectDialog(frame,modal,object);
		d.setTitle(title);
		d.qpanel.setPreferredSize(dim);
		d.qpanel.setAO(object);
		d.centerParent(frame);
		d.setVisible(true);
		return d;
	
	}	
	public static AmbitObjectDialog createAndShow(
			boolean modal,
			String title,
			JComponent component, 
			AmbitObject object ) {
		return createAndShow(modal,title,component,object,new Dimension(500,500));
	}
	public static AmbitObjectDialog createAndShowModal(
			boolean modal,
			String title,
			JFrame frame, 
			AmbitObject object) {
		return createAndShow(modal,title,frame,object,new Dimension(500,500));
	}
	
	public static AmbitObjectDialog createAndShow(
			boolean modal,
			String title,
			JComponent component, 
				AmbitObject object, Dimension dim) {
		
		Container owner = null;
		if ( component != null) {
			owner = component.getParent();
			while (!(owner instanceof JFrame)) 
				owner = owner.getParent();
		}
		return createAndShow(modal,title,(JFrame) owner,object,dim);
	}
	
	public void setAmbitObject(AmbitObject ao) {
		qpanel.setAO(ao);
	}

}
