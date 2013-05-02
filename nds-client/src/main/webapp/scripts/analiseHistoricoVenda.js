var analiseHistoricoVendaController = $.extend(true, {
	
	init : function() {
		
		var flexGridService = new FlexGridService();
		
		// EXPORTAÇÃO
		$('#analiseHistoricoVendaXLS').attr('href', contextPath + "/distribuicao/historicoVenda/exportar?fileType=XLS");
		
		$('#analiseHistoricoVendaPDF').attr('href', contextPath + "/distribuicao/historicoVenda/exportar?fileType=PDF");
		
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
				}),
		};
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
	
	preProcessPopUpGrid : function preProcessPopUpGrid(response){
		
		analiseHistoricoVendaController.popularPopUpInformacoesCota(response.cotaDto);
		
		for ( var i in response.tableModel.rows) {
			row = response.tableModel.rows[i];
			
			if (row.cell.principal) {
				row.cell.principal = "<img src='images/ico_check.gif' />";
			}else {
				row.cell.principal = "";
			}
			
		}
		
		return response.tableModel;
		
	},
	
	preProcessAnaliseGrid : function preProcessAnaliseGrid(result){
		var rodape = {
				qtdCota  : 0,
				qtdPdv  :  0,
				reparteMedio : 0,
				vendaMedia : 0,
				ed1Rep : 0,
				ed1Venda : 0,
				ed2Rep :  0,
				ed2Venda : 0,
				ed3Rep :  0,
				ed3Venda : 0,
				ed4Rep :  0,
				ed4Venda : 0,
				ed5Rep :  0,
				ed5Venda : 0,
				ed6Rep :  0,
				ed6Venda : 0
		};
		
		for ( var index in result.rows) {
			row = result.rows[index];
			
			// montar o link no nome da cota
			link = '<a href="javascript:;" onclick="analiseHistoricoVendaController.popup_cotas_detalhes('+row.cell.numeroCota+');" style="cursor:pointer">'+row.cell.nomePessoa+'</a>';
			row.cell.nomePessoa = link;
			
			rodape.qtdCota  +=  parseInt((row.cell.numeroCota ? 1 : 0));
			rodape.qtdPdv  +=  parseInt((row.cell.qtdPdv || 0));
			rodape.reparteMedio  +=  (row.cell.reparteMedio ||  0);
			rodape.vendaMedia +=  (row.cell.vendaMedia ||  0);
			rodape.ed1Rep += parseInt((row.cell.ed1Reparte  ||  0));
			rodape.ed1Venda +=  parseInt((row.cell.ed1Venda ||  0));
			rodape.ed2Rep +=  parseInt((row.cell.ed2Reparte ||  0));
			rodape.ed2Venda +=  parseInt((row.cell.ed2Venda ||  0));
			rodape.ed3Rep +=  parseInt((row.cell.ed3Reparte ||  0));
			rodape.ed3Venda +=  parseInt((row.cell.ed3Venda ||  0));
			rodape.ed4Rep +=  parseInt((row.cell.ed4Reparte ||  0));
			rodape.ed4Venda +=  parseInt((row.cell.ed4Venda ||  0));
			rodape.ed5Rep +=  parseInt((row.cell.ed5Reparte ||  0));
			rodape.ed5Venda +=  parseInt((row.cell.ed5Venda ||  0));
			rodape.ed6Rep +=  parseInt((row.cell.ed6Reparte ||  0));
			rodape.ed6Venda +=  parseInt((row.cell.ed6Venda ||  0));
		}
		
		 html = '<td width="50" >Qtde Cotas:</td>' +
		        '<td width="103" >' + rodape.qtdCota + '</td>' +
		        '<td width="30" align="right">' + rodape.qtdPdv + '</td>' +
		        '<td width="32" align="right">' + rodape.reparteMedio + '</td>' +
		        '<td width="32" align="right">' + rodape.vendaMedia + '</td>' +
		        '<td width="32" align="right">' + rodape.ed1Rep + '</td>' +
		        '<td width="32" align="right">' + rodape.ed1Venda + '</td>' +
		        '<td width="32" align="right">' + rodape.ed2Rep + '</td>' +
		        '<td width="32" align="right">' + rodape.ed2Venda + '</td>' +
		        '<td width="32" align="right">' + rodape.ed3Rep + '</td>' +
		        '<td width="32" align="right">' + rodape.ed3Venda + '</span></td>' +
		        '<td width="32" align="right">' + rodape.ed4Rep + '</td>' +
		        '<td width="32" align="right">' + rodape.ed4Venda + '</span></td>' +
		        '<td width="32" align="right">' + rodape.ed5Rep + '</td>' +
		        '<td width="32" align="right">' + rodape.ed5Venda + '</span></td>' +
		        '<td width="32" align="right">' + rodape.ed6Rep + '</td>' +
		        '<td width="32" align="right">' + rodape.ed6Venda + '</span></td>' +
		        '<td width="15" >&nbsp;</td>';
		 
		 $('#rodapeAnaliseHistorico').html(html);
		 
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
			}
		});
	}
	
}, BaseController);
//@ sourceURL=analiseHistoricoVenda.js