package br.com.abril.nds.model.planejamento;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "HISTORICO_CONF_CHAMADA_ENCALHE_COTA")
public class HistoricoConfChamadaEncalheCota {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INICIO", nullable = false)
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_FIM", nullable = false)
	private Date dataFim;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BOX_ID")
	private Box box;
	
	@Column(name = "VALOR_ENCALHE", nullable = false)
	private BigDecimal valorEncalhe;
	
	public HistoricoConfChamadaEncalheCota(Date dataInicio, Date dataFim, Usuario usuario, Cota cota, Date dataOperacao,
			Box box, BigDecimal valorEncalhe) {
		super();
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.usuario = usuario;
		this.cota = cota;
		this.dataOperacao = dataOperacao;
		this.box = box;
		this.valorEncalhe = valorEncalhe;
	}

	/*
	 * Obtém id
	 * 
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém dataInicio
	 * 
	 * @return Date
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * Atribuí dataInicio
	 * 
	 * @param dataInicio
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * Obtém dataFim
	 * 
	 * @return Date
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * Atribuí dataFim
	 * 
	 * @param dataFim
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * Obtém usuario
	 * 
	 * @return Usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Atribuí usuario
	 * 
	 * @param usuario
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	public void setValorEncalhe(BigDecimal valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}
}