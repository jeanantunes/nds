package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.MovimentosEstoqueCotaSaldoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaJuramentadoRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;
import br.com.abril.nds.util.MathUtil;

@Service
public class MovimentoEstoqueServiceImpl implements MovimentoEstoqueService {

	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;

	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;

	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;

	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DescontoService descontoService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private EstoqueProdutoCotaJuramentadoRepository estoqueProdutoCotaJuramentadoRepository;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Override
	@Transactional
	public void gerarMovimentoEstoqueFuroPublicacao(Lancamento lancamento, Long idUsuario) {

		Long idProdutoEdicao = lancamento.getProdutoEdicao().getId();

		TipoMovimentoEstoque tipoMovimento =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);

		TipoMovimentoEstoque tipoMovimentoCota =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);
		
		TipoMovimentoEstoque tipoMovimentoEstCotaAusente =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);

		BigInteger total = BigInteger.ZERO;

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas = 
			lancamentoRepository.buscarMovimentosEstoqueCotaParaFuro(
				lancamento, tipoMovimentoCota);

		MovimentoEstoqueCota movimento = null;

		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCotas) {
			
			movimento = (MovimentoEstoqueCota) movimentoEstoqueCota.clone();

			movimento = 
				gerarMovimentoCota(
					null, idProdutoEdicao, movimento.getCota().getId(), idUsuario, 
						movimento.getQtde(), tipoMovimentoCota, new Date(), null, lancamento.getId(), null);

			if (movimentoEstoqueCota.getTipoMovimento() != tipoMovimentoEstCotaAusente){
			
				total = total.add(movimento.getQtde());
			} else {
				
				total = total.subtract(movimento.getQtde());
			}

			movimentoEstoqueCota.setMovimentoEstoqueCotaFuro(movimento);
			
			movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);

		}

		gerarMovimentoEstoque(null, idProdutoEdicao, idUsuario, total, tipoMovimento);

	}

	@Override
	@Transactional
	public void gerarMovimentoEstoqueDeExpedicao(Date dataPrevista,Date dataDistribuidor, Long idProdutoEdicao, Long idLancamento
			, Long idUsuario, Date dataOperacao, TipoMovimentoEstoque tipoMovimento, TipoMovimentoEstoque tipoMovimentoCota) {
		
		List<EstudoCotaDTO> listaEstudoCota = estudoCotaRepository.
			obterEstudoCotaPorDataProdutoEdicao(dataPrevista, idProdutoEdicao);
		
		BigInteger total = BigInteger.ZERO;		

		Map<String, DescontoDTO> descontos = descontoService.obterDescontosPorLancamentoProdutoEdicaoMap(idLancamento, idProdutoEdicao);
		
		List<MovimentoEstoqueCota> movimentosEstoqueCota = new ArrayList<>();
		for (EstudoCotaDTO estudoCota : listaEstudoCota) {

			MovimentoEstoqueCota mec = criarMovimentoExpedicaoCota(
				dataPrevista, idProdutoEdicao, estudoCota.getIdCota(),
					idUsuario, estudoCota.getQtdeEfetiva(),tipoMovimentoCota,
						dataDistribuidor,dataOperacao, idLancamento, estudoCota.getId(), descontos, false);
			
			total = total.add(estudoCota.getQtdeEfetiva());
			
			movimentosEstoqueCota.add(mec);
		}

		gerarMovimentoEstoque(idProdutoEdicao, idUsuario, total, tipoMovimento, dataDistribuidor, false);
		
		int count = 0;
		for(MovimentoEstoqueCota mec : movimentosEstoqueCota) {
			movimentoEstoqueCotaRepository.merge(mec);
			
			if ( ++count % 20 == 0 ) {
				movimentoEstoqueCotaRepository.flush();
				movimentoEstoqueCotaRepository.clear();
			}
			
		}
		
		movimentoEstoqueCotaRepository.flush();
		movimentoEstoqueCotaRepository.clear();
		
	}

	/**
	 * Obtem Objeto com Lista de movimentos de estoque referentes à reparte e Map de edicoes com saidas e entradas diversas
	 * @param listaMovimentoCota
	 * @return MovimentosEstoqueCotaSaldoDTO
	 */
	@Override
	public MovimentosEstoqueCotaSaldoDTO getMovimentosEstoqueCotaSaldo(List<MovimentoEstoqueCota> listaMovimentoCota){
		
		List<MovimentoEstoqueCota> listaMovimentosEstoqueCotaReparte = new ArrayList<MovimentoEstoqueCota>();
		
		Map<Long,BigInteger> produtoEdicaoQtdSaida = new HashMap<Long, BigInteger>();
		
		Map<Long,BigInteger> produtoEdicaoQtdEntrada = new HashMap<Long, BigInteger>();
		
		for (MovimentoEstoqueCota movimentoCota : listaMovimentoCota) {
			
			if (((TipoMovimentoEstoque) movimentoCota.getTipoMovimento())
					.getGrupoMovimentoEstoque().equals(
							GrupoMovimentoEstoque.RECEBIMENTO_REPARTE)) {
				
				listaMovimentosEstoqueCotaReparte.add(movimentoCota);
			}
			else{
			
				BigInteger qtdProduto = BigInteger.ZERO;
				
				if (((TipoMovimentoEstoque) movimentoCota.getTipoMovimento())
						.getGrupoMovimentoEstoque().getOperacaoEstoque().equals(OperacaoEstoque.SAIDA)) {
					
					qtdProduto = produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId())!=null?
				                 produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId()):
					             BigInteger.ZERO;
					
					qtdProduto = movimentoCota.getQtde().add(qtdProduto);
					
					produtoEdicaoQtdSaida.put(movimentoCota.getProdutoEdicao().getId(), qtdProduto);
				}
				
				if (((TipoMovimentoEstoque) movimentoCota.getTipoMovimento())
						.getGrupoMovimentoEstoque().getOperacaoEstoque().equals(OperacaoEstoque.ENTRADA)) {
					
					qtdProduto = produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId())!=null?
								 produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId()):
					             BigInteger.ZERO;
	
	                qtdProduto = movimentoCota.getQtde().add(qtdProduto);
					
					produtoEdicaoQtdEntrada.put(movimentoCota.getProdutoEdicao().getId(), qtdProduto);
				}
			}
		}
		
		return new MovimentosEstoqueCotaSaldoDTO(listaMovimentosEstoqueCotaReparte, produtoEdicaoQtdSaida, produtoEdicaoQtdEntrada);
	}
	
	@Override
	@Transactional
	public List<MovimentoEstoqueCota> enviarSuplementarCotaAusente(Date data,
																  Long idCota,
																  List<MovimentoEstoqueCota> listaMovimentoCota) 
																  throws TipoMovimentoEstoqueInexistenteException {

		Cota cota = cotaRepository.buscarPorId(idCota);

		if(listaMovimentoCota==null || listaMovimentoCota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota '" +cota.getNumeroCota()+ "' não possui reparte na data.");
		}

		TipoMovimentoEstoque tipoMovimento =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);

		TipoMovimentoEstoque tipoMovimentoCota =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);

		if ( tipoMovimento == null ) {
		 	throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		}

		if ( tipoMovimentoCota == null ) {
			throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		}
		
		List<MovimentoEstoqueCota> listaMovimentoCotaEnvio = 
			new ArrayList<MovimentoEstoqueCota>();

	    MovimentosEstoqueCotaSaldoDTO movimentosSaldo = this.getMovimentosEstoqueCotaSaldo(listaMovimentoCota);

		listaMovimentoCotaEnvio = this.estornarMovimentosDaCotaAusente(movimentosSaldo.getMovimentosEstoqueCota(), 
											                           data, 
											                           tipoMovimento, 
											                           tipoMovimentoCota, 
											                           movimentosSaldo.getProdutoEdicaoQtdSaida(), 
											                           movimentosSaldo.getProdutoEdicaoQtdEntrada());
		
		return listaMovimentoCotaEnvio;
	}
	
	private List<MovimentoEstoqueCota> estornarMovimentosDaCotaAusente(List<MovimentoEstoqueCota> listaMovimentosEstoqueCotaReparte, 
			                                                           Date data, 
			                                                           TipoMovimentoEstoque tipoMovimento, 
			                                                           TipoMovimentoEstoque tipoMovimentoCota,
			                                                           Map<Long,BigInteger> produtoEdicaoQtdSaida,
			                                                           Map<Long,BigInteger> produtoEdicaoQtdEntrada){
		
		List<MovimentoEstoqueCota> listaMovimentoCotaEnvio = new ArrayList<MovimentoEstoqueCota>();
		
		for (MovimentoEstoqueCota movimentoCota : listaMovimentosEstoqueCotaReparte) {

			if (movimentoCota.getData() != null 
				&& movimentoCota.getProdutoEdicao() != null
				&& movimentoCota.getUsuario() != null
				&& movimentoCota.getQtde() != null ) {
				
				
				BigInteger quantidadeSaidas = produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId())!=null?
						                      produtoEdicaoQtdSaida.get(movimentoCota.getProdutoEdicao().getId()):
							                  BigInteger.ZERO;
				
				BigInteger quantidadeEntradas = produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId())!=null?
						                        produtoEdicaoQtdEntrada.get(movimentoCota.getProdutoEdicao().getId()):
							                    BigInteger.ZERO;
				
				BigInteger saldoProduto = quantidadeEntradas.subtract(quantidadeSaidas);

				BigInteger quantidade = movimentoCota.getQtde().add(saldoProduto);

				gerarMovimentoEstoque(data, 
						              movimentoCota.getProdutoEdicao().getId(),
						              movimentoCota.getUsuario().getId(), 
						              quantidade, 
							          tipoMovimento);

				listaMovimentoCotaEnvio.add(gerarMovimentoCota(data, 
						                                       movimentoCota.getProdutoEdicao().getId(), 
							                                   movimentoCota.getCota().getId(), 
							                                   movimentoCota.getUsuario().getId(),
							                                   quantidade, 
								                               tipoMovimentoCota, 
								                               data, 
								                               null, 
								                               movimentoCota.getLancamento()!=null?movimentoCota.getLancamento().getId():null, 
								                               null));
			}		
		}

		return listaMovimentoCotaEnvio;
	}

	public MovimentoEstoque gerarMovimentoEstoqueJuramentado(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false,false, true, null);

		return movimentoEstoque;
	}

	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(dataLancamento, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false,false, true, null);

		return movimentoEstoque;
	}

	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque,Origem origem) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, origem, null, false,false, true, null);

		return movimentoEstoque;
	}

	@Override
	public MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, null, false,false, true, null);

		return movimentoEstoque;
	}

	
	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, Date dataOperacao, boolean isImportacao) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null, dataOperacao, isImportacao,false, true, null);

		return movimentoEstoque;
	}
	
	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoqueDiferenca(Long idProdutoEdicao, Long idUsuario, 
														   BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, 
														   boolean isMovimentoDiferencaAutomatica,
														   boolean validarTransfEstoqueDiferenca,
														   Date dataLancamento, StatusIntegracao statusIntegracao, Origem origem) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, 
																		tipoMovimentoEstoque, origem, null, 
																		false, isMovimentoDiferencaAutomatica, validarTransfEstoqueDiferenca, statusIntegracao);
		return movimentoEstoque;
	}
	
	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoqueDiferenca(Long idProdutoEdicao, Long idUsuario, 
														   BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, 
														   boolean isMovimentoDiferencaAutomatica,
														   boolean validarTransfEstoqueDiferenca,
														   Date dataLancamento, StatusIntegracao statusIntegracao) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, 
																		tipoMovimentoEstoque, null, null, 
																		false, isMovimentoDiferencaAutomatica, validarTransfEstoqueDiferenca, statusIntegracao);
		return movimentoEstoque;
	}

	private MovimentoEstoque criarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, 
												   BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, 
												   Origem origem, Date dataOperacao, boolean isImportacao,
												   boolean isMovimentoDiferencaAutomatica, boolean validarTransfEstoqueDiferenca, 
												   StatusIntegracao statusIntegracao){

		this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.DISTRIBUIDOR);
		
		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		
		if (dataOperacao == null) {
			
			dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		}
		
		if (dataLancamento!= null) {
			
			Long idItemRecebimentoFisico =
				this.itemRecebimentoFisicoRepository.obterItemPorDataLancamentoIdProdutoEdicao(dataLancamento, idProdutoEdicao);

			if (idItemRecebimentoFisico != null) {
			
				movimentoEstoque.setItemRecebimentoFisico(new ItemRecebimentoFisico(idItemRecebimentoFisico));
			}
		}

		movimentoEstoque.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));

		movimentoEstoque.setData(dataOperacao);
		movimentoEstoque.setDataCriacao(dataOperacao);
		movimentoEstoque.setUsuario(new Usuario(idUsuario));
		movimentoEstoque.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoque.setQtde(quantidade);
		movimentoEstoque.setOrigem(origem);
		movimentoEstoque.setAprovadoAutomaticamente(tipoMovimentoEstoque.isAprovacaoAutomatica());

		if (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatica) {
		
			movimentoEstoque.setStatus(StatusAprovacao.APROVADO);
			movimentoEstoque.setAprovador(new Usuario(idUsuario));
			movimentoEstoque.setDataAprovacao(this.distribuidorService.obterDataOperacaoDistribuidor());
			
			Long idEstoque = this.atualizarEstoqueProduto(tipoMovimentoEstoque, movimentoEstoque, isImportacao, validarTransfEstoqueDiferenca);			
			
			movimentoEstoque.setEstoqueProduto(new EstoqueProduto(idEstoque));
		
		}
		
		if (statusIntegracao != null) {
			
			movimentoEstoque.setStatusIntegracao(statusIntegracao);
		}
		
		movimentoEstoque = movimentoEstoqueRepository.merge(movimentoEstoque);
		
		return movimentoEstoque;
	}

	@Override
	@Transactional
	public Long atualizarEstoqueProduto(TipoMovimentoEstoque tipoMovimentoEstoque,
			 							MovimentoEstoque movimentoEstoque) {
		return this.atualizarEstoqueProduto(tipoMovimentoEstoque, movimentoEstoque, false, true);
	}

	@Override
	@Transactional
	public Long atualizarEstoqueProduto(TipoMovimentoEstoque tipoMovimentoEstoque,
			 							MovimentoEstoque movimentoEstoque, boolean isImportacao,
			 							boolean validarTransfEstoqueDiferenca) {

		if (StatusAprovacao.APROVADO.equals(movimentoEstoque.getStatus())) {
			
			Long idProdutoEdicao = movimentoEstoque.getProdutoEdicao().getId();
			
			EstoqueProduto estoqueProduto = 
				this.estoqueProdutoRespository.buscarEstoqueProdutoPorProdutoEdicao(
					idProdutoEdicao);

			if (estoqueProduto == null) {

				estoqueProduto = new EstoqueProduto();
				
				ProdutoEdicao produtoEdicao = 
					this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
				
				estoqueProduto.setProdutoEdicao(produtoEdicao);

				estoqueProduto.setQtde(BigInteger.ZERO);
			}
			
			TipoEstoque tipoEstoque = 
				tipoMovimentoEstoque.getGrupoMovimentoEstoque().getTipoEstoque();
			
			if (TipoEstoque.COTA.equals(tipoEstoque)) {
				
				return estoqueProduto.getId();
			}
			
			BigInteger novaQuantidade;
			
			BigInteger novaQuantidadeSomatorioEstoque = BigInteger.ZERO;

			boolean isOperacaoEntrada = 
				OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque());

			switch (tipoEstoque) {

				case LANCAMENTO:
	
					 novaQuantidade = isOperacaoEntrada ? estoqueProduto.getQtde().add(movimentoEstoque.getQtde()) :
						 								  estoqueProduto.getQtde().subtract(movimentoEstoque.getQtde());
					 
					 estoqueProduto.setQtde(novaQuantidade);
	
					 break;
	
				case PRODUTOS_DANIFICADOS:
	
					 BigInteger qtdeDanificado = estoqueProduto.getQtdeDanificado() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDanificado();
	
					 novaQuantidade = isOperacaoEntrada ? qtdeDanificado.add(movimentoEstoque.getQtde()) :
						 							      qtdeDanificado.subtract(movimentoEstoque.getQtde());
					 
					 estoqueProduto.setQtdeDanificado(novaQuantidade);
	
					 break;
	
				case DEVOLUCAO_ENCALHE:
	
					 BigInteger qtdeEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
	
					 novaQuantidade = isOperacaoEntrada ? qtdeEncalhe.add(movimentoEstoque.getQtde()) :
						 								  qtdeEncalhe.subtract(movimentoEstoque.getQtde());
	
					 estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
	
					 break;
	
				case DEVOLUCAO_FORNECEDOR:
	
					 BigInteger qtdeDevolucaoFornecedor = estoqueProduto.getQtdeDevolucaoFornecedor() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoFornecedor();
	
					 novaQuantidade = qtdeDevolucaoFornecedor.add(movimentoEstoque.getQtde());
					 
					 BigInteger _qtdeLancamento = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
					 BigInteger _qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
					 BigInteger _qtdeDevolucaoEncalhe = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
	
					 novaQuantidadeSomatorioEstoque = _qtdeLancamento.add(_qtdeSuplementar).add(_qtdeDevolucaoEncalhe);
					 novaQuantidadeSomatorioEstoque = novaQuantidadeSomatorioEstoque.subtract(movimentoEstoque.getQtde());
					 
					 BigInteger totalValorSubtrairDoEstoque = movimentoEstoque.getQtde();
					 BigInteger valorSubtrairDoEstoque = BigInteger.ZERO;
					 
					 valorSubtrairDoEstoque = subtrairDoEstoque(_qtdeLancamento, totalValorSubtrairDoEstoque);
					 _qtdeLancamento = _qtdeLancamento.subtract(valorSubtrairDoEstoque);
					 totalValorSubtrairDoEstoque = totalValorSubtrairDoEstoque.subtract(valorSubtrairDoEstoque);
					 
					 valorSubtrairDoEstoque = subtrairDoEstoque(_qtdeSuplementar, totalValorSubtrairDoEstoque);
					 _qtdeSuplementar = _qtdeSuplementar.subtract(valorSubtrairDoEstoque);
					 totalValorSubtrairDoEstoque = totalValorSubtrairDoEstoque.subtract(valorSubtrairDoEstoque);
					 
					 valorSubtrairDoEstoque = subtrairDoEstoque(_qtdeDevolucaoEncalhe, totalValorSubtrairDoEstoque);
					 _qtdeDevolucaoEncalhe = _qtdeDevolucaoEncalhe.subtract(valorSubtrairDoEstoque);
					 
					 estoqueProduto.setQtde(_qtdeLancamento);
					 estoqueProduto.setQtdeSuplementar(_qtdeSuplementar);
					 estoqueProduto.setQtdeDevolucaoEncalhe(_qtdeDevolucaoEncalhe);
					 estoqueProduto.setQtdeDevolucaoFornecedor(novaQuantidade);
	
					 break;
	
				case SUPLEMENTAR:
	
					 BigInteger qtdeSuplementar = estoqueProduto.getQtdeSuplementar() == null ? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
	
					 novaQuantidade = isOperacaoEntrada ? qtdeSuplementar.add(movimentoEstoque.getQtde()) :
						 								  qtdeSuplementar.subtract(movimentoEstoque.getQtde());
	
					 estoqueProduto.setQtdeSuplementar(novaQuantidade);
	
					 break;
	
				case RECOLHIMENTO:
	
					BigInteger qtdeRecolhimento = estoqueProduto.getQtdeDevolucaoEncalhe() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoEncalhe();
	
					novaQuantidade = isOperacaoEntrada ? qtdeRecolhimento.add(movimentoEstoque.getQtde()) :
														 qtdeRecolhimento.subtract(movimentoEstoque.getQtde());
	
					estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
	
					break;
	
				case JURAMENTADO:
					
					BigInteger qtde = estoqueProduto.getQtde() == null ? BigInteger.ZERO : estoqueProduto.getQtde();
	
					novaQuantidade = isOperacaoEntrada ? 	qtde.add(movimentoEstoque.getQtde()) :
															qtde.subtract(movimentoEstoque.getQtde());
	
					estoqueProduto.setQtde(novaQuantidade);
	
					break;
					
				case PERDA:
					
					BigInteger qtdePerda = estoqueProduto.getQtdePerda() == null ? BigInteger.ZERO : estoqueProduto.getQtdePerda();
					
					if (movimentoEstoque.getOrigem()!=null && movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)){
						
						if (estoqueProduto.getQtdeDevolucaoEncalhe()==null){
							
							estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
						}

					    novaQuantidade = estoqueProduto.getQtdeDevolucaoEncalhe().subtract(movimentoEstoque.getQtde());
					    
					    estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
					}
					else{
						
						if (estoqueProduto.getQtde()==null){
								
							estoqueProduto.setQtde(BigInteger.ZERO);
						}
	
						novaQuantidade = estoqueProduto.getQtde().subtract(movimentoEstoque.getQtde());
						
						estoqueProduto.setQtde(novaQuantidade);
					}
					
					qtdePerda = qtdePerda.add( movimentoEstoque.getQtde());
					
					estoqueProduto.setQtdePerda(qtdePerda);
					
					break;
					 
				case GANHO:

					BigInteger qtdeGanho = estoqueProduto.getQtdeGanho() == null ? BigInteger.ZERO : estoqueProduto.getQtdeGanho();
					
					if (movimentoEstoque.getOrigem()!=null && movimentoEstoque.getOrigem().equals(Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE)){
						
						if (estoqueProduto.getQtdeDevolucaoEncalhe()==null){
							
							estoqueProduto.setQtdeDevolucaoEncalhe(BigInteger.ZERO);
						}
						
						novaQuantidade = estoqueProduto.getQtdeDevolucaoEncalhe().add(movimentoEstoque.getQtde());
					    
					    estoqueProduto.setQtdeDevolucaoEncalhe(novaQuantidade);
					}
					else{
						
                        if (estoqueProduto.getQtde()==null){
							
							estoqueProduto.setQtde(BigInteger.ZERO);
						}
	
						novaQuantidade = estoqueProduto.getQtde().add(movimentoEstoque.getQtde());	
						
						estoqueProduto.setQtde(novaQuantidade);
					}
					
					qtdeGanho = qtdeGanho.add(movimentoEstoque.getQtde());
					
					estoqueProduto.setQtdeGanho(qtdeGanho);
					
					break;
					
				default:
	
					 throw new ValidacaoException(TipoMensagem.WARNING, "Estoque inválido para a operação.");
			}

			// Caso seja importação, deve inserir mesmo se o estoque ficar negativo - Definido em conjunto com Cesar Pop Punk
			if (!isImportacao && !TipoEstoque.DEVOLUCAO_FORNECEDOR.equals(tipoEstoque)) {
				this.validarAlteracaoEstoqueProdutoDistribuidor(
					novaQuantidade, tipoEstoque, estoqueProduto.getProdutoEdicao(),
					validarTransfEstoqueDiferenca);
			}
			
			if(!isImportacao && TipoEstoque.DEVOLUCAO_FORNECEDOR.equals(tipoEstoque)) {
				this.validarAlteracaoEstoqueProdutoDistribuidorParaDevolucaoFornecedor(
						novaQuantidadeSomatorioEstoque, estoqueProduto.getProdutoEdicao(), 
						validarTransfEstoqueDiferenca);
			}
			
			if (estoqueProduto.getId() == null) {
				
				return this.estoqueProdutoRespository.adicionar(estoqueProduto);
				
			} else {
				
				this.estoqueProdutoRespository.merge(estoqueProduto);
				
				return estoqueProduto.getId();
			}
		}
		
		return null;
	}

	private BigInteger subtrairDoEstoque(BigInteger qtdeEstoque, BigInteger qtdeSubtrairDoEstoque) {
		
		if( BigInteger.ZERO.compareTo(qtdeEstoque) >= 0 ) {
		
			return BigInteger.ZERO;
		
		}
		
		if(qtdeEstoque.compareTo(qtdeSubtrairDoEstoque) > 0) {
		
			return qtdeSubtrairDoEstoque;
		
		} else {
			
			return qtdeEstoque;
			
		}
		
		
	}

	private void validarAlteracaoEstoqueProdutoDistribuidorParaDevolucaoFornecedor(
			BigInteger saldoEstoque, ProdutoEdicao produtoEdicao, boolean validarTransfEstoqueDiferenca) {

		if (validarTransfEstoqueDiferenca
				&& !this.validarSaldoEstoque(saldoEstoque)) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Saldo do produto ["
							+ produtoEdicao.getProduto().getCodigo() + " - "
							+ produtoEdicao.getProduto().getNomeComercial()
							+ " - " + produtoEdicao.getNumeroEdicao()
							+ "] nos estoques \"Lançamento, Devolução Encalhe e Suplementar\", " +
							"insuficientes para movimentação.");
		}
	}
	
	
	private void validarAlteracaoEstoqueProdutoDistribuidor(BigInteger saldoEstoque, 
															TipoEstoque tipoEstoque,
															ProdutoEdicao produtoEdicao,
															boolean validarTransfEstoqueDiferenca) {
		
		if (validarTransfEstoqueDiferenca && !this.validarSaldoEstoque(saldoEstoque)) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, 
					"Saldo do produto [" + produtoEdicao.getProduto().getCodigo() 
						+ " - " + produtoEdicao.getProduto().getNomeComercial() + " - " 
						+ produtoEdicao.getNumeroEdicao() 
						+ "] no estoque \"" + tipoEstoque.getDescricao() 
						+ "\", insuficiente para movimentação.");
		}
	}
	
	private void validarAlteracaoEstoqueProdutoCota(BigInteger saldoEstoque, 
													ProdutoEdicao produtoEdicao) {
		
		if (!this.validarSaldoEstoque(saldoEstoque)) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, 
					"Saldo do produto [" + produtoEdicao.getProduto().getCodigo() 
						+ " - " + produtoEdicao.getProduto().getNomeComercial() + " - " 
						+ produtoEdicao.getNumeroEdicao() 
						+ "] no estoque da cota, insuficiente para movimentação.");
		}
	}
	
	private boolean validarSaldoEstoque(BigInteger saldoEstoque) {
		
		return (saldoEstoque != null && saldoEstoque.compareTo(BigInteger.ZERO) >= 0);
	}
	
	@Override
	@Transactional
	public MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, 
			Long idProdutoEdicao, Long idCota, Long idUsuario, 
			BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, Date dataOperacao) {
		
		return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, idUsuario, quantidade, tipoMovimentoEstoque, null, dataOperacao, null, null, false);
		
	}
	
	@Override
	@Transactional
	public MovimentoEstoqueCota gerarMovimentoCotaDiferenca(Date dataLancamento,Long idProdutoEdicao, 
															Long idCota, Long idUsuario, 
															BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, 
															Long idEstudoCota,
															boolean isMovimentoDiferencaAutomatico) {
		
		return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, 
				idUsuario, quantidade, tipoMovimentoEstoque, null, null, null, idEstudoCota,isMovimentoDiferencaAutomatico);
	}
	
	
	@Override
	@Transactional
	public MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, 
			Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, 
			Date dataMovimento, Date dataOperacao, Long idLancamento, Long idEstudoCota){
		
		return criarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, idUsuario, quantidade, 
						   		  tipoMovimentoEstoque, dataMovimento, dataOperacao, idLancamento, idEstudoCota, false);
	}

	private MovimentoEstoqueCota criarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, 
			Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, 
			Date dataMovimento, Date dataOperacao, Long idLancamento, Long idEstudoCota,boolean isMovimentoDiferencaAutomatico) {

		this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.COTA);
		
		if (dataOperacao == null) {
			
			dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		}
		
		MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
		
		movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoqueCota.setCota(new Cota(idCota));
		
		movimentoEstoqueCota.setData(dataMovimento==null? dataOperacao : dataMovimento);

		movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
		
		movimentoEstoqueCota.setDataCriacao(dataOperacao);
		movimentoEstoqueCota.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
		movimentoEstoqueCota.setQtde(quantidade);
		movimentoEstoqueCota.setUsuario(new Usuario(idUsuario));
		movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);

		if (idEstudoCota != null) {
			
			movimentoEstoqueCota.setEstudoCota(new EstudoCota(idEstudoCota));
		}
		
		if (dataLancamento != null && idProdutoEdicao != null) {
			
			if (idLancamento==null) {
				
				idLancamento = 
					lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
						new ProdutoEdicao(idProdutoEdicao), null, dataLancamento);
			}
			
				
			if (idLancamento != null) {

				Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
				
				movimentoEstoqueCota.setLancamento(lancamento);
				
				ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);

				Desconto desconto = descontoService.obterDescontoPorCotaProdutoEdicao(lancamento, new Cota(idCota), produtoEdicao);
				
				BigDecimal precoComDesconto = 
						produtoEdicao.getPrecoVenda().subtract(
								MathUtil.calculatePercentageValue(produtoEdicao.getPrecoVenda(), desconto.getValor()));

				ValoresAplicados valoresAplicados = new ValoresAplicados();
				valoresAplicados.setPrecoVenda(produtoEdicao.getPrecoVenda());
				valoresAplicados.setValorDesconto(desconto.getValor());
				valoresAplicados.setPrecoComDesconto(precoComDesconto);
				
				movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
				
			}			
		}
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatico) {

			movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
			movimentoEstoqueCota.setAprovador(new Usuario(idUsuario));
			movimentoEstoqueCota.setDataAprovacao(dataOperacao);
			
			movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
			
			Long idEstoqueCota = this.atualizarEstoqueProdutoCota(tipoMovimentoEstoque, movimentoEstoqueCota);
			
			movimentoEstoqueCota.setEstoqueProdutoCota(new EstoqueProdutoCota(idEstoqueCota));

		} else {
			
			movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
		}
		
		return movimentoEstoqueCota;
	}

	@Override
	@Transactional
	public Long atualizarEstoqueProdutoCota(TipoMovimentoEstoque tipoMovimentoEstoque,
											MovimentoEstoqueCota movimentoEstoqueCota) {

		if (StatusAprovacao.APROVADO.equals(movimentoEstoqueCota.getStatus())) {

			if (tipoMovimentoEstoque.isIncideJuramentado()) {

				this.atualizarEstoqueProdutoCotaJuramentado(movimentoEstoqueCota, tipoMovimentoEstoque);
			}

			Long idCota = movimentoEstoqueCota.getCota().getId();
			Long idProdutoEd = movimentoEstoqueCota.getProdutoEdicao().getId();
			
			EstoqueProdutoCota estoqueProdutoCota =
				this.estoqueProdutoCotaRepository.buscarEstoquePorProdutoECota(
					idProdutoEd, idCota);

			if (estoqueProdutoCota == null) {
				
				estoqueProdutoCota = new EstoqueProdutoCota();
				
				ProdutoEdicao produtoEdicao = 
					this.produtoEdicaoRepository.buscarPorId(idProdutoEd);
				
				estoqueProdutoCota.setProdutoEdicao(produtoEdicao);
				estoqueProdutoCota.setQtdeDevolvida(BigInteger.ZERO);
				estoqueProdutoCota.setQtdeRecebida(BigInteger.ZERO);
				estoqueProdutoCota.setCota(new Cota(idCota));
			}
			
			BigInteger novaQuantidade;

			BigInteger quantidadeRecebida;

			BigInteger quantidadeDevolvida;

			if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {

				quantidadeRecebida = (estoqueProdutoCota.getQtdeRecebida() != null)
									  ? estoqueProdutoCota.getQtdeRecebida() : BigInteger.ZERO;

				novaQuantidade = quantidadeRecebida.add(movimentoEstoqueCota.getQtde());
				
				estoqueProdutoCota.setQtdeRecebida(novaQuantidade);

			} else {

				quantidadeDevolvida = (estoqueProdutoCota.getQtdeDevolvida() != null)
						 			  ? estoqueProdutoCota.getQtdeDevolvida() : BigInteger.ZERO;

				novaQuantidade = quantidadeDevolvida.add(movimentoEstoqueCota.getQtde());
				
				estoqueProdutoCota.setQtdeDevolvida(novaQuantidade);
			}
			
			this.validarAlteracaoEstoqueProdutoCota(
				novaQuantidade, estoqueProdutoCota.getProdutoEdicao());

			if (estoqueProdutoCota.getId() == null) {
				
				return this.estoqueProdutoCotaRepository.adicionar(estoqueProdutoCota);
				
			} else {
				
				this.estoqueProdutoCotaRepository.alterar(estoqueProdutoCota);
				
				return estoqueProdutoCota.getId();
			}
		}
		
		return null;
	}

	/*
	 * Atualiza o estoque do produto da cota juramentado.
	 */
	private void atualizarEstoqueProdutoCotaJuramentado(MovimentoEstoqueCota movimentoEstoqueCota,
														TipoMovimentoEstoque tipoMovimentoEstoque) {

		Long idProdutoEdicao = movimentoEstoqueCota.getProdutoEdicao().getId();
		Long idCota = movimentoEstoqueCota.getCota().getId();

		EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado =
			this.estoqueProdutoCotaJuramentadoRepository.buscarEstoquePorProdutoECotaNaData(
				idProdutoEdicao, idCota, new Date());

		if (estoqueProdutoCotaJuramentado == null) {

			ProdutoEdicao produtoEdicao = 
				this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);

			Cota cota = this.cotaRepository.buscarPorId(idCota);

			estoqueProdutoCotaJuramentado = new EstoqueProdutoCotaJuramentado();
			
			estoqueProdutoCotaJuramentado.setProdutoEdicao(produtoEdicao);
			estoqueProdutoCotaJuramentado.setCota(cota);
			estoqueProdutoCotaJuramentado.setData(new Date());
		}

		BigInteger qtdeAtual =
			(estoqueProdutoCotaJuramentado.getQtde() == null)
				? BigInteger.ZERO : estoqueProdutoCotaJuramentado.getQtde();

		BigInteger qtdeMovimento =
			(movimentoEstoqueCota.getQtde() == null) ? BigInteger.ZERO : movimentoEstoqueCota.getQtde();

		if (OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque())) {

			estoqueProdutoCotaJuramentado.setQtde(qtdeAtual.add(qtdeMovimento));

		} else {

			estoqueProdutoCotaJuramentado.setQtde(qtdeAtual.subtract(qtdeMovimento));
		}
		
		this.validarAlteracaoEstoqueProdutoCota(
			estoqueProdutoCotaJuramentado.getQtde(), 
				estoqueProdutoCotaJuramentado.getProdutoEdicao());

		estoqueProdutoCotaJuramentado.getMovimentos().add(movimentoEstoqueCota);

		if (estoqueProdutoCotaJuramentado.getId() == null) {

			this.estoqueProdutoCotaJuramentadoRepository.adicionar(estoqueProdutoCotaJuramentado);

		} else {

			this.estoqueProdutoCotaJuramentadoRepository.alterar(estoqueProdutoCotaJuramentado);
		}
	}

	@Override
	@Transactional
	public void processarRegistroHistoricoVenda(HistoricoVendaInput vendaInput, Date dataOperacao) {

		Integer reparte = vendaInput.getQtdReparte();
		Integer encalhe = vendaInput.getQtdEncalhe();

		ProdutoEdicao edicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				vendaInput.getCodigoProduto().toString(), vendaInput.getNumeroEdicao().longValue());

		if(edicao == null)
			throw new ImportacaoException("Edição " + vendaInput.getNumeroEdicao()
										+ " inexistente para produto : " + vendaInput.getCodigoProduto().toString());

		Cota cota = cotaRepository.obterPorNumerDaCota(vendaInput.getNumeroCota());

		if(cota == null)
			throw new ImportacaoException("Cota " + vendaInput.getNumeroCota() + " inexistente.");

		Long idUsuario = usuarioRepository.getUsuarioImportacao().getId();
		
		persistirRegistroVendaHistoricoReparte(idUsuario, reparte, edicao, cota, dataOperacao);

		persistirRegistroVendaHistoricoEncalhe(idUsuario, encalhe, edicao, cota, dataOperacao);

	}

	/**
	 * Persistem os dados de reparte de histórico de vendas
	 *
	 * @param idUsuario
	 * @param reparte
	 * @param edicao
	 * @param cota
	 * @param dataOperacao 
	 */
	private void persistirRegistroVendaHistoricoReparte(Long idUsuario, Integer reparte, ProdutoEdicao edicao, Cota cota, Date dataOperacao){

		if(reparte != null && reparte>0) {

			TipoMovimentoEstoque tipoMovimentoEnvioReparte =
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);

			if(tipoMovimentoEnvioReparte == null){

				tipoMovimentoEnvioReparte = new TipoMovimentoEstoque();
				tipoMovimentoEnvioReparte.setAprovacaoAutomatica(true);
				tipoMovimentoEnvioReparte.setDescricao("Envio a Jornaleiro");
				tipoMovimentoEnvioReparte.setIncideDivida(true);
				tipoMovimentoEnvioReparte.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);

				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoEnvioReparte);
			}

			TipoMovimentoEstoque tipoMovimentoRecebimentoReparte =
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);

			if(tipoMovimentoRecebimentoReparte == null){

				tipoMovimentoRecebimentoReparte  = new TipoMovimentoEstoque();
				tipoMovimentoRecebimentoReparte.setAprovacaoAutomatica(true);
				tipoMovimentoRecebimentoReparte.setDescricao("Recebimento Reparte");
				tipoMovimentoRecebimentoReparte.setIncideDivida(true);
				tipoMovimentoRecebimentoReparte.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);

				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoRecebimentoReparte);
			}

			gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoEnvioReparte, dataOperacao, true);

			gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoRecebimentoReparte, dataOperacao);
		}
	}

	/**
	 * Persistem os dados de encalhe de histórico de vendas
	 *
	 * @param idUsuario
	 * @param encalhe
	 * @param edicao
	 * @param cota
	 */
	private void persistirRegistroVendaHistoricoEncalhe(Long idUsuario, Integer encalhe, ProdutoEdicao edicao, Cota cota, 
			Date dataOperacao){

		if(encalhe != null && encalhe>0) {

			TipoMovimentoEstoque tipoMovimentoEnvioEncalhe =
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);

			if(tipoMovimentoEnvioEncalhe == null){

				tipoMovimentoEnvioEncalhe = new TipoMovimentoEstoque();
				tipoMovimentoEnvioEncalhe.setAprovacaoAutomatica(true);
				tipoMovimentoEnvioEncalhe.setDescricao("Envio Encalhe - Estoque");
				tipoMovimentoEnvioEncalhe.setIncideDivida(true);
				tipoMovimentoEnvioEncalhe.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_ENCALHE);

				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoEnvioEncalhe);
			}

			TipoMovimentoEstoque tipoMovimentoRecebimentoEncalhe =
						tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);

			if(tipoMovimentoRecebimentoEncalhe  == null){

				tipoMovimentoRecebimentoEncalhe = new TipoMovimentoEstoque();
				tipoMovimentoRecebimentoEncalhe.setAprovacaoAutomatica(true);
				tipoMovimentoRecebimentoEncalhe.setDescricao("Recebimento Encalhe");
				tipoMovimentoRecebimentoEncalhe.setIncideDivida(true);
				tipoMovimentoRecebimentoEncalhe.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);

				tipoMovimentoEstoqueRepository.adicionar(tipoMovimentoRecebimentoEncalhe);
			}

			gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoRecebimentoEncalhe, dataOperacao, true);

			gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoEnvioEncalhe, dataOperacao);
		}
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.MovimentoEstoqueService#devolverConsignadoNotaCanceladaParaDistribuidor(br.com.abril.nds.model.fiscal.nota.NotaFiscal)
	 */
	@Override
	@Transactional
	public void devolucaoConsignadoNotaCancelada(NotaFiscal notaFiscalCancelada) {

		TipoMovimentoEstoque tipoMovimento = this.tipoMovimentoEstoqueRepository.
				buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_CONSIGNADO);

		gerarMovimentoCancelamentoNotaFiscal(notaFiscalCancelada, tipoMovimento);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.MovimentoEstoqueService#devolucaoRecolhimentoNotaCancelada(br.com.abril.nds.model.fiscal.nota.NotaFiscal)
	 */
	@Override
	public void devolucaoRecolhimentoNotaCancelada(NotaFiscal notaFiscalCancelada) {

		TipoMovimentoEstoque tipoMovimento = this.tipoMovimentoEstoqueRepository.
				buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_ENCALHE);

		gerarMovimentoCancelamentoNotaFiscal(notaFiscalCancelada, tipoMovimento);
	}

	private void gerarMovimentoCancelamentoNotaFiscal(NotaFiscal notaFiscalCancelada,
			TipoMovimentoEstoque tipoMovimento) {
		List<ProdutoServico> listaProdutosServicosNotaCancelada = notaFiscalCancelada.getProdutosServicos();

		Long idUsuario = this.usuarioService.getUsuarioLogado().getId();

		for (ProdutoServico produtoServico : listaProdutosServicosNotaCancelada) {

			this.criarMovimentoEstoque(null,
					produtoServico.getProdutoEdicao().getId(),
					idUsuario, produtoServico.getQuantidade(), tipoMovimento,null, null, false,false, true, null);
		}
	}
	
	@Override
	public BigInteger obterReparteDistribuidoProduto(String produtoEdicaoId){
		return this.movimentoEstoqueRepository.obterReparteDistribuidoProduto(produtoEdicaoId);
	}
	
	private void validarDominioGrupoMovimentoEstoque(TipoMovimentoEstoque tipoMovimentoEstoque, 
													 Dominio dominio) {
		
		if (tipoMovimentoEstoque != null
				&& dominio != null
				&& !dominio.equals(tipoMovimentoEstoque.getGrupoMovimentoEstoque().getDominio())) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Domínio do grupo de movimento de estoque inválido");
		}
	}
	
	@Transactional
	public MovimentoEstoqueCota criarMovimentoExpedicaoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, 
			Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, 
			Date dataMovimento, Date dataOperacao, Long idLancamento, Long idEstudoCota, Map<String, DescontoDTO> descontos, boolean isMovimentoDiferencaAutomatico) {

		this.validarDominioGrupoMovimentoEstoque(tipoMovimentoEstoque, Dominio.COTA);
		
		if (dataOperacao == null) {
			
			dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		}
		
		MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();
		
		movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoqueCota.setCota(new Cota(idCota));
		
		movimentoEstoqueCota.setData(dataMovimento==null? dataOperacao : dataMovimento);

		movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
		
		movimentoEstoqueCota.setDataCriacao(dataOperacao);
		movimentoEstoqueCota.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
		movimentoEstoqueCota.setQtde(quantidade);
		movimentoEstoqueCota.setUsuario(new Usuario(idUsuario));
		movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);

		if (idEstudoCota != null) {
			
			movimentoEstoqueCota.setEstudoCota(new EstudoCota(idEstudoCota));
		}
		
		if (dataLancamento != null && idProdutoEdicao != null) {
			
			if (idLancamento==null) {
				
				idLancamento = 
					lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
						new ProdutoEdicao(idProdutoEdicao), null, dataLancamento);
			}
			
				
			if (idLancamento != null) {

				Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
				
				movimentoEstoqueCota.setLancamento(lancamento);
				
				ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);

				//Desconto desconto = descontoService.obterDescontoPorCotaProdutoEdicao(lancamento, new Cota(idCota), produtoEdicao);
				String key = new StringBuilder()
					.append(idCota)
					.append(produtoEdicao.getProduto().getFornecedor().getId())
					.append(produtoEdicao.getId())
					.append(produtoEdicao.getProduto().getId())
					.toString();
				
				DescontoDTO descontoDTO = descontos.get(key);
				if(descontoDTO == null) {
					key = new StringBuilder()
						.append(idCota)
						.append(produtoEdicao.getProduto().getFornecedor().getId())
						.toString();
				
					descontoDTO = descontos.get(key);
				}
				
				if(descontoDTO == null) {
					key = new StringBuilder()
						.append(produtoEdicao.getProduto().getFornecedor().getId())
						.toString();
				
					descontoDTO = descontos.get(key);
				}
				
				if(descontoDTO == null) {
					key = new StringBuilder()
						.append(produtoEdicao.getId())
						.toString();
				
					descontoDTO = descontos.get(key);
				}
				
				if(descontoDTO == null) {
					key = new StringBuilder()
						.append(produtoEdicao.getProduto().getId())
						.toString();
				
					descontoDTO = descontos.get(key);
				}
				
				BigDecimal desconto = descontoDTO.getValor();				
				
				BigDecimal precoComDesconto = 
						produtoEdicao.getPrecoVenda().subtract(
								MathUtil.calculatePercentageValue(produtoEdicao.getPrecoVenda(), desconto));

				ValoresAplicados valoresAplicados = new ValoresAplicados();
				valoresAplicados.setPrecoVenda(produtoEdicao.getPrecoVenda());
				valoresAplicados.setValorDesconto(desconto);
				valoresAplicados.setPrecoComDesconto(precoComDesconto);
				
				movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
				
			}			
		}
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica() || isMovimentoDiferencaAutomatico) {

			movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
			movimentoEstoqueCota.setAprovador(new Usuario(idUsuario));
			movimentoEstoqueCota.setDataAprovacao(dataOperacao);
			
			//movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
			
			Long idEstoqueCota = this.atualizarEstoqueProdutoCota(tipoMovimentoEstoque, movimentoEstoqueCota);
			
			movimentoEstoqueCota.setEstoqueProdutoCota(new EstoqueProdutoCota(idEstoqueCota));

		} else {
			
			//movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
		}
		
		return movimentoEstoqueCota;
	}

}
