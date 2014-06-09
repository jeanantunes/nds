package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/*
 *  @author Samuel Mendes
 *  
 */
@Exportable
public class AreaInfluenciaGeradorFluxoDTO implements Serializable {

	private static final long serialVersionUID = -3481326311215518825L;

	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private SituacaoCadastro statusCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String nomeCota;
	
	@Export(label = "Tipo PDV", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String tipoPdv;
	
	@Export(label = "Bairro", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String bairro;
	
	@Export(label = "Cidade", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String cidade;
	
	private BigDecimal faturamento;

	@Export(label = "Faturamento R$", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String faturamentoFormatado;
	
	@Export(label = "Área de Influência", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private String areaInfluencia;
	
	@Export(label = "Gerador 1", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String geradorFluxoPrincipal;
	
	//TODO Retirar após o ajuste na EMS de geradorFluxoSecundario.
	@Export(label = "Gerador 2", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String geradorFluxoSecundario = "";

	public AreaInfluenciaGeradorFluxoDTO(){
		
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getTipoPdv() {
		return tipoPdv;
	}

	public void setTipoPdv(String tipoPdv) {
		if(tipoPdv == null){
			this.tipoPdv = "";
		}else{
			this.tipoPdv = tipoPdv;			
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

	public BigDecimal getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(BigDecimal faturamento) {		
		this.faturamento = faturamento;
		this.setFaturamentoFormatado(CurrencyUtil.formatarValor(faturamento));
	}

	public String getFaturamentoFormatado() {
		return faturamentoFormatado;
	}

	public void setFaturamentoFormatado(String faturamentoFormatado) {
		if(faturamentoFormatado == null){
			this.faturamentoFormatado = "";
		}else{
			this.faturamentoFormatado = faturamentoFormatado;			
		}
	}

	public String getGeradorFluxoPrincipal() {
		return geradorFluxoPrincipal;
	}

	public void setGeradorFluxoPrincipal(String geradorFluxoPrincipal) {
		if(geradorFluxoPrincipal == null){
			this.geradorFluxoPrincipal = "";
		}else{
			this.geradorFluxoPrincipal = geradorFluxoPrincipal;						
		}
	}

	public String getGeradorFluxoSecundario() {
		return geradorFluxoSecundario;
	}

	public void setGeradorFluxoSecundario(String geradorFluxoSecundario) {
		if(geradorFluxoSecundario == null){
			this.geradorFluxoSecundario = "";
		}else{
			this.geradorFluxoSecundario = geradorFluxoSecundario;									
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}