package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;
import br.com.abril.nds.util.export.fiscal.nota.NFEExports;

@Entity
@Table(name = "NOTA_FISCAL_PRODUTO_SERVICO")
public class ProdutoServico implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6402390731085431454L;

	@EmbeddedId
	@NFEExportType
	private ProdutoServicoPK produtoServicoPK;
	
	/**
	 * Encargos financeiros
	 */
	@OneToOne(optional = false, mappedBy = "produtoServico")
	@PrimaryKeyJoinColumn
	@NFEExportType
	private EncargoFinanceiro encargoFinanceiro;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	/**
	 * cProd
	 */
	@Column(name="CODIGO_PRODUTO", length=60, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=0, tamanho=60)
	private String codigoProduto;
	
	/**
	 * cEAN
	 */
	@Column(name="CODIGO_BARRAS", length=14, nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=1, tamanho=14), @NFEExport(secao=TipoSecao.I, posicao=11, tamanho=14)})
	private Long codigoBarras;
	
	/**
	 * xProd
	 */
	@Column(name="DESCRICAO_PRODUTO", length=120, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=2, tamanho=120)
	private String descricaoProduto;
	
	/**
	 * NCM
	 */
	@Column(name="NCM", length=8, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=3, tamanho=8)
	private Long ncm;
	
	/**
	 * EXTIPI
	 */
	@Column(name="EXTIPI", length=2, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=4, tamanho=3)
	private Long extipi;
	
	/**
	 * CFOP
	 */
	@Column(name="CFOP", length=4, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=6, tamanho=4)
	private Integer cfop;
	
	/**
	 * uCom
	 */
	@Column(name="UNIDADE_COMERCIAL", length=6, nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=7, tamanho=6),@NFEExport(secao=TipoSecao.I, posicao=12, tamanho=6)})
	private String unidade;
	
	/**
	 * qCom
	 */
	@Column(name="QUANTIDADE_COMERCIAL", precision=15, scale=2 , nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=8, tamanho=12),@NFEExport(secao=TipoSecao.I, posicao=13, tamanho=12)})
	private BigInteger quantidade;
	
	/**
	 * vUnCom
	 */
	@Column(name="VALOR_UNITARIO_COMERCIAL",precision=21, scale=2 , nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=9, tamanho=16),@NFEExport(secao=TipoSecao.I, posicao=14, tamanho=16)})
	private BigDecimal valorUnitario;
	
	/**
	 * vProd
	 */
	@Column(name="VALOR_TOTAL_BRUTO", precision=15, scale=2, nullable=false)
	private BigDecimal valorTotalBruto;
	
	/**
	 * vFrete
	 */
	@Column(name="VALOR_FRETE", precision=15, scale=2, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=15, tamanho=15)
	private BigDecimal valorFrete;
	
	/**
	 * vSeg
	 */
	@Column(name="VALOR_SERGURO", precision=15, scale=2, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=16, tamanho=15)
	private BigDecimal valorSeguro;
	
	/**
	 * vDesc
	 */
	@Column(name="VALOR_DESCONTO", precision=15, scale=2, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=17, tamanho=15)
	private BigDecimal valorDesconto;
	
	/**
	 * vOutro
	 */
	@Column(name="VALOR_OUTROS", precision=15, scale=2, nullable=true)
	private BigDecimal valorOutros;
	
	
	
	@ManyToMany
	@JoinTable( joinColumns = {			
			@JoinColumn(name = "PRODUTO_SERVICO_SEQUENCIA", referencedColumnName="SEQUENCIA"),
			@JoinColumn(name = "NOTA_FISCAL_ID", referencedColumnName="NOTA_FISCAL_ID")
		},
			inverseJoinColumns = {
			@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID", referencedColumnName="ID")
	})
	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCota;
	
	/**
	 * @return the encargoFinanceiro
	 */
	public EncargoFinanceiro getEncargoFinanceiro() {
		return encargoFinanceiro;
	}

	/**
	 * @param encargoFinanceiro the encargoFinanceiro to set
	 */
	public void setEncargoFinanceiro(EncargoFinanceiro encargoFinanceiro) {
		this.encargoFinanceiro = encargoFinanceiro;
	}
	
	/**
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the produtoServicoPK
	 */
	public ProdutoServicoPK getProdutoServicoPK() {
		return produtoServicoPK;
	}

	/**
	 * @param produtoServicoPK the produtoServicoPK to set
	 */
	public void setProdutoServicoPK(ProdutoServicoPK produtoServicoPK) {
		this.produtoServicoPK = produtoServicoPK;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the codigoBarras
	 */
	public Long getCodigoBarras() {
		return codigoBarras;
	}

	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(Long codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	/**
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	/**
	 * @return the ncm
	 */
	public Long getNcm() {
		return ncm;
	}

	/**
	 * @param ncm the ncm to set
	 */
	public void setNcm(Long ncm) {
		this.ncm = ncm;
	}

	/**
	 * @return the extipi
	 */
	public Long getExtipi() {
		return extipi;
	}

	/**
	 * @param extipi the extipi to set
	 */
	public void setExtipi(Long extipi) {
		this.extipi = extipi;
	}

	/**
	 * @return the cfop
	 */
	public Integer getCfop() {
		return cfop;
	}

	/**
	 * @param cfop the cfop to set
	 */
	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}

	/**
	 * @return the unidade
	 */
	public String getUnidade() {
		return unidade;
	}

	/**
	 * @param unidade the unidade to set
	 */
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	/**
	 * @return the quantidade
	 */
	public BigInteger getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the valorTotalBruto
	 */
	public BigDecimal getValorTotalBruto() {
		return valorTotalBruto;
	}

	/**
	 * @param valorTotalBruto the valorTotalBruto to set
	 */
	public void setValorTotalBruto(BigDecimal valorTotalBruto) {
		this.valorTotalBruto = valorTotalBruto;
	}

	/**
	 * @return the valorFrete
	 */
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	/**
	 * @param valorFrete the valorFrete to set
	 */
	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	/**
	 * @return the valorSeguro
	 */
	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	/**
	 * @param valorSeguro the valorSeguro to set
	 */
	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	/**
	 * @return the valorDesconto
	 */
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	/**
	 * @return the valorOutros
	 */
	public BigDecimal getValorOutros() {
		return valorOutros;
	}

	/**
	 * @param valorOutros the valorOutros to set
	 */
	public void setValorOutros(BigDecimal valorOutros) {
		this.valorOutros = valorOutros;
	}
	
	/**
	 * @return the listaMovimentoEstoqueCota
	 */
	public List<MovimentoEstoqueCota> getListaMovimentoEstoqueCota() {
		return listaMovimentoEstoqueCota;
	}

	/**
	 * @param listaMovimentoEstoqueCota the listaMovimentoEstoqueCota to set
	 */
	public void setListaMovimentoEstoqueCota(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota) {
		this.listaMovimentoEstoqueCota = listaMovimentoEstoqueCota;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((produtoServicoPK == null) ? 0 : produtoServicoPK.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoServico other = (ProdutoServico) obj;
		if (produtoServicoPK == null) {
			if (other.produtoServicoPK != null)
				return false;
		} else if (!produtoServicoPK.equals(other.produtoServicoPK))
			return false;
		return true;
	}
}
