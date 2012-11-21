package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.Origem;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
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
	
	@Column(name = "CODIGO", unique = true, length = 30)
	private String codigo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERIODICIDADE", nullable = false)
	private PeriodicidadeProduto periodicidade;
	
	@Column(name = "NOME", nullable = false, unique = false, length = 60)
	private String nome;
	
	@Column(name = "NOME_COMERCIAL")
	private String nomeComercial;
	
	@ManyToMany(fetch=FetchType.LAZY)
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
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
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

	@Column(name="GRUPO_EDITORIAL", length=25, nullable = true)
	private String grupoEditorial;
	
	@Column(name="SUB_GRUPO_EDITORIAL", length=25, nullable = true)
	private String subGrupoEditorial;
	
	
	protected String fase;
	
	protected Long numeroLancamento;
	
	@Embedded
	private SegmentacaoProduto segmentacao;
	
	@Column(name="DESCONTO")
	private BigDecimal desconto;
	
	@Column(name="DESCRICAO_DESCONTO")
	private String descricaoDesconto;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	
	@Override
	public String toString() {
		return new StringBuilder(codigo).append("-").append(nome).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
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

	/**
	 * @return the grupoEditorial
	 */
	public String getGrupoEditorial() {
		return grupoEditorial;
	}

	/**
	 * @param grupoEditorial the grupoEditorial to set
	 */
	public void setGrupoEditorial(String grupoEditorial) {
		this.grupoEditorial = grupoEditorial;
	}

	/**
	 * @return the subGrupoEditorial
	 */
	public String getSubGrupoEditorial() {
		return subGrupoEditorial;
	}
	
	public void setSubGrupoEditorial(String subGrupoEditorial) {
		this.subGrupoEditorial = subGrupoEditorial;
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
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
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

	/**
     * Verifica se o produto é um publicação
     * 
     * @return true se o produto é uma publicação, false caso contrário
     */
	public boolean isPublicacao() {
	    return !GrupoProduto.OUTROS.equals(tipoProduto.getGrupoProduto());
	}

}
