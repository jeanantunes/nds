package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para Consulta de Notas Fiscais.
 * 
 * @author Discover Technology
 *
 */
@Exportable

public class ConsultaNotaFiscalVO implements Serializable {
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1614668906130031084L;
	
	@Export(label = "Número da Nota")
	private Long numeroNota;
	
	@Export(label = "Data de Emissão", alignment = Alignment.CENTER)
	private Date dataEmissao;
	
	@Export(label = "Data de Expedição", alignment = Alignment.CENTER)
	private Date dataExpedicao;
	
	@Export(label = "Tipo")
	private String tipoNotaFiscal;
	
	@Export(label = "Fornecedor")
	private String nomeFornecedor;
	
	@Export(label = "Valor R$")
	private String valor;
	
	@Export(label = "Nota Recebida")
	private String notaRecebida;
	
	/**
	 * Construtor padrão.
	 */
	public ConsultaNotaFiscalVO() {
		
	}

	/**
	 * @return the numeroNota
	 */
	public Long getNumeroNota() {
		return numeroNota;
	}

	/**
	 * @param numeroNota the numeroNota to set
	 */
	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the dataExpedicao
	 */
	public Date getDataExpedicao() {
		return dataExpedicao;
	}

	/**
	 * @param dataExpedicao the dataExpedicao to set
	 */
	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public String getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(String tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the notaRecebida
	 */
	public String getNotaRecebida() {
		return notaRecebida;
	}

	/**
	 * @param notaRecebida the notaRecebida to set
	 */
	public void setNotaRecebida(String notaRecebida) {
		this.notaRecebida = notaRecebida;
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
	
	

}
