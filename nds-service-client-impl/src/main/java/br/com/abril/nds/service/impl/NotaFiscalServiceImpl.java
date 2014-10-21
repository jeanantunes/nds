package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.Condicao;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.pk.NotaFiscalReferenciadaPK;
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EncargoFinanceiroRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TributacaoService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.fiscal.nota.NFEExporter;
import br.com.abril.nds.vo.ValidacaoVO;

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
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#
	 * obterTotalItensNotaFiscalPorCotaEmLote
	 * (br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO)
	 */
	@Override
	@Transactional
	public Map<Cota, QuantidadePrecoItemNotaDTO> obterTotalItensNotaFiscalPorCotaEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {

		Intervalo<Date> periodo = dadosConsultaLoteNotaFiscal.getPeriodoMovimento();

		Set<TipoNotaFiscal> tiposNotaFiscal = dadosConsultaLoteNotaFiscal.getTipoNotaFiscal();

		List<Long> listaIdFornecedores = dadosConsultaLoteNotaFiscal.getListaIdFornecedores();

		Map<Cota, QuantidadePrecoItemNotaDTO> idCotaTotalItensNota = new HashMap<Cota, QuantidadePrecoItemNotaDTO>();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		
		TipoAtividade tipoAtividade = this.distribuidorRepository.tipoAtividade();
		
		for (Long idCota : dadosConsultaLoteNotaFiscal.getIdsCotasDestinatarias()) {
			
			Cota cota = this.cotaRepository.buscarPorId(idCota);
			
			for (TipoNotaFiscal tipoNotaFiscal : tiposNotaFiscal) {
				
				if (tipoNotaFiscal.getTipoAtividade().equals(tipoAtividade)) {
	
					if (cota.getParametrosCotaNotaFiscalEletronica() != null) {
	
						if (cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica() == tipoNotaFiscal.isContribuinte()) {
	
							List<ItemNotaFiscalSaida> itensNotaFiscal = obterItensNotaFiscalPor(
									parametrosRecolhimentoDistribuidor, 
									cota, periodo,
									listaIdFornecedores, null, tipoNotaFiscal);
	
							if (itensNotaFiscal != null && !itensNotaFiscal.isEmpty()) {
								idCotaTotalItensNota.put(cota, this.sumarizarTotalItensNota(itensNotaFiscal));
							}
						}
					}
				}
			}
		}

		return idCotaTotalItensNota;
	}

	@Override
	public List<NotaFiscal> gerarDadosNotaFicalEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
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
	public List<RetornoNFEDTO> processarRetornoNotaFiscal(
			List<RetornoNFEDTO> listaDadosRetornoNFE) {

		List<RetornoNFEDTO> listaDadosRetornoNFEProcessados = new ArrayList<RetornoNFEDTO>();

		for (RetornoNFEDTO dadosRetornoNFE : listaDadosRetornoNFE) {

			if (dadosRetornoNFE.getIdNotaFiscal() != null) {

				NotaFiscal notaFiscal = this.notaFiscalRepository.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());

				if (notaFiscal != null) {

					IdentificacaoEmitente emitente = notaFiscal.getIdentificacaoEmitente();

					String cpfCnpjEmitente = emitente.getDocumento();

					InformacaoEletronica informacaoEletronica = notaFiscal.getInformacaoEletronica();

					if (cpfCnpjEmitente.equals(dadosRetornoNFE.getCpfCnpj())) {

						if (StatusProcessamentoInterno.ENVIADA.equals(notaFiscal.getStatusProcessamentoInterno())) {

							if (Status.AUTORIZADO.equals(dadosRetornoNFE.getStatus())
									|| Status.USO_DENEGADO.equals(dadosRetornoNFE.getStatus())) {

								listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
							}

						} else if (StatusProcessamentoInterno.RETORNADA.equals(notaFiscal.getStatusProcessamentoInterno())) {

							if (Status.AUTORIZADO.equals(informacaoEletronica.getRetornoComunicacaoEletronica().getStatus())
									&& Status.CANCELAMENTO_HOMOLOGADO.equals(dadosRetornoNFE.getStatus())) {

								listaDadosRetornoNFEProcessados.add(dadosRetornoNFE);
							}
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

		NotaFiscal notaFiscalCancelada = this.notaFiscalRepository.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());

		TipoNotaFiscal tipoNotaFiscal = notaFiscalCancelada.getIdentificacao()
				.getTipoNotaFiscal();

		if (isRemessaMercadoriaConsignacao(tipoNotaFiscal)) {
			movimentoEstoqueService.devolucaoConsignadoNotaCancelada(notaFiscalCancelada);
			movimentoEstoqueCotaService.envioConsignadoNotaCancelada(notaFiscalCancelada);

		}else if(isDevolucaoMerdadoriaRecebiaConsignacao(tipoNotaFiscal)){			
			if(isSobraMercadoria(notaFiscalCancelada) || isDevolucaoEncalhe(notaFiscalCancelada) ){
				movimentoEstoqueService.devolucaoRecolhimentoNotaCancelada(notaFiscalCancelada);

			}else if(isFaltaMercadoria(notaFiscalCancelada)){
				movimentoEstoqueService.devolucaoConsignadoNotaCancelada(notaFiscalCancelada);

			}
		}
		atualizaRetornoNFe(dadosRetornoNFE);
	}

	/**
	 * indetifica se a nota foi gerada com a condição de falta de mercadoria.
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isFaltaMercadoria(NotaFiscal notaFiscal) {
		return Condicao.FALTA_MERCADORIA == notaFiscal.getCondicao();
	}

	/**
	 * indetifica se a nota foi gerada com a condição de devolução de encalhe.
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isDevolucaoEncalhe(NotaFiscal notaFiscal) {		
		return Condicao.DEVOLUCAO_ENCALHE == notaFiscal.getCondicao();
	}

	/**
	 * indentifica se a nota foi gerada com a condição de sobra de mercadoria.
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isSobraMercadoria(NotaFiscal notaFiscal) {
		return Condicao.SOBRA_MERCADORIA == notaFiscal.getCondicao();
	}

	/**
	 * Identifica se é um nota de Devolução de Mercadoria Recebida em Consignação.
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isDevolucaoMerdadoriaRecebiaConsignacao(TipoNotaFiscal tipoNotaFiscal) {
		return tipoNotaFiscal.getGrupoNotaFiscal() == GrupoNotaFiscal.NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO
				&& tipoNotaFiscal.getEmitente() == TipoUsuarioNotaFiscal.DISTRIBUIDOR
				&& tipoNotaFiscal.getDestinatario() == TipoUsuarioNotaFiscal.TREELOG;
	}

	/**
	 * @param tipoNotaFiscal
	 * @return
	 */
	private boolean isRemessaMercadoriaConsignacao(TipoNotaFiscal tipoNotaFiscal) {
		return tipoNotaFiscal.getGrupoNotaFiscal() == GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO
				&& tipoNotaFiscal.getEmitente() == TipoUsuarioNotaFiscal.DISTRIBUIDOR
				&& tipoNotaFiscal.getDestinatario() == TipoUsuarioNotaFiscal.COTA;
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
	public void autorizarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {
		atualizaRetornoNFe(dadosRetornoNFE);
	}

	/**
	 * Atualiza o Retorno de um NotaFiscal que já foi enviada.
	 * 
	 * @param dadosRetornoNFE
	 */
	private void atualizaRetornoNFe(RetornoNFEDTO dadosRetornoNFE) {

		NotaFiscal notaFiscal = this.notaFiscalRepository.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());

		InformacaoEletronica informacaoEletronica = notaFiscal.getInformacaoEletronica();

		if (informacaoEletronica == null) {
			notaFiscal.setInformacaoEletronica(new InformacaoEletronica());
			informacaoEletronica = notaFiscal.getInformacaoEletronica();
		}

		informacaoEletronica.setChaveAcesso(dadosRetornoNFE.getChaveAcesso());

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(dadosRetornoNFE.getDataRecebimento());
		retornoComunicacaoEletronica.setMotivo(dadosRetornoNFE.getMotivo());
		retornoComunicacaoEletronica.setProtocolo(dadosRetornoNFE.getProtocolo());
		retornoComunicacaoEletronica.setStatus(dadosRetornoNFE.getStatus());

		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);

		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal.setStatusProcessamentoInterno(StatusProcessamentoInterno.RETORNADA);

		this.notaFiscalRepository.merge(notaFiscal);	

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
			notaFiscal.setStatusProcessamentoInterno(StatusProcessamentoInterno.ENVIADA);
			this.notaFiscalRepository.merge(notaFiscal);
		}
	}

	@Override
	@Transactional
	public synchronized void exportarNotasFiscais(
			Long... idNotaFiscals)
			throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{

		List<NotaFiscal> notasFiscaisParaExportacao = new ArrayList<NotaFiscal>(idNotaFiscals.length);
		for (Long id : idNotaFiscals) {
			notasFiscaisParaExportacao.add(notaFiscalRepository.buscarPorId(id));
		}

		exportarNotasFiscais(notasFiscaisParaExportacao);

		gerarArquivoNota(notasFiscaisParaExportacao);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#exportarNotasFiscais()
	 */
	@Override
	@Transactional
	public synchronized void exportarNotasFiscais(
			List<NotaFiscal> notasFiscaisParaExportacao)
					throws FileNotFoundException, IOException {

		String dados = "";

		try {

			dados = gerarArquivoNota(notasFiscaisParaExportacao);

		} catch (Exception e) {
			
			if (e instanceof ValidacaoException){
				
				ValidacaoException ex = (ValidacaoException) e;
				
				StringBuilder msgs = new StringBuilder();
				
				for (String msg : ex.getValidacao().getListaMensagens()){
					
					if (msgs.length() != 0){
						msgs.append(", ");
					}
					
					msgs.append(msg);
				}
				
				throw new ValidacaoException(TipoMensagem.ERROR, 
						"Erro ao gerar arquivo de nota: " + msgs.toString());
			}
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Falha ao gerar arquivo de exportação: " + e.getMessage()));
		}

		ParametroSistema pathNFEExportacao = this.parametroSistemaService
				.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);

		if (pathNFEExportacao == null) {
			throw new ValidacaoException(
					new ValidacaoVO(TipoMensagem.WARNING,
							"Informe o diretório de exportação das notas na tela de parametros do sistema"));
		}

		File diretorioExportacaoNFE = new File(pathNFEExportacao.getValor());

		if (!diretorioExportacaoNFE.isDirectory()) {
			throw new FileNotFoundException(
					"O diretório["+pathNFEExportacao.getValor()+"] de exportação parametrizado não é válido!");
		}
		Long time = new Date().getTime();
		File notaExportacao = new File(diretorioExportacaoNFE + File.separator
				+ new File("NFeExportacao" + time + ".txt"));

		FileWriter fileWriter;

		fileWriter = new FileWriter(notaExportacao);

		BufferedWriter buffer = new BufferedWriter(fileWriter);

		buffer.write(dados);

		buffer.close();

		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {
			this.enviarNotaFiscal(notaFiscal.getId());
		}
	}

	private String gerarArquivoNota(List<NotaFiscal> notasFiscaisParaExportacao)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		StringBuilder sBuilder = new StringBuilder();

		NFEExporter nfeExporter = new NFEExporter();

		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {

			nfeExporter.clear();

			nfeExporter.execute(notaFiscal);

			String s = nfeExporter.gerarArquivo();
			sBuilder.append(s);
		}

		return "NOTA FISCAL|" + notasFiscaisParaExportacao.size() + "|\n"
		+ sBuilder.toString();
	}

	/**
	 * Carrega Grupo das informações de identificação da NF-e
	 * 
	 * @param idTipoNotaFiscal
	 * @param dataEmissao
	 * @param listNotaFiscalReferenciada
	 * @return
	 */
	private Identificacao carregaIdentificacao(TipoNotaFiscal tipoNotaFiscal,
			Date dataEmissao,
			List<NotaFiscalReferenciada> listNotaFiscalReferenciada) {

		Identificacao identificacao = new Identificacao();
		identificacao.setDataEmissao(dataEmissao);
		identificacao.setTipoOperacao(tipoNotaFiscal.getTipoOperacao());
		identificacao.setDescricaoNaturezaOperacao(tipoNotaFiscal
				.getNopDescricao());
		identificacao.setSerie(tipoNotaFiscal.getSerieNotaFiscal());
		identificacao.setNumeroDocumentoFiscal(serieRepository
				.next(tipoNotaFiscal.getSerieNotaFiscal()));
		identificacao.setTipoNotaFiscal(tipoNotaFiscal);
		// TODO indPag
		identificacao.setFormaPagamento(FormaPagamento.A_VISTA);
		identificacao.setTipoEmissao(TipoEmissao.NORMAL);

		identificacao.setListReferenciadas(listNotaFiscalReferenciada);
		return identificacao;
	}

	/**
	 * Carrega Grupo de identificação do emitente da NF-e
	 * 
	 * @return
	 */
	private IdentificacaoEmitente carregaEmitente() {
		IdentificacaoEmitente identificacaoEmitente = new IdentificacaoEmitente();

		PessoaJuridica pessoaJuridica = this.distribuidorRepository.juridica();
		
		String cnpj = Util.removerMascaraCnpj(pessoaJuridica.getCnpj());
		
		identificacaoEmitente.setDocumento(cnpj);
		identificacaoEmitente.setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
		identificacaoEmitente.setInscricaoMunicipal(pessoaJuridica.getInscricaoMunicipal());
		identificacaoEmitente.setNome(pessoaJuridica.getNome());
		identificacaoEmitente.setNomeFantasia(pessoaJuridica.getNomeFantasia());
		identificacaoEmitente.setPessoaEmitenteReferencia(pessoaJuridica);

		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository
				.obterEnderecoPrincipal();

		if (enderecoDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do distribuidor não encontrada!");
		}

		try {
			identificacaoEmitente
			.setEndereco(cloneEndereco(enderecoDistribuidor
					.getEndereco()));
		} catch (Exception exception) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do distribuidor!");
		}

		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository
				.obterTelefonePrincipal();

		if (telefoneDistribuidor != null) {
			Telefone telefone = telefoneDistribuidor.getTelefone();
			telefoneRepository.detach(telefone);
			telefone.setId(null);
			telefone.setPessoa(null);
			telefoneRepository.adicionar(telefone);
			identificacaoEmitente.setTelefone(telefone);
		}
		// TODO: Como definir o Regime Tributario
		identificacaoEmitente
		.setRegimeTributario(RegimeTributario.SIMPLES_NACIONAL);

		return identificacaoEmitente;
	}
	
	/**
	 * Grupo de identificação do Destinatário da NF-e
	 * 
	 * @param cota
	 * @return
	 */
	private IdentificacaoDestinatario carregaDestinatario(Cota cota) {
		IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota deve ser informada!");
		}

		destinatario.setDocumento(cota.getPessoa().getDocumento());
		destinatario.setEmail(cota.getPessoa().getEmail());

		EnderecoCota enderecoCota = cotaRepository
				.obterEnderecoPrincipal(cota.getId());

		if (enderecoCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal da cota " + cota.getNumeroCota() + " não encontrada!");
		}

		try {
			destinatario.setEndereco(cloneEndereco(enderecoCota.getEndereco()));
		} catch (CloneNotSupportedException e) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do Emitente!");
		}

		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			
			String inscricaoEstadual = Util.truncarValor(Util.removerMascaraCnpj(pessoaJuridica.getInscricaoEstadual()), 14);
			
			destinatario.setInscricaoEstadual(inscricaoEstadual);
			
			destinatario.setNomeFantasia(pessoaJuridica.getNomeFantasia());
		}
		destinatario.setNome(cota.getPessoa().getNome());
		destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());


		Telefone telefone = telefoneCotaRepository.obterTelefonePrincipalCota(cota.getId());
		
        if (telefone!=null){
			telefoneRepository.detach(telefone);
			telefone.setId(null);
			telefone.setPessoa(null);
			telefoneRepository.adicionar(telefone);
			destinatario.setTelefone(telefone);
        }
        
		return destinatario;
	}


	/**
	 * Grupo de identificação do Destinatário da NF-e
	 * 
	 * @param cota
	 * @return
	 */
	private IdentificacaoDestinatario carregaDestinatario(Fornecedor fornecedor) {
		IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
		if (fornecedor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedordeve ser informada!");
		}

		destinatario.setDocumento(fornecedor.getJuridica().getDocumento());
		destinatario.setEmail(fornecedor.getJuridica().getEmail());

		EnderecoFornecedor enderecoFornecedor = fornecedorRepository
				.obterEnderecoPrincipal(fornecedor.getId());

		if (enderecoFornecedor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do Fornecedor " + fornecedor.getId() + " não encontrado!");
		}

		try {
			destinatario.setEndereco(cloneEndereco(enderecoFornecedor.getEndereco()));
		} catch (CloneNotSupportedException e) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do Emitente!");
		}
		destinatario.setInscricaoEstadual(fornecedor.getJuridica().getInscricaoEstadual());
		destinatario.setNomeFantasia(fornecedor.getJuridica().getNomeFantasia());

		destinatario.setNome(fornecedor.getJuridica().getNome());
		destinatario.setPessoaDestinatarioReferencia(fornecedor.getJuridica());

		TelefoneFornecedor telefoneFornecedor = telefoneFornecedorRepository
				.obterTelefonePrincipal(fornecedor.getId());
		if (telefoneFornecedor != null) {
			Telefone telefone = telefoneFornecedor.getTelefone();

			telefoneRepository.detach(telefone);
			telefone.setId(null);
			telefone.setPessoa(null);
			telefoneRepository.adicionar(telefone);
			destinatario.setTelefone(telefone);
		}

		return destinatario;
	}

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
			long idProdutoEdicao, BigInteger quantidade, int cfop,
			TipoOperacao tipoOperacao, String ufOrigem, String ufDestino,
			int naturezaOperacao, String codigoNaturezaOperacao,
			Date dataVigencia, BigDecimal valorItem, String raizCNPJ,
			String cstICMS) {
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository
				.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição "
					+ idProdutoEdicao + " não encontrado!");
		}


		ProdutoServico produtoServico = new ProdutoServico();

		produtoServico.setCodigoBarras(Long.valueOf(produtoEdicao
				.getCodigoDeBarras()));
		produtoServico.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		produtoServico.setDescricaoProduto(produtoEdicao.getProduto()
				.getNomeComercial());
		produtoServico.setNcm(produtoEdicao.getProduto().getTipoProduto()
				.getNcm().getCodigo());
		produtoServico.setProdutoEdicao(produtoEdicao);
		produtoServico.setQuantidade(quantidade);
		
		if (produtoEdicao.getDescontoLogistica() != null){
		
			BigDecimal valorDesconto = 
				valorItem.multiply(
					produtoEdicao.getDescontoLogistica().getPercentualDesconto()).divide(
						new BigDecimal(100));
			
			produtoServico.setValorUnitario(valorItem.subtract(valorDesconto));
			
			produtoServico.setValorDesconto(valorDesconto);
			
			valorItem = produtoServico.getValorUnitario();
		} else if (produtoEdicao.getProduto().getDescontoLogistica() != null) {
			
			BigDecimal valorDesconto = 
				valorItem.multiply(
					produtoEdicao.getProduto().getDescontoLogistica().getPercentualDesconto()).divide(
						new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
			
			produtoServico.setValorUnitario(valorItem.subtract(valorDesconto));
			
			produtoServico.setValorDesconto(valorDesconto);
			
			valorItem = produtoServico.getValorUnitario();
		} else {
			
			produtoServico.setValorUnitario(valorItem);
			
			produtoServico.setValorDesconto(BigDecimal.ZERO);
		}
		
		produtoServico.setUnidade(produtoEdicao.getProduto().getTipoProduto()
				.getNcm().getUnidadeMedida());	
		
		produtoServico.setCfop(cfop);
		produtoServico.setValorTotalBruto(valorItem.multiply(new BigDecimal(
				quantidade)));

		EncargoFinanceiroProduto encargoFinanceiroProduto = tributacaoService
				.calcularTributoProduto(raizCNPJ, tipoOperacao, ufOrigem,
						ufDestino, naturezaOperacao, codigoNaturezaOperacao,
						produtoEdicao.getProduto().getTipoProduto()
						.getCodigoNBM(), dataVigencia, cstICMS,
						valorItem);

		produtoServico.setEncargoFinanceiro(encargoFinanceiroProduto);

		return produtoServico;
	}

	/**
	 * Grupo do detalhamento de Produtos e Serviços da NF-e
	 * @param ufOrigem
	 * @param ufDestino
	 * @param raizCNPJ
	 * @param raizCNPJ
	 *            ,TipoOperacao tipoOperacao
	 * @param cstICMS
	 * @param descontos TODO
	 * 
	 * @return
	 */
	private ProdutoServico carregaProdutoServico(Cota cota,
			long idProdutoEdicao, BigInteger quantidade, int cfop,
			TipoOperacao tipoOperacao, String ufOrigem, String ufDestino,
			int naturezaOperacao, String codigoNaturezaOperacao,
			Date dataVigencia, BigDecimal valorItem, String raizCNPJ,
			String cstICMS, Map<String, DescontoDTO> descontos) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição "
					+ idProdutoEdicao + " não encontrado!");
		}

		ProdutoServico produtoServico = new ProdutoServico();

		produtoServico.setCodigoBarras(Long.valueOf(produtoEdicao.getCodigoDeBarras()));
		produtoServico.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		produtoServico.setDescricaoProduto(produtoEdicao.getProduto().getNomeComercial());
		produtoServico.setNcm(produtoEdicao.getProduto().getTipoProduto().getNcm().getCodigo());
		produtoServico.setProdutoEdicao(produtoEdicao);
		produtoServico.setQuantidade(quantidade);
		produtoServico.setValorUnitario(valorItem);
		produtoServico.setUnidade(produtoEdicao.getProduto().getTipoProduto().getNcm().getUnidadeMedida());

		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
		//BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, cota, produtoEdicao);
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
		produtoServico.setValorTotalBruto(
				valorItem.multiply(new BigDecimal(quantidade)));

		EncargoFinanceiroProduto encargoFinanceiroProduto = tributacaoService
				.calcularTributoProduto(raizCNPJ, tipoOperacao, ufOrigem,
						ufDestino, naturezaOperacao, codigoNaturezaOperacao,
						produtoEdicao.getProduto().getTipoProduto()
						.getCodigoNBM(), dataVigencia, cstICMS,
						valorItem);

		produtoServico.setEncargoFinanceiro(encargoFinanceiroProduto);

		return produtoServico;
	}

	@Override
	@Transactional
	public Long emitiNotaFiscal(long idTipoNotaFiscal, Date dataEmissao,
			Fornecedor fornecedor, List<ItemNotaFiscalSaida> listItemNotaFiscal,
			InformacaoTransporte transporte,
			InformacaoAdicional informacaoAdicional,
			List<NotaFiscalReferenciada> listNotaFiscalReferenciada,
			Set<Processo> processos, Condicao condicao){

		NotaFiscal notaFiscal = new NotaFiscal();

		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository
				.buscarPorId(idTipoNotaFiscal);

		if (tipoNotaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Tipo da Nota Fiscal " + idTipoNotaFiscal
					+ " não encontrada!");
		}

		notaFiscal.setIdentificacao(carregaIdentificacao(tipoNotaFiscal, dataEmissao, listNotaFiscalReferenciada));
		notaFiscal.setIdentificacaoDestinatario(carregaDestinatario(fornecedor));
		notaFiscal.setIdentificacaoEmitente(carregaEmitente());

		String raizCNPJ = notaFiscal.getIdentificacaoEmitente().getDocumento().substring(0, 7);
		String ufOrigem = notaFiscal.getIdentificacaoEmitente().getEndereco().getUf();
		String ufDestino = notaFiscal.getIdentificacaoDestinatario().getEndereco().getUf();

		notaFiscal.setProdutosServicos(new ArrayList<ProdutoServico>(listItemNotaFiscal.size()));
		int cfop;

		if (ufOrigem.equals(ufDestino)) {
			if (tipoNotaFiscal.getCfopEstado() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"CFOP do estado para tipo nota fiscal "
								+ idTipoNotaFiscal + " não encontrada!");
			}

			cfop = Integer.valueOf(tipoNotaFiscal.getCfopEstado().getCodigo());
		} else {

			if (tipoNotaFiscal.getCfopOutrosEstados() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"CFOP para outros estados para tipo nota fiscal "
								+ idTipoNotaFiscal + " não encontrada!");
			}
			cfop = Integer.valueOf(tipoNotaFiscal.getCfopOutrosEstados()
					.getCodigo());
		}

		InformacaoValoresTotais informacaoValoresTotais = new InformacaoValoresTotais();
		notaFiscal.setInformacaoValoresTotais(informacaoValoresTotais);

		notaFiscal.setInformacaoTransporte(transporte);

		notaFiscal.setInformacaoAdicional(informacaoAdicional);

		notaFiscal
		.setStatusProcessamentoInterno(StatusProcessamentoInterno.GERADA);

		notaFiscal.setProcessos(processos);

		notaFiscalRepository.adicionar(notaFiscal);

		int sequencia = 1;
		for (ItemNotaFiscalSaida itemNotaFiscal : listItemNotaFiscal) {

			ProdutoServico produtoServico = carregaProdutoServico(fornecedor,
					itemNotaFiscal.getIdProdutoEdicao(),
					itemNotaFiscal.getQuantidade(), cfop,
					tipoNotaFiscal.getTipoOperacao(), ufOrigem, ufDestino,
					tipoNotaFiscal.getNopCodigo().intValue(),
					tipoNotaFiscal.getNopDescricao(), dataEmissao,
					itemNotaFiscal.getValorUnitario(), raizCNPJ,
					itemNotaFiscal.getCstICMS());

			produtoServico.setProdutoServicoPK(new ProdutoServicoPK(notaFiscal,
					sequencia++));
			
			InformacaoAdicional info = 
					produtoServico.getProdutoServicoPK().getNotaFiscal().getInformacaoAdicional();
			
			if (info == null){
				
				info = new InformacaoAdicional();
				produtoServico.getProdutoServicoPK().getNotaFiscal().setInformacaoAdicional(info);
			}
			
			info.setInformacoesComplementares(itemNotaFiscal.getInfoComplementar());

			EncargoFinanceiro encargoFinanceiro = produtoServico
					.getEncargoFinanceiro();

			informacaoValoresTotais.setValorProdutos(BigDecimalUtil.soma(
					informacaoValoresTotais.getValorProdutos(),
					produtoServico.getValorTotalBruto()));
			
			informacaoValoresTotais.setValorNotaFiscal(informacaoValoresTotais.getValorProdutos());

			if (encargoFinanceiro instanceof EncargoFinanceiroProduto) {
				EncargoFinanceiroProduto encargoFinanceiroProduto = (EncargoFinanceiroProduto) encargoFinanceiro;
				ICMS icms = encargoFinanceiroProduto.getIcms();

				informacaoValoresTotais.setValorBaseCalculoICMS(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorBaseCalculoICMS(),
						icms.getValorBaseCalculo()));

				informacaoValoresTotais
				.setValorICMS(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorICMS(),
						icms.getValor()));

				IPI ipi = encargoFinanceiroProduto.getIpi();

				informacaoValoresTotais.setValorIPI(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorIPI(), ipi.getValor()));

				COFINS cofins = encargoFinanceiroProduto.getCofins();

				informacaoValoresTotais.setValorCOFINS(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorCOFINS(),
						cofins.getValor()));

			}
			encargoFinanceiro.setProdutoServico(produtoServico);
			encargoFinanceiroRepository.adicionar(encargoFinanceiro);
			produtoServicoRepository.adicionar(produtoServico);
			notaFiscal.getProdutosServicos().add(produtoServico);
		}
		notaFiscalRepository.merge(notaFiscal);
		return notaFiscal.getId();

	}



	@Override
	@Transactional(readOnly=true)
	public Long emitiNotaFiscal(long idTipoNotaFiscal, Date dataEmissao,
			Cota cota, List<ItemNotaFiscalSaida> listItemNotaFiscal,
			InformacaoTransporte transporte,
			InformacaoAdicional informacaoAdicional,
			List<NotaFiscalReferenciada> listNotaFiscalReferenciada,
			Set<Processo> processos, Condicao condicao) {

		NotaFiscal notaFiscal = new NotaFiscal();

		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository
				.buscarPorId(idTipoNotaFiscal);

		if (tipoNotaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Tipo da Nota Fiscal " + idTipoNotaFiscal
					+ " não encontrada!");
		}

		notaFiscal.setIdentificacao(carregaIdentificacao(tipoNotaFiscal,
				dataEmissao, listNotaFiscalReferenciada));
		notaFiscal.setIdentificacaoDestinatario(carregaDestinatario(cota));
		notaFiscal.setIdentificacaoEmitente(carregaEmitente());

		String raizCNPJ = Util.removerMascaraCnpj(notaFiscal.getIdentificacaoEmitente().getDocumento())
				.substring(0, 7);
		String ufOrigem = notaFiscal.getIdentificacaoEmitente().getEndereco()
				.getUf();
		String ufDestino = notaFiscal.getIdentificacaoDestinatario()
				.getEndereco().getUf();

		notaFiscal.setProdutosServicos(new ArrayList<ProdutoServico>(
				listItemNotaFiscal.size()));
		int cfop;

		if (ufOrigem.equals(ufDestino)) {
			if (tipoNotaFiscal.getCfopEstado() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"CFOP do estado para tipo nota fiscal "
								+ idTipoNotaFiscal + " não encontrada!");
			}

			cfop = Integer.valueOf(tipoNotaFiscal.getCfopEstado().getCodigo());
		} else {

			if (tipoNotaFiscal.getCfopOutrosEstados() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"CFOP para outros estados para tipo nota fiscal "
								+ idTipoNotaFiscal + " não encontrada!");
			}
			cfop = Integer.valueOf(tipoNotaFiscal.getCfopOutrosEstados()
					.getCodigo());
		}

		InformacaoValoresTotais informacaoValoresTotais = new InformacaoValoresTotais();
		notaFiscal.setInformacaoValoresTotais(informacaoValoresTotais);

		notaFiscal.setInformacaoTransporte(transporte);

		notaFiscal.setInformacaoAdicional(informacaoAdicional);

		notaFiscal
		.setStatusProcessamentoInterno(StatusProcessamentoInterno.GERADA);
		
		notaFiscal.setProcessos(processos);
		
		int tamanhoCampoMapeado = 60;
		
