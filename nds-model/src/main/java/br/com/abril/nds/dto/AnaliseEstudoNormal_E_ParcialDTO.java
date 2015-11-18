package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class AnaliseEstudoNormal_E_ParcialDTO implements Serializable{

	private static final long serialVersionUID = 9151033672990209972L;
	
	
	private List<AnaliseParcialDTO> analiseParcialDTO;
	
	private BigInteger total_qtdCotas;
    private BigInteger total_somatorioReparteSugerido;
    private BigInteger total_somatorioUltimoReparte;
    
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
	
}
