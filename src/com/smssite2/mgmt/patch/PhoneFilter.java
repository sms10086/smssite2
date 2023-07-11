package com.smssite2.mgmt.patch;

import java.util.Arrays;

public class PhoneFilter {	
	public long [] id;
	public int count;
	
	public PhoneFilter(int total) {
		id = new long[total];
	}
	
	public boolean isExist(long mobile) {
		int index = binarySearch0(id, 0, count, mobile);
		if ( index >= 0 )
			return true;
		if (count >= id.length) {
			int newLength = id.length;
			if (newLength < 10000 ) newLength = 20000;
			else newLength = newLength + (newLength >> 1);
			try {
				id = Arrays.copyOf(id, newLength);
			} catch (Throwable thr) {
				System.out.println("FATAL: Can't allocate memory for phone filter " + newLength);
				return true;
			}
		}
		index = - index - 1;
		System.arraycopy(id, index, id, index+1, count - index);
		id[index] = mobile;
		count ++;
		return false;
	}

	private static int binarySearch0(long[] a, int fromIndex, int toIndex, long key) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else
				return mid;
		}
		return -(low + 1);
	}
}
