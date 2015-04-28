package br.com.abril.nds.client.vo;


import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.RegimeTributarioDTO;
import br.com.abril.nds.dto.TributoAliquotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.vo.EnderecoVO;


/**
 * VO para controle dos parametros do distribuidor
 * @author InfoA2
 */
public class ParametrosDistribuidorVO {

    // -= Aba Cadastro / Fiscal =-
	// Frame Cadastro Distribuidor
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String email;
    private String codigoDistribuidorDinap;
    private String codigoDistribuidorFC;
    // Frame Endereço
    private EnderecoVO endereco = new EnderecoVO();
    // Frame Fiscal
    private RegimeTributarioDTO regimeTributario;
    
    private TipoAtividade tipoAtividade;
    
    private boolean possuiRegimeEspecialDispensaInterna;
    
    private String nfInformacoesAdicionais;
    
	private String numeroDispositivoLegal;
	
	private Date dataLimiteVigenciaRegimeEspecial;
    
    private String numeroTelefone;
    
    private String numeroDDD;
  
    private String cnae;
    
    // -= Aba Operação =-
    // Frame Parciais / Matriz de Lançamento
    private int relancamentoParciaisEmDias;
    // Frame Recolhimento
    private TipoContabilizacaoCE tipoContabilizacaoCE;
    private boolean aceitaEncalheJuramentada;
    private boolean diaRecolhimentoPrimeiro;
    private boolean diaRecolhimentoSegundo;
    private boolean diaRecolhimentoTerceiro;
    private boolean diaRecolhimentoQuarto;
    private boolean diaRecolhimentoQuinto;
    private boolean limiteCEProximaSemana;
    private boolean aceitaRecolhimentoParcialAtraso;
    private boolean supervisionaVendaNegativa;
    private boolean conferenciaCegaRecebimento;
    private boolean conferenciaCegaEncalhe;
    
    private boolean pararAcumuloDividas;

    // Frame Capacidade de Manuseio
    private Integer capacidadeManuseioHomemHoraLancamento;
    private Integer capacidadeManuseioHomemHoraRecolhimento;

    // Frame Reutilização de Código de Cota
    private Long reutilizacaoCodigoCotaInativa;
    private boolean utilizaSugestaoIncrementoCodigo;
    
    // -= Aba Documentos =-
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

    private String nomeArquivoInterfaceLED1;
    
    private String nomeArquivoInterfaceLED2;
    
    private String nomeArquivoInterfaceLED3;

    // Frame Impressão NECA / DANFE
    // MODELO_1, MODELO_2 ou DANFE
    private String impressaoNECADANFE;
    // Frame Impressão CE
    // MODELO_1 ou MODELO_2
    private String impressaoCE;
    
    // -= Aba Contratos e Garantias =-
    // Frame Condições de Contratação
    private boolean utilizaContratoComCotas;
    private Integer prazoContrato;
    private String informacoesComplementaresContrato;

    // Frame Procuração
    private boolean utilizaProcuracaoEntregadores;
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
    private boolean sugereSuspensao;
    private Boolean parcelamentoDividas;
    private Boolean utilizaDesconto;
    private String percentualDesconto;
    private String negociacaoAteParcelas;
    private boolean aceitaBaixaPagamentoMaior;
    private boolean aceitaBaixaPagamentoMenor;
    private boolean aceitaBaixaPagamentoVencido;
    private Integer numeroDiasNovaCobranca;
    private String assuntoEmailCobranca;
    private String mensagemEmailCobranca;
    private String descricaoTaxaExtra;
    private BigDecimal percentualTaxaExtra;
    
    // Aba Aprovação
    // Frame Aprovação
    private boolean utilizaControleAprovacao;
    private boolean paraDebitosCreditos;
    private boolean negociacao;
    private boolean ajusteEstoque;
    private boolean postergacaoCobranca;
    private boolean devolucaoFornecedor;
    private boolean faltasSobras;

    private boolean consolidadoCota;
    
    private String aprovacaoFaltaDe;
    private String aprovacaoSobraDe;
    private String aprovacaoFaltaEm;
    private String aprovacaoSobraEm;
    private Integer prazoFollowUp;
    private Integer prazoAvisoPrevioValidadeGarantia;
    private Integer chamadaoDiasSuspensao;
    private String chamadaoValorConsignado;
    
    private List<TiposNotasFiscaisParametrosVO> tiposNotasFiscais;
    private List<TributoAliquotaDTO> tributosAliquotas;

    // Aba Distribuição
    // Grid Distribuição
	private boolean geracaoAutomaticaEstudo;
	private Integer vendaMediaMais;
	private boolean pracaVeraneio;
	private boolean complementarAutomatico;
	private Integer percentualMaximoFixacao;
    
	// Tabela Classificação de Cota
	private List<DistribuidorClassificacaoCotaVO> listClassificacaoCota;
	
	// Tabela % Excedente
	private List<DistribuidorPercentualExcedenteVO> listPercentualExcedente;
	
    /**
     * @return the razaoSocial
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @param razaoSocial the razaoSocial to set
     */
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * @return the nomeFantasia
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     * @param nomeFantasia the nomeFantasia to set
     */
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @param inscricaoEstadual the inscricaoEstadual to set
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * @return the inscricaoMunicipal
     */
    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    /**
     * @param inscricaoMunicipal the inscricaoMunicipal to set
     */
    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the codigoDistribuidorDinap
     */
    public String getCodigoDistribuidorDinap() {
        return codigoDistribuidorDinap;
    }

    /**
     * @param codigoDistribuidorDinap the codigoDistribuidorDinap to set
     */
    public void setCodigoDistribuidorDinap(String codigoDistribuidorDinap) {
        this.codigoDistribuidorDinap = codigoDistribuidorDinap;
    }

    /**
     * @return the codigoDistribuidorFC
     */
    public String getCodigoDistribuidorFC() {
        return codigoDistribuidorFC;
    }

    /**
     * @param codigoDistribuidorFC the codigoDistribuidorFC to set
     */
    public void setCodigoDistribuidorFC(String codigoDistribuidorFC) {
        this.codigoDistribuidorFC = codigoDistribuidorFC;
    }
    
    /**
     * @return the regimeTributario
     */
    public RegimeTributarioDTO getRegimeTributario() {
        return regimeTributario;
    }

    /**
     * @param regimeTributario the regimeTributario to set
     */
    public void setRegimeTributario(RegimeTributarioDTO regimeTributario) {
        this.regimeTributario = regimeTributario;
    }
    
    public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public boolean isPossuiRegimeEspecialDispensaInterna() {
        return possuiRegimeEspecialDispensaInterna;
    }

    public void setPossuiRegimeEspecialDispensaInterna(boolean possuiRegimeEspecialDispensaInterna) {
        this.possuiRegimeEspecialDispensaInterna = possuiRegimeEspecialDispensaInterna;
    }

    public String getNfInformacoesAdicionais() {
		return nfInformacoesAdicionais;
	}

	public void setNfInformacoesAdicionais(String nfInformacoesAdicionais) {
		this.nfInformacoesAdicionais = nfInformacoesAdicionais;
	}

	public String getNumeroDispositivoLegal() {
		return numeroDispositivoLegal;
	}

	public void setNumeroDispositivoLegal(String numeroDispositivoLegal) {
		this.numeroDispositivoLegal = numeroDispositivoLegal;
	}

	public Date getDataLimiteVigenciaRegimeEspecial() {
		return dataLimiteVigenciaRegimeEspecial;
	}

	public void setDataLimiteVigenciaRegimeEspecial(
			Date dataLimiteVigenciaRegimeEspecial) {
		this.dataLimiteVigenciaRegimeEspecial = dataLimiteVigenciaRegimeEspecial;
	}

	/**
     * @return the endereco
     */
    public EnderecoVO getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(EnderecoVO endereco) {
        this.endereco = endereco;
    }

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

    public boolean isAceitaEncalheJuramentada() {
        return aceitaEncalheJuramentada;
    }

    public void setAceitaEncalheJuramentada(boolean aceitaEncalheJuramentada) {
        this.aceitaEncalheJuramentada = aceitaEncalheJuramentada;
    }

    public boolean isDiaRecolhimentoPrimeiro() {
        return diaRecolhimentoPrimeiro;
    }

    public void setDiaRecolhimentoPrimeiro(boolean diaRecolhimentoPrimeiro) {
        this.diaRecolhimentoPrimeiro = diaRecolhimentoPrimeiro;
    }

    public boolean isDiaRecolhimentoSegundo() {
        return diaRecolhimentoSegundo;
    }

    public void setDiaRecolhimentoSegundo(boolean diaRecolhimentoSegundo) {
        this.diaRecolhimentoSegundo = diaRecolhimentoSegundo;
    }

    public boolean isDiaRecolhimentoTerceiro() {
        return diaRecolhimentoTerceiro;
    }

    public void setDiaRecolhimentoTerceiro(boolean diaRecolhimentoTerceiro) {
        this.diaRecolhimentoTerceiro = diaRecolhimentoTerceiro;
    }

    public boolean isDiaRecolhimentoQuarto() {
        return diaRecolhimentoQuarto;
    }

    public void setDiaRecolhimentoQuarto(boolean diaRecolhimentoQuarto) {
        this.diaRecolhimentoQuarto = diaRecolhimentoQuarto;
    }

    public boolean isDiaRecolhimentoQuinto() {
        return diaRecolhimentoQuinto;
    }

    public void setDiaRecolhimentoQuinto(boolean diaRecolhimentoQuinto) {
        this.diaRecolhimentoQuinto = diaRecolhimentoQuinto;
    }

    public boolean isLimiteCEProximaSemana() {
        return limiteCEProximaSemana;
    }

    public void setLimiteCEProximaSemana(boolean limiteCEProximaSemana) {
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

    public boolean isConferenciaCegaRecebimento() {
        return conferenciaCegaRecebimento;
    }

    public void setConferenciaCegaRecebimento(boolean conferenciaCegaRecebimento) {
        this.conferenciaCegaRecebimento = conferenciaCegaRecebimento;
    }

    public boolean isConferenciaCegaEncalhe() {
        return conferenciaCegaEncalhe;
    }

    public void setConferenciaCegaEncalhe(boolean conferenciaCegaEncalhe) {
        this.conferenciaCegaEncalhe = conferenciaCegaEncalhe;
    }
    
	/**
	 * @return the pararAcumuloDividas
	 */
	public boolean isPararAcumuloDividas() {
		return pararAcumuloDividas;
	}

	/**
	 * @param pararAcumuloDividas the pararAcumuloDividas to set
	 */
	public void setPararAcumuloDividas(boolean pararAcumuloDividas) {
		this.pararAcumuloDividas = pararAcumuloDividas;
	}

	public Integer getCapacidadeManuseioHomemHoraLancamento() {
        return capacidadeManuseioHomemHoraLancamento;
    }

    public void setCapacidadeManuseioHomemHoraLancamento(
            Integer capacidadeManuseioHomemHoraLancamento) {
        this.capacidadeManuseioHomemHoraLancamento = capacidadeManuseioHomemHoraLancamento;
    }

    public Integer getCapacidadeManuseioHomemHoraRecolhimento() {
        return capacidadeManuseioHomemHoraRecolhimento;
    }

    public void setCapacidadeManuseioHomemHoraRecolhimento(
            Integer capacidadeManuseioHomemHoraRecolhimento) {
        this.capacidadeManuseioHomemHoraRecolhimento = capacidadeManuseioHomemHoraRecolhimento;
    }

    public Long getReutilizacaoCodigoCotaInativa() {
        return reutilizacaoCodigoCotaInativa;
    }

    public void setReutilizacaoCodigoCotaInativa(
            Long reutilizacaoCodigoCotaInativa) {
        this.reutilizacaoCodigoCotaInativa = reutilizacaoCodigoCotaInativa;
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

    /**
	 * @return the nomeArquivoInterfaceLED1
	 */
	public String getNomeArquivoInterfaceLED1() {
		return nomeArquivoInterfaceLED1;
	}

	/**
	 * @param nomeArquivoInterfaceLED1 the nomeArquivoInterfaceLED1 to set
	 */
	public void setNomeArquivoInterfaceLED1(String nomeArquivoInterfaceLED1) {
		this.nomeArquivoInterfaceLED1 = nomeArquivoInterfaceLED1;
	}

	/**
	 * @return the nomeArquivoInterfaceLED2
	 */
	public String getNomeArquivoInterfaceLED2() {
		return nomeArquivoInterfaceLED2;
	}

	/**
	 * @param nomeArquivoInterfaceLED2 the nomeArquivoInterfaceLED2 to set
	 */
	public void setNomeArquivoInterfaceLED2(String nomeArquivoInterfaceLED2) {
		this.nomeArquivoInterfaceLED2 = nomeArquivoInterfaceLED2;
	}

	/**
	 * @return the nomeArquivoInterfaceLED3
	 */
	public String getNomeArquivoInterfaceLED3() {
		return nomeArquivoInterfaceLED3;
	}

	/**
	 * @param nomeArquivoInterfaceLED3 the nomeArquivoInterfaceLED3 to set
	 */
	public void setNomeArquivoInterfaceLED3(String nomeArquivoInterfaceLED3) {
		this.nomeArquivoInterfaceLED3 = nomeArquivoInterfaceLED3;
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

    public boolean isUtilizaContratoComCotas() {
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

    public boolean isUtilizaProcuracaoEntregadores() {
        return utilizaProcuracaoEntregadores;
    }

    public void setUtilizaProcuracaoEntregadores(
            boolean utilizaProcuracaoEntregadores) {
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

    public boolean isUtilizaGarantiaPdv() {
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
     * @param validadeChequeCaucao the validadeChequeCaucao to set
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

	public boolean isSugereSuspensao() {
		return sugereSuspensao;
	}

	public void setSugereSuspensao(boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
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

	/**
	 * @return the aceitaBaixaPagamentoMaior
	 */
	public boolean isAceitaBaixaPagamentoMaior() {
		return aceitaBaixaPagamentoMaior;
	}

	/**
	 * @param aceitaBaixaPagamentoMaior the aceitaBaixaPagamentoMaior to set
	 */
	public void setAceitaBaixaPagamentoMaior(boolean aceitaBaixaPagamentoMaior) {
		this.aceitaBaixaPagamentoMaior = aceitaBaixaPagamentoMaior;
	}

	/**
	 * @return the aceitaBaixaPagamentoMenor
	 */
	public boolean isAceitaBaixaPagamentoMenor() {
		return aceitaBaixaPagamentoMenor;
	}

	/**
	 * @param aceitaBaixaPagamentoMenor the aceitaBaixaPagamentoMenor to set
	 */
	public void setAceitaBaixaPagamentoMenor(boolean aceitaBaixaPagamentoMenor) {
		this.aceitaBaixaPagamentoMenor = aceitaBaixaPagamentoMenor;
	}

	/**
	 * @return the aceitaBaixaPagamentoVencido
	 */
	public boolean isAceitaBaixaPagamentoVencido() {
		return aceitaBaixaPagamentoVencido;
	}

	/**
	 * @param aceitaBaixaPagamentoVencido the aceitaBaixaPagamentoVencido to set
	 */
	public void setAceitaBaixaPagamentoVencido(boolean aceitaBaixaPagamentoVencido) {
		this.aceitaBaixaPagamentoVencido = aceitaBaixaPagamentoVencido;
	}

	/**
	 * @return the numeroDiasNovaCobranca
	 */
	public Integer getNumeroDiasNovaCobranca() {
		return numeroDiasNovaCobranca;
	}

	/**
	 * @param numeroDiasNovaCobranca the numeroDiasNovaCobranca to set
	 */
	public void setNumeroDiasNovaCobranca(Integer numeroDiasNovaCobranca) {
		this.numeroDiasNovaCobranca = numeroDiasNovaCobranca;
	}

	/**
	 * @return the assuntoEmailCobranca
	 */
	public String getAssuntoEmailCobranca() {
		return assuntoEmailCobranca;
	}

	/**
	 * @param assuntoEmailCobranca the assuntoEmailCobranca to set
	 */
	public void setAssuntoEmailCobranca(String assuntoEmailCobranca) {
		this.assuntoEmailCobranca = assuntoEmailCobranca;
	}

	/**
	 * @return the mensagemEmailCobranca
	 */
	public String getMensagemEmailCobranca() {
		return mensagemEmailCobranca;
	}

	/**
	 * @param mensagemEmailCobranca the mensagemEmailCobranca to set
	 */
	public void setMensagemEmailCobranca(String mensagemEmailCobranca) {
		this.mensagemEmailCobranca = mensagemEmailCobranca;
	}

	public String getDescricaoTaxaExtra() {
		return descricaoTaxaExtra;
	}

	public void setDescricaoTaxaExtra(String descricaoTaxaExtra) {
		this.descricaoTaxaExtra = descricaoTaxaExtra;
	}

	public BigDecimal getPercentualTaxaExtra() {
		return percentualTaxaExtra;
	}

	public void setPercentualTaxaExtra(BigDecimal percentualTaxaExtra) {
		this.percentualTaxaExtra = percentualTaxaExtra;
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

    public Integer getPrazoFollowUp() {
        return prazoFollowUp;
    }

    public void setPrazoFollowUp(Integer prazoFollowUp) {
        this.prazoFollowUp = prazoFollowUp;
    }

    public Integer getPrazoAvisoPrevioValidadeGarantia() {
        return prazoAvisoPrevioValidadeGarantia;
    }

    public void setPrazoAvisoPrevioValidadeGarantia(
            Integer prazoAvisoPrevioValidadeGarantia) {
        this.prazoAvisoPrevioValidadeGarantia = prazoAvisoPrevioValidadeGarantia;
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
    
	/**
	 * @return the consolidadoCota
	 */
	public boolean isConsolidadoCota() {
		return consolidadoCota;
	}

	/**
	 * @param consolidadoCota the consolidadoCota to set
	 */
	public void setConsolidadoCota(boolean consolidadoCota) {
		this.consolidadoCota = consolidadoCota;
	}

	/**
     * Verifica se alguma garantia foi marcada para utilização
     * @return true se ao menos uma ganatia esta sendo utilizada
     */
    public boolean isGarantiasUtilizadas() {
        return utilizaChequeCaucao || utilizaCaucaoLiquida || utilizaFiador
                || utilizaNotaPromissoria || utilizaImovel
                || utilizaOutros;
    }

	/**
	 * Obtém utilizaSugestaoIncrementoCodigo
	 *
	 * @return boolean
	 */
	public boolean isUtilizaSugestaoIncrementoCodigo() {
		return utilizaSugestaoIncrementoCodigo;
	}

	/**
	 * Atribuí utilizaSugestaoIncrementoCodigo
	 * @param utilizaSugestaoIncrementoCodigo 
	 */
	public void setUtilizaSugestaoIncrementoCodigo(
			boolean utilizaSugestaoIncrementoCodigo) {
		this.utilizaSugestaoIncrementoCodigo = utilizaSugestaoIncrementoCodigo;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public String getNumeroDDD() {
		return numeroDDD;
	}

	public void setNumeroDDD(String numeroDDD) {
		this.numeroDDD = numeroDDD;
	}
	
	public String getCnae() {
		return cnae;
	}

	public void setCnae(String cnae) {
		this.cnae = cnae;
	}

	public List<TiposNotasFiscaisParametrosVO> getTiposNotasFiscais() {
		return tiposNotasFiscais;
	}

	public void setTiposNotasFiscais(List<TiposNotasFiscaisParametrosVO> tiposNotasFiscais) {
		this.tiposNotasFiscais = tiposNotasFiscais;
	}
	
	public List<TributoAliquotaDTO> getTributosAliquotas() {
		return tributosAliquotas;
	}

	public void setTributosAliquotas(List<TributoAliquotaDTO> tributosAliquotas) {
		this.tributosAliquotas = tributosAliquotas;
	}

	public boolean isGeracaoAutomaticaEstudo() {
		return geracaoAutomaticaEstudo;
	}

	public void setGeracaoAutomaticaEstudo(boolean geracaoAutomaticaEstudo) {
		this.geracaoAutomaticaEstudo = geracaoAutomaticaEstudo;
	}

	public Integer getVendaMediaMais() {
		return vendaMediaMais;
	}

	public void setVendaMediaMais(Integer vendaMediaMais) {
		this.vendaMediaMais = vendaMediaMais;
	}

	public boolean isPracaVeraneio() {
		return pracaVeraneio;
	}

	public void setPracaVeraneio(boolean pracaVeraneio) {
		this.pracaVeraneio = pracaVeraneio;
	}

	public boolean isComplementarAutomatico() {
		return complementarAutomatico;
	}

	public void setComplementarAutomatico(boolean complementarAutomatico) {
		this.complementarAutomatico = complementarAutomatico;
	}

	public Integer getPercentualMaximoFixacao() {
		return percentualMaximoFixacao;
	}

	public void setPercentualMaximoFixacao(Integer percentualMaximoFixacao) {
		this.percentualMaximoFixacao = percentualMaximoFixacao;
	}

	public List<DistribuidorClassificacaoCotaVO> getListClassificacaoCota() {
		return listClassificacaoCota;
	}

	public void setListClassificacaoCota(
			List<DistribuidorClassificacaoCotaVO> listClassificacaoCota) {
		this.listClassificacaoCota = listClassificacaoCota;
	}

	public List<DistribuidorPercentualExcedenteVO> getListPercentualExcedente() {
		return listPercentualExcedente;
	}

	public void setListPercentualExcedente(
			List<DistribuidorPercentualExcedenteVO> listPercentualExcedente) {
		this.listPercentualExcedente = listPercentualExcedente;
	}

	public boolean isAceitaRecolhimentoParcialAtraso() {
		return aceitaRecolhimentoParcialAtraso;
	}

	public void setAceitaRecolhimentoParcialAtraso(
			boolean aceitaRecolhimentoParcialAtraso) {
		this.aceitaRecolhimentoParcialAtraso = aceitaRecolhimentoParcialAtraso;
	} 
	
	
}
