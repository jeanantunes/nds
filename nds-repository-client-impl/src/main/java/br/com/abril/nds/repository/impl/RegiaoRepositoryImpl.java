package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RegiaoRepository;

@Repository
public class RegiaoRepositoryImpl extends AbstractRepositoryModel<Regiao, Long> implements RegiaoRepository {
	
	public RegiaoRepositoryImpl( ) {
		super(Regiao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegiaoDTO> buscarRegiao() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT");
		hql.append(" regiao.id as idRegiao,");
		hql.append(" regiao.nomeRegiao as nomeRegiao, ");
		hql.append(" regiao.regiaoIsFixa as isFixa, ");
		hql.append(" regiao.dataRegiao as dataAlteracao, ");
		hql.append(" usuario.nome as nomeUsuario ");
		
		hql.append(" FROM Regiao as regiao ");
		hql.append(" JOIN regiao.idUsuario as usuario");
		hql.append(" order by regiao.nomeRegiao");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RegiaoDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegiaoCotaDTO> buscarCotasPorSegmento(FiltroCotasRegiaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT DISTINCT ");
		
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,");
		hql.append(" cota.tipoDistribuicaoCota as tipoDistribuicaoCota, ");
		hql.append(" cota.situacaoCadastro as tipoStatus ");
		
		hql.append(" FROM RankingSegmento as ranking ");
		hql.append(" LEFT JOIN ranking.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		hql.append(" LEFT JOIN ranking.tipoSegmentoProduto as segmento ");

		hql.append(" WHERE segmento.id = :idSegmento ");
		hql.append(" AND pdv.caracteristicas.pontoPrincipal = true");
		hql.append(" order by ranking.quantidade desc ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idSegmento", filtro.getIdSegmento());
		query.setMaxResults(filtro.getLimiteBuscaPorSegmento());
		query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoCotaDTO.class));
		
		return query.list();
	}
	
	@Override
	public void execucaoQuartz() {
		this.quartz1();
		this.quartz2();
	}
	
	private void quartz1 (){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" delete ");
		hql.append(" 	from ");
		hql.append("		 REGISTRO_COTA_REGIAO ");
		hql.append(" 	where ");
		hql.append(" 		REGIAO_ID in ( ");
		hql.append(" 			select regiao.ID ");
		hql.append(" 			from  regiao ");
		hql.append(" 			where  datediff(now(),data_regiao)>90 and REGIAO_IS_FIXA = 0 ) ");
		
		SQLQuery createSQLQuery = this.getSession().createSQLQuery(hql.toString());
		
		createSQLQuery.executeUpdate();
	}
	
	private void quartz2 (){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" delete from regiao WHERE datediff(now(),data_regiao)>90 and REGIAO_IS_FIXA = 0; ");
		SQLQuery createSQLQuery = this.getSession().createSQLQuery(hql.toString());
		
		createSQLQuery.executeUpdate();
	}
}
