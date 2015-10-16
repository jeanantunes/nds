package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DiretorioTransferenciaArquivoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TransferenciaArquivoRepository;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class TransferenciaArquivoRepositoryImpl extends AbstractRepositoryModel<DiretorioTransferenciaArquivo, Long> implements TransferenciaArquivoRepository {

	public TransferenciaArquivoRepositoryImpl() {
		super(DiretorioTransferenciaArquivo.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DiretorioTransferenciaArquivoDTO> carregarDiretorios(DiretorioTransferenciaArquivoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ");
		sql.append(" ID as idDiretorio, ");
		sql.append(" NOME_DIRETORIO as nomeDiretorio, ");
		sql.append(" ENDERECO_DIRETORIO as pastaDiretorio ");

		sql.append(" FROM diretorio  ");

		sql.append(" order by NOME_DIRETORIO");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idDiretorio", StandardBasicTypes.LONG);
		query.addScalar("nomeDiretorio", StandardBasicTypes.STRING);
		query.addScalar("pastaDiretorio", StandardBasicTypes.STRING);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DiretorioTransferenciaArquivoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
		
	}
	
	@Override
	public Boolean isDiretorioExistente(String pathDiretorio){
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select count(id) qtd from diretorio d where d.ENDERECO_DIRETORIO = :path ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString()).addScalar("qtd", StandardBasicTypes.LONG);
		
		query.setParameter("path", pathDiretorio);
		
		Long count = (Long) Util.nvl(query.uniqueResult(),0);
        
    	return (count > 0);
		
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {

		if(filtro == null){
			return;
		}
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}

}
