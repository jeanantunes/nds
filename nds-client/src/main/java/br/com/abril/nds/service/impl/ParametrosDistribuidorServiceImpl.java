package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PoliticaChamadao;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNECADANFE;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.repository.ParametroContratoCotaRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorFaltasSobrasRepository;
import br.com.abril.nds.repository.TipoGarantiaAceitaRepository;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;

/**
 * Implementação da interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
@Service
public class ParametrosDistribuidorServiceImpl implements ParametrosDistribuidorService {

	private final static String CHECKED = "checked";

	private final static String UNDEFINED = "undefined";
	
	@Autowired
	DistribuidorService distribuidorService;

	@Autowired
	ParametroContratoCotaRepository parametroContratoCotaRepository;

	@Autowired
	ParametrosDistribuidorEmissaoDocumentoRepository parametrosDistribuidorEmissaoDocumentoRepository;

	@Autowired
	ParametrosDistribuidorFaltasSobrasRepository parametrosDistribuidorFaltasSobrasRepository;

	@Autowired
	TipoGarantiaAceitaRepository tipoGarantiaAceitaRepository;

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getParametrosDistribuidor()
	 */
	@Transactional(readOnly = true)
	@Override
	public ParametrosDistribuidorVO getParametrosDistribuidor() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		ParametrosDistribuidorVO parametrosDistribuidor = new ParametrosDistribuidorVO();
		
		// Parciais / Matriz de Lançamento
		parametrosDistribuidor.setRelancamentoParciaisEmDias(distribuidor.getFatorRelancamentoParcial());
		
		// Recolhimento
		parametrosDistribuidor.setAceitaEncalheJuramentada(verificaCheckString(distribuidor.isAceitaJuramentado()));
		parametrosDistribuidor.setDiaRecolhimentoPrimeiro(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoPrimeiro()));
		parametrosDistribuidor.setDiaRecolhimentoSegundo(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoSegundo()));
		parametrosDistribuidor.setDiaRecolhimentoTerceiro(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoTerceiro()));
		parametrosDistribuidor.setDiaRecolhimentoQuarto(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuarto()));
		parametrosDistribuidor.setDiaRecolhimentoQuinto(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuinto()));
		parametrosDistribuidor.setLimiteCEProximaSemana(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isPermiteRecolherDiasPosteriores()));
		parametrosDistribuidor.setConferenciaCegaEncalhe(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isConferenciaCegaEncalhe()));
		parametrosDistribuidor.setConferenciaCegaRecebimento(verificaCheckString(distribuidor.getParametrosRecolhimentoDistribuidor().isConferenciaCegaRecebimento()));
		parametrosDistribuidor.setTipoContabilizacaoCE(distribuidor.getTipoContabilizacaoCE());
		parametrosDistribuidor.setSupervisionaVendaNegativa(distribuidor.isSupervisionaVendaNegativa());		
		
		// Capacidade de Manuseio
		parametrosDistribuidor.setCapacidadeManuseioHomemHoraLancamento(CurrencyUtil.formatarValorTruncado(distribuidor.getCapacidadeDistribuicao()));
		parametrosDistribuidor.setCapacidadeManuseioHomemHoraRecolhimento(CurrencyUtil.formatarValorTruncado(distribuidor.getCapacidadeRecolhimento()));
		
		//Política chamadão
		PoliticaChamadao politicaChamadao = distribuidor.getPoliticaChamadao();
		if (politicaChamadao != null) {
		    parametrosDistribuidor.setChamadaoDiasSuspensao(politicaChamadao.getDiasSuspenso());
		    parametrosDistribuidor.setChamadaoValorConsignado(CurrencyUtil.formatarValor(politicaChamadao.getValorConsignado()));
		}
		
		
		// Reutilização de Código de Cota
		parametrosDistribuidor.setReutilizacaoCodigoCotaInativa(CurrencyUtil.formatarValorTruncado(distribuidor.getQntDiasReutilizacaoCodigoCota()));
		
		// Fiscal
		parametrosDistribuidor.setObrigacaoFiscao(verificaCheckString(distribuidor.isObrigacaoFiscao()));
		parametrosDistribuidor.setRegimeEspecial(verificaCheckString(distribuidor.isRegimeEspecial()));
		if (distribuidor.getTipoAtividade() != null)
			parametrosDistribuidor.setDistribuidor(distribuidor.getTipoAtividade().name());

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
		
		// Impressão NE
		if (distribuidor.getTipoImpressaoNE() != null)
			parametrosDistribuidor.setImpressaoNE(distribuidor.getTipoImpressaoNE().name());

		// Impressão NECA / Danfe
		if (distribuidor.getTipoImpressaoNECADANFE() != null)
			parametrosDistribuidor.setImpressaoNECADANFE(distribuidor.getTipoImpressaoNECADANFE().name());

		// Impressão CE
		if (distribuidor.getTipoImpressaoCE() != null)
			parametrosDistribuidor.setImpressaoCE(distribuidor.getTipoImpressaoCE().name());
		
		// Condições de Contratação:
		boolean utilizaContratoComCotas = (distribuidor.getParametroContratoCota() != null);
		parametrosDistribuidor.setUtilizaContratoComCotas((utilizaContratoComCotas ? CHECKED : ""));
		if (utilizaContratoComCotas) {
			parametrosDistribuidor.setPrazoContrato(CurrencyUtil.formatarValorTruncado(distribuidor.getParametroContratoCota().getDuracaoContratoCota()));
			parametrosDistribuidor.setInformacoesComplementaresContrato(distribuidor.getParametroContratoCota().getComplementoContrato());
		}
		
		// Procuração
		parametrosDistribuidor.setUtilizaProcuracaoEntregadores(verificaCheckString(distribuidor.isUtilizaProcuracaoEntregadores()));
		parametrosDistribuidor.setInformacoesComplementaresProcuracao(distribuidor.getInformacoesComplementaresProcuracao());
		
		// Garantia
		parametrosDistribuidor.setUtilizaGarantiaPdv(distribuidor.isUtilizaGarantiaPdv());
		
		for (TipoGarantiaAceita tipoGarantiaAceita : distribuidor.getTiposGarantiasAceita()) {
			if (tipoGarantiaAceita.isUtilizar()) {
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
					parametrosDistribuidor.setUtilizaAntecedenciaValidade(true);
					parametrosDistribuidor.setValidadeAntecedenciaValidade(tipoGarantiaAceita.getValor());
				}  else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.OUTROS) {
                    parametrosDistribuidor.setUtilizaOutros(true);
                    parametrosDistribuidor.setValidadeOutros(tipoGarantiaAceita.getValor());
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
		parametrosDistribuidor.setParcelamentoDividas(verificaCheckString(distribuidor.isParcelamentoDividas()));
		parametrosDistribuidor.setNegociacaoAteParcelas(CurrencyUtil.formatarValorTruncado(distribuidor.getNegociacaoAteParcelas()));
		parametrosDistribuidor.setPermitePagamentoDividasDivergentes(verificaCheckString(distribuidor.isPermitePagamentoDividasDivergentes()));
		
		// Aprovação
		parametrosDistribuidor.setUtilizaControleAprovacao(verificaCheckString(distribuidor.isUtilizaControleAprovacao()));
		
		if (distribuidor.getParametrosAprovacaoDistribuidor() != null) {
			parametrosDistribuidor.setParaDebitosCreditos(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isDebitoCredito()));
			parametrosDistribuidor.setNegociacao(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isNegociacao()));
			parametrosDistribuidor.setAjusteEstoque(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isAjusteEstoque()));
			parametrosDistribuidor.setPostergacaoCobranca(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isPostergacaoCobranca()));
			parametrosDistribuidor.setDevolucaoFornecedor(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isDevolucaoFornecedor()));
			parametrosDistribuidor.setRecibo(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isRecibo()));
			parametrosDistribuidor.setFaltasSobras(verificaCheckString(distribuidor.getParametrosAprovacaoDistribuidor().isFaltasSobras()));
		}
		
		parametrosDistribuidor.setPrazoFollowUp(CurrencyUtil.formatarValorTruncado(distribuidor.getPrazoFollowUp()));
		parametrosDistribuidor.setPrazoAvisoPrevioValidadeGarantia(CurrencyUtil.formatarValorTruncado(distribuidor.getPrazoAvisoPrevioValidadeGarantia()));
		
		for (ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras : distribuidor.getParametrosDistribuidorFaltasSobras()) {
			if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.APROVACAO) {
				parametrosDistribuidor.setAprovacaoFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setAprovacaoFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setAprovacaoSobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setAprovacaoFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));
			} else if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.AVISO_PREVIO_VALIDADE_GARANTIA) {
				parametrosDistribuidor.setPrazoAvisoPrevioValidadeGarantiaFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setPrazoAvisoPrevioValidadeGarantiaFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setPrazoAvisoPrevioValidadeGarantiaSobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setPrazoAvisoPrevioValidadeGarantiaFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));
			} else if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_CE) {
				parametrosDistribuidor.setImpressaoCEFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setImpressaoCEFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setImpressaoCESobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setImpressaoCEFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));
			} else if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_NE) {
				parametrosDistribuidor.setImpressaoNEFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setImpressaoNEFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setImpressaoNESobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setImpressaoNEFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));
			} else if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_NECA_DANFE) {
				parametrosDistribuidor.setImpressaoNECADANFEFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setImpressaoNECADANFEFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setImpressaoNECADANFESobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setImpressaoNECADANFEFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));
			} else if (parametrosDistribuidorFaltasSobras.getTipoParametrosDistribuidorFaltasSobras() == TipoParametrosDistribuidorFaltasSobras.PRAZO_FOLLOW_UP) {
				parametrosDistribuidor.setPrazoFollowUpFaltaDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaDe()));
				parametrosDistribuidor.setPrazoFollowUpFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isFaltaEm()));
				parametrosDistribuidor.setPrazoFollowUpSobraDe(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraDe()));
				parametrosDistribuidor.setPrazoFollowUpFaltaEm(verificaCheckString(parametrosDistribuidorFaltasSobras.isSobraEm()));				
			}
		}
		
		return parametrosDistribuidor;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getDistribuidor(br.com.abril.nds.client.vo.ParametrosDistribuidorVO)
	 */
	@Transactional(readOnly = false)
	@Override
	public Distribuidor getDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor) {
		Distribuidor distribuidor = distribuidorService.obter();

		// Parciais / Matriz de Lançamento
		distribuidor.setFatorRelancamentoParcial(parametrosDistribuidor.getRelancamentoParciaisEmDias());

		// Recolhimento
		distribuidor.setAceitaJuramentado(verificaCheckBoolean(parametrosDistribuidor.getAceitaEncalheJuramentada()));
		
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
		if (distribuidor.getParametrosRecolhimentoDistribuidor() != null)
			parametrosRecolhimentoDistribuidor = distribuidor.getParametrosRecolhimentoDistribuidor();
		else
			parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
			
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(verificaCheckBoolean(parametrosDistribuidor.getDiaRecolhimentoPrimeiro()));
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(verificaCheckBoolean(parametrosDistribuidor.getDiaRecolhimentoSegundo()));
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(verificaCheckBoolean(parametrosDistribuidor.getDiaRecolhimentoTerceiro()));
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(verificaCheckBoolean(parametrosDistribuidor.getDiaRecolhimentoQuarto()));
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(verificaCheckBoolean(parametrosDistribuidor.getDiaRecolhimentoQuinto()));
		parametrosRecolhimentoDistribuidor.setPermiteRecolherDiasPosteriores(verificaCheckBoolean(parametrosDistribuidor.getLimiteCEProximaSemana()));
		parametrosRecolhimentoDistribuidor.setConferenciaCegaEncalhe(verificaCheckBoolean(parametrosDistribuidor.getConferenciaCegaEncalhe()));
		parametrosRecolhimentoDistribuidor.setConferenciaCegaRecebimento(verificaCheckBoolean(parametrosDistribuidor.getConferenciaCegaRecebimento()));
		distribuidor.setParametrosRecolhimentoDistribuidor(parametrosRecolhimentoDistribuidor);
		
		// Capacidade de Manuseio
		if (parametrosDistribuidor.getCapacidadeManuseioHomemHoraLancamento() != null && !parametrosDistribuidor.getCapacidadeManuseioHomemHoraLancamento().isEmpty()) {
			distribuidor.setCapacidadeDistribuicao(CurrencyUtil.converterValor(parametrosDistribuidor.getCapacidadeManuseioHomemHoraLancamento()));
		} else {
			distribuidor.setCapacidadeDistribuicao(null);
		}
		if (parametrosDistribuidor.getCapacidadeManuseioHomemHoraRecolhimento() != null && !parametrosDistribuidor.getCapacidadeManuseioHomemHoraRecolhimento().isEmpty()) {
			distribuidor.setCapacidadeRecolhimento(CurrencyUtil.converterValor(parametrosDistribuidor.getCapacidadeManuseioHomemHoraRecolhimento()));
		} else {
			distribuidor.setCapacidadeRecolhimento(null);
		}
		
		// Reutilização de Código de Cota
		if (parametrosDistribuidor.getReutilizacaoCodigoCotaInativa() != null && !parametrosDistribuidor.getReutilizacaoCodigoCotaInativa().isEmpty()) {
			distribuidor.setQntDiasReutilizacaoCodigoCota(CurrencyUtil.converterValor(parametrosDistribuidor.getReutilizacaoCodigoCotaInativa()).setScale(0,BigDecimal.ROUND_UP).longValueExact());
		} else {
			distribuidor.setQntDiasReutilizacaoCodigoCota(null);
		}

		// Fiscal
		distribuidor.setObrigacaoFiscao(verificaCheckBoolean(parametrosDistribuidor.getObrigacaoFiscao()));

		distribuidor.setRegimeEspecial(verificaCheckBoolean(parametrosDistribuidor.getRegimeEspecial()));

		if (parametrosDistribuidor.getDistribuidor() != null && !parametrosDistribuidor.getDistribuidor().isEmpty()) {
			TipoAtividade tipoDistribuidor = Enum.valueOf(TipoAtividade.class, parametrosDistribuidor.getDistribuidor());
			distribuidor.setTipoAtividade(tipoDistribuidor);
		} else {
			distribuidor.setTipoAtividade(null);
		}
		
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
		
		// Impressão NE
		if (parametrosDistribuidor.getImpressaoNE() != null && !parametrosDistribuidor.getImpressaoNE().isEmpty() &&  !parametrosDistribuidor.getImpressaoNE().equalsIgnoreCase(UNDEFINED)) {
			TipoImpressaoNE tipoImpressaoNE = Enum.valueOf(TipoImpressaoNE.class, parametrosDistribuidor.getImpressaoNE());
			distribuidor.setTipoImpressaoNE(tipoImpressaoNE);
		} else {
			distribuidor.setTipoImpressaoNE(null);
		}

		// Impressão NECA / Danfe
		if (parametrosDistribuidor.getImpressaoNECADANFE() != null && !parametrosDistribuidor.getImpressaoNECADANFE().isEmpty() && !parametrosDistribuidor.getImpressaoNECADANFE().equalsIgnoreCase(UNDEFINED)) {
			TipoImpressaoNECADANFE tipoImpressaoNECADANFE = Enum.valueOf(TipoImpressaoNECADANFE.class, parametrosDistribuidor.getImpressaoNECADANFE());
			distribuidor.setTipoImpressaoNECADANFE(tipoImpressaoNECADANFE);
		} else {
			distribuidor.setTipoImpressaoNECADANFE(null);
		}

		// Impressão CE
		if (parametrosDistribuidor.getImpressaoCE() != null && !parametrosDistribuidor.getImpressaoCE().isEmpty() && !parametrosDistribuidor.getImpressaoCE().equalsIgnoreCase(UNDEFINED)) {
			TipoImpressaoCE tipoImpressaoCE = Enum.valueOf(TipoImpressaoCE.class, parametrosDistribuidor.getImpressaoCE());
			distribuidor.setTipoImpressaoCE(tipoImpressaoCE);
		} else {
			distribuidor.setTipoImpressaoCE(null);
		}

		// Condições de Contratação:
		boolean utilizaContratoComCotas = verificaCheckBoolean(parametrosDistribuidor.getUtilizaContratoComCotas());
		if (utilizaContratoComCotas) {
			ParametroContratoCota parametroContratoCota = null;
			if (distribuidor.getParametroContratoCota() != null) {
				parametroContratoCota = distribuidor.getParametroContratoCota();
			} else {
				parametroContratoCota = new ParametroContratoCota();
			}
			if (parametrosDistribuidor.getPrazoContrato() != null && !parametrosDistribuidor.getPrazoContrato().isEmpty()) {
				parametroContratoCota.setDuracaoContratoCota(CurrencyUtil.converterValor(parametrosDistribuidor.getPrazoContrato()).intValue());
			} else {
				parametroContratoCota.setDuracaoContratoCota(2); // DEFAULT, entretanto, nunca deverá cair aqui
			}
			parametroContratoCota.setComplementoContrato(parametrosDistribuidor.getInformacoesComplementaresContrato());
			
			parametroContratoCotaRepository.alterar(parametroContratoCota);

			distribuidor.setParametroContratoCota(parametroContratoCota);

		} else {
			distribuidor.setParametroContratoCota(null);
		}
		
		// Procuração
		distribuidor.setUtilizaProcuracaoEntregadores(verificaCheckBoolean(parametrosDistribuidor.getUtilizaProcuracaoEntregadores()));
		distribuidor.setInformacoesComplementaresProcuracao(parametrosDistribuidor.getInformacoesComplementaresProcuracao());

		List<TipoGarantiaAceita> listaTipoGarantiaAceitas = new ArrayList<TipoGarantiaAceita>();

		distribuidor.setUtilizaGarantiaPdv(parametrosDistribuidor.getUtilizaGarantiaPdv());
		
		//Garantias Aceitas
		//TODO: Refatorar
		TipoGarantiaAceita tipoGarantiaAceitaChequeCaucao = new TipoGarantiaAceita();
		tipoGarantiaAceitaChequeCaucao.setTipoGarantia(TipoGarantia.CHEQUE_CAUCAO);
		tipoGarantiaAceitaChequeCaucao.setValor(parametrosDistribuidor.getValidadeChequeCaucao());
		tipoGarantiaAceitaChequeCaucao.setUtilizar(parametrosDistribuidor.isUtilizaChequeCaucao());
		listaTipoGarantiaAceitas.add(tipoGarantiaAceitaChequeCaucao);

		TipoGarantiaAceita tipoGarantiaAceitaFiador = new TipoGarantiaAceita();
		tipoGarantiaAceitaFiador.setTipoGarantia(TipoGarantia.FIADOR);
		tipoGarantiaAceitaFiador.setValor(parametrosDistribuidor.getValidadeFiador());
		tipoGarantiaAceitaFiador.setUtilizar(parametrosDistribuidor.isUtilizaFiador());
		listaTipoGarantiaAceitas.add(tipoGarantiaAceitaFiador);

		TipoGarantiaAceita tipoGarantiaAceitaImovel = new TipoGarantiaAceita();
		tipoGarantiaAceitaImovel.setTipoGarantia(TipoGarantia.IMOVEL);
		tipoGarantiaAceitaImovel.setValor(parametrosDistribuidor.getValidadeImovel());
		tipoGarantiaAceitaImovel.setUtilizar(parametrosDistribuidor.isUtilizaImovel());
		listaTipoGarantiaAceitas.add(tipoGarantiaAceitaImovel);

		TipoGarantiaAceita tipoGarantiaAceitaCaucaoLiquida = new TipoGarantiaAceita();
		tipoGarantiaAceitaCaucaoLiquida.setTipoGarantia(TipoGarantia.CAUCAO_LIQUIDA);
		tipoGarantiaAceitaCaucaoLiquida.setValor(parametrosDistribuidor.getValidadeCaucaoLiquida());
		tipoGarantiaAceitaCaucaoLiquida.setUtilizar(parametrosDistribuidor.isUtilizaCaucaoLiquida());
		listaTipoGarantiaAceitas.add(tipoGarantiaAceitaCaucaoLiquida);

		TipoGarantiaAceita tipoGarantiaAceitaNotaPromissoria = new TipoGarantiaAceita();
		tipoGarantiaAceitaNotaPromissoria.setTipoGarantia(TipoGarantia.NOTA_PROMISSORIA);
		tipoGarantiaAceitaNotaPromissoria.setValor(parametrosDistribuidor.getValidadeNotaPromissoria());
		tipoGarantiaAceitaNotaPromissoria.setUtilizar(parametrosDistribuidor.isUtilizaNotaPromissoria());
		listaTipoGarantiaAceitas.add(tipoGarantiaAceitaNotaPromissoria);

		TipoGarantiaAceita tipoGarantiaAceitaAntecedenciaValidade = new TipoGarantiaAceita();
		tipoGarantiaAceitaAntecedenciaValidade.setTipoGarantia(TipoGarantia.ANTECEDENCIA_VALIDADE);
		tipoGarantiaAceitaAntecedenciaValidade.setValor(parametrosDistribuidor.getValidadeAntecedenciaValidade());
		tipoGarantiaAceitaAntecedenciaValidade.setUtilizar(parametrosDistribuidor.isUtilizaAntecedenciaValidade());
		listaTipoGarantiaAceitas.add(tipoGarantiaAceitaAntecedenciaValidade);
		
		TipoGarantiaAceita tipoGarantiaOutros = new TipoGarantiaAceita();
		tipoGarantiaOutros.setTipoGarantia(TipoGarantia.OUTROS);
		tipoGarantiaOutros.setValor(parametrosDistribuidor.getValidadeOutros());
		tipoGarantiaOutros.setUtilizar(parametrosDistribuidor.isUtilizaOutros());
        listaTipoGarantiaAceitas.add(tipoGarantiaOutros);

		for (TipoGarantiaAceita tipoGarantiaAceita : listaTipoGarantiaAceitas) {
			tipoGarantiaAceita.setDistribuidor(distribuidor);
			tipoGarantiaAceitaRepository.alterarOuCriar(tipoGarantiaAceita);
		}
		
		distribuidor.setTiposGarantiasAceita(listaTipoGarantiaAceitas);
		
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
		
		distribuidor.setParcelamentoDividas(verificaCheckBoolean(parametrosDistribuidor.getParcelamentoDividas()));
		if (parametrosDistribuidor.getNegociacaoAteParcelas() !=null && !parametrosDistribuidor.getNegociacaoAteParcelas().isEmpty()) {
			distribuidor.setNegociacaoAteParcelas(CurrencyUtil.converterValor(parametrosDistribuidor.getNegociacaoAteParcelas()).intValueExact());
		} else {
			distribuidor.setNegociacaoAteParcelas(null);
		}
		distribuidor.setPermitePagamentoDividasDivergentes(verificaCheckBoolean(parametrosDistribuidor.getPermitePagamentoDividasDivergentes()));
		
		// Aprovação
		distribuidor.setUtilizaControleAprovacao(verificaCheckBoolean(parametrosDistribuidor.getUtilizaControleAprovacao()));
		
		ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor = new ParametrosAprovacaoDistribuidor();
		parametrosAprovacaoDistribuidor.setDebitoCredito(verificaCheckBoolean(parametrosDistribuidor.getParaDebitosCreditos()));

		parametrosAprovacaoDistribuidor.setNegociacao(verificaCheckBoolean(parametrosDistribuidor.getNegociacao()));
		parametrosAprovacaoDistribuidor.setAjusteEstoque(verificaCheckBoolean(parametrosDistribuidor.getAjusteEstoque()));
		parametrosAprovacaoDistribuidor.setPostergacaoCobranca(verificaCheckBoolean(parametrosDistribuidor.getPostergacaoCobranca()));
		parametrosAprovacaoDistribuidor.setDevolucaoFornecedor(verificaCheckBoolean(parametrosDistribuidor.getDevolucaoFornecedor()));
		parametrosAprovacaoDistribuidor.setRecibo(verificaCheckBoolean(parametrosDistribuidor.getRecibo()));
		parametrosAprovacaoDistribuidor.setFaltasSobras(verificaCheckBoolean(parametrosDistribuidor.getFaltasSobras()));
		distribuidor.setParametrosAprovacaoDistribuidor(parametrosAprovacaoDistribuidor);
		
		if (parametrosDistribuidor.getPrazoFollowUp() != null && !parametrosDistribuidor.getPrazoFollowUp().isEmpty())
			distribuidor.setPrazoFollowUp(CurrencyUtil.converterValor(parametrosDistribuidor.getPrazoFollowUp()).intValueExact());
		else
			distribuidor.setPrazoFollowUp(null);
		
		if (parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantia() != null && !parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantia().isEmpty())
			distribuidor.setPrazoAvisoPrevioValidadeGarantia(CurrencyUtil.converterValor(parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantia()).intValueExact());
		else
			distribuidor.setPrazoAvisoPrevioValidadeGarantia(null);
		
		List<ParametrosDistribuidorFaltasSobras> listaParametrosDistribuidorFaltasSobras = new ArrayList<ParametrosDistribuidorFaltasSobras>();
		
		if (verificaCheckBoolean(parametrosDistribuidor.getFaltasSobras())) {
			
			ParametrosDistribuidorFaltasSobras parametrosAprovacao = new ParametrosDistribuidorFaltasSobras();
			parametrosAprovacao.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.APROVACAO);
			parametrosAprovacao.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoFaltaDe()));
			parametrosAprovacao.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoFaltaEm()));
			parametrosAprovacao.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoSobraDe()));
			parametrosAprovacao.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getAprovacaoSobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosAprovacao);

			ParametrosDistribuidorFaltasSobras parametrosAvisoPrevioValidadeGarantia = new ParametrosDistribuidorFaltasSobras();
			parametrosAvisoPrevioValidadeGarantia.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.AVISO_PREVIO_VALIDADE_GARANTIA);
			parametrosAvisoPrevioValidadeGarantia.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantiaFaltaDe()));
			parametrosAvisoPrevioValidadeGarantia.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantiaFaltaEm()));
			parametrosAvisoPrevioValidadeGarantia.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantiaSobraDe()));
			parametrosAvisoPrevioValidadeGarantia.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getPrazoAvisoPrevioValidadeGarantiaSobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosAvisoPrevioValidadeGarantia);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoCE = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoCE.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_CE);
			parametrosImpressaoCE.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getImpressaoCEFaltaDe()));
			parametrosImpressaoCE.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getImpressaoCEFaltaEm()));
			parametrosImpressaoCE.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getImpressaoCESobraDe()));
			parametrosImpressaoCE.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getImpressaoCESobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoCE);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoNE = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoNE.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_NE);
			parametrosImpressaoNE.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNEFaltaDe()));
			parametrosImpressaoNE.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNEFaltaEm()));
			parametrosImpressaoNE.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNESobraDe()));
			parametrosImpressaoNE.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNESobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoNE);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoNECADANFE = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoNECADANFE.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.IMPRESSAO_NECA_DANFE);
			parametrosImpressaoNECADANFE.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNECADANFEFaltaDe()));
			parametrosImpressaoNECADANFE.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNECADANFEFaltaEm()));
			parametrosImpressaoNECADANFE.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNECADANFESobraDe()));
			parametrosImpressaoNECADANFE.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getImpressaoNECADANFESobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoNECADANFE);

			ParametrosDistribuidorFaltasSobras parametrosImpressaoPrazoFollowUp = new ParametrosDistribuidorFaltasSobras();
			parametrosImpressaoPrazoFollowUp.setTipoParametrosDistribuidorFaltasSobras(TipoParametrosDistribuidorFaltasSobras.PRAZO_FOLLOW_UP);
			parametrosImpressaoPrazoFollowUp.setFaltaDe(verificaCheckBoolean(parametrosDistribuidor.getPrazoFollowUpFaltaDe()));
			parametrosImpressaoPrazoFollowUp.setFaltaEm(verificaCheckBoolean(parametrosDistribuidor.getPrazoFollowUpFaltaEm()));
			parametrosImpressaoPrazoFollowUp.setSobraDe(verificaCheckBoolean(parametrosDistribuidor.getPrazoFollowUpSobraDe()));
			parametrosImpressaoPrazoFollowUp.setSobraEm(verificaCheckBoolean(parametrosDistribuidor.getPrazoFollowUpSobraEm()));
			listaParametrosDistribuidorFaltasSobras.add(parametrosImpressaoPrazoFollowUp);

			for (ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras : listaParametrosDistribuidorFaltasSobras) {
				parametrosDistribuidorFaltasSobras.setDistribuidor(distribuidor);
				parametrosDistribuidorFaltasSobrasRepository.alterarOuCriar(parametrosDistribuidorFaltasSobras);
			}
			
			distribuidor.setParametrosDistribuidorFaltasSobras(listaParametrosDistribuidorFaltasSobras);
			
		} else {
			distribuidor.setParametrosDistribuidorFaltasSobras(null);
		}
		
		return distribuidor;
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
