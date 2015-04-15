package br.com.abril.nds.model.cadastro;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnaliseNormalProdutoEdicaoDTO {

	private BigInteger edicao;
	private String dataLancamento;
	private BigInteger reparte;
	private BigInteger venda;
	private String status;
	private String capa;

	public void setEdicao(BigInteger edicao) {
		this.edicao = edicao;
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dateFormat.format(dataLancamento);
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCapa(String capa) {
		this.capa = capa;
	}

}
