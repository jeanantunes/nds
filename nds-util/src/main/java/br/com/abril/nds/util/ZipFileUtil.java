package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;

public class ZipFileUtil {

	public static byte[] getZipFile(String fileName, byte[] fileBytes) throws IOException {
		
		if (StringUtils.isEmpty(fileName) || fileBytes == null) {
			return null;
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ZipOutputStream zos = new ZipOutputStream(out);
		
		ZipEntry fileToZip = null;
				
		fileToZip = new ZipEntry(fileName);
			
		zos.putNextEntry(fileToZip);
		zos.write(fileBytes);
		
		zos.closeEntry();
		zos.close();
		
		return out.toByteArray();
	}

}
