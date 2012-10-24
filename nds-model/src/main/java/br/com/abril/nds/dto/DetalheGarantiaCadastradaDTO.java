package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class DetalheGarantiaCadastradaDTO implements Serializable {

	private static final long serialVersionUID = 3974338449916012348L;

	@Export(label = "Cota", alignment=Alignment.LEFT)
	private String numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT)
	private String nomeCota;
	
	@Export(label = "Garantia", alignment=Alignment.LEFT)
	private TipoGarantia tipoGarantia;
	
	@Export(label = "Vencimento", alignment=Alignment.LEFT)
	private Date vencimento;
	
	@Export(label = "Valor Garantia R$", alignment=Alignment.LEFT)
	private BigDecimal valor;
	
	@Export(label = "Faturamento", alignment=Alignment.LEFT)
	private BigDecimal faturamentoMes;
	
	@Export(label = "% Garantia", alignment=Alignment.LEFT)
	private BigDecimal percGarantiaFaturamento;

	/**
	 * @return the numeroCota
	 */
	public String getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(String numeroCota) {
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
	 * @return the tipoGarantia
	 */
	public TipoGarantia getTipoGarantia() {
		return tipoGarantia;
	}

	/**
	 * @param tipoGarantia the tipoGarantia to set
	 */
	public void setTipoGarantia(TipoGarantia tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}

	/**
	 * @return the vencimento
	 */
	public Date getVencimento() {
		return vencimento;
	}

	/**
	 * @param vencimento the vencimento to set
	 */
	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the faturamentoMes
	 */
	public BigDecimal getFaturamentoMes() {
		return faturamentoMes;
	}

	/**
	 * @param faturamentoMes the faturamentoMes to set
	 */
	public void setFaturamentoMes(BigDecimal faturamentoMes) {
		this.faturamentoMes = faturamentoMes;
	}

	/**
	 * @return the percGarantiaFaturamento
	 */
	public BigDecimal getPercGarantiaFaturamento() {
		return percGarantiaFaturamento;
	}

	/**
	 * @param percGarantiaFaturamento the percGarantiaFaturamento to set
	 */
	public void setPercGarantiaFaturamento(BigDecimal percGarantiaFaturamento) {
		this.percGarantiaFaturamento = percGarantiaFaturamento;
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
}
