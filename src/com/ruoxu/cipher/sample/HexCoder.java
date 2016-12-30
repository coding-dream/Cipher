package com.ruoxu.cipher.sample;

public class HexCoder {

	public static void main(String[] args) {

		String str = "hello java";

		//把 二进制字符串转换为十六进制
		String hexStr = encodeHexString(str.getBytes());
		System.out.println(hexStr);
		
		//把十六进制字符串还原为二进制字符串 
		byte [] datas = decode(hexStr.getBytes());
		System.out.println(new String(datas));
		
		
	}

	// 把十六进制 转为二进制
	public static byte[] decode(byte[] array) {
		try {
			char[] data;
			data = new String(array, "UTF-8").toCharArray();
			int len = data.length;
	        if ((len & 0x01) != 0) {
	            throw new RuntimeException("Odd number of characters.");
	        }

	        byte[] out = new byte[len >> 1];

	        // two characters form the hex value.
	        for (int i = 0, j = 0; j < len; i++) {
	            int f = toDigit(data[j], j) << 4;
	            j++;
	            f = f | toDigit(data[j], j);
	            j++;
	            out[i] = (byte) (f & 0xFF);
	        }
	        	
	        return out;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 把二进制转换为十六进制
	protected static String encodeHexString(byte[] data) {

		char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return new String(out);
	}

	
    protected static int toDigit(char ch, int index)  {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    
}
