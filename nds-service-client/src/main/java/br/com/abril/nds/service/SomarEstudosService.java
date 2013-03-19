package br.com.abril.nds.service;

import java.math.BigInteger;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface SomarEstudosService {

	
	public void somarEstudos(Long idEstudo, ProdutoDistribuicaoVO distribuicaoVO);

}
