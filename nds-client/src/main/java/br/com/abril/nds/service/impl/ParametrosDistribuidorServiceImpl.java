package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametroEntregaBanca;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaChamadao;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoInterfaceLED;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.repository.EnderecoDistribuidorRepository;
import br.com.abril.nds.repository.ParametroContratoCotaRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorFaltasSobrasRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.TipoGarantiaAceitaRepository;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.vo.EnderecoVO;

import com.google.gson.JsonObject;

/**
 * Implementação da interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
@Service
public class ParametrosDistribuidorServiceImpl implements ParametrosDistribuidorService {

	private final static String CHECKED = "checked";

	private final static String UNDEFINED = "undefined";
	
	private static final String ATTACHMENT_LOGOTIPO = "imagem_logotipo";
	
	private static final String DB_NAME = "db_parametro_distribuidor";
	
	@Autowired
	private DistribuidorService distribuidorService;

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
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	@PostConstruct
	public void initCouchDbClient() {
		this.couchDbClient = new CouchDbClient(DB_NAME, true,
				couchDbProperties.getProtocol(), 
				couchDbProperties.getHost(),
				couchDbProperties.getPort(), 
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword());
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
		
		parametrosDistribuidor.setEndereco(
			this.popularEnderecoVO(distribuidor.getEnderecoDistribuidor()));
		
		parametrosDistribuidor.setRegimeTributario(distribuidor.getTipoAtividade());
		parametrosDistribuidor.setObrigacaoFiscal(distribuidor.getObrigacaoFiscal());
		parametrosDistribuidor.setRegimeEspecial(distribuidor.isRegimeEspecial());
		
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
		if (distribuidor.isUtilizaGarantiaPdv()) {
		    for (TipoGarantiaAceita tipoGarantiaAceita : distribuidor
		            .getTiposGarantiasAceita()) {
		        
		        if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.CHEQUE_CAUCAO) {
		            parametrosDistribuidor.setUtilizaChequeCaucao(true);
		            parametrosDistribuidor
		            .setValidadeChequeCaucao(tipoGarantiaAceita.getValor());
		        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.FIADOR) {
		            parametrosDistribuidor.setUtilizaFiador(true);
		            parametrosDistribuidor.setValidadeFiador(tipoGarantiaAceita
		                    .getValor());
		        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.IMOVEL) {
		            parametrosDistribuidor.setUtilizaImovel(true);
		            parametrosDistribuidor.setValidadeImovel(tipoGarantiaAceita
		                    .getValor());
		        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.CAUCAO_LIQUIDA) {
		            parametrosDistribuidor.setUtilizaCaucaoLiquida(true);
		            parametrosDistribuidor
		            .setValidadeCaucaoLiquida(tipoGarantiaAceita.getValor());
		        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.NOTA_PROMISSORIA) {
		            parametrosDistribuidor.setUtilizaNotaPromissoria(true);
		            parametrosDistribuidor
		            .setValidadeNotaPromissoria(tipoGarantiaAceita
		                    .getValor());
		        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.ANTECEDENCIA_VALIDADE) {
		            parametrosDistribuidor.setUtilizaAntecedenciaValidade(true);
		            parametrosDistribuidor
		            .setValidadeAntecedenciaValidade(tipoGarantiaAceita
		                    .getValor());
		        } else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.OUTROS) {
		            parametrosDistribuidor.setUtilizaOutros(true);
		            parametrosDistribuidor.setValidadeOutros(tipoGarantiaAceita
		                    .getValor());
		        }
		    }
		}
		

		// Negociação de Dividas
		if (distribuidor.getPoliticaSuspensao() != null) {
			if (distribuidor.getPoliticaSuspensao().getNumeroAcumuloDividaWrapped() != null) 
				parametrosDistribuidor.setSugereSuspensaoQuandoAtingirBoletos(CurrencyUtil.formatarValorTruncado(distribuidor.getPoliticaSuspensao().getNumeroAcumuloDivida()));
			if (distribuidor.getPoliticaSuspensao().getValor() != null)
				parametrosDistribuidor.setSugereSuspensaoQuandoAtingirReais(CurrencyUtil.formatarValor(distribuidor.getPoliticaSuspensao().getValor()));
		}
		parametrosDistribuidor.setParcelamentoDividas(distribuidor.isParcelamentoDividas());
		
		if(distribuidor.getDescontoCotaNegociacao().compareTo(BigDecimal.ZERO) == 0) {
			parametrosDistribuidor.setUtilizaDesconto(false);
			parametrosDistribuidor.setPercentualDesconto(CurrencyUtil.formatarValor(0));
		} else {
			parametrosDistribuidor.setUtilizaDesconto(true);
			parametrosDistribuidor.setPercentualDesconto(CurrencyUtil.formatarValor(distribuidor.getDescontoCotaNegociacao()));
		}
		
		parametrosDistribuidor.setNegociacaoAteParcelas(CurrencyUtil.formatarValorTruncado(distribuidor.getNegociacaoAteParcelas()));
		
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
		
		return parametrosDistribuidor;
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
		endereco.setNumero((enderecoVO.getNumero().trim().isEmpty()) ? null : enderecoVO.getNumero());
		endereco.setComplemento(enderecoVO.getComplemento());
		endereco.setBairro(enderecoVO.getBairro());
		endereco.setCidade(enderecoVO.getLocalidade());
		endereco.setUf(enderecoVO.getUf());
		
		endereco.setCodigoBairro(
			(enderecoVO.getCodigoBairro() == null) ? null : enderecoVO.getCodigoBairro());
		
		endereco.setCodigoCidadeIBGE(
			(enderecoVO.getCodigoCidadeIBGE() == null) ? null : enderecoVO.getCodigoCidadeIBGE().intValue());
		
		enderecoDistribuidor.setDistribuidor(distribuidor);
		enderecoDistribuidor.setTipoEndereco(enderecoVO.getTipoEndereco());
		enderecoDistribuidor.setEndereco(endereco);
		
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
		
		enderecoVO.setCodigoBairro(
			(endereco.getCodigoBairro() == null) ? null : endereco.getCodigoBairro());
		
		enderecoVO.setCodigoCidadeIBGE(
			(endereco.getCodigoCidadeIBGE() == null) ? null : endereco.getCodigoCidadeIBGE().longValue());
		
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
		
		distribuidor.setTipoAtividade(parametrosDistribuidor.getRegimeTributario());
		distribuidor.setObrigacaoFiscal(parametrosDistribuidor.getObrigacaoFiscal());
		distribuidor.setRegimeEspecial(parametrosDistribuidor.getRegimeEspecial());
		
		// Parciais / Matriz de Lançamento
		distribuidor.setFatorRelancamentoParcial(parametrosDistribuidor.getRelancamentoParciaisEmDias());

		// Recolhimento
		distribuidor.setAceitaJuramentado(parametrosDistribuidor.isAceitaEncalheJuramentada());
		
		distribuidor.setTipoContabilizacaoCE(parametrosDistribuidor.getTipoContabilizacaoCE());
		distribuidor.setSupervisionaVendaNegativa(parametrosDistribuidor.isSupervisionaVendaNegativa());
		PoliticaChamadao politicaChamadao = distribuidor.getPoliticaChamadao();
		if(politicaChamadao == null) {
		    politicaChamadao = new PoliticaChamadao();
		    distribuidor.setPoliticaChamadao(politicaChamadao);
		}
		Integer chamadaoDiasSuspensao = parametrosDistribuidor.getChamadaoDiasSuspensao();
		BigDecimal chamadaoConsignado = CurrencyUtil.converterValor(parametrosDistribuidor.getChamadaoValorConsignado());
		politicaChamadao.setDiasSuspenso(chamadaoDiasSuspensao);
		politicaChamadao.setValorConsignado(chamadaoConsignado);
		
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
		parametrosRecolhimentoDistribuidor.setConferenciaCegaEncalhe(parametrosDistribuidor.isConferenciaCegaEncalhe());
		parametrosRecolhimentoDistribuidor.setConferenciaCegaRecebimento(parametrosDistribuidor.isConferenciaCegaRecebimento());
		distribuidor.setParametrosRecolhimentoDistribuidor(parametrosRecolhimentoDistribuidor);
		
		distribuidor.setCapacidadeDistribuicao(new BigDecimal(parametrosDistribuidor.getCapacidadeManuseioHomemHoraLancamento()));
		distribuidor.setCapacidadeRecolhimento(new BigDecimal(parametrosDistribuidor.getCapacidadeManuseioHomemHoraRecolhimento()));
		distribuidor.setQntDiasReutilizacaoCodigoCota(parametrosDistribuidor.getReutilizacaoCodigoCotaInativa());
		
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
			politicaSuspensao.setValor(CurrencyUtil.converterValor(parametrosDistribuidor.getSugereSuspensaoQuandoAtingirReais()));
		} else {
			politicaSuspensao.setValor(null);
		}

		distribuidor.setPoliticaSuspensao(politicaSuspensao);
				
		if(parametrosDistribuidor.getUtilizaDesconto() != null && parametrosDistribuidor.getUtilizaDesconto())
			distribuidor.setDescontoCotaNegociacao(CurrencyUtil.converterValor(parametrosDistribuidor.getPercentualDesconto()));
		else
			distribuidor.setDescontoCotaNegociacao(BigDecimal.ZERO);
		
		if (parametrosDistribuidor.getNegociacaoAteParcelas() !=null && !parametrosDistribuidor.getNegociacaoAteParcelas().isEmpty()) {
			distribuidor.setNegociacaoAteParcelas(CurrencyUtil.converterValor(parametrosDistribuidor.getNegociacaoAteParcelas()).intValueExact());
		} else {
			distribuidor.setNegociacaoAteParcelas(null);
		}
		
		// Aprovação
		distribuidor.setUtilizaControleAprovacao(parametrosDistribuidor.getUtilizaControleAprovacao());
		
		ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor = new ParametrosAprovacaoDistribuidor();
		parametrosAprovacaoDistribuidor.setDebitoCredito(parametrosDistribuidor.getParaDebitosCreditos());

		parametrosAprovacaoDistribuidor.setNegociacao(parametrosDistribuidor.getNegociacao());
		parametrosAprovacaoDistribuidor.setAjusteEstoque(parametrosDistribuidor.getAjusteEstoque());
		parametrosAprovacaoDistribuidor.setPostergacaoCobranca(parametrosDistribuidor.getPostergacaoCobranca());
		parametrosAprovacaoDistribuidor.setDevolucaoFornecedor(parametrosDistribuidor.getDevolucaoFornecedor());
		parametrosAprovacaoDistribuidor.setFaltasSobras(parametrosDistribuidor.getFaltasSobras());
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
		
		distribuidorService.alterar(distribuidor);
		
		distribuidor.setEnderecoDistribuidor(
			this.gravarEnderecoDistribuidor(distribuidor, parametrosDistribuidor.getEndereco()));
		
		this.salvarLogo(imgLogotipo, imgContentType);
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
		
			jsonObject =
				couchDbClient.find(JsonObject.class, TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name());
		
		} catch (NoDocumentException e) {
			
			return;
		}
		
		this.couchDbClient.remove(jsonObject);
	}
	
	@Override
	public InputStream getLogotipoDistribuidor() {
		
		InputStream inputStream = null;
		
		try {
			
			inputStream = couchDbClient.find(
					TipoParametroSistema.LOGOTIPO_DISTRIBUIDOR.name()
					+ "/" + ATTACHMENT_LOGOTIPO);
		} catch (NoDocumentException e) {
			
			return null;
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
            
            if (parametrosDistribuidor.isUtilizaAntecedenciaValidade()) {
                distribuidor.addTipoGarantiaAceita(TipoGarantia.ANTECEDENCIA_VALIDADE, parametrosDistribuidor
                                .getValidadeAntecedenciaValidade());
            } else {
                distribuidor.removerTipoGarantiaAceita(TipoGarantia.ANTECEDENCIA_VALIDADE);
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
	
}
