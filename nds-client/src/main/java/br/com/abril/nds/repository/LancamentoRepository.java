package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

public interface LancamentoRepository extends Repository<Lancamento, Long> {
	
	List<Lancamento> obterBalanceamentoMatrizLancamentos(FiltroLancamentoDTO filtro);
	
	SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores);

	void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista);

	List<ResumoPeriodoBalanceamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores, GrupoProduto grupoCromo);
	
	List<ResumoPeriodoBalanceamentoDTO> buscarResumosPeriodoRecolhimento(
		List<Date> periodoRecolhimento, List<Long> fornecedores, GrupoProduto grupoCromo);
	
	List<Lancamento> obterLancamentosNaoExpedidos(
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo);
	
	Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo);
	
	 Lancamento obterLancamentoPorItensRecebimentoFisico(Date dataPrevista, TipoLancamento tipoLancamento, Long idProdutoEdicao);
	 
	 Date obterDataRecolhimentoPrevista(String codigoProduto, Long numeroEdicao);
	 
	 /**
	  * Método que retorna o balanceamento do recolhimento referentes a um periodo e determinados fornecedores.
	  * 
	  * @param periodoRecolhimento
	  * 
	  * @param listaIdsFornecedores 
	  * 
	  * @return List<ProdutoRecolhimentoDTO>
	  */
	 List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimento(PeriodoVO periodoRecolhimento, 
					 											 List<Long> fornecedores,
					 											 GrupoProduto grupoCromo);
}
