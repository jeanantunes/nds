var consultaConsignadoCotaController = $.extend(true, {

	init : function() {
		
		$(".consignadosCotaDetalhesGrid", consultaConsignadoCotaController.workspace).flexigrid({
			preProcess: consultaConsignadoCotaController.executarPreProcessamentoCota,
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
					name : 'precoCapa',
					width : 70,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDesconto',
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
					name : 'total',
					width : 40,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total Desc. $',
					name : 'totalDesconto',
					width : 60,
					sortable : true,
					align : 'right'
				}],
				sortname : "codigoProduto",
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
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Dt. Lancto',
					name : 'dataLancamento',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Dt. Rec.',
					name : 'dataRecolhimento',
					width : 60,
					sortable : true,
					align : 'center'
				},{
					display : 'Preço Capa R$',
					name : 'precoCapa',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDesconto',
					width : 80,
					sortable : true,
					align : 'right'
				},{
					display : 'Reparte',
					name : 'reparte',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total $',
					name : 'total',
					width : 50,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total Desc. $',
					name : 'totalDesconto',
					width : 70,
					sortable : true,
					align : 'right'
				}],
				sortname : "dataLancamento",
				sortorder : "desc",
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
					display : 'Reparte Total',
					name : 'consignado',
					width : 140,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 120,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total Desc. R$',
					name : 'totalDesconto',
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
		
		var cota = $('#codigoCota', consultaConsignadoCotaController.workspace).val().trim();
		var idFornecedor = $('#idFornecedor', consultaConsignadoCotaController.workspace).val();
		
		if(idFornecedor == "-1"){
			idFornecedor = "";
		}else if(idFornecedor == "0"){
			idFornecedor = "";
		}
		
		
		if(!idFornecedor == "" && $("#opcaoDetalhe", consultaConsignadoCotaController.workspace).is(":checked")){
			$("#opcaoDetalhe", consultaConsignadoCotaController.workspace).attr("checked", false);			
		}
		
		if(cota != "" ){
		
			$("#numeroNomeCota", consultaConsignadoCotaController.workspace).html(
				"Consignados da Cota: " + $("#codigoCota", consultaConsignadoCotaController.workspace).val()
				+ " - " + $("#nomeCota", consultaConsignadoCotaController.workspace).html()
			);
			
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
	
//	popularTotal : function(){
//		
// 		$.postJSON(
//			contextPath + '/financeiro/consultaConsignadoCota/buscarTotalGeralCota',
//			[{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
//			{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}],
//			function(result) {
//				var idFornecedor = $('#idFornecedor', consultaConsignadoCotaController.workspace).val();				
//				if( idFornecedor != "0"){
//					$("#totalGeralCota", consultaConsignadoCotaController.workspace).hide();
//					$('.tabelaGeralDetalhado', consultaConsignadoCotaController.workspace).hide();
//					$('.tabelaGeralPorFornecedor', consultaConsignadoCotaController.workspace).show();
//					$("#totalGeralPorFornecedor", consultaConsignadoCotaController.workspace).html(" <table width='190' border='0' cellspacing='1' cellpadding='1' align='right'>" +
//							"<tr> <td><strong>Total Geral:</strong></td>" +
//        						" <td>&nbsp;</td> "+
//        						" <td align='right'><strong>"+result+"</strong></td></tr>");
//				}else{
//					$("#totalGeralCota", consultaConsignadoCotaController.workspace).show();
//					$(".tabelaGeralDetalhado", consultaConsignadoCotaController.workspace).hide();
//					$(".tabelaGeralPorFornecedor", consultaConsignadoCotaController.workspace).hide();
//					$("#totalGeralCota", consultaConsignadoCotaController.workspace).html(" <table width='190' border='0' cellspacing='1' cellpadding='1' align='right' >" +
//												"<tr> <td><strong>Total Geral:</strong></td>" +
//			                						" <td>&nbsp;</td> "+
//			                						" <td align='right'><strong>"+result+"</strong></td></tr>");					
//				} 
//				
//			},
//			null,
//			true
//		);
//		
//	},
	
	popularTotal : function(){
		
 		$.postJSON(
			contextPath + '/financeiro/consultaConsignadoCota/buscarTotalGeralCota',
			[{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
			{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}],
			function(result) {

				$("#totalGeralCota", consultaConsignadoCotaController.workspace).hide();
				$('.tabelaGeralDetalhado', consultaConsignadoCotaController.workspace).hide();
				$('.tabelaGeralPorFornecedor', consultaConsignadoCotaController.workspace).show();
				
				var htmlTotais =
					" <table width='450' border='0' cellspacing='1' cellpadding='1' align='right'>"
					// + "<tr> <td><strong>Total Geral:</strong></td>"
					+ "<tr> <td><a href='javascript:;' onclick='consultaConsignadoCotaController.obterTotalConsiganadoAvista();'><strong>Total Geral:</strong></a></td> " 
					
					+ " <td>&nbsp;</td> "
					+ " <td align='right'><strong>" + result.totalGeral + "</strong></td></tr>";
				
				if (result.totaisFornecedores) {
					
					$.each(result.totaisFornecedores, function(index, value) {
								
						htmlTotais += "<tr>";
						
						if (index == 0) {
							htmlTotais += "<td width='71'><strong>Total por Fornecedor:</strong></td>";		
						} else {
							htmlTotais += "<td>&nbsp;</td>";
						}
						
						htmlTotais += "<td width='90'><strong>" + value.nomeFornecedor + "</strong></td>";
						htmlTotais += "<td width='60' align='right'><strong>" + value.totalFormatado + "</strong></td>";
						htmlTotais += "</tr>";
					});
				}
				
				$("#totalGeralPorFornecedor", consultaConsignadoCotaController.workspace).html(htmlTotais);
			},
			null,
			true
		);
		
	},
	
	obterTotalConsiganadoAvista : function(){
		
		$.postJSON(
			contextPath + '/financeiro/consultaConsignadoCota/buscarTotalGeralAvistaCota',
			[{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
			{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}],
			function(result) {
				$("#dialog-total-consignado-avista", consultaConsignadoCotaController.workspace).html(result);
				    $( "#dialog-total-consignado-avista" ).dialog({
				      resizable: false,
				      height: "auto",
				      width: 400,
				      modal: true,
				      buttons: {
				        "Fechar ": function() {
				        	$( this ).dialog( "close" );
				        },
				      }
				    });
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
				$("#totalGeralCota", consultaConsignadoCotaController.workspace).hide();
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
	
	mostra_detalhes : function(idCota, idFornecedor,nomeCota) {
		
		$("#numeroNomeCotaPopUp").html("Dados da Cota: " + idCota + " - " + nomeCota);
		
		$(".consignadosCotaDetalhesGrid").flexOptions({
			url: contextPath + "/financeiro/consultaConsignadoCota/pesquisarConsignadoCota",
			params: [
						{name:'filtro.idCota', value:idCota},
						{name:'filtro.idFornecedor', value:idFornecedor}
						]
		});		
		
		$(".consignadosCotaDetalhesGrid").flexReload();
	},
	
	exibirDialogCota:function(){
		
		$( "#dialog-detalhes").dialog({
			resizable: false,
			height:370,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-detalhes", this.workspace).parents("form")
		});
	},

	executarPreProcessamentoCota : function(resultado) {
		
		$.each(resultado.rows, function(index, row) {				
		   	
		   	row.cell.precoCapa = row.cell.precoCapaFormatado;
		   	row.cell.precoDesconto = row.cell.precoDescontoFormatado;
		   	row.cell.total = row.cell.totalFormatado;
			row.cell.totalDesconto = row.cell.totalDescontoFormatado;		
		});

		consultaConsignadoCotaController.exibirDialogCota();
		
		return resultado;
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
		   	
			var nomeCota  = "'"+row.cell.nomeCota+"'";
			
			var linkAcao = '<a href="javascript:;" onclick="consultaConsignadoCotaController.mostra_detalhes('+row.cell.numeroCota+','+row.cell.idFornecedor+','+nomeCota+');" style="cursor:pointer">' +
						   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
	                         '</a>';   
		   	
		   	row.cell.precoCapa = row.cell.precoCapaFormatado;
		   	row.cell.precoDesconto = row.cell.precoDescontoFormatado;
		   	row.cell.total = row.cell.totalFormatado;
			row.cell.totalDesconto = row.cell.totalDescontoFormatado;
			
			row.cell.acao = linkAcao;			
		});
		
		$(".grids", consultaConsignadoCotaController.workspace).show();
		
		consultaConsignadoCotaController.mostrarGrid();
		
		return resultado;
	},
	
	pesquisarCota : function() {
 		
		numeroCota = $("#codigoCota", consultaConsignadoCotaController.workspace).val();
 		
		numeroCota = $.trim(numeroCota);
		
		if (numeroCota.length < 1) {
			
			$("#nomeCota", consultaConsignadoCotaController.workspace).html("");
			
			return;
		}
		
 		$.postJSON(
			contextPath + "/financeiro/consultaConsignadoCota/buscarCotaPorNumero",
			{ "numeroCota": numeroCota },
			function(result) {
				$("#nomeCota", consultaConsignadoCotaController.workspace).html(result);
			},
			function() {
			    $("#nomeCota", consultaConsignadoCotaController.workspace).html("");
			    $("#codigoCota", consultaConsignadoCotaController.workspace).val("");
			},
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
//@ sourceURL=consultaConsignadoCota.js