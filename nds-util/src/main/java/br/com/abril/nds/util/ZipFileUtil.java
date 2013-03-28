package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;

public class ZipFileUtil {

	public static byte[] getZipFile(Map<String, byte[]> filesToZip) throws IOException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ZipOutputStream zos = new ZipOutputStream(out);
		
		ZipEntry fileToZip = null;
				
		for(Map.Entry<String, byte[]> file : filesToZip.entrySet()) {
		
			String fileName = file.getKey();
			byte[] fileBytes = file.getValue();
			
			if (StringUtils.isEmpty(fileName) || fileBytes == null) {
				
				continue;
			}
			
			fileToZip = new ZipEntry(fileName);
				
			zos.putNextEntry(fileToZip);
			zos.write(fileBytes);
		}
		
		zos.closeEntry();
		zos.close();
		
		return out.toByteArray();
	}

}
