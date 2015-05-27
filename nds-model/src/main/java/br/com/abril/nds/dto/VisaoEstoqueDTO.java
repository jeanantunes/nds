package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VisaoEstoqueDTO implements Serializable{

	private static final long serialVersionUID = -4171186893803302134L;
	
	private String tipoEstoque;
	
	@Export(label = "Estoque", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String estoque;
	
	@Export(label = "Produtos", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private Long produtos;
	
	@Export(label = "Exemplares", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private BigInteger exemplares;
	
	private BigDecimal valor;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String valorFormatado;
	
	private String acao = "";

	
	
	public String getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(String tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

	public String getEstoque() {
		return estoque;
	}

	public void setEstoque(String estoque) {
		this.estoque = estoque;
	}

	public Long getProdutos() {
		return produtos;
	}

	public void setProdutos(Long produtos) {
		this.produtos = produtos;
	}

	public BigInteger getExemplares() {
		return exemplares;
	}

	public void setExemplares(BigInteger exemplares) {
		this.exemplares = exemplares;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
		this.valorFormatado = CurrencyUtil.formatarValor(this.valor);
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	public String getValorFormatado() {
		return this.valorFormatado;
	}
}
