var consultaBoletosController = $.extend({
	
	path : contextPath + '/financeiro/boletos/',
	
	init : function() {
		
		$("#boleto-dataDe", this.workspace).mask("99/99/9999");
		
		$( "#boleto-dataDe" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#boleto-dataAte", this.workspace).mask("99/99/9999");
		
		$("#boleto-dataAte" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#boleto-numCota", consultaBoletosController.workspace).numeric();
		
		$("#boleto-descricaoCota", consultaBoletosController.workspace).autocomplete({source: ""});
		
		consultaBoletosController.initGridBoleto();
		
		$('#linkConsultaBoletoXLS', consultaBoletosController.workspace).click(function() {
			var params = {"fileType": 'XLS'};
			
			$.fileDownload(consultaBoletosController.path + 'exportar', {
				httpMethod : "GET",
				data : params,
				failCallback: function (result) {
					
					result = $.parseJSON($(result).text());

					if((typeof result != "undefined") && result.mensagens) {
						
						result = result.mensagens;
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens, "");
						}
					}
			    }
			});
		});
		
		$('#linkConsultaBoletoPDF', consultaBoletosController.workspace).click(function() {
			var params = {"fileType": 'PDF'};
			
			$.fileDownload(consultaBoletosController.path + 'exportar', {
				httpMethod : "GET",
				data : params,
				failCallback: function (result) {
					
					result = $.parseJSON($(result).text());

					if((typeof result != "undefined") && result.mensagens) {
						
						result = result.mensagens;
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens, "");
						}
					}
			    }
			});
		});
		
	},
	
	mostrarGridConsulta : function() {
		
		/*PASSAGEM DE PARAMETROS*/
		$(".boletosCotaGrid", consultaBoletosController.workspace).flexOptions({	
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: contextPath + "/financeiro/boletos/consultaBoletos",
			params: [
			         {name:'numCota', value:$("#boleto-numCota", consultaBoletosController.workspace).val()},
			         {name:'descricaoCota', value:$("#boleto-descricaoCota", consultaBoletosController.workspace).val()},
			         {name:'dataDe', value:$("#boleto-dataDe", consultaBoletosController.workspace).val()},
			         {name:'dataAte', value:$("#boleto-dataAte", consultaBoletosController.workspace).val()},
			         {name:'status', value:$("#boleto-status", consultaBoletosController.workspace).val()}
			        ] ,
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boletosCotaGrid", consultaBoletosController.workspace).flexReload();
		
		$(".grids", consultaBoletosController.workspace).show();
	},
	
	getDataFromResult : function(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", consultaBoletosController.workspace).hide();
			return resultado.tableModel;
		}
		
		var dadosPesquisa;
		$.each(resultado, function(index, value) {
			  if(value[0] == "TblModelBoletos") {
				  dadosPesquisa = value[1];
			  }
	    });
		
		$.each(dadosPesquisa.rows, 
				function(index, row) {
			         var linkEmail='';
			         var linkImpressao='';
			         
		        	 linkImpressao = '<a isEdicao="true" href="javascript:;" onclick="consultaBoletosController.imprimeBoleto(\'' + row.cell[0] + '\');" style="cursor:pointer">' +
				 					 '<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0px" title="Imprime boleto" />' +
				 					 '</a>';
		        	 
		        	 
		        	 
		        	 if(row.cell[8] == 'true')
		        	 {
		        		 linkEmail = '<a isEdicao="true" href="javascript:;" onclick="consultaBoletosController.enviaBoleto(\'' + row.cell[0] + '\');" style="cursor:pointer">' +
	                     '<img src="' + contextPath + '/images/ico_email.png" hspace="5" border="0px" title="Envia boleto por e-mail" />' +
				             '</a>';
		        	 }
		        	 else
		        	 {
		        		 linkEmail = '<img border="0px" hspace="5" style="opacity:0.4; filter:alpha(opacity=40)"' +
		        		 'title="Cota não tem suporte para Envio de Arquivo por E-Mail" '+ 
		        		 'src="' + contextPath + '/images/ico_email.png">';
		        	 } 
			         		 					 
									
				     row.cell[8] = linkImpressao + linkEmail;
		         }
		);
		
		return dadosPesquisa;
	},
		
	enviaBoleto : function(nossoNumero) {
		var data = [
	   				   {
	   					   name: 'nossoNumero', value: nossoNumero
	   				   }
	   			   ];
		$.postJSON(contextPath + "/financeiro/boletos/enviaBoleto", data);
	},
	
	imprimeBoleto : function(nossoNumero) {
		var data = [
	   				   {
	   					   name: 'nossoNumero', value: nossoNumero
	   				   }
	   			   ];
		$.postJSON(contextPath + "/financeiro/boletos/verificaBoleto", data, consultaBoletosController.imprimirBoleto );
	},
	
	imprimirBoleto : function(result){
		
		if (result!=''){
			
			$('#download-iframe-boleto', consultaBoletosController.workspace).attr('src', contextPath + "/financeiro/boletos/imprimeBoleto?nossoNumero=" + result);
		}
	},

	initGridBoleto : function () {

		$(".boletosCotaGrid", consultaBoletosController.workspace).flexigrid({
			onSuccess: function() {bloquearItensEdicao(consultaBoletosController.workspace);},
		    preProcess: consultaBoletosController.getDataFromResult,
		    dataType : 'json',
			colModel : [ {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Emissão',
				name : 'dtEmissao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dtVencimento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Pagamento',
				name : 'dtPagto',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encargos',
				name : 'encargos',
				width : 65,
				sortable : true,
				align : 'right',
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Tipo Baixa',
				name : 'tipoBaixa',
				width : 95,
				sortable : true,
				align : 'center',
			}, {
				display : 'Status',
				name : 'status',
				width : 145,
				sortable : true,
				align : 'left',
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'left',
			}],
			sortname : "dtVencimento",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	}
	
}, BaseController);
//@ sourceURL=consultaBoletos.js