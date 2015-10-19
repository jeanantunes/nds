package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;








import br.com.abril.nds.dto.DeparaDTO;


import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface DeparaService {

	
	
	List<DeparaDTO> buscarDepara();
	void salvarDepara(Depara depara);
	void excluirDepara(Long id);
	Depara obterDeparaPorId(Long idDepara);
	void alterarDepara(Depara depara);
	String obterBoxDinap(String boxfc);
	public List<Depara> busca(String fc, String dinap, String orderBy, Ordenacao ordenacao, Integer initialResult, Integer maxResults) ;

	public Long quantidade(String fc, String dinap) ;
	public Depara buscarPorId(Long id) ;
	public Depara merge(Depara entity) throws UniqueConstraintViolationException, RelationshipRestrictionException;
	
}
