package br.com.abril.nds.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;

public class GerarCobrancaHelper {
	
	private Cota cota;
	
	private FormaCobranca formaCobrancaPrincipal;
	
	private boolean cobrarHoje;
	
	private ConsolidadoFinanceiroCota consolidadoFinanceiroCota;
	
	private int qtdDiasNovaCobranca;
	
	private Fornecedor fornecedor;
	
	private List<Integer> diasSemanaConcentracaoPagamento;
	
	private Date dataVencimento, dataConsolidado;
	
	public GerarCobrancaHelper(Cota cota, FormaCobranca formaCobrancaPrincipal, boolean cobrarHoje, 
							   ConsolidadoFinanceiroCota consolidadoFinanceiroCota, int qtdDiasNovaCobranca,
							   Fornecedor fornecedor,
							   Date dataVencimento, Date dateConsolidado){
		
		this.cota = cota;
		this.formaCobrancaPrincipal = formaCobrancaPrincipal;
		this.cobrarHoje = cobrarHoje;
		this.consolidadoFinanceiroCota = consolidadoFinanceiroCota;
		this.qtdDiasNovaCobranca = qtdDiasNovaCobranca;
		this.fornecedor = fornecedor;
		this.dataVencimento = dataVencimento;
		this.dataConsolidado = dateConsolidado;
	}

	public FormaCobranca getFormaCobrancaPrincipal() {
		return formaCobrancaPrincipal;
	}

	public void setFormaCobrancaPrincipal(FormaCobranca formaCobrancaPrincipal) {
		this.formaCobrancaPrincipal = formaCobrancaPrincipal;
	}

	public boolean isCobrarHoje() {
		return cobrarHoje;
	}

	public void setCobrarHoje(boolean cobrarHoje) {
		this.cobrarHoje = cobrarHoje;
	}

	public ConsolidadoFinanceiroCota getConsolidadoFinanceiroCota() {
		return consolidadoFinanceiroCota;
	}

	public void setConsolidadoFinanceiroCota(
			ConsolidadoFinanceiroCota consolidadoFinanceiroCota) {
		this.consolidadoFinanceiroCota = consolidadoFinanceiroCota;
	}

	public int getQtdDiasNovaCobranca() {
		return qtdDiasNovaCobranca;
	}

	public void setQtdDiasNovaCobranca(int qtdDiasNovaCobranca) {
		this.qtdDiasNovaCobranca = qtdDiasNovaCobranca;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<Integer> getDiasSemanaConcentracaoPagamento() {
		
		if (formaCobrancaPrincipal!=null && 
			formaCobrancaPrincipal.getConcentracaoCobrancaCota() != null && 
			!formaCobrancaPrincipal.getConcentracaoCobrancaCota().isEmpty()){
			    
		    this.diasSemanaConcentracaoPagamento = new ArrayList<Integer>();
		    
		    for (ConcentracaoCobrancaCota c : formaCobrancaPrincipal.getConcentracaoCobrancaCota()){
		    	
		        this.diasSemanaConcentracaoPagamento.add(c.getDiaSemana().getCodigoDiaSemana());
		    }
		}
		
		return diasSemanaConcentracaoPagamento;
	}

	public void setDiasSemanaConcentracaoPagamento(
			List<Integer> diasSemanaConcentracaoPagamento) {
		this.diasSemanaConcentracaoPagamento = diasSemanaConcentracaoPagamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Date getDataConsolidado() {
		return dataConsolidado;
	}

	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}
}
