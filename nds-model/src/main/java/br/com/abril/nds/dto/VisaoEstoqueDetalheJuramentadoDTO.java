package br.com.abril.nds.dto;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VisaoEstoqueDetalheJuramentadoDTO extends VisaoEstoqueDetalheDTO {

	private static final long serialVersionUID = -3060314962002173216L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer cota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nome;
	
	
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
}
