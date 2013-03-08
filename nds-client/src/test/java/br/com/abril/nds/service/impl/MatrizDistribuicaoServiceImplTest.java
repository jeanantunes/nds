package br.com.abril.nds.service.impl;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.service.MatrizDistribuicaoService;

public class MatrizDistribuicaoServiceImplTest  {
	
	
	public static void main(String[] args) {
		
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("../nds-client/src/test/resources/applicationContext-local.xml");
				
		MatrizDistribuicaoService matrizDistribuicaoService = applicationContext.getBean(MatrizDistribuicaoService.class);
	
		ProdutoDistribuicaoVO distribuicaoVO = new ProdutoDistribuicaoVO();
		distribuicaoVO.setIdUsuario(1l);
		distribuicaoVO.setIdLancamento(new BigInteger("4"));
		distribuicaoVO.setCodigoProduto("26218001");
		distribuicaoVO.setNumeroEdicao(new BigInteger("15"));
		
		List<ProdutoDistribuicaoVO> list = new ArrayList<ProdutoDistribuicaoVO>();
 		
		list.add(distribuicaoVO);
		
		matrizDistribuicaoService.excluirEstudos(list);
		
	}
}
