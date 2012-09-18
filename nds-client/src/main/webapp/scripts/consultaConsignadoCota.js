var consultaConsignadoCotaController = $.extend(true, {

	init : function() {
		$(".consignadosCotaDetalhesGrid", consultaConsignadoCotaController.workspace).flexigrid({
			preProcess: consultaConsignadoCotaController.executarPreProcessamento,
			dataType : 'json',
				colModel : [  {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Dt. Lancto',
					name : 'dataLancamento',
					width : 70,
					sortable : true,
					align : 'center'
				},{
					display : 'Preço Capa R$',
					name : 'precoCapaFormatado',
					width : 70,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDescontoFormatado',
					width : 70,
					sortable : true,
					align : 'right'
				},{
					display : 'Reparte',
					name : 'reparte',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total $',
					name : 'totalFormatado',
					width : 40,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total Desc. $',
					name : 'totalDescontoFormatado',
					width : 60,
					sortable : true,
					align : 'right'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 800,
				height : 180
			});
		
		$(".consignadosCotaGrid", consultaConsignadoCotaController.workspace).flexigrid({
			preProcess: consultaConsignadoCotaController.executarPreProcessamento,
			dataType : 'json',
				colModel : [  {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 135,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Dt. Lancto',
					name : 'dataLancamento',
					width : 80,
					sortable : true,
					align : 'center'
				},{
					display : 'Preço Capa R$',
					name : 'precoCapaFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDescontoFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				},{
					display : 'Reparte',
					name : 'reparte',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total $',
					name : 'totalFormatado',
					width : 70,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total Desc. $',
					name : 'totalDescontoFormatado',
					width : 70,
					sortable : true,
					align : 'right'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});

	$(".consignadosGrid", consultaConsignadoCotaController.workspace).flexigrid({
			preProcess: consultaConsignadoCotaController.executarPreProcessamento,
			dataType : 'json',
				colModel : [  {
					display : 'Cota',
					name : 'numeroCota',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Consignado Total',
					name : 'reparte',
					width : 140,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'totalFormatado',
					width : 120,
					sortable : true,
					align : 'right'
				}, {
					display : 'Consignado c/ Desc. R$',
					name : 'totalDescontoFormatado',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 40,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		
	},
	
	gridCota : function(){
		$('.pesqCota', consultaConsignadoCotaController.workspace).show();
		$('.pesqTodos', consultaConsignadoCotaController.workspace).hide();
		$('.dados', consultaConsignadoCotaController.workspace).show();
	},
	
	gridTotal : function(){
		$('.pesqCota', consultaConsignadoCotaController.workspace).hide();
		$('.pesqTodos', consultaConsignadoCotaController.workspace).show();
		$('.dados', consultaConsignadoCotaController.workspace).hide();
	},
	
	pesquisar : function(){
		
		var cota = $('#codigoCota', consultaConsignadoCotaController.workspace).val();
		var idFornecedor = $('#idFornecedor', consultaConsignadoCotaController.workspace).val();
		
		if(idFornecedor == "-1"){
			idFornecedor = ""
		}else if(idFornecedor == "0"){
			idFornecedor = "";
		}
		
		
		if(!idFornecedor == "" && $("#opcaoDetalhe", consultaConsignadoCotaController.workspace).is(":checked")){
			$("#opcaoDetalhe", consultaConsignadoCotaController.workspace).attr("checked", false);			
		}
		
		if(cota != "" && idFornecedor == "" ){
			
			$(".consignadosCotaGrid", consultaConsignadoCotaController.workspace).flexOptions({
				url: contextPath + "/financeiro/consultaConsignadoCota/pesquisarConsignadoCota",
				dataType : 'json',
				params: [
							{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
							{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}
							]
			});
			
			$(".consignadosCotaGrid", consultaConsignadoCotaController.workspace).flexReload();
			$('#valorGrid', consultaConsignadoCotaController.workspace).val('GridCota');
			consultaConsignadoCotaController.popularTotal();			
		}else{
			$(".consignadosGrid", consultaConsignadoCotaController.workspace).flexOptions({
				url: contextPath + "/financeiro/consultaConsignadoCota/pesquisarMovimentoCotaPeloFornecedor",
				dataType : 'json',
				params: [
							{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
							{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}
							]
			});
			
			$(".consignadosGrid", consultaConsignadoCotaController.workspace).flexReload();
			$('#valorGrid', consultaConsignadoCotaController.workspace).val('GridTotal');
			
			if($("#opcaoDetalhe", consultaConsignadoCotaController.workspace).is(":checked")){
				consultaConsignadoCotaController.popularTotalDetalhado();
			}else{
				consultaConsignadoCotaController.popularTotal();
			}
			
		}
	},
	
	popularTotal : function(){
		
 		$.postJSON(
			contextPath + '/financeiro/consultaConsignadoCota/buscarTotalGeralCota',
			[{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
			{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}],
			function(result) {
				var idFornecedor = $('#idFornecedor', consultaConsignadoCotaController.workspace).val();				
				if( idFornecedor != "0"){
					$('.tabelaGeralDetalhado', consultaConsignadoCotaController.workspace).hide();
					$('.tabelaGeralPorFornecedor', consultaConsignadoCotaController.workspace).show();
					$("#totalGeralPorFornecedor", consultaConsignadoCotaController.workspace).html(" <table width='190' border='0' cellspacing='1' cellpadding='1' align='right'>" +
							"<tr> <td><strong>Total Geral:</strong></td>" +
        						" <td>&nbsp;</td> "+
        						" <td align='right'><strong>"+result+"</strong></td></tr>");
				}else{					
					$("#totalGeralCota", consultaConsignadoCotaController.workspace).html(" <table width='190' border='0' cellspacing='1' cellpadding='1' align='right' >" +
												"<tr> <td><strong>Total Geral:</strong></td>" +
			                						" <td>&nbsp;</td> "+
			                						" <td align='right'><strong>"+result+"</strong></td></tr>");					
				} 
				
			},
			null,
			true
		);
		
	},
	
	popularTotalDetalhado : function(){
		
 		$.postJSON(
			contextPath + '/financeiro/consultaConsignadoCota/buscarTotalGeralDetalhado',
			[{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
			{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}],
			function(result) {
				$('.tabelaGeralDetalhado', consultaConsignadoCotaController.workspace).show();
				$('.tabelaGeralPorFornecedor', consultaConsignadoCotaController.workspace).hide();
				$("#totalGeralDetalhado", consultaConsignadoCotaController.workspace).html(result);
			},
			null,
			true
		);
		
	},
	
	mostrarGrid : function(){
		var valorGrid = $('#valorGrid', consultaConsignadoCotaController.workspace).val();
		
		if(valorGrid == 'GridTotal'){
			consultaConsignadoCotaController.gridTotal();
		}else if(valorGrid == 'GridPopUp'){
			
		}else{
			consultaConsignadoCotaController.gridCota();
		}
		
	},
	
	mostra_detalhes : function(idCota, idFornecedor) {
		
		consultaConsignadoCotaController.popularPopUp(idCota, idFornecedor);
	
		$( "#dialog-detalhes", consultaConsignadoCotaController.workspace ).dialog({
			resizable: false,
			height:370,
			width:860,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-detalhes", this.workspace).parents("form")
		});
	},
	
	popularPopUp : function(idCota, idFornecedor){
		
		$(".consignadosCotaDetalhesGrid", consultaConsignadoCotaController.workspace).flexOptions({
			url: contextPath + "/financeiro/consultaConsignadoCota/pesquisarConsignadoCota",
			dataType : 'json',
			params: [
						{name:'filtro.idCota', value:idCota},
						{name:'filtro.idFornecedor', value:idFornecedor}
						]
		});		
		$(".consignadosCotaDetalhesGrid", consultaConsignadoCotaController.workspace).flexReload();
		
		$.postJSON(
				contextPath + '/financeiro/consultaConsignadoCota/buscarCotaPorNumero',
				{ "numeroCota": idCota },
				function(result) {								  
					$("#numeroNomeCotaPopUp", consultaConsignadoCotaController.workspace).html("Dados da Cota: " + idCota + " - " + result);
				},
				null,
				true
			);
		$('#valorGrid', consultaConsignadoCotaController.workspace).val('GridPopUp');
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", consultaConsignadoCotaController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {				
		   	var linkAcao = '<a href="javascript:;" onclick="consultaConsignadoCotaController.mostra_detalhes('+row.cell.numeroCota+','+row.cell.idFornecedor+');" style="cursor:pointer">' +
						   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
	                         '</a>';           
			
			row.cell.acao = linkAcao;			
		});
		
		consultaConsignadoCotaController.mostrarGrid();
		
		return resultado;
	},
	
	pesquisarCota : function() {
 		
		numeroCota = $("#codigoCota", consultaConsignadoCotaController.workspace).val();
 		
 		$.postJSON(
			contextPath + "/financeiro/consultaConsignadoCota/buscarCotaPorNumero",
			{ "numeroCota": numeroCota },
			function(result) {
				$("#nomeCota", consultaConsignadoCotaController.workspace).html(result);				  
				$("#numeroNomeCota", consultaConsignadoCotaController.workspace).html("Consignados da Cota: " + numeroCota + " - " + result);
			},
			null,
			true
		);
 	},
	
	detalharTodos : function(opcao) {
		//var detalhar = document.getElementById("detalhes");
		
		switch (opcao) {   
			case '-1':   
				$("#detalhes", consultaConsignadoCotaController.workspace).show();
				//detalhar.style.display = ""; 
			  
			break;		
			default:   
				$("#detalhes", consultaConsignadoCotaController.workspace).hide();
				//detalhar.style.display = "none";			
			break;   
		}   
	}

}, BaseController);
