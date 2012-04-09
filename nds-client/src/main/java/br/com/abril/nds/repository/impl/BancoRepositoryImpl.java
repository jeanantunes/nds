package br.com.abril.nds.repository.impl;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.repository.BancoRepository;

@Repository
public class BancoRepositoryImpl extends AbstractRepository<Banco,Long> implements BancoRepository  {

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
				case AGENCIA_DIGITO:
					hql.append(" order by b.agencia ");
					break;
				case CONTA_CORRENTE_DIGITO:
					hql.append(" order by b.conta ");
					break;
				case CEDENTE:
					hql.append(" order by b.codigoCedente ");
					break;
				case MOEDA:
					hql.append(" order by b.moeda ");
					break;
				case CARTEIRA:
					hql.append(" order by b.carteira.tipoRegistroCobranca ");
					break;
				case ATIVO:
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
	
}
