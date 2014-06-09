package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class verificadorFixacaoDTO implements Serializable {

	private static final long serialVersionUID = -4914972208117581441L;
	
	private String codICDFixacao;
	private BigInteger idLancamento;
	
	public String getCodICDFixacao() {
		return codICDFixacao;
	}

	public void setCodICDFixacao(String codICDFixacao) {
		
		if(codICDFixacao != null){
			this.codICDFixacao = codICDFixacao;
		}else{
			this.codICDFixacao = "0";
		}
		
	}

	public BigInteger getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(BigInteger idLancamento) {
		this.idLancamento = idLancamento;
	}
}
