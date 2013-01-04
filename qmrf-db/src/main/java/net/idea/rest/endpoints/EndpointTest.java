package net.idea.rest.endpoints;

import ambit2.base.data.Dictionary;

public class EndpointTest extends Dictionary {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5987449761837302780L;
	protected String code;
	protected String parentCode;
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	protected int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EndpointTest(String arg1,String arg2) {
		super(arg1,arg2);
	}
	
	public EndpointTest(String arg1,String arg2,String arg3) {
		super(arg1,arg2,arg3);
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return String.format("%s.%s",getCode(),getName());
	}
	
	public static String[] split(String label) {
		String[] decoded = new String[2];
		String endpointname = label;
		String[] split = endpointname.split("\\.");
		StringBuilder endpointCode = new StringBuilder();
		for (int i=0; i < (split.length-1);i++) {endpointCode.append(split[i]);endpointCode.append(".");}
		if(split.length>0) endpointname = split[split.length-1].trim();
		decoded[0] = endpointCode.toString();
		decoded[1] = endpointname;
		return decoded;
	}
}
