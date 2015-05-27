package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.util.export.Exportable;


@Exportable
public class ProdutoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3332013714066228911L;
	private String numero;
	private String label;
	private String razaoSocial;
	private String nomeFantasia;
	private String tipoSegmentoProduto;
	private Produto produto;
	
	public ProdutoVO() {
		// TODO Auto-generated constructor stub
	}
	
	public ProdutoVO(String numero, String label, Produto produto) {
		this.numero = numero;
		this.label = label;
		this.produto = produto;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTipoSegmentoProduto() {
		return tipoSegmentoProduto;
	}

	public void setTipoSegmentoProduto(String tipoSegmentoProduto) {
		this.tipoSegmentoProduto = tipoSegmentoProduto;
	}
	
	
}
