package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroDetalheBaixaBoletoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 4889806122551405294L;

	@Export(label = "Data da operação")
	private Date data;
	
	private OrdenacaoColunaDetalheBaixaBoleto ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroDetalheBaixaBoletoDTO() {
		
	}
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 */
	public enum OrdenacaoColunaDetalheBaixaBoleto {
		
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		NOME_BANCO("nomeBanco"),
		NUMERO_CONTA("numeroConta"),
		NOSSO_NUMERO("nossoNumero"),
		VALOR_BOLETO("valorBoleto"),
		VALOR_PAGO("valorPago"),
		VALOR_DIFERENCA("valorDiferenca"), 
		DATA_VENCIMENTO("dataVencimento"),
		MOTIVO_REJEITADO("motivoRejeitado"),
		MOTIVO_DIVERGENCIA("motivoDivergencia");
		
		private String nomeColuna;
		
		private OrdenacaoColunaDetalheBaixaBoleto(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}
	
	/**
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColunaDetalheBaixaBoleto getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaDetalheBaixaBoleto ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroDetalheBaixaBoletoDTO other = (FiltroDetalheBaixaBoletoDTO) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		return true;
	}

}
