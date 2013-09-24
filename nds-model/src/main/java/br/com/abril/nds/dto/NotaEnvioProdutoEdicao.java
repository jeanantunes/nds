package br.com.abril.nds.dto;

import java.math.BigInteger;
import java.util.Date;

public class NotaEnvioProdutoEdicao {
	
	private Integer numeroNotaEnvio;
	private Date dataEmissao;
	private Long idProdutoEdicao;
	private BigInteger reparte;

	public Integer getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Integer numeroNotaEnvio) {
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
	
	
}
