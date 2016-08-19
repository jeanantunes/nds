																			package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProdutoEdicaoVendaMediaDTO implements Serializable {

    private static final long serialVersionUID = -2108084689144884556L;

    private BigInteger id;
    private BigInteger numeroEdicao;
    private BigInteger idProduto;
    private String codigoProduto;
    private String nome;
    private Integer periodo;
    private boolean parcial;
    private boolean isParcialConsolidado;
    private String dataLancamentoFormatada;
    private BigInteger reparte;
    private BigInteger venda;
    private String status;
    private String classificacao;
    private BigInteger idClassificacao;
    private BigInteger idLancamento;
    
    private BigDecimal percentualVenda;

    private BigDecimal indicePeso;

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public Integer getPeriodo() {
	return periodo;
    }

    public void setPeriodo(Integer periodo) {
	this.periodo = periodo;
    }

    public Date getDataLancamento() {
	try {
	    return new SimpleDateFormat("dd/MM/yyyy")
	    .parse(dataLancamentoFormatada);
	} catch (ParseException e) {
	    return null;
	}
    }

    public String getDataLancamentoFormatada() {
	return dataLancamentoFormatada;
    }

    public void setDataLancamento(Date dataLancamento) {
	this.dataLancamentoFormatada = new SimpleDateFormat("dd/MM/yyyy")
	.format(dataLancamento);
    }

    public void setDataLancamentoFormatada(String dataLancamentoFormatada) {
	this.dataLancamentoFormatada = dataLancamentoFormatada;
    }

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public String getStatus() {
		return status;
    }

    public void setStatus(String status) {
    	this.status = status;
    }

    public String getClassificacao() {
	return classificacao;
    }

    public void setClassificacao(String classificacao) {
	this.classificacao = classificacao;
    }

    public String getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public boolean isParcial() {
	return parcial;
    }

    public void setParcial(Integer parcial) {
		if (parcial == null) {
		    this.parcial = false;
		} else {
		    this.parcial = parcial.equals(1);
		}
    }

	public BigDecimal getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(BigDecimal percentualVenda) {
		this.percentualVenda = percentualVenda;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(BigInteger idProduto) {
		this.idProduto = idProduto;
	}

	public BigInteger getIdClassificacao() {
		return idClassificacao;
	}

	public void setIdClassificacao(BigInteger idClassificacao) {
		this.idClassificacao = idClassificacao;
	}

	public BigDecimal getIndicePeso() {
		return indicePeso;
	}

	public void setIndicePeso(BigDecimal indicePeso) {
		this.indicePeso = indicePeso;
	}

	public boolean isParcialConsolidado() {
		return isParcialConsolidado;
	}

	public void setParcialConsolidado(Boolean isParcialConsolidado) {
		if (isParcialConsolidado == null || isParcialConsolidado == false) {
			this.isParcialConsolidado = false;
		} else {
			this.isParcialConsolidado = isParcialConsolidado;
		}
	}

	public BigInteger getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(BigInteger idLancamento) {
		this.idLancamento = idLancamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classificacao == null) ? 0 : classificacao.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime
				* result
				+ ((dataLancamentoFormatada == null) ? 0
						: dataLancamentoFormatada.hashCode());
		result = prime * result
				+ ((idClassificacao == null) ? 0 : idClassificacao.hashCode());
		result = prime * result
				+ ((idLancamento == null) ? 0 : idLancamento.hashCode());
		result = prime * result
				+ ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result
				+ ((indicePeso == null) ? 0 : indicePeso.hashCode());
		result = prime * result + (isParcialConsolidado ? 1231 : 1237);
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result + (parcial ? 1231 : 1237);
		result = prime * result
				+ ((percentualVenda == null) ? 0 : percentualVenda.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
		result = prime * result + ((reparte == null) ? 0 : reparte.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
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
		ProdutoEdicaoVendaMediaDTO other = (ProdutoEdicaoVendaMediaDTO) obj;
		if (classificacao == null) {
			if (other.classificacao != null)
				return false;
		} else if (!classificacao.equals(other.classificacao))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (dataLancamentoFormatada == null) {
			if (other.dataLancamentoFormatada != null)
				return false;
		} else if (!dataLancamentoFormatada
				.equals(other.dataLancamentoFormatada))
			return false;
		if (idClassificacao == null) {
			if (other.idClassificacao != null)
				return false;
		} else if (!idClassificacao.equals(other.idClassificacao))
			return false;
		if (idLancamento == null) {
			if (other.idLancamento != null)
				return false;
		} else if (!idLancamento.equals(other.idLancamento))
			return false;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		if (indicePeso == null) {
			if (other.indicePeso != null)
				return false;
		} else if (!indicePeso.equals(other.indicePeso))
			return false;
		if (isParcialConsolidado != other.isParcialConsolidado)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (parcial != other.parcial)
			return false;
		if (percentualVenda == null) {
			if (other.percentualVenda != null)
				return false;
		} else if (!percentualVenda.equals(other.percentualVenda))
			return false;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		if (reparte == null) {
			if (other.reparte != null)
				return false;
		} else if (!reparte.equals(other.reparte))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}
	
}