package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaAtendidaTransportadorVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6706571333704887161L;
	
	@Export(label = "Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome")
	private String nomeCota;
	
	@Export(label = "Box")
	private String box;
	
	@Export(label = "Roteiro")
	private String roteiro;
	
	@Export(label = "Rota")
	private String rota;
	
	@Export(label = "Valor R$ / %")
	private BigDecimal valor;
	
	public CotaAtendidaTransportadorVO(){}
	
	public CotaAtendidaTransportadorVO(Integer numeroCota, String nomeCota, String box,
			String roteiro, String rota, BigDecimal valor){
		
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.box = box;
		this.roteiro = roteiro;
		this.rota = rota;
		this.valor = valor;
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

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public String getRoteiro() {
		return roteiro;
	}

	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}

	public String getRota() {
		return rota;
	}

	public void setRota(String rota) {
		this.rota = rota;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}