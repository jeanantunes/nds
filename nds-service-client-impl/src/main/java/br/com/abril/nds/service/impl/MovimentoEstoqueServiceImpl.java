package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
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
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO);

		TipoMovimentoEstoque tipoMovimentoCota =
			tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);

		BigInteger total = BigInteger.ZERO;

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas = lancamentoRepository.buscarMovimentosEstoqueCotaParaFuro(lancamento, tipoMovimentoCota);

		MovimentoEstoqueCota movimento = null;

		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCotas) {

			movimento = (MovimentoEstoqueCota) movimentoEstoqueCota.clone();

			movimento = 
				gerarMovimentoCota(
					null, idProdutoEdicao, movimento.getCota().getId(), idUsuario, 
						movimento.getQtde(), tipoMovimentoCota, new Date(), null, lancamento.getId(), null);

			total = total.add(movimento.getQtde());

			movimentoEstoqueCota.setMovimentoEstoqueCotaFuro(movimento);
			
			movimentoEstoqueCotaRepository.alterar(movimentoEstoqueCota);

		}

		gerarMovimentoEstoque(null, idProdutoEdicao, idUsuario, total, tipoMovimento);

	}

	@Override
	@Transactional
	public void gerarMovimentoEstoqueDeExpedicao(Date dataPrevista,Date dataDistribuidor, Long idProdutoEdicao, Long idLancamento, Long idUsuario,Date dataOperacao, TipoMovimentoEstoque tipoMovimento, TipoMovimentoEstoque tipoMovimentoCota) {
		
		List<EstudoCotaDTO> listaEstudoCota = estudoCotaRepository.
				obterEstudoCotaPorDataProdutoEdicao(dataPrevista, idProdutoEdicao);

		BigInteger total = BigInteger.ZERO;		

		for( EstudoCotaDTO estudoCota:listaEstudoCota ) {
				gerarMovimentoCota(dataPrevista,idProdutoEdicao,estudoCota.getIdCota(),
						idUsuario, estudoCota.getQtdeEfetiva(),tipoMovimentoCota, dataDistribuidor,dataOperacao, idLancamento, estudoCota.getId());

			total = total.add(estudoCota.getQtdeEfetiva());
			
		}

		gerarMovimentoEstoque(dataPrevista, idProdutoEdicao, idUsuario, total, tipoMovimento);

	}

	@Override
	@Transactional
	public void enviarSuplementarCotaAusente(Date data, Long idCota,List<MovimentoEstoqueCota> listaMovimentoCota) throws TipoMovimentoEstoqueInexistenteException{

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


		for(MovimentoEstoqueCota movimentoCota : listaMovimentoCota){

			if(movimentoCota.getData() != null &&  movimentoCota.getProdutoEdicao()!=null
					&& movimentoCota.getUsuario() != null
					&&  movimentoCota.getQtde() != null ){

				gerarMovimentoEstoque(movimentoCota.getData(), movimentoCota.getProdutoEdicao().getId(), movimentoCota.getUsuario().getId(), movimentoCota.getQtde(), tipoMovimento);

				gerarMovimentoCota(movimentoCota.getData(), movimentoCota.getProdutoEdicao().getId(), movimentoCota.getCota().getId(),movimentoCota.getUsuario().getId(), movimentoCota.getQtde(), tipoMovimentoCota);

			}
		}
	}

	public MovimentoEstoque gerarMovimentoEstoqueJuramentado(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null);

		return movimentoEstoque;
	}

	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(dataLancamento, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null);

		return movimentoEstoque;
	}

	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque,Origem origem) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque, origem);

		return movimentoEstoque;
	}

	@Override
	@Transactional
	public MovimentoEstoque gerarMovimentoEstoque(Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque) {

		MovimentoEstoque movimentoEstoque = this.criarMovimentoEstoque(null, idProdutoEdicao, idUsuario, quantidade, tipoMovimentoEstoque,null);

		return movimentoEstoque;
	}

	private MovimentoEstoque criarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao, Long idUsuario, BigInteger quantidade,TipoMovimentoEstoque tipoMovimentoEstoque, Origem origem){

		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();

		if(dataLancamento!= null){
			Long idItemRecebimentoFisico =
				itemRecebimentoFisicoRepository.obterItemPorDataLancamentoIdProdutoEdicao(dataLancamento, idProdutoEdicao);

			movimentoEstoque.setItemRecebimentoFisico(new ItemRecebimentoFisico(idItemRecebimentoFisico));
		}

		movimentoEstoque.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
		movimentoEstoque.setData(new Date());
		movimentoEstoque.setUsuario(new Usuario(idUsuario));
		movimentoEstoque.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoque.setQtde(quantidade);
		movimentoEstoque.setOrigem(origem);
		movimentoEstoque.setAprovadoAutomaticamente(tipoMovimentoEstoque.isAprovacaoAutomatica());

		if (tipoMovimentoEstoque.isAprovacaoAutomatica()) {
		
			movimentoEstoque.setStatus(StatusAprovacao.APROVADO);
			movimentoEstoque.setAprovador(new Usuario(idUsuario));
			movimentoEstoque.setDataAprovacao(this.distribuidorService.obter().getDataOperacao());
			
			Long idEstoque = this.atualizarEstoqueProduto(tipoMovimentoEstoque,movimentoEstoque);			
			
			movimentoEstoque.setEstoqueProduto(new EstoqueProduto(idEstoque));
		
		}
		movimentoEstoqueRepository.adicionar(movimentoEstoque);
		
		return movimentoEstoque;
	}

	@Override
	@Transactional
	public Long atualizarEstoqueProduto(TipoMovimentoEstoque tipoMovimentoEstoque,
			 							MovimentoEstoque movimentoEstoque) {

		if (StatusAprovacao.APROVADO.equals(movimentoEstoque.getStatus())) {

			Long idProdutoEdicao = movimentoEstoque.getProdutoEdicao().getId();
			
			EstoqueProduto estoqueProduto = 
				this.estoqueProdutoRespository.buscarEstoqueProdutoPorProdutoEdicao(
					idProdutoEdicao);

			if (estoqueProduto == null) {

				estoqueProduto = new EstoqueProduto();

				estoqueProduto.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));

				estoqueProduto.setQtde(BigInteger.ZERO);
			}
			
			BigInteger novaQuantidade;

			boolean isOperacaoEntrada = 
				OperacaoEstoque.ENTRADA.equals(tipoMovimentoEstoque.getOperacaoEstoque());

			TipoEstoque tipoEstoque = 
				tipoMovimentoEstoque.getGrupoMovimentoEstoque().getTipoEstoque();

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
	
					 BigInteger qtdeFornecedor = estoqueProduto.getQtdeDevolucaoFornecedor() == null ? BigInteger.ZERO : estoqueProduto.getQtdeDevolucaoFornecedor();
	
					 novaQuantidade = isOperacaoEntrada ? qtdeFornecedor.add(movimentoEstoque.getQtde()) :
						 							      qtdeFornecedor.subtract(movimentoEstoque.getQtde());
	
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
					
					
				default:
	
					 throw new ValidacaoException(TipoMensagem.WARNING, "Estoque inválido para a operação.");
			}

			this.validarAlteracaoEstoqueProduto(novaQuantidade);
			
			if (estoqueProduto.getId() == null) {
				
				return this.estoqueProdutoRespository.adicionar(estoqueProduto);
				
			} else {
				
				this.estoqueProdutoRespository.alterar(estoqueProduto);
				
				return estoqueProduto.getId();
			}
		}
		
		return null;
	}
	
	private void validarAlteracaoEstoqueProduto(BigInteger novaQuantidade) {
		
		if (novaQuantidade.compareTo(BigInteger.ZERO) < 0) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Saldo insuficiente para movimentação de estoque.");
		}
	}

	@Override
	@Transactional
	public MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque) {
		return gerarMovimentoCota(dataLancamento, idProdutoEdicao, idCota, idUsuario, quantidade, tipoMovimentoEstoque, new Date(), null,null,null);
	}
	
	@Override
	@Transactional
	public MovimentoEstoqueCota gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigInteger quantidade, TipoMovimentoEstoque tipoMovimentoEstoque, Date dataMovimento, Date dataOperacao, Long idLancamento, Long idEstudoCota) {

		MovimentoEstoqueCota movimentoEstoqueCota = new MovimentoEstoqueCota();

		movimentoEstoqueCota.setTipoMovimento(tipoMovimentoEstoque);
		movimentoEstoqueCota.setCota(new Cota(idCota));
		
		if (dataOperacao == null) {
			
			dataOperacao = distribuidorService.obterDatatOperacaoDistribuidor();
		}
		
		movimentoEstoqueCota.setData(dataOperacao);

		movimentoEstoqueCota.setDataLancamentoOriginal(dataMovimento);
		
		movimentoEstoqueCota.setDataCriacao(new Date());
		movimentoEstoqueCota.setProdutoEdicao(new ProdutoEdicao(idProdutoEdicao));
		movimentoEstoqueCota.setQtde(quantidade);
		movimentoEstoqueCota.setUsuario(new Usuario(idUsuario));
		movimentoEstoqueCota.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);

		if (dataLancamento != null && idProdutoEdicao != null) {
			
			if (idLancamento==null) {
				
				idLancamento = 
					lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
						new ProdutoEdicao(idProdutoEdicao), null, dataLancamento);
			}
			
				
			if (idLancamento != null) {
				
				movimentoEstoqueCota.setLancamento(new Lancamento(idLancamento));
				
				Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
				Cota cota = cotaRepository.buscarPorId(idCota);
				ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
				
				Desconto desconto = descontoService.obterDescontoPorCotaProdutoEdicao(lancamento, cota, produtoEdicao);
								
				BigDecimal precoComDesconto = produtoEdicao.getPrecoVenda()
						.subtract(produtoEdicao.getPrecoVenda()
								.multiply(desconto.getValor()
										.divide(new BigDecimal("100")
										)
									)
								);
				
				ValoresAplicados valoresAplicados = new ValoresAplicados();
				valoresAplicados.setPrecoVenda(produtoEdicao.getPrecoVenda());
				valoresAplicados.setValorDesconto(desconto.getValor());
				valoresAplicados.setPrecoComDesconto(precoComDesconto);
				
				movimentoEstoqueCota.setValoresAplicados(valoresAplicados);
				
			}			
			
			if (idEstudoCota != null) {
				
				movimentoEstoqueCota.setEstudoCota(new EstudoCota(idEstudoCota));
			}
		}
		
		if (tipoMovimentoEstoque.isAprovacaoAutomatica()) {

			movimentoEstoqueCota.setStatus(StatusAprovacao.APROVADO);
			movimentoEstoqueCota.setAprovador(new Usuario(idUsuario));
			movimentoEstoqueCota.setDataAprovacao(dataOperacao);
			
			Long idEstoqueCota = this.atualizarEstoqueProdutoCota(tipoMovimentoEstoque,movimentoEstoqueCota);
			
			movimentoEstoqueCota.setEstoqueProdutoCota(new EstoqueProdutoCota(idEstoqueCota));

		}
			
		movimentoEstoqueCotaRepository.adicionar(movimentoEstoqueCota);
		
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
				
				estoqueProdutoCota.setProdutoEdicao(new ProdutoEdicao(idProdutoEd));
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
			
			this.validarAlteracaoEstoqueProduto(novaQuantidade);

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

			ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);

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
		
		this.validarAlteracaoEstoqueProduto(estoqueProdutoCotaJuramentado.getQtde());

		estoqueProdutoCotaJuramentado.getMovimentos().add(movimentoEstoqueCota);

		if (estoqueProdutoCotaJuramentado.getId() == null) {

			this.estoqueProdutoCotaJuramentadoRepository.adicionar(estoqueProdutoCotaJuramentado);

		} else {

			this.estoqueProdutoCotaJuramentadoRepository.alterar(estoqueProdutoCotaJuramentado);
		}
	}

	@Override
	@Transactional
	public void processarRegistroHistoricoVenda(HistoricoVendaInput vendaInput) {

		Integer reparte = vendaInput.getQtdReparte();
		Integer encalhe = vendaInput.getQtdEncalhe();

		ProdutoEdicao edicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				vendaInput.getCodigoProduto().toString(), vendaInput.getNumeroEdicao().longValue());

		if(edicao == null)
			throw new ImportacaoException("Edição inexistente.");

		Cota cota = cotaRepository.obterPorNumerDaCota(vendaInput.getNumeroCota());

		if(cota == null)
			throw new ImportacaoException("Cota inexistente.");

		Long idUsuario = usuarioRepository.getUsuarioImportacao().getId();

		persistirRegistroVendaHistoricoReparte(idUsuario, reparte, edicao, cota);

		persistirRegistroVendaHistoricoEncalhe(idUsuario, encalhe, edicao, cota);

	}

	/**
	 * Persistem os dados de reparte de histórico de vendas
	 *
	 * @param idUsuario
	 * @param reparte
	 * @param edicao
	 * @param cota
	 */
	private void persistirRegistroVendaHistoricoReparte(Long idUsuario, Integer reparte, ProdutoEdicao edicao, Cota cota){

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

			gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoEnvioReparte);

			gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(reparte), tipoMovimentoRecebimentoReparte);
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
	private void persistirRegistroVendaHistoricoEncalhe(Long idUsuario, Integer encalhe, ProdutoEdicao edicao, Cota cota){

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

			gerarMovimentoEstoque(edicao.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoRecebimentoEncalhe);

			gerarMovimentoCota(null, edicao.getId(), cota.getId(), idUsuario, BigInteger.valueOf(encalhe), tipoMovimentoEnvioEncalhe);
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
					idUsuario, produtoServico.getQuantidade(), tipoMovimento,null);
		}
	}
}
