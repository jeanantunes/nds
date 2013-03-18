package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;


public interface FixacaoReparteService {
	
	void incluirFixacaoReparte(FixacaoReparte fixacaoReparte);
	
	public List<FixacaoReparteDTO>obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO
			filtroConsultaFixacaoProdutoDTO);
	
	
	public List<FixacaoReparteDTO>obterFixacoesRepartePorCota(FiltroConsultaFixacaoCotaDTO
			filtroConsultaFixacaoCotaDTO);
	
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorProduto(FiltroConsultaFixacaoProdutoDTO produto);
	
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorCota(FiltroConsultaFixacaoCotaDTO filtroCota);
	
	public void adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO);
	
	public void removerFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO);
	
	public List<TipoClassificacaoProduto> obterClassificacoesProduto();
	
	public FixacaoReparte obterFixacao(Long id);
	
	public FixacaoReparteDTO obterFixacaoDTO(Long id);
	
	public List<PdvDTO> obterListaPdvPorFixacao(Long id);
	
	public FixacaoReparte buscarFixacaoCadastrada(FixacaoReparte fixacaoReparte);
	
	public boolean isCotaPossuiVariosPdvs(Long idCota);
}
