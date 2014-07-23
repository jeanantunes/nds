package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Fornecedor;

/**
 * Classe responsável por armazenar os valores referente aos registros de 
 * dias de operação (lançamento e recolhimento) do fornecedor.
 * @author InfoA2
 */
public class RegistroDiaOperacaoFornecedorVO implements Serializable {

	private static final long serialVersionUID = 4974266889254112715L;

	private Fornecedor fornecedor;
	
	private String diasLancamento;
	
	private String diasRecolhimento;

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getDiasLancamento() {
		return diasLancamento;
	}

	public void setDiasLancamento(String diasLancamento) {
		this.diasLancamento = diasLancamento;
	}

	public String getDiasRecolhimento() {
		return diasRecolhimento;
	}

	public void setDiasRecolhimento(String diasRecolhimento) {
		this.diasRecolhimento = diasRecolhimento;
	}
	
}
