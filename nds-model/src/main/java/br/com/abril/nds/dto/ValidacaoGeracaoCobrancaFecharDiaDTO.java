package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ValidacaoGeracaoCobrancaFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 4116643880953265518L;
	
	private String tipoFormaCobranca;
	
	private Long formaCobrancaId;
	
	private Integer diaDoMes;

	
	public String getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}

	public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca.getDescricao();
	}

	public Long getFormaCobrancaId() {
		return formaCobrancaId;
	}

	public void setFormaCobrancaId(Long formaCobrancaId) {
		this.formaCobrancaId = formaCobrancaId;
	}
	
	public Integer getDiaDoMes() {
		return diaDoMes;
	}

	public void setDiaDoMes(Integer diaDoMes) {
		this.diaDoMes = diaDoMes;
	}
	
}
