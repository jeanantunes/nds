package br.com.abril.nds.integracao.ems0113.processor;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.integracao.model.canonic.EMS0111Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0113MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Override
	public void processMessage(Message message) {

		EMS0111Input input = (EMS0111Input) message.getBody();

		// Validar Produto/Edicao
		StringBuilder cmd = new StringBuilder();
		cmd.append("SELECT prodEdicao FROM ProdutoEdicao prodEdicao ");
		cmd.append("			JOIN FETCH prodEdicao.produto prodCod");
		cmd.append(" WHERE ");
		cmd.append("	prodCod.codigo = :codigoProduto ");
		cmd.append(" AND	prodEdicao.numeroEdicao = :numeroEdicao ");

		Query consulta = entityManager.createQuery(cmd.toString());
		consulta.setParameter("codigoProduto", input.getCodigoProduto());
		consulta.setParameter("numeroEdicao", input.getEdicaoProduto());

		try {
			ProdutoEdicao produtoEdicao = (ProdutoEdicao) consulta
					.getSingleResult();
			
			
			//SE EXISTIR PRODUTO/EDICAO NA TABELA
			//VERIFICAR SE EXISTE LANCAMENTO CADASTRADO PARA O PRODUTO/EDICAO
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT lcto FROM Lancamento lcto ");
			sql.append(" WHERE ");
			sql.append("		lcto.produtoEdicao = :produtoeEicaoId");
			
			Query query = entityManager.createQuery(sql.toString());
			query.setParameter("produtoEdicaoId", produtoEdicao.getId());
			

			try {
				Lancamento lancamento = (Lancamento) query.getSingleResult();
				
				//VERIFICAR SE OS CAMPOS ESTAO DESATUALIZADOS
				//CASO NECESSARIO, ATUALIZAR OS CAMPOS
				
				if(!lancamento.getDataLancamentoPrevista().equals(input.getDataLancamento())){
					lancamento.setDataLancamentoPrevista(input.getDataLancamento());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao da data de lancamento: "+input.getDataLancamento());
				}
				
				
				if(!lancamento.getTipoLancamento().equals(input.getTipoLancamento()));{
					//CORRIGIR ENUM
					/*lancamento.setTipoLancamento(input.getTipoLancamento());*/
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do tipo de lancamento: "+input.getTipoLancamento());

				}
					
				
				if(lancamento.getReparte()!=input.getRepartePrevisto());{
					lancamento.setReparte(input.getRepartePrevisto());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Reparte Previsto: "+input.getRepartePrevisto());

				}
				//FALTA INSERIR O CAMPO NA TABELA LANCAMENTO
				/*if(lancamento.getRepartePromocional()!=input.getRepartePromocional());{
					lancamento.setRepartePromocional(input.getRepartePromocional());
					ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do Reparte Promocional: "+input.getRepartePromocional()));

				}*/
				


			} catch (NoResultException e) {
				//NAO EXISTE LANCAMENTO PARA O PRODUTO/EDICAO  INFORMADO
				//INSERIR LANCAMENTO
				
				Lancamento lancamento = new Lancamento();
				Calendar data = Calendar.getInstance();
				
				data.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
								
				lancamento.setId(null);
				lancamento.setProdutoEdicao(produtoEdicao);
				lancamento.setDataLancamentoPrevista(input.getDataLancamento());
				//lancamento.setTipoLancamento((TipoLancamento) input.getTipoLancamento());
				lancamento.setReparte(input.getRepartePrevisto());
				lancamento.setStatus(StatusLancamento.PLANEJADO);//confirmado
				/*lancamento.setRepartePromocional(input.getRepartePromocional());*///falta o campo na tabela lancamento
				lancamento.setDataCriacao(new Date());//confirmado
				lancamento.setDataLancamentoDistribuidor(input.getDataLancamento());//confirmado
				
				lancamento.setDataRecolhimentoDistribuidor(data.getTime());//confirmado
				lancamento.setDataRecolhimentoPrevista(data.getTime());//confirmado
				
				lancamento.setDataStatus(new Date());//confirmado
				
				lancamento.setExpedicao(null);//default
				lancamento.setHistoricos(null);//default
				lancamento.setRecebimentos(null);//default
				
				lancamento.setNumeroReprogramacoes(null);//confirmado
				lancamento.setSequenciaMatriz(null);//confirmado
				
				//EFETIVAR INSERCAO NA BASE
				entityManager.persist(lancamento);
							
			}			
			

		} catch (NoResultException e) {
			// NAO ENCONTROU Produto/Edicao, DEVE LOGAR
			// NÃO É POSSIVEL REALIZAR INSERT/UPDATE 
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossível realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ input.getCodigoProduto() + " e Edicao: "
							+ input.getEdicaoProduto() + " na tabela produto_edicao");

			e.printStackTrace();

		}

		

	}

}
