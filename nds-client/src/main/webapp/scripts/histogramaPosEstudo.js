var histogramaPosEstudoController = $.extend(true, {
	MIN_FAIXA : 0, 
	MAX_FAIXA : 99999999,
	modoAnalise : 'NORMAL',
	fieldSetValues : {},
	matrizSelecionado : {},
	analiseGridRowConsolidada : {},
	oldTabContent : "",
	oldTabHeight : 0,
	matrizDistribuicaoController : null,
	
	createInput : function createInput(id, value){
		return '<input type="text" id="input' + id + '" onkeydown="histogramaPosEstudoController.alterarFaixaAte(' + id + ', event);" value=' + value + ' />';
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
	
	formatarColunasGrid : function formatarColunasGrid(rowCell){
		rowCell.reparteTotalFormatado = formatarMilhar(Math.round(rowCell.reparteTotalFormatado));
		rowCell.vendaNominalFormatado = formatarMilhar(Math.round(rowCell.vendaNominalFormatado));
		rowCell.qtdCotasFormatado = formatarMilhar(rowCell.qtdCotasFormatado);
		rowCell.qtdCotaPossuemReparteMenorVendaFormatado = formatarMilhar(rowCell.qtdCotaPossuemReparteMenorVendaFormatado);
		rowCell.encalheMedioFormatado = floatToPrice(parseFloat(rowCell.encalheMedioFormatado / 100));
		rowCell.participacaoReparteFormatado = floatToPrice(parseFloat(rowCell.participacaoReparteFormatado / 100));
		rowCell.reparteMedioFormatado = floatToPrice(parseFloat(rowCell.reparteMedioFormatado / 100));
		rowCell.vendaMediaFormatado = floatToPrice(parseFloat(rowCell.vendaMediaFormatado / 100)) ;
		rowCell.vendaPercentFormatado = floatToPrice(parseFloat(rowCell.vendaPercentFormatado / 100));
	},
	
	init : function () {

		var flexGridService = new FlexGridService();
		
		/**
		 * Associando eventos ao DOM
		 */
		// Analise do estudo - EMS 2031
		$('#analiseEstudo').click(function() {
			
			var urlAnalise = contextPath + '/distribuicao/analise/parcial/?id=' + histogramaPosEstudoController.matrizSelecionado.estudo +
			    '&modoAnalise='+ histogramaPosEstudoController.modoAnalise;
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
							
							var regx = /\s{0,}[0-9]+\s{0,}a\s{0,}[0-9]+/;
							var faixasArr = regx.exec(row.cell.faixaReparte)[0].split("a");
							
							newRow = {
								id : rowId,
								cell : {
									faixaReparteDe : parseInt(faixasArr[0]),
									faixaReparteAte : createInput(rowId, parseInt(faixasArr[1])),

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
						
						var faixasReparteGrid = histogramaPosEstudoController.Grids.FaixasReparteGrid,
							faixasReparte = [],
							row = {},
							faixa;
						
						faixasReparte.push({
								name : "estudoId",
								value : histogramaPosEstudoController.fieldSetValues.estudo	
						});
						
						for ( var int = 0; int < faixasReparteGrid.tableModel.rows.length; int++) {
							row = faixasReparteGrid.tableModel.rows[int];
							
							if (row.id === 1 ) {
								faixa = row.cell.faixaReparteDe + "-" + $(row.cell.faixaReparteAte).val();
							}else{
								faixa = row.cell.faixaReparteDe.replace(" a","-") + $(row.cell.faixaReparteAte).val();
							}
							
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
					preProcess : function(response){
						var rowConsolidada = $(response.rows).last()[0];
						rowConsolidada.cell.faixaReparte = "Total:";
						
						histogramaPosEstudoController.Grids.EstudosAnaliseGrid.tableModel = response;
						
						$.each( response.rows, function( key, row ) {
							histogramaPosEstudoController.formatarColunasGrid(row.cell);
							
							if (rowConsolidada.id !== row.id) {
								// formatando faixaDeReparte com milhar
								faixaDe = row.cell.faixaReparte.split("a")[0];
								faixaAte = row.cell.faixaReparte.split("a")[1];
								faixaReparteFormatada = histogramaPosEstudoController.formatarMilhar(parseInt(faixaDe)) + " a " + histogramaPosEstudoController.formatarMilhar(parseInt(faixaAte));
								
								if(parseInt(row.cell.qtdCotasFormatado)>0){
									var elemLink = '<a href="javascript:;" onclick="histogramaPosEstudoController.abrirAnaliseFaixa('+ faixaDe +', '+ faixaAte +')">'+ faixaReparteFormatada +'</a>';
									row.cell.faixaReparte = elemLink;
								}
								
								//gerando link do rep menor vda
								if(parseInt(row.cell.qtdCotaPossuemReparteMenorVendaFormatado)>0){
									row.cell.qtdCotaPossuemReparteMenorVendaFormatado=
										"<a href='javascript:;' onclick='histogramaPosEstudoController.abrirAnaliseFaixa("+faixaDe+","+faixaAte+","+key+")'>"+row.cell.qtdCotaPossuemReparteMenorVendaFormatado+"</a>";
								}
							}
						});
						
						histogramaPosEstudoController.analiseGridRowConsolidada = rowConsolidada;
						
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
						height : 230
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
				})
		};
	},
	
	abrirAnaliseFaixa : function(faixaDe, faixaAte,idxFaixa) {
		var url = contextPath + '/distribuicao/analise/parcial/?id=' + histogramaPosEstudoController.matrizSelecionado.estudo +'&faixaDe='+ faixaDe +'&faixaAte='+ faixaAte;
		
		if(idxFaixa!=null){
			histogramaPosEstudo_cotasRepMenorVenda  = histogramaPosEstudoController.Grids.EstudosAnaliseGrid.tableModel.rows[parseInt(idxFaixa)].cell.numeroCotasStr;
		}else{
			histogramaPosEstudo_cotasRepMenorVenda="";
		}
        console.log(idxFaixa);
		console.log(histogramaPosEstudo_cotasRepMenorVenda);
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
						params : [{ name : 'estudoId' , value : jsonData.estudo}]
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

					 if(jsonData.estudoLiberado)
						 $('#estudoLiberadoFs').show();
					 else
						 $('#estudoLiberadoFs').hide();
					 
				 }
			 }
		);
	},
	
	popularFieldsetResumoEstudo : function (){
		var url = contextPath + "/distribuicao/histogramaPosEstudo/carregarDadosFieldSetResumoEstudo",
			estudoId = $('#codigoEstudoFs').text();
			
		$.postJSON(
				url,
				[{name : "estudoId" , value : estudoId}],
				function(response){
					
					// Primeira coluna
					$('#fieldSetResumoReparteTotal').html(formatarMilhar(response.qtdReparteDistribuidor || 0));
					$('#fieldSetResumoRepartePromocional').html(formatarMilhar(response.qtdRepartePromocional || 0));
					$('#fieldSetResumoReservaTecnica').html(formatarMilhar(response.qtdSobraEstudo || 0));
					$('#fieldSetResumoReparteDistribuida').html(formatarMilhar(response.qtdReparteDistribuidoEstudo || 0));
					
					// Segunda Coluna
					$('#fieldSetResumoReparteMedioCota').html(floatToPrice(parseFloat(response.reparteMedioCota || 0).toFixed(2))); // quantidade de exemplares que cada cota irá receber na média
					$('#fieldSetResumoNpdvAtual').html(formatarMilhar(response.qtdCotasAtivas || 0)); // count tb cotas onde status for ativo
					$('#fieldSetResumoNpdvProduto').html(formatarMilhar(response.qtdCotasRecebemReparte || 0)); // quantidade de cotas que irão receber reparte (fazem parte do estudo)
					
					// quantidade de cotas que foram adicionadas no estudo pela “complementar automática”; segundo o Jhonis é "CP" no campo classificação na tb estudo_cota
					$('#fieldSetResumoNpdvComplementar').html(formatarMilhar(response.qtdCotasAdicionadasPelaComplementarAutomatica || 0)); 
					
					// Terceira coluna
					$('#fieldSetResumoReparteMinimoSugerida').html(formatarMilhar(response.qtdReparteMinimoSugerido || 0));
					$('#fieldSetResumoReparteMinimoEstudo').html(formatarMilhar(response.qtdReparteMinimoEstudo || 0));
					$('#fieldSetResumoAbrangenciaSugerida').html(floatToPrice(parseFloat(response.abrangenciaSugerida || 0.00).toFixed(2)) + '% ');
					$('#fieldSetResumoAbrangenciaEstudo').html(floatToPrice(parseFloat(response.abrangenciaEstudo || 0.00).toFixed(2)) + '% ');
					
					$('#fieldSetResumoAbrangenciaVendaPercent').html('Abrangência de Venda:&nbsp;&nbsp;' + floatToPrice((parseFloat(response.abrangenciaDeVenda) || 0 ).toFixed(2)) + '% ');
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
			
			$('#input'+ (selectedRow.id + 1)).focus();
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
		var urlAnalise = contextPath + '/distribuicaoVendaMedia/index?estudoId=' + $('#codigoEstudoFs').text();
	    
		$('#workspace').tabs('addTab', 'Distribuição Venda Média', urlAnalise);
	},
	
}, BaseController);
//@ sourceURL=histogramaPosEstudo.js