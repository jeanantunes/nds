package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroInformacoesProdutoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 3715540687164148151L;
	
	private String codProduto;
	private String nomeProduto;
	private Long idTipoClassificacaoProd;
	private Long numeroEdicao;
	
	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getIdTipoClassificacaoProd() {
		return idTipoClassificacaoProd;
	}
	public void setIdTipoClassificacaoProd(Long idTipoClassificacaoProd) {
		this.idTipoClassificacaoProd = idTipoClassificacaoProd;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	
}
