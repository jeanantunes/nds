var mixCotaProdutoController = $.extend(true, {
//Grid de cota 	
	init : function() {
		$('.porProduto').hide();
		$('.porCota').hide();
		
		$(".mixCotasGrid").flexigrid({
			preProcess: function(data){return mixCotaProdutoController.executarPreprocessamentoGridCota(data);},
			dataType : 'json',
			colModel : [ {
				display : 'C�digo',
				name : 'codigoProduto',
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
				display : 'Classifica��o',
				name : 'classificacaoProduto',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Rep. M�dio',
				name : 'reparteMedio',
				width : 60,
				sortable : true,
				align : 'right'
			},  {
				display : 'Vda. M�dia',
				name : 'vendaMedia',
				width : 60,
				sortable : true,
				align : 'right'
			},  {
				display : '�lt. Rep.',
				name : 'ultimoReparte',
				width : 60,
				sortable : true,
				align : 'right'
			},  {
				display : 'Rep. Inf. M�n.',
				name : 'reparteMinimo',
				width : 70,
				sortable : true,
				align : 'right'
			},  {
				display : 'Rep. Inf. Max.',
				name : 'reparteMaximo',
				width : 70,
				sortable : true,
				align : 'right'
			},  {
				display : 'Usu�rio',
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
				display : 'A��o',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
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
				display : 'Rep. M�dio',
				name : 'reparteMedio',
				width : 70,
				sortable : true,
				align : 'right'
			},  {
				display : 'Vda. M�dia',
				name : 'vendaMedia',
				width : 70,
				sortable : true,
				align : 'right'
			},  {
				display : '�lt. Rep.',
				name : 'ultimoReparte',
				width : 70,
				sortable : true,
				align : 'right'
			},  {
				display : 'Rep. Inf. M�n.',
				name : 'reparteMinimo',
				width : 80,
				sortable : true,
				align : 'right'
			},  {
				display : 'Rep. Inf. Max.',
				name : 'reparteMaximo',
				width : 80,
				sortable : true,
				align : 'right'
			},  {
				display : 'Usu�rio',
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
				display : 'A��o',
				name : 'acao',
				width : 50,
				sortable : true,
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
		$(".pdvCotaGrid").flexigrid({
			preProcess: function(data){return mixCotaProdutoController.preProcessarListaPdv(data);},
			dataType : 'json',
			colModel : [ {
				display : 'C�digo',
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
				display : 'Endere�o',
				name : 'endereco',
				width : 320,
				sortable : true,
				align : 'left'
			},  {
				display : 'Reparte',
				name : 'reparte',
				width : 40,
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
			$("#btAddLoteMixCota").hide();
			$("#btGerarArquivoMixCota").hide();
			$("#btImprimirMixCota").hide();
			$("#btExcluirTudoCota").hide();
			return data;
		}else{
			$("#spanLegendCota").text($("#codigoCotaMix").val() + "-" +$("#nomeCotaMix").val());// preenche fieldset com os valores digitados no fitro
			$("#btNovoMixCota").show();
			$("#btAddLoteMixCota").show();
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
		}
		
		$('.mixCotasGrid').show();
		if (data.result){
			return data.result[1];
		}
		return data;
	},	
	
	//retorna acoes de edicao e exclusao de fixacao por cota
	getActionsGridCota: function (cell){
		
		components="";
		
		imgAlteracao =  '<a href="javascript:;" id="editar" onclick="mixCotaProdutoController.editarRepartePorPdv(' + cell.id + ','+cell.reparteMaximo + ','+cell.idCota+', \'' + cell.idProduto + '\')" ' +
		' style="cursor:pointer;border:0px;margin:5px" title="Reparte por PDV ">' +
		'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
		'</a>' ;
		
		imgExcluir = '<a href="javascript:;" onclick="mixCotaProdutoController.excluirMixCota(' + cell.id + ')" '  +
		' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fixação">' +
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
		if($("#radio").attr('checked') == 'checked'){
			$(".mixCotasGrid").flexReload();
		}
		else{
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
			$("#btAddLoteMixProduto").hide();
			$("#btGerarArquivoMixProduto").hide();
			$("#btImprimirMixProduto").hide();
			$("#btExcluirTudoProduto").hide();
			return data;
		}else{
			// pesquisa achou resultado
			$("#spanLegendProduto").text($("#codigoProdutoMix").val() + "-" +$("#nomeProdutoMix").val());// preenche fieldset com os valores digitados no fitro
			$("#btNovoMixProduto").show();
			$("#btAddLoteMixProduto").show();
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
		
		imgAlteracao =  '<a href="javascript:;" id="editar" onclick="mixCotaProdutoController.editarRepartePorPdv(' + cell.id + ','+cell.reparteMaximo + ','+cell.idCota+', \'' + cell.idProduto + '\')" ' +
		' style="cursor:pointer;border:0px;margin:5px" title="Reparte por PDV ">' +
		'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
		'</a>' ;
		
		imgExcluir = '<a href="javascript:;" onclick="mixCotaProdutoController.excluirMixCota(' + cell.id + ')" '  +
		' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fixação">' +
		'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
		'</a>';
		
		//Checa quantidade de pdvs. Caso seja maior que 1 o retorna botoes editar/excluir senao apenas o botao excluir
		if (cell.qtdPdv > 1) {
			components = imgAlteracao;
		}
			components += imgExcluir;
		
		return  components;
	},
	
	
	excluirTodos: function(){
		$("#dialog-excluirTodos").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(contextPath + '/distribuicao/mixCotaProduto/excluirTodos','undefined',mixCotaProdutoController.exclusaoTodosSucesso, mixCotaProdutoController.exclusaoMixProdutoaErro);
					
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
			$(".mixCotasGrid").flexReload();
		}else{
			$(".mixProdutosGrid").flexReload();
		}
		exibirMensagem("SUCCESS", ["Opera��o Realizada com sucesso!"]);
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
				mixCotaProdutoController.definirIdInput();
				
			},
			buttons: {
				"Confirmar": function() {
					var listaNovosMixCota=new Array();
					//tudo preenchido
					if(mixCotaProdutoController.validarInputs()){
					listaNovosMixCota.push({
								  name : "cotaId" , 
								  value : $("#codigoCotaMix").val()
								  });
							 
							 $("#tableNovoCota tbody tr").each(function(idx, linha){
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
							 });
							 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/adicionarMixCota',listaNovosMixCota,function(result){ mixCotaProdutoController.adicionarMixCotaSucesso();});
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
				exibirMensagem("WARNING", ["C�digo de produto j� utilizado."]);
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
				exibirMensagem("WARNING", ["Reparte M�nimo maior que Reparte M�ximo para o produto."]);
				return false;
			}
		}
		
		
		return true;
	},
	
	adicionarMixCotaSucesso:function(result){
		$(".mixCotasGrid").flexReload();
		exibirMensagem("SUCCESS", ["Opera��o Realizada com sucesso!"]);
	},
	
	adicionarMixProdutoSucesso:function(result){
		$(".mixProdutosGrid").flexReload();
		exibirMensagem("SUCCESS", ["Opera��o Realizada com sucesso!"]);
	},
	
	
	//Adiciona nova linha ao grid por Produto
	novoMixPorProduto : function(){
		
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
										  name : "listaNovosMixProduto["+idx+"].numeroCota" , 
										  value : $("#numeroCotaModal"+idx).val()
										  });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].nomeCota" , 
										  value : $("#cotaModal"+idx).val()
										  });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].reparteMinimo" , 
										  value : $("#repMinimo"+idx).val()
										  });
								 list.push({
										  name : "listaNovosMixProduto["+idx+"].reparteMaximo" , 
										  value : $("#repMaximo"+idx).val()
										  });
//								 });
							 });
							 
							 $.postJSON(contextPath + '/distribuicao/mixCotaProduto/adicionarMixProduto',list,function(result){ mixCotaProdutoController.adicionarMixProdutoSucesso();});
							 $(this).dialog("close");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
		});
		
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
			$("#tableNovoProduto tr:last").find("input[type='image']").show();
			this.definirIdInputProduto();
		}
		
	},
	
	definirIdInput:function(){
		
		$("#tableNovoCota tbody tr").each(function(idx,tr){
			
			$(tr).find("input:text:eq(0)").attr("id","codigoModal"+idx);
			$(tr).find("input:text:eq(1)").attr("id","produtoModal"+idx);
			$(tr).find("input:text:eq(2)").attr("id","repMinimo"+idx);
			$(tr).find("input:text:eq(3)").attr("id","repMaximo"+idx);
			
		});
	},
	
	definirIdInputProduto:function(){
		
		$("#tableNovoProduto tbody tr").each(function(idx,tr){
			
			var inputList = $(tr).find("input:text");
			
			$(inputList[0]).attr("id","numeroCotaModal"+idx);
			$(inputList[1]).attr("id","cotaModal"+idx);
			$(inputList[2]).attr("id","repMinimo"+idx);
			$(inputList[3]).attr("id","repMaximo"+idx);
			
		});
	},
	

	//funcao que executa chamada postJSON que busca dados do mix 
	editarRepartePorPdv:function (id,reparteMaximo, idCota, idProduto){
		var reparteTotal= reparteMaximo;
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
			$("#codigoCotaModalReparte").text(result.numeroCota);
			$("#nomeCotaModalReparte").text(result.nomeCota);
			$("#codigoProdutoModalReparte").text(result.codigoProduto);
			$("#nomeProdutoModalReparte").text(result.nomeProduto);
			$("#classificacaoModalReparte").text(result.classificacaoProduto);
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
					  $("#pdvCotaGrid .reparteGridinput").each(function(idx, linha){
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
						  value : $("#codigoProdutoModalReparte").text()
						  });
					  listaPDV.push({
						  name : "codCota" , 
						  value : $("#codigoCotaModalReparte").text()
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
						$.postJSON(contextPath + '/distribuicao/mixCotaProduto/salvarGridPdvReparte', listaPDV);
						if($("#radio").attr('checked') == 'checked'){
							$(".mixCotasGrid").flexReload();
						}else{
							$(".mixProdutosGrid").flexReload();
						}
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
		$(".pdvCotaGrid").flexOptions({
			url: contextPath + "/distribuicao/mixCotaProduto/carregarGridPdv",
			dataType : 'json',
			params: parametrosPesquisaReparte
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
			
			data.rows[i].cell["reparte"]=mixCotaProdutoController.getInputReparte(data.rows[i].cell);
		}
		$('.pdvCotaGrid').show();
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
		return "<input type='text' class='reparteGridinput' name='"+cell.id+"' value=\'"+ (cell.reparte || 0)  +"\'/>";
		
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
			
			var elemento = $("#" + campo, fixacaoReparteController.workspace);
			
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
			data.push({name:'filtro.classificacaoProduto',	value: $("select['select'] option:selected").val()});
			
			return data;
		},
		
		//Getter dos parametros necessarios para pesquisa de mix  por cota
		getDadosCota : function() {
			
			var data = [];
			data.push({name:'filtro.cota',	value: $("#codigoCotaMix").val()});
			data.push({name:'filtro.nomeCota',	value: $("#nomeCotaMix").val()});
			
			return data;
		},
		
	
	}, BaseController);
//@ sourceURL=mixCotaProduto.js