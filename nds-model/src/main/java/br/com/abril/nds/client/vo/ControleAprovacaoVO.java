package br.com.abril.nds.client.vo;


/**
 * Value Object para controle de aprovações.
 * 
 * @author Discover Technology
 */
public class ControleAprovacaoVO {

	private String id;
	
	private String tipoMovimento;
	
	private String dataMovimento;
	
	private String numeroCota;
	
	private String nomeCota;
	
	private String valor;
	
	private String parcelas;
	
	private String prazo;
	
	private String requerente;

	private String status;
	
	private String motivo;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the tipoMovimento
	 */
	public String getTipoMovimento() {
		return tipoMovimento;
	}

	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * @return the dataMovimento
	 */
	public String getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * @return the numeroCota
	 */
	public String getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(String numeroCota) {
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
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the parcelas
	 */
	public String getParcelas() {
		return parcelas;
	}

	/**
	 * @param parcelas the parcelas to set
	 */
	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	/**
	 * @return the prazo
	 */
	public String getPrazo() {
		return prazo;
	}

	/**
	 * @param prazo the prazo to set
	 */
	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	/**
	 * @return the requerente
	 */
	public String getRequerente() {
		return requerente;
	}

	/**
	 * @param requerente the requerente to set
	 */
	public void setRequerente(String requerente) {
		this.requerente = requerente;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
