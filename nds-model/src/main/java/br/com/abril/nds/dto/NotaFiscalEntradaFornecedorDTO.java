package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;

public class NotaFiscalEntradaFornecedorDTO implements Serializable {

	private static final long serialVersionUID = -4211336967127609972L;

	private Long id;
	
	private String numero;
	
	private String serie;
	
	private Long numeroNotaEnvio;
	
	private String dataEmissao;
	
	private String dataExpedicao;
	
	private String descricao;
	
	private String valorTotalNota;
	
	private String valorTotalNotaComDesconto;
	
	private String notaRecebida;
	
	private String razaoSocial;
	
	private String chaveAcesso;

	public NotaFiscalEntradaFornecedorDTO(Long id, Long numero,
			Date dataEmissao, Date dataExpedicao, String descricao,
			BigDecimal valorTotalNota, StatusNotaFiscalEntrada statusNotaFiscal, Date dataRecebimento, String razaoSocial) {
		this.id = id;
		this.numero = (numero == null) ? "" : numero.toString();
		this.dataEmissao = DateUtil.formatarDataPTBR(dataEmissao);
		this.dataExpedicao = DateUtil.formatarDataPTBR(dataExpedicao);
		this.descricao = StringUtils.defaultString(descricao);
		this.valorTotalNota = CurrencyUtil.formatarValor(MathUtil.round(valorTotalNota, 2));
		this.notaRecebida = 
				StatusNotaFiscalEntrada.RECEBIDA.equals(statusNotaFiscal) 
					? DateUtil.formatarDataPTBR(dataRecebimento)
					: " ";		
		this.razaoSocial = StringUtils.defaultString(razaoSocial);
	}
	
	public NotaFiscalEntradaFornecedorDTO(Long id, Long numero, Long serie, Long numeroNotaEnvio,
			Date dataEmissao, Date dataExpedicao, String descricao,
			BigDecimal valorTotalNota, StatusNotaFiscalEntrada statusNotaFiscal, Date dataRecebimento, String razaoSocial,String chaveAcesso) {
		this.id = id;
		this.numero = (numero == null) ? "" : numero.toString();
		this.serie = (serie == null) ? "" : serie.toString();
		this.numeroNotaEnvio = numeroNotaEnvio;
		this.dataEmissao = DateUtil.formatarDataPTBR(dataEmissao);
		this.dataExpedicao = DateUtil.formatarDataPTBR(dataExpedicao);
		this.descricao = StringUtils.defaultString(descricao);
		this.valorTotalNota = CurrencyUtil.formatarValor(MathUtil.round(valorTotalNota, 2));
		this.notaRecebida = 
				StatusNotaFiscalEntrada.RECEBIDA.equals(statusNotaFiscal) 
					? DateUtil.formatarDataPTBR(dataRecebimento)
					: " ";		
		this.razaoSocial = StringUtils.defaultString(razaoSocial);
		this.chaveAcesso = chaveAcesso;
	}

	public NotaFiscalEntradaFornecedorDTO(Long id, Long numero, Long serie, Long numeroNotaEnvio,
			Date dataEmissao, Date dataExpedicao, String descricao,
			BigDecimal valorTotalNota, BigDecimal valorTotalNotaComDesconto, 
			StatusNotaFiscalEntrada statusNotaFiscal, 
			Date dataRecebimento, String razaoSocial,String chaveAcesso) {
		this.id = id;
		this.numero = (numero == null) ? "" : numero.toString();
		this.serie = (serie == null) ? "" : serie.toString();
		this.numeroNotaEnvio = numeroNotaEnvio;
		this.dataEmissao = DateUtil.formatarDataPTBR(dataEmissao);
		this.dataExpedicao = DateUtil.formatarDataPTBR(dataExpedicao);
		this.descricao = StringUtils.defaultString(descricao);
		this.valorTotalNota = CurrencyUtil.formatarValor(MathUtil.round(valorTotalNota, 2));
		this.valorTotalNotaComDesconto = CurrencyUtil.formatarValorQuatroCasas(valorTotalNotaComDesconto);
		this.notaRecebida = 
				StatusNotaFiscalEntrada.RECEBIDA.equals(statusNotaFiscal) 
					? DateUtil.formatarDataPTBR(dataRecebimento)
					: " ";		
		this.razaoSocial = StringUtils.defaultString(razaoSocial);
		this.chaveAcesso = chaveAcesso;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getDataExpedicao() {
		return dataExpedicao;
	}

	public void setDataExpedicao(String dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValorTotalNota() {
		return valorTotalNota;
	}

	public void setValorTotalNota(String valorTotalNota) {
		this.valorTotalNota = valorTotalNota;
	}

	public String getValorTotalNotaComDesconto() {
		return valorTotalNotaComDesconto;
	}

	public void setValorTotalNotaComDesconto(String valorTotalNotaComDesconto) {
		this.valorTotalNotaComDesconto = valorTotalNotaComDesconto;
	}

	public String getNotaRecebida() {
		return notaRecebida;
	}

	public void setNotaRecebida(String notaRecebida) {
		this.notaRecebida = notaRecebida;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	
	
}
