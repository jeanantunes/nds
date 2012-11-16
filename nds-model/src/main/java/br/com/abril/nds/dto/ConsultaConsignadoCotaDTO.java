package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaConsignadoCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 3076443242075398273L;
	
	@Export(label = "Código" , alignment= Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto" , alignment= Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	private Long idProdutoEdicao;
	
	@Export(label = "Edição" , alignment= Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	private Long cotaId;
	
	private Integer numeroCota;
	
	private Long fornecedorId;
	
	@Export(label = "Fornecedor" , alignment= Alignment.LEFT, exhibitionOrder = 4)
	private String nomeFornecedor;
	
	@Export(label = "Dt. Lancto" , alignment= Alignment.CENTER, exhibitionOrder = 5)
	private String dataLancamento;
	
	private BigDecimal precoCapa;
	private BigDecimal precoDesconto;
	private BigDecimal desconto;
	
	@Export(label = "Reparte" , alignment= Alignment.CENTER, exhibitionOrder = 8)
	private BigInteger reparte;
	private BigDecimal total;
	private BigDecimal totalDesconto;
	
	private String precoCapaFormatado;
	private String precoDescontoFormatado;
	private String totalFormatado;
	private String totalDescontoFormatado;
	
	public ConsultaConsignadoCotaDTO() {}

	public ConsultaConsignadoCotaDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String nomeFornecedor, String dataLancamento,
			BigDecimal precoCapa, BigDecimal precoDesconto, BigInteger reparte,
			BigDecimal total, BigDecimal totalDesconto) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.nomeFornecedor = nomeFornecedor;
		this.dataLancamento = dataLancamento;
		this.precoCapa = precoCapa;
		this.precoDesconto = precoDesconto;
		this.reparte = reparte;
		this.total = total;
		this.totalDesconto = totalDesconto;
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

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getCotaId() {
		return cotaId;
	}

	public void setCotaId(Long cotaId) {
		this.cotaId = cotaId;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public Long getFornecedorId() {
		return fornecedorId;
	}

	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarData(dataLancamento, Constantes.DATE_PATTERN_PT_BR);		
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		if (precoCapa != null) {
			precoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
		}
	}
	
	@Export(label = "Preço Capa R$", alignment= Alignment.RIGHT, exhibitionOrder = 6)
	public String getPrecoCapaFormatado(){
		return precoCapaFormatado;
	}

	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
		if(precoDesconto != null){
			precoDescontoFormatado = CurrencyUtil.formatarValor(precoDesconto);
		}
	}
	
	@Export(label = "Preço Desc R$", alignment= Alignment.RIGHT, exhibitionOrder = 7)
	public String getPrecoDescontoFormatado(){
		return precoDescontoFormatado;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
		if(total != null){
			totalFormatado = CurrencyUtil.formatarValor(total);
		}
	}
	
	@Export(label = "Total $", alignment= Alignment.RIGHT, exhibitionOrder = 9)
	public String getTotalFormatado(){
		return totalFormatado;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
		if(totalDesconto != null){
			totalDescontoFormatado = CurrencyUtil.formatarValor(totalDesconto);
		}
	}
	
	@Export(label = "Total Desc $", alignment= Alignment.RIGHT, exhibitionOrder = 10)
	public String getTotalDescontoFormatado(){
		return totalDescontoFormatado;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	
}
