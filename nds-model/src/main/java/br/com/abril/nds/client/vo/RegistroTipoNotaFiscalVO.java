package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroTipoNotaFiscalVO implements Serializable {

	private static final long serialVersionUID = -2039605389828028947L;
	
	private Long id;
	
	@Export(label="Operação", exhibitionOrder= 1)
	private String tipoAtividade;
	
	@Export(label="Processo", exhibitionOrder=2)
	private String processo;
	
	@Export(label = "Tipo de Nota", exhibitionOrder = 3)
	private String nopDescricao;
	
	@Export(label = "CFOP Dentro UF", exhibitionOrder = 4)
	private String cfopEstado;
	
	@Export(label = "CFOP Fora UF", exhibitionOrder = 5)
	private String cfopOutrosEstados;
	
	@Export(label = "Nota", exhibitionOrder = 6)
	private Long numeroNotaFiscal;
	
	@Export(label = "Serie", exhibitionOrder = 7)
	private Long serieNotaFiscal;

	public String getNopDescricao() {
		return nopDescricao;
	}

	public void setNopDescricao(String nopDescricao) {
		this.nopDescricao = nopDescricao;
	}

	public String getCfopEstado() {
		return cfopEstado;
	}

	public void setCfopEstado(String cfopEstado) {
		this.cfopEstado = cfopEstado;
	}

	public String getCfopOutrosEstados() {
		return cfopOutrosEstados;
	}

	public void setCfopOutrosEstados(String cfopOutrosEstados) {
		this.cfopOutrosEstados = cfopOutrosEstados;
	}

	/**
	 * @return the tipoAtividade
	 */
	public String getTipoAtividade() {
		return tipoAtividade;
	}

	/**
	 * @param tipoAtividade the tipoAtividade to set
	 */
	public void setTipoAtividade(String tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	/**
	 * @return the processo
	 */
	public String getProcesso() {
		return processo;
	}

	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public Long getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(Long numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public Long getSerieNotaFiscal() {
		return serieNotaFiscal;
	}

	public void setSerieNotaFiscal(Long serieNotaFiscal) {
		this.serieNotaFiscal = serieNotaFiscal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
