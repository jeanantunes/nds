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
		result = prime * result + ((this.getDataInicioVigencia() == null) ? 0 : this.getDataInicioVigencia().hashCode());
		result = prime * result + ((this.getDataProcessamento() == null) ? 0 : this.getDataProcessamento().hashCode());
		result = prime * result + ((this.getDescricao() == null) ? 0 : this.getDescricao().hashCode());
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getPercentualDesconto() == null) ? 0 : this.getPercentualDesconto().hashCode());
		result = prime * result + ((this.getPercentualPrestacaoServico() == null) ? 0 : this.getPercentualPrestacaoServico().hashCode());
		result = prime * result + ((this.getTipoDesconto() == null) ? 0 : this.getTipoDesconto().hashCode());
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
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getDataInicioVigencia() == null) {
			if (other.getDataInicioVigencia() != null)
				return false;
		} else if (!this.getDataInicioVigencia().equals(other.getDataInicioVigencia()))
			return false;
		if (this.getDataProcessamento() == null) {
			if (other.getDataProcessamento() != null)
				return false;
		} else if (!this.getDataProcessamento().equals(other.getDataProcessamento()))
			return false;
		if (this.getDescricao() == null) {
			if (other.getDescricao() != null)
				return false;
		} else if (!this.getDescricao().equals(other.getDescricao()))
			return false;
		if (this.getPercentualDesconto() == null) {
			if (other.getPercentualDesconto() != null)
				return false;
		} else if (!this.getPercentualDesconto().equals(other.getPercentualDesconto()))
			return false;
		if (this.getPercentualPrestacaoServico() == null) {
			if (other.getPercentualPrestacaoServico() != null)
				return false;
		} else if (!this.getPercentualPrestacaoServico()
				.equals(other.getPercentualPrestacaoServico()))
			return false;
		if (this.getTipoDesconto() == null) {
			if (other.getTipoDesconto() != null)
				return false;
		} else if (!this.getTipoDesconto().equals(other.getTipoDesconto()))
			return false;
		return true;
	}
}
