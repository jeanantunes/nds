package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.repository.FechamentoEncalheRepository;

@Repository
public class FechamentoEncalheRepositoryImpl extends AbstractRepository<FechamentoEncalhe, FechamentoEncalhePK> implements FechamentoEncalheRepository {

	public FechamentoEncalheRepositoryImpl(Class<FechamentoEncalhe> clazz) {
		super(clazz);
	}
	
	@Override
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro) {
		
		StringBuffer hql = new StringBuffer();
		hql.append("   select p.codigo,                                          \n");
		hql.append("          p.nome,                                            \n");
		hql.append("          pe.numeroEdicao,                                   \n");
		hql.append("          pe.precoVenda,                                     \n");
		hql.append("          sum(mec.qtde) as qtde,                             \n");
		hql.append("          (pe.precoVenda * mec.qtde) as total                \n");
		hql.append("     from ConferenciaEncalhe ce,                             \n");
		hql.append("          MovimentoEstoqueCota mec,                          \n");
		hql.append("          ControleConferenciaEncalheCota ccec,               \n");
		hql.append("          ProdutoEdicao pe,                                  \n");
		hql.append("          Produto p,                                         \n");
		hql.append("          ProdutoFornecedor pf                               \n");
		hql.append("    where ce.movimentoEstoqueCota.id = mec.id                \n");
		hql.append("      and ce.controleConferenciaEncalheCota.id = ccec.id     \n");
		hql.append("      and mec.produto_edicao_id = pe.id                      \n");
		hql.append("      and pe.produto_id = p.id                               \n");
		hql.append("      and ccec.box_id = 1                                    \n");
		hql.append("      and pf.produto_id = p.id                               \n");
		hql.append("      and pf.fornecedores_id = 2                             \n");
		hql.append(" group by p.id, p.nome, pe.numero_edicao, pe.preco_venda     \n");
		
		return null;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe) {
		
		String hql = " select distinct cec.cota.id                       \n"
				   + "   from ChamadaEncalhe ce,                         \n"
				   + "        ChamadaEncalheCota cec,                    \n"
				   + "        Cota c                                     \n"
				   + "  where ce.id = cec.chamadaEncalhe.id              \n"
				   + "    and ce.dataRecolhimento = :dataEncalhe         \n"
				   + "    and cec.cota.id not in (                       \n"
				   + "        select ccec.cota.id                        \n"
				   + "          from ControleConferenciaEncalheCota ccec \n"
				   + "         where ccec.dataOperacao = :dataEncalhe    \n"
				   + "        )";
		
		Query query = this.getSession().createQuery(hql);
		query.setParameter("dataEncalhe", dataEncalhe);
		query.setResultTransformer(Transformers.aliasToBean(CotaAusenteEncalheDTO.class));
		
		return query.list();
	}
}
