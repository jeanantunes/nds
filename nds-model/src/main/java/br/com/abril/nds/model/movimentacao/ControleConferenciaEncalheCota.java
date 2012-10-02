package br.com.abril.nds.model.movimentacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Entidade que abstrai a sinalização do status da 
 * conferência de encalhe da cota.
 * 
 * @author Discover Technology 
 */
@Entity
@Table(name = "CONTROLE_CONFERENCIA_ENCALHE_COTA")
@SequenceGenerator(name="CTRL_CONF_ENCALHE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ControleConferenciaEncalheCota {
	
	@Id
	@GeneratedValue(generator = "CTRL_CONF_ENCALHE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INICIO", nullable = false)
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_FIM", nullable = false)
	private Date dataFim;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	
	@Column(name = "NUMERO_SLIP")
	private Long numeroSlip; 
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusOperacao status;

	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CTRL_CONF_ENCALHE_ID")
	private ControleConferenciaEncalhe controleConferenciaEncalhe;
	
	@OneToOne(optional = true)
	@JoinColumn(name="NOTA_FISCAL_ENTRADA_COTA_ID")
	private NotaFiscalEntradaCota notaFiscalEntradaCota;

	@OneToMany(mappedBy = "controleConferenciaEncalheCota")
	private List<ConferenciaEncalhe> conferenciasEncalhe = new ArrayList<ConferenciaEncalhe>();

	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	@ManyToOne(optional = false)
	@JoinColumn(name = "BOX_ID")
	private Box box;

	/**
	 * Obtém id
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
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
	 * @param dataFim 
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * Obtém dataOperacao
	 *
	 * @return Date
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * Atribuí dataOperacao
	 * @param dataOperacao 
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	/**
	 * Obtém status
	 *
	 * @return StatusOperacao
	 */
	public StatusOperacao getStatus() {
		return status;
	}

	/**
	 * Atribuí status
	 * @param status 
	 */
	public void setStatus(StatusOperacao status) {
		this.status = status;
	}
	
	/**
	 * Obtém cota
	 *
	 * @return Cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * Atribuí cota
	 * @param cota 
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * Obtém controleConferenciaEncalhe
	 *
	 * @return ControleConferenciaEncalhe
	 */
	public ControleConferenciaEncalhe getControleConferenciaEncalhe() {
		return controleConferenciaEncalhe;
	}

	/**
	 * Atribuí controleConferenciaEncalhe
	 * @param controleConferenciaEncalhe 
	 */
	public void setControleConferenciaEncalhe(
			ControleConferenciaEncalhe controleConferenciaEncalhe) {
		this.controleConferenciaEncalhe = controleConferenciaEncalhe;
	}

	public NotaFiscalEntradaCota getNotaFiscalEntradaCota() {
		return notaFiscalEntradaCota;
	}

	public void setNotaFiscalEntradaCota(NotaFiscalEntradaCota notaFiscalEntradaCota) {
		this.notaFiscalEntradaCota = notaFiscalEntradaCota;
	}

	/**
	 * Obtém conferenciasEncalhe
	 *
	 * @return List<ConferenciaEncalhe>
	 */
	public List<ConferenciaEncalhe> getConferenciasEncalhe() {
		return conferenciasEncalhe;
	}

	/**
	 * Atribuí conferenciasEncalhe
	 * @param conferenciasEncalhe 
	 */
	public void setConferenciasEncalhe(List<ConferenciaEncalhe> conferenciasEncalhe) {
		this.conferenciasEncalhe = conferenciasEncalhe;
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
	 * @param usuario 
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtém box
	 *
	 * @return Box
	 */
	public Box getBox() {
		return box;
	}

	/**
	 * Atribuí box
	 * @param box 
	 */
	public void setBox(Box box) {
		this.box = box;
	}

	/**
	 * @return the numeroSlip
	 */
	public Long getNumeroSlip() {
		return numeroSlip;
	}

	/**
	 * @param numeroSlip the numeroSlip to set
	 */
	public void setNumeroSlip(Long numeroSlip) {
		this.numeroSlip = numeroSlip;
	}
	
}
