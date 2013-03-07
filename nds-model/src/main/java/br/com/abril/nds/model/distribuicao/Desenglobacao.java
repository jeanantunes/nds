package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "DESENGLOBACAO")
@SequenceGenerator(name = "DESENGLOBACAO_SEQ", initialValue = 1, allocationSize = 1)
public class Desenglobacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -670097663318412728L;
	
	@Id
	@GeneratedValue(generator = "DESENGLOBACAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "COTA_ID")
	private Long idCota;
	
	@Column(name = "NOME_COTA")
	private String nomeCota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_PDV_ID")
	private TipoPontoPDV tipoPDV;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@Column(name = "PORCENTAGEM_COTA")
	private Float porcentagemCota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public TipoPontoPDV getTipoPDV() {
		return tipoPDV;
	}

	public void setTipoPDV(TipoPontoPDV tipoPDV) {
		this.tipoPDV = tipoPDV;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Float getPorcentagemCota() {
		return porcentagemCota;
	}

	public void setPorcentagemCota(Float porcentagemCota) {
		this.porcentagemCota = porcentagemCota;
	}
}
