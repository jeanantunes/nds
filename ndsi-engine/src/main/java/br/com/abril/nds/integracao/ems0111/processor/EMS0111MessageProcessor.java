package br.com.abril.nds.integracao.ems0111.processor;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0111Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

/**
 * @author Jones.Costa
 * @version 1.0
 */

@Component
public class EMS0111MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	// METODO PARA AJUSTAR A INTERFACE AO ENUM
	public TipoLancamento parseTipo(String tipo) {
		if (tipo.equalsIgnoreCase("LAN"))
			return TipoLancamento.LANCAMENTO;
		if (tipo.equalsIgnoreCase("SUP"))
			return TipoLancamento.SUPLEMENTAR;
		if (tipo.equalsIgnoreCase("REL"))
			return TipoLancamento.RELANCAMENTO;
		if (tipo.equalsIgnoreCase("PAR"))
			return TipoLancamento.PARCIAL;
		return null;
	}

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

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

		Query consulta = getSession().createQuery(cmd.toString());
		consulta.setParameter("codigoProduto", input.getCodigoProduto());
		consulta.setParameter("numeroEdicao", input.getEdicaoProduto());

		ProdutoEdicao produtoEdicao = (ProdutoEdicao) consulta.uniqueResult();
		if (null != produtoEdicao) {
			// SE EXISTIR PRODUTO/EDICAO NA TABELA
			// VERIFICAR SE EXISTE LANCAMENTO CADASTRADO PARA O PRODUTO/EDICAO
				
			produtoEdicao.setPrecoVenda(input.getPrecoPrevisto());			
			
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT lcto FROM Lancamento lcto ");
			sql.append(" WHERE ");
			sql.append("		lcto.produtoEdicao = :produtoEdicao");
			sql.append(" AND   ");
			sql.append(" 		lcto.dataLancamentoPrevista = :dataLancamento   ");

			Query query = getSession().createQuery(sql.toString());
			query.setParameter("produtoEdicao", produtoEdicao);
			query.setParameter("dataLancamento", input.getDataLancamento());

			Lancamento lancamento = (Lancamento) query.uniqueResult();
			if (null != lancamento) {

				// VERIFICAR SE OS CAMPOS ESTAO DESATUALIZADOS
				// CASO NECESSARIO, ATUALIZAR OS CAMPOS

				if (!lancamento.getDataLancamentoPrevista().equals(
						input.getDataLancamento())) {
					lancamento.setDataLancamentoPrevista(input
							.getDataLancamento());
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao da data de lancamento: "
									+ input.getDataLancamento());
				}

				if (!lancamento.getTipoLancamento().equals(
						parseTipo(input.getTipoLancamento())))
					;
				{

					lancamento.setTipoLancamento(parseTipo(input
							.getTipoLancamento()));
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do tipo de lancamento: "
									+ input.getTipoLancamento());

				}

				if (lancamento.getReparte().longValue() != input.getRepartePrevisto())
					;
				{
					lancamento.setReparte( new BigInteger( input.getRepartePrevisto().toString() ));
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Reparte Previsto: "
									+ input.getRepartePrevisto());

				}

				if (lancamento.getRepartePromocional().longValue() != input
						.getRepartePromocional())
					;
				{
					lancamento.setRepartePromocional( new BigInteger( input
							.getRepartePromocional().toString()));

					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Reparte Promocional: "
									+ input.getRepartePromocional());

				}

			} else {
				// NAO EXISTE LANCAMENTO PARA O PRODUTO/EDICAO INFORMADO
				// INSERIR LANCAMENTO

				lancamento = new Lancamento();
				Calendar data = Calendar.getInstance();

				data.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());

				lancamento.setId(null);
				lancamento.setProdutoEdicao(produtoEdicao);
				lancamento.setDataLancamentoPrevista(input.getDataLancamento());
				lancamento.setTipoLancamento(parseTipo(input.getTipoLancamento()));
				lancamento.setReparte(new BigInteger( input.getRepartePrevisto().toString()));
				lancamento.setStatus(StatusLancamento.PLANEJADO);// confirmado
				lancamento.setRepartePromocional(new BigInteger( input.getRepartePromocional().toString()));// confirmado
				lancamento.setDataCriacao(new Date());// confirmado
				lancamento.setDataLancamentoDistribuidor(input.getDataLancamento());// confirmado
				lancamento.setDataRecolhimentoDistribuidor(data.getTime());// confirmado
				lancamento.setDataRecolhimentoPrevista(data.getTime());// confirmado
				lancamento.setDataStatus(new Date());// confirmado
				lancamento.setExpedicao(null);// default
				lancamento.setHistoricos(null);// default
				lancamento.setRecebimentos(null);// default
				lancamento.setNumeroReprogramacoes(null);// confirmado
				lancamento.setSequenciaMatriz(null);// confirmado				

				// EFETIVAR INSERCAO NA BASE
				getSession().persist(lancamento);

			}

		} else {
			// NAO ENCONTROU Produto/Edicao, DEVE LOGAR
			// NAO E POSSIVEL REALIZAR INSERT/UPDATE
			ndsiLoggerFactory
					.getLogger()
					.logError(
							message,
							EventoExecucaoEnum.RELACIONAMENTO,
							"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
									+ input.getCodigoProduto()
									+ " e Edicao: "
									+ input.getEdicaoProduto()
									+ " na tabela produto_edicao");

		}

	}
	
	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
