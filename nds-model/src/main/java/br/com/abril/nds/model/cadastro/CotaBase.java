package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COTA_BASE")
@SequenceGenerator(name="COTA_BASE_SEQ", initialValue = 1, allocationSize = 1)
public class CotaBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "COTA_BASE_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "DATA_INICIO")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@Column(name = "DATA_FIM")
	@Temporal(TemporalType.DATE)
	private Date dataFim;
	
	@Column(name = "INDICE_AJUSTE", precision=18, scale=4)
	private BigDecimal indiceAjuste;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public BigDecimal getIndiceAjuste() {
		return indiceAjuste;
	}

	public void setIndiceAjuste(BigDecimal indiceAjuste) {
		this.indiceAjuste = indiceAjuste;
	}
	

}
