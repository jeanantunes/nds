package br.com.abril.nds.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.enums.Dominio;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal.DistribuidorGrupoNotaFiscal;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoFiscal;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.OrigemItem;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Condicao;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.model.fiscal.nota.pk.NotaFiscalReferenciadaPK;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EncargoFinanceiroRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TributacaoService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.service.xml.nfe.signature.SignatureHandler;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.inf.portalfiscal.nfe.util.Util;
import br.inf.portalfiscal.nfe.util.XmlDomUtils;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.nota.NotaFiscal}
 * 
 * @author Discover Technology
 * 
 */
@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotaFiscalServiceImpl.class);

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Autowired
	private ParametroSistemaService parametroSistemaService;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private NaturezaOperacaoRepository naturezaOperacaoRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private TributacaoService tributacaoService;

	@Autowired
	private SerieRepository serieRepository;

	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private PdvRepository pdvRepository;

	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;

	@Autowired
	private EncargoFinanceiroRepository encargoFinanceiroRepository;

	@Autowired
	private DescontoService descontoService;

	@Autowired
	private RoteirizacaoRepository roterizacaoRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;

	@Autowired
	private GeracaoNotaEnvioService geracaoNotaEnvioService;

	@Autowired
	private SignatureHandler signatureHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#
	 * obterTotalItensNotaFiscalPorCotaEmLote
	 * (br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO)
	 */
	@Override
	@Transactional
	public Map<Cota, QuantidadePrecoItemNotaDTO> obterTotalItensNotaFiscalPorCotaEmLote(ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {

		Intervalo<Date> periodo = dadosConsultaLoteNotaFiscal.getPeriodoMovimento();

		Set<NaturezaOperacao> tiposNotaFiscal = dadosConsultaLoteNotaFiscal.getTipoNotaFiscal();

		List<Long> listaIdFornecedores = dadosConsultaLoteNotaFiscal.getListaIdFornecedores();

		Map<Cota, QuantidadePrecoItemNotaDTO> idCotaTotalItensNota = new HashMap<Cota, QuantidadePrecoItemNotaDTO>();

		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = this.distribuidorRepository.parametrosRecolhimentoDistribuidor();

		TipoAtividade tipoAtividade = this.distribuidorRepository.tipoAtividade();

		for (Long idCota : dadosConsultaLoteNotaFiscal.getIdsCotasDestinatarias()) {

			Cota cota = this.cotaRepository.buscarPorId(idCota);

			for (NaturezaOperacao tipoNotaFiscal : tiposNotaFiscal) {

				if (tipoNotaFiscal.getTipoAtividade().equals(tipoAtividade)) {

					if (cota.getParametrosCotaNotaFiscalEletronica() != null) {

						if (cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica()) {

							List<ItemNotaFiscalSaida> itensNotaFiscal = obterItensNotaFiscalPor(
									parametrosRecolhimentoDistribuidor, cota,
									periodo, listaIdFornecedores, null,
									tipoNotaFiscal);

							if (itensNotaFiscal != null && !itensNotaFiscal.isEmpty()) {
								idCotaTotalItensNota.put(cota,this.sumarizarTotalItensNota(itensNotaFiscal));
							}
						}
					}
				}
			}
		}

		return idCotaTotalItensNota;
	}

	@Override
	public List<NotaFiscal> gerarDadosNotaFicalEmLote(ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#processarRetornoNotaFiscal
	 * (br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public List<RetornoNFEDTO> processarRetornoNotaFiscal(List<RetornoNFEDTO> listaDadosRetornoNFE) {

		List<RetornoNFEDTO> listaDadosRetornoNFEProcessados = new ArrayList<RetornoNFEDTO>();

		for (RetornoNFEDTO dadosRetornoNFE : listaDadosRetornoNFE) {

			if (dadosRetornoNFE.getNumeroNotaFiscal() != null || dadosRetornoNFE.getProtocolo() != null) {
				NotaFiscal notaFiscal = null;

				if (dadosRetornoNFE.getStatus().equals(StatusRetornado.CANCELAMENTO_HOMOLOGADO)) {
					notaFiscal = this.notaFiscalRepository.obterChaveAcesso(dadosRetornoNFE);
				} else {
					notaFiscal = this.notaFiscalRepository.buscarNotaFiscalChaveAcesso(dadosRetornoNFE.getChaveAcesso());
				}

				if (notaFiscal != null) {
					InformacaoEletronica informacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();

					if (informacaoEletronica.getChaveAcesso().equals(dadosRetornoNFE.getChaveAcesso())) {

						if (StatusProcessamento.EM_PROCESSAMENTO.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento())) {
							if (StatusRetornado.AUTORIZADO.equals(dadosRetornoNFE.getStatus()) || StatusRetornado.USO_DENEGADO.equals(dadosRetornoNFE.getStatus())) {
								listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
							}

						} else if (StatusProcessamento.RETORNADA.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento())) {

							if (StatusRetornado.AUTORIZADO.equals(informacaoEletronica.getRetornoComunicacaoEletronica().getStatusRetornado())
									|| StatusRetornado.CANCELAMENTO_HOMOLOGADO.equals(dadosRetornoNFE.getStatus())) {

								listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
							}
						} else if (StatusProcessamento.SOLICITACAO_CANCELAMENTO.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento())) {
							listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
						} else {
							throw new ValidacaoException(TipoMensagem.ERROR, "A chave de acesso do arquivo não confere com a base de dados.");
						}
					}
				}
			}
		}

		return listaDadosRetornoNFEProcessados;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancelarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {

		System.out.println("Realizar a solicitação de um pedido de cancelamento da nota");

		// validar se a nota fiscal emitida esta no prazo de 24 horas
		if (Util.diferencaEntreDatasEmDias(dadosRetornoNFE.getDataRecebimento(), new Date()) > 1) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "O cancelamento da nota não pode ser realizado, a data ultrapassa o prazo limite de 24 horas.");
		}

		NotaFiscal notaFiscal = this.notaFiscalRepository.buscarNotaFiscalChaveAcesso(dadosRetornoNFE.getChaveAcesso());
		if(notaFiscal != null && notaFiscal.getNotaFiscalInformacoes() != null && notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() != null) {
			
			for(DetalheNotaFiscal detNF : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				for(OrigemItemNotaFiscal origemItem : detNF.getProdutoServico().getOrigemItemNotaFiscal()) {
					if(origemItem.getOrigem().equals(OrigemItem.MOVIMENTO_ESTOQUE_COTA)) {
						
						MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) origemItem).getMovimentoEstoqueCota();
						mec.setNotaFiscalEmitida(false);
					}
				}
			}
		}
		
