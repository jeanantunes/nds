package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupCadastroParcialDTO implements Serializable {

	private static final long serialVersionUID = 2182164575358271226L;

	@Export(label = "Código do produto", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Nome do produto", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Numero da edição do produto", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	
	public ConsultaFollowupCadastroParcialDTO() {}


	public ConsultaFollowupCadastroParcialDTO(String codigoProduto,
			String nomeProduto, Long numeroEdicao) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
	}


	public String getCodigoProduto() {
		return codigoProduto;
	}


	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}


	public String getNomeProduto() {
		return nomeProduto;
	}


	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}


	public Long getNumeroEdicao() {
		return numeroEdicao;
	}


	public void setNumeroEdicaoo(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	
}
