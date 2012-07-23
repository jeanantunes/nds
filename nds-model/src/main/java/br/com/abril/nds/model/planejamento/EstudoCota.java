package br.com.abril.nds.model.planejamento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO_COTA")
@SequenceGenerator(name="ESTUDO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoCota implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2730755900853136814L;
	@Id
	@GeneratedValue(generator = "ESTUDO_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "QTDE_PREVISTA", nullable = false)
	private BigDecimal qtdePrevista;
	@Column(name = "QTDE_EFETIVA", nullable = false)
	private BigDecimal qtdeEfetiva;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTUDO_ID")
	private Estudo estudo;
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@OneToMany(mappedBy = "estudoCota")
	private Set<RateioDiferenca> rateiosDiferenca = new HashSet<RateioDiferenca>();
	
	@OneToOne(mappedBy = "estudoCota", targetEntity=MovimentoEstoqueCota.class)
	private MovimentoEstoqueCota movimentosEstoqueCota; 
	
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

	public MovimentoEstoqueCota getMovimentosEstoqueCota() {
		return movimentosEstoqueCota;
	}

	public void setMovimentosEstoqueCota(MovimentoEstoqueCota movimentosEstoqueCota) {
		this.movimentosEstoqueCota = movimentosEstoqueCota;
	}

}