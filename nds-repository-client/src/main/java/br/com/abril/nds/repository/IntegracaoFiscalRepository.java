package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;
import br.com.abril.nds.dto.IntegracaoFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaIntegracaoFiscal;


public interface IntegracaoFiscalRepository   {

	public List<IntegracaoFiscalDTO> pesquisarPorMesAno(FiltroConsultaIntegracaoFiscal filtro);

	public void obterFixacoesRepartePorProduto(
			FiltroConsultaIntegracaoFiscal filtro);

	public List<ExtratoEdicaoArquivoP7DTO> inventarioP7(Date time);

	public Integer countInventarioP7(Date time);
	
}