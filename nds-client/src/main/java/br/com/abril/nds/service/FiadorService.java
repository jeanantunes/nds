package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Fiador;

public interface FiadorService {

	public List<Fiador> obterFiadores(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO);
}
