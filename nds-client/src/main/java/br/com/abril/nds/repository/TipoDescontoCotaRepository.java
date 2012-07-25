package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}  
 * 
 *
 */
public interface TipoDescontoCotaRepository extends Repository<TipoDescontoCota,Long>{

	Integer buscarTotalDescontoPorCota();
	
	void incluirDesconto(TipoDescontoCota tipoDescontoCota);	
	
	void excluirDesconto(TipoDescontoCota tipoDescontoCota);
	
	TipoDescontoCota obterTipoDescontoCotaPorId(long idDesconto);
	
	Integer buscarTotalDescontosPorCota();
	
	List<TipoDescontoCotaDTO> obterTipoDescontosCota();
	
}
