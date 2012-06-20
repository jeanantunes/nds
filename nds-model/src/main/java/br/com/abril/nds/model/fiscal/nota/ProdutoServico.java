package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExports;

@Entity
@Table(name = "PRODUTO_SERVICO_NOTA_FISCAL")
@SequenceGenerator(name = "PRODUTO_SERVICO_NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
public class ProdutoServico implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6402390731085431454L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "PRODUTO_SERVICO_NOTA_FISCAL_SEQ")
	private Long id;
	
	/**
	 * Encargos financeiros
	 */
	@OneToOne(optional = false, mappedBy = "produtoServico")
	@PrimaryKeyJoinColumn
	private EncargoFinanceiro encargoFinanceiro;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscal notaFiscal;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	/**
	 * cProd
	 */
	@Column(name="CODIGO_PRODUTO", length=60, nullable=false)
	@NFEExport(secao="I", posicao=0, tamanho=60)
	private String codigoProduto;
	
	/**
	 * cEAN
	 */
	@Column(name="CODIGO_BARRAS", length=14, nullable=false)
	@NFEExports({@NFEExport(secao="I", posicao=1, tamanho=14), @NFEExport(secao="I", posicao=11, tamanho=14)})
	private Long codigoBarras;
	
	/**
	 * xProd
	 */
	@Column(name="DESCRICAO_PRODUTO", length=120, nullable=false)
	@NFEExport(secao="I", posicao=2, tamanho=120)
	private String descricaoProduto;
	
	/**
	 * NCM
	 */
	@Column(name="NCM", length=8, nullable=false)
	@NFEExport(secao="I", posicao=3, tamanho=8)
	private Long ncm;
	
	/**
	 * EXTIPI
	 */
	@Column(name="EXTIPI", length=2, nullable=true)
	@NFEExport(secao="I", posicao=4, tamanho=3)
	private Long extipi;
	
	/**
	 * CFOP
	 */
	@Column(name="CFOP", length=4, nullable=false)
	@NFEExport(secao="I", posicao=6, tamanho=4)
	private Integer cfop;
	
	/**
	 * uCom
	 */
	@Column(name="UNIDADE_COMERCIAL", length=6, nullable=false)
	@NFEExports({@NFEExport(secao="I", posicao=7, tamanho=6),@NFEExport(secao="I", posicao=12, tamanho=6)})
	private String unidade;
	
	/**
	 * qCom
	 */
	@Column(name="QUANTIDADE_COMERCIAL", length=15, nullable=false)
	@NFEExports({@NFEExport(secao="I", posicao=8, tamanho=12),@NFEExport(secao="I", posicao=13, tamanho=12)})
	private Long quantidade;
	
	/**
	 * vUnCom
	 */
	@Column(name="VALOR_UNITARIO_COMERCIAL", length=21, nullable=false)
	@NFEExports({@NFEExport(secao="I", posicao=9, tamanho=16),@NFEExport(secao="I", posicao=14, tamanho=16)})
	private Double valorUnitario;
	
	/**
	 * vProd
	 */
	@Column(name="VALOR_TOTAL_BRUTO", length=15, nullable=false)
	private Double valorTotalBruto;
	
	/**
	 * vFrete
	 */
	@Column(name="VALOR_FRETE", length=15, nullable=true)
	@NFEExport(secao="I", posicao=15, tamanho=15)
	private Double valorFrete;
	
	/**
	 * vSeg
	 */
	@Column(name="VALOR_SERGURO", length=15, nullable=true)
	@NFEExport(secao="I", posicao=16, tamanho=15)
	private Double valorSeguro;
	
	/**
	 * vDesc
	 */
	@Column(name="VALOR_DESCONTO", length=15, nullable=true)
	@NFEExport(secao="I", posicao=17, tamanho=15)
	private Double valorDesconto;
	
	/**
	 * vOutro
	 */
	@Column(name="VALOR_OUTROS", length=15, nullable=true)
	private Double valorOutros;
	
	
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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
	 * @return the notaFiscal
	 */
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	/**
	 * @param notaFiscal the notaFiscal to set
	 */
	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
	public Long getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the valorUnitario
	 */
	public Double getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the valorTotalBruto
	 */
	public Double getValorTotalBruto() {
		return valorTotalBruto;
	}

	/**
	 * @param valorTotalBruto the valorTotalBruto to set
	 */
	public void setValorTotalBruto(Double valorTotalBruto) {
		this.valorTotalBruto = valorTotalBruto;
	}

	/**
	 * @return the valorFrete
	 */
	public Double getValorFrete() {
		return valorFrete;
	}

	/**
	 * @param valorFrete the valorFrete to set
	 */
	public void setValorFrete(Double valorFrete) {
		this.valorFrete = valorFrete;
	}

	/**
	 * @return the valorSeguro
	 */
	public Double getValorSeguro() {
		return valorSeguro;
	}

	/**
	 * @param valorSeguro the valorSeguro to set
	 */
	public void setValorSeguro(Double valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	/**
	 * @return the valorDesconto
	 */
	public Double getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	/**
	 * @return the valorOutros
	 */
	public Double getValorOutros() {
		return valorOutros;
	}

	/**
	 * @param valorOutros the valorOutros to set
	 */
	public void setValorOutros(Double valorOutros) {
		this.valorOutros = valorOutros;
	}
	
}
