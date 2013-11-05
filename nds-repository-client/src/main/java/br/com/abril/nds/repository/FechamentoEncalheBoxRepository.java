package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;

public interface FechamentoEncalheBoxRepository extends Repository<FechamentoEncalheBox, FechamentoEncalheBoxPK> {
	
	 List<FechamentoEncalheBox> buscarFechamentoEncalheBox(	FiltroFechamentoEncalheDTO filtro);
	 
	 boolean verificarExistenciaFechamentoEncalheDetalhado(Date dataEncalhe);
	 
	 

}
