package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaBaseHistoricoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaBaseRepository;

@Repository
public class CotaBaseRepositoryImpl extends AbstractRepositoryModel<CotaBase, Long> implements CotaBaseRepository {

	public CotaBaseRepositoryImpl() {
		super(CotaBase.class);
	}

	@Override
	public FiltroCotaBaseDTO obterDadosFiltro(CotaBase cotaBase, boolean obterFaturamento, boolean semCotaBase, Integer numeroCota) {
		StringBuilder hql = new StringBuilder();
        
        // RETURNING FIELDS
        hql.append(" select ");        
        hql.append(" cota.id as idCota, "); // NÚMERO DA COTA
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA
                                                                        // DE
                                                                        // INFLUÊNCIA
        hql.append(" cota.tipoDistribuicaoCota as tpDistribCota "); // TIPO
                                                                    // DISTRIBUIÇÃO
                                                                    // COTA
        
        if(obterFaturamento){
        	hql.append(" , sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamentoMedio "); // FATURAMENTO MENSAL        	
        	hql.append(" FROM EstoqueProdutoCota as estoqueProdutoCota ");
        	hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");
        	hql.append(" LEFT JOIN estoqueProdutoCota.cota as cota ");
        }else if(semCotaBase){  
        	//COTA
        	hql.append(" FROM Cota as cota ");
        }else{
        	//COTA BASE 
        	hql.append(" , cotaBase.dataInicio as dataInicial, "); // DATA INICIAL
        	hql.append(" cotaBase.dataFim as dataFinal "); // DATA INICIAL
        	hql.append(" FROM CotaBaseCota as cotaBaseCota ");
        	hql.append(" LEFT JOIN cotaBaseCota.cota as cota ");
        	hql.append(" LEFT JOIN cotaBaseCota.cotaBase as cotaBase ");
        }
        
        hql.append(" left join cota.enderecos as cotaEndereco with cotaEndereco.principal = true ");
        hql.append(" left join cota.pessoa as pessoa ");
        hql.append(" left join cotaEndereco.endereco as endereco ");
        hql.append(" left join cota.pdvs as pdv with pdv.caracteristicas.pontoPrincipal = true ");
        hql.append(" left join pdv.segmentacao as segmento ");
        hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
        hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");
        
        hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
        hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");
        hql.append(" where  1 = 1");
        if(semCotaBase || obterFaturamento){
        	hql.append(" and cota.numeroCota = :numeroCota ");
        }else{
        	hql.append(" and cotaBase.id = :idCotaNova ");        	
        }
        
        hql.append(" group by cota.id ");
        
        Query query =  getSession().createQuery(hql.toString());
        
        if(semCotaBase || obterFaturamento){
        	query.setParameter("numeroCota", numeroCota);
        }else{
        	query.setParameter("idCotaNova", cotaBase.getId());
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FiltroCotaBaseDTO.class));
        
        return  (FiltroCotaBaseDTO) query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaBaseDTO> obterCotasBases(CotaBase cotaBase, CotaBaseDTO dto) {
		
		StringBuilder hql = new StringBuilder();
		
		// RETURNING FIELDS
        hql.append(" select ");
        hql.append(" cota.id as idCota, "); // NÚMERO DA COTA
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA
                                                                        // DE
                                                                        // INFLUÊNCIA
        hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamentoMedio, "); // FATURAMENTO MENSAL
        hql.append(" cotaBase.indiceAjuste as indiceAjuste "); // ÍNDICE DE
                                                               // AJUSTE
        if(dto != null){
        	hql.append(" , cotaBase.dataInicio as dtInicio "); // DATA INICIO
        	hql.append(" , cotaBase.dataFim as dtFinal "); // DATA FIM
        	hql.append(" FROM CotaBase as cotaBase ");
        	hql.append(" left join cotaBase.cota as cota ");
        }else{
        	hql.append(" FROM CotaBaseCota as cotaBaseCota ");
        	hql.append(" left join cotaBaseCota.cota as cota ");
        	hql.append(" left join cotaBaseCota.cotaBase as cotaBase ");
        }
        
        // FROM
        hql.append(" left join cota.estoqueProdutoCotas as estoqueProdutoCota ");
        hql.append(" left join estoqueProdutoCota.produtoEdicao as produtoEdicao ");        
        hql.append(" left join cota.enderecos as cotaEndereco ");
        hql.append(" left join cota.pessoa as pessoa ");
        hql.append(" left join cotaEndereco.endereco as endereco ");
        hql.append(" left join cota.pdvs as pdv ");
        hql.append(" left join pdv.segmentacao as segmento ");
        hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
        hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");
        
        hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
        hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");
        hql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
        if(dto == null){
        	hql.append(" and cotaBaseCota.ativo = true ");        	
        	hql.append(" and cotaBase.id = :idCotaBase ");        	
        }
        
        if(dto != null){
        	hql.append(" GROUP BY cotaBase.id");
        }else{
        	hql.append(" GROUP BY cota.id");        	
        }
        
        hql.append(" order by numeroCota ASC");
        
        Query query =  getSession().createQuery(hql.toString());
        
        if(dto == null){
        	query.setParameter("idCotaBase", cotaBase.getId());
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaBaseDTO.class));
        
        if(dto != null){
        	configurandoPaginacao(dto, query);
        }
        
		return query.list();
	}

	@Override
	public CotaBase obterCotaNova(Integer numeroCotaNova, Boolean ativo) {
		
		StringBuilder hql = new StringBuilder();
        
        hql.append(" SELECT cotaBase as cotaBase");
        
        hql.append(" FROM CotaBaseCota as cotaBaseCota");
        hql.append(" JOIN cotaBaseCota.cotaBase as cotaBase");
       
        if(ativo){
        	hql.append(" WHERE cotaBaseCota.ativo = true ");        	
        	hql.append(" AND cotaBase.cota.numeroCota = :numeroCotaNova ");
        }else{
        	hql.append(" WHERE cotaBase.cota.numeroCota = :numeroCotaNova ");
        }
        
        hql.append(" GROUP BY cotaBase.cota.id ");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("numeroCotaNova", numeroCotaNova);
        
        return  (CotaBase) query.uniqueResult();
	}

	@Override
	public FiltroCotaBaseDTO obterCotaDoFiltro(CotaBase cotaBase) {
		
		StringBuilder hql = new StringBuilder();
        
        // RETURNING FIELDS
        hql.append(" select ");        
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA
                                                                        // DE
                                                                        // INFLUÊNCIA
    	hql.append(" cotaBase.dataInicio as dataInicial, "); // DATA INICIAL
    	hql.append(" cotaBase.dataFim as dataFinal, "); // DATA INICIAL
        hql.append(" cota.tipoDistribuicaoCota as tpDistribCota "); // TIPO
                                                                    // DISTRIBUIÇÃO
                                                                    // COTA
    	
    	hql.append(" FROM CotaBaseCota as cotaBaseCota ");
    	hql.append(" LEFT JOIN cotaBaseCota.cotaBase as cotaBase ");        
    	hql.append(" LEFT JOIN cotaBase.cota as cota ");
        hql.append(" left join cota.enderecos as cotaEndereco ");
        hql.append(" left join cota.pessoa as pessoa ");
        hql.append(" left join cotaEndereco.endereco as endereco ");
        hql.append(" left join cota.pdvs as pdv ");
        hql.append(" left join pdv.segmentacao as segmento ");
        hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
        hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");        
        hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
        hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");
        
        hql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
       	hql.append(" and cotaBase.id = :idCotaNova ");        	
        
        hql.append(" group by cota.id ");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("idCotaNova", cotaBase.getId());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FiltroCotaBaseDTO.class));
        
        return  (FiltroCotaBaseDTO) query.uniqueResult();
	}
	
	private void configurandoPaginacao(CotaBaseDTO dto, Query query) {
		
		if (dto.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(dto.getPaginacao().getPosicaoInicial());
		}
		
		if (dto.getPaginacao().getQtdResultadosTotal().equals(0)) {
			dto.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(dto.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(dto.getPaginacao().getQtdResultadosPorPagina());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaBaseHistoricoDTO> obterCotasHistorico(CotaBase cotaBase, CotaBaseDTO dto) {
		
		StringBuilder hql = new StringBuilder();
		
		// RETURNING FIELDS
        hql.append(" select ");
        hql.append(" cota.id as idCota, "); // ID DA COTA
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA
                                                                        // DE
                                                                        // INFLUÊNCIA
        hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamentoMedio, "); // FATURAMENTO MENSAL
        hql.append(" CASE cotaBaseCota.tipoAlteracao WHEN 'INCLUSAO' THEN cotaBaseCota.dtInicioVigencia ELSE   "); 
        hql.append(" CASE cotaBaseCota.tipoAlteracao WHEN 'EXCLUSAO' THEN cotaBaseCota.dtFimVigencia END END "); 
        hql.append(" as dataAlteracao, "); // DATA ALTERAÇÃO
        hql.append(" cotaBaseCota.tipoAlteracao as tipoAlteracao "); // TIPO DE
                                                                     // ALTERAÇÃO
        
        // FROM
        hql.append(" FROM CotaBaseCota as cotaBaseCota ");        
        hql.append(" left join cotaBaseCota.cotaBase as cotaBase ");
        hql.append(" left join cotaBaseCota.cota as cota ");
        hql.append(" left join cota.estoqueProdutoCotas as estoqueProdutoCota ");
        hql.append(" left join estoqueProdutoCota.produtoEdicao as produtoEdicao ");        
        hql.append(" left join cota.enderecos as cotaEndereco ");
        hql.append(" left join cota.pessoa as pessoa ");
        hql.append(" left join cotaEndereco.endereco as endereco ");
        hql.append(" left join cota.pdvs as pdv ");
        hql.append(" left join pdv.segmentacao as segmento ");
        hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
        hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");
        
        hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
        hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");
        hql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
        hql.append(" and cotaBase.id = :idCotaBase ");
        
        
        hql.append(" GROUP BY cotaBaseCota.id");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("idCotaBase", cotaBase.getId());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaBaseHistoricoDTO.class));        
        
        configurandoPaginacao(dto, query);        
        
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaBaseDTO> obterListaCotaPesquisaGeral(CotaBaseDTO dto) {
		
		StringBuilder hql = new StringBuilder();
		
		// RETURNING FIELDS
        hql.append(" select ");
        hql.append(" cota.id as idCota, "); // NÚMERO DA COTA
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA
                                                                        // DE
                                                                        // INFLUÊNCIA
        hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamentoMedio, "); // FATURAMENTO MENSAL
        hql.append(" cotaBase.indiceAjuste as indiceAjuste "); // ÍNDICE DE
                                                               // AJUSTE
        hql.append(" , cotaBase.dataInicio as dtInicio "); // DATA INICIO
        hql.append(" , cotaBase.dataFim as dtFinal "); // DATA FIM
        
        // FROM
        hql.append(" FROM CotaBase as cotaBase ");
        hql.append(" left join cotaBase.cota as cota ");
        hql.append(" left join cota.estoqueProdutoCotas as estoqueProdutoCota ");
        hql.append(" left join estoqueProdutoCota.produtoEdicao as produtoEdicao ");        
        hql.append(" left join cota.enderecos as cotaEndereco ");
        hql.append(" left join cota.pessoa as pessoa ");
        hql.append(" left join cotaEndereco.endereco as endereco ");
        hql.append(" left join cota.pdvs as pdv ");
        hql.append(" left join pdv.segmentacao as segmento ");
        hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
        hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");
        
        hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
        hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");
        hql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
        hql.append(" and ((select count(cbc.id) from CotaBaseCota cbc where cbc.cotaBase.id = cotaBase.id and cbc.dtFimVigencia > (select dataOperacao from Distribuidor)) > 0)");
        
        if (dto.getNumeroCota() != null) {
        	hql.append(" and cota.numeroCota = :numeroCota");
        }
        
        hql.append(" GROUP BY cota.id");
        
        hql.append(" ORDER BY cota.numeroCota ASC ");
        
        Query query =  getSession().createQuery(hql.toString());
        
        if (dto.getNumeroCota() != null) {
        	query.setParameter("numeroCota", dto.getNumeroCota());
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaBaseDTO.class));
        
        configurandoPaginacao(dto, query);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaBaseDTO> obterListaTelaDetalhe(CotaBase cotaBase) {
		
		StringBuilder hql = new StringBuilder();
		
		// RETURNING FIELDS
        hql.append(" select ");
        hql.append(" cota.id as idCota, "); // ID DA COTA
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota, "); // NOME DA PESSOA        
        hql.append(" cotaBase.indiceAjuste as indiceAjuste "); // ÍNDICE DE
                                                               // AJUSTE
        
        // FROM
    	hql.append(" FROM CotaBaseCota as cotaBaseCota ");
    	hql.append(" left join cotaBaseCota.cota as cota ");
    	hql.append(" left join cotaBaseCota.cotaBase as cotaBase ");        
        hql.append(" left join cota.pessoa as pessoa ");
        
        //WHERE
    	hql.append(" WHERE cotaBaseCota.ativo = true ");        	
    	hql.append(" AND cotaBase.id = :idCotaBase ");        	
    
    	hql.append(" GROUP BY cota.id");
    	
        Query query =  getSession().createQuery(hql.toString());
        
       	query.setParameter("idCotaBase", cotaBase.getId());
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaBaseDTO.class));
        
		return query.list();
	}
	
	@Override
	public boolean cotaTemCotaBase(Long idCota){
		
		Query query = 
			this.getSession().createQuery(
				"select count(b.id) from CotaBase b where b.cota.id = :idCota ");
		
		query.setParameter("idCota", idCota);
		
		return (Long)query.uniqueResult() > 0;
	}
	
	@Override
	public CotaBase obterSituacaoCota(Long idCota){
		
		final Criteria criteria = super.getSession().createCriteria(CotaBase.class);
        
		criteria.add(Restrictions.eq("cota.id", idCota));
		
        criteria.addOrder(Order.desc("dataFim"));
        criteria.setMaxResults(1);
        
        return (CotaBase)criteria.uniqueResult();
		
	}

}
