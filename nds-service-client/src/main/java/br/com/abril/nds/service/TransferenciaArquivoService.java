package br.com.abril.nds.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DiretorioTransferenciaArquivoDTO;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

public interface TransferenciaArquivoService {

	void salvarDiretorio(DiretorioTransferenciaArquivo diretorio);

	void excluirDiretorio(Long id);

	List<DiretorioTransferenciaArquivo> buscarDiretorios();

	DiretorioTransferenciaArquivo buscarPorId(Long id);

	List<DiretorioTransferenciaArquivoDTO> obterTodosDiretorios(DiretorioTransferenciaArquivoDTO dto);

	String upload(UploadedFile uploadedFile, String parametroPath) throws IOException;

	Boolean isDiretorioExistente(String pathDiretorio);

}
