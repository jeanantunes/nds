package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DiretorioTransferenciaArquivoDTO;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;
import br.com.abril.nds.repository.TransferenciaArquivoRepository;
import br.com.abril.nds.service.TransferenciaArquivoService;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

@Service
public class TransferenciaArquivoServiceImpl implements TransferenciaArquivoService {

	@Autowired
	private TransferenciaArquivoRepository transferenciaArquivosRepository;
	
	@Override
	@Transactional
	public void salvarDiretorio(DiretorioTransferenciaArquivo diretorio) {
		transferenciaArquivosRepository.adicionar(diretorio);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<DiretorioTransferenciaArquivo> buscarDiretorios() {
		return transferenciaArquivosRepository.buscarTodos();
	}
	
	@Override
	@Transactional
	public DiretorioTransferenciaArquivo buscarPorId(Long id){
		return transferenciaArquivosRepository.buscarPorId(id);
	}
	
	@Override
	@Transactional
	public void excluirDiretorio(Long id) {
		transferenciaArquivosRepository.removerPorId(id);
		
	}
	
	@Override
	@Transactional
	public List<DiretorioTransferenciaArquivoDTO> obterTodosDiretorios(DiretorioTransferenciaArquivoDTO dto){
		return transferenciaArquivosRepository.carregarDiretorios(dto);
	}
	
	@Override
	public String upload(UploadedFile uploadedFile, String parametroPath) throws IOException{
		
		String fileName = "";
        
        if (uploadedFile != null) {
            
            final String dirBase = (parametroPath).replace("\\", "/");
            
            File file = new File(dirBase);
            file.mkdirs();
            
            fileName = converteFileName(uploadedFile.getFileName());
            
            File destino = new File(file, fileName);

            try {
              IOUtils.copy(uploadedFile.getFile(), new FileOutputStream(destino));
            } catch (IOException e) {
              throw new RuntimeException("Erro ao copiar arquivo", e);
            }
            
          }
		
        return fileName;
	}
	
	public String converteFileName(String text) {   
        
        return text.replaceAll("[ãâàáä]", "a")     
                    .replaceAll("[êèéë]", "e")     
                    .replaceAll("[îìíï]", "i")     
                    .replaceAll("[õôòóö]", "o")     
                    .replaceAll("[ûúùü]", "u")     
                    .replaceAll("[ÃÂÀÁÄ]", "A")     
                    .replaceAll("[ÊÈÉË]", "E")     
                    .replaceAll("[ÎÌÍÏ]", "I")     
                    .replaceAll("[ÕÔÒÓÖ]", "O")     
                    .replaceAll("[ÛÙÚÜ]", "U")     
                    .replace('ç', 'c')     
                    .replace('Ç', 'C')     
                    .replace('ñ', 'n')     
                    .replace('Ñ', 'N')  
                    .replaceAll("!", "")
                    .replaceAll("[\\+\\'\\ª\\º\\*\\¨\\´\\`\\?!\\@\\~\\^\\/]"," ");
    }
}
