package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.AlteracaoCotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 */
@Repository
public class AlteracaoCotaRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements AlteracaoCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public AlteracaoCotaRepositoryImpl() {
		super(Cota.class);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO){
		
		StringBuilder hql = new StringBuilder();
		//boolean addedAnd = false;
		
		hql.append(" select new ").append(ConsultaAlteracaoCotaDTO.class.getCanonicalName());
		hql.append(" (cota.id, cota.numeroCota, pessoa.nome, pessoapj.razaoSocial, descontosProdutoEdicao.tipoDesconto, parametroCobranca.fatorVencimento, parametroCobranca.valorMininoCobranca, parametroDistribuicao.descricaoTipoEntrega, box.nome) ");
		hql.append(" from Cota cota ");
		hql.append(" JOIN cota.fornecedores fornecedor ");
		hql.append(" JOIN fornecedor.juridica pessoapj ");
		hql.append(" JOIN cota.descontosProdutoEdicao descontosProdutoEdicao ");
		hql.append(" JOIN descontosProdutoEdicao.fornecedor fornecedorDescontosProdutoEdicao ");
		hql.append(" JOIN cota.parametroDistribuicao parametroDistribuicao ");
		hql.append(" JOIN cota.pessoa pessoa ");
		hql.append(" JOIN cota.enderecos enderecoCota 	");
		hql.append(" JOIN enderecoCota.endereco endereco 	");
        hql.append(" JOIN cota.box box ");
        hql.append(" JOIN cota.parametroCobranca parametroCobranca ");
		hql.append(" where ");
		hql.append(" fornecedorDescontosProdutoEdicao.id = fornecedor.id ");
		
		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			hql.append(" and cota.numeroCota = :numeroCota ");

		}
		
		if (filtroAlteracaoCotaDTO.getNomeCota() != null && !filtroAlteracaoCotaDTO.getNomeCota().isEmpty()&& !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			hql.append(" and ");	
			hql.append("  (upper(pessoa.nome) like upper(:nomeCota) OR upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");

		}
		
		if (filtroAlteracaoCotaDTO.getIdBairro() != null && filtroAlteracaoCotaDTO.getIdBairro()>0) {
			hql.append(" and ");	
			hql.append(" endereco.codigoBairro = :idBairro ");

		}
		
		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null && !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty()&& !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			hql.append(" and ");	
			hql.append("  upper(endereco.cidade) like :idMunicipio ");

		}
		
		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() > 0) {
			hql.append(" and ");	
			hql.append(" fornecedor.id = :idFornecedor ");

		}
		
		if (filtroAlteracaoCotaDTO.getTipoDesconto() != null ) {
			hql.append(" and ");	
			hql.append(" descontosProdutoEdicao.tipoDesconto = :tipoDesconto ");

		}
		
		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null && !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty() && filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			hql.append(" and ");		
			hql.append(" parametroCobranca.valorMininoCobranca >= :idVrMinimo ");
		}
		
		if (filtroAlteracaoCotaDTO.getDescricaoTipoEntrega() != null ) {
			hql.append(" and parametroDistribuicao.descricaoTipoEntrega = :descricaoTipoEntrega ");

		}
		
		if (filtroAlteracaoCotaDTO.getIdVencimento() != null && filtroAlteracaoCotaDTO.getIdVencimento()>0) {
			hql.append(" and parametroCobranca.fatorVencimento = :fatorVencimento ");

		}
		
		
		
		
		
		if (filtroAlteracaoCotaDTO.getPaginacao() != null && filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null) {
			hql.append(" order by ").append(filtroAlteracaoCotaDTO.getPaginacao().getSortOrder()).append(" ").append(filtroAlteracaoCotaDTO.getPaginacao().getOrdenacao().getOrdenacao());
		}
		
		Query query = getSession().createQuery(hql.toString());

		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			query.setParameter("numeroCota", filtroAlteracaoCotaDTO.getNumeroCota());
		}
		
		if (filtroAlteracaoCotaDTO.getNomeCota() != null && !filtroAlteracaoCotaDTO.getNomeCota().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			query.setParameter("nomeCota", filtroAlteracaoCotaDTO.getNomeCota().toUpperCase() +"%" );
		}
		
		if (filtroAlteracaoCotaDTO.getIdBairro() != null && filtroAlteracaoCotaDTO.getIdBairro()>0) {
			query.setParameter("idBairro", filtroAlteracaoCotaDTO.getIdBairro());
		}
		
		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null && !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			query.setParameter("idMunicipio", filtroAlteracaoCotaDTO.getIdMunicipio().toUpperCase() +"%" );
		}
		
		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() >0) {
			query.setParameter("idFornecedor", filtroAlteracaoCotaDTO.getIdFornecedor());
		}
		
		if (filtroAlteracaoCotaDTO.getTipoDesconto() != null) {
			query.setParameter("tipoDesconto", filtroAlteracaoCotaDTO.getTipoDesconto());
		}
		
		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null && !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty() && filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			query.setParameter("idVrMinimo", filtroAlteracaoCotaDTO.getIdVrMinimo());
		}
		
		if (filtroAlteracaoCotaDTO.getDescricaoTipoEntrega() != null ) {
			query.setParameter("descricaoTipoEntrega",filtroAlteracaoCotaDTO.getDescricaoTipoEntrega());
		}
		
		if (filtroAlteracaoCotaDTO.getIdVencimento() != null && filtroAlteracaoCotaDTO.getIdVencimento()>0) {
			query.setParameter("fatorVencimento",filtroAlteracaoCotaDTO.getIdVencimento());
		}
		
		
		
		return query.list();
	}

	
}