//		this.gerarArquivoSolicitacaoCancelamento(notaFiscal);

	}

	private void gerarArquivoSolicitacaoCancelamento(NotaFiscal notaFiscal) throws TransformerFactoryConfigurationError {
		try {
			
			Document document = this.criarDocumentoCancelamento(notaFiscal);
			ParametroSistema diretorioSaida = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
			String numeroNF = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNumeroDocumentoFiscal().toString();
			String serieNF = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getSerie().toString();

			OutputStream os2 = new FileOutputStream(diretorioSaida.getValor()
					+ "/" + "NF-e-Solicitacao-Cancelamento" + serieNF + "-"
					+ numeroNF + ".xml");
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = null;

			trans = tf.newTransformer();
			trans.transform(new DOMSource(document), new StreamResult(os2));
			os2.flush();
			os2.close();
		} catch (ParserConfigurationException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao realizar o parser do documento de cancelamento.");
		} catch (Exception e) {
			LOGGER.error("Erro ao gerar XML", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro no transporter do arquivo gerado.");
		}
	}

	/**
	 * indetifica se a nota foi gerada com a condição de falta de mercadoria.
	 * 
	 * @param naturezaOprecao
	 * @return
	 */
	private boolean isFaltaMercadoria(NotaFiscal notaFiscal) {
		return Condicao.FALTA_MERCADORIA == notaFiscal
				.getNotaFiscalInformacoes().getCondicao();
	}

	/**
	 * indetifica se a nota foi gerada com a condição de devolução de encalhe.
	 * 
	 * @param naturezaOprecao
	 * @return
	 */
	private boolean isDevolucaoEncalhe(NotaFiscal notaFiscal) {
		return Condicao.DEVOLUCAO_ENCALHE == notaFiscal
				.getNotaFiscalInformacoes().getCondicao();
	}

	/**
	 * indentifica se a nota foi gerada com a condição de sobra de mercadoria.
	 * 
	 * @param naturezaOprecao
	 * @return
	 */
	private boolean isSobraMercadoria(NotaFiscal notaFiscal) {
		return Condicao.SOBRA_MERCADORIA == notaFiscal
				.getNotaFiscalInformacoes().getCondicao();
	}

	/**
	 * Identifica se é um nota de Devolução de Mercadoria Recebida em
	 * Consignação.
	 * 
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isDevolucaoMerdadoriaRecebiaConsignacao(
			NaturezaOperacao tipoNotaFiscal) {
		/*
		 * return tipoNotaFiscal.getGrupoNotaFiscal() ==
		 * GrupoNotaFiscal.NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO &&
		 * tipoNotaFiscal.getEmitente() == TipoUsuarioNotaFiscal.DISTRIBUIDOR &&
		 * tipoNotaFiscal.getDestinatario() == TipoUsuarioNotaFiscal.TREELOG;
		 */

		return false;
	}

	/**
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isRemessaMercadoriaConsignacao(NaturezaOperacao tipoNotaFiscal) {
		/*
		 * return tipoNotaFiscal.getGrupoNotaFiscal() ==
		 * GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO &&
		 * tipoNotaFiscal.getEmitente() == TipoUsuarioNotaFiscal.DISTRIBUIDOR &&
		 * tipoNotaFiscal.getDestinatario() == TipoUsuarioNotaFiscal.COTA;
		 */
		return false;
	}

	@Override
	@Transactional
	public void denegarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {
		atualizaRetornoNFe(dadosRetornoNFE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#autorizarNotaFiscal(br.com
	 * .abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public NotaFiscal autorizarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {

		NotaFiscal notaFiscal = atualizaRetornoNFe(dadosRetornoNFE);
		Cota cota = notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota();
		
		if(cota != null ){			
			if(cota.getParametrosCotaNotaFiscalEletronica() != null) {			
				gerarNotaEnvioAtravesNotaFiscal(notaFiscal.getId(), cota, notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao());
			}
		}

		return notaFiscal;
	}

	@Transactional
	public void gerarNotaEnvioAtravesNotaFiscal(Long notaFiscalId, Cota cota, NaturezaOperacao naturezaOperacao) {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		TipoImpressaoNENECADANFE tipoImpressao = distribuidorRepository.tipoImpressaoNENECADANFE();
		switch (tipoImpressao) {

		case MODELO_1:
		case MODELO_2:
			if (!distribuidor.isPossuiRegimeEspecialDispensaInterna() || 
				((cota.getParametrosCotaNotaFiscalEletronica() != null &&  
				cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica() != null) ? cota.getParametrosCotaNotaFiscalEletronica().isExigeNotaFiscalEletronica() : false)) {

				for (DistribuidorTipoNotaFiscal distribuidorTipoNotaFiscal : distribuidor.getTiposNotaFiscalDistribuidor()) {
					if (distribuidorTipoNotaFiscal.getNaturezaOperacao().contains(naturezaOperacao)) {

						if (distribuidorTipoNotaFiscal.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_ENVIO_PARA_COTA)
								|| distribuidorTipoNotaFiscal.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_DEVOLUCAO_PELA_COTA)
								|| distribuidorTipoNotaFiscal.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_VENDA)) {

							this.geracaoNotaEnvioService.gerarNotaEnvioAtravesNotaFiscal(notaFiscalRepository.buscarPorId(notaFiscalId));

						}

					}
				}
			}

			break;

		default:
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração da nota de envio!");
		}
	}

	/**
	 * Atualiza o Retorno de um NotaFiscal que já foi enviada.
	 * 
	 * @param dadosRetornoNFE
	 */
	private NotaFiscal atualizaRetornoNFe(RetornoNFEDTO dadosRetornoNFE) {

		NotaFiscal notaFiscal = this.notaFiscalRepository.obterNotaFiscalNumeroChaveAcesso(dadosRetornoNFE);

		InformacaoEletronica informacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();
		
		if (informacaoEletronica == null) {
			notaFiscal.getNotaFiscalInformacoes().setInformacaoEletronica(new InformacaoEletronica());
			informacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();
		}

		informacaoEletronica.setChaveAcesso(dadosRetornoNFE.getChaveAcesso());

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(dadosRetornoNFE.getDataRecebimento());
		retornoComunicacaoEletronica.setMotivo(dadosRetornoNFE.getMotivo());
		retornoComunicacaoEletronica.setProtocolo(dadosRetornoNFE.getProtocolo());
		retornoComunicacaoEletronica.setStatusRetornado(dadosRetornoNFE.getStatus());

		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);
		notaFiscal.getNotaFiscalInformacoes().setInformacaoEletronica(informacaoEletronica);
		notaFiscal.getNotaFiscalInformacoes().setStatusProcessamento(StatusProcessamento.RETORNADA);

		this.notaFiscalRepository.merge(notaFiscal);
		this.notaFiscalRepository.flush();
		return notaFiscal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#enviarNotaFiscal(java.lang
	 * .Long)
	 */
	@Override
	@Transactional
	public void enviarNotaFiscal(Long id) {

		NotaFiscal notaFiscal = this.notaFiscalRepository.buscarPorId(id);

		if (notaFiscal != null) {
			notaFiscal.getNotaFiscalInformacoes().setStatusProcessamento(StatusProcessamento.EM_PROCESSAMENTO);
			this.notaFiscalRepository.merge(notaFiscal);
		}
	}

	@Override
	@Transactional
	public synchronized void exportarNotasFiscais(Long... idNotaFiscals) throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		List<NotaFiscal> notasFiscaisParaExportacao = new ArrayList<NotaFiscal>(idNotaFiscals.length);
		
		for (Long id : idNotaFiscals) {
			notasFiscaisParaExportacao.add(notaFiscalRepository.buscarPorId(id));
		}

		exportarNotasFiscais(notasFiscaisParaExportacao);

		try {
			gerarArquivoNota(notasFiscaisParaExportacao);
		} catch (JAXBException e) {
			LOGGER.error("Erro ao gerar arquivo da nota fiscal.", e);
		} catch (Exception e) {
			LOGGER.error("Erro ao gerar arquivo da nota fiscal.", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#exportarNotasFiscais()
	 */
	@Override
	@Transactional
	public synchronized void exportarNotasFiscais(List<NotaFiscal> notasFiscaisParaExportacao) throws FileNotFoundException, IOException {

		// String dados = "";

		try {

			this.gerarArquivoNota(notasFiscaisParaExportacao);

		} catch (Exception e) {

			if (e instanceof ValidacaoException) {

				ValidacaoException ex = (ValidacaoException) e;

				StringBuilder msgs = new StringBuilder();

				for (String msg : ex.getValidacao().getListaMensagens()) {

					if (msgs.length() != 0) {
						msgs.append(", ");
					}

					msgs.append(msg);
				}

				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar arquivo de nota: " + msgs.toString());
			}

			LOGGER.error("Falha ao gerar arquivo de exportação: ", e);
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Falha ao gerar arquivo de exportação: " + e.getMessage()));
		}

		ParametroSistema pathNFEExportacao = this.parametroSistemaService
				.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);

		if (pathNFEExportacao == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe o diretório de exportação das notas na tela de parametros do sistema"));
		}

		/*
		 * File diretorioExportacaoNFE = new File(pathNFEExportacao.getValor());
		 * 
		 * if (!diretorioExportacaoNFE.isDirectory()) { throw new
		 * FileNotFoundException( "O diretório["+pathNFEExportacao.getValor()+
		 * "] de exportação parametrizado não é válido!"); } Long time = new
		 * Date().getTime(); File notaExportacao = new
		 * File(diretorioExportacaoNFE + File.separator + new
		 * File("NFeExportacao" + time + ".txt"));
		 * 
		 * FileWriter fileWriter;
		 * 
		 * fileWriter = new FileWriter(notaExportacao);
		 * 
		 * BufferedWriter buffer = new BufferedWriter(fileWriter);
		 * 
		 * buffer.write(dados);
		 * 
		 * buffer.close();
		 */

		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {
			this.enviarNotaFiscal(notaFiscal.getId());
		}
	}

	private String gerarArquivoNota(List<NotaFiscal> notasFiscaisParaExportacao)
			throws Exception {

		StringBuilder sBuilder = new StringBuilder();

		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {

			JAXBContext jc;
			try {

				long index = 1;
				for (DetalheNotaFiscal dnf : notaFiscal
						.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
					dnf.setSequencia(index++);
				}

				jc = JAXBContext.newInstance(NotaFiscal.class);

				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);
				DocumentBuilder documentBuilder;
				try {
					
					documentBuilder = documentBuilderFactory.newDocumentBuilder();
					Document document = documentBuilder.newDocument();

					XmlDomUtils.removeUnusedNamespaces(document);

					marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

					marshaller.marshal(notaFiscal, document);

					/*
					 * Trecho para corrigir o prefix ns2
					 */
					Element root = document.getDocumentElement();

					// document.getFirstChild().setPrefix(null);
					// NamedNodeMap nnm =
					// document.getFirstChild().getAttributes();
					// Node nodeXmlsNS = nnm.getNamedItem("xmlns:ns2");
					// Attr xmlns = document.createAttribute("xmlns");
					// xmlns.setNodeValue(nodeXmlsNS.getNodeValue());
					// nnm.setNamedItem(xmlns);
					// nnm.removeNamedItem("xmlns:ns2");
					// document.getFirstChild().getAttributes().removeNamedItem("xmlns:ns2");

					// document.getDocumentElement().removeAttribute("xmlns:ns2");
					// ((Element)
					// document.getDocumentElement().getElementsByTagName("ns2:NFe").item(0)).setAttribute("xmlns",
					// "http://www.portalfiscal.inf.br/nfe");

					NodeList elements = document.getElementsByTagName("infNFe");
					Element el = (Element) elements.item(0);
					el.setIdAttribute("Id", true);

					// document.normalizeDocument();

					XmlDomUtils.removeUnusedNamespaces(document.getFirstChild());

					ParametroSistema diretorioSaida = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
					String numeroNF = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getCodigoNF();
					String serieNF = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getSerie().toString();
					
					Integer numeroCota = null;
					
					if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota()!= null) {
						
						numeroCota = notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota().getNumeroCota();
					}
					
					OutputStream os = null;
					
					if(numeroCota != null ) {
						os = new FileOutputStream(diretorioSaida.getValor() + "/" + "NF-e-" + numeroCota + serieNF + "-" + numeroNF + ".xml");						
					} else {
						os = new FileOutputStream(diretorioSaida.getValor() + "/" + "NF-e-" + serieNF + "-" + numeroNF + ".xml");
					}
					
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer trans = null;

					trans = tf.newTransformer();
					trans.setOutputProperty(OutputKeys.INDENT, "yes");
					trans.transform(new DOMSource(root), new StreamResult(os));

					os.flush();
					os.close();
					
					if(numeroCota != null ) {
						ajustaXml(new File(diretorioSaida.getValor() +"/"+ "NF-e-" + numeroCota + "-" + serieNF + "-" + numeroNF + ".xml"));
					} else {
						ajustaXml(new File(diretorioSaida.getValor() +"/"+ "NF-e-" + serieNF + "-" + numeroNF + ".xml"));
					}
					
					// Instantiate the document to be signed.   
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();   
					dbf.setNamespaceAware(true);   
					
					Document doc = null;
					
					if(numeroCota != null ) {						
						doc = dbf.newDocumentBuilder().parse(new FileInputStream(new File(diretorioSaida.getValor() +"/"+ "NF-e-" + numeroCota + "-" + serieNF + "-" + numeroNF + ".xml")));   
					} else {
						doc = dbf.newDocumentBuilder().parse(new FileInputStream(new File(diretorioSaida.getValor() +"/"+ "NF-e-" + serieNF + "-" + numeroNF + ".xml")));
					}
					
					NodeList elementos = doc.getElementsByTagName("infNFe");
					Element elo = (Element) elementos.item(0);
					elo.setIdAttribute("Id", true);
					
					signatureHandler.sign(new DOMStructure(doc.getDocumentElement()), "infNFe");
					
					if(numeroCota != null ) {						
						os = new FileOutputStream(diretorioSaida.getValor() + "/" + "NF-e-" + numeroCota + "-" + serieNF + "-" + numeroNF + ".xml");
					} else {
						os = new FileOutputStream(diretorioSaida.getValor() + "/" + "NF-e-" + serieNF + "-" + numeroNF + ".xml");
					}
					
					tf = TransformerFactory.newInstance();
					trans = null;
					
					trans = tf.newTransformer();
					trans.transform(new DOMSource(doc), new StreamResult(os));
					
				} catch (ParserConfigurationException e) {
					LOGGER.error("Erro ao gerar XML", e);
					throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar XML.");
				}

			} catch (JAXBException e) {
				LOGGER.error("Erro ao gerar XML", e);
				throw new JAXBException("Erro ao gerar XML.");
			} catch (FileNotFoundException e) {
				LOGGER.error("Erro ao gerar XML", e);
				throw new JAXBException("Erro ao gerar XML");
			}

		}

		return "NOTA FISCAL|" + notasFiscaisParaExportacao.size() + "|\n" + sBuilder.toString();
	}

	// passa-se o file a ser re-formatado como string
	public void ajustaXml(File file) throws Exception {
		FileReader reader = new FileReader(file);
		BufferedReader leitor = new BufferedReader(reader);

		leitor.read();

		String vlr = "";
		StringBuffer vlrFile = new StringBuffer();
		String line = leitor.readLine();

		while (line != null) {
			vlrFile.append(line);
			line = leitor.readLine();
		}

		vlr = vlrFile.toString();

		if (vlr.indexOf("ns2:") > -1) {
			vlr = vlr.replaceAll("ns2:", "");
		}

		if (vlr.indexOf(":ns2") > -1) {
			vlr = vlr.replaceAll(":ns2", "");
		}

		leitor.close();
		reader.close();

		FileWriter writer = new FileWriter(file);
		PrintWriter saida = new PrintWriter(writer);

		saida.print("<" + vlr);

		writer.close();
		saida.close();
	}

	/**
	 * Carrega Grupo das informações de identificação da NF-e
	 * 
	 * @param idTipoNotaFiscal
	 * @param dataEmissao
	 * @param listNotaFiscalReferenciada
	 * @return
	 */
	private Identificacao carregaIdentificacao(NaturezaOperacao naturezaOperacao, Date dataEmissao, List<NotaFiscalReferenciada> listNotaFiscalReferenciada) {

		Identificacao identificacao = new Identificacao();
		identificacao.setDataEmissao(dataEmissao);
		identificacao.setTipoOperacao(naturezaOperacao.getTipoOperacao());
		// identificacao.setDescricaoNaturezaOperacao(tipoNotaFiscal.getNopDescricao());
		// identificacao.setSerie(tipoNotaFiscal.getSerieNotaFiscal());
		// identificacao.setNumeroDocumentoFiscal(serieRepository.next(tipoNotaFiscal.getSerieNotaFiscal()));
		identificacao.setNaturezaOperacao(naturezaOperacao);
		// TODO indPag
		identificacao.setFormaPagamento(FormaPagamento.A_VISTA);
		identificacao.setTipoEmissao(TipoEmissao.NORMAL);

		if(identificacao.getListReferenciadas() == null) {
			identificacao.setListReferenciadas(new ArrayList<NotaFiscalReferenciada>());
		} else {
			identificacao.setListReferenciadas(listNotaFiscalReferenciada);
		}
		
		return identificacao;
	}

	/**
	 * Carrega Grupo de identificação do emitente da NF-e
	 * 
	 * @return
	 */
	/*
	 * 
	 * private IdentificacaoEmitente carregaEmitente() { IdentificacaoEmitente
	 * identificacaoEmitente = new IdentificacaoEmitente();
	 * 
	 * PessoaJuridica pessoaJuridica = this.distribuidorRepository.juridica();
	 * 
	 * String cnpj = Util.removerMascaraCnpj(pessoaJuridica.getCnpj());
	 * 
	 * identificacaoEmitente.setDocumento(cnpj);
	 * identificacaoEmitente.setInscricaoEstadual
	 * (pessoaJuridica.getInscricaoEstadual());
	 * identificacaoEmitente.setInscricaoMunicipal
	 * (pessoaJuridica.getInscricaoMunicipal());
	 * identificacaoEmitente.setNome(pessoaJuridica.getNome());
	 * identificacaoEmitente.setNomeFantasia(pessoaJuridica.getNomeFantasia());
	 * identificacaoEmitente.setPessoaEmitenteReferencia(pessoaJuridica);
	 * 
	 * EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository
	 * .obterEnderecoPrincipal();
	 * 
	 * if (enderecoDistribuidor == null) { throw new
	 * ValidacaoException(TipoMensagem.ERROR,
	 * "Endereço principal do distribuidor não encontrada!"); }
	 * 
	 * try { identificacaoEmitente
	 * .setEndereco(cloneEndereco(enderecoDistribuidor .getEndereco())); } catch
	 * (Exception exception) { throw new ValidacaoException(TipoMensagem.ERROR,
	 * "Erro ao adicionar o endereço do distribuidor!"); }
	 * 
	 * TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository
	 * .obterTelefonePrincipal();
	 * 
	 * if (telefoneDistribuidor != null) { Telefone telefone =
	 * telefoneDistribuidor.getTelefone(); telefoneRepository.detach(telefone);
	 * telefone.setId(null); telefone.setPessoa(null);
	 * telefoneRepository.adicionar(telefone);
	 * identificacaoEmitente.setTelefone(telefone); } // TODO: Como definir o
	 * Regime Tributario identificacaoEmitente
	 * .setRegimeTributario(RegimeTributario.SIMPLES_NACIONAL);
	 * 
	 * return identificacaoEmitente; }
	 */

	/**
	 * Grupo de identificação do Destinatário da NF-e
	 * 
	 * @param cota
	 * @return
	 */

	/**
	 * private IdentificacaoDestinatario carregaDestinatario(Cota cota) {
	 * IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
	 * if (cota == null) { throw new ValidacaoException(TipoMensagem.ERROR,
	 * "Cota deve ser informada!"); }
	 * 
	 * destinatario.setDocumento(cota.getPessoa().getDocumento());
	 * destinatario.setEmail(cota.getPessoa().getEmail());
	 * 
	 * EnderecoCota enderecoCota = cotaRepository
	 * .obterEnderecoPrincipal(cota.getId());
	 * 
	 * if (enderecoCota == null) { throw new
	 * ValidacaoException(TipoMensagem.ERROR, "Endereço principal da cota " +
	 * cota.getNumeroCota() + " não encontrada!"); }
	 * 
	 * try {
	 * destinatario.setEndereco(cloneEndereco(enderecoCota.getEndereco())); }
	 * catch (CloneNotSupportedException e) { throw new
	 * ValidacaoException(TipoMensagem.ERROR,
	 * "Erro ao adicionar o endereço do Emitente!"); }
	 * 
	 * if (cota.getPessoa() instanceof PessoaJuridica) { PessoaJuridica
	 * pessoaJuridica = (PessoaJuridica) cota.getPessoa();
	 * 
	 * String inscricaoEstadual =
	 * Util.truncarValor(Util.removerMascaraCnpj(pessoaJuridica
	 * .getInscricaoEstadual()), 14);
	 * 
	 * destinatario.setInscricaoEstadual(inscricaoEstadual);
	 * 
	 * destinatario.setNomeFantasia(pessoaJuridica.getNomeFantasia()); }
	 * destinatario.setNome(cota.getPessoa().getNome());
	 * destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());
	 * 
	 * 
	 * Telefone telefone =
	 * telefoneCotaRepository.obterTelefonePrincipalCota(cota.getId());
	 * 
	 * if (telefone!=null){ telefoneRepository.detach(telefone);
	 * telefone.setId(null); telefone.setPessoa(null);
	 * telefoneRepository.adicionar(telefone);
	 * destinatario.setTelefone(telefone); }
	 * 
	 * return destinatario; }
	 */

	/**
	 * Grupo de identificação do Destinatário da NF-e
	 * 
	 * @param cota
	 * @return
	 */
	/*
	 * 
	 * private IdentificacaoDestinatario carregaDestinatario(Fornecedor
	 * fornecedor) { IdentificacaoDestinatario destinatario = new
	 * IdentificacaoDestinatario(); if (fornecedor == null) { throw new
	 * ValidacaoException(TipoMensagem.ERROR, "Fornecedordeve ser informada!");
	 * }
	 * 
	 * destinatario.setDocumento(fornecedor.getJuridica().getDocumento());
	 * destinatario.setEmail(fornecedor.getJuridica().getEmail());
	 * 
	 * EnderecoFornecedor enderecoFornecedor = fornecedorRepository
	 * .obterEnderecoPrincipal(fornecedor.getId());
	 * 
	 * if (enderecoFornecedor == null) { throw new
	 * ValidacaoException(TipoMensagem.ERROR,
	 * "Endereço principal do Fornecedor " + fornecedor.getId() +
	 * " não encontrado!"); }
	 * 
	 * try {
	 * destinatario.setEndereco(cloneEndereco(enderecoFornecedor.getEndereco
	 * ())); } catch (CloneNotSupportedException e) { throw new
	 * ValidacaoException(TipoMensagem.ERROR,
	 * "Erro ao adicionar o endereço do Emitente!"); }
	 * destinatario.setInscricaoEstadual
	 * (fornecedor.getJuridica().getInscricaoEstadual());
	 * destinatario.setNomeFantasia(fornecedor.getJuridica().getNomeFantasia());
	 * 
	 * destinatario.setNome(fornecedor.getJuridica().getNome());
	 * destinatario.setPessoaDestinatarioReferencia(fornecedor.getJuridica());
	 * 
	 * TelefoneFornecedor telefoneFornecedor = telefoneFornecedorRepository
	 * .obterTelefonePrincipal(fornecedor.getId()); if (telefoneFornecedor !=
	 * null) { Telefone telefone = telefoneFornecedor.getTelefone();
	 * 
	 * telefoneRepository.detach(telefone); telefone.setId(null);
	 * telefone.setPessoa(null); telefoneRepository.adicionar(telefone);
	 * destinatario.setTelefone(telefone); }
	 * 
	 * return destinatario; }
	 */

	/**
	 * Grupo do detalhamento de Produtos e Serviços da NF-e
	 * 
	 * @param ufOrigem
	 * @param ufDestino
	 * @param raizCNPJ
	 * @param cstICMS
	 * @param raizCNPJ
	 *            ,TipoOperacao tipoOperacao
	 * @return
	 */
	private ProdutoServico carregaProdutoServico(Fornecedor fornecedor,
			long idProdutoEdicao, 
			BigInteger quantidade, 
			int cfop,
			TipoOperacao tipoOperacao, 
			String ufOrigem, String ufDestino,
			int naturezaOperacao, 
			String codigoNaturezaOperacao,
			Date dataVigencia, 
			BigDecimal valorItem, 
			String raizCNPJ,
			String cstICMS) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição "+ idProdutoEdicao + " não encontrado!");
		}

		ProdutoServico produtoServico = new ProdutoServico();

		produtoServico.setCodigoBarras(produtoEdicao.getCodigoDeBarras());
		produtoServico.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		produtoServico.setDescricaoProduto(produtoEdicao.getProduto().getNomeComercial());
		produtoServico.setNcm(produtoEdicao.getProduto().getTipoProduto().getNcm().getCodigo());
		produtoServico.setProdutoEdicao(produtoEdicao);
		produtoServico.setQuantidade(quantidade);

		if (produtoEdicao.getDescontoLogistica() != null) {

			BigDecimal valorDesconto = valorItem.multiply(produtoEdicao.getDescontoLogistica().getPercentualDesconto()).divide(new BigDecimal(100));

			produtoServico.setValorUnitario(valorItem.subtract(valorDesconto));

			produtoServico.setValorDesconto(valorDesconto);

			valorItem = produtoServico.getValorUnitario();
		} else if (produtoEdicao.getProduto().getDescontoLogistica() != null) {

			BigDecimal valorDesconto = valorItem.multiply(produtoEdicao.getProduto().getDescontoLogistica().getPercentualDesconto()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);

			produtoServico.setValorUnitario(valorItem.subtract(valorDesconto));

			produtoServico.setValorDesconto(valorDesconto);

			valorItem = produtoServico.getValorUnitario();
		} else {

			produtoServico.setValorUnitario(valorItem);

			produtoServico.setValorDesconto(BigDecimal.ZERO);
		}

		produtoServico.setUnidade(produtoEdicao.getProduto().getTipoProduto().getNcm().getUnidadeMedida());

		produtoServico.setCfop(cfop);
		produtoServico.setValorTotalBruto(valorItem.multiply(new BigDecimal(quantidade)));

		EncargoFinanceiroProduto encargoFinanceiroProduto = tributacaoService.calcularTributoProduto(raizCNPJ, 
				tipoOperacao, 
				ufOrigem,
				ufDestino, 
				naturezaOperacao, 
				codigoNaturezaOperacao,
				produtoEdicao.getProduto().getTipoProduto().getCodigoNBM(), 
				dataVigencia, 
				cstICMS,
				valorItem);

		// produtoServico.setEncargoFinanceiro(encargoFinanceiroProduto);

		return produtoServico;
	}

	/**
	 * Grupo do detalhamento de Produtos e Serviços da NF-e
	 * 
	 * @param ufOrigem
	 * @param ufDestino
	 * @param raizCNPJ
	 * @param raizCNPJ
	 *            ,TipoOperacao tipoOperacao
	 * @param cstICMS
	 * @param descontos
	 *            TODO
	 * 
	 * @return
	 */
	private ProdutoServico carregaProdutoServico(Cota cota,
			long idProdutoEdicao, BigInteger quantidade, int cfop,
			TipoOperacao tipoOperacao, String ufOrigem, String ufDestino,
			int naturezaOperacao, String codigoNaturezaOperacao,
			Date dataVigencia, BigDecimal valorItem, String raizCNPJ,
			String cstICMS, Map<String, DescontoDTO> descontos) {

		ProdutoEdicao produtoEdicao = produtoEdicaoRepository
				.buscarPorId(idProdutoEdicao);

		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição "
					+ idProdutoEdicao + " não encontrado!");
		}

		ProdutoServico produtoServico = new ProdutoServico();

		produtoServico.setCodigoBarras(produtoEdicao.getCodigoDeBarras());
		produtoServico.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		produtoServico.setDescricaoProduto(produtoEdicao.getProduto().getNomeComercial());
		produtoServico.setNcm(produtoEdicao.getProduto().getTipoProduto().getNcm().getCodigo());
		produtoServico.setProdutoEdicao(produtoEdicao);
		produtoServico.setQuantidade(quantidade);
		produtoServico.setValorUnitario(valorItem);
		produtoServico.setUnidade(produtoEdicao.getProduto().getTipoProduto().getNcm().getUnidadeMedida());

		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
		// BigDecimal percentualDesconto =
		// descontoService.obterValorDescontoPorCotaProdutoEdicao(null, cota,
		// produtoEdicao);
		DescontoDTO descontoDTO = null;
		try {
			descontoDTO = descontoService.obterDescontoPor(descontos, cota.getId()
					, produtoEdicao.getProduto().getFornecedor().getId()
					, produtoEdicao.getProduto().getEditor().getId()
					, produtoEdicao.getProduto().getId(), produtoEdicao.getId()) ;
		} catch (Exception e) {

		}

		BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, (descontoDTO != null ? descontoDTO.getValor() : BigDecimal.ZERO));

		produtoServico.setValorDesconto(valorDesconto);

		produtoServico.setCfop(cfop);
		produtoServico.setValorTotalBruto(valorItem.multiply(new BigDecimal(quantidade)));

		EncargoFinanceiroProduto encargoFinanceiroProduto = tributacaoService.calcularTributoProduto(raizCNPJ, 
				tipoOperacao, ufOrigem,
				ufDestino, naturezaOperacao, codigoNaturezaOperacao,
				produtoEdicao.getProduto().getTipoProduto().getCodigoNBM(), dataVigencia, cstICMS, valorItem);

		// produtoServico.setEncargoFinanceiro(encargoFinanceiroProduto);

		return produtoServico;
	}
	

	/**
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#obterItensNotaFiscalPor(br
	 *      .com.abril.nds.model.fiscal.GrupoNotaFiscal, java.lang.Long,
	 *      br.com.abril.nds.util.Intervalo, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public List<ItemNotaFiscalSaida> obterItensNotaFiscalPor(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor,
			Cota cota, Intervalo<Date> periodo, List<Long> listaIdFornecedores,
			List<Long> listaIdProdutos, NaturezaOperacao tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> itensNotaFiscal = new ArrayList<ItemNotaFiscalSaida>();

		GrupoNotaFiscal grupoNotaFiscal = null; // tipoNotaFiscal.getGrupoNotaFiscal();

		Long idCota = cota.getId();

		switch (grupoNotaFiscal) {

		case NF_REMESSA_CONSIGNACAO:
			itensNotaFiscal = this.obterItensNFeRemessaEmConsignacao(
					parametrosRecolhimentoDistribuidor, idCota, periodo,
					listaIdFornecedores, listaIdProdutos, tipoNotaFiscal);
			break;

		case NF_DEVOLUCAO_REMESSA_CONSIGNACAO:

			if (cota.getParametrosCotaNotaFiscalEletronica() != null) {

				if (!cota.getParametrosCotaNotaFiscalEletronica()
						.isExigeNotaFiscalEletronica()) {
					itensNotaFiscal = this
							.obterItensNFeEntradaDevolucaoRemessaConsignacao(
									parametrosRecolhimentoDistribuidor, idCota,
									periodo, listaIdFornecedores,
									listaIdProdutos, tipoNotaFiscal);
				}
			}

			break;

		case NF_DEVOLUCAO_SIMBOLICA:

			if (cota.getParametrosCotaNotaFiscalEletronica() != null) {
				if (!cota.getParametrosCotaNotaFiscalEletronica()
						.isExigeNotaFiscalEletronica()) {
					itensNotaFiscal = this.obterItensNFeVenda(
							parametrosRecolhimentoDistribuidor, idCota,
							periodo, listaIdFornecedores, listaIdProdutos,
							tipoNotaFiscal);
				}
			}

			break;

		case NF_VENDA:
			itensNotaFiscal = this.obterItensNFeVenda(
					parametrosRecolhimentoDistribuidor, idCota, periodo,
					listaIdFornecedores, listaIdProdutos, tipoNotaFiscal);
			break;
		case NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO:
			itensNotaFiscal = this
					.obterItensNFeSaisaDevolucaoMercadoriaRecebidaConsignacao(
							parametrosRecolhimentoDistribuidor, idCota,
							periodo, listaIdFornecedores, listaIdProdutos,
							tipoNotaFiscal);
			break;
		default:
			break;
		}

		return itensNotaFiscal;
	}

	/**
	 * Obtém Itens para NFes de Envio de Consignado.
	 * 
	 * @param cota
	 * @param periodo
	 *            intervalo do periodo de lançamento
	 */
	private List<ItemNotaFiscalSaida> obterItensNFeRemessaEmConsignacao(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor,
			Long idCota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores, List<Long> listaIdProduto,
			NaturezaOperacao tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> listaItemNotaFiscal = null;

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaService
				.obterMovimentoEstoqueCotaPor(
						parametrosRecolhimentoDistribuidor, idCota,
						tipoNotaFiscal, listaGrupoMovimentoEstoque, periodo,
						listaIdFornecedores, listaIdProduto);

		if (listaMovimentoEstoqueCota != null
				&& !listaMovimentoEstoqueCota.isEmpty()) {

			listaItemNotaFiscal = this.gerarItensNotaFiscal(
					listaMovimentoEstoqueCota, tipoNotaFiscal, idCota);
		}

		return listaItemNotaFiscal;
	}

	/**
	 * Obtém Itens para NFes de Devolução de Consignado.
	 * 
	 * @param cota
	 * @param periodo
	 *            intervalo do periodo de lançamento
	 * 
	 * @return lista de itens nota fiscal
	 */
	private List<ItemNotaFiscalSaida> obterItensNFeEntradaDevolucaoRemessaConsignacao(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor,
			Long idCota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores, List<Long> listaIdProduto,
			NaturezaOperacao tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> listaItemNotaFiscal = null;

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque
				.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaService
				.obterMovimentoEstoqueCotaPor(
						parametrosRecolhimentoDistribuidor, idCota,
						tipoNotaFiscal, listaGrupoMovimentoEstoque, periodo,
						listaIdFornecedores, listaIdProduto);

		if (listaMovimentoEstoqueCota != null
				&& !listaMovimentoEstoqueCota.isEmpty()) {
			listaItemNotaFiscal = this.gerarItensNotaFiscal(
					listaMovimentoEstoqueCota, tipoNotaFiscal, idCota);
		}

		return listaItemNotaFiscal;
	}

	/**
	 * Obtém Itens para NFes de Devolução de Mercadoria Recebida em Consignação.
	 * 
	 * @param cota
	 * @param periodo
	 *            intervalo do periodo de lançamento
	 * 
	 * @return lista de itens nota fiscal
	 */
	private List<ItemNotaFiscalSaida> obterItensNFeSaisaDevolucaoMercadoriaRecebidaConsignacao(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor,
			Long idCota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores, List<Long> listaIdProduto,
			NaturezaOperacao tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> listaItemNotaFiscal = null;

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaService
				.obterMovimentoEstoqueCotaPor(
						parametrosRecolhimentoDistribuidor, idCota,
						tipoNotaFiscal, listaGrupoMovimentoEstoque, periodo,
						listaIdFornecedores, listaIdProduto);

		if (listaMovimentoEstoqueCota != null
				&& !listaMovimentoEstoqueCota.isEmpty()) {
			listaItemNotaFiscal = this.gerarItensNotaFiscal(
					listaMovimentoEstoqueCota, tipoNotaFiscal, idCota);
		}

		return listaItemNotaFiscal;
	}

	/**
	 * Obtém Itens para NFes de Venda. Itens de Envio menos Itens de Devolução;
	 * 
	 * @param cota
	 * @param periodo
	 *            intervalo do periodo de lançamento
	 */
	private List<ItemNotaFiscalSaida> obterItensNFeVenda(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor,
			Long idCota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores, List<Long> listaIdProdutos,
			NaturezaOperacao tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> itensNFeEnvioConsignado = this
				.obterItensNFeRemessaEmConsignacao(
						parametrosRecolhimentoDistribuidor, idCota, periodo,
						listaIdFornecedores, listaIdProdutos, tipoNotaFiscal);

		List<ItemNotaFiscalSaida> itensNFeDevolucaoConsignado = this
				.obterItensNFeEntradaDevolucaoRemessaConsignacao(
						parametrosRecolhimentoDistribuidor, idCota, periodo,
						listaIdFornecedores, listaIdProdutos, tipoNotaFiscal);

		List<ItemNotaFiscalSaida> itensNFeVenda = new ArrayList<ItemNotaFiscalSaida>();

		if (itensNFeEnvioConsignado != null) {
			for (ItemNotaFiscalSaida itemNFeEnvio : itensNFeEnvioConsignado) {

				ItemNotaFiscalSaida itemNFeVenda = itemNFeEnvio;

				if (itensNFeDevolucaoConsignado != null
						&& !itensNFeDevolucaoConsignado.isEmpty()) {

					if (itensNFeDevolucaoConsignado.contains(itemNFeEnvio)) {
						ItemNotaFiscalSaida itemNFeDevolucao = itensNFeDevolucaoConsignado
								.get(itensNFeDevolucaoConsignado
										.indexOf(itemNFeEnvio));

						BigInteger quantidade = itemNFeEnvio.getQuantidade()
								.add(itemNFeDevolucao.getQuantidade());

						itemNFeVenda.setQuantidade(quantidade);
					}
				}

				itensNFeVenda.add(itemNFeEnvio);
			}
		}

		return itensNFeVenda;
	}

	/**
	 * Gera itens da nota com base nos movimentos de estoque cota
	 * 
	 * @param listaMovimentoEstoqueCota
	 * @return
	 */
	private List<ItemNotaFiscalSaida> gerarItensNotaFiscal(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota,
			NaturezaOperacao tipoNotaFiscal, Long idCota) {

		Map<Long, ItemNotaFiscalSaida> mapItemNotaFiscal = new HashMap<Long, ItemNotaFiscalSaida>();

		GrupoNotaFiscal grupoNotaFiscal = null; // tipoNotaFiscal.getGrupoNotaFiscal();

		Cota cota = cotaRepository.buscarPorId(idCota);

		Map<String, DescontoDTO> descontos = descontoService
				.obterDescontosMapPorLancamentoProdutoEdicao();

		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

			TipoMovimentoEstoque tipoMovimentoEstoque = (TipoMovimentoEstoque) movimentoEstoqueCota
					.getTipoMovimento();

			GrupoMovimentoEstoque grupoMovimento = tipoMovimentoEstoque
					.getGrupoMovimentoEstoque();

			ProdutoEdicao produtoEdicao = movimentoEstoqueCota
					.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			// BigDecimal percentualDesconto =
			// descontoService.obterValorDescontoPorCotaProdutoEdicao(null,
			// cota, produtoEdicao);
			DescontoDTO descontoDTO = null;
			try {
				descontoDTO = descontoService.obterDescontoPor(descontos, cota.getId()
						, produtoEdicao.getProduto().getFornecedor().getId()
						, produtoEdicao.getProduto().getEditor().getId()
						, produtoEdicao.getProduto().getId(), produtoEdicao.getId());
			} catch (Exception e) {

			}
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, (descontoDTO != null ? descontoDTO.getValor() : BigDecimal.ZERO));

			BigDecimal valorUnitario = precoVenda.subtract(valorDesconto);

			BigInteger quantidade = movimentoEstoqueCota.getQtde();

			List<MovimentoEstoqueCota> listaMovimentoEstoqueItem = new ArrayList<MovimentoEstoqueCota>();

			listaMovimentoEstoqueItem.add(movimentoEstoqueCota);

			if (grupoMovimento.getDominio().equals(Dominio.COTA)
					&& grupoMovimento.getOperacaoEstoque().equals(OperacaoEstoque.SAIDA)
					&& !grupoNotaFiscal.equals(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO)) {
				quantidade = quantidade.negate();
			}

			if (grupoMovimento.getDominio().equals(Dominio.COTA) 
					&& grupoMovimento.getOperacaoEstoque().equals(OperacaoEstoque.ENTRADA)
					&& grupoNotaFiscal.equals(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO)) {
				quantidade = quantidade.negate();
			}

			if (mapItemNotaFiscal.containsKey(produtoEdicao.getId())) {
				ItemNotaFiscalSaida item = mapItemNotaFiscal.get(produtoEdicao.getId());
				quantidade = quantidade.add(item.getQuantidade());
				listaMovimentoEstoqueItem.addAll(item.getListaMovimentoEstoqueCota());
			}

			ItemNotaFiscalSaida itemNotaFiscal = new ItemNotaFiscalSaida();

			itemNotaFiscal.setIdProdutoEdicao(produtoEdicao.getId());

			if (produtoEdicao.getProduto().getTributacaoFiscal() != null) {
				itemNotaFiscal.setCstICMS(produtoEdicao.getProduto().getTributacaoFiscal().getCST());
			}

			itemNotaFiscal.setQuantidade(quantidade);
			itemNotaFiscal.setValorUnitario(valorUnitario);
			itemNotaFiscal.setListaMovimentoEstoqueCota(listaMovimentoEstoqueItem);

			mapItemNotaFiscal.put(produtoEdicao.getId(), itemNotaFiscal);
		}

		return new ArrayList<ItemNotaFiscalSaida>(mapItemNotaFiscal.values());
	}

	/**
	 * Sumariza a quantidade total dos itens da nota
	 * 
	 * @param listaItemNotaFiscal
	 *            intes para nota fiscal
	 * @return somatoria total da quantidade de itens
	 */
	private QuantidadePrecoItemNotaDTO sumarizarTotalItensNota(
			List<ItemNotaFiscalSaida> listaItemNotaFiscal) {

		QuantidadePrecoItemNotaDTO dto = new QuantidadePrecoItemNotaDTO();

		BigInteger quantidade = BigInteger.ZERO;
		BigDecimal preco = BigDecimal.ZERO;
		BigDecimal precoComDesconto = BigDecimal.ZERO;

		// Map<String, DescontoDTO> descontos =
		// descontoService.obterDescontosMapPorLancamentoProdutoEdicao(null,
		// null);

		for (ItemNotaFiscalSaida item : listaItemNotaFiscal) {
			quantidade = quantidade.add(item.getQuantidade());
			preco = preco.add(item.getValorUnitario().multiply(
					new BigDecimal(quantidade)));
			/*
			 * ProdutoEdicao produtoEdicao =
			 * produtoEdicaoRepository.buscarPorId(item.getIdProdutoEdicao());
			 * DescontoDTO descontoDTO = null; try { descontoDTO =
			 * descontoService.obterDescontoPor(descontos ,
			 * item.getListaMovimentoEstoqueCota().get(0).getCota().getId() ,
			 * produtoEdicao.getProduto().getFornecedor().getId() ,
			 * produtoEdicao.getProduto().getId() , produtoEdicao.getId()); }
			 * catch (Exception e) {
			 * 
			 * }
			 * 
			 * BigDecimal desconto = (descontoDTO != null ?
			 * descontoDTO.getValor() : BigDecimal.ZERO);
			 */
			precoComDesconto = precoComDesconto.add(item.getValorUnitario().multiply(new BigDecimal(item.getQuantidade())));
			/*
			 * precoComDesconto.add( item.getValorUnitario() .subtract(desconto,
			 * new MathContext(3)) .multiply(item.getValorUnitario())
			 * .divide(new BigDecimal(100))).multiply( new
			 * BigDecimal(item.getQuantidade()));
			 */
		}

		dto.setQuantidade(quantidade);
		dto.setPreco(preco);
		dto.setPrecoComDesconto(precoComDesconto);

		return dto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#obterTransporte(java.lang.
	 * Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public InformacaoTransporte obterTransporte(Long idCota) {
		InformacaoTransporte transporte = new InformacaoTransporte();

		PDV pdv = this.pdvRepository.obterPDVPrincipal(idCota);

		// OBTEM ROTEIRIZACAO POR COTA, VERIFICAR NECESSIDADE DE OBTER POR PDV,
		// DEVIDO ÀS MUDANÇAS NO MODELO DE DADOS
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		Roteirizacao roteirizacao = this.roterizacaoRepository
				.buscarRoteirizacaoDeCota(cota.getNumeroCota());

		if (pdv != null && roteirizacao != null) {
			transporte.setModalidadeFrete(0); // Por conta emitente

			// *****Comentado porque não é obrigatório*****//

			/*
			 * transporte.setNome(associacaoVeiculoMotoristaRota.getTransportador
			 * ().getPessoaJuridica().getRazaoSocial());
			 * transporte.setDocumento(
			 * associacaoVeiculoMotoristaRota.getTransportador
			 * ().getPessoaJuridica().getCnpj());
			 * transporte.setInscricaoEstadual
			 * (associacaoVeiculoMotoristaRota.getTransportador
			 * ().getPessoaJuridica().getInscricaoEstadual());
			 * 
			 * Endereco endereco =
			 * associacaoVeiculoMotoristaRota.getTransportador
			 * ().getEnderecosTransportador().get(0).getEndereco();
			 * transporte.setEndereco(endereco);
			 * transporte.setUf(endereco.getUf());
			 * 
			 * Veiculo veiculo = new Veiculo();
			 * veiculo.setPlaca(associacaoVeiculoMotoristaRota
			 * .getVeiculo().getPlaca()); veiculo.setUf();
			 * veiculo.setRegistroTransCarga(registroTransCarga);
			 * transporte.setVeiculo(veiculo);
			 */
		} else {
			transporte.setModalidadeFrete(1); // Por conta destinatário
		}

		return transporte;
	}

	/**
	 * Obtém notas fiscais de referência
	 * 
	 * @param movimentoEstoqueCota
	 *            movimento estoque cota
	 * @return
	 */
	@Transactional
	public List<NotaFiscalReferenciada> obterNotasReferenciadas(
			List<ItemNotaFiscalSaida> listaItensNotaFiscal) {

		if (listaItensNotaFiscal == null || listaItensNotaFiscal.isEmpty())
			return null;

		Set<NotaFiscalReferenciada> notaFiscalReferenciada = new HashSet<NotaFiscalReferenciada>();

		// FIXME: Ajustar a busca de nota referenciada
		/*
		 * for (ItemNotaFiscalSaida itemNotaFiscal : listaItensNotaFiscal) {
		 * 
		 * List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = itemNotaFiscal
		 * .getListaMovimentoEstoqueCota();
		 * 
		 * if (listaMovimentoEstoqueCota == null ||
		 * listaMovimentoEstoqueCota.isEmpty()) continue;
		 * 
		 * for (MovimentoEstoqueCota movimentoEstoqueCota :
		 * listaMovimentoEstoqueCota) {
		 * 
		 * List<DetalheNotaFiscal> listaProdutoServicos =
		 * movimentoEstoqueCota.getListaProdutoServicos();
		 * 
		 * if (listaProdutoServicos != null && !listaProdutoServicos.isEmpty())
		 * {
		 * 
		 * for (ProdutoServico produtoServico : listaProdutoServicos) {
		 * 
		 * NotaFiscal notaFiscal = produtoServico
		 * .getProdutoServicoPK().getNotaFiscal();
		 * 
		 * if (notaFiscal != null) {
		 * 
		 * GrupoNotaFiscal grupoNotaFiscal = null;
		 * //notaFiscal.getIdentificacao(
		 * ).getTipoNotaFiscal().getGrupoNotaFiscal();
		 * 
		 * if (GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO .equals(grupoNotaFiscal))
		 * {
		 * 
		 * notaFiscalReferenciada .add(this
		 * .converterNotaFiscalToNotaFiscalReferenciada(notaFiscal)); } } } } }
		 * }
		 */

		return new ArrayList<NotaFiscalReferenciada>(notaFiscalReferenciada);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#
	 * converterNotaFiscalToNotaFiscalReferenciada
	 * (br.com.abril.nds.model.fiscal.nota.NotaFiscal)
	 */
	@Override
	@Transactional
	public NotaFiscalReferenciada converterNotaFiscalToNotaFiscalReferenciada(NotaFiscal notaFiscal) {

		NotaFiscalReferenciada notaReferenciada = null;

		InformacaoEletronica informacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();

		if (informacaoEletronica != null) {

			NotaFiscalReferenciadaPK pk = new NotaFiscalReferenciadaPK();
			pk.setChaveAcesso(informacaoEletronica.getChaveAcesso());
			pk.setNotaFiscal(notaFiscal);

			notaReferenciada = new NotaFiscalReferenciada();
			notaReferenciada.setPk(pk);
		}

		return notaReferenciada;
	}

	@Override
	@Transactional
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {

		validarFiltrosNFe(filtro);
		
		if(naturezaOperacao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Natureza de Operação incorreta.");
		}
		
		List<TipoMovimento> itensMovimentosFiscais = obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
		
		if(itensMovimentosFiscais.size() > 0) {
			
			ajustarFiltroNaturezaDevSimbolicaVenda(filtro, naturezaOperacao);
			
			return this.notaFiscalRepository.consultaCotaExemplaresMFFSumarizados(filtro);
		} else {
		
			return this.notaFiscalRepository.consultaCotaExemplaresMECSumarizados(filtro);
		}
	}

	private void ajustarFiltroNaturezaDevSimbolicaVenda(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		
		if(naturezaOperacao.isNotaFiscalDevolucaoSimbolica()) {
			
			filtro.setNotaFiscalDevolucaoSimbolica(true);
		} else if(naturezaOperacao.isNotaFiscalVendaConsignado()) {
			
			filtro.setNotaFiscalVendaConsignado(true);
		}
	}

	@Override
	@Transactional
	public Long consultaCotaExemplaresSumarizadoQtd(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		
		validarFiltrosNFe(filtro);
		
		List<TipoMovimento> itensMovimentosFiscais = obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
		
		if(itensMovimentosFiscais.size() > 0) {
			
			ajustarFiltroNaturezaDevSimbolicaVenda(filtro, naturezaOperacao);
			
			return this.notaFiscalRepository.consultaCotaExemplaresMFFSumarizadosQtd(filtro);
		} else {
		
			return this.notaFiscalRepository.consultaCotaExemplaresMECSumarizadosQtd(filtro);
		}
		
	}

	@Override
	@Transactional
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplaresSumarizados(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		
		validarFiltrosNFe(filtro);
		
		naturezaOperacao = this.naturezaOperacaoRepository.buscarPorId(naturezaOperacao.getId());
		
		List<TipoMovimento> itensMovimentosFiscais = obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
		
		if(itensMovimentosFiscais.size() > 0) {
			
			ajustarFiltroNaturezaDevSimbolicaVenda(filtro, naturezaOperacao);
			
			return this.notaFiscalRepository.consultaFornecedorExemplaresMFFSumarizados(filtro);
		} else {
		
			return this.notaFiscalRepository.consultaFornecedorExemplaresMESumarizados(filtro);
		}
		
	}

	@Override
	@Transactional
	public Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao) {
		
		validarFiltrosNFe(filtro);
		
		List<TipoMovimento> itensMovimentosFiscais = obterMovimentosFiscaisNaturezaOperacao(naturezaOperacao);
		
		if(itensMovimentosFiscais.size() > 0) {
			
			ajustarFiltroNaturezaDevSimbolicaVenda(filtro, naturezaOperacao);
			
			return this.notaFiscalRepository.consultaFornecedorExemplaresMFFSumarizadosQtd(filtro);
		} else {
		
			return this.notaFiscalRepository.consultaFornecedorExemplaresMESumarizadosQtd(filtro);
		}
		
	}

	@Transactional
	public Document criarDocumentoCancelamento(NotaFiscal notaFiscal) throws ParserConfigurationException {

		if (notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getJustificativaEntradaContigencia() == null) {
			throw new IllegalArgumentException("Justificativa não pode ser nula");
		}

		if (notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getJustificativaEntradaContigencia().length() < 15
				|| notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getJustificativaEntradaContigencia().length() > 255) {
			throw new IllegalArgumentException(
					"Justificativa deve possuir entre 15 e 255 caracteres, tamanho atual: "
							+ notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getJustificativaEntradaContigencia().length());
		}

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		Element cancNFeElement = doc.getDocumentElement();
		cancNFeElement.setAttribute("versao", parametroSistemaService.buscarParametroSistemaGeral().getNfeInformacoesVersaoEmissor());

		Element infCancElement = doc.createElement("infCanc");
		infCancElement.setAttribute("Id", new StringBuilder("ID").append(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getChaveAcesso()).toString());
		cancNFeElement.appendChild(infCancElement);

		Element tpAmbElement = doc.createElement("tpAmb");
		tpAmbElement.setTextContent(String.valueOf(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getTipoAmbiente()));
		infCancElement.appendChild(tpAmbElement);

		Element xServElement = doc.createElement("xServ");
		xServElement.setTextContent("CANCELAR");
		infCancElement.appendChild(xServElement);

		Element chNFeElement = doc.createElement("chNFe");
		chNFeElement.setTextContent(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getChaveAcesso().toString());
		infCancElement.appendChild(chNFeElement);

		Element nProtElement = doc.createElement("nProt");
		nProtElement.setTextContent(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getRetornoComunicacaoEletronica().getProtocolo().toString());
		infCancElement.appendChild(nProtElement);

		Element xJustElement = doc.createElement("xJust");
		xJustElement.setTextContent(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getJustificativaEntradaContigencia());
		infCancElement.appendChild(xJustElement);

		return doc;
	}
	
	@Override
	@Transactional
	public List<TipoMovimento> obterMovimentosFiscaisNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
		List<TipoMovimento> itensMovimentosFiscais = new ArrayList<>();
		if(naturezaOperacao != null) {
			for(TipoMovimento tm : naturezaOperacao.getTipoMovimento()) {
				if(tm instanceof TipoMovimentoFiscal) {
					itensMovimentosFiscais.add(tm);
				}
			}
		}
		return itensMovimentosFiscais;
	}

	private void validarFiltrosNFe(FiltroNFeDTO filtro) {
		if (filtro.getDataInicial() == null || filtro.getDataFinal() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O intervalo de datas não pode ser nula!");
		}
	}

	@Override
	@Transactional
	public List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(Long idConferenciaCota, String  orderBy,Ordenacao ordenacao, Integer firstResult, Integer maxResults) {		
		List<ItemNotaFiscalPendenteDTO> listaRetorno =  this.notaFiscalRepository.buscarItensPorNota(idConferenciaCota, orderBy, ordenacao, firstResult, maxResults);
		
		for(ItemNotaFiscalPendenteDTO dto: listaRetorno){
			Long qtdDiferencaDias = DateUtil.obterDiferencaDias(dto.getDataConferenciaEncalhe(), dto.getDataChamadaEncalhe()) + 1;			
			dto.setDia(qtdDiferencaDias.toString() + "°");		
			
		}
		return listaRetorno;
	}
	
	@Override
	@Transactional
	public Integer qtdeNota(Long idConferenciaCota) {		
		return this.notaFiscalRepository.qtdeNota(idConferenciaCota);
	}	
}
