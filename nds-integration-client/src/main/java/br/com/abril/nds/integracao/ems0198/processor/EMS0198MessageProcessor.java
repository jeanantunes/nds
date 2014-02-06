package br.com.abril.nds.integracao.ems0198.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Detalhe;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Header;
import br.com.abril.nds.integracao.ems0198.outbound.EMS0198Trailer;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0198MessageProcessor extends AbstractRepository implements MessageProcessor {
    
    private static final Logger LOGGER = Logger.getLogger(EMS0198MessageProcessor.class);

	private static final String ENCALHE_FOLDER = "encalhe";

	private static final String ENCALHE_EXT = ".enc";

	@Autowired
	private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	private Date dataLctoDistrib;

	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	
	private int quantidadeArquivosGerados = 0;
	
	@Override
	public void processMessage(Message message) {
		
		this.quantidadeArquivosGerados = 0;
		
		List<ChamadaEncalheCota> cecs = findListPDV(message);
		
		Integer numerCota =  0;
		
		int qtdeRegistros = 0;
		
		PrintWriter print =  null;
		
		for (ChamadaEncalheCota cec: cecs) {
			
			if (numerCota == cec.getCota().getNumeroCota()) {
		
				qtdeRegistros++;
			
			} else {
			
				if (!numerCota.equals(0)) {
		
					criaTrailer(print, numerCota, qtdeRegistros);
					
					print.flush();
					print.close();					
				}
				
				numerCota = cec.getCota().getNumeroCota();
				
				qtdeRegistros = 0;
				
				print = geraArquivo(message, this.dataLctoDistrib, cec.getCota().getPessoa().getNome(), numerCota);
			}			
			
			criaDetalhes(print, cec);
		}
		if (!numerCota.equals(0)) {
		
			criaTrailer(print, numerCota, qtdeRegistros);
			
			print.flush();
			print.close();					
		}
		
	}
	
	private List<ChamadaEncalheCota> findListPDV(Message message) {

		StringBuilder sql = new StringBuilder();

		sql.append(" select cec from ");
		sql.append(" ChamadaEncalhe ce ");
		sql.append(" join ce.chamadaEncalheCotas cec ");
		sql.append(" join cec.cota c ");
		sql.append(" join c.pdvs pdv ");


		sql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
		sql.append(" and ce.dataRecolhimento = :dataRecolhimento ");
		sql.append(" order by c.numeroCota");

		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameter("dataRecolhimento", this.dataLctoDistrib);
		
		@SuppressWarnings("unchecked")
		List<ChamadaEncalheCota> chamadaEncalhe = (List<ChamadaEncalheCota>) query.list();

		if (chamadaEncalhe.isEmpty()) {

			message.getHeader().put(
					MessageHeaderProperties.FILE_NAME.getValue(), "");
			message.getHeader().put(
					MessageHeaderProperties.LINE_NUMBER.getValue(), 0);

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.GERACAO_DE_ARQUIVO,
					"Nenhum registro encontrado!");

			throw new RuntimeException("Nenhum registro encontrado!");
		} else {

			return chamadaEncalhe;
		}
	}
	
	
	private PrintWriter geraArquivo(Message message, Date data, String nome, int numeroCota) {
		
		try {
			
			String nomeArquivo = String.format("%1$05d%2$s", numeroCota, sdf.format(dataLctoDistrib));

			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(
					TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name()) + File.separator + ENCALHE_FOLDER + File.separator + nomeArquivo + ENCALHE_EXT));
			
			criaHeader(print, numeroCota, nome, data);
			
			return print;
			
		} catch (IOException e) {
			
			LOGGER.error(e.getMessage(), e);
			
			throw new RuntimeException("Falha na geracao do Arquivo!");					

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

		                /*
         * A quantidade de arquivos gerados é incrementado aqui pois
         * considera-se que o arquivo foi gerado corretamente.
         */
		this.quantidadeArquivosGerados++;
	}

	private void criaDetalhes(PrintWriter print, ChamadaEncalheCota cec) {
		
		EMS0198Detalhe outdetalhe = new EMS0198Detalhe();

				
		outdetalhe.setCodigoCota(cec.getCota().getNumeroCota().toString());
		outdetalhe.setCodigoProduto(cec.getChamadaEncalhe().getProdutoEdicao().getProduto().getCodigo());
		outdetalhe.setEdicao(cec.getChamadaEncalhe().getProdutoEdicao().getNumeroEdicao().toString());
		outdetalhe.setNomePublicacao(cec.getChamadaEncalhe().getProdutoEdicao().getProduto().getNome());
		outdetalhe.setCodigoDeBarras(cec.getChamadaEncalhe().getProdutoEdicao().getCodigoDeBarras());
		outdetalhe.setPrecoCusto((cec.getChamadaEncalhe().getProdutoEdicao().getPrecoCusto() != null ? cec.getChamadaEncalhe().getProdutoEdicao().getPrecoCusto().toString():""));
		outdetalhe.setPrecoVenda((cec.getChamadaEncalhe().getProdutoEdicao().getPrecoVenda() != null ? cec.getChamadaEncalhe().getProdutoEdicao().getPrecoVenda().toString():""));			
		outdetalhe.setDesconto(descontoService.obterDescontoPorCotaProdutoEdicao(null, cec.getCota().getId(), cec.getChamadaEncalhe().getProdutoEdicao()).toString());												
		outdetalhe.setQuantidade(cec.getQtdePrevista().toString());
					
		outdetalhe.setDataEncalhe(sdf.format(cec.getChamadaEncalhe().getDataRecolhimento()));
		outdetalhe.setDiaChamada("1");
		
		print.println(fixedFormatManager.export(outdetalhe));
					
	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		

	}

	@Override
	public void posProcess(Object tempVar) {
		

	}

	public void setDataLctoDistrib(Date data) {
		this.dataLctoDistrib = data;
		
	}

	/**
	 * Retorna a quantidade de arquivos gerados apos o processamento.
	 * 
	 * @return
	 */
	public int getQuantidadeArquivosGerados() {
		return quantidadeArquivosGerados;
	}
	
}
