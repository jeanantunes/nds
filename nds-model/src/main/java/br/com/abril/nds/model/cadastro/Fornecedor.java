package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "FORNECEDOR")
@SequenceGenerator(name="FORNECEDOR_SEQ", initialValue = 1, allocationSize = 1)
public class Fornecedor implements Serializable {

	private static final long serialVersionUID = -1534481069627822217L;
	
	@Id
	@GeneratedValue(generator = "FORNECEDOR_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "TIPO_CONTRATO")
	private String tipoContrato;
	@Column(name = "PERMITE_BALANCEAMENTO", nullable = false)
	private boolean permiteBalanceamento;
	@ManyToOne(optional = false)
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private PessoaJuridica juridica;
	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_CADASTRO", nullable = false)
	private SituacaoCadastro situacaoCadastro;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTipoContrato() {
		return tipoContrato;
	}
	
	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	
	public boolean isPermiteBalanceamento() {
		return permiteBalanceamento;
	}
	
	public void setPermiteBalanceamento(boolean permiteBalanceamento) {
		this.permiteBalanceamento = permiteBalanceamento;
	}
	
	public PessoaJuridica getJuridica() {
		return juridica;
	}
	
	public void setJuridica(PessoaJuridica juridica) {
		this.juridica = juridica;
	}
	
	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}
	
	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

}