//		try {
//			tamanhoCampoMapeado = (int) Column.class.getField("length").get(
//					notaFiscal.getIdentificacao().getDescricaoNaturezaOperacao());
//			
//		} catch (IllegalArgumentException | IllegalAccessException
//				| NoSuchFieldException | SecurityException e) {
//			
//			LOGGER.warn(e.getLocalizedMessage(), e);
//			LOGGER.error(e.getMessage(), e);
//		}
		
		if (notaFiscal.getIdentificacao().getDescricaoNaturezaOperacao().length() > tamanhoCampoMapeado){
			notaFiscal.getIdentificacao().setDescricaoNaturezaOperacao(
					notaFiscal.getIdentificacao().getDescricaoNaturezaOperacao().substring(0, tamanhoCampoMapeado));
		}
		
		notaFiscalRepository.adicionar(notaFiscal);
		
		Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao();

		int sequencia = 1;
		for (ItemNotaFiscalSaida itemNotaFiscal : listItemNotaFiscal) {

			ProdutoServico produtoServico = carregaProdutoServico(cota,
					itemNotaFiscal.getIdProdutoEdicao(),
					itemNotaFiscal.getQuantidade(), cfop,
					tipoNotaFiscal.getTipoOperacao(), ufOrigem, ufDestino,
					tipoNotaFiscal.getNopCodigo().intValue(),
					tipoNotaFiscal.getNopDescricao(), dataEmissao,
					itemNotaFiscal.getValorUnitario(), raizCNPJ,
					itemNotaFiscal.getCstICMS(), descontos);

			produtoServico.setProdutoServicoPK(new ProdutoServicoPK(notaFiscal,
					sequencia++));

			EncargoFinanceiro encargoFinanceiro = produtoServico
					.getEncargoFinanceiro();

			informacaoValoresTotais.setValorProdutos(BigDecimalUtil.soma(
					informacaoValoresTotais.getValorProdutos(),
					produtoServico.getValorTotalBruto()));
			
			informacaoValoresTotais.setValorNotaFiscal(informacaoValoresTotais.getValorProdutos());

			if (encargoFinanceiro instanceof EncargoFinanceiroProduto) {
				EncargoFinanceiroProduto encargoFinanceiroProduto = (EncargoFinanceiroProduto) encargoFinanceiro;
				ICMS icms = encargoFinanceiroProduto.getIcms();

				informacaoValoresTotais.setValorBaseCalculoICMS(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorBaseCalculoICMS(),
						icms.getValorBaseCalculo()));

				informacaoValoresTotais
				.setValorICMS(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorICMS(),
						icms.getValor()));

				IPI ipi = encargoFinanceiroProduto.getIpi();

				informacaoValoresTotais.setValorIPI(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorIPI(), ipi.getValor()));

				COFINS cofins = encargoFinanceiroProduto.getCofins();

				informacaoValoresTotais.setValorCOFINS(BigDecimalUtil.soma(
						informacaoValoresTotais.getValorCOFINS(),
						cofins.getValor()));

			}
			encargoFinanceiro.setProdutoServico(produtoServico);
			encargoFinanceiroRepository.adicionar(encargoFinanceiro);
			produtoServicoRepository.adicionar(produtoServico);
			notaFiscal.getProdutosServicos().add(produtoServico);
		}
		notaFiscalRepository.merge(notaFiscal);
		return notaFiscal.getId();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#obterItensNotaFiscalPor(br
	 * .com.abril.nds.model.fiscal.GrupoNotaFiscal, java.lang.Long,
	 * br.com.abril.nds.util.Intervalo, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public List<ItemNotaFiscalSaida> obterItensNotaFiscalPor(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, Cota cota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores, List<Long> listaIdProdutos,
			TipoNotaFiscal tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> itensNotaFiscal = new ArrayList<ItemNotaFiscalSaida>();

		GrupoNotaFiscal grupoNotaFiscal = tipoNotaFiscal.getGrupoNotaFiscal();

		Long idCota = cota.getId();

		switch (grupoNotaFiscal) {

		case NF_REMESSA_CONSIGNACAO:
			itensNotaFiscal = this.obterItensNFeRemessaEmConsignacao(	
					parametrosRecolhimentoDistribuidor, idCota, periodo, listaIdFornecedores,
					listaIdProdutos, tipoNotaFiscal);
			break;

		case NF_DEVOLUCAO_REMESSA_CONSIGNACAO:

			if (cota.getParametrosCotaNotaFiscalEletronica() != null) {

				if (!cota.getParametrosCotaNotaFiscalEletronica()
						.getEmiteNotaFiscalEletronica()) {
					itensNotaFiscal = this.obterItensNFeEntradaDevolucaoRemessaConsignacao(
									parametrosRecolhimentoDistribuidor, idCota, periodo,
									listaIdFornecedores, listaIdProdutos,
									tipoNotaFiscal);
				}
			}

			break;

		case NF_DEVOLUCAO_SIMBOLICA:

			if (cota.getParametrosCotaNotaFiscalEletronica() != null) {
				if (!cota.getParametrosCotaNotaFiscalEletronica()
						.getEmiteNotaFiscalEletronica()) {
					itensNotaFiscal = this.obterItensNFeVenda(parametrosRecolhimentoDistribuidor,
							idCota, periodo, listaIdFornecedores,
							listaIdProdutos, tipoNotaFiscal);
				}
			}

			break;

		case NF_VENDA:
			itensNotaFiscal = this.obterItensNFeVenda(parametrosRecolhimentoDistribuidor, idCota,
					periodo, listaIdFornecedores, listaIdProdutos,
					tipoNotaFiscal);
			break;
		case NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO:
			itensNotaFiscal = this.obterItensNFeSaisaDevolucaoMercadoriaRecebidaConsignacao(
					parametrosRecolhimentoDistribuidor, idCota, periodo, listaIdFornecedores, 
					listaIdProdutos, tipoNotaFiscal);
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
			TipoNotaFiscal tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> listaItemNotaFiscal = null;

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque
		.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoque
		.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque
		.add(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque
		.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoque
		.add(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaService
				.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, idCota,
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
			TipoNotaFiscal tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> listaItemNotaFiscal = null;

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaService
				.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, idCota,
						tipoNotaFiscal, listaGrupoMovimentoEstoque, periodo,
						listaIdFornecedores, listaIdProduto);

		if (listaMovimentoEstoqueCota != null
				&& !listaMovimentoEstoqueCota.isEmpty()) {
			listaItemNotaFiscal = this.gerarItensNotaFiscal(listaMovimentoEstoqueCota, tipoNotaFiscal, idCota);
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
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, Long idCota, 
			Intervalo<Date> periodo,
			List<Long> listaIdFornecedores, List<Long> listaIdProduto,
			TipoNotaFiscal tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> listaItemNotaFiscal = null;

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaService
				.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, idCota,
						tipoNotaFiscal, listaGrupoMovimentoEstoque, periodo,
						listaIdFornecedores, listaIdProduto);

		if (listaMovimentoEstoqueCota != null
				&& !listaMovimentoEstoqueCota.isEmpty()) {
			listaItemNotaFiscal = this.gerarItensNotaFiscal(listaMovimentoEstoqueCota, tipoNotaFiscal, idCota);
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
			TipoNotaFiscal tipoNotaFiscal) {

		List<ItemNotaFiscalSaida> itensNFeEnvioConsignado = this
				.obterItensNFeRemessaEmConsignacao(parametrosRecolhimentoDistribuidor, idCota,
						periodo, listaIdFornecedores, listaIdProdutos,
						tipoNotaFiscal);

		List<ItemNotaFiscalSaida> itensNFeDevolucaoConsignado = this
				.obterItensNFeEntradaDevolucaoRemessaConsignacao(parametrosRecolhimentoDistribuidor,
						idCota, periodo, listaIdFornecedores, listaIdProdutos,
						tipoNotaFiscal);

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
			TipoNotaFiscal tipoNotaFiscal, Long idCota) {

		Map<Long, ItemNotaFiscalSaida> mapItemNotaFiscal = new HashMap<Long, ItemNotaFiscalSaida>();

		GrupoNotaFiscal grupoNotaFiscal = tipoNotaFiscal.getGrupoNotaFiscal();

		Cota cota = cotaRepository.buscarPorId(idCota);
		
		Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao();
		
		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

			TipoMovimentoEstoque tipoMovimentoEstoque = (TipoMovimentoEstoque) movimentoEstoqueCota.getTipoMovimento();

			GrupoMovimentoEstoque grupoMovimento = tipoMovimentoEstoque.getGrupoMovimentoEstoque();

			ProdutoEdicao produtoEdicao = movimentoEstoqueCota.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			//BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, cota, produtoEdicao);
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
		
		// Map<String, DescontoDTO> descontos = descontoService.obterDescontosMapPorLancamentoProdutoEdicao(null, null);

		for (ItemNotaFiscalSaida item : listaItemNotaFiscal) {
			quantidade = quantidade.add(item.getQuantidade());
			preco = preco.add(item.getValorUnitario().multiply(
					new BigDecimal(quantidade)));
/*
			ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(item.getIdProdutoEdicao());
			DescontoDTO descontoDTO = null;
			try {
				descontoDTO = descontoService.obterDescontoPor(descontos
							, item.getListaMovimentoEstoqueCota().get(0).getCota().getId()
							, produtoEdicao.getProduto().getFornecedor().getId()
							, produtoEdicao.getProduto().getId()
							, produtoEdicao.getId());
			} catch (Exception e) {

			}
			
			BigDecimal desconto = (descontoDTO != null ? descontoDTO.getValor() : BigDecimal.ZERO);
			*/
			precoComDesconto = precoComDesconto.add(item.getValorUnitario().multiply(new BigDecimal(item.getQuantidade())));
					/*precoComDesconto.add(
					item.getValorUnitario()
					.subtract(desconto, new MathContext(3))
					.multiply(item.getValorUnitario())
					.divide(new BigDecimal(100))).multiply(
							new BigDecimal(item.getQuantidade()));*/
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
	@Transactional(readOnly=true)
	public InformacaoTransporte obterTransporte(Long idCota) {
		InformacaoTransporte transporte = new InformacaoTransporte();

		PDV pdv = this.pdvRepository.obterPDVPrincipal(idCota);

		// OBTEM ROTEIRIZACAO POR COTA, VERIFICAR NECESSIDADE DE OBTER POR PDV,
		// DEVIDO ÀS MUDANÇAS NO MODELO DE DADOS
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		Roteirizacao roteirizacao = this.roterizacaoRepository
				.buscarRoteirizacaoDeCota(cota.getNumeroCota());

		if (pdv != null && roteirizacao != null) {
			transporte.setModalidadeFrente(0); // Por conta emitente

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
			transporte.setModalidadeFrente(1); // Por conta destinatário
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

		for (ItemNotaFiscalSaida itemNotaFiscal : listaItensNotaFiscal) {

			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = itemNotaFiscal
					.getListaMovimentoEstoqueCota();

			if (listaMovimentoEstoqueCota == null
					|| listaMovimentoEstoqueCota.isEmpty())
				continue;

			for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

				List<ProdutoServico> listaProdutoServicos = movimentoEstoqueCota
						.getListaProdutoServicos();

				if (listaProdutoServicos != null
						&& !listaProdutoServicos.isEmpty()) {

					for (ProdutoServico produtoServico : listaProdutoServicos) {

						NotaFiscal notaFiscal = produtoServico
								.getProdutoServicoPK().getNotaFiscal();

						if (notaFiscal != null) {

							GrupoNotaFiscal grupoNotaFiscal = notaFiscal
									.getIdentificacao().getTipoNotaFiscal()
									.getGrupoNotaFiscal();

							if (GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO
									.equals(grupoNotaFiscal)) {

								notaFiscalReferenciada
								.add(this
										.converterNotaFiscalToNotaFiscalReferenciada(notaFiscal));
							}
						}
					}
				}
			}
		}

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
	public NotaFiscalReferenciada converterNotaFiscalToNotaFiscalReferenciada(
			NotaFiscal notaFiscal) {

		NotaFiscalReferenciada notaReferenciada = null;

		InformacaoEletronica informacaoEletronica = notaFiscal
				.getInformacaoEletronica();

		if (informacaoEletronica != null) {

			NotaFiscalReferenciadaPK pk = new NotaFiscalReferenciadaPK();
			pk.setChaveAcesso(new BigInteger(informacaoEletronica
					.getChaveAcesso()));
			pk.setNotaFiscal(notaFiscal);

			notaReferenciada = new NotaFiscalReferenciada();
			notaReferenciada.setPk(pk);
		}

		return notaReferenciada;
	}

	private Endereco cloneEndereco(Endereco endereco)
			throws CloneNotSupportedException {
		Endereco novoEndereco = endereco.clone();
		enderecoRepository.detach(novoEndereco);
		novoEndereco.setId(null);
		novoEndereco.setPessoa(null);
		if (novoEndereco.getCep() != null) {
			novoEndereco.setCep(novoEndereco.getCep().replace("-", ""));
		}
		/*if (novoEndereco.getCodigoUf() == null
				&& novoEndereco.getCodigoCidadeIBGE() != null) {
			novoEndereco.setCodigoUf(novoEndereco
					.getCodigoCidadeIBGE().toString().substring(0, 2));
		}*/
		enderecoRepository.adicionar(novoEndereco);
		return novoEndereco;
	}

	public byte[] imprimirNotasEnvio(List<NotaEnvio> notasEnvio) {
		
		return null;

	}

}
