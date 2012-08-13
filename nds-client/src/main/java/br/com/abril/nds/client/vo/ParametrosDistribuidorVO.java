package br.com.abril.nds.client.vo;

import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;


/**
 * VO para controle dos parametros do distribuidor
 * @author InfoA2
 */
public class ParametrosDistribuidorVO {

	// Aba Operação
	
	// Frame Parciais / Matriz de Lançamento
	private int relancamentoParciaisEmDias;
	
	// Frame Recolhimento
	
	private TipoContabilizacaoCE tipoContabilizacaoCE;
	
	private String aceitaEncalheJuramentada;
	
	private String diaRecolhimentoPrimeiro;
	
	private String diaRecolhimentoSegundo;

	private String diaRecolhimentoTerceiro;

	private String diaRecolhimentoQuarto;

	private String diaRecolhimentoQuinto;

	private String limiteCEProximaSemana;
	
	private boolean supervisionaVendaNegativa;

	private String conferenciaCegaRecebimento;

	private String conferenciaCegaEncalhe;

	// Frame Capacidade de Manuseio
	private String capacidadeManuseioHomemHoraLancamento;
	
	private String capacidadeManuseioHomemHoraRecolhimento;

	// Frame Reutilização de Código de Cota
	private String reutilizacaoCodigoCotaInativa;

	// Aba Fiscal
	
	// Frame Fiscal
	private String obrigacaoFiscao;
	
	private String regimeEspecial;

	// PRESTADOR_SERVICO ou MERCANTIL
	private String distribuidor;
	
	// Frame Emissão de Documentos
	private boolean slipImpressao;

	private boolean slipEmail;

	private boolean boletoImpressao;

	private boolean boletoEmail;

	private boolean boletoSlipImpressao;

	private boolean boletoSlipEmail;

	private boolean reciboImpressao;

	private boolean reciboEmail;

	private boolean notaEnvioImpressao;

	private boolean notaEnvioEmail;

	private boolean chamadaEncalheImpressao;

	private boolean chamadaEncalheEmail;
	
	// Frame Impressão NE
	// MODELO_1 ou MODELO_2
	private String impressaoNE;

	private String impressaoNEFaltaDe;
	
	private String impressaoNEFaltaEm;
	
	private String impressaoNESobraDe;
	
	private String impressaoNESobraEm;

	// Frame Impressão NECA / DANFE
	// MODELO_1, MODELO_2 ou DANFE
	private String impressaoNECADANFE;

	private String impressaoNECADANFEFaltaDe;
	
	private String impressaoNECADANFEFaltaEm;
	
	private String impressaoNECADANFESobraDe;
	
	private String impressaoNECADANFESobraEm;

	// Frame Impressão CE
	// MODELO_1 ou MODELO_2
	private String impressaoCE;
	
	private String impressaoCEFaltaDe;
	
	private String impressaoCEFaltaEm;
	
	private String impressaoCESobraDe;
	
	private String impressaoCESobraEm;
	
	// Aba Contratos e Garantias
	// Frame Condições de Contratação
	private String utilizaContratoComCotas;
	
	private String prazoContrato;

	private String informacoesComplementaresContrato;

	// Frame Procuração
	private String utilizaProcuracaoEntregadores;

	private String informacoesComplementaresProcuracao;

	// Frame Garantia
	private String utilizaGarantiaPdv;
	
	private String chequeCalcao;
	
	private String chequeCalcaoValor;
	
	private String fiador;
	
	private String fiadorValor;
	
	private String imovel;
	
	private String imovelValor;
	
	private String caucaoLiquida;
	
	private String caucaoLiquidaValor;
	
	private String notaPromissoria;
	
	private String notaPromissoriaValor;
	
	private String antecedenciaValidade;
	
	private String antecedenciaValidadeValor;
	
	private String indicadorReajusteCaucaoLiquida;
	
	private String indicadorReajusteCaucaoLiquidaValor;
	
	// Aba Negociação
	// Frame Negociação de Dívidas
	private String sugereSuspensaoQuandoAtingirBoletos;

	private String sugereSuspensaoQuandoAtingirReais;

	private String parcelamentoDividas;
	
	private String negociacaoAteParcelas;
	
	private String permitePagamentoDividasDivergentes;

	// Aba Aprovação
	// Frame Aprovação
	private String utilizaControleAprovacao;

	private String paraDebitosCreditos;

