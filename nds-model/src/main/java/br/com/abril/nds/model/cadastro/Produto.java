
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.util.export.Exportable;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Exportable
@Entity
@Table(name = "PRODUTO")
@SequenceGenerator(name="PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class Produto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6632216954435821598L;

	@Id
	@GeneratedValue(generator = "PRODUTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="DATA_CRIACAO", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
	
	@Column(name = "CODIGO", unique = true, length = 30)
	private String codigo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERIODICIDADE", nullable = false)
	private PeriodicidadeProduto periodicidade;
	
	@Column(name = "NOME", nullable = false, unique = false, length = 60)
	private String nome;
	
	@Column(name = "NOME_COMERCIAL")
	private String nomeComercial;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();

	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM", nullable = false)
	private Origem origem;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_PRODUTO_ID")
	private TipoProduto tipoProduto;
	
	@Column(name = "ATIVO", nullable = true)
	private boolean ativo = true;
	
	@Column(name = "DATA_DESATIVACAO", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dataDesativacao;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="DESCONTO_LOGISTICA_ID", nullable=true)
	private DescontoLogistica descontoLogistica;

	/**
	 * Editor do produto
	 */
	@ManyToOne
	@JoinColumn(name = "EDITOR_ID")
	private Editor editor;
	
	@Column(name = "COD_CONTEXTO", nullable = true)
	private Integer codigoContexto;
	
	@Column(name="SLOGAN", length = 50, nullable = true)
	protected String slogan;
	
	@Column(name = "PACOTE_PADRAO", nullable = false)
	protected int pacotePadrao;
	
	@Column(name = "PEB", nullable = false)
	protected int peb;
	
	@Column(name = "PESO", nullable = false)
	protected Long peso;
	
	/**
	 * Dimensões do produto (largura, etc)
	 */
	@Embedded
	private Dimensao dimensao;

	@Column(name="LANCAMENTO_IMEDIATO", nullable = true)
	private Boolean lancamentoImediato;

	@Column(name="PERCENTUAl_ABRANGENCIA", nullable = true)
	private Double percentualAbrangencia;

	@Enumerated(EnumType.STRING)
	@Column(name = "TRIBUTACAO_FISCAL", nullable = true)
	private TributacaoFiscal tributacaoFiscal; 

	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_TRIBUTARIA", nullable = true)
	private SituacaoTributaria situacaoTributaria; 

	@Enumerated(EnumType.STRING)
	@Column(name = "FORMA_COMERCIALIZACAO", nullable = true)
	private FormaComercializacao formaComercializacao; 

	@Column(name = "PERC_LIMITE_COTA_FIXACAO", nullable = true)
	private Double percentualLimiteCotaFixacao;

	@Column(name = "PERC_LIMITE_REPARTE_FIXACAO", nullable = true)
	private Double percentualLimiteReparteFixacao;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ALGORITMO_ID")
	private Algoritmo algoritmo;
	
	protected String fase;
	
	protected Long numeroLancamento;
	
	@Embedded
	private SegmentacaoProduto segmentacao;
	
	/**
	 * Desconto aplicado no cadastro de Tipo Desconto Cota
	 */
	@OneToOne(optional = true)
	@JoinColumn(name = "DESCONTO_ID")
	private Desconto descontoProduto;

	@Column(name="DESCONTO")
	private BigDecimal desconto;
	
	@Column(name="DESCRICAO_DESCONTO")
	private String descricaoDesconto;
	
	/**
	 * Segmento do Produto
	 */
	@OneToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="TIPO_SEGMENTO_PRODUTO_ID")
	private TipoSegmentoProduto tipoSegmentoProduto;
	
	@Column(name = "GERACAO_AUTOMATICA", nullable = true, columnDefinition="boolean default false")
	private Boolean isGeracaoAutomatica = false;
	
	@Column(name = "REMESSA_DISTRIBUICAO", nullable = true, columnDefinition="boolean default false")
	private Boolean isRemessaDistribuicao = false;
	
	@OneToMany(mappedBy = "produto")
	private List<ProdutoEdicao> produtoEdicao;
	
	@Column(name = "CODIGO_ICD", nullable = false)
	private String codigoICD;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return StringUtils.leftPad(codigo, 8, '0');
	}
	
	public void setCodigo(String codigo) {
		this.codigo = StringUtils.leftPad(codigo, 8, '0');
	}
	
	public PeriodicidadeProduto getPeriodicidade() {
		return periodicidade;
	}
	
	public void setPeriodicidade(PeriodicidadeProduto periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNomeComercial() {
		return nomeComercial;
	}
	
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void addFornecedor(Fornecedor fornecedor) {
		getFornecedores().add(fornecedor);
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}
	
	public Origem getOrigem() {
		return origem;
	}
	
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	public Editor getEditor() {
		return editor;
	}
	
	public void setEditor(Editor editor) {
		this.editor = editor;
	}
		
	public Integer getCodigoContexto() {
		return codigoContexto;
	}

	public void setCodigoContexto(Integer codigoContexto) {
		this.codigoContexto = codigoContexto;
	}
	
	public Fornecedor getFornecedor() {
		Fornecedor fornecedor = fornecedores.isEmpty() ? null : fornecedores.iterator().next();
		if (GrupoProduto.OUTROS.equals(tipoProduto.getGrupoProduto())) {
			return fornecedor;
		} else {
			if (fornecedores.size() > 1) {
				throw new IllegalStateException("PRODUTO PUBLICACAO COM MAIS DE UM FORNECEDOR!");
			}
			return fornecedor;
		}
	}

	/**
	 * @return the slogan
	 */
	public String getSlogan() {
		return slogan;
	}

	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	/**
	 * @return the pacotePadrao
	 */
	public int getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the peb
	 */
	public int getPeb() {
		return peb;
	}

	/**
	 * @param peb the peb to set
	 */
	public void setPeb(int peb) {
		this.peb = peb;
	}

	/**
	 * @return the dimensao
	 */
	public Dimensao getDimensao() {
		return dimensao;
	}

	/**
	 * @param dimensao the dimensao to set
	 */
	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
	}

	/**
	 * @return the peso
	 */
	public Long getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(Long peso) {
		this.peso = peso;
	}

	

	/**
	 * @return the lancamentoImediato
	 */
	public Boolean getLancamentoImediato() {
		return lancamentoImediato;
	}

	/**
	 * @param lancamentoImediato the lancamentoImediato to set
	 */
	public void setLancamentoImediato(Boolean lancamentoImediato) {
		this.lancamentoImediato = lancamentoImediato;
	}

	/**
	 * @return the percentualAbrangencia
	 */
	public Double getPercentualAbrangencia() {
		return percentualAbrangencia;
	}

	/**
	 * @param percentualAbrangencia the percentualAbrangencia to set
	 */
	public void setPercentualAbrangencia(Double percentualAbrangencia) {
		this.percentualAbrangencia = percentualAbrangencia;
	}

	/**
	 * @return the tributacaoFiscal
	 */
	public TributacaoFiscal getTributacaoFiscal() {
		return tributacaoFiscal;
	}

	/**
	 * @param tributacaoFiscal the tributacaoFiscal to set
	 */
	public void setTributacaoFiscal(TributacaoFiscal tributacaoFiscal) {
		this.tributacaoFiscal = tributacaoFiscal;
	}

	/**
	 * @return the situacaoTributaria
	 */
	public SituacaoTributaria getSituacaoTributaria() {
		return situacaoTributaria;
	}

	/**
	 * @param situacaoTributaria the situacaoTributaria to set
	 */
	public void setSituacaoTributaria(SituacaoTributaria situacaoTributaria) {
		this.situacaoTributaria = situacaoTributaria;
	}

	/**
	 * @return the formaComercializacao
	 */
	public FormaComercializacao getFormaComercializacao() {
		return formaComercializacao;
	}

	/**
	 * @param formaComercializacao the formaComercializacao to set
	 */
	public void setFormaComercializacao(FormaComercializacao formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}

	/**
	 * @return the percentualLimiteCotaFixacao
	 */
	public Double getPercentualLimiteCotaFixacao() {
		return percentualLimiteCotaFixacao;
	}

	/**
	 * @param percentualLimiteCotaFixacao the percentualLimiteCotaFixacao to set
	 */
	public void setPercentualLimiteCotaFixacao(Double percentualLimiteCotaFixacao) {
		this.percentualLimiteCotaFixacao = percentualLimiteCotaFixacao;
	}

	/**
	 * @return the percentualLimiteReparteFixacao
	 */
	public Double getPercentualLimiteReparteFixacao() {
		return percentualLimiteReparteFixacao;
	}

	/**
	 * @param percentualLimiteReparteFixacao the percentualLimiteReparteFixacao to set
	 */
	public void setPercentualLimiteReparteFixacao(
			Double percentualLimiteReparteFixacao) {
		this.percentualLimiteReparteFixacao = percentualLimiteReparteFixacao;
	}

	/**
	 * @return the algoritmo
	 */
	public Algoritmo getAlgoritmo() {
		return algoritmo;
	}

	/**
	 * @param algoritmo the algoritmo to set
	 */
	public void setAlgoritmo(Algoritmo algoritmo) {
		this.algoritmo = algoritmo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public DescontoLogistica getDescontoLogistica() {
		return descontoLogistica;
	}

	public void setDescontoLogistica(DescontoLogistica descontoLogistica) {
		this.descontoLogistica = descontoLogistica;
	}

	/**
	 * @return the fase
	 */
	public String getFase() {
		return fase;
	}

	/**
	 * @param fase the fase to set
	 */
	public void setFase(String fase) {
		this.fase = fase;
	}

	/**
	 * @return the numeroLancamento
	 */
	public Long getNumeroLancamento() {
		return numeroLancamento;
	}

	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Long numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}

	/**
	 * @return the segmentacao
	 */
	public SegmentacaoProduto getSegmentacao() {
		return segmentacao;
	}

	/**
	 * @param segmentacao the segmentacao to set
	 */
	public void setSegmentacao(SegmentacaoProduto segmentacao) {
		this.segmentacao = segmentacao;
	}

	/**
	 * @return the desconto
	 */
	public Desconto getDescontoProduto() {
		return descontoProduto;
	}

	public void setDescontoProduto(Desconto descontoProduto) {
		this.descontoProduto = descontoProduto;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the descricaoDesconto
	 */
	public String getDescricaoDesconto() {
		return descricaoDesconto;
	}

	/**
	 * @param descricaoDesconto the descricaoDesconto to set
	 */
	public void setDescricaoDesconto(String descricaoDesconto) {
		this.descricaoDesconto = descricaoDesconto;
	}

    public Date getDataCriacao() {
        return dataCriacao;
    }

    
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * Verifica se o produto é um publicação
     * 
     * @return true se o produto é uma publicação, false caso contrário
     */
	public boolean isPublicacao() {
	    return !GrupoProduto.OUTROS.equals(tipoProduto.getGrupoProduto());
	}

	public TipoSegmentoProduto getTipoSegmentoProduto() {
		return tipoSegmentoProduto;
	}

	public void setTipoSegmentoProduto(TipoSegmentoProduto tipoSegmentoProduto) {
		this.tipoSegmentoProduto = tipoSegmentoProduto;
	}

	public List<ProdutoEdicao> getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(List<ProdutoEdicao> produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	@Override
	public String toString() {
		return new StringBuilder(codigo).append("-").append(nome).toString();
	}
	
	public Boolean getIsGeracaoAutomatica() {
		return isGeracaoAutomatica;
	}
	
	public void setIsGeracaoAutomatica(Boolean isGeracaoAutomatica) {
		this.isGeracaoAutomatica = isGeracaoAutomatica;
	}
	
	public Boolean getIsRemessaDistribuicao() {
		return isRemessaDistribuicao;
	}

	public void setIsRemessaDistribuicao(Boolean isRemessaDistribuicao) {
		this.isRemessaDistribuicao = isRemessaDistribuicao;
	}

	public String getCodigoICD() {
		return codigoICD;
	}

	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;			
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getCodigo() == null) ? 0 : this.getCodigo().hashCode());
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
		Produto other = (Produto) obj;
		if (this.getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!this.getCodigo().equals(other.getCodigo()))
			return false;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}
