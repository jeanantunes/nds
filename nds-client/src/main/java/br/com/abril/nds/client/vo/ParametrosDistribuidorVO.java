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
	
	// Frame Impressão Interface LED
	// MODELO_1 ou MODELO_2 ou MODELO_3
	private String impressaoInterfaceLED;

	// Frame Impressão NECA / DANFE
	// MODELO_1, MODELO_2 ou DANFE
	private String impressaoNECADANFE;

	// Frame Impressão CE
	// MODELO_1 ou MODELO_2
	private String impressaoCE;
	
	// Aba Contratos e Garantias
	// Frame Condições de Contratação
	private boolean utilizaContratoComCotas;
	
	private Integer prazoContrato;

	private String informacoesComplementaresContrato;

	// Frame Procuração
	private String utilizaProcuracaoEntregadores;

	private String informacoesComplementaresProcuracao;
	
	/**
	 * Distribuidor utiliza termo de adesão para entrega em bancas
	 */
	private boolean utilizaTermoAdesaoEntregaBancas;
	
	/**
	 * Complemento termo de adesão entrega em bancas
	 */
	private String complementoTermoAdesaoEntregaBancas;

	//Frame Garantia
	private boolean utilizaGarantiaPdv;
	
	private boolean utilizaChequeCaucao;

	private Integer validadeChequeCaucao;
	
	private boolean utilizaFiador;
	
	private Integer validadeFiador;
	
	private boolean utilizaImovel;
	
	private Integer validadeImovel;
	
	private boolean utilizaCaucaoLiquida;
	
	private Integer validadeCaucaoLiquida;
	
	private boolean utilizaNotaPromissoria;
	
	private Integer validadeNotaPromissoria;
	
	private boolean utilizaAntecedenciaValidade;
	
	private Integer validadeAntecedenciaValidade;
	
	/**
	 * Distribuidor utiliza garantia "OUTROS"
	 */
	private boolean utilizaOutros;
	
	/**
	 * Validade da garantia "OUTROS" em meses
	 */
	private Integer validadeOutros;
	
	// Aba Negociação
	// Frame Negociação de Dívidas
	private String sugereSuspensaoQuandoAtingirBoletos;

	private String sugereSuspensaoQuandoAtingirReais;

	private Boolean parcelamentoDividas;
	

	private Boolean utilizaDesconto;
	

	private String percentualDesconto;
	
	private String negociacaoAteParcelas;
	
	// Aba Aprovação
	// Frame Aprovação
	private boolean utilizaControleAprovacao;

	private boolean paraDebitosCreditos;

	private boolean negociacao;
	
	private boolean ajusteEstoque;
	
	private boolean postergacaoCobranca;
	
	private boolean devolucaoFornecedor;
	
	private boolean faltasSobras;
	
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

	public String getImpressaoInterfaceLED() {
		return impressaoInterfaceLED;
	}

	public void setImpressaoInterfaceLED(String impressaoInterfaceLED) {
		this.impressaoInterfaceLED = impressaoInterfaceLED;
	}

	public String getImpressaoNECADANFE() {
		return impressaoNECADANFE;
	}

	public void setImpressaoNECADANFE(String impressaoNECADANFE) {
		this.impressaoNECADANFE = impressaoNECADANFE;
	}

	public String getImpressaoCE() {
		return impressaoCE;
	}

	public void setImpressaoCE(String impressaoCE) {
		this.impressaoCE = impressaoCE;
	}

	public boolean getUtilizaContratoComCotas() {
		return utilizaContratoComCotas;
	}

	public void setUtilizaContratoComCotas(boolean utilizaContratoComCotas) {
		this.utilizaContratoComCotas = utilizaContratoComCotas;
	}

	public Integer getPrazoContrato() {
		return prazoContrato;
	}

	public void setPrazoContrato(Integer prazoContrato) {
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
	
	/**
     * @return the utilizaTermoAdesaoEntregaBancas
     */
    public boolean isUtilizaTermoAdesaoEntregaBancas() {
        return utilizaTermoAdesaoEntregaBancas;
    }

    /**
     * @param utilizaTermoAdesaoEntregaBancas the utilizaTermoAdesaoEntregaBancas to set
     */
    public void setUtilizaTermoAdesaoEntregaBancas(
            boolean utilizaTermoAdesaoEntregaBancas) {
        this.utilizaTermoAdesaoEntregaBancas = utilizaTermoAdesaoEntregaBancas;
    }

    /**
     * @return the complementoTermoAdesaoEntregaBancas
     */
    public String getComplementoTermoAdesaoEntregaBancas() {
        return complementoTermoAdesaoEntregaBancas;
    }

    /**
     * @param complementoTermoAdesaoEntregaBancas the complementoTermoAdesaoEntregaBancas to set
     */
    public void setComplementoTermoAdesaoEntregaBancas(
            String complementoTermoAdesaoEntregaBancas) {
        this.complementoTermoAdesaoEntregaBancas = complementoTermoAdesaoEntregaBancas;
    }

    public boolean getUtilizaGarantiaPdv() {
		return utilizaGarantiaPdv;
	}

	public void setUtilizaGarantiaPdv(boolean utilizaGarantiaPdv) {
		this.utilizaGarantiaPdv = utilizaGarantiaPdv;
	}
	
	/**
     * @return the utilizaChequeCaucao
     */
    public boolean isUtilizaChequeCaucao() {
        return utilizaChequeCaucao;
    }

    /**
     * @param utilizaChequeCaucao the utilizaChequeCaucao to set
     */
    public void setUtilizaChequeCaucao(boolean utilizaChequeCaucao) {
        this.utilizaChequeCaucao = utilizaChequeCaucao;
    }

    /**
     * @return the validadeChequeCaucao
     */
    public Integer getValidadeChequeCaucao() {
        return validadeChequeCaucao;
    }

    /**
     * @param validadeCheque the validadeChequeCaucao to set
     */
    public void setValidadeChequeCaucao(Integer validadeChequeCaucao) {
        this.validadeChequeCaucao = validadeChequeCaucao;
    }

    /**
     * @return the utilizaFiador
     */
    public boolean isUtilizaFiador() {
        return utilizaFiador;
    }

    /**
     * @param utilizaFiador the utilizaFiador to set
     */
    public void setUtilizaFiador(boolean utilizaFiador) {
        this.utilizaFiador = utilizaFiador;
    }

    /**
     * @return the validadeFiador
     */
    public Integer getValidadeFiador() {
        return validadeFiador;
    }

    /**
     * @param validadeFiador the validadeFiador to set
     */
    public void setValidadeFiador(Integer validadeFiador) {
        this.validadeFiador = validadeFiador;
    }

    /**
     * @return the utilizaImovel
     */
    public boolean isUtilizaImovel() {
        return utilizaImovel;
    }

    /**
     * @param utilizaImovel the utilizaImovel to set
     */
    public void setUtilizaImovel(boolean utilizaImovel) {
        this.utilizaImovel = utilizaImovel;
    }

    /**
     * @return the validadeImovel
     */
    public Integer getValidadeImovel() {
        return validadeImovel;
    }

    /**
     * @param validadeImovel the validadeImovel to set
     */
    public void setValidadeImovel(Integer validadeImovel) {
        this.validadeImovel = validadeImovel;
    }

    /**
     * @return the utilizaCaucaoLiquida
     */
    public boolean isUtilizaCaucaoLiquida() {
        return utilizaCaucaoLiquida;
    }

    /**
     * @param utilizaCaucaoLiquida the utilizaCaucaoLiquida to set
     */
    public void setUtilizaCaucaoLiquida(boolean utilizaCaucaoLiquida) {
        this.utilizaCaucaoLiquida = utilizaCaucaoLiquida;
    }

    /**
     * @return the validadeCaucaoLiquida
     */
    public Integer getValidadeCaucaoLiquida() {
        return validadeCaucaoLiquida;
    }

    /**
     * @param validadeCaucaoLiquida the validadeCaucaoLiquida to set
     */
    public void setValidadeCaucaoLiquida(Integer validadeCaucaoLiquida) {
        this.validadeCaucaoLiquida = validadeCaucaoLiquida;
    }

    /**
     * @return the utilizaNotaPromissoria
     */
    public boolean isUtilizaNotaPromissoria() {
        return utilizaNotaPromissoria;
    }

    /**
     * @param utilizaNotaPromissoria the utilizaNotaPromissoria to set
     */
    public void setUtilizaNotaPromissoria(boolean utilizaNotaPromissoria) {
        this.utilizaNotaPromissoria = utilizaNotaPromissoria;
    }

    /**
     * @return the validadeNotaPromissoria
     */
    public Integer getValidadeNotaPromissoria() {
        return validadeNotaPromissoria;
    }

    /**
     * @param validadeNotaPromissoria the validadeNotaPromissoria to set
     */
    public void setValidadeNotaPromissoria(Integer validadeNotaPromissoria) {
        this.validadeNotaPromissoria = validadeNotaPromissoria;
    }

    /**
     * @return the utilizaAntecedenciaValidade
     */
    public boolean isUtilizaAntecedenciaValidade() {
        return utilizaAntecedenciaValidade;
    }

    /**
     * @param utilizaAntecedenciaValidade the utilizaAntecedenciaValidade to set
     */
    public void setUtilizaAntecedenciaValidade(boolean utilizaAntecedenciaValidade) {
        this.utilizaAntecedenciaValidade = utilizaAntecedenciaValidade;
    }

    /**
     * @return the validadeAntecedenciaValidade
     */
    public Integer getValidadeAntecedenciaValidade() {
        return validadeAntecedenciaValidade;
    }

    /**
     * @param validadeAntecedenciaValidade the validadeAntecedenciaValidade to set
     */
    public void setValidadeAntecedenciaValidade(Integer validadeAntecedenciaValidade) {
        this.validadeAntecedenciaValidade = validadeAntecedenciaValidade;
    }

    /**
     * @return the utilizaOutros
     */
    public boolean isUtilizaOutros() {
        return utilizaOutros;
    }

    /**
     * @param utilizaOutros the utilizaOutros to set
     */
    public void setUtilizaOutros(boolean utilizaOutros) {
        this.utilizaOutros = utilizaOutros;
    }

    /**
     * @return the validadeOutros
     */
    public Integer getValidadeOutros() {
        return validadeOutros;
    }

    /**
     * @param validadeOutros the validadeOutros to set
     */
    public void setValidadeOutros(Integer validadeOutros) {
        this.validadeOutros = validadeOutros;
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

	public Boolean getParcelamentoDividas() {
		return parcelamentoDividas;
	}

	public void setParcelamentoDividas(Boolean parcelamentoDividas) {
		this.parcelamentoDividas = parcelamentoDividas;
	}

	public String getNegociacaoAteParcelas() {
		return negociacaoAteParcelas;
	}

	public void setNegociacaoAteParcelas(String negociacaoAteParcelas) {
		this.negociacaoAteParcelas = negociacaoAteParcelas;
	}

	public boolean getUtilizaControleAprovacao() {
		return utilizaControleAprovacao;
	}

	public void setUtilizaControleAprovacao(boolean utilizaControleAprovacao) {
		this.utilizaControleAprovacao = utilizaControleAprovacao;
	}

	public boolean getParaDebitosCreditos() {
		return paraDebitosCreditos;
	}

	public void setParaDebitosCreditos(boolean paraDebitosCreditos) {
		this.paraDebitosCreditos = paraDebitosCreditos;
	}

	public boolean getNegociacao() {
		return negociacao;
	}

	public void setNegociacao(boolean negociacao) {
		this.negociacao = negociacao;
	}

	public boolean getAjusteEstoque() {
		return ajusteEstoque;
	}

	public void setAjusteEstoque(boolean ajusteEstoque) {
		this.ajusteEstoque = ajusteEstoque;
	}

	public boolean getPostergacaoCobranca() {
		return postergacaoCobranca;
	}

	public void setPostergacaoCobranca(boolean postergacaoCobranca) {
		this.postergacaoCobranca = postergacaoCobranca;
	}

	public boolean getDevolucaoFornecedor() {
		return devolucaoFornecedor;
	}

	public void setDevolucaoFornecedor(boolean devolucaoFornecedor) {
		this.devolucaoFornecedor = devolucaoFornecedor;
	}

	public boolean getFaltasSobras() {
		return faltasSobras;
	}

	public void setFaltasSobras(boolean faltasSobras) {
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

	/**
	 * @return the utilizaDesconto
	 */
	public Boolean getUtilizaDesconto() {
		return utilizaDesconto;
	}

	/**
	 * @param utilizaDesconto the utilizaDesconto to set
	 */
	public void setUtilizaDesconto(Boolean utilizaDesconto) {
		this.utilizaDesconto = utilizaDesconto;
	}

	/**
	 * @return the percentualDesconto
	 */
	public String getPercentualDesconto() {
		return percentualDesconto;
	}

	/**
	 * @param percentualDesconto the percentualDesconto to set
	 */
	public void setPercentualDesconto(String percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

}
