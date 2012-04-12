package br.com.abril.nds.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.service.ConsultaEncalheService;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Transactional
	public ConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ConsultaEncalheDTO> getMockedResult() {
	
		List<ConsultaEncalheDTO> resultado = new LinkedList<ConsultaEncalheDTO>();
		
		/*
		ConsultaEncalheDTO consulta = new ConsultaEncalheDTO();
		
		consulta.setCodigoProduto(codigoProduto)
		consulta.setEncalhe(encalhe);
		consulta.setFornecedor(fornecedor);
		consulta.setIdProdutoEdicao(idProdutoEdicao);
		consulta.setNomeProduto(nomeProduto);
		consulta.setNumeroEdicao(numeroEdicao);
		consulta.setPrecoComDesconto(precoComDesconto);
		
		resultado.add(arg0)
		*/
		
		return resultado;
		
	}
	

}
