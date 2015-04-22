package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "CONTRATO")
@SequenceGenerator(name="CONTRATO_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class Contrato implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3981273457575929246L;
	
	@Id
	@GeneratedValue(generator = "CONTRATO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DATA_INICIO")
	private Date dataInicio;
	
	@Column(name = "DATA_TERMINO")
	private Date dataTermino;
	
	@Column(name = "PRAZO")
	private Integer prazo;
	
	@Column(name = "AVISO_PREVIO_RESCISAO")
	private Integer avisoPrevioRescisao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public Integer getAvisoPrevioRescisao() {
		return avisoPrevioRescisao;
	}

	public void setAvisoPrevioRescisao(Integer avisoPrevioRescisao) {
		this.avisoPrevioRescisao = avisoPrevioRescisao;
	}

}