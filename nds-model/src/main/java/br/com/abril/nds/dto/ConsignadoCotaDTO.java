package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;
@Exportable
public class ConsignadoCotaDTO implements Serializable{
	
	private static final long serialVersionUID = 2186060384671120600L;
	
	@Export(label="Código")
	private String codigoProduto;
	
	@Export(label="Produto")		
	private String nomeProduto;
	
	@Export(label="Edição")
	private Long numeroEdicao;
	
	@Export(label="Preço Capa R$" , alignment= Alignment.RIGHT )
	private BigDecimal precoCapa;
	
	@Export(label="Preço c/ Desc. R$" , alignment= Alignment.RIGHT )
	private BigDecimal precoComDesconto;
	
	@Export(label="Reparte Sugerido")
	private BigDecimal reparteSugerido;
	
	@Export(label="Reparte Final")
	private BigDecimal reparteFinal;
	
	@Export(label="Diferenca")
	private BigDecimal diferenca;
	
	@Export(label="Motivo")
	private TipoDiferenca motivo;
	
	@Export(label="Fornecedor")
	private String nomeFornecedor;
	
	@Export(label="Total")
	private BigDecimal total;
	
	private BigDecimal desconto;

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

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public BigDecimal getReparteSugerido() {
		return reparteSugerido;
	}

	public void setReparteSugerido(BigDecimal reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}

	public BigDecimal getReparteFinal() {
		return reparteFinal;
	}

	public void setReparteFinal(BigDecimal reparteFinal) {
		this.reparteFinal = reparteFinal;
	}

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public TipoDiferenca getMotivo() {
		return motivo;
	}

	public void setMotivo(TipoDiferenca motivo) {
		this.motivo = motivo;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
}
