package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HISTORICO_DESCONTO_LOGISTICA")
@SequenceGenerator(name="HISTORICO_DESCONTO_LOGISTICA", initialValue = 1, allocationSize = 1)
public class HistoricoDescontoLogistica {
	
	@Id
	@GeneratedValue(generator = "HISTORICO_DESCONTO_LOGISTICA")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "TIPO_DESCONTO", nullable = false)
	private Integer tipoDesconto;
	
	@Column(name = "PERCENTUAL_DESCONTO", nullable = false, precision=18, scale=4)
	private BigDecimal percentualDesconto;
	
	@Column(name = "PERCENTUAL_PRESTACAO_SERVICO", nullable = false, precision=18, scale=4)
	private BigDecimal percentualPrestacaoServico;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO_VIGENCIA", nullable = false)
	private Date dataInicioVigencia;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_PROCESSAMENTO", nullable = true)
	private Date dataProcessamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(Integer tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	public BigDecimal getPercentualPrestacaoServico() {
		return percentualPrestacaoServico;
	}

	public void setPercentualPrestacaoServico(BigDecimal percentualPrestacaoServico) {
		this.percentualPrestacaoServico = percentualPrestacaoServico;
	}

	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dataInicioVigencia == null) ? 0 : dataInicioVigencia
						.hashCode());
		result = prime
				* result
				+ ((dataProcessamento == null) ? 0 : dataProcessamento
						.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((percentualDesconto == null) ? 0 : percentualDesconto
						.hashCode());
		result = prime
				* result
				+ ((percentualPrestacaoServico == null) ? 0
						: percentualPrestacaoServico.hashCode());
		result = prime * result
				+ ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
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
		HistoricoDescontoLogistica other = (HistoricoDescontoLogistica) obj;
		if (dataInicioVigencia == null) {
			if (other.dataInicioVigencia != null)
				return false;
		} else if (!dataInicioVigencia.equals(other.dataInicioVigencia))
			return false;
		if (dataProcessamento == null) {
			if (other.dataProcessamento != null)
				return false;
		} else if (!dataProcessamento.equals(other.dataProcessamento))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (percentualDesconto == null) {
			if (other.percentualDesconto != null)
				return false;
		} else if (!percentualDesconto.equals(other.percentualDesconto))
			return false;
		if (percentualPrestacaoServico == null) {
			if (other.percentualPrestacaoServico != null)
				return false;
		} else if (!percentualPrestacaoServico
				.equals(other.percentualPrestacaoServico))
			return false;
		if (tipoDesconto == null) {
			if (other.tipoDesconto != null)
				return false;
		} else if (!tipoDesconto.equals(other.tipoDesconto))
			return false;
		return true;
	}
}
