package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaSuspensaoDTO implements Serializable{

	private static final long serialVersionUID = 6095689901379670370L;
	
	private Long idCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT)
	private Integer numCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT)
	private String nome;
	
	@Export(label = "Valor Consignado TotalR$", alignment=Alignment.RIGHT)
	private String vlrConsignado;
	
	@Export(label = "Valor Reparte do Dia R$", alignment=Alignment.RIGHT)
	private String vlrReparte;
	
	@Export(label = "Divida Acumulada R$", alignment=Alignment.RIGHT)
	private String dividaAcumulada;
	
	@Export(label = "Faturamento R$", alignment=Alignment.RIGHT)
	private String faturamento;
	
	@Export(label = "% Dívida", alignment=Alignment.RIGHT)
	private String percDivida;
		
	private Double doubleDividaAcumulada;
	
	@Export(label = "Dias em Aberto", alignment=Alignment.CENTER)
	private Long diasAberto;
	
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
		this.dividaAcumulada = dividaAcumulada;
		this.diasAberto = diasAberto;
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
		setDoubleConsignado(vlrConsignado.doubleValue()); 
		this.vlrConsignado = CurrencyUtil.formatarValor(vlrConsignado);
	}

	public String getVlrReparte() {
		return vlrReparte;
	}

	public void setVlrReparte(BigDecimal vlrReparte) {
		this.vlrReparte = CurrencyUtil.formatarValor(vlrReparte);
	}

	public String getDividaAcumulada() {
		return dividaAcumulada;
	}

	public void setDividaAcumulada(BigDecimal dividaAcumulada) {
		this.doubleDividaAcumulada = dividaAcumulada.doubleValue();
		this.dividaAcumulada = CurrencyUtil.formatarValor(dividaAcumulada);
	}

	public Long getDiasAberto() {
		return diasAberto;
	}

	public void setDiasAberto(Long diasAberto) {
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
		this.diasAberto = dataInicioDivida==null? 0L : (((new Date()).getTime() - dataInicioDivida.getTime()) / 86400000L);
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
	public void setFaturamento(String faturamento) {
		this.faturamento = faturamento;
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
	public void setPercDivida(String percDivida) {
		this.percDivida = percDivida;
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
