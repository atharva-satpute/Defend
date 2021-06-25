package com.app.defend;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

	public static String getAlphaNumericString(int n) {

		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789"
				+ "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			int index
					= (int) (AlphaNumericString.length()
					* Math.random());

			sb.append(AlphaNumericString
					.charAt(index));
		}

		return sb.toString();
	}

	public static void saveKeysToSP(String privateKey, String publicKey, Context context) {
		SharedPreferences sp = context.getSharedPreferences("configs", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("privateKey", privateKey);
		edit.putString("publicKey", publicKey);
		edit.apply();
	}

	public static String getPrivateKey(Context context) {
		SharedPreferences sp = context.getSharedPreferences("configs", Context.MODE_PRIVATE);
		String privateKey = sp.getString("privateKey", null);
		return privateKey;
	}

	public static String getPublicKey(Context context) {
		SharedPreferences sp = context.getSharedPreferences("configs", Context.MODE_PRIVATE);
		String publicKey = sp.getString("publicKey", null);
		return publicKey;
	}

}
