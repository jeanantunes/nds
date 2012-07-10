package br.com.abril.nds.service.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.PermissaoRepository;
import br.com.abril.nds.service.PermissaoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Implementação da entidade do serviço de permissões do sistema
 * @author InfoA2
 */
@Service
public class PermissaoServiceImpl implements PermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.PermissaoService#busca(java.lang.String, java.lang.String, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Transactional
	@Override
	public List<Permissao> busca(String nome, String descricao, String orderBy,
			Ordenacao ordenacao, int initialResult, int maxResults) {
		return permissaoRepository.busca(nome, descricao, orderBy, ordenacao, initialResult, maxResults);
	}

	@Override
	public Long quantidade(String nome, String descricao) {
		return permissaoRepository.quantidade(nome, descricao);
	}
	
}
