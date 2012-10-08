package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliticoEncalheVO implements Serializable {

	private static final long serialVersionUID = -6737419809956273600L;
	
	@Export(label="Cota", alignment=Export.Alignment.LEFT, exhibitionOrder=1)
	private String numeroCota;
	
	@Export(label="Nome", alignment=Export.Alignment.LEFT, exhibitionOrder=2)
	private String nomeCota;
	
	@Export(label="Box Encalhe", alignment=Export.Alignment.LEFT, exhibitionOrder=3)
	private String boxEncalhe;
	
	@Export(label="Total R$", alignment=Export.Alignment.RIGHT, exhibitionOrder=4)
	private String total;
	
	@Export(label="Cobrança", alignment=Export.Alignment.LEFT, exhibitionOrder=5)
	private String statusCobranca;
	
	
	public AnaliticoEncalheVO()
	{}
	
	
	public AnaliticoEncalheVO(AnaliticoEncalheDTO dto) {
		this.setNumeroCota(dto.getNumeroCota().toString());
		this.setNomeCota(dto.getNomeCota());
		this.setBoxEncalhe(dto.getBoxEncalhe());
		this.setTotal(CurrencyUtil.formatarValor(dto.getTotal()));
		this.setStatusCobranca(dto.getStatusCobranca().toString());
	}


	public String getNumeroCota() {
		return numeroCota;
	}


	public void setNumeroCota(String numeroCota) {
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
	
	

}
