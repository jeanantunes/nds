var histogramaPosEstudoController = $.extend(true, {
	MIN_FAIXA : 0, 
	MAX_FAIXA : 99999999,
	fieldSetValues : {},
	matrizSelecionado : {},
	analiseGridRowConsolidada : {},
	oldTabContent : "",
	oldTabHeight : 0,
	matrizDistribuicaoController : null,
	
	createInput : function createInput(id, value){
		return '<input type="text" onkeydown="histogramaPosEstudoController.alterarFaixaAte(' + id + ', event);" value=' + value + ' />';
	},
	
	createImgExcluir : function createImgExcluir(rowId){
		return '<a href="javascript:;" onclick="histogramaPosEstudoController.excluirFaixa('+rowId+', event);" style="cursor:pointer">' +
			   '<img title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
			   '</a>';
	},
	
	formatarMilhar : function formatarMilhar(num){	
		x = 0;   
		
		if(num < 0){
			num = Math.abs(num);
			x = 1;
		}
		
		if(isNaN(num)) 
			num = "0";
		
		num = Math.floor((num*100+0.5)/100).toString();
		
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		
		num = num.substring(0,num.length-(4*i+3))+'.'
			 +num.substring(num.length-(4*i+3));
		ret = num;
		
		if (x == 1) ret = ' - ' + ret;
		
		return ret;	
	},
	
	init : function () {

		var flexGridService = new FlexGridService();
		
		/**
		 * Associando eventos ao DOM
		 */
		// Analise do estudo - EMS 2031
		$('#analiseEstudo').click(function() {
			
			var urlAnalise = contextPath + '/distribuicao/analise/parcial/?id=' + histogramaPosEstudoController.matrizSelecionado.estudo;
			$('#workspace').tabs('addTab', 'Análise de Estudos', urlAnalise);
			
			/*
			$('#workspace').tabs('addTab', 'Análise de Estudos', contextPath + '/distribuicao/analiseEstudo');
			$('#workspace').tabs({load : function(event, ui) {
				
				$("#idEstudo").val(histogramaPosEstudoController.matrizSelecionado.estudo);
				analiseEstudoController.carregarEstudos();
				
				$('#workspace').tabs({load : function(event, ui) {}});
			}});
			*/
		});
		
		// RECALCULAR ESTUDO - EMS 2025 - Distribuição Venda Média
		$('#recalcularEstudo').click(function(){
			histogramaPosEstudoController.recalcularEstudo();
		});
		
		$('#excluirEstudo').click(function(){
			$( "#popup_confirmar_exclusao_estudo" ).dialog({
				resizable: false,
				height:150,
				width:200,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						
						var url = contextPath + "/distribuicao/histogramaPosEstudo/excluirEstudo",
							matrizSelecionada = histogramaPosEstudoController.matrizSelecionado;
						
						$.postJSON(
							url,
							[{name : "id", value : matrizSelecionada.estudo}],
							function(response){
								// refaz a pesquisa na matriz de distribuicao
								var filtro = histogramaPosEstudoController.matrizDistribuicaoController.parametrosDePesquisa;
								histogramaPosEstudoController.matrizDistribuicaoController.pesquisar(filtro);
								// fecha a aba
								$('.ui-tabs-selected').children('.ui-icon-close').click();
							}
						);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});
		
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
									faixaReparteDe : replaceAll($(row.cell.faixaReparte).text().split("a")[0].replace(" ", ""),".","") + " a",
									faixaReparteAte : createInput(rowId, replaceAll($(row.cell.faixaReparte).text().split("a")[1].replace(" ", ""),".","")),
									acao : createImgExcluir(rowId)
								}
							};
						
							grids.FaixasReparteGrid.addData(newRow);
						}
					};
				},
				buttons: {
					"Confirmar": function () {
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
							// formatando faixaDeReparte com milhar
							faixaDe = row.cell.faixaReparte.split("a")[0];
							faixaAte = row.cell.faixaReparte.split("a")[1];
							faixaReparteFormatada = histogramaPosEstudoController.formatarMilhar(parseInt(faixaDe)) + " a " + histogramaPosEstudoController.formatarMilhar(parseInt(faixaAte));
							
							var elemLink = '<a href="javascript:;" onclick="histogramaPosEstudoController.abrirAnaliseFaixa('+ faixaDe +', '+ faixaAte +')">'+ faixaReparteFormatada +'</a>';
							// adicionando a linha
							row.cell.faixaReparte = elemLink;
							
							rowConsolidado.cell.reparteTotalFormatado += parseInt(row.cell.reparteTotalFormatado || 0);
							rowConsolidado.cell.vendaNominalFormatado += parseInt(row.cell.vendaNominalFormatado || 0);
							rowConsolidado.cell.qtdCotasFormatado += parseInt(row.cell.qtdCotas || 0);
							rowConsolidado.cell.qtdCotaPossuemReparteMenorVendaFormatado += parseInt(row.cell.qtdCotaPossuemReparteMenorVendaFormatado || 0);
							rowConsolidado.cell.qtdRecebida += parseInt(row.cell.qtdRecebida || 0);
						});
						
						reparteMedioFormatado = (rowConsolidado.cell.reparteTotalFormatado / rowConsolidado.cell.qtdCotasFormatado).toFixed(2);
						vendaMediaFormatada = (rowConsolidado.cell.vendaNominalFormatado / rowConsolidado.cell.qtdCotasFormatado).toFixed(2);
						vendaPercentFormatado = ((rowConsolidado.cell.vendaNominalFormatado / rowConsolidado.cell.reparteTotalFormatado) * 100).toFixed(2).toString().replace('.', ',');
						encalheMedioFormatado = ((rowConsolidado.cell.reparteTotalFormatado - rowConsolidado.cell.vendaNominalFormatado) / rowConsolidado.cell.qtdCotasFormatado).toFixed(2);
						participacaoReparteFormatado = ((rowConsolidado.cell.qtdRecebida / rowConsolidado.cell.reparteTotalFormatado) * 100).toFixed(2).toString().replace('.', ',');
						
						rowConsolidado.cell.reparteMedioFormatado = !isNaN(reparteMedioFormatado) ? reparteMedioFormatado : "0,00";
						rowConsolidado.cell.vendaMediaFormatado = !isNaN(vendaMediaFormatada) ? vendaMediaFormatada : "0,00";
						rowConsolidado.cell.vendaPercentFormatado = vendaPercentFormatado != "NaN" ? vendaPercentFormatado : "0,00";
						rowConsolidado.cell.encalheMedioFormatado = !isNaN(encalheMedioFormatado)  ? encalheMedioFormatado : "0,00";
						rowConsolidado.cell.participacaoReparteFormatado = participacaoReparteFormatado != "NaN"  ? participacaoReparteFormatado : "0,00";
						
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
					cached : true,
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Faixa de Reparte De',
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
	
	abrirAnaliseFaixa : function(faixaDe, faixaAte) {
		var url = contextPath + '/distribuicao/analise/parcial/?id=' + histogramaPosEstudoController.matrizSelecionado.estudo +'&faixaDe='+ faixaDe +'&faixaAte='+ faixaAte;
		$('#workspace').tabs('addTab', 'Análise de Estudos', url);
	},
	
	popularFieldsetHistogramaPreAnalise : function (selecionado, matrizDistribuicaoController){
		
		histogramaPosEstudoController.matrizDistribuicaoController = matrizDistribuicaoController;
		
		var	url = contextPath + "/distribuicao/histogramaPosEstudo/carregarDadosFieldsetHistogramaPreAnalise";

		$.postJSON(
			 url,
			 selecionado,
			 function onSucessCallBack(jsonData){
				 if (jsonData) {
					 histogramaPosEstudoController.matrizSelecionado = jsonData;
					 histogramaPosEstudoController.fieldSetValues = jsonData;
					 
					 histogramaPosEstudoController.Grids.EstudosAnaliseGrid.reload({
						params : [{ name : 'estudoId' , value : jsonData.estudo}],
					 });
					 
					 $('#codigoProdutoFs').html(jsonData.codigoProduto);
					 $('#nomeProdutoFs').html(jsonData.nomeProduto);
					 $('#edicaoProdutoFs').html(jsonData.edicao);
					 $('#classificacaoProdutoFs').html(jsonData.classificacao);
					 
					 if (jsonData.tipoSegmentoProduto != undefined) {
						 $('#segmentoFs').html(jsonData.tipoSegmentoProduto.descricao);
					 }
					 
					 $('#codigoEstudoFs').html(jsonData.estudo);
					 $('#periodoFs').html(jsonData.periodicidadeProduto);
					 $('#parcial').val(jsonData.parcial);

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
	
	popularFieldsetResumoEstudo : function (){
		var matrizSelecionada = histogramaPosEstudoController.matrizSelecionado,
			rowConsolidada = histogramaPosEstudoController.analiseGridRowConsolidada,
			url = contextPath + "/distribuicao/histogramaPosEstudo/carregarDadosFieldSetResumoEstudo";
		
		// Primeira coluna
		$('#fieldSetResumoReparteTotal').html(rowConsolidada.cell.reparteTotalFormatado);
		$('#fieldSetResumoRepartePromocional').html(parseInt(matrizSelecionada.promo || 0));
		$('#fieldSetResumoReservaTecnica').html(matrizSelecionada.sobra);
		$('#fieldSetResumoReparteDistribuida').html(matrizSelecionada.repDistrib);
		
		$.postJSON(
				url,
				[{name : "estudoId" , value :matrizSelecionada.estudo}],
				function(response){
					
					// Segunda coluna
					$('#fieldSetResumoNpdvAtual').html(response.qtdCotasAtivas); // count tb cotas onde status for ativo
					
					$('#fieldSetResumoNpdvProduto').html(response.qtdCotasRecebemReparte); // quantidade de cotas que irão receber reparte (fazem parte do estudo)
					
					// quantidade de cotas que foram adicionadas no estudo pela “complementar automática”; segundo o Jhonis é "CP" no campo classificação na tb estudo_cota
					$('#fieldSetResumoNpdvComplementar').html(response.qtdCotasAdicionadasPelaComplementarAutomatica || 0); 
					
					$('#fieldSetResumoReparteMedioCota').html((parseInt(matrizSelecionada.repDistrib) / parseInt(response.qtdCotasRecebemReparte)) || 0 ); // quantidade de exemplares que cada cota irá receber na média 
					
					// Terceira coluna
					$('#fieldSetResumoReparteMinimoSugerida').html(response.qtdReparteMinimoSugerido || 0);
					$('#fieldSetResumoReparteMinimoEstudo').html(response.qtdReparteMinimoEstudo);
					
					$('#fieldSetResumoAbrangenciaSugerida').html(response.abrangenciaSugerida || 0);
					$('#fieldSetResumoAbrangenciaEstudo').html((response.abrangenciaEstudo * 100).toFixed(2));
					
					$('#fieldSetResumoAbrangenciaVendaPercent').html('Abrangência de Venda:&nbsp;&nbsp;' + (parseFloat(response.abrangenciaDeVenda) || 0 ).toFixed(2) + '% ');
				}
		);
	},
	
	alterarFaixaAte : function (rowId, event){
		var	faixaReparteGrid = histogramaPosEstudoController.Grids.FaixasReparteGrid,
			selectedRow = {},
			nextRow = {};
		
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
	
	excluirFaixa : function (rowId, event){
		var	faixaReparteGrid = histogramaPosEstudoController.Grids.FaixasReparteGrid,
			createInput = histogramaPosEstudoController.createInput,
			createImgExcluir = histogramaPosEstudoController.createImgExcluir,
			selectedRow = {},
			previousRow = {},
			nextRow = {};
			

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
			exibirMensagem("WARNING", ["Deve existir pelo menos uma faixa."]);
		}
	},
	
	organizarRowId : function (grid){
		// organizando os ID das linhas
		for ( var int = 0; int < grid.tableModel.rows.length; int++) {
			row = grid.tableModel.rows[int];
			row.id = int + 1;
			row.cell.faixaReparteAte = histogramaPosEstudoController.createInput(row.id, $(row.cell.faixaReparteAte).val());
			row.cell.acao = histogramaPosEstudoController.createImgExcluir(row.id);
		}
	},
	
	mostrarBaseEstudo : function (estudoId){
		var baseEstudo = histogramaPosEstudoController.Grids.BaseEstudoGrid;
		
		baseEstudo.reload({
			params : [{
				name : "estudoId",
				value : estudoId
			}]
		});
		
		var baseSugerida = histogramaPosEstudoController.Grids.BaseSugeridaGrid;
		
		baseSugerida.reload({
			params : [{
				name : "estudoId",
				value : estudoId
			}]
		});
	},
	
	recalcularEstudo : function (){
		var data = [],
			matrizSelecionado = histogramaPosEstudoController.matrizSelecionado;
		
		data.push({name: "edicao", value: matrizSelecionado.edicao});
		data.push({name: "estudoId", value: matrizSelecionado.estudo});
		data.push({name: "lancamentoId", value: matrizSelecionado.idLancamento});
		data.push({name: "codigoProduto", value: matrizSelecionado.codigoProduto});
		
		data.push({name: "juramentado", value: matrizSelecionado.juram || 0});
		data.push({name: "suplementar", value: matrizSelecionado.suplem || 0});
		data.push({name: "lancado", value: matrizSelecionado.lancto || 0});
		data.push({name: "promocional", value: matrizSelecionado.promo || 0});
		data.push({name: "sobra", value: matrizSelecionado.sobra || 0});
		$.post(contextPath + "/distribuicaoVendaMedia/index", data, function(response) {
			$('#matrizDistribuicaoContent').hide();
			$('#telasAuxiliaresContent').html(response);
			$('#telasAuxiliaresContent').show();
		});
	},
	
	analise : function() {
		
	}

}, BaseController);
//@ sourceURL=histogramaPosEstudo.js