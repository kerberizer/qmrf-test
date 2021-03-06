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


package net.idea.ambit.qmrf.chapters;

import net.idea.ambit.qmrf.swing.QMRFSubChapterAlgorithmEditor;
import net.idea.ambit.qmrf.xml.XMLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ambit2.base.interfaces.IAmbitEditor;

public class QMRFSubchapterAlgorithm extends QMRFSubChapterReference {
	public QMRFSubchapterAlgorithm() {
		this("subchapter","0","Subchapter title","Help");
	}
    public QMRFSubchapterAlgorithm(String elementID) {
        this(elementID,"0","Title","Help on "+ elementID);
    }       
	public QMRFSubchapterAlgorithm(String elementid, String chapter,String title,String help) {
		super(elementid,chapter,title,help);
	}

    public void fromXML(Element xml) throws XMLException {
        super.fromXML(xml);
        NodeList children = xml.getChildNodes();
        for (int i=0; i < children.getLength();i++) 
            if (Node.ELEMENT_NODE==children.item(i).getNodeType()) {
                if ("equation".equals(children.item(i).getLocalName())) {
                	setText(((Element)children.item(i)).getTextContent());
                }
            }
     
    }
    public Element toXML(Document document) throws XMLException {
        Element element = super.toXML(document);
        Element equation = document.createElement("equation");
        equation.appendChild(document.createTextNode(text));
        element.appendChild(equation);
        return element;
    }
    @Override
    protected Node createTextNode(Document document) {
    	return null;
    }
    @Override
    public IAmbitEditor editor(boolean editable) {
    	return new QMRFSubChapterAlgorithmEditor(this,editable);
    }
}


