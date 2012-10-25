package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;

public class GarantiaCadastradaDTO implements Serializable {

	private static final long serialVersionUID = 3974338449916012348L;

	private TipoGarantia tipoGarantia;
	
	private BigInteger quantidadeCotas;
	
	private BigDecimal valorTotal;

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
	 * @return the quantidadeCotas
	 */
	public BigInteger getQuantidadeCotas() {
		return quantidadeCotas;
	}

	/**
	 * @param quantidadeCotas the quantidadeCotas to set
	 */
	public void setQuantidadeCotas(Long quantidadeCotas) {
		this.quantidadeCotas = quantidadeCotas == null ? BigInteger.ZERO : new BigInteger(quantidadeCotas.toString());
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
}
