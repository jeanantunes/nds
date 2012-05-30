package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao serviço da entidade
 * {@link br.com.abril.nds.model.cadastro.Box}
 * 
 * @author Discover Technology
 * 
 */
@Service
public class BoxServiceImpl implements BoxService {

	@Autowired
	private BoxRepository boxRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Box> obterBoxPorProduto(String codigoProduto) {

		return boxRepository.obterBoxPorProduto(codigoProduto);
	}

	@Override
	@Transactional(rollbackFor = { RelationshipRestrictionException.class })
	public void remover(Long id) throws RelationshipRestrictionException {

		if (!boxRepository.hasCotasVinculadas(id)
				&& !boxRepository.hasCotasVinculadas(id)) {
			boxRepository.removerPorId(id);
		} else {
			throw new RelationshipRestrictionException(
					"Box está em uso e não pode ser removido.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public Box buscarPorId(Long id) {
		return boxRepository.buscarPorId(id);
	}

	@Override
	@Transactional
	public Box merge(Box entity) throws UniqueConstraintViolationException, RelationshipRestrictionException {
		if (boxRepository.hasCodigo(entity.getCodigo(), entity.getId())) {
			throw new UniqueConstraintViolationException("Código "
					+ entity.getCodigo() + " do box em uso.");
		}
		
		if (entity.getId() != null &&  hasAssociacao(entity.getId())) {
			Box boxSalvo = boxRepository.buscarPorId(entity.getId());			
			if(!compareBox(entity, boxSalvo)){
				throw new RelationshipRestrictionException(
						"Box está em uso. Apenas o Nome pode ser editado.");
			}
		}
		
		
		return boxRepository.merge(entity);
	}
	
	/**
	 * Compara igualdade dos boxs desconsiderando o nome
	 * @param box
	 * @param other
	 * @return
	 */
	private boolean compareBox(Box box, Box other){
		
		if (box.getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!box.getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (box.getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!box.getId().equals(other.getId())) {
			return false;
		}
		if (box.isPostoAvancado() != other.isPostoAvancado()) {
			return false;
		}
		if (box.getTipoBox() != other.getTipoBox()) {
			return false;
		}
		return true;
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Box> busca(String codigoBox, TipoBox tipoBox,
			boolean postoAvancado, String orderBy, Ordenacao ordenacao,
			int initialResult, int maxResults) {
		return boxRepository.busca(codigoBox, tipoBox, postoAvancado, orderBy,
				ordenacao, initialResult, maxResults);
	}

	@Override
	@Transactional(readOnly = true)
	public Long quantidade(String codigoBox, TipoBox tipoBox,
			boolean postoAvancado) {
		return boxRepository.quantidade(codigoBox, tipoBox, postoAvancado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long id) {
		return boxRepository.obtemCotaRotaRoteiro(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Box> buscarPorTipo(TipoBox tipo) {
		return boxRepository.obterListaBox(tipo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Box> buscarTodos(TipoBox tipoBox) {

		if (tipoBox == null) {
			return boxRepository.buscarTodos();
		}

		return boxRepository.obterListaBox(tipoBox);
	}
	
	@Transactional(readOnly=true)
	@Override
	public boolean hasAssociacao(long id){
		return boxRepository.hasCotasVinculadas(id) || boxRepository.hasRoteirosVinculados(id);
	}
}
