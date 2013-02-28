package br.com.abril.nds.model.integracao;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.dto.chamadaencalhe.integracao.ChamadaEncalheFornecedorIntegracaoDTO;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;

public class EMS0138NotasCEIntegracao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ChamadaEncalheFornecedorIntegracaoDTO> chamadasEncalhe;
	
	private List<NotaFiscalSaidaFornecedor> notasFiscaisSaida;
	
	private String tipoDocumento;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<ChamadaEncalheFornecedorIntegracaoDTO> getChamadasEncalhe() {
		return chamadasEncalhe;
	}

	public void setChamadasEncalhe(List<ChamadaEncalheFornecedorIntegracaoDTO> chamadasEncalhe) {
		this.chamadasEncalhe = chamadasEncalhe;
	}

	public List<NotaFiscalSaidaFornecedor> getNotasFiscaisSaida() {
		return notasFiscaisSaida;
	}

	public void setNotasFiscaisSaida(
			List<NotaFiscalSaidaFornecedor> notasFiscaisSaida) {
		this.notasFiscaisSaida = notasFiscaisSaida;
	}
	
}
