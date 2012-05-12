function TabCota(tabName , funcaoSalvarInicial) {
	
	var T = this;
	
	/**
	 * Função que será chamada ao realizar a mudança de abas
	 */
	this.funcaoSalvar = funcaoSalvarInicial;
	
	this.novaAba=null;
		
	/**
	 * Verifica qual aba foi selecionada, define o metodo de salvamento da mesma e chama seu método carregamento.
	 */
	this.carregaDadosNovaAba = function(abaSelecionada) {
			
		switch(abaSelecionada) {
		
		/**DADOS CADASTRAIS**/
		case 0: 
			T.funcaoSalvar = function(){alert('//TODO - Definir método(tabCota.js)(tabCota.js) - Salvar Dados Cadastrais.');};
			alert('//TODO - Definir método(tabCota.js) - Carregar dados da Aba Dados Cadastrais.');
			break;
			
		/**ENDERECOS**/
		case 1:
			T.funcaoSalvar = function(){alert('//TODO - Definir método(tabCota.js) - Salvar enderecos.');};
			ENDERECO_COTA.popularGridEnderecos();
			break;
		
		/**TELEFONES**/
		case 2: 
			T.funcaoSalvar = function(){alert('//TODO - Definir método(tabCota.js) - Salvar Telefones.');};
			COTA.carregarTelefones();
			break;

		/**PDV**/
		case 3: 
			T.funcaoSalvar = function(){alert('//TODO - Definir método(tabCota.js) - Salvar Dados PDV.');};
			carregarPDV();
			break;
		
		/**GARANTIA**/
		case 4: 
			T.funcaoSalvar = function(){alert('//TODO - Definir método(tabCota.js) - Salvar Garantia.');};
			alert('//TODO - Definir método(tabCota.js) - Carregar dados da Aba Garantia.');
			break;
		
		/**FINANCEIRO**/
		case 5:
			T.funcaoSalvar = function(){postarParametroCobranca();};
			carregaFinanceiro();
			break;

		/**BANCOS**/		
		case 6:
			T.funcaoSalvar = function(){return confirm('Deseja sair da aba sem salvar? \n//TODO - Definir método(tabCota.js) - Salvar Dados Bancos.'); };
			alert('//TODO - Definir método(tabCota.js) - Carregar dados da Aba Bancos.');
			break;
		
		/**DISTRIBUICAO**/
		case 7:
			T.funcaoSalvar = DISTRIB_COTA_CPF.salvar;
			DISTRIB_COTA_CPF.carregarDadosDistribuicaoCota( $('#_idCotaRef').val() );
			break;
		
		/**FORNECEDOR**/
		case 8:
			T.funcaoSalvar = function(){alert('//TODO - Definir método(tabCota.js) - Salvar Dados Fornecedor.');};
			alert('//TODO - Definir método(tabCota.js) - Carregar dados da Aba Fornecedor.');
			break;
		}
		
	},
	
	this.abrirNovaAba = function() {
		
		T.funcaoSalvar = null;
		
		$("#" + tabName).tabs('select', T.novaAba);
	};
		
	$(function() {
		
		if(T.funcaoSalvar) {
			T.funcaoSalvar();
    	}
		
		$( "#" + tabName ).tabs();
				
		$("#" + tabName).tabs({
						
		    select: function(event, ui) {
		    	
		    	if(!T.funcaoSalvar) {
		    		return;
		    	}
		    	
		    	T.novaAba = ui.index;
		    	
		    	return T.funcaoSalvar(T.abrirNovaAba);
		    	
		    	//return false;
		    }
		});	
		
		$("#" + tabName).tabs({
			
		    show: function(event, ui) {				
		    	T.carregaDadosNovaAba(ui.index);
		    }
		});		
		
	});
		
}