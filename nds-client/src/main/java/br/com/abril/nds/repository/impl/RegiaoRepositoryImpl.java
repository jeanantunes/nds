package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.repository.RegiaoRepository;

@Repository
public class RegiaoRepositoryImpl extends AbstractRepositoryModel<Regiao, Long> implements RegiaoRepository {
	
	public RegiaoRepositoryImpl( ) {
		super(Regiao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegiaoCotaDTO> carregarCotasRegiao(Long id) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append(" registroCotaRegiao.id as registroCotaRegiaoId, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,");
		hql.append(" tipoPontoPDV.descricao as tipoPDV, ");
		hql.append(" cota.situacaoCadastro as tipoStatus, ");
		hql.append(" endereco.bairro as bairro, ");
		hql.append(" endereco.cidade as cidade, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" registroCotaRegiao.dataAlteracao as data, ");
		hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamento "); // FATURAMENTO

		hql.append(" FROM RegistroCotaRegiao AS registroCotaRegiao, EstoqueProdutoCota as estoqueProdutoCota ");
		hql.append(" JOIN registroCotaRegiao.cota as cota ");
		hql.append(" JOIN cota.enderecos as enderecoCota ");
		hql.append(" JOIN enderecoCota.endereco as endereco ");
		hql.append(" JOIN cota.pessoa as pessoa ");
		hql.append(" JOIN cota.pdvs as pdv ");
		hql.append(" JOIN pdv.segmentacao as segmentacao ");
		hql.append(" JOIN segmentacao.tipoPontoPDV as tipoPontoPDV ");
		hql.append(" JOIN registroCotaRegiao.usuario  as usuario ");
		hql.append(" JOIN registroCotaRegiao.regiao  as regiao ");
		hql.append(" JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");

		hql.append(" WHERE registroCotaRegiao.cota.id = estoqueProdutoCota.cota.id AND ");
		hql.append(" registroCotaRegiao.regiao.id = :ID_REGIAO ");
		hql.append(" group by cota.id ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("ID_REGIAO", id);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RegiaoCotaDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegiaoDTO> buscarRegiao() {
		
		
		StringBuilder hql = new StringBuilder();
		
		
		hql.append("SELECT");
		hql.append(" regiao.id as idRegiao,");
		hql.append(" regiao.nomeRegiao as nomeRegiao, ");
		hql.append(" regiao.regiaoIsFixa as isFixa, ");
		hql.append(" regiao.dataRegiao as dataAlteracao, ");
		hql.append(" usuario.nome as nomeUsuario ");
		
		hql.append(" FROM Regiao as regiao ");
		hql.append(" JOIN regiao.idUsuario as usuario");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RegiaoDTO.class));
		
		return query.list();
	}
	
	// Alterar para classe RegiaoCotaRepository
	@Override
	public List<RegiaoCotaDTO> buscarPorCEP(FiltroCotasRegiaoDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT");
		hql.append(" registroCotaRegiao.id as registroCotaRegiaoId, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,");
		hql.append(" cota.situacaoCadastro as tipoStatus ");
		
		hql.append(" FROM RegistroCotaRegiao AS registroCotaRegiao ");
		
		hql.append(" JOIN registroCotaRegiao.cota as cota ");
		hql.append(" JOIN cota.pdvs as pdv ");
		hql.append(" JOIN pdv.enderecos as enderecosPdv "); 
		hql.append(" JOIN enderecosPdv.endereco as enderecoPdv ");
		hql.append(" JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE enderecoPdv.cep between :CEP_INICIAL and :CEP_FINAL ");
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("CEP_INICIAL", filtro.getCepInicial()); 
		query.setParameter("CEP_FINAL", filtro.getCepFinal());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoCotaDTO.class));
				
		return query.list();
	}
}
