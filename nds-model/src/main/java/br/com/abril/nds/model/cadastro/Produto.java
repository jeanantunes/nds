package br.com.abril.nds.model.cadastro;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PRODUTO")
@SequenceGenerator(name="PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class Produto {

	@Id
	@GeneratedValue(generator = "PRODUTO_SEQ")
	private Long id;
	@Column(unique = true)
	private String codigo;
	@Enumerated
	private PeriodicidadeProduto periodicidade;
	private String nome;
	private String descricao;
	@ManyToMany
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	@OneToOne
	private TipoProduto tipoProduto;
	
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
	
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	public Fornecedor getFornecedorUnico() {
		if (fornecedores.size() > 1) {
			throw new IllegalStateException("Produto possui mais de 1 fornecedor!");
		}
		return fornecedores.iterator().next();
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
	
	
	
}