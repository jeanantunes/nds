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
	private String cota;
	
	@Export(label="Nome", alignment=Export.Alignment.LEFT, exhibitionOrder=2)
	private String nome;
	
	@Export(label="Box Encalhe", alignment=Export.Alignment.LEFT, exhibitionOrder=3)
	private String boxEncalhe;
	
	@Export(label="Total R$", alignment=Export.Alignment.RIGHT, exhibitionOrder=4)
	private String total;
	
	@Export(label="Cobrança", alignment=Export.Alignment.LEFT, exhibitionOrder=5)
	private String cobranca;
	
	
	public AnaliticoEncalheVO()
	{}
	
	
	public AnaliticoEncalheVO(AnaliticoEncalheDTO dto) {
		this.setCota(dto.getNumeroCota().toString());
		this.setNome(dto.getNomeCota());
		this.setBoxEncalhe(dto.getBoxEncalhe());
		this.setTotal(CurrencyUtil.formatarValor(dto.getTotal()));
		this.setCobranca(dto.getStatusCobranca());
	}
	
	
	public String getCota() {
		return cota;
	}
	public void setCota(String cota) {
		this.cota = cota;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public String getCobranca() {
		return cobranca;
	}
	public void setCobranca(String cobranca) {
		this.cobranca = cobranca;
	}
}
