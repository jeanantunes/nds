package br.com.abril.nds.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.CotaRepository;
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
	
	@Autowired
	private CotaRepository cotaRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Box> obterBoxPorProduto(String codigoProduto) {
		
		codigoProduto = StringUtils.leftPad(codigoProduto, 8, '0');

		return boxRepository.obterBoxPorProduto(codigoProduto);
	}

	@Override
	@Transactional(rollbackFor = { RelationshipRestrictionException.class })
	public void remover(Long id) throws RelationshipRestrictionException {

		try{
			this.boxRepository.removerPorId(id);
		} catch (DataIntegrityViolationException e) {
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
					+ entity.getCodigo() + " do box em uso.");		}
		
		/*if (entity.getId() != null &&  hasAssociacao(entity.getId())) {
			throw new RelationshipRestrictionException(
						"Box está em uso e não pode ser editado.");
			
		}*/
		
		
		return boxRepository.merge(entity);
	}
	

	
	
	@Override
	@Transactional(readOnly = true)
	public List<Box> busca(Integer codigoBox, TipoBox tipoBox, String orderBy, Ordenacao ordenacao, Integer initialResult, Integer maxResults) {
		return boxRepository.busca(codigoBox, tipoBox, orderBy, ordenacao, initialResult, maxResults);
	}

	@Override
	@Transactional(readOnly = true)
	public Long quantidade(Integer codigoBox, TipoBox tipoBox) {
		return boxRepository.quantidade(codigoBox, tipoBox);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long id, String sortname, String sortorder) {
		return boxRepository.obtemCotaRotaRoteiro(id,sortname,sortorder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Box> buscarPorTipo(List<TipoBox> tipo) {
		return boxRepository.obterListaBox(tipo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Box> buscarTodos(TipoBox tipoBox) {

		if (tipoBox == null) {
			return boxRepository.buscarTodos();
		}

		return boxRepository.obterListaBox(Arrays.asList(tipoBox));
	}
	
	@Transactional(readOnly=true)
	@Override
	public boolean hasAssociacao(long id){
		return boxRepository.hasCotasVinculadas(id) || boxRepository.hasRoteirosVinculados(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Box obterBoxPorCota(Integer numeroCota) {
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		if(cota == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não existe.");
		
		return cota.getBox();
	}
	
	/**
	 * Obtem lista de Box por intervalo de Código
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 * @return List<Box>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Box> obterBoxPorIntervaloCodigo(Integer codigoBoxDe, Integer codigoBoxAte){
		
		if (codigoBoxDe != null && codigoBoxAte != null && codigoBoxDe.compareTo(codigoBoxAte) > 0 ){
			
			codigoBoxDe = codigoBoxAte;
			
			codigoBoxAte = codigoBoxDe;
		}

		List<Box> boxes = null;
		
		if (codigoBoxDe != null && codigoBoxAte != null) {
		
			boxes = this.boxRepository.obterBoxPorIntervaloCodigo(codigoBoxDe, codigoBoxAte);
		
		} else if (codigoBoxDe != null) {
			
			boxes = this.boxRepository.obterBoxPorCodigo(codigoBoxDe);
			
		} else if (codigoBoxAte != null) {
			
			boxes = this.boxRepository.obterBoxPorCodigo(codigoBoxAte);
		}
		
		return boxes;
	}
	
	/**
	 * Busca lista de Box por Rota
	 * @param rotaId
	 * @return List<Box>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Box> buscarBoxPorRota(Long rotaId) {

		return boxRepository.buscarBoxPorRota(rotaId);
	}

	/**
	 * Busca lista de Box por Roteiro
	 * @param roteiroId
	 * @return List<Box>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Box> buscarBoxPorRoteiro(Long roteiroId) {

		return boxRepository.buscarBoxPorRoteiro(roteiroId);
	}

	@Override
	@Transactional(readOnly=true)
	public String obterDescricaoBoxPorCota(Integer numeroCota) {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número de cota é obrigatório.");
		}
		
		return this.boxRepository.obterDescricaoBoxPorCota(numeroCota);
	}
}
