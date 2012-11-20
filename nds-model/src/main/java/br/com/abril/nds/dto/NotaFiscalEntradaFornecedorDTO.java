package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.util.DateUtil;

public class NotaFiscalEntradaFornecedorDTO implements Serializable {

	private static final long serialVersionUID = -4211336967127609972L;

	private Long id;
	
	private String numero;
	
	private String dataEmissao;
	
	private String dataExpedicao;
	
	private String descricao;

	private BigDecimal valorTotalNota;
	
	private String notaRecebida;
	
	private String razaoSocial;

	public NotaFiscalEntradaFornecedorDTO(Long id, Long numero,
			Date dataEmissao, Date dataExpedicao, String descricao,
			BigDecimal valorTotalNota, StatusNotaFiscalEntrada statusNotaFiscal, Date dataRecebimento, String razaoSocial) {
		this.id = id;
		this.numero = numero.toString();
		this.dataEmissao = DateUtil.formatarDataPTBR(dataEmissao);
		this.dataExpedicao = DateUtil.formatarDataPTBR(dataExpedicao);
		this.descricao = descricao;
		this.valorTotalNota = valorTotalNota;
		this.notaRecebida = 
				StatusNotaFiscalEntrada.RECEBIDA.equals(statusNotaFiscal) 
					? DateUtil.formatarDataPTBR(dataRecebimento)
					: " ";		
		this.razaoSocial = razaoSocial;
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

	public BigDecimal getValorTotalNota() {
		return valorTotalNota;
	}

	public void setValorTotalNota(BigDecimal valorTotalNota) {
		this.valorTotalNota = valorTotalNota;
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
	
}
