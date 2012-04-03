package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class CotaAusenteDTO implements Serializable{
	
	private static final long serialVersionUID = -5403191577161993585L;
	
	private Long idCotaAusente;
	
	private String data;
	
	private String box;
	
	private Integer cota;
	
	private String nome;
	
	private String valorNe;
	
	public CotaAusenteDTO(){
		
	}
	
	public String getData() {
		return data;
	}
	public void setData(Date data) {		
		this.data = DateUtil.formatarData(data, Constantes.DATE_PATTERN_PT_BR);
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public Integer getCota() {
		return cota;
	}
	public void setCota(Integer cota) {
		this.cota = cota;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getValorNe() {
		return valorNe;
	}
	public void setValorNe(BigDecimal valorNe) {
		
		if(valorNe == null) {
			this.valorNe = CurrencyUtil.formatarValor(0);
			return;
		}
		this.valorNe = CurrencyUtil.formatarValor(valorNe);
	}

	public Long getIdCotaAusente() {
		return idCotaAusente;
	}

	public void setIdCotaAusente(BigInteger idCotaAusente) {
		if(idCotaAusente == null)  {
			this.idCotaAusente  = null;
			return;
		}
		this.idCotaAusente = idCotaAusente.longValue();
	}
	
}
