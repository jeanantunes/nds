package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.DistribuidorClassificacaoCotaVO;
import br.com.abril.nds.client.vo.DistribuidorPercentualExcedenteVO;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.client.vo.TiposNotasFiscaisParametrosVO;
import br.com.abril.nds.dto.RegimeTributarioDTO;
import br.com.abril.nds.dto.TributoAliquotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorClassificacaoCota;
import br.com.abril.nds.model.cadastro.DistribuidorGridDistribuicao;
import br.com.abril.nds.model.cadastro.DistribuidorPercentualExcedente;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametroEntregaBanca;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaChamadao;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.RegimeTributario;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoInterfaceLED;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.CouchDBRepository;
import br.com.abril.nds.repository.DistribuidorClassificacaoCotaRepository;
import br.com.abril.nds.repository.DistribuidorGridDistribuicaoRepository;
import br.com.abril.nds.repository.DistribuidorPercentualExcedenteRepository;
import br.com.abril.nds.repository.EnderecoDistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.repository.ParametroContratoCotaRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorFaltasSobrasRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.RegimeTributarioRepository;
import br.com.abril.nds.repository.TelefoneDistribuidorRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.repository.TipoGarantiaAceitaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.TributoAliquotaRepository;
import br.com.abril.nds.service.GrupoPermissaoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.vo.EnderecoVO;

import com.google.gson.JsonObject;

