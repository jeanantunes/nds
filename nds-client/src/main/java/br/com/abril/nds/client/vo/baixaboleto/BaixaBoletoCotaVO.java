package br.com.abril.nds.client.vo.baixaboleto;

import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre Boletos e as respectivas Cotas.
 * 
 * Este VO será usado para exportar as informações dos seguintes grids de baixa automática:
 * 
 * 		- Boletos Previstos;
 * 		- Boletos Baixados;
 * 		- Inadimplentes.   
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaBoletoCotaVO extends BaixaBoletoBaseVO {

	private static final long serialVersionUID = -8543728277437275653L;

	@Export(label="Cota", exhibitionOrder=1)
	private Integer numeroCota;
	
	@Export(label="Nome", exhibitionOrder=2)
	private String nomeCota;
	
	@Export(label="Nosso Número", exhibitionOrder=5)
	private String nossoNumero;
	
	@Export(label="Data de Vencimento", exhibitionOrder=7)
	private Date dataVencimento;

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
}
