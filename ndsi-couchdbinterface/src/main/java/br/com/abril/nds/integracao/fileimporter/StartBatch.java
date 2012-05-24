package br.com.abril.nds.integracao.fileimporter;

import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

/**
 * Classe principal para executar as interfaces por linha de comando.
 */
public class StartBatch {

	public static void main(String[] args) {
		
		String usuario = null;
		Long codigoInterface = null;
		Long codigoDistribuidor = null;
		
		if (args == null || args.length < 2 || args.length > 3) {
			System.out.println("ERRO: numero de argumentos invalido");
			return;
		}
		
		usuario = args[0];
		
		try {
			codigoInterface = Long.valueOf(args[1]);
		} catch (NumberFormatException e)  {
			System.out.println("ERRO: codigo de interface invalido");
			return;
		}
		
		InterfaceEnum interfaceEnum = InterfaceEnum.getByCodigo(codigoInterface);
		if (interfaceEnum == null) {
			System.out.println("ERRO: interface invalida");
			return;
		}
		
		if (args.length == 3) {
			try {
				codigoDistribuidor = Long.valueOf(args[2]);
			} catch (NumberFormatException e)  {
				System.out.println("ERRO: codigo de distribuidor invalido");
				return;
			}
		}
		
		InterfaceExecutor executor = new InterfaceExecutor();
		executor.executarInterface(usuario, interfaceEnum, codigoDistribuidor.toString());
	}
}
