package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;

public class CopiaProporcionalDeDistribuicaoVO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7667148595172234162L;

	private Long idLancamento;
	
	private Long idEstudo;
	
	private boolean fixacao; 
	
	private BigInteger pacotePadrao; 
	
	private BigInteger reparteDistribuido;
	
	public CopiaProporcionalDeDistribuicaoVO(Long idEstudo, boolean fixacao, BigInteger pacotePadrao, BigInteger reparteDistribuido) {
		
		this.pacotePadrao = pacotePadrao;
		this.fixacao = fixacao;
		this.idEstudo = idEstudo;
		this.reparteDistribuido = reparteDistribuido;
	}
	
	public CopiaProporcionalDeDistribuicaoVO() {
		// TODO Auto-generated constructor stub
	}

	public Long getIdEstudo() {
		return idEstudo;
	}

	public void setIdEstudo(Long idEstudo) {
		this.idEstudo = idEstudo;
	}

	public boolean isFixacao() {
		return fixacao;
	}

	public void setFixacao(boolean fixacao) {
		this.fixacao = fixacao;
	}

	public BigInteger getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(BigInteger pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public BigInteger getReparteDistribuido() {
		return reparteDistribuido;
	}

	public void setReparteDistribuido(BigInteger reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
	}

	public Long getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return true;
	}

}
