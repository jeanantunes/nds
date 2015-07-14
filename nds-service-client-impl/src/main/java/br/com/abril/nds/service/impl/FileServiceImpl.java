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
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

@Service
public class FileServiceImpl implements FileService {

	private static final String TEMP_DIR = "temp";
	private static final String REAL_DIR = "real";
	private final static int MB =  1024 * 1024;
	
	
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
		limparDiretorio(tempDir);
	}
	
	public void esvaziarReal(String dirBase) {
		
		File realDir = new File(dirBase + "/" + REAL_DIR);	
		limparDiretorio(realDir);
		
	}
	
	public void eliminarReal(String dirBase) {
		
		File realDir = new File(dirBase + "/" + REAL_DIR);	
		realDir.delete();
		
	}
	
	@Override
	public void limparDiretorio(File diretorio) {
		if(diretorio.exists()) {
			
		 	for (File file : diretorio.listFiles()) {
		 		file.delete();
		 	}
		
		} else {
			diretorio.mkdirs();
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

	@Override
	public void validarArquivo(int maxSize, UploadedFile file, FileType... extensoes) throws IOException {

		InputStream inputStream = file.getFile();
		int tamanhoArquivo = inputStream.available();
		
		int tamanhoMaximo = maxSize*MB;
		
		if (tamanhoMaximo < tamanhoArquivo) 
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Arquivo excedeu o tamanho limite de "+tamanhoMaximo+"MB"));
		
		boolean valido = false;
		
		for(FileType extensao : extensoes) {
			
			if(extensao.getContentType().equals(file.getContentType())) {
				valido = true;
				break;
			}
		}
		
		if(!valido) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Formato de arquivo inválido"));
		}	
	}
	
	@Override
	public String uploadTempFile(UploadedFile uploadedFile, String tempPath) {
		
		String tmpPath = "/images/tmp";
		
		File file = new File(tempPath, tmpPath);
		
		if(!file.exists()) {
			file.mkdirs();
		}
		
		String fileName = uploadedFile.getFileName();
		
		file = new File(file, fileName);
		
		FileOutputStream fos = null;
		
		InputStream is = uploadedFile.getFile();
		
		try {
			
			fos = new FileOutputStream(file);
			
			IOUtils.copyLarge(is, fos);
			
			fos.close();
			
		} catch (IOException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, " Falha ao fazer upload de arquivo temporario ");
		}
		
		return new File(tmpPath, fileName).getPath();
	}
}
