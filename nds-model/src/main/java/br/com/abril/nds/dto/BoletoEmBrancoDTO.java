package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BoletoEmBrancoDTO implements Serializable {

	private static final long serialVersionUID = -7936154707508705342L;
	
	private Long idBoletoAntecipado;
	
	private Long idChamadaEncalheCota;
	
	private Long idFornecedor;
	
	private String numeroBanco;
	
	private Long numeroAgencia;
	
	private Long numeroConta;
	
	private String digitoVerificadorConta;
	
	private Integer numeroCota;
    
	private BigDecimal valorReparteLiquido;
	
	private BigDecimal valorEncalhe;
	
	private BigDecimal valorTotalLiquido;
	
	private BigDecimal valorTotalDebitos;
	
	private BigDecimal valorTotalCreditos;
    
	private Date data;
    
    private Date dataRecolhimento;
    
    private Date dataVencimento;
    
    private Date dataRecolhimentoCEDe;
    
    private Date dataRecolhimentoCEAte;
    
    private String nossoNumero;
    
    private String digitoNossoNumero;
    
    public BoletoEmBrancoDTO(){
    	
    }

	/**
	 * @param idChamadaEncalheCota
	 * @param idFornecedor
	 * @param numeroBanco
	 * @param numeroAgencia
	 * @param numeroConta
	 * @param digitoVerificadorConta
	 * @param numeroCota
	 * @param valorReparteLiquido
	 * @param valorEncalhe
	 * @param valorTotalLiquido
	 * @param valorTotalDebitos
	 * @param valorTotalCreditos
	 * @param data
	 * @param dataRecolhimento
	 * @param dataVencimento
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 */
	public BoletoEmBrancoDTO(Long idChamadaEncalheCota, 
			                 Long idFornecedor,
			                 String numeroBanco, 
			                 Long numeroAgencia, 
			                 Long numeroConta,
			                 String digitoVerificadorConta,
			                 Integer numeroCota, 
			                 BigDecimal valorReparteLiquido,
			                 BigDecimal valorEncalhe,
			                 BigDecimal valorTotalLiquido,
			                 BigDecimal valorTotalDebitos, 
			                 BigDecimal valorTotalCreditos,
			                 Date data, 
			                 Date dataRecolhimento,
			                 Date dataVencimento,
			                 Date dataRecolhimentoCEDe,
			                 Date dataRecolhimentoCEAte) {
		super();
		this.idChamadaEncalheCota = idChamadaEncalheCota;
		this.idFornecedor = idFornecedor;
		this.numeroBanco = numeroBanco;
		this.numeroAgencia = numeroAgencia;
		this.numeroConta = numeroConta;
		this.numeroCota = numeroCota;
		this.digitoVerificadorConta = digitoVerificadorConta;
		this.valorReparteLiquido = valorReparteLiquido; 
		this.valorEncalhe = valorEncalhe; 
		this.valorTotalLiquido = valorTotalLiquido;
		this.valorTotalDebitos = valorTotalDebitos;
		this.valorTotalCreditos = valorTotalCreditos;
		this.data = data;
		this.dataRecolhimento = dataRecolhimento;
		this.dataVencimento = dataVencimento;
		this.dataRecolhimentoCEDe = dataRecolhimentoCEDe;
		this.dataRecolhimentoCEAte = dataRecolhimentoCEAte;
	}

	public Long getIdBoletoAntecipado() {
		return idBoletoAntecipado;
	}

	public void setIdBoletoAntecipado(Long idBoletoAntecipado) {
		this.idBoletoAntecipado = idBoletoAntecipado;
	}

	public Long getIdChamadaEncalheCota() {
		return idChamadaEncalheCota;
	}

	public void setIdChamadaEncalheCota(Long idChamadaEncalheCota) {
		this.idChamadaEncalheCota = idChamadaEncalheCota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public BigDecimal getValorReparteLiquido() {
		return valorReparteLiquido;
	}

	public void setValorReparteLiquido(BigDecimal valorReparteLiquido) {
		this.valorReparteLiquido = valorReparteLiquido;
	}

	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	public void setValorEncalhe(BigDecimal valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}

	public BigDecimal getValorTotalLiquido() {
		return valorTotalLiquido;
	}

	public void setValorTotalLiquido(BigDecimal valorTotalLiquido) {
		this.valorTotalLiquido = valorTotalLiquido;
	}

	public BigDecimal getValorTotalDebitos() {
		return valorTotalDebitos;
	}

	public void setValorTotalDebitos(BigDecimal valorTotalDebitos) {
		this.valorTotalDebitos = valorTotalDebitos;
	}

	public BigDecimal getValorTotalCreditos() {
		return valorTotalCreditos;
	}

	public void setValorTotalCreditos(BigDecimal valorTotalCreditos) {
		this.valorTotalCreditos = valorTotalCreditos;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public Long getNumeroAgencia() {
		return numeroAgencia;
	}

	public void setNumeroAgencia(Long numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public Long getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(Long numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getDigitoVerificadorConta() {
		return digitoVerificadorConta;
	}

	public void setDigitoVerificadorConta(String digitoVerificadorConta) {
		this.digitoVerificadorConta = digitoVerificadorConta;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public Date getDataRecolhimentoCEDe() {
		return dataRecolhimentoCEDe;
	}

	public void setDataRecolhimentoCEDe(Date dataRecolhimentoCEDe) {
		this.dataRecolhimentoCEDe = dataRecolhimentoCEDe;
	}

	public Date getDataRecolhimentoCEAte() {
		return dataRecolhimentoCEAte;
	}

	public void setDataRecolhimentoCEAte(Date dataRecolhimentoCEAte) {
		this.dataRecolhimentoCEAte = dataRecolhimentoCEAte;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}
}
