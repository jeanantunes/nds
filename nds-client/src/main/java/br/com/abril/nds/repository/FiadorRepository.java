package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Pessoa;

public interface FiadorRepository extends Repository<Fiador, Long> {

	Fiador obterFiadorPorCpf(String cpf);
	
	Fiador obterFiadorPorCnpj(String cnpj);
	
	ConsultaFiadorDTO obterFiadoresCpfCnpj(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO);

	Pessoa buscarPessoaFiadorPorId(Long idFiador);

	Long buscarIdPessoaFiador(Long idFiador);
	
	List<Pessoa> buscarSociosFiador(Long idFiador);

	Date buscarDataInicioAtividadeFiadorPorId(Long id);

	List<Cota> obterCotasAssociadaFiador(Long idFiador);
}