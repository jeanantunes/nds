var codigos =  [],
	repartes =  [],
	listaPDV = [];

var pesquisaCota = new PesquisaCota();
var autoComplete;

var fixacaoReparteController = $.extend(true, {
//Grid de historico de edicoes 	
    wsp : function() { return fixacaoReparteController.workspace; },
	init : function() {
		
		autoComplete = new AutoCompleteCampos(fixacaoReparteController.workspace);
		
		$(".historicoGrid",fixacaoReparteController.workspace).flexigrid({
			preProcess: fixacaoReparteController.preProcessHistoricoGrid,
			dataType : 'json',
			colModel : [ 
			{
				display : 'Produto',
				name : 'codigoProduto',
				width : 75,
				sortable : true,
				align : 'left'
			},{
				display : 'Classificação',
				name : 'classificacaoProduto',
				width : 75,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 75,
				sortable : true,
				align : 'left'
			},{
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Lançamento',
				name : 'dataLancamentoString',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Recolhimento',
				name : 'dataRecolhimentoString',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Status',
				name : 'status',
				width : 100,
				sortable : true,
				align : 'left'
			}],
			width : 760,
			height : 100,
			sortname : "produtoFixado",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			rp : 6
		});
//Grid de fixacao por produto
	$(".fixacaoProdutoGrid").flexigrid({
			preProcess:function(data){return fixacaoReparteController.executarPreProcessamentoFixacaoProduto(data);}, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cotaFixada',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição Inicial',
				name : 'edicaoInicial',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Edição Final',
				name : 'edicaoFinal',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ed. Atendidas',
				name : 'edicoesAtendidas',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde Edições',
				name : 'qtdeEdicoes',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'qtdeExemplares',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 45,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "cotaFixada",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 250
		});
	//Grid de repartes por pdv	
	$(".pdvCotaGrid").flexigrid({
		preProcess:function(data){return fixacaoReparteController.preProcessarListaPdv(data);},
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'id',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome PDV',
			name : 'nomePDV',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Endereço',
			name : 'endereco',
			width : 320,
			sortable : true,
			align : 'left'
		},  {
			display : 'Reparte',
			name : 'reparte',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigo",
		sortorder : "asc",
		usepager : false,
		useRp : false,
		rp : 15,
		showTableToggleBtn : false,
		width : 600,
		height : 200
	});


	//Grid de fixacao por cota
	$(".fixacaoCotaGrid").flexigrid({
			preProcess: function(data){return fixacaoReparteController.preProcessarResultadoConsultaFixacao(data);},			
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'produtoFixado',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Classificação',
				name : 'classificacaoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição Inicial',
				name : 'edicaoInicial',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição Final',
				name : 'edicaoFinal',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ed. Atendidas',
				name : 'edicoesAtendidas',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde Ed.',
				name : 'qtdeEdicoes',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'qtdeExemplares',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "produtoFixado",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 250
		});
	
	$('#nomeCota').keyup(function (){
		autoComplete.autoCompletarPorNome("/cadastro/cota/autoCompletarPorNome",'#codigoCota', '#nomeCota', "nomeCota", 3);
	});
	
	$('#nomeProduto').keyup(function (){
		autoComplete.autoCompletarPorNome("/produto/autoCompletarPorNomeProdutoAutoComplete",'#codigoProduto', '#nomeProduto', "nome", 3);
		
	});
	
	},

	// remove itens duplicados e vazios de uma array
	uniqArray: function(dirtyArr, keyParam) {
		var cleanArrObj = {},
		returnArr = [];

		for ( var i=0; i < dirtyArr.length; i++ ) {
			if(dirtyArr[i].value !== '') {
				cleanArrObj[dirtyArr[i][keyParam]] = dirtyArr[i];
			}
		}

		for ( key in cleanArrObj ) {
			returnArr.push(cleanArrObj[key]);
		}

		return returnArr;
	},
	    
	//funcao de pre-processamento do resultado da busca de fixacao por produto
	//, exibindo/escondendo botoes de geracao de excel/pdf dependendo do resultado da pesquisa
	executarPreProcessamentoFixacaoProduto : function(resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);

			$("#btAddLoteProduto").hide();
			$("#btGerarArquivoProduto").hide();
			$("#btImprimirProduto").hide();

		}else{
			$("#btNovoProduto").show();
			$("#btAddLoteProduto").show();
			$("#btGerarArquivoProduto").show();
			$("#btImprimirProduto").show();
		}
		var i;
		for (i = 0 ; i < resultado.rows.length; i++) {

			var lastIndex = resultado.rows[i].cell.length;

			resultado.rows[i].cell["acao"]=fixacaoReparteController.getActionsConsultaFixacaoProduto(resultado.rows[i].cell);
		}
		$('.fixacaoProdutoGrid').show();
		if (resultado.result){
			return resultado.result[1];
		}

		return resultado;
	},

	//funcao de pre-processamento do resultado da busca de fixacao por cota
	//, exibindo/escondendo botoes de geracao de excel/pdf dependendo do resultado da pesquisa
	preProcessarResultadoConsultaFixacao:function(data){

		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			$("#btAddLoteCota").hide();
			$("#btGerarArquivoCota").hide();
			$("#btImprimirCota").hide();
			return data;
		}else{
			$("#btNovoCota").show();
			$("#btAddLoteCota").show();
			$("#btGerarArquivoCota").show();
			$("#btImprimirCota").show();
		}

		if (data.result){
			data.rows = data.result[1].rows;
		}



		var i;
		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell["acao"]=fixacaoReparteController.getActionsConsultaFixacaoCota(data.rows[i].cell);
		}
		
		
		if((data.mensagem == undefined) || (data.mensagens.listaMensagens[0] != "Não é possivel fixar reparte para cota tipo[Alternativo].")){
			$('#fixacaoReparte_porCota').show();
			$('.fixacaoCotaGrid').show();
		}
	
		if (data.result){
			return data.result[1];
		}
		return data;

	},
	
	//retorna acoes de edicao e exclusao de fixacao por produto
	getActionsConsultaFixacaoProduto: function (cell){

		imgAlteracao = '<a href="javascript:;" id="editar" onclick="fixacaoReparteController.editarFixacao(' + cell.id + ','+cell.qtdeExemplares + ','+cell.cotaFixada+', \'' + cell.codigoProduto + '\')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Reparte por PDV ">' +
				'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
				'</a>' ;
		imgExcluir =  '<a href="javascript:;" onclick="fixacaoReparteController.excluirFixacaoProduto(' + cell.id + ');" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fixação">' +
				'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
				'</a>';

		components = "";

		//Checa quantidade de pdvs. Caso seja maior que 1 o retorna botoes editar/excluir senao apenas o botao excluir
		if (cell.qtdPdv > 1) {
			components = imgAlteracao;
		}
			components += imgExcluir;

		return  components;

	},

	//retorna acoes de edicao e exclusao de fixacao por cota
	getActionsConsultaFixacaoCota: function (cell){
		imgAlteracao ='<a href="javascript:;" id="editar" onclick="fixacaoReparteController.editarFixacao(' + cell.id + ','+cell.qtdeExemplares + ','+cell.cotaFixada+', \'' + cell.produtoFixado + '\')" ' +
						' style="cursor:pointer;border:0px;margin:5px" title="Reparte por PDV ">' +
						'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
						'</a>';
		imgExcluir = '<a href="javascript:;" onclick="fixacaoReparteController.excluirFixacaoCota(' + cell.id + ')" '  +
						' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fixação">' +
						'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
						'</a>';


		components = "";

		//Checa quantidade de pdvs. Caso seja maior que 1 o retorna botoes editar/excluir senao apenas o botao excluir
		if (cell.qtdPdv > 1) {
			components = imgAlteracao;
		}
			components += imgExcluir;

		return  components;
	},
	
	//exibe msg de confirmacao de fixação produto
	excluirFixacaoProduto:function (idFixacao){
		$("#dialog-excluir").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(contextPath + '/distribuicao/fixacaoReparte/removerFixacaoReparte', fixacaoReparteController.getIdExcluir(idFixacao),fixacaoReparteController.exclusaoProdutoSucesso,fixacaoReparteController.exclusaoProdutoErro);
					$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
	},
	//exibe msg de confirmacao de fixação cota
	excluirFixacaoCota:function (idFixacao){
		$("#dialog-excluir").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
						$.postJSON(contextPath + '/distribuicao/fixacaoReparte/removerFixacaoReparte', fixacaoReparteController.getIdExcluir(idFixacao) , fixacaoReparteController.exclusaoCotaSucesso, fixacaoReparteController.exclusaoCotaErro);
						$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
	},

	//retorna msg de sucesso durante exclusao de produto
	exclusaoProdutoSucesso:function(result){
		$(".fixacaoProdutoGrid").flexReload();

	},

	//retorna msg de erro durante exclusao de produto
	exclusaoProdutoErro:function(result){
		exibirMensagem("ERROR", ["Houve um erro durante a exclusão "]);
	},

	//retorna msg de sucesso durante exclusao de cota
	exclusaoCotaSucesso:function(result){

		$(".fixacaoCotaGrid").flexReload();
		
	},

	//retorna msg de erro durante exclusao de cota
	exclusaoCotaErro:function(result){
		exibirMensagem("ERROR", ["Houve um erro durante a exclusão "]);
	},

	//retorna o id da fixacao. Utilizada para exclusao de fixacao
	getIdExcluir : function(idFixacao) {

		var data = [];
		data.push({name:'fixacaoReparteDTO.id',	value: idFixacao});

		return data;
	},

	//funcao que executa chamada postJSON que busca dados da fixacao 
	editarFixacao:function (idFixacao,qtdeReparte, numeroCota, codigoProduto){
		var reparteTotal= qtdeReparte,
			arrayPesquisa = [];
		
		arrayPesquisa.push({
			name : "filtro.codigoProduto", 
			value : codigoProduto
		});
		arrayPesquisa.push({
			name : "filtro.codigoCota", 
			value : numeroCota
		});

		arrayPesquisa.push({
			name : "filtro.idFixacao", 
			value : idFixacao
		});
		$.postJSON(contextPath + '/distribuicao/fixacaoReparte/editarFixacao', fixacaoReparteController.getIdSelecionado(idFixacao)
				,		
				function(result){
			$("#codigoCotaModalReparte").text(result.cotaFixadaString);
			$("#nomeCotaModalReparte").text(result.nomeCota);
			$("#codigoProdutoModalReparte").text(result.produtoFixado);
			$("#nomeProdutoModalReparte").text(result.nomeProduto);
			$("#classificacaoModalReparte").text(result.classificacaoProduto);
		}		

		);

		$( "#dialog-defineReparte" ).dialog({
			resizable: false,
			height:590,
			width:650,
			modal: true,
			//Funcao que abre o modal de repartes por pdv
			open: 	fixacaoReparteController.preencherGridPdv(arrayPesquisa),
			buttons: {
				"Confirmar": function() {
					var somaReparte=0,
						listaPDV = [];
					
					$("#pdvCotaGrid .reparteGridinput").each(function(idx, linha){
						if(linha.name == 'undefined'){
							codigos.push(0);
						}else{
							codigos.push( $(linha).val());
							//adiciona a lista de parametros os valores diretamente ao dto definido no parametro do metodo da controller.
							//Para lista, deve-se informar obrigatoriamente o indice do elemento
							listaPDV.push({
								name : "listPDV["+idx+"].codigoPdv" , 
								value : linha.name
							});
						}

						if(linha.value == 'undefined'){
							repartes.push(0);
						}else{
							if($(linha).val() !='undefined' || $(linha).val()!= ''){
								somaReparte += parseInt(linha.value);  
							}
							repartes.push($(linha).val());
							listaPDV.push({
								name : "listPDV["+idx+"].reparte" , 
								value : linha.value
							});
						}

					});

					listaPDV.push({
						name : "codProduto" , 
						value : $("#codigoProdutoModalReparte").text()
					});
					listaPDV.push({
						name : "codCota" , 
						value : $("#codigoCotaModalReparte").text()
					});

					listaPDV.push({
						name : "idFixacao" , 
						value : idFixacao
					});

					listaPDV.push({
						name : "manterFixa" , 
						value : $("#manterFixa").is(":checked")
					});

					var uniqlistaPDV = fixacaoReparteController.uniqArray(listaPDV, 'name');
					
					if(somaReparte != reparteTotal) {
						$("#dialog-confirma-reparte").dialog({
							resizable: false,
							height:'auto',
							width:300,
							modal: true,
							buttons: {
								"Confirmar": function() {
									//parametros para salvar repartes pdvs
									$.postJSON(contextPath + '/distribuicao/fixacaoReparte/salvarGridPdvReparte',  uniqlistaPDV, function(result) {
										
										$(".fixacaoCotaGrid, .fixacaoProdutoGrid", fixacaoReparteController.workspace)
										.filter(':visible')
										.flexReload();
										
										$("#dialog-defineReparte").dialog("close"); 
									},
									function(result){ });
									$(this).dialog("close");
								},
								"Cancelar": function() {
									$(this).dialog("close");
								}
							},
						});

					} else {
						$.postJSON(contextPath + '/distribuicao/fixacaoReparte/salvarGridPdvReparte', uniqlistaPDV, function(result) {
							$("#dialog-defineReparte").dialog("close");
						});
					}
//					$( this ).dialog( "close" );
//					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		$('#dialog-defineReparte').bind('dialogclose', function(event) {
//			fixacaoReparteController.limparCamposModalReparteAoFechar();
		});
	},
	
	//funcao que obtem os valores de cada reparte e o codigo do pdv 
	getValoresReparte:function(idFixacao){
		var data = [];
		data.push({name:"repartes",	value: repartes.join(",")});
		data.push({name:"codigos",	value: codigos.join(",")});
		data.push({name:"idFixacao",	value: idFixacao});
		return data;
	},

	//funcao que executa a chamada que preenche a grid de repartes por pdv
	preencherGridPdv:function(params){
		$(".pdvCotaGrid").flexOptions({
			url: contextPath + "/distribuicao/fixacaoReparte/carregarGridPdv",
			dataType : 'json',
			params: params
		});

		$(".pdvCotaGrid").flexReload();
	},
	//funcao que limpa os dados que foram carregados no modal de reparte ao fechar a tela
	limparCamposModalReparteAoFechar:function(){
		$("#codigoCotaModalReparte").text("");
		$("#nomeCotaModalReparte").text("");
		$("#codigoProdutoModalReparte").text("");
		$("#nomeProdutoModalReparte").text("");
		$("#classificacaoModalReparte").text("");
		$(".pdvCotaGrid").html("");
	},

	//funcao de pre-processamento da grid de repartes por pdv, adicionando inputs de reparte para cada linha da grid
	preProcessarListaPdv:function(data){
		if (data.result){
			data.rows = data.result[1].rows;
		}
		var i;
		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell["reparte"]=fixacaoReparteController.getInputReparte(data.rows[i].cell);
		}
		//$('.excessaoCotaGrid').show();
		if (data.result){
			return data.result[1];
		}
		return data;

	},
	
	//funcao que retorna id da fixação a lista de fixacoes
	getIdSelecionado: function(idFixacao){
		var data = [];
		data.push({name:'filtro.idFixacao',	value: idFixacao});
		data.push({name:'filtro.codigoCota', value: $("#codigoCota").val()});
		return data;
	},

	//funcao que retorna input de reparte a grid de reparte por pdv
	getInputReparte:function(cell){
		return "<input type='text' class='reparteGridinput'  onkeydown='onlyNumeric(event);' maxlength='5' size='6'  name='"+cell.id+"' value=\'"+ (cell.reparte || 0)  +"\'/>";
		
	},
	//funcao de exibicao de grid
	escondeGridCota:function(){

		$('#fixacaoReparte_porCota').hide();
		$('#fixacaoReparte_fixacaoProduto').hide();
	},

	//funcao de exibicao de grid
	exibeGridProduto:function(){

		$('#fixacaoReparte_fixacaoProduto').show();
		$('#fixacaoReparte_porCota').hide();
	},

	//Funcao de pre-processamento da chamada postJSON que preenche a grid de historico
	preProcessHistoricoGrid:function(result){
		if(result.rows[0]){
			$("#edicaoDestaque").text(result.rows[0].cell.edicao);
			$("#statusDestaque").text(result.rows[0].cell.status);
			$("#historicoXLS").show();
			$("#historicoPDF").show();
		}else{
			$("#historicoXLS").hide();
			$("#historicoPDF").hide();
			$("#edicaoDestaque").text("");
			$("#statusDestaque").text("");
		}
		return result;
	},
	
	//Funcao que realiza pesquisa de fixações por produto
	pesquisarPorProduto:function(){
		
		if(fixacaoReparteController.validarFiltroPrincipal(codigoProduto, nomeProduto)){
			fixacaoReparteController.exibeGridProduto();
			$(".fixacaoProdutoGrid", fixacaoReparteController.workspace).flexOptions({
				url: contextPath + "/distribuicao/fixacaoReparte/pesquisarPorProduto",
				dataType : 'json',
				params: fixacaoReparteController.getDadosProduto()
			});
	
			$(".fixacaoProdutoGrid", fixacaoReparteController.workspace).flexReload();
		}else{
			$('#fixacaoReparte_fixacaoProduto').hide();
			exibirMensagem("WARNING", ["Por favor preencha os campos necessários!"]);
		}
	},
	
	//Funcao que realiza pesquisa de fixações por cota
	pesquisarPorCota:function(){
		
		if(fixacaoReparteController.validarFiltroPrincipal(codigoCota, nomeCota)){

//			fixacaoReparteController.escondeGridCota();
			// fazer exibir grid cota
			
			$('#fixacaoReparte_porCota').show();
			
			$(".fixacaoCotaGrid", fixacaoReparteController.workspace).flexOptions({
				url: contextPath + "/distribuicao/fixacaoReparte/pesquisarPorCota",
				dataType : 'json',
				params: fixacaoReparteController.getDadosCota()
			});
			
			$(".fixacaoCotaGrid", fixacaoReparteController.workspace).flexReload();
			
		}else{
			fixacaoReparteController.escondeGridCota();
			exibirMensagem("WARNING", ["Por favor preencha os campos necessários!"]);
		}
				
	},

		get : function(campo) {

			var elemento = $("#" + campo, fixacaoReparteController.workspace);

			if(elemento.attr('type') == 'checkbox') {
				return (elemento.attr('checked') == 'checked') ;
			} else {
				return elemento.val();
			}


		},
		//Getter dos parametros necessarios para pesquisa de fixação  por produto
		getDadosProduto : function() {

			var data = [];
			data.push({name:'filtro.codigoProduto',	value: $("#codigoProduto", fixacaoReparteController.wsp).val()});
			data.push({name:'filtro.nomeProduto',	value: $("#nomeProduto", fixacaoReparteController.wsp).val()});
			data.push({name:'filtro.classificacaoProduto',	value: $("#filtroClassificacaoFixacao option:selected", fixacaoReparteController.wsp).val()});

			return data;
		},
		
		//Getter dos parametros necessarios para pesquisa de fixação  por cota
		getDadosCota : function() {

			var data = [];
			data.push({name:'filtro.cota',	value: $("#codigoCota").val()});
			data.push({name:'filtro.nomeCota',	value: $("#nomeCota").val()});

			return data;
		},

		//Getter dos parametros necessarios para pesquisa que preenche a grid de historico por cota
		getDadosProdutoHistorico : function() {

			var data = [];
			data.push({name:'filtro.codigoProduto',value: $("#codigoModal").val()});
			data.push({name:'filtro.nomeCota',	value: $("#spanNomeProduto").text()});
			data.push({name:'filtro.cota',	value: $("#spanCodigoProduto").text()});

			return data;
		},

		//Getter dos parametros necessarios para pesquisa que preenche a grid de historico por produto
		getDadosCotaHistorico : function() {

			var data = [];
			data.push({name:'filtro.cota',value: $("#codigoModal").val()});
			data.push({name:'filtro.nomeProduto',	value: $("#spanNomeProduto").text()});
			data.push({name:'filtro.codigoProduto',	value: $("#spanCodigoProduto").text()});

			return data;
		},
		//Fornece dados necessarios para adicionar nova fixacao
		getDadosAdicionarFixacao : function() {

			var data = [];
			if($("#subtitulo1").text() == 'Produto'){
				data.push({name:'fixacaoReparteDTO.cotaFixada',value: $("#codigoModal").val()});
				data.push({name:'fixacaoReparteDTO.produtoFixado',value: $("#spanCodigoProduto").text()});
			}

			if($("#subtitulo1").text() == 'Cota'){
				data.push({name:'fixacaoReparteDTO.cotaFixada',value: $("#spanCodigoProduto").text()});
				data.push({name:'fixacaoReparteDTO.produtoFixado',value: $("#codigoModal").val()});
			}
		
			data.push({name:'fixacaoReparteDTO.edicao', 		value: $('#edicaoDestaque').text()});
			data.push({name:'fixacaoReparteDTO.qtdeEdicoes',	value: $("#qtdeEdicoesModal").val()});
			data.push({name:'fixacaoReparteDTO.qtdeExemplares',	value: $("#qtdeFixadaModal").val()});
			data.push({name:'fixacaoReparteDTO.edicaoInicial',	value: $("#edInicialModal").val()});
			data.push({name:'fixacaoReparteDTO.edicaoFinal',	value: $("#edFinalModal").val()});
			data.push({name:'fixacaoReparteDTO.qtdeEdicoesMarcado',	value: $("#radioQtdeEdicoes").is(":checked")});
            data.push({name:'fixacaoReparteDTO.classificacaoProduto',	value: $("#filtroClassificacaoFixacao option:selected", fixacaoReparteController.wsp).val()});

			return data;
		},
		//Abre modal Nova Fixação
		novo:function () {

			if($("input:radio:checked").val() == 'Produto'){

				if(($("#codigoProduto").val()=='' || $("#codigoProduto").val() =='undefined' )){
					exibirMensagem("WARNING", ["Por favor preencha o campo codigo "]);
					return false;
				}else{
					fixacaoReparteController.exibeCodigoNomeProdutoSelecionado();
				}
			}
			
			if($("input:radio:checked").val() == 'Cota'){

				if(($("#codigoCota").val()=='' || $("#codigoCota").val() =='undefined' )){
					exibirMensagem("WARNING", ["Por favor preencha o campo codigo"]);
					return false;
					}else{
						fixacaoReparteController.exibeCodigoNomeCotaSelecionado();
					}
			}
			 $("#dialog-novo").dialog({
					resizable: false,
					height:550,
					width:810,
					modal: true,
					buttons: {
						"Confirmar": function() {
							if(fixacaoReparteController.validaCamposVaziosNovoFixacao()){
								$.postJSON(contextPath + '/distribuicao/fixacaoReparte/adicionarFixacaoReparte', fixacaoReparteController.getDadosAdicionarFixacao(),
										fixacaoReparteController.executarSuccessCallBack,fixacaoReparteController.executarErrorCallBack);
							}
						},
						"Cancelar": function() {
							$(this,fixacaoReparteController.workspace).dialog('close');
						},
					}
			});
			 $('#dialog-novo').bind('dialogclose', function(event) {
				 fixacaoReparteController.limparCamposAoFechar();
			 });


		},
		//Executada em caso de sucesso durante tentativa de chamada postJSON para adicionar fixação
		executarSuccessCallBack:function(result){
			if($('#selectModal').css('display')=='inline-block'){
				$(".fixacaoCotaGrid",fixacaoReparteController.workspace).flexReload();
				fixacaoReparteController.limparCamposModalNovo();
				$("#dialog-novo").dialog('close');
			}else if($('#selectModal').css('display')=='none'){
				$(".fixacaoProdutoGrid",fixacaoReparteController.workspace).flexReload();
				fixacaoReparteController.limparCamposModalNovo();
				$("#dialog-novo").dialog('close');
			}


		},
		
		//Limpa os campos preenchidos durante a fixação, apos finalizada a adição de fixação
		limparCamposModalNovo:function(){
			$("#qtdeEdicoesModal").val("");
			$("#qtdeFixadaModal").val("");
			$('#edInicialModal').val("");
			$('#edFinalModal').val("");
			$(".historicoGrid").html("");
		},

		//Executada em caso de erro durante tentativa de chamada postJSON para adicionar fixação
		executarErrorCallBack:function(result){
			if(result.mensagens.listaMensagens[0] === "Operação realizada com sucesso!<br>Status da Cota: Suspenso") {
				fixacaoReparteController.executarSuccessCallBack(result);
			}
		},
		
		// Função que valida campos obrigatorios no modal  de nova fixação
		validaCamposVaziosNovoFixacao:function(){	
			if($("#codigoModal").val() =="" || $("#nomeModal").val() ==""){
				exibirMensagem("WARNING", ["Produto/nome não informado "]);
				return false;
			}

			if((!$('#radioIntervalo').is(":checked")) && !$('#radioQtdeEdicoes').is(":checked")){
				exibirMensagem("WARNING", ["informe um dos dois "]);

			}

			if($('#radioQtdeEdicoes').is(":checked")){
				if($("#qtdeEdicoesModal").val()==''){
					exibirMensagem("WARNING", ["Qtde. edições não informado "]);
					return false;
			}

			}
				if($('#radioIntervalo').is(":checked")){
					if($("#edInicialModal").val()=='' || $("#edIniciaFinal").val()==''){
						exibirMensagem("WARNING", ["Informe Ed. inicial / final "]);
						return false;
					}
			}

				return true;	

		},
			 
		//Ao abrir o modal novo carrega e exibe dados do produto selecionado para fixação	 
		exibeCodigoNomeProdutoSelecionado:function() {
			$('#subtitulo1').text('Produto');
			$('#subtitulo2').text('Pesquisar cota');
			$('#label1').text('Cota');
			$('#label2').text('Nome');
			$('#selectModal').hide();
			$('#codigoModal').attr('onchange', 'fixacaoReparteController.autoCompleteNumeroCota("#codigoModal","#nomeModal")');
			$('#pesquisaModal').attr('onClick','fixacaoReparteController.pesquisaHistoricoPorCota();');
			$('#spanCodigoProduto').text($('#codigoProduto').val());
			$('#spanNomeProduto').text($('#nomeProduto').val());

		},
		//Ao abrir o modal novo carrega e exibe dados da cota selecionada para fixação
		exibeCodigoNomeCotaSelecionado:function() {
			$('#subtitulo1').text('Cota');
			$('#subtitulo2').text('Pesquisar Produto');
			$('#label1').text('Produto');
			$('#label2').text('Nome ');
			$('#selectModal').show();
			$('#codigoModal').attr('onchange','pesquisaProduto.pesquisarPorCodigoProduto("#codigoModal","#nomeModal",false,undefined,undefined )');
			$('#pesquisaModal').attr('onClick','fixacaoReparteController.pesquisaHistoricoPorProduto();');
			$('#spanCodigoProduto').text($('#codigoCota').val());
			$('#spanNomeProduto').text($('#nomeCota').val());

		},
		
		// Função utilizada para limpar os fields que são preenchidos ao abrir o modal
		limparCamposAoFechar:function(){
			$("#edicaoDestaque").text("");
			$("#statusDestaque").text("");
			$("#codigoModal").val("");
			$("#nomeModal").val("");
			$('#spanCodigoProduto').text("");
			$('#spanNomeProduto').text("");
			$(".historicoGrid").html("");
			$('#qtdeFixadaModal').val("");
			$('#qtdeEdicoesModal').val("");
			$('#edInicialModal').val("");
			$('#edFinalModal').val("");

		},
		
		//Função que realiza a pesquisa que preenche os dados da grid historico produto
		pesquisaHistoricoPorProduto:function(){
			
			if(fixacaoReparteController.validarFiltroPrincipal(codigoModal, nomeModal)){

				$(".historicoGrid").flexOptions({
					url: contextPath + "/distribuicao/fixacaoReparte/carregarGridHistoricoProduto",
					dataType : 'json',
					params: fixacaoReparteController.getDadosProdutoHistorico()
				});

				$(".historicoGrid").flexReload();
				
			}else{
				exibirMensagem("WARNING", ["Por favor preencha os campos necessários!"]);
			}
			
		},

		//Função que realiza a pesquisa que preenche os dados da grid historico produto
		pesquisaHistoricoPorCota:function(){
			
			$(".historicoGrid").show();
			
			if(fixacaoReparteController.validarFiltroPrincipal(codigoModal, nomeModal)){
				
				$.postJSON(contextPath + '/distribuicao/fixacaoReparte/validarTipoCota', {numeroCota:$(codigoModal).val()},
						function(result){

							$(".historicoGrid").flexOptions({
								url: contextPath + "/distribuicao/fixacaoReparte/carregarGridHistoricoCota",
								dataType : 'json',
								params: fixacaoReparteController.getDadosCotaHistorico()
							});
							$(".historicoGrid").flexReload();
							
						},
						function(result){

							$('#codigoModal').val('');
							$('#nomeModal').val('');
							$("#edicaoDestaque").text("");
							$("#statusDestaque").text("");
							
							$(".historicoGrid").hide();
						});
				
			}else{
				exibirMensagem("WARNING", ["Por favor preencha os campos necessários!"]);
			}
			

		},
		
		// click do botao adicionar em lote		
		add_lote : function() {
			$("#modalUploadArquivo").dialog({
				resizable: false,
				height:'auto',
				width:400,
				modal: true,
				buttons: {
					"Confirmar": function() {
						fixacaoReparteController.executarSubmitArquivo();
					},
					"Cancelar": function() {
						$("#excelFileFixacao").val("");
						$(this).dialog("close");
					}
				},
			});
		},
		
		
		
		executarSubmitArquivo:function(){
			 var fileName = $("#excelFileFixacao").val();
		      
		       var ext = fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase();
		       if(ext!="xls" & ext!="xlsx"){
		    	   exibirMensagem("WARNING", ["Somente arquivos com extensão .XLS ou .XLSX são permitidos."]);
		    	   $(this).val('');
		    	   return;
		       }else{
		    	   
		    	   $("#formUploadLoteFixacao").ajaxSubmit({
		     		   
		     		      success: function(responseText, statusText, xhr, $form)  { 
		     		    	  
		     		    	  var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
		     		          var tipoMensagem = mensagens.tipoMensagem;
		     		          var listaMensagens = mensagens.listaMensagens;

		     		          if (tipoMensagem && listaMensagens) {
		     		           
		     		           if (tipoMensagem != 'SUCCESS') {
		     		            
		     		            exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialog-msg-upload');
		     		           }
		     		           $("#dialog-lote").dialog( "close" );
		     		           
		     		           exibirMensagem(tipoMensagem, listaMensagens); 
		     		          }
		     		      }, 
		     		      type: 'POST',
		     		      dataType: 'json',

		     		   
		     	   });
		    	   
		       }
		},
		
		abrirCopiaDialog:function(){
			var type  =$("input[type=radio][name=filtroPrincipalRadio]:checked").val();
			type = type.toLowerCase();
			var target = type+'CopiaFixacao-dialog';

			var idsCopiaCota = "#cotaFixacaoOrigemInput,#nomeCotaFixacaoOrigemInput,#cotaFixacaoDestinoInput,#nomeCotaFixacaoDestinoInput";
			var idsCopiaProduto = "#codigoProdutoFixacaoOrigemInput,#nomeProdutoFixacaoOrigemInput,#codigoProdutoFixacaoDestinoInput,#nomeProdutoFixacaoDestinoInput";
			
			$("#"+target).dialog({
				resizable: false,
				height:'auto',
				width:500,
				draggable: false,
				modal: true,
				buttons: {
					"Iníciar cópia": function() {

						var len=0;
						
						if(type=='cota') {
							len = $(idsCopiaCota).filter(function(){return this.value == "";}).length;
						}else if(type=='produto'){
							len = $(idsCopiaProduto).filter(function(){return this.value == "";}).length;
							
						}

						if(len>0){
							exibirMensagem("WARNING",["Dados para cópia não preenchidos."]);
							return;
						}
						
						var msgWar=null;
						if(type=='cota' &&  ( $("#cotaFixacaoOrigemInput").val()==$("#cotaFixacaoDestinoInput").val())){
							msgWar="Cota Origem não pode ser igual a cota destino.";
						}else if(type=='produto' &&  ( $("#codigoProdutoFixacaoOrigemInput").val()==$("#codigoProdutoFixacaoDestinoInput").val() )){
							msgWar="Produto Origem não pode ser igual a produto destino.";
						}
						
						if(msgWar!=null){
							exibirMensagem("WARNING",[msgWar]);
							return;
						}
						
						var data = [];
//						console.log(type);
						data.push({name:"copiaDTO.tipoCopia",	value: type.toUpperCase()});
						data.push({name:"copiaDTO.cotaNumeroOrigem",	value: $("#cotaFixacaoOrigemInput").val()});
						data.push({name:"copiaDTO.nomeCotaOrigem",	value: $("#nomeCotaFixacaoOrigemInput").val()});
						data.push({name:"copiaDTO.cotaNumeroDestino",	value: $("#cotaFixacaoDestinoInput").val()});
						data.push({name:"copiaDTO.nomeCotaDestino",	value: $("#nomeCotaFixacaoDestinoInput").val()});
						                 
						data.push({name:"copiaDTO.codigoProdutoOrigem",	value: $("#codigoProdutoFixacaoOrigemInput").val()});
						data.push({name:"copiaDTO.nomeProdutoOrigem",	value: $("#nomeProdutoFixacaoOrigemInput").val()});
						data.push({name:"copiaDTO.codigoProdutoDestino",	value: $("#codigoProdutoFixacaoDestinoInput").val()});
						data.push({name:"copiaDTO.nomeProdutoDestino",	value: $("#nomeProdutoFixacaoDestinoInput").val()});
						
						modal = this;
						$.postJSON(contextPath + '/distribuicao/fixacaoReparte/gerarCopiaFixacao',  data, 
								function(result){
								console.log(result);
								$(modal).dialog("close");
										exibirMensagem("WARNING",result.listaMensagens);
								},
								function(result){ });
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				},
				close:function(){$(idsCopiaCota+","+idsCopiaProduto).val('');}
			});
			
			
		},
		
		autoCompleteNumeroCota:function(codigoCota, nomeCota){
			
			if($(codigoCota).val() != ""){
				
				pesquisaCota.pesquisarPorNumeroCota(codigoCota,nomeCota,false,

				function(result){
					$.postJSON(contextPath + '/distribuicao/fixacaoReparte/validarTipoCota', {numeroCota:$(codigoCota).val()},
							function(result){
						
					},
					function(result){
						$('#codigoModal').val('');
						$('#nomeModal').val('');
						$("#edicaoDestaque").text("");
						$("#statusDestaque").text("");
						$(".historicoGrid").hide();
					});
				}		
				,undefined);

			}
		},
		
		autoCompleteNomeModal:function(){
			
			if($("input:radio:checked").val() == 'Produto'){
				this.autoCompletarPorNome("/cadastro/cota/autoCompletarPorNome",'#codigoModal', '#nomeModal', "nomeCota", 3);
			}
			
			if($("input:radio:checked").val() == 'Cota'){
				this.autoCompletarPorNome("/produto/autoCompletarPorNomeProdutoAutoComplete",'#codigoModal', '#nomeModal', "nome", 3);
			}
		},
		
		autoCompletarPorNome:function(action, idCampoCodigo, idCampoNome, parametroPesquisa, disparoDaPesquisa, isFromModal) {
			
			var	localParametroPesquisa = parametroPesquisa;
			
			if(!disparoDaPesquisa){
				disparoDaPesquisa = 2;
			}
			
			if(!parametroPesquisa){
				localParametroPesquisa = nomeProduto;
			}
			
			var nome = $(idCampoNome).val();
			
			nome = $.trim(nome);
			
			$(idCampoNome).autocomplete({source: [""]});
			
			if (nome && nome.length > disparoDaPesquisa) {
				
				$.postJSON(
					contextPath + action, [{name : localParametroPesquisa, value : nome}],
					function(result) { 
						fixacaoReparteController.exibirAutoCompletePorNome(result, idCampoCodigo, idCampoNome);
					},
					null, 
					isFromModal
				);
			}
	},
	
	//Exibe o auto complete no campo
	exibirAutoCompletePorNome : function(result, idCampoCodigo, idCampoNome) {
	
			$(idCampoNome).autocomplete({
				source: result,
				
				select : function(event, ui) {
					$(idCampoCodigo).val(ui.item.chave.numero);
					$(idCampoNome).val(ui.item.chave.label);
				},
				focus : function(event, ui) {
					$(idCampoCodigo).val(ui.item.chave.numero);
					$(idCampoNome).val(ui.item.chave.label);
				},
				close : function(event, ui) {
					$(idCampoCodigo).val(ui.item.chave.numero);
					$(idCampoNome).val(ui.item.chave.label);
				},
				minLength: this.tamanhoInicial,
				delay : 0,
			});
	},
	
	validarFiltroPrincipal : function(codigo, nome){

		if((($(codigo).val()) == 'undefined') || (($(codigo).val()) == '')){
			return false;
		}else{
			if((($(nome).val()) == 'undefined') || (($(nome).val()) == '')){
				return false;
			}else{
				return true;
			}
		}
		
	},
	
	}, BaseController);
//@ sourceURL=fixacaoReparte.js
