package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ResumoPeriodoLancamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.vo.PaginacaoVO;

public interface LancamentoRepository extends Repository<Lancamento, Long> {
	
	List<Lancamento> obterBalanceamentoMatrizLancamentos(FiltroLancamentoDTO filtro);
	
	SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores);

	void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista);

	List<ResumoPeriodoLancamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores, GrupoProduto grupoCromo);
	
	List<Lancamento> obterLancamentosNaoExpedidos(
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo);
	
	Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo);
	
	 Lancamento obterLancamentoPorItensRecebimentoFisico(Date dataPrevista, TipoLancamento tipoLancamento, Long idProdutoEdicao);
}
