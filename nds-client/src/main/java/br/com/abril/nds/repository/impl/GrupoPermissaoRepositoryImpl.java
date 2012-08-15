package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.repository.GrupoPermissaoRepository;
 
@Repository
public class GrupoPermissaoRepositoryImpl extends AbstractRepositoryModel<GrupoPermissao,Long> implements GrupoPermissaoRepository {

	/**
	 * Construtor padrão
	 */
	public GrupoPermissaoRepositoryImpl() {
		super(GrupoPermissao.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.GrupoPermissaoRepository#buscaFiltrada()
	 */
	@Override
	public List<ResultadoGrupoVO> buscaFiltrada(FiltroConsultaGrupoDTO filtro) {

		Criteria criteria =  getSession().createCriteria(GrupoPermissao.class, "grupoPermissao");	
		
		criteria.setProjection( Projections.projectionList()
			    .add(Projections.groupProperty("grupoPermissao.id"), "id")  
			    .add(Projections.groupProperty("grupoPermissao.nome"), "nome"))
			    .setResultTransformer(Transformers.aliasToBean(ResultadoGrupoVO.class)); 
		
		if (filtro != null) {
			if( filtro.getNome() != null && !filtro.getNome().isEmpty()){
				criteria.add(Restrictions.ilike("nome", "%" + filtro.getNome() + "%"));
			}
		}
		
		return criteria.list();
	}

}
