package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ArquivoDTO;
import br.com.abril.nds.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private static final String TEMP_DIR = "temp";
	private static final String REAL_DIR = "real";
	
	@Override
	public void resetTemp(String dirBase) throws IOException {
		
		this.esvaziarTemp(dirBase);
		
		File realDir = new File(dirBase + "/" + REAL_DIR);	
		
		File arquivo = null;
		
		if(realDir.exists()) {
			
			if(realDir.list().length > 0)
				arquivo = realDir.listFiles()[0];
		 			
		} else {
			realDir.mkdirs();
		}
		
		if(arquivo != null) {
			FileInputStream fis = new FileInputStream(arquivo);
			this.setArquivoTemp(dirBase, arquivo.getName(), fis);
			fis.close();
		}
	}

	@Override
	public void setArquivoTemp(String dirBase, String nomeArquivo, InputStream inputStream) throws IOException {
		
		this.esvaziarTemp(dirBase);
		
		File arquivo = new File(dirBase + "/" + TEMP_DIR, nomeArquivo);
		
		FileOutputStream fos = new FileOutputStream(arquivo);
		
		IOUtils.copyLarge(inputStream, fos);
		
		inputStream.close();
		
		fos.close();
	}

	@Override
	public void esvaziarTemp(String dirBase) {
		
		File tempDir = new File(dirBase + "/" + TEMP_DIR);	
		
		if(tempDir.exists()) {
			
		 	for (File file : tempDir.listFiles()) {
		 		file.delete();
		 	}
		
		} else {
			tempDir.mkdirs();
		}		
	}
	
	public void esvaziarReal(String dirBase) {
		
		File realDir = new File(dirBase + "/" + REAL_DIR);	
		
		if(realDir.exists()) {
			
		 	for (File file : realDir.listFiles()) {
		 		file.delete();
		 	}
		
		} else {
			realDir.mkdirs();
		}		
	}

	@Override
	public void persistirTemporario(String dirBase) throws FileNotFoundException, IOException {
		
		File tempDir = new File(dirBase + "/" + TEMP_DIR);	
		
		File arquivo = null;
		
		if(tempDir.exists()) {
			
			if(tempDir.list().length > 0)
				arquivo = tempDir.listFiles()[0];
		}
		

		esvaziarReal(dirBase);
		
		if(arquivo != null) {
			
			File novoArquivo = new File(dirBase + "/" + REAL_DIR + "/" + arquivo.getName());
			
			FileOutputStream fos = new FileOutputStream(novoArquivo);
			
			FileInputStream fis = new FileInputStream(arquivo);
						
			IOUtils.copyLarge(fis, fos);
			

			fis.close();
			
			fos.close();
		}
	}

	@Override
	public ArquivoDTO obterArquivoTemp(String dirBase) throws IOException {
		
		File tempDir = new File(dirBase + "/" + TEMP_DIR);	
		
		File arquivo = null;
		
		if(tempDir.exists()) {
			
			if(tempDir.list().length > 0)
				arquivo = tempDir.listFiles()[0];
		}
		
		if(arquivo == null) 
			return null;
		
		ArquivoDTO dto = new ArquivoDTO();
		
		FileInputStream fis = new FileInputStream(arquivo);
		
		dto.setArquivo(fis);
				
		dto.setNomeArquivo(arquivo.getName());
		dto.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(arquivo));
		return dto;
	}
	
	@Override
	public String obterNomeArquivoTemp(String dirBase) {
		
		File tempDir = new File(dirBase + "/" + TEMP_DIR);	
		
		File arquivo = null;
		
		if(tempDir.exists()) {
			
			if(tempDir.list().length > 0)
				arquivo = tempDir.listFiles()[0];
		}
		
		if(arquivo == null) 
			return null;
		else
			return arquivo.getName();
	}
}
