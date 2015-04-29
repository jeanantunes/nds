package br.com.abril.nds.service;

import java.io.File;
import java.util.List;

import br.com.abril.nds.dto.BaixaBancariaConsolidadaDTO;

public interface LeitorArquivoBaixaFinanConsolidadaService {

	public List<BaixaBancariaConsolidadaDTO> obterPagamentosParaBaixa(File arquivo, String nomeArquivo);

	
}
