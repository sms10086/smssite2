package com.note.common;

import java.io.Serializable;
import org.hibernate.engine.SessionImplementor;

public class UUIDHexGenerator extends AbstractUUIDGenerator {	
	public static final String NULL_ID = "0";

	protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace( 8-formatted.length(), 8, formatted );
		return buf.toString();
	}

	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace( 4-formatted.length(), 4, formatted );
		return buf.toString();
	}

	public Serializable generate(SessionImplementor session, Object obj) {
		return generate();
	}
	
	public String generate() {
		return new StringBuffer(36)
		.append( format( getIP() ) )
		.append( format( getJVM() ) )
		.append( format( getHiTime() ) )
		.append( format( getLoTime() ) )
		.append( format( getCount() ) )
		.toString();
	}

}
