package net.idea.qmrf.client;

import net.idea.restnet.aa.local.UserLoginFormResource;
import net.idea.restnet.aa.resource.AdminResource;
import net.idea.restnet.c.resource.TaskResource;

public class Resources {
	public static final String welcome = "/welcome";
	public static final String protocol = "/protocol";
	public static final String unpublished = "/unpublished";
	public static final String chemical = "/chemical";
	public static final String structure = "/structure";
	public static final String dataset = "/dataset";
	public static final String endpoint = "/endpoint";
	public static final String project = "/project";
	public static final String organisation = "/organisation";
	public static final String user = "/user";
	public static final String versions = "/versions";
	public static final String previous = "/previous";
	public static final String authors = "/authors";
	public static final String chapter = "/chapter";
	public static final String document = "/document";
	public static final String attachment = "/attachment";
	public static final String editor = "/editor";
	public static final String login = String.format("/%s",UserLoginFormResource.resource);
	public static final String myaccount = "/myaccount";
	public static final String register = "/register";
	public static final String admin = String.format("/%s",AdminResource.resource);
	public static final String task = TaskResource.resource;
	public static final String alert = "/alert";
	public static final String reset = "/reset";

	/*
	public static final String data_training = "/data_training";
	public static final String data_validation = "/data_validation";
	*/
	public enum Config {
		qmrf_protected,
		qmrf_attachments_dir,
		qmrf_ambit_service,
		qmrf_default_owner,
		qmrf_default_project,
		qmrf_default_organisation,
		google_analytics,
		logo_left,
		logo_right,
		secret,
		insecure,
		qmrf_email,
		qmrf_editor,
		qmrf_template,
		qmrf_manual,
		qmrf_faq,
		qmrf_oecd,
		qmrf_jrc,
		sessiontimeout
	}
	
	public final static String AMBIT_LOCAL_USER="aa.local.admin.name";
	public final static String AMBIT_LOCAL_PWD="aa.local.admin.pass";
}
