package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.CotaBaseRepository;

@Repository
public class CotaBaseRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements CotaBaseRepository {

	public CotaBaseRepositoryImpl() {
		super(Cota.class);
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

	

}
