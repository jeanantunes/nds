package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
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
	private BigInteger qtdePrevista;
	@Column(name = "QTDE_EFETIVA", nullable = false)
	private BigInteger qtdeEfetiva;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTUDO_ID")
	private Estudo estudo;
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;

	@Column(name = "REPARTE")
	private BigInteger reparte;
	
	@OneToMany(mappedBy = "estudoCota")
	private Set<RateioDiferenca> rateiosDiferenca = new HashSet<RateioDiferenca>();
	
	@OneToMany(mappedBy = "estudoCota")
	private List<MovimentoEstoqueCota> movimentosEstoqueCota; 
	
	@OneToMany(mappedBy = "estudoCota")
	private List<ItemNotaEnvio> itemNotaEnvios;
	
	@Column(name = "CLASSIFICACAO")
	private String classificacao;
	
	public EstudoCota() {
		
	}
	
	public EstudoCota(Long id) {
		this.id=id;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigInteger getQtdePrevista() {
		return qtdePrevista;
	}
	
	public void setQtdePrevista(BigInteger qtdePrevista) {
		this.qtdePrevista = qtdePrevista;
	}
	
	public BigInteger getQtdeEfetiva() {
		return qtdeEfetiva;
	}
	
	public void setQtdeEfetiva(BigInteger qtdeEfetiva) {
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

	public List<MovimentoEstoqueCota> getMovimentosEstoqueCota() {
		return movimentosEstoqueCota;
	}

	public void setMovimentosEstoqueCota(List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		this.movimentosEstoqueCota = movimentosEstoqueCota;
	}

	/**
	 * @return the itemNotaEnvios
	 */
	public List<ItemNotaEnvio> getItemNotaEnvios() {
		return itemNotaEnvios;
	}

	/**
	 * @param itemNotaEnvios the itemNotaEnvios to set
	 */
	public void setItemNotaEnvios(List<ItemNotaEnvio> itemNotaEnvios) {
		this.itemNotaEnvios = itemNotaEnvios;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public BigInteger getReparte() {
		return reparte;
	}
	
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

}
