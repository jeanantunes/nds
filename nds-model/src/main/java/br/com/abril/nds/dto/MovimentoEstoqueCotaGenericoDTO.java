package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class MovimentoEstoqueCotaGenericoDTO implements Serializable {

	private Long idCota;
	
	private Long idProdutoEdicao;
	
	private BigInteger qtde;
	
	private Long idChamadaEncalhe;

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public Long getIdChamadaEncalhe() {
		return idChamadaEncalhe;
	}

	public void setIdChamadaEncalhe(Long idChamadaEncalhe) {
		this.idChamadaEncalhe = idChamadaEncalhe;
	}
	
	
	
}
