package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseEstudoFiltroExportPDFDTO {
	
	@Export(label="Codigo produto", exhibitionOrder=0)
	private String codigoProduto;
	
	@Export(label="Edição", exhibitionOrder=1)
	private Long numeroEdicao;
	
	@Export(label="Nome produto", exhibitionOrder=2)
	private String nomeProduto;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	
}
