package br.com.abril.nds.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.DesenglobaDTO;
import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.repository.DesenglobacaoRepository;
import br.com.abril.nds.service.DesenglobacaoService;

@Service
public class DesenglobacaoServiceImpl implements DesenglobacaoService {

	@Autowired private DesenglobacaoRepository repository;
	
	@Override
	public void obterDesenglobacaoPorCota() {
		
	}

	@Override
	public void inserirDesenglobacao(List<DesenglobaDTO> desenglobaDTO) {
		List<Desenglobacao> desenglobada = new ArrayList<Desenglobacao>();
		try {
			
			BeanUtils.copyProperties(desenglobada, desenglobaDTO);
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repository.adicionar(desenglobada.get(0));
	}
}
