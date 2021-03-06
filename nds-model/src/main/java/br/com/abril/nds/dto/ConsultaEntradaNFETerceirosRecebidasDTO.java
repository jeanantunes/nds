package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaEntradaNFETerceirosRecebidasDTO implements Serializable {

	private static final long serialVersionUID = 8366815250237375585L;

	@Export(label = "N� Nota", alignment = Alignment.LEFT)
	private Long numeroNota;

	@Export(label = "Serie", alignment = Alignment.LEFT)
	private String serie;

	@Export(label = "Chave Acesso", alignment = Alignment.LEFT)
	private String chaveAcesso;
	
	@Export(label = "Data Emissao", alignment = Alignment.LEFT)
	private String dataEmissao;

	@Export(label = "Tipo Nota", alignment = Alignment.LEFT)
	private String tipoNotaFiscal;

	@Export(label = "Fornecedor/Cota", alignment = Alignment.LEFT)
	private String nome;

	@Export(label = "Valor Nota", alignment = Alignment.RIGHT)
	private BigDecimal valorNota;

	private boolean contemDiferenca;

	/**
	 * @return the numeroNota
	 */
	public Long getNumeroNota() {
		return numeroNota;
	}

	/**
	 * @param numeroNota
	 *            the numeroNota to set
	 */
	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie
	 *            the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * @return the chaveAcesso
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}

	/**
	 * @param chaveAcesso
	 *            the chaveAcesso to set
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}


	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the valorNota
	 */
	public BigDecimal getValorNota() {
		return valorNota;
	}

	/**
	 * @param valorNota
	 *            the valorNota to set
	 */
	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	/**
	 * @return the contemDiferenca
	 */
	public boolean isContemDiferenca() {
		return contemDiferenca;
	}

	/**
	 * @param contemDiferenca
	 *            the contemDiferenca to set
	 */
	public void setContemDiferenca(Integer contemDiferenca) {
		this.contemDiferenca = (contemDiferenca.equals(0)) ? false : true;
	}

	/**
	 * @return the dataEmissao
	 */
	public String getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = DateUtil.formatarData(dataEmissao, Constantes.DATE_PATTERN_PT_BR);
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

}
