package br.com.abril.nds.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "INDICADOR")
@SequenceGenerator(name="INDICADOR_SEQ", initialValue = 1, allocationSize = 1)
public class Indicador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 377242597916336051L;

	@Id
	@GeneratedValue(generator = "INDICADOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VALOR")
	private String valor;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA")
	private Date data;
	
	@ManyToOne
	@JoinColumn(name = "DISTRIBUIDOR")
	private OperacaoDistribuidor operacaoDistribuidor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_INDICADOR")
	private TipoIndicador tipoIndicador;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "GRUPO_INDICADOR")
	private GrupoIndicador grupoIndicador;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FORMATO_INDICADOR")
	private FormatoIndicador formatoIndicador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public OperacaoDistribuidor getDistribuidor() {
		return operacaoDistribuidor;
	}

	public void setDistribuidor(OperacaoDistribuidor operacaoDistribuidor) {
		this.operacaoDistribuidor = operacaoDistribuidor;
	}

	public TipoIndicador getTipoIndicador() {
		return tipoIndicador;
	}

	public void setTipoIndicador(TipoIndicador tipoIndicador) {
		this.tipoIndicador = tipoIndicador;
	}

	public GrupoIndicador getGrupoIndicador() {
		return grupoIndicador;
	}

	public void setGrupoIndicador(GrupoIndicador grupoIndicador) {
		this.grupoIndicador = grupoIndicador;
	}

	public FormatoIndicador getFormatoIndicador() {
		return formatoIndicador;
	}

	public void setFormatoIndicador(FormatoIndicador formatoIndicador) {
		this.formatoIndicador = formatoIndicador;
	}
}