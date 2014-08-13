package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.util.upload.XlsMapper;

public class EstudoCotaDTO implements Serializable {

	private static final long serialVersionUID = 1031571090326764200L;
	
	private Long id;
	
	private Long idCota;
	private Long idFornecedorPadraoCota;
	private TipoEstudoCota tipoEstudo;
	
	private boolean devolveEncalhe;
	
	private TipoCota tipoCota;
	
	private boolean cotaContribuinteExigeNotaFiscal;

	private Cota cota;
	
	@XlsMapper(value="cota")
	private Integer numeroCota;
	
	@XlsMapper(value="reparte")
	private BigInteger qtdeEfetiva;
	
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

	public boolean isDevolveEncalhe() {
		return devolveEncalhe;
	}

	public void setDevolveEncalhe(boolean devolveEncalhe) {
		this.devolveEncalhe = devolveEncalhe;
	}

	public TipoCota getTipoCota() {
		return tipoCota;
	}

	public void setTipoCota(TipoCota tipoCota) {
		this.tipoCota = tipoCota;
	}

	public boolean isCotaContribuinteExigeNotaFiscal() {
		return cotaContribuinteExigeNotaFiscal;
	}

	public void setCotaContribuinteExigeNotaFiscal(boolean cotaContribuinteExigeNotaFiscal) {
		this.cotaContribuinteExigeNotaFiscal = cotaContribuinteExigeNotaFiscal;
	}
		
	public Long getIdFornecedorPadraoCota() {
		return idFornecedorPadraoCota;
	}

	public void setIdFornecedorPadraoCota(Long idFornecedorPadraoCota) {
		this.idFornecedorPadraoCota = idFornecedorPadraoCota;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}
}