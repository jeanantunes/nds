package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;

public interface MovimentoEstoqueService {

	void gerarMovimentoEstoqueDeExpedicao(Lancamento lancamento, Long idUsuario);
	//void gerarMovimentoEstoqueDeExpedicao(Date dataLancamento, Long idProdutoEdicao, Long idUsuario);
	
	MovimentoEstoque gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao,Long idUsuario,BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);
	
	MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);
	
	void enviarSuplementarCotaAusente(Date data, Long idCota,List<MovimentoEstoqueCota> listaMovimentoCota) throws TipoMovimentoEstoqueInexistenteException;

	void atualizarEstoqueProduto(TipoMovimentoEstoque tipoMovimentoEstoque,
							 	 MovimentoEstoque movimentoEstoque);
	
	void atualizarEstoqueProdutoCota(TipoMovimentoEstoque tipoMovimentoEstoque,
								 	 MovimentoEstoqueCota movimentoEstoqueCota);
	
	MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);

	void processarRegistroHistoricoVenda(HistoricoVendaInput vendaInput);
	
	/**
	 * Gera movimento para cancelamento de nota, 
	 * devolvendo produtos da nota para estoque de lançamento do distribuidor
	 * 
	 * @param notaFiscal
	 * @param idUsuario
	 */
	void devolucaoConsignadoNotaCancelada(NotaFiscal notaFiscalCancelada);
	
	
	/**
	 * Gera movimento para cancelamento de nota, 
	 * devolvendo produtos da nota para estoque de recolhimento do distribuidor
	 * 
	 * @param notaFiscalCancelada
	 * @param idUsuario
	 */
	void devolucaoRecolhimentoNotaCancelada(NotaFiscal notaFiscalCancelada);
	
	void gerarMovimentoEstoqueFuroPublicacao(Lancamento lancamento, Long idUsuario);
	
}