	private String negociacao;
	
	private String ajusteEstoque;
	
	private String postergacaoCobranca;
	
	private String devolucaoFornecedor;
	
	private String recibo;
	
	private String faltasSobras;
	
	private String aprovacaoFaltaDe;
	
	private String aprovacaoSobraDe;
	
	private String aprovacaoFaltaEm;

	private String aprovacaoSobraEm;

	private String prazoFollowUp;
	
	private String prazoFollowUpFaltaDe;
	
	private String prazoFollowUpSobraDe;
	
	private String prazoFollowUpFaltaEm;

	private String prazoFollowUpSobraEm;
	
	private String prazoAvisoPrevioValidadeGarantia;

	private String prazoAvisoPrevioValidadeGarantiaFaltaDe;
	
	private String prazoAvisoPrevioValidadeGarantiaSobraDe;
	
	private String prazoAvisoPrevioValidadeGarantiaFaltaEm;

	private String prazoAvisoPrevioValidadeGarantiaSobraEm;
	
	private Integer chamadaoDiasSuspensao;
	
	private String chamadaoValorConsignado;

	public int getRelancamentoParciaisEmDias() {
		return relancamentoParciaisEmDias;
	}

	public void setRelancamentoParciaisEmDias(int relancamentoParciaisEmDias) {
		this.relancamentoParciaisEmDias = relancamentoParciaisEmDias;
	}

	/**
     * @return the tipoContabilizacaoCE
     */
    public TipoContabilizacaoCE getTipoContabilizacaoCE() {
        return tipoContabilizacaoCE;
    }

    /**
     * @param tipoContabilizacaoCE the tipoContabilizacaoCE to set
     */
    public void setTipoContabilizacaoCE(TipoContabilizacaoCE tipoContabilizacaoCE) {
        this.tipoContabilizacaoCE = tipoContabilizacaoCE;
    }

    public String getAceitaEncalheJuramentada() {
		return aceitaEncalheJuramentada;
	}

	public void setAceitaEncalheJuramentada(String aceitaEncalheJuramentada) {
		this.aceitaEncalheJuramentada = aceitaEncalheJuramentada;
	}

	public String getDiaRecolhimentoPrimeiro() {
		return diaRecolhimentoPrimeiro;
	}

	public void setDiaRecolhimentoPrimeiro(String diaRecolhimentoPrimeiro) {
		this.diaRecolhimentoPrimeiro = diaRecolhimentoPrimeiro;
	}

	public String getDiaRecolhimentoSegundo() {
		return diaRecolhimentoSegundo;
	}

	public void setDiaRecolhimentoSegundo(String diaRecolhimentoSegundo) {
		this.diaRecolhimentoSegundo = diaRecolhimentoSegundo;
	}

	public String getDiaRecolhimentoTerceiro() {
		return diaRecolhimentoTerceiro;
	}

	public void setDiaRecolhimentoTerceiro(String diaRecolhimentoTerceiro) {
		this.diaRecolhimentoTerceiro = diaRecolhimentoTerceiro;
	}

	public String getDiaRecolhimentoQuarto() {
		return diaRecolhimentoQuarto;
	}

	public void setDiaRecolhimentoQuarto(String diaRecolhimentoQuarto) {
		this.diaRecolhimentoQuarto = diaRecolhimentoQuarto;
	}

	public String getDiaRecolhimentoQuinto() {
		return diaRecolhimentoQuinto;
	}

	public void setDiaRecolhimentoQuinto(String diaRecolhimentoQuinto) {
		this.diaRecolhimentoQuinto = diaRecolhimentoQuinto;
	}

	public String getLimiteCEProximaSemana() {
		return limiteCEProximaSemana;
	}

	public void setLimiteCEProximaSemana(String limiteCEProximaSemana) {
		this.limiteCEProximaSemana = limiteCEProximaSemana;
	}

	/**
     * @return the supervisionaVendaNegativa
     */
    public boolean isSupervisionaVendaNegativa() {
        return supervisionaVendaNegativa;
    }

    /**
     * @param supervisionaVendaNegativa the supervisionaVendaNegativa to set
     */
    public void setSupervisionaVendaNegativa(boolean supervisionaVendaNegativa) {
        this.supervisionaVendaNegativa = supervisionaVendaNegativa;
    }

    public String getConferenciaCegaRecebimento() {
		return conferenciaCegaRecebimento;
	}

