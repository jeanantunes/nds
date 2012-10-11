package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class MovimentoEstoqueCotaDTO implements Serializable {

	private static final long serialVersionUID = -1230165588204635338L;

	private Long idCota;
	private Long idProdEd;
	private String codigoProd;
	private Long edicaoProd;
	private String nomeProd;
	private Integer qtdeReparte;
	private List<RateioDTO> rateios;
	
	public MovimentoEstoqueCotaDTO() {
	}
	
	public MovimentoEstoqueCotaDTO(Long idCota, Long idProdEd,
			String codigoProd, Long edicaoProd, String nomeProd,
			Integer qtdeReparte, List<RateioDTO> rateios) {
		super();
		this.idCota = idCota;
		this.idProdEd = idProdEd;
		this.codigoProd = codigoProd;
		this.edicaoProd = edicaoProd;
		this.nomeProd = nomeProd;
		this.qtdeReparte = qtdeReparte;
		this.rateios = rateios;
	}

	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	public Long getIdProdEd() {
		return idProdEd;
	}
	public void setIdProdEd(Long idProdEd) {
		this.idProdEd = idProdEd;
	}
	public String getCodigoProd() {
		return codigoProd;
	}
	public void setCodigoProd(String codigoProd) {
		this.codigoProd = codigoProd;
	}
	public Long getEdicaoProd() {
		return edicaoProd;
	}
	public void setEdicaoProd(Long edicaoProd) {
		this.edicaoProd = edicaoProd;
	}
	public String getNomeProd() {
		return nomeProd;
	}
	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}
	public Integer getQtdeReparte() {
		return qtdeReparte;
	}
	public void setQtdeReparte(BigInteger qtdeReparte) {
		this.qtdeReparte = qtdeReparte.intValue();
	}
	public List<RateioDTO> getRateios() {
		return rateios;
	}
	public void setRateios(List<RateioDTO> rateios) {
		this.rateios = rateios;
	}
	
	
}
