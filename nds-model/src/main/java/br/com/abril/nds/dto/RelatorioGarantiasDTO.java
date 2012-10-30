package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;

public class RelatorioGarantiasDTO implements Serializable{

	private static final long serialVersionUID = -9219157726900444816L;
	
    private TipoGarantia tipoGarantia;
	
	private Long qtdCotas;
	
	private BigDecimal vlrTotal;
	
	/**
	 * @return the tipoGarantia
	 */
	public TipoGarantia getTipoGarantia() {
		return tipoGarantia;
	}

	/**
	 * @param tipoGarantia the tipoGarantia to set
	 */
	public void setTipoGarantia(Class<CotaGarantia> clazz) {

		if (CotaGarantiaCaucaoLiquida.class.equals(clazz)) { 
		
			this.tipoGarantia = TipoGarantia.CAUCAO_LIQUIDA;
		
		} else if (CotaGarantiaChequeCaucao.class.equals(clazz)) { 
		
			this.tipoGarantia = TipoGarantia.CHEQUE_CAUCAO;
		
		} else if (CotaGarantiaFiador.class.equals(clazz)) { 
		
			this.tipoGarantia = TipoGarantia.FIADOR;
		
		} else if (CotaGarantiaImovel.class.equals(clazz)) { 
		
			this.tipoGarantia = TipoGarantia.IMOVEL;
		
		} else if (CotaGarantiaNotaPromissoria.class.equals(clazz)) { 
		
			this.tipoGarantia = TipoGarantia.NOTA_PROMISSORIA;
		
		} else if (CotaGarantiaOutros.class.equals(clazz)) { 
		
			this.tipoGarantia = TipoGarantia.OUTROS;
		}
	}

	/**
	 * @return the qtdCotas
	 */
	public Long getQtdCotas() {
		return qtdCotas;
	}

	/**
	 * @param qtdCotas the qtdCotas to set
	 */
	public void setQtdCotas(Long qtdCotas) {
		this.qtdCotas = qtdCotas;
	}

	/**
	 * @return the vlrTotal
	 */
	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}

	/**
	 * @param vlrTotal the vlrTotal to set
	 */
	public void setVlrTotal(BigDecimal vlrTotal) {
		this.vlrTotal = vlrTotal;
	}

}