/**
 * Implementação da interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
@Service
public class ParametrosDistribuidorServiceImpl implements ParametrosDistribuidorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParametrosDistribuidorServiceImpl.class);
	
	private final static String CHECKED = "checked";

	private final static String UNDEFINED = "undefined";
	
	private static final String ATTACHMENT_LOGOTIPO = "imagem_logotipo";
	
//	private static final String DB_NAME = "db_parametro_distribuidor";
	
	@Autowired
	DistribuidorClassificacaoCotaRepository classificacaoCotaRepository;
	
	@Autowired
	DistribuidorPercentualExcedenteRepository percentualExcedenteRepository;
	
	@Autowired
	DistribuidorGridDistribuicaoRepository gridDistribuicaoRepository;
	
	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;

	@Autowired
	private ParametroContratoCotaRepository parametroContratoCotaRepository;

	@Autowired
	private ParametrosDistribuidorEmissaoDocumentoRepository parametrosDistribuidorEmissaoDocumentoRepository;

	@Autowired
	private ParametrosDistribuidorFaltasSobrasRepository parametrosDistribuidorFaltasSobrasRepository;

	@Autowired
	private TipoGarantiaAceitaRepository tipoGarantiaAceitaRepository;
	
	@Autowired
	private EnderecoDistribuidorRepository enderecoDistribuidorRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoRepository movimentoRepository ;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private TelefoneDistribuidorRepository telefoneDistribuidorRepository;
	
	@Autowired
	private RegimeTributarioRepository regimeTributarioRepository;
	
	@Autowired
	private TributoAliquotaRepository tributoAliquotaRepository;
	
	@Autowired
	private CouchDBRepository couchDBRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private GrupoPermissaoService grupoPermissaoService;
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	@PostConstruct
	public void initCouchDbClient() {
		
		String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
		try {
			Long codigoDistribuidorNumerico = Long.parseLong(codigoDistribuidor);
			if(codigoDistribuidorNumerico < 1) {
				codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorFC();
			}
		} catch (NumberFormatException nfe) {
			
			LOGGER.error("O código do Distribuidor deve ser numérico.", nfe);
			codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorFC();
		}
		
		this.couchDbClient = couchDBRepository.getCouchDBClient(codigoDistribuidor, true);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getParametrosDistribuidor()
	 */
	@Transactional(readOnly = true)
	@Override
	public ParametrosDistribuidorVO getParametrosDistribuidor() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		ParametrosDistribuidorVO parametrosDistribuidor = new ParametrosDistribuidorVO();
		
		if (distribuidor == null) {
			
			return parametrosDistribuidor;
		}
		
		// Cadastro / Fiscal
		parametrosDistribuidor.setRazaoSocial(distribuidor.getJuridica().getRazaoSocial());
		parametrosDistribuidor.setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		parametrosDistribuidor.setCnpj(distribuidor.getJuridica().getCnpj());
		parametrosDistribuidor.setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		parametrosDistribuidor.setInscricaoMunicipal(distribuidor.getJuridica().getInscricaoMunicipal());
		parametrosDistribuidor.setEmail(distribuidor.getJuridica().getEmail());
		parametrosDistribuidor.setCodigoDistribuidorDinap(distribuidor.getCodigoDistribuidorDinap());
		parametrosDistribuidor.setCodigoDistribuidorFC(distribuidor.getCodigoDistribuidorFC());
		
		parametrosDistribuidor.setEndereco(this.popularEnderecoVO(distribuidor.getEnderecoDistribuidor()));
		
		this.atribuirDadosTelefoneDistribuidor(parametrosDistribuidor,distribuidor);
		
		List<TributoAliquota> ta = distribuidor.getRegimeTributario().getTributosAliquotas();
		if(ta != null) distribuidor.getRegimeTributario().getTributosAliquotas().isEmpty();
		
		RegimeTributarioDTO regimeTributario = new RegimeTributarioDTO();
		regimeTributario.setId(distribuidor.getRegimeTributario().getId());
		regimeTributario.setCodigo(distribuidor.getRegimeTributario().getCodigo());
		regimeTributario.setDescricao(distribuidor.getRegimeTributario().getDescricao());
		regimeTributario.setAtivo(distribuidor.getRegimeTributario().isAtivo());
		
		parametrosDistribuidor.setRegimeTributario(regimeTributario);
		parametrosDistribuidor.setTipoAtividade(distribuidor.getTipoAtividade());
		parametrosDistribuidor.setPossuiRegimeEspecialDispensaInterna(distribuidor.isPossuiRegimeEspecialDispensaInterna());
		
		// Parciais / Matriz de Lançamento
		parametrosDistribuidor.setRelancamentoParciaisEmDias(distribuidor.getFatorRelancamentoParcial());
		
		// Recolhimento
		parametrosDistribuidor.setAceitaEncalheJuramentada(distribuidor.isAceitaJuramentado());
		parametrosDistribuidor.setDiaRecolhimentoPrimeiro(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoPrimeiro());
		parametrosDistribuidor.setDiaRecolhimentoSegundo(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoSegundo());
		parametrosDistribuidor.setDiaRecolhimentoTerceiro(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoTerceiro());
		parametrosDistribuidor.setDiaRecolhimentoQuarto(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuarto());
		parametrosDistribuidor.setDiaRecolhimentoQuinto(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuinto());
		parametrosDistribuidor.setLimiteCEProximaSemana(distribuidor.getParametrosRecolhimentoDistribuidor().isPermiteRecolherDiasPosteriores());
		parametrosDistribuidor.setConferenciaCegaEncalhe(distribuidor.getParametrosRecolhimentoDistribuidor().isConferenciaCegaEncalhe());
		parametrosDistribuidor.setConferenciaCegaRecebimento(distribuidor.getParametrosRecolhimentoDistribuidor().isConferenciaCegaRecebimento());
		parametrosDistribuidor.setTipoContabilizacaoCE(distribuidor.getTipoContabilizacaoCE());
		parametrosDistribuidor.setAceitaRecolhimentoParcialAtraso(distribuidor.isAceitaRecolhimentoParcialAtraso());
		parametrosDistribuidor.setSupervisionaVendaNegativa(distribuidor.isSupervisionaVendaNegativa());		
		
		// Capacidade de Manuseio
		parametrosDistribuidor.setCapacidadeManuseioHomemHoraLancamento(distribuidor.getCapacidadeDistribuicao().intValue());
		parametrosDistribuidor.setCapacidadeManuseioHomemHoraRecolhimento(distribuidor.getCapacidadeRecolhimento().intValue());
		
		//Política chamadão
		PoliticaChamadao politicaChamadao = distribuidor.getPoliticaChamadao();
		if (politicaChamadao != null) {
		    parametrosDistribuidor.setChamadaoDiasSuspensao(politicaChamadao.getDiasSuspenso());
		    parametrosDistribuidor.setChamadaoValorConsignado(CurrencyUtil.formatarValor(politicaChamadao.getValorConsignado()));
		}
		
		// Reutilização de Código de Cota
		parametrosDistribuidor.setReutilizacaoCodigoCotaInativa(distribuidor.getQntDiasReutilizacaoCodigoCota());
		
		boolean utilizaSugestaoIncrementoCodigo = false;
		
		if (distribuidor.getUtilizaSugestaoIncrementoCodigo() != null) {
			
			utilizaSugestaoIncrementoCodigo = distribuidor.getUtilizaSugestaoIncrementoCodigo();
		}
		
		parametrosDistribuidor.setUtilizaSugestaoIncrementoCodigo(utilizaSugestaoIncrementoCodigo);

		// Emissão de Documentos
		for (ParametrosDistribuidorEmissaoDocumento emissaoDocumentos : distribuidor.getParametrosDistribuidorEmissaoDocumentos()) {
			
			switch(emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento()){
			case BOLETO:
				
				parametrosDistribuidor.setBoletoEmail(emissaoDocumentos.isUtilizaEmail());
				parametrosDistribuidor.setBoletoImpressao(emissaoDocumentos.isUtilizaImpressao());
			break;
			case BOLETO_SLIP:
				
				parametrosDistribuidor.setBoletoSlipEmail(emissaoDocumentos.isUtilizaEmail());
				parametrosDistribuidor.setBoletoSlipImpressao(emissaoDocumentos.isUtilizaImpressao());
			break;
			case CHAMADA_ENCALHE:
				
				parametrosDistribuidor.setChamadaEncalheEmail(emissaoDocumentos.isUtilizaEmail());
				parametrosDistribuidor.setChamadaEncalheImpressao(emissaoDocumentos.isUtilizaImpressao());
			break;
			case NOTA_ENVIO:
				
				parametrosDistribuidor.setNotaEnvioEmail(emissaoDocumentos.isUtilizaEmail());
				parametrosDistribuidor.setNotaEnvioImpressao(emissaoDocumentos.isUtilizaImpressao());
			break;
			case RECIBO:
				
				parametrosDistribuidor.setReciboEmail(emissaoDocumentos.isUtilizaEmail());
				parametrosDistribuidor.setReciboImpressao(emissaoDocumentos.isUtilizaImpressao());
			break;
			case SLIP:
				
				parametrosDistribuidor.setSlipEmail(emissaoDocumentos.isUtilizaEmail());
				parametrosDistribuidor.setSlipImpressao(emissaoDocumentos.isUtilizaImpressao());
			break;
			}
		}
		
		parametrosDistribuidor.setNomeArquivoInterfaceLED1(distribuidor.getArquivoInterfaceLedPicking1());
		parametrosDistribuidor.setNomeArquivoInterfaceLED2(distribuidor.getArquivoInterfaceLedPicking2());
		parametrosDistribuidor.setNomeArquivoInterfaceLED3(distribuidor.getArquivoInterfaceLedPicking3());
		
		// Impressão Interface LED
		if (distribuidor.getTipoImpressaoInterfaceLED() != null)
			parametrosDistribuidor.setImpressaoInterfaceLED(distribuidor.getTipoImpressaoInterfaceLED().toString());

		// Impressão NECA / Danfe
		if (distribuidor.getTipoImpressaoNENECADANFE() != null)
			parametrosDistribuidor.setImpressaoNECADANFE(distribuidor.getTipoImpressaoNENECADANFE().toString());

		// Impressão CE
		if (distribuidor.getTipoImpressaoCE() != null)
			parametrosDistribuidor.setImpressaoCE(distribuidor.getTipoImpressaoCE().toString());
		
		// Condições de Contratação:
		boolean utilizaContratoComCotas = (distribuidor.getParametroContratoCota() != null);
		parametrosDistribuidor.setUtilizaContratoComCotas(utilizaContratoComCotas);
		if (utilizaContratoComCotas) {
			parametrosDistribuidor.setPrazoContrato(distribuidor.getParametroContratoCota().getDuracaoContratoCota());
			parametrosDistribuidor.setInformacoesComplementaresContrato(distribuidor.getParametroContratoCota().getComplementoContrato());
		}
		
		// Procuração
		parametrosDistribuidor.setUtilizaProcuracaoEntregadores(distribuidor.isUtilizaProcuracaoEntregadores());
		parametrosDistribuidor.setInformacoesComplementaresProcuracao(distribuidor.getInformacoesComplementaresProcuracao());
		
		// Termo de adesão entrega em bancas
		if (distribuidor.getParametroEntregaBanca() != null) {
		    parametrosDistribuidor.setUtilizaTermoAdesaoEntregaBancas(distribuidor.getParametroEntregaBanca().isUtilizaTermoAdesao());
		    parametrosDistribuidor.setComplementoTermoAdesaoEntregaBancas(distribuidor.getParametroEntregaBanca().getComplementoTermoAdesao());
		}
		
		// Garantia
		parametrosDistribuidor.setUtilizaGarantiaPdv(distribuidor.isUtilizaGarantiaPdv());
		
	    for (TipoGarantiaAceita tipoGarantiaAceita : distribuidor
	            .getTiposGarantiasAceita()) {
	        
	        if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.CHEQUE_CAUCAO) {
	            parametrosDistribuidor.setUtilizaChequeCaucao(true);
	            parametrosDistribuidor.setValidadeChequeCaucao(tipoGarantiaAceita.getValor());
	        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.FIADOR) {
	            parametrosDistribuidor.setUtilizaFiador(true);
	            parametrosDistribuidor.setValidadeFiador(tipoGarantiaAceita.getValor());
	        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.IMOVEL) {
	            parametrosDistribuidor.setUtilizaImovel(true);
	            parametrosDistribuidor.setValidadeImovel(tipoGarantiaAceita.getValor());
	        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.CAUCAO_LIQUIDA) {
	            parametrosDistribuidor.setUtilizaCaucaoLiquida(true);
	            parametrosDistribuidor.setValidadeCaucaoLiquida(tipoGarantiaAceita.getValor());
	        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.NOTA_PROMISSORIA) {
	            parametrosDistribuidor.setUtilizaNotaPromissoria(true);
	            parametrosDistribuidor.setValidadeNotaPromissoria(tipoGarantiaAceita.getValor());
	        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.ANTECEDENCIA_VALIDADE) {
	            parametrosDistribuidor.setValidadeAntecedenciaValidade(tipoGarantiaAceita.getValor());
	        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.OUTROS) {
	            parametrosDistribuidor.setUtilizaOutros(true);
	            parametrosDistribuidor.setValidadeOutros(tipoGarantiaAceita.getValor());
	        }
	    }
	    
		// Negociação de Dividas
		if (distribuidor.getPoliticaSuspensao() != null) {
			if (distribuidor.getPoliticaSuspensao().getNumeroAcumuloDividaWrapped() != null) 
				parametrosDistribuidor.setSugereSuspensaoQuandoAtingirBoletos(CurrencyUtil.formatarValorTruncado(distribuidor.getPoliticaSuspensao().getNumeroAcumuloDivida()));
			if (distribuidor.getPoliticaSuspensao().getValor() != null)
				parametrosDistribuidor.setSugereSuspensaoQuandoAtingirReais(CurrencyUtil.formatarValor(distribuidor.getPoliticaSuspensao().getValor()));
			
			parametrosDistribuidor.setSugereSuspensao(distribuidor.isSugereSuspensao());
		}

		parametrosDistribuidor.setParcelamentoDividas(distribuidor.isParcelamentoDividas());
		
		if(distribuidor.getDescontoCotaNegociacao() == null ||  distribuidor.getDescontoCotaNegociacao().compareTo(BigDecimal.ZERO) == 0) {
			parametrosDistribuidor.setUtilizaDesconto(false);
			parametrosDistribuidor.setPercentualDesconto(CurrencyUtil.formatarValor(0));
		} else {
			parametrosDistribuidor.setUtilizaDesconto(true);
			parametrosDistribuidor.setPercentualDesconto(CurrencyUtil.formatarValor(distribuidor.getDescontoCotaNegociacao()));
		}
		
		parametrosDistribuidor.setNegociacaoAteParcelas(CurrencyUtil.formatarValorTruncado(distribuidor.getNegociacaoAteParcelas()));
		
		parametrosDistribuidor.setAceitaBaixaPagamentoMaior(
			(distribuidor.getAceitaBaixaPagamentoMaior() == null) ? false : distribuidor.getAceitaBaixaPagamentoMaior());
		
		parametrosDistribuidor.setAceitaBaixaPagamentoMenor(
			(distribuidor.getAceitaBaixaPagamentoMenor() == null) ? false : distribuidor.getAceitaBaixaPagamentoMenor());
		
		parametrosDistribuidor.setAceitaBaixaPagamentoVencido(
			(distribuidor.getAceitaBaixaPagamentoVencido() == null) ? false : distribuidor.getAceitaBaixaPagamentoVencido());
		
		parametrosDistribuidor.setNumeroDiasNovaCobranca(distribuidor.getNumeroDiasNovaCobranca());
		parametrosDistribuidor.setAssuntoEmailCobranca(distribuidor.getAssuntoEmailCobranca());
		parametrosDistribuidor.setMensagemEmailCobranca(distribuidor.getMensagemEmailCobranca());
		
		parametrosDistribuidor.setDescricaoTaxaExtra(distribuidor.getDescricaoTaxaExtra());
		parametrosDistribuidor.setPercentualTaxaExtra(distribuidor.getPercentualTaxaExtra()!=null ?distribuidor.getPercentualTaxaExtra().toString():null);
		
		// Aprovação
		parametrosDistribuidor.setUtilizaControleAprovacao(distribuidor.isUtilizaControleAprovacao());
		
		if (distribuidor.getParametrosAprovacaoDistribuidor() != null) {
			parametrosDistribuidor.setParaDebitosCreditos(distribuidor.getParametrosAprovacaoDistribuidor().isDebitoCredito());
			parametrosDistribuidor.setNegociacao(distribuidor.getParametrosAprovacaoDistribuidor().isNegociacao());
			parametrosDistribuidor.setAjusteEstoque(distribuidor.getParametrosAprovacaoDistribuidor().isAjusteEstoque());
			parametrosDistribuidor.setPostergacaoCobranca(distribuidor.getParametrosAprovacaoDistribuidor().isPostergacaoCobranca());
			parametrosDistribuidor.setDevolucaoFornecedor(distribuidor.getParametrosAprovacaoDistribuidor().isDevolucaoFornecedor());
			parametrosDistribuidor.setFaltasSobras(distribuidor.getParametrosAprovacaoDistribuidor().isFaltasSobras());
		}
		
		parametrosDistribuidor.setPrazoFollowUp(distribuidor.getPrazoFollowUp());
		parametrosDistribuidor.setPrazoAvisoPrevioValidadeGarantia(distribuidor.getPrazoAvisoPrevioValidadeGarantia());
		
		for (ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras : distribuidor.getParametrosDistribuidorFaltasSobras()) {
			if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.APROVACAO) {
				parametrosDistribuidor.setAprovacaoFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setAprovacaoFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setAprovacaoSobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setAprovacaoFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));
			}
		}
		
		parametrosDistribuidor.setCnae(distribuidor.getCnae());
		parametrosDistribuidor.setNfInformacoesAdicionais(distribuidor.getNfInformacoesAdicionais());
		parametrosDistribuidor.setNumeroDispositivoLegal(distribuidor.getNumeroDispositivoLegal());
		parametrosDistribuidor.setDataLimiteVigenciaRegimeEspecial(distribuidor.getDataLimiteVigenciaRegimeEspecial());
		
        // Aba Distribuição - Grid Distribuição
        DistribuidorGridDistribuicao gridDistribuicao = distribuidor.getGridDistribuicao();
        if (gridDistribuicao == null) {
                gridDistribuicao = new DistribuidorGridDistribuicao();
                gridDistribuicao.setVendaMediaMais(1);
                gridDistribuicao.setPracaVeraneio(false);
                gridDistribuicao.setComplementarAutomatico(true);
                gridDistribuicao.setGeracaoAutomaticaEstudo(false);
                gridDistribuicao.setPercentualMaximoFixacao(50);
        }
        parametrosDistribuidor.setGeracaoAutomaticaEstudo(gridDistribuicao.isGeracaoAutomaticaEstudo());
        parametrosDistribuidor.setVendaMediaMais(gridDistribuicao.getVendaMediaMais());
        parametrosDistribuidor.setPracaVeraneio(gridDistribuicao.isPracaVeraneio());
        parametrosDistribuidor.setComplementarAutomatico(gridDistribuicao.isComplementarAutomatico());
        parametrosDistribuidor.setPercentualMaximoFixacao(gridDistribuicao.getPercentualMaximoFixacao());
        
        // Aba Distribuição - Grid Classificação Cota
        List<DistribuidorClassificacaoCotaVO> listClassificacaoCotaVO = new ArrayList<>();
        
        List<DistribuidorClassificacaoCota> listClassificacaoCota = distribuidor.getListClassificacaoCota();
        
        if (listClassificacaoCota != null) {
                for (DistribuidorClassificacaoCota classificacaoCota : listClassificacaoCota) {
                        DistribuidorClassificacaoCotaVO classificacaoCotaVO = new DistribuidorClassificacaoCotaVO();
                        classificacaoCotaVO.setId(classificacaoCota.getId());
                        classificacaoCotaVO.setCodigoClassificacaoCota(classificacaoCota.getCodigoClassificacaoCota());
                        classificacaoCotaVO.setValorDe(classificacaoCota.getValorDe());
                        classificacaoCotaVO.setValorAte(classificacaoCota.getValorAte());
                        listClassificacaoCotaVO.add(classificacaoCotaVO);
                }
        }
        
        parametrosDistribuidor.setListClassificacaoCota(listClassificacaoCotaVO);
		
		// Aba Distribuição - Grid Percentual de Excedente
        
		List<DistribuidorPercentualExcedenteVO> listPercentualExcedenteVO = new ArrayList<>();
        
		List<DistribuidorPercentualExcedente> listPercentualExcedente = distribuidor.getListPercentualExcedente();
                
			if (listPercentualExcedente != null) {
                    for (DistribuidorPercentualExcedente percentualExcedente : listPercentualExcedente) {
                            DistribuidorPercentualExcedenteVO percentualExcedenteVO = new DistribuidorPercentualExcedenteVO();
                            percentualExcedenteVO.setId(percentualExcedente.getId());
                            percentualExcedenteVO.setEficiencia(percentualExcedente.getEficiencia());
                            percentualExcedenteVO.setVenda(percentualExcedente.getVenda());
                            percentualExcedenteVO.setPdv(percentualExcedente.getPdv());
                            listPercentualExcedenteVO.add(percentualExcedenteVO);
                    }
            }
				
        parametrosDistribuidor.setListPercentualExcedente(listPercentualExcedenteVO);
        
        parametrosDistribuidor.setPararAcumuloDividas(distribuidor.isPararAcumuloDividas());
                
		return parametrosDistribuidor;
	}
	
	private void atribuirDadosTelefoneDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor, Distribuidor distribuidor) {
		
		TelefoneDistribuidor telefone = this.getTefoneDistribuidorPrincipal(distribuidor);
		
		if(telefone!= null){
			parametrosDistribuidor.setNumeroDDD(telefone.getTelefone().getDdd());
			parametrosDistribuidor.setNumeroTelefone(telefone.getTelefone().getNumero());
		}
	}
	
	private TelefoneDistribuidor getTefoneDistribuidorPrincipal(Distribuidor distribuidor){
		
		for(TelefoneDistribuidor item : distribuidor.getTelefones()){
			if(item.isPrincipal()){
				return item;
			}
		}
		
		return null;
	}
	
	private void gravarTelefoneDistribuidor(Distribuidor distribuidor, String numerotelefone, String numeroDD){
		
		TelefoneDistribuidor telefoneFDistribuidor = this.getTefoneDistribuidorPrincipal(distribuidor);
		
		if (telefoneFDistribuidor == null){
			
			Telefone telefone = new Telefone();
			telefone.setDdd(numeroDD);
			telefone.setNumero(numerotelefone);
			
			telefone.setId(telefoneRepository.adicionar(telefone));
			
			telefoneFDistribuidor = new TelefoneDistribuidor();
			
			telefoneFDistribuidor.setDistribuidor(distribuidor);
			telefoneFDistribuidor.setPrincipal(true);
			telefoneFDistribuidor.setTipoTelefone(TipoTelefone.COMERCIAL);
			telefoneFDistribuidor.setTelefone(telefone);
			
			telefoneDistribuidorRepository.adicionar(telefoneFDistribuidor);
			
		}
		else{
			
			Telefone telefone = telefoneFDistribuidor.getTelefone();
			telefone.setDdd(numeroDD);
			telefone.setNumero(numerotelefone);
			
			telefoneRepository.alterar(telefone);
		}
	}

	private PessoaJuridica gravarPessoaJuridicaDistribuidor(Distribuidor distribuidor,
															ParametrosDistribuidorVO parametrosDistribuidor) {
		
		PessoaJuridica pessoaJuridica = distribuidor.getJuridica();
		
		if (pessoaJuridica == null) {
			
			pessoaJuridica = new PessoaJuridica();
		}
		
		pessoaJuridica.setRazaoSocial(parametrosDistribuidor.getRazaoSocial());
		pessoaJuridica.setNomeFantasia(parametrosDistribuidor.getNomeFantasia());
		pessoaJuridica.setCnpj(parametrosDistribuidor.getCnpj());
		pessoaJuridica.setInscricaoEstadual(parametrosDistribuidor.getInscricaoEstadual());
		pessoaJuridica.setInscricaoMunicipal(parametrosDistribuidor.getInscricaoMunicipal());
		pessoaJuridica.setEmail(parametrosDistribuidor.getEmail());
		
		this.pessoaRepository.merge(pessoaJuridica);
		
		return pessoaJuridica;
	}

	private EnderecoDistribuidor gravarEnderecoDistribuidor(Distribuidor distribuidor,
														 	 EnderecoVO enderecoVO) {
		
		EnderecoDistribuidor enderecoDistribuidor = distribuidor.getEnderecoDistribuidor();
		
		if (enderecoDistribuidor == null) {
			
			enderecoDistribuidor = new EnderecoDistribuidor();
		}
		
		Endereco endereco = enderecoDistribuidor.getEndereco();

		if (endereco == null) {
			
			endereco = new Endereco();
		}
		
		endereco.setCep(enderecoVO.getCep());
		endereco.setTipoLogradouro(enderecoVO.getTipoLogradouro());
		endereco.setLogradouro(enderecoVO.getLogradouro());
		endereco.setNumero((enderecoVO.getNumero() == null || enderecoVO.getNumero().trim().isEmpty()) ? null : enderecoVO.getNumero());
		endereco.setComplemento(enderecoVO.getComplemento());
		endereco.setBairro(enderecoVO.getBairro());
		endereco.setCidade(enderecoVO.getLocalidade());
		endereco.setUf(enderecoVO.getUf());
		
		endereco.setCodigoCidadeIBGE(
			(enderecoVO.getCodigoCidadeIBGE() == null) ? null : enderecoVO.getCodigoCidadeIBGE().intValue());
		
		enderecoDistribuidor.setPrincipal(true);
		enderecoDistribuidor.setDistribuidor(distribuidor);
		enderecoDistribuidor.setTipoEndereco(enderecoVO.getTipoEndereco());
		enderecoDistribuidor.setEndereco(endereco);
		enderecoDistribuidor.setPrincipal(true);
		
		enderecoDistribuidorRepository.merge(enderecoDistribuidor);
		
		return enderecoDistribuidor;
	}
	
	private EnderecoVO popularEnderecoVO(EnderecoDistribuidor enderecoDistribuidor) {
		
		EnderecoVO enderecoVO = new EnderecoVO();
		
		if (enderecoDistribuidor == null) {
			
			return enderecoVO;
		}
		
		enderecoVO.setTipoEndereco(enderecoDistribuidor.getTipoEndereco());
		
		Endereco endereco = enderecoDistribuidor.getEndereco();
		
		if (endereco == null) {
			
			return enderecoVO;
		}
		
		enderecoVO.setCep(endereco.getCep());
		enderecoVO.setTipoLogradouro(endereco.getTipoLogradouro());
		enderecoVO.setLogradouro(endereco.getLogradouro());
		enderecoVO.setNumero(endereco.getNumero());
		enderecoVO.setComplemento(endereco.getComplemento());
		enderecoVO.setBairro(endereco.getBairro());
		enderecoVO.setLocalidade(endereco.getCidade());
		enderecoVO.setUf(endereco.getUf());
		
		enderecoVO.setCodigoCidadeIBGE((endereco.getCodigoCidadeIBGE() == null) ? null : endereco.getCodigoCidadeIBGE().longValue());
		
		return enderecoVO;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#salvarDistribuidor(br.com.abril.nds.client.vo.ParametrosDistribuidorVO, java.io.InputStream, java.lang.String)
	 */
	@Transactional
	@Override
	public void salvarDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor,
								   InputStream imgLogotipo,
								   String imgContentType) {
		
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor == null) {
			
			distribuidor = new Distribuidor();
		}
		
		// Cadastro / Fiscal
		
		distribuidor.setJuridica(this.gravarPessoaJuridicaDistribuidor(distribuidor, parametrosDistribuidor));
		
		distribuidor.setCodigoDistribuidorDinap(parametrosDistribuidor.getCodigoDistribuidorDinap());
		distribuidor.setCodigoDistribuidorFC(parametrosDistribuidor.getCodigoDistribuidorFC());
		
		distribuidor.setTipoAtividade(parametrosDistribuidor.getTipoAtividade());
		distribuidor.setPossuiRegimeEspecialDispensaInterna(parametrosDistribuidor.isPossuiRegimeEspecialDispensaInterna());
		
		// Parciais / Matriz de Lançamento
		distribuidor.setFatorRelancamentoParcial(parametrosDistribuidor.getRelancamentoParciaisEmDias());

		// Recolhimento
		distribuidor.setAceitaJuramentado(parametrosDistribuidor.isAceitaEncalheJuramentada());
		
		distribuidor.setTipoContabilizacaoCE(parametrosDistribuidor.getTipoContabilizacaoCE());
		
		distribuidor.setAceitaRecolhimentoParcialAtraso(parametrosDistribuidor.isAceitaRecolhimentoParcialAtraso());
		
		distribuidor.setSupervisionaVendaNegativa(parametrosDistribuidor.isSupervisionaVendaNegativa());
		PoliticaChamadao politicaChamadao = distribuidor.getPoliticaChamadao();
		
		if(politicaChamadao == null) {
		    politicaChamadao = new PoliticaChamadao();
		    distribuidor.setPoliticaChamadao(politicaChamadao);
		}
		
		Integer chamadaoDiasSuspensao = parametrosDistribuidor.getChamadaoDiasSuspensao();
		politicaChamadao.setDiasSuspenso(chamadaoDiasSuspensao);

		BigDecimal chamadaoConsignado = null;
		
		if (parametrosDistribuidor.getChamadaoValorConsignado() != null) {
			
			chamadaoConsignado = CurrencyUtil.getBigDecimal(parametrosDistribuidor.getChamadaoValorConsignado());
			
			politicaChamadao.setValorConsignado(chamadaoConsignado);
		}
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = null;
		if (distribuidor.getParametrosRecolhimentoDistribuidor() != null) {
			parametrosRecolhimentoDistribuidor = distribuidor.getParametrosRecolhimentoDistribuidor();
		} else {
			parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		}
			
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(parametrosDistribuidor.isDiaRecolhimentoPrimeiro());
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(parametrosDistribuidor.isDiaRecolhimentoSegundo());
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(parametrosDistribuidor.isDiaRecolhimentoTerceiro());
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(parametrosDistribuidor.isDiaRecolhimentoQuarto());
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(parametrosDistribuidor.isDiaRecolhimentoQuinto());
		parametrosRecolhimentoDistribuidor.setPermiteRecolherDiasPosteriores(parametrosDistribuidor.isLimiteCEProximaSemana());

		this.tratarDadosConferenciaCega(parametrosRecolhimentoDistribuidor, parametrosDistribuidor);
		
		distribuidor.setParametrosRecolhimentoDistribuidor(parametrosRecolhimentoDistribuidor);
		
		distribuidor.setCapacidadeDistribuicao((parametrosDistribuidor.getCapacidadeManuseioHomemHoraLancamento() != null)
												? new BigInteger(parametrosDistribuidor.getCapacidadeManuseioHomemHoraLancamento().toString())
												: null);
		
		distribuidor.setCapacidadeRecolhimento((parametrosDistribuidor.getCapacidadeManuseioHomemHoraRecolhimento() != null)
												? new BigInteger(parametrosDistribuidor.getCapacidadeManuseioHomemHoraRecolhimento().toString())
												: null);
		
		distribuidor.setQntDiasReutilizacaoCodigoCota(
			parametrosDistribuidor.getReutilizacaoCodigoCotaInativa() == null 
				? null : parametrosDistribuidor.getReutilizacaoCodigoCotaInativa() * 30);
		
		distribuidor.setUtilizaSugestaoIncrementoCodigo(parametrosDistribuidor.isUtilizaSugestaoIncrementoCodigo());

		// Emissão de Documentos
		ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumentoBoleto = new ParametrosDistribuidorEmissaoDocumento();
		parametrosDistribuidorEmissaoDocumentoBoleto.setDistribuidor(distribuidor);
		parametrosDistribuidorEmissaoDocumentoBoleto.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.BOLETO);
		parametrosDistribuidorEmissaoDocumentoBoleto.setUtilizaEmail(parametrosDistribuidor.getBoletoEmail());
		parametrosDistribuidorEmissaoDocumentoBoleto.setUtilizaImpressao(parametrosDistribuidor.getBoletoImpressao());

		ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumentoBoletoSlip = new ParametrosDistribuidorEmissaoDocumento();
		parametrosDistribuidorEmissaoDocumentoBoletoSlip.setDistribuidor(distribuidor);
		parametrosDistribuidorEmissaoDocumentoBoletoSlip.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP);
		parametrosDistribuidorEmissaoDocumentoBoletoSlip.setUtilizaEmail(parametrosDistribuidor.getBoletoSlipEmail());
		parametrosDistribuidorEmissaoDocumentoBoletoSlip.setUtilizaImpressao(parametrosDistribuidor.getBoletoSlipImpressao());

		ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumentoChamadaEncalhe = new ParametrosDistribuidorEmissaoDocumento();
		parametrosDistribuidorEmissaoDocumentoChamadaEncalhe.setDistribuidor(distribuidor);
		parametrosDistribuidorEmissaoDocumentoChamadaEncalhe.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.CHAMADA_ENCALHE);
		parametrosDistribuidorEmissaoDocumentoChamadaEncalhe.setUtilizaEmail(parametrosDistribuidor.getChamadaEncalheEmail());
		parametrosDistribuidorEmissaoDocumentoChamadaEncalhe.setUtilizaImpressao(parametrosDistribuidor.getChamadaEncalheImpressao());

		ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumentoNotaEnvio = new ParametrosDistribuidorEmissaoDocumento();
		parametrosDistribuidorEmissaoDocumentoNotaEnvio.setDistribuidor(distribuidor);
		parametrosDistribuidorEmissaoDocumentoNotaEnvio.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO);
		parametrosDistribuidorEmissaoDocumentoNotaEnvio.setUtilizaEmail(parametrosDistribuidor.getNotaEnvioEmail());
		parametrosDistribuidorEmissaoDocumentoNotaEnvio.setUtilizaImpressao(parametrosDistribuidor.getNotaEnvioImpressao());

		ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumentoRecibo = new ParametrosDistribuidorEmissaoDocumento();
		parametrosDistribuidorEmissaoDocumentoRecibo.setDistribuidor(distribuidor);
		parametrosDistribuidorEmissaoDocumentoRecibo.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.RECIBO);
		parametrosDistribuidorEmissaoDocumentoRecibo.setUtilizaEmail(parametrosDistribuidor.getReciboEmail());
		parametrosDistribuidorEmissaoDocumentoRecibo.setUtilizaImpressao(parametrosDistribuidor.getReciboImpressao());

		ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumentoSlip = new ParametrosDistribuidorEmissaoDocumento();
		parametrosDistribuidorEmissaoDocumentoSlip.setDistribuidor(distribuidor);
		parametrosDistribuidorEmissaoDocumentoSlip.setTipoParametrosDistribuidorEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento.SLIP);
		parametrosDistribuidorEmissaoDocumentoSlip.setUtilizaEmail(parametrosDistribuidor.getSlipEmail());
		parametrosDistribuidorEmissaoDocumentoSlip.setUtilizaImpressao(parametrosDistribuidor.getSlipImpressao());

		List<ParametrosDistribuidorEmissaoDocumento> listaParametrosDistribuidorEmissaoDocumentos = new ArrayList<ParametrosDistribuidorEmissaoDocumento>();

		parametrosDistribuidorEmissaoDocumentoRepository.alterarOuCriar(parametrosDistribuidorEmissaoDocumentoBoleto);
		parametrosDistribuidorEmissaoDocumentoRepository.alterarOuCriar(parametrosDistribuidorEmissaoDocumentoBoletoSlip);
		parametrosDistribuidorEmissaoDocumentoRepository.alterarOuCriar(parametrosDistribuidorEmissaoDocumentoChamadaEncalhe);
		parametrosDistribuidorEmissaoDocumentoRepository.alterarOuCriar(parametrosDistribuidorEmissaoDocumentoNotaEnvio);
		parametrosDistribuidorEmissaoDocumentoRepository.alterarOuCriar(parametrosDistribuidorEmissaoDocumentoRecibo);
		parametrosDistribuidorEmissaoDocumentoRepository.alterarOuCriar(parametrosDistribuidorEmissaoDocumentoSlip);
		
		listaParametrosDistribuidorEmissaoDocumentos.add(parametrosDistribuidorEmissaoDocumentoBoleto);
		listaParametrosDistribuidorEmissaoDocumentos.add(parametrosDistribuidorEmissaoDocumentoBoletoSlip);
		listaParametrosDistribuidorEmissaoDocumentos.add(parametrosDistribuidorEmissaoDocumentoChamadaEncalhe);
		listaParametrosDistribuidorEmissaoDocumentos.add(parametrosDistribuidorEmissaoDocumentoNotaEnvio);
		listaParametrosDistribuidorEmissaoDocumentos.add(parametrosDistribuidorEmissaoDocumentoRecibo);
		listaParametrosDistribuidorEmissaoDocumentos.add(parametrosDistribuidorEmissaoDocumentoSlip);
		distribuidor.setParametrosDistribuidorEmissaoDocumentos(listaParametrosDistribuidorEmissaoDocumentos);
		
		// Impressão Interface LED
		if (parametrosDistribuidor.getImpressaoInterfaceLED() != null && !parametrosDistribuidor.getImpressaoInterfaceLED().isEmpty() &&  !parametrosDistribuidor.getImpressaoInterfaceLED().equalsIgnoreCase(UNDEFINED)) {
			TipoImpressaoInterfaceLED tipoImpressaoInterfaceLED = Enum.valueOf(TipoImpressaoInterfaceLED.class, parametrosDistribuidor.getImpressaoInterfaceLED());
			distribuidor.setTipoImpressaoInterfaceLED(tipoImpressaoInterfaceLED);
		} else {
			distribuidor.setTipoImpressaoInterfaceLED(null);
		}
		
		distribuidor.setArquivoInterfaceLedPicking1(parametrosDistribuidor.getNomeArquivoInterfaceLED1());;
		distribuidor.setArquivoInterfaceLedPicking2(parametrosDistribuidor.getNomeArquivoInterfaceLED2());;
		distribuidor.setArquivoInterfaceLedPicking3(parametrosDistribuidor.getNomeArquivoInterfaceLED3());;
		
		distribuidor.setPararAcumuloDividas(parametrosDistribuidor.isPararAcumuloDividas());

		// Impressão NECA / Danfe
		if (parametrosDistribuidor.getImpressaoNECADANFE() != null && !parametrosDistribuidor.getImpressaoNECADANFE().isEmpty() && !parametrosDistribuidor.getImpressaoNECADANFE().equalsIgnoreCase(UNDEFINED)) {
			TipoImpressaoNENECADANFE tipoImpressaoNECADANFE = Enum.valueOf(TipoImpressaoNENECADANFE.class, parametrosDistribuidor.getImpressaoNECADANFE());
			distribuidor.setTipoImpressaoNENECADANFE(tipoImpressaoNECADANFE);
		} else {
			distribuidor.setTipoImpressaoNENECADANFE(null);
		}

		// Impressão CE
		if (parametrosDistribuidor.getImpressaoCE() != null && !parametrosDistribuidor.getImpressaoCE().isEmpty() && !parametrosDistribuidor.getImpressaoCE().equalsIgnoreCase(UNDEFINED)) {
			TipoImpressaoCE tipoImpressaoCE = Enum.valueOf(TipoImpressaoCE.class, parametrosDistribuidor.getImpressaoCE());
			distribuidor.setTipoImpressaoCE(tipoImpressaoCE);
		} else {
			distribuidor.setTipoImpressaoCE(null);
		}

		// Condições de Contratação:
		boolean utilizaContratoComCotas = parametrosDistribuidor.isUtilizaContratoComCotas();
		if (utilizaContratoComCotas) {
			ParametroContratoCota parametroContratoCota = null;
			if (distribuidor.getParametroContratoCota() != null) {
				parametroContratoCota = distribuidor.getParametroContratoCota();
			} else {
				parametroContratoCota = new ParametroContratoCota();
				distribuidor.setParametroContratoCota(parametroContratoCota);
			}
			parametroContratoCota.setDuracaoContratoCota(parametrosDistribuidor.getPrazoContrato());
			parametroContratoCota.setComplementoContrato(parametrosDistribuidor.getInformacoesComplementaresContrato());
		} else {
			distribuidor.setParametroContratoCota(null);
		}
		
		// Procuração Entregadores
		distribuidor.setUtilizaProcuracaoEntregadores(parametrosDistribuidor.isUtilizaProcuracaoEntregadores());
		if (parametrosDistribuidor.isUtilizaProcuracaoEntregadores()) {
		    distribuidor.setInformacoesComplementaresProcuracao(parametrosDistribuidor.getInformacoesComplementaresProcuracao());
		} else {
		    distribuidor.setInformacoesComplementaresProcuracao(null);
		}
		
		// Termo Adesão entrega em bancas
		if (parametrosDistribuidor.isUtilizaTermoAdesaoEntregaBancas()) {
            ParametroEntregaBanca parametro = new ParametroEntregaBanca();
            distribuidor.setParametroEntregaBanca(parametro);
            parametro.setUtilizaTermoAdesao(true);
            parametro.setComplementoTermoAdesao(parametrosDistribuidor.getComplementoTermoAdesaoEntregaBancas());
        } else {
            distribuidor.setParametroEntregaBanca(null);
        }

		processaUtilizacaoGarantiasPDV(parametrosDistribuidor, distribuidor);

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		if (parametrosDistribuidor.getSugereSuspensaoQuandoAtingirBoletos() != null && !parametrosDistribuidor.getSugereSuspensaoQuandoAtingirBoletos().isEmpty()) {
			politicaSuspensao.setNumeroAcumuloDivida(CurrencyUtil.converterValor(parametrosDistribuidor.getSugereSuspensaoQuandoAtingirBoletos()).intValueExact());
		} else {
			politicaSuspensao.setNumeroAcumuloDivida(null);
		}
		
		if (parametrosDistribuidor.getSugereSuspensaoQuandoAtingirReais() != null && !parametrosDistribuidor.getSugereSuspensaoQuandoAtingirReais().isEmpty()) {
			politicaSuspensao.setValor(CurrencyUtil.getBigDecimal(parametrosDistribuidor.getSugereSuspensaoQuandoAtingirReais()));
		} else {
			politicaSuspensao.setValor(null);
		}

		distribuidor.setPoliticaSuspensao(politicaSuspensao);
		
		distribuidor.setSugereSuspensao(parametrosDistribuidor.isSugereSuspensao());
				
		if(parametrosDistribuidor.getUtilizaDesconto() != null && parametrosDistribuidor.getUtilizaDesconto())
			distribuidor.setDescontoCotaNegociacao(CurrencyUtil.converterValor(parametrosDistribuidor.getPercentualDesconto()));
		else
			distribuidor.setDescontoCotaNegociacao(BigDecimal.ZERO);
		
		if (parametrosDistribuidor.getNegociacaoAteParcelas() != null && !parametrosDistribuidor.getNegociacaoAteParcelas().isEmpty()) {
			distribuidor.setNegociacaoAteParcelas(CurrencyUtil.converterValor(parametrosDistribuidor.getNegociacaoAteParcelas()).intValueExact());
		} else {
			distribuidor.setNegociacaoAteParcelas(null);
		}
		
		distribuidor.setAceitaBaixaPagamentoMaior(parametrosDistribuidor.isAceitaBaixaPagamentoMaior());
		distribuidor.setAceitaBaixaPagamentoMenor(parametrosDistribuidor.isAceitaBaixaPagamentoMenor());
		distribuidor.setAceitaBaixaPagamentoVencido(parametrosDistribuidor.isAceitaBaixaPagamentoVencido());
		distribuidor.setNumeroDiasNovaCobranca(parametrosDistribuidor.getNumeroDiasNovaCobranca());
		distribuidor.setAssuntoEmailCobranca(parametrosDistribuidor.getAssuntoEmailCobranca());
		distribuidor.setMensagemEmailCobranca(parametrosDistribuidor.getMensagemEmailCobranca());
		distribuidor.setDescricaoTaxaExtra(parametrosDistribuidor.getDescricaoTaxaExtra());
		
		if(!parametrosDistribuidor.getPercentualTaxaExtra().equals("0")) {
			distribuidor.setPercentualTaxaExtra(new BigDecimal(parametrosDistribuidor.getPercentualTaxaExtra()));			
		}
		
		
		// Aprovação
		distribuidor.setUtilizaControleAprovacao(parametrosDistribuidor.getUtilizaControleAprovacao());
		
		if (parametrosDistribuidor.getUtilizaControleAprovacao()) {
		    
			this.validarUtilizacaoControleAprovacao(parametrosDistribuidor);
		}
		
		ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor = new ParametrosAprovacaoDistribuidor();
		
		parametrosAprovacaoDistribuidor.setDebitoCredito(parametrosDistribuidor.getParaDebitosCreditos());
		
		this.atualizarTiposMovimentoFinanceiro(parametrosDistribuidor,
											   this.getGruposMovimentoFinanceiroDebitosCreditos(),
											   !parametrosDistribuidor.getParaDebitosCreditos());
		
		parametrosAprovacaoDistribuidor.setNegociacao(parametrosDistribuidor.getNegociacao());
		
		this.atualizarTiposMovimentoFinanceiro(parametrosDistribuidor,
				 					 		   this.getGruposMovimentoFinanceiroNegociacao(),
				 					 		   !parametrosDistribuidor.getNegociacao());
		
		parametrosAprovacaoDistribuidor.setAjusteEstoque(parametrosDistribuidor.getAjusteEstoque());
				
		parametrosAprovacaoDistribuidor.setPostergacaoCobranca(parametrosDistribuidor.getPostergacaoCobranca());
		
		this.atualizarTiposMovimentoFinanceiro(parametrosDistribuidor,
									 		   this.getGruposMovimentoFinanceiroPostergacaoCobranca(),
									 		   !parametrosDistribuidor.getPostergacaoCobranca());
		
		parametrosAprovacaoDistribuidor.setDevolucaoFornecedor(parametrosDistribuidor.getDevolucaoFornecedor());
		
		TipoMovimentoEstoque tipoMovimentoEstoque = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
		tipoMovimentoEstoque.setAprovacaoAutomatica(!parametrosDistribuidor.getDevolucaoFornecedor());
		this.tipoMovimentoEstoqueRepository.merge(tipoMovimentoEstoque);
		
		/*this.atualizarTiposMovimentoEstoque(parametrosDistribuidor,
				 					 		this.getGruposMovimentoEstoqueDevolucaoFornecedor(),
				 					 		!parametrosDistribuidor.getDevolucaoFornecedor());*/
		
		parametrosAprovacaoDistribuidor.setFaltasSobras(parametrosDistribuidor.getFaltasSobras());
		
		this.atualizarTiposMovimentoEstoque(parametrosDistribuidor,
						 					this.getGruposMovimentoEstoqueFaltasSobras(),
						 					!parametrosDistribuidor.getFaltasSobras());

		//#### F2 ####
