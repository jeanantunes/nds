package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.repository.SegmentoNaoRecebidoRepository;

@Repository
public class SegmentoNaoRecebidoRepositoryImpl extends AbstractRepositoryModel<SegmentoNaoRecebido, Long> implements SegmentoNaoRecebidoRepository {

	public SegmentoNaoRecebidoRepositoryImpl() {
		super(SegmentoNaoRecebido.class);
	}

	@Override
	public List<CotaNaoRecebeSegmentoDTO> buscarCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		Criteria criteria = getSession().createCriteria(SegmentoNaoRecebido.class);
		criteria = criteria.createCriteria("tipoSegmentoProduto");
		criteria.add(Restrictions.eq("id", filtro.getTipoSegmentoProdutoId()));
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(CotaNaoRecebeSegmentoDTO.class));
		
		List<CotaNaoRecebeSegmentoDTO> test = criteria.list();
		
		return criteria.list();
	}

	@Override
	public List<SegmentoNaoRecebeCotaDTO> buscarSegmentosNaoRecebemCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}


}
