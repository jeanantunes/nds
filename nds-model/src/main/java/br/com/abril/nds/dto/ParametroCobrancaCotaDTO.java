package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCota;

public class ParametroCobrancaCotaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idParametroCobranca;
	
	private Long idCota;
	
	private Integer numCota;
	
	private long fatorVencimento;
	
	private boolean sugereSuspensao;
	
	private boolean contrato;
	
	private Date inicioContrato;
	
	private Date terminoContrato;
	
	private boolean contratoRecebido;
	
	private BigDecimal valorMinimo = BigDecimal.ZERO;
	
	private Integer qtdDividasAberto = Integer.valueOf(0);
	
	private BigDecimal vrDividasAberto = BigDecimal.ZERO;
	
	private TipoCota tipoCota;
	
	
	public Long getIdParametroCobranca() {
		return idParametroCobranca;
	}

	public void setIdParametroCobranca(Long idParametroCobranca) {
		this.idParametroCobranca = idParametroCobranca;
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


	public boolean isSugereSuspensao() {
		return sugereSuspensao;
	}


	public void setSugereSuspensao(boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
	}


	public boolean isContrato() {
		return contrato;
	}


	public void setContrato(boolean contrato) {
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
    
    public boolean isContratoRecebido() {
        return contratoRecebido;
    }

    public void setContratoRecebido(boolean contratoRecebido) {
        this.contratoRecebido = contratoRecebido;
    }

    public BigDecimal getValorMinimo() {
		return valorMinimo;
	}


	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public Integer getQtdDividasAberto() {
		return qtdDividasAberto;
	}


	public void setQtdDividasAberto(Integer qtdDividasAberto) {
		this.qtdDividasAberto = qtdDividasAberto;
	}


	public BigDecimal getVrDividasAberto() {
		return vrDividasAberto;
	}


	public void setVrDividasAberto(BigDecimal vrDividasAberto) {
		this.vrDividasAberto = vrDividasAberto;
	}


	public TipoCota getTipoCota() {
		return tipoCota;
	}


	public void setTipoCota(TipoCota tipoCota) {
		this.tipoCota = tipoCota;
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