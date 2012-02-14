package br.com.abril.nds.model.cadastro;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "COTA")
public class Cota {

	@Id
	private Long id;
	private int cota;
	@ManyToOne
	private Pessoa pessoa;
	@OneToMany
	@JoinColumn(name = "COTA_ID")
	private List<PDV> pdvs;
	@Enumerated(EnumType.STRING)
	private SituacaoCadastro situacaoCadastro;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getCota() {
		return cota;
	}
	
	public void setCota(int cota) {
		this.cota = cota;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

}