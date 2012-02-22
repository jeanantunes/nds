package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.service.FuroProdutoService;

@Service
public class FuroProdutoServiceImpl implements FuroProdutoService {

	@Transactional
	@Override
	public void efetuarFuroProduto(String codigo, Long edicao, Date novaData) {
		if (codigo == null || codigo.isEmpty()){
			throw new IllegalArgumentException("Código é obrigatório.");
		}
		
		if (edicao == null){
			throw new IllegalArgumentException("Edição é obrigatório.");
		}
		
		if (novaData == null){
			throw new IllegalArgumentException("Data Lançamento é obrigatório.");
		}
	}

}
