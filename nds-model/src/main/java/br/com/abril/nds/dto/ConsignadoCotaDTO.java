package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.util.export.ColumType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
@Exportable
public class ConsignadoCotaDTO implements Serializable{
	
	private static final long serialVersionUID = 2186060384671120600L;
	
	@Export(label="SM", fontSize=9, widthPercent=7)
	private String sequencia;
	
	@Export(label="Código", fontSize=9)
	private String codigoProduto;
	
	@Export(label="Produto", fontSize=9, widthPercent=30)		
	private String nomeProduto;
	
	@Export(label="Edição", fontSize=9)
	private Long numeroEdicao;
	
	@Export(label="Preço Capa R$" , alignment= Alignment.RIGHT, columnType = ColumType.MOEDA, fontSize=9)
	private BigDecimal precoCapa;
	
	@Export(label="Preço c/ Desc. R$" , alignment= Alignment.RIGHT, columnType = ColumType.MOEDA_QUATRO_CASAS, fontSize=9)
	private BigDecimal precoComDesconto;
	
	@Export(label="Reparte Sugerido", fontSize=9)
	private BigInteger reparteSugerido;
	
	@Export(label="Reparte Final", fontSize=9)
	private BigInteger reparteFinal;
	
	@Export(label="Diferenca", fontSize=9, widthPercent=7)
	private BigInteger diferenca;
	
	@Export(label="Motivo", fontSize=9)
	private TipoDiferenca motivo;
	
	@Export(label="Motivo Desc.", fontSize=9)
	private String motivoTexto;
	
	@Export(label="Fornecedor", fontSize=9, widthPercent=7)
	private String nomeFornecedor;
	
	@Export(label="Total", columnType = ColumType.MOEDA_QUATRO_CASAS, fontSize=9)
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
		this.precoComDesconto = precoComDesconto != null ? precoComDesconto.setScale(4, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
	}

	public BigInteger getReparteSugerido() {
		return reparteSugerido;
	}

	public void setReparteSugerido(BigInteger reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}

	public BigInteger getReparteFinal() {
		return reparteFinal;
	}

	public void setReparteFinal(BigInteger reparteFinal) {
		this.reparteFinal = reparteFinal;
	}

	public BigInteger getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigInteger diferenca) {
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
		this.total = total != null ? total.setScale(4, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
	}

	public TipoDiferenca getMotivo() {
		return motivo;
	}

	public void setMotivo(TipoDiferenca motivo) {
		this.motivo = motivo;
	}
	
	public String getMotivoText() {
		return motivoTexto;
	}

	public void setMotivoText(String motivoTexto) {
		this.motivoTexto = motivoTexto;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public String getSequencia() {
		return sequencia;
	}

	public void setSequencia(String sequencia) {
		this.sequencia = sequencia != null ? sequencia : "Postergado";
	}
	
}