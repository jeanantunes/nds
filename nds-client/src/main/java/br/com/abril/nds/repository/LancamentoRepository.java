package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
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
	  * @param grupoCromo
	  * 
	  * @return List<ProdutoRecolhimentoDTO>
	  */
	 List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimento(PeriodoVO periodoRecolhimento, 
					 											 List<Long> fornecedores,
					 											 GrupoProduto grupoCromo);
	 
	 /**
	  * Método que retorna o balanceamento do recolhimento referentes a um periodo 
	  * ordernados por editor e data de recolhimento do distribuidor.
	  * 
	  * @param periodoRecolhimento
	  * 
	  * @param listaIdsFornecedores 
	  * 
	  * @param grupoCromo
	  * 
	  * @return List<ProdutoRecolhimentoDTO>
	  */
	 List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimentoPorEditorData(PeriodoVO periodoRecolhimento, 
								 											  List<Long> fornecedores,
								 											  GrupoProduto grupoCromo);

	 /**
	  * Método que retorna expectativas de encalhe baseadas nas datas do período informado. 
	  * 
	  * @param periodoRecolhimento
	  * 
	  * @param fornecedores
	  * 
	  * @param grupoCromo
	  * 
	  * @return Map<Date, BigDecimal>
	  */
	 TreeMap<Date, BigDecimal> obterExpectativasEncalhePorData(PeriodoVO periodoRecolhimento, 
															   List<Long> fornecedores,
															   GrupoProduto grupoCromo);
	 
	 /**
	  * Método que verifica a existência de uma chamada de encalhe do tipo Matriz Recolhimento
	  * para o período especificado.
	  * 
	  * @param periodo - Período a ser utilizado na pesquisa.
	  * 
	  * @return caso exista chamada: true, caso não exista: false.
	  */
	 boolean verificarExistenciaChamadaEncalheMatrizRecolhimento(PeriodoVO periodo);

	 /**
	  * Obtém o último Lancamento de determinado ProdutoEdicao
	  * 
	  * @param idProdutoEdicao - Id do ProdutoEdicao
	  * @return Lancamento
	  */
	Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao);
	 
	 /**
	  * Obtém uma lista de lancamentos de acordo com o parâmetro informado
	  * 
	  * @param idsLancamento - identificadores de lancamento
	  * 
	  * @return {@link List<Lancamento>}
	  */
	 List<Lancamento> obterLancamentosPorId(Set<Long> idsLancamento);
	 
}
