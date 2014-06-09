package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;


public interface IntegracaoFiscalRepository   {

	public List<ExtratoEdicaoArquivoP7DTO> inventarioP7(Date time);

	public Integer countInventarioP7(Date time);
	
}
