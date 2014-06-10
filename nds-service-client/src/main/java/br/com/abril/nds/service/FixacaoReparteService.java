package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;


public interface FixacaoReparteService {
	
	public List<FixacaoReparteDTO>obterFixacoesRepartePorProduto(FiltroConsultaFixacaoProdutoDTO filtroConsultaFixacaoProdutoDTO);
	
	public List<FixacaoReparteDTO>obterFixacoesRepartePorCota(FiltroConsultaFixacaoCotaDTO filtroConsultaFixacaoCotaDTO);
	
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorProduto(FiltroConsultaFixacaoProdutoDTO produto);
	
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorCota(FiltroConsultaFixacaoCotaDTO filtroCota);
	
	public FixacaoReparte adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO);
	
	public void removerFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO);
	
	public List<TipoClassificacaoProduto> obterClassificacoesProduto();
	
	public FixacaoReparte obterFixacao(Long id);
	
	public FixacaoReparteDTO obterFixacaoDTO(Long id);
	
	public List<PdvDTO> obterListaPdvPorFixacao(Long id);
	
	public boolean isCotaPossuiVariosPdvs(Long idCota);

	void excluirFixacaoPorCota(Long idCota);
	
	public boolean isFixacaoExistente(FixacaoReparteDTO fixacaoReparteDTO);

	public boolean isCotaValida(FixacaoReparteDTO fixacaoReparteDTO);

	boolean gerarCopiafixacao(CopiaMixFixacaoDTO copiaDTO);
	
	public void atualizaFixacao(List<BigInteger> lancamentosDiarios);
	
	public void verificarFixacao (Date dataOperacaoAserFechada);

	public FixacaoReparte buscarPorProdutoCotaClassificacao(Cota cota, String codigoICD, TipoClassificacaoProduto tipoClassificacaoProduto);
	
}
