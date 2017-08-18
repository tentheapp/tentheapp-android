package com.nvcomputers.ten.utils;

import android.location.Address;

public class AddressUtil {

	public static String createFormattedAddressFromAddress(final Address address) {
		if (address == null)
			return "";
		StringBuilder mSb = new StringBuilder();
		mSb.setLength(0);
		final int addressLineSize = address.getMaxAddressLineIndex();
		for (int i = 0; i < addressLineSize; i++) {
			mSb.append(address.getAddressLine(i));
			if (i != addressLineSize - 1) {
				mSb.append(", ");
			}
		}
		return mSb.toString();
	}

}
