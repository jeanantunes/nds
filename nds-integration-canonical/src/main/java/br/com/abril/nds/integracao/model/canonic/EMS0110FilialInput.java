package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0110FilialInput extends EMS0110Input implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
   
    private String codigoBarrasCorporativo;
    
    @Field(offset = 94, length = 18)
    public String getCodigoBarrasCorporativo() {
		return codigoBarrasCorporativo;
	}
    
    public void setCodigoBarrasCorporativo(String codigoCorporativo) {
		this.codigoBarrasCorporativo = codigoCorporativo ;
	}

	@Field(offset = 112, length = 5)
    public Long getPesoUni() {
        return pesoUni;
    }
    
    @Field(offset = 117, length = 3)
    public String getTipoProd() {
        return tipoProd;
    }
    
    @Field(offset = 120, length = 3)
    public int getPeb() {
        return peb;
    }
        
    @Field(offset = 123, length = 5)
    public Float getLargura() {
        return largura;
    }
    
    @Field(offset = 128, length = 5)
    public Float getComprimento() {
        return comprimento;
    }
    
    @Field(offset = 133, length = 5)
    public Float getExpessura() {
        return expessura;
    }
    
    @Field(offset = 138, length = 1)
    public String getCodSitTributaria() {
        return codSitTributaria;
    }
    
    @Field(offset = 139, length = 1)
    public String getCodSitFiscal() {
        return codSitFiscal;
    }
    
    @Field(offset = 140, length = 8)
    public int getPactPadrao() {
        return pactPadrao;
    }
    
    @Field(offset = 148, length = 3)
    public String getTipoMaterialPromocional() {
        return tipoMaterialPromocional;
    }
    
    @Field(offset = 151, length = 3)
    public String getTipoMaterialDivulgacao() {
        return tipoMaterialDivulgacao;
    }
    
    @Field(offset = 154, length = 3)
    public String getTipoMaterialTroca() {
        return tipoMaterialTroca;
    }
    
    @Field(offset = 157, length = 10)
    public String getValorValeDesconto() {
        return valorValeDesconto;
    }
    
    @Field(offset = 167, length = 10)
    public String getValorMaterialTroca() {
        return valorMaterialTroca;
    }
    
    @Field(offset = 177, length = 3)
    public String getFormaComercializacao() {
        return formaComercializacao;
    }
    
    @Field(offset = 204, length = 1)
    @FixedFormatBoolean(trueValue = "S", falseValue = "N")
    public boolean isContemBrinde() {
        return contemBrinde;
    }
    
    @Field(offset = 205, length = 10)
    public String getCodNBM() {
        return codNBM;
    }
    
    @Field(offset = 217, length = 300)
    public String getDescBrinde() {
        return descBrinde;
    }
    
    @FixedFormatBoolean(falseValue = "N", trueValue = "S")
    @Field(offset = 517, length = 1)
    public Boolean getCondVendeSeparado() {
        return condVendeSeparado;
    }
    
    @FixedFormatBoolean(trueValue = "A")
    @Field(offset = 519, length = 1)
    public boolean getStatusProd() {
        return statusProd;
    }
    
    @Field(offset = 520, length = 8)
    public Date getDataDesativacao() {
        return dataDesativacao;
    }
    
    @Field(offset = 531, length = 30)
    public String getChamadaCapa() {
        return chamadaCapa;
    }
    
    @Field(offset = 561, length = 4)
    public String getEdicao() {
        return edicao;
    }
    
    @Field(offset = 570, length = 1)
    public String getRegimeRecolhimento() {
        return regimeRecolhimento;
    }
    
    @Field(offset = 570, length = 2)
    public String getSegmentacaoClasseSocial() {
        return segmentacaoClasseSocial;
    }
    
    @Field(offset = 573, length = 3)
    public String getSegmentacaoPeriodicidade() {
        return segmentacaoPeriodicidade;
    }
    
    @Field(offset = 576, length = 2)
    public String getSegmentacaoFormaFiscal() {
        return segmentacaoFormaFiscal;
    }
    
    @Field(offset = 578, length = 1)
    public String getSegmentacaoSexo() {
        return segmentacaoSexo;
    }
    
    @Field(offset = 579, length = 1)
    public String getSegmentacaoIdade() {
        return segmentacaoIdade;
    }
    
    @Field(offset = 580, length = 1)
    public String getSegmentacaoLancamento() {
        return segmentacaoLancamento;
    }
    
    @Field(offset = 581, length = 2)
    public String getSegmentacaoTemaPrincipal() {
        return segmentacaoTemaPrincipal;
    }
    
    @Field(offset = 583, length = 2)
    public String getSegmentacaoTemaSecundario() {
        return segmentacaoTemaSecundario;
    }
    
    @Field(offset = 585, length = 3)
    public Long getCodCategoria() {
        return codCategoria;
    }
    
    @Field(offset = 588, length = 1)
    public String getContextoProdReferencia() {
        return contextoProdReferencia;
    }
    
    
    @Field(offset = 589, length = 1)
    public Long getCodFornecProdReferencia() {
        return codFornecProdReferencia;
    }
    
    
    @Field(offset = 596, length = 12)
    public String getCodProdReferencia() {
        return codProdReferencia;
    }
    
    
    @Field(offset = 608, length = 2)
    public String getTipoDesconto() {
        return tipoDesconto;
    }
    
    @Field(offset = 610, length = 1)
    public Integer getContextoEditor() {
        return contextoEditor;
    }
    
    @Field(offset = 611, length = 7)
    public Long getCodEditor() {
        return codEditor;
    }
    
    @Field(offset = 618, length = 1)
    public Integer getContextoPublicacao() {
        return contextoPublicacao;
    }
    
    @Field(offset = 619, length = 7)
    public Integer getCodFornecPublicacao() {
        return codFornecPublicacao;
    }
    
    @Field(offset = 626, length = 3)
    public String getCodColecao() {
        return codColecao;
    }
    
    @Field(offset = 629, length = 1)
    public String getFormaInclusao() {
        return formaInclusao;
    }
    
    @Field(offset = 630, length = 8)
    public String getCodPublicacao() {
        return codPublicacao;
    }
    
    @Field(offset = 638, length = 6)
    public String getCampoObscuro() {
        return campoObscuro;
    }
    
    @Field(offset = 644, length = 45)
    public String getNomeComercial() {
        return nomeComercial;
    }
    
    @Field(offset = 689, length=30)
    public String getClassificacao() {
        return classificacao;
    }
    
    // offset = 719, length=25)
    //offset = 599, length=2)
    @Field(offset = 563, length=2)
    public String getSegmento() {
        return segmento;
    }
    
    public void setSegmento(final String segmento) {
        this.segmento = segmento;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigoBarrasCorporativo == null) ? 0 : codigoBarrasCorporativo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EMS0110FilialInput other = (EMS0110FilialInput) obj;
		if (codigoBarrasCorporativo == null) {
			if (other.codigoBarrasCorporativo != null)
				return false;
		} else if (!codigoBarrasCorporativo.equals(other.codigoBarrasCorporativo))
			return false;
		return true;
	}
}