	public void setConferenciaCegaRecebimento(String conferenciaCegaRecebimento) {
		this.conferenciaCegaRecebimento = conferenciaCegaRecebimento;
	}

	public String getConferenciaCegaEncalhe() {
		return conferenciaCegaEncalhe;
	}

	public void setConferenciaCegaEncalhe(String conferenciaCegaEncalhe) {
		this.conferenciaCegaEncalhe = conferenciaCegaEncalhe;
	}

	public String getCapacidadeManuseioHomemHoraLancamento() {
		return capacidadeManuseioHomemHoraLancamento;
	}

	public void setCapacidadeManuseioHomemHoraLancamento(
			String capacidadeManuseioHomemHoraLancamento) {
		this.capacidadeManuseioHomemHoraLancamento = capacidadeManuseioHomemHoraLancamento;
	}

	public String getCapacidadeManuseioHomemHoraRecolhimento() {
		return capacidadeManuseioHomemHoraRecolhimento;
	}

	public void setCapacidadeManuseioHomemHoraRecolhimento(
			String capacidadeManuseioHomemHoraRecolhimento) {
		this.capacidadeManuseioHomemHoraRecolhimento = capacidadeManuseioHomemHoraRecolhimento;
	}

	public String getReutilizacaoCodigoCotaInativa() {
		return reutilizacaoCodigoCotaInativa;
	}

	public void setReutilizacaoCodigoCotaInativa(
			String reutilizacaoCodigoCotaInativa) {
		this.reutilizacaoCodigoCotaInativa = reutilizacaoCodigoCotaInativa;
	}

	public String getObrigacaoFiscao() {
		return obrigacaoFiscao;
	}

	public void setObrigacaoFiscao(String obrigacaoFiscao) {
		this.obrigacaoFiscao = obrigacaoFiscao;
	}

	public String getRegimeEspecial() {
		return regimeEspecial;
	}

	public void setRegimeEspecial(String regimeEspecial) {
		this.regimeEspecial = regimeEspecial;
	}

	public String getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(String distribuidor) {
		this.distribuidor = distribuidor;
	}

	public boolean getSlipImpressao() {
		return slipImpressao;
	}

	public void setSlipImpressao(boolean slipImpressao) {
		this.slipImpressao = slipImpressao;
	}

	public boolean getSlipEmail() {
		return slipEmail;
	}

	public void setSlipEmail(boolean slipEmail) {
		this.slipEmail = slipEmail;
	}

	public boolean getBoletoImpressao() {
		return boletoImpressao;
	}

	public void setBoletoImpressao(boolean boletoImpressao) {
		this.boletoImpressao = boletoImpressao;
	}

	public boolean getBoletoEmail() {
		return boletoEmail;
	}

	public void setBoletoEmail(boolean boletoEmail) {
		this.boletoEmail = boletoEmail;
	}

	public boolean getBoletoSlipImpressao() {
		return boletoSlipImpressao;
	}

	public void setBoletoSlipImpressao(boolean boletoSlipImpressao) {
		this.boletoSlipImpressao = boletoSlipImpressao;
	}

	public boolean getBoletoSlipEmail() {
		return boletoSlipEmail;
	}

	public void setBoletoSlipEmail(boolean boletoSlipEmail) {
		this.boletoSlipEmail = boletoSlipEmail;
	}

	public boolean getReciboImpressao() {
		return reciboImpressao;
	}

	public void setReciboImpressao(boolean reciboImpressao) {
		this.reciboImpressao = reciboImpressao;
	}

	public boolean getReciboEmail() {
		return reciboEmail;
	}

	public void setReciboEmail(boolean reciboEmail) {
		this.reciboEmail = reciboEmail;
	}

	public boolean getNotaEnvioImpressao() {
		return notaEnvioImpressao;
	}

	public void setNotaEnvioImpressao(boolean notaEnvioImpressao) {
		this.notaEnvioImpressao = notaEnvioImpressao;
	}

	public boolean getNotaEnvioEmail() {
		return notaEnvioEmail;
	}

	public void setNotaEnvioEmail(boolean notaEnvioEmail) {
		this.notaEnvioEmail = notaEnvioEmail;
	}

	public boolean getChamadaEncalheImpressao() {
		return chamadaEncalheImpressao;
	}

	public void setChamadaEncalheImpressao(boolean chamadaEncalheImpressao) {
		this.chamadaEncalheImpressao = chamadaEncalheImpressao;
	}

	public boolean getChamadaEncalheEmail() {
		return chamadaEncalheEmail;
	}

	public void setChamadaEncalheEmail(boolean chamadaEncalheEmail) {
		this.chamadaEncalheEmail = chamadaEncalheEmail;
	}

	public String getImpressaoNE() {
		return impressaoNE;
	}

	public void setImpressaoNE(String impressaoNE) {
		this.impressaoNE = impressaoNE;
	}

	public String getImpressaoNEFaltaDe() {
		return impressaoNEFaltaDe;
	}

	public void setImpressaoNEFaltaDe(String impressaoNEFaltaDe) {
		this.impressaoNEFaltaDe = impressaoNEFaltaDe;
	}

	public String getImpressaoNEFaltaEm() {
		return impressaoNEFaltaEm;
	}

	public void setImpressaoNEFaltaEm(String impressaoNEFaltaEm) {
		this.impressaoNEFaltaEm = impressaoNEFaltaEm;
	}

	public String getImpressaoNESobraDe() {
		return impressaoNESobraDe;
	}

	public void setImpressaoNESobraDe(String impressaoNESobraDe) {
		this.impressaoNESobraDe = impressaoNESobraDe;
	}

	public String getImpressaoNESobraEm() {
		return impressaoNESobraEm;
	}

	public void setImpressaoNESobraEm(String impressaoNESobraEm) {
		this.impressaoNESobraEm = impressaoNESobraEm;
	}

	public String getImpressaoNECADANFE() {
		return impressaoNECADANFE;
	}

	public void setImpressaoNECADANFE(String impressaoNECADANFE) {
		this.impressaoNECADANFE = impressaoNECADANFE;
	}

	public String getImpressaoNECADANFEFaltaDe() {
		return impressaoNECADANFEFaltaDe;
	}

	public void setImpressaoNECADANFEFaltaDe(String impressaoNECADANFEFaltaDe) {
		this.impressaoNECADANFEFaltaDe = impressaoNECADANFEFaltaDe;
	}

	public String getImpressaoNECADANFEFaltaEm() {
		return impressaoNECADANFEFaltaEm;
	}

	public void setImpressaoNECADANFEFaltaEm(String impressaoNECADANFEFaltaEm) {
		this.impressaoNECADANFEFaltaEm = impressaoNECADANFEFaltaEm;
	}

	public String getImpressaoNECADANFESobraDe() {
		return impressaoNECADANFESobraDe;
	}

	public void setImpressaoNECADANFESobraDe(String impressaoNECADANFESobraDe) {
		this.impressaoNECADANFESobraDe = impressaoNECADANFESobraDe;
	}

	public String getImpressaoNECADANFESobraEm() {
		return impressaoNECADANFESobraEm;
	}

	public void setImpressaoNECADANFESobraEm(String impressaoNECADANFESobraEm) {
		this.impressaoNECADANFESobraEm = impressaoNECADANFESobraEm;
	}

	public String getImpressaoCE() {
		return impressaoCE;
	}

	public void setImpressaoCE(String impressaoCE) {
		this.impressaoCE = impressaoCE;
	}

	public String getImpressaoCEFaltaDe() {
		return impressaoCEFaltaDe;
	}

	public void setImpressaoCEFaltaDe(String impressaoCEFaltaDe) {
		this.impressaoCEFaltaDe = impressaoCEFaltaDe;
	}

	public String getImpressaoCEFaltaEm() {
		return impressaoCEFaltaEm;
	}

	public void setImpressaoCEFaltaEm(String impressaoCEFaltaEm) {
		this.impressaoCEFaltaEm = impressaoCEFaltaEm;
	}

	public String getImpressaoCESobraDe() {
		return impressaoCESobraDe;
	}

	public void setImpressaoCESobraDe(String impressaoCESobraDe) {
		this.impressaoCESobraDe = impressaoCESobraDe;
	}

	public String getImpressaoCESobraEm() {
		return impressaoCESobraEm;
	}

	public void setImpressaoCESobraEm(String impressaoCESobraEm) {
		this.impressaoCESobraEm = impressaoCESobraEm;
	}

	public String getUtilizaContratoComCotas() {
		return utilizaContratoComCotas;
	}

	public void setUtilizaContratoComCotas(String utilizaContratoComCotas) {
		this.utilizaContratoComCotas = utilizaContratoComCotas;
	}

	public String getPrazoContrato() {
		return prazoContrato;
	}

	public void setPrazoContrato(String prazoContrato) {
		this.prazoContrato = prazoContrato;
	}

	public String getInformacoesComplementaresContrato() {
		return informacoesComplementaresContrato;
	}

	public void setInformacoesComplementaresContrato(
			String informacoesComplementaresContrato) {
		this.informacoesComplementaresContrato = informacoesComplementaresContrato;
	}

	public String getUtilizaProcuracaoEntregadores() {
		return utilizaProcuracaoEntregadores;
	}

	public void setUtilizaProcuracaoEntregadores(
			String utilizaProcuracaoEntregadores) {
		this.utilizaProcuracaoEntregadores = utilizaProcuracaoEntregadores;
	}

	public String getInformacoesComplementaresProcuracao() {
		return informacoesComplementaresProcuracao;
	}

	public void setInformacoesComplementaresProcuracao(
			String informacoesComplementaresProcuracao) {
		this.informacoesComplementaresProcuracao = informacoesComplementaresProcuracao;
	}

	public String getUtilizaGarantiaPdv() {
		return utilizaGarantiaPdv;
	}

	public void setUtilizaGarantiaPdv(String utilizaGarantiaPdv) {
		this.utilizaGarantiaPdv = utilizaGarantiaPdv;
	}

	public String getChequeCalcao() {
		return chequeCalcao;
	}

	public void setChequeCalcao(String chequeCalcao) {
		this.chequeCalcao = chequeCalcao;
	}

	public String getChequeCalcaoValor() {
		return chequeCalcaoValor;
	}

	public void setChequeCalcaoValor(String chequeCalcaoValor) {
		this.chequeCalcaoValor = chequeCalcaoValor;
	}

	public String getFiador() {
		return fiador;
	}

	public void setFiador(String fiador) {
		this.fiador = fiador;
	}

	public String getFiadorValor() {
		return fiadorValor;
	}

	public void setFiadorValor(String fiadorValor) {
		this.fiadorValor = fiadorValor;
	}

	public String getImovel() {
		return imovel;
	}

	public void setImovel(String imovel) {
		this.imovel = imovel;
	}

	public String getImovelValor() {
		return imovelValor;
	}

	public void setImovelValor(String imovelValor) {
		this.imovelValor = imovelValor;
	}

	public String getCaucaoLiquida() {
		return caucaoLiquida;
	}

	public void setCaucaoLiquida(String caucaoLiquida) {
		this.caucaoLiquida = caucaoLiquida;
	}

	public String getCaucaoLiquidaValor() {
		return caucaoLiquidaValor;
	}

	public void setCaucaoLiquidaValor(String caucaoLiquidaValor) {
		this.caucaoLiquidaValor = caucaoLiquidaValor;
	}

	public String getNotaPromissoria() {
		return notaPromissoria;
	}

	public void setNotaPromissoria(String notaPromissoria) {
		this.notaPromissoria = notaPromissoria;
	}

	public String getNotaPromissoriaValor() {
		return notaPromissoriaValor;
	}

	public void setNotaPromissoriaValor(String notaPromissoriaValor) {
		this.notaPromissoriaValor = notaPromissoriaValor;
	}

	public String getAntecedenciaValidade() {
		return antecedenciaValidade;
	}

	public void setAntecedenciaValidade(String antecedenciaValidade) {
		this.antecedenciaValidade = antecedenciaValidade;
	}

	public String getAntecedenciaValidadeValor() {
		return antecedenciaValidadeValor;
	}

	public void setAntecedenciaValidadeValor(String antecedenciaValidadeValor) {
		this.antecedenciaValidadeValor = antecedenciaValidadeValor;
	}

	public String getIndicadorReajusteCaucaoLiquida() {
		return indicadorReajusteCaucaoLiquida;
	}

	public void setIndicadorReajusteCaucaoLiquida(
			String indicadorReajusteCaucaoLiquida) {
		this.indicadorReajusteCaucaoLiquida = indicadorReajusteCaucaoLiquida;
	}

	public String getIndicadorReajusteCaucaoLiquidaValor() {
		return indicadorReajusteCaucaoLiquidaValor;
	}

