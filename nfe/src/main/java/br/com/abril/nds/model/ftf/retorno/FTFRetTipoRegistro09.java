package br.com.abril.nds.model.ftf.retorno;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;

public class FTFRetTipoRegistro09 extends FTFBaseDTO{


	
	private String tipoRegistro;
	
	private String quantidadePedidosDoArquivo;
	
	private String quantidadePedidosEnviados;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	@FTFfield(tamanho = 1, tipo = "char", ordem = 1)
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getQuantidadePedidosDoArquivo() {
		return quantidadePedidosDoArquivo;
	}

	@FTFfield(tamanho = 6, tipo = "char", ordem = 2)
	public void setQuantidadePedidosDoArquivo(String quantidadePedidosDoArquivo) {
		this.quantidadePedidosDoArquivo = quantidadePedidosDoArquivo;
	}

	public String getQuantidadePedidosEnviados() {
		return quantidadePedidosEnviados;
	}

	@FTFfield(tamanho = 6, tipo = "char", ordem = 3)
	public void setQuantidadePedidosEnviados(String quantidadePedidosEnviados) {
		this.quantidadePedidosEnviados = quantidadePedidosEnviados;
	}
	
	
	
}
