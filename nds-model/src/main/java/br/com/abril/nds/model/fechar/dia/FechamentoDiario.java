package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "FECHAMENTO_DIARIO")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@Column(name="DATA_FECHAMENTO",unique=true)
	@Temporal(TemporalType.DATE)
	private Date dataFechamento; 
	
	@Column(name="USUARIO")
	private Usuario usuario;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private HistoricoFechamentoDiarioConsolidadoReparte consolidadoReparte;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private HistoricoFechamentoDiarioConsolidadoEncalhe consolidadoEncalhe;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private HistoricoFechamentoDiarioConsolidadoSuplementar consolidadoSuplementar;
	
	@OneToMany(mappedBy = "fechamentoDiario")
	private List<HistoricoFechamentoDiarioConsolidadoDivida> consolidadoDividas;
	
	@OneToOne(mappedBy = "fechamentoDiario")
	private HistoricoFechamentoDiarioConsolidadoCota consolidadoCota;
	
	@OneToMany(mappedBy = "fechamentoDiario")
	private List<HistoricoFechamentoDiarioResumoConsignado> consolidadoResumoConsignado;
	
	@OneToMany(mappedBy = "fechamentoDiario")
	private List<HistoricoFechamentoDiarioResumoEstoque> consolidadoResumoEstoque;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public HistoricoFechamentoDiarioConsolidadoReparte getConsolidadoReparte() {
		return consolidadoReparte;
	}

	public void setConsolidadoReparte(HistoricoFechamentoDiarioConsolidadoReparte consolidadoReparte) {
		this.consolidadoReparte = consolidadoReparte;
	}

	public HistoricoFechamentoDiarioConsolidadoEncalhe getConsolidadoEncalhe() {
		return consolidadoEncalhe;
	}

	public void setConsolidadoEncalhe(
			HistoricoFechamentoDiarioConsolidadoEncalhe consolidadoEncalhe) {
		this.consolidadoEncalhe = consolidadoEncalhe;
	}

	public HistoricoFechamentoDiarioConsolidadoSuplementar getConsolidadoSuplementar() {
		return consolidadoSuplementar;
	}

	public void setConsolidadoSuplementar(
			HistoricoFechamentoDiarioConsolidadoSuplementar consolidadoSuplementar) {
		this.consolidadoSuplementar = consolidadoSuplementar;
	}

	public List<HistoricoFechamentoDiarioConsolidadoDivida> getConsolidadoDividas() {
		return consolidadoDividas;
	}

	public void setConsolidadoDividas(
			List<HistoricoFechamentoDiarioConsolidadoDivida> consolidadoDividas) {
		this.consolidadoDividas = consolidadoDividas;
	}

	public HistoricoFechamentoDiarioConsolidadoCota getConsolidadoCota() {
		return consolidadoCota;
	}

	public void setConsolidadoCota(
			HistoricoFechamentoDiarioConsolidadoCota consolidadoCota) {
		this.consolidadoCota = consolidadoCota;
	}

	public List<HistoricoFechamentoDiarioResumoConsignado> getConsolidadoResumoConsignado() {
		return consolidadoResumoConsignado;
	}

	public void setConsolidadoResumoConsignado(
			List<HistoricoFechamentoDiarioResumoConsignado> consolidadoResumoConsignado) {
		this.consolidadoResumoConsignado = consolidadoResumoConsignado;
	}

	public List<HistoricoFechamentoDiarioResumoEstoque> getConsolidadoResumoEstoque() {
		return consolidadoResumoEstoque;
	}

	public void setConsolidadoResumoEstoque(
			List<HistoricoFechamentoDiarioResumoEstoque> consolidadoResumoEstoque) {
		this.consolidadoResumoEstoque = consolidadoResumoEstoque;
	}
	
}
