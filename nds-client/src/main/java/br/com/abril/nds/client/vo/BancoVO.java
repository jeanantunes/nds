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
    String moeda;
    String carteira;
    BigDecimal juros;
    boolean ativo;
    BigDecimal multa;
    String instrucoes;
    
    public BancoVO(){
    	
    }
    

	public BancoVO(long idBanco, String numero, String nome,
			String codigoCedente, Long agencia, Long conta, String digito,
			String moeda, String carteira, BigDecimal juros, boolean ativo,
			BigDecimal multa, String instrucoes) {
		super();
		this.idBanco = idBanco;
		this.numero = numero;
		this.nome = nome;
		this.codigoCedente = codigoCedente;
		this.agencia = agencia;
		this.conta = conta;
		this.digito = digito;
		this.moeda = moeda;
		this.carteira = carteira;
		this.juros = juros;
		this.ativo = ativo;
		this.multa = multa;
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

	public String getMoeda() {
		return moeda;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
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
		result = prime * result + (ativo ? 1231 : 1237);
		result = prime * result
				+ ((carteira == null) ? 0 : carteira.hashCode());
		result = prime * result
				+ ((codigoCedente == null) ? 0 : codigoCedente.hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((digito == null) ? 0 : digito.hashCode());
		result = prime * result + (int) (idBanco ^ (idBanco >>> 32));
		result = prime * result
				+ ((instrucoes == null) ? 0 : instrucoes.hashCode());
		result = prime * result + ((juros == null) ? 0 : juros.hashCode());
		result = prime * result + ((moeda == null) ? 0 : moeda.hashCode());
		result = prime * result + ((multa == null) ? 0 : multa.hashCode());
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
		if (ativo != other.ativo)
			return false;
		if (carteira == null) {
			if (other.carteira != null)
				return false;
		} else if (!carteira.equals(other.carteira))
			return false;
		if (codigoCedente == null) {
			if (other.codigoCedente != null)
				return false;
		} else if (!codigoCedente.equals(other.codigoCedente))
			return false;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (digito == null) {
			if (other.digito != null)
				return false;
		} else if (!digito.equals(other.digito))
			return false;
		if (idBanco != other.idBanco)
			return false;
		if (instrucoes == null) {
			if (other.instrucoes != null)
				return false;
		} else if (!instrucoes.equals(other.instrucoes))
			return false;
		if (juros == null) {
			if (other.juros != null)
				return false;
		} else if (!juros.equals(other.juros))
			return false;
		if (moeda == null) {
			if (other.moeda != null)
				return false;
		} else if (!moeda.equals(other.moeda))
			return false;
		if (multa == null) {
			if (other.multa != null)
				return false;
		} else if (!multa.equals(other.multa))
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