package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;


public interface FixacaoReparteService {
	
	void incluirFixacaoReparte(FixacaoReparte fixacaoReparte);
	
	public List<FixacaoReparteDTO>obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO
			filtroConsultaFixacaoProdutoDTO);
	
	
	public List<FixacaoReparteDTO>obterFixacoesRepartePorCota(FiltroConsultaFixacaoCotaDTO
			filtroConsultaFixacaoCotaDTO);
	
}
