package br.com.abril.nds.component.singleton;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;


public class ProcessoCadastroDescontoSingleton {

	public enum StatusProcesso {
		
		INICIADO(true), ENCERRADO(false); 
		
		private boolean ativo;
		
		private StatusProcesso(boolean ativo) {
			this.ativo = ativo;
		}
		
		public boolean getBooleanValue() {
			return ativo;
		}
	}
	
	private Boolean processoDescontoProduto = false;

	private Boolean processoDescontoGeral = false;
	
	private Boolean processoDescontoEspecifico = false;
	
	private static ProcessoCadastroDescontoSingleton instance = null;
	
	
	private ProcessoCadastroDescontoSingleton() {
	
	}

	public static ProcessoCadastroDescontoSingleton getInstance() {
		if (instance == null) {
			instance = new ProcessoCadastroDescontoSingleton();
		}
		return instance;
	}

	
	public static boolean isProcessoAtivo(TipoDesconto tipoDesconto) {
		return  getInstance().obterStatusProcesso(tipoDesconto);
	}

	
	public static void iniciarProcesso(TipoDesconto tipoDesconto) {
		
		if (isProcessoAtivo(tipoDesconto)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Não foi possível iniciar este processo, " +
					"já existe um processo de cadastro para este tipo de desconto em andamento. Por favor, aguarde!"));
		}
		
		getInstance().atualizarStatusProcesso(tipoDesconto, StatusProcesso.INICIADO);
	}
	
	public static void encerrarProcesso(TipoDesconto tipoDesconto) {
		getInstance().atualizarStatusProcesso(tipoDesconto, StatusProcesso.ENCERRADO);
	}
	
	/**
	 * Obtem status do processo
	 * 
	 * @param tipoDesconto
	 * @return
	 */
	private Boolean obterStatusProcesso(TipoDesconto tipoDesconto) {
		
		switch (tipoDesconto) {
		
		case GERAL:
			return this.processoDescontoGeral;

		case ESPECIFICO:
			return this.processoDescontoEspecifico;
			
		case PRODUTO:
			return this.processoDescontoProduto;
			
		default:
			return false;
		}
	}
	
	/**
	 * Atualiza status do processo
	 * 
	 * @param tipoDesconto
	 * @param status
	 */
	private void atualizarStatusProcesso(TipoDesconto tipoDesconto, StatusProcesso status) {
		
		switch (tipoDesconto) {
		
		case GERAL:
			this.processoDescontoGeral = status.getBooleanValue();
			break;
		
		case ESPECIFICO:
			this.processoDescontoEspecifico = status.getBooleanValue();
			break;
		
		case PRODUTO:
			this.processoDescontoProduto = status.getBooleanValue();
			break;
		}
	}	
}
