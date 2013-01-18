package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.repository.CotaBaseRepository;

@Repository
public class CotaBaseRepositoryImpl extends AbstractRepositoryModel<CotaBase, Long> implements CotaBaseRepository {

	public CotaBaseRepositoryImpl() {
		super(CotaBase.class);
	}

	@Override
	public FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota) {
		StringBuilder hql = new StringBuilder();
        
        // RETURNING FIELDS
        hql.append(" select ");
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA        
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia "); // AREA DE INFLUÊNCIA
        
        // FROM
        hql.append(" FROM Cota as cota ");
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
        hql.append(" and cotaEndereco.principal = true ");
        hql.append(" and cota.numeroCota = :numeroCota ");
        
        hql.append(" group by cota.id ");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("numeroCota", numeroCota);        
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FiltroCotaBaseDTO.class));
        
        return  (FiltroCotaBaseDTO) query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaBaseDTO> obterCotasBases(Cota cotaNova) {
		
		StringBuilder hql = new StringBuilder();
		
		// RETURNING FIELDS
        hql.append(" select ");
        hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA        
        hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
        hql.append(" tipoPontoPDV.descricao as tipoPDV, "); // TIPO PDV
        hql.append(" endereco.bairro as bairro, "); // BAIRRO
        hql.append(" endereco.cidade as cidade, "); // CIDADE        
        hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorDeFluxo, "); // GERADOR DE FLUXO PRINCIPAL
        hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA DE INFLUÊNCIA
        hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamentoMedio "); // FATURAMENTO MENSAL
        
        // FROM
        hql.append(" FROM CotaBase as cotaBase ");
        hql.append(" left join cotaBase.cotas as cota ");
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
        hql.append(" and cotaEndereco.principal = true ");
        hql.append(" and cotaBase.id = :idCota ");
        
        hql.append(" GROUP BY cota.id");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("idCota", cotaNova.getId());        
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CotaBaseDTO.class));
        
		return query.list();
	}

	

}
