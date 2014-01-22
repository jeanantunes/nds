package br.com.abril.nds.service;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;

public interface SomarEstudosService {

	
	public void somarEstudos(Long idEstudo, ProdutoDistribuicaoVO distribuicaoVO);

	public Boolean verificarCoincidenciaEntreCotas(Long estudoBase,
			Long estudoSomado);

}
