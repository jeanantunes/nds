Array.prototype.removeByValue = function(){
    var what, a= arguments, L= a.length, ax;
    while(L && this.length){
        what= a[--L];
        while((ax= this.indexOf(what))!= -1){
            this.splice(ax, 1);
        }
    }
    return this;
};

var impressaoNfeController = $.extend(true, {

	filtroProdutos : [],
	
	filtroNotasImprimirNFe : [],
	
	filtroCotasImprimirNFe : [],
	
	paginaAtualPesquisarGrid : 1,
	
	totalRegistros : 0,
	
	/**
	 * path de geração de nfe
	 */
	path : contextPath + '/nfe/impressaoNFE/',
	
	init : function() {
		
		params = [];
		params.push({name: 'tipoEmitente', value: 'DISTRIBUIDOR'});
		params.push({name: 'tipoDestinatario', value: 'COTA'});
		
		$.postJSON(contextPath + '/administracao/naturezaOperacao/obterNaturezasOperacoesPorEmitenteDestinatario', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			$("#impressaoNfe-filtro-naturezaOperacao").empty();
			
			$('#impressaoNfe-filtro-naturezaOperacao').append($('<option>', { 
		        value: '-1',
		        text : 'Selecione...'
		    }));
			
			$.each(data.rows, function (i, row) {
			    $('#impressaoNfe-filtro-naturezaOperacao').append($('<option>', { 
			        value: row.cell.key,
			        text : row.cell.value
			    }));
			});
			
		});
		

		impressaoNfeController.initInputs();
		impressaoNfeController.initFlexiGrids();
		impressaoNfeController.initFiltroDatas();
		impressaoNfeController.initDialog();
	
		$("#impressaoNfe-filtro-tipoEmissao").attr('disabled', 'disabled');
		
		$( "#msgBoxDataMovimentoInvalida", this.workspace ).dialog({
			autoOpen: false,
			modal: true,
		});

		$(".menu_produtos", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_produtos", impressaoNfeController.workspace).hide();
		});		

		//$(".grids").show();

		this.criarDialogPesquisarProdutos();
		
		$(".areaBts", impressaoNfeController.workspace).hide();
	},
	
	desabilitarFiltro : function() {
		// roteiro 
		$("#impressaoNfe-filtro-idRoteiro").attr('disabled', 'disabled');
		$("#impressaoNfe-filtro-idRoteiro", impressaoNfeController.workspace).val("-1");
		
		// rota
		$("#impressaoNfe-filtro-idRota", impressaoNfeController.workspace).val("-1");
		$("#impressaoNfe-filtro-idRota").attr('disabled', 'disabled');
		
		$("#impressaoNfe-filtro-idCotaInicial").attr('disabled', 'disabled');
		$("#impressaoNfe-filtro-idCotaFinal").attr('disabled', 'disabled');
		$("#impressaoNfe-filtro-idCotaInicial").val("");
		$("#impressaoNfe-filtro-idCotaFinal").val("");
		$("#impressaoNfe-filtro-inputIntervaloBoxDe", impressaoNfeController.workspace).val("-1");
		$("#impressaoNfe-filtro-inputIntervaloBoxDe").attr('disabled', 'disabled');
		$("#impressaoNfe-filtro-inputIntervaloBoxAte", impressaoNfeController.workspace).val("-1");
		$("#impressaoNfe-filtro-inputIntervaloBoxAte").attr('disabled', 'disabled');
		
	},
	
	habilitarFiltro : function() {
		
		$("#impressaoNfe-filtro-idRoteiro").removeAttr('disabled');
		$("#impressaoNfe-filtro-idRota").removeAttr('disabled');
		$("#impressaoNfe-filtro-idCotaInicial").removeAttr('disabled');
		$("#impressaoNfe-filtro-idCotaFinal").removeAttr('disabled');
		$("#impressaoNfe-filtro-inputIntervaloBoxDe").removeAttr('disabled');
		$("#impressaoNfe-filtro-inputIntervaloBoxAte").removeAttr('disabled');
		
	},
	
	verificarTipoDestinatario : function(element) {
		
		if(element.value != "FORNECEDOR") {
			$("#impressaoNfe-filtro-selectFornecedoresDestinatarios option:selected").removeAttr("selected");
			$("#impressaoNfe-filtro-selectFornecedoresDestinatarios").multiselect("disable");
			
			impressaoNfeController.habilitarFiltro();
			
		} else {
			
			impressaoNfeController.desabilitarFiltro();
			$("#impressaoNfe-filtro-selectFornecedoresDestinatarios").multiselect("enable");
		
		}
		
		var emitente = '';
		if(element.value == 'COTA') {
			emitente = 'DISTRIBUIDOR';
		} else if(element.value == 'DISTRIBUIDOR') {
			emitente = 'COTA';
		} else if(element.value == 'FORNECEDOR') {
			emitente = 'DISTRIBUIDOR';
		}
		
		params = [];
		params.push({name: 'tipoEmitente', value: emitente});
		params.push({name: 'tipoDestinatario', value: element.value});
		
		$.postJSON(contextPath + '/administracao/naturezaOperacao/obterNaturezasOperacoesPorEmitenteDestinatario', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			$("#impressaoNfe-filtro-naturezaOperacao").empty();
			
			$('#impressaoNfe-filtro-naturezaOperacao').append($('<option>', { 
		        value: '-1',
		        text : 'Selecione...'
		    }));
			
			$.each(data.rows, function (i, row) {
			    $('#impressaoNfe-filtro-naturezaOperacao').append($('<option>', { 
			        value: row.cell.key,
			        text : row.cell.value
			    }));
			});
			
		});
		
	},
	
	criarDialogPesquisarProdutos : function() {		

		$("#dialog-pesqProdutos", this.workspace).dialog({
			resizable: false,
			height:550,
			width:540,
			modal: true,
			autoOpen: false,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			div: $("#dialog-pesqProdutos", this.workspace).parents("div")
		});

	},
	
	prepararJSONPesquisaProdutos : function(data) {

		$.each(data.rows, function() {
			checked = false;
			for(var i = 0; i < impressaoNfeController.filtroProdutos.length; i++) {
				if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == this.cell.codigoProduto) {
					checked = true;
				}
			}
			this.cell.sel = '<input type="checkbox" '+ (checked ? 'checked=checked':'') +' name="codigoProduto" id="codigoProduto_'+ this.cell.codigoProduto +'" value="'+ this.cell.codigoProduto +'" alt="'+ this.cell.nomeProduto + '"  onclick="impressaoNfeController.adicionarAosProdutosFiltradosERecarregar(this.checked, '+ this.cell.codigoProduto +', \''+ this.cell.nomeProduto +'\')" />';
		});
		return data;
	},

	prepararJSONPesquisaProdutosFiltrados : function(data) {

		$.each(data.rows, function() {
			this.cell.sel = '<img style="cursor: pointer;" src="'+ contextPath +'/images/ico_excluir.gif" alt="Excluir do filtro" title="Excluir do filtro" name="removerProduto" id="removerProduto_'+ this.cell.codigoProduto +'" value="'+ this.cell.codigoProduto + '" onclick="impressaoNfeController.removerDosProdutosFiltrado('+ this.cell.codigoProduto +')" />';
		});
		return data;
	},

	/**
	 * Retorna as cotas com NFe disponíveis para emissão baseado nos filtros selecionados
	 */
	pesquisar : function() {

		impressaoNfeController.initFlexiGrids();
		
		impressaoNfeController.filtroNotasImprimirNFe = [];
		
		var tipoDestinatario = $("input[name='tipoDestinatario']:checked").val();
		
		if(typeof($('#impressaoNfe-filtro-naturezaOperacao', impressaoNfeController.workspace).val()) === 'undefined' 
				|| $('#impressaoNfe-filtro-naturezaOperacao', impressaoNfeController.workspace).val() < 0) {
			
			exibirMensagemDialog("WARNING", ["Selecione a Natureza de Operação desejada."], "");
			return;
		}
		
		params = [ 	{name:'filtro.idNaturezaOperacao', value:$('#impressaoNfe-filtro-naturezaOperacao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoInicial', value:$('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoFinal', value:$('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissao', value:$('#impressaoNfe-filtro-dataEmissao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRoteiro', value:$('#impressaoNfe-filtro-idRoteiro option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRota', value:$('#impressaoNfe-filtro-idRota option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaInicial', value:$('#impressaoNfe-filtro-idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaFinal', value:$('#impressaoNfe-filtro-idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxInicial', value:$('#impressaoNfe-filtro-inputIntervaloBoxDe', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxFinal', value:$('#impressaoNfe-filtro-inputIntervaloBoxAte', impressaoNfeController.workspace).val()},
		           	{name:'filtro.numeroNotaDe', value:$('#impressaoNfe-filtro-notaDe', impressaoNfeController.workspace).val()},
		           	{name:'filtro.numeroNotaAte', value:$('#impressaoNfe-filtro-notaAte', impressaoNfeController.workspace).val()}
		           	];
		
		if ($('#impressaoNfe-filtro-selectFornecedoresDestinatarios').val()) {
			$.each($("#impressaoNfe-filtro-selectFornecedoresDestinatarios").val(), function(index, v) {
				params.push({name : "filtro.listIdFornecedor[]", value : v});
			});
		}
		
		for(var i = 0; i < impressaoNfeController.filtroProdutos.length; i++) {

			params.push({
				'name' : 'filtro.codigosProdutos[]',
				'value' : impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto']
			});
		}
		
		if(tipoDestinatario == "FORNECEDOR"){
			
			$(".impressaoGridFornecedor", impressaoNfeController.workspace).flexOptions({
				preProcess: impressaoNfeController.executarPreProcessamento,
				url: contextPath + "/nfe/impressaoNFE/pesquisarImpressaoNFE",
				dataType : 'json',
				params: params
			});
			$(".impressaoGrid", impressaoNfeController.workspace).parent().parent().hide();
			$(".grids", impressaoNfeController.workspace).hide();
			
			$(".impressaoGridFornecedor", impressaoNfeController.workspace).flexReload();
			$(".impressaoGridFornecedor", impressaoNfeController.workspace).parent().parent().show();
			
		} else {
			
			$(".impressaoGrid", impressaoNfeController.workspace).flexOptions({
				preProcess: impressaoNfeController.executarPreProcessamento,
				url: contextPath + "/nfe/impressaoNFE/pesquisarImpressaoNFE",
				dataType : 'json',
				params: params
			});
			
			$(".impressaoGridFornecedor", impressaoNfeController.workspace).parent().parent().hide();
			$(".grids", impressaoNfeController.workspace).hide();
			
			$(".impressaoGrid", impressaoNfeController.workspace).flexReload();
			$(".impressaoGrid", impressaoNfeController.workspace).parent().parent().show();
		}
		
		$(".grids", impressaoNfeController.workspace).show();
		
		$(".areaBts", impressaoNfeController.workspace).show();
		
	},

	/**
	 * Carrega o combo de rotas baseado no roteiro selecionado 
	 */
	carregarRotas : function() {
		$.postJSON(contextPath + "/nfe/impressaoNFE/carregarRotasImpressaoNFE"
				, {idRoteiro: $('#impressaoNfe-filtro-idRoteiro', impressaoNfeController.workspace).val()}
		, function(data) {

			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}

			$('#impressaoNfe-filtro-idRota').find('option').remove().end();

			$("#impressaoNfe-filtro-idRota").append($("<option />").val(-1).text("Selecione..."));
			$.each(data, function(index, item) {
				$("#impressaoNfe-filtro-idRota").append($("<option />").val(item["key"]["$"]).text(item["value"]["$"]));
			});
		}
		);
	},
	
	
	/**
	 * Recarregar combos por Box
	 */
    changeBox : function() {
		
    	var boxDe =  $("#impressaoNfe-filtro-inputIntervaloBoxDe").val();
    	
    	var boxAte = $("#impressaoNfe-filtro-inputIntervaloBoxAte").val();
    	
    	var params = [{
			            name : "codigoBoxDe",
			            value : boxDe	
					  },{
						name : "codigoBoxAte",
						value : boxAte
					  }];
    	
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorBox', params, 
			function(result) {
    		    
    		    var listaBox = result[2];
    		    
    		    impressaoNfeController.recarregarCombo($("#impressaoNfe-filtro-inputIntervaloBoxDe", impressaoNfeController.workspace), listaBox ,boxDe);
     		    
    		    impressaoNfeController.recarregarCombo($("#impressaoNfe-filtro-inputIntervaloBoxAte", impressaoNfeController.workspace), listaBox ,boxAte);
    	    }    
		);
	},
	
	/**
	 * Recarregar combo
	 */
	recarregarCombo : function (comboNameComponent, content, valSelected) {
		
		comboNameComponent.empty();

		comboNameComponent.append(new Option('Selecione...', '', true, true));
		
	    $.each(content, function(index, row) {
		    	
	    	comboNameComponent.append(new Option(row.value.$, row.key.$, true, true));
		});

	    if (valSelected) {
	    	
	        $(comboNameComponent).val(valSelected);
	    } else {
	    	
	        $(comboNameComponent).val('');
	    }
	},
	
	
	executarPreProcessamento : function(resultado) {

		if (resultado.mensagens) {

			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);

			$(".grids", impressaoNfeController.workspace).hide();

			return resultado;
		}

		$.each(resultado.rows, function() {

			for(var i=0; i < impressaoNfeController.filtroProdutos.length; i++) {
				if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == this.cell.codigoProduto) {
					checked = true;
				}
			}

			if(this.cell.notaImpressa)
				this.cell.notaImpressa = '<img src="'+ contextPath +'/images/ico_check.gif" alt="Nota Impressa" title="Nota Impressa" id="imgNotaImpress_'+ this.cell.idCota +'" />';
			else
				this.cell.notaImpressa = '';

			this.cell.sel = '<input type="checkbox" name="imprimirNFe" id="imprimirNFe_'+ this.cell.idNota +'" cota="'+ this.cell.idCota +'" value="'+ this.cell.idNota + '" onclick="impressaoNfeController.adicionarAsNFesAImprimir(this.checked, '+ this.cell.idNota +'); impressaoNfeController.adicionarAsCotasAImprimir(this.checked, '+ this.cell.idCota +')" />';
		});

		$(".grids", impressaoNfeController.workspace).show();
		
		//Adiciona opcao do combo. Numero de registros a exibir no mesmo grid (sem paginacao)
		//50: maior valor padrao de quantidade de registros
		if(resultado.total > $('div .pGroup select option:last-child').val()) {
			
			if($('div .pGroup select option:last-child').val() != resultado.total) {
				$('div .pGroup select').append($("<option />").val(resultado.total).text(resultado.total));
			}
			
		}

		return resultado;
	},

	/**
	 * Imprime o resultado da pesquisa no formato de arquivo parametrizado
	 * 
	 * @param fileType - tipo do arquivo para impressão
	 */
	imprimir : function(fileType) {

		if(impressaoNfeController.filtroNotasImprimirNFe.length < 1 && fileType != 'XLS') {
			exibirMensagem("WARNING", ["Favor selecionar as notas a serem impressa!"]);
			return false;			
		}
		
		params = [];
		
		params = [ 	{name:'filtro.idNaturezaOperacao', value:$('#impressaoNfe-filtro-naturezaOperacao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoInicial', value:$('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoFinal', value:$('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissao', value:$('#impressaoNfe-filtro-dataEmissao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRoteiro', value:$('#impressaoNfe-filtro-idRoteiro option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRota', value:$('#impressaoNfe-filtro-idRota option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaInicial', value:$('#impressaoNfe-filtro-idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaFinal', value:$('#impressaoNfe-filtro-idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxInicial', value:$('#impressaoNfe-filtro-inputIntervaloBoxDe', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxFinal', value:$('#impressaoNfe-filtro-inputIntervaloBoxAte', impressaoNfeController.workspace).val()}
		           	];
		
		
		params.push({
				'name' : 'fileType',
				'value' : fileType
			});
		
		for(var i=0; i < impressaoNfeController.filtroNotasImprimirNFe.length; i++) {
			params.push({
				'name' : 'filtro.numerosNotas[]',
				'value' : impressaoNfeController.filtroNotasImprimirNFe[i]
			});
		}
		
		var preparingFileModal = $("#preparing-file-modal").dialog({ modal: true });
		
		if(fileType == 'XLS') {
			
			$.fileDownload(contextPath +'/nfe/impressaoNFE/exportar', {
				httpMethod : "POST",
				data : params,
				
				successCallback: function (url) {
			    	$("#preparing-file-modal").dialog('close');
			    },
			    failCallback: function (responseHtml, url) {
			        preparingFileModal.dialog('close');
			        $("#error-modal").dialog({ modal: true });
			    }
			});
			
		} else {
			
			$.fileDownload(contextPath +'/nfe/impressaoNFE/imprimirNFe', {
				httpMethod : "POST",
				data : params,
				
				successCallback: function (url) {
			    	console.log('success');
			    	$("#preparing-file-modal").hide();
			    },
			    failCallback: function (responseHtml, url) {
			        preparingFileModal.dialog('close');
			        $("#error-modal").dialog({ modal: true });
			    }
				
			});
			
		}
		$('#preparing-file-modal').dialog('close');
	},
	
	enviarEmail : function(fileType){
		
		params = [];
		
		params = [ 	{name:'filtro.idNaturezaOperacao', value:$('#impressaoNfe-filtro-naturezaOperacao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoInicial', value:$('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoFinal', value:$('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissao', value:$('#impressaoNfe-filtro-dataEmissao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRoteiro', value:$('#impressaoNfe-filtro-idRoteiro option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRota', value:$('#impressaoNfe-filtro-idRota option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaInicial', value:$('#impressaoNfe-filtro-idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaFinal', value:$('#impressaoNfe-filtro-idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxInicial', value:$('#impressaoNfe-filtro-inputIntervaloBoxDe', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxFinal', value:$('#impressaoNfe-filtro-inputIntervaloBoxAte', impressaoNfeController.workspace).val()}
		           	];
		
		
		params.push({
				'name' : 'fileType',
				'value' : fileType
			});
		
		for(var i=0; i < impressaoNfeController.filtroNotasImprimirNFe.length; i++) {
			params.push({
				'name' : 'filtro.numerosNotas[]',
				'value' : impressaoNfeController.filtroNotasImprimirNFe[i]
			});
		}
		
		for(var i=0; i < impressaoNfeController.filtroCotasImprimirNFe.length; i++) {
			params.push({
				'name' : 'filtro.cotasDasNotasJaFiltradas[]',
				'value' : impressaoNfeController.filtroCotasImprimirNFe[i]
			});
		}
		
		this.confirmDialog = new ConfirmDialog('Confirmar Envio de email?', function() {
	    	
			$.postJSON(impressaoNfeController.path + 'enviarEmail', params), function(data){
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens, "");
				}
			};
	            
			return true;
		    }, function() {
		    	 
		 });
		 this.confirmDialog.open();
		
			
	},
	
	/**
	 * Marca todas as cotas do grid
	 */
	checkTodasAsNotas : function() {
		
		var tipoDestinatario = $("input[name='tipoDestinatario']:checked").val();

		var inputs = null;
		
		if(tipoDestinatario == "FORNECEDOR"){
			
			inputs = $('.impressaoGridFornecedor :checkbox', impressaoNfeController.workspace);
			
		} else {
			
			inputs = $('.impressaoGrid :checkbox', impressaoNfeController.workspace);
			
		}
		

		inputs.each(function(index) {
			if(this.id != "selTodasAsNotas") {

				if($("#selTodasAsNotas").is(':checked'))
					this.checked = true;
				else
					this.checked = false;

				impressaoNfeController.adicionarAsNFesAImprimir(this.checked, parseInt(this.value));
//				impressaoNfeController.adicionarAsCotasAImprimir(this.checked, parseInt($('#'+this.id).attr("cota")));
				
			}
		});
	},

	/**
	 * Marca todos os fornecedores do grid
	 */
	checkTodosFornecedores : function() {

		var inputs = $('#menuFornecedores :checkbox', impressaoNfeController.workspace);

		inputs.each(function(index) {
			if(this.id != "selecionarTodosFornecedores") {

				if($("#selecionarTodosFornecedores").is(':checked'))
					this.checked = true;
				else
					this.checked = false;

			}
		});
	},

	/**
	 * Marca todos os produtos do grid
	 */
	checkTodosProdutos : function(checked) {

		$('.produtosPesqGrid :input').each(function(k, v) {
			v.checked = checked;
			impressaoNfeController.adicionarAosProdutosFiltrados(checked, v.value, v.alt);
		});

		var data = {
				page:1,
				total: impressaoNfeController.filtroProdutos.length,
				rows: impressaoNfeController.filtroProdutos
		};

		impressaoNfeController.atualizarProdutosFiltrados( data );

	},
	
	filtrarProdutos : function(codigoProduto, nomeProduto) {
		params = [ 	{name:'filtro.dataEmissaoInicial', value:$('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoFinal', value:$('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).val()},
		           	{name:'filtro.codigoProduto', value:$('#dialog-pesqProdutos-codigoProduto').val()},
		           	{name:'filtro.nomeProduto', value:$('#dialog-pesqProdutos-nomeProduto').val()},
		           	];
		$(".produtosPesqGrid").flexOptions({params: params, url: contextPath + "/nfe/impressaoNFE/pesquisarProdutosImpressaoNFE"}).flexReload();
	},

	adicionarAosProdutosFiltradosERecarregar : function(check, codigoProduto, nomeProduto) {

		impressaoNfeController.atualizarProdutosFiltrados( impressaoNfeController.adicionarAosProdutosFiltrados(check, codigoProduto, nomeProduto));

	},

	adicionarAosProdutosFiltrados : function(check, codigoProduto, nomeProduto) {

		if(check) {
			impressaoNfeController.filtroProdutos.push({'cell' : {
				'codigoProduto' : codigoProduto,
				'nomeProduto' : nomeProduto
			}
			});
		} else {
			for(var i=0; i< impressaoNfeController.filtroProdutos.length; i++) {
				if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == codigoProduto) {
					impressaoNfeController.filtroProdutos.splice(i, 1);
				}
			}
		}

		var data = {
				page:1,
				total: impressaoNfeController.filtroProdutos.length,
				rows: impressaoNfeController.filtroProdutos
		};

		return data;
	},

	removerDosProdutosFiltrado : function(codigoProduto) {

		for(var i=0; i< impressaoNfeController.filtroProdutos.length; i++) {
			if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == codigoProduto) {
				impressaoNfeController.filtroProdutos.splice(i, 1);
			}
		}

		var data = {
				page:1,
				total: impressaoNfeController.filtroProdutos.length,
				rows: impressaoNfeController.filtroProdutos
		};

		var inputs = $(".produtosPesqGrid :checkbox");
		inputs.each(function(index) {
			if(this.value == codigoProduto) {	 
				this.checked = false;
			}
		});

		impressaoNfeController.atualizarProdutosFiltrados( data );

	},

	atualizarProdutosFiltrados : function(data) {
		$(".produtosAdicionadosPesqGrid").flexAddData(data);
		$(".produtosAdicionadosPesqGrid").flexReload();
	},

	adicionarAsNFesAImprimir : function(checked, idNotaFiscal) {
		
		//Verifica se ainda esta na mesma pagina, se nao, atualiza a pagina atual e limpa as cotas selecionadas na pagina anterior
		var tipoDestinatario = $("input[name='tipoDestinatario']:checked").val();
		
		if(tipoDestinatario == "FORNECEDOR") {
			if(impressaoNfeController.paginaAtualPesquisarGrid != $(".impressaoGridFornecedor").flexGetPageNumber()) {
				impressaoNfeController.paginaAtualPesquisarGrid = $(".impressaoGridFornecedor").flexGetPageNumber();
				impressaoNfeController.filtroNotasImprimirNFe = [];
			}
		} else {			
			if(impressaoNfeController.paginaAtualPesquisarGrid != $(".impressaoGrid").flexGetPageNumber()) {
				impressaoNfeController.paginaAtualPesquisarGrid = $(".impressaoGrid").flexGetPageNumber();
				impressaoNfeController.filtroNotasImprimirNFe = [];
			}
		} 
		
		
		arrayOrdenado = impressaoNfeController.filtroNotasImprimirNFe.sort();
		
		inserirIdCota = true;
		
		if(checked) {
			if(impressaoNfeController.filtroNotasImprimirNFe.length > 1) {
				for (var i = 0; i < arrayOrdenado.length; i++) {
				    if (arrayOrdenado[i] == idNotaFiscal) {
				    	inserirIdCota = false;
				    }
				}
				if(inserirIdCota)
					impressaoNfeController.filtroNotasImprimirNFe.push(idNotaFiscal);
			} else {
				if(impressaoNfeController.filtroNotasImprimirNFe[0] != idNotaFiscal);
					impressaoNfeController.filtroNotasImprimirNFe.push(idNotaFiscal);
			}
			
		} else {
			impressaoNfeController.filtroNotasImprimirNFe.removeByValue(idNotaFiscal);
		}
		
		impressaoNfeController.filtroNotasImprimirNFe.sort();
	},
	
	adicionarAsCotasAImprimir : function(checked, idCota) {
		
		//Verifica se ainda esta na mesma pagina, se nao, atualiza a pagina atual e limpa as cotas selecionadas na pagina anterior
		var tipoDestinatario = $("input[name='tipoDestinatario']:checked").val();
		
		if(tipoDestinatario == "FORNECEDOR") {
			if(impressaoNfeController.paginaAtualPesquisarGrid != $(".impressaoGridFornecedor").flexGetPageNumber()) {
				impressaoNfeController.paginaAtualPesquisarGrid = $(".impressaoGridFornecedor").flexGetPageNumber();
				impressaoNfeController.filtroCotasImprimirNFe = [];
			}
		} else {			
			if(impressaoNfeController.paginaAtualPesquisarGrid != $(".impressaoGrid").flexGetPageNumber()) {
				impressaoNfeController.paginaAtualPesquisarGrid = $(".impressaoGrid").flexGetPageNumber();
				impressaoNfeController.filtroCotasImprimirNFe = [];
			}
		} 
		
		
		arrayOrdenado = impressaoNfeController.filtroCotasImprimirNFe.sort();
		
		inserirIdCota = true;
		
		if(checked) {
			if(impressaoNfeController.filtroCotasImprimirNFe.length > 1) {
				for (var i = 0; i < arrayOrdenado.length; i++) {
				    if (arrayOrdenado[i] == idCota) {
				    	inserirIdCota = false;
				    }
				}
				if(inserirIdCota)
					impressaoNfeController.filtroCotasImprimirNFe.push(idCota);
			} else {
				if(impressaoNfeController.filtroCotasImprimirNFe[0] != idCota);
					impressaoNfeController.filtroCotasImprimirNFe.push(idCota);
			}
			
		} else {
			impressaoNfeController.filtroCotasImprimirNFe.removeByValue(idCota);
		}
		
		impressaoNfeController.filtroCotasImprimirNFe.sort();
	},
	

	initFlexiGrids : function() {
		$(".produtosPesqGrid", impressaoNfeController.workspace).flexigrid({
			preProcess : impressaoNfeController.prepararJSONPesquisaProdutos,
			//url : contextPath + "/nfe/impressaoNFE/pesquisarProdutosImpressaoNFE",
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : true,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 340,
				sortable : true,
				align : 'left',
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			showToggleBtn : false,
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : false,
			useRp : true,
			rp : 15,
			width : 500,
			height : 130
		});

		$(".produtosAdicionadosPesqGrid", impressaoNfeController.workspace).flexigrid({
			preProcess : impressaoNfeController.prepararJSONPesquisaProdutosFiltrados,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : false,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 340,
				sortable : false,
				align : 'left',
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			showToggleBtn : false,
			showTableToggleBtn : false,
			onChangeSort: true,
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			rp : 15,
			width : 500,
			height : 100,
			onChangeSort: function(name, order) {
				impressaoNfeController.sortGrid(".produtosAdicionadosPesqGrid", order); 
			},
		});

		$(".impressaoGrid", impressaoNfeController.workspace).flexigrid({
			preProcess : null,
			dataType : 'json',
			colModel : [ {
				display : 'Nota',
				name : 'numeroNota',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 340,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'vlrTotal',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. R$',
				name : 'vlrTotalDesconto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Impressão',
				name : 'notaImpressa',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "idNota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rpOptions: [10, 15, 20, 30, 50],
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			//TODO: Sérgio - Sobrescrever o changePage do Flexigrid
			/*onChangePage : function(ctype) {
				impressaoNfeController.filtroNotasImprimirNFe = [];
			},*/
		});
		
		$(".impressaoGridFornecedor", impressaoNfeController.workspace).flexigrid({
			preProcess : null,
			dataType : 'json',
			colModel : [ {
				display : 'Nota',
				name : 'numeroNota',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome do Forncedor',
				name : 'nomeCota',
				width : 340,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'vlrTotal',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. R$',
				name : 'vlrTotalDesconto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Impressão',
				name : 'notaImpressa',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "idNota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rpOptions: [10, 15, 20, 30, 50],
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			
		});
	},
	
	initFiltroDatas : function() {
		
		$.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor', null, 
				function(result) {
					$("#impressaoNfe-dataEmissaoInicial", this.workspace).val(result);
					
					$("#impressaoNfe-dataEmissaoFinal", this.workspace).val(result);
		        }
		);
		
		$( "#impressaoNfe-filtro-dataEmissao", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#impressaoNfe-dataEmissaoInicial", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#impressaoNfe-dataEmissaoFinal", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).mask("99/99/9999");
		
		$('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).mask("99/99/9999");
	},
	
	initInputs : function() {
		
		$(".input-date").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
		
		$(".input-date").mask("99/99/9999");
		$("#impressaoNfe-filtro-dataEmissao").val(formatDateToString(new Date()));
		
		$("#impressaoNfe-filtro-selectFornecedoresDestinatarios").multiselect({
			selectedList : 6,
		});
		$("#impressaoNfe-filtro-selectFornecedoresDestinatarios").multiselect("disable");
		
		$("#impressaoNfe-filtro-selectFornecedores").multiselect({
			selectedList : 6
		}).multiselect("checkAll");
		
		$("#selFornecedor", impressaoNfeController.workspace).click(function() {
			$(".menu_fornecedor", impressaoNfeController.workspace).show().fadeIn("fast");
		});

		$(".menu_fornecedor", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_fornecedor", impressaoNfeController.workspace).hide();
		});
		
	},
	
	initDialog : function(){
		$("#selProdutos", impressaoNfeController.workspace).click(function() {
			if($('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).val() == ''
				|| $('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).val() == '') {
				
				$( "#msgBoxDataMovimentoInvalida", this.workspace ).dialog( "open" );
				return;
			}
			
			params = [ 	
		           	{name:'filtro.dataEmissaoInicial', value:$('#impressaoNfe-dataEmissaoInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissaoFinal', value:$('#impressaoNfe-dataEmissaoFinal', impressaoNfeController.workspace).val()}
		           	];
			$(".produtosPesqGrid").flexOptions({params : params}).flexReload();
			$("#dialog-pesqProdutos", this.workspace).dialog( "open" );
		});
	},
	
}, BaseController);
//@ sourceURL=impressaoNfe.js