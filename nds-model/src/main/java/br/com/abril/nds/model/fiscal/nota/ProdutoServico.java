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
	@Column(name="CODIGO_PRODUTO", length=60, nullable=false, columnDefinition="Código do Produto ou Serviço")
	@NFEExport(secao="I", posicao=0, tamanho=60)
	private String codigoProduto;
	
	/**
	 * cEAN
	 */
	@Column(name="CODIGO_BARRAS", length=14, nullable=false, columnDefinition="GTIN (Global Trade Item Number) do produto, antigo código EAN ou código de barras")
	@NFEExport(secao="I", posicao=1, tamanho=14)
	private Long codigoBarras;
	
	/**
	 * xProd
	 */
	@Column(name="DESCRICAO_PRODUTO", length=120, nullable=false, columnDefinition="Descrição do produto ou serviço")
	@NFEExport(secao="I", posicao=2, tamanho=120)
	private String descricaoProduto;
	
	/**
	 * NCM
	 */
	@Column(name="NCM", length=8, nullable=false, columnDefinition="Nomenclatura Comum do MERCOSUL")
	@NFEExport(secao="I", posicao=3, tamanho=8)
	private Long ncm;
	
	/**
	 * EXTIPI
	 */
	@Column(name="EXTIPI", length=2, nullable=true, columnDefinition="")
	@NFEExport(secao="I", posicao=4, tamanho=3)
	private Long extipi;
	
	/**
	 * CFOP
	 */
	@Column(name="CFOP", length=4, nullable=false, columnDefinition="Código Fiscal de Operações e Prestações")
	@NFEExport(secao="I", posicao=6, tamanho=4)
	private Integer cfop;
	
	/**
	 * uCom
	 */
	@Column(name="UNIDADE_COMERCIAL", length=6, nullable=false, columnDefinition="Unidade Comercial")
	@NFEExport(secao="I", posicao=7, tamanho=6)
	private String unidade;
	
	/**
	 * qCom
	 */
	@Column(name="QUANTIDADE_COMERCIAL", length=15, nullable=false, columnDefinition="Quantidade Comercial")
	@NFEExport(secao="I", posicao=8, tamanho=12)
	private Long quantidade;
	
	/**
	 * vUnCom
	 */
	@Column(name="VALOR_UNITARIO_COMERCIAL", length=21, nullable=false, columnDefinition="Valor Unitário de Comercialização")
	@NFEExport(secao="I", posicao=9, tamanho=16)
	private Double valorUnitario;
	
	
	
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
	
}
