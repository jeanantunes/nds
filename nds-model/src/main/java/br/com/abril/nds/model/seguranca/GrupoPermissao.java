package br.com.abril.nds.model.seguranca;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.vo.PeriodoVO;

@Entity	
@Table(name = "GRUPO_PERMISSAO")
@SequenceGenerator(name="GRUPO_PERMISSAO_SEQ", initialValue = 1, allocationSize = 1)
public class GrupoPermissao {

	@Id
	@GeneratedValue(generator = "GRUPO_PERMISSAO_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOME", nullable = false)
	private String nome;

	@ElementCollection(targetClass=Permissao.class, fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
    @JoinTable(
            name="GRUPO_PERMISSAO_PERMISSAO", // ref table.
            joinColumns = {@JoinColumn(name="PERMISSAO_GRUPO_ID")}
    )
    @Column(name="PERMISSAO_ID")
	private Set<Permissao> permissoes = new HashSet<Permissao>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	
}
