package com.smssite2.mgmt.patch;

import java.util.StringTokenizer;
import org.apache.commons.lang.math.NumberUtils;
import com.note.common.ConfigUtil;

public class MobileUtil {
	public static final int TYPE_CM = 0;
	public static final int TYPE_UC = 1;
	public static final int TYPE_TC = 2;
	public static final int TYPE_UNKNOWN = -1;
	
	public static int [] TypeTable = new int[1000];
	
	static {
		setType( ConfigUtil.getProperties("mobile"), TYPE_CM);
		setType( ConfigUtil.getProperties("unicom"), TYPE_UC);
		setType( ConfigUtil.getProperties("ctc"), TYPE_TC);
	}

	private static void setType(String s, int type) {
		if (s == null)
			return;
		StringTokenizer st = new StringTokenizer(s, ",; ");
		while ( st.hasMoreTokens() ) {
			String x = st.nextToken();
			int i = NumberUtils.toInt(x, -1);
			if ( i < 0 || i > 1000)
				continue;
			TypeTable[i] = type + 1;
		}
	}
	
	public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
        	char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
	
	protected static int getType(String mobile) {
		
		if ( mobile == null )
			return TYPE_UNKNOWN;
		
		if ( mobile.length() != 11 )
			return TYPE_UNKNOWN;
		
		if ( ! isNumeric( mobile ) )
			return TYPE_UNKNOWN;
		
		String seg = mobile.substring(0, 3);
		
		return TypeTable[ Integer.parseInt(seg) ] - 1;
	}
	
	protected static String format(String s) {
		if (s == null)
			return null;
		
		s = s.trim();
		if (s.startsWith("+")) s = s.substring(1);
		if (s.startsWith("86")) s = s.substring(2);
		return s;
	}
}
