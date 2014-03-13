package br.com.abril.nds.service;

import java.io.File;
import java.util.Date;

public interface P3Service {

	public File gerarTxt(Date dataInicial, Date dataFinal);
	
	public Integer countBusca (Date dataInicial, Date dataFinal);
	
	public File gerarMovimentacaoCompletaTxt(Date dataInicial, Date dataFinal);
}