	public void setIndicadorReajusteCaucaoLiquidaValor(
			String indicadorReajusteCaucaoLiquidaValor) {
		this.indicadorReajusteCaucaoLiquidaValor = indicadorReajusteCaucaoLiquidaValor;
	}

	public String getSugereSuspensaoQuandoAtingirBoletos() {
		return sugereSuspensaoQuandoAtingirBoletos;
	}

	public void setSugereSuspensaoQuandoAtingirBoletos(
			String sugereSuspensaoQuandoAtingirBoletos) {
		this.sugereSuspensaoQuandoAtingirBoletos = sugereSuspensaoQuandoAtingirBoletos;
	}

	public String getSugereSuspensaoQuandoAtingirReais() {
		return sugereSuspensaoQuandoAtingirReais;
	}

	public void setSugereSuspensaoQuandoAtingirReais(
			String sugereSuspensaoQuandoAtingirReais) {
		this.sugereSuspensaoQuandoAtingirReais = sugereSuspensaoQuandoAtingirReais;
	}

	public String getParcelamentoDividas() {
		return parcelamentoDividas;
	}

	public void setParcelamentoDividas(String parcelamentoDividas) {
		this.parcelamentoDividas = parcelamentoDividas;
	}

	public String getNegociacaoAteParcelas() {
		return negociacaoAteParcelas;
	}

	public void setNegociacaoAteParcelas(String negociacaoAteParcelas) {
		this.negociacaoAteParcelas = negociacaoAteParcelas;
	}

	public String getPermitePagamentoDividasDivergentes() {
		return permitePagamentoDividasDivergentes;
	}

	public void setPermitePagamentoDividasDivergentes(
			String permitePagamentoDividasDivergentes) {
		this.permitePagamentoDividasDivergentes = permitePagamentoDividasDivergentes;
	}

	public String getUtilizaControleAprovacao() {
		return utilizaControleAprovacao;
	}

	public void setUtilizaControleAprovacao(String utilizaControleAprovacao) {
		this.utilizaControleAprovacao = utilizaControleAprovacao;
	}

	public String getParaDebitosCreditos() {
		return paraDebitosCreditos;
	}

	public void setParaDebitosCreditos(String paraDebitosCreditos) {
		this.paraDebitosCreditos = paraDebitosCreditos;
	}

	public String getNegociacao() {
		return negociacao;
	}

	public void setNegociacao(String negociacao) {
		this.negociacao = negociacao;
	}

	public String getAjusteEstoque() {
		return ajusteEstoque;
	}

	public void setAjusteEstoque(String ajusteEstoque) {
		this.ajusteEstoque = ajusteEstoque;
	}

	public String getPostergacaoCobranca() {
		return postergacaoCobranca;
	}

	public void setPostergacaoCobranca(String postergacaoCobranca) {
		this.postergacaoCobranca = postergacaoCobranca;
	}

	public String getDevolucaoFornecedor() {
		return devolucaoFornecedor;
	}

	public void setDevolucaoFornecedor(String devolucaoFornecedor) {
		this.devolucaoFornecedor = devolucaoFornecedor;
	}

	public String getRecibo() {
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}

	public String getFaltasSobras() {
		return faltasSobras;
	}

	public void setFaltasSobras(String faltasSobras) {
		this.faltasSobras = faltasSobras;
	}

	public String getAprovacaoFaltaDe() {
		return aprovacaoFaltaDe;
	}

	public void setAprovacaoFaltaDe(String aprovacaoFaltaDe) {
		this.aprovacaoFaltaDe = aprovacaoFaltaDe;
	}

	public String getAprovacaoSobraDe() {
		return aprovacaoSobraDe;
	}

	public void setAprovacaoSobraDe(String aprovacaoSobraDe) {
		this.aprovacaoSobraDe = aprovacaoSobraDe;
	}

	public String getAprovacaoFaltaEm() {
		return aprovacaoFaltaEm;
	}

	public void setAprovacaoFaltaEm(String aprovacaoFaltaEm) {
		this.aprovacaoFaltaEm = aprovacaoFaltaEm;
	}

	public String getAprovacaoSobraEm() {
		return aprovacaoSobraEm;
	}

	public void setAprovacaoSobraEm(String aprovacaoSobraEm) {
		this.aprovacaoSobraEm = aprovacaoSobraEm;
	}

	public String getPrazoFollowUp() {
		return prazoFollowUp;
	}

