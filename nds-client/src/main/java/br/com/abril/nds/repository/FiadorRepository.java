package br.com.abril.nds.repository;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Fiador;

public interface FiadorRepository extends Repository<Fiador, Long> {

	Fiador obterFiadorPorCpf(String cpf);
	
	Fiador obterFiadorPorCnpj(String cnpj);
	
	ConsultaFiadorDTO obterFiadoresCpfCnpj(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO);
}