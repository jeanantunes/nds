package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.export.ColumType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;

@Exportable
public class CotaSuspensaoDTO implements Serializable{

	private static final long serialVersionUID = 6095689901379670370L;
	
	private Long idCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT)
	@Footer(label = "Total de Cotas Sugeridas", type=FooterType.COUNT)
	private Integer numCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, widthPercent=30f)
	private String nome;
	
	@Export(label = "Valor Consignado TotalR$", alignment=Alignment.RIGHT)
	private String vlrConsignado;
	
	@Export(label = "Valor Reparte do Dia R$", alignment=Alignment.RIGHT)
	private String vlrReparte;
	
	@Export(label = "Divida Acumulada R$", alignment=Alignment.RIGHT, columnType=ColumType.MOEDA)
	@Footer(label = "Total R$", type=FooterType.SUM, columnType=ColumType.MOEDA)
	private BigDecimal dividaAcumulada;
	
	@Export(label = "Dias em Aberto", alignment=Alignment.CENTER, widthPercent=5f)
	private Integer diasAberto;

	@Export(label = "Faturamento R$", alignment=Alignment.RIGHT)
	private String faturamento;
	
	@Export(label = "% Dívida", alignment=Alignment.RIGHT, widthPercent=5f)
	private String percDivida;
		
	private Double doubleDividaAcumulada;
	
	private Double doubleConsignado;
		
	private Boolean selecionado;
	
	
	public CotaSuspensaoDTO(){
		
	}	
	
	public CotaSuspensaoDTO(Long idCota, Integer numCota, String nome, String vlrConsignado, String vlrReparte, String dividaAcumulada, Long diasAberto, Boolean selecionado) {
		super();
		this.idCota = idCota;
		this.numCota = numCota;
		this.nome = nome;
		this.vlrConsignado = vlrConsignado;
		this.vlrReparte = vlrReparte;
		//this.dividaAcumulada = dividaAcumulada;
		//this.diasAberto = diasAberto;
		this.selecionado = selecionado;
	}
		
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(BigInteger idCota) {
		this.idCota = idCota.longValue();
	}
	
//	public void setIdCota(Long idCota) {
//		this.idCota = idCota;
//	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if(nome==null)
			return;
		
		this.nome = nome;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		if(razaoSocial==null)
			return;
		
		this.nome = razaoSocial;
	}

	public String getVlrConsignado() {
		return vlrConsignado;
	}

	public void setVlrConsignado(BigDecimal vlrConsignado) {
		setDoubleConsignado(vlrConsignado == null ? 0 : vlrConsignado.doubleValue()); 
		this.vlrConsignado = CurrencyUtil.formatarValor(vlrConsignado);
	}

	public String getVlrReparte() {
		return vlrReparte;
	}

	public void setVlrReparte(BigDecimal vlrReparte) {
		this.vlrReparte = CurrencyUtil.formatarValor(vlrReparte == null ? BigDecimal.ZERO : vlrReparte);
	}

	public BigDecimal getDividaAcumulada() {
		return dividaAcumulada;
	}

	public void setDividaAcumulada(BigDecimal dividaAcumulada) {
		this.dividaAcumulada = dividaAcumulada == null ? dividaAcumulada : MathUtil.round(dividaAcumulada, 2);
	}

	public Integer getDiasAberto() {
		return diasAberto;
	}

	public void setDiasAberto(Integer diasAberto) {
		this.diasAberto = diasAberto;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getNumCota() {
		return numCota;
	}

	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}

	public void setDataAbertura(Date dataInicioDivida) {
		//this.diasAberto = dataInicioDivida==null? 0L : (((new Date()).getTime() - dataInicioDivida.getTime()) / 86400000L);
	}
	
	
	public Double getDoubleDividaAcumulada() {
		return doubleDividaAcumulada;
	}

	/**
	 * @return the faturamento
	 */
	public String getFaturamento() {
		return faturamento;
	}

	/**
	 * @param faturamento the faturamento to set
	 */
	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = CurrencyUtil.formatarValor(faturamento);
	}

	/**
	 * @return the percDivida
	 */
	public String getPercDivida() {
		return percDivida;
	}

	/**
	 * @param percDivida the percDivida to set
	 */
	public void setPercDivida(BigDecimal percDivida) {
		this.percDivida = CurrencyUtil.formatarValor(percDivida);
	}

	/**
	 * @return the doubleConsignado
	 */
	public Double getDoubleConsignado() {
		return doubleConsignado;
	}

	/**
	 * @param doubleConsignado the doubleConsignado to set
	 */
	public void setDoubleConsignado(Double doubleConsignado) {
		this.doubleConsignado = doubleConsignado;
	}

	public enum Ordenacao{
		
		COTA("cota"),
		NOME("name"),
		VLR_CONSIGNADO("vlrConsignado"),
		VLR_REPARTE("vlrReparte");
		
		private String nomeColuna;
		
		private Ordenacao(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
}
	
}
