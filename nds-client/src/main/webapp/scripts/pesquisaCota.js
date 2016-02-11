function PesquisaCota(workspace) {

	var pesquisaCota = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número da cota
	this.pesquisarPorNumeroCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		
		var numeroCota = $(idCampoNumeroCota, pesquisaCota.workspace).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota, pesquisaCota.workspace).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
					{numeroCota:numeroCota},
				function(result) {
						//metodo validateResult pode ser sobrescrito por outro arquivo *.js para tratar negocialmente a cota pesquisada
						if(checkValidateResult && pesquisaCota.validateResult){
							var msgArray = pesquisaCota.validateResult(result);
							if(msgArray && msgArray.length>0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					pesquisaCota.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					pesquisaCota.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	this.pesquisarNomeCota = function(idCampoNomeCota, idCampoNumeroCota, isFromModal, successCallBack, errorCallBack) {
		
		var nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();

		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNome", {nomeCota:nomeCota},
				function(result) { 
					pesquisaCota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, successCallBack); 
				},
				function() {
					pesquisaCota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por número da cota
	this.pesquisarPorNumeroSuccessCallBack = function(result, idCampoNomeCota, successCallBack) {

		pesquisaCota.pesquisaRealizada = true;
		
		$(idCampoNomeCota, pesquisaCota.workspace).val(result.nome);
		$(situacaoCadastro, pesquisaCota.workspace).val(result.situacaoCadastro);
		if (successCallBack) {
			
			successCallBack(result);
		}
	},
	
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		$(idCampoNumeroCota, pesquisaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	
	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(idCampoNomeCota, isFromModal) {
		
		pesquisaCota.pesquisaRealizada = false;
		
		var nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNomeCota, pesquisaCota.workspace).autocomplete({source: [""]});
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", {nomeCota:nomeCota},
				function(result) { 
					pesquisaCota.exibirAutoComplete(result, idCampoNomeCota, 3);
				},
				null, 
				isFromModal
			);
		}
	},
	
	this.descricaoAtribuida = true,
	
	this.pesquisaRealizada = false,
	
	this.numeroCotaSelecionada = null,
	
	this.intervalo = null,
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, idCampoCota, tamanhoInicial) {
		
		$(idCampoCota, pesquisaCota.workspace).autocomplete({
			source: result,
			focus : function(event, ui) {
				pesquisaCota.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				pesquisaCota.descricaoAtribuida = true;
				
				if (ui.item){
					pesquisaCota.numeroCotaSelecionada = ui.item.chave.numero;
				}
			},
			select : function(event, ui) {
				pesquisaCota.descricaoAtribuida = true;
				pesquisaCota.numeroCotaSelecionada = ui.item.chave.numero;
			},
			minLength: tamanhoInicial,
			delay : 0,
		});
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		setTimeout(function() { clearInterval(pesquisaCota.intervalo); }, 10 * 1000);
		
		pesquisaCota.intervalo = $().interval(function() {
			
			if (pesquisaCota.descricaoAtribuida) {
				
				if (pesquisaCota.pesquisaRealizada) {
					
					clearInterval(pesquisaCota.intervalo);
					
					return;
				}
				
				pesquisaCota.pesquisarPorNomeCotaAposIntervalo(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack,checkValidateResult);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeCotaAposIntervalo = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		clearInterval(pesquisaCota.intervalo);
		
		var nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				{nomeCota:nomeCota},
				function(result) {

					if (result.length > 1){

						if (pesquisaCota.numeroCotaSelecionada){
							
							pesquisaCota.pesquisaRealizada = true;
							
							$(idCampoNumeroCota, pesquisaCota.workspace).val(pesquisaCota.numeroCotaSelecionada);
						}
					}
					else if(result.numero){

						pesquisaCota.pesquisaRealizada = true;
						
						$(idCampoNumeroCota, pesquisaCota.workspace).val(result.numero);
						
						//metodo validateResult pode ser sobrescrito por outro arquivo *.js para tratar negocialmente a cota pesquisada
						if(checkValidateResult && pesquisaCota.validateResult){
							var msgArray = pesquisaCota.validateResult(result);
							if(msgArray && msgArray.length>0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					}
					else{
					
					    pesquisaCota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack);
					}
				},
				function() {
					pesquisaCota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
				}, 
				isFromModal
			);
		} else {

			if (errorCallBack && typeof(errorCallBack) === 'function') {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por nome da cota
	this.pesquisarPorNomeSuccessCallBack = function(result, idCampoNumeroCota, idCampoNomeCota, successCallBack) {
		
		if (result != "") {
			
			$(idCampoNumeroCota, pesquisaCota.workspace).val(result.numero);
			$(idCampoNomeCota, pesquisaCota.workspace).val(result.nome);
			$(situacaoCadastro, pesquisaCota.workspace).val(result.situacaoCadastro);
			if (successCallBack) {
				successCallBack(result);
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota, pesquisaCota.workspace).val("");
		
		$(idCampoNomeCota, pesquisaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Obtém uma cota pelo número
	this.obterPorNumeroCota = function(numeroCota, isFromModal, successCallback, errorCallBack) {
		
		if (numeroCota && numeroCota.length > 0) {

			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				{numeroCota:numeroCota},
				function(result) { 
					successCallback(result); 
				},
				function() {
					errorCallBack();
				}, 
				isFromModal
			);
		}
	};
	
	this.obterTipoDistribuicaoPorNumeroCota = function(numeroCota) {
		
		var tipoDistribuicaoCota='';
		
		
		return tipoDistribuicaoCota;
	};
	
}
//@ sourceURL=pesquisaCota.js