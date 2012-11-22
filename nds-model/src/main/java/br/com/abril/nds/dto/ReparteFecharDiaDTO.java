package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ReparteFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 2126291233836764519L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Preço Capa", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private BigDecimal precoVenda;
	
	private BigDecimal sobras;
	
	private BigDecimal faltas;
	
	private BigDecimal transferencias;
	
	private BigDecimal distribuidos;
	
	private BigDecimal valorTotalReparte;
	
	private Long qtdReparte;
	
	private Long qtdSobras;
	
	private Long qtdFaltas;
	
	private Long qtdTransferido;
	
	private Long qtdADistribuir;
	
	private Long qtdDistribuido;
	
	private Long qtdSobraDiferenca;
	
	private Long qtdDiferenca;
	
	private String qtdReparteFormatado;
	
	private String qtdSobrasFormatado;
	
	private String qtdFaltasFormatado;
	
	private String qtdTransferenciaFormatado;
	
	private String qtdADistribuirFormatado;
	
	private String qtdDistribuidoFormatado;
	
	private String qtdSobraDistribuidoFormatado;
	
	private String qtdDiferencaFormatado;
	
	private Long qtdeSobraDe = Long.valueOf(0);
	
	private Long qtdeSobraEm = Long.valueOf(0);
	
	private Long qtdeFaltaDe = Long.valueOf(0);
	
	private Long qtdeFaltaEm = Long.valueOf(0);
	
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getValorTotalReparte() {
		return valorTotalReparte;
	}

	public void setValorTotalReparte(BigDecimal valorTotalReparte) {
		this.valorTotalReparte = valorTotalReparte;
	}
	
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getSobras() {
		return sobras;
	}

	public void setSobras(BigDecimal sobras) {
		this.sobras = sobras;
	}

	public BigDecimal getFaltas() {
		return faltas;
	}

	public void setFaltas(BigDecimal faltas) {
		this.faltas = faltas;
	}

	public BigDecimal getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(BigDecimal transferencias) {
		this.transferencias = transferencias;
	}

	public BigDecimal getDistribuidos() {
		return distribuidos;
	}

	public void setDistribuidos(BigDecimal distribuidos) {
		this.distribuidos = distribuidos;
	}

	public Long getQtdReparte() {
		return qtdReparte;
	}

	public void setQtdReparte(Long qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	public Long getQtdSobras() {
		return qtdSobras;
	}

	public void setQtdSobras(Long qtdSobras) {
		this.qtdSobras = qtdSobras;		
	}

	public Long getQtdFaltas() {
		return qtdFaltas;
	}

	public void setQtdFaltas(Long qtdFaltas) {
		this.qtdFaltas = qtdFaltas;
	}

	public Long getQtdTransferido() {
		return qtdTransferido;
	}

	public void setQtdTransferido(Long qtdTransferido) {
		this.qtdTransferido = qtdTransferido;
	}

	public Long getQtdADistribuir() {
		return qtdADistribuir;
	}

	public void setQtdADistribuir(Long qtdADistribuir) {
		this.qtdADistribuir = qtdADistribuir;
	}

	public Long getQtdDistribuido() {
		return qtdDistribuido;
	}

	public void setQtdDistribuido(Long qtdDistribuido) {
		this.qtdDistribuido = qtdDistribuido;
	}

	public Long getQtdSobraDiferenca() {
		return qtdSobraDiferenca;
	}

	public void setQtdSobraDiferenca(Long qtdSobraDiferenca) {
		this.qtdSobraDiferenca = qtdSobraDiferenca;
	}

	public Long getQtdDiferenca() {
		return qtdDiferenca;
	}

	public void setQtdDiferenca(Long qtdDiferenca) {
		this.qtdDiferenca = qtdDiferenca;
	}

	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 5)
	public String getQtdReparteFormatado() {
		return qtdReparteFormatado;
	}

	public void setQtdReparteFormatado(String qtdReparteFormatado) {
		this.qtdReparteFormatado = qtdReparteFormatado;
	}
	@Export(label = "Sobras", alignment=Alignment.CENTER, exhibitionOrder = 6)
	public String getQtdSobrasFormatado() {
		return qtdSobrasFormatado;
	}

	public void setQtdSobrasFormatado(String qtdSobrasFormatado) {
		this.qtdSobrasFormatado = qtdSobrasFormatado;
	}
	
	@Export(label = "Faltas", alignment=Alignment.CENTER, exhibitionOrder = 7)
	public String getQtdFaltasFormatado() {
		return qtdFaltasFormatado;
	}

	public void setQtdFaltasFormatado(String qtdFaltasFormatado) {
		this.qtdFaltasFormatado = qtdFaltasFormatado;
	}

	@Export(label = "Transf.", alignment=Alignment.CENTER, exhibitionOrder = 8)
	public String getQtdTransferenciaFormatado() {
		return qtdTransferenciaFormatado;
	}

	public void setQtdTransferenciaFormatado(String qtdTransferenciaFormatado) {
		this.qtdTransferenciaFormatado = qtdTransferenciaFormatado;
	}

	@Export(label = "A Distr.", alignment=Alignment.CENTER, exhibitionOrder = 9)
	public String getQtdADistribuirFormatado() {
		return qtdADistribuirFormatado;
	}

	public void setQtdADistribuirFormatado(String qtdADistribuirFormatado) {
		this.qtdADistribuirFormatado = qtdADistribuirFormatado;
	}

	@Export(label = "Distribuido", alignment=Alignment.CENTER, exhibitionOrder = 10)
	public String getQtdDistribuidoFormatado() {
		return qtdDistribuidoFormatado;
	}

	public void setQtdDistribuidoFormatado(String qtdDistribuidoFormatado) {
		this.qtdDistribuidoFormatado = qtdDistribuidoFormatado;
	}

	@Export(label = "Sobra Distr.", alignment=Alignment.LEFT, exhibitionOrder = 11)
	public String getQtdSobraDistribuidoFormatado() {
		return qtdSobraDistribuidoFormatado;
	}

	public void setQtdSobraDistribuidoFormatado(String qtdSobraDistribuidoFormatado) {
		this.qtdSobraDistribuidoFormatado = qtdSobraDistribuidoFormatado;
	}

	@Export(label = "Diferença", alignment=Alignment.CENTER, exhibitionOrder = 12)
	public String getQtdDiferencaFormatado() {
		return qtdDiferencaFormatado;
	}

	public void setQtdDiferencaFormatado(String qtdDiferencaFormatado) {
		this.qtdDiferencaFormatado = qtdDiferencaFormatado;
	}

    /**
     * @return the qtdeSobraDe
     */
    public Long getQtdeSobraDe() {
        return qtdeSobraDe;
    }

    /**
     * @param qtdeSobraDe the qtdeSobraDe to set
     */
    public void setQtdeSobraDe(Long qtdeSobraDe) {
        this.qtdeSobraDe = qtdeSobraDe;
    }

    /**
     * @return the qtdeSobraEm
     */
    public Long getQtdeSobraEm() {
        return qtdeSobraEm;
    }

    /**
     * @param qtdeSobraEm the qtdeSobraEm to set
     */
    public void setQtdeSobraEm(Long qtdeSobraEm) {
        this.qtdeSobraEm = qtdeSobraEm;
    }

    /**
     * @return the qtdeFaltaDe
     */
    public Long getQtdeFaltaDe() {
        return qtdeFaltaDe;
    }

    /**
     * @param qtdeFaltaDe the qtdeFaltaDe to set
     */
    public void setQtdeFaltaDe(Long qtdeFaltaDe) {
        this.qtdeFaltaDe = qtdeFaltaDe;
    }

    /**
     * @return the qtdeFaltaEm
     */
    public Long getQtdeFaltaEm() {
        return qtdeFaltaEm;
    }

    /**
     * @param qtdeFaltaEm the qtdeFaltaEm to set
     */
    public void setQtdeFaltaEm(Long qtdeFaltaEm) {
        this.qtdeFaltaEm = qtdeFaltaEm;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReparteFecharDiaDTO other = (ReparteFecharDiaDTO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		return true;
	}
	
}
