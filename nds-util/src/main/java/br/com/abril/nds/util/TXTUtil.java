package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class TXTUtil {

	public static byte[] mergeTXTs(List<byte[]> arquivos) {

		try {
			ByteArrayOutputStream txts = new ByteArrayOutputStream();

			for (byte[] byteFile : arquivos) {
				txts.write(byteFile);
			}
			
			return txts.toByteArray();
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
}
