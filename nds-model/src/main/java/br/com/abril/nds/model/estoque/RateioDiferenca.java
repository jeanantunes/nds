package br.com.abril.nds.model.estoque;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.EstudoCota;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "RATEIO_DIFERENCA")
@SequenceGenerator(name="RATEIO_DIFERENCA_SEQ", initialValue = 1, allocationSize = 1)
public class RateioDiferenca implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3167117959346667910L;

	@Id
	@GeneratedValue(generator = "RATEIO_DIFERENCA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DIFERENCA_ID")
	private Diferenca diferenca;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTUDO_COTA_ID")
	private EstudoCota estudoCota;
	
	public RateioDiferenca(){}
	
	public RateioDiferenca (RateioDiferenca rateioDiferenca, Cota cota, EstudoCota estudoCota) {
		this.id = rateioDiferenca.getId();
		this.qtde = rateioDiferenca.getQtde();
		this.cota = cota;
		this.estudoCota = estudoCota;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigInteger getQtde() {
		return qtde;
	}
	
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public Diferenca getDiferenca() {
		return diferenca;
	}
	
	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}
	
	public EstudoCota getEstudoCota() {
		return estudoCota;
	}
	
	public void setEstudoCota(EstudoCota estudoCota) {
		this.estudoCota = estudoCota;
	}
}