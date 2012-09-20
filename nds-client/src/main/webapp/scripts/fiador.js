var fiadorController = $.extend(true, {
		fecharModalCadastroFiador: false,
		addConjuge : false,
		
		popupCadastroFiadorCPF:function () {
			
			$("#fiadorController-tabSocio", fiadorController.workspace).hide();
			$('#fiadorController-tabs', fiadorController.workspace).tabs('select', 0);
			
			$("#fiadorController-cadastroCnpj", fiadorController.workspace).hide();
			$("#fiadorController-cadastroCpf", fiadorController.workspace).show();
			
			this.modalCadastroFiador("CPF", fiadorController.workspace);
		},
	
		popupCadastroFiadorCNPJ:function () {
			
			$("#fiadorController-tabSocio", fiadorController.workspace).show();
			$('#fiadorController-tabs', fiadorController.workspace).tabs('select', 0);
			
			$("#fiadorController-cadastroCnpj", fiadorController.workspace).show();
			$("#fiadorController-cadastroCpf", fiadorController.workspace).hide();
			
			this.modalCadastroFiador("CNPJ", fiadorController.workspace);
		},
		
		modalCadastroFiador: function (paramCpfCnpj){
			var _this = this;
			this.fecharModalCadastroFiador = false;
			
			this.limparCamposCadastroFiador();
			
			$("#fiadorController-dialog-fiador", fiadorController.workspace).dialog({
				resizable: false,
				height:560,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						if (paramCpfCnpj == "CPF") {
							_this.cadastrarFiadorCpf(this);
						} else {
							_this.cadastrarFiadorCnpj(this);
						}
					},
					"Cancelar": function() {
						
						$(this).dialog("close");
					}
				},
				beforeClose: function(event, ui) {
					
					if (!_this.fecharModalCadastroFiador){
						_this.cancelarCadastro();
					}
					
					return _this.fecharModalCadastroFiador;
				},
				form: $("#workspaceFiador", fiadorController.workspace)
			});
		
			$(".fiadorController-trSocioPrincipal", fiadorController.workspace).hide();
		},
		
		cancelarCadastro:function (){
			var _this =  this;
			$("#fiadorController-dialog-cancelar-cadastro-fiador", fiadorController.workspace).dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON(contextPath + '/cadastro/fiador/cancelarCadastro', null, 
							function(result){
								_this.fecharModalCadastroFiador = true;
								$("#fiadorController-dialog-close", fiadorController.workspace).dialog("close");
								$("#fiadorController-dialog-fiador", fiadorController.workspace).dialog("close");
								$("#fiadorController-dialog-cancelar-cadastro-fiador", fiadorController.workspace).dialog("close");					
								
								_this.limparCamposCadastroFiador();
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
						_this.fecharModalCadastroFiador = false;
					}
				},
				form: $("#fiadorController-dialog-cancelar-cadastro-fiador").parents("form")
			});
		},
	
		popup_excluir:function () {
			var _this = this;
			$( "#fiadorController-dialog-excluir", fiadorController.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						_this.cadastrarFiadorCnpj(this);
					},
					"Cancelar": function() {
						$(this).dialog( "close" );
					}
				},
				form: $("#fiadorController-dialog-excluir").parents("form")
			});
		},
		
		exibirGridFiadoresCadastrados:function (){
			var data = "filtro.nome=" + $("#fiadorController-nomeFiadorPesquisa", fiadorController.workspace).val() + "&filtro.cpfCnpj=" + $("#fiadorController-cpfCnpjFiadorPesquisa", fiadorController.workspace).val();
			$.postJSON(contextPath + '/cadastro/fiador/pesquisarFiador', data, 
				function(result){
					
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid", fiadorController.workspace).flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados,.fiadorController-gridFiadoresCadastrados", fiadorController.workspace).show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados,.fiadorController-gridFiadoresCadastrados", fiadorController.workspace).hide();
					}
				}
			);
		},
		
		init : function() {
			var _this  = this;
			$("#fiadorController-tabs", fiadorController.workspace).tabs();
			
			$(".fiadorController-pessoasGrid", fiadorController.workspace).flexigrid({
				preProcess: function(data){return _this.processarResultadoConsultaFiadores(data);},
				dataType : 'json',
				colModel : [  {
					display : 'Código',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'CPF / CNPJ',
					name : 'cpfCnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'RG / Inscrição Estadual',
					name : 'rg',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-Mail',
					name : 'email',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "desc",
				disableSelect: true,
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			});
			
			$(".fiadorController-pessoasGrid", fiadorController.workspace).flexOptions({url:contextPath + '/cadastro/fiador/pesquisarFiador'});
			
			
			$("#fiadorController-cnpjFiador", fiadorController.workspace).mask("99.999.999/9999-99",{completed:function(){_this.buscarPessoaCNPJ(this.val()); }});
			jQuery(function($){
				   $.mask.definitions['#']='[\-\.0-9]';
				   $("#fiadorController-inscricaoEstadualFiador", fiadorController.workspace).mask("?##################",{placeholder:" "});
			});
			
			$('#fiadorController-cpfFiador', fiadorController.workspace).mask("999.999.999-99",{completed:function(){
				_this.buscarPessoaCPF(this.value, true);
			}});	
			$('#fiadorController-socio-cpfFiador', fiadorController.workspace).mask("999.999.999-99",{completed:function(){
				_this.buscarPessoaCPF(this.value, true);
			}});
			$('#fiadorController-cpfConjuge, #fiadorController-socio-cpfFiador', fiadorController.workspace).mask("999.999.999-99");
			$('#fiadorController-dataNascimentoFiadorCpf,#fiadorController-socio-dataNascimentoFiadorCpf', fiadorController.workspace).mask("99/99/9999");
			$('#fiadorController-dataNascimentoConjugeCpf,#fiadorController-socio-dataNascimentoConjugeCpf', fiadorController.workspace).mask("99/99/9999");
			$('#fiadorController-selectUfOrgaoEmiCpf,#fiadorController-socio-selectUfOrgaoEmiConjugeCpf', fiadorController.workspace).mask("aa");
			
			$('.justLetter', fiadorController.workspace).justLetter();
			
			$(".fiadorController-sociosGrid", fiadorController.workspace).flexigrid({
				preProcess:function(data){return _this.processarResultadoSocios(data);},
				dataType : 'json',
				colModel : [  {
					display : 'Nome',
					name : 'nome',
					width : 580,
					sortable : true,
					align : 'left'
				}, {
					display : 'Principal',
					name : 'principal',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 160
			});
			
			$(".fiadorController-cotasAssociadasGrid", fiadorController.workspace).flexOptions({url:contextPath + '/cadastro/fiador/pesquisarSociosFiador'});
			
			
			
			$(".fiadorController-imoveisGrid", fiadorController.workspace).flexigrid({
				preProcess: function(data){return _this.processarResultadoGarantias(data);},
				dataType : 'json',
				colModel : [ {
					display : 'Descrição',
					name : 'garantia.descricao',
					width : 510,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor R$',
					name : 'garantia.valor',
					width : 130,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				} ],
				width : 770,
				height : 150,
				sortname : "garantia.descricao",
				sortorder : "asc",
				disableSelect: true
			});
			
			$(".fiadorController-imoveisGrid", fiadorController.workspace).flexOptions({url:contextPath + '/cadastro/fiador/obterGarantiasFiador'});
			
			$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).numeric();
			$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).priceFormat({
			    centsSeparator: ',',
			    thousandsSeparator: '.',
			    centsLimit: 4
			});
			
			$(".fiadorController-cotasAssociadasGrid", fiadorController.workspace).flexigrid({
				preProcess: function(data){return _this.processarResultadoCotasAssociadas(data);},
				url: contextPath + '/cadastro/fiador/obterAssociacoesCotaFiador',
				dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 620,
					sortable : true,
					align : 'left'
				}, {
					display : '',
					name : 'sel',
					width : 30,
					sortable : false,
					align : 'center'
				} ],
				width : 770,
				height : 150,
				sortname : "numeroCota",
				sortorder : "asc",
				disableSelect: true
			});
			
			$("#numeroCota", fiadorController.workspace).numeric();
		
			
			
		},
		
		processarResultadoConsultaFiadores:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					"fiadorController-dialog-fiador"
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result[1].rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = this.getActionsConsultaFiadores(data.rows[i].id);
			}

			$('.fiadorController-socio-imoveisGrid', fiadorController.workspace).show();
			
			if (data.result){
				return data.result[1];
			}
			return data;
		},
		
		getActionsConsultaFiadores: function (idFiador){
			return '<a href="javascript:;" onclick="fiadorController.editarFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Fiador">' +
					'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="fiadorController.excluirFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fiador">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		},
		
		editarFiador:function (idFiador){
			var _this = this;
			$.postJSON(contextPath +'/cadastro/fiador/editarFiador', "idFiador=" + idFiador, 
				function(result) {
					
					$(".fiadorController-inicioAtividadeNovo", fiadorController.workspace).hide();
					$(".fiadorController-inicioAtividadeEdicao", fiadorController.workspace).show();
					
					_this.limparCamposCadastroFiador();
				
					if (result[0] == "CPF"){
						
						_this.popupCadastroFiadorCPF();
						
						$("#fiadorController-nomeFiadorCpf", fiadorController.workspace).val(result[1]);
						$("#fiadorController-emailFiadorCpf", fiadorController.workspace).val(result[2]);
						$("#fiadorController-cpfFiador", fiadorController.workspace).val(result[3]).attr("disabled", true);
						$("#fiadorController-rgFiador", fiadorController.workspace).val(result[4]);
						$("#fiadorController-dataNascimentoFiadorCpf", fiadorController.workspace).val(result[5]);
						$("#fiadorController-orgaoEmissorFiadorCpf", fiadorController.workspace).val(result[6]);
						$("#fiadorController-selectUfOrgaoEmiCpf", fiadorController.workspace).val(result[7]);
						$("#fiadorController-estadoCivilFiadorCpf", fiadorController.workspace).val(result[8]);
						$("#fiadorController-selectSexoFiador", fiadorController.workspace).val(result[9]);
						$("#fiadorController-nacionalidadeFiadorCpf", fiadorController.workspace).val(result[10]);
						$("#fiadorController-naturalFiadorCpf", fiadorController.workspace).val(result[11]);
						
						if (result[8] == "CASADO"){
							
							_this.opcaoCivilPf("CASADO",'fiadorController-');
							
							$("#fiadorController-nomeConjugeCpf", fiadorController.workspace).val(result[12]);
					        $("#fiadorController-emailConjugeCpf", fiadorController.workspace).val(result[13]);
					        $("#fiadorController-cpfConjuge", fiadorController.workspace).val(result[14]).attr("disabled", true);
					        $("#fiadorController-rgConjuge", fiadorController.workspace).val(result[15]);
					        $("#fiadorController-dataNascimentoConjugeCpf", fiadorController.workspace).val(result[16]);
					        $("#fiadorController-orgaoEmissorConjugeCpf", fiadorController.workspace).val(result[17]);
					        $("#fiadorController-selectUfOrgaoEmiConjugeCpf", fiadorController.workspace).val(result[18]);
					        $("#fiadorController-selectSexoConjuge", fiadorController.workspace).val(result[19]);
					        $("#fiadorController-nacionalidadeConjugeCpf", fiadorController.workspace).val(result[20]);
					        $("#fiadorController-naturalConjugeCpf", fiadorController.workspace).val(result[21]);
					        
					        $(".fiadorController-inicioAtividadeEdicao", fiadorController.workspace).text(result[22]);
					        
						} else {
							$(".fiadorController-inicioAtividadeEdicao", fiadorController.workspace).text(result[12]);
						}
						
					} else {
						
						_this.popupCadastroFiadorCNPJ();
						
						$("#fiadorController-razaoSocialFiador", fiadorController.workspace).val(result[1]);
						$("#fiadorController-nomeFantasiaFiador", fiadorController.workspace).val(result[2]);
						$("#fiadorController-inscricaoEstadualFiador", fiadorController.workspace).val(result[3]);
						$("#fiadorController-cnpjFiador", fiadorController.workspace).val(result[4]);
						$("#fiadorController-emailFiadorCnpj", fiadorController.workspace).val(result[5]);
						$(".fiadorController-inicioAtividadeEdicao", fiadorController.workspace).text(result[6]);
						
						$("#fiadorController-cnpjFiador", fiadorController.workspace).attr("disabled", true);
					}
				}
			);
		},
		
		excluirFiador:function (idFiador){
			$(".fiadorController-dialog-excluir-fiador", fiadorController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath + '/cadastro/fiador/excluirFiador', "idFiador=" + idFiador, 
							function(result) {
								
								if (result[0].tipoMensagem){
									exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
								}
								
								if (result[1] != ""){
									$(".fiadorController-pessoasGrid", fiadorController.workspace).flexAddData({
										page: result[1].page, total: result[1].total, rows: result[1].rows
									});
									
									$("#fiadorController-gridFiadoresCadastrados", fiadorController.workspace).show();
								} else {
									$("#fiadorController-gridFiadoresCadastrados", fiadorController.workspace).hide();
								}
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				},
				form: $(".fiadorController-dialog-excluir-fiador").parents("form")
			});
			
			$(".fiadorController-dialog-excluir-fiador", fiadorController.workspace).show();
		},
		
		
		limparCamposCadastroFiador:function (){
			//dados cadastrais cpf
			this.limparDadosCadastraisCPFFiador();
	        
	        //dados cadastrais cnpj
			this.limparDadosCadastraisCNPJ();
	        
			//dados cadastrais socios
			this.limparDadosCadastraisCPFSocio();
		    
		    //endereÃ§os
			ENDERECO_FIADOR.limparFormEndereco();
		    
		    //telefones
		    FIADOR.limparCamposTelefone();
		    
		    //garantias
		    this.limparCamposGarantias();
		    
		    //cotas associadas
		    this.limparCamposCotasAssociadas();
		    
		    this.opcaoCivilPf("",'fiadorController-socio-');
		    this.opcaoCivilPf("",'fiadorController-');
		},
		cadastrarFiadorCnpj :function (janela){
			var _this = this;
			this.fecharModalCadastroFiador = true;
			
			var data = "fiador.razaoSocial=" + $("#fiadorController-razaoSocialFiador", fiadorController.workspace).val() + "&" +
			           "fiador.nomeFantasia=" + $("#fiadorController-nomeFantasiaFiador", fiadorController.workspace).val() + "&" +
			           "fiador.inscricaoEstadual=" + $("#fiadorController-inscricaoEstadualFiador", fiadorController.workspace).val() + "&" +
			           "fiador.cnpj=" + $("#fiadorController-cnpjFiador", fiadorController.workspace).val() + "&" +
			           "fiador.email=" + $("#fiadorController-emailFiadorCnpj", fiadorController.workspace).val();
			
			$.postJSON(contextPath + '/cadastro/fiador/cadastrarFiadorCnpj', data, 
				function(result){
			
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid", fiadorController.workspace).flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados", fiadorController.workspace).show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados", fiadorController.workspace).hide();
					}
					
					if (result[0].tipoMensagem == "SUCCESS"){
						$(janela).dialog("close");
						$("#fiadorController-cnpjFiador", fiadorController.workspace).removeAttr("disabled");
					}
				},
				function(){
					_this.fecharModalCadastroFiador = false;
				},
				true,
				"dialog-fiador"
			);
		},
		
		limparDadosCadastraisCNPJ:function (){
			$("#fiadorController-razaoSocialFiador", fiadorController.workspace).val("");
			$("#fiadorController-nomeFantasiaFiador", fiadorController.workspace).val("");
			$("#fiadorController-inscricaoEstadualFiador", fiadorController.workspace).val("");
			$("#fiadorController-cnpjFiador", fiadorController.workspace).val("");
			$("#fiadorController-emailFiadorCnpj", fiadorController.workspace).val("");
		},
		
		buscarPessoaCNPJ:function (cnpj){
			
			if (cnpj != "__.___.___/____-__" && cnpj != ""){
				
				$.postJSON(contextPath + '/cadastro/fiador/buscarPessoaCNPJ', "cnpj=" + cnpj, 
					function(result) {
						
						if (result[0] != undefined){
							$("#fiadorController-razaoSocialFiador", fiadorController.workspace).val(result[0]);
							$("#fiadorController-nomeFantasiaFiador", fiadorController.workspace).val(result[1]);
							$("#fiadorController-inscricaoEstadualFiador", fiadorController.workspace).val(result[2]);
							$("#fiadorController-cnpjFiador", fiadorController.workspace).val(result[3]);
							$("#fiadorController-emailFiadorCnpj", fiadorController.workspace).val(result[4]);
						}
					},
					null,
					true
				);
			}
		},
		
		opcaoCivilPf:function (valor,prefix){
			if (valor == "CASADO"){
				$("."+prefix+"divConjuge", fiadorController.workspace).show();
				this.addConjuge = true;
			} else {
				$("."+prefix+"divConjuge", fiadorController.workspace).hide();
				this.addConjuge = false;
			}
		},
		
		cadastrarFiadorCpf:function (janela){
			var _this = this;
			this.fecharModalCadastroFiador = true;
			
			var data = "pessoa.nome=" + $("#fiadorController-nomeFiadorCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.email=" + $("#fiadorController-emailFiadorCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.cpf=" + $("#fiadorController-cpfFiador", fiadorController.workspace).val() + "&" +
			           "pessoa.rg=" + $("#fiadorController-rgFiador", fiadorController.workspace).val() + "&" +
			           "pessoa.dataNascimento=" + $("#fiadorController-dataNascimentoFiadorCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.orgaoEmissor=" + $("#fiadorController-orgaoEmissorFiadorCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.ufOrgaoEmissor=" + $("#fiadorController-selectUfOrgaoEmiCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.estadoCivil=" + $("#fiadorController-estadoCivilFiadorCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.sexo=" + $("#fiadorController-selectSexoFiador", fiadorController.workspace).val() + "&" +
			           "pessoa.nacionalidade=" + $("#fiadorController-nacionalidadeFiadorCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.natural=" + $("#fiadorController-naturalFiadorCpf", fiadorController.workspace).val();
			
			if (this.addConjuge){ 
			           data = data + "&pessoa.conjuge.nome=" + $("#fiadorController-nomeConjugeCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.email=" + $("#fiadorController-emailConjugeCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.cpf=" + $("#fiadorController-cpfConjuge", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.rg=" + $("#fiadorController-rgConjuge", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.dataNascimento=" + $("#fiadorController-dataNascimentoConjugeCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.orgaoEmissor=" + $("#fiadorController-orgaoEmissorConjugeCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.ufOrgaoEmissor=" + $("#fiadorController-selectUfOrgaoEmiConjugeCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.sexo=" + $("#fiadorController-selectSexoConjuge", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.nacionalidade=" + $("#fiadorController-nacionalidadeConjugeCpf", fiadorController.workspace).val() + "&" +
			           "pessoa.conjuge.natural=" + $("#fiadorController-naturalConjugeCpf", fiadorController.workspace).val();
			}
			
			$.postJSON(contextPath + '/cadastro/fiador/cadastrarFiadorCpf', data, 
				function(result){
					
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
				
					if (result[1]){
						$(".fiadorController-pessoasGrid", fiadorController.workspace).flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados", fiadorController.workspace).show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados", fiadorController.workspace).hide();
					}
					
					if (result[0].tipoMensagem == "SUCCESS"){
						$(janela).dialog("close");
						$("#fiadorController-cpfFiador", fiadorController.workspace).removeAttr("disabled");
						$("#fiadorController-cpfConjuge", fiadorController.workspace).removeAttr("disabled");
					}
				},
				function (){
					_this.fecharModalCadastroFiador = false;
				},
				true,
				"dialog-fiador"
			);
		},
		limparDadosCadastraisCPFFiador:function(){
			$('input','#fiadorController-cadastroCpf').attr('disabled', false).val("").uncheck();
		},
		limparDadosCadastraisCPFSocio:function(){
			$('input','#fiadorController-tab-socios').attr('disabled', false).val("").uncheck();
		},
		buscarPessoaCPF:function (cpf, fiador){
			var _this = this;
			
			if (cpf != "___.___.___-__" && cpf != ""){
				
				var data = "cpf=" + cpf + "&isFiador=" + fiador + "&cpfConjuge=" + $('#fiadorController-cpfConjuge', fiadorController.workspace).val() +
					"&socio=" + (!fiador);
				
				$.postJSON(contextPath +  '/cadastro/fiador/buscarPessoaCPF', data, 
					function(result) {
					
						if (result[0] != undefined){
							
							if (fiador){
								$('#fiadorController-nomeFiadorCpf', fiadorController.workspace).val(result[0]);
								$('#fiadorController-emailFiadorCpf', fiadorController.workspace).val(result[1]);
								$('#fiadorController-cpfFiador', fiadorController.workspace).val(result[2]);
								$('#fiadorController-rgFiador', fiadorController.workspace).val(result[3]);
								$('#fiadorController-dataNascimentoFiadorCpf', fiadorController.workspace).val(result[4]);
								$('#fiadorController-orgaoEmissorFiadorCpf', fiadorController.workspace).val(result[5]);
								$('#fiadorController-selectUfOrgaoEmiCpf', fiadorController.workspace).val(result[6]);
								$('#fiadorController-estadoCivilFiadorCpf', fiadorController.workspace).val(result[7]);
								$('#fiadorController-selectSexoFiador', fiadorController.workspace).val(result[8]);
								$('#fiadorController-nacionalidadeFiadorCpf', fiadorController.workspace).val(result[9]);
								$('#fiadorController-naturalFiadorCpf', fiadorController.workspace).val(result[10]);
								
								if (result[7] == "CASADO"){
									
									_this.opcaoCivilPf(result[7],'fiadorController-');
									
									$('#fiadorController-nomeConjugeCpf', fiadorController.workspace).val(result[11]);
									$('#fiadorController-emailConjugeCpf', fiadorController.workspace).val(result[12]);
									$('#fiadorController-cpfConjuge', fiadorController.workspace).val(result[13]);
									$('#fiadorController-rgConjuge', fiadorController.workspace).val(result[14]);
									$('#fiadorController-dataNascimentoConjugeCpf', fiadorController.workspace).val(result[15]);
									$('#fiadorController-orgaoEmissorConjugeCpf', fiadorController.workspace).val(result[16]);
									$('#fiadorController-selectUfOrgaoEmiConjugeCpf', fiadorController.workspace).val(result[17]);
									$('#fiadorController-selectSexoConjuge', fiadorController.workspace).val(result[18]);
									$('#fiadorController-nacionalidadeConjugeCpf', fiadorController.workspace).val(result[19]);
									$('#fiadorController-naturalConjugeCpf', fiadorController.workspace).val(result[20]);
								}
							} else {
								
								$('#fiadorController-socio-nomeConjugeCpf', fiadorController.workspace).val(result[0]);
								$('#fiadorController-socio-emailConjugeCpf', fiadorController.workspace).val(result[1]);
								$('#fiadorController-socio-cpfConjuge', fiadorController.workspace).val(result[2]);
								$('#fiadorController-socio-rgConjuge', fiadorController.workspace).val(result[3]);
								$('#fiadorController-socio-dataNascimentoConjugeCpf', fiadorController.workspace).val(result[4]);
								$('#fiadorController-socio-orgaoEmissorConjugeCpf', fiadorController.workspace).val(result[5]);
								$('#fiadorController-socio-selectUfOrgaoEmiConjugeCpf', fiadorController.workspace).val(result[6]);
								$('#fiadorController-socio-selectSexoConjuge', fiadorController.workspace).val(result[8]);
								$('#fiadorController-socio-nacionalidadeConjugeCpf', fiadorController.workspace).val(result[9]);
								$('#fiadorController-socio-naturalConjugeCpf', fiadorController.workspace).val(result[10]);
							}
						}
					},
					null,
					true
				);
			}
		},
		
		carregarSocios:function (){
			$.postJSON(contextPath + '/cadastro/fiador/pesquisarSociosFiador', null, 
				function(result) {
					$(".fiadorController-sociosGrid", fiadorController.workspace).flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					$("#fiadorController-btnAddEditarSocio", fiadorController.workspace).text("Incluir Novo");
				},
				null,
				true
			);
		},
		
		processarResultadoSocios:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					"fiadorController-dialog-fiador"
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result.rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = fiadorController.getActionsSocios(data.rows[i].id);
				
				data.rows[i].cell[1] = 
					data.rows[i].cell[1] == "true" ? '<img src="'+contextPath+'/images/ico_check.gif" border="0px"/>'
						                           : '&nbsp;';
			}

			$('.fiadorController-sociosGrid', fiadorController.workspace).show();
			
			if (data.result){
				return data.result;
			}
			return data;
		},
		
		getActionsSocios:function (idSocio){
			return '<a href="javascript:;" onclick="fiadorController.editarSocio(' + idSocio + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Socio">' +
					'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="fiadorController.removerSocio(' + idSocio + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Socio">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		},
		
		adicionarSocio: function (){
			var data = "pessoa.nome=" + $('#fiadorController-socio-nomeFiadorCpf', fiadorController.workspace).val() + "&" +
				"pessoa.email=" + $('#fiadorController-socio-emailFiadorCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.cpf=" + $('#fiadorController-socio-cpfFiador', fiadorController.workspace).val() + "&" +
		        "pessoa.rg=" + $('#fiadorController-socio-rgFiador', fiadorController.workspace).val() + "&" +
		        "pessoa.dataNascimento=" + $('#fiadorController-socio-dataNascimentoFiadorCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.orgaoEmissor=" + $('#fiadorController-socio-orgaoEmissorFiadorCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.ufOrgaoEmissor=" + $('#fiadorController-socio-selectUfOrgaoEmiCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.estadoCivil=" + $('#fiadorController-socio-estadoCivilFiadorCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.sexo=" + $('#fiadorController-socio-selectSexoFiador', fiadorController.workspace).val() + "&" +
		        "pessoa.nacionalidade=" + $('#fiadorController-socio-nacionalidadeFiadorCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.natural=" + $('#fiadorController-socio-naturalFiadorCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.socioPrincipal=" + ("" + $('#fiadorController-socio-checkboxSocioPrincipal', fiadorController.workspace).attr("checked") == 'checked');
		    
		    if (this.addConjuge){ 
		        data = data + "&pessoa.conjuge.nome=" + $('#fiadorController-socio-nomeConjugeCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.email=" + $('#fiadorController-socio-emailConjugeCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.cpf=" + $('#fiadorController-socio-cpfConjuge', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.rg=" + $('#fiadorController-socio-rgConjuge', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.dataNascimento=" + $('#fiadorController-socio-dataNascimentoConjugeCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.orgaoEmissor=" + $('#fiadorController-socio-orgaoEmissorConjugeCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.ufOrgaoEmissor=" + $('#fiadorController-socio-selectUfOrgaoEmiConjugeCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.sexo=" + $('#fiadorController-socio-selectSexoConjuge', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.nacionalidade=" + $('#fiadorController-socio-nacionalidadeConjugeCpf', fiadorController.workspace).val() + "&" +
		        "pessoa.conjuge.natural=" + $('#fiadorController-socio-naturalConjugeCpf', fiadorController.workspace).val();
		    }
		    
		    data = data + "&referencia=" + $("#fiadorController-socio-idSocioEdicao", fiadorController.workspace).val();
		    var _this =this;
			$.postJSON(contextPath +'/cadastro/fiador/adicionarSocio', data, 
				function(result){
			
					if (result != ""){
						$(".fiadorController-sociosGrid", fiadorController.workspace).flexAddData({
							page: result.page, total: result.total, rows: result.rows
						});
						
						_this.limparDadosCadastraisCPFSocio();
						
						_this.opcaoCivilPf("",'fiadorController-socio-', fiadorController.workspace);
						
						$("#fiadorController-btnAddEditarSocio", fiadorController.workspace).text("Incluir Novo");
						
						$('#fiadorController-socio-cpfFiador', fiadorController.workspace).removeAttr("disabled");
						$('#fiadorController-socio-cpfConjuge', fiadorController.workspace).removeAttr("disabled");
						
						$("#fiadorController-socio-idSocioEdicao", fiadorController.workspace).val("");
					}
				},
				null,
				true
			);
		},
		
		editarSocio : function (referencia){
			var _this = this;
			$.postJSON(contextPath + '/cadastro/fiador/editarSocio', "referencia=" + referencia, 
				function(result) {
					
				_this.limparDadosCadastraisCPFSocio();
				
					if (result){
						
						$('#fiadorController-socio-nomeFiadorCpf', fiadorController.workspace).val(result[0]);
						$('#fiadorController-socio-emailFiadorCpf', fiadorController.workspace).val(result[1]);
						$('#fiadorController-socio-cpfFiador', fiadorController.workspace).val(result[2]);
						$('#fiadorController-socio-rgFiador', fiadorController.workspace).val(result[3]);
						$('#fiadorController-socio-dataNascimentoFiadorCpf', fiadorController.workspace).val(result[4]);
						$('#fiadorController-socio-orgaoEmissorFiadorCpf', fiadorController.workspace).val(result[5]);
						$('#fiadorController-socio-selectUfOrgaoEmiCpf', fiadorController.workspace).val(result[6]);
						$('#fiadorController-socio-estadoCivilFiadorCpf', fiadorController.workspace).val(result[7]);
						$('#fiadorController-socio-selectSexoFiador', fiadorController.workspace).val(result[8]);
						$('#fiadorController-socio-nacionalidadeFiadorCpf', fiadorController.workspace).val(result[9]);
						$('#fiadorController-socio-naturalFiadorCpf', fiadorController.workspace).val(result[10]);
						
						if (result[7] == "CASADO"){
							
							_this.opcaoCivilPf(result[7],'fiadorController-socio-');
							
							$('#fiadorController-socio-nomeConjugeCpf', fiadorController.workspace).val(result[11]);
							$('#fiadorController-socio-emailConjugeCpf', fiadorController.workspace).val(result[12]);
							$('#fiadorController-socio-cpfConjuge', fiadorController.workspace).val(result[13]);
							$('#fiadorController-socio-rgConjuge', fiadorController.workspace).val(result[14]);
							$('#fiadorController-socio-dataNascimentoConjugeCpf', fiadorController.workspace).val(result[15]);
							$('#fiadorController-socio-orgaoEmissorConjugeCpf', fiadorController.workspace).val(result[16]);
							$('#fiadorController-socio-selectUfOrgaoEmiConjugeCpf', fiadorController.workspace).val(result[17]);
							$('#fiadorController-socio-selectSexoConjuge', fiadorController.workspace).val(result[18]);
							$('#fiadorController-socio-nacionalidadeConjugeCpf', fiadorController.workspace).val(result[19]);
							$('#fiadorController-socio-naturalConjugeCpf', fiadorController.workspace).val(result[20]);
							
							if (result[21] == "true"){
								$('#fiadorController-socio-checkboxSocioPrincipal', fiadorController.workspace).check();
							} else {
								$('#fiadorController-socio-checkboxSocioPrincipal', fiadorController.workspace).uncheck();
							}
						} else {
							
							if (result[11] == "true"){
								$('#fiadorController-socio-checkboxSocioPrincipal', fiadorController.workspace).check();
							} else {
								$('#fiadorController-socio-checkboxSocioPrincipal', fiadorController.workspace).uncheck();
							}
						}
						
						
						
						$("#fiadorController-idSocioEdicao", fiadorController.workspace).val(referencia);
					}
					
					$('#fiadorController-socio-cpfFiador', fiadorController.workspace).attr("disabled", true);
					$('#fiadorController-socio-cpfConjuge', fiadorController.workspace).attr("disabled", true);
					
					$("#fiadorController-btnAddEditarSocio", fiadorController.workspace).text("Editar");
				}
			);
		},
		
		removerSocio:function (referencia){
			var _this = this;
			$("#fiadorController-dialog-excluir-socio", fiadorController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath + '/cadastro/fiador/removerSocio', "referencia=" + referencia, 
							function(result) {
								$(".sociosGrid", fiadorController.workspace).flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								$("#fiadorController-btnAddEditarSocio", fiadorController.workspace).text("Incluir Novo");
								_this.limparDadosCadastraisCPFSocio();
							},
							null,
							true
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				},
				form: $("#fiadorController-dialog-excluir-socio").parents("form")
			});
			
			$("#fiadorController-dialog-excluir-socio", fiadorController.workspace).show();
		},
		
		carregarGarantias:function (){
			$.postJSON(contextPath + '/cadastro/fiador/obterGarantiasFiador', null, 
				function(result) {
					$(".fiadorController-imoveisGrid", fiadorController.workspace).flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});	
					
					$("#fiadorController-botaoAddEditarGarantia", fiadorController.workspace).text("Incluir Novo");
					
					$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).val("");
					$("#fiadorController-garantia-descricaoGarantia", fiadorController.workspace).val("");
				},
				null,
				true
			);
		},
		
		processarResultadoGarantias:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					"fiadorController-dialog-fiador"
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result.rows;
			}
			
			var i;
			
			for (i = 0 ; i < data.rows.length; i++) {
				
				var lastIndex = data.rows[i].cell.length;
				
				data.rows[i].cell[lastIndex] = fiadorController.getActionsGarantia(data.rows[i].id);
			}

			$('.fiadorController-imoveisGrid', fiadorController.workspace).show();
			
			if (data.result){
				return data.result;
			}
			return data;
		},
		
		getActionsGarantia:function (idGarantia) {

			return '<a href="javascript:;" onclick="fiadorController.editarGarantia(' + idGarantia + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Garantia">' +
					'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/></a>' +
					'<a href="javascript:;" onclick="fiadorController.removerGarantia(' + idGarantia + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Garantia">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/></a>';
		},
		
		adicionarEditarGarantia:function (){
			var data = "garantia.valor=" + $("#fiadorController-garantia-valorGarantia", fiadorController.workspace).floatValue() + "&" +
	        		   "garantia.descricao=" + $("#fiadorController-garantia-descricaoGarantia", fiadorController.workspace).val() + "&" +
	        		   "referencia=" + $("#fiadorController-garantia-referenciaGarantia", fiadorController.workspace).val();
			
			$.postJSON(contextPath + '/cadastro/fiador/adicionarGarantia', data, 
				function(result) {
					$('.fiadorController-imoveisGrid', fiadorController.workspace).flexAddData({
						page: 1, total: 1, rows: result.rows
					});	
					
					$("#fiadorController-botaoAddEditarGarantia", fiadorController.workspace).text("Incluir Novo");
					
					$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).val("");
					$("#fiadorController-garantia-descricaoGarantia", fiadorController.workspace).val("");
					$("#fiadorController-referenciaGarantia", fiadorController.workspace).val("");
				},
				null,
				true
			);
		},
		
		editarGarantia:function (referencia){
			$.postJSON(contextPath + '/cadastro/fiador/editarGarantia', "referencia=" + referencia, 
				function(result) {
					
					$("#fiadorController-botaoAddEditarGarantia", fiadorController.workspace).text("Editar");
					
					$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).val(result.valor);
					$("#fiadorController-garantia-descricaoGarantia", fiadorController.workspace).val(result.descricao);
					$("#fiadorController-referenciaGarantia", fiadorController.workspace).val(referencia);
				},
				null,
				true
			);
		}, 
		
		removerGarantia:function (referencia){
			$("#fiadorController-dialog-excluir-garantia", fiadorController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath + '/cadastro/fiador/excluirGarantia', "referencia=" + referencia, 
							function(result) {
								
								$(".fiadorController-imoveisGrid", fiadorController.workspace).flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								$("#fiadorController-botaoAddEditarGarantia", fiadorController.workspace).text("Incluir Novo");
								
								$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).val("");
								$("#fiadorController-garantia-descricaoGarantia", fiadorController.workspace).val("");
								$("#fiadorController-referenciaGarantia", fiadorController.workspace).val("");
							},
							null,
							true
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				},
				form: $("#fiadorController-dialog-excluir-garantia").parents("form")
			});
			
			$("#fiadorController-dialog-excluir-garantia", fiadorController.workspace).show();
		},
		
		limparCamposGarantias:function (){
			$("#fiadorController-garantia-valorGarantia", fiadorController.workspace).val("");
			$("#fiadorController-garantia-descricaoGarantia", fiadorController.workspace).val("");
		},
		
		processarResultadoCotasAssociadas:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					"fiadorController-dialog-fiador"
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result.rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = fiadorController.getActionCotaCadastrada(data.rows[i].id);
			}

			$('.fiadorController-cotasAssociadasGrid', fiadorController.workspace).show();
			
			if (data.result){
				return data.result;
			}
			return data;
		},
		
		getActionCotaCadastrada:function (referencia){
			return '<a href="javascript:;" onclick="fiadorController.removerAssociacaoCota(' + referencia + ')" ' +
			' style="cursor:pointer;border:0px;margin:5px" title="Excluir AssociaÃ§Ã£o">' +
			'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
			'</a>';
		},
		
		carregarCotasAssociadas:function (){
			$.postJSON(contextPath+'/cadastro/fiador/obterAssociacoesCotaFiador', null, 
				function(result) {
					$(".fiadorController-cotasAssociadasGrid", fiadorController.workspace).flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});
					
					$("#fiadorController-cotasAssociadas-numeroCota", fiadorController.workspace).val("");
					$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val("");
				},
				null,
				true
			);
		},
		
		adicionarAssociacaoCota:	function (){
			var data = "numeroCota=" + $("#fiadorController-cotasAssociadas-numeroCota", fiadorController.workspace).val() + "&nomeCota=" + $("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val();
			
			$.postJSON(contextPath + '/cadastro/fiador/adicionarAssociacaoCota', data, 
				function(result) {
					$(".fiadorController-cotasAssociadasGrid", fiadorController.workspace).flexAddData({
						page: 1, total: 1, rows: result.rows
					});
					
					$("#fiadorController-cotasAssociadas-numeroCota", fiadorController.workspace).val("");
					$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val("");
				},
				null,
				true
			);
		},
		
		removerAssociacaoCota:function (referencia){
			
			$(".fiadorController-cotasAssociadas-dialog-excluir", fiadorController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON(contextPath +'/cadastro/fiador/removerAssociacaoCota', "referencia=" + referencia, 
							function(result) {
								$(".fiadorController-cotasAssociadasGrid", fiadorController.workspace).flexAddData({
									page: 1, total: 1, rows: result.rows
								});
								
								$("#fiadorController-cotasAssociadas-numeroCota", fiadorController.workspace).val("");
								$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val("");
							},
							null,
							true
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				},
				form: $(".fiadorController-cotasAssociadas-dialog-excluir").parents("form")
			});
			
			$(".fiadorController-cotasAssociadas-dialog-excluir", fiadorController.workspace).show();
		},
		
		limparCamposCotasAssociadas:function (){
			$("#fiadorController-cotasAssociadas-numeroCota", fiadorController.workspace).val("");
			$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val("");
		},
		
		buscarNomeCota:function (){
			
			var numeroCota = $("#fiadorController-cotasAssociadas-numeroCota", fiadorController.workspace).val();
			
			if (numeroCota.length > 0){
			
				$.postJSON(contextPath + '/cadastro/fiador/pesquisarNomeCotaPorNumeroCota', "numeroCota=" + numeroCota, 
					function(result) {
						if (result != ""){
							
							$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val(result);
							$("#fiadorController-cotasAssociadas-adicionarCotaAssociacao", fiadorController.workspace).attr("href", "javascript:fiadorController.adicionarAssociacaoCota();");
						} else {
							
							$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val("");
							$("#fiadorController-cotasAssociadas-adicionarCotaAssociacao", fiadorController.workspace).removeAttr("href");
						}
					},
					null,
					true
				);
			} else {
				$("#fiadorController-cotasAssociadas-nomeCota", fiadorController.workspace).val("");
			}
		}	
}, BaseController);
//@ sourceURL=fiador.js