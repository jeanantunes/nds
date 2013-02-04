package br.com.abril.nds.client.vo.baixaboleto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre Baixas de Boletos.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaBoletoBaseVO implements Serializable {

	private static final long serialVersionUID = 2743160908272450578L;
	
	@Export(label = "Banco", exhibitionOrder=0)
	private String nomeBanco;
	
	@Export(label = "Conta-corrente", exhibitionOrder=0)
	private String numeroConta;
	
	

	private TipoBaixaBoleto tipoBaixaBoleto;

	/**
	 * Enum que define o tipo de baixa que será usado na exportação
	 * 
	 * @author Discover Technology
	 *
	 */
	@SuppressWarnings("rawtypes")
	public enum TipoBaixaBoleto {

		PREVISTOS(BaixaBoletoCotaVO.class, "baixa-boleto-previsto"),
		BAIXADOS(BaixaBoletoCotaVO.class, "baixa-boleto-baixados"),
		REJEITADOS(BaixaBoletoRejeitadoVO.class, "baixa-boleto-rejeitados"),
		DIVERGENTES(BaixaBoletoDivergenteVO.class, "baixa-boleto-divergentes"),
		INADIMPLENTES(BaixaBoletoCotaVO.class, "baixa-boleto-inadimplentes"),
		TOTAL_BANCARIO(BaixaTotalBancarioVO.class, "baixa-total-bancario");
		
		private Class tipoImpressaoVO;
		
		private String nomeArquivo;
		
		private TipoBaixaBoleto(Class tipoImpressaoVO, String nomeArquivo) {
			
			this.tipoImpressaoVO = tipoImpressaoVO;
			this.nomeArquivo = nomeArquivo;
		}

		/**
		 * @return the tipoImpressaoVO
		 */
		public Class getTipoImpressaoVO() {
			return tipoImpressaoVO;
		}



		/**
		 * @return the nomeArquivo
		 */
		public String getNomeArquivo() {
			return nomeArquivo;
		}
	}
	
	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}

	/**
	 * @param nomeBanco the nomeBanco to set
	 */
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the numeroConta
	 */
	public String getNumeroConta() {
		return numeroConta;
	}

	/**
	 * @param numeroConta the numeroConta to set
	 */
	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	/**
	 * @return the tipoBaixaBoleto
	 */
	public TipoBaixaBoleto getTipoBaixaBoleto() {
		return tipoBaixaBoleto;
	}

	/**
	 * @param tipoBaixaBoleto the tipoBaixaBoleto to set
	 */
	public void setTipoBaixaBoleto(TipoBaixaBoleto tipoBaixaBoleto) {
		this.tipoBaixaBoleto = tipoBaixaBoleto;
	}
}
