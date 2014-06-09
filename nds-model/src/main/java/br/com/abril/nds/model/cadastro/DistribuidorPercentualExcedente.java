package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

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

import br.com.abril.nds.model.EficienciaEnum;

@Entity
@Table(name = "DISTRIBUIDOR_PERCENTUAL_EXCEDENTE")
@SequenceGenerator(name = "DISTRIBUIDOR_PERCENTUAL_EXCEDENTE_SEQ", initialValue = 1, allocationSize = 1)
public class DistribuidorPercentualExcedente implements Serializable {

	private static final long serialVersionUID = 797497271334056763L;

	@Id
	@GeneratedValue(generator = "DISTRIBUIDOR_PERCENTUAL_EXCEDENTE_SEQ")
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;

	@Enumerated(EnumType.STRING)
	@Column(name = "EFICIENCIA")
	private EficienciaEnum eficiencia;

	@Column(name = "VENDA", nullable = false)
	private Integer venda;

	@Column(name = "PDV", nullable = false)
	private Integer pdv;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public EficienciaEnum getEficiencia() {
		return eficiencia;
	}

	public void setEficiencia(EficienciaEnum eficiencia) {
		this.eficiencia = eficiencia;
	}

	public Integer getVenda() {
		return venda;
	}

	public void setVenda(Integer venda) {
		this.venda = venda;
	}

	public Integer getPdv() {
		return pdv;
	}

	public void setPdv(Integer pdv) {
		this.pdv = pdv;
	}

}
