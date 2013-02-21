package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.cadastro.TipoAlteracao;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaBaseHistoricoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 7771337394074049477L;
	
	private Long idCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Tipo PDV", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String tipoPDV;
	
	@Export(label = "Bairro", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String bairro;
	
	@Export(label = "Cidade", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String cidade;
	
	@Export(label = "Gerador de Fluxo", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String geradorDeFluxo;
	
	@Export(label = "Área Influencia", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String areaInfluencia;
	
	@Export(label = "Faturamento Médio R$", alignment=Alignment.RIGHT, exhibitionOrder = 8)
	private String faturamentoFormatado;
	
	@Export(label = "Tipo Alteração", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private String tipoAlteracao;
	
	@Export(label = "Data Alteração", alignment=Alignment.CENTER, exhibitionOrder = 10)
	private String dataAlteracaoFormatado;
	
	private Date dataAlteracao;
	
	private BigDecimal faturamentoMedio;
	
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
	
	public String getDataAlteracaoFormatado() {
		return dataAlteracaoFormatado;
	}
	public void setDataAlteracaoFormatado(String dataAlteracaoFormatado) {
		this.dataAlteracaoFormatado = dataAlteracaoFormatado;
	}
	
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
		if(dataAlteracao != null){
			this.dataAlteracaoFormatado = DateUtil.formatarDataPTBR(dataAlteracao);
		}
	}
	public String getTipoAlteracao() {
		return tipoAlteracao;
	}
	public void setTipoAlteracao(TipoAlteracao tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao.getTipoAlteracao();
	}
	
	
	
	
}
