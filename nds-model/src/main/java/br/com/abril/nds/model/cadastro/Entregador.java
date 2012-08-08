package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * Entidade para o papel de entregador no sistema
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "ENTREGADOR")
@SequenceGenerator(name="ENTREGADOR_SEQ", initialValue = 1, allocationSize = 1)
public class Entregador {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "ENTREGADOR_SEQ")
	private Long id;
	
	@Column(name = "CODIGO", nullable = false, unique=true)
	private Long codigo;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	@Column(name = "INICIO_ATIVIDADE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date inicioAtividade;
	
	@Column(name = "COMISSIONADO", nullable = false)
	private boolean comissionado;
	
	@Column(name = "PERCENTUAL_COMISSAO")
	private BigDecimal percentualComissao;
	
	@Column(name = "PROCURACAO", nullable = false)
	private boolean procuracao;
	
	@OneToOne(mappedBy = "entregador", cascade=CascadeType.REMOVE)
	private ProcuracaoEntregador procuracaoEntregador;
	
	@OneToMany(mappedBy = "entregador")
	private Set<EnderecoEntregador> enderecos = new HashSet<EnderecoEntregador>();
	
	@OneToMany(mappedBy = "entregador")
	private Set<TelefoneEntregador> telefones = new HashSet<TelefoneEntregador>();
	
	@OneToOne
	@JoinColumn(name="ROTEIRO_ID")
	private Roteiro roteiro;
	
	@OneToOne
	@JoinColumn(name="ROTA_ID")
	private Rota rota;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa the pessoa to set
	 */
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the inicioAtividade
	 */
	public Date getInicioAtividade() {
		return inicioAtividade;
	}

	/**
	 * @param inicioAtividade the inicioAtividade to set
	 */
	public void setInicioAtividade(Date inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
	}

	/**
	 * @return the comissionado
	 */
	public boolean isComissionado() {
		return comissionado;
	}

	/**
	 * @param comissionado the comissionado to set
	 */
	public void setComissionado(boolean comissionado) {
		this.comissionado = comissionado;
	}

	/**
	 * @return the percentualComissao
	 */
	public BigDecimal getPercentualComissao() {
		return percentualComissao;
	}

	/**
	 * @param percentualComissao the percentualComissao to set
	 */
	public void setPercentualComissao(BigDecimal percentualComissao) {
		this.percentualComissao = percentualComissao;
	}

	/**
	 * @return the procuracao
	 */
	public boolean isProcuracao() {
		return procuracao;
	}

	/**
	 * @param procuracao the procuracao to set
	 */
	public void setProcuracao(boolean procuracao) {
		this.procuracao = procuracao;
	}
	
	/**
	 * @return the procuracaoEntregador
	 */
	public ProcuracaoEntregador getProcuracaoEntregador() {
		return procuracaoEntregador;
	}

	/**
	 * @param procuracaoEntregador the procuracaoEntregador to set
	 */
	public void setProcuracaoEntregador(ProcuracaoEntregador procuracaoEntregador) {
		this.procuracaoEntregador = procuracaoEntregador;
	}

	/**
	 * @return the enderecos
	 */
	public Set<EnderecoEntregador> getEnderecos() {
		return enderecos;
	}

	/**
	 * @param enderecos the enderecos to set
	 */
	public void setEnderecos(Set<EnderecoEntregador> enderecos) {
		this.enderecos = enderecos;
	}
	
	/**
	 * @return the telefones
	 */
	public Set<TelefoneEntregador> getTelefones() {
		return telefones;
	}

	/**
	 * @param telefones the telefones to set
	 */
	public void setTelefones(Set<TelefoneEntregador> telefones) {
		this.telefones = telefones;
	}
	
	public Roteiro getRoteiro() {
		return roteiro;
	}

	public void setRoteiro(Roteiro roteiro) {
		this.roteiro = roteiro;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
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
		Entregador other = (Entregador) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}