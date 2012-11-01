package br.com.abril.nds.integracao.icd.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.FetchMode;

@Entity
@Table(name = "DETALHE_FALTAS_SOBRAS")
public class DetalheFaltaSobra {

	@EmbeddedId
	private IcdPK icdPK;	
	
	@Column(name = "COD_SITUACAO_ACERTO", nullable = false)
	private String codigoAcerto;
	
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumns(value = { 
			@JoinColumn (name = "COD_DISTRIBUIDOR", referencedColumnName = "COD_DISTRIBUIDOR", insertable=false, updatable=false)
			, @JoinColumn (name = "DAT_SOLICITACAO", referencedColumnName = "DAT_SOLICITACAO", insertable=false, updatable=false)
			, @JoinColumn (name = "HRA_SOLICITACAO", referencedColumnName = "HRA_SOLICITACAO", insertable=false, updatable=false)
	})
	private MotivoSituacaoFaltaSobra motivoSituacaoFaltaSobra;

	/**
	 * @return the icdPK
	 */
	public IcdPK getIcdPK() {
		return icdPK;
	}

	/**
	 * @param icdPK the icdPK to set
	 */
	public void setIcdPK(IcdPK icdPK) {
		this.icdPK = icdPK;
	}

	/**
	 * @return the codigoAcerto
	 */
	public String getCodigoAcerto() {
		return codigoAcerto;
	}

	/**
	 * @param codigoAcerto the codigoAcerto to set
	 */
	public void setCodigoAcerto(String codigoAcerto) {
		this.codigoAcerto = codigoAcerto;
	}

	/**
	 * @return the motivoSituacaoFaltaSobra
	 */
	public MotivoSituacaoFaltaSobra getMotivoSituacaoFaltaSobra() {
		return motivoSituacaoFaltaSobra;
	}

	/**
	 * @param motivoSituacaoFaltaSobra the motivoSituacaoFaltaSobra to set
	 */
	public void setMotivoSituacaoFaltaSobra(
			MotivoSituacaoFaltaSobra motivoSituacaoFaltaSobra) {
		this.motivoSituacaoFaltaSobra = motivoSituacaoFaltaSobra;
	}

	
	
}
