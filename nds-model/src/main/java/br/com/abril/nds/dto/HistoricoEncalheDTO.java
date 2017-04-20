package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class HistoricoEncalheDTO implements Serializable {

	private static final long serialVersionUID = -6737419809956273600L;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private String boxEncalhe;
	
	private String total;
	
	private String statusCobranca;
	
	private String usuario;
	
	private String inicio;
	
	private String fim;
	
	private Long id;
	
	private BigDecimal valorEncalhe;
	
	private String valorEncalheFormatado;
	
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

	public String getBoxEncalhe() {
		return boxEncalhe;
	}

	public void setBoxEncalhe(String boxEncalhe) {
		this.boxEncalhe = boxEncalhe;
	}
	
	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public String getStatusCobranca() {
		return statusCobranca;
	}


	public void setStatusCobranca(String statusCobranca) {
		this.statusCobranca = statusCobranca;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getInicio() {
		return inicio;
	}
	
	public void setInicio(Date inicio) {
		this.inicio = DateUtil.formatarHoraMinuto(inicio);
	}
	
	public String getFim() {
		return fim;
	}
	
	public void setFim(Date fim) {
		this.fim = DateUtil.formatarHoraMinuto(fim);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	public void setValorEncalhe(BigDecimal valorEncalhe) {
		
		this.valorEncalheFormatado = CurrencyUtil.formatarValor(valorEncalhe);
		
		this.valorEncalhe = valorEncalhe;
	}

	public String getValorEncalheFormatado() {
		return valorEncalheFormatado;
	}
}