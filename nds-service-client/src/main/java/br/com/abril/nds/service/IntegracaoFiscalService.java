package br.com.abril.nds.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;

public interface IntegracaoFiscalService {
	
	public File gerarArquivoP7(Date time);

	public Integer countGeracaoArquivoP7(Date time);

	public List<ExtratoEdicaoArquivoP7DTO> inventarioP7(Date time);
	
}
