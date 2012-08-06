package br.com.abril.nds.integracao.ems0107.inbound;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import br.com.abril.nds.model.cadastro.Cota;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
@Entity
@Table(name = "ndsi_ems0107_tmp")
public class EMS0107Input implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column
	private String codigoPublicacao;

	@Column
	private Long edicao;

	@Column(name="NUMERO_COTA", nullable=true)
	private Long codigoCota;

	@Column
	private BigInteger quantidadeReparte;

	@Column
	private String repartePDV;

	@ForeignKey(name="fk_ndsi_ems0107_tmp_cota")
	@ManyToOne(optional=true)
	@JoinColumn(name = "NUMERO_COTA", insertable=false, updatable=false, nullable=true)
	private Cota cota;

	@Field(offset = 1, length = 8)
	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}

	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}

	@Field(offset = 9, length = 4)
	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	@Field(offset = 13, length = 4)
	public Long getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Long codigoCota) {
		this.codigoCota = codigoCota;
	}

	@Field(offset = 17, length = 8)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = false)
	public BigInteger getQuantidadeReparte() {
		return quantidadeReparte;
	}

	public void setQuantidadeReparte(BigInteger quantidadeReparte) {
		this.quantidadeReparte = quantidadeReparte;
	}

	@Field(offset = 25, length = 1)
	public String getRepartePDV() {
		return repartePDV;
	}

	public void setRepartePDV(String repartePDV) {
		this.repartePDV = repartePDV;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoCota == null) ? 0 : codigoCota.hashCode());
		result = prime
				* result
				+ ((codigoPublicacao == null) ? 0 : codigoPublicacao.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime
				* result
				+ ((quantidadeReparte == null) ? 0 : quantidadeReparte
						.hashCode());
		result = prime * result
				+ ((repartePDV == null) ? 0 : repartePDV.hashCode());
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
		EMS0107Input other = (EMS0107Input) obj;
		if (codigoCota == null) {
			if (other.codigoCota != null)
				return false;
		} else if (!codigoCota.equals(other.codigoCota))
			return false;
		if (codigoPublicacao == null) {
			if (other.codigoPublicacao != null)
				return false;
		} else if (!codigoPublicacao.equals(other.codigoPublicacao))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (quantidadeReparte == null) {
			if (other.quantidadeReparte != null)
				return false;
		} else if (!quantidadeReparte.equals(other.quantidadeReparte))
			return false;
		if (repartePDV == null) {
			if (other.repartePDV != null)
				return false;
		} else if (!repartePDV.equals(other.repartePDV))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EMS0107Input [codigoPublicacao=" + codigoPublicacao
				+ ", edicao=" + edicao + ", codigoCota=" + codigoCota
				+ ", quantidadeReparte=" + quantidadeReparte + ", repartePDV="
				+ repartePDV + "]";
	}
}