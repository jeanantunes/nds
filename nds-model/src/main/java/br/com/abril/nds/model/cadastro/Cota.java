package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "COTA")
@SequenceGenerator(name="COTA_SEQ", initialValue = 1, allocationSize = 1)
public class Cota {

	@Id
	@GeneratedValue(generator = "COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "NUMERO_COTA", nullable = false)
	private Integer numeroCota;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	@Column(name = "VIP", nullable = false)
	private boolean vip;
	@OneToMany
	@JoinColumn(name = "COTA_ID")
	private List<PDV> pdvs = new ArrayList<PDV>();
	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_CADASTRO", nullable = false)
	private SituacaoCadastro situacaoCadastro;
	@Column(name  ="FATOR_DESCONTO")
	private BigDecimal fatorDesconto;
	@OneToMany(mappedBy = "cota")
	private Set<EnderecoCota> enderecos = new HashSet<EnderecoCota>();
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OneToOne(optional = false)
	@JoinColumn(name = "CONTRATO_COTA_ID")
	private ContratoCota contratoCota;
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Box box;
	@Cascade(value = org.hibernate.annotations.CascadeType.PERSIST)
	@OneToMany(mappedBy = "cota")
	private List<HistoricoSituacaoCota> historicos = new ArrayList<HistoricoSituacaoCota>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public boolean isVip() {
		return vip;
	}
	
	public void setVip(boolean vip) {
		this.vip = vip;
	}
	
	public List<PDV> getPdvs() {
		return pdvs;
	}
	
	public void setPdvs(List<PDV> pdvs) {
		this.pdvs = pdvs;
	}
	
	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}
	
	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public BigDecimal getFatorDesconto() {
		return fatorDesconto;
	}
	
	public void setFatorDesconto(BigDecimal fatorDesconto) {
		this.fatorDesconto = fatorDesconto;
	}
	
	public Set<EnderecoCota> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(Set<EnderecoCota> enderecos) {
		this.enderecos = enderecos;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}
	
	public ContratoCota getContratoCota() {
		return contratoCota;
	}
	
	public void setContratoCota(ContratoCota contratoCota) {
		this.contratoCota = contratoCota;
	}
	
	public List<HistoricoSituacaoCota> getHistoricos() {
		return historicos;
	}
	
	public void setHistoricos(List<HistoricoSituacaoCota> historicos) {
		this.historicos = historicos;
	}

}