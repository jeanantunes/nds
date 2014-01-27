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
	private Long numeroEstudo;
	
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		CODIGO("codigoICD"),
		EDICAO("numeroEdicao"),
		PRODUTO("nomeProduto"),
		CLASSIFICACAO("tipoClassificacaoProdutoDescricao"),
		PERIODO("periodo"),
		PRECO("preco"),
		STATUS("status"),
		REPARTE("reparteDistribuido"),
		VENDA("venda"),
		ABRANGENCIA("percentualAbrangencia"),
		DATA_LANCAMENTO("dataLcto"),
		DATA_RELANCAMENTO("dataRcto"),
		ALGORITMO("algoritmo"),
		REPARTE_MINIMO("reparteMinimo"),
		ESTUDO("estudo"),
		USUARIO("nomeUsuario"),
		DATA("dataAlteracao"),
		HORA("hora");
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		@Override
		public String toString() {
			
			return this.descricao;
		}
	}
	
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
	public Long getNumeroEstudo() {
		return numeroEstudo;
	}
	public void setNumeroEstudo(Long numeroEstudo) {
		this.numeroEstudo = numeroEstudo;
	}
	public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}
	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}
}
