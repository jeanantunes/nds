package br.com.abril.nds.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;
import br.com.abril.nds.dto.IntegracaoFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaIntegracaoFiscal;



public interface IntegracaoFiscalService {
	
	
	public List<IntegracaoFiscalDTO> pesquisarPorMesAno(FiltroConsultaIntegracaoFiscal filtro);

	public List<IntegracaoFiscalDTO> obterFixacoesRepartePorProduto(
			FiltroConsultaIntegracaoFiscal filtro);

	public File gerarArquivoP7(Date time);

	public Integer countGeracaoArquivoP7(Date time);

	public List<ExtratoEdicaoArquivoP7DTO> inventarioP7(Date time);
	
}
