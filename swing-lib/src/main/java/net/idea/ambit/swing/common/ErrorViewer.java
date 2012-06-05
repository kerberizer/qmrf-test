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
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class ErrorViewer<T extends JComponent> {
    protected T errorsLabel;
	protected Exception theError;    

	public ErrorViewer() {
		errorsLabel = createComponent();
	}
	protected abstract T createComponent();

	public T getView() {
		return errorsLabel;
	}
    public void setError(Exception x) {
    	theError = x;
    	update();
    }
    protected abstract void update();

    
    public void showErrorlog(Exception x) {
    	if (x == null) return;
        JPanel p = new JPanel(new BorderLayout());
        

        JTextArea t;
        String top = x.toString();
        
        StringWriter w = new StringWriter();
        x.printStackTrace(new PrintWriter(w));
        t = new JTextArea(w.toString());
        w = null;        
       
        
        t.setAutoscrolls(true);
        
        JScrollPane sp = new JScrollPane(t);
        sp.setAutoscrolls(true);
        sp.setPreferredSize(new Dimension(350,200));
        p.add(sp,BorderLayout.CENTER);
        
        Object[] options = {"Hide",
                "Clear"
                };

        if (JOptionPane.showOptionDialog(errorsLabel,
                p,
                "Last error",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]) == 1) {
            setError(null);
        }   
    }	    
	
}
