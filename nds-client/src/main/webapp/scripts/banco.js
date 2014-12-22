var bancoController = $.extend(true, {
	init : function() {

			$(".bancosGrid", this.workspace).flexigrid({
				preProcess: this.getDataFromResult,
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
					width : 148,
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
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status',
					name : 'status',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
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
				height : 180
			});
		
			$("#banco-numero", this.workspace).numeric();		
			
			$("#newNumero", this.workspace).numeric();	
			
			$("#newAgencia", this.workspace).numeric();	
			$("#newConta", this.workspace).numeric();	
			//$("#newDigito", this.workspace).numeric();
			$("#newCarteira", this.workspace).numeric();
			
			$("#alterNumero", this.workspace).numeric();	
			$("#alterAgencia", this.workspace).numeric();	
			$("#alterConta", this.workspace).numeric();	
			//$("#alterDigito", this.workspace).numeric();	
			$("#alterJuros", this.workspace).numeric();	
			$("#alterMulta", this.workspace).numeric();
			$("#alterVrMulta", this.workspace).numeric();
			
			$("#nome", this.workspace).autocomplete({source: []});
			
			bancoController.formatarValores();
			
			$(document).ready(function(){
				
				focusSelectRefField($("#nome", this.workspace));
				
				$(document.body).keydown(function(e) {
					if(keyEventEnterAux(e)){
						bancoController.mostrarGridConsulta();
					}
					
					return true;
				});
			});
		},
		
		formatarValores : function(){
			
			$('#newJuros', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#newMulta', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#newVrMulta', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			
			$('#alterJuros', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#alterMulta', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
			
			$('#alterVrMulta', this.workspace).priceFormat({
				allowNegative: false,
				centsSeparator: ',',
			    thousandsSeparator: '.'
			});
		},
		
	    popup : function() {
	    	
	    	if(!verificarPermissaoAcesso(this.workspace))
				return;
	    	
	    	bancoController.limparTelaCadastroBanco();
			
			$( "#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:310,
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
				form: $("#dialog-novo", this.workspace).parents("form")
			});
		},
		
		popup_alterar : function() {
		
			$( "#dialog-alterar", this.workspace).dialog({
				resizable: false,
				height:310,
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
				form: $("#dialog-alterar", this.workspace).parents("form")
			});	
			      
		},
		
		popup_excluir : function(idBanco) {
			
			if(!verificarPermissaoAcesso(this.workspace)){
				return;
			}
			
			$( "#dialog-excluir", this.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,

				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   bancoController.excluirBanco(idBanco);
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
				form: $("#dialog-excluir", this.workspace).parents("form")
				
			});
		},
		
	    mostrarGridConsulta : function(skipMsg) {
	    	
	    	$("#ativo", this.workspace).val(0);
	    	if (document.formularioFiltro.ativo.checked){
	    		$("#ativo", this.workspace).val(1);
			}
	    	
			/*PASSAGEM DE PARAMETROS*/
			$(".bancosGrid", this.workspace).flexOptions({
				/*METODO QUE RECEBERA OS PARAMETROS*/
				url: contextPath + "/banco/consultaBancos",
				params: [
				         {name:'nome', value:$("#nome", this.workspace).val()},
				         {name:'numero', value:$("#banco-numero", this.workspace).val()},
				         {name:'cedente', value:$("#cedente", this.workspace).val()},
				         {name:'ativo', value:$("#ativo", this.workspace).val() }
				        ] ,
				 newp: 1,
				 preProcess: function(resultado){return bancoController.getDataFromResult(resultado, skipMsg);}
			});
			
			/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
			$(".bancosGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
		},
		
		getDataFromResult : function(resultado, skipMsg) {
						
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens && !skipMsg) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids", this.workspace).hide();
				return resultado.tableModel;
			}
			
			var dadosPesquisa = null;
			
			$.each(resultado, function(index, value) {
				
				
				  if(value[0] == "TblModelBancos") {
					  dadosPesquisa = value[1];
				  
				  
					  $.each(dadosPesquisa.rows, 
								function(index, row) {
			
									 var linkEditar = '<a href="javascript:;" onclick="bancoController.editarBanco(' + row.cell[0] + ');" style="margin-right:10px; ">' +
				                                      '<img src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" title="Altera banco" />' +
					                                  '</a>';			
								
							         var linkExcluir =    '<a href="javascript:;" onclick="bancoController.popup_excluir(' + row.cell[0] + ');" style="cursor:pointer">' +
							                              '<img src="'+ contextPath + '/images/ico_excluir.gif" border="0px" title="Exclui banco" />' +
										                  '</a>';		 					 
													
								     row.cell[9] = linkEditar + linkExcluir;
			
						         }
						);
				  }
			});
			
			if(!dadosPesquisa) {
				$(".grids", this.workspace).hide();
				return resultado.tableModel;
			}
			
			return dadosPesquisa;
		},
		
		fecharDialogs : function() {
			$( "#dialog-novo", this.workspace ).dialog( "close" );
		    $( "#dialog-alterar", this.workspace ).dialog( "close" );
		    $( "#dialog-excluir", this.workspace ).dialog( "close" );
		},
		
	    novoBanco : function() {
			var param ={
					 numero     : $("#newNumero", this.workspace).val(),
					 nome       : $("#newNome", this.workspace).val(),
					 codigoCedente    : $("#newCodigoCedente", this.workspace).val(),
					 agencia    : $("#newAgencia", this.workspace).val(),
					 digitoAgencia    : $("#newDigitoAgencia", this.workspace).val(),
					 conta      : $("#newConta", this.workspace).val(),
					 digito     : $("#newDigito", this.workspace).val(),
					 apelido    : $("#newApelido", this.workspace).val(),
					 carteira   : $("#newCarteira", this.workspace).val(),
					 juros      : $("#newJuros", this.workspace).val(),
					 ativo      : $("#newAtivo", this.workspace).is(':checked'),
					 multa      : $("#newMulta", this.workspace).val(),
					 vrMulta    : $("#newVrMulta", this.workspace).val(),
					 instrucoes : $("#newInstrucoes", this.workspace).val()
			
			};

			$.postJSON(contextPath + "/banco/novoBanco",param,
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
			
			var param = {idBanco    : $("#idBanco", this.workspace).val(),
			    	 numero     : $("#alterNumero", this.workspace).val(),
					 nome       : $("#alterNome", this.workspace).val(),
					 codigoCedente    : $("#alterCodigoCedente", this.workspace).val(),
					 agencia    : $("#alterAgencia", this.workspace).val(),
					 digitoAgencia    : $("#alterDigitoAgencia", this.workspace).val(),
					 conta      : $("#alterConta", this.workspace).val(),
					 digito     : $("#alterDigito", this.workspace).val(),
					 apelido    : $("#alterApelido", this.workspace).val(),
					 carteira   : $("#alterCarteira", this.workspace).val(),
					 juros      : $("#alterJuros", this.workspace).val(),					
					 ativo      : $("#alterAtivo", this.workspace).is(':checked'),					
					 multa      : $("#alterMulta", this.workspace).val(),
					 vrMulta    : $("#alterVrMulta", this.workspace).val(),
					 instrucoes : $("#alterInstrucoes", this.workspace).val()};

			$.postJSON(contextPath + "/banco/alteraBanco", param,
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
			
			$("#idBanco", this.workspace).val(resultado.idBanco);
			$("#alterNumero", this.workspace).val(resultado.numero);
			$("#alterNome", this.workspace).val(resultado.nome);
			$("#alterCodigoCedente", this.workspace).val(resultado.codigoCedente);
			$("#alterAgencia", this.workspace).val(resultado.agencia);
			$("#alterDigitoAgencia", this.workspace).val(resultado.digitoAgencia);
			$("#alterConta", this.workspace).val(resultado.conta);
			$("#alterDigito", this.workspace).val(resultado.digito ? resultado.digito : '');
			$("#alterApelido", this.workspace).val(resultado.apelido);
			$("#alterCarteira", this.workspace).val(resultado.carteira);
			$("#alterJuros", this.workspace).val(resultado.juros);
			
			$("#alterAtivo", this.workspace).val(resultado.ativo);
			document.formularioAlteraBanco.alterAtivo.checked = resultado.ativo;
			
			$("#alterMulta", this.workspace).val(resultado.multa);
			$("#alterVrMulta", this.workspace).val(resultado.vrMulta);
			$("#alterInstrucoes", this.workspace).val(resultado.instrucoes);
			
			bancoController.formatarValores();
			
			bancoController.popup_alterar();
		},
		
	    excluirBanco : function(idBanco) {
	    	var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath + "/banco/excluirBanco",
					   data,
					   function(result) {
						   $( "#dialog-excluir", this.workspace ).dialog( "close" );
						   bancoController.mostrarGridConsulta(true);
						   if (result.tipoMensagem && result.listaMensagens) 
							   exibirMensagem(result.tipoMensagem,result.listaMensagens);
				       },
				       function(result) {
				    	   $( "#dialog-excluir", this.workspace ).dialog( "close" );
				    	   bancoController.mostrarGridConsulta(true);
				    	   if (result.tipoMensagem && result.listaMensagens) 
							   exibirMensagem(result.tipoMensagem,result.listaMensagens);
				       });
		},
		
	    limparTelaCadastroBanco : function() {
			$("#newNumero", this.workspace).val("");
			$("#newNome", this.workspace).val("");
			$("#newCodigoCedente", this.workspace).val("");
			$("#newAgencia", this.workspace).val("");
			$("#newConta", this.workspace).val("");
			$("#newDigito", this.workspace).val("");
			$("#newDigitoAgencia", this.workspace).val("");
			$("#newApelido", this.workspace).val("");
			$("#newCarteira", this.workspace).val("");
			$("#newJuros", this.workspace).val("");
			$("#newAtivo", this.workspace).val("");
			$("#newMulta", this.workspace).val("");
			$("#newVrMulta", this.workspace).val("");
			$("#newInstrucoes", this.workspace).val("");
		}, 
	    
	    limparMulta : function(){
	    	
	    	if(priceToFloat($("#newVrMulta", this.workspace).val()) == 0.0)
	    		$("#newVrMulta", this.workspace).val("");
	    	else 	    	
	    		$("#newMulta", this.workspace).val("");
	    	
	    	if(priceToFloat($("#alterVrMulta", this.workspace).val()) == 0.0)
	    		$("#alterVrMulta", this.workspace).val("");
	    	else 	    	
	    		$("#alterMulta", this.workspace).val("");
	    	
	    },
	    
	    limparVrMulta : function(){
	    	
	    	if(priceToFloat($("#newMulta", this.workspace).val()) == 0.0)
	    		$("#newMulta", this.workspace).val("");
	    	else 	    	
	    		$("#newVrMulta", this.workspace).val("");
	    	
	    	if(priceToFloat($("#alterMulta", this.workspace).val()) == 0.0)
	    		$("#alterMulta", this.workspace).val("");
	    	else 	    	
	    		$("#alterVrMulta", this.workspace).val("");
	    },
	    
		autoCompletarPorNomeBanco : function(idCampoNome) {
			
			var nomeBanco = $(idCampoNome,this.workspace).val();
			
			nomeBanco = $.trim(nomeBanco);
			
			if (nomeBanco && nomeBanco.length > 2) {
				
				$.postJSON(
					contextPath + "/banco/autoCompletarPorNomeBanco", {nomeBanco:nomeBanco},
					function(result) { 
					
						$(idCampoNome,this.workspace).autocomplete({
							source: result,
							delay : 0,
						});
						
						$(idCampoNome,this.workspace).autocomplete(
							"search",nomeBanco
						);
						
					},
					null
				);
			}
		}
	    
}, BaseController);

//@ sourceURL=scriptBancos.js
