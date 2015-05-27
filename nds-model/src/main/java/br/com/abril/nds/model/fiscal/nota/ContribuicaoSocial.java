package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ContribuicaoSocial implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7720053662091868121L;
	
	
	@NFEWhens({
			@NFEWhen(condition=NFEConditions.COFINS_TRIB_ALIQ, 
					 export=@NFEExport(secao=TipoSecao.S02, posicao=0, tamanho=2)),
		    @NFEWhen(condition=NFEConditions.COFINS_TRIB_QTDE, 
					 export=@NFEExport(secao=TipoSecao.S03, posicao=0, tamanho=2)),
		    @NFEWhen(condition=NFEConditions.COFINS_NAO_TRIB, 
		    		 export=@NFEExport(secao=TipoSecao.S04, posicao=0, tamanho=2)),
		    @NFEWhen(condition=NFEConditions.COFINS_OUTROS, 
					 export=@NFEExport(secao=TipoSecao.S05, posicao=0, tamanho=2)),
		    @NFEWhen(condition=NFEConditions.PIS_TRIB_ALIQ, 
					 export=@NFEExport(secao=TipoSecao.Q02, posicao=0, tamanho=2)),
			@NFEWhen(condition=NFEConditions.PIS_TRIB_QTDE, 
					 export=@NFEExport(secao=TipoSecao.Q03, posicao=0, tamanho=2)),
			@NFEWhen(condition=NFEConditions.PIS_NAO_TRIB, 
			 		 export=@NFEExport(secao=TipoSecao.Q04, posicao=0, tamanho=2)),
			@NFEWhen(condition=NFEConditions.PIS_OUTROS, 
			 		 export=@NFEExport(secao=TipoSecao.Q05, posicao=0, tamanho=2))
	})
	@XmlTransient
	protected String cst;
	
	
	@NFEWhens({
		 @NFEWhen(condition=NFEConditions.COFINS_TRIB_QTDE, 
				  export=@NFEExport(secao=TipoSecao.S02, posicao=1, tamanho=15)),
				  
		 @NFEWhen(condition=NFEConditions.COFINS_OUTROS_ALIQ, 
				  export=@NFEExport(secao=TipoSecao.S07, posicao=0, tamanho=15)),
		
		 @NFEWhen(condition=NFEConditions.COFINS_SUBSTITUICAO_TRIB,
				 export=@NFEExport(secao=TipoSecao.T02, posicao = 0, tamanho=15)),
		
		 @NFEWhen(condition=NFEConditions.PIS_TRIB_ALIQ, 
				  export=@NFEExport(secao=TipoSecao.Q02, posicao=1, tamanho=15)),
				  
		 @NFEWhen(condition=NFEConditions.PIS_OUTROS_ALIQ, 
				  export=@NFEExport(secao=TipoSecao.Q07, posicao=0, tamanho=15)),
				  
		 @NFEWhen(condition=NFEConditions.PIS_SUBSTITUICAO_TRIB,
				  export=@NFEExport(secao=TipoSecao.R02, posicao=0, tamanho=15))
	})
	@XmlTransient
	protected BigDecimal valorBaseCalculo;
	
	@NFEWhens({
		@NFEWhen(condition=NFEConditions.COFINS_TRIB_ALIQ, 
				 export=@NFEExport(secao=TipoSecao.S02, posicao=2, tamanho=5)),
		@NFEWhen(condition=NFEConditions.COFINS_OUTROS_ALIQ, 
				 export=@NFEExport(secao=TipoSecao.S07, posicao=1, tamanho=5)),
		@NFEWhen(condition=NFEConditions.COFINS_SUBSTITUICAO_TRIB,
				 export=@NFEExport(secao=TipoSecao.T02, posicao=1, tamanho=5)),
		@NFEWhen(condition=NFEConditions.PIS_TRIB_ALIQ, 
				 export=@NFEExport(secao=TipoSecao.Q02, posicao=2, tamanho=5)),
		@NFEWhen(condition=NFEConditions.PIS_OUTROS_ALIQ, 
				 export=@NFEExport(secao=TipoSecao.Q07, posicao=1, tamanho=5)),
	    @NFEWhen(condition=NFEConditions.PIS_SUBSTITUICAO_TRIB, 
	    		 export=@NFEExport(secao=TipoSecao.R02, posicao=1, tamanho=5))
	})
	@XmlTransient
	protected BigDecimal percentualAliquota;
	
	@NFEWhens({
		
		@NFEWhen(condition=NFEConditions.COFINS_TRIB_QTDE,
				 export=@NFEExport(secao=TipoSecao.S03, posicao=1, tamanho=16)),
				 
		@NFEWhen(condition=NFEConditions.COFINS_OUTROS_QTDE,
				 export=@NFEExport(secao=TipoSecao.S09, posicao=0, tamanho=16)),
		
	    @NFEWhen(condition=NFEConditions.COFINS_SUBSTITUICAO_TRIB, 
	    		 export=@NFEExport(secao=TipoSecao.T04, posicao=0, tamanho=16)),
		
		@NFEWhen(condition=NFEConditions.PIS_TRIB_QTDE,
				 export=@NFEExport(secao=TipoSecao.S03, posicao=1, tamanho=16)),
		
		@NFEWhen(condition=NFEConditions.PIS_OUTROS_QTDE,
				 export=@NFEExport(secao=TipoSecao.Q10, posicao=0, tamanho=16)),
				 
	    @NFEWhen(condition=NFEConditions.PIS_SUBSTITUICAO_TRIB,
	    		 export=@NFEExport(secao=TipoSecao.R04, posicao=0, tamanho=16))
	})
	protected BigDecimal quantidadeVendida;
		
	@NFEWhens({
		
		@NFEWhen(condition=NFEConditions.COFINS_TRIB_QTDE,
				 export=@NFEExport(secao=TipoSecao.S03, posicao=2, tamanho=15)),
				 
		@NFEWhen(condition=NFEConditions.COFINS_OUTROS_QTDE,
				 export=@NFEExport(secao=TipoSecao.S09, posicao=1, tamanho=15)),
		
		@NFEWhen(condition=NFEConditions.PIS_TRIB_QTDE,
				 export=@NFEExport(secao=TipoSecao.S03, posicao=2, tamanho=15)),
		
		@NFEWhen(condition=NFEConditions.PIS_OUTROS_QTDE,
				 export=@NFEExport(secao=TipoSecao.Q10, posicao=1, tamanho=15))
	})
	@XmlTransient
	protected BigDecimal valorAliquota;
	
	@NFEWhens({
		@NFEWhen(condition=NFEConditions.COFINS_TRIB_ALIQ,
				 export=@NFEExport(secao=TipoSecao.Q02, posicao=3, tamanho=15)),
		
		@NFEWhen(condition=NFEConditions.COFINS_TRIB_QTDE,
				 export=@NFEExport(secao=TipoSecao.Q03, posicao=3, tamanho=15)),
		
		@NFEWhen(condition=NFEConditions.COFINS_OUTROS,
				 export=@NFEExport(secao=TipoSecao.Q05, posicao=1, tamanho=15)),
				 
		@NFEWhen(condition=NFEConditions.COFINS_SUBSTITUICAO_TRIB,
				 export=@NFEExport(secao=TipoSecao.T, posicao=0, tamanho=15)),
		
		@NFEWhen(condition=NFEConditions.PIS_TRIB_ALIQ,
				 export=@NFEExport(secao=TipoSecao.S02, posicao=3, tamanho=15)),
				 
		@NFEWhen(condition=NFEConditions.PIS_TRIB_QTDE,
				 export=@NFEExport(secao=TipoSecao.S03, posicao=3, tamanho=15)),
		
		@NFEWhen(condition=NFEConditions.PIS_OUTROS,
				 export=@NFEExport(secao=TipoSecao.S05, posicao=1, tamanho=15)),
				 
		@NFEWhen(condition=NFEConditions.PIS_SUBSTITUICAO_TRIB,
				 export=@NFEExport(secao=TipoSecao.R, posicao=0, tamanho=15))
	})
	@XmlTransient
	protected BigDecimal valor;

	/**
	 * @return the cst
	 */
	public String getCst() {
		return cst;
	}

	/**
	 * @param cst the cst to set
	 */
	public void setCst(String cst) {
		this.cst = cst;
	}

	/**
	 * @return the valorBaseCalculo
	 */
	public BigDecimal getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	/**
	 * @param valorBaseCalculo the valorBaseCalculo to set
	 */
	public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	/**
	 * @return the percentualAliquota
	 */
	public BigDecimal getPercentualAliquota() {
		return percentualAliquota;
	}

	/**
	 * @param percentualAliquota the percentualAliquota to set
	 */
	public void setPercentualAliquota(BigDecimal percentualAliquota) {
		this.percentualAliquota = percentualAliquota;
	}

	/**
	 * @return the quantidadeVendida
	 */
	public BigDecimal getQuantidadeVendida() {
		return quantidadeVendida;
	}

	/**
	 * @param quantidadeVendida the quantidadeVendida to set
	 */
	public void setQuantidadeVendida(BigDecimal quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	/**
	 * @return the valorAliquota
	 */
	public BigDecimal getValorAliquota() {
		return valorAliquota;
	}

	/**
	 * @param valorAliquota the valorAliquota to set
	 */
	public void setValorAliquota(BigDecimal valorAliquota) {
		this.valorAliquota = valorAliquota;
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

	
}
