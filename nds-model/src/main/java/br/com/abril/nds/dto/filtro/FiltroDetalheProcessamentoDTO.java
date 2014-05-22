package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

public class FiltroDetalheProcessamentoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -3105477104866376867L;

	private OrdenacaoColunaConsulta ordenacaoColuna;
	private Date dataProcessamento;
	private Long codigoLogExecucao;
	private Long idLogExecucao;
	
	
	public enum OrdenacaoColunaConsulta {
		
		TIPO_ERRO("tipoErro"),
		MENSAGEM("mensagem"),
		NUMERO_LINHA("numeroLinha"),
		DEFAULT("Default");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public Long getCodigoLogExecucao() {
		return codigoLogExecucao;
	}

	public void setCodigoLogExecucao(Long codigoLogExecucao) {
		this.codigoLogExecucao = codigoLogExecucao;
	}

	public Long getIdLogExecucao() {
		return idLogExecucao;
	}

	public void setIdLogExecucao(Long idLogExecucao) {
		this.idLogExecucao = idLogExecucao;
	}

	
}
