package br.com.abril.nds.client.vo.baixaboleto;

import java.math.BigDecimal;

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

	@Export(label="Cota", exhibitionOrder=-2, widthPercent = 5)
	private Integer numeroCota;
	
	@Export(label="Nome", exhibitionOrder=-1, widthPercent = 30)
	private String nomeCota;
	
	@Export(label="Nosso Número", exhibitionOrder=1, widthPercent = 20)
	private String nossoNumero;
	
	@Export(label = "Valor R$", exhibitionOrder=2)
	private BigDecimal valorBoleto;
	
	@Export(label="Data de Emissão", exhibitionOrder=3)
	private String dataEmissao;

	@Export(label="Data de Vencimento", exhibitionOrder=4)
	private String dataVencimento;
	
	
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
	public String getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the valorBoleto
	 */
	public BigDecimal getValorBoleto() {
		return valorBoleto;
	}

	/**
	 * @param valorBoleto the valorBoleto to set
	 */
	public void setValorBoleto(BigDecimal valorBoleto) {
		this.valorBoleto = valorBoleto;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	
}
