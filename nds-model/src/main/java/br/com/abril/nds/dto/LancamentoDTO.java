package br.com.abril.nds.dto;

import java.io.Serializable;

public class LancamentoDTO  implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;

	private Long id;
	private String codigoProduto;
	private String nomeProduto;
	private Long numEdicao;
	private String preco;
	private int pacotePadrao;
	private String reparte;
	private String lancamento;
	private String dataRecolhimento;
	private Long idFornecedor;
	private String nomeFornecedor;
	private String dataPrevisto;
	private String dataMatrizDistrib;
	private String total;
	private String fisico;
	private String qtdeEstudo;
	private boolean semFisico;
	private boolean cancelamentoGD;
	private boolean furo;
	private boolean estudoFechado;
	private boolean expedido;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public Long getNumEdicao() {
		return numEdicao;
	}
	
	public void setNumEdicao(Long numEdicao) {
		this.numEdicao = numEdicao;
	}
	
	public String getPreco() {
		return preco;
	}
	
	public void setPreco(String preco) {
		this.preco = preco;
	}
	
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	public String getReparte() {
		return reparte;
	}
	
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}
	
	public String getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(String lancamento) {
		this.lancamento = lancamento;
	}
	
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	
	public String getDataPrevisto() {
		return dataPrevisto;
	}
	
	public void setDataPrevisto(String dataPrevisto) {
		this.dataPrevisto = dataPrevisto;
	}
	
	public String getDataMatrizDistrib() {
		return dataMatrizDistrib;
	}
	
	public void setDataMatrizDistrib(String dataMatrizDistrib) {
		this.dataMatrizDistrib = dataMatrizDistrib;
	}
	
	public String getTotal() {
		return total;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getFisico() {
		return fisico;
	}
	
	public void setFisico(String fisico) {
		this.fisico = fisico;
	}
	
	public String getQtdeEstudo() {
		return qtdeEstudo;
	}
	
	public void setQtdeEstudo(String qtdeEstudo) {
		this.qtdeEstudo = qtdeEstudo;
	}
	
	public boolean isSemFisico() {
		return semFisico;
	}
	
	public void setSemFisico(boolean semFisico) {
		this.semFisico = semFisico;
	}
	
	public boolean isCancelamentoGD() {
		return cancelamentoGD;
	}
	
	public void setCancelamentoGD(boolean cancelamentoGD) {
		this.cancelamentoGD = cancelamentoGD;
	}
	
	public boolean isFuro() {
		return furo;
	}
	
	public void setFuro(boolean furo) {
		this.furo = furo;
	}
	
	public boolean isEstudoFechado() {
		return estudoFechado;
	}
	
	public void setEstudoFechado(boolean estudoFechado) {
		this.estudoFechado = estudoFechado;
	}
	
	public boolean isExpedido() {
		return expedido;
	}
	
	public void setExpedido(boolean expedido) {
		this.expedido = expedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LancamentoDTO other = (LancamentoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
