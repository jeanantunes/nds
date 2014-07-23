package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.P3DTO;

public interface P3Repository {

	Boolean isRegimeEspecial();

	List<P3DTO> obterP3SemRegimeEspecial_Entrada(Date dataInicial, Date dataFinal);
	
	List<P3DTO> obterP3SemRegimeEspecial_Saida(Date dataInicial, Date dataFinal);
	
	List<P3DTO> obterP3ComRegimeEspecial_NotaEnvio(Date dataInicial, Date dataFinal);
	
	List<P3DTO> obterP3ComRegimeEspecial_NotaFiscalNovo(Date dataInicial, Date dataFinal);
	
	List<P3DTO> obterP3MovimentacaoCompleta(Date dataInicial, Date dataFinal);
	
	Integer count_obterP3SemRegimeEspecial_Entrada(Date dataInicial, Date dataFinal);
	
	Integer count_obterP3SemRegimeEspecial_Saida(Date dataInicial, Date dataFinal);
	
	Integer count_obterP3ComRegimeEspecial_NotaEnvio(Date dataInicial, Date dataFinal);
	
	Integer count_obterP3ComRegimeEspecial_NotaFiscalNovo(Date dataInicial, Date dataFinal);

	Integer count_obterP3MovimentacaoCompleta(Date dataInicial,Date dataFinal); 
}
