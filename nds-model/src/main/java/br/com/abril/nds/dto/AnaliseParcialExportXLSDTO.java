package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseParcialExportXLSDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Export(label = "Cota", alignment = Alignment.LEFT, exhibitionOrder = 1)
    private int cota;

    @Export(label = "Classificacao", alignment = Alignment.LEFT, exhibitionOrder = 2)
    private String classificacao;

    @Export(label = "Nome", alignment = Alignment.LEFT, exhibitionOrder = 3)
    private String nome;
    
    @Export(label = "Tipo Logradouro", alignment = Alignment.LEFT, exhibitionOrder = 4)
    private String tipoLogradouro;
    
    @Export(label = "Logradouro", alignment = Alignment.LEFT, exhibitionOrder = 5)
    private String logradouro;
    
    @Export(label = "Número", alignment = Alignment.LEFT, exhibitionOrder = 6)
    private String numeroEndereco;
    
    @Export(label = "Complemento", alignment = Alignment.LEFT, exhibitionOrder = 7)
    private String complemento;
    
    @Export(label = "Bairro", alignment = Alignment.LEFT, exhibitionOrder = 8)
    private String bairro;
    
    @Export(label = "Cidade", alignment = Alignment.LEFT, exhibitionOrder = 9)
    private String cidade;
    
    @Export(label = "CEP", alignment = Alignment.LEFT, exhibitionOrder = 10)
    private String cep;
    
    @Export(label = "Telefone", alignment = Alignment.LEFT, exhibitionOrder = 11)
    private String telefone;
    
    @Export(label = "Quantidade PDVs", alignment = Alignment.LEFT, exhibitionOrder = 12)
    private BigInteger npdv;

    @Export(label = "Reparte Sugerido", alignment = Alignment.LEFT, exhibitionOrder = 13)
    private BigInteger reparteSugerido;

    @Export(label = "Legenda", alignment = Alignment.LEFT, exhibitionOrder = 14)
    private String leg;

    @Export(label = "Juramentado", alignment = Alignment.LEFT, exhibitionOrder = 15)
    private BigInteger juramento;

    @Export(label = "Media Venda", alignment = Alignment.LEFT, exhibitionOrder = 16)
    private BigDecimal mediaVenda;

    @Export(label = "Último Reparte", alignment = Alignment.LEFT, exhibitionOrder = 17)
    private BigInteger ultimoReparte;
    
    private Boolean cotaNova;
    private String descricaoLegenda;
    private BigInteger reparteEstudo;
    private BigInteger reparteEstudoOrigemCopia;
    private Boolean ajustado;
    private BigInteger quantidadeAjuste;
    private Boolean contemRepartePorPDV = false;
    private Boolean pdvAtivo = false;
    
    private Long mixID;
    private Long fixacaoID;
    
    private BigInteger somatorioQtdCotas;
    private BigInteger somatorioUltimoReparte;
    private BigInteger somatorioReparteSugerido;
    
    
    public AnaliseParcialExportXLSDTO() {
	}
    
    public String getDescricaoLegenda() {
        return descricaoLegenda;
    }

    public void setDescricaoLegenda(String descricaoLegenda) {
        this.descricaoLegenda = descricaoLegenda;
    }

    private List<EdicoesProdutosDTO> edicoesBase;

    public int getCota() {
	return cota;
    }

    public void setCota(int cota) {
	this.cota = cota;
    }
    
    
    private  int cotaId;
    public int getCotaId() {
    	return cotaId;
        }

        public void setCotaId(int cotaId) {
    	this.cotaId = cotaId;
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

    public BigInteger getUltimoReparte() {
	return ultimoReparte;
    }

    public void setUltimoReparte(BigInteger ultimoReparte) {
        this.ultimoReparte = ultimoReparte;
    }
    @Export(label = "Reparte 1", alignment = Alignment.LEFT, exhibitionOrder = 19)
    public BigInteger getReparte1() {
	if (edicoesBase.size() > 0) {
	    return edicoesBase.get(0).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 1", alignment = Alignment.LEFT, exhibitionOrder = 20)
    public BigInteger getVenda1() {
	if (edicoesBase.size() > 0) {
	    return edicoesBase.get(0).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 1", alignment = Alignment.LEFT, exhibitionOrder = 18)
    public BigInteger getNumeroEdicao1() {
	if (edicoesBase.size() > 0) {
	    return edicoesBase.get(0).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 2", alignment = Alignment.LEFT, exhibitionOrder = 19)
    public BigInteger getReparte2() {
	if (edicoesBase.size() > 1) {
	    return edicoesBase.get(1).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 2", alignment = Alignment.LEFT, exhibitionOrder = 20)
    public BigInteger getVenda2() {
	if (edicoesBase.size() > 1) {
	    return edicoesBase.get(1).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 2", alignment = Alignment.LEFT, exhibitionOrder = 18)
    public BigInteger getNumeroEdicao2() {
	if (edicoesBase.size() > 1) {
	    return edicoesBase.get(1).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 3", alignment = Alignment.LEFT, exhibitionOrder = 19)
    public BigInteger getReparte3() {
	if (edicoesBase.size() > 2) {
	    return edicoesBase.get(2).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 3", alignment = Alignment.LEFT, exhibitionOrder = 20)
    public BigInteger getVenda3() {
	if (edicoesBase.size() > 2) {
	    return edicoesBase.get(2).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 3", alignment = Alignment.LEFT, exhibitionOrder = 18)
    public BigInteger getNumeroEdicao3() {
	if (edicoesBase.size() > 2) {
	    return edicoesBase.get(2).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 4", alignment = Alignment.LEFT, exhibitionOrder = 19)
    public BigInteger getReparte4() {
	if (edicoesBase.size() > 3) {
	    return edicoesBase.get(3).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 4", alignment = Alignment.LEFT, exhibitionOrder = 20)
    public BigInteger getVenda4() {
	if (edicoesBase.size() > 3) {
	    return edicoesBase.get(3).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 4", alignment = Alignment.LEFT, exhibitionOrder = 18)
    public BigInteger getNumeroEdicao4() {
	if (edicoesBase.size() > 3) {
	    return edicoesBase.get(3).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 5", alignment = Alignment.LEFT, exhibitionOrder = 19)
    public BigInteger getReparte5() {
	if (edicoesBase.size() > 4) {
	    return edicoesBase.get(4).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 5", alignment = Alignment.LEFT, exhibitionOrder = 20)
    public BigInteger getVenda5() {
	if (edicoesBase.size() > 4) {
	    return edicoesBase.get(4).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 5", alignment = Alignment.LEFT, exhibitionOrder = 18)
    public BigInteger getNumeroEdicao5() {
	if (edicoesBase.size() > 4) {
	    return edicoesBase.get(4).getEdicao();
	}
	return null;
    }
    @Export(label = "Reparte 6", alignment = Alignment.LEFT, exhibitionOrder = 19)
    public BigInteger getReparte6() {
	if (edicoesBase.size() > 5) {
	    return edicoesBase.get(5).getReparte();
	}
	return null;
    }
    @Export(label = "Venda 6", alignment = Alignment.LEFT, exhibitionOrder = 20)
    public BigInteger getVenda6() {
	if (edicoesBase.size() > 5) {
	    return edicoesBase.get(5).getVenda();
	}
	return null;
    }
    @Export(label = "Número Edição 6", alignment = Alignment.LEFT, exhibitionOrder = 18)
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

    public Boolean isCotaNova() {
        return cotaNova;
    }

    public void setCotaNova(Boolean cotaNova) {
        this.cotaNova = cotaNova;
    }

    public BigInteger getReparteEstudo() {
        return reparteEstudo;
    }

    public void setReparteEstudo(BigDecimal reparteEstudo) {
        this.reparteEstudo = null;
        if (reparteEstudo != null) {
            this.reparteEstudo = reparteEstudo.toBigInteger();
        }
    }

    public BigInteger getReparteEstudoOrigemCopia() {
        return reparteEstudoOrigemCopia;
    }

    public void setReparteEstudoOrigemCopia(BigDecimal reparteEstudoOrigemCopia) {
        this.reparteEstudoOrigemCopia = null;
        if (reparteEstudoOrigemCopia != null) {
            this.reparteEstudoOrigemCopia = reparteEstudoOrigemCopia.toBigInteger();
        }
    }

	public Boolean getAjustado() {
		return ajustado;
	}

	public void setAjustado(Boolean ajustado) {
		this.ajustado = ajustado;
	}

	public BigInteger getQuantidadeAjuste() {
		return quantidadeAjuste;
	}

	public void setQuantidadeAjuste(BigInteger quantidadeAjuste) {
		this.quantidadeAjuste = quantidadeAjuste;
	}

	public Boolean getContemRepartePorPDV() {
		return contemRepartePorPDV;
	}

	public void setContemRepartePorPDV(Boolean contemRepartePorPDV) {
		this.contemRepartePorPDV = contemRepartePorPDV;
	}

	public Long getMixID() {
		return mixID;
	}

	public void setMixID(Long mixID) {
		this.mixID = mixID;
	}

	public Long getFixacaoID() {
		return fixacaoID;
	}

	public void setFixacaoID(Long fixacaoID) {
		this.fixacaoID = fixacaoID;
	}

	public BigInteger getSomatorioQtdCotas() {
		return somatorioQtdCotas;
	}

	public void setSomatorioQtdCotas(BigInteger somatorioQtdCotas) {
		this.somatorioQtdCotas = somatorioQtdCotas;
	}

	public BigInteger getSomatorioUltimoReparte() {
		return somatorioUltimoReparte;
	}

	public void setSomatorioUltimoReparte(BigInteger somatorioUltimoReparte) {
		this.somatorioUltimoReparte = somatorioUltimoReparte;
	}

	public BigInteger getSomatorioReparteSugerido() {
		return somatorioReparteSugerido;
	}

	public void setSomatorioReparteSugerido(BigInteger somatorioReparteSugerido) {
		this.somatorioReparteSugerido = somatorioReparteSugerido;
	}

	public Boolean getPdvAtivo() {
		return pdvAtivo;
	}

	public void setPdvAtivo(Boolean pdvAtivo) {
		this.pdvAtivo = pdvAtivo;
	}

	public String getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumeroEndereco() {
		return numeroEndereco;
	}

	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	
}
