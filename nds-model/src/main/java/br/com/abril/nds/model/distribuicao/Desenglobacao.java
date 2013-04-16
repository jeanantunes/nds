package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Column(name = "COTA_ID_DESENGLOBADA")
	private Long desenglobaNumeroCota;
	
	@Column(name = "NOME_COTA_DESENGLOBADA")
	private String desenglobaNomePessoa;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_PDV_ID")
	private TipoPontoPDV tipoPDV;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@Column(name = "COTA_ID_ENGLOBADA")
	private Long englobadaNumeroCota;
	
	@Column(name = "NOME_COTA_ENGLOBADA")
	private String englobadaNomePessoa;
	
	@Column(name = "PORCENTAGEM_COTA_ENGLOBADA")
	private Float englobadaPorcentagemCota;

	@Column (name = "DATA_ALTERACAO")
	private Date dataAlteracao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDesenglobaNumeroCota() {
		return desenglobaNumeroCota;
	}

	public void setDesenglobaNumeroCota(Long desenglobaNumeroCota) {
		this.desenglobaNumeroCota = desenglobaNumeroCota;
	}

	public String getDesenglobaNomePessoa() {
		return desenglobaNomePessoa;
	}

	public void setDesenglobaNomePessoa(String desenglobaNomePessoa) {
		this.desenglobaNomePessoa = desenglobaNomePessoa;
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

	public Long getEnglobadaNumeroCota() {
		return englobadaNumeroCota;
	}

	public void setEnglobadaNumeroCota(Long englobadaNumeroCota) {
		this.englobadaNumeroCota = englobadaNumeroCota;
	}

	public String getEnglobadaNomePessoa() {
		return englobadaNomePessoa;
	}

	public void setEnglobadaNomePessoa(String englobadaNomePessoa) {
		this.englobadaNomePessoa = englobadaNomePessoa;
	}

	public Float getEnglobadaPorcentagemCota() {
		return englobadaPorcentagemCota;
	}

	public void setEnglobadaPorcentagemCota(Float englobadaPorcentagemCota) {
		this.englobadaPorcentagemCota = englobadaPorcentagemCota;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
}
