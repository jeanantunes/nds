package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ProdutoDistribuicaoVO  implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;
	
	private BigInteger id;
	
	@Export(label="Codigo", exhibitionOrder = 0)
	private String codigoProduto;
	
	@Export(label="Produto", exhibitionOrder = 1)
	private String nomeProduto;
	
	@Export(label="Edição", exhibitionOrder = 2)
	private BigInteger numeroEdicao;

	@Export(label="Periodo", exhibitionOrder = 3)
	private String periodo;
	
	@Export(label="Preço RS", exhibitionOrder = 4)
	private BigDecimal precoVenda;

	@Export(label="Clas.", exhibitionOrder = 5)
	private String classificacao;
	
	@Export(label="Pct. Padrão", exhibitionOrder = 6)
	private Integer pctPadrao;
	
	@Export(label="Fornecedor", exhibitionOrder = 7)
	private String nomeFornecedor;
	
	@Export(label="Juram.", exhibitionOrder = 8)
	private Integer juram;
	
	@Export(label="Suplem..", exhibitionOrder = 9)
	private BigDecimal suplem;
	
	@Export(label="Lancto.", exhibitionOrder = 10)
	private Integer lancto;
	
	@Export(label="Promo.", exhibitionOrder = 11)
	private BigDecimal promo;
	
	@Export(label="Lib.", exhibitionOrder = 12)
	private String liberado;

	@Export(label="Estudo", exhibitionOrder = 13)
	private BigInteger idEstudo;
	
	public BigInteger getId() {
		return id;
	}

	public String getLiberado() {
		return liberado;
	}

	public void setLiberado(String liberado) {
		this.liberado = liberado;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Integer getPctPadrao() {
		return pctPadrao;
	}

	public void setPctPadrao(Integer pctPadrao) {
		this.pctPadrao = pctPadrao;
	}

	public Integer getJuram() {
		return juram;
	}

	public void setJuram(Integer juram) {
		this.juram = juram;
	}

	public BigDecimal getSuplem() {
		return suplem;
	}

	public void setSuplem(BigDecimal suplem) {
		this.suplem = suplem;
	}

	public Integer getLancto() {
		return lancto;
	}

	public void setLancto(Integer lancto) {
		this.lancto = lancto;
	}

	public BigDecimal getPromo() {
		return promo;
	}

	public void setPromo(BigDecimal promo) {
		this.promo = promo;
	}

	public BigInteger getIdEstudo() {
		return idEstudo;
	}

	public void setIdEstudo(BigInteger idEstudo) {
		this.idEstudo = idEstudo;
	}
	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoDistribuicaoVO other = (ProdutoDistribuicaoVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}