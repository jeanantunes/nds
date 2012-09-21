package br.com.abril.nds.integracao.ems0132.outbound;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * 
 * @author Discover Technology
 */
@Record
public class EMS0132Output implements Serializable {

	private static final long serialVersionUID = 5447737644024208144L;

	private Long codigoDistribuidor;
	private Date dataGeracaoArquivo;
	private Date hotaGeracaoArquivo;
	private String mnemonicoTabela = "LANP";
	private Integer codigoContexto;
	private Integer codigoFornecedor;
	private String codigoProduto;
	private Long numeroEdicao;
	private Long numeroLancamento;
	private Integer numeroFase;
	private Date dataLancamento;
	// FIXME: produto_parcial -> NumPeriodo 
	
	/**
	 * Construtor Padrao
	 */
	public EMS0132Output() {		
	}

	/**
	 * @return the codigoDistribuidor
	 */
	@Field(offset=1, length=7, paddingChar='0')
	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	/**
	 * @param codigoDistribuidor the codigoDistribuidor to set
	 */
	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	/**
	 * @return the dataGeracaoArquivo
	 */
	@Field(offset=8, length=8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}

	/**
	 * @param dataGeracaoArquivo the dataGeracaoArquivo to set
	 */
	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}

	/**
	 * @return the hotaGeracaoArquivo
	 */
	@Field(offset=16, length=6)
	@FixedFormatPattern("HHmmss")
	public Date getHotaGeracaoArquivo() {
		return hotaGeracaoArquivo;
	}

	/**
	 * @param hotaGeracaoArquivo the hotaGeracaoArquivo to set
	 */
	public void setHotaGeracaoArquivo(Date hotaGeracaoArquivo) {
		this.hotaGeracaoArquivo = hotaGeracaoArquivo;
	}

	/**
	 * @return the mnemonicoTabela
	 */
	@Field(offset=22, length=4)
	public String getMnemonicoTabela() {
		return mnemonicoTabela;
	}

	/**
	 * @param mnemonicoTabela the mnemonicoTabela to set
	 */
	public void setMnemonicoTabela(String mnemonicoTabela) {
		this.mnemonicoTabela = mnemonicoTabela;
	}
		
	/**
	 * @return the codigoContexto
	 */
	@Field(offset=26, length=1)
	public Integer getCodigoContexto() {
		return codigoContexto;
	}

	/**
	 * @param codigoContexto the codigoContexto to set
	 */
	public void setCodigoContexto(Integer codigoContexto) {
		this.codigoContexto = codigoContexto;
	}

	/**
	 * @return the codigoFornecedor
	 */
	@Field(offset=27, length=7)
	public Integer getCodigoFornecedor() {
		return codigoFornecedor;
	}

	/**
	 * @param codigoFornecedor the codigoFornecedor to set
	 */
	public void setCodigoFornecedor(Integer codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}
	
	/**
	 * @return the codigoProduto
	 */
	@Field(offset=34, length=8)
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	@Field(offset=42, length=4, paddingChar='0', align=Align.RIGHT)
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the numeroLancamento
	 */
	@Field(offset=46, length=2, paddingChar='0')
	public Long getNumeroLancamento() {
		return numeroLancamento;
	}

	/**
	 * @param numeroLancamento the numeroLancamento to set
	 */
	public void setNumeroLancamento(Long numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}
	
	/**
	 * @return the numeroFase
	 */
	@Field(offset=48, length=2, paddingChar='0')
	public Integer getNumeroFase() {
		return numeroFase;
	}

	/**
	 * @param numeroFase the numeroFase to set
	 */
	public void setNumeroFase(Integer numeroFase) {
		this.numeroFase = numeroFase;
	}

	/**
	 * @return the dataLancamento
	 */
	@Field(offset=50, length=8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoContexto == null) ? 0 : codigoContexto.hashCode());
		result = prime
				* result
				+ ((codigoDistribuidor == null) ? 0 : codigoDistribuidor
						.hashCode());
		result = prime
				* result
				+ ((codigoFornecedor == null) ? 0 : codigoFornecedor.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());		
		result = prime
				* result
				+ ((dataGeracaoArquivo == null) ? 0 : dataGeracaoArquivo
						.hashCode());
		result = prime * result
				+ ((dataLancamento == null) ? 0 : dataLancamento.hashCode());
		result = prime
				* result
				+ ((hotaGeracaoArquivo == null) ? 0 : hotaGeracaoArquivo
						.hashCode());
		result = prime * result
				+ ((mnemonicoTabela == null) ? 0 : mnemonicoTabela.hashCode());
		result = prime * result
				+ ((numeroFase == null) ? 0 : numeroFase.hashCode());
		result = prime
				* result
				+ ((numeroLancamento == null) ? 0 : numeroLancamento.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EMS0132Output other = (EMS0132Output) obj;
		if (codigoContexto == null) {
			if (other.codigoContexto != null)
				return false;
		} else if (!codigoContexto.equals(other.codigoContexto))
			return false;
		if (codigoDistribuidor == null) {
			if (other.codigoDistribuidor != null)
				return false;
		} else if (!codigoDistribuidor.equals(other.codigoDistribuidor))
			return false;
		if (codigoFornecedor == null) {
			if (other.codigoFornecedor != null)
				return false;
		} else if (!codigoFornecedor.equals(other.codigoFornecedor))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (dataGeracaoArquivo == null) {
			if (other.dataGeracaoArquivo != null)
				return false;
		} else if (!dataGeracaoArquivo.equals(other.dataGeracaoArquivo))
			return false;
		if (dataLancamento == null) {
			if (other.dataLancamento != null)
				return false;
		} else if (!dataLancamento.equals(other.dataLancamento))
			return false;
		if (hotaGeracaoArquivo == null) {
			if (other.hotaGeracaoArquivo != null)
				return false;
		} else if (!hotaGeracaoArquivo.equals(other.hotaGeracaoArquivo))
			return false;
		if (mnemonicoTabela == null) {
			if (other.mnemonicoTabela != null)
				return false;
		} else if (!mnemonicoTabela.equals(other.mnemonicoTabela))
			return false;
		if (numeroFase == null) {
			if (other.numeroFase != null)
				return false;
		} else if (!numeroFase.equals(other.numeroFase))
			return false;
		if (numeroLancamento == null) {
			if (other.numeroLancamento != null)
				return false;
		} else if (!numeroLancamento.equals(other.numeroLancamento))
			return false;
		return true;
	}
	
}
