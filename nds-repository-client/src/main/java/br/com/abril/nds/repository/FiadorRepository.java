package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;

public interface FiadorRepository extends Repository<Fiador, Long> {

	Fiador obterFiadorPorCpf(String cpf);
	
	Fiador obterFiadorPorCnpj(String cnpj);
	
	ConsultaFiadorDTO obterFiadoresCpfCnpj(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO);

	Pessoa buscarPessoaFiadorPorId(Long idFiador);

	Long buscarIdPessoaFiador(Long idFiador);
	
	List<Pessoa> buscarSociosFiador(Long idFiador);

	Date buscarDataInicioAtividadeFiadorPorId(Long id);

	List<Cota> obterCotasAssociadaFiador(Long idFiador, Set<Long> cotasIgnorar);

	boolean verificarAssociacaoFiadorCota(Long idFiador, Integer numeroCota, Set<Long> idsIgnorar);

	PessoaFisica buscarSocioFiadorPorCPF(Long idFiador, String cpf);
	
	/**
	 * Busca imprecisa o Fiador.</br>
	 * O nome é comparado pelo inicio.
	 * @param nome Nome
	 * @param maxResults quantidade maxima de resultados
	 * @return <code>key</code> valorizado com o ID e <code>value</code com o Nome
	 */
	public abstract List<ItemDTO<Long, String>> buscaFiador(String nome, int maxResults);
	
	/**
	 * Obtem o fiador por CPF ou CNPJ
	 * @param doc CPF ou CNPJ
	 * @return Fiador
	 */
	public abstract Fiador obterPorCpfCnpj(String doc);

	List<Pessoa> obterFiadorPorNome(String nomeFiador);
}