package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.Origem;

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

	@Column(name = "COD_INTERFACE", nullable = true)
	private Integer codigoInterface;

	@Column(name = "TIPO_CONTRATO")
	private String tipoContrato;

	@Column(name = "INICIO_ATIVIDADE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date inicioAtividade;

	@Column(name = "PERMITE_BALANCEAMENTO", nullable = false)
	private boolean permiteBalanceamento;

	@ManyToOne(optional = false)
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "JURIDICA_ID",unique=true)
	private PessoaJuridica juridica;

	@Column(name="POSSUI_CONTRATO")
	private boolean possuiContrato;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="VALIDADE_CONTRATO")
	private Date validadeContrato;

	@Column(name="RESPONSAVEL")
	private String responsavel;

	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_CADASTRO", nullable = false)
	private SituacaoCadastro situacaoCadastro;

	@OneToMany(mappedBy = "fornecedor")
	@Cascade(value={CascadeType.ALL})
	private Set<EnderecoFornecedor> enderecos = new HashSet<EnderecoFornecedor>();

	@OneToMany(mappedBy = "fornecedor")
	@Cascade(value={CascadeType.ALL})
	private Set<TelefoneFornecedor> telefones = new HashSet<TelefoneFornecedor>();

	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@ManyToOne(optional = true)
	@JoinColumn(name = "TIPO_FORNECEDOR_ID")
	private TipoFornecedor tipoFornecedor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM", nullable = false)
	private Origem origem;

	@Column(name = "EMAIL_NFE")
	private String emailNfe;

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

	public Date getInicioAtividade() {
		return inicioAtividade;
	}

	public void setInicioAtividade(Date inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
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

	public boolean isPossuiContrato() {
		return possuiContrato;
	}

	public void setPossuiContrato(boolean possuiContrato) {
		this.possuiContrato = possuiContrato;
	}

	public Date getValidadeContrato() {
		return validadeContrato;
	}

	public void setValidadeContrato(Date validadeContrato) {
		this.validadeContrato = validadeContrato;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}

	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public Set<EnderecoFornecedor> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(Set<EnderecoFornecedor> enderecos) {
		this.enderecos = enderecos;
	}

	public Set<TelefoneFornecedor> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<TelefoneFornecedor> telefones) {
		this.telefones = telefones;
	}

	public TipoFornecedor getTipoFornecedor() {
		return tipoFornecedor;
	}

	public void setTipoFornecedor(TipoFornecedor tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

	public Integer getCodigoInterface() {
		return codigoInterface;
	}

	public void setCodigoInterface(Integer codigoInterface) {
		this.codigoInterface = codigoInterface;
	}

	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/**
	 * @return the emailNfe
	 */
	public String getEmailNfe() {
		return emailNfe;
	}

	/**
	 * @param emailNfe the emailNfe to set
	 */
	public void setEmailNfe(String emailNfe) {
		this.emailNfe = emailNfe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Fornecedor other = (Fornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
