package br.com.abril.nds.model.cadastro;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "DISTRIBUIDOR")
public class Distribuidor {

	@Id
	private Long id;
	private Date dataOperacao;
	@OneToOne
	private PessoaJuridica juridica;
	@OneToMany(mappedBy = "distribuidor")
	private Set<DistribuicaoFornecedor> diasDistribuicao = new HashSet<DistribuicaoFornecedor>();

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataOperacao() {
		return dataOperacao;
	}
	
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	public PessoaJuridica getJuridica() {
		return juridica;
	}
	
	public void setJuridica(PessoaJuridica juridica) {
		this.juridica = juridica;
	}
	
	public Set<DistribuicaoFornecedor> getDiasDistribuicao() {
		return diasDistribuicao;
	}
	
	public void setDiasDistribuicao(Set<DistribuicaoFornecedor> diasDistribuicao) {
		this.diasDistribuicao = diasDistribuicao;
	}

}