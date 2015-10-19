package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;






import br.com.abril.nds.dto.DeparaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.DeparaRepository;
import br.com.abril.nds.service.DeparaService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class DeparaServiceImpl implements DeparaService  {
	
	@Autowired
	private DeparaRepository deparaRepository;

	
	
	@Override
	@Transactional
	public void salvarDepara(Depara depara) {
		deparaRepository.adicionar(depara);
		}
	
	@Transactional(readOnly=true)
	@Override
	public List<DeparaDTO> buscarDepara() {
		return deparaRepository.buscarDepara();
	}
	
	

	@Override
	@Transactional
	public void excluirDepara(Long id) {
		Depara depara = this.deparaRepository.buscarPorId(id);
		
		deparaRepository.remover(depara);
	}

	
	@Override
	@Transactional
	public Depara obterDeparaPorId(Long idDepara) {
		return deparaRepository.buscarPorId(idDepara);
	}



	@Override
	@Transactional
	public void alterarDepara(Depara depara) {
		deparaRepository.merge(depara);
	}
	
	@Override
	@Transactional
	public String obterBoxDinap(String boxfc){
		 return deparaRepository.obterBoxDinap(boxfc);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Depara> busca(String fc, String dinap, String orderBy, Ordenacao ordenacao, Integer initialResult, Integer maxResults) {
		return deparaRepository.busca(fc, dinap, orderBy, ordenacao, initialResult, maxResults);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long quantidade(String fc, String dinap) {
		return deparaRepository.quantidade(fc, dinap);
	}

	
	@Override
	@Transactional(readOnly = true)
	public Depara buscarPorId(Long id) {
		return deparaRepository.buscarPorId(id);
	}

	@Override
	@Transactional
	public Depara merge(Depara entity) throws UniqueConstraintViolationException, RelationshipRestrictionException {
		if (deparaRepository.hasFc(entity.getFc())) {
			throw new UniqueConstraintViolationException("Código FC "
					+ entity.getFc() + " do Depara em uso.");		}
		
		/*if (entity.getId() != null &&  hasAssociacao(entity.getId())) {
			throw new RelationshipRestrictionException(
						"Box está em uso e não pode ser editado.");
			
		}*/
		
		
		return deparaRepository.merge(entity);
	}
	
	
}
