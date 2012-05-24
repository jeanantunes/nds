package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	
	@Column(name = "CODIGO", unique = true)
	private String codigo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERIODICIDADE", nullable = false)
	private PeriodicidadeProduto periodicidade;
	
	@Column(name = "NOME", nullable = false, unique = true)
	private String nome;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@ManyToMany
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();

	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM")
	private Origem origem;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_PRODUTO_ID")
	private TipoProduto tipoProduto;
	
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
	protected BigDecimal peso;
	
	/**
	 * Dimensões do produto (largura, etc)
	 */
	@Embedded
	private Dimensao dimensao;

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
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	public BigDecimal getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	
}