//		TODO: Comentado pois, as Transferências não entrarão no workflow de aprovação. 
//	  	  Tratar futuramente, como será utilizado o parametro de ajuste.
//	
//	this.atualizarTiposMovimentoEstoque(parametrosDistribuidor,
//			 					 		this.getGruposMovimentoEstoqueAjusteEstoque(),
//			 					 		!parametrosDistribuidor.getAjusteEstoque());
		
		distribuidor.setParametrosAprovacaoDistribuidor(parametrosAprovacaoDistribuidor);
		
		if (parametrosDistribuidor.getPrazoFollowUp() != null)
			distribuidor.setPrazoFollowUp(parametrosDistribuidor.getPrazoFollowUp());
		else
			distribuidor.setPrazoFollowUp(null);
		
		if (parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantia() != null)
			distribuidor.setPrazoAvisoPrevioValidadeGarantia(parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantia());
		else
			distribuidor.setPrazoAvisoPrevioValidadeGarantia(null);
		
		distribuidor.setParcelamentoDividas(parametrosDistribuidor.getParcelamentoDividas());
		
		List<ParametrosDistribuidorFaltasSobras> listaParametrosDistribuidorFaltasSobras = new ArrayList<ParametrosDistribuidorFaltasSobras>();
		
		if (parametrosDistribuidor.getFaltasSobras()) {
			
			ParametrosDistribuidorFaltasSobras parametrosAprovacao = new ParametrosDistribuidorFaltasSobras();
			parametrosAprovacao.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.APROVACAO);
			parametrosAprovacao.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoFaltaDe()));
			parametrosAprovacao.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoFaltaEm()));
			parametrosAprovacao.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoSobraDe()));
			parametrosAprovacao.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoSobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosAprovacao);

			ParametrosDistribuidorFaltasSobras parametrosAvisoPrevioValidadeGarantia = new ParametrosDistribuidorFaltasSobras();
			parametrosAvisoPrevioValidadeGarantia.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.AVISO_PREVIO_VALIDADE_GARANTIA);
			listaParametrosDistribuidorFaltasSobras.add(parametrosAvisoPrevioValidadeGarantia);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoCE = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoCE.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_CE);
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoCE);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoNE = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoNE.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_NE);
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoNE);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoNECADANFE = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoNECADANFE.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_NECA_DANFE);
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoNECADANFE);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoPrazoFollowUp = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoPrazoFollowUp.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.PRAZO_FOLLOW_UP);

			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoPrazoFollowUp);

			for (ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras : listaParametrosDistribuidorFaltasSobras) {
				parametrosDistribuidorFaltasSobras.setDistribuidor(distribuidor);
				parametrosDistribuidorFaltasSobrasRepository.alterarOuCriar(parametrosDistribuidorFaltasSobras);
			}
			
			distribuidor.setParametrosDistribuidorFaltasSobras(listaParametrosDistribuidorFaltasSobras);
			
		} else {
			distribuidor.setParametrosDistribuidorFaltasSobras(null);
		}
		
		distribuidor.setNfInformacoesAdicionais(parametrosDistribuidor.getNfInformacoesAdicionais());
		
		if(parametrosDistribuidor.getRegimeTributario() != null) {
			
			RegimeTributario regimeTributario = regimeTributarioRepository.buscarPorId(parametrosDistribuidor.getRegimeTributario().getId());
						
			for(TributoAliquotaDTO taDTO : parametrosDistribuidor.getTributosAliquotas()) {
				
				TributoAliquota ta = tributoAliquotaRepository.buscarPorId(taDTO.getId());
				ta.setValor(taDTO.getValor());
				tributoAliquotaRepository.alterar(ta);
				
			}
			
			distribuidor.setRegimeTributario(regimeTributario);
		}
		
		if(parametrosDistribuidor.getCnae() == null){
			throw new ValidacaoException(TipoMensagem.WARNING ,"O campo 'CNAE' não pode ser vazio!");
		} else {
			distribuidor.setCnae(parametrosDistribuidor.getCnae());
		}
		
		if(parametrosDistribuidor.isPossuiRegimeEspecialDispensaInterna()) {

			distribuidor.setPossuiRegimeEspecialDispensaInterna(true);
			
			if(parametrosDistribuidor.getNumeroDispositivoLegal() == null){
				throw new ValidacaoException(TipoMensagem.WARNING ,"O campo número do dispositivo legal não pode ser vazio!");
			} else {
				distribuidor.setNumeroDispositivoLegal(parametrosDistribuidor.getNumeroDispositivoLegal());
			}
			
			if(parametrosDistribuidor.getDataLimiteVigenciaRegimeEspecial() == null){
				throw new ValidacaoException(TipoMensagem.WARNING ,"O campo data de término da vigência não pode ser vazio!");
			} else {				
				distribuidor.setDataLimiteVigenciaRegimeEspecial(parametrosDistribuidor.getDataLimiteVigenciaRegimeEspecial());
			}
			
			Map<String, Long> tiposNotasFiscaisMap = new HashMap<String, Long>();
			for(TiposNotasFiscaisParametrosVO tnfp : parametrosDistribuidor.getTiposNotasFiscais()) {
				tiposNotasFiscaisMap.put(tnfp.getNome(), Long.parseLong(tnfp.getValor()));
			}
			
			for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
				
				NotaFiscalTipoEmissao notaFiscalTipoEmissao = new NotaFiscalTipoEmissao();
				notaFiscalTipoEmissao.setId(tiposNotasFiscaisMap.get(dtnf.getNomeCampoTela()));
				dtnf.setTipoEmissao(notaFiscalTipoEmissao);
	
			}
			
		} else {
			
			distribuidor.setPossuiRegimeEspecialDispensaInterna(false);
			
			distribuidor.setNumeroDispositivoLegal(null);
			
			distribuidor.setDataLimiteVigenciaRegimeEspecial(null);
			
			for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {

				dtnf.setTipoEmissao(null);
	
			}
		}

		// Aba Distribuição - Grid Distribuição
		distribuidor.setGridDistribuicao(gravarGridDistribuicao(distribuidor, parametrosDistribuidor));
		
		// Aba Distribuição - Grid Classificação Cota
		distribuidor.setListClassificacaoCota(this.gravarClassificacaoCota(distribuidor, parametrosDistribuidor.getListClassificacaoCota()));
		
		// Aba Distribuição - Grid Percentual de Excedente
		distribuidor.setListPercentualExcedente(this.gravarPercentualExcedente(distribuidor, parametrosDistribuidor.getListPercentualExcedente()));
		
		distribuidorService.alterar(distribuidor);
		
		distribuidor.setEnderecoDistribuidor(this.gravarEnderecoDistribuidor(distribuidor, parametrosDistribuidor.getEndereco()));
		
		//Recarrega os codigos do Distribuidor para acessar o CouchDB
		this.initCouchDbClient();
		
		this.salvarLogo(imgLogotipo, imgContentType);
		
		this.gravarTelefoneDistribuidor(distribuidor, parametrosDistribuidor.getNumeroTelefone(), parametrosDistribuidor.getNumeroDDD());
	}
	
	private void tratarDadosConferenciaCega(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, 
				   							ParametrosDistribuidorVO parametrosDistribuidor) {

		parametrosRecolhimentoDistribuidor.setConferenciaCegaEncalhe(parametrosDistribuidor.isConferenciaCegaEncalhe());
		parametrosRecolhimentoDistribuidor.setConferenciaCegaRecebimento(parametrosDistribuidor.isConferenciaCegaRecebimento());
		
		if (!parametrosDistribuidor.isConferenciaCegaEncalhe()) {
			this.grupoPermissaoService.removerGrupoPermissaoPermissao(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_CONF_CEGA);
		}
		
		if (!parametrosDistribuidor.isConferenciaCegaRecebimento()) {
			this.grupoPermissaoService.removerGrupoPermissaoPermissao(Permissao.ROLE_ESTOQUE_RECEBIMENTO_FISICO_CONF_CEGA);
		}

	}
	
	private DistribuidorGridDistribuicao gravarGridDistribuicao(
			Distribuidor distribuidor,
			ParametrosDistribuidorVO parametrosDistribuidor) {
		
		DistribuidorGridDistribuicao gridDistribuicao = distribuidor.getGridDistribuicao();
		if (gridDistribuicao == null) {
			gridDistribuicao = new DistribuidorGridDistribuicao();
			gridDistribuicao.setDistribuidor(distribuidor);
		}
		gridDistribuicao.setGeracaoAutomaticaEstudo(parametrosDistribuidor.isGeracaoAutomaticaEstudo());
		gridDistribuicao.setVendaMediaMais(parametrosDistribuidor.getVendaMediaMais());
		gridDistribuicao.setPracaVeraneio(parametrosDistribuidor.isPracaVeraneio());
		gridDistribuicao.setComplementarAutomatico(parametrosDistribuidor.isComplementarAutomatico());
		gridDistribuicao.setPercentualMaximoFixacao(parametrosDistribuidor.getPercentualMaximoFixacao());

		gridDistribuicaoRepository.merge(gridDistribuicao);
		
		return gridDistribuicao;
	}

	private List<DistribuidorPercentualExcedente> gravarPercentualExcedente(
			Distribuidor distribuidor,
            List<DistribuidorPercentualExcedenteVO> listPercentualExcedenteVO) {

        List<DistribuidorPercentualExcedente> listPercentualExcedente = distribuidor.getListPercentualExcedente();
		if (listPercentualExcedente == null) {
			listPercentualExcedente = new ArrayList<>();
		}
		
		for (DistribuidorPercentualExcedenteVO percentualExcedenteVO : listPercentualExcedenteVO) {
			if (percentualExcedenteVO.getVenda() == null || percentualExcedenteVO.getPdv() == null) {
				continue;
			}
			DistribuidorPercentualExcedente percentualExcedente = percentualExcedenteVO.getId()==null?null:percentualExcedenteRepository.buscarPorId(percentualExcedenteVO.getId());
			if (percentualExcedente == null) {
				percentualExcedente = new DistribuidorPercentualExcedente();
				percentualExcedente.setDistribuidor(distribuidor);
			}
			percentualExcedente.setEficiencia(percentualExcedenteVO.getEficiencia());
			percentualExcedente.setVenda(percentualExcedenteVO.getVenda());
			percentualExcedente.setPdv(percentualExcedenteVO.getPdv());
			
//			percentualExcedenteRepository.merge(percentualExcedente);
			
			listPercentualExcedente.add(percentualExcedente);
		}
		
		return listPercentualExcedente;
	}

	private List<DistribuidorClassificacaoCota> gravarClassificacaoCota(
            Distribuidor distribuidor,
            List<DistribuidorClassificacaoCotaVO> listClassificacaoCotaVO) {

        List<DistribuidorClassificacaoCota> listClassificacaoCota = distribuidor.getListClassificacaoCota();
		if (listClassificacaoCota == null) {
			listClassificacaoCota = new ArrayList<>();
		}
		
		for (DistribuidorClassificacaoCotaVO classificacaoCotaVO : listClassificacaoCotaVO) {
			if (classificacaoCotaVO.getValorDe() == null || classificacaoCotaVO.getValorAte() == null) {
				continue;
			}
			DistribuidorClassificacaoCota classificacaoCota = classificacaoCotaVO.getId()==null?null:classificacaoCotaRepository.buscarPorId(classificacaoCotaVO.getId());
			if (classificacaoCota == null) {
				classificacaoCota = new DistribuidorClassificacaoCota();
				classificacaoCota.setDistribuidor(distribuidor);
			}
			classificacaoCota.setCodigoClassificacaoCota(classificacaoCotaVO.getCodigoClassificacaoCota());
			classificacaoCota.setValorDe(classificacaoCotaVO.getValorDe());
			classificacaoCota.setValorAte(classificacaoCotaVO.getValorAte());
			
			listClassificacaoCota.add(classificacaoCota);
		}
		
		return listClassificacaoCota;
	}
	
	private void validarUtilizacaoControleAprovacao(ParametrosDistribuidorVO parametrosDistribuidor) {
		
		List<String> mensagens = new ArrayList<>();
		
		if (!parametrosDistribuidor.getParaDebitosCreditos()) {
			if (this.movimentoRepository
					.existeMovimentoFinanceiroPendente(getGruposMovimentoFinanceiroDebitosCreditos())) {
	
				mensagens.add(
					"Não é possível não utilizar controle de [Aprovação] para [Débitos e Créditos]. Existem movimentos pendentes!");
			}
		}

		if (!parametrosDistribuidor.getNegociacao()) {
			if (this.movimentoRepository
					.existeMovimentoFinanceiroPendente(getGruposMovimentoFinanceiroNegociacao())) {

				mensagens.add(
					"Não é possível não utilizar controle de [Aprovação] para [Negociação]. Existem movimentos pendentes!");
			}
		}
		
//		TODO: Comentado pois, as Transferências não entrarão no workflow de aprovação. 
//			  Tratar futuramente, como será utilizado o parametro de ajuste.
//		if (!parametrosDistribuidor.getAjusteEstoque()) {
//			if (this.movimentoRepository
//					.existeMovimentoEstoquePendente(getGruposMovimentoEstoqueAjusteEstoque())) {
//
//				mensagens.add(
//					"Não é possível não utilizar controle de [Aprovação] para [Ajuste de Estoque]. Existem movimentos pendentes!");
//			}
//		}

		if (!parametrosDistribuidor.getPostergacaoCobranca()) {
			if (this.movimentoRepository
					.existeMovimentoFinanceiroPendente(getGruposMovimentoFinanceiroPostergacaoCobranca())) {

				mensagens.add(
					"Não é possível não utilizar controle de [Aprovação] para [Postergação de Cobranca]. Existem movimentos pendentes!");
			}
		}

		if (!parametrosDistribuidor.getDevolucaoFornecedor()) {
			if (this.movimentoRepository
					.existeMovimentoEstoquePendente(getGruposMovimentoEstoqueDevolucaoFornecedor())) {

				mensagens.add(
					"Não é possível não utilizar controle de [Aprovação] para [Devolucao Fornecedor]. Existem movimentos pendentes!");
			}
		}

		if (!parametrosDistribuidor.getFaltasSobras()) {
			if (this.movimentoRepository
					.existeMovimentoEstoquePendente(getGruposMovimentoEstoqueFaltasSobras())) {

				mensagens.add(
					"Não é possível não utilizar controle de [Aprovação] para [Faltas e Sobras]. Existem movimentos pendentes!");
			}
		}
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
		}
	}
	
	private List<GrupoMovimentoFinaceiro> getGruposMovimentoFinanceiroDebitosCreditos() {
		
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = 
			Arrays.asList(GrupoMovimentoFinaceiro.DEBITO, GrupoMovimentoFinaceiro.CREDITO);
		
		return gruposMovimentoFinanceiro;
	}
	
	private List<GrupoMovimentoFinaceiro> getGruposMovimentoFinanceiroNegociacao() {
		
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = 
			Arrays.asList(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
		
		return gruposMovimentoFinanceiro;
	}

	@SuppressWarnings("unused")
	private List<GrupoMovimentoEstoque> getGruposMovimentoEstoqueAjusteEstoque() {

        return Arrays.asList(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO,
                      GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO,
                      GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS,
                      GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS,
                      GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR,
                      GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR,
                      GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO,
                      GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO,
                      GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR,
                      GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
	}
	
	private List<GrupoMovimentoFinaceiro> getGruposMovimentoFinanceiroPostergacaoCobranca() {
		
		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = 
			Arrays.asList(GrupoMovimentoFinaceiro.POSTERGADO_CREDITO,
						  GrupoMovimentoFinaceiro.POSTERGADO_DEBITO);
		
		return gruposMovimentoFinanceiro;
	}
	
	private List<GrupoMovimentoEstoque> getGruposMovimentoEstoqueDevolucaoFornecedor() {
		
		List<GrupoMovimentoEstoque> gruposMovimentoEstoque = Arrays.asList(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
		
		return gruposMovimentoEstoque;
	}
	
	private List<GrupoMovimentoEstoque> getGruposMovimentoEstoqueFaltasSobras() {
		
		List<GrupoMovimentoEstoque> gruposMovimentoEstoque = 
			Arrays.asList(GrupoMovimentoEstoque.FALTA_DE, GrupoMovimentoEstoque.FALTA_EM,
						  GrupoMovimentoEstoque.SOBRA_DE, GrupoMovimentoEstoque.SOBRA_EM,
						  GrupoMovimentoEstoque.FALTA_DE_COTA, GrupoMovimentoEstoque.FALTA_EM_COTA,
						  GrupoMovimentoEstoque.SOBRA_DE_COTA, GrupoMovimentoEstoque.SOBRA_EM_COTA);
		
		return gruposMovimentoEstoque;
	}

	private void atualizarTiposMovimentoEstoque(ParametrosDistribuidorVO parametrosDistribuidor,
										 		List<GrupoMovimentoEstoque> gruposMovimentoEstoque,
										 		boolean aprovacaoAutomatica) {
		
		List<TipoMovimentoEstoque> tiposMovimentoEstoque = 
			this.tipoMovimentoEstoqueRepository.buscarTiposMovimentoEstoque(gruposMovimentoEstoque);
		
		for (TipoMovimentoEstoque tipoMovimentoEstoque : tiposMovimentoEstoque) {
			
			tipoMovimentoEstoque.setAprovacaoAutomatica(aprovacaoAutomatica);
			
			this.tipoMovimentoEstoqueRepository.merge(tipoMovimentoEstoque);
		}
	}
	
	private void atualizarTiposMovimentoFinanceiro(ParametrosDistribuidorVO parametrosDistribuidor,
										 		   List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro,
										 		   boolean aprovacaoAutomatica) {

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiro =
			this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(gruposMovimentoFinanceiro);

		for (TipoMovimentoFinanceiro tipoMovimentoFinanceiro : tiposMovimentoFinanceiro) {

			tipoMovimentoFinanceiro.setAprovacaoAutomatica(aprovacaoAutomatica);

			this.tipoMovimentoFinanceiroRepository.merge(tipoMovimentoFinanceiro);
		}
	}
	
	private void salvarLogo(InputStream imgLogotipo, String imgContentType) {
		
		if (imgLogotipo != null && imgContentType != null) {
		
			removerLogo();
			
			couchDbClient.saveAttachment(
				imgLogotipo, ATTACHMENT_LOGOTIPO, imgContentType,
				TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name(), null);
		}
	}
	
	private void removerLogo() {
		
		JsonObject jsonObject = null;
		
		try {
		
			jsonObject = couchDbClient.find(JsonObject.class, TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name());
		
		} catch (NoDocumentException e) {
			
			return;
		}
		
		this.couchDbClient.remove(jsonObject);
	}
	
	@Override
	public InputStream getLogotipoDistribuidor() {
		
		InputStream inputStream = null;
		
		try {
			
			inputStream = couchDbClient.find(TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name()+ "/" + ATTACHMENT_LOGOTIPO);
		
		} catch (NoDocumentException e) {
			
			URL url = Thread.currentThread().getContextClassLoader().getResource("/no_image.jpeg");
			
			File noImage = new File(url.getPath());
			
			try {
			
				inputStream = new FileInputStream(noImage);
			
			} catch (FileNotFoundException e1) {
				
				return null;
			}
			
		}
		
		return inputStream;
	}

    /**
     * Processa as informações de parametrização do distribuidor
     * com relação as informações de tipos de garantias aceitas
     * @param parametrosDistribuidor Value Object com as informações 
     * @param distribuidor distribuidor em processamento
     */
	private void processaUtilizacaoGarantiasPDV(
            ParametrosDistribuidorVO parametrosDistribuidor,
            Distribuidor distribuidor) {
        distribuidor.setUtilizaGarantiaPdv(parametrosDistribuidor.isUtilizaGarantiaPdv());
		if (distribuidor.isUtilizaGarantiaPdv()) {
		    
            if (parametrosDistribuidor.isUtilizaChequeCaucao()) {
                distribuidor.addTipoGarantiaAceita(TipoGarantia.CHEQUE_CAUCAO, parametrosDistribuidor
                                .getValidadeChequeCaucao());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.CHEQUE_CAUCAO);
            }
            
            if (parametrosDistribuidor.isUtilizaFiador()) {
                distribuidor.addTipoGarantiaAceita(TipoGarantia.FIADOR, parametrosDistribuidor
                                .getValidadeFiador());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.FIADOR);
            }
            
            if (parametrosDistribuidor.isUtilizaImovel()) {
                distribuidor.addTipoGarantiaAceita(
                        TipoGarantia.IMOVEL, parametrosDistribuidor
                                .getValidadeImovel());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.IMOVEL);
            }
            
            if (parametrosDistribuidor.isUtilizaCaucaoLiquida()) {
                distribuidor.addTipoGarantiaAceita(TipoGarantia.CAUCAO_LIQUIDA, parametrosDistribuidor
                                .getValidadeCaucaoLiquida());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.CAUCAO_LIQUIDA);
            }
            
            if (parametrosDistribuidor.isUtilizaNotaPromissoria()) {
                distribuidor.addTipoGarantiaAceita(TipoGarantia.NOTA_PROMISSORIA, parametrosDistribuidor
                                .getValidadeNotaPromissoria());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.NOTA_PROMISSORIA);
            }
            
            if (parametrosDistribuidor.isUtilizaOutros()) {
                distribuidor.addTipoGarantiaAceita(TipoGarantia.OUTROS, parametrosDistribuidor
                                .getValidadeOutros());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.OUTROS);
            }
		} else {
		    distribuidor.removerTodosTiposGarantiasAceitas();
		}
		
		distribuidor.addTipoGarantiaAceita(TipoGarantia.ANTECEDENCIA_VALIDADE, parametrosDistribuidor
                 .getValidadeAntecedenciaValidade());
    }

	/**
	 * Retorna "checked" caso seja true ou "" caso contrário 
	 * @param b boolean
	 * @return String
	 */
	private String verificaCheckString(boolean b) {
		if (b)
			return CHECKED;
		return "";
	}
	
	/**
	 * Retorna true caso a String seja "checked" ou false caso contrário
	 * @param s String
	 * @return boolean
	 */
	private boolean verificaCheckBoolean(String s) {
		if (s.equals(CHECKED))
			return true;
		return false;
	}

	/**
	 * Obtém o ControleConferenciaEncalhe referente a dataOperacao atual.
	 * 
	 * @param dataOperacao
	 * 
	 * @return ControleConferenciaEncalhe
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ControleConferenciaEncalhe obterControleConferenciaEncalhe(Date dataOperacao) {
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = controleConferenciaEncalheRepository.obterControleConferenciaEncalhe(dataOperacao);
		
		if(controleConferenciaEncalhe == null) {
			
			controleConferenciaEncalhe = new ControleConferenciaEncalhe();
			
			controleConferenciaEncalhe.setData(dataOperacao);
			
			controleConferenciaEncalhe.setStatus(StatusOperacao.EM_ANDAMENTO);
			
			controleConferenciaEncalheRepository.adicionar(controleConferenciaEncalhe);
		}
		
		return controleConferenciaEncalhe;
		
	}

	@Override
	@Transactional
	public List<DistribuidorTipoNotaFiscal> obterTiposNotaFiscalDistribuidor() {
		List<DistribuidorTipoNotaFiscal> tiposNotaFiscal = new ArrayList<>();
		tiposNotaFiscal.addAll(distribuidorService.obter().getTiposNotaFiscalDistribuidor());
		if(!tiposNotaFiscal.isEmpty()) {
			for(DistribuidorTipoNotaFiscal dtnf : tiposNotaFiscal) {
				for(NotaFiscalTipoEmissao nfte : dtnf.getTipoEmissaoDisponiveis()) {
					nfte.getDescricao();
				}
			}
		}
		return tiposNotaFiscal;
	}

	@Override
	@Transactional
	public List<NotaFiscalTipoEmissao> obterTiposEmissoesNotaFiscalDistribuidor() {
		
		List<NotaFiscalTipoEmissao> tiposEmissaoNotaFiscal = new ArrayList<>();
		tiposEmissaoNotaFiscal.addAll(distribuidorService.obter().getTiposEmissoesNotaFiscalDistribuidor());
		
		return tiposEmissaoNotaFiscal;
	}

	@Override
	@Transactional
	public List<String> obterEstadosAtendidosPeloDistribuidor() {
		
		return enderecoRepository.obterUFsCotas();
	}

}
