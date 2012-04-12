package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade para a procuração do entregador
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "PROCURACAO_ENTREGADOR")
@SequenceGenerator(name="PROCURACAO_ENTREGADOR_SEQ", initialValue = 1, allocationSize = 1)
public class ProcuracaoEntregador {
	
	@Id
	@GeneratedValue(generator = "PROCURACAO_ENTREGADOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "NUMERO_PERMISSAO", nullable = false)
	private String numeroPermissao;
	
	@Column(name = "PROCURADOR", nullable = false)
	private String procurador;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_CIVIL", nullable = false)
	private EstadoCivil estadoCivil;

	@Column(name = "ENDERECO")
	private String endereco;
	
	@Column(name = "RG")
	private String rg;
	
	@Column(name = "PROFISSAO")
	private String profissao;
	
	@Column(name = "NACIONALIDADE")
	private String nacionalidade;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "ENTREGADOR_ID")
	private Entregador entregador;

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
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the numeroPermissao
	 */
	public String getNumeroPermissao() {
		return numeroPermissao;
	}

	/**
	 * @param numeroPermissao the numeroPermissao to set
	 */
	public void setNumeroPermissao(String numeroPermissao) {
		this.numeroPermissao = numeroPermissao;
	}

	/**
	 * @return the procurador
	 */
	public String getProcurador() {
		return procurador;
	}

	/**
	 * @param procurador the procurador to set
	 */
	public void setProcurador(String procurador) {
		this.procurador = procurador;
	}

	/**
	 * @return the estadoCivil
	 */
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil the estadoCivil to set
	 */
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}

	/**
	 * @param rg the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}

	/**
	 * @return the profissao
	 */
	public String getProfissao() {
		return profissao;
	}

	/**
	 * @param profissao the profissao to set
	 */
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	/**
	 * @return the nacionalidade
	 */
	public String getNacionalidade() {
		return nacionalidade;
	}

	/**
	 * @param nacionalidade the nacionalidade to set
	 */
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	/**
	 * @return the entregador
	 */
	public Entregador getEntregador() {
		return entregador;
	}

	/**
	 * @param entregador the entregador to set
	 */
	public void setEntregador(Entregador entregador) {
		this.entregador = entregador;
	}
	
	
	
	
	
}
