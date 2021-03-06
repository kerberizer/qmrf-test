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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JTextPane;

import net.idea.qmrf.converters.QMRFConverter;

public class PatchedTextPane extends JTextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = -127884166854024049L;
	/**
	 * This one can paste also from Word 2000 etc. 
	 * Strange tags and comments are removed.
	 */
	@Override
	public void paste()
	{
		Clipboard clipboard = getToolkit().getSystemClipboard();
		final Transferable content = clipboard.getContents(this);
		/*
		 * create a new transferable to filter the original one
		 */

		Transferable newContent = new Transferable()
		{
			public DataFlavor[] getTransferDataFlavors()
			{
				DataFlavor[] flavors = content.getTransferDataFlavors();
				ArrayList<DataFlavor> myFlavorList = new ArrayList<DataFlavor>(flavors.length);
				try {
					for (int i = 0; i < flavors.length; i++)
					{
						DataFlavor flavor = flavors[i];
						
						//String s = "text/plain; class=String; charset=UTF-8";

						try {
							if ( flavor.getPrimaryType().equals("text") &&
								 (flavor.getSubType().equals("html") || flavor.getSubType().equals("plain")))  {

								myFlavorList.add(flavor);
													
							} 
						} catch (Exception x) {
							x.printStackTrace();
						}
					}
					DataFlavor[] myFlavors = new DataFlavor[myFlavorList.size()];
					for (int i = 0; i < myFlavorList.size(); i++)
					{
						DataFlavor flavor = (DataFlavor) myFlavorList.get(i);
						myFlavors[i] = flavor;
					}
					return myFlavors;
				} catch (Exception x) {
					x.printStackTrace();
					return null;
				}
			}
			
			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				return content.isDataFlavorSupported(flavor);
			}

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
			{
				String mimeType = flavor.getMimeType();
			
				if ( flavor.getPrimaryType().equals("text") &&
						 (flavor.getSubType().equals("html") || flavor.getSubType().equals("plain")))  {

					Object o = content.getTransferData(flavor);
					
					if (o instanceof InputStream) {
						
						String result = QMRFConverter.htmlBoning((InputStream)o);
						
						((InputStream)o).close();
						return result;
					} else if (o instanceof Reader) {
						String result = QMRFConverter.htmlBoning((Reader)o);
						
						((Reader)o).close();
						return new StringReader(result);
						/*
						BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)o,"UTF-8"));
	        			String line;
	        			StringBuffer b = new StringBuffer();
	        			while ((line = reader.readLine()) != null) {
	        		         b.append(line);
	        		    } 
	        			reader.close();
	        			
	        			
	        			return b.toString();
	        			*/
					} else return o;	
				}
				else
				{
					String data = (String) content.getTransferData(flavor);
					//data = data.substring(data.indexOf("<body>"), data.indexOf("</body>") + 7);

					return data;
				}
			}
			
		};

		/*
		 * set the new transferable to the clipboard
		 */
		clipboard.setContents(newContent, null);
		super.paste();
	}
	@Override
	public boolean isManagingFocus() {
		return isEditable()	;
	}
}
