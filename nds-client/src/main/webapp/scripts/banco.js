var bancoController = $.extend(true, {
	init : function() {

			$(".bancosGrid", bancoController.workspace).flexigrid({
				preProcess: bancoController.getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigo',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Banco',
					name : 'banco',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Agência / Dígito',
					name : 'agencia',
					width : 90,
					sortable : true,
					align : 'left',
				}, {
					display : 'Conta-Corrente / Dígito',
					name : 'contaCorrente',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Cedente',
					name : 'cedente',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Apelido',
					name : 'apelido',
					width : 80,
					sortable : true,
					align : 'left'
				},{
					display : 'Carteira',
					name : 'carteira',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status',
					name : 'status',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "Nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			});
		
			$("#numero", bancoController.workspace).numeric();	
			$("#cedente", bancoController.workspace).numeric();	
			
			$("#newNumero", bancoController.workspace).numeric();	
			$("#newCodigoCedente", bancoController.workspace).numeric();	
			$("#newAgencia", bancoController.workspace).numeric();	
			$("#newConta", bancoController.workspace).numeric();	
			$("#newDigito", bancoController.workspace).numeric();	
			$("#newJuros", bancoController.workspace).numeric();	
			$("#newMulta", bancoController.workspace).numeric();
			$("#newVrMulta", bancoController.workspace).numeric();
			
			$("#alterNumero", bancoController.workspace).numeric();	
			$("#alterCodigoCedente", bancoController.workspace).numeric();	
			$("#alterAgencia", bancoController.workspace).numeric();	
			$("#alterConta", bancoController.workspace).numeric();	
			$("#alterDigito", bancoController.workspace).numeric();	
			$("#alterJuros", bancoController.workspace).numeric();	
			$("#alterMulta", bancoController.workspace).numeric();
			$("#alterVrMulta", bancoController.workspace).numeric();

		},
		
	    popup : function() {
		
	    	bancoController.limparTelaCadastroBanco();
			
			$( "#dialog-novo", bancoController.workspace ).dialog({
				resizable: false,
				height:350,
				width:655,
				modal: true,
				buttons: {
					"Confirmar": function() {
						bancoController.novoBanco();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				},
				form: $("#dialog-novo", bancoController.workspace).parents("form")
			});
		},
		
		popup_alterar : function() {
		
			$( "#dialog-alterar", bancoController.workspace ).dialog({
				resizable: false,
				height:350,
				width:655,
				modal: true,
				buttons: {
					"Confirmar": function() {
						bancoController.alterarBanco();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
			    },
				form: $("#dialog-alterar", bancoController.workspace).parents("form")
			});	
			      
		},
		
		popup_excluir : function(idBanco) {
		
			$( "#dialog-excluir", bancoController.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,

				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   bancoController.desativarBanco(idBanco);
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ],
		        
				beforeClose: function() {
					clearMessageDialogTimeout();
			    },
				form: $("#dialog-excluir", bancoController.workspace).parents("form")
				
			});
		},
		
	    mostrarGridConsulta : function() {
	    	
	    	$("#ativo", bancoController.workspace).val(0);
	    	if (document.formularioFiltro.ativo.checked){
	    		$("#ativo", bancoController.workspace).val(1);
			}
	    	
			/*PASSAGEM DE PARAMETROS*/
			$(".bancosGrid", bancoController.workspace).flexOptions({
				/*METODO QUE RECEBERA OS PARAMETROS*/
				url: contextPath + "/banco/consultaBancos",
				params: [
				         {name:'nome', value:$("#nome", bancoController.workspace).val()},
				         {name:'numero', value:$("#numero", bancoController.workspace).val()},
				         {name:'cedente', value:$("#cedente", bancoController.workspace).val()},
				         {name:'ativo', value:$("#ativo", bancoController.workspace).val() }
				        ] ,
				        newp: 1
			});
			
			/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
			$(".bancosGrid", bancoController.workspace).flexReload();
			
			$(".grids", bancoController.workspace).show();
		},
		
		getDataFromResult : function(resultado) {
			
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids", bancoController.workspace).hide();
				return resultado.tableModel;
			}
			
			var dadosPesquisa;
			$.each(resultado, function(index, value) {
				  if(value[0] == "TblModelBancos") {
					  dadosPesquisa = value[1];
				  }
		    });

			$.each(dadosPesquisa.rows, 
					function(index, row) {

						 var linkEditar = '<a href="javascript:;" onclick="bancoController.editarBanco(' + row.cell[0] + ');" style="cursor:pointer">' +
	                                      '<img src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" title="Altera banco" />' +
		                                  '</a>';			
					
				         var linkExcluir =    '<a href="javascript:;" onclick="bancoController.popup_excluir(' + row.cell[0] + ');" style="cursor:pointer">' +
				                              '<img src="'+ contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" title="Exclui banco" />' +
							                  '</a>';		 					 
										
					     row.cell[9] = linkEditar + linkExcluir;

			         }
			);
			
			return dadosPesquisa;
		},
		
		fecharDialogs : function() {
			$( "#dialog-novo", bancoController.workspace ).dialog( "close" );
		    $( "#dialog-alterar", bancoController.workspace ).dialog( "close" );
		    $( "#dialog-excluir", bancoController.workspace ).dialog( "close" );
		},
		
	    novoBanco : function() {
			
	    	var numero     = $("#newNumero", bancoController.workspace).val();
			var nome       = $("#newNome", bancoController.workspace).val();
			var cedente    = $("#newCodigoCedente", bancoController.workspace).val();
			var agencia    = $("#newAgencia", bancoController.workspace).val();
			var conta      = $("#newConta", bancoController.workspace).val();
			var digito     = $("#newDigito", bancoController.workspace).val();
			var apelido    = $("#newApelido", bancoController.workspace).val();
			var carteira   = $("#newCarteira", bancoController.workspace).val();
			var juros      = $("#newJuros", bancoController.workspace).val();
			
			$("#newAtivo", bancoController.workspace).val(0);
	    	if (document.formularioNovoBanco.newAtivo.checked){
	    		$("#newAtivo", bancoController.workspace).val(1);
			}
			var ativo      = $("#newAtivo", bancoController.workspace).val();
			
			var multa      = $("#newMulta", bancoController.workspace).val();
			var vrMulta    = $("#newVrMulta", bancoController.workspace).val();
			var instrucoes = $("#newInstrucoes", bancoController.workspace).val();

			$.postJSON(contextPath + "/banco/novoBanco",
					   "numero="+numero+
					   "&nome="+ nome +
					   "&codigoCedente="+ cedente+
					   "&agencia="+ agencia +
					   "&conta="+ conta+
					   "&digito="+ digito+
					   "&apelido="+ apelido+
					   "&codigoCarteira="+ carteira+
					   "&juros="+ juros+
					   "&ativo="+ ativo+
					   "&multa="+ multa+
					   "&vrMulta="+ vrMulta+
					   "&instrucoes="+ instrucoes,
					   function(result) {
						   bancoController.fecharDialogs();
						   var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   bancoController.mostrarGridConsulta();
		               },
					   null,
					   true);
		},
		
		alterarBanco : function() {
			
			var idBanco    = $("#idBanco", bancoController.workspace).val();
	    	var numero     = $("#alterNumero", bancoController.workspace).val();
			var nome       = $("#alterNome", bancoController.workspace).val();
			var cedente    = $("#alterCodigoCedente", bancoController.workspace).val();
			var agencia    = $("#alterAgencia", bancoController.workspace).val();
			var conta      = $("#alterConta", bancoController.workspace).val();
			var digito     = $("#alterDigito", bancoController.workspace).val();
			var apelido    = $("#alterApelido", bancoController.workspace).val();
			var carteira   = $("#alterCarteira", bancoController.workspace).val();
			var juros      = $("#alterJuros", bancoController.workspace).val();
			
			$("#alterAtivo", bancoController.workspace).val(0);
	    	if (document.formularioAlteraBanco.alterAtivo.checked){
	    		$("#alterAtivo", bancoController.workspace).val(1);
			}
			var ativo      = $("#alterAtivo", bancoController.workspace).val();
			
			var multa      = $("#alterMulta", bancoController.workspace).val();
			var vrMulta    = $("#alterVrMulta", bancoController.workspace).val();
			var instrucoes = $("#alterInstrucoes", bancoController.workspace).val();

			$.postJSON(contextPath + "/banco/alteraBanco",
					   "idBanco="+idBanco+
					   "&numero="+numero+
					   "&nome="+ nome +
					   "&codigoCedente="+ cedente+
					   "&agencia="+ agencia +
					   "&conta="+ conta+
					   "&digito="+ digito+
					   "&apelido="+ apelido+
					   "&codigoCarteira="+ carteira+
					   "&juros="+ juros+
					   "&ativo="+ ativo+
					   "&multa="+ multa+
					   "&vrMulta="+ vrMulta+
					   "&instrucoes="+ instrucoes,
					   function(result) {
						   bancoController.fecharDialogs();
					       var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   bancoController.mostrarGridConsulta();
				       },
					   null,
					   true);
		},
		
		editarBanco : function(idBanco){
			var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath + "/banco/buscaBanco",
					   data,
					   bancoController.sucessCallbackCadastroBanco, 
					   bancoController.fecharDialogs);
		},
		
		sucessCallbackCadastroBanco : function(resultado) {
			
			$("#idBanco", bancoController.workspace).val(resultado.idBanco);
			$("#alterNumero", bancoController.workspace).val(resultado.numero);
			$("#alterNome", bancoController.workspace).val(resultado.nome);
			$("#alterCodigoCedente", bancoController.workspace).val(resultado.codigoCedente);
			$("#alterAgencia", bancoController.workspace).val(resultado.agencia);
			$("#alterConta", bancoController.workspace).val(resultado.conta);
			$("#alterDigito", bancoController.workspace).val(resultado.digito);
			$("#alterApelido", bancoController.workspace).val(resultado.apelido);
			$("#alterCarteira", bancoController.workspace).val(resultado.codigoCarteira);
			$("#alterJuros", bancoController.workspace).val(resultado.juros);
			
			$("#alterAtivo", bancoController.workspace).val(resultado.ativo);
			document.formularioAlteraBanco.alterAtivo.checked = resultado.ativo;
			
			$("#alterMulta", bancoController.workspace).val(resultado.multa);
			$("#alterVrMulta", bancoController.workspace).val(resultado.vrMulta);
			$("#alterInstrucoes", bancoController.workspace).val(resultado.instrucoes);
			
			bancoController.popup_alterar();
		},
	    desativarBanco : function(idBanco) {
	    	var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath + "/banco/desativaBanco",
					   data,
					   function(result) {
						   bancoController.fecharDialogs();
						   bancoController.mostrarGridConsulta();
				       },
				       function(result) {
				    	   bancoController.fecharDialogs();
						   bancoController.mostrarGridConsulta();
				       });
		},
		
	    limparTelaCadastroBanco : function() {
			$("#newNumero", bancoController.workspace).val("");
			$("#newNome", bancoController.workspace).val("");
			$("#newCodigoCedente", bancoController.workspace).val("");
			$("#newAgencia", bancoController.workspace).val("");
			$("#newConta", bancoController.workspace).val("");
			$("#newDigito", bancoController.workspace).val("");
			$("#newApelido", bancoController.workspace).val("");
			$("#newCarteira", bancoController.workspace).val("");
			$("#newJuros", bancoController.workspace).val("");
			$("#newAtivo", bancoController.workspace).val("");
			$("#newMulta", bancoController.workspace).val("");
			$("#newVrMulta", bancoController.workspace).val("");
			$("#newInstrucoes", bancoController.workspace).val("");
		}, 
	    
	    limparMulta : function(){
	    	$("#newMulta", bancoController.workspace).val("");
	    	$("#alterMulta", bancoController.workspace).val("");
	    },
	    
	    limparVrMulta : function(){
	    	$("#newVrMulta", bancoController.workspace).val("");
	    	$("#alterVrMulta", bancoController.workspace).val("");
	    }
	    
}, BaseController);
