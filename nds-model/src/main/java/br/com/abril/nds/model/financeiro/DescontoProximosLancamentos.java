package br.com.abril.nds.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "DESCONTO_PROXIMOS_LANCAMENTOS")
@SequenceGenerator(name="DESCONTO_PROXIMOS_LANCAMENTOS_SEQ", initialValue = 1, allocationSize = 1)
public class DescontoProximosLancamentos implements Serializable {

	private static final long serialVersionUID = -6826873084437759107L;

	@Id
	@GeneratedValue(generator="DESCONTO_PROXIMOS_LANCAMENTOS_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "PRODUTO_ID", nullable = true)
	private Produto produto;
	
	@Column(name = "QUANTIDADE_PROXIMOS_LANCAMENTOS", nullable = true)
	private Integer quantidadeProximosLancamaentos;
	
	@Column(name ="VALOR_DESCONTO", nullable = true)
	private BigDecimal valorDesconto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="DATA_INICIO_DESCONTO", nullable = true)
	private Date dataInicioDesconto;
	
	@ManyToMany
	@JoinTable(name = "DESCONTO_LANCAMENTO_COTA", joinColumns = {@JoinColumn(name = "DESCONTO_LANCAMENTO_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "COTA_ID")})
	private Set<Cota> cotas = new HashSet<Cota>();
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	
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
	 * @return the produto
	 */
	public Produto getProduto() {
		return produto;
	}

	/**
	 * @param produto the produto to set
	 */
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	/**
	 * @return the quantidadeProximosLancamaentos
	 */
	public Integer getQuantidadeProximosLancamaentos() {
		return quantidadeProximosLancamaentos;
	}

	/**
	 * @param quantidadeProximosLancamaentos the quantidadeProximosLancamaentos to set
	 */
	public void setQuantidadeProximosLancamaentos(
			Integer quantidadeProximosLancamaentos) {
		this.quantidadeProximosLancamaentos = quantidadeProximosLancamaentos;
	}

	/**
	 * @return the valorDesconto
	 */
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	/**
	 * @return the dataInicioDesconto
	 */
	public Date getDataInicioDesconto() {
		return dataInicioDesconto;
	}

	/**
	 * @param dataInicioDesconto the dataInicioDesconto to set
	 */
	public void setDataInicioDesconto(Date dataInicioDesconto) {
		this.dataInicioDesconto = dataInicioDesconto;
	}

	/**
	 * @return the cotas
	 */
	public Set<Cota> getCotas() {
		return cotas;
	}

	/**
	 * @param cotas the cotas to set
	 */
	public void setCotas(Set<Cota> cotas) {
		this.cotas = cotas;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the distribuidor
	 */
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	/**
	 * @param distribuidor the distribuidor to set
	 */
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
	
}
