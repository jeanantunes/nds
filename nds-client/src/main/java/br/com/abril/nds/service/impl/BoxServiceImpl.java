package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
/**
 * Classe de implementação referente ao serviço da entidade 
 * {@link br.com.abril.nds.model.cadastro.Box}
 * 
 * @author Discover Technology
 *
 */
@Service
public class BoxServiceImpl implements BoxService{

	@Autowired
	private BoxRepository boxRepository;
	
	@Transactional(readOnly=true)
	@Override
	public List<Box> obterBoxPorProduto(String codigoProduto) {
		
		return boxRepository.obterBoxPorProduto(codigoProduto);
	}
	
	@Override
	@Transactional
	public void remover(Long id) throws RelationshipRestrictionException {
		Box box = boxRepository.buscarPorId(id);
		if(box != null){
			if(box.getCotas().isEmpty()){
				boxRepository.remover(box);
			}else{
				throw new RelationshipRestrictionException("Box: " + box.getCodigo() + " está relacionado há cota(s).");
			}
		}
	}

	@Override
	public Box buscarPorId(Long id) {
		return boxRepository.buscarPorId(id);
	}

	@Override
	public Box merge(Box entity) {
		return boxRepository.merge(entity);
	}

	@Override
	public List<Box> busca(String codigoBox, TipoBox tipoBox,
			boolean postoAvancado, String orderBy, Ordenacao ordenacao,
			int initialResult, int maxResults) {
		return boxRepository.busca(codigoBox, tipoBox, postoAvancado, orderBy,
				ordenacao, initialResult, maxResults);
	}

	@Override
	public Long quantidade(String codigoBox, TipoBox tipoBox,
			boolean postoAvancado) {
		return boxRepository.quantidade(codigoBox, tipoBox, postoAvancado);
	}
	
	
	
	
	
	
	
}
