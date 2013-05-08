package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseParcialDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Export(label = "Cota", alignment = Alignment.LEFT, exhibitionOrder = 1)
    private int cota;

    @Export(label = "Classificacao", alignment = Alignment.LEFT, exhibitionOrder = 2)
    private String classificacao;

    @Export(label = "Nome", alignment = Alignment.LEFT, exhibitionOrder = 3)
    private String nome;

    @Export(label = "Quantidade PDVs", alignment = Alignment.LEFT, exhibitionOrder = 4)
    private BigInteger npdv;

    @Export(label = "Reparte Sugerido", alignment = Alignment.LEFT, exhibitionOrder = 5)
    private BigInteger reparteSugerido;

    @Export(label = "Legenda", alignment = Alignment.LEFT, exhibitionOrder = 6)
    private String leg;

    @Export(label = "Juramento", alignment = Alignment.LEFT, exhibitionOrder = 7)
    private BigInteger juramento;

    @Export(label = "Media Venda", alignment = Alignment.LEFT, exhibitionOrder = 8)
    private BigDecimal mediaVenda;

    @Export(label = "Último Reparte", alignment = Alignment.LEFT, exhibitionOrder = 9)
    private BigDecimal ultimoReparte;
    
    private boolean cotaNova;

    private List<EdicoesProdutosDTO> edicoesBase;

    public int getCota() {
	return cota;
    }

    public void setCota(int cota) {
	this.cota = cota;
    }

    public String getClassificacao() {
	return classificacao;
    }

    public void setClassificacao(String classificacao) {
	this.classificacao = classificacao;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public BigInteger getNpdv() {
	return npdv;
    }

    public void setNpdv(BigInteger npdv) {
	this.npdv = npdv;
    }

    public BigInteger getReparteSugerido() {
	return reparteSugerido;
    }

    public void setReparteSugerido(BigInteger reparteSugerido) {
	this.reparteSugerido = reparteSugerido;
    }

    public String getLeg() {
	return leg;
    }

    public void setLeg(String leg) {
	this.leg = leg;
    }

    public BigInteger getJuramento() {
	return juramento;
    }

    public void setJuramento(BigInteger juramento) {
	this.juramento = juramento;
    }

    public BigDecimal getMediaVenda() {
	return mediaVenda;
    }

    public void setMediaVenda(BigDecimal mediaVenda) {
	this.mediaVenda = mediaVenda;
    }

    public BigDecimal getUltimoReparte() {
	return ultimoReparte;
    }

    public void setUltimoReparte(BigDecimal ultimoReparte) {
	this.ultimoReparte = ultimoReparte;
    }
    @Export(label = "Reparte 1", alignment = Alignment.LEFT, exhibitionOrder = 16)
    public BigDecimal getReparte1() {
	if (edicoesBase.size() > 0) {
	    return edicoesBase.get(0).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 1", alignment = Alignment.LEFT, exhibitionOrder = 17)
    public BigDecimal getVenda1() {
	if (edicoesBase.size() > 0) {
	    return edicoesBase.get(0).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 1", alignment = Alignment.LEFT, exhibitionOrder = 10)
    public BigInteger getNumeroEdicao1() {
	if (edicoesBase.size() > 0) {
	    return edicoesBase.get(0).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 2", alignment = Alignment.LEFT, exhibitionOrder = 16)
    public BigDecimal getReparte2() {
	if (edicoesBase.size() > 1) {
	    return edicoesBase.get(1).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 2", alignment = Alignment.LEFT, exhibitionOrder = 17)
    public BigDecimal getVenda2() {
	if (edicoesBase.size() > 1) {
	    return edicoesBase.get(1).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 2", alignment = Alignment.LEFT, exhibitionOrder = 10)
    public BigInteger getNumeroEdicao2() {
	if (edicoesBase.size() > 1) {
	    return edicoesBase.get(1).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 3", alignment = Alignment.LEFT, exhibitionOrder = 16)
    public BigDecimal getReparte3() {
	if (edicoesBase.size() > 2) {
	    return edicoesBase.get(2).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 3", alignment = Alignment.LEFT, exhibitionOrder = 17)
    public BigDecimal getVenda3() {
	if (edicoesBase.size() > 2) {
	    return edicoesBase.get(2).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 3", alignment = Alignment.LEFT, exhibitionOrder = 10)
    public BigInteger getNumeroEdicao3() {
	if (edicoesBase.size() > 2) {
	    return edicoesBase.get(2).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 4", alignment = Alignment.LEFT, exhibitionOrder = 16)
    public BigDecimal getReparte4() {
	if (edicoesBase.size() > 3) {
	    return edicoesBase.get(3).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 4", alignment = Alignment.LEFT, exhibitionOrder = 17)
    public BigDecimal getVenda4() {
	if (edicoesBase.size() > 3) {
	    return edicoesBase.get(3).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 4", alignment = Alignment.LEFT, exhibitionOrder = 10)
    public BigInteger getNumeroEdicao4() {
	if (edicoesBase.size() > 3) {
	    return edicoesBase.get(3).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 5", alignment = Alignment.LEFT, exhibitionOrder = 16)
    public BigDecimal getReparte5() {
	if (edicoesBase.size() > 4) {
	    return edicoesBase.get(4).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 5", alignment = Alignment.LEFT, exhibitionOrder = 17)
    public BigDecimal getVenda5() {
	if (edicoesBase.size() > 4) {
	    return edicoesBase.get(4).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 5", alignment = Alignment.LEFT, exhibitionOrder = 10)
    public BigInteger getNumeroEdicao5() {
	if (edicoesBase.size() > 4) {
	    return edicoesBase.get(4).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 6", alignment = Alignment.LEFT, exhibitionOrder = 16)
    public BigDecimal getReparte6() {
	if (edicoesBase.size() > 5) {
	    return edicoesBase.get(5).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 6", alignment = Alignment.LEFT, exhibitionOrder = 17)
    public BigDecimal getVenda6() {
	if (edicoesBase.size() > 5) {
	    return edicoesBase.get(5).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 6", alignment = Alignment.LEFT, exhibitionOrder = 10)
    public BigInteger getNumeroEdicao6() {
	if (edicoesBase.size() > 5) {
	    return edicoesBase.get(5).getEdicao();
	}
	return null;
    }
    
    public List<EdicoesProdutosDTO> getEdicoesBase() {
        return edicoesBase;
    }

    public void setEdicoesBase(List<EdicoesProdutosDTO> edicoesBase) {
        this.edicoesBase = edicoesBase;
    }

    public boolean isCotaNova() {
        return cotaNova;
    }

    public void setCotaNova(boolean cotaNova) {
        this.cotaNova = cotaNova;
    }
}
