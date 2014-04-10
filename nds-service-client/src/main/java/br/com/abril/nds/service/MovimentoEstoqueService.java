package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentosEstoqueCotaSaldoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;

public interface MovimentoEstoqueService {

    void transferirEstoqueProdutoEdicaoParcialParaLancamento(Long idProdutoEdicao, Usuario usuario);
	
    void transferirEstoqueProdutoChamadaoParaRecolhimento(Long idProdutoEdicao, Usuario usuario);
    
	void gerarMovimentoEstoqueDeExpedicao(Date dataPrevista, Date dataDistribuidor, Long idProduto,Long idProdutoEdicao, Long idLancamento, Long idUsuario, Date dataOperacao, TipoMovimentoEstoque tipoMovimento, TipoMovimentoEstoque tipoMovimentoCota,TipoMovimentoEstoque tipoMovimentoJuramentado);
	
	MovimentoEstoque gerarMovimentoEstoque(Long idItemRecebimentoFisico, Long idProdutoEdicao,Long idUsuario,BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);
	
    MovimentoEstoque gerarMovimentoEstoque(final Long idProdutoEdicao, final Long idUsuario, final BigInteger quantidade, final TipoMovimentoEstoque tipoMovimentoEstoque, boolean enfileiraAlteracaoEstoqueProduto, Cota cota);

	
	MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, Date dataOperacao);

	MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, Date dataMovimento, Date dataOperacao,Long idLancamento,Long idEestudoCota);

	List<MovimentoEstoqueCota> enviarSuplementarCotaAusente(Date data, Long idCota,List<MovimentoEstoqueCota> listaMovimentoCota) throws TipoMovimentoEstoqueInexistenteException;

	public void excluirRegistroMovimentoEstoqueDeEncalhe(Cota cota, MovimentoEstoque movimentoEstoque);
	
	public void atualizarMovimentoEstoqueDeEncalhe(
			Cota cota,
			MovimentoEstoque movimentoEstoque, 
			BigInteger newQtdeMovEstoque);


	Long atualizarEstoqueProduto(TipoMovimentoEstoque tipoMovimentoEstoque,
							 	 MovimentoEstoque movimentoEstoque);
	
	void atualizarEstoqueProdutoDaFilaCota(Integer numeroCota);
	
	Long atualizarEstoqueProdutoCota(TipoMovimentoEstoque tipoMovimentoEstoque,
								 	 MovimentoEstoqueCota movimentoEstoqueCota);
	
	MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque);

	void processarRegistroHistoricoVenda(HistoricoVendaInput vendaInput, Date dataOperacao);
	
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
	
	void gerarMovimentoEstoqueFuroPublicacao(Lancamento lancamento, FuroProduto furoProduto, Long idUsuario);
	
	MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque,Origem origem);
	
	public BigInteger obterReparteDistribuidoProduto(String codigoProduto);

	MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao,
			Long idUsuario, BigInteger quantidade,
			TipoMovimentoEstoque tipoMovimentoEstoque, Date dataOperacao,
			boolean isImportacao);
	
	MovimentoEstoque gerarMovimentoEstoqueDiferenca(Long idProdutoEdicao, Long idUsuario, 
													   BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, 
													   boolean isMovimentoDiferencaAutomatico, 
													   boolean validarTransfEstoqueDiferenca,
													   Date dataLancamento, StatusIntegracao statusIntegracao);
	
	 MovimentoEstoqueCota gerarMovimentoCotaDiferenca(Date dataLancamento,Long idProdutoEdicao, 
														Long idCota, Long idUsuario, 
														BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, 
														Long idEstudoCota,
														boolean isMovimentoDiferencaAutomatico);

    /**
	 * Obtem Objeto com Lista de movimentos de estoque referentes à reparte e Map de edicoes com saidas e entradas diversas
	 * @param listaMovimentoCota
	 * @return MovimentosEstoqueCotaSaldoDTO
	 */
	MovimentosEstoqueCotaSaldoDTO getMovimentosEstoqueCotaSaldo(List<MovimentoEstoqueCota> listaMovimentoCota);

	MovimentoEstoque gerarMovimentoEstoqueDiferenca(Long idProdutoEdicao,
													Long idUsuario, BigInteger quantidade,
													TipoMovimentoEstoque tipoMovimentoEstoque,
													boolean isMovimentoDiferencaAutomatica,
													boolean validarTransfEstoqueDiferenca, Date dataLancamento,
													StatusIntegracao statusIntegracao, Origem origem);
	
	MovimentoEstoqueCotaDTO criarMovimentoExpedicaoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, 
			Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, 
			Date dataMovimento, Date dataOperacao, Long idLancamento, Long idEstudoCota, Map<String, DescontoDTO> descontos, boolean isMovimentoDiferencaAutomatico);

	
	
	MovimentoEstoque obterMovimentoEstoqueDoItemNotaFiscal(Long idItemNotaFiscal, TipoMovimentoEstoque tipoMovimento);

	EstoqueProdutoCotaJuramentado atualizarEstoqueProdutoCotaJuramentado(MovimentoEstoqueCota movimentoEstoqueCota,
			TipoMovimentoEstoque tipoMovimentoEstoque);
	
	public List<Long> obterMovimentosRepartePromocionalSemEstornoRecebimentoFisico(
			Long idProdutoEdicao, 
			GrupoMovimentoEstoque grupoMovimentoEstoqueRepartePromocional,
			GrupoMovimentoEstoque grupoMovimentoEstoqueEstornoRecebimentoFisico);
	
}
