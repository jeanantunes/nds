package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;

public interface ViewContaCorrenteCotaRepository extends Repository<ViewContaCorrenteCota,Integer>{

	List<ViewContaCorrenteCota> getListaViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro);

	public abstract Long getQuantidadeViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro); 
}
