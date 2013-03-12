var historicoVendaController = $.extend(true, {
	
	errorCallBack : function errorCallBack(){
		$('#statusCota').val('');
		$('#filtroPrincipalNumeroCota').val('');
		$('#filtroPrincipalNomePessoa').val('');
	},
	
	init : function() {
	
		var flexGridService = new FlexGridService(),
			pesquisaCota = new PesquisaCota(),
			pesquisaProduto = new PesquisaProduto();
		
		// #### ASSOCIANDO OS EVENTOS NO DOM ####

		$("#componente").change(function(){
			  carregarCombo(contextPath + "/distribuicao/historicoVenda/carregarElementos", 
					  {"componente":$("#componente").val()},
			            $("#elemento", this.workspace), null, null);
		});
		
		// ### POR PRODUTO ###
		$('#pesquisaFiltroProduto').click(function (){
			
			var filtro = [];
				filtro = $('#pesquisaPorProduto').serializeArray();
			
			historicoVendaController.Grids.EdicaoProdCadastradosGrid.reload({
				params : filtro
			});
		});

		$('#numeroCota').change(function (){
			pesquisaCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota');
		});
		
		$('#nomeCota').keyup(function (){
			pesquisaCota.autoCompletarPorNome('#nomeCota');
		});
		
//		$('#nomeCota').blur(function (){
//			pesquisaCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota');
//		});
		
		$('#filtroCodigoProduto').change(function (){
			pesquisaProduto.pesquisarPorCodigoProduto('#filtroCodigoProduto', '#filtroNomeProduto', {}, false, undefined, historicoVendaController.errorCallBack);
		});
		
		$('#filtroNomeProduto').keyup(function (){
			pesquisaProduto.autoCompletarPorNomeProduto('#filtroNomeProduto', false);
		});
		
		$('#filtroNomeProduto').blur(function (){
			pesquisaProduto.pesquisarPorNomeProduto('#filtroCodigoProduto', '#filtroNomeProduto', {}, false, undefined, historicoVendaController.errorCallBack);
		});
		
		// PESQUISA POR REPARTE
		
		$('#pesquisaPorReparte').click(function (){

			url = contextPath + "/distribuicao/historicoVenda/pesquisaCotaPorReparte";
			
			historicoVendaController.pesquisarCotasHistorico(url);
		});
		
		// EXPORTAÇÃO
//		$('#porCotaGerarPDF').attr('href', contextPath + "/distribuicao/classificacaoNaoRecebida/exportar?fileType=PDF&porCota=true");
//		
//		$('#porCotaGerarXLS').attr('href', contextPath + "/distribuicao/classificacaoNaoRecebida/exportar?fileType=XLS&porCota=true");
		
		// URLs usadas para requisições post (Inserção e Deleção)
		historicoVendaController.Url = {
				// URLs aqui
		},
		
		historicoVendaController.Util = {
			getFiltroByForm : function(idForm){
				var filtro;
				
				if(idForm === undefined){
					return null;
				}else {
					filtro = $('#' + idForm).serializeArray();
					
					for ( var index in filtro) {
						if (filtro[index].value === "on") {
							filtro[index].value = true; 
						}else if (filtro[index].value === "off") {
							filtro[index].value = false; 
						}
					}

					return filtro;
				}
			},
		};
		
		historicoVendaController.Grids = {
			EdicaoProdCadastradosGrid : flexGridService.GridFactory.createGrid({
				gridName : "edicaoProdCadastradosGrid",
				url : contextPath + "/distribuicao/historicoVenda/pesquisaProduto",
				cached : true,
				inputModel : [{
						element : "input",
						inputType : "checkbox",
						attributeName : "edicaoSelecionada",
						cellValue : "numeroEdicao",
						columnName : "sel",
						events : [{
							type : "onchange",
							functionName : "historicoVendaController.addSelectedRowToGrid",
							parameter : "rowId"
						}]
					},
					{
						element : "img",
						columnName : "chamadaCapa",
						fileReference : "images/ico_detalhes.png",
						style : "cursor:pointer",
						events : [{
							functionName : "historicoVendaController.popUpCapaOpen",
							type : "onmouseover",
							parameter : "codigoProduto,numeroEdicao"
						},{
							functionName : "historicoVendaController.popUpCapaClose",
							type : "onmouseout",
						}]
					}
				],
				gridConfiguration : {
					dataType: 'json',
					colModel : [ {
						display : 'Edição',
						name : 'numeroEdicao',
						width : 40,
						sortable : true,
						align : 'left'
					},{
						display : 'Período',
						name : 'periodo',
						width : 40,
						sortable : true,
						align : 'center'
					}, {
						display : 'Dt. Lançamento',
						name : 'dataLancamentoFormatada',
						width : 80,
						sortable : true,
						align : 'center'
					}, {
						display : 'Reparte',
						name : 'repartePrevisto',
						width : 40,
						sortable : true,
						align : 'right'
					}, {
						display : 'Venda',
						name : 'qtdeVendas',
						width : 35,
						sortable : true,
						align : 'right'
					}, {
						display : 'Status',
						name : 'situacaoLancamento',
						width : 80,
						sortable : true,
						align : 'left'
					}, {
						display : 'Capa',
						name : 'chamadaCapa',
						width : 30,
						sortable : true,
						align : 'center'
					}, {
						display : '',
						name : 'sel',
						width : 20,
						sortable : true,
						align : 'center'
					}],
					width : 480,
					height : 160
				}
			}),
			EdicaoSelecionadaGrid : flexGridService.GridFactory.createGrid({
				gridName : "edicaoSelecionadaGrid",
				cached : true,
				inputModel : [{
						element : "img",
						columnName : "acao",
						fileReference : "images/ico_excluir.gif",
						style : "cursor:pointer",
						events : [{
							functionName : "historicoVendaController.removeRowfromGridProduto",
							type : "onclick",
							parameter : "rowId"
							
						}]
					}
				],
				gridConfiguration : {
					dataType : 'json',
					colModel : [ {
						display : 'Código',
						name : 'codigoProduto',
						width : 45,
						sortable : true,
						align : 'left'
					},{
						display : 'Produto',
						name : 'nomeProduto',
						width : 140,
						sortable : true,
						align : 'left'
					},{
						display : 'Edição',
						name : 'numeroEdicao',
						width : 40,
						sortable : true,
						align : 'left'
					},{
						display : 'Período',
						name : 'periodo',
						width : 40,
						sortable : true,
						align : 'center'
					}, {
						display : 'Reparte',
						name : 'reparte',
						width : 40,
						sortable : true,
						align : 'right'
					}, {
						display : 'Venda',
						name : 'qtdeVendas',
						width : 40,
						sortable : true,
						align : 'right'
					}, {
						display : 'Ação',
						name : 'acao',
						width : 30,
						sortable : true,
						align : 'center'
					}],
					width : 480,
					height : 110,
				}
			}),
			PesqHistoricoGrid : flexGridService.GridFactory.createGrid({
				gridName : "pesqHistoricoGrid",
				cached : true,
				inputModel : [{
						element : "img",
						columnName : "acao",
						fileReference : "images/ico_excluir.gif",
						style : "cursor:pointer",
						events : [{
							functionName : "historicoVendaController.removeRowfromGridCota",
							type : "onclick",
							parameter : "rowId"
						}]
					}
				],
				gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 60,
							sortable : true,
							align : 'left'
						},  {
							display : 'Nome',
							name : 'nomePessoa',
							width : 270,
							sortable : true,
							align : 'left'
						},  {
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : true,
							align : 'center'
						}],
						width : 415,
						height : 140
					}
			})
			
		};
	},
	
	pesquisarCotasHistorico : function pesquisarCotasPorHistorio(url){
		var filtro = [];
			produtosSelecionados = [],
			grids = historicoVendaController.Grids,
			filtro = $('#filtroHistoricoVenda').serializeArray();
	
		filtro.push({
			name : "filtro.cotasAtivas",
			value : $('#cotasAtivas').is(':checked')
		});
		
		for ( var i in grids.EdicaoSelecionadaGrid.tableModel.rows) {
			row = grids.EdicaoSelecionadaGrid.tableModel.rows[i];
			filtro.push({name : "filtro.listProdutoEdicaoDTO["+i+"].numeroEdicao", value :  row.cell.numeroEdicao});
			filtro.push({name : "filtro.listProdutoEdicaoDTO["+i+"].codigoProduto", value :  row.cell.codigoProduto});
		}
		
		grids.PesqHistoricoGrid.reload({
			url : url,
			params : filtro
		});
	},
	
	removeRowfromGridCota : function removeRowfromGrid(rowId){
		var grids = historicoVendaController.Grids,
			row;
		
		for ( var index in grids.PesqHistoricoGrid.tableModel.rows) {
			if (grids.PesqHistoricoGrid.tableModel.rows[index].id == rowId) {
				row =  grids.PesqHistoricoGrid.tableModel.rows[index];
			}
		}
		
		grids.PesqHistoricoGrid.removeRow(rowId);
		
	},
	
	removeRowfromGridProduto : function removeRowfromGrid(rowId){
		var grids = historicoVendaController.Grids,
			row;
		
		for ( var index in grids.EdicaoSelecionadaGrid.tableModel.rows) {
			if (grids.EdicaoSelecionadaGrid.tableModel.rows[index].id == rowId) {
				row =  grids.EdicaoSelecionadaGrid.tableModel.rows[index];
			}
		}
		
		if (row.targetCheckbox) {
			row.targetCheckbox.checked = false;
		}
		
		grids.EdicaoSelecionadaGrid.removeRow(rowId);
		
	},
	
	addSelectedRowToGrid : function addSelectedRowToGrid(rowId, event){
		var	grids = historicoVendaController.Grids,
			containsElement = {
				contain : false,
				rowId : 0
		};
		
		row = grids.EdicaoProdCadastradosGrid.tableModel.rows[rowId -1];
		
		// guarda uma referência do checkbox
		row["targetCheckbox"] = event.target;
		
		if (grids.EdicaoSelecionadaGrid.tableModel.rows) {
			for ( var index in grids.EdicaoSelecionadaGrid.tableModel.rows) {
				if(grids.EdicaoSelecionadaGrid.tableModel.rows[index].cell.numeroEdicao == row.cell.numeroEdicao){
					containsElement.contain = true;
					containsElement.rowId = grids.EdicaoSelecionadaGrid.tableModel.rows[index].id;
				}
			}
		}
		
		if (containsElement.contain) {
			grids.EdicaoSelecionadaGrid.removeRow(containsElement.rowId);
		}else {
			if(grids.EdicaoSelecionadaGrid.tableModel.rows.length < 6){
				grids.EdicaoSelecionadaGrid.addData(row);
			}else {
				event.target.checked = false;
				exibirMensagem("WARNING",
						["Não pode adicionar mais produtos"]);
			}
		}
	},
	
	popUpCapaOpen : function popUpCapaOpen(codigoProduto, numeroEdicao,event) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		produto = {
				codigoProduto : codigoProduto,
				numeroEdicao : numeroEdicao
		},
		
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
			open: historicoVendaController.open(event, produto),
			close : function(){
				$( "#dialog-detalhes" ).dialog( "close" );
			},
			position: { my: "left", at: "right", of: event.target }
		});
	},
	
	popUpCapaClose : function popUpCapaClose() {
		$( "#dialog-detalhes" ).dialog( "close" );
	},
	
	open : function(event,produto) {
	
	   var randomnumber=Math.floor(Math.random()*11);
	   
	   $("#imagemCapaEdicao")
	     .attr("src",contextPath
	         + "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
	         + produto.codigoProduto
	         + "&numeroEdicao="
	         + produto.numeroEdicao);
	   console.log($("#imagemCapaEdicao").attr("src"));
	  },
	
	
}, BaseController);
//@ sourceURL=historicoVenda.js