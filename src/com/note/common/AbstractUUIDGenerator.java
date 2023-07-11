package com.note.common;

import java.net.InetAddress;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.hibernate.util.BytesHelper;

public abstract class AbstractUUIDGenerator implements IdentifierGenerator {
	private static final int IP;
	static {
		int ipadd;
		try {
			ipadd = BytesHelper.toInt( InetAddress.getLocalHost().getAddress() );
		}
		catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}
	protected static short counter = (short) 0;
	private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );

	public AbstractUUIDGenerator() {
	}

	protected int getJVM() {
		return JVM;
	}

	protected short getCount() {
		synchronized(AbstractUUIDGenerator.class) {
			if (counter<0) counter=0;
			return counter++;
		}
	}

	protected int getIP() {
		return IP;
	}

	protected short getHiTime() {
		return (short) ( System.currentTimeMillis() >>> 32 );
	}
	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}

}
