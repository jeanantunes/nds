var histogramaPosEstudoController = $.extend(true, {
	MIN_FAIXA : 0, 
	MAX_FAIXA : 99999999,
	fieldSetValues : {},
	matrizSelecionado : {},
	analiseGridRowConsolidada : {},
	
	createInput : function createInput(id, value){
		return '<input type="text" onkeydown="histogramaPosEstudoController.alterarFaixaAte(' + id + ', event);" value=' + value + ' />';
	},
	
	createImgExcluir : function createImgExcluir(rowId){
		return '<a href="javascript:;" onclick="histogramaPosEstudoController.excluirFaixa('+rowId+', event);" style="cursor:pointer">' +
			   '<img title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
			   '</a>';
	},
	
	init:function(){

		var flexGridService = new FlexGridService();
		
		/**
		 * Associando eventos ao DOM
		 */
		$('#alterarFaixaReparte').click(function(){
			$( "#dialog-alterar-faixa" ).dialog({
				resizable: false,
				height:340,
				width:400,
				modal: true,
				open: function(){
					var grids = histogramaPosEstudoController.Grids,
						createInput = histogramaPosEstudoController.createInput,
						createImgExcluir = histogramaPosEstudoController.createImgExcluir;
					
					grids.FaixasReparteGrid.tableModel.rows = [];
					
					for ( var index in grids.EstudosAnaliseGrid.tableModel.rows) {
						row = grids.EstudosAnaliseGrid.tableModel.rows[index];
						
						if (index != grids.EstudosAnaliseGrid.tableModel.rows.length - 1) {
							
							rowId = parseInt(index) + 1;
							
							newRow = {
								id : rowId,
								cell : {
									faixaReparteDe : row.cell.faixaReparte.split("a")[0].replace(" ", "") + " a",
									faixaReparteAte : createInput(rowId, row.cell.faixaReparte.split("a")[1].replace(" ", "")),
									acao : createImgExcluir(rowId)
								}
							};
						
							grids.FaixasReparteGrid.addData(newRow);
						}
					};
				},
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						
						var faixasReparteGrid = histogramaPosEstudoController.Grids.FaixasReparteGrid;
						
						faixasReparte = [];
						
						faixasReparte.push({
								name : "estudoId",
								value : histogramaPosEstudoController.fieldSetValues.estudo	
						});
						
						for ( var int = 0; int < faixasReparteGrid.tableModel.rows.length; int++) {
							row = faixasReparteGrid.tableModel.rows[int];
							
							faixa = row.cell.faixaReparteDe.replace(" a","-") + $(row.cell.faixaReparteAte).val();
							
							faixasReparte.push({
								name : "faixasReparte",
								value : faixa
							});
						}
						
						histogramaPosEstudoController.Grids.EstudosAnaliseGrid.reload({
							params : faixasReparte
						});
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});
		
		$('#baseEstudo').click(function(){
			var estudoId = $('#codigoEstudoFs').html();
			
			$( "#dialog-divergencia" ).dialog({
				resizable: false,
				height:360,
				width:690,
				modal: true,
				open : histogramaPosEstudoController.mostrarBaseEstudo(estudoId),
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});
		
		$('#botaoVoltarMatrizDistribuicao').click(function(){
			$('#matrizDistribuicaoContent').show();
			$('#histogramaPosEstudoContent').hide();
		});
		
		histogramaPosEstudoController.Grids = {
				EstudosAnaliseGrid : flexGridService.GridFactory.createGrid({
					gridName : "estudosAnaliseGrid",
					url : contextPath + "/distribuicao/histogramaPosEstudo/carregarGridAnalise",
					onSuccess : histogramaPosEstudoController.popularFieldsetResumoEstudo,
					// BaseEstudoAnaliseFaixaReparteDTO.java utilizado na resposta(response) do servidor
					preProcess : function(response){
						
						histogramaPosEstudoController.Grids.EstudosAnaliseGrid.tableModel = response;
						
						var rowConsolidado = {
								id : 6,
								cell : {
									faixaReparte : "Total",
									qtdRecebida : 0,
									reparteTotalFormatado : 0,
									reparteMedioFormatado : 0,
									vendaNominalFormatado : 0,
									vendaMediaFormatado : 0,
									vendaPercentFormatado : 0,
									encalheMedioFormatado : 0,
									participacaoReparteFormatado : 0,
									qtdCotasFormatado : 0,
									qtdCotaPossuemReparteMenorVendaFormatado : 0
								}
						};
						
						$.each( response.rows, function( key, row ) {
							rowConsolidado.cell.reparteTotalFormatado += parseInt(row.cell.reparteTotalFormatado || 0);
							rowConsolidado.cell.vendaNominalFormatado += parseInt(row.cell.vendaNominalFormatado || 0);
							rowConsolidado.cell.qtdCotasFormatado += parseInt(row.cell.qtdCotas || 0);
							rowConsolidado.cell.qtdCotaPossuemReparteMenorVendaFormatado += parseInt(row.cell.qtdCotaPossuemReparteMenorVendaFormatado || 0);
							rowConsolidado.cell.qtdRecebida += parseInt(row.cell.qtdRecebida || 0);
						});
						
						rowConsolidado.cell.reparteMedioFormatado = (rowConsolidado.cell.reparteTotalFormatado / rowConsolidado.cell.qtdCotasFormatado).toFixed(2).toString().replace('.', ',');
						rowConsolidado.cell.vendaMediaFormatado = (rowConsolidado.cell.vendaNominalFormatado / rowConsolidado.cell.qtdCotasFormatado).toFixed(2).toString().replace('.', ',');
						rowConsolidado.cell.vendaPercentFormatado = ((rowConsolidado.cell.vendaNominalFormatado / rowConsolidado.cell.reparteTotalFormatado) * 100).toFixed(2).toString().replace('.', ',');
						rowConsolidado.cell.encalheMedioFormatado = (((rowConsolidado.cell.reparteTotalFormatado - rowConsolidado.cell.vendaNominalFormatado) / rowConsolidado.cell.qtdCotasFormatado) * 100).toFixed(2).toString().replace('.', ',');
						rowConsolidado.cell.participacaoReparteFormatado = ((rowConsolidado.cell.qtdRecebida / rowConsolidado.cell.reparteTotalFormatado) * 100).toFixed(2).toString().replace('.', ',');
						
						histogramaPosEstudoController.analiseGridRowConsolidada = rowConsolidado;
						
						response.rows.push(rowConsolidado);
						
						return response;
					},
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Faixa de Reparte',
							name : 'faixaReparte',
							width : 170,
							sortable : true,
							align : 'left'
						}, {
							display : 'Rep. Total',
							name : 'reparteTotalFormatado',
							width : 65,
							sortable : true,
							align : 'right'
						}, {
							display : 'Rep. Médio',
							name : 'reparteMedioFormatado',
							width : 65,
							sortable : true,
							align : 'right'
						}, {
							display : 'Vda Nominal',
							name : 'vendaNominalFormatado',
							width : 65,
							sortable : true,
							align : 'right'
						}, {
							display : 'Vda Média',
							name : 'vendaMediaFormatado',
							width : 60,
							sortable : true,
							align : 'right'
						}, {
							display : '% Vda',
							name : 'vendaPercentFormatado',
							width : 90,
							sortable : true,
							align : 'right'
						}, {
							display : 'Enc. Médio',
							name : 'encalheMedioFormatado',
							width : 90,
							sortable : true,
							align : 'right'
						}, {
							display : 'Part. Reparte',
							name : 'participacaoReparteFormatado',
							width : 65,
							sortable : true,
							align : 'right'
						}, {
							display : 'Qtde. Cotas',
							name : 'qtdCotasFormatado',
							width : 70,
							sortable : true,
							align : 'right'
						}, {
							display : 'Rep Menor Vda',
							name : 'qtdCotaPossuemReparteMenorVendaFormatado',
							width : 80,
							sortable : true,
							align : 'right'
						}],
						sortname : "faixaReparte",
						sortorder : "asc",
						usepager : true,
						useRp : true,
						rp : 15,
						showTableToggleBtn : true,
						width : 960,
						height : 150
					}
				}),
				BaseSugeridaGrid : flexGridService.GridFactory.createGrid({
					gridName : "baseSugeridaGrid",
					url : contextPath + "/distribuicao/histogramaPosEstudo/carregarGridBaseSugerida",
					cached : true,
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Código',
							name : 'codigo',
							width : 60,
							sortable : true,
							align : 'left'
						},{
							display : 'Produto',
							name : 'produto',
							width : 95,
							sortable : true,
							align : 'left'
						}, {
							display : 'Edição',
							name : 'edicao',
							width : 50,
							sortable : true,
							align : 'left'
						}, {
							display : 'Peso',
							name : 'peso',
							width : 25,
							sortable : true,
							align : 'right'
						}],
						width : 300,
						height : 180
					}
				}),
				BaseEstudoGrid : flexGridService.GridFactory.createGrid({
					gridName : "baseEstudoGrid",
					url : contextPath + "/distribuicao/histogramaPosEstudo/carregarGridBaseEstudo",
					cached : true,
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Código',
							name : 'codigoProduto',
							width : 60,
							sortable : true,
							align : 'left'
						},{
							display : 'Produto',
							name : 'nomeProduto',
							width : 95,
							sortable : true,
							align : 'left'
						}, {
							display : 'Edição',
							name : 'numeroEdicao',
							width : 50,
							sortable : true,
							align : 'left'
						}, {
							display : 'Peso',
							name : 'peso',
							width : 25,
							sortable : true,
							align : 'right'
						}],
						width : 300,
						height : 180
					}
				}),
				FaixasReparteGrid : flexGridService.GridFactory.createGrid({
					gridName : "faixasReparteGrid",
//					url : contextPath + "/distribuicao/histogramaPosEstudo/alterarFaixaReparte",
					cached : true,
//					preProcess : function(tableModel){
//						$.each( tableModel.rows, function( key, row ) {
//							
//						});
//					},
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Faixa de Reparte Dê',
							name : 'faixaReparteDe',
							width : 130,
							sortable : true,
							align : 'left'
						},{
							display : 'Faixa de Reparte Até',
							name : 'faixaReparteAte',
							width : 130,
							sortable : true,
							align : 'left'
						}, {
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : true,
							align : 'center'
						}],
						width : 350,
						height : 180
					}
				}),
		};
	},
	
	
	popularFieldsetHistogramaPreAnalise : function popularFieldsetHistogramaPreAnalise(selecionado){
		
		var	url = contextPath + "/distribuicao/histogramaPosEstudo/carregarDadosFieldsetHistogramaPreAnalise";

		$.postJSON(
			 url,
			 selecionado,
			 function onSucessCallBack(jsonData){
				 if (jsonData) {
					 histogramaPosEstudoController.fieldSetValues = jsonData;
					 
					 histogramaPosEstudoController.Grids.EstudosAnaliseGrid.reload({
						params : jsonData.estudo,
					 });
					 
					 $('#codigoProdutoFs').html(jsonData.codigoProduto);
					 $('#nomeProdutoFs').html(jsonData.nomeProduto);
					 $('#edicaoProdutoFs').html(jsonData.edicao);
					 $('#classificacaoProdutoFs').html(jsonData.classificacao);
					 $('#segmentoFs').html(jsonData.tipoSegmentoProduto.descricao);
					 $('#codigoEstudoFs').html(jsonData.estudo);
					 $('#periodoFs').html(jsonData.periodicidadeProduto);

					 if (jsonData.estudoLiberado) {
						 $('#estudoLiberadoFs').show();
						 $('#estudoLiberadoFs').attr('src', "images/ico_check.gif");
					 }else {
						 $('#estudoLiberadoFs').hide();
					}
				 }
			 }
		);
	},
	
	popularFieldsetResumoEstudo : function popularFieldsetResumoEstudo(){
		var matrizSelecionada = histogramaPosEstudoController.matrizSelecionado,
			rowConsolidada = histogramaPosEstudoController.analiseGridRowConsolidada;

		// Primeira coluna
		$('#fieldSetResumoReparteTotal').html(rowConsolidada.cell.reparteTotalFormatado);
		$('#fieldSetResumoRepartePromocional').html(matrizSelecionada.promo);
		$('#fieldSetResumoReservaTecnica').html(matrizSelecionada.sobra);
		$('#fieldSetResumoReparteDistribuida').html(matrizSelecionada.repDistrib);
		
		// Segunda coluna
		$('#fieldSetResumoNpdvAtual').html(123); // count tb cotas onde status for ativo
		$('#fieldSetResumoNpdvProduto').html(123); // quantidade de cotas que irão receber reparte (fazem parte do estudo)
		$('#fieldSetResumoNpdvComplementar').html(123); // quantidade de cotas que foram adicionadas no estudo pela “complementar automática”; segundo o Jhonis é "CP" no campo classificação na tb estudo_cota
		$('#fieldSetResumoReparteMedioCota').html(123); // quantidade de exemplares que cada cota irá receber na média 
		
		// Terceira coluna
		$('#fieldSetResumoReparteMinimoSugerida').html(123);
		$('#fieldSetResumoReparteMinimoEstudo').html(123);
		 
		$('#fieldSetResumoAbrangenciaSugerida').html(123);
		$('#fieldSetResumoAbrangenciaEstudo').html(123);

		$('#fieldSetResumoAbrangenciaVendaPercent').html('Abrangência de Venda:&nbsp;&nbsp;'+50+'% ');
		
		
		
		
	},
	alterarFaixaAte : function alterarFaixaAte(rowId, event){
		var	faixaReparteGrid = histogramaPosEstudoController.Grids.FaixasReparteGrid;
		
		if (event.keyIdentifier == "Enter") {
			event.cancelBubble = true;
			event.bubbles = false;

			selectedRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId;
			})[0];
			
			if (parseInt(event.target.value) < parseInt(selectedRow.cell.faixaReparteDe)) {
				event.target.value = $(selectedRow.cell.faixaReparteAte).val();
				exibirMensagem("WARNING", ['Valor "ATÉ" deve ser maior que "DE".']);
				return;
			}
			
			nextRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId+1;
			})[0];
			
			if (nextRow) {
				selectedRow.cell.faixaReparteAte = histogramaPosEstudoController.createInput(selectedRow.id,parseInt(event.target.value));
				nextRow.cell.faixaReparteDe = (parseInt(event.target.value) + 1 ) + " a";
			} else {
				// copia a última linha
				var newRow = {
						id : 0,
						cell : {
							faixaReparteDe : 0,
							faixaReparteAte : 0
						}
					};
				
				// altera seu id
				newRow.id = parseInt(selectedRow.id) + 1;
				
				// altera o valor do input 
				selectedRow.cell.faixaReparteAte = histogramaPosEstudoController.createInput(selectedRow.id,parseInt(event.target.value));
				
				// cria a última linha do grid
				newRow.cell.faixaReparteDe = (parseInt(event.target.value) + 1 ) + " a";
				newRow.cell.faixaReparteAte = histogramaPosEstudoController.createInput(newRow.id,parseInt(histogramaPosEstudoController.MAX_FAIXA));
				
				// adiciona ao tableModel do grid
				faixaReparteGrid.tableModel.rows.push(newRow);
			}
			
			histogramaPosEstudoController.organizarRowId(faixaReparteGrid);
			
			faixaReparteGrid.addTableModel(faixaReparteGrid.tableModel);
		}
	},
	
	excluirFaixa : function excluirFaixa(rowId, event){
		var	faixaReparteGrid = histogramaPosEstudoController.Grids.FaixasReparteGrid;
			createInput = histogramaPosEstudoController.createInput,
			createImgExcluir = histogramaPosEstudoController.createImgExcluir;

		if (faixaReparteGrid.tableModel.rows.length > 1) {
				
			selectedRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId;
			})[0];
			
			previousRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId - 1;
			})[0];
			
			nextRow  = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId + 1;
			})[0];
	
			// usuário deleta a primeira linha
			if (!previousRow) {
				nextRow.cell.faixaReparteDe = 0 + " a";
			}
			// usuário deleta a última linha
			else if (!nextRow) {
				previousRow.cell.faixaReparteAte = createInput(previousRow.id, $(selectedRow.cell.faixaReparteAte).val());
			} 
			else {
				previousRow.cell.faixaReparteAte = createInput(previousRow.id, $(selectedRow.cell.faixaReparteAte).val());
			} 
			
			faixaReparteGrid.removeRow(selectedRow.id);
			
			histogramaPosEstudoController.organizarRowId(faixaReparteGrid);
			
			faixaReparteGrid.addTableModel(faixaReparteGrid.tableModel);
		}else {
			exibirMensagem("WARNING", ["deve existir pelo menos uma faixa."]);
		}
	},
	
	organizarRowId : function(grid){
		// organizando os ID das linhas
		for ( var int = 0; int < grid.tableModel.rows.length; int++) {
			row = grid.tableModel.rows[int];
			row.id = int + 1;
			row.cell.faixaReparteAte = histogramaPosEstudoController.createInput(row.id, $(row.cell.faixaReparteAte).val());
			row.cell.acao = histogramaPosEstudoController.createImgExcluir(row.id);
		}
	},
	
	mostrarBaseEstudo : function mostrarBaseEstudo(estudoId){

		var baseEstudo = histogramaPosEstudoController.Grids.BaseEstudoGrid;
		
		baseEstudo.reload({
			params : [{
				name : "estudoId",
				value : estudoId
			}]
		});
	}

}, BaseController);
//@ sourceURL=histogramaPosEstudo.js