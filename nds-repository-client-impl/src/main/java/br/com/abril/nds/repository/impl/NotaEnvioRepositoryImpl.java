package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.NotaEnvioProdutoEdicao;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaEnvioRepository;

@Repository
public class NotaEnvioRepositoryImpl  extends AbstractRepositoryModel<NotaEnvio, Long> implements NotaEnvioRepository{

	public NotaEnvioRepositoryImpl() {
		super(NotaEnvio.class);
	}

	@Override
	public boolean verificarExistenciaEmissaoDeNotasPeloEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(*) from EstudoCota estudoCota")
		   .append(" where estudoCota.estudo.id = :idEstudo") 
		   .append(" and   estudoCota.itemNotaEnvio = null");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("idEstudo", idEstudo);
		
		
		Integer count = (Integer)query.uniqueResult();
		
		return count == 0;
	}
	
	@Override
	public Date obterMenorDataLancamentoPorNotaEnvio(Long numeroNotaEnvio) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select min(lancamento.dataLancamentoDistribuidor) ");
		hql.append(" from NotaEnvio notaEnvio ");
		hql.append(" join notaEnvio.listaItemNotaEnvio itemNotaEnvio ");
		hql.append(" join itemNotaEnvio.estudoCota estudoCota ");
		hql.append(" join estudoCota.estudo estudo ");
		hql.append(" join estudo.lancamentos lancamento ");
		hql.append(" where notaEnvio.id = :numeroNotaEnvio ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroNotaEnvio", numeroNotaEnvio);
		
		return (Date) query.uniqueResult();
	}
	
	
	public List<NotaEnvioProdutoEdicao> obterNotaEnvioProdutoEdicao(Integer numeroCota, List<Long> idsProdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select nota.numero as numeroNotaEnvio, ");
		hql.append(" nota.dataEmissao as dataEmissao, 		");
		hql.append(" pe.id as idProdutoEdicao,				");
		hql.append(" item.reparte as reparte				");
		
		hql.append(" from ItemNotaEnvio item 		");
		hql.append(" inner join item.itemNotaEnvioPK.notaEnvio nota	");
		hql.append(" inner join item.produtoEdicao pe 				");
		hql.append(" where	");
		hql.append(" nota.destinatario.numeroCota = :numeroCota	");
		hql.append(" and pe.id in (:idsProdutoEdicao)	");
		
		hql.append(" group by nota.id, item.id, pe.id	");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);
		
		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaEnvioProdutoEdicao.class));
		
		return query.list();
		
	}
	
}
