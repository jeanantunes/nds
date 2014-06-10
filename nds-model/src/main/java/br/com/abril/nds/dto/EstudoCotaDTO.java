package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.planejamento.TipoEstudoCota;

public class EstudoCotaDTO implements Serializable {

	private static final long serialVersionUID = 1031571090326764200L;
	
	private Long id;
	private BigInteger qtdeEfetiva;
	private Long idCota;
	private Long idFornecedorPadraoCota;
	
	private TipoEstudoCota tipoEstudo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getQtdeEfetiva() {
		return qtdeEfetiva;
	}

	public void setQtdeEfetiva(BigInteger qtdeEfetiva) {
		this.qtdeEfetiva = qtdeEfetiva;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TipoEstudoCota getTipoEstudo() {
		return tipoEstudo;
	}

	public void setTipoEstudo(TipoEstudoCota tipoEstudo) {
		this.tipoEstudo = tipoEstudo;
	}

	public Long getIdFornecedorPadraoCota() {
		return idFornecedorPadraoCota;
	}

	public void setIdFornecedorPadraoCota(Long idFornecedorPadraoCota) {
		this.idFornecedorPadraoCota = idFornecedorPadraoCota;
	}
	
	
	
	
}
