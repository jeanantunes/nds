package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.PdvRepository;

@Repository
public class PdvRepositoryImpl extends AbstractRepository<PDV, Long> implements PdvRepository {

	public PdvRepositoryImpl() {
		super(PDV.class);
	}
	
	public List<PDV> obterPDVsPorCota(FiltroPdvDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT pdv FROM PDV pdv ")
		.append(" JOIN pdv.cota cota ")
		.append(" JOIN pdv.enderecos endereco ")
		.append(" JOIN pdv.telefones telefone ")
		.append(" WHERE cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", filtro.getIdCota());
		
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
	
	private String getOrdenacaoPDV(FiltroPdvDTO filtro){
		
		if(filtro == null || filtro.getColunaOrdenacao() == null){
			return "";
		}
		
		StringBuilder hql = new StringBuilder();
		
		switch (filtro.getColunaOrdenacao()) {
			case CONTATO:
				hql.append(" order by box.codigo ");
				break;
			case FATURAMENTO:
				hql.append(" order by cota.numeroCota ");
				break;	
			case NOME_PDV:
				hql.append(" order by  estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
				break;
			case STATUS:
				hql.append(" order by  estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
				break;
			case TIPO_PONTO:
				hql.append(" order by  estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida ");
				break;
			default:
				hql.append(" order by  box.codigo ");
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
}
