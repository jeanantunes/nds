package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InfoGeralExtratoEdicaoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4567167441475999143L;

	@Export(label = "Saldo em Estoque", alignWithHeader = "Parcial", alignment = Alignment.CENTER)
	private BigInteger saldoTotalExtratoEdicao;
	
	private List<ExtratoEdicaoDTO> listaExtratoEdicao;

	public BigInteger getSaldoTotalExtratoEdicao() {
		return saldoTotalExtratoEdicao;
	}

	public void setSaldoTotalExtratoEdicao(BigInteger saldoTotalExtratoEdicao) {
		this.saldoTotalExtratoEdicao = saldoTotalExtratoEdicao;
	}

	public List<ExtratoEdicaoDTO> getListaExtratoEdicao() {
		return listaExtratoEdicao;
	}

	public void setListaExtratoEdicao(List<ExtratoEdicaoDTO> listaExtratoEdicao) {
		this.listaExtratoEdicao = listaExtratoEdicao;
	}
	
	
}