	public void setPrazoFollowUp(String prazoFollowUp) {
		this.prazoFollowUp = prazoFollowUp;
	}

	public String getPrazoFollowUpFaltaDe() {
		return prazoFollowUpFaltaDe;
	}

	public void setPrazoFollowUpFaltaDe(String prazoFollowUpFaltaDe) {
		this.prazoFollowUpFaltaDe = prazoFollowUpFaltaDe;
	}

	public String getPrazoFollowUpSobraDe() {
		return prazoFollowUpSobraDe;
	}

	public void setPrazoFollowUpSobraDe(String prazoFollowUpSobraDe) {
		this.prazoFollowUpSobraDe = prazoFollowUpSobraDe;
	}

	public String getPrazoFollowUpFaltaEm() {
		return prazoFollowUpFaltaEm;
	}

	public void setPrazoFollowUpFaltaEm(String prazoFollowUpFaltaEm) {
		this.prazoFollowUpFaltaEm = prazoFollowUpFaltaEm;
	}

	public String getPrazoFollowUpSobraEm() {
		return prazoFollowUpSobraEm;
	}

	public void setPrazoFollowUpSobraEm(String prazoFollowUpSobraEm) {
		this.prazoFollowUpSobraEm = prazoFollowUpSobraEm;
	}

	public String getPrazoAvisoPrevioValidadeGarantia() {
		return prazoAvisoPrevioValidadeGarantia;
	}

	public void setPrazoAvisoPrevioValidadeGarantia(
			String prazoAvisoPrevioValidadeGarantia) {
		this.prazoAvisoPrevioValidadeGarantia = prazoAvisoPrevioValidadeGarantia;
	}

	public String getPrazoAvisoPrevioValidadeGarantiaFaltaDe() {
		return prazoAvisoPrevioValidadeGarantiaFaltaDe;
	}

	public void setPrazoAvisoPrevioValidadeGarantiaFaltaDe(
			String prazoAvisoPrevioValidadeGarantiaFaltaDe) {
		this.prazoAvisoPrevioValidadeGarantiaFaltaDe = prazoAvisoPrevioValidadeGarantiaFaltaDe;
	}

	public String getPrazoAvisoPrevioValidadeGarantiaSobraDe() {
		return prazoAvisoPrevioValidadeGarantiaSobraDe;
	}

	public void setPrazoAvisoPrevioValidadeGarantiaSobraDe(
			String prazoAvisoPrevioValidadeGarantiaSobraDe) {
		this.prazoAvisoPrevioValidadeGarantiaSobraDe = prazoAvisoPrevioValidadeGarantiaSobraDe;
	}

	public String getPrazoAvisoPrevioValidadeGarantiaFaltaEm() {
		return prazoAvisoPrevioValidadeGarantiaFaltaEm;
	}

	public void setPrazoAvisoPrevioValidadeGarantiaFaltaEm(
			String prazoAvisoPrevioValidadeGarantiaFaltaEm) {
		this.prazoAvisoPrevioValidadeGarantiaFaltaEm = prazoAvisoPrevioValidadeGarantiaFaltaEm;
	}

	public String getPrazoAvisoPrevioValidadeGarantiaSobraEm() {
		return prazoAvisoPrevioValidadeGarantiaSobraEm;
	}

	public void setPrazoAvisoPrevioValidadeGarantiaSobraEm(
			String prazoAvisoPrevioValidadeGarantiaSobraEm) {
		this.prazoAvisoPrevioValidadeGarantiaSobraEm = prazoAvisoPrevioValidadeGarantiaSobraEm;
	}

    /**
     * @return the chamadaoDiasSuspensao
     */
    public Integer getChamadaoDiasSuspensao() {
        return chamadaoDiasSuspensao;
    }

    /**
     * @param chamadaoDiasSuspensao the chamadaoDiasSuspensao to set
     */
    public void setChamadaoDiasSuspensao(Integer chamadaoDiasSuspensao) {
        this.chamadaoDiasSuspensao = chamadaoDiasSuspensao;
    }

    /**
     * @return the chamadaoValorConsignado
     */
    public String getChamadaoValorConsignado() {
        return chamadaoValorConsignado;
    }

    /**
     * @param chamadaoValorConsignado the chamadaoValorConsignado to set
     */
    public void setChamadaoValorConsignado(String chamadaoValorConsignado) {
        this.chamadaoValorConsignado = chamadaoValorConsignado;
    }

}
