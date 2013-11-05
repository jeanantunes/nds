package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.Date;

public class NotaEnvioProdutoEdicao {
	
	private BigInteger numeroNotaEnvio;
	private Date dataEmissao;
	private Date dataConsignacao;
	private Long idProdutoEdicao;
	private BigInteger reparte;

	public BigInteger getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(BigInteger numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public Date getDataConsignacao() {
		return dataConsignacao;
	}

	public void setDataConsignacao(Date dataConsignacao) {
		this.dataConsignacao = dataConsignacao;
	}
	
	
}
