package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.util.Util;

/**
 * Data Transfer Object para consulta de movimento para aprovação.
 * 
 * @author Discover Technology
 */
public class MovimentoAprovacaoDTO {

	private Long idMovimento;
	
	private String descricaoTipoMovimento;
	
	private Date dataCriacao;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigDecimal valor;
	
	private Integer parcelas;
	
	private Integer prazo;
	
	private String nomeUsuarioRequerente;
	
	private StatusAprovacao statusMovimento;
	
	private String motivo;
	
	/**
	 * Contrutor padrão
	 */
	public MovimentoAprovacaoDTO() {
		
	}

	/**
	 * @return the idMovimento
	 */
	public Long getIdMovimento() {
		return idMovimento;
	}

	/**
	 * @param idMovimento the idMovimento to set
	 */
	public void setIdMovimento(Long idMovimento) {
		this.idMovimento = idMovimento;
	}

	/**
	 * @return the descricaoTipoMovimento
	 */
	public String getDescricaoTipoMovimento() {
		return descricaoTipoMovimento;
	}

	/**
	 * @param descricaoTipoMovimento the descricaoTipoMovimento to set
	 */
	public void setDescricaoTipoMovimento(String descricaoTipoMovimento) {
		this.descricaoTipoMovimento = descricaoTipoMovimento;
	}

	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao the dataCriacao to set
	 */
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the parcelas
	 */
	public Integer getParcelas() {
		return parcelas;
	}

	/**
	 * @param parcelas the parcelas to set
	 */
	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	/**
	 * @return the prazo
	 */
	public Integer getPrazo() {
		return prazo;
	}

	/**
	 * @param prazo the prazo to set
	 */
	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	/**
	 * @return the nomeUsuarioRequerente
	 */
	public String getNomeUsuarioRequerente() {
		return nomeUsuarioRequerente;
	}

	/**
	 * @param nomeUsuarioRequerente the nomeUsuarioRequerente to set
	 */
	public void setNomeUsuarioRequerente(String nomeUsuarioRequerente) {
		this.nomeUsuarioRequerente = nomeUsuarioRequerente;
	}

	/**
	 * @return the statusMovimento
	 */
	public StatusAprovacao getStatusMovimento() {
		return statusMovimento;
	}

	/**
	 * @param statusMovimento the statusMovimento to set
	 */
	public void setStatusMovimento(String statusMovimento) {
		
		this.statusMovimento = Util.getEnumByStringValue(StatusAprovacao.values(), statusMovimento);
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}