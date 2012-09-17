package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VisaoEstoqueDTO implements Serializable{

	private static final long serialVersionUID = -4171186893803302134L;
	
	@Export(label = "Estoque", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String estoque;
	
	@Export(label = "Produtos", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private Long produtos;
	
	@Export(label = "Exemplares", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private BigDecimal exemplares;
	
	private BigDecimal valor;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String valorFormatado;
	
	private String acao = "";

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

	public BigDecimal getExemplares() {
		return exemplares;
	}

	public void setExemplares(BigDecimal exemplares) {
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
