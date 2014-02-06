package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.log4j.Logger;

public class TXTUtil {
    
    private static final Logger LOGGER = Logger.getLogger(TXTUtil.class);

	public static byte[] mergeTXTs(List<byte[]> arquivos) {

		try {
			ByteArrayOutputStream txts = new ByteArrayOutputStream();

			for (byte[] byteFile : arquivos) {
				txts.write(byteFile);
			}
			
			return txts.toByteArray();
		
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return null;
	}
	
}
