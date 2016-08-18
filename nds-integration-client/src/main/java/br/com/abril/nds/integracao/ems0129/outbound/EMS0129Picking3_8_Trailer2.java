package br.com.abril.nds.integracao.ems0129.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking3_8_Trailer2 implements Serializable {
	
	private static final long serialVersionUID = -1715610039995884874L;
	
	private String identificadorLinha;

	private String codigoCota;
	
	private String sequencia;
	
	private String produto;
	
	private String edicao;
	
	private String nome;
	
	private String preco;
	
	private String precoDesconto;
	
	private String desconto;
	
	private String quantidade;
	

	public EMS0129Picking3_8_Trailer2(String identificadorLinha, String codigoCota, String sequencia, String produto, String edicao, String nome,
			String preco, String precoDesconto, String desconto, String quantidade) {
		this.identificadorLinha = identificadorLinha;
		this.codigoCota = codigoCota;
		this.sequencia = sequencia;
		this.produto = produto;
		this.edicao = edicao;
		this.nome = nome;
		this.preco = preco;
		this.precoDesconto = precoDesconto;
		this.desconto = desconto;
		this.quantidade = quantidade;
	}
	
	@Field(offset = 1, length = 2)
	public String getIdentificadorLinha() {
		return identificadorLinha;
	}

	@Field(offset = 3, length = 5)
	public String getCodigoCota() {
		return codigoCota;
	}

	@Field(offset = 8, length = 4)
	public String getSequencia() {
		return sequencia;
	}

	@Field(offset = 12, length = 9)
	public String getProduto() {
		return produto;
	}

	@Field(offset = 21, length = 5)
	public String getEdicao() {
		return edicao;
	}

	@Field(offset = 26, length = 21)
	public String getNome() {
		return nome;
	}

	@Field(offset = 47, length = 11)
	public String getPreco() {
		return preco;
	}

	@Field(offset = 58, length = 11)
	public String getPrecoDesconto() {
		return precoDesconto;
	}
	
	@Field(offset = 69, length = 6)
	public String getDesconto() {
		return desconto;
	}

	@Field(offset = 75, length = 7)
	public String getQuantidade() {
		return quantidade;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setSequencia(String sequencia) {
		this.sequencia = sequencia;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public void setIdentificadorLinha(String identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}
	
	
	
	
}
