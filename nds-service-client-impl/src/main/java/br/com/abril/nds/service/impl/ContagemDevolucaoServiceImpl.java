package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.ChamadaEncalheFornecedorDTOAssembler;
import br.com.abril.nds.client.vo.ProdutoEdicaoFechadaVO;
import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.dto.ContagemDevolucaoAgregationValuesDTO;
import br.com.abril.nds.dto.ContagemDevolucaoConferenciaCegaDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.chamadaencalhe.ChamadasEncalheFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.Condicao;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.service.ContagemDevolucaoService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EdicoesFechadasService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class ContagemDevolucaoServiceImpl implements ContagemDevolucaoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ContagemDevolucaoServiceImpl.class);

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

	
	@Transactional
	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado) {

		InfoContagemDevolucaoDTO info = new InfoContagemDevolucaoDTO();

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
	
	
	@Override
	@Transactional(readOnly=true)
	public List<ContagemDevolucaoConferenciaCegaDTO> obterInfoContagemDevolucaoCega(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado) {
		
		
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
	
	
	private void inserirCorrecaoContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario, Date dataOperacao) {
		
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		BigInteger qtdTotalConferenciaEncalheParcialOld = conferenciaEncalheParcialRepository.obterQtdTotalEncalheParcial(
				StatusAprovacao.PENDENTE,
				null, 
				codigoProduto, 
				numeroEdicao);
		
		BigInteger qtdTotalConferenciaEncalheParcialNew = contagem.getQtdNota();
		
		if( qtdTotalConferenciaEncalheParcialNew == null ) {
			return;
		}
		
		BigInteger correcao = null;
		
		if( qtdTotalConferenciaEncalheParcialOld != null ) {
			
			if(qtdTotalConferenciaEncalheParcialOld.compareTo(qtdTotalConferenciaEncalheParcialNew) == 0) {
				return;
			}
			
			correcao = qtdTotalConferenciaEncalheParcialNew.subtract(qtdTotalConferenciaEncalheParcialOld);
			
		} else {
			
			correcao = qtdTotalConferenciaEncalheParcialNew;
			
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
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
		
        // FIXME: ajustar função de confirmar para geração de notas ou impressão
        // de CE de acordo com a obrigação fiscal.
		gerarNotasFiscaisPorFornecedor(listaContagemDevolucao, usuario, false);
		
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
	
	
	private void ajustarDiferencaConferenciaEncalheContagemDevolucao(ContagemDevolucaoDTO contagem, Usuario usuario) {
		
		if(contagem.getQtdDevolucao() == null) {
			contagem.setQtdDevolucao(BigInteger.ZERO);
		}
		
		if(contagem.getQtdNota() == null) {
			contagem.setQtdNota(BigInteger.ZERO);
		}
		
		BigInteger calculoQdeDiferenca = contagem.getQtdDevolucao().subtract(contagem.getQtdNota());
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(contagem.getIdProdutoEdicao());					

		Diferenca diferenca = new Diferenca();
		
		if( calculoQdeDiferenca.compareTo(BigInteger.ZERO) < 0 ) {
			
			diferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE);
			
		} else if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) > 0) {
			
			diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_DE);
			
		} else if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) == 0) {
			
			sinalizarDiferencaApurada(contagem);
			
			return;
			
		}
		
		diferenca.setQtde(calculoQdeDiferenca.abs());
		diferenca.setResponsavel(usuario);
		diferenca.setProdutoEdicao(produtoEdicao);
		
		diferencaEstoqueService.lancarDiferencaAutomaticaContagemDevolucao(diferenca);
		
		sinalizarDiferencaApurada(contagem);
		
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
		
		NaturezaOperacao tipoNotaFiscal = this.tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO);

		if(tipoNotaFiscal == null) {
            throw new IllegalStateException("Nota Fiscal Saida não parametrizada no sistema");
		}
		
		List<ItemNotaFiscalSaida> listItemNotaFiscal = carregarDadosNFSaida(listaAgrupadaContagemDevolucao);
		
		InformacaoTransporte transporte = new InformacaoTransporte();
		
		transporte.setModalidadeFrente(0);
		
		InformacaoAdicional informacaoAdicional = new InformacaoAdicional();
		
		Set<Processo> processos = new HashSet<Processo>(1);		
		
		processos.add(Processo.DEVOLUCAO_AO_FORNECEDOR);
		
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
		
		this.gerarMovimentoEstoque(listaAgrupadaContagemDevolucao);
		
		List<NotaFiscal> listaNotas = new ArrayList<NotaFiscal>();
		
		listaNotas.add(notaFiscalRepository.buscarPorId(idNota));
		
		notaFiscalService.exportarNotasFiscais(listaNotas);
		
		try {
			
			notaFiscalService.exportarNotasFiscais(idNota);
			
		} catch (Exception e) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao gerar arquivo de NFe"));
			
		}
	}
	
	private void gerarMovimentoEstoque(List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		for ( ContagemDevolucaoDTO contagem : listaContagemDevolucao ) {

			TipoMovimentoEstoque tipoMovimento = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);

			movimentoEstoqueService.gerarMovimentoEstoque(
					dataOperacao,
					contagem.getIdProdutoEdicao(), 
					usuarioService.getUsuarioLogado().getId(), 
					contagem.getQtdNota(),
					tipoMovimento);

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

			List<ContagemDevolucaoDTO> contagemAgrupada = conferenciaEncalheParcialRepository.
					obterListaContagemDevolucao(
					null, 
					false, 
					StatusAprovacao.APROVADO, 
					null,
					contagemDevolucaoAprovada.getCodigoProduto(), 
					contagemDevolucaoAprovada.getNumeroEdicao(),
					null);

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
	
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public byte[] gerarImpressaoChamadaEncalheFornecedor(Long idFornecedor,
            Integer numeroSemana, Intervalo<Date> periodo) {
        List<ChamadaEncalheFornecedor> chamadasEncalheFornecedor = chamadaEncalheFornecedorRepository
                .obterChamadasEncalheFornecedor(idFornecedor, numeroSemana,
                        periodo);

        if(chamadasEncalheFornecedor.isEmpty())
        	throw new ValidacaoException(TipoMensagem.WARNING, "");
        
        Distribuidor distribuidor = distribuidorService.obter();
        Collection<ChamadasEncalheFornecedorDTO> chamadasEncalheDTO = ChamadaEncalheFornecedorDTOAssembler
                .criarChamadasEncalheFornecedorDTO(chamadasEncalheFornecedor,
                        distribuidor);
        
        return gerarPDFChamadaEncalheFornecedor(chamadasEncalheDTO);
    }

    @Override
    @Transactional
	public void gerarNotasFiscaisPorFornecedorFecharLancamentos(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) throws FileNotFoundException, IOException {
		
    	this.gerarNotasFiscaisPorFornecedor(listaContagemDevolucao, usuario, true);
    	
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

    /**
     * Gera o PDF com as chamadas de encalhe recebidas
     * 
     * @param chamadas chamadas de encalhe para geração do PDF
     * @return PDF gerado com as chamadas de encalhe
     */
    protected byte[] gerarPDFChamadaEncalheFornecedor(Collection<ChamadasEncalheFornecedorDTO> chamadas) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/CE_Devolucao_Fornecedor_lote.jasper");
        try {
            JRDataSource dataSource = new JRBeanCollectionDataSource(chamadas);
            String path = url.toURI().getPath();
            return JasperRunManager.runReportToPdf(path, new HashMap<String, Object>(), dataSource);
        } catch (URISyntaxException | JRException ex) {
            LOGGER.error("Erro gerando PDF Chamada de Encalhe Fornecedor!", ex);
            throw new RuntimeException("Erro gerando PDF Chamada de Encalhe Fornecedor!", ex);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, boolean perfilEncarregado){
    	
    	return this.movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, perfilEncarregado);
    }
}