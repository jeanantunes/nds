package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoEdicaoFechadaVO;
import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.dto.ContagemDevolucaoAgregationValuesDTO;
import br.com.abril.nds.dto.ContagemDevolucaoConferenciaCegaDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.OrigemItemMovFechamentoFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemMovFechamentoFiscalDevolucaoFornecedor;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFechamentoFiscalRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;
import br.com.abril.nds.repository.ProcessoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFiscalRepository;
import br.com.abril.nds.service.ContagemDevolucaoService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EdicoesFechadasService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.vo.ValidacaoVO;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@Service
public class ContagemDevolucaoServiceImpl implements ContagemDevolucaoService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private ConferenciaEncalheParcialRepository conferenciaEncalheParcialRepository;

	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private ControleContagemDevolucaoRepository controleContagemDevolucaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ParametroEmissaoNotaFiscalRepository parametroEmissaoNotaFiscalRepository;
	
	@Autowired
	private NaturezaOperacaoRepository tipoNotaFiscalRepository;
	
	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private EdicoesFechadasService edicoesFechadasService;
	
	@Autowired
	private ChamadaEncalheFornecedorRepository chamadaEncalheFornecedorRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private MovimentoFechamentoFiscalRepository movimentoFechamentoFiscalRepository;
	
	@Autowired
	private TipoMovimentoFiscalRepository tipoMovimentoFiscalRepository;
	
	@Transactional
	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado) {

		InfoContagemDevolucaoDTO info = new InfoContagemDevolucaoDTO();
		
		this.tratarFiltroPesquisaFornecedores(filtroPesquisa);
		
		ContagemDevolucaoAgregationValuesDTO contagemDevolucaoAgregationValues = 
				movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(filtroPesquisa);

		info.setQtdTotalRegistro(contagemDevolucaoAgregationValues.getQuantidadeTotal().intValue());
		info.setValorTotalGeral(contagemDevolucaoAgregationValues.getValorTotalGeral());
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				filtroPesquisa,	indPerfilUsuarioEncarregado);
		
		info.setListaContagemDevolucao(listaContagemDevolucao);
		
		if(indPerfilUsuarioEncarregado) {
			carregarDadosAdicionais(info, listaContagemDevolucao);
		}
		
		return info;	
	}
	
	private void tratarFiltroPesquisaFornecedores(final FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa){
		
		if(filtroPesquisa.getIdFornecedor() == null){
			
			filtroPesquisa.setFornecedores(this.obterIdsFornecedoresNaoUnificados());
		
		} else{
			
			filtroPesquisa.setFornecedores(Lists.newArrayList(filtroPesquisa.getIdFornecedor()));
		}
		
		if (filtroPesquisa.getFornecedores() == null || filtroPesquisa.getFornecedores().isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro foi encontrado.");
		}
	}
	
	private List<Long> obterIdsFornecedoresNaoUnificados(){
		
		List<Fornecedor> forncedores = fornecedorService.obterFornecedoresNaoUnificados();
		
		Collection<Long> ids = Collections2.transform(forncedores,new Function<Fornecedor, Long>() {
				public Long apply(final Fornecedor item) {
					return item.getId();
				}
			}
		);
		
		return Lists.newArrayList(ids);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<ContagemDevolucaoConferenciaCegaDTO> obterInfoContagemDevolucaoCega(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado) {
		
		this.tratarFiltroPesquisaFornecedores(filtroPesquisa);
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				filtroPesquisa, indPerfilUsuarioEncarregado);
		
		List<ContagemDevolucaoConferenciaCegaDTO> cegaDTOs = new ArrayList<ContagemDevolucaoConferenciaCegaDTO>(listaContagemDevolucao.size());
		for(ContagemDevolucaoDTO contagemDevolucaoDTO : listaContagemDevolucao){
			ContagemDevolucaoConferenciaCegaDTO cegaDTO = new ContagemDevolucaoConferenciaCegaDTO();
			
			cegaDTO.setCodigoProduto(contagemDevolucaoDTO.getCodigoProduto());
			cegaDTO.setIdProdutoEdicao(contagemDevolucaoDTO.getIdProdutoEdicao());
			cegaDTO.setNomeProduto(contagemDevolucaoDTO.getNomeProduto());
			cegaDTO.setNumeroEdicao(contagemDevolucaoDTO.getNumeroEdicao());
			cegaDTO.setPrecoVenda(contagemDevolucaoDTO.getPrecoVenda());
			cegaDTO.setExemplarNota("   ");
			
			cegaDTOs.add(cegaDTO);
			
		}
		
		return cegaDTOs;	
	}
	
	/**
	 * Calcula dados adicionais.
	 * 
	 * @param listaContagemDevolucao
	 */
	private void carregarDadosAdicionais(InfoContagemDevolucaoDTO info, List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			adicionarValores(contagem);
		}
	}


	private void adicionarValores(ContagemDevolucaoDTO contagem) {
		
		BigDecimal precoVenda = (contagem.getPrecoVenda() == null) ? BigDecimal.ZERO : contagem.getPrecoVenda();
		
		BigInteger qtdMovimento = (contagem.getQtdDevolucao() == null) ? BigInteger.ZERO : contagem.getQtdDevolucao();
		
		BigInteger qtdNota = (contagem.getQtdNota() == null) ? BigInteger.ZERO : contagem.getQtdNota();
		
		BigDecimal desconto = contagem.getDesconto();
		
		BigInteger diferenca = qtdMovimento.subtract(qtdNota);
		
		BigInteger quantidade = qtdNota.compareTo(BigInteger.ZERO) == 0 ? qtdMovimento : qtdNota;
			
		BigDecimal valorTotal = precoVenda.multiply(new BigDecimal(quantidade));
		
		BigDecimal totalComDesconto = valorTotal;
		
		if (desconto != null) {	

			totalComDesconto = valorTotal.subtract(valorTotal.multiply(desconto.divide(new BigDecimal(100))));
		}
		
		contagem.setDiferenca(diferenca);
		contagem.setValorTotal(valorTotal);
		contagem.setTotalComDesconto(totalComDesconto);
		
	}
	
	        /**
     * Insere os dados parciais de devolução digitados pelo usuario.
     * 
     * @param listaContagemDevolucao
     * @param usuario
     * @param mockPerfilUsuario
     */
	@Transactional
	public void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario, boolean indPerfilUsuarioEncarregado) {
		
		if(indPerfilUsuarioEncarregado) {
			
			inserirCorrecaoListaContagemDevolucao(listaContagemDevolucao, usuario);
		
		} else {
		
			inserirListaContagemDevolucao(listaContagemDevolucao, usuario);
		
		}
		
	}
	
	
	        /**
     * Salva os dados parciais de devolução digitados pelo usuario.
     * 
     * @param listaContagemDevolucao
     * @param usuario
     */
	private void inserirListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirContagemDevolucao(contagem, dataAtual, usuario, dataOperacao);
		
		}
		
		
	}
	
	/**
	 * Caso algum valor tiver sido corrigido, o valor da diferenca sera 
	 * grava como novo registro de ConferenciaEncalheParcial.
	 * 
	 * @param listaContagemDevolucao
	 * @param usuario
	 */
	private void inserirCorrecaoListaContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual, usuario, dataOperacao);
			
		}
		
				
	}
	
	private void inserirContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario, Date dataOperacao) {
		
		if(contagem.getQtdNota() == null) {
			return;
		}
		
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao	= contagem.getNumeroEdicao();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataMovimento(dataOperacao);
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(contagem.getQtdNota());
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		conferenciaEncalheParcial.setNfParcialGerada(false);
		conferenciaEncalheParcial.setDiferencaApurada(false);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	/**
	 * Remove Conferencia Encalhe Parcial(Contagem Devolucao Fornecedor) do produto edição na Data
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param dataOperacao
	 */
	private void removerContagemDevolucaoProdutoEdicaoData(String codigoProduto, Long numeroEdicao, Date dataOperacao ){
		
		List<ConferenciaEncalheParcial> listaCeParc = this.conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(false, 
																											                false, 
																											                StatusAprovacao.PENDENTE, 
																											                dataOperacao, 
																											                null,
																											                codigoProduto, 
																											                numeroEdicao);
		
		for (ConferenciaEncalheParcial item : listaCeParc){
		
		    conferenciaEncalheParcialRepository.remover(item);
		}
	}
	
	private void inserirCorrecaoContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario, Date dataOperacao) {
		
		String codigoProduto = contagem.getCodigoProduto();
		
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		boolean qtdeNaoInformada = (contagem.getQtdNota() == null);
		
		if (qtdeNaoInformada){
			
			this.removerContagemDevolucaoProdutoEdicaoData(codigoProduto, numeroEdicao, dataOperacao);
			
			return;
		}	
		
		BigInteger qtdTotalConferenciaEncalheParcialOld = conferenciaEncalheParcialRepository.obterQtdTotalEncalheParcial(
				StatusAprovacao.PENDENTE,
				null, 
				codigoProduto, 
				numeroEdicao);
		
		BigInteger correcao = null;
		
		BigInteger qtdTotalConferenciaEncalheParcialNew = contagem.getQtdNota();
		
		if( qtdTotalConferenciaEncalheParcialNew == null ) {
			return;
		}
		
		if( qtdTotalConferenciaEncalheParcialOld != null ) {
			
			if(qtdTotalConferenciaEncalheParcialOld.compareTo(qtdTotalConferenciaEncalheParcialNew) == 0) {
				return;
			}
			
			correcao = qtdTotalConferenciaEncalheParcialNew.subtract(qtdTotalConferenciaEncalheParcialOld);
			
		} else {
			
			correcao = qtdTotalConferenciaEncalheParcialNew;			
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		contagem.setIdProdutoEdicao(produtoEdicao.getId());
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataMovimento(dataOperacao);
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(correcao);
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		conferenciaEncalheParcial.setDiferencaApurada(false);
		conferenciaEncalheParcial.setNfParcialGerada(false);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	@Transactional
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException {
		
		confirmarValoresContagemDevolucao(listaContagemDevolucao, usuario);
	}
	
	private void confirmarValoresContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		Date dataAtual = new Date();
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual, usuario, dataOperacao);
			
			String codigoProduto = contagem.getCodigoProduto();
			Long numeroEdicao = contagem.getNumeroEdicao();
			
			List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
					conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
					null, 
					null,
					StatusAprovacao.PENDENTE, 
					null,
					null,
					codigoProduto, 
					numeroEdicao);
			
			aprovarConferenciaEncalheParcial(listaConferenciaEncalheParcial, dataOperacao, usuario);
			
		}
	}
	
	
	private void gerarDiferencaEstoque(ContagemDevolucaoDTO contagem, Date dataOperacao, Usuario usuario) {
		
		Diferenca diferenca = new Diferenca();

		ProdutoEdicao produtoEdicao = 
			this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
					contagem.getCodigoProduto(), contagem.getNumeroEdicao());
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(
					TipoMensagem.ERROR,
                    "Não foi encontrado o produto/edição para inventário de estoque!");
		}

		BigInteger qtdeDiferenca = contagem.getDiferenca();
		
		diferenca.setProdutoEdicao(produtoEdicao);
		diferenca.setQtde(qtdeDiferenca.abs());
		diferenca.setResponsavel(usuario);
		
		if (BigInteger.ZERO.compareTo(qtdeDiferenca) < 0) {
			
			diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_EM);
			
		} else {
			
			diferenca.setTipoDiferenca(TipoDiferenca.FALTA_EM);
		}

		diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		diferenca.setTipoDirecionamento(TipoDirecionamentoDiferenca.ESTOQUE);
		diferenca.setTipoEstoque(TipoEstoque.DEVOLUCAO_FORNECEDOR);
		diferenca.setAutomatica(true);
		diferenca.setDataMovimento(dataOperacao);
		
		this.diferencaEstoqueRepository.adicionar(diferenca);
	}


	/**
	 * Aprova os registros de Status Conferencia Encalhe Parcial.
	 * 
	 * @param listaConferenciaEncalheParcial
	 * @param dataOperacao
	 * @param usuario
	 */
	private void aprovarConferenciaEncalheParcial(List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial, Date dataOperacao, Usuario usuario) {
		
		for(ConferenciaEncalheParcial parcial :  listaConferenciaEncalheParcial) {
			
			parcial.setStatusAprovacao(StatusAprovacao.APROVADO);
			parcial.setResponsavel(usuario);
			parcial.setDataAprovacao(dataOperacao);
			
			conferenciaEncalheParcialRepository.alterar(parcial);	
		}		
	}

	private void sinalizarItemNFParcialGerada(ContagemDevolucaoDTO contagem) {
		
		List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
				conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
				null, 
				false, 
				StatusAprovacao.APROVADO, 
				null, 
				contagem.getIdProdutoEdicao(), 
				null,
				null);
		
		for(ConferenciaEncalheParcial parcial :  listaConferenciaEncalheParcial) {
			
			parcial.setNfParcialGerada(true);
			
			conferenciaEncalheParcialRepository.alterar(parcial);
			
		}
		
	}
	
	private void processarMovimentosPerdaGanhoEstoque(
			ContagemDevolucaoDTO contagem, 
			Usuario usuario,
			TipoMovimentoEstoque tipoMovimentoPerda,
			TipoMovimentoEstoque tipoMovimentoSobraEmReparte){
		
		if(contagem.getQtdDevolucao() == null) {
			contagem.setQtdDevolucao(BigInteger.ZERO);
		}
		
		if(contagem.getQtdNota() == null) {
			contagem.setQtdNota(BigInteger.ZERO);
		}
		
		BigInteger calculoQdeDiferenca = contagem.getQtdDevolucao().subtract(contagem.getQtdNota());
		
		if( calculoQdeDiferenca.compareTo(BigInteger.ZERO) < 0 ) {
			
			//TIPO MOVIMENTO SOBRA_EM_ENCALHE (criar tipo de movimento de estoque)
			if(tipoMovimentoSobraEmReparte == null) {
				tipoMovimentoSobraEmReparte = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SOBRA_EM_DEVOLUCAO);
			}
			
			MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(
					contagem.getIdProdutoEdicao(),
					usuario.getId(), calculoQdeDiferenca.abs(), 
					tipoMovimentoSobraEmReparte);
			
			ProdutoEdicao produtoEdicao  = produtoEdicaoRepository.buscarPorId(contagem.getIdProdutoEdicao());
			
			if(produtoEdicao != null) {
				if(produtoEdicao.getProduto().getOrigem().equals(Origem.MANUAL)) {
					movimentoEstoque.setStatusIntegracao(StatusIntegracao.NAO_INTEGRAR);
					movimentoEstoque.setOrigem(Origem.MANUAL);
				}
			}
			
			this.processarDiferenca(movimentoEstoque, 
									usuario, 
									produtoEdicao, 
									calculoQdeDiferenca.abs(), 
									TipoDiferenca.GANHO_EM, 
									tipoMovimentoSobraEmReparte.getGrupoMovimentoEstoque().getTipoEstoque(), 
									StatusAprovacao.GANHO);
			
		} else if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) > 0) {
			
			//TIPO MOVIMENTO PERDA_EM_DEVOLUCAO
			if(tipoMovimentoPerda == null){
				tipoMovimentoPerda = 
						tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.PERDA_EM_DEVOLUCAO);
			}
			
			MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(
					contagem.getIdProdutoEdicao(),
					usuario.getId(), 
					calculoQdeDiferenca, 
					tipoMovimentoPerda,Origem.TRANSFERENCIA_PERDA_EM_DEVOLUCAO_ENCALHE_FORNECEDOR);
			
			ProdutoEdicao produtoEdicao  = produtoEdicaoRepository.buscarPorId(contagem.getIdProdutoEdicao());
			
			if(produtoEdicao != null) {
				if(produtoEdicao.getProduto().getOrigem().equals(Origem.MANUAL)) {
					movimentoEstoque.setStatusIntegracao(StatusIntegracao.NAO_INTEGRAR);
					movimentoEstoque.setOrigem(Origem.MANUAL);
				}
			}
			
			this.processarDiferenca(movimentoEstoque, 
									usuario, 
									produtoEdicao, 
									calculoQdeDiferenca, 
									TipoDiferenca.PERDA_EM, 
									tipoMovimentoPerda.getGrupoMovimentoEstoque().getTipoEstoque(), 
									StatusAprovacao.PERDA);
			
		} 
		
	}
	
	private Diferenca processarDiferenca(MovimentoEstoque movimentoEstoque,
			Usuario usuario,
			ProdutoEdicao produtoEdicao, 
			BigInteger quantidade,
			TipoDiferenca tipoDiferenca,
			TipoEstoque tipoEstoque, 
			StatusAprovacao statusAprovacao){
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setResponsavel(usuario);
		
		diferenca.setProdutoEdicao(produtoEdicao);
		
		diferenca.setTipoDiferenca(tipoDiferenca);
			
		diferenca.setTipoEstoque(tipoEstoque);			
		
		diferenca.setQtde(quantidade);
			
		diferenca.setQtde(quantidade);
		
		diferenca = diferencaEstoqueService.lancarDiferencaFechamentoCEIntegracao(diferenca,movimentoEstoque, statusAprovacao);
		
		return diferenca;
	}
		
	 /**
     * Obtém os registro de ConferenciaEncalheParcial relativos a um objeto de
     * contagem agrupado e sinaliza-os mesmo que a diferenca foi apurada.
     * 
     * @param contagem
     */
	private void sinalizarDiferencaApurada(ContagemDevolucaoDTO contagem) {
		
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
				conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
				false, 
				null,
				StatusAprovacao.APROVADO, 
				null,
				null,
				codigoProduto, 
				numeroEdicao);
		
		for(ConferenciaEncalheParcial parcial : listaConferenciaEncalheParcial) {
			parcial.setDiferencaApurada(true);
			conferenciaEncalheParcialRepository.alterar(parcial);
		}
		
	}

	 /**
     * Separa os itens a serem utilizados na geração da NF por fornecedor,
     * gerando assim uma NF para cada grupo de produtos de um fornecedor.
     * 
     * @param listaContagemDevolucaoAprovada
     * @param usuarioopa
     * @param indConfirmarContagemDevolucao
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
	@Override
	@Transactional
	public void gerarNotasFiscaisPorFornecedor(
			List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada, 
			Usuario usuario, 
			boolean indConfirmarContagemDevolucao) throws FileNotFoundException, IOException {
		
		if(indConfirmarContagemDevolucao) {
			confirmarValoresContagemDevolucao(listaContagemDevolucaoAprovada, usuario);
		}
		
		Map<Fornecedor, List<ContagemDevolucaoDTO>> mapaFornecedorListaContagemDevolucao = new HashMap<Fornecedor, List<ContagemDevolucaoDTO>>();
		
		Map<String, Fornecedor> mapaCodProdutoFornecedor = new HashMap<String, Fornecedor>();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucaoAprovada) {
			
			Fornecedor fornecedor = obterFornecedorPorCodigoProduto(mapaCodProdutoFornecedor, contagem.getCodigoProduto());
			
			if( mapaFornecedorListaContagemDevolucao.get(fornecedor) != null ) {
				mapaFornecedorListaContagemDevolucao.get(fornecedor).add(contagem);
			} else {
				List<ContagemDevolucaoDTO> listaContagemDevolucao = new ArrayList<ContagemDevolucaoDTO>();
				listaContagemDevolucao.add(contagem);
				mapaFornecedorListaContagemDevolucao.put(fornecedor, listaContagemDevolucao);
			}
			
		}
		
		for(Entry<Fornecedor, List<ContagemDevolucaoDTO>> entry : mapaFornecedorListaContagemDevolucao.entrySet()) {
			
			gerarNotaFiscalParcial(entry.getKey(), entry.getValue());
			
		}
		
		
	}
	
	        /**
     * Obtem o fornecedor correspondente ao código produto.
     * 
     * @param mapaCodProdutoFornecedor
     * @param codigoProduto
     * 
     * @return Fornecedor
     */
	private Fornecedor obterFornecedorPorCodigoProduto(Map<String, Fornecedor> mapaCodProdutoFornecedor, String codigoProduto) {
		
		if(mapaCodProdutoFornecedor.get(codigoProduto) != null) {
			return mapaCodProdutoFornecedor.get(codigoProduto);
		}
		
		Fornecedor fornecedor = fornecedorService.obterFornecedorUnico(codigoProduto);

		mapaCodProdutoFornecedor.put(codigoProduto, fornecedor);
		
		return fornecedor;
		
	}
	
	/**
	 * Gera registro de nota fiscal saida fornecedor. 
	 * 
	 * @param fornecedor
	 * @param listaContagemDevolucaoAprovada
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void gerarNotaFiscalParcial(Fornecedor fornecedor, List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada ) throws FileNotFoundException, IOException {
		
		List<ContagemDevolucaoDTO> listaAgrupadaContagemDevolucao = obterListaContagemDevolucaoTotalAgrupado(listaContagemDevolucaoAprovada, null, false, StatusAprovacao.APROVADO);
		
		if(listaAgrupadaContagemDevolucao == null || listaAgrupadaContagemDevolucao.isEmpty()) {
			return;
		}
		
		NaturezaOperacao tipoNotaFiscal = this.tipoNotaFiscalRepository.obterNaturezaOperacao(GrupoNotaFiscal.NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO);

		if(tipoNotaFiscal == null) {
            throw new IllegalStateException("Nota Fiscal Saida não parametrizada no sistema");
		}
		
		List<ItemNotaFiscalSaida> listItemNotaFiscal = carregarDadosNFSaida(listaAgrupadaContagemDevolucao);
		
		InformacaoTransporte transporte = new InformacaoTransporte();
		
		transporte.setModalidadeFrete(0);
		
		InformacaoAdicional informacaoAdicional = new InformacaoAdicional();
		
		
		Processo processo = this.processoRepository.buscarPeloNome("DEVOLUCAO_AO_FORNECEDOR");
		
		Set<Processo> processos = new HashSet<Processo>(1);		
		processos.add(processo);
		
		Long idNota = null; 
		
		/*
		notaFiscalService.emitiNotaFiscal(
				tipoNotaFiscal.getId(), 
				new Date(), 
				fornecedor, 
				listItemNotaFiscal, 
				transporte, 
				informacaoAdicional, 
				null, 
				processos, 
				Condicao.DEVOLUCAO_ENCALHE);
		*/
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		this.gerarMovimentoEstoque(listaAgrupadaContagemDevolucao,usuario.getId());
		
		List<NotaFiscal> listaNotas = new ArrayList<NotaFiscal>();
		
		listaNotas.add(notaFiscalRepository.buscarPorId(idNota));
		
		notaFiscalService.exportarNotasFiscais(listaNotas);
		
		try {
			
			notaFiscalService.exportarNotasFiscais(idNota);
			
		} catch (Exception e) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao gerar arquivo de NFe"));
			
		}
	}
	
	private void gerarMovimentoEstoque(List<ContagemDevolucaoDTO> listaContagemDevolucao, Long idUsuario) {
		
		TipoMovimentoEstoque tipoMovimentoDevolucaoEncalhe = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
		
		List<Fornecedor> fornecedores = null;
				
		for ( ContagemDevolucaoDTO contagem : listaContagemDevolucao ) {
			
			if (contagem.getQtdNota() == null){
				
				continue;
			}
			
			if(listaContagemDevolucao != null && listaContagemDevolucao.size() > 0) {
				
				fornecedores = fornecedorService.obterFornecedoresPorProduto(contagem.getCodigoProduto(), null);
			}
			
			Fornecedor fornecedor = null;
			if(fornecedores != null && fornecedores.size() > 0) {
				fornecedor = fornecedores.iterator().next();
			}
			
			MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(contagem.getIdProdutoEdicao(), idUsuario, contagem.getQtdNota(), tipoMovimentoDevolucaoEncalhe);

			gerarMovimentoFechamentoFiscalFornecedor(tipoMovimentoDevolucaoEncalhe, contagem, fornecedor, movimentoEstoque);
		}
	}

	private MovimentoFechamentoFiscalFornecedor gerarMovimentoFechamentoFiscalFornecedor(
			TipoMovimentoEstoque tipoMovimentoDevolucaoEncalhe, ContagemDevolucaoDTO contagem, Fornecedor fornecedor,
			MovimentoEstoque movimentoEstoque) {
		
		MovimentoFechamentoFiscalFornecedor mfff = movimentoFechamentoFiscalRepository.buscarPorProdutoEdicaoTipoMovimentoEstoque(new ProdutoEdicao(contagem.getIdProdutoEdicao()), tipoMovimentoDevolucaoEncalhe);
		
		Set<MovimentoEstoque> movimentos = movimentoEstoque.getProdutoEdicao().getMovimentoEstoques();
		
		BigInteger qtdeDevSimb = BigInteger.ZERO;
		for(MovimentoEstoque me : movimentos) {
			if(((TipoMovimentoEstoque) me.getTipoMovimento()).getGrupoMovimentoEstoque().equals(GrupoMovimentoEstoque.RECEBIMENTO_FISICO)) {
				qtdeDevSimb = qtdeDevSimb.add(me.getQtde());
			} 
			
			if(((TipoMovimentoEstoque) me.getTipoMovimento()).getGrupoMovimentoEstoque().equals(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE)) {
				qtdeDevSimb = qtdeDevSimb.subtract(me.getQtde());
			}
		}
		
		if(qtdeDevSimb.intValue() > 0) {
			
			if(mfff == null) {
				
				List<OrigemItemMovFechamentoFiscal> listaOrigemMovsFiscais = new ArrayList<>();
				OrigemItemMovFechamentoFiscalDevolucaoFornecedor oimffdf = new OrigemItemMovFechamentoFiscalDevolucaoFornecedor();
				oimffdf.setMovimento(movimentoEstoque);
				listaOrigemMovsFiscais.add(oimffdf);
				
				BigDecimal precoVenda = null;
				BigDecimal precoComDesconto = null;
				BigDecimal valorDesconto = null;
				
				if(movimentoEstoque != null && movimentoEstoque.getProdutoEdicao() != null) {
					
					precoVenda = movimentoEstoque.getProdutoEdicao().getPrecoVenda();
					
					if(movimentoEstoque.getProdutoEdicao().getOrigem().equals(Origem.MANUAL)) {
						
						valorDesconto = movimentoEstoque.getProdutoEdicao().getDesconto();
						if(valorDesconto == null && movimentoEstoque.getProdutoEdicao().getProduto() != null) {
							valorDesconto = movimentoEstoque.getProdutoEdicao().getProduto().getDesconto();
						}
						
					} else {
						
						if(movimentoEstoque.getProdutoEdicao().getDescontoLogistica() != null) {
							valorDesconto = movimentoEstoque.getProdutoEdicao().getDescontoLogistica().getPercentualDesconto();
							if(valorDesconto == null 
									&& movimentoEstoque.getProdutoEdicao().getProduto() != null
									&& movimentoEstoque.getProdutoEdicao().getProduto().getDescontoLogistica() != null) {
								valorDesconto = movimentoEstoque.getProdutoEdicao().getProduto().getDescontoLogistica().getPercentualDesconto();
							}
						}
						
					}
					
					if(valorDesconto != null && precoVenda != null) {
						precoComDesconto = precoVenda.multiply(valorDesconto).divide(BigDecimal.valueOf(100));
					}
					
				}
				
				validarValoresAplicados(movimentoEstoque, precoVenda, precoComDesconto, valorDesconto);
				
				ValoresAplicados valoresAplicados = new ValoresAplicados(precoVenda, precoComDesconto, valorDesconto);
				
				mfff = new MovimentoFechamentoFiscalFornecedor();
				mfff.setOrigemMovimentoFechamentoFiscal(listaOrigemMovsFiscais);
				mfff.setQtde(contagem.getQtdNota());
				mfff.setValoresAplicados(valoresAplicados);
				mfff.setNotaFiscalLiberadaEmissao(true);
				mfff.setData(distribuidorService.obterDataOperacaoDistribuidor());
				mfff.setTipoMovimento(tipoMovimentoFiscalRepository.buscarTiposMovimentoFiscalPorTipoOperacao(OperacaoEstoque.SAIDA));
				mfff.setFornecedor(fornecedor);
				mfff.setProdutoEdicao(new ProdutoEdicao(contagem.getIdProdutoEdicao()));
				mfff.setTipoDestinatario(TipoDestinatario.FORNECEDOR);
				mfff.setNotaFiscalDevolucaoSimbolicaEmitida(false);
				mfff.setDesobrigaNotaFiscalDevolucaoSimbolica(false);
				oimffdf.setMovimentoFechamentoFiscal(mfff);
				
				movimentoFechamentoFiscalRepository.adicionar(mfff);
				
			} else {
				
				OrigemItemMovFechamentoFiscalDevolucaoFornecedor oimffdv = new OrigemItemMovFechamentoFiscalDevolucaoFornecedor();
				oimffdv.setMovimento(movimentoEstoque);
				mfff.getOrigemMovimentoFechamentoFiscal().add(oimffdv);
				mfff.setQtde(mfff.getQtde().add(movimentoEstoque.getQtde()));
				oimffdv.setMovimentoFechamentoFiscal(mfff);
				
				movimentoFechamentoFiscalRepository.alterar(mfff);
			}
			
		}
		
		return mfff;
	}

	private void validarValoresAplicados(MovimentoEstoque movimentoEstoque,
			BigDecimal precoVenda, BigDecimal precoComDesconto,
			BigDecimal valorDesconto) {
		if(precoVenda == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, 
					String.format("Erro ao obter o preço de venda do produto: %s / %s"
							, movimentoEstoque.getProdutoEdicao().getProduto().getCodigo()
							, movimentoEstoque.getProdutoEdicao().getNumeroEdicao()));
		}
		
		if(precoComDesconto == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, 
					String.format("Erro ao obter o preço com desconto do produto: %s / %s"
							, movimentoEstoque.getProdutoEdicao().getProduto().getCodigo()
							, movimentoEstoque.getProdutoEdicao().getNumeroEdicao()));
		}
		
		if(valorDesconto == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, 
					String.format("Erro ao obter o valor do desconto do produto: %s / %s"
							, movimentoEstoque.getProdutoEdicao().getProduto().getCodigo()
							, movimentoEstoque.getProdutoEdicao().getNumeroEdicao()));
		}
	}
	
	/**
     * Obtém uma lista de contagem devolução com as qtndes de itens sumarizadas
     * agrupando por produto edicao.
     * 
     * @param listaContagemDevolucaoAprovada
     * @param indDiferencaApurada
     * @param indNfParcialGerada
     * 
     * @return List<ContagemDevolucaoDTO>
     */
	private List<ContagemDevolucaoDTO> obterListaContagemDevolucaoTotalAgrupado(
			List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada,
			Boolean indDiferencaApurada,
			Boolean indNfParcialGerada,
			StatusAprovacao statusAprovacao) {
		
		List<ContagemDevolucaoDTO> listaAgrupadaContagemDevolucao = new ArrayList<ContagemDevolucaoDTO>();
		
		for(ContagemDevolucaoDTO contagemDevolucaoAprovada : listaContagemDevolucaoAprovada) {
			
			/*if(contagemDevolucaoAprovada.getDiferenca() == null){
				
				continue;
			}*/

			List<ContagemDevolucaoDTO> contagemAgrupada = conferenciaEncalheParcialRepository.
					obterListaContagemDevolucao(
					null, 
					false, 
					StatusAprovacao.APROVADO, 
					contagemDevolucaoAprovada.getIdProdutoEdicao(),
					contagemDevolucaoAprovada.getCodigoProduto(), 
					contagemDevolucaoAprovada.getNumeroEdicao(),
					null);
			
			for (ContagemDevolucaoDTO item : contagemAgrupada) {
				item.setQtdNota(contagemDevolucaoAprovada.getQtdNota());
			}

			if(contagemAgrupada == null || contagemAgrupada.isEmpty()) {
				continue;
			}

			listaAgrupadaContagemDevolucao.addAll(contagemAgrupada);
			
		}
		
		return listaAgrupadaContagemDevolucao;
		
	}
	
	/**
	 * Carrega os itens da nota fiscal.
	 * 
	 * @param listaContagemDevolucao
	 * 
	 * @return List - ItemNotaFiscalSaida
	 */
	private List<ItemNotaFiscalSaida> carregarDadosNFSaida(
			List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		ItemNotaFiscalSaida itemNotaFiscal = null;
		
		ProdutoEdicao produtoEdicao  = null;
		
		List<ItemNotaFiscalSaida> listItemNotaFiscal = new ArrayList<ItemNotaFiscalSaida>(listaContagemDevolucao.size());;
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			if(	contagem.getIdProdutoEdicao() != null && 
				contagem.getQtdNota() != null && 
				contagem.getQtdNota().compareTo(BigInteger.ZERO) > 0 ) {
				
				produtoEdicao = produtoEdicaoRepository.buscarPorId(contagem.getIdProdutoEdicao());
				
				itemNotaFiscal = new ItemNotaFiscalSaida();		
				
				itemNotaFiscal.setIdProdutoEdicao(produtoEdicao.getId());
				
				if (produtoEdicao.getProduto().getTributacaoFiscal() != null) {
					itemNotaFiscal.setCstICMS(produtoEdicao.getProduto()
							.getTributacaoFiscal().getCST());
				}

				itemNotaFiscal.setQuantidade(contagem.getQtdNota());
				
				itemNotaFiscal.setValorUnitario(produtoEdicao.getPrecoVenda());
				
				itemNotaFiscal.setInfoComplementar("Semana chamada encalhe: " + SemanaUtil.obterDiaDaSemana(contagem.getDataMovimento()));

				listItemNotaFiscal.add(itemNotaFiscal);
				
				sinalizarItemNFParcialGerada(contagem);
				
			}
			
		}
		return listItemNotaFiscal;
		
		
	}
	

	@Override
	@Transactional
	public List<ContagemDevolucaoDTO> obterContagemDevolucaoEdicaoFechada(
			boolean checkAll, List<ProdutoEdicaoFechadaVO> listaEdicoesFechadas, FiltroDigitacaoContagemDevolucaoDTO filtro) {
		
		List<ContagemDevolucaoDTO> listaContagemEdicaoFechada = new ArrayList<ContagemDevolucaoDTO>();
		
		List<RegistroEdicoesFechadasVO> listaRegistroEdicoesFechadasVO =
				edicoesFechadasService.obterResultadoEdicoesFechadas(filtro.getDataInicial(), 
						filtro.getDataFinal(), filtro.getIdFornecedor(), null, null, filtro.getPaginacao().getPaginaAtual(), 
						filtro.getPaginacao().getQtdResultadosPorPagina()); 
		
		
		for (RegistroEdicoesFechadasVO registroEdicoesFechadas : listaRegistroEdicoesFechadasVO) {
			
			if (!checkAll && !listaEdicoesFechadas.contains(registroEdicoesFechadas)) {
				continue;
			}
			
			ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(registroEdicoesFechadas.getIdProdutoEdicao());
			
			ContagemDevolucaoDTO contagem = new ContagemDevolucaoDTO();
			
			ConferenciaEncalheParcial conferenciaEncalheParcial = 
					this.conferenciaEncalheParcialRepository.obterConferenciaEncalheParcialPor(produtoEdicao.getId(), registroEdicoesFechadas.getDataLancamento());
						
			contagem.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			contagem.setPrecoVenda(produtoEdicao.getPrecoVenda());
			contagem.setIdProdutoEdicao(produtoEdicao.getId());
			contagem.setNomeProduto(produtoEdicao.getProduto().getNome());
			contagem.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			contagem.setQtdDevolucao(registroEdicoesFechadas.getSaldo());
			contagem.setEdicaoFechada(true);
			
			if (conferenciaEncalheParcial != null) {
				
				contagem.setQtdNota(conferenciaEncalheParcial.getQtde());
				contagem.setStatusAprovacao(conferenciaEncalheParcial.getStatusAprovacao());
				contagem.setDataAprovacao(conferenciaEncalheParcial.getDataAprovacao());
				contagem.setDiferenca(contagem.getQtdDevolucao().subtract(contagem.getQtdNota()));
			}
			
			this.adicionarValores(contagem);
			
			listaContagemEdicaoFechada.add(contagem);
		}
		
		return listaContagemEdicaoFechada;
	}
	
	
	@Transactional
	public void efetuarDevolucaoParcial(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario){
		
		this.confirmarValoresContagemDevolucao(listaContagemDevolucao, usuario);
		
		this.gerarMovimentoEstoque(listaContagemDevolucao, usuario.getId());
	}
	
	@Transactional
	public void efetuarDevolucaoFinal(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario){
		
		this.validarItensContagemDevolucao(listaContagemDevolucao);
		
		this.confirmarValoresContagemDevolucao(listaContagemDevolucao, usuario);
		
		List<ContagemDevolucaoDTO> listaAgrupadaContagemDevolucao = 
				obterListaContagemDevolucaoTotalAgrupado(listaContagemDevolucao, null, false, StatusAprovacao.APROVADO);
		
		TipoMovimentoEstoque tipoMovimentoDevolucaoEncalhe = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
		
		TipoMovimentoEstoque tipoMovimentoSobraEmReparte = null;
		
		TipoMovimentoEstoque tipoMovimentoPerda = null;
		
		for ( ContagemDevolucaoDTO item : listaAgrupadaContagemDevolucao ) {
			
			estoqueProdutoService.processarTransferenciaEntreEstoques(
					item.getIdProdutoEdicao(),
					TipoEstoque.LANCAMENTO, 
					TipoEstoque.DEVOLUCAO_ENCALHE, 
					usuario.getId());
			
			estoqueProdutoService.processarTransferenciaEntreEstoques(
					item.getIdProdutoEdicao(),
					TipoEstoque.SUPLEMENTAR, 
					TipoEstoque.DEVOLUCAO_ENCALHE, 
					usuario.getId());
			
			this.processarMovimentosPerdaGanhoEstoque(
					item, 
					usuario, 
					tipoMovimentoPerda,
					tipoMovimentoSobraEmReparte);
			
			MovimentoEstoque movimentoEstoque = movimentoEstoqueService.gerarMovimentoEstoque(
					item.getIdProdutoEdicao(),
					usuario.getId(),
					item.getQtdNota(),
					tipoMovimentoDevolucaoEncalhe);
			
			for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
				
				gerarMovimentoFechamentoFiscalFornecedor(tipoMovimentoDevolucaoEncalhe, contagem
						, movimentoEstoque.getProdutoEdicao().getProduto().getFornecedor(), movimentoEstoque);
			}
		}
	}
	
	private void validarItensContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		Set<Long> idsProdutoEdicao = new HashSet<Long>();
		
		for (ContagemDevolucaoDTO contagemDevolucao : listaContagemDevolucao) {
			
			idsProdutoEdicao.add(contagemDevolucao.getIdProdutoEdicao());
		}
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterLancamentosRecolhidosPorEdicoes(idsProdutoEdicao);
		
		for (Lancamento lancamento : lancamentos) {
			
			if (StatusLancamento.EM_RECOLHIMENTO.equals(lancamento.getStatus())) {

				String nomeProduto = lancamento.getProdutoEdicao().getNomeComercial();
				String codigoProduto = lancamento.getProdutoEdicao().getProduto().getCodigo();

				Long edicaoProduto = lancamento.getProdutoEdicao().getNumeroEdicao();

				throw new ValidacaoException(TipoMensagem.WARNING, 
						String.format("Lançamento do produto %s [Cod.: %s, Ed.: %s encontra-se em recolhimento.", nomeProduto, codigoProduto, edicaoProduto));
			}
		}		
	}
	
	@Override
    @Transactional
	public void gerarNotasFiscaisPorFornecedorFecharLancamentos(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException {
		
    	this.gerarNotasFiscaisPorFornecedor(listaContagemDevolucao, usuario, true);
    	
    	fecharLancamentos(listaContagemDevolucao, usuario);		
	}

	@Override
    @Transactional
	public void fecharLancamentos(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException {

		Set<Long> idsProdutoEdicao = new TreeSet<>();
        
        for (ContagemDevolucaoDTO dto : listaContagemDevolucao) {
        	
        	ProdutoEdicao pe =produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(dto.getCodigoProduto(), dto.getNumeroEdicao());
        	
        	idsProdutoEdicao.add(pe.getId());        	
        }
        
        if (!idsProdutoEdicao.isEmpty()){
	        List<Lancamento> lancamentos =
	        	this.lancamentoRepository.obterLancamentosRecolhidosPorEdicoes(idsProdutoEdicao);
	        
	        for (Lancamento lancamento : lancamentos) {
				
				lancamento.setStatus(StatusLancamento.FECHADO);
				lancamento.setUsuario(usuarioService.getUsuarioLogado());
				
				this.lancamentoRepository.merge(lancamento);
			}
        }
	}

	
    @Override
    @Transactional(readOnly = true)
    public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, boolean perfilEncarregado){
    	
    	return this.movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, perfilEncarregado);
    }
}