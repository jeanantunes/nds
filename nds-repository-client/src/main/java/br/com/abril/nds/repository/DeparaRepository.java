package br.com.abril.nds.repository;

import java.util.List;





import br.com.abril.nds.dto.DeparaDTO;
import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface DeparaRepository extends Repository<Depara, Long> {
	
	
	
	List<DeparaDTO> buscarDepara();
	String obterBoxDinap(String boxfc);
	
	public List<Depara> busca(String fc, String dinap, String  orderBy ,
			Ordenacao ordenacao, Integer initialResult, Integer maxResults) ;
	public Long quantidade(String fc, String dinap ) ;
	public boolean hasFc(String fc);

	
}
