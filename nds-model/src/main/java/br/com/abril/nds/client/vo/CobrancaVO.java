package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CobrancaVO implements Serializable  {
    
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5988272381369010529L;

	
	//BOLETO
	private String codigo;
	
	@Export(label = "Cota", widthPercent=5)
	private Integer numeroCota;
	
	@Export(label = "Nome")
	private String nome;
	
	@Export(label = "Data Emissão")
	private String dataEmissao;
	
	@Export(label = "Data Vencimento")
	private String dataVencimento;
	
	@Export(label = "Valor Dívida R$")
	private String valor;
	
	private String cota;
	private String banco;
	private String nossoNumero;	
	
	//DIVIDA
	private String dividaTotal;
	private String dataPagamento;
	private String desconto;
	private String juros;
	private String valorTotal;
	private String dataOperacao;
	
	
	private boolean check;
	
	//MOVIMENTO FINANCEIRO
	private String multa;
	private String valorSaldo;
	
	private boolean boletoAntecipado;
	
	public CobrancaVO() {
		
	}
	
	public CobrancaVO(BigInteger codigo, Integer numeroCota, String nome, Date dataEmissao, 
					  Date dataVencimento, BigDecimal valor, boolean boletoAntecipado, String nossoNumero) {		
		this.numeroCota= numeroCota;
		this.codigo = codigo == null ? "" : codigo.toString();
		this.nome = nome;
		this.dataEmissao = DateUtil.formatarDataPTBR(dataEmissao);
		this.dataVencimento = DateUtil.formatarDataPTBR(dataVencimento);
		this.valor = CurrencyUtil.formatarValor(valor);
		this.boletoAntecipado = boletoAntecipado;
		this.nossoNumero = nossoNumero;
	}

	public CobrancaVO(BigInteger codigo, Date dataEmissao, Date dataVencimento, BigDecimal valor, String nossoNumero, Date dataOperacao) {		
		this.codigo = codigo == null ? "" : codigo.toString();
		this.dataEmissao = DateUtil.formatarDataPTBR(dataEmissao);
		this.dataVencimento = DateUtil.formatarDataPTBR(dataVencimento);
		this.valor = CurrencyUtil.formatarValor(valor);
		this.nossoNumero = nossoNumero;
		this.dataOperacao = DateUtil.formatarDataPTBR(dataOperacao);		
	}	

	public String getCota() {
		return cota;
	}

	public void setCota(String cota) {
		this.cota = cota;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}
	
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDividaTotal() {
		return dividaTotal;
	}

	public void setDividaTotal(String dividaTotal) {
		this.dividaTotal = dividaTotal;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public String getJuros() {
		return juros;
	}

	public void setJuros(String juros) {
		this.juros = juros;
	}

	public String getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getMulta() {
		return multa;
	}

	public void setMulta(String multa) {
		this.multa = multa;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(String valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((banco == null) ? 0 : banco.hashCode());
		result = prime * result + (check ? 1231 : 1237);
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((cota == null) ? 0 : cota.hashCode());
		result = prime * result
				+ ((dataEmissao == null) ? 0 : dataEmissao.hashCode());
		result = prime * result
				+ ((dataPagamento == null) ? 0 : dataPagamento.hashCode());
		result = prime * result
				+ ((dataVencimento == null) ? 0 : dataVencimento.hashCode());
		result = prime * result
				+ ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result
				+ ((dividaTotal == null) ? 0 : dividaTotal.hashCode());
		result = prime * result + ((juros == null) ? 0 : juros.hashCode());
		result = prime * result + ((multa == null) ? 0 : multa.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((nossoNumero == null) ? 0 : nossoNumero.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result
				+ ((valorSaldo == null) ? 0 : valorSaldo.hashCode());
		result = prime * result
				+ ((valorTotal == null) ? 0 : valorTotal.hashCode());
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
		CobrancaVO other = (CobrancaVO) obj;
		if (banco == null) {
			if (other.banco != null)
				return false;
		} else if (!banco.equals(other.banco))
			return false;
		if (check != other.check)
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (cota == null) {
			if (other.cota != null)
				return false;
		} else if (!cota.equals(other.cota))
			return false;
		if (dataEmissao == null) {
			if (other.dataEmissao != null)
				return false;
		} else if (!dataEmissao.equals(other.dataEmissao))
			return false;
		if (dataPagamento == null) {
			if (other.dataPagamento != null)
				return false;
		} else if (!dataPagamento.equals(other.dataPagamento))
			return false;
		if (dataVencimento == null) {
			if (other.dataVencimento != null)
				return false;
		} else if (!dataVencimento.equals(other.dataVencimento))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (dividaTotal == null) {
			if (other.dividaTotal != null)
				return false;
		} else if (!dividaTotal.equals(other.dividaTotal))
			return false;
		if (juros == null) {
			if (other.juros != null)
				return false;
		} else if (!juros.equals(other.juros))
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
		if (nossoNumero == null) {
			if (other.nossoNumero != null)
				return false;
		} else if (!nossoNumero.equals(other.nossoNumero))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (valorSaldo == null) {
			if (other.valorSaldo != null)
				return false;
		} else if (!valorSaldo.equals(other.valorSaldo))
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		return true;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the boletoAntecipado
	 */
	public boolean isBoletoAntecipado() {
		return boletoAntecipado;
	}

	/**
	 * @param boletoAntecipado the boletoAntecipado to set
	 */
	public void setBoletoAntecipado(boolean boletoAntecipado) {
		this.boletoAntecipado = boletoAntecipado;
	}

	public String getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
}