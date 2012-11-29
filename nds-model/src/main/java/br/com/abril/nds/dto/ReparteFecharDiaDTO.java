package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ReparteFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
    @Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
    @Export(label = "Edição", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
    @Export(label = "Preço Capa", alignment = Alignment.LEFT, exhibitionOrder = 4)
	private BigDecimal precoVenda;

    @Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger qtdeReparte;
	
    @Export(label = "Sobras", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger qtdeSobra;
	
    @Export(label = "Faltas", alignment = Alignment.CENTER, exhibitionOrder = 7)
	private BigInteger qtdeFalta;
	
	private BigInteger qtdeSobraDe;
	
	private BigInteger qtdeSobraEm;
	
	private BigInteger qtdeFaltaDe;
	
	private BigInteger qtdeFaltaEm;

    @Export(label = "Transf.", alignment = Alignment.CENTER, exhibitionOrder = 8)
	private BigInteger qtdeTransferencia;
	
    @Export(label = "A Distr.", alignment = Alignment.CENTER, exhibitionOrder = 9)
	private BigInteger qtdeDistribuir;
	
    @Export(label = "Distribuido", alignment = Alignment.CENTER, exhibitionOrder = 10)
	private BigInteger qtdeDistribuido;
	
    @Export(label = "Sobra Distr.", alignment = Alignment.LEFT, exhibitionOrder = 11)
	private BigInteger qtdeSobraDistribuicao;
	
    @Export(label = "Diferença", alignment = Alignment.CENTER, exhibitionOrder = 12)
	private BigInteger qtdeDiferenca;

	private BigDecimal sobras;
	
	private BigDecimal faltas;
	
	private BigDecimal transferencias;
	
	private BigDecimal distribuidos;
	
	private BigDecimal valorTotalReparte;

	public ReparteFecharDiaDTO() {
    }
	
    public ReparteFecharDiaDTO(String codigo, String nomeProduto, Long numeroEdicao, BigDecimal precoVenda, BigInteger qtdeReparte,
            BigInteger qtdeSobraDe, BigInteger qtdeSobraEm, BigInteger qtdeFaltaDe, BigInteger qtdeFaltaEm, BigInteger qtdeDistribuido, 
            BigInteger qtdeTransferencia) {
        this.codigo = codigo;
        this.nomeProduto = nomeProduto;
        this.numeroEdicao = numeroEdicao;
        this.precoVenda = precoVenda;
        this.qtdeReparte = qtdeReparte;
        this.qtdeSobraDe = Util.nvl(qtdeSobraDe, BigInteger.ZERO);
        this.qtdeSobraEm = Util.nvl(qtdeSobraEm, BigInteger.ZERO);
        this.qtdeFaltaDe = Util.nvl(qtdeFaltaDe, BigInteger.ZERO);
        this.qtdeFaltaEm = Util.nvl(qtdeFaltaEm, BigInteger.ZERO);
        this.qtdeDistribuido = Util.nvl(qtdeDistribuido, BigInteger.ZERO);
        this.qtdeTransferencia = Util.nvl(qtdeTransferencia, BigInteger.ZERO);
        
        this.qtdeSobra = this.qtdeSobraDe.add(this.qtdeSobraEm);
        this.qtdeFalta = this.qtdeFaltaDe.add(this.qtdeFaltaEm);
        this.qtdeDistribuir = this.qtdeReparte.add(this.qtdeSobra).subtract(this.qtdeFalta).add(this.qtdeTransferencia);
        this.qtdeSobraDistribuicao = this.qtdeDistribuir.subtract(this.qtdeDistribuido);
        this.qtdeDiferenca = this.qtdeDistribuido.subtract(this.qtdeSobraDistribuicao);
    }


    /**
     * @return the sobras
     */
    public BigDecimal getSobras() {
        return sobras;
    }


    /**
     * @param sobras the sobras to set
     */
    public void setSobras(BigDecimal sobras) {
        this.sobras = sobras;
    }


    /**
     * @return the transferencias
     */
    public BigDecimal getTransferencias() {
        return transferencias;
    }


    /**
     * @param transferencias the transferencias to set
     */
    public void setTransferencias(BigDecimal transferencias) {
        this.transferencias = transferencias;
    }


    /**
     * @return the valorTotalReparte
     */
    public BigDecimal getValorTotalReparte() {
        return valorTotalReparte;
    }


    /**
     * @param valorTotalReparte the valorTotalReparte to set
     */
    public void setValorTotalReparte(BigDecimal valorTotalReparte) {
        this.valorTotalReparte = valorTotalReparte;
    }


    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nomeProduto
     */
    public String getNomeProduto() {
        return nomeProduto;
    }

    /**
     * @param nomeProduto the nomeProduto to set
     */
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    /**
     * @return the numeroEdicao
     */
    public Long getNumeroEdicao() {
        return numeroEdicao;
    }

    /**
     * @param numeroEdicao the numeroEdicao to set
     */
    public void setNumeroEdicao(Long numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    /**
     * @return the precoVenda
     */
    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    /**
     * @param precoVenda the precoVenda to set
     */
    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    /**
     * @return the qtdeReparte
     */
    public BigInteger getQtdeReparte() {
        return qtdeReparte;
    }


    /**
     * @return the qtdeSobra
     */
    public BigInteger getQtdeSobra() {
        return qtdeSobra;
    }


    /**
     * @return the qtdeFalta
     */
    public BigInteger getQtdeFalta() {
        return qtdeFalta;
    }


    /**
     * @return the qtdeSobraDe
     */
    public BigInteger getQtdeSobraDe() {
        return qtdeSobraDe;
    }


    /**
     * @return the qtdeSobraEm
     */
    public BigInteger getQtdeSobraEm() {
        return qtdeSobraEm;
    }


    /**
     * @return the qtdeFaltaDe
     */
    public BigInteger getQtdeFaltaDe() {
        return qtdeFaltaDe;
    }


    /**
     * @return the qtdeFaltaEm
     */
    public BigInteger getQtdeFaltaEm() {
        return qtdeFaltaEm;
    }


    /**
     * @return the qtdeTransferencia
     */
    public BigInteger getQtdeTransferencia() {
        return qtdeTransferencia;
    }


    /**
     * @return the qtdeDistribuir
     */
    public BigInteger getQtdeDistribuir() {
        return qtdeDistribuir;
    }


    /**
     * @return the qtdeDistribuido
     */
    public BigInteger getQtdeDistribuido() {
        return qtdeDistribuido;
    }


    /**
     * @return the qtdeSobraDistribuicao
     */
    public BigInteger getQtdeSobraDistribuicao() {
        return qtdeSobraDistribuicao;
    }


    /**
     * @return the qtdeDiferenca
     */
    public BigInteger getQtdeDiferenca() {
        return qtdeDiferenca;
    }


    /**
     * @return the faltas
     */
    public BigDecimal getFaltas() {
        return faltas;
    }


    /**
     * @return the distribuidos
     */
    public BigDecimal getDistribuidos() {
        return distribuidos;
    }
	
}
