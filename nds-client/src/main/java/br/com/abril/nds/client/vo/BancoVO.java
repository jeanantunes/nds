package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

public class BancoVO {

	long idBanco;
	String numero;
    String nome;
    String codigoCedente;
    Long agencia;
    Long conta;
    String digito;
    String apelido;
    Integer codigoCarteira;
    BigDecimal juros;
    boolean ativo;
    BigDecimal multa;
    BigDecimal vrMulta;
    String instrucoes;
    
    public BancoVO(){
    	
    }
    

	public BancoVO(long idBanco, String numero, String nome,
			String codigoCedente, Long agencia, Long conta, String digito,
			String apelido, Integer codigoCarteira, BigDecimal juros,
			boolean ativo, BigDecimal multa, BigDecimal vrMulta,
			String instrucoes) {
		super();
		this.idBanco = idBanco;
		this.numero = numero;
		this.nome = nome;
		this.codigoCedente = codigoCedente;
		this.agencia = agencia;
		this.conta = conta;
		this.digito = digito;
		this.apelido = apelido;
		this.codigoCarteira = codigoCarteira;
		this.juros = juros;
		this.ativo = ativo;
		this.multa = multa;
		this.vrMulta = vrMulta;
		this.instrucoes = instrucoes;
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

	public String getCodigoCedente() {
		return codigoCedente;
	}

	public void setCodigoCedente(String codigoCedente) {
		this.codigoCedente = codigoCedente;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
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

	public Integer getCodigoCarteira() {
		return codigoCarteira;
	}

	public void setCodigoCarteira(Integer codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
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

	public String getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
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