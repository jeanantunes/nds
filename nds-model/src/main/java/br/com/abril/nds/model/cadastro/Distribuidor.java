package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "DISTRIBUIDOR")
@SequenceGenerator(name="DISTRIB_SEQ", initialValue = 1, allocationSize = 1)
public class Distribuidor {

	@Id
	@GeneratedValue(generator = "DISTRIB_SEQ")
	@Column(name = "ID")
	private Long id;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	@OneToOne(optional = false)
	@JoinColumn(name = "PJ_ID")
	private PessoaJuridica juridica;
	@OneToMany(mappedBy = "distribuidor")
	private Set<DistribuicaoFornecedor> diasDistribuicao = new HashSet<DistribuicaoFornecedor>();
	@Column(name = "FATOR_DESCONTO")
	private BigDecimal fatorDesconto;

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
	
	public BigDecimal getFatorDesconto() {
		return fatorDesconto;
	}
	
	public void setFatorDesconto(BigDecimal fatorDesconto) {
		this.fatorDesconto = fatorDesconto;
	}

}