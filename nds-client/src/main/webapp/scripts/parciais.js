var ParciaisController = $.extend(true, {
	
	idProdutoEdicao : null,
	dataLancamento : null,
	dataRecolhimento : null,
	codigoProduto : null,
	nomeProduto : null,
	numEdicao : null,
	nomeFornecedor : null,
	precoCapa : null,
	statusParcial : null,
		
	idLancamento : null,
	idPeriodo : null,
	idLancamentoRedistribuicao:null,
	
	init : function() {
		$( "#parcial-dataLancamentoEd", ParciaisController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#parcial-dataRecolhimentoEd", ParciaisController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#parcial-dataInicial", ParciaisController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#parcial-dataFinal", ParciaisController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#parcial-lancamentoNovaRed", ParciaisController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#parcial-dataRecolhimentoManual", ParciaisController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$('#parcial-dataLancamentoEd', ParciaisController.workspace).mask("99/99/9999");
		$('#parcial-dataRecolhimentoEd', ParciaisController.workspace).mask("99/99/9999");
		$('#parcial-dataInicial', ParciaisController.workspace).mask("99/99/9999");
		$('#parcial-dataFinal', ParciaisController.workspace).mask("99/99/9999");
		$('#parcial-lancamentoNovaRed', ParciaisController.workspace).mask("99/99/9999");
		$("#parcial-dataRecolhimentoManual", ParciaisController.workspace).mask("99/99/9999");

		$("#parcial-nomeProduto", ParciaisController.workspace).autocomplete({source: ""});
		$("#parcial-edicaoProduto", ParciaisController.workspace).autocomplete({source: ""});
		
		ParciaisController.inicializarGrids();
	},
	
	/**
	 * Ação de clique do botão pesquisar
	 */
	cliquePesquisar : function() {
		
		if(ParciaisController.get('parcial-codigoProduto').length != 0 && ParciaisController.get('parcial-edicaoProduto').length != 0) {
			$('#parcial-painelLancamentos',ParciaisController.workspace).hide();
			$('#parcial-painelPeriodos',ParciaisController.workspace).show();	
			
			
			ParciaisController.codigoProduto = ParciaisController.get('parcial-codigoProduto');
			ParciaisController.numEdicao = ParciaisController.get('parcial-edicaoProduto');
			ParciaisController.nomeProduto = ParciaisController.get('parcial-nomeProduto');
			
			ParciaisController.pesquisarPeriodosParciais();
		} else {
			ParciaisController.codigoProduto = ParciaisController.get('parcial-codigoProduto');
			ParciaisController.nomeProduto = ParciaisController.get('parcial-nomeProduto');
			$('#parcial-painelPeriodos',ParciaisController.workspace).hide();
			$('#parcial-painelLancamentos',ParciaisController.workspace).show();
			ParciaisController.pesquisarLancamentosParciais();
		}		
	},
	
	pesquisarLancamentosParciais : function() {
		
		$(".parcial-parciaisGrid", ParciaisController.workspace).flexOptions({			
			url : contextPath + "/parciais/pesquisarParciais",
			dataType : 'json',
			preProcess: ParciaisController.processaRetornoPesquisaParciais,
			params:ParciaisController.getDados()
		});
		
		$(".parcial-parciaisGrid", ParciaisController.workspace).flexReload();
	},
	
	pesquisarPeriodosParciais : function() {
		
		$(".parcial-periodosGrid",ParciaisController.workspace).flexOptions({
			onSuccess: function() {bloquearItensEdicao(ParciaisController.workspace);},
			url : contextPath + "/parciais/pesquisarPeriodosParciais",
			dataType : 'json',
			preProcess: ParciaisController.processaRetornoPeriodosParciais,
			params:ParciaisController.getDados()
		});
		
		$(".parcial-periodosGrid", ParciaisController.workspace).flexReload();
		
	},

	pesquisarPeriodosParciaisModal : function(codigoProduto,numEdicao) {
		
		var data = [];
		
		data.push({name: 'filtro.codigoProduto',	value: codigoProduto});
		data.push({name: 'filtro.edicaoProduto',	value: numEdicao});
		
		$(".parcial-parciaisPopGrid",ParciaisController.workspace).flexOptions({	
			onSuccess: function() {bloquearItensEdicao(ParciaisController.workspace);},
			url : contextPath + "/parciais/pesquisarPeriodosParciais",
			dataType : 'json',
			preProcess: ParciaisController.processaRetornoPeriodosParciaisModal,
			params:data
		});
		
		$(".parcial-parciaisPopGrid",ParciaisController.workspace).flexReload();
		
	},
	
	pesquisarRedistribuicaoPeriodo:function(idPeriodo){
		
		var data = [];
	
		data.push({name: 'idPeriodo',	value: idPeriodo});
		
		$(".parcial-parciaisRedistribuicaoGrid", ParciaisController.workspace).flexOptions({	
			onSuccess: function() {bloquearItensEdicao(ParciaisController.workspace);},
			url : contextPath + "/parciais/pesquisarRedistribuicao",
			dataType : 'json',
			preProcess: ParciaisController.processaRetornoRedistribuicaoPeriodo,
			params:data
		});
		
		$(".parcial-parciaisRedistribuicaoGrid", ParciaisController.workspace).flexReload();
	},

	inserirPeriodo : function(modal) {
		$.postJSON(contextPath + "/parciais/inserirPeriodo",
			ParciaisController.getDadosNovoPeriodoManual(),
			function(result){
				$( "#dialog-novo-manual",ParciaisController.workspace).dialog( "close" );
				if(modal)
					$(".parcial-parciaisPopGrid",ParciaisController.workspace).flexReload();
				else
					$(".parcial-periodosGrid",ParciaisController.workspace).flexReload();
			},
			null, 
			true,
			"dialog-detalhes"
		);
	},

	gerarPeriodosParcias : function(modal) {
		$.postJSON(contextPath + "/parciais/gerarPeriodosParcias",
			ParciaisController.getDadosNovosPeriodo(),
			function(result){
				$( "#dialog-novo",ParciaisController.workspace).dialog( "close" );
				if(modal)
					$(".parcial-parciaisPopGrid",ParciaisController.workspace).flexReload();
				else
					$(".parcial-periodosGrid",ParciaisController.workspace).flexReload();
			},
			null, 
			true,
			"dialog-detalhes"
		);
	},
	
	processaRetornoPesquisaParciais : function(result) {
		
		$('#exportacaoPeriodos',ParciaisController.workspace).hide();
		
		if(result.mensagens){
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
		} 
		
		if(result.rows.length==0) {
			$("#exportacao",ParciaisController.workspace).hide();
			$('#parcial-painelLancamentos',ParciaisController.workspace).hide();
		} else {
			$("#exportacao",ParciaisController.workspace).show();
		}
		
		$.each(result.rows, function(index,row) {
			
			ParciaisController.gerarAcaoPrincipal(index, row);
		});
				
		return result;
	},
	
	processaRetornoPeriodosParciais : function(result) {
		
		$("#exportacao", ParciaisController.workspace).hide();
		$("#btnIncluirPeriodos", ParciaisController.workspace).show();
		
		if(result.mensagens) {
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
		}
		
		if(result.rows.length==0) {
			$('#exportacaoPeriodos',ParciaisController.workspace).hide();
		} else {
			$('#exportacaoPeriodos',ParciaisController.workspace).show();
			ParciaisController.idProdutoEdicao = result.rows[0].cell.idProdutoEdicao;
		}
		
		var indexUtimoRegistro = result.rows.length-1 ;
		var isExcluir = false;
		$.each(result.rows, function(index, row) {
			
			var isExcluir = (index != indexUtimoRegistro);
			if(row.cell.statusLancamento == 'EXPEDIDO') {
				
				$("#btnIncluirPeriodos", ParciaisController.workspace).hide();
			}
			
			ParciaisController.gerarAcaoDetalhes(index, row, isExcluir);
		} );
				
		return result;
	},
	
	processaRetornoPeriodosParciaisModal : function(result) {
		
		if(result.mensagens)
				exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "dialog-detalhes");
					
		if(result.rows.length==0) {
			$('#exportacaoPeriodosModal',ParciaisController.workspace).hide();
		} else {
			$('#exportacaoPeriodosModal',ParciaisController.workspace).show();
		}
		
		if(result.rows.length > 0 && result.rows[0].cell.geradoPorInterface==true)
			$("#btnIncluirPeriodosModal",ParciaisController.workspace).hide();
		else
			$("#btnIncluirPeriodosModal",ParciaisController.workspace).show();		
		
		var indexUtimoRegistro = result.rows.length -1 ;
		var isExcluir = false;
		$.each(result.rows, function(index,row){
			var isExcluir = (index != indexUtimoRegistro);
			ParciaisController.gerarAcaoDetalhes(index,row,isExcluir);
		} );
				
		return result;
	},
	
	processaRetornoRedistribuicaoPeriodo:function(result){
		
		if(result.mensagens) 
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
		
		var indexUtimoRegistro = result.rows.length -1 ;
		
		var isExcluir = false;
		
		$.each(result.rows, function(index,row){
		
			row.cell.acao="";
			row.cell.numeroLancamento = row.cell.numeroLancamento ? row.cell.numeroLancamento : "-";
			
			if(index > 0){	
				isExcluir = (index != indexUtimoRegistro);
				ParciaisController.gerarAcaoRedistribuicao(index,row,isExcluir);
			}
		} );
				
		return result;
	},

	/**
	 * Retorna todos os dados da tela principal no padrão utilizado pelo VRaptor
	 * @return Espelho de FiltroParciaisDTO (br.com.abril.nds.dto) 
	 */
	getDados : function() {
	
		var data = [];
		
		data.push({name:'filtro.codigoProduto',		value: ParciaisController.get("parcial-codigoProduto")});
		data.push({name:'filtro.nomeProduto',		value: ParciaisController.get("parcial-nomeProduto")});
		data.push({name:'filtro.edicaoProduto',		value: ParciaisController.get("parcial-edicaoProduto")});
		data.push({name:'filtro.idFornecedor',		value: ParciaisController.get("parcial-idFornecedor")});
		data.push({name:'filtro.dataInicial',		value: ParciaisController.get("parcial-dataInicial")});
		data.push({name:'filtro.dataFinal',			value: ParciaisController.get("parcial-dataFinal")});
//		data.push({name:'filtro.status',			value: ParciaisController.get("status")});
		
		data.push({name:'filtro.nomeFornecedor',	value: $('#parcial-idFornecedor option:selected').text()});
		
		return data;
	},
	
	getDadosNovosPeriodo : function() {
		
		var data = [];
		
		data.push({name:'peb',				value: ParciaisController.get("parcial-peb")});
		data.push({name:'qtde',				value: ParciaisController.get("parcial-qtde")});
		data.push({name:'idProdutoEdicao',	        value: ParciaisController.idProdutoEdicao});
		
		return data;
	},
	
	getDadosNovoPeriodoManual: function() {
		
		var data = [];

		data.push({name:'dataRecolhimento',	value: ParciaisController.get("parcial-dataRecolhimentoManual")});
		data.push({name:'idProdutoEdicao',	value: ParciaisController.idProdutoEdicao});
		
		return data;
	},
	
	getDadosParaPeb : function() {
		
		var data = [];
		
		data.push({name:'codigoProduto',		value: ParciaisController.codigoProduto});
		data.push({name:'edicaoProduto',		value: ParciaisController.numEdicao});
		data.push({name:'periodos',				        value: ParciaisController.get("parcial-qtde")});
		return data;
	},
	
	getDadosEdicaoPeriodo : function() {
		
		var data = [];
		
		data.push({name:'dataLancamento',		value: ParciaisController.get('parcial-dataLancamentoEd')});
		data.push({name:'dataRecolhimento',		value: ParciaisController.get('parcial-dataRecolhimentoEd')});
		data.push({name:'idLancamento',			        value: ParciaisController.idLancamento});	
		
		return data;
	},
	
	getDadosNovaRedistribuicao:function(){
	
		var data = [];
		
		data.push({name:'redistribuicaoDTO.dataLancamento',		value: ParciaisController.get('parcial-lancamentoNovaRed')});
		data.push({name:'redistribuicaoDTO.dataRecolhimento',	value: $("#parcial-dataRecolhimentoRed",ParciaisController.workspace).text()});
		data.push({name:'redistribuicaoDTO.idPeriodo',			value: ParciaisController.idPeriodo});	
		
		return data;
	},
	
	getDatasEdicaoPeriodoParaValidacao : function() {
		
		var data = [];
		
		data.push({name:'dataLancamento',		value: ParciaisController.get('parcial-dataLancamentoEd')});
		data.push({name:'dataRecolhimento',		value: ParciaisController.get('parcial-dataRecolhimentoEd')});
		
		return data;
	},
	
	getDatasNovaRedistribuicaoParaValidacao : function() {
		
		var data = [];
		
		data.push({name:'dataLancamento',		value: ParciaisController.get('parcial-lancamentoNovaRed')});
		data.push({name:'dataRecolhimento',		value: $("#parcial-dataRecolhimentoRed",ParciaisController.workspace).text()});

		return data;
	},
	
	carregaPeb : function(periodos) {
		
		ParciaisController.set('parcial-qtde', periodos);
		ParciaisController.set('parcial-peb','');
		
		$.postJSON(contextPath + "/parciais/obterPebDoProduto",
				ParciaisController.getDadosParaPeb(),
				function(result){ParciaisController.set('parcial-peb', result);},
				null, 
				true,
				"dialog-novo");
		
	},
	
	gerarAcaoPrincipal : function(index,row) {
		row.cell.acao = 
			'<a href="javascript:;" onclick="ParciaisController.carregarDetalhes(\''+ 
				row.cell.idProdutoEdicao +'\', \''+
				row.cell.dataLancamento +'\', \''+
				row.cell.dataRecolhimento +'\', \''+
				row.cell.codigoProduto +'\', \''+
				row.cell.nomeProduto +'\', \''+
				row.cell.numEdicao +'\', \''+
				row.cell.nomeFornecedor +'\', \''+ 
				row.cell.precoCapa +'\', \''+ 
				row.cell.statusParcial + '\')">' +
				'<img src="'+contextPath+'/images/ico_detalhes.png" border="0" /></a>';
	},
	
	gerarAcaoDetalhes : function(index, row,isExcluir) {
		
		if(row.cell.vendas > 0){
			
			row.cell.vendas = '<a href="javascript:;" onclick="ParciaisController.detalheVendas(\'' +
			row.cell.dataLancamento +'\', \''+
			row.cell.dataRecolhimento +'\', \''+
			row.cell.idProdutoEdicao +'\', \''+
			row.cell.idPeriodo +'\', \''+
			'\');">' + row.cell.vendas + '</a>';
		}
	
		row.cell.acao = 
			'<a href="javascript:;" isEdicao="true" ' +
			'onclick="ParciaisController.carregarEdicaoDetalhes(\''+ 
					row.cell.idLancamento +'\', \''+
					row.cell.dataLancamento +'\', \''+
					row.cell.dataRecolhimento +
			        ' \')"'+
			' ><img src="'+contextPath+'/images/ico_editar.gif" border="0"  style="margin-right:5px;" /></a>' +
			
			'<a href="javascript:;" isEdicao="true" '+
			(isExcluir==true?'style="opacity: 0.5;"':' onclick="ParciaisController.carregarExclusaoPeriodo(\'' + row.cell.idLancamento+ '\');" ')+
			'><img src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0" style="margin-right:5px;" /></a>' +
			
			'<a href="javascript:;" onclick="ParciaisController.carregarRedistribuicao(\''+
				row.cell.idPeriodo +'\',\''+
				row.cell.numeroPeriodo +'\',\''+
				row.cell.dataLancamento +'\',\''+
				row.cell.dataRecolhimento +'\');\"'+
			'><img src="'+contextPath+'/images/bt_lancamento.png" border="0" hspace="5"/></a>';
	},
	
	gerarAcaoRedistribuicao:function(index, row,isExcluir){
		
		row.cell.acao = 
			'<a href="javascript:;" isEdicao="true" onclick="ParciaisController.carregarEdicaoRedistribuicao(\''+ 
					row.cell.idLancamentoRedistribuicao +'\', \''+
					row.cell.dataLancamento +'\', \''+
					row.cell.dataRecolhimento +
			        ' \')"'+    
			'><img src="'+contextPath+'/images/ico_editar.gif" border="0"  style="margin-right:5px;" /></a>' +
			
			'<a href="javascript:;" isEdicao="true" '+
			(isExcluir==true?'style="opacity: 0.5;"':' onclick="ParciaisController.carregarExclusaoRedistribuicao(\'' + row.cell.idLancamentoRedistribuicao+ '\');" ')+
			'><img src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0" style="margin-right:5px;" /></a>';
		
	},
	
	carregarRedistribuicao:function(idPeriodo, numeroPeriodo, dataLancamento, dataRecolhimento){
		
		$('#parcial-codigoProdutoRed',ParciaisController.workspace).text(ParciaisController.codigoProduto);
		$('#parcial-nomeProdutoRed',ParciaisController.workspace).text(ParciaisController.nomeProduto);
		$('#parcial-numEdicaoRed',ParciaisController.workspace).text(ParciaisController.numEdicao);
		$('#parcial-dataLancamentoRed',ParciaisController.workspace).text(dataLancamento);
		$('#parcial-dataRecolhimentoRed',ParciaisController.workspace).text(dataRecolhimento);
		$('#parcial-numeroPeriodoRed',ParciaisController.workspace).text(numeroPeriodo);
		
		ParciaisController.idPeriodo = idPeriodo;
		
		ParciaisController.pesquisarRedistribuicaoPeriodo(idPeriodo);
		
		ParciaisController.popup_redistribuicao();
	},
	
	carregarDetalhes : function(idProdutoEdicao , dataLancamento, dataRecolhimento, codigoProduto, 
			nomeProduto, numEdicao, nomeFornecedor, precoCapa, statusParcial) {
				
		ParciaisController.idProdutoEdicao = idProdutoEdicao;
		ParciaisController.dataLancamento = dataLancamento;
		ParciaisController.dataRecolhimento = dataRecolhimento;
		ParciaisController.codigoProduto = codigoProduto;
		ParciaisController.nomeProduto = nomeProduto;
		ParciaisController.numEdicao = numEdicao;
		ParciaisController.nomeFornecedor = nomeFornecedor;
		ParciaisController.precoCapa = precoCapa;
		ParciaisController.statusParcial = statusParcial;
		
		
		$('#parcial-codigoProdutoM',ParciaisController.workspace).text(codigoProduto);
		$('#parcial-nomeProdutoM',ParciaisController.workspace).text(nomeProduto);
		$('#parcial-numEdicaoM').text(numEdicao);
		$('#parcial-nomeFornecedorM',ParciaisController.workspace).text(nomeFornecedor);
		$('#parcial-dataLancamentoM',ParciaisController.workspace).text(dataLancamento);
		$('#parcial-dataRecolhimentoM',ParciaisController.workspace).text(dataRecolhimento);
		
		ParciaisController.pesquisarPeriodosParciaisModal(codigoProduto,numEdicao);
		
		ParciaisController.popup_detalhes();
	},
	
	carregarEdicaoDetalhes : function(idLancamento, dataLancamento,dataRecolhimento) {
		
		ParciaisController.idLancamento = idLancamento;
		
		$('#parcial-codigoProdutoEd', ParciaisController.workspace).val(ParciaisController.codigoProduto);
		$('#parcial-nomeProdutoEd', ParciaisController.workspace).val(ParciaisController.nomeProduto);
		$('#parcial-numEdicaoEd', ParciaisController.workspace).val(ParciaisController.numEdicao);
		$('#parcial-nomeFornecedorEd', ParciaisController.workspace).val(ParciaisController.nomeFornecedor);
		$('#parcial-dataLancamentoEd', ParciaisController.workspace).datepicker("setDate", $.trim(dataLancamento));
		$('#parcial-dataRecolhimentoEd', ParciaisController.workspace).datepicker("setDate", $.trim(dataRecolhimento));
		$('#parcial-precoCapaEd', ParciaisController.workspace).val(ParciaisController.precoCapa);
		
		ParciaisController.popup_edit_produto();
	},
	
	carregarEdicaoRedistribuicao:function(idLancamentoRedistribuicao,dataLancamento,dataRecolhimento){
		
		ParciaisController.idLancamentoRedistribuicao = idLancamentoRedistribuicao;
		ParciaisController.set("parcial-lancamentoNovaRed", dataLancamento);
		
		ParciaisController.popupNovaRedistribuicao(false);
	},
	
	carregarExclusaoPeriodo : function(idLancamento) {
		
		ParciaisController.idLancamento = idLancamento;
		
		ParciaisController.popup_excluir();
	},
	
	carregarExclusaoRedistribuicao : function(idLancamentoRedistribuicao) {
		
		$( "#dialog-excluir-redistribuicao", ParciaisController.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						ParciaisController.excluirRedistribuicao(idLancamentoRedistribuicao);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-excluir-redistribuicao", ParciaisController.workspace).parents("form")
			});
	},
	
	editarPeriodoParcial : function() {
		
		$.postJSON(contextPath + "/parciais/editarPeriodoParcial",
				ParciaisController.getDadosEdicaoPeriodo(),
				function(result){

					$( "#dialog-edit-produto", ParciaisController.workspace).dialog( "close" );
					
					if($('#parcial-painelPeriodos',ParciaisController.workspace).css('display')=='none') {
						exibirMensagemDialog('SUCCESS', ['Período alterado com sucesso.'], "dialog-detalhes");			
						$(".parcial-parciaisPopGrid",ParciaisController.workspace).flexReload();
					} else {
						$(".parcial-periodosGrid",ParciaisController.workspace).flexReload();
						exibirMensagem('SUCCESS', ['Período alterado com sucesso.']);
					}
					
				},	
				null,
				true,
				'dialog-edit-produto');		
	},
	
	excluirPeriodoParcial : function() {
		
		var data = [];		
		data.push({name:'idLancamento',			value: ParciaisController.idLancamento});	
		
		$.postJSON(contextPath + "/parciais/excluirPeriodoParcial",
				data,
				function(result){
			
					if($('#parcial-painelPeriodos',ParciaisController.workspace).css('display')=='none') {
						exibirMensagemDialog('SUCCESS', ['Período excluido com sucesso.'], "dialog-detalhes");			
						$(".parcial-parciaisPopGrid",ParciaisController.workspace).flexReload();
					} else {
						$(".parcial-periodosGrid",ParciaisController.workspace).flexReload();
						exibirMensagem('SUCCESS', ['Período excluido com sucesso.']);
					}
					
					$( "#dialog-excluir", ParciaisController.workspace).dialog( "close" );					
				},	
				function(result) {
					$( "#dialog-excluir", ParciaisController.workspace).dialog( "close"); 
				},
				true,
				'dialog-detalhes');	
		
	},
	
	incluirNovaRedistribuicao:function(){
			
		$.postJSON(contextPath + "/parciais/incluirRedistribuicao",
			ParciaisController.getDadosNovaRedistribuicao(),
			function(result){

				$("#dialog-nova-redistribuicao", ParciaisController.workspace).dialog( "close" );
				
				ParciaisController.pesquisarRedistribuicaoPeriodo(ParciaisController.idPeriodo);			
			},	
			null,
			true,
			'dialog-nova-redistribuicao');		
	},
	
	excluirRedistribuicao:function(idLancamentoRedistribuicao){
		
		var data = [];
		
		data.push({name:'idPeriodo',value: ParciaisController.idPeriodo});
		data.push({name:'idLancamentoRedistribuicao',value: idLancamentoRedistribuicao});
		
		$.postJSON(contextPath + "/parciais/excluirRedistribuicao",
			data,
			function(result){
				ParciaisController.pesquisarRedistribuicaoPeriodo(ParciaisController.idPeriodo);

				$( "#dialog-excluir-redistribuicao" ).dialog( "close" );		
			},	
			null,
			true,
			'dialog-nova-redistribuicao');		
	},
	
	editarRedistribuicao:function(){
		
		var data  = ParciaisController.getDadosNovaRedistribuicao();
		data.push({name:'redistribuicaoDTO.idLancamentoRedistribuicao',value: ParciaisController.idLancamentoRedistribuicao});
		
		$.postJSON(contextPath + "/parciais/editarRedistribuicao",
			data,
			function(result){

				$("#dialog-nova-redistribuicao", ParciaisController.workspace).dialog( "close" );
				
				ParciaisController.pesquisarRedistribuicaoPeriodo(ParciaisController.idPeriodo);			
			},	
			null,
			true,
			'dialog-nova-redistribuicao');		
	},
	
	validarDiaUtil : function(successCallback, datasValidacao){
		
		$.postJSON(contextPath + "/parciais/validarDiaUtil",
			datasValidacao,
			function(result){
				
				if(result.length > 0){
					
					$("#mensagemAlertaDiaUtil",ParciaisController.workspace).text(result);
					
					ParciaisController.popup_alert_dia_util(successCallback);
				}
				else{
					successCallback();
				}				
			},	
			null,
			true,
			'dialog-edit-lancamento-dia-util');	
	},
	
	detalheVendas : function(dtLcto, dtRcto, idProdutoEdicao, idPeriodo) {
		
		var data = [];
		
		data.push({name:'dtLcto',				value: dtLcto});
		data.push({name:'dtRcto',				value: dtRcto});
		data.push({name:'idPeriodo',			value: idPeriodo});
		data.push({name:'idProdutoEdicao',		value: idProdutoEdicao});

		$(".parcial-parciaisVendaGrid", ParciaisController.workspace).flexOptions({			
			url : contextPath + "/parciais/pesquisarParciaisVenda",
			dataType : 'json',
			params: data
		});

		$(".parcial-parciaisVendaGrid",ParciaisController.workspace).flexReload();

		ParciaisController.popup_detalheVendas();
	},
		
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	set : function(campo,value) {
				
		var elemento = $("#" + campo ,ParciaisController.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	get : function(campo) {
		
		var elemento = $("#" + campo, ParciaisController.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	

		popup : function(dialogID, confirmAction, modal) {
		
			$( dialogID, ParciaisController.workspace).dialog({
				resizable: false,
				height:200,
				width:400,
				modal: true,
				
				buttons:[{
			           id:"bt_confirmar",
			           text:"Confirmar", 
			           click: function() {
			        	   confirmAction(modal);
			           }
		           }, {
			           id:"bt_cancelar",
			           text:"Cancelar", 
			           click: function() {
			        	   $( this ).dialog( "close" );
			           }
		           }
		        ],
		        form: $(dialogID, ParciaisController.workspace).parents("form")
			
			});
		},
		
		popupInserirPeriodoAutomatico: function(isFromModal) {

			ParciaisController.carregaPeb(null);
			
			ParciaisController.popup("#dialog-novo", ParciaisController.gerarPeriodosParcias, isFromModal);
		},
		
		popupInserirPeriodoManual: function(isFromModal) {

			$("#parcial-dataRecolhimentoManual").val("");

			ParciaisController.popup("#dialog-novo-manual", ParciaisController.inserirPeriodo, isFromModal);
		},
		
		popup_detalheVendas : function() {
			
			$( "#dialog-detalhe-venda", ParciaisController.workspace).dialog({
				resizable: false,
				height:450,
				width:660,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
					},
				},
				form: $("#dialog-detalhe-venda", ParciaisController.workspace).parents("form")
			});
		},
		
		popup_alterar : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo", ParciaisController.workspace).dialog({
				resizable: false,
				height:200,
				width:350,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-novo", ParciaisController.workspace).parents("form")
			});	
			      
		},
		
		popup_excluir : function() {
		
			$( "#dialog-excluir", ParciaisController.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						ParciaisController.excluirPeriodoParcial();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-excluir", ParciaisController.workspace).parents("form")
			});
		},
	
		popup_detalhes : function() {
		
			$( "#dialog-detalhes", ParciaisController.workspace).dialog({
				resizable: false,
				height:510,
				width:950,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-detalhes", ParciaisController.workspace).parents("form")
			});
		},
		
		popup_redistribuicao : function() {
		
			$( "#dialog-redistribuicao", ParciaisController.workspace).dialog({
				resizable: false,
				height:550,
				width:550,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-redistribuicao", ParciaisController.workspace).parents("form")
			});
		},
		
		popup_edit_produto : function() {
		
			$( "#dialog-edit-produto", ParciaisController.workspace).dialog({
				resizable: false,
				height:360,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						ParciaisController.validarDiaUtil(
							ParciaisController.editarPeriodoParcial, 
							ParciaisController.getDatasEdicaoPeriodoParaValidacao()
						);						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-edit-produto", ParciaisController.workspace).parents("form")
			});
		},
		
		popup_alert_dia_util : function(confirmAction) {
		
			$( "#dialog-edit-lancamento-dia-util", ParciaisController.workspace).dialog({
				resizable: false,
				height:150,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
					
						confirmAction();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-edit-lancamento-dia-util", ParciaisController.workspace).parents("form")
			});
		},
	
		popupNovaRedistribuicao:function(isNovoItem){
			
			if(isNovoItem){
				ParciaisController.set("parcial-lancamentoNovaRed","");
			}
			
			$( "#dialog-nova-redistribuicao", ParciaisController.workspace).dialog({
				resizable: false,
				height:150,
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						var successCallback;
						var datasValidacao = ParciaisController.getDatasNovaRedistribuicaoParaValidacao();
						
						if (isNovoItem){
							
							successCallback = ParciaisController.incluirNovaRedistribuicao;
						
						} else{
							
							successCallback = ParciaisController.editarRedistribuicao;
						}
						
						ParciaisController.validarDiaUtil(successCallback, datasValidacao);						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-nova-redistribuicao", ParciaisController.workspace).parents("form")
			});
		},
		
		inicializarGrids : function() {
			

			$(".parcial-parciaisGrid", ParciaisController.workspace).flexigrid({
				colModel : [ {
					display : 'Data Lancto',
					name : 'dataLancamento',
					width : 95,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Recolhimento',
					name : 'dataRecolhimento',
					width : 95,
					sortable : true,
					align : 'center'
				}, {
					display : 'Código',
					name : 'codigoProduto',
					width : 65,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 260,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numEdicao',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status',
					name : 'statusParcial',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 40,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigoProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
		}); 	

		$(".grids", ParciaisController.workspace).show();	


		$(".parcial-periodosGrid", ParciaisController.workspace).flexigrid({
				colModel : [ {
					display : 'Período',
					name : 'numeroPeriodo',
					width : 40,
					sortable : false,
					align : 'center'
				},{
					display : 'Lcto',
					name : 'dataLancamento',
					width : 50,
					sortable : false,
					align : 'center'
				},{
					display : 'Rcto',
					name : 'dataRecolhimento',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Redistribuição',
					name : 'suplementacao',
					width : 80,
					sortable : false,
					align : 'center'
				}, {
					display : 'Encalhe',
					name : 'encalhe',
					width : 40,
					sortable : false,
					align : 'center'
				}, {
					display : 'Venda',
					name : 'vendas',
					width : 40,
					sortable : false,
					align : 'center'
				}, {
					display : '% Venda',
					name : 'percVenda',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Venda CE',
					name : 'vendaCE',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Reparte Acum.',
					name : 'reparteAcum',
					width : 75,
					sortable : false,
					align : 'center'
				}, {
					display : 'Venda Acum.',
					name : 'vendaAcumulada',
					width : 70,
					sortable : false,
					align : 'center'
				}, {
					display : '% Venda Acum.',
					name : 'percVendaAcumulada',
					width : 80,
					sortable : false,
					align : 'center'
				},{
					display : 'Lct Prodin',
					name : 'dataLancamentoPrevista',
					width : 55,
					sortable : false,
					align : 'center'
				}, {
					display : 'Rct Prodin',
					name : 'dataRecolhimentoPrevista',
					width : 55,
					sortable : false,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 55,
					sortable : false,
					align : 'center'
				}],

				sortname : "dataLancamento",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 980,
				height : 255
			}); 

		$(".parcial-parciaisPopGrid", ParciaisController.workspace).flexigrid({
				colModel : [{
					display : 'Período',
					name : 'numeroPeriodo',
					width : 40,
					sortable : false,
					align : 'center'
				}, {
					display : 'Lcto',
					name : 'dataLancamento',
					width : 55,
					sortable : false,
					align : 'center'
				},{
					display : 'Rcto',
					name : 'dataRecolhimento',
					width : 55,
					sortable : false,
					align : 'center'
				},{
					display : 'Reparte',
					name : 'reparte',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Redistribuição',
					name : 'suplementacao',
					width : 90,
					sortable : false,
					align : 'center'
				}, {
					display : 'Encalhe',
					name : 'encalhe',
					width : 40,
					sortable : false,
					align : 'center'
				}, {
					display : 'Venda',
					name : 'vendas',
					width : 40,
					sortable : false,
					align : 'center'
				}, {
					display : '% Venda',
					name : 'percVenda',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Venda CE',
					name : 'vendaCE',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Reparte Acum.',
					name : 'reparteAcum',
					width : 75,
					sortable : false,
					align : 'center'
				}, {
					display : 'Venda Acum.',
					name : 'vendaAcumulada',
					width : 70,
					sortable : false,
					align : 'center'
				}, {
					display : '% Venda Acum.',
					name : 'percVendaAcumulada',
					width : 80,
					sortable : false,
					align : 'center'
				},{
					display : 'Lct Prodin',
					name : 'dataLancamentoPrevista',
					width : 55,
					sortable : false,
					align : 'center'
				}, {
					display : 'Rct Prodin',
					name : 'dataRecolhimentoPrevista',
					width : 55,
					sortable : false,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 55,
					sortable : false,
					align : 'center'
				}],
				sortname : "dataLancamento",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 950,
				height : 200
			}); 
			

		$(".parcial-parciaisVendaGrid", ParciaisController.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Juramentada',
				name : 'vendaJuramentada',
				width : 110,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 595,
			height : 200
		});
		
		$(".parcial-parciaisRedistribuicaoGrid", ParciaisController.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'N. Lcto',
				name : 'numeroLancamento',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Lançamento',
				name : 'dataLancamento',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimento',
				width : 150,
				sortable : true,
				align : 'center'
			},{
				display : 'Ação',
				name : 'acao',
				width : 55,
				sortable : false,
				align : 'center'
			}],
			
			showTableToggleBtn : true,
			width : 500,
			height : 230
		});
	}

}, BaseController);

$(function() {
	
	ParciaisController.init();
	bloquearItensEdicao(ParciaisController.workspace);				
});

//@ sourceURL=parciais.js
