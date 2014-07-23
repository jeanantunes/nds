package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.Util;

/**
 * DTO com a sumarização do reparte do fechamento diário
 * 
 * @author francisco.garcia
 *
 */
public class SumarizacaoReparteDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private BigDecimal totalReparte;

    private BigDecimal totalSobras;
    
    private BigDecimal totalFaltas;
    
    private BigDecimal totalTransferencias;
    
    private BigDecimal totalDistribuir;
    
    private BigDecimal totalDistribuido;
    
    private BigDecimal totalSobraDistribuicao;
    
    private BigDecimal totalDiferenca;
    
    public SumarizacaoReparteDTO(){}
   
    public SumarizacaoReparteDTO(BigDecimal totalReparte,
            BigDecimal totalSobras, BigDecimal totalFaltas,
            BigDecimal totalTransferencias, BigDecimal totalDistribuido, BigDecimal totalDiferenca) {
        
    	this.totalReparte = Util.nvl(totalReparte, BigDecimal.ZERO);
        this.totalSobras = Util.nvl(totalSobras, BigDecimal.ZERO);
        this.totalFaltas = Util.nvl(totalFaltas, BigDecimal.ZERO);
        this.totalTransferencias = Util.nvl(totalTransferencias, BigDecimal.ZERO);
        this.totalDistribuido = Util.nvl(totalDistribuido, BigDecimal.ZERO);
        this.totalDiferenca = Util.nvl(totalDiferenca, BigDecimal.ZERO);
        this.totalDistribuir = this.totalReparte.add(this.totalSobras).subtract(this.totalDiferenca)
                .subtract(this.totalFaltas).add(this.totalTransferencias);
        this.totalSobraDistribuicao = this.totalDistribuir.subtract(this.totalDistribuido); 
    }

    /**
     * @return the totalReparte
     */
    public BigDecimal getTotalReparte() {
        return totalReparte;
    }

    /**
     * @return the totalSobras
     */
    public BigDecimal getTotalSobras() {
        return totalSobras;
    }

    /**
     * @return the totalFaltas
     */
    public BigDecimal getTotalFaltas() {
        return totalFaltas;
    }

    /**
     * @return the totalTransferencias
     */
    public BigDecimal getTotalTransferencias() {
        return totalTransferencias;
    }

    /**
     * @return the totalDistribuir
     */
    public BigDecimal getTotalDistribuir() {
        return totalDistribuir;
    }

    /**
     * @return the totalDistribuido
     */
    public BigDecimal getTotalDistribuido() {
        return totalDistribuido;
    }

    /**
     * @return the totalSobraDistribuicao
     */
    public BigDecimal getTotalSobraDistribuicao() {
        return totalSobraDistribuicao;
    }

    /**
     * @return the totalDiferenca
     */
    public BigDecimal getTotalDiferenca() {
        return totalDiferenca;
    }
    
    public String getTotalReparteFormatado() {
        return CurrencyUtil.formatarValor(totalReparte);
    }
    
    public String getTotalSobrasFormatado() {
        return CurrencyUtil.formatarValor(totalSobras);
    }
    
    public String getTotalFaltasFormatado() {
        return CurrencyUtil.formatarValor(totalFaltas);
    }
    
    public String getTotalTransferenciasFormatado() {
        return CurrencyUtil.formatarValor(totalTransferencias);
    }
    
    public String getTotalDistribuirFormatado() {
        return CurrencyUtil.formatarValor(totalDistribuir);
    }
    
    public String getTotalDistribuidoFormatado() {
        return CurrencyUtil.formatarValor(totalDistribuido);
    }
    
    public String getTotalSobraDistribuicaoFormatado() {
        return CurrencyUtil.formatarValor(totalSobraDistribuicao);
    }
    
    public String getTotalDiferencaFormatado() {
        return CurrencyUtil.formatarValor(totalDiferenca);
    }

	public void setTotalReparte(BigDecimal totalReparte) {
		this.totalReparte = totalReparte;
	}

	public void setTotalSobras(BigDecimal totalSobras) {
		this.totalSobras = totalSobras;
	}

	public void setTotalFaltas(BigDecimal totalFaltas) {
		this.totalFaltas = totalFaltas;
	}

	public void setTotalTransferencias(BigDecimal totalTransferencias) {
		this.totalTransferencias = totalTransferencias;
	}

	public void setTotalDistribuir(BigDecimal totalDistribuir) {
		this.totalDistribuir = totalDistribuir;
	}

	public void setTotalDistribuido(BigDecimal totalDistribuido) {
		this.totalDistribuido = totalDistribuido;
	}

	public void setTotalSobraDistribuicao(BigDecimal totalSobraDistribuicao) {
		this.totalSobraDistribuicao = totalSobraDistribuicao;
	}

	public void setTotalDiferenca(BigDecimal totalDiferenca) {
		this.totalDiferenca = totalDiferenca;
	}
}