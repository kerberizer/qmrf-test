package net.idea.qmrf.task;

import java.io.Writer;
import java.util.Date;
import java.util.UUID;

import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.SimpleTaskResource;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.TaskHTMLReporter;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.i.task.Task;
import net.idea.restnet.i.task.TaskResult;

import org.restlet.Request;

public class QMRFTaskHTMLReporter<USERID> extends TaskHTMLReporter<USERID> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8566674498695951801L;
	
	public QMRFTaskHTMLReporter(ITaskStorage<USERID> storage,Request ref,ResourceDoc doc) {
		this(storage,ref,doc,null);
	}
	public QMRFTaskHTMLReporter(ITaskStorage<USERID> storage,Request ref,ResourceDoc doc,HTMLBeauty htmlbeauty) {
		super(storage,ref,doc,htmlbeauty);
	}
	//<meta http-equiv="refresh" content="2;url=http://example.com/"
	
	
	@Override
	public void processItem(UUID name, Writer output) {
		Task<TaskResult,USERID> item = storage.findTask(name);
		String t = "";
		String status = "Unknown";
		try {
			t = item.getUri()==null?"":item.getUri().toString();
			status = item.getStatus().toString();
		} catch (Exception x) {
			x.printStackTrace();
			status = "Error";
			t = "";
		} finally {

			try {

				output.write(
				String.format(		
				"<div class=\"ui-widget \" style=\"margin-top: 20px; padding: 0 .7em;\">\n"+
				"<div class=\"ui-widget-header ui-corner-top\"><p><a href='%s%s/%s'>Job</a> started %s&nbsp;</p></div>\n"+
				"<div class=\"ui-widget-content ui-corner-bottom %s\">\n"+
				"<p>Name:&nbsp;<strong>%s</strong></p><p>Status:%s %s <img src=\"%s/images/%s\">&nbsp;<a href='%s'>%s</a>"+
				//"<div class=\"%s ui-corner-all\" style=\"position:relative;width:50%%; right:-40%%; margin: 0; padding: 0 .7em;\">\n"+ 
				"	<p><span class=\"ui-icon %s\" style=\"float: right; margin-right: .3em;\"></span>\n"+
				"	%s:&nbsp;&nbsp;<strong>%s</strong>" +
				"	%s:&nbsp;&nbsp;<strong>%s</strong>" +
				"</p>\n"+
			//	"</div>" +
				"</div></div>\n",

				baseReference.toString(),
				SimpleTaskResource.resource,
				item.getUuid(),
				new Date(item.getStarted()),
				item.isDone()?(item.getError()==null?"":"ui-state-highlight"):"",
				item.getName(),
				item.getError()!=null?"<strong>Error</strong>":item.getTimeCompleted()>0?"Completed":"",
				item.getError()!=null?"":item.getTimeCompleted()>0?new Date(item.getTimeCompleted()):"",
				baseReference.toString(),
				item.isDone()?(item.getError()==null?"tick.png":"cross.png"):"progress.gif",
				(item.isDone()&&item.getError()==null)?t:"",(item.isDone()&&item.getError()==null)?"Results available":"",
			
				item.isDone()?(item.getError()==null?"ui-icon-check":"ui-icon-alert"):"ui-icon-info",
				status,item.getError()==null?"":item.getError().getMessage(),
				item.getPolicyError()==null?"":"Policy error",item.getPolicyError()==null?"":item.getPolicyError().getMessage()
				));


			} catch (Exception x) {
				x.printStackTrace();
			}
		}
	};
	
	
	@Override
	protected void printNavigation() throws Exception {
	
	}
}
