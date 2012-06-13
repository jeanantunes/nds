package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoGarantiaAceita;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.ParametrosDistribuidorService;

/**
 * Implementação da interface de serviços do parametrosDistribuidorVO
 * @author InfoA2
 */
@Service
public class ParametrosDistribuidorServiceImpl implements ParametrosDistribuidorService {

	private final static String CHECKED = "checked";
	
	@Autowired
	DistribuidorService distribuidorService;
	
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
		parametrosDistribuidor.setAceitaEncalheJuramentada(verificaCheck(distribuidor.isAceitaJuramentado()));
		parametrosDistribuidor.setDiaRecolhimentoPrimeiro(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoPrimeiro()));
		parametrosDistribuidor.setDiaRecolhimentoSegundo(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoSegundo()));
		parametrosDistribuidor.setDiaRecolhimentoTerceiro(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoTerceiro()));
		parametrosDistribuidor.setDiaRecolhimentoQuarto(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuarto()));
		parametrosDistribuidor.setDiaRecolhimentoQuinto(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuinto()));
		parametrosDistribuidor.setLimiteCEProximaSemana(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isPermiteRecolherDiasPosteriores()));
		parametrosDistribuidor.setConferenciaCegaEncalhe(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isConferenciaCegaEncalhe()));
		parametrosDistribuidor.setConferenciaCegaRecebimento(verificaCheck(distribuidor.getParametrosRecolhimentoDistribuidor().isConferenciaCegaRecebimento()));
		
		// Capacidade de Manuseio
		parametrosDistribuidor.setCapacidadeManuseioHomemHoraLancamento(distribuidor.getCapacidadeDistribuicao().toString());
		parametrosDistribuidor.setCapacidadeManuseioHomemHoraRecolhimento(distribuidor.getCapacidadeRecolhimento().toString());
		
		// Reutilização de Código de Cota
		parametrosDistribuidor.setReutilizacaoCodigoCotaInativa(distribuidor.getQntDiasReutilizacaoCodigoCota().toString());
		
		// Fiscal
		parametrosDistribuidor.setObrigacaoFiscao(verificaCheck(distribuidor.isObrigacaoFiscao()));
		parametrosDistribuidor.setRegimeEspecial(verificaCheck(distribuidor.isRegimeEspecial()));
		parametrosDistribuidor.setDistribuidor(distribuidor.getTipoDistribuidor().toString()); // CONFERIR!

		// Emissão de Documentos
		for (ParametrosDistribuidorEmissaoDocumento emissaoDocumentos : distribuidor.getParametrosDistribuidorEmissaoDocumentos()) {
			if (emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento() == TipoParametrosDistribuidorEmissaoDocumento.BOLETO) {
				parametrosDistribuidor.setBoletoEmail(verificaCheck(emissaoDocumentos.isUtilizaEmail()));
				parametrosDistribuidor.setBoletoImpressao(verificaCheck(emissaoDocumentos.isUtilizaImpressao()));
			} else if (emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento() == TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP) {
				parametrosDistribuidor.setBoletoSlipEmail(verificaCheck(emissaoDocumentos.isUtilizaEmail()));
				parametrosDistribuidor.setBoletoSlipImpressao(verificaCheck(emissaoDocumentos.isUtilizaImpressao()));
			} else if (emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento() == TipoParametrosDistribuidorEmissaoDocumento.CHAMADA_ENCALHE) {
				parametrosDistribuidor.setChamadaEncalheEmail(verificaCheck(emissaoDocumentos.isUtilizaEmail()));
				parametrosDistribuidor.setChamadaEncalheImpressao(verificaCheck(emissaoDocumentos.isUtilizaImpressao()));
			} else if (emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento() == TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO) {
				parametrosDistribuidor.setNotaEnvioEmail(verificaCheck(emissaoDocumentos.isUtilizaEmail()));
				parametrosDistribuidor.setNotaEnvioImpressao(verificaCheck(emissaoDocumentos.isUtilizaImpressao()));
			} else if (emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento() == TipoParametrosDistribuidorEmissaoDocumento.RECIBO) {
				parametrosDistribuidor.setReciboEmail(verificaCheck(emissaoDocumentos.isUtilizaEmail()));
				parametrosDistribuidor.setReciboImpressao(verificaCheck(emissaoDocumentos.isUtilizaImpressao()));
			} else if (emissaoDocumentos.getTipoParametrosDistribuidorEmissaoDocumento() == TipoParametrosDistribuidorEmissaoDocumento.SLIP) {
				parametrosDistribuidor.setSlipEmail(verificaCheck(emissaoDocumentos.isUtilizaEmail()));
				parametrosDistribuidor.setSlipImpressao(verificaCheck(emissaoDocumentos.isUtilizaImpressao()));
			}
		}
		
		// Impressão NE
		parametrosDistribuidor.setImpressaoNE(distribuidor.getTipoImpressaoNE().toString());

		// Impressão NECA / Danfe
		parametrosDistribuidor.setImpressaoNECADANFE(distribuidor.getTipoImpressaoNECADANFE().toString());

		// Impressão CE
		parametrosDistribuidor.setImpressaoCE(distribuidor.getTipoImpressaoCE().toString());
		
		// Condições de Contratação:
		boolean utilizaContratoComCotas = (distribuidor.getParametroContratoCota() != null);
		parametrosDistribuidor.setUtilizaContratoComCotas((utilizaContratoComCotas ? CHECKED : ""));
		if (utilizaContratoComCotas) {
			parametrosDistribuidor.setPrazoContrato(Integer.toString(distribuidor.getParametroContratoCota().getDuracaoContratoCota()));
			parametrosDistribuidor.setInformacoesComplementaresContrato(distribuidor.getParametroContratoCota().getComplementoContrato());
		}
		
		// Procuração
		parametrosDistribuidor.setUtilizaProcuracaoEntregadores(verificaCheck(distribuidor.isUtilizaProcuracaoEntregadores()));
		parametrosDistribuidor.setInformacoesComplementaresProcuracao(distribuidor.getInformacoesComplementaresProcuracao());
		
		// Garantia
		for (TipoGarantiaAceita tipoGarantiaAceita : distribuidor.getTiposGarantiasAceita()) {
			if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.CHEQUE_CAUCAO) {
				parametrosDistribuidor.setChequeCalcao(CHECKED);
				parametrosDistribuidor.setChequeCalcaoValor(tipoGarantiaAceita.getValor());
			} else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.FIADOR) {
				parametrosDistribuidor.setFiador(CHECKED);
				parametrosDistribuidor.setFiadorValor(tipoGarantiaAceita.getValor());
			} else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.IMOVEL) {
				parametrosDistribuidor.setImovel(CHECKED);
				parametrosDistribuidor.setImovelValor(tipoGarantiaAceita.getValor());
			} else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.CAUCAO_LIQUIDA) {
				parametrosDistribuidor.setCaucaoLiquida(CHECKED);
				parametrosDistribuidor.setCaucaoLiquidaValor(tipoGarantiaAceita.getValor());
			} else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.NOTA_PROMISSORIA) {
				parametrosDistribuidor.setNotaPromissoria(CHECKED);
				parametrosDistribuidor.setNotaPromissoriaValor(tipoGarantiaAceita.getValor());
			} else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.ANTECEDENCIA_VALIDADE) {
				parametrosDistribuidor.setAntecedenciaValidade(CHECKED);
				parametrosDistribuidor.setAntecedenciaValidadeValor(tipoGarantiaAceita.getValor());
			} else if (tipoGarantiaAceita.getTipoGarantia() == TipoGarantia.INDICADOR_REAJUSTE_CAUCAO_LIQUIDA) {
				parametrosDistribuidor.setIndicadorReajusteCaucaoLiquida(CHECKED);
				parametrosDistribuidor.setIndicadorReajusteCaucaoLiquida(tipoGarantiaAceita.getValor());
			}

			tipoGarantiaAceita.getValor();
		}
		
		return parametrosDistribuidor;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getDistribuidor(br.com.abril.nds.client.vo.ParametrosDistribuidorVO)
	 */
	@Transactional(readOnly = true)
	@Override
	public Distribuidor getDistribuidor(ParametrosDistribuidorVO parametrosDistribuidor) {
		Distribuidor distribuidor = distribuidorService.obter();
		// TODO: setar os atributos aqui
		//distribuidor.set
		return distribuidor;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametrosDistribuidorService#getListaParametrosSistema(br.com.abril.nds.client.vo.ParametrosDistribuidorVO)
	 */
	@Transactional(readOnly = true)
	@Override
	public List<ParametroSistema> getListaParametrosSistema(ParametrosDistribuidorVO parametrosDistribuidor) {
		List<ParametroSistema> parametrosSistema = new ArrayList<ParametroSistema>();
		// TODO: adicionar a lista de parâmetros do sistema
		//parametrosSistema.add(
		return parametrosSistema;
	}

	/**
	 * Retorna "checked" caso seja true ou "" caso contrário 
	 * @param b boolean
	 * @return String
	 */
	private String verificaCheck(boolean b) {
		if (b)
			return CHECKED;
		return "";
	}
	
}
