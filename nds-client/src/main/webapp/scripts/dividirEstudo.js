var dividirEstudo = $.extend(true, {
    
    inicializar : function() {
	
	var numeroEstudoOriginal = $('#numeroEstudoOriginal', this.workspace);
	var codigoProduto = $('#codigoProduto', this.workspace);
	var nomeProduto = $('#nomeProduto', this.workspace);
	var edicaoProduto = $('#edicaoProduto', this.workspace);
	var classificacao = $('#classificacao', this.workspace);
	var dataDistribuicao = $('#dataDistribuicao', this.workspace);
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo', this.workspace);
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo', this.workspace);
	var quantidadeReparte = $('#quantidadeReparte', this.workspace);
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo', this.workspace);
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo', this.workspace);
	var numeroSegundoEstudo = $('#numeroSegundoEstudo', this.workspace);
	var reparteSegundoEstudo = $('#reparteSegundoEstudo', this.workspace);
	var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo', this.workspace);
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo', this.workspace);
	
	numeroEstudoOriginal.numeric();
	codigoProduto.numeric();
	edicaoProduto.numeric();
	percentualDivisaoPrimeiroEstudo.numeric();
	percentualDivisaoSegundoEstudo.numeric();
	quantidadeReparte.numeric();
	
	
	console.log("typeof(matrizDistribuicao):::"+typeof(matrizDistribuicao));
		if(typeof(matrizDistribuicao)=="object"){
			var estudo = estudoParaDivisao;
			$(codigoProduto).val(estudo.codigoProduto);
			$(numeroEstudoOriginal).val(estudo.estudo);
			$(nomeProduto).val(estudo.nomeProduto);
			$(edicaoProduto).val(estudo.edicao);
//			$(dataDistribuicao).val($("#datepickerDe").val());
			$(dataDistribuicao).val(estudo.dataLancto);
			$(classificacao).val(estudo.classificacao);
			
			
			
		}
    },

    gerarDivisao : function() {

	var numeroEstudoOriginal = $('#numeroEstudoOriginal');
	var codigoProduto = $('#codigoProduto');
	var nomeProduto = $('#nomeProduto');
	var edicaoProduto = $('#edicaoProduto');
	var dataDistribuicao = $('#dataDistribuicao');
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo');
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo');
	var quantidadeReparte = $('#quantidadeReparte');
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo');
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo');
	var numeroSegundoEstudo = $('#numeroSegundoEstudo');
	var reparteSegundoEstudo = $('#reparteSegundoEstudo');
	var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo');
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo');

	var dados = [];

	dados.push({
	    name : "divisaoEstudo.numeroEstudoOriginal",
	    value : numeroEstudoOriginal.val()
	});

	dados.push({
	    name : "divisaoEstudo.codigoProduto",
	    value : codigoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.nomeProduto",
	    value : nomeProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.edicaoProduto",
	    value : edicaoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataDistribuicao",
	    value : dataDistribuicao.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
	    value : percentualDivisaoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
	    value : percentualDivisaoSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.quantidadeReparte",
	    value : quantidadeReparte.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroPrimeiroEstudo",
	    value : numeroPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.repartePrimeiroEstudo",
	    value : repartePrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroSegundoEstudo",
	    value : numeroSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.reparteSegundoEstudo",
	    value : reparteSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
	    value : dataLancamentoSegundoEstudo.val()
	});

	$.postJSON('dividirEstudo/gerarDivisao', dados, function(response) {
	    numeroPrimeiroEstudo.val(response.divisaoEstudoDTO.numeroPrimeiroEstudo);
//	    repartePrimeiroEstudo.val(response.divisaoEstudoDTO.repartePrimeiroEstudo);
//	    dataLancamentoPrimeiroEstudo.val(response.divisaoEstudoDTO.dataLancamentoPrimeiroEstudo);
	    dataLancamentoPrimeiroEstudo.val($(dataDistribuicao).val());
	    
	    numeroSegundoEstudo.val(response.divisaoEstudoDTO.numeroSegundoEstudo);
//	    reparteSegundoEstudo.val(response.divisaoEstudoDTO.reparteSegundoEstudo);
	    $('.corpo').flexReload();
	}, function() {
	});

    },

    confirmar : function() {

		var numeroEstudoOriginal = $('#numeroEstudoOriginal');
		var codigoProduto = $('#codigoProduto');
		var nomeProduto = $('#nomeProduto');
		var edicaoProduto = $('#edicaoProduto');
		var dataDistribuicao = $('#dataDistribuicao');
		var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo');
		var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo');
		var quantidadeReparte = $('#quantidadeReparte');
		var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo');
		var repartePrimeiroEstudo = $('#repartePrimeiroEstudo');
		var numeroSegundoEstudo = $('#numeroSegundoEstudo');
		var reparteSegundoEstudo = $('#reparteSegundoEstudo');
		var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo');
		var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo');

		if(dataLancamentoSegundoEstudo.val() == "") {
			exibirMensagem("WARNING",["Favor digitar uma data para o estudo 2."]);
			return;
		}
	
		var dados = [];

		dados.push({
		    name : "divisaoEstudo.numeroEstudoOriginal",
		    value : numeroEstudoOriginal.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.codigoProduto",
		    value : codigoProduto.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.nomeProduto",
		    value : nomeProduto.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.edicaoProduto",
		    value : edicaoProduto.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.dataDistribuicao",
		    value : dataDistribuicao.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
		    value : percentualDivisaoPrimeiroEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
		    value : percentualDivisaoSegundoEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.quantidadeReparte",
		    value : quantidadeReparte.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.numeroPrimeiroEstudo",
		    value : numeroPrimeiroEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.repartePrimeiroEstudo",
		    value : repartePrimeiroEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.dataLancamentoPrimeiroEstudo",
		    value : dataLancamentoPrimeiroEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.numeroSegundoEstudo",
		    value : numeroSegundoEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.reparteSegundoEstudo",
		    value : reparteSegundoEstudo.val()
		});
	
		dados.push({
		    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
		    value : dataLancamentoSegundoEstudo.val()
		});

		$.postJSON('dividirEstudo/confirmar', dados, function(response) {
	/*//	    $('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
	//	    var pathTela = "/nds-client-f2";
		    if(typeof(matrizDistribuicao)=="object"){
	//	    	var matrizDistribuicao = new MatrizDistribuicao(pathTela, "matrizDistribuicao", BaseController.workspace);
	//	    	console.log("pesquisando retorno da divisão")
	//	    	matrizDistribuicao.pesquisar();
	*/
			
			if(response){
				exibirMensagem("SUCCESS",[response.msg]);
				$('#repartePrimeiroEstudo').val(response.reparte[0]);
				$('#reparteSegundoEstudo').val(response.reparte[1]);
				
				$('#numeroPrimeiroEstudo').val(response.estudo[0]);
				$('#numeroSegundoEstudo').val(response.estudo[1]);
				
				if(typeof(matrizDistribuicao)=="object"){
	//		    	var matrizDistribuicao = new MatrizDistribuicao(pathTela, "matrizDistribuicao", BaseController.workspace);
			    	matrizDistribuicao.pesquisar();
				}
			}
	    
		}, function() {
		});
    },

    acaoVoltar:function(tabTitle){
    	
    	$('#workspace').tabs('remove', $('#workspace').tabs('option', 'selected')); selectTabTitle(tabTitle);
    	
    },
    cancelar : function() {

	var numeroEstudoOriginal = $('#numeroEstudoOriginal');
	var codigoProduto = $('#codigoProduto');
	var nomeProduto = $('#nomeProduto');
	var edicaoProduto = $('#edicaoProduto');
	var dataDistribuicao = $('#dataDistribuicao');
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo');
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo');
	var quantidadeReparte = $('#quantidadeReparte');
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo');
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo');
	var numeroSegundoEstudo = $('#numeroSegundoEstudo');
	var reparteSegundoEstudo = $('#reparteSegundoEstudo');
	var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo');
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo');

	var dados = [];

	dados.push({
	    name : "divisaoEstudo.numeroEstudoOriginal",
	    value : numeroEstudoOriginal.val()
	});

	dados.push({
	    name : "divisaoEstudo.codigoProduto",
	    value : codigoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.nomeProduto",
	    value : nomeProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.edicaoProduto",
	    value : edicaoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataDistribuicao",
	    value : dataDistribuicao.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
	    value : percentualDivisaoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
	    value : percentualDivisaoSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.quantidadeReparte",
	    value : quantidadeReparte.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroPrimeiroEstudo",
	    value : numeroPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.repartePrimeiroEstudo",
	    value : repartePrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoPrimeiroEstudo",
	    value : dataLancamentoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroSegundoEstudo",
	    value : numeroSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.reparteSegundoEstudo",
	    value : reparteSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
	    value : dataLancamentoSegundoEstudo.val()
	});

	$.postJSON('dividirEstudo/cancelar', dados, function(response) {
	    percentualDivisaoPrimeiroEstudo.val(response.divisaoEstudoDTO.percentualDivisaoPrimeiroEstudo);
	    percentualDivisaoSegundoEstudo.val(response.divisaoEstudoDTO.percentualDivisaoSegundoEstudo);
	    quantidadeReparte.val(response.divisaoEstudoDTO.quantidadeReparte);
	    numeroPrimeiroEstudo.val(response.divisaoEstudoDTO.numeroPrimeiroEstudo);
	    numeroSegundoEstudo.val(response.divisaoEstudoDTO.numeroSegundoEstudo);
	    repartePrimeiroEstudo.val(response.divisaoEstudoDTO.repartePrimeiroEstudo);
	    reparteSegundoEstudo.val(response.divisaoEstudoDTO.reparteSegundoEstudo);
	    dataLancamentoPrimeiroEstudo.val(response.divisaoEstudoDTO.dataLancamentoPrimeiroEstudo);
	    dataLancamentoSegundoEstudo.val(response.divisaoEstudoDTO.dataLancamentoSegundoEstudo);
	}, function() {
	});
    },
    
    analisar : function() {
		//testa se registro selecionado possui estudo gerado
//		if ($('#numeroEstudoOriginal').val() == null || $('#numeroEstudoOriginal').val() == "") {
    	if ($('#numeroPrimeiroEstudo').val() == null || $('#numeroPrimeiroEstudo').val() == "") {
    	
			exibirMensagem("WARNING",["Gere o estudo antes de fazer a análise."]);
			return;
		} else {
			// Deve ir direto para EMS 2031 
			//funcao declarada no arquivo Util.js
			insertTelaAnalise('#dividirEstudoContent', '#dividirEstudoTelaAnalise', $('#numeroPrimeiroEstudo').val());
		}
	},
	
    tratarPercentualDivisao:function(input,target){
    	
    	var perc1 =0;
    	if(input.value!=""){
    		perc1 = parseInt(input.value);
    	}
    	
    	$(target).val(100-perc1);
    	
    }
}, BaseController);
//@ sourceURL=dividirEstudo.js