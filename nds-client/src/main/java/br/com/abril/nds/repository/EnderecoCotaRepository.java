package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;

public interface EnderecoCotaRepository extends Repository<EnderecoCota, Long> {

	void removerEnderecosCota(Long idCota, List<Endereco> listaEndereco);
	
}
