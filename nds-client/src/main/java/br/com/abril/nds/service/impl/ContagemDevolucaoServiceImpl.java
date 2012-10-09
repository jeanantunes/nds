package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperRunManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.report.ImpressaoCEDataSource;
import br.com.abril.nds.dto.ContagemDevolucaoConferenciaCegaDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.IdentificacaoImpressaoCEDevolucaoDTO;
import br.com.abril.nds.dto.ImpressaoCEDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.ProdutoImpressaoCEDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.StatusEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleContagemDevolucaoRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalSaidaRepository;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.ContagemDevolucaoService;
import br.com.abril.nds.service.ControleNumeracaoNotaFiscalService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FornecedorService;

@Service
public class ContagemDevolucaoServiceImpl implements ContagemDevolucaoService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;  
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private ConferenciaEncalheParcialRepository conferenciaEncalheParcialRepository;

	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Autowired
	private NotaFiscalSaidaRepository notaFiscalSaidaRepository;

	@Autowired
	private ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;

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
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private ControleNumeracaoNotaFiscalService controleNumeracaoNotaFiscalService;
	
	 private static final Logger LOG = LoggerFactory.getLogger(ContagemDevolucaoServiceImpl.class);
	
	
	@Transactional
	public InfoContagemDevolucaoDTO obterInfoContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado) {
		
		InfoContagemDevolucaoDTO info = new InfoContagemDevolucaoDTO();
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		Integer qtdTotalRegistro = movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(filtroPesquisa, tipoMovimentoEstoque);
		info.setQtdTotalRegistro(qtdTotalRegistro);
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				filtroPesquisa, 
				tipoMovimentoEstoque, 
				indPerfilUsuarioEncarregado);
		
		info.setListaContagemDevolucao(listaContagemDevolucao);
		
		BigDecimal valorTotalGeral = BigDecimal.ZERO;
		info.setValorTotalGeral(valorTotalGeral);
		
		if(indPerfilUsuarioEncarregado) {
			carregarDadosAdicionais(info, listaContagemDevolucao);
		}
		
		return info;	
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<ContagemDevolucaoConferenciaCegaDTO> obterInfoContagemDevolucaoCega(FiltroDigitacaoContagemDevolucaoDTO filtroPesquisa, boolean indPerfilUsuarioEncarregado) {
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				filtroPesquisa, 
				tipoMovimentoEstoque, 
				indPerfilUsuarioEncarregado);
		
		List<ContagemDevolucaoConferenciaCegaDTO> cegaDTOs = new ArrayList<ContagemDevolucaoConferenciaCegaDTO>(listaContagemDevolucao.size());
		for(ContagemDevolucaoDTO contagemDevolucaoDTO : listaContagemDevolucao){
			ContagemDevolucaoConferenciaCegaDTO cegaDTO = new ContagemDevolucaoConferenciaCegaDTO();
			
			cegaDTO.setCodigoProduto(contagemDevolucaoDTO.getCodigoProduto());
			cegaDTO.setIdProdutoEdicao(contagemDevolucaoDTO.getIdProdutoEdicao());
			cegaDTO.setNomeProduto(contagemDevolucaoDTO.getNomeProduto());
			cegaDTO.setNumeroEdicao(contagemDevolucaoDTO.getNumeroEdicao());
			cegaDTO.setPrecoVenda(contagemDevolucaoDTO.getPrecoVenda());
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
			info.setValorTotalGeral(info.getValorTotalGeral().add(valorTotal));
		}
		
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
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirContagemDevolucao(contagem, dataAtual, usuario);
		
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
		
		if(listaContagemDevolucao == null || listaContagemDevolucao.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual, usuario);
			
		}
		
				
	}
	
	private void inserirContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario) {
		
		if(contagem.getQtdNota() == null) {
			return;
		}
		
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao	= contagem.getNumeroEdicao();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = new ConferenciaEncalheParcial();
		
		conferenciaEncalheParcial.setDataConfEncalheParcial(dataAtual);
		conferenciaEncalheParcial.setDataMovimento(contagem.getDataMovimento());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(contagem.getQtdNota());
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		conferenciaEncalheParcial.setNfParcialGerada(false);
		conferenciaEncalheParcial.setDiferencaApurada(false);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	
	private void inserirCorrecaoContagemDevolucao(ContagemDevolucaoDTO contagem, Date dataAtual, Usuario usuario) {
		
		Date dataMovimento = contagem.getDataMovimento();
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		BigInteger qtdTotalConferenciaEncalheParcialOld = conferenciaEncalheParcialRepository.obterQtdTotalEncalheParcial(
				StatusAprovacao.PENDENTE,
				dataMovimento, 
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
		conferenciaEncalheParcial.setDataMovimento(contagem.getDataMovimento());
		conferenciaEncalheParcial.setProdutoEdicao(produtoEdicao);
		conferenciaEncalheParcial.setQtde(correcao);
		conferenciaEncalheParcial.setResponsavel(usuario);
		conferenciaEncalheParcial.setStatusAprovacao(StatusAprovacao.PENDENTE);
		conferenciaEncalheParcial.setDiferencaApurada(false);
		conferenciaEncalheParcial.setNfParcialGerada(false);
		
		conferenciaEncalheParcialRepository.adicionar(conferenciaEncalheParcial);
		
	}
	
	@Transactional
	public void confirmarContagemDevolucao(List<ContagemDevolucaoDTO> listaContagemDevolucao, Usuario usuario) {
		
		Date dataAtual = new Date();
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			inserirCorrecaoContagemDevolucao(contagem, dataAtual, usuario);
			
			Date dataMovimento = contagem.getDataMovimento();
			String codigoProduto = contagem.getCodigoProduto();
			Long numeroEdicao = contagem.getNumeroEdicao();
			
			List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
					conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
					false, 
					false,
					StatusAprovacao.PENDENTE, 
					dataMovimento,
					null,
					codigoProduto, 
					numeroEdicao);
			
			aprovarConferenciaEncalheParcial(listaConferenciaEncalheParcial, dataAtual, usuario);
			
		}
		
		gerarNotasFiscaisPorFornecedor(listaContagemDevolucao);

		verificarConferenciaEncalheFinalizada(usuario);
		
	}
	
	
	/**
	 * Aprova os registros de Status Conferencia Encalhe Parcial.
	 * 
	 * @param listaConferenciaEncalheParcial
	 * @param dataAtual
	 * @param usuario
	 */
	private void aprovarConferenciaEncalheParcial(List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial, Date dataAtual, Usuario usuario) {
		
		for(ConferenciaEncalheParcial parcial :  listaConferenciaEncalheParcial) {
			
			parcial.setStatusAprovacao(StatusAprovacao.APROVADO);
			parcial.setResponsavel(usuario);
			parcial.setDataAprovacao(dataAtual);
			
			conferenciaEncalheParcialRepository.alterar(parcial);
			
		}
		
	}
	
	
	private void sinalizarItemNFParcialGerada(ContagemDevolucaoDTO contagem) {
		
		List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
				conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
				false, 
				false, 
				StatusAprovacao.APROVADO, 
				contagem.getDataMovimento(), 
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
		
		diferencaEstoqueService.lancarDiferencaAutomatica(diferenca);
		
		sinalizarDiferencaApurada(contagem);
		
	}
	
	/**
	 * Obtém os registro de ConferenciaEncalheParcial relativos 
	 * a um objeto de contagem agrupado e sinaliza-os mesmo que
	 * a diferenca foi apurada.
	 * 
	 * @param contagem
	 */
	private void sinalizarDiferencaApurada(ContagemDevolucaoDTO contagem) {
		
		Date dataMovimento = contagem.getDataMovimento();
		String codigoProduto = contagem.getCodigoProduto();
		Long numeroEdicao = contagem.getNumeroEdicao();
		
		List<ConferenciaEncalheParcial> listaConferenciaEncalheParcial = 
				conferenciaEncalheParcialRepository.obterListaConferenciaEncalhe(
				false, 
				null,
				StatusAprovacao.APROVADO, 
				dataMovimento,
				null,
				codigoProduto, 
				numeroEdicao);
		
		for(ConferenciaEncalheParcial parcial : listaConferenciaEncalheParcial) {
			parcial.setDiferencaApurada(true);
			conferenciaEncalheParcialRepository.alterar(parcial);
		}
		
	}
	
	private boolean verificarConferenciaEncalheFinalizadaParaData(Map<Date, StatusOperacao> mapaControleConferencia, Date dataMovimento) {
	
		if( mapaControleConferencia.get(dataMovimento) == null ) {

			ControleConferenciaEncalhe controleConferenciaEncalhe = controleConferenciaEncalheRepository.obterControleConferenciaEncalhe(dataMovimento);
			
			if(controleConferenciaEncalhe == null || controleConferenciaEncalhe.getStatus() == null) {
				mapaControleConferencia.put(dataMovimento, StatusOperacao.EM_ANDAMENTO);
				return false;
			}
			
			StatusOperacao statusOperacao = controleConferenciaEncalhe.getStatus();
			
			mapaControleConferencia.put(dataMovimento, statusOperacao);
		
			return StatusOperacao.CONCLUIDO.equals(statusOperacao);
			
		} else {
		
			return StatusOperacao.CONCLUIDO.equals(mapaControleConferencia.get(dataMovimento));
			
		}
		
	}
	
	/**
	 * Obtém uma lista de ContagemDevolucao a partir de registros
	 * de ConferenciaEncalheParcial que estejam com o seguinte formato
	 * (statusAprovacao = APROVADO, diferencaApurada = false, nfParcialGerada = true) 
	 * e verifica para cada registro se a conferencia de encalhe do mesmo foi 
	 * finalizada, caso positivo, aponta as diferencas entre qtde do movimentoEstoqueCota 
	 * do mesmo e a qtde conferenciaEncalheParcial para o mesmo.
	 * 
	 */
	private void verificarConferenciaEncalheFinalizada(Usuario usuario) {
		
		Map<Date, StatusOperacao> mapaControleConferencia = new HashMap<Date, StatusOperacao>();

		List<ContagemDevolucaoDTO> listaContagemDevolucao = 
				conferenciaEncalheParcialRepository.obterListaContagemDevolucao(false, null, StatusAprovacao.APROVADO, null, null, null, null);
		
		for(ContagemDevolucaoDTO contagemDevolucaoDTO : listaContagemDevolucao) {
			
			boolean indConferenciaEncalheFinalizada = verificarConferenciaEncalheFinalizadaParaData(mapaControleConferencia, contagemDevolucaoDTO.getDataMovimento());
			
			if(indConferenciaEncalheFinalizada) {
				
				ajustarDiferencaConferenciaEncalheContagemDevolucao(contagemDevolucaoDTO, usuario);
				
				sinalizarControleContagemDevolucaoFinalizada(contagemDevolucaoDTO);
				
			}
			
		}
				
	
	}
	
	private void sinalizarControleContagemDevolucaoFinalizada(ContagemDevolucaoDTO contagem) {
		
		Date dataMovimento = contagem.getDataMovimento();
		Long idProdutoEdicao = contagem.getIdProdutoEdicao();
		
		ControleContagemDevolucao controleContagemDevolucao = 
				controleContagemDevolucaoRepository.obterControleContagemDevolucao(dataMovimento, idProdutoEdicao);
		
		if(controleContagemDevolucao != null && StatusOperacao.CONCLUIDO.equals(controleContagemDevolucao.getStatus())) {
			return;
		}
		
		if(controleContagemDevolucao == null) {

			ProdutoEdicao produtoEdicao = new ProdutoEdicao();
			
			produtoEdicao.setId(idProdutoEdicao);
			
			controleContagemDevolucao = new ControleContagemDevolucao();
			controleContagemDevolucao.setData(dataMovimento);
			controleContagemDevolucao.setProdutoEdicao(produtoEdicao);
			controleContagemDevolucao.setStatus(StatusOperacao.CONCLUIDO);
			
			controleContagemDevolucaoRepository.adicionar(controleContagemDevolucao);
			
		} else {
			controleContagemDevolucao.setStatus(StatusOperacao.CONCLUIDO);
			controleContagemDevolucaoRepository.alterar(controleContagemDevolucao);
		}
		
	}
	
	/**
	 * Separa os itens a serem utilizados na geração da NF por fornecedor, gerando assim 
	 * um NF para cada grupo de produtos de um 
	 * 
	 * @param listaContagemDevolucaoAprovada
	 */
	@Override
	@Transactional
	public void gerarNotasFiscaisPorFornecedor(List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada) {
		
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
		
		for(Fornecedor fornecedor : mapaFornecedorListaContagemDevolucao.keySet()) {
			
			gerarNotaFiscalParcial(fornecedor, mapaFornecedorListaContagemDevolucao.get(fornecedor));
			
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
	 */
	private void gerarNotaFiscalParcial( Fornecedor fornecedor, List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada ) {
		
		List<ContagemDevolucaoDTO> listaAgrupadaContagemDevolucao = obterListaContagemDevolucaoAprovadaTotalAgrupado(listaContagemDevolucaoAprovada);

		if(listaAgrupadaContagemDevolucao == null || listaAgrupadaContagemDevolucao.isEmpty()) {
			return;
		}
		
		NotaFiscalSaidaFornecedor nfSaidaFornecedor = new NotaFiscalSaidaFornecedor();
		List<ItemNotaFiscalSaida> itensNotaFiscalSaida = new ArrayList<ItemNotaFiscalSaida>();
		
		carregarDadosNFSaida(nfSaidaFornecedor, itensNotaFiscalSaida,  listaAgrupadaContagemDevolucao);
		
		if(itensNotaFiscalSaida.isEmpty()) {
			return;
		}
		
		Date dataAtual = new Date();
		
		StatusEmissaoNotaFiscal statusNF = StatusEmissaoNotaFiscal.AGUARDANDO_GERACAO_NFE;
		
		ParametroEmissaoNotaFiscal parametroEmissaoNF = parametroEmissaoNotaFiscalRepository.obterParametroEmissaoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);
		
		if(parametroEmissaoNF == null) {
			throw new IllegalStateException("Nota Fiscal Saida não parametrizada no sistema");
		}
		
		CFOP cfop = parametroEmissaoNF.getCfopDentroEstado();
		String serieNF = parametroEmissaoNF.getSerieNF();

		Distribuidor distribuidor = distribuidorService.obter();

		if(distribuidor == null) {
			throw new IllegalStateException("Informações do distribuidor não encontradas");
		}

		TipoNotaFiscal tipoNF = tipoNotaFiscalRepository.obterTipoNotaFiscal(GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR);

		if(tipoNF == null) {
			throw new IllegalStateException("TipoNotaFiscal não parametrizada");
		}
		
		Long numeroNF = controleNumeracaoNotaFiscalService.obterProximoNumeroNotaFiscal(serieNF);
		
		nfSaidaFornecedor.setCfop(cfop);
		nfSaidaFornecedor.setDataEmissao(dataAtual);
		nfSaidaFornecedor.setDataExpedicao(dataAtual);
		nfSaidaFornecedor.setFornecedor(fornecedor);

		nfSaidaFornecedor.setNumero(numeroNF);
		nfSaidaFornecedor.setSerie(serieNF);
		nfSaidaFornecedor.setStatusEmissao(statusNF);
		nfSaidaFornecedor.setTipoNotaFiscal(tipoNF);
		
		notaFiscalSaidaRepository.adicionar(nfSaidaFornecedor);
		
		nfSaidaFornecedor.setNumero(nfSaidaFornecedor.getId());
		nfSaidaFornecedor.setSerie(nfSaidaFornecedor.getId().toString());
		notaFiscalSaidaRepository.alterar(nfSaidaFornecedor);
		
		inserirItensNotaFiscalSaida(nfSaidaFornecedor, itensNotaFiscalSaida);
		
	}
	
	
	
	
	/**
	 * Gera e retorna uma lista de ContagemDevolucao, 
	 * sendo que cada item da lista contem os seguintes valores:
	 * 
	 * precoVenda 		- preco do produtoEdicao.
	 * 
	 * dataMovimento 	- data do movimento 
	 * 
	 * qtdDevolucao     - qtd total registrada no movimento 
	 * estoque cota para a dataMovimento e produtoEdicao
	 * 
	 * qtdNota			- qtd total confirmada em tela
	 * para a dataMovimento e produtoEdicao
	 * 
	 * @param listaContagemDevolucaoAprovada
	 * 
	 * @return List<ContagemDevolucaoDTO>
	 */
	private List<ContagemDevolucaoDTO> obterListaContagemDevolucaoAprovadaTotalAgrupado(List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada) {
		
		List<ContagemDevolucaoDTO> listaAgrupadaContagemDevolucao = new ArrayList<ContagemDevolucaoDTO>();
		
		for(ContagemDevolucaoDTO contagemDevolucaoAprovada : listaContagemDevolucaoAprovada) {

			List<ContagemDevolucaoDTO> contagemAgrupada = conferenciaEncalheParcialRepository.
					obterListaContagemDevolucao(
					false, 
					false, 
					StatusAprovacao.APROVADO, 
					null,
					contagemDevolucaoAprovada.getCodigoProduto(), 
					contagemDevolucaoAprovada.getNumeroEdicao(),
					contagemDevolucaoAprovada.getDataMovimento());

			if(contagemAgrupada == null || contagemAgrupada.isEmpty()) {
				continue;
			}
			
			listaAgrupadaContagemDevolucao.addAll(contagemAgrupada);
			
		}
		
		return listaAgrupadaContagemDevolucao;
		
	}
	
	/**
	 * Carrega os dados NFSaidaFornecedor(Valor Bruto, Liquido e Desconto) e 
	 * ItensNFSaidaFornecedor a partir da lista de ContagemDevolucao.
	 * 
	 * @param nfSaidaFornecedor
	 * @param itensNotaFiscalSaida
	 * @param listaContagemDevolucao
	 */
	private void carregarDadosNFSaida(
			NotaFiscalSaidaFornecedor nfSaidaFornecedor, 
			List<ItemNotaFiscalSaida> itensNotaFiscalSaida,
			List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		
		BigDecimal valorTotal = BigDecimal.ZERO;
		
		for(ContagemDevolucaoDTO contagem : listaContagemDevolucao) {
			
			if(contagem.getIdProdutoEdicao() != null && contagem.getQtdNota() != null && contagem.getQtdNota().doubleValue() > 0.0D) {
				
				if(contagem.getPrecoVenda()!=null) {
					valorTotal = valorTotal.add( contagem.getPrecoVenda().multiply( new BigDecimal(contagem.getQtdNota()) ) );
				}
				
				ProdutoEdicao produtoEdicao = new ProdutoEdicao();
				produtoEdicao.setId(contagem.getIdProdutoEdicao());
				
				ItemNotaFiscalSaida item = new ItemNotaFiscalSaida();
				
				item.setId(null);
				item.setNotaFiscal(null);
				item.setProdutoEdicao(produtoEdicao);
				item.setQtde(contagem.getQtdNota());
				
				itensNotaFiscalSaida.add(item);
				
				sinalizarItemNFParcialGerada(contagem);
				
			}
			
		}
		
		nfSaidaFornecedor.setValorBruto(valorTotal);
		nfSaidaFornecedor.setValorLiquido(valorTotal);
		nfSaidaFornecedor.setValorDesconto(BigDecimal.ZERO);
		
	}
	
	/**
	 * Insere os itens da Nota Fiscal Saida Fornecedor
	 * 
	 * @param nfSaidaFornecedor
	 * @param itensNotaFiscalSaida
	 */
	private void inserirItensNotaFiscalSaida(NotaFiscalSaidaFornecedor nfSaidaFornecedor, List<ItemNotaFiscalSaida> itensNotaFiscalSaida) {
		
		for( ItemNotaFiscalSaida item : itensNotaFiscalSaida ) {
			item.setNotaFiscal(nfSaidaFornecedor);
			itemNotaFiscalSaidaRepository.adicionar(item);
		}
		
	}

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public byte[] gerarCEDevolucao(
            List<ContagemDevolucaoDTO> listaContagemDevolucaoAprovada) {
        ImpressaoCEDevolucaoDTO dto = new ImpressaoCEDevolucaoDTO();
        dto.setDataEmissao(new Date());
        dto.setDataRecolhimento(new Date());
        EnderecoDTO enderecoDistribuidor = new EnderecoDTO(null, "Centro",
                "13720-000", null, "São José do Rio Pardo", null, "Rua",
                "Treze de Maio ", "150", "SP", null);

        dto.setDistribuidor(new IdentificacaoImpressaoCEDevolucaoDTO(
                "Acme Distribuidora", enderecoDistribuidor,
                "07.564.178/0001-04", "646.123.456.789"));
        
        EnderecoDTO enderecoFornecedor = new EnderecoDTO(null, "",
                "60842-395", null, "Fortaleza", null, "Rodovia",
                "BR 116 ", "3200", "CE", null);
        
        dto.setFornecedor(new IdentificacaoImpressaoCEDevolucaoDTO(
                "Treelog S/A Logística e Distribuição", enderecoFornecedor,
                "61.438.248/0062-45", "060084472"));

        dto.setNumero(Long.MAX_VALUE);
     
        BigDecimal totalBruto = BigDecimal.ZERO;
        BigDecimal totalDesconto = BigDecimal.ZERO;
        BigDecimal totalLiquido = BigDecimal.ZERO;
        for (int i = 1; i <= 20; i++) {
            ProdutoImpressaoCEDevolucaoDTO produto = new ProdutoImpressaoCEDevolucaoDTO();
            produto.setCodigo(String.valueOf(i));
            produto.setDataLancamento(new Date());
            produto.setDesconto(BigDecimal.ONE);
            produto.setDevolucao(BigInteger.TEN);
            produto.setEdicao(Long.valueOf(i + 100));
            produto.setPrecoDesconto(BigDecimal.TEN);
            produto.setProduto("Caras " + i);
            produto.setReparte(BigInteger.valueOf(100));
            produto.setSequenciaMatriz(i);
            produto.setValorVenda(BigDecimal.valueOf(810));
            produto.setVenda(BigInteger.valueOf(90));
            produto.setTipoRecolhimento("P");
            totalDesconto = totalDesconto.multiply(produto.getDesconto().multiply(new BigDecimal(produto.getVenda())));
            totalLiquido = totalLiquido.add(produto.getValorVenda());
            totalBruto = totalBruto.add(totalLiquido.add(totalDesconto));
            dto.addProduto(produto);
        }
        dto.setTotalBruto(totalBruto);
        dto.setTotalDesconto(totalDesconto);
        dto.setTotalLiquido(totalLiquido);
        
        URL url = 
            Thread.currentThread().getContextClassLoader().getResource("/reports/CEDevolucao.jasper");
        
        try {
            String path = url.toURI().getPath();
            return JasperRunManager.runReportToPdf(path,
                    new HashMap<String, Object>(), new ImpressaoCEDataSource(dto));
        } catch (Exception ex) {
            LOG.error("Erro gerando arquivo CE Devolução!", ex);
            throw new RuntimeException("Erro gerando arquivo CE Devolução!", ex);
        }
    }
	
}
