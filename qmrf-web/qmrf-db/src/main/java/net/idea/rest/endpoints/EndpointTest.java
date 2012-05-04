package net.idea.rest.endpoints;

import ambit2.base.data.Dictionary;

public class EndpointTest extends Dictionary {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5987449761837302780L;
	protected String code;
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
}
