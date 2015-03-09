package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExports;

@Embeddable
@XmlType(name="prod")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProdutoServico implements Serializable {

	/*
	@Transient
	@XmlTransient
	DecimalFormat df = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale ("pt", "BR")));  
	*/
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6402390731085431454L;

	@XmlTransient
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	/**
	 * cProd
	 */
	@Column(name="CODIGO_PRODUTO", length=60, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=0, tamanho=60)
	@XmlElement(name="cProd")
	private String codigoProduto;
	
	/**
	 * cEAN
	 */
	@Column(name="CODIGO_BARRAS", length=14, nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=1, tamanho=14), @NFEExport(secao=TipoSecao.I, posicao=10, tamanho=14)})
	@XmlElement(name="cEAN")
	private Long codigoBarras;
	
	/**
	 * xProd
	 */
	@Column(name="DESCRICAO_PRODUTO", length=120, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=2, tamanho=120)
	@XmlElement(name="xProd")
	private String descricaoProduto;
	
	/**
	 * NCM
	 */
	@Column(name="NCM", length=8, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=3, tamanho=8)
	@XmlElement(name="NCM")
	private Long ncm;
	
	/**
	 * EXTIPI
	 */
	@Column(name="EXTIPI", length=2, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=4, tamanho=3)
	@XmlElement(name="EXTIPI")
	private Long extipi;
	
	/**
	 * CFOP
	 */
	@Column(name="CFOP", length=4, nullable=false)
	@NFEExport(secao=TipoSecao.I, posicao=5, tamanho=4)
	@XmlElement(name="CFOP")
	private Integer cfop;
	
	/**
	 * CFOP
	 */
	@Column(name="CST", length=4, nullable=false)
	@XmlTransient	
	private String cst;
	
	/**
	 * uCom
	 */
	@Column(name="UNIDADE_COMERCIAL", length=6, nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=6, tamanho=6),@NFEExport(secao=TipoSecao.I, posicao=11, tamanho=6)})
	@XmlElement(name="uCom")
	private String unidade;
	
	/**
	 * qCom
	 */
	@Column(name="QUANTIDADE_COMERCIAL", precision=15, scale=2 , nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=7, tamanho=12),@NFEExport(secao=TipoSecao.I, posicao=12, tamanho=12)})
	@XmlElement(name="qCom")
	private BigInteger quantidade;
	
	/**
	 * vUnCom
	 */
	@Column(name="VALOR_UNITARIO_COMERCIAL", precision=18, scale=4, nullable=false)
	@NFEExports({@NFEExport(secao=TipoSecao.I, posicao=8, tamanho=16),@NFEExport(secao=TipoSecao.I, posicao=13, tamanho=16)})
	@XmlElement(name="vUnCom")
	private BigDecimal valorUnitario;
	
	/**
	 * vProd
	 */
	@Column(name="VALOR_TOTAL_BRUTO", precision=18, scale=4, nullable=false)
	@XmlElement(name="vProd")
	private BigDecimal valorTotalBruto;

	@Transient
	@XmlElement(name="cEANTrib")
	private Long codigoBarrasXML;
	

	@Transient
	@XmlElement(name="uTrib")
	private String unidadeTributada;	
	

	@Transient
	@XmlElement(name="qTrib")
	private BigInteger quantidadeTributada;

	@Transient
	@XmlElement(name="vUnTrib")
	private String valorUnitarioTributado;
	
	/**
	 * vFrete
	 */
	@Column(name="VALOR_FRETE", precision=18, scale=4, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=14, tamanho=15)
	@XmlElement(name="vFrete")
	private BigDecimal valorFrete;
	
	/**
	 * vSeg
	 */
	@Column(name="VALOR_SERGURO", precision=18, scale=4, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=15, tamanho=15)
	@XmlElement(name="vSeg")
	private BigDecimal valorSeguro;
	
	/**
	 * vDesc
	 */
	@Column(name="VALOR_DESCONTO", precision=18, scale=4, nullable=true)
	@NFEExport(secao=TipoSecao.I, posicao=16, tamanho=15)
	@XmlElement(name="vDesc")
	private BigDecimal valorDesconto;
	
	/**
	 * vOutro
	 */
	@Column(name="VALOR_OUTROS", precision=18, scale=4, nullable=true)
	@XmlElement(name="vOutro")
	private BigDecimal valorOutros;
	
	@Column(name="VALOR_ALIQUOTA_ICMS", precision=13, scale=4, nullable=false)
	@XmlTransient
	private BigDecimal valorAliquotaICMS;
	
	@Column(name="VALOR_ALIQUOTA_IPI", precision=13, scale=4, nullable=false)
	@XmlTransient
	private BigDecimal valorAliquotaIPI;
	
	@Column(name="VALOR_COMPOE_VALOR_NF", precision=13, nullable=false)
	@XmlTransient
	private boolean valorCompoeValorNF;
	
	@Transient
	@XmlElement(name="indTot")
	private Long valorCompoeValorNFXML;
	
	@XmlTransient
	@Column(name="VALOR_FRETE_COMPOE_VALOR_NF", precision=13, nullable=false)
	private boolean valorFreteCompoeValorNF;
	
	@ManyToMany
	@JoinTable( joinColumns = {			
			@JoinColumn(name = "PRODUTO_SERVICO_SEQUENCIA", referencedColumnName="SEQUENCIA"),
			@JoinColumn(name = "NOTA_FISCAL_ID", referencedColumnName="NOTA_FISCAL_ID")
		},
			inverseJoinColumns = {
			@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID", referencedColumnName="ID")
	})
	@Cascade(value = {CascadeType.ALL})
	@XmlTransient
	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCota;
	
	@OneToMany(cascade=javax.persistence.CascadeType.ALL)
	@JoinTable(name = "NOTA_FISCAL_ITEM_NOTA_FISCAL_ORIGEM_ITEM", 
			joinColumns = {
				@JoinColumn(name = "PRODUTO_SERVICO_SEQUENCIA", referencedColumnName="SEQUENCIA"),
				@JoinColumn(name = "NOTA_FISCAL_ID", referencedColumnName="NOTA_FISCAL_ID")
			}, 
			inverseJoinColumns = {@JoinColumn(name = "ORIGEM_ITEM_NOTA_FISCAL_ID")}
	)
	@XmlTransient
	private List<OrigemItemNotaFiscal> origemItemNotaFiscal;
	
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
		this.codigoBarrasXML = codigoBarras;
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

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
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
		this.unidadeTributada = unidade;
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
		this.quantidadeTributada = quantidade;
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
		this.valorUnitarioTributado = valorUnitario.toString();
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
	
	public BigDecimal getValorAliquotaICMS() {
		return valorAliquotaICMS;
	}

	public void setValorAliquotaICMS(BigDecimal valorAliquotaICMS) {
		this.valorAliquotaICMS = valorAliquotaICMS;
	}

	public BigDecimal getValorAliquotaIPI() {
		return valorAliquotaIPI;
	}

	public void setValorAliquotaIPI(BigDecimal valorAliquotaIPI) {
		this.valorAliquotaIPI = valorAliquotaIPI;
	}

	
	public boolean isValorCompoeValorNF() {
		return valorCompoeValorNF;
	}

	public void setValorCompoeValorNF(boolean valorCompoeValorNF) {
		this.valorCompoeValorNF = valorCompoeValorNF;
		this.valorCompoeValorNFXML = valorCompoeValorNF ? 1L : 0L;
	}

	public boolean isValorFreteCompoeValorNF() {
		return valorFreteCompoeValorNF;
	}

	public void setValorFreteCompoeValorNF(boolean valorFreteCompoeValorNF) {
		this.valorFreteCompoeValorNF = valorFreteCompoeValorNF; 
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
	 * @return
	 */
	public List<OrigemItemNotaFiscal> getOrigemItemNotaFiscal() {
		return origemItemNotaFiscal;
	}

	/**
	 * @param origemItemNotaFiscal
	 */
	public void setOrigemItemNotaFiscal(List<OrigemItemNotaFiscal> origemItemNotaFiscal) {
		this.origemItemNotaFiscal = origemItemNotaFiscal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getCodigoBarras() == null) ? 0 : this.getCodigoBarras().hashCode());
		result = prime * result + ((this.getCodigoProduto() == null) ? 0 : this.getCodigoProduto().hashCode());
		result = prime * result + ((this.getListaMovimentoEstoqueCota() == null) ? 0 : this.getListaMovimentoEstoqueCota().hashCode());
		result = prime * result + ((this.getOrigemItemNotaFiscal() == null) ? 0 : this.getOrigemItemNotaFiscal().hashCode());
		result = prime * result + ((this.getProdutoEdicao() == null) ? 0 : this.getProdutoEdicao().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoServico other = (ProdutoServico) obj;
		if (this.getCodigoBarras() == null) {
			if (other.getCodigoBarras() != null)
				return false;
		} else if (!this.getCodigoBarras().equals(other.getCodigoBarras()))
			return false;
		if (this.getCodigoProduto() == null) {
			if (other.getCodigoProduto() != null)
				return false;
		} else if (!this.getCodigoProduto().equals(other.getCodigoProduto()))
			return false;
		if (this.getListaMovimentoEstoqueCota() == null) {
			if (other.getListaMovimentoEstoqueCota() != null)
				return false;
		} else if (!this.getListaMovimentoEstoqueCota()
				.equals(other.getListaMovimentoEstoqueCota()))
			return false;
		if (this.getOrigemItemNotaFiscal() == null) {
			if (other.getOrigemItemNotaFiscal() != null)
				return false;
		} else if (!this.getOrigemItemNotaFiscal().equals(other.getOrigemItemNotaFiscal()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
	}
}