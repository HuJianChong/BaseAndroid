package com.hjc.baselibrary.utils;


import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Base64Code工具类
 */
public class Base64Code {


	/**
	 * 采用Base64.Default模式进行编码；
	 * @param encodeStr
	 * @return
	 */
	public static String encodeDefault(String encodeStr) {
		if (TextUtils.isEmpty(encodeStr)) {
			return "";
		}

		return Base64.encodeToString(encodeStr.getBytes(), Base64.DEFAULT);
	}

	/**
	 * 采用Base64.Default模式进行解码；
	 * @param decodeStr
	 * @return
	 */
	public static String decodeDefault(String decodeStr) {
		if (TextUtils.isEmpty(decodeStr)) {
			return "";
		}

		byte[] decodeByte = Base64.decode(decodeStr, Base64.DEFAULT);

		if (decodeByte == null || decodeByte.length <=0) {
			return "";
		}

		return new String(decodeByte);
	}

	private Base64Code() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("Base64Code cannot be instantiated");

	}

	/**
	 * 解码
	 * @param encodeString
	 * @return
	 */
	public static String encode(String encodeString) {
		if (null == encodeString || encodeString.length() < 1) {
			return "";
		}
		try {
			char[] chars = encode(encodeString.getBytes("utf-8"));
			int len = chars.length;
			byte[] bytes = new byte[len];
			int codeMin = (len % 64) / 2;
			int codeMax = codeMin * 2;
			int curCode = codeMax;

			for (int i = 0; i < len; i++) {
				int b = chars[i];
				b += curCode;
				b %= 128;
				bytes[i] = (byte) b;
				curCode--;

				if (curCode < codeMin) {
					curCode = codeMax;
				}
			}

			return new String(encode(bytes));
		} catch (Throwable t) {
		}
		return "";
	}

	/**
	 * 加码
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		if (null == str || str.length() < 1) {
			return "";
		}

		try {
			byte[] bytes = decode(str.toCharArray());

			if (null == bytes || bytes.length < 1) {
				return "";
			}

			int len = bytes.length;
			char[] chars = new char[len];
			int codeMin = (len % 64) / 2;
			int codeMax = codeMin * 2;
			int curCode = codeMax;

			for (int i = 0; i < len; i++) {
				int b = bytes[i];
				b -= curCode;
				if (b < 0) {
					b += 128;
				}

				chars[i] = (char) b;
				curCode--;

				if (curCode < codeMin) {
					curCode = codeMax;
				}
			}

			byte[] db = decode(chars);
			if (null == db || db.length < 1) {
				return "";
			}

			return new String(db, "utf-8");
		} catch (Throwable t) {
		}

		return "";
	}

	// Mapping table from 6-bit nibbles to Base64 characters.
	private static char[] map1 = new char[64];

	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++) {
			map1[i++] = c;
		}

		for (char c = 'a'; c <= 'z'; c++) {
			map1[i++] = c;
		}

		for (char c = '0'; c <= '9'; c++) {
			map1[i++] = c;
		}
		map1[i++] = '+';
		map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.
	private static byte[] map2 = new byte[128];

	static {
		for (int i = 0; i < map2.length; i++) {
			map2[i] = -1;
		}

		for (int i = 0; i < 64; i++) {
			map2[map1[i]] = (byte) i;
		}
	}

	static char[] encode(byte[] in) {
		return encode(in, in.length);
	}

	static char[] encode(byte[] in, int iLen) {
		int oDataLen = (iLen * 4 + 2) / 3;
		int oLen = ((iLen + 2) / 3) * 4;
		char[] out = new char[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iLen ? in[ip++] & 0xff : 0;
			int i2 = ip < iLen ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}
		return out;
	}

	static byte[] decode(char[] in) {
		int iLen = in.length;
		if (iLen % 4 != 0) {
			throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
		}

		while (iLen > 0 && in[iLen - 1] == '=') {
			iLen--;
		}

		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = in[ip++];
			int i1 = in[ip++];
			int i2 = ip < iLen ? in[ip++] : 'A';
			int i3 = ip < iLen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
				throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
			}
			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
				throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
			}
			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;
			if (op < oLen) {
				out[op++] = (byte) o1;
			}
			if (op < oLen) {
				out[op++] = (byte) o2;
			}
		}
		return out;
	}
	public static String base64Decode(String str) {
		try {
			return new String(Base64.encode(str.getBytes("UTF8"), Base64.DEFAULT));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
