package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.util.CurrencyUtil;

public class ParametroCobrancaCotaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idParametroCobranca;
    
    private Long idFormaCobranca;
	
	private Long idCota;
	
	private Integer numCota;
	
	private long fatorVencimento;
	
	private Boolean sugereSuspensao;
	
	private Boolean sugereSuspensaoDistribuidor;
	
	private Boolean contrato;
	
	private Date inicioContrato;
	
	private Date terminoContrato;
	
	private Boolean contratoRecebido;
	
	private String valorMinimo;
	
	private Integer qtdDividasAberto;
	
	private String vrDividasAberto;
	
	private TipoCota tipoCota;
	
	private Long idFornecedor;
	
	private Boolean unificaCobranca;
	
	private Boolean devolveEncalhe;
	
	private Boolean parametroDistribuidor;
	
	private String situacaoCadastro;	
	
	private boolean boletoNFE;
	
	public Long getIdParametroCobranca() {
		return idParametroCobranca;
	}

	public void setIdParametroCobranca(Long idParametroCobranca) {
		this.idParametroCobranca = idParametroCobranca;
	}


	public Long getIdFormaCobranca() {
		return idFormaCobranca;
	}

	public void setIdFormaCobranca(Long idFormaCobranca) {
		this.idFormaCobranca = idFormaCobranca;
	}

	public Long getIdCota() {
		return idCota;
	}


	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	
	public Integer getNumCota() {
		return numCota;
	}


	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}
	
	
	public long getFatorVencimento() {
		return fatorVencimento;
	}


	public void setFatorVencimento(long fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}


	public Boolean isSugereSuspensao() {
		return sugereSuspensao;
	}


	public void setSugereSuspensao(Boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
	}

	public Boolean isSugereSuspensaoDistribuidor() {
		return sugereSuspensaoDistribuidor;
	}

	public void setSugereSuspensaoDistribuidor(Boolean sugereSuspensaoDistribuidor) {
		this.sugereSuspensaoDistribuidor = sugereSuspensaoDistribuidor;
	}

	public Boolean isContrato() {
		return contrato;
	}


	public void setContrato(Boolean contrato) {
		this.contrato = contrato;
	}
	

    public Date getInicioContrato() {
        return inicioContrato;
    }


    public void setInicioContrato(Date inicioContrato) {
        this.inicioContrato = inicioContrato;
    }


    public Date getTerminoContrato() {
        return terminoContrato;
    }


    public void setTerminoContrato(Date terminoContrato) {
        this.terminoContrato = terminoContrato;
    }
    
    public Boolean isContratoRecebido() {
        return contratoRecebido;
    }

    public void setContratoRecebido(Boolean contratoRecebido) {
        this.contratoRecebido = contratoRecebido;
    }

    public String getValorMinimo() {
		return valorMinimo;
	}

    public BigDecimal getValorMinimoBigDecimal() {
		
		BigDecimal vr = CurrencyUtil.converterValor(this.valorMinimo);
		
		return vr;
	}

	public void setValorMinimo(String valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public Integer getQtdDividasAberto() {
		return qtdDividasAberto;
	}


	public void setQtdDividasAberto(Integer qtdDividasAberto) {
		this.qtdDividasAberto = qtdDividasAberto;
	}


	public String getVrDividasAberto() {
		return vrDividasAberto;
	}
	
	public BigDecimal getVrDividasAbertoBigDecimal() {
		
		BigDecimal vr = CurrencyUtil.converterValor(this.vrDividasAberto);
		
		return vr;
	}

	public void setVrDividasAberto(String vrDividasAberto) {
		this.vrDividasAberto = vrDividasAberto;
	}


	public TipoCota getTipoCota() {
		return tipoCota;
	}


	public void setTipoCota(TipoCota tipoCota) {
		this.tipoCota = tipoCota;
	}

	
	public Long getIdFornecedor() {
		return idFornecedor;
	}


	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	

	public Boolean isUnificaCobranca() {
		return unificaCobranca;
	}


	public void setUnificaCobranca(Boolean unificaCobranca) {
		this.unificaCobranca = unificaCobranca;
	}

	
	public Boolean isParametroDistribuidor() {
		return parametroDistribuidor;
	}

	
	public void setParametroDistribuidor(Boolean parametroDistribuidor) {
		this.parametroDistribuidor = parametroDistribuidor;
	}

	
	public Boolean isDevolveEncalhe() {
		return devolveEncalhe;
	}

	
	public void setDevolveEncalhe(Boolean devolveEncalhe) {
		this.devolveEncalhe = devolveEncalhe;
	}

	
	public String getSituacaoCadastro() {
		return situacaoCadastro;
	}

	public void setSituacaoCadastro(String situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}
	
	public boolean isBoletoNFE() {
		return boletoNFE;
	}

	public void setBoletoNFE(boolean boletoNFE) {
		this.boletoNFE = boletoNFE;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
        result = prime
                * result
                + ((idParametroCobranca == null) ? 0 : idParametroCobranca
                        .hashCode());
        result = prime * result + ((numCota == null) ? 0 : numCota.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ParametroCobrancaCotaDTO other = (ParametroCobrancaCotaDTO) obj;
        if (idCota == null) {
            if (other.idCota != null) {
                return false;
            }
        } else if (!idCota.equals(other.idCota)) {
            return false;
        }
        if (idParametroCobranca == null) {
            if (other.idParametroCobranca != null) {
                return false;
            }
        } else if (!idParametroCobranca.equals(other.idParametroCobranca)) {
            return false;
        }
        if (numCota == null) {
            if (other.numCota != null) {
                return false;
            }
        } else if (!numCota.equals(other.numCota)) {
            return false;
        }
        return true;
    }
}