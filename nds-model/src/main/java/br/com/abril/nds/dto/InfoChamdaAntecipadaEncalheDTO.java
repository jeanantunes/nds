package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InfoChamdaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private List<ChamadaAntecipadaEncalheDTO> chamadasAntecipadaEncalhe;
	
	@Export(label = "Total Cotas",alignment = Alignment.CENTER)
	private BigDecimal totalCotas;
	
	@Export(label = "Exemplares",alignment = Alignment.CENTER)
	private BigDecimal totalExemplares;
	
	private Long totalRegistros;

	private String codigoProduto;
	
	private Long numeroEdicao;
	
	private Date dataAntecipacao;
	
	private Date dataProgramada;
	
	private boolean recolhimentoFinal;

	private Date dataRecolhimentoPrevista;
	
	private TipoChamadaEncalhe tipoChamadaEncalhe;
	
	/**
	 * @return the totalRegistros
	 */
	public Long getTotalRegistros() {
		return totalRegistros;
	}

	/**
	 * @param totalRegistros the totalRegistros to set
	 */
	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	/**
	 * @return the chamadasAntecipadaEncalhe
	 */
	public List<ChamadaAntecipadaEncalheDTO> getChamadasAntecipadaEncalhe() {
		return chamadasAntecipadaEncalhe;
	}

	/**
	 * @param chamadasAntecipadaEncalhe the chamadasAntecipadaEncalhe to set
	 */
	public void setChamadasAntecipadaEncalhe(
			List<ChamadaAntecipadaEncalheDTO> chamadasAntecipadaEncalhe) {
		this.chamadasAntecipadaEncalhe = chamadasAntecipadaEncalhe;
	}

	/**
	 * @return the totalCotas
	 */
	public BigDecimal getTotalCotas() {
		return totalCotas;
	}

	/**
	 * @param totalCotas the totalCotas to set
	 */
	public void setTotalCotas(BigDecimal totalCotas) {
		this.totalCotas = totalCotas;
	}

	/**
	 * @return the totalExemplares
	 */
	public BigDecimal getTotalExemplares() {
		return totalExemplares;
	}

	/**
	 * @param totalExemplares the totalExemplares to set
	 */
	public void setTotalExemplares(BigDecimal totalExemplares) {
		this.totalExemplares = totalExemplares;
	}

	/**
	 * @return the codigoProduto
	 */
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
	 * @return the dataAntecipacao
	 */
	public Date getDataAntecipacao() {
		return dataAntecipacao;
	}

	/**
	 * @param dataAntecipacao the dataAntecipacao to set
	 */
	public void setDataAntecipacao(Date dataAntecipacao) {
		this.dataAntecipacao = dataAntecipacao;
	}

	/**
	 * @return the dataProgramada
	 */
	public Date getDataProgramada() {
		return dataProgramada;
	}

	/**
	 * @param dataProgramada the dataProgramada to set
	 */
	public void setDataProgramada(Date dataProgramada) {
		this.dataProgramada = dataProgramada;
	}

	public boolean isRecolhimentoFinal() {
		return recolhimentoFinal;
	}

	public void setRecolhimentoFinal(boolean recolhimentoFinal) {
		this.recolhimentoFinal = recolhimentoFinal;
	}

	public Date getDataRecolhimentoPrevista() {
		return dataRecolhimentoPrevista;
	}

	public void setDataRecolhimentoPrevista(Date dataRecolhimentoPrevista) {
		this.dataRecolhimentoPrevista = dataRecolhimentoPrevista;
	}

	public TipoChamadaEncalhe getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}

	public void setTipoChamadaEncalhe(TipoChamadaEncalhe tipoChamadaEncalhe) {
		this.tipoChamadaEncalhe = tipoChamadaEncalhe;
	}
	
}