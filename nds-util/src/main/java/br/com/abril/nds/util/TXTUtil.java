package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TXTUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TXTUtil.class);

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
