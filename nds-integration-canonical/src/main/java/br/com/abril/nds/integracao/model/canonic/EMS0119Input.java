package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0119Input extends IntegracaoDocument implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoDaPublicacao; 
	private String nomeDaPublicacao;
	private String numeroDeEdicoes;
	private Integer periodicidade; 
	private Long tipoDePublicacao;
	private boolean statusDaPublicacao; 
	private Long codigoDoEditor;
	private String cobrancaAntecipada;
	private String condTransmiteHistograma;
	private Integer pacotePadrao; 
	private String condPublicacaoEspecial;
	private String condProdutoAVista;
	private String contextoFornecedorProduto;
	private String codigoFornecedorPublic;
	private BigDecimal desconto;
	private String origemDoProduto;
	private String cromos; 
	private String nomeComercial;
	
	@Field(offset = 1, length = 8)
	public String getCodigoDaPublicacao() {
		return codigoDaPublicacao;
	}
	public void setCodigoDaPublicacao(String codigoDaPublicacao) {
		this.codigoDaPublicacao = codigoDaPublicacao;
	}
		
	@Field(offset = 9, length = 20)
	public String getNomeDaPublicacao() {
		return nomeDaPublicacao;
	}
	public void setNomeDaPublicacao(String nomeDaPublicacao) {
		this.nomeDaPublicacao = nomeDaPublicacao;
	}
	
	@Field(offset = 29, length = 2)
	public String getNumeroDeEdicoes() {
		return numeroDeEdicoes;
	}
	public void setNumeroDeEdicoes(String numeroDeEdicoes) {
		this.numeroDeEdicoes = numeroDeEdicoes;
	}
	
	@Field(offset = 31, length = 3)
	public Integer getPeriodicidade() {
		return periodicidade;
	}
	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	@Field(offset = 34, length = 2)
	public Long getTipoDePublicacao() {
		return tipoDePublicacao;
	}
	public void setTipoDePublicacao(Long tipoDePublicacao) {
		this.tipoDePublicacao = tipoDePublicacao;
	}
	
	@Field(offset = 36, length = 1)
	@FixedFormatBoolean(trueValue = "1", falseValue = "2")
	public boolean getStatusDaPublicacao() {
		return statusDaPublicacao;
	}
	public void setStatusDaPublicacao(boolean statusDaPublicacao) {
		this.statusDaPublicacao = statusDaPublicacao;
	}
	
	@Field(offset = 37, length = 7)
	public Long getCodigoDoEditor() {
		return codigoDoEditor;
	}
	public void setCodigoDoEditor(Long codigoDoEditor) {
		this.codigoDoEditor = codigoDoEditor;
	}
	
	@Field(offset = 44, length = 6)
	public String getCobrancaAntecipada() {
		return cobrancaAntecipada;
	}
	public void setCobrancaAntecipada(String cobrancaAntecipada) {
		this.cobrancaAntecipada = cobrancaAntecipada;
	}
	
	@Field(offset = 50, length = 1)
	public String getCondTransmiteHistograma() {
		return condTransmiteHistograma;
	}
	public void setCondTransmiteHistograma(String condTransmiteHistograma) {
		this.condTransmiteHistograma = condTransmiteHistograma;
	}
	
	@Field(offset = 51, length = 5)
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	@Field(offset = 56, length = 1)
	public String getCondPublicacaoEspecial() {
		return condPublicacaoEspecial;
	}
	public void setCondPublicacaoEspecial(String condPublicacaoEspecial) {
		this.condPublicacaoEspecial = condPublicacaoEspecial;
	}
	
	@Field(offset = 57, length = 1)
	public String getCondProdutoAVista() {
		return condProdutoAVista;
	}
	public void setCondProdutoAVista(String condProdutoAVista) {
		this.condProdutoAVista = condProdutoAVista;
	}
	
	@Field(offset = 58, length = 1)
	public String getContextoFornecedorProduto() {
		return contextoFornecedorProduto;
	}
	public void setContextoFornecedorProduto(String contextoFornecedorProduto) {
		this.contextoFornecedorProduto = contextoFornecedorProduto;
	}
	
	@Field(offset = 59, length = 7)
	public String getCodigoFornecedorPublic() {
		return codigoFornecedorPublic;
	}
	public void setCodigoFornecedorPublic(String codigoFornecedorPublic) {
		this.codigoFornecedorPublic = codigoFornecedorPublic;
	}
	
	@Field(offset = 66, length = 8)
	@FixedFormatDecimal(decimals = 4, useDecimalDelimiter = true)
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	@Field(offset = 74, length = 1)
	public String getOrigemDoProduto() {
		return origemDoProduto;
	}
	public void setOrigemDoProduto(String origemDoProduto) {
		this.origemDoProduto = origemDoProduto;
	}
	
	@Field(offset = 75, length = 1)
	public String getCromos() {
		return cromos;
	}
	public void setCromos(String cromos) {
		this.cromos = cromos;
	}
	
	@Field(offset = 76, length = 45)
	public String getNomeComercial() {
		return nomeComercial;
	}
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cobrancaAntecipada == null) ? 0 : cobrancaAntecipada
						.hashCode());
		result = prime
				* result
				+ ((codigoDaPublicacao == null) ? 0 : codigoDaPublicacao
						.hashCode());
		result = prime * result
				+ ((codigoDoEditor == null) ? 0 : codigoDoEditor.hashCode());
		result = prime
				* result
				+ ((codigoFornecedorPublic == null) ? 0
						: codigoFornecedorPublic.hashCode());
		result = prime
				* result
				+ ((condProdutoAVista == null) ? 0 : condProdutoAVista
						.hashCode());
		result = prime
				* result
				+ ((condPublicacaoEspecial == null) ? 0
						: condPublicacaoEspecial.hashCode());
		result = prime
				* result
				+ ((condTransmiteHistograma == null) ? 0
						: condTransmiteHistograma.hashCode());
		result = prime
				* result
				+ ((contextoFornecedorProduto == null) ? 0
						: contextoFornecedorProduto.hashCode());
		result = prime * result + ((cromos == null) ? 0 : cromos.hashCode());
		result = prime * result
				+ ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result
				+ ((nomeComercial == null) ? 0 : nomeComercial.hashCode());
		result = prime
				* result
				+ ((nomeDaPublicacao == null) ? 0 : nomeDaPublicacao.hashCode());
		result = prime * result
				+ ((numeroDeEdicoes == null) ? 0 : numeroDeEdicoes.hashCode());
		result = prime * result
				+ ((origemDoProduto == null) ? 0 : origemDoProduto.hashCode());
		result = prime * result
				+ ((pacotePadrao == null) ? 0 : pacotePadrao.hashCode());
		result = prime * result
				+ ((periodicidade == null) ? 0 : periodicidade.hashCode());
		result = prime * result + (statusDaPublicacao ? 1231 : 1237);
		result = prime
				* result
				+ ((tipoDePublicacao == null) ? 0 : tipoDePublicacao.hashCode());
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
		EMS0119Input other = (EMS0119Input) obj;
		if (cobrancaAntecipada == null) {
			if (other.cobrancaAntecipada != null)
				return false;
		} else if (!cobrancaAntecipada.equals(other.cobrancaAntecipada))
			return false;
		if (codigoDaPublicacao == null) {
			if (other.codigoDaPublicacao != null)
				return false;
		} else if (!codigoDaPublicacao.equals(other.codigoDaPublicacao))
			return false;
		if (codigoDoEditor == null) {
			if (other.codigoDoEditor != null)
				return false;
		} else if (!codigoDoEditor.equals(other.codigoDoEditor))
			return false;
		if (codigoFornecedorPublic == null) {
			if (other.codigoFornecedorPublic != null)
				return false;
		} else if (!codigoFornecedorPublic.equals(other.codigoFornecedorPublic))
			return false;
		if (condProdutoAVista == null) {
			if (other.condProdutoAVista != null)
				return false;
		} else if (!condProdutoAVista.equals(other.condProdutoAVista))
			return false;
		if (condPublicacaoEspecial == null) {
			if (other.condPublicacaoEspecial != null)
				return false;
		} else if (!condPublicacaoEspecial.equals(other.condPublicacaoEspecial))
			return false;
		if (condTransmiteHistograma == null) {
			if (other.condTransmiteHistograma != null)
				return false;
		} else if (!condTransmiteHistograma
				.equals(other.condTransmiteHistograma))
			return false;
		if (contextoFornecedorProduto == null) {
			if (other.contextoFornecedorProduto != null)
				return false;
		} else if (!contextoFornecedorProduto
				.equals(other.contextoFornecedorProduto))
			return false;
		if (cromos == null) {
			if (other.cromos != null)
				return false;
		} else if (!cromos.equals(other.cromos))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (nomeComercial == null) {
			if (other.nomeComercial != null)
				return false;
		} else if (!nomeComercial.equals(other.nomeComercial))
			return false;
		if (nomeDaPublicacao == null) {
			if (other.nomeDaPublicacao != null)
				return false;
		} else if (!nomeDaPublicacao.equals(other.nomeDaPublicacao))
			return false;
		if (numeroDeEdicoes == null) {
			if (other.numeroDeEdicoes != null)
				return false;
		} else if (!numeroDeEdicoes.equals(other.numeroDeEdicoes))
			return false;
		if (origemDoProduto == null) {
			if (other.origemDoProduto != null)
				return false;
		} else if (!origemDoProduto.equals(other.origemDoProduto))
			return false;
		if (pacotePadrao == null) {
			if (other.pacotePadrao != null)
				return false;
		} else if (!pacotePadrao.equals(other.pacotePadrao))
			return false;
		if (periodicidade == null) {
			if (other.periodicidade != null)
				return false;
		} else if (!periodicidade.equals(other.periodicidade))
			return false;
		if (statusDaPublicacao != other.statusDaPublicacao)
			return false;
		if (tipoDePublicacao == null) {
			if (other.tipoDePublicacao != null)
				return false;
		} else if (!tipoDePublicacao.equals(other.tipoDePublicacao))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "EMS0119Input [codigoDaPublicacao=" + codigoDaPublicacao
				+ ", nomeDaPublicacao=" + nomeDaPublicacao + ", numeroDeEdicoes=" + numeroDeEdicoes
				+ ", periodicidade=" + periodicidade + ", tipoDePublicacao="
				+ tipoDePublicacao + ", statusDaPublicacao="
				+ statusDaPublicacao + ", codigoDoEditor=" + codigoDoEditor
				+ ", cobrancaAntecipada=" + cobrancaAntecipada
				+ ", condTransmiteHistograma=" + condTransmiteHistograma
				+ ", pacotePadrao=" + pacotePadrao
				+ ", condPublicacaoEspecial=" + condPublicacaoEspecial
				+ ", condProdutoAVista=" + condProdutoAVista
				+ ", contextoFornecedorProduto=" + contextoFornecedorProduto
				+ ", codigoFornecedorPublic=" + codigoFornecedorPublic
				+ ", desconto=" + desconto + ", origemDoProduto="
				+ origemDoProduto + ", cromos=" + cromos + ", nomeComercial="
				+ nomeComercial + "]";
	}
	
	
	
	
}