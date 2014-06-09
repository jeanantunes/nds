package br.com.abril.nds.model.cadastro.pdv;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Gerador de fluxo do PDV
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "GERADOR_FLUXO_PDV")
@SequenceGenerator(name="GERADOR_FLUXO_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class GeradorFluxoPDV {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "GERADOR_FLUXO_PDV_SEQ")
	private Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "PDV_ID", unique = true)
	private PDV pdv;
	
	@OneToOne
	@JoinColumn(name = "TIPO_GERADOR_FLUXO_ID")
	private TipoGeradorFluxoPDV principal;
	
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = "GERADOR_FLUXO_PDV_TIPO_GERADOR_FLUXO_PDV", joinColumns = {@JoinColumn(name = "GERADOR_FLUXO_PDV_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "TIPO_GERADOR_FLUXO_PDV_ID")})
	private Set<TipoGeradorFluxoPDV> secundarios = new HashSet<TipoGeradorFluxoPDV>();

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
	 * @return the principal
	 */
	public TipoGeradorFluxoPDV getPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(TipoGeradorFluxoPDV principal) {
		this.principal = principal;
	}

	/**
	 * @return the secundarios
	 */
	public Set<TipoGeradorFluxoPDV> getSecundarios() {
		return secundarios;
	}

	/**
	 * @param secundarios the secundarios to set
	 */
	public void setSecundarios(Set<TipoGeradorFluxoPDV> secundarios) {
		this.secundarios = secundarios;
	}

	/**
	 * @return the pdv
	 */
	public PDV getPdv() {
		return pdv;
	}

	/**
	 * @param pdv the pdv to set
	 */
	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}

}
