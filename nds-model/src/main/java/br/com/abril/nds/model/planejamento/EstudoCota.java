package br.com.abril.nds.model.planejamento;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.RateioDiferenca;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO_COTA")
@SequenceGenerator(name="ESTUDO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoCota {

	@Id
	@GeneratedValue(generator = "ESTUDO_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "QTDE_PREVISTA", nullable = false)
	private BigDecimal qtdePrevista;
	@Column(name = "QTDE_EFETIVA", nullable = false)
	private BigDecimal qtdeEfetiva;
	@ManyToOne(optional = false)
	private Estudo estudo;
	@ManyToOne(optional = false)
	private Cota cota;
	@OneToMany(mappedBy = "estudoCota")
	private Set<RateioDiferenca> rateiosDiferenca = new HashSet<RateioDiferenca>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getQtdePrevista() {
		return qtdePrevista;
	}
	
	public void setQtdePrevista(BigDecimal qtdePrevista) {
		this.qtdePrevista = qtdePrevista;
	}
	
	public BigDecimal getQtdeEfetiva() {
		return qtdeEfetiva;
	}
	
	public void setQtdeEfetiva(BigDecimal qtdeEfetiva) {
		this.qtdeEfetiva = qtdeEfetiva;
	}
	
	public Estudo getEstudo() {
		return estudo;
	}
	
	public void setEstudo(Estudo estudo) {
		this.estudo = estudo;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public Set<RateioDiferenca> getRateiosDiferenca() {
		return rateiosDiferenca;
	}
	
	public void setRateiosDiferenca(Set<RateioDiferenca> rateiosDiferenca) {
		this.rateiosDiferenca = rateiosDiferenca;
	}

}