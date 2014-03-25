package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCotaBaseDTO implements Serializable {

	private static final long serialVersionUID = 2433782322485644813L;
	
	private Long idCota;
	private Integer numeroCota;	
	private String nomeCota;
	private String tipoPDV;
	private String bairro;
	private String cidade;
	private String geradorDeFluxo;
	private String areaInfluencia;
	private String faturamentoMedio;
	
	private String diasRestantes;
	
	private Date dataInicial;
	private Date dataFinal;
	
	private String dataInicialFormatado;
	private String dataFinalFormatado;
	
	private TipoDistribuicaoCota tpDistribCota;
	
	public Long getIdCota() {
		return idCota;
	}


	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}


	public Integer getNumeroCota() {
		return numeroCota;
	}


	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}


	public String getNomeCota() {
		return nomeCota;
	}


	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}


	public String getTipoPDV() {
			return tipoPDV;
	}


	public void setTipoPDV(String tipoPDV) {
		this.tipoPDV = tipoPDV;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getCidade() {
		return cidade;
	}


	public void setCidade(String cidade) {
		this.cidade = cidade;
	}


	public String getGeradorDeFluxo() {
		return geradorDeFluxo;
	}


	public void setGeradorDeFluxo(String geradorDeFluxo) {
		this.geradorDeFluxo = geradorDeFluxo;
	}


	public String getAreaInfluencia() {
		return areaInfluencia;
	}


	public void setAreaInfluencia(String areaInfluencia) {
		this.areaInfluencia = areaInfluencia;
	}


	public String getFaturamentoMedio() {
		return faturamentoMedio;
	}


	public void setFaturamentoMedio(BigDecimal faturamentoMedio) {
		if(faturamentoMedio != null){
			this.faturamentoMedio = CurrencyUtil.formatarValor(faturamentoMedio);			
		}
	}


	public Date getDataInicial() {
		return dataInicial;
	}


	public void setDataInicial(Date dataInicial) {
		this.dataInicial =  dataInicial;
		if(dataInicial != null){
			this.dataInicialFormatado =  DateUtil.formatarDataPTBR(dataInicial);			
		}
	}


	public Date getDataFinal() {
		return dataFinal;
	}


	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
		if(dataFinal != null){
			this.dataFinalFormatado = DateUtil.formatarDataPTBR(dataFinal);
			
		}
	}


	public String getDataInicialFormatado() {
		return dataInicialFormatado;
	}

	public String getDataFinalFormatado() {
		return dataFinalFormatado;
	}


	public String getDiasRestantes() {
		return diasRestantes;
	}


	public void setDiasRestantes(String diasRestantes) {
		this.diasRestantes = diasRestantes;
	}

	public TipoDistribuicaoCota getTpDistribCota() {
		return tpDistribCota;
	}

	public void setTpDistribCota(TipoDistribuicaoCota tpDistribCota) {
		this.tpDistribCota = tpDistribCota;
	}

}
