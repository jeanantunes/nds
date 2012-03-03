package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.planejamento.TipoLancamento;

public class ItemNotaRecebimentoFisicoDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private Long idNotaFiscal;
	
	private Long idItemRecebimentoFisico;
	
	private Long idRecebimentoFisico;
	
	private Long idProdutoEdicao;
	
	private Long idUser;
			
	private Date dataLancamento;
	
	private Date dataRecolhimento;
		
	private BigDecimal repartePrevisto;
	
	private TipoLancamento tipoLancamento;
	

	public ItemNotaRecebimentoFisicoDTO(
			Date dataLancamento,
			Date dataRecolhimento,			
			BigDecimal repartePrevisto, 
			TipoLancamento tipoLancamento,
			Long idNotaFiscal,
			Long idItemRecebimentoFisico,
			Long idRecebimentoFisico,
			Long idProdutoEdicao,
			Long idUser){
				
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.repartePrevisto = repartePrevisto;
		this.tipoLancamento = tipoLancamento;
		this.idNotaFiscal = idNotaFiscal;
		this.idItemRecebimentoFisico = idItemRecebimentoFisico;
		this.idRecebimentoFisico = idRecebimentoFisico;
		this.idProdutoEdicao = idProdutoEdicao;
		this.idUser = idUser;
	}


	public Long getIdUser() {
		return idUser;
	}


	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}


	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}


	public void setIdNotaFiscal(Long idNotaFiscal) {
		this.idNotaFiscal = idNotaFiscal;
	}


	public Long getIdItemRecebimentoFisico() {
		return idItemRecebimentoFisico;
	}


	public void setIdItemRecebimentoFisico(Long idItemRecebimentoFisico) {
		this.idItemRecebimentoFisico = idItemRecebimentoFisico;
	}


	public Long getIdRecebimentoFisico() {
		return idRecebimentoFisico;
	}


	public void setIdRecebimentoFisico(Long idRecebimentoFisico) {
		this.idRecebimentoFisico = idRecebimentoFisico;
	}


	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}


	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}


	public Date getDataLancamento() {
		return dataLancamento;
	}


	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}


	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}


	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}


	public BigDecimal getRepartePrevisto() {
		return repartePrevisto;
	}


	public void setRepartePrevisto(BigDecimal repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}


	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}


	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
	

}