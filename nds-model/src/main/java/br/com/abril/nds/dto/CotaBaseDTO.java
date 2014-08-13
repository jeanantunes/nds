package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaBaseDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 7771337394074049477L;
	
	private Long idCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Tipo PDV", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String tipoPDV;
	
	@Export(label = "Situação", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String situacao;

	@Export(label = "Início", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String dtInicioFormatado;
	
	@Export(label = "Fim", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String dtFinalFormatado;
	
	@Export(label = "Dias Faltantes", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String diasRestantes;
	
	private String bairro;
	private String cidade;
	private String geradorDeFluxo;
	private String areaInfluencia;
	private BigDecimal faturamentoMedio;
	
	private String faturamentoFormatado;
	
	private String indiceAjuste;
	
	private Date dtInicio;
	private Date dtFinal;
	
	private String equivalente01 = "";
	private String equivalente02 = "";
	private String equivalente03 = "";
	
	
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
		if(tipoPDV == null){
			this.tipoPDV = "";
		}else{
			this.tipoPDV = tipoPDV;			
		}
	}
	
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		if(bairro == null){
			this.bairro = "";
		}else{
			this.bairro = bairro;		
		}
	}
	
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		if(cidade == null){
			this.cidade = "";
		}else{
			this.cidade = cidade;		
		}
	}
	public String getGeradorDeFluxo() {
		return geradorDeFluxo;
	}
	public void setGeradorDeFluxo(String geradorDeFluxo) {
		if(geradorDeFluxo == null){
			this.geradorDeFluxo = "";
		}else{
			this.geradorDeFluxo = geradorDeFluxo;		
		}
	}
	
	public String getAreaInfluencia() {
		return areaInfluencia;
	}
	public void setAreaInfluencia(String areaInfluencia) {
		if(areaInfluencia == null){
			this.areaInfluencia = "";
		}else{
			this.areaInfluencia = areaInfluencia;		
		}
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
	public String getFaturamentoFormatado() {
		return faturamentoFormatado;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	
	public String getIndiceAjuste() {
		return indiceAjuste;
	}
	public void setIndiceAjuste(BigDecimal indiceAjuste) {
		this.indiceAjuste = CurrencyUtil.formatarValor(indiceAjuste);
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {		
		this.situacao = situacao;
	}
	
	public Date getDtInicio() {
		return dtInicio;
	}
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
		if(dtInicio != null){
			this.dtInicioFormatado =  DateUtil.formatarDataPTBR(dtInicio);
		}		
	}
	
	public Date getDtFinal() {
		return dtFinal;
	}
	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
		if(dtFinal != null){
			this.dtFinalFormatado =  DateUtil.formatarDataPTBR(dtFinal);
		}
	}
	
	public String getDiasRestantes() {
		return diasRestantes;
	}
	public void setDiasRestantes(String diasRestantes) {
		if(diasRestantes == null){
			this.diasRestantes = "";	
		}else{
			this.diasRestantes = diasRestantes;			
		}
	}
	
	public String getDtInicioFormatado() {
		return dtInicioFormatado;
	}
	public void setDtInicioFormatado(String dtInicioFormatado) {
		this.dtInicioFormatado = dtInicioFormatado;
	}
	public String getDtFinalFormatado() {
		return dtFinalFormatado;
	}
	public void setDtFinalFormatado(String dtFinalFormatado) {
		this.dtFinalFormatado = dtFinalFormatado;
	}
	public String getEquivalente01() {
		return equivalente01;
	}
	public void setEquivalente01(String equivalente01) {
		this.equivalente01 = equivalente01;
	}
	public String getEquivalente02() {
		return equivalente02;
	}
	public void setEquivalente02(String equivalente02) {
		this.equivalente02 = equivalente02;
	}
	public String getEquivalente03() {
		return equivalente03;
	}
	public void setEquivalente03(String equivalente03) {		
		this.equivalente03 = equivalente03;
	}
	
}
