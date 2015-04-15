package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.ColumType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;

@Exportable
public class ConsultaConsignadoCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 3076443242075398273L;
	
	@Export(label = "Código" , alignment= Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto" , alignment= Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	private Long produtoEdicaoId;
	
	@Export(label = "Edição" , alignment= Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	private Long cotaId;
	
	private Integer numeroCota;
	
	private Long fornecedorId;
	
	@Export(label = "Fornecedor" , alignment= Alignment.LEFT, exhibitionOrder = 4)
	private String nomeFornecedor;
	
	@Export(label = "Dt. Lancto" , alignment= Alignment.CENTER, exhibitionOrder = 5)
	private String dataLancamento;
	
	@Export(label = "Dt. Recolhimento" , alignment= Alignment.CENTER, exhibitionOrder = 6)
	private String dataRecolhimento;
	
	private BigDecimal precoCapa;
	private BigDecimal precoDesconto;
	private BigDecimal desconto;
	
	@Export(label = "Reparte" , alignment= Alignment.CENTER, exhibitionOrder = 9, columnType = ColumType.INTEGER)
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

	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}

	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
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

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarDataPTBR(dataRecolhimento);
	}

	@Export(label = "Preço Capa R$", alignment= Alignment.RIGHT, exhibitionOrder = 7, columnType = ColumType.MOEDA)
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		if (precoCapa != null) {
			precoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
		}
	}
	
	
	public String getPrecoCapaFormatado(){
		return precoCapaFormatado;
	}

	@Export(label = "Preço Desc R$", alignment= Alignment.RIGHT, exhibitionOrder = 8, columnType = ColumType.MOEDA_QUATRO_CASAS)
	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
		if(precoDesconto != null){
			precoDescontoFormatado = CurrencyUtil.formatarValorQuatroCasas(precoDesconto);
		}
	}
	
	
	public String getPrecoDescontoFormatado(){
		return precoDescontoFormatado;
	}

	@Export(label = "Total $", alignment= Alignment.RIGHT, exhibitionOrder = 10, columnType = ColumType.MOEDA, widthPercent = 12.5f)
	@Footer(label = "Total", type = FooterType.SUM, columnType = ColumType.MOEDA)
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
		if(total != null){
			totalFormatado = CurrencyUtil.formatarValor(total);
		}
	}
	
	
	public String getTotalFormatado(){
		return totalFormatado;
	}

	@Export(label = "Total Desc $", alignment= Alignment.RIGHT, exhibitionOrder = 11, columnType = ColumType.MOEDA_QUATRO_CASAS, widthPercent = 12.5f)
	@Footer(type = FooterType.SUM, columnType = ColumType.MOEDA_QUATRO_CASAS)
	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
		if(totalDesconto != null){
			totalDescontoFormatado = CurrencyUtil.formatarValorQuatroCasas(totalDesconto);
		}
	}
	
	
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
