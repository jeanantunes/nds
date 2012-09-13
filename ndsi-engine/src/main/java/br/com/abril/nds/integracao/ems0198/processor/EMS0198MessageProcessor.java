package br.com.abril.nds.integracao.ems0198.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Detalhe;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Header;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Trailer;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.repository.impl.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0198MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	@Override
	public void processMessage(Message message) {
		
		List<PDV> pdvs = findListPDV(message);

		Date data = getDataLancDistrib(message);
		
		int numeroCota = pdvs.get(0).getCota().getNumeroCota();

		int qtdeRegistros = 0;
		
		PrintWriter print =  geraArquivo(message, data, pdvs.get(0).getNome(), numeroCota);
							
		for (PDV pdv: pdvs) {
			
			if (numeroCota == pdv.getCota().getNumeroCota()) {
				
				qtdeRegistros += criaDetalhes(print, numeroCota, pdv.getCota().getMovimentoEstoqueCotas());
				
			} else {
				
				criaTrailer(print, numeroCota, qtdeRegistros);
				
				print.flush();
				print.close();
				
				numeroCota = pdv.getCota().getNumeroCota();
				
				print = geraArquivo(message, data, pdvs.get(0).getNome(), numeroCota);
				
				qtdeRegistros = 0;
				
			}
			
		}

		criaTrailer(print, numeroCota, qtdeRegistros);
		
		print.flush();
		print.close();
			
	}
	
	private PrintWriter geraArquivo(Message message, Date data, String nome, int numeroCota) {
		
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(
					MessageHeaderProperties.OUTBOUND_FOLDER.getValue()) + "/" + numeroCota + sdf.format(data) + ".enc"));
			
			criaHeader(print, numeroCota, nome, data);
			
			return print;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			throw new RuntimeException("Falha na geracao do Arquivo!");					

		}
						
	}
	
	private Date getDataLancDistrib(Message message) {
		
		Date data = (Date) message.getHeader().get("DATA_LCTO_DISTRIB");
		
		if (data == null) {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Data nao informada!");

			throw new RuntimeException("Execute a interface EMS0129 passando a dataLancamentoDistribuidor !");
		}
		
		return data;
	}
	
	private List<PDV> findListPDV(Message message) {
		
		Date data = getDataLancDistrib(message);
			
		StringBuilder sql = new StringBuilder();

		sql.append(" select pdv from PDV pdv ");
		sql.append(" join fetch pdv.cota co ");
		sql.append(" join fetch co.pessoa p ");
		sql.append(" left join fetch co.movimentoEstoqueCotas mov ");
		sql.append(" left join fetch mov.produtoEdicao pe ");
		sql.append(" left join fetch pe.chamadaEncalhes ce");
		sql.append(" left join fetch pe.produto pd ");
		sql.append(" left join mov.tipoMovimento tme ");
		sql.append(" left join pe.lancamentos lan ");
		
		sql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
		sql.append(" and lan.dataLancamentoDistribuidor = :dataLancDistrib ");
		sql.append(" and tme.grupoMovimentoEstoque = :tipoMovimento ");
		sql.append(" order by co.numeroCota");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("dataLancDistrib", data);
		query.setParameter("tipoMovimento", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);

		@SuppressWarnings("unchecked")
		List<PDV> pdvs = (List<PDV>) query.list();

		if (pdvs.isEmpty()) {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Nenhum registro encontrado!");

			throw new RuntimeException("Nenhum registro encontrado!");

		} else {

			return pdvs;
			
		}
	}
	
	private void criaHeader(PrintWriter print, Integer numeroCota, String nome, Date data) {

		EMS0198Header outheader = new EMS0198Header();

		outheader.setCodigoCota(numeroCota.toString());
		outheader.setNomeCota(nome);
		outheader.setData(sdf.format(data));

		print.println(fixedFormatManager.export(outheader));
		
	}
	
	private void criaTrailer(PrintWriter print, Integer numeroCota,	Integer quantidadeRegistros) {

		EMS0198Trailer outtrailer = new EMS0198Trailer();

		outtrailer.setCodigoCota(numeroCota.toString());
		outtrailer.setQuantidadeRegistros(quantidadeRegistros.toString());
		
		print.println(fixedFormatManager.export(outtrailer));

	}

	private Integer criaDetalhes(PrintWriter print, Integer numeroCota, Set<MovimentoEstoqueCota> movimentoEstoqueCotas) {
		
		EMS0198Detalhe outdetalhe = new EMS0198Detalhe();
		
		Integer qtdeRegistros = 0;
		
		for (MovimentoEstoqueCota moviEstCota : movimentoEstoqueCotas) {

			outdetalhe.setCodigoCota(numeroCota.toString());
			outdetalhe.setCodigoProduto(moviEstCota.getProdutoEdicao().getProduto().getCodigo());
			outdetalhe.setEdicao(moviEstCota.getProdutoEdicao().getNumeroEdicao().toString());
			outdetalhe.setNomePublicacao(moviEstCota.getProdutoEdicao().getProduto().getNome());
			outdetalhe.setCodigoDeBarras(moviEstCota.getProdutoEdicao().getCodigoDeBarras());
			outdetalhe.setPrecoCusto(moviEstCota.getProdutoEdicao().getPrecoCusto().toString());
			outdetalhe.setPrecoVenda(moviEstCota.getProdutoEdicao().getPrecoVenda().toString());
			outdetalhe.setDesconto(moviEstCota.getProdutoEdicao().getDescontoLogistica().toString());
			outdetalhe.setQuantidade(moviEstCota.getQtde().toString());
						
			if (!moviEstCota.getProdutoEdicao().getChamadaEncalhes().isEmpty()) {

				for (ChamadaEncalhe chamadaEncalhe : moviEstCota.getProdutoEdicao().getChamadaEncalhes()) {

					Calendar dataAtual = Calendar.getInstance();
					Calendar dataRecolhimento = Calendar.getInstance();
						
					dataRecolhimento.setTime(chamadaEncalhe.getDataRecolhimento());
					
					Long diferenca = dataRecolhimento.getTimeInMillis() - dataAtual.getTimeInMillis();
					
					// Quantidade de milissegundos em um dia
					int tempoDia = 1000 * 60 * 60 * 24;
				
					Long diasDiferenca = diferenca / tempoDia + 1;

					outdetalhe.setDataEncalhe(chamadaEncalhe.getDataRecolhimento().toString());
					outdetalhe.setDiaChamada("" + diasDiferenca);
					
					print.println(fixedFormatManager.export(outdetalhe));
					qtdeRegistros++;
					
				}

			} else {

				print.println(fixedFormatManager.export(outdetalhe));
				qtdeRegistros++;

			}
			
		} 
		
		return qtdeRegistros;
		
	}

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
		
	}
}
