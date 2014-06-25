package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import br.com.abril.nds.model.cadastro.desconto.Desconto;

@Entity
@Table(name = "EDITOR")
@SequenceGenerator(name="EDITOR_SEQ", initialValue = 1, allocationSize = 1)
public class Editor implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6934629458438025418L;

	@Id
	@GeneratedValue(generator = "EDITOR_SEQ")
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "CODIGO", nullable = false)
	private Long codigo;

	/**
	 * Flag que indica se o Editor foi criado atraves de interface de sistemas ou por cadastro
	 */
	@Column(name = "ORIGEM_INTERFACE", nullable = true)
	private Boolean origemInterface;
	
	@Column(name = "NOME_CONTATO", nullable = true)
	private String nomeContato;
	
	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;
	
	@ManyToMany
	@JoinTable(name = "EDITOR_FORNECEDOR", joinColumns = {@JoinColumn(name = "EDITOR_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	
	@OneToMany(mappedBy = "editor")
	private Set<EnderecoEditor> enderecos = new HashSet<EnderecoEditor>();
	
	@OneToMany(mappedBy = "editor")
	private Set<TelefoneEditor> telefones = new HashSet<TelefoneEditor>();
	
	@OneToMany(mappedBy = "editor")
	private Set<TipoProdutoEditor> tiposProduto = new HashSet<TipoProdutoEditor>();
	
	@ManyToOne(optional = false)
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "JURIDICA_ID")
	private PessoaJuridica pessoaJuridica;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "DESCONTO_ID")
	private Desconto desconto;
	
	public Editor(){}
	
	public Editor(Long id, String razaoSocial){
		this.id = id;
		this.pessoaJuridica = new PessoaJuridica();
		this.pessoaJuridica.setRazaoSocial(razaoSocial);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
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
		Editor other = (Editor) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	/**
	 * @return the enderecos
	 */
	public Set<EnderecoEditor> getEnderecos() {
		return enderecos;
	}

	/**
	 * @param enderecos the enderecos to set
	 */
	public void setEnderecos(Set<EnderecoEditor> enderecos) {
		this.enderecos = enderecos;
	}

	/**
	 * @return the nomeContato
	 */
	public String getNomeContato() {
		return nomeContato;
	}

	/**
	 * @param nomeContato the nomeContato to set
	 */
	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the pessoaJuridica
	 */
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	/**
	 * @param pessoaJuridica the pessoaJuridica to set
	 */
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	/**
	 * @return the telefones
	 */
	public Set<TelefoneEditor> getTelefones() {
		return telefones;
	}

	/**
	 * @param telefones the telefones to set
	 */
	public void setTelefones(Set<TelefoneEditor> telefones) {
		this.telefones = telefones;
	}

	/**
	 * @return the tiposProduto
	 */
	public Set<TipoProdutoEditor> getTiposProduto() {
		return tiposProduto;
	}

	/**
	 * @param tiposProduto the tiposProduto to set
	 */
	public void setTiposProduto(Set<TipoProdutoEditor> tiposProduto) {
		this.tiposProduto = tiposProduto;
	}
	
	public Boolean getOrigemInterface() {
		return origemInterface;
	}

	public void setOrigemInterface(Boolean origemInterface) {
		this.origemInterface = origemInterface;
	}

	public Desconto getDesconto() {
		return desconto;
	}

	public void setDesconto(Desconto desconto) {
		this.desconto = desconto;
	}

}