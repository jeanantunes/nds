package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaBaseDTO implements Serializable {

	private static final long serialVersionUID = 7771337394074049477L;
	
	private Long idCota;	
	private String nomeCota;
	private String tipoPDV;
	private String bairro;
	private String cidade;
	private String geradorDeFluxo;
	private String areaInfluencia;
	private BigDecimal faturamentoMedio;
	private Integer numeroCota;
	
	private String faturamentoFormatado;
	
	
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
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
	public BigDecimal getFaturamentoMedio() {
		return faturamentoMedio;
	}
	public void setFaturamentoMedio(BigDecimal faturamentoMedio) {
		this.faturamentoMedio = faturamentoMedio;
		if(faturamentoMedio != null){
			this.faturamentoFormatado = CurrencyUtil.formatarValor(faturamentoMedio);
		}
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getFaturamentoFormatado() {
		return faturamentoFormatado;
	}
	
	
	
	
}