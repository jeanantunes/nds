var historicoVendaController = $.extend(true, {
	
	Grids : {},
	
	flexGridService : new FlexGridService(),
	
	errorCallBack : function errorCallBack(){
		$('#statusCota', historicoVendaController.workspace).val('');
		$('#filtroPrincipalNumeroCota', historicoVendaController.workspace).val('');
		$('#filtroPrincipalNomePessoa', historicoVendaController.workspace).val('');
	},
	
	init : function() {
	
		var flexGridService = new FlexGridService(),
			pesquisaCota = new PesquisaCota(),
			pesquisaProduto = new PesquisaProduto(),
			autoComp = new AutoCompleteCampos(historicoVendaController.workspace);
		
		
		// #### ASSOCIANDO OS EVENTOS NO DOM ####

		
		$("#componente", historicoVendaController.workspace).change(function(){
			selectedItem = $("#componente", historicoVendaController.workspace).val();
			
			if(selectedItem != 'Selecione...'){
				carregarCombo(contextPath + "/distribuicao/historicoVenda/carregarElementos", 
					  {"componente":selectedItem},
			            $("#elemento", this.workspace), null, null);
			} else {
				$('#elemento', historicoVendaController.workspace).html('');
				$('#elemento', historicoVendaController.workspace).append("<option value='-1'>Selecione...</option>");
			}
		});
		
		// ### POR PRODUTO ###
		$('#pesquisaFiltroProduto', historicoVendaController.workspace).click(function (){
			
			var filtro = [];
				filtro = $('#pesquisaPorProduto', historicoVendaController.workspace).serializeArray();
			historicoVendaController.Grids.EdicaoProdCadastradosGrid.reload({
				params : filtro
			});
		});

		$('#numeroCota', historicoVendaController.workspace).change(function (){
			autoComp.pesquisarPorCodigo("/cadastro/cota/pesquisarPorNumero", '#numeroCota', '#nomePessoa', 'numeroCota', 'nome', 'numero');
		});
		
		$('#nomePessoa', historicoVendaController.workspace).keyup(function (){
			autoComp.autoCompletarPorNome("/cadastro/cota/autoCompletarPorNome",'#numeroCota', '#nomePessoa', "nomeCota", 2);
		});
		
		$('#filtroCodigoProduto', historicoVendaController.workspace).change(function (){
			pesquisaProduto.pesquisarPorCodigoProduto('#filtroCodigoProduto', '#filtroNomeProduto', {}, false, undefined, historicoVendaController.errorCallBack);
		});
		
		$('#filtroNomeProduto', historicoVendaController.workspace).keyup(function (){
			pesquisaProduto.autoCompletarPorNomeProduto('#filtroNomeProduto', false);
		});
		
		$('#filtroNomeProduto', historicoVendaController.workspace).blur(function (){
			pesquisaProduto.pesquisarPorNomeProduto('#filtroCodigoProduto', '#filtroNomeProduto', {}, false, undefined, historicoVendaController.errorCallBack);
		});
		
		// PESQUISA POR REPARTE
		
		$('#pesquisaPorQtdReparte', historicoVendaController.workspace).click(function (){

			url = contextPath + "/distribuicao/historicoVenda/pesquisaCotaPorQtdReparte";
			
			historicoVendaController.pesquisarCotasHistorico(url);
		});
		
		$('#pesquisaPorQtdVenda', historicoVendaController.workspace).click(function (){

			url = contextPath + "/distribuicao/historicoVenda/pesquisaCotaPorQtdVenda";
			
			historicoVendaController.pesquisarCotasHistorico(url);
		});
		
		$('#pesquisaPorPercentualVenda', historicoVendaController.workspace).click(function (){

			url = contextPath + "/distribuicao/historicoVenda/pesquisaCotaPorPercentualVenda";
			
			historicoVendaController.pesquisarCotasHistorico(url);
		});

		$('#pesquisaPorComponenentes', historicoVendaController.workspace).click(function (){
			
			url = contextPath + "/distribuicao/historicoVenda/pesquisaCotaPorComponentes";
			
			historicoVendaController.pesquisarCotasHistoricoCotaOuComponentes(url);
		});
		
		$('#pesquisaCotaPorNumeroOuNome', historicoVendaController.workspace).click(function (){

			url = contextPath + "/distribuicao/historicoVenda/pesquisaCotaPorNumeroOuNome";
			
			historicoVendaController.pesquisarCotasHistoricoCotaOuComponentes(url);
		});
		
		$('#pesquisarTodasAsCotas', historicoVendaController.workspace).click(function (){

			url = contextPath + "/distribuicao/historicoVenda/pesquisarTodasAsCotas";
			
			historicoVendaController.pesquisarCotasHistoricoCotaOuComponentes(url);
		});
		
		
		// Botão Cancelar
		$('#botaoCancelar', historicoVendaController.workspace).click(historicoVendaController.botaoCancelar);
		
		$('#analiseHistorico', historicoVendaController.workspace).click(function (){
			var grids = historicoVendaController.Grids,
				params = [];

			if (!grids.PesqHistoricoGrid.tableModel.rows || grids.PesqHistoricoGrid.tableModel.rows.length == 0) {
				
				exibirMensagem("WARNING", ["Nenhuma cota foi informada"]);
				
				return;
			}
			
			url = contextPath + "/distribuicao/historicoVenda/analiseHistorico";
			
			for ( var index in grids.PesqHistoricoGrid.tableModel.rows) {
				row = grids.PesqHistoricoGrid.tableModel.rows[index];
				params.push({name : "cotas["+index+"]", value :  row.cell.numeroCota});
			}
			
			for ( var i in grids.EdicaoSelecionadaGrid.tableModel.rows) {
				row = grids.EdicaoSelecionadaGrid.tableModel.rows[i];
				params.push({name : "listProdutoEdicaoDto["+i+"].numeroEdicao", value :  row.cell.numeroEdicao});
				params.push({name : "listProdutoEdicaoDto["+i+"].codigoProduto", value :  row.cell.codigoProduto});
			}
			
			$.post(url, params, function(data){
			      if(data){ 
			    	  $("#baseAnalise", historicoVendaController.workspace).hide();
			    	  $("#analiseHistorico", historicoVendaController.workspace).hide();
			    	  $('#analiseHistoricoContent', historicoVendaController.workspace).html(data);
			    	  $('#analiseHistoricoContent', historicoVendaController.workspace).show();
			    	  
			    	  analiseHistoricoVendaController.Grids.BaseHistoricoGrid.reload();
			    	  
			    	  // limpando conteúdo
			    	  // tr produto
			    	  $('#analiseHistoricoPopUpNomeProduto', historicoVendaController.workspace).html('');
			    	  $('#analiseHistoricoPopUpNomeProduto', historicoVendaController.workspace).append(
			    			  "<td class='class_linha_1' width = '39%'><strong>Produto:</strong></td>");
			    	  
			    	  // tr numeroEdicao
			    	  $('#analiseHistoricoPopUpNumeroEdicao', historicoVendaController.workspace).html('');
			    	  $('#analiseHistoricoPopUpNumeroEdicao', historicoVendaController.workspace).append(
			    			  "<td class='class_linha_1'  ><strong>Edição:</strong></td>");
			    	  
			    	  // tr dataLancamento
			    	  $('#analiseHistoricoPopUpDatalancamento', historicoVendaController.workspace).html('');
			    	  $('#analiseHistoricoPopUpDatalancamento', historicoVendaController.workspace).append(
			    			  "<td class='class_linha_2' ><strong>Data Lançamento:</strong></td>");
			    	  
			    	  // tr reparte
			    	  $('#analiseHistoricoPopUpReparte', historicoVendaController.workspace).html('');
			    	  $('#analiseHistoricoPopUpReparte', historicoVendaController.workspace).append(
			    			  "<td class='class_linha_1' ><strong>Reparte:</strong></td>");
			    	  
			    	  // tr venda
			    	  $('#analiseHistoricoPopUpVenda', historicoVendaController.workspace).html('');
			    	  $('#analiseHistoricoPopUpVenda', historicoVendaController.workspace).append(
			    			  "<td class='class_linha_2' ><strong>Venda:</strong></td>");
			    	 
			    	  var tm_ordenado = grids.EdicaoSelecionadaGrid.tableModel;

			    	  var size_tm_ordenado = tm_ordenado.rows.length;

			    	  // carregando popUp_analiseHistoricoVenda
			    	  for ( var int = 0; int < size_tm_ordenado; int++) {
			    		  
			    		  row = tm_ordenado.rows[int];
			    		  
			    	      $('#analiseHistoricoPopUpNomeProduto', historicoVendaController.workspace).append(
			    	    		  '<td align="center" class="class_linha_1">'+row.cell.nomeProduto+'</td>');
			    	      
			    	      $('#analiseHistoricoPopUpNumeroEdicao', historicoVendaController.workspace).append(
			    	    		  '<td align="center" class="class_linha_1">'+row.cell.numeroEdicao+'</td>');
			    	      
			    	      $('#analiseHistoricoPopUpDatalancamento', historicoVendaController.workspace).append(
			    	    		  '<td width="130" align="center" class="class_linha_2">' + row.cell.dataLancamentoFormatada + '</td>');
			    	      
			    	      $('#analiseHistoricoPopUpReparte', historicoVendaController.workspace).append(
			    	    		  '<td align="center" class="class_linha_1">' + row.cell.repartePrevisto +'</td>');
			    	      
			    	      $('#analiseHistoricoPopUpVenda', historicoVendaController.workspace).append(
			    	    		  '<td align="center" class="class_linha_1">' + row.cell.qtdVendasFormatada + '</td>');
			    	  }		    
			    	  
			    	  size_tm_ordenado = 6 - tm_ordenado.rows.length; 

			    	  // por estética de layout, insiro elementos td vazios
			    	  for ( var int = 0; int < size_tm_ordenado; int++) {
			    		  $('#analiseHistoricoPopUpNomeProduto', historicoVendaController.workspace).append('<td class="class_linha_1"></td>');
			    	      $('#analiseHistoricoPopUpNumeroEdicao', historicoVendaController.workspace).append('<td class="class_linha_1"></td>');
			    	      $('#analiseHistoricoPopUpDatalancamento', historicoVendaController.workspace).append('<td width="130" align="center" class="class_linha_2"></td>');
			    	      $('#analiseHistoricoPopUpReparte', historicoVendaController.workspace).append('<td align="right" class="class_linha_1"></td>');
			    	      $('#analiseHistoricoPopUpVenda', historicoVendaController.workspace).append('<td align="right" class="class_linha_1"></td>');
			    	  }
			      };
			    });
		});
		
		historicoVendaController.Util = {
			getFiltroByForm : function(idForm){
				var filtro;
				
				if(idForm === undefined){
					return null;
				}else {
					filtro = $('#' + idForm, historicoVendaController.workspace).serializeArray();
					
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
							parameters : [{
								name : "rowId"
							}]
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
							parameters : [{
								name : "codigoProduto",
								type : "string"
							},
							{
								name : "numeroEdicao",
								type : "number"
							}]
						},{
							functionName : "historicoVendaController.popUpCapaClose",
							type : "onmouseout",
						}]
					},{
						element : "span",
						cellText : "qtdVendasFormatada",
						columnName : "qtdVendasFormatada"
					}
				],
				gridConfiguration : {
					dataType: 'json',
					colModel : [{
						display : '',
						name : 'sel',
						width : 20,
						sortable : true,
						align : 'center'
					},{
						display : 'Código',
						name : 'codigoProduto',
						width : 40,
						sortable : true,
						align : 'left'
					},{
						display : 'Classificação',
						name : 'tipoClassificacaoFormatado',
						width : 60,
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
						name : 'periodoString',
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
						name : 'qtdVendasFormatada',
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
					}],
					width : 480,
					height : 160,
					sortname : "dataLancamentoFormatada",
		            sortorder : "desc"
				}
			}),
			EdicaoSelecionadaGrid : flexGridService.GridFactory.createGrid({
				gridName : "edicaoSelecionadaGridHistoricoVenda",
				cached : true,
				inputModel : [{
						element : "img",
						columnName : "acao",
						fileReference : "images/ico_excluir.gif",
						style : "cursor:pointer",
						events : [{
							functionName : "historicoVendaController.removeRowfromGridProduto",
							type : "onclick",
							parameters : [{
								name : "rowId"
							}]
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
						name : 'periodoString',
						width : 40,
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
						name : 'qtdVendasFormatada',
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
							parameters : [{
								name : "rowId"
							}]
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
		

		$("input[type=radio]", historicoVendaController.workspace).change(function (){
			
			historicoVendaController.limparGrids(historicoVendaController.Grids.PesqHistoricoGrid);
			
		});
	},

	limparGrids : function limparGrids(grid){
		
		var tamanhoGrid = grid.tableModel.rows.length;
		
		for (var i=0; i <= tamanhoGrid; i++){
			grid.removeRow(i);
		}
	},
	
	pesquisarCotasHistorico : function pesquisarCotasPorHistorio(url){
		var filtro = [];
			produtosSelecionados = [],
			grids = historicoVendaController.Grids,
			filtro = $('#filtroHistoricoVenda', historicoVendaController.workspace).serializeArray();
	
		filtro.push({
			name : "filtro.cotasAtivas",
			value : $('#cotasAtivas', historicoVendaController.workspace).is(':checked')
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
	
	pesquisarCotasHistoricoCotaOuComponentes : function pesquisarCotasPorHistorio(url){
		var filtro = [];
			produtosSelecionados = [],
			grids = historicoVendaController.Grids,
			filtro = $('#filtroHistoricoVenda', historicoVendaController.workspace).serializeArray();
	
		filtro.push({
			name : "filtro.cotasAtivas",
			value : $('#cotasAtivas', historicoVendaController.workspace).is(':checked')
		});
		
		for ( var i in grids.EdicaoSelecionadaGrid.tableModel.rows) {
			row = grids.EdicaoSelecionadaGrid.tableModel.rows[i];
			filtro.push({name : "filtro.listProdutoEdicaoDTO["+i+"].numeroEdicao", value :  row.cell.numeroEdicao});
			filtro.push({name : "filtro.listProdutoEdicaoDTO["+i+"].codigoProduto", value :  row.cell.codigoProduto});
		}
		
		grids.PesqHistoricoGrid.reload({
			url : url,
			params : filtro,
			workspace: historicoVendaController.workspace,
			preProcess : function(response){
				
				if (response.mensagens) {

					exibirMensagem(response.mensagens.tipoMensagem,
							response.mensagens.listaMensagens);

					return grids.PesqHistoricoGrid.tableModel;
				}else{
					if (grids.PesqHistoricoGrid.tableModel.rows.length > 0) {
						
						for ( var int = 0; int < response.rows.length; int++) {
							row = response.rows[int];
							
							cotaEncontradaNaTabela = $.grep(grids.PesqHistoricoGrid.tableModel.rows, function(val, index) {
								return val.cell.numeroCota == row.cell.numeroCota;
							});
							
							if (cotaEncontradaNaTabela.length == 0) {
								grids.PesqHistoricoGrid.tableModel.rows.push(row);
							}
						}
						
						grids.PesqHistoricoGrid.tableModel.total = grids.PesqHistoricoGrid.tableModel.rows.length;
					}else {
						grids.PesqHistoricoGrid.tableModel = response;
					}
					
					for ( var int2 = 0; int2 < grids.PesqHistoricoGrid.tableModel.rows.length; int2++) {
						row = grids.PesqHistoricoGrid.tableModel.rows[int2];
						row.id = int2 + 1;
						row.cell.acao = '<img onclick="historicoVendaController.removeRowfromGridCota('+row.id +', event);" style="cursor:pointer" src="images/ico_excluir.gif">';
					}
					
					$(".grids", historicoVendaController.workspace).show();
				}
				
				return grids.PesqHistoricoGrid.tableModel;
			}  
		});
	},
	
	botaoCancelar : function(){
		var grids = historicoVendaController.Grids;
		
		emptyTable = {
				total : 0,
				rows : [],
				page : 0
			};
		
		grids.PesqHistoricoGrid.tableModel = emptyTable;
		
		// limpando os registros da tabela
		grids.PesqHistoricoGrid.addTableModel(emptyTable);
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
				if((grids.EdicaoSelecionadaGrid.tableModel.rows[index].cell.numeroEdicao == row.cell.numeroEdicao)
						&& (grids.EdicaoSelecionadaGrid.tableModel.rows[index].cell.codigoProduto == row.cell.codigoProduto)){
					containsElement.contain = true;
					containsElement.rowId = grids.EdicaoSelecionadaGrid.tableModel.rows[index].id;
					break;
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
		
		$( "#dialog-detalhes", historicoVendaController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
			open: historicoVendaController.open(event, produto),
			close : function(){
				$( "#dialog-detalhes", historicoVendaController.workspace).dialog( "close" );
			},
			position: { my: "left", at: "right", of: event.target }
		});
	},
	
	popUpCapaClose : function popUpCapaClose() {
		$( "#dialog-detalhes", historicoVendaController.workspace).dialog( "close" );
	},
	
	open : function(event,produto) {
	
		var randomnumber=Math.floor(Math.random()*11);
		
		$("#imagemCapaEdicao", historicoVendaController.workspace)
	     .attr("src",contextPath
	         + "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
	         + produto.codigoProduto
	         + "&numeroEdicao="
	         + produto.numeroEdicao);
	},
	
	allFilters: ".filtroQtdeReparte, .filtroQtdeVenda, .filtroComponentes, .filtroCotas, .filtroPercVenda, .filtroTodasAsCotas",

	filtroTodasAsCotas : function(){
		$(historicoVendaController.allFilters).hide();
		$(".filtroTodasAsCotas", historicoVendaController.workspace).show();
	},

	filtroReparte : function(){
		$(historicoVendaController.allFilters).hide();
		$('.filtroQtdeReparte', historicoVendaController.workspace).show();
		
		historicoVendaController.limparInputsFiltro(
				false, false, '#qtdVendaInicial', '#qtdVendaFinal', '#percentualVenda', 
				'#componente', '#elemento', '#numeroCota', '#nomePessoa');
	},
	    
	filtroVenda : function(){
		$(historicoVendaController.allFilters).hide();
		$('.filtroQtdeVenda', historicoVendaController.workspace).show();
		
		historicoVendaController.limparInputsFiltro(
				'#qtdReparteInicial', '#qtdReparteFinal', false, false, '#percentualVenda', 
				'#componente', '#elemento', '#numeroCota', '#nomePessoa');
	},

	filtroComponentes : function(){
		$(historicoVendaController.allFilters).hide();
		$('.filtroComponentes', historicoVendaController.workspace).show();
		
		this.limparInputsFiltro('#qtdReparteInicial', '#qtdReparteFinal', '#qtdVendaInicial', 
				'#qtdVendaFinal', '#percentualVenda', false, false, '#numeroCota', '#nomePessoa');
	},
	
	filtroCotas : function(){
		$(historicoVendaController.allFilters).hide();
	    $('.filtroCotas', historicoVendaController.workspace).show();
	    
	    historicoVendaController.limparInputsFiltro('#qtdReparteInicial', '#qtdReparteFinal', 
	    		'#qtdVendaInicial', '#qtdVendaFinal', '#percentualVenda', 
	    		'#componente', '#elemento', false, false);
	},

	filtroPercVenda : function(){
		$(historicoVendaController.allFilters).hide();
		$('.filtroPercVenda', historicoVendaController.workspace).show();
		
		historicoVendaController.limparInputsFiltro('#qtdReparteInicial', '#qtdReparteFinal', 
				'#qtdVendaInicial', '#qtdVendaFinal', false, '#componente', 
				'#elemento', '#numeroCota', '#nomePessoa');
	},
	
	limparInputsFiltro : function(input1, input2, input3, input4, input5, input6, input7, input8, input9){
		$(input1).val("");
	    $(input2).val("");
	    $(input3).val("");
	    $(input4).val("");
	    $(input5).val("");
	    $(input6).val("");
	    $(input7).val("");
	    $(input8).val("");
	    $(input9).val("");
	}
	
}, BaseController);
//@ sourceURL=historicoVenda.js