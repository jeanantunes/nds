package br.com.abril.nds.dto.filtro;


public class FiltroAnaliseEstudoDTO extends FiltroDTO {

	private static final long serialVersionUID = -7460175679601254408L;

	private Long numeroEstudo;
	private Long numeroEdicao;
	private Long idTipoClassificacaoProduto;
	private String codigoProduto;
	private String nome;
	
	public Long getNumeroEstudo() {
		return numeroEstudo;
	}

	public void setNumeroEstudo(Long numeroEstudo) {
		this.numeroEstudo = numeroEstudo;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getIdTipoClassificacaoProduto() {
		return idTipoClassificacaoProduto;
	}

	public void setIdTipoClassificacaoProduto(Long idTipoClassificacaoProduto) {
		this.idTipoClassificacaoProduto = idTipoClassificacaoProduto;
	}

	
}
