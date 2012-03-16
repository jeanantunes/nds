package br.com.abril.nds.service;

import br.com.abril.nds.client.endereco.vo.EnderecoVO;

public interface ConsultaBaseEnderecoService {
	
	EnderecoVO buscarPorCep(String cep);

}
