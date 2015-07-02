package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.distribuicao.ClassificacaoNaoRecebida;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ClassificacaoNaoRecebidaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ClassificacaoNaoRecebidarRepositoryImpl extends AbstractRepositoryModel<ClassificacaoNaoRecebida, Long> implements ClassificacaoNaoRecebidaRepository {

	public ClassificacaoNaoRecebidarRepositoryImpl() {
		super(ClassificacaoNaoRecebida.class);
	}

	@Override
	public List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" cota.numeroCota as numeroCota, "); // NUMERO DA COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomePessoa "); // NOME DA COTA
		
		hql.append(" FROM Cota as cota ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		hql.append(" cota.situacaoCadastro in ('ATIVO','SUSPENSO') and ");
		
		if (filtro.getCotaDto() != null) {
			if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
				hql.append(" cota.numeroCota = :numeroCota and ");
				parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
			}else if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
				if (filtro.isAutoComplete()) {
					hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') like :nomePessoa and ");
					parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa() + "%");
				}else {
					hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa and ");
					parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
				}
			}
		}
		
		hql.append(" cota.id not in ");
		hql.append(" ( SELECT cota.id  ");
		hql.append(" FROM ClassificacaoNaoRecebida classificacaoNaoRecebida ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.cota as cota ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.tipoClassificacaoProduto as tipoClassificacaoProduto ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		hql.append(" WHERE ");
		
        if (filtro.getIdTipoClassificacaoProduto() != null && filtro.getIdTipoClassificacaoProduto() != 0) {
			hql.append(" tipoClassificacaoProduto.id = :tipoClassificacaoProduto )");
			parameters.put("tipoClassificacaoProduto", filtro.getIdTipoClassificacaoProduto());
		}
		
		hql.append(" order by numeroCota, nomePessoa");
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueRecebeClassificacaoDTO.class));
		
		return query.list();
	}
	
	@Override
	public List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" classificacaoNaoRecebida.id as idClassificacaoNaoRecebida,"); // ID ClassificacaoNaoRecebida
		hql.append(" classificacaoNaoRecebida.usuario.nome as nomeUsuario,"); //NOME USUARIO
		hql.append(" classificacaoNaoRecebida.dataAlteracao as dataAlteracao,"); //DATA DE ALTERACAO
		hql.append(" cota.numeroCota as numeroCota, "); // NUMERO DA COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomePessoa"); // NOME DA COTA
		
		hql.append(" FROM ClassificacaoNaoRecebida as classificacaoNaoRecebida");
		hql.append(" INNER JOIN classificacaoNaoRecebida.tipoClassificacaoProduto as tipoClassificacaoProduto");
		hql.append(" INNER JOIN classificacaoNaoRecebida.cota as cota");
		hql.append(" INNER JOIN cota.pessoa as pessoa");
		
        // O filtro sempre terá OU nomeCota OU codigoCota
		hql.append(" WHERE ");
		
        if (filtro.getIdTipoClassificacaoProduto() != null && filtro.getIdTipoClassificacaoProduto() != 0L) {
			hql.append(" tipoClassificacaoProduto.id = :tipoClassificacaoProduto ");
			parameters.put("tipoClassificacaoProduto", filtro.getIdTipoClassificacaoProduto());
		}
		
        if((filtro.getPaginacao() != null) && (filtro.getPaginacao().getSortColumn() != null)){
          	
         	if(filtro.getPaginacao().getSortColumn().equalsIgnoreCase("ACAO")){
        		hql.append(" order by numeroCota, nomePessoa");
        	}else{
        		
        		if(filtro.getPaginacao().getSortColumn().equalsIgnoreCase("dataAlteracaoFormatada")){
    	  			filtro.getPaginacao().setSortColumn("dataAlteracao");
    	  		}
        		
        		hql.append(" ORDER BY ");
        		hql.append(" " + filtro.getPaginacao().getSortColumn());
        	}
        	hql.append(" " + filtro.getPaginacao().getSortOrder());
        }else{
        	hql.append(" order by numeroCota, nomePessoa");
        }
        
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoRecebeClassificacaoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClassificacaoNaoRecebidaDTO> obterClassificacoesNaoRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" classificacaoNaoRecebida.id as idClassificacaoNaoRecebida, "); 
		hql.append(" tipoClassificacaoProduto.descricao as nomeClassificacao, "); 
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" classificacaoNaoRecebida.dataAlteracao as dataAlteracao ");
		
		hql.append(" FROM ClassificacaoNaoRecebida as classificacaoNaoRecebida ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.usuario as usuario ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.tipoClassificacaoProduto as tipoClassificacaoProduto ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.cota as cota ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		
        // O filtro sempre terá OU nomeCota OU codigoCota
		hql.append(" WHERE ");
		
		if (filtro.getCotaDto() != null) {
			if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
				hql.append(" cota.numeroCota = :numeroCota ");
				parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
			}else if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
				hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa ");
				parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
			}
		}
		
	  if((filtro.getPaginacao() != null) && (filtro.getPaginacao().getSortColumn() != null)){
		  
		  if(filtro.getPaginacao().getSortColumn().equalsIgnoreCase("ACAO")){
        		hql.append(" order by tipoClassificacaoProduto.descricao");
		  }else{

			  if(filtro.getPaginacao().getSortColumn().equalsIgnoreCase("dataAlteracaoFormatada")){
				  filtro.getPaginacao().setSortColumn("dataAlteracao");
			  }
        		
        		hql.append(" ORDER BY ");
        		hql.append(" " + filtro.getPaginacao().getSortColumn());
        }
        	hql.append(" " + filtro.getPaginacao().getSortOrder());
	  } else {
        	
		  	hql.append(" order by nomeClassificacao");
        	
	  }
	  
	  Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ClassificacaoNaoRecebidaDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	@Override
	public List<TipoClassificacaoProduto> obterClassificacoesRecebidasPelaCota(FiltroClassificacaoNaoRecebidaDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" tipoClassificacaoProduto.id as id, "); 
		hql.append(" tipoClassificacaoProduto.descricao as descricao "); 
		
		hql.append(" FROM TipoClassificacaoProduto as tipoClassificacaoProduto ");
		
		hql.append(" WHERE ");
		
		hql.append(" tipoClassificacaoProduto.id not in ");
		hql.append(" ( SELECT tipoClassificacaoProduto.id  ");
		hql.append(" FROM ClassificacaoNaoRecebida classificacaoNaoRecebida ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.cota as cota ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.tipoClassificacaoProduto as tipoClassificacaoProduto ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		hql.append(" WHERE ");
		
		if (filtro.getCotaDto() != null) {
			if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
				hql.append(" cota.numeroCota = :numeroCota ) ");
				parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
			}else if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
				hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa ) ");
				parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
			}
		}
		
		hql.append(" order by tipoClassificacaoProduto.descricao ");
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(TipoClassificacaoProduto.class));
		
		return query.list();
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao == null) 
			return;
		
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
