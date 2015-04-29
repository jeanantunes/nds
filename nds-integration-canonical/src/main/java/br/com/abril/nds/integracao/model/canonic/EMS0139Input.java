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

	private Long situacaoAtendimentoDDE;

	private String codigoProduto;

	private Long numeroEdicao;
	
	private String nomeComercial;

	private String nomeDestinoDDE;

	private Long numeroPrioridadeAtendimentoDDE;

	private String tipoAtendimentoDDE;

	@Field(offset=0, length=6)
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	@Field(offset=8, length=6)
	public String getSemanaRecolhimento() {
		return semanaRecolhimento;
	}

	public void setSemanaRecolhimento(String semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}

	@Field(offset=15, length=10)
	public Long getSituacaoAtendimentoDDE() {
		return situacaoAtendimentoDDE;
	}

	public void setSituacaoAtendimentoDDE(Long situacaoAtendimentoDDE) {
		this.situacaoAtendimentoDDE = situacaoAtendimentoDDE;
	}

	@Field(offset=26, length=8)
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	@Field(offset=35, length=4)
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	@Field(offset=40, length=45)
	public String getNomeComercial() {
		return nomeComercial;
	}

	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}

	@Field(offset=86, length=20)
	public String getNomeDestinoDDE() {
		return nomeDestinoDDE;
	}

	public void setNomeDestinoDDE(String nomeDestinoDDE) {
		this.nomeDestinoDDE = nomeDestinoDDE;
	}

	@Field(offset=107, length=2)
	public Long getNumeroPrioridadeAtendimentoDDE() {
		return numeroPrioridadeAtendimentoDDE;
	}

	public void setNumeroPrioridadeAtendimentoDDE(Long numeroPrioridadeAtendimentoDDE) {
		this.numeroPrioridadeAtendimentoDDE = numeroPrioridadeAtendimentoDDE;
	}

	@Field(offset=110, length=84)
	public String getTipoAtendimentoDDE() {
		return tipoAtendimentoDDE;
	}

	public void setTipoAtendimentoDDE(String tipoAtendimentoDDE) {
		this.tipoAtendimentoDDE = tipoAtendimentoDDE;
	}

}