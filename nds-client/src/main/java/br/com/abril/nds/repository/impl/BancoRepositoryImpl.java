package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.repository.BancoRepository;

@Repository
public class BancoRepositoryImpl extends AbstractRepositoryModel<Banco,Long> implements BancoRepository  {

	/**
	 * Construtor padrão
	 */
	public BancoRepositoryImpl() {
		super(Banco.class);
	}

	@Override
	public long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro) {
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(b) from Banco b ");		
		hql.append(" where b.ativo = :ativo ");
		
		if ((filtro.getNome()!=null)&&(!"".equals(filtro.getNome()))){
		    hql.append(" and b.nome = :nome ");
		}
		if ((filtro.getNumero()!=null)&&(!"".equals(filtro.getNumero()))){
		    hql.append(" and b.numeroBanco = :numero ");
		}
		if ((filtro.getCedente()!=null)&&(!"".equals(filtro.getCedente()))){	  
			hql.append(" and b.codigoCedente = :cedente");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("ativo", filtro.getAtivo());
		
		if ((filtro.getNome()!=null)&&(!"".equals(filtro.getNome().trim()))){
			query.setParameter("nome", filtro.getNome());
		}
		if ((filtro.getNumero()!=null)&&(!"".equals(filtro.getNumero().trim()))){
			query.setParameter("numero", filtro.getNumero());
		}
		if ((filtro.getCedente()!=null)&&(!"".equals(filtro.getCedente().trim()))){	  
			query.setParameter("cedente", filtro.getCedente());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banco> obterBancos(FiltroConsultaBancosDTO filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from Banco b ");		
		hql.append(" where b.ativo = :ativo ");
		
		if ((filtro.getNome()!=null)&&(!"".equals(filtro.getNome()))){
		    hql.append(" and b.nome = :nome ");
		}
		if ((filtro.getNumero()!=null)&&(!"".equals(filtro.getNumero()))){
		    hql.append(" and b.numeroBanco = :numero ");
		}
		if ((filtro.getCedente()!=null)&&(!"".equals(filtro.getCedente()))){	  
			hql.append(" and b.codigoCedente = :cedente");
		}
		
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case CODIGO_BANCO:
					hql.append(" order by b.id ");
					break;
				case NUMERO_BANCO:
					hql.append(" order by b.numeroBanco ");
					break;
				case NOME_BANCO:
					hql.append(" order by b.nome ");
					break;
				case AGENCIA_BANCO:
					hql.append(" order by b.agencia ");
					break;
				case CONTA_CORRENTE_BANCO:
					hql.append(" order by b.conta ");
					break;
				case CEDENTE_BANCO:
					hql.append(" order by b.codigoCedente ");
					break;
				case APELIDO_BANCO:
					hql.append(" order by b.apelido ");
					break;
				case CARTEIRA_BANCO:
					hql.append(" order by b.carteira ");
					break;
				case ATIVO_BANCO:
					hql.append(" order by b.ativo ");
					break;
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
        Query query = super.getSession().createQuery(hql.toString());
		
        query.setParameter("ativo", filtro.getAtivo());
        
        if ((filtro.getNome()!=null)&&(!"".equals(filtro.getNome().trim()))){
			query.setParameter("nome", filtro.getNome());
		}
		if ((filtro.getNumero()!=null)&&(!"".equals(filtro.getNumero().trim()))){
			query.setParameter("numero", filtro.getNumero());
		}
		if ((filtro.getCedente()!=null)&&(!"".equals(filtro.getCedente().trim()))){	  
			query.setParameter("cedente", filtro.getCedente());
		}
		
        if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}
	
	@Override
	public Banco obterBancoPorNumero(String numero) {
		Criteria criteria = super.getSession().createCriteria(Banco.class);
		criteria.add(Restrictions.eq("numeroBanco", numero));
		criteria.setMaxResults(1);
		return (Banco) criteria.uniqueResult();
	}
	
	@Override
	public Banco obterBanco(String numeroBanco, Long numeroAgencia, Long numeroConta) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select banco ");
		hql.append(" from Banco banco ");		
		hql.append(" where banco.numeroBanco = :numeroBanco ");
		hql.append(" and banco.agencia = :numeroAgencia ");
		hql.append(" and concat(conta, dvConta) = :numeroConta ");
		
        Query query = super.getSession().createQuery(hql.toString());
        
        query.setParameter("numeroBanco", numeroBanco);
        query.setParameter("numeroAgencia", numeroAgencia);
        query.setParameter("numeroConta", numeroConta.toString());
		
    	return (Banco) query.uniqueResult();
	}

	@Override
	public Banco obterbancoPorNome(String nome) {
		Criteria criteria = super.getSession().createCriteria(Banco.class);
		criteria.add(Restrictions.eq("nome", nome));
		criteria.setMaxResults(1);
		return (Banco) criteria.uniqueResult();
	}

	@Override
	public void desativarBanco(long idBanco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" update Banco b ");		
		hql.append(" set b.ativo = :ativo ");
		hql.append(" where b.id = :idBanco ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("ativo", false);
        query.setParameter("idBanco", idBanco);
		query.executeUpdate();
	}

	@Override
	public boolean verificarPedencias(long idBanco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from Cobranca c ");		
		hql.append(" where c.banco.id = :idBanco ");
		hql.append(" and c.statusCobranca = :status ");
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("idBanco", idBanco);
        query.setParameter("status", StatusCobranca.NAO_PAGO);
		return (query.list().size() > 0);
	}

	@Override
	public Banco obterbancoPorApelido(String apelido) {
		Criteria criteria = getSession().createCriteria(Banco.class);
		criteria.add(Restrictions.eq("apelido", apelido));
		return (Banco) criteria.uniqueResult();
	}
	
}
