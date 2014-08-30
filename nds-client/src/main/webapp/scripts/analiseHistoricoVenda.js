var analiseHistoricoVendaController = $.extend(true, {
	
	init : function() {
		
		var flexGridService = new FlexGridService();
		
		// EXPORTAÇÃO
		$('#analiseHistoricoVendaXLS', analiseHistoricoVendaController.workspace).attr(
				'href', contextPath + "/distribuicao/historicoVenda/exportar?fileType=XLS");
		
		$('#analiseHistoricoVendaPDF', analiseHistoricoVendaController.workspace).attr(
				'href', contextPath + "/distribuicao/historicoVenda/exportar?fileType=PDF");
		
		analiseHistoricoVendaController.Grids = {
				BaseHistoricoGrid : flexGridService.GridFactory.createGrid({
					gridName : "baseHistoricoGrid",
					url : contextPath + "/distribuicao/historicoVenda/carregarGridAnaliseHistorico",
					cached : true,
					preProcess : analiseHistoricoVendaController.preProcessAnaliseGrid,
					gridConfiguration : {
						dataType: 'json',
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 35,
							sortable : true,
							align : 'left'
						}, {
							display : 'Status',
							name : 'statusCota',
							width : 50,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 100,
							sortable : true,
							align : 'left'
						}, {
							display : 'NPDV',
							name : 'qtdPdv',
							width : 30,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'reparteMedio',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'vendaMedia',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed1Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed1Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed2Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed2Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed3Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed3Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed4Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed4Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed5Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed5Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed6Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed6Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}],
						width : 950,
						height : 270
					}
				}),
				CotasDetalhesGrid : flexGridService.GridFactory.createGrid({
					gridName : "cotasDetalhesGrid",
					url : contextPath + "/distribuicao/historicoVenda/carregarPdv",
					cached : true,
					preProcess : analiseHistoricoVendaController.preProcessPopUpGrid,
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Código',
							name : 'id',
							width : 40,
							sortable : true,
							align : 'left'
						}, {
							display : 'Tipo',
							name : 'descricaoTipoPontoPDV',
							width : 90,
							sortable : true,
							align : 'left'
						}, {
							display : '% Fat.',
							name : 'porcentagemFaturamento',
							width : 30,
							sortable : true,
							align : 'right'
						}, {
							display : 'Princ.',
							name : 'principal',
							width : 30,
							sortable : true,
							align : 'center'
						}, {
							display : 'Endereço',
							name : 'endereco',
							width : 420,
							sortable : true,
							align : 'left'
						}],
						width : 690,
						height : 200
					}
				})
		};
	},
	
	
	preProcessPopUpGrid : function preProcessPopUpGrid(response){
		
		analiseHistoricoVendaController.popularPopUpInformacoesCota(response.cotaDto);
		
		if (response.tableModel) {
			for ( var i in response.tableModel.rows) {
				row = response.tableModel.rows[i];
				
				if (row.cell.principal) {
					row.cell.principal = "<img src='images/ico_check.gif' />";
				}else {
					row.cell.principal = "";
				}
				
			}
		}
		
		return response.tableModel;
		
	},

	popularPopUpInformacoesCota : function(cotaDto){
		if(cotaDto){
			$('#popUpNumeroCota').text(cotaDto.numeroCota);
			$('#popUpNomePessoa').text(cotaDto.nomePessoa);
			$('#popUpTipoCota').text(cotaDto.tipoDistribuicaoCota);
			$('#popUpRanking').text(cotaDto.rankId);
			$('#popUpFaturamentoCota').text(cotaDto.faturamentoFormatado);
			$('#popUpData').text(cotaDto.dataGeracaoFormat);
		}
	},
	
	limparPopUpInformacoesCota : function(cotaDto){
		       $('#popUpNumeroCota').text('');
		       $('#popUpNomePessoa').text('');
		       $('#popUpTipoCota').text('');
		       $('#popUpRanking').text('');
		       $('#popUpFaturamentoCota').text('');
		       $('#popUpData').text('');
	},
	
	preProcessAnaliseGrid : function preProcessAnaliseGrid(result){
		
		for ( var index in result.rows) {
			row = result.rows[index];
			
			// montar o link no nome da cota
			link = '<a href="javascript:;" onclick="analiseHistoricoVendaController.popup_cotas_detalhes('+row.cell.numeroCota+');" style="cursor:pointer">'+row.cell.nomePessoa+'</a>';
			row.cell.nomePessoa = link;
			
			row.cell.ed1Reparte = row.cell.ed1Reparte || row.cell.ed1Reparte == 0 ? row.cell.ed1Reparte : "";
			row.cell.ed1Venda = row.cell.ed1Venda || row.cell.ed1Venda == 0 ? row.cell.ed1Venda : "";
			row.cell.ed2Reparte = row.cell.ed2Reparte || row.cell.ed2Reparte == 0 ? row.cell.ed2Reparte : "";
			row.cell.ed2Venda = row.cell.ed2Venda || row.cell.ed2Venda == 0 ? row.cell.ed2Venda : "";
			row.cell.ed3Reparte = row.cell.ed3Reparte || row.cell.ed3Reparte == 0 ? row.cell.ed3Reparte : "";
			row.cell.ed3Venda = row.cell.ed3Venda || row.cell.ed3Venda == 0 ? row.cell.ed3Venda : "";
			row.cell.ed4Reparte = row.cell.ed4Reparte || row.cell.ed4Reparte == 0 ? row.cell.ed4Reparte : "";
			row.cell.ed4Venda = row.cell.ed4Venda || row.cell.ed4Venda == 0 ? row.cell.ed4Venda : "";
			row.cell.ed5Reparte = row.cell.ed5Reparte || row.cell.ed5Reparte == 0 ? row.cell.ed5Reparte : "";
			row.cell.ed5Venda = row.cell.ed5Venda || row.cell.ed5Venda == 0 ? row.cell.ed5Venda : "";
			row.cell.ed6Reparte = row.cell.ed6Reparte || row.cell.ed6Reparte == 0 ? row.cell.ed6Reparte : "";
			row.cell.ed6Venda = row.cell.ed6Venda || row.cell.ed6Venda == 0 ? row.cell.ed6Venda : "";
			
			row.cell.reparteMedio = (row.cell.reparteMedio || 0).toFixed(0);
			row.cell.vendaMedia = (row.cell.vendaMedia || 0).toFixed(0);
			
		}
		
		var resumo = result.rows[result.rows.length - 1].cell;
		
		html = '<td width="50" >Qtde Cotas:</td>' +
		        '<td width="103" >' + resumo.numeroCota + '</td>' +
		        '<td width="30" align="right">' + resumo.qtdPdv + '</td>' +
		        '<td width="32" align="right">' + resumo.reparteMedio + '</td>' +
		        '<td width="32" align="right">' + (analiseHistoricoVendaController.isFaixaZero ? 0 : resumo.vendaMedia) + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed1Reparte ? resumo.ed1Reparte : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed1Venda ? resumo.ed1Venda : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed2Reparte ? resumo.ed2Reparte : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed2Venda ? resumo.ed2Venda : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed3Reparte ? resumo.ed3Reparte : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed3Venda ? resumo.ed3Venda : '') + '</span></td>' +
		        '<td width="32" align="right">' + (resumo.ed4Reparte ? resumo.ed4Reparte : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed4Venda ? resumo.ed4Venda : '') + '</span></td>' +
		        '<td width="32" align="right">' + (resumo.ed5Reparte ? resumo.ed5Reparte: '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed5Venda ? resumo.ed5Venda : '') + '</span></td>' +
		        '<td width="32" align="right">' + (resumo.ed6Reparte ? resumo.ed6Reparte : '') + '</td>' +
		        '<td width="32" align="right">' + (resumo.ed6Venda ? resumo.ed6Venda : '') + '</span></td>' +
		        '<td width="15" >&nbsp;</td>';
		 
		 $('#rodapeAnaliseHistorico', analiseHistoricoVendaController.workspace).html(html);
		 
		 result.rows.splice(result.rows.length - 1, 1);
		 
		 return result;
	},
	
	// PopUp visulizado quando o usuário clica no nome da cota dentro do Grid Principal
	popup_cotas_detalhes : function popup_cotas_detalhes(numeroCota) {
		$( "#dialog-cotas-detalhes" ).dialog({
			resizable: false,
			height:450,
			width:740,
			modal: true,
			open: function(){
				analiseHistoricoVendaController.Grids.CotasDetalhesGrid.reload({
					params : [{ name : 'numeroCota' , value : numeroCota}]
				});
			},
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			beforeClose: function() {
	        	analiseHistoricoVendaController.limparPopUpInformacoesCota();
	        }
		});
	}
	
}, BaseController);
//@ sourceURL=analiseHistoricoVenda.js