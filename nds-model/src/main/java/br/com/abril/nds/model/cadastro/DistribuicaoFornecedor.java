package br.com.abril.nds.model.cadastro;

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

import br.com.abril.nds.model.DiaSemana;

@Entity
@Table(name = "DISTRIBUICAO_FORNECEDOR")
@SequenceGenerator(name="DIST_FORN_SEQ", initialValue = 1, allocationSize = 1)
public class DistribuicaoFornecedor {
	
	@Id
	@GeneratedValue(generator = "DIST_FORN_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@Column(name = "DIA_SEMANA", nullable = false)
	private Integer codigoDiaSemana;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OPERACAO_DISTRIBUIDOR")
	private OperacaoDistribuidor operacaoDistribuidor;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public DiaSemana getDiaSemana() {
		return DiaSemana.getByCodigoDiaSemana(codigoDiaSemana);
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
		this.codigoDiaSemana = diaSemana.getCodigoDiaSemana();
	}
	
	public OperacaoDistribuidor getOperacaoDistribuidor() {
		return operacaoDistribuidor;
	}
	
	public void setOperacaoDistribuidor(
			OperacaoDistribuidor operacaoDistribuidor) {
		this.operacaoDistribuidor = operacaoDistribuidor;
	}
	
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistribuicaoFornecedor other = (DistribuicaoFornecedor) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder(fornecedor.getJuridica().getNomeFantasia())
				.append("-").append(getDiaSemana()).append("-")
				.append(operacaoDistribuidor).toString();
	}

}
