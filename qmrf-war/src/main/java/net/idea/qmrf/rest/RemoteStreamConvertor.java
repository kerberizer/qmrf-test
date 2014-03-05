package net.idea.qmrf.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.restnet.i.tools.DownloadTool;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;


public class RemoteStreamConvertor extends DefaultAmbitProcessor<URL,Representation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5395806328786966562L;

	@Override
	public Representation process(final URL url) throws Exception {
		OutputRepresentation rep = new OutputRepresentation(MediaType.APPLICATION_JSON) {
            @Override
            public void write(OutputStream stream) throws IOException {
            	InputStream in = null;
            	try {
                	in = url.openStream();
               		DownloadTool.download(in, stream);
            		//writer.flush();
            		//stream.flush();
            	} catch (Exception x) {
            		Throwable ex = x;
            		while (ex!=null) {
            			if (ex instanceof IOException) 
            				throw (IOException)ex;
            			ex = ex.getCause();
            		}
            		Context.getCurrentLogger().warning(x.getMessage()==null?x.toString():x.getMessage());

            	} finally {

            		try {if (stream !=null) stream.flush(); } catch (Exception x) { x.printStackTrace();}
            		try {if (in !=null) in.close(); } catch (Exception x) { x.printStackTrace();}
            	}
            }
        };	
        //setDisposition(rep);
        return rep;
	}

}
