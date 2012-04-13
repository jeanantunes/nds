package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
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

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PDV")
@SequenceGenerator(name="PDV_SEQ", initialValue = 1, allocationSize = 1)
public class PDV implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5121689715572569495L;
	
	@Id
	@GeneratedValue(generator = "PDV_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PDV", nullable = false)
	private TipoPDV tipoPDV;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToMany(mappedBy = "pdv")
	private Set<EnderecoPDV> enderecos = new HashSet<EnderecoPDV>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public TipoPDV getTipoPDV() {
		return tipoPDV;
	}
	
	public void setTipoPDV(TipoPDV tipoPDV) {
		this.tipoPDV = tipoPDV;
	}
	
	public boolean isPrincipal() {
		return principal;
	}
	
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public Set<EnderecoPDV> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(Set<EnderecoPDV> enderecos) {
		this.enderecos = enderecos;
	}

}