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
	
}