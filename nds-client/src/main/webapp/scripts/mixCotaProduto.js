var mixCotaProdutoController = $.extend(true, {
//Grid de cota 	
	
	init : function() {
		
		
		$(".mixCotasGrid").flexigrid({
			preProcess: function(data){return mixCotaProdutoController.executarPreprocessamentoGridCota(data);},
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoICD',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 115,
				sortable : true,
				align : 'left'
			},  {
				display : 'Classificação',
				name : 'classificacaoProduto',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Rep. Médio',
				name : 'reparteMedio',
				width : 60,
				sortable : false,
				align : 'right'
			},  {
				display : 'Vda. Média',
				name : 'vendaMedia',
				width : 60,
				sortable : false,
				align : 'right'
			},  {
				display : 'Ult. Rep.',
				name : 'ultimoReparte',
				width : 60,
				sortable : false,
				align : 'right'
			},  {
				display : 'Rep. Inf. Min.',
				name : 'reparteMinimoInput',
				width : 70,
				sortable : false,
				align : 'right'
			},  {
				display : 'Rep. Inf. Max.',
				name : 'reparteMaximoInput',
				width : 70,
				sortable : false,
				align : 'right'
			},  {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Dt. Manut.',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Hora',
				name : 'hora',
				width : 40,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigoICD",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		//grid de produto
		$(".mixProdutosGrid").flexigrid({
			preProcess: function(data){return mixCotaProdutoController.executarPreprocessamentoGridProduto(data);},
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 125,
				sortable : true,
				align : 'left'
			},  {
				display : 'Rep. Médio',
				name : 'reparteMedio',
				width : 70,
				sortable : false,
				align : 'right'
			},  {
				display : 'Vda. Média',
				name : 'vendaMedia',
				width : 70,
				sortable : false,
				align : 'right'
			},  {
				display : 'Ult. Rep.',
				name : 'ultimoReparte',
				width : 70,
				sortable : false,
				align : 'right'
			},  {
				display : 'Rep. Inf. Min.',
				name : 'reparteMinimoInput',
				width : 80,
				sortable : false,
				align : 'right'
			},  {
				display : 'Rep. Inf. Max.',
				name : 'reparteMaximoInput',
				width : 80,
				sortable : false,
				align : 'right'
			},  {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Dt. Manut.',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		//Grid de repartes por pdv
		$(".MX_pdvCotaGrid").flexigrid({
			preProcess: function(data){return mixCotaProdutoController.preProcessarListaPdv(data);},
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
		
		$('.porProduto').hide();
		$('.porCota').hide();
		$("#reparteOriginal").hide();
		
		$('#nomeCotaMix').keyup(function (){
			autoComplete.autoCompletarPorNome("/cadastro/cota/autoCompletarPorNome",'#codigoCotaMix', '#nomeCotaMix', "nomeCota", 3);
		});
		
		$('#nomeProdutoMix').keyup(function (){
			autoComplete.autoCompletarPorNome("/produto/autoCompletarPorNomeProdutoAutoComplete",'#codigoProdutoMix', '#nomeProdutoMix', "nome", 3);
		});
		
	},
		
		
	//funcao de pre-processamento do resultado da busca de fixacao por produto
	//, exibindo/escondendo botoes de geracao de excel/pdf dependendo do resultado da pesquisa
	executarPreprocessamentoGridCota:function(data){
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			$("#spanLegendCota").text(""); //limpa o titulo produto, evitando permanecer valores digitados em pesquisa anterior
			$("#btNovoMixCota").show();
//			$("#btAddLoteMixCota").hide();
			$("#btGerarArquivoMixCota").hide();
			$("#btImprimirMixCota").hide();
			$("#btExcluirTudoCota").hide();
			return data;
		}else{
			$("#spanLegendCota").text($("#codigoCotaMix").val() + "-" +$("#nomeCotaMix").val());// preenche fieldset com os valores digitados no fitro
			$("#btNovoMixCota").show();
//			$("#btAddLoteMixCota").show();
			$("#btGerarArquivoMixCota").show();
			$("#btImprimirMixCota").show();
			$("#btExcluirTudoCota").show();
		}
		
		if (data.result){
			data.rows = data.result[1].rows;
		}
		
		var i;
		
		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;
			
			data.rows[i].cell["acao"]=mixCotaProdutoController.getActionsGridCota(data.rows[i].cell);
			data.rows[i].cell["reparteMinimoInput"]="<input type='text' id='updInputRepMin_"+data.rows[i].cell["id"]+"' onkeydown='onlyNumeric(event);' size='7' value='"+data.rows[i].cell["reparteMinimo"]+"' onchange='mixCotaProdutoController.updateReparteMinMax(this,\"MIN\","+data.rows[i].cell["id"]+","+data.rows[i].cell["reparteMinimo"]+", "+ (data.rows[i].cell["somaPdv"] > 0) +")' />";
			data.rows[i].cell["reparteMaximoInput"]="<input type='text' id='updInputRepMax_"+data.rows[i].cell["id"]+"' onkeydown='onlyNumeric(event);' size='7' value='"+data.rows[i].cell["reparteMaximo"]+"' onchange='mixCotaProdutoController.updateReparteMinMax(this,\"MAX\","+data.rows[i].cell["id"]+","+data.rows[i].cell["reparteMaximo"]+", "+ (data.rows[i].cell["somaPdv"] > 0) +")' />";
				
				
			//arrumar formatacao campos bigdecimal para duas casas decimais
			var ar = ["reparteMedio","vendaMedia","ultimoReparte"];
			for ( var int = 0; int < ar.length; int++) {
				var valorarrumado = parseFloat(data.rows[i].cell[ar[int]]).toFixed(0);
				data.rows[i].cell[ar[int]]=valorarrumado;
			}
		}
		
		$('.mixCotasGrid').show();
		if (data.result){
			return data.result[1];
		}
		return data;
	},	
	
	updateReparteMinMax:function(input, tipoCampo, idMix, lastValue, exibirDialogConfReparte){
		
			
		var listaNovoReparte = new Array();
		 
		if(input.value.trim().length==0 ){

			listaNovoReparte.push({
				  name : "novoValorReparte" , 
				  value : lastValue
				  });
			
			return;
			
		 }else{
			 
			 listaNovoReparte.push({
				 name : "novoValorReparte" , 
				 value : input.value
			 });
		 }
		 
		 listaNovoReparte.push({
			  name : "tipoCampo" , 
			  value : tipoCampo
			  });
		 
		 listaNovoReparte.push({
			  name : "idMix" , 
			  value : idMix
			  });
		 
		 console.log("id "+$(input).attr('id'));
		 
		 if($(input).attr('id').indexOf('updInputRepMin_')  > -1 || $(input).attr('id').indexOf('updInputRepMax_')  > -1) {

			 strIdMix = '';
			 if($(input).attr('id').indexOf('updInputRepMin_')  > -1) {
				 
				 strIdMix = $(input).attr('id').split('_')[1];
			 } else if($(input).attr('id').indexOf('updInputRepMax_')  > -1) {
				 
				 strIdMix = $(input).attr('id').split('_')[1];
			 }
			 
		 }

		 if($('#editar_'+ strIdMix).attr('id') == undefined) {
			 
			 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/updateReparteMixCotaProduto', listaNovoReparte  , function(){
					
					if($("#radio").attr('checked') == 'checked'){
						$(".mixCotasGrid").flexReload();
					}
					else{
						$(".mixProdutosGrid").flexReload();
					}
					
				}, function(){
					input.value=lastValue;
			});
			 
			 return;
		 }
		 
		 if(exibirDialogConfReparte){
			 $("#dialog-confirma-alteracao-reparte").dialog({
					resizable: false,
					height:'auto',
					width:300,
					modal: true,
					buttons: {
						"Confirmar": function() {

							$.postJSON(contextPath + '/distribuicao/mixCotaProduto/updateReparteMixCotaProduto', listaNovoReparte  , function(){
								
								if($("#radio").attr('checked') == 'checked'){
									$(".mixCotasGrid").flexReload();
								}
								else{
									$(".mixProdutosGrid").flexReload();
								}
								
							}, function(){
								input.value=lastValue;
							});
							
							$(this).dialog("close");
						},
						"Cancelar": function() {
							input.value=lastValue;
							$(this).dialog("close");
						}
					},
			});
			 
		 } else {

			 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/updateReparteMixCotaProduto', listaNovoReparte  , function(){
					
				if($("#radio").attr('checked') == 'checked'){
					$(".mixCotasGrid").flexReload();
				}
				else{
					$(".mixProdutosGrid").flexReload();
				}
			
			});
		 }
		 
		 
	},
	//retorna acoes de edicao e exclusao de fixacao por cota
	getActionsGridCota: function (cell){
		
		components="";
		
		imgAlteracao =  '<a href="javascript:;" id="editar_'+cell.id+'" onclick="mixCotaProdutoController.editarRepartePorPdv(' + cell.id + ',' + cell.reparteMinimo + ','+cell.reparteMaximo + ','+cell.idCota+', \'' + cell.codigoICD + '\')" ' +
		' style="cursor:pointer;border:0px;margin:5px" title="Reparte por PDV ">' +
		'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
		'</a>' ;
		
		imgExcluir = '<a href="javascript:;" onclick="mixCotaProdutoController.excluirMixCota(' + cell.id + ')" '  +
		' style="cursor:pointer;border:0px;margin:5px" title="Excluir Mix">' +
		'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
		'</a>';
		
		//Checa quantidade de pdvs. Caso seja maior que 1 o retorna botoes editar/excluir senao apenas o botao excluir
		if (cell.qtdPdv > 1) {
			components = imgAlteracao;
		}
			components += imgExcluir;
		
		return  components;
				
	},
	
	
	//exibe msg de confirmacao de fixação cota
	excluirMixCota:function (idMix){
		$("#dialog-excluir").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(contextPath + '/distribuicao/mixCotaProduto/removerMixCotaProduto', mixCotaProdutoController.getIdExcluir(idMix) , mixCotaProdutoController.exclusaoMixCotaSucesso, mixCotaProdutoController.exclusaoMixCotaErro);
					$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
	},
	
	//retorna msg de sucesso durante exclusao de mix por cota
	exclusaoMixCotaSucesso:function(result){
		if($(".mixCotasGrid").is(":visible")){
			$(".mixCotasGrid").flexReload();
		}
		else if($(".mixProdutosGrid").is(":visible")){
			$(".mixProdutosGrid").flexReload();
		}
		
	},
	
	//retorna msg de erro durante exclusao de mix por cota
	exclusaoMixCotaErro:function(result){
		exibirMensagem("ERROR", ["Houve um erro durante a exclusão "]);
	},
	
	
	//retorna o id da fixacao. Utilizada para exclusao de fixacao
	getIdExcluir : function(idMix) {
		
		var data = [];
		data.push({name:'filtro.id',	value: idMix});
		
		return data;
	},
	
	
	
	//funcao de pre-processamento do resultado da busca de mix por produto
	//, exibindo/escondendo botoes de geracao de excel/pdf dependendo do resultado da pesquisa
	executarPreprocessamentoGridProduto:function(data){
		// msg erro
		if (data.mensagens) {
			
			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$("#spanLegendProduto").text(""); //limpa o titulo produto, evitando permanecer valores digitados em pesquisa anterior
			$("#btNovoMixProduto").show();
//			$("#btAddLoteMixProduto").show();
			$("#btGerarArquivoMixProduto").hide();
			$("#btImprimirMixProduto").hide();
			$("#btExcluirTudoProduto").hide();
			return data;
		}else{
			// pesquisa achou resultado
			$("#spanLegendProduto").text($("#codigoProdutoMix").val() + "-" +$("#nomeProdutoMix").val());// preenche fieldset com os valores digitados no fitro
			$("#btNovoMixProduto").show();
// 			$("#btAddLoteMixProduto").show();
			$("#btGerarArquivoMixProduto").show();
			$("#btImprimirMixProduto").show();
			$("#btExcluirTudoProduto").show();
		}
		
		if (data.result){
			data.rows = data.result[1].rows;
		}
		
		var i;
		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;
			
			data.rows[i].cell["acao"]=mixCotaProdutoController.getActionsGridProduto(data.rows[i].cell);
			data.rows[i].cell["reparteMinimoInput"]="<input type='text' id='updInputRepMin_"+data.rows[i].cell["id"]+"' onkeydown='onlyNumeric(event);' size='7' value='"+data.rows[i].cell["reparteMinimo"]+"' onchange='mixCotaProdutoController.updateReparteMinMax(this,\"MIN\","+data.rows[i].cell["id"]+","+data.rows[i].cell["reparteMinimo"]+", "+ (data.rows[i].cell["somaPdv"] > 0) +")' />";
			data.rows[i].cell["reparteMaximoInput"]="<input type='text' id='updInputRepMax_"+data.rows[i].cell["id"]+"' onkeydown='onlyNumeric(event);' size='7' value='"+data.rows[i].cell["reparteMaximo"]+"' onchange='mixCotaProdutoController.updateReparteMinMax(this,\"MAX\","+data.rows[i].cell["id"]+","+data.rows[i].cell["reparteMaximo"]+", "+ (data.rows[i].cell["somaPdv"] > 0) +")' />";
		}
		
		$('.mixProdutosGrid').show();
		if (data.result){
			return data.result[1];
		}
		return data;
	},
	
	
	
	//retorna acoes de edicao e exclusao de fixacao por cota
	getActionsGridProduto: function (cell){
	components="";
		
		imgAlteracao =  '<a href="javascript:;" id="editar_'+cell.id+'" onclick="mixCotaProdutoController.editarRepartePorPdv(' + cell.id + ','+cell.reparteMinimo + ',' + cell.reparteMaximo + ','+cell.idCota+', \'' + cell.codigoProduto + '\')" ' +
		' style="cursor:pointer;border:0px;margin:5px" title="Reparte por PDV ">' +
		'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
		'</a>' ;
		
		imgExcluir = '<a href="javascript:;" onclick="mixCotaProdutoController.excluirMixCota(' + cell.id + ')" '  +
		' style="cursor:pointer;border:0px;margin:5px" title="Excluir Mix">' +
		'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
		'</a>';
		
		//Checa quantidade de pdvs. Caso seja maior que 1 o retorna botoes editar/excluir senao apenas o botao excluir
		if (cell.qtdPdv > 1) {
			components = imgAlteracao;
		}
			components += imgExcluir;
		
		return  components;
	},
	
	excluirTodosPorCota: function() {
		$("#dialog-excluirTodos").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					params = [];
					params.push({'name': 'numeroCota', 'value': $('#codigoCotaMix').val()});
					$.postJSON(contextPath + '/distribuicao/mixCotaProduto/excluirTodosPorCota'
							, params
							, mixCotaProdutoController.exclusaoTodosSucesso
							, mixCotaProdutoController.exclusaoMixProdutoaErro);
					
					$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
	},
	
	excluirTodosPorProduto: function() {
		$("#dialog-excluirTodos").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					params = [];
					params.push({'name': 'codigoICD', 'value': $('#codigoProdutoMix').val()});
					$.postJSON(contextPath + '/distribuicao/mixCotaProduto/excluirTodosPorProduto'
							, params
							, mixCotaProdutoController.exclusaoTodosSucesso
							, mixCotaProdutoController.exclusaoMixProdutoaErro);
					
					$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
	},
	
	//retorna msg de sucesso durante exclusao Todos
	exclusaoTodosSucesso:function(result){
		if($("#radio").attr('checked') == 'checked'){
			$(".mixCotasGrid").flexAddData();
		}else{
			$(".mixProdutosGrid").flexAddData();
		}
		exibirMensagem("SUCCESS", ["Operação Realizada com sucesso!"]);
	},
	
	//retorna msg de erro durante exclusao Todos
	exclusaoMixProdutoaErro:function(result){
		exibirMensagem("ERROR", ["Houve um erro durante a exclusão "]);
	},
	
	//exibe msg de confirmacao de fixação produto
	excluirMixProduto:function (idMix){
		$("#dialog-excluir").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(contextPath + '/distribuicao/mixCotaProduto/removerMixCotaProduto', mixCotaProdutoController.getIdExcluir(idMix) , mixCotaProdutoController.exclusaoMixProdutoSucesso, mixCotaProdutoController.exclusaoMixProdutoErro);
					
					$(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
	},
	
	
	//retorna msg de sucesso durante exclusao de mix por produto
	exclusaoMixProdutoSucesso:function(result){
		$(".mixProdutosGrid").flexReload();
		
	},
	
	//retorna msg de erro durante exclusao de mix por produto
	exclusaoMixProdutoaErro:function(result){
		exibirMensagem("ERROR", ["Houve um erro durante a exclusão "]);
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
		$(".excessaoCotaGrid").flexReload();
		
	},
	
	//retorna msg de erro durante exclusao de cota
	exclusaoCotaErro:function(result){
		exibirMensagem("ERROR", ["Houve um erro durante a exclusão "]);
	},
	
	
	editarMixPorCota : function(){
		
		$("#dialog-editar-mix-cota").dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					var listaNovosMixCota=new Array();
					//tudo preenchido
					if(mixCotaProdutoController.validarInputs()){
					listaNovosMixCota.push({
								  name : "cotaId" , 
								  value : $("#codigoCotaMix").val()
								  });
							 
								var nomeCota = $("#nomeCotaMix").val();
								var nrCota = $("#codigoCotaMix").val();
								
							 $("#tableEdicaoMixCota tbody tr").each(function(idx, linha){
								 
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].numeroCota" , 
									  value : nrCota
								 });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].nomeCota" , 
									  value : nomeCota
								 });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].codigoProduto" , 
									  value : $("#codigoModal"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].nomeProduto" , 
									  value : $("#produtoModal"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].reparteMinimo" , 
									  value : $("#repMinimo"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].reparteMaximo" , 
									  value : $("#repMaximo"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].classificacaoProduto" , 
									  value : $("#classifMixModal"+idx).val()
									  });
							 });
							 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/adicionarMixCota',listaNovosMixCota,function(result){ mixCotaProdutoController.adicionarMixCotaSucesso();},
									 function(result){ mixCotaProdutoController.adicionarMixCotaSucesso();});
							 $(this).dialog("close");
					}
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			}
		});
			
		
	},
	
	//Adiciona nova linha ao grid por Cota
	novoMixPorCota : function(){
		
		$("#dialog-incluir-mix-cota").dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			open:function(){
				
				$("#tableNovoCota tbody tr:gt(0)").remove();
				$("#tableNovoCota tbody tr:eq(0) input[type='text']").val('');
				$('#classifMixModal').val("NORMAL");
				mixCotaProdutoController.definirIdInput();
				
			},
			buttons: {
				"Confirmar": function() {
					var listaNovosMixCota=new Array();
					var qntItens=0;
					//tudo preenchido
					if(mixCotaProdutoController.validarInputs()){
					listaNovosMixCota.push({
								  name : "cotaId" , 
								  value : $("#codigoCotaMix").val()
								  });
					
							 $("#tableNovoCota tbody tr").each(function(idx, linha){
								 listaNovosMixCota.push({
									  "name" : "listaNovosMixCota["+idx+"].numeroCota" , 
									  "value" : $("#codigoCotaMix").val()
								 });
								 listaNovosMixCota.push({
									  "name" : "listaNovosMixCota["+idx+"].nomeCota" , 
									  "value" : $("#nomeCotaMix").val()
								 });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].codigoProduto" , 
									  value : $("#codigoModal"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].nomeProduto" , 
									  value : $("#produtoModal"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].reparteMinimo" , 
									  value : $("#repMinimo"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].reparteMaximo" , 
									  value : $("#repMaximo"+idx).val()
									  });
								 listaNovosMixCota.push({
									  name : "listaNovosMixCota["+idx+"].classificacaoProduto" , 
									  value : $("#classifMixModal"+idx).val()
									  });
								 qntItens ++;
							 });
							 
							 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/adicionarMixCota',listaNovosMixCota,function(result){ 
								 
								 mixCotaProdutoController.adicionarMixCotaSucesso();
								 },
									 function(result){ 

										 if(result.mensagens.listaMensagens.length != qntItens){
											 $(".mixCotasGrid").flexReload(); 
										 }
									 
								 });
							 $(this).dialog("close");
					}
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
		
	},
	
	validarInputs:function(){
		var a= new Array();
		var countLinhas = $("#tableNovoCota tbody tr").length;

		//valida codigo duplicado
		for ( var int = 0; int < countLinhas; int++) {
			if(a.indexOf($("#codigoModal"+int).val())>-1){
				exibirMensagem("WARNING", ["Código de produto já utilizado."]);
				return false;
			}else{
				a.push($("#codigoModal"+int).val());
			}
		}
		
		//Reparte Inicial < Final
		
		
		for ( var int2 = 0; int2 < countLinhas; int2++) {
			var repMin = parseInt($("#repMinimo"+int2).val());
			var repMax = parseInt($("#repMaximo"+int2).val());
			
			if(repMin>repMax){
				exibirMensagem("WARNING", ["Reparte Mínimo maior que Reparte Máximo para o produto."]);
				return false;
			}
		}
		
		
		return true;
	},
	
	adicionarMixCotaSucesso:function(result){
		$(".mixCotasGrid").flexReload();
		exibirMensagem("SUCCESS", ["Operação Realizada com sucesso!"]);
	},
	
	adicionarMixProdutoSucesso:function(result){
		$(".mixProdutosGrid").flexReload();
		exibirMensagem("SUCCESS", ["Operação Realizada com sucesso!"]);
	},
	
	
	//Adiciona nova linha ao grid por Produto
	novoMixPorProduto : function(){
		if($("#codigoProdutoMix").val()==''){exibirMensagem("WARNING", ["Pesquise um produto para o Mix."]); return; }
		if($("#filtroClassificacaoMix").val()==''){exibirMensagem("WARNING", ["Selecione uma classificação."]); return; }

		$("#dialog-incluir-mix-produto").dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			open:function(){

				$("#tableNovoProduto tbody tr:gt(0)").remove();
				$("#tableNovoProduto tbody tr:eq(0) input[type='text']").val('');
				mixCotaProdutoController.definirIdInputProduto();
			},
			buttons: {
				"Confirmar": function() {
					var list=new Array();
					
//						if($("#tableNovoCota tbody tr td input:text[value='']").length>0){
							//tudo preenchido
					
					list.push({
								  name : "produtoId" , 
								  value : $("#codigoProdutoMix").val()
								  });
							 
							 $("#tableNovoProduto tbody tr").each(function(idx, linha){
								 
								 	
//								 $(linha).find("input[type='text']").each(function(){
								 list.push({
									  name : "listaNovosMixProduto["+idx+"].codigoProduto" , 
									  value : $("#codigoProdutoMix").val()
								 });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].numeroCota" , 
										  value : $("#numeroCotaModal"+idx).val()
										  });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].nomeCota" , 
										  value : $("#cotaModal"+idx).val()
										  });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].reparteMinimo" , 
										  value : $("#repMinimoProduto"+idx).val()
										  });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].reparteMaximo" , 
										  value : $("#repMaximoProduto"+idx).val()
										  });
								 list.push({
									  name : "listaNovosMixProduto["+idx+"].classificacaoProduto" , 
									  value : $("#filtroClassificacaoMix").val()
									  });
//								 });
							 });
							 
							 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/adicionarMixProduto',list,function(result){ mixCotaProdutoController.adicionarMixProdutoSucesso();},
									 function(result){ /*mixCotaProdutoController.adicionarMixProdutoSucesso();*/});
							 $(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
		
	},
	
	excluirLinha:function(linha) {
		
		if (linha.parents('table').find('tr').size() > 2) {
			
			linha.parent().parent().remove();
			mixCotaProdutoController.definirIdInput();
		}
		else {
			
			$(linha).parents('tr').find("input:text").val('');
		}
	},
	
	addLinha:function(){
		if($("#tableNovoCota tbody tr td input:text[value='']").length > 0){
			exibirMensagem("WARNING", [" Verifique os campos em branco e preencha-os! "]);
		}else{
			var novaLinha=	$("#tableNovoCota");
			novaLinha = $("#tableNovoCota tr:last").clone();
			novaLinha.find("input").val("");
			novaLinha.insertAfter("#tableNovoCota tr:last");
			$("#tableNovoCota tr:last").find("input[type='image']").show();
			$("#excluirLinha0").show();
			this.definirIdInput();
		}
		
	},
	
	addLinhaProduto:function(){
		if($("#tableNovoProduto tbody tr td input:text[value='']").length > 0){
			exibirMensagem("WARNING", [" Verifique os campos em branco e preencha-os! "]);
		}else{
			var novaLinha=	$("#tableNovoProduto");
			novaLinha = $("#tableNovoProduto tr:last").clone();
			novaLinha.find("input").val("");
			novaLinha.insertAfter("#tableNovoProduto tr:last");
//			$("#tableNovoProduto tr:last").find("input[type='image']").show();
			this.definirIdInputProduto();
		}
		
	},
	
	definirIdInput:function(){
		
		$("#tableNovoCota tbody tr").each(function(idx,tr){
			
			$(tr).find("input:text:eq(0)").attr("id","codigoModal"+idx);
			$(tr).find("input:text:eq(1)").attr("id","produtoModal"+idx);
			$(tr).find("input:text:eq(2)").attr("id","repMinimo"+idx);
			$(tr).find("input:text:eq(3)").attr("id","repMaximo"+idx);
			$(tr).find("select:eq(0)").attr("id","classifMixModal"+idx);
			
		});
	},
	
	definirIdInputProduto:function(){
		
		$("#tableNovoProduto tbody tr").each(function(idx,tr){
			
			var inputList = $(tr).find("input:text");
			
			$(inputList[0]).attr("id","numeroCotaModal"+idx);
			$(inputList[1]).attr("id","cotaModal"+idx);
			$(inputList[2]).attr("id","repMinimoProduto"+idx);
			$(inputList[3]).attr("id","repMaximoProduto"+idx);
			
		});
	},
	
	//funcao que executa chamada postJSON que busca dados do mix 
	editarRepartePorPdv:function (id, reparteMinimo, reparteMaximo, idCota, idProduto){
	
		if (reparteMinimo !=  reparteMaximo) {
			
			exibirMensagem("WARNING", ["Operação não pode ser realizada. Reparte mínimo deve ser igual ao reparte máximo."]);
			return;
		}
		
		var reparteTotal= reparteMaximo;
		$("#reparteOriginal").text(reparteTotal).hide();
		//parametros da pesquisa de repartes por pdv 
		parametrosPesquisaReparte = [];
		parametrosPesquisaReparte.push({
			  name : "filtro.cotaId" , 
			  value : idCota
			  });
		parametrosPesquisaReparte.push({
			  name : "filtro.produtoId" , 
			  value : idProduto
			  });
		  
		parametrosPesquisaReparte.push({
			  name : "filtro.id" , 
			  value : id
			  });
		
		$.postJSON(contextPath + '/distribuicao/mixCotaProduto/editarRepartePorPdv', parametrosPesquisaReparte
		,		
		function(result){
			$("#MX_codigoCotaModalReparte").text(result.numeroCota);
			$("#MX_nomeCotaModalReparte").text(result.nomeCota);
			$("#MX_codigoProdutoModalReparte").text(result.codigoProduto);
			$("#MX_nomeProdutoModalReparte").text(result.nomeProduto);
			$("#MX_classificacaoModalReparte").text(result.classificacaoProduto);
//			$("#reparteOriginal").text(result.reparteMaximo).hide();
//			$(".MX_reparteGridinput:eq(0)").val(reparteTotal);
		});
		
		$( "#dialog-defineReparte" ).dialog({
			resizable: false,
			height:590,
			width:650,
			modal: true,
			open:	mixCotaProdutoController.preencherGridPdv(parametrosPesquisaReparte),		//Funcao que abre o modal de repartes por pdv
			
			buttons: {
				"Confirmar": function() {
					
					var somaReparte=0;
					listaPDV =[];
					
					  $("#MX_pdvCotaGrid .MX_reparteGridinput").each(function(idx, linha){
						  if(linha.name == 'undefined'){
							  codigos.push(0);
						  }else{
							  codigos.push( $(linha).val());
							  //adiciona a lista de parametros os valores diretamente ao dto definido no parametro do metodo da controller.
							  //Para lista, deve-se informar obrigatoriamente o indice do elemento
							  listaPDV.push({
								  name : "listPDV["+idx+"].codigoPdv", 
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
						  value : $("#MX_codigoProdutoModalReparte").text()
						  });
					  listaPDV.push({
						  name : "codCota" , 
						  value : $("#MX_codigoCotaModalReparte").text()
						  });
					  
					  listaPDV.push({
						  name : "idMix" , 
						  value : id
						  });
					  
					if(somaReparte > reparteTotal){
						//abre dialog de confirma��o de altera��o de reparte maximo
						$("#dialog-confirma-reparte").dialog({
							resizable: false,
							height:'auto',
							width:300,
							modal: true,
							buttons: {
								"Confirmar": function() {
									//parametros para salvar repartes pdvs
									$.postJSON(contextPath + '/distribuicao/mixCotaProduto/salvarGridPdvReparte',  listaPDV, 
											function(result){
														if($("#radio").attr('checked') == 'checked'){
															$(".mixCotasGrid").flexReload();
														}else{
															$(".mixProdutosGrid").flexReload();
														}
														listaPDV =[];
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
						
					}else{
						$.postJSON(contextPath + '/distribuicao/mixCotaProduto/salvarGridPdvReparte', listaPDV,
								function(result){
									if($("#radio").attr('checked') == 'checked'){
										$(".mixCotasGrid").flexReload();
									}else{
										$(".mixProdutosGrid").flexReload();
									}
						});
						listaPDV =[];
						$("#dialog-defineReparte").dialog("close");
					}

				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		$('#dialog-defineReparte').bind('dialogclose', function(event) {
			mixCotaProdutoController.limparCamposModalReparteAoFechar();
		 });
		
		
	},
	//funcao que obtem os valores de cada reparte e o codigo do pdv 
	getValoresReparte:function(idMix){
		var data = [];
		data.push({name:"repartes",	value: repartes.join(",")});
		data.push({name:"codigos",	value: codigos.join(",")});
		data.push({name:"idMix",	value: idMix});
		return data;
	},
	
	//funcao que executa a chamada que preenche a grid de repartes por pdv
	preencherGridPdv:function(parametrosPesquisaReparte){
		$(".MX_pdvCotaGrid").flexOptions({
			url: contextPath + "/distribuicao/mixCotaProduto/carregarGridPdv",
			dataType : 'json',
			params: parametrosPesquisaReparte
		});
		
		$(".MX_pdvCotaGrid").flexReload();
	},
	//funcao que limpa os dados que foram carregados no modal de reparte ao fechar a tela
	limparCamposModalReparteAoFechar:function(){
		$("#MX_codigoCotaModalReparte").text("");
		$("#MX_nomeCotaModalReparte").text("");
		$("#MX_codigoProdutoModalReparte").text("");
		$("#MX_nomeProdutoModalReparte").text("");
		$("#MX_classificacaoModalReparte").text("");
		$(".MX_pdvCotaGrid").html("");
	},
	
	//funcao de pre-processamento da grid de repartes por pdv, adicionando inputs de reparte para cada linha da grid
	preProcessarListaPdv:function(data){
		if (data.result){
			data.rows = data.result[1].rows;
		}
		
		if(data.rows.length==1){
			exibirMensagem("WARNING", ["Operação não pode ser realizada. A cota possui somente 01 PDV."]);
			$( "#dialog-defineReparte" ).dialog("close");
			return;
		}
		
		var isReparteDefinido = false;
		var qtdReparteDivididoPorPDV = 0;
		
		for (var i=0; i<data.rows.length; i++){
			if(data.rows[i].cell.reparte > 0){
				qtdReparteDivididoPorPDV++;
			}
		}
		
		if(qtdReparteDivididoPorPDV > 1){
			isReparteDefinido = true;
		}
		
        for (var i=0; i<data.rows.length; i++) {
            
        	var cell = data.rows[i].cell;
            
        	if(isReparteDefinido){
        		cell.reparte = '<input class="MX_reparteGridinput" name="'+data.rows[i].cell.id+'" value="#">'.replace(/#/, data.rows[i].cell.reparte);
        	}else{
        		cell.reparte = '<input class="MX_reparteGridinput" name="'+data.rows[i].cell.id+'" value="#">'.replace(/#/, 0);
        	}
        }
		
		$('.MX_pdvCotaGrid').show();
	
		if (data.result){
			return data.result[1];
		}
		
		return data;

	},
	
	//funcao que retorna id da fixação a lista de fixacoes
	getIdSelecionado: function(cell){
		var data = [];
		data.push({name:'filtro.id',	value: cell.id});
		
		return data;
	},
	
	//funcao que retorna input de reparte a grid de reparte por pdv
	getInputReparte:function(cell){
		return "<input type='text' maxlength='5' size='7' class='MX_reparteGridinput' name='"+cell.id+"' value=\'"+ (cell.reparte || 0)  +"\'/>";
		
	},
	//funcao de exibicao de grid
	exibeGridCota:function(){
		$('.pesqCota').show();
		$('.pesqProduto').hide();
	},
	
	//funcao de exibicao de grid
	exibeGridProduto:function(){
		$('.pesqProduto').show();
		$('.pesqCota').hide();
	},
	
	//Funcao que realiza pesquisa de mix por produto
	pesquisarPorProduto:function(){

		mixCotaProdutoController.exibeGridProduto();
		$(".mixProdutosGrid").flexOptions({
			url: contextPath + "/distribuicao/mixCotaProduto/pesquisarPorProduto",
			dataType : 'json',
			params: mixCotaProdutoController.getDadosProduto()
		});
		
		$(".mixProdutosGrid").flexReload();
				
	},
	
	//Funcao que realiza pesquisa de fixações por cota
	pesquisarPorCota:function(){
	
		mixCotaProdutoController.exibeGridCota();
		$(".mixCotasGrid").flexOptions({
			url: contextPath + "/distribuicao/mixCotaProduto/pesquisarPorCota",
			dataType : 'json',
			params: mixCotaProdutoController.getDadosCota()
		});
		
		$(".mixCotasGrid").flexReload();
				
	},
	
		get : function(campo) {
			
			var elemento = $("#" + campo, mixCotaProdutoController.workspace);
			
			if(elemento.attr('type') == 'checkbox') {
				return (elemento.attr('checked') == 'checked') ;
			} else {
				return elemento.val();
			}
			
			
		},
		//Getter dos parametros necessarios para pesquisa de mix  por produto
		getDadosProduto : function() {
			
			var data = [];
			data.push({name:'filtro.codigoProduto',	value: $("#codigoProdutoMix").val()});
			data.push({name:'filtro.nomeProduto',	value: $("#nomeProdutoMix").val()});
			data.push({name:'filtro.classificacaoProduto',	value: $("#filtroClassificacaoMix option:selected").val()});
			
			return data;
		},
		
		//Getter dos parametros necessarios para pesquisa de mix  por cota
		getDadosCota : function() {
			
			var data = [];
			data.push({name:'filtro.cota',	value: $("#codigoCotaMix").val()});
			data.push({name:'filtro.nomeCota',	value: $("#nomeCotaMix").val()});
			
			return data;
		},
		
		addLoteMix:function(){
			$("#modalUploadArquivoMix").dialog({
				resizable: false,
				height:'auto',
				width:400,
				modal: true,
				buttons: {
					"Confirmar": function() {
						mixCotaProdutoController.executarSubmitArquivo();
					},
					"Cancelar": function() {
						$("#excelFile").val("");
						$(this).dialog("close");
					}
				},
			});
			
		},
		
		//submit do arquivo adicionar em lote
		executarSubmitArquivo:function(){
			 var fileName = $("#excelFile").val();
		      
		       var ext = fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase();
		       if(ext!="xls" & ext!="xlsx"){
		    	   exibirMensagem("WARNING", ["Somente arquivos com extensão .XLS ou .XLSX são permitidos."]);
		    	   $(this).val('');
		    	   return;
		       }else{
		    	   
		    	   $("#formUploadLoteMix").ajaxSubmit({
					
						success: function(responseText, statusText, xhr, $form)  { 
						
							if(responseText === null) {
								exibirMensagemDialog("SUCCESS", ["Todo o arquivo foi importado com sucesso!"],"");
							} else {
							
								var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;
								if(typeof(mensagens)!='undefined'){
									var tipoMensagem = mensagens.tipoMensagem;
									var listaMensagens = mensagens.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										if (tipoMensagem != 'SUCCESS') {
											
											exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
										}
										$(this).dialog( "close" );
										exibirMensagem(tipoMensagem, listaMensagens);	
									}
									
								}else if (typeof(responseText.mixCotaDTOInconsistente)=='object'){
																		
									var a = new Array();
									a.push("O arquivo possui [" + responseText.mixCotaDTOInconsistente.length + "] registros inconsistentes:");
									for ( var int = 0; int < responseText.mixCotaDTOInconsistente.length; int++) {
										a.push("Produto["+responseText.mixCotaDTOInconsistente[int].codigoProduto+"]"
												+", Cota["+responseText.mixCotaDTOInconsistente[int].numeroCota+"]"
												+", Reparte Minimo["+responseText.mixCotaDTOInconsistente[int].reparteMinimo+"]"
												+", Reparte Maximo["+responseText.mixCotaDTOInconsistente[int].reparteMaximo+"] : "+responseText.mixCotaDTOInconsistente[int].error);
									}
									exibirMensagemDialog("WARNING", a);
									return;
									
								}
							}
							
							$('#modalUploadArquivoMix').dialog('close');
						}, 
						type: 'POST',
						dataType: 'json'
					});
		    	   
		       }
		},
		
		abrirCopiaDialog:function(){
			var type  =$("input[type=radio][name=radio]:checked").val();
			type = type.toLowerCase();
			var target = type+'Copia-dialog';
			
			var idsCopiaCota="#cotaOrigemInput,#nomeCotaOrigemInput,#cotaDestinoInput,#nomeCotaDestinoInput";
			var idsCopiaProduto="#codigoProdutoOrigemInput,#nomeProdutoOrigemInput,#codigoProdutoDestinoInput,#nomeProdutoDestinoInput";
			
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
						if(type=='cota' && ($("#cotaOrigemInput").val()==$("#cotaDestinoInput").val())){
							msgWar="Cota Origem não pode ser igual a cota destino.";
						}else if(type=='produto' && ($("#codigoProdutoOrigemInput").val()==$("#codigoProdutoDestinoInput").val() )){
							msgWar="Produto Origem não pode ser igual a produto destino.";
						}
						
						if(msgWar!=null){
							exibirMensagem("WARNING",[msgWar]);
							return;
						}
						
						var data = [];
						data.push({name:"copiaMix.tipoCopia",	value: type.toUpperCase()});
						data.push({name:"copiaMix.cotaNumeroOrigem",	value: $("#cotaOrigemInput").val()});
						data.push({name:"copiaMix.nomeCotaOrigem",	value: $("#nomeCotaOrigemInput").val()});
						data.push({name:"copiaMix.cotaNumeroDestino",	value: $("#cotaDestinoInput").val()});
						data.push({name:"copiaMix.nomeCotaDestino",	value: $("#nomeCotaDestinoInput").val()});
						
						data.push({name:"copiaMix.codigoProdutoOrigem",	value: $("#codigoProdutoOrigemInput").val()});
						data.push({name:"copiaMix.nomeProdutoOrigem",	value: $("#nomeProdutoOrigemInput").val()});
						data.push({name:"copiaMix.codigoProdutoDestino",	value: $("#codigoProdutoDestinoInput").val()});
						data.push({name:"copiaMix.nomeProdutoDestino",	value: $("#nomeProdutoDestinoInput").val()});
						
						modal = this;
						$.postJSON(contextPath + '/distribuicao/mixCotaProduto/gerarCopiaMix',  data, 
								function(result){
									$(modal).dialog("close");
									exibirMensagem("WARNING",result.listaMensagens);
									
									if(type=='cota'){
										mixCotaProdutoController.pesquisarPorCota();
									}else if(type=='produto'){
										mixCotaProdutoController.pesquisarPorProduto();
									}
									
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
		
		pesquisarPorCodigoProduto : function (idCodigo, idProduto, idClassificacao){
			
//			$.postJSON(contextPath + '/distribuicao/mixCotaProduto/removerMixCotaProduto', mixCotaProdutoController.getIdExcluir(idMix) , mixCotaProdutoController.exclusaoMixCotaSucesso, mixCotaProdutoController.exclusaoMixCotaErro);
			
			var codigoPdt = $(idCodigo).val();
			
			$.postJSON(contextPath + '/distribuicao/mixCotaProduto/pesquisarPorCodigoProdutoAutoComplete',
					{codigo : codigoPdt},
					function(result) {
						if (result) {

							if(codigoPdt.length == 6){
								$(idCodigo).val(result[0].codigoICD);
							}else{
								$(idCodigo).val(result[0].codigo);
							}
							$(idProduto).val(result[0].nome);
							$(idClassificacao).val(result[1]);
						}
					}
			);
		}
		
	}, BaseController);
//@ sourceURL=mixCotaProduto.js
