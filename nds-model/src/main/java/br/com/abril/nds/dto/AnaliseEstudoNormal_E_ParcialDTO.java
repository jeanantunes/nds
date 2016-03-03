package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class AnaliseEstudoNormal_E_ParcialDTO implements Serializable{

	private static final long serialVersionUID = 9151033672990209972L;
	
	
	private List<AnaliseParcialDTO> analiseParcialDTO;
	
	private List<AnaliseParcialExportXLSDTO> analiseParcialXLSDTO;
	
	private BigInteger total_qtdCotas;
    private BigInteger total_somatorioReparteSugerido;
    private BigInteger total_somatorioUltimoReparte;
    private BigInteger total_somatorioReparteEstudoOrigem;
    private BigDecimal percentualAbrangencia;
    private List<BigInteger> reparteTotalEdicao;
    private List<BigInteger> vendaTotalEdicao;
    private BigInteger saldo;
    
    
    private TableModel<CellModelKeyValue<AnaliseParcialDTO>> table;

    public List<AnaliseParcialDTO> getAnaliseParcialDTO() {
		return analiseParcialDTO;
	}
	public void setAnaliseParcialDTO(List<AnaliseParcialDTO> analiseParcialDTO) {
		this.analiseParcialDTO = analiseParcialDTO;
	}
	public BigInteger getTotal_qtdCotas() {
		return total_qtdCotas;
	}
	public void setTotal_qtdCotas(BigInteger total_qtdCotas) {
		this.total_qtdCotas = total_qtdCotas;
	}
	public BigInteger getTotal_somatorioReparteSugerido() {
		return total_somatorioReparteSugerido;
	}
	public void setTotal_somatorioReparteSugerido(BigInteger total_somatorioReparteSugerido) {
		this.total_somatorioReparteSugerido = total_somatorioReparteSugerido;
	}
	public BigInteger getTotal_somatorioUltimoReparte() {
		return total_somatorioUltimoReparte;
	}
	public void setTotal_somatorioUltimoReparte(BigInteger total_somatorioUltimoReparte) {
		this.total_somatorioUltimoReparte = total_somatorioUltimoReparte;
	}
	public TableModel<CellModelKeyValue<AnaliseParcialDTO>> getTable() {
		return table;
	}
	public void setTable(TableModel<CellModelKeyValue<AnaliseParcialDTO>> table) {
		this.table = table;
	}
	public BigDecimal getPercentualAbrangencia() {
		return percentualAbrangencia;
	}
	public void setPercentualAbrangencia(BigDecimal percentualAbrangencia) {
		this.percentualAbrangencia = percentualAbrangencia;
	}
	public List<BigInteger> getReparteTotalEdicao() {
		return reparteTotalEdicao;
	}
	public void setReparteTotalEdicao(List<BigInteger> reparteTotalEdicao) {
		this.reparteTotalEdicao = reparteTotalEdicao;
	}
	public List<BigInteger> getVendaTotalEdicao() {
		return vendaTotalEdicao;
	}
	public void setVendaTotalEdicao(List<BigInteger> vendaTotalEdicao) {
		this.vendaTotalEdicao = vendaTotalEdicao;
	}
	public List<AnaliseParcialExportXLSDTO> getAnaliseParcialXLSDTO() {
		return analiseParcialXLSDTO;
	}
	public void setAnaliseParcialXLSDTO(List<AnaliseParcialExportXLSDTO> analiseParcialXLSDTO) {
		this.analiseParcialXLSDTO = analiseParcialXLSDTO;
	}
	public BigInteger getSaldo() {
		return saldo;
	}
	public void setSaldo(BigInteger saldo) {
		this.saldo = saldo;
	}
	public BigInteger getTotal_somatorioReparteEstudoOrigem() {
		return total_somatorioReparteEstudoOrigem;
	}
	public void setTotal_somatorioReparteEstudoOrigem(BigInteger total_somatorioReparteEstudoOrigem) {
		this.total_somatorioReparteEstudoOrigem = total_somatorioReparteEstudoOrigem;
	}
	
}
