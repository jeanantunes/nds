package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FechamentoCEIntegracaoDTO implements Serializable {
	
	private static final long serialVersionUID = -6711767359292325571L;

	@Export(label = "Sequencial", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private Integer sequencial;
	
	@Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String codigo;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String produto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private Long edicao;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger reparte;
	
	@Export(label = "Encalhe", alignment = Alignment.CENTER, exhibitionOrder = 7)
	private BigInteger encalhe;

	@Export(label = "Venda", alignment = Alignment.CENTER, exhibitionOrder = 8)
	private BigInteger venda;
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, exhibitionOrder = 9)
	private BigDecimal precoCapa;
	
	@Export(label = "Valor Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 10)
	private String valorVendaFormatado;
	
	private Boolean tipo;
	
	private BigDecimal desconto;
	
	private String tipoFormatado;
	
	@Export(label = "Tipo", alignment = Alignment.CENTER, exhibitionOrder = 5)
	public String getTipoFormatado() {
		return tipoFormatado;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigInteger getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	
	public Integer getSequencial() {
		return sequencial;
	}

	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public String getvalorVendaFormatado() {
		return valorVendaFormatado;
	}

	public void setvalorVendaFormatado(String valorVendaFormatado) {
		this.valorVendaFormatado = valorVendaFormatado;
	}

	public Boolean getTipo() {
		return tipo;
	}
	
	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
		if(tipo){
			tipoFormatado = "Final";
		}else{
			tipoFormatado = "Parcial";
		}
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
}
