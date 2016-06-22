package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

public class BancoVO {

	long idBanco;
	
	String numero;
    
	String nome;
    
	long idPessoaCedente;
	
    String codigoCedente;
    
    String digitoCodigoCedente;
    
    Long agencia;
    
    String digitoAgencia;
    
    Long conta;
    
    String digito;
    
    String apelido;
    
    Integer carteira;
    
    BigDecimal juros;
    
    boolean ativo;
    
    BigDecimal multa;
    
    BigDecimal vrMulta;
    
    String instrucoes1;
    
    String instrucoes2;
    
    String instrucoes3;
    
    String instrucoes4;
    
    String convenio;
    
    public BancoVO() {
    	
    }

	public BancoVO(long idBanco, String numero, String nome,
			String codigoCedente, Long agencia, String digitoAgencia,
			Long conta, String digito, String apelido, Integer carteira,
			BigDecimal juros, boolean ativo, BigDecimal multa,
			BigDecimal vrMulta, String instrucoes1, String instrucoes2, String instrucoes3, String instrucoes4, String convenio) {
		super();
		this.idBanco = idBanco;
		this.numero = numero;
		this.nome = nome;
		this.codigoCedente = codigoCedente;
		this.agencia = agencia;
		this.digitoAgencia = digitoAgencia;
		this.conta = conta;
		this.digito = digito;
		this.apelido = apelido;
		this.carteira = carteira;
		this.juros = juros;
		this.ativo = ativo;
		this.multa = multa;
		this.vrMulta = vrMulta;
		this.instrucoes1 = instrucoes1;
		this.instrucoes2 = instrucoes2;
		this.instrucoes3 = instrucoes3;
		this.instrucoes4 = instrucoes4;
		this.convenio = convenio;
	}


	public long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(long idBanco) {
		this.idBanco = idBanco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getIdPessoaCedente() {
		return idPessoaCedente;
	}

	public void setIdPessoaCedente(long idPessoaCedente) {
		this.idPessoaCedente = idPessoaCedente;
	}

	public String getCodigoCedente() {
		return codigoCedente;
	}

	public void setCodigoCedente(String codigoCedente) {
		this.codigoCedente = codigoCedente;
	}

	public String getDigitoCodigoCedente() {
		return digitoCodigoCedente;
	}

	public void setDigitoCodigoCedente(String digitoCodigoCedente) {
		this.digitoCodigoCedente = digitoCodigoCedente;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}

	/**
	 * @return the digitoAgencia
	 */
	public String getDigitoAgencia() {
		return digitoAgencia;
	}


	/**
	 * @param digitoAgencia the digitoAgencia to set
	 */
	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}


	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	
	public Integer getCarteira() {
		return carteira;
	}

	public void setCarteira(Integer carteira) {
		this.carteira = carteira;
	}

	public BigDecimal getJuros() {
		return juros;
	}

	public void setJuros(BigDecimal juros) {
		this.juros = juros;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public BigDecimal getMulta() {
		return multa;
	}

	public void setMulta(BigDecimal multa) {
		this.multa = multa;
	}
	
	public BigDecimal getVrMulta() {
		return vrMulta;
	}

	public void setVrMulta(BigDecimal vrMulta) {
		this.vrMulta = vrMulta;
	}

	public String getInstrucoes1() {
		return instrucoes1;
	}

	public void setInstrucoes1(String instrucoes1) {
		this.instrucoes1 = instrucoes1;
	}

	public String getInstrucoes2() {
		return instrucoes2;
	}

	public void setInstrucoes2(String instrucoes2) {
		this.instrucoes2 = instrucoes2;
	}

	public String getInstrucoes3() {
		return instrucoes3;
	}

	public void setInstrucoes3(String instrucoes3) {
		this.instrucoes3 = instrucoes3;
	}

	public String getInstrucoes4() {
		return instrucoes4;
	}

	public void setInstrucoes4(String instrucoes4) {
		this.instrucoes4 = instrucoes4;
	}
	
	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agencia == null) ? 0 : agencia.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		BancoVO other = (BancoVO) obj;
		if (agencia == null) {
			if (other.agencia != null)
				return false;
		} else if (!agencia.equals(other.agencia))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

}