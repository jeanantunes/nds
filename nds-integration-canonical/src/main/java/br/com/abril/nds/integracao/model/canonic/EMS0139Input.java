package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;

/**
 * Importação de arquivos do tipo "<i>Bandeiras</i>."
 */
@Record
public class EMS0139Input extends IntegracaoDocument implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7669913155208995341L;

	private String codigoDistribuidor;

	private String semanaRecolhimento;

	private String situacaoAtendimentoDDE;

	private String codigoProduto;

	private Long numeroEdicao;
	
	private String nomeComercial;

	private String nomeDestinoDDE;

	private Long numeroPrioridadeAtendimentoDDE;

	private String tipoAtendimentoDDE;
	
	private String nomeEditor;

	@Field(offset=1, length=7)
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	@Field(offset=9, length=6)
	public String getSemanaRecolhimento() {
		return semanaRecolhimento;
	}

	public void setSemanaRecolhimento(String semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}

	@Field(offset=16, length=10)
	public String getSituacaoAtendimentoDDE() {
		return situacaoAtendimentoDDE;
	}

	public void setSituacaoAtendimentoDDE(String situacaoAtendimentoDDE) {
		this.situacaoAtendimentoDDE = situacaoAtendimentoDDE;
	}

	@Field(offset=27, length=8)
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	@Field(offset=36, length=4)
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	@Field(offset=41, length=45)
	public String getNomeComercial() {
		return nomeComercial;
	}

	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}

	@Field(offset=87, length=20)
	public String getNomeDestinoDDE() {
		return nomeDestinoDDE;
	}

	public void setNomeDestinoDDE(String nomeDestinoDDE) {
		this.nomeDestinoDDE = nomeDestinoDDE;
	}

	@Field(offset=108, length=2)
	public Long getNumeroPrioridadeAtendimentoDDE() {
		return numeroPrioridadeAtendimentoDDE;
	}

	public void setNumeroPrioridadeAtendimentoDDE(Long numeroPrioridadeAtendimentoDDE) {
		this.numeroPrioridadeAtendimentoDDE = numeroPrioridadeAtendimentoDDE;
	}

	@Field(offset=111, length=8)
	public String getTipoAtendimentoDDE() {
		return tipoAtendimentoDDE;
	}

	public void setTipoAtendimentoDDE(String tipoAtendimentoDDE) {
		this.tipoAtendimentoDDE = tipoAtendimentoDDE;
	}
	
	@Field(offset=120, length=60)
	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

}