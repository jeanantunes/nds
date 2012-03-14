var cota = {
	
	//Limpar campos pesquisa
	limparCamposPesquisa : function(idCampoNumeroCota, idCampoNomeCota, callback) {
		
		$(idCampoNumeroCota).val("");
		$(idCampoNomeCota).val("");
		
		if (callback) {
			
			callback();
		}
	},
		
	//Pesquisa por número da cota
	pesquisarPorNumeroCota : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		//Seta um time out para esperar a execução da função que limpa os campos
		//no momento que o número da cota é alterado na tela.
		setTimeout(
			function() { 
				cota.pesquisarPorNumeroCotaAposTimeOut(
					idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack);
			}, 
			100
		);
	},
	
	//Pesquisa por número da cota após um time out
	pesquisarPorNumeroCotaAposTimeOut : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				"numeroCota=" + numeroCota,
				function(result) { 
					cota.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					cota.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack); 
				}, 
				isFromModal
			);
		}
	},
	
	//Success callback para pesquisa por número da cota
	pesquisarPorNumeroSuccessCallBack : function(result, idCampoNomeCota, successCallBack) {

		$(idCampoNomeCota).val(result.nome);
		
		$(idCampoNomeCota).focus();
		
		if (successCallBack) {
			
			successCallBack();
		}
	},
	
	//Error callback para pesquisa por número da cota
	pesquisarPorNumeroErrorCallBack : function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota).val("");
		
		$(idCampoNumeroCota).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Busca dados para o auto complete do nome da cota
	autoCompletarPorNome : function(idCampoNomeCota, isFromModal) {
		
		var nomeCota = $(idCampoNomeCota).val();
		
		nomeCota = $.trim(nomeCota);
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", "nomeCota=" + nomeCota,
				function(result) { 
					cota.exibirAutoComplete(result, idCampoNomeCota); 
				},
				null, 
				isFromModal
			);
		}
	},
	
	//Exibe o auto complete no campo
	exibirAutoComplete : function(result, idCampoNomeCota) {
		
		$(idCampoNomeCota).autocomplete({
			source: result,
		});
	},
	
	//Pesquisar por nome da cota
	pesquisarPorNomeCota : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		//Seta um time out para esperar o auto complete do campo 
		//de nome da cota a ser preenchido pelo jQuery
		setTimeout(
			function() {
				cota.pesquisarPorNomeCotaAposTimedOut(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack); 
			},  
			200
		);
	},
	
	//Pesquisa por nome da cota após um time out
	pesquisarPorNomeCotaAposTimedOut : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var nomeCota = $(idCampoNomeCota).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				"nomeCota=" + nomeCota,
				function(result) {
					cota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack);
				},
				function() {
					cota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
				}, 
				isFromModal
			);
		}
	},
	
	//Success callback para pesquisa por nome da cota
	pesquisarPorNomeSuccessCallBack : function(result, idCampoNumeroCota, idCampoNomeCota, successCallBack) {
		
		if (result != "") {
			
			$(idCampoNumeroCota).val(result.numero);
			$(idCampoNomeCota).val(result.nome);
			
			if (successCallBack) {
				
				successCallBack();
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	pesquisarPorNomeErrorCallBack : function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota).val("");
		
		$(idCampoNomeCota).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	}
	
};