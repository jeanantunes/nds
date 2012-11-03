
var PDV =  $.extend(true, {
	
		idCota:"",
        idHistorico:"",
        idPdv:"",
        fecharModalCadastroPDV:false,
		diasFuncionamento:[],
        modoTela: null,
		
		
		init : function() {
			
			PDV.inicializarBinds();
			PDV.inicializarGrids();
			PDV.inicializarAbaDadosBasicos();
			PDV.inicializarGeradorFluxo();
			PDV.inicializarMap();
            PDV.modoTela = ModoTela.CADASTRO_COTA;
		},

         //Define o modo da tela
         definirModoTela : function(modoTela) {
        	 PDV.modoTela = modoTela;
             ENDERECO_PDV.definirReadonly(PDV.isReadOnly());
             TELEFONE_PDV.definirReadonly(PDV.isReadOnly());
              if (PDV.isReadOnly()) {
                 $('#PDVbtnNovo', this.workspace).hide();
                 $('#PDVbtnAdicionarDiasFuncionamento', this.workspace).hide();
                 $('#idBtnExcluir', this.workspace).hide();
                 $('#PDVbtnUploadImagem', this.workspace).hide();
                 $("#dialog-pdv", this.workspace).find(':input:not(:disabled)').prop('disabled', true);
                 $("#textoLuminoso", this.workspace).prop("disabled", true);
                 PDV.fecharModalCadastroPDV = true;

             } else {
                 $('#PDVbtnNovo', this.workspace).show();
                 $('#PDVbtnAdicionarDiasFuncionamento', this.workspace).show();
                 $('#idBtnExcluir', this.workspace).show();
                 $('#PDVbtnUploadImagem', this.workspace).show();
                 $("#dialog-pdv", this.workspace).find(':input(:disabled)').prop('disabled', false);
                 $("#textoLuminoso", this.workspace).prop("disabled", false);
                 PDV.fecharModalCadastroPDV = false;
             }
         },
         
     	isModoTelaCadastroCota : function() {
    		return PDV.modoTela == ModoTela.CADASTRO_COTA;
    	},
    	
    	isReadOnly : function() {
    		return !PDV.isModoTelaCadastroCota();
    	},
		
		inicializarMap : function() {
			$("select[name='selectMap']", this.workspace).multiSelect("select[name='selectMaterialPromocional']", {trigger: "#linkMapVoltarTodos"});
			
			$("select[name='selectMaterialPromocional']", this.workspace).multiSelect("select[name='selectMap']", {trigger: "#linkMapEnviarTodos"});
		},
		
		inicializarGeradorFluxo : function() {
			$("select[name='selectFluxoSecundario']", this.workspace).multiSelect("select[name='selecTipoGeradorFluxo']", {trigger: "#linkFluxoVoltarTodos"});
			
			$("select[name='selecTipoGeradorFluxo']", this.workspace).multiSelect("select[name='selectFluxoSecundario']", {trigger: "#linkFluxoEnviarTodos"});
		},
		
		inicializarAbaDadosBasicos : function() {

			$('#inicioHorario', this.workspace).mask('99:99'); 
			$('#fimHorario', this.workspace).mask('99:99'); 
			$("#porcentagemFaturamento", this.workspace).numeric();
			$("#qntFuncionarios", this.workspace).numeric();
			$("#numerolicenca", this.workspace).numeric();	
			
			var options = {
					success: PDV.tratarRetornoUploadImagem
				};
				
				$('#formBaixaAutomatica', this.workspace).ajaxForm(options);	
		},
		
		pesquisarPdvs: function (){
			
			var param = [{name:"idCota", value: PDV.idCota},
                {name:"idHistorico", value: PDV.idHistorico},
                {name:"modoTela", value: PDV.modoTela.value}];
			
			$(".PDVsGrid", this.workspace).flexOptions({
				url: contextPath + "/cadastro/pdv/consultar",
				params: param
			});
			
			$(".PDVsGrid", this.workspace).flexReload();
		},
		
		executarPreProcessamento : function (resultado){
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				var param = '\'' + row.cell.idPdv +'\','+'\''+ row.cell.idCota +'\'';

                var title = PDV.isReadOnly() ? 'Visualizar PDV' : 'Editar Endereço';

				var linkEdicao = '<a href="javascript:;" onclick="PDV.editarPDV('+ param +');" style="cursor:pointer; margin-right:10px;">' +
					 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="'+title+'" />' +
					 '</a>';

                var linkExclusao = '';
                if (!PDV.isReadOnly()) {
                    linkExclusao ='<a href="javascript:;" onclick="PDV.exibirDialogExclusao('+param +' );" style="cursor:pointer">' +
                        '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir PDV" />' +
                        '</a>';
                }

				
				var colunaprincipal = "";
				
				if(row.cell.isPrincipal){
					 colunaprincipal = '<img src="'+ contextPath +'/images/ico_check.gif" hspace="5" border="0px" title="Pdv Principal" />';	
				}
				
				 row.cell.principal = colunaprincipal;
				
                 row.cell.acao = linkEdicao + linkExclusao; 
			});
				
			return resultado;
		},
		
		carregarAbaDadosBasico: function (result){

            if (PDV.isModoTelaCadastroCota()) {
                if(result.pdvDTO.pathImagem) {
                    $("#idImagem", this.workspace).attr("src",contextPath + "/" + result.pdvDTO.pathImagem);
                } else {
                    $("#idImagem", this.workspace).attr("src",contextPath + "/images/pdv/no_image.jpeg");
                }
            } else {
                if (result.pdvDTO.possuiImagem) {
                	$("#idImagem", this.workspace).attr("src",contextPath + "/cadastro/pdv/imagemPdvHistoricoTitularidade?idPdv=" + PDV.idPdv);
                } else {
                	$("#idImagem", this.workspace).attr("src",contextPath + "/images/pdv/no_image.jpeg");
                }
            }

			$("#idBtnExcluir", this.workspace).click(function() {
				PDV.excluirImagem(result.pdvDTO.id);
			});
			
			$("#idPDV", this.workspace).val(result.pdvDTO.id);
			$("#idCotaImagem", this.workspace).val(result.pdvDTO.idCota);
			$("#selectStatus", this.workspace).val(result.pdvDTO.statusPDV);
			if(result.pdvDTO.dataInicio)
				$("#dataInicio", this.workspace).val(result.pdvDTO.dataInicio.$.substr(0,10));
			$("#nomePDV", this.workspace).val(result.pdvDTO.nomePDV);
			$("#contatoPDV", this.workspace).val(result.pdvDTO.contato);
			$("#sitePDV", this.workspace).val(result.pdvDTO.site);
			$("#emailPDV", this.workspace).val(result.pdvDTO.email);
			$("#pontoReferenciaPDV", this.workspace).val(result.pdvDTO.pontoReferencia);
			if (PDV.isModoTelaCadastroCota()) {
                PDV.carregarTiposEstabelecimento(result.pdvDTO.tipoEstabelecimentoAssociacaoPDV.codigo);
                PDV.carregarTiposLicencaMunicipal(result.pdvDTO.tipoLicencaMunicipal.codigo);
            }else {
                if (result.pdvDTO.tipoEstabelecimentoAssociacaoPDV.codigo) {
                    montarComboBoxUnicaOpcao(result.pdvDTO.tipoEstabelecimentoAssociacaoPDV.codigo,
                        result.pdvDTO.tipoEstabelecimentoAssociacaoPDV.descricao,$("#selectTipoEstabelecimento", this.workspace));
                }

                if (result.pdvDTO.tipoLicencaMunicipal.codigo) {
                    montarComboBoxUnicaOpcao(result.pdvDTO.tipoLicencaMunicipal.codigo, result.pdvDTO.tipoLicencaMunicipal.descricao,
                        $("#selectTipoLicenca"));
                }
            }
            $("#selectTamanhoPDV", this.workspace).val(result.pdvDTO.tamanhoPDV);
            $("#qntFuncionarios", this.workspace).val(result.pdvDTO.qtdeFuncionarios);
            $("#sistemaIPV", this.workspace).attr("checked", result.pdvDTO.sistemaIPV ? "checked" : null);
            $("#porcentagemFaturamento", this.workspace).val(result.pdvDTO.porcentagemFaturamento);
            $("#numerolicenca", this.workspace).val(result.pdvDTO.numeroLicenca);
			$("#nomeLicenca", this.workspace).val(result.pdvDTO.nomeLicenca);
			$("#arrendatario", this.workspace).attr("checked", result.pdvDTO.arrendatario ? "checked" : null);
			$("#ptoPrincipal", this.workspace).attr("checked", result.pdvDTO.caracteristicaDTO.pontoPrincipal ? "checked" : null);
			$("#dentroOutroEstabelecimento", this.workspace).attr(
					"checked", result.pdvDTO.dentroOutroEstabelecimento ? "checked" : null);
			
			PDV.opcaoEstabelecimento("#dentroOutroEstabelecimento");
			
			if(result.pdvDTO.periodosFuncionamentoDTO.length > 0){
				
				result.pdvDTO.periodosFuncionamentoDTO.forEach( function(diaFuncionamento){
					
					PDV.diasFuncionamento.push({
						tipoPeriodo:diaFuncionamento.tipoPeriodoFuncionamentoPDV,
						descTipoPeriodo:diaFuncionamento.nomeTipoPeriodo,
						inicio:diaFuncionamento.inicio,
						fim:diaFuncionamento.fim});						
				});

				PDV.atualizarComboDiasFuncionamento();
				
				PDV.montartabelaDiasFuncionamento();								
			}
			else{
				
				PDV.carregarPeriodosFuncionamento();
			}
	
		},
		
		carregarAbaCaracteristica: function (result){
			
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.balcaoCentral,"#balcaoCentral");
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.temComputador,"#temComputador");
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.luminoso,"#luminoso");
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.possuiCartao,"#possuiCartao");
			
			$("#textoLuminoso", this.workspace).val(result.pdvDTO.caracteristicaDTO.textoLuminoso);

            if (PDV.isModoTelaCadastroCota()) {
                PDV.carregarTiposPontoPdv(result.pdvDTO.tipoPontoPDV.codigo);
                PDV.carregarCaracteristicasPdv(result.pdvDTO.caracteristicaDTO.tipoCaracteristicaSegmentacaoPDV);
                PDV.carregarAreasInfluenciaPdv(result.pdvDTO.caracteristicaDTO.areaInfluencia);
            } else {
                if (result.pdvDTO.tipoPontoPDV.codigo) {
                    montarComboBoxUnicaOpcao(result.pdvDTO.tipoPontoPDV.codigo, result.pdvDTO.tipoPontoPDV.descricao,
                        $("#selectdTipoPonto", this.workspace));
                }
                if (result.pdvDTO.caracteristicaDTO.tipoCaracteristicaSegmentacaoPDV) {
                    montarComboBoxUnicaOpcao(result.pdvDTO.caracteristicaDTO.tipoCaracteristicaSegmentacaoPDV,
                        result.pdvDTO.caracteristicaDTO.descricaoTipoCaracteristica, $("#selectCaracteristica", this.workspace));
               }
               if (result.pdvDTO.caracteristicaDTO.areaInfluencia) {
                   montarComboBoxUnicaOpcao(result.pdvDTO.caracteristicaDTO.areaInfluencia, result.pdvDTO.caracteristicaDTO.descricaoAreaInfluencia,
                       $("#selectAreainfluencia", this.workspace));
               }
            }

	        if(this.isChecked("#luminoso") && !PDV.isReadOnly()){
				$("#textoLuminoso", this.workspace).prop("disabled", false);
			}
	        else{
	        	$("#textoLuminoso", this.workspace).prop("disabled", true);
	        }
	        
		},
		
		atribuirValorChecked:function(valor,idChecked){
			
			var cheked = (valor == true)?"checked":null;
			
			$(idChecked).attr("checked",cheked);
		},
				
		carregarAbaGeradorFluxo: function (result){
			
			var parametros = [];
			
			$.each(result.pdvDTO.geradorFluxoSecundario, function(index, valor) {
				
				parametros.push({name:'codigos['+ index +']', value: valor});
		  	});
			
			if(parametros.length >  0){
				
				PDV.carregarGeradorFluxoSecundario(parametros);
				
				parametros.push({name:'codigos['+ (parametros.length) +']', value:result.pdvDTO.geradorFluxoPrincipal});
				PDV.carregarGeradorFluxoNotIn(parametros);
				
				var parametro = [ {name:"codigos",value:result.pdvDTO.geradorFluxoPrincipal}, 
				                  {name:"modoTela", value: PDV.modoTela.value}, 
				                  {name: "idPdv", value: PDV.idPdv}];
				$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxo",
						parametro, 
						   function(result){
					$("#txtGeradorFluxoPrincipal", this.workspace).val(result[0].value.$);
					$("#hiddenGeradorFluxoPrincipal", this.workspace).val(result[0].key.$);	
				});
			}
			else{
				PDV.carregarGeradorFluxo(null);
			}
		},
		
		carregarAbaMap: function (result){
			
			var parametros = [];
			
			$.each(result.pdvDTO.maps, function(index, valor) {
				
				parametros.push({name:'codigos['+ index +']', value: valor});
		  	});
			
			if(parametros.length > 0){
				
				PDV.carregarMaterialPromocionalSelecionado(parametros);
				PDV.carregarMaterialPromocionalNotIn(parametros);
			}
			else{
				PDV.carregarMaterialPromocional(null);
			}
			
			PDV.atribuirValorChecked(result.pdvDTO.expositor, "#expositor");
			$("#tipoExpositor", this.workspace).val(result.pdvDTO.tipoExpositor); 
			
			PDV.mostra_expositor("#expositor");
		},
		
		editarPDV:function (idPdv,idCota){
			PDV.idPdv = idPdv;
			PDV.limparCamposTela();
			
			$.postJSON(contextPath + "/cadastro/pdv/editar",
					[{name:"idPdv", value:idPdv},
					 {name:"idCota", value:idCota},
					 {name:"modoTela", value: PDV.modoTela.value}], function(result){
				PDV.fecharModalCadastroPDV = false;
				PDV.carregarDadosEdicao(result);
				
			},null,true); 
		},
		
		excluirPDV:function(idPdv,idCota){

			$.postJSON(contextPath + "/cadastro/pdv/excluir",
					[{name:"idPdv",value:idPdv},
					 {name:"idCota",value:idCota}
					 ], function(result){
				
				PDV.pesquisarPdvs();
				
			},null,true);
		},
		
		carregarDadosEdicao:function (result){
			
			PDV.popup_novoPdv();
			PDV.carregarAbaDadosBasico(result);
			PDV.carregarAbaCaracteristica(result);
			PDV.carregarAbaGeradorFluxo(result);
			PDV.carregarAbaMap(result);
		},
		
		salvarPDV : function(){
	
			$.postJSON(contextPath + "/cadastro/pdv/salvar",
					this.getDadosBasico(), function(result){
			
				if(result.listaMensagens){
					
					if(result.tipoMensagem && result.tipoMensagem== "ERROR" ){
						
						exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalPDV");
						
						ENDERECO_PDV.popularGridEnderecos();
						
						return;
					}
				}
			
				$("#dialog-pdv", this.workspace).dialog( "close" );
				PDV.fecharModalCadastroPDV = true;
				PDV.pesquisarPdvs();
				PDV.limparCamposTela();	
				
				if(result.listaMensagens){
					
					exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
				}
				
			},null,true,"idModalPDV");
			
		},

		getDadosBasico: function (){
			
			var dados = {"pdvDTO.idCota":PDV.idCota , 
					"pdvDTO.id":$("#idPDV", this.workspace).val() , 
					"pdvDTO.statusPDV":$("#selectStatus", this.workspace).val() , 
					"pdvDTO.dataInicio":$("#dataInicio", this.workspace).val(), 
					"pdvDTO.nomePDV":$("#nomePDV", this.workspace).val(), 
					"pdvDTO.contato":$("#contatoPDV", this.workspace).val(), 
					"pdvDTO.site":$("#sitePDV", this.workspace).val(), 
					"pdvDTO.email":$("#emailPDV", this.workspace).val(), 
					"pdvDTO.pontoReferencia":$("#pontoReferenciaPDV", this.workspace).val(), 
					"pdvDTO.dentroOutroEstabelecimento":this.isChecked("#dentroOutroEstabelecimento", this.workspace) , 
					"pdvDTO.arrendatario":this.isChecked("#arrendatario", this.workspace) , 
					"pdvDTO.caracteristicaDTO.pontoPrincipal":this.isChecked("#ptoPrincipal", this.workspace),
					"pdvDTO.tipoEstabelecimentoAssociacaoPDV.codigo":$("#selectTipoEstabelecimento", this.workspace).val(), 
					"pdvDTO.tamanhoPDV":$("#selectTamanhoPDV", this.workspace).val(), 
					"pdvDTO.qtdeFuncionarios":$("#qntFuncionarios", this.workspace).val(), 
					"pdvDTO.sistemaIPV":this.isChecked("#sistemaIPV", this.workspace), 
					"pdvDTO.porcentagemFaturamento":$("#porcentagemFaturamento", this.workspace).val(), 
					"pdvDTO.tipoLicencaMunicipal.id":$("#selectTipoLicenca", this.workspace).val(), 
					"pdvDTO.numeroLicenca":$("#numerolicenca", this.workspace).val(), 
					"pdvDTO.nomeLicenca":$("#nomeLicenca", this.workspace).val(),
					"pdvDTO.caracteristicaDTO.balcaoCentral":this.isChecked("#balcaoCentral"),
					"pdvDTO.caracteristicaDTO.temComputador":this.isChecked("#temComputador"),
					"pdvDTO.caracteristicaDTO.possuiCartao":this.isChecked("#possuiCartao"),
					"pdvDTO.caracteristicaDTO.luminoso":this.isChecked("#luminoso"),
					"pdvDTO.caracteristicaDTO.textoLuminoso":$("#textoLuminoso",this.workspace).val(),
					"pdvDTO.caracteristicaDTO.tipoPonto":$("#selectdTipoPonto",this.workspace).val(),
					"pdvDTO.caracteristicaDTO.tipoCaracteristicaSegmentacaoPDV":$("#selectCaracteristica",this.workspace).val(),
					"pdvDTO.caracteristicaDTO.areaInfluencia":$("#selectAreainfluencia",this.workspace).val(),
					"pdvDTO.geradorFluxoPrincipal":$("#hiddenGeradorFluxoPrincipal", this.workspace).val(),
					"pdvDTO.expositor":PDV.isChecked("#expositor"), 
					"pdvDTO.tipoExpositor":$("#tipoExpositor", this.workspace).val()};
			
			dados = serializeArrayToPost('pdvDTO.periodosFuncionamentoDTO', PDV.diasFuncionamento, dados);	
			
			 var listaFluxoSecundario = new Array();				
			 $("#selectFluxoSecundario option", this.workspace).each(function (index) {
				 listaFluxoSecundario.push($(this).val());
			 });
			 
			 dados =  serializeArrayToPost('pdvDTO.geradorFluxoSecundario', listaFluxoSecundario, dados);
			 
			 var listaMaps = new Array();
				
			 $("#selectMap option", this.workspace).each(function (index) {
				 listaMaps.push($(this).val());
				 
            });
			 
			 dados = serializeArrayToPost('pdvDTO.maps', listaMaps, dados);
			 
			return dados;
		},
		
		isChecked : function (idCampo){	
			return ($(idCampo, this.workspace).attr("checked") == "checked");
		},
		
		opcaoEstabelecimento: function (idCampo){
			
			if(this.isChecked(idCampo)){
				$("#divTipoEstabelecimento", this.workspace).show();
			}
			else{
				$("#divTipoEstabelecimento", this.workspace).hide();
			}
		},
		
		opcaoTextoLuminoso: function (idCampo){
			
			if(this.isChecked(idCampo)){
				$("#textoLuminoso", this.workspace).removeAttr("disabled");
				$("#textoLuminoso", this.workspace).val("");
				$("#textoLuminoso", this.workspace).focus();

			}
			else{
				$("#textoLuminoso", this.workspace).attr("disabled","disabled");
				$("#textoLuminoso", this.workspace).val("");
			}
		},
		
		adicionarDiaFuncionamento: function() {
			
			var tipoPeriodo = $("#selectDiasFuncionamento", this.workspace).val();
			var inicioHorario = $("#inicioHorario", this.workspace).val();
			var fimHorario = $("#fimHorario", this.workspace).val();		
						
			var parametros = PDV.getDiasFuncionamentoPDV();
						
			parametros.push({name:'novoPeriodo.tipoPeriodo', value: tipoPeriodo});
			parametros.push({name:'novoPeriodo.inicio', value: inicioHorario});
			parametros.push({name:'novoPeriodo.fim', value: fimHorario});			
			
			$.postJSON(contextPath + "/cadastro/pdv/adicionarPeriodo",
					parametros, 
					function(result) {PDV.retornoAdicaoDiaFuncionamento(result,true);},
					null,
					true,
					"idModalPDV"
			);
		},
		
		atualizarComboDiasFuncionamento : function() {
			
			var parametros = PDV.getDiasFuncionamentoPDV();
			
			$.postJSON(contextPath + "/cadastro/pdv/atualizarComboDiasFuncionamento",
					parametros, 
					function(result) {PDV.retornoAdicaoDiaFuncionamento(result,false);},
					null,
					true,
					"idModalPDV"
			);
		},
		
		getDiasFuncionamentoPDV:function() {
			
			var parametros = [];
			
			$.each(PDV.diasFuncionamento, function(index, diaFuncionamento) {
				
				parametros.push({name:'periodos['+ index +'].tipoPeriodo', value: diaFuncionamento.tipoPeriodo});
				parametros.push({name:'periodos['+ index +'].inicio', value: diaFuncionamento.inicio});
				parametros.push({name:'periodos['+ index +'].fim', value: diaFuncionamento.fim});
		  	});
			
			return parametros;			
		},
		
		retornoAdicaoDiaFuncionamento: 	function(result,hasAdicao){
			
			var items = result[0];
			var mensagens = result[1];
			var status = result[2];
	
			if(status == "SUCCESS") {
				
				if(hasAdicao) {
					
					var tipoPeriodo = $("#selectDiasFuncionamento").val();
					var descTipoPeriodo = $("#selectDiasFuncionamento option:selected").text();
					var inicioHorario = $("#inicioHorario").val();
					var fimHorario = $("#fimHorario").val();
					
					var novoPeriodo = {tipoPeriodo:tipoPeriodo,descTipoPeriodo:descTipoPeriodo,inicio:inicioHorario,fim:fimHorario};
					
					PDV.diasFuncionamento.push(novoPeriodo);
				}	
				
				PDV.montartabelaDiasFuncionamento();
			
				items.splice(0,0,{"key": {"@class": "string","$": "-1"},"value": {"@class": "string","$": "Selecione"}});
				
				var combo =  montarComboBox(items, false);
				$("#selectDiasFuncionamento", this.workspace).html(combo);
				
			} else {
				
				if(mensagens!=null && mensagens.length!=0) {
					exibirMensagem(status,mensagens);
				}
			}
		},
		
		montartabelaDiasFuncionamento: function(){
				 
			 $('#listaDiasFuncionais tr', this.workspace).remove();;
			 
			
			$.each(PDV.diasFuncionamento, function(index, row) {
					
				 var inputHidden = "<input type='hidden' value='"+row.tipoPeriodo +"' name='diasFuncionamento'/>";
					
				 var tr = $('<tr class="class_linha_1"></tr>');
				
				 tr.append("<td width='138'>&nbsp; "+ inputHidden+"</td>");
				 tr.append("<td width='249' class='diasFunc'>"+ row.descTipoPeriodo +"</td>");
				 tr.append("<td width='47'>&nbsp;</td>");
				
				 var as = (row.descTipoPeriodo == '24 horas' ? '':' as ');
				 tr.append("<td width='100'>"+ row.inicio + as + row.fim +"</td>");
								 
				 if (PDV.isModoTelaCadastroCota()) {
					 tr.append("<td width='227'>"+
							 "<a onclick='PDV.removerDiasFuncionamento($(this).parent().parent(),"+index+");'" +
							 " href='javascript:;'><img src='"+contextPath+"/images/ico_excluir.gif' alt='Excluir'" +
					 "width='15' height='15' border='0'/></a></td>");
				 } else {
                     tr.append("<td width='227'/>");
                 }
				 
			      
				 $('#listaDiasFuncionais', this.workspace).append(tr);
			});
		},
		
		removerDiasFuncionamento: function (linha,indice){
			
			PDV.diasFuncionamento.splice(indice,1);
			
			var parametros = [];
						
			$.each(PDV.diasFuncionamento, function(index, diaFuncionamento) {
				
				parametros.push({name:'periodos['+ index +'].tipoPeriodo', value: diaFuncionamento.tipoPeriodo});
				parametros.push({name:'periodos['+ index +'].inicio', value: diaFuncionamento.inicio});
				parametros.push({name:'periodos['+ index +'].fim', value: diaFuncionamento.fim});
		  	});
			
			$.postJSON(contextPath + "/cadastro/pdv/obterPeriodosPossiveis",
					parametros, 
					function(result) {PDV.retornoObterPeriodosPossiveis(result);
					linha.remove();}
			);
						
			
		},
		
		retornoObterPeriodosPossiveis: function(result) {
			
			var items = result[0];
			var mensagens = result[1];
			var status = result[2];
			
			if(mensagens!=null && mensagens.length!=0) {
				exibirMensagem(status,mensagens);
			}
			
			var combo = $("#selectDiasFuncionamento", this.workspace);
			combo.clear();
			
			var option = document.createElement("OPTION");
			option.innerHTML = "Selecione";
			option.value = "-1";
					
			combo.append(option);
			
			$.each(items, function(index, item) {
				var option = document.createElement("OPTION");
				option.innerHTML = item.value.$;
				option.value = item.key.$;
						
				combo.append(option);
		  	});
			
		},
		
		exibirDialogExclusao:function(idPdv,idCota){
			
			$("#dialog-excluirPdv", this.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:250,
				modal: true,
				buttons: {
					"Confirmar": function() {
						PDV.excluirPDV(idPdv, idCota);
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#workspaceCota", this.workspace)
			});
		},
		
		cancelarCadastro:function(){
			
			$("#dialog-cancelar-cadastro-pdv", this.workspace).dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON(contextPath +'/cadastro/pdv/cancelarCadastro', null, 
							
							function(result){
								
								PDV.fecharModalCadastroPDV = true;
								PDV.limparCamposTela();
								
								$("#dialog-close", this.workspace).dialog("close");
								$("#dialog-cancelar-cadastro-pdv", this.workspace).dialog("close");
								$("#dialog-pdv", this.workspace).dialog("close");
							}
						);
					},
					"Cancelar": function() {
						PDV.fecharModalCadastroPDV = false;
						$(this).dialog("close");
					}
				},
				form: $("#workspaceCota", this.workspace)
			});
		},
	
		
		poupNovoPDV:function(){
						
			PDV.limparCamposTela();
			PDV.carregarMaterialPromocional(null);
			PDV.carregarGeradorFluxo(null);
			PDV.carregarPeriodosFuncionamento();
			
			PDV.diasFuncionamento = [];
			
			$.postJSON(contextPath + "/cadastro/pdv/novo",
					{idCota:PDV.idCota}, 
					function(){
						PDV.popup_novoPdv();
						PDV.fecharModalCadastroPDV = false;
					}
			);
		},
		
		popup_novoPdv : function() {
			if (ModoTela.HISTORICO_TITULARIDADE == PDV.modoTela) {

                $( "#dialog-pdv", this.workspace ).dialog({
                    resizable: false,
                    height:600,
                    width:890,
                    modal: true,
                    buttons: {
                        "Fechar": function() {
                            PDV.fecharModalCadastroPDV = true;
                            $( this ).dialog( "close" );
                        }
                    },
                    form: $("#workspaceCota", this.workspace)
                });

            } else {
                $( "#dialog-pdv", this.workspace ).dialog({
                    resizable: false,
                    height:600,
                    width:890,
                    modal: true,
                    buttons: {
                        "Confirmar": function() {

                            if ($("#ptoPrincipal", this.workspace).attr("checked")){

                                data = {idCota:PDV.idCota,idPdv:$("#idPDV").val()};

                                $.postJSON(contextPath + "/cadastro/pdv/verificarPontoPrincipal",
                                    data,
                                    function(result){

                                        if (result){

                                            $("#dialog-confirmaPontoPrincipal", this.workspace).dialog({
                                                resizable : false,
                                                height : 200,
                                                width : 360,
                                                modal : true,
                                                buttons : {
                                                    "Confirmar" : function() {

                                                        $("#dialog-confirmaPontoPrincipal", this.workspace).dialog("close");
                                                        PDV.fecharModalCadastroPDV = true;
                                                        PDV.salvarPDV();
                                                    },
                                                    "Cancelar" : function() {

                                                        $("#dialog-confirmaPontoPrincipal", this.workspace).dialog("close");
                                                        PDV.fecharModalCadastroPDV = true;
                                                    }
                                                },
                                                form: $("#workspaceCota", this.workspace)
                                            });
                                        } else {
                                            $("#dialog-confirmaPontoPrincipal", this.workspace).dialog("close");
                                            PDV.fecharModalCadastroPDV = true;
                                            PDV.salvarPDV();
                                        }
                                    },
                                    null,
                                    true,
                                    "idModalPDV"
                                );
                            } else {

                                PDV.fecharModalCadastroPDV = true;
                                PDV.salvarPDV();
                            }
                        },
                        "Cancelar": function() {
                            PDV.fecharModalCadastroPDV = false;
                            $( this ).dialog( "close" );
                        }
                    },
                    beforeClose: function(event, ui) {

                        clearMessageDialogTimeout("idModalPDV");
                        clearMessageDialogTimeout();

                        if (!PDV.fecharModalCadastroPDV){

                            PDV.cancelarCadastro();

                            return PDV.fecharModalCadastroPDV;
                        }

                        return PDV.fecharModalCadastroPDV;

                    },
                    form: $("#workspaceCota", this.workspace)
                });
            }
		},
		
		enviarFluxoPrincipal: function (){
			if (PDV.isReadOnly()) {
                return;
            }

			var txtGeradorFluxoPrincipal ="";
			var hiddenGeradorFluxoPrincipal=""; 
			
			if($("#txtGeradorFluxoPrincipal", this.workspace).val().length == 0){
				
				txtGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected", this.workspace).text();
				hiddenGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected", this.workspace).val();
				$("#selecTipoGeradorFluxo option:selected", this.workspace).remove();
			}
			else{
				
				txtGeradorFluxoPrincipal = $("#txtGeradorFluxoPrincipal", this.workspace).val();
				hiddenGeradorFluxoPrincipal=$("#hiddenGeradorFluxoPrincipal", this.workspace).val(); 
				var option = "<option value='" + hiddenGeradorFluxoPrincipal + "'>" + txtGeradorFluxoPrincipal + "</option>";
				
				$("#selecTipoGeradorFluxo").append(option);
				
				txtGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected", this.workspace).text();
				hiddenGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected", this.workspace).val();
				
				$("#selecTipoGeradorFluxo option:selected", this.workspace).remove();
			}
			
			$("#txtGeradorFluxoPrincipal", this.workspace).val(txtGeradorFluxoPrincipal);
			$("#hiddenGeradorFluxoPrincipal", this.workspace).val(hiddenGeradorFluxoPrincipal);
			
			$("#selecTipoGeradorFluxo", this.workspace).sortOptions();

		}, 
		popup_img:function () {
			
			$( "#dialog-img", this.workspace ).dialog({
				resizable: false,
				height:'auto',
				width:450,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$('#formBaixaAutomatica').submit();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {	
					clearMessageDialogTimeout();
				},
				form: $("#workspaceCota", this.workspace)
			});

		},
		
		excluirImagem :function (idPDV) {
			
			$.postJSON(contextPath + "/cadastro/pdv/excluirImagem",
					   {idPdv:idPDV}, 
					   function(result){
				
				var mensagens = result[0];
				var status = result[1];
				
				$("#idImagem", this.workspace).attr("src", contextPath + "/images/pdv/no_image.jpeg");
							
				if(mensagens!=null && mensagens.length!=0) {
					exibirMensagem(status,mensagens);
				}
			});

		},
		validarEmail : function (email)	{
			er = /^[a-zA-Z0-9][a-zA-Z0-9\._-]+@([a-zA-Z0-9\._-]+\.)[a-zA-Z-0-9]{2}/;
			if(!er.exec(email)) {
				exibirMensagemDialog("WARNING",["N&atildeo &eacute um email v&aacutelido."],'idModalPDV');	
				$("#emailPDV", this.workspace).focus();
			}
		},
		carregarPeriodosFuncionamento:function(){
			$.postJSON(contextPath + "/cadastro/pdv/carregarPeriodoFuncionamento",
					   null, 
					   function(result){
							
							result.splice(0,0,{"key": {"@class": "string","$": "-1"},"value": {"@class": "string","$": "Selecione"}});
							result.splice(6,0,{"key": {"@class": "string","$": "-1"},"value": {"@class": "string","$": "-------------------------"}});
							
							var combo =  montarComboBox(result, false);
							$("#selectDiasFuncionamento", this.workspace).html(combo);
			},null,true,"idModalPDV");
		},
		
		carregarMaterialPromocional:function(data){
            if (!data) {
                data = [];
            }
            data.push({name: 'modoTela', value: PDV.modoTela.value});
            data.push({name: 'idPdv', value: PDV.idPdv});
            $.postJSON(contextPath + "/cadastro/pdv/carregarMaterialPromocional",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectMaterialPromocional", this.workspace).html(combo);
							$("#selectMaterialPromocional", this.workspace).sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarMaterialPromocionalNotIn:function(data){
            if (!data) {
                data = [];
            }
            data.push({name: 'modoTela', value: PDV.modoTela.value});
            $.postJSON(contextPath + "/cadastro/pdv/carregarMaterialPromocionalNotIn",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectMaterialPromocional", this.workspace).html(combo);
							$("#selectMaterialPromocional", this.workspace).sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarMaterialPromocionalSelecionado:function(data){
			if (!data) {
                data = [];
            }
            data.push({name: 'modoTela', value: PDV.modoTela.value});
            data.push({name: 'idPdv', value: PDV.idPdv});
            $.postJSON(contextPath + "/cadastro/pdv/carregarMaterialPromocional",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectMap", this.workspace).html(combo);
							$("#selectMap", this.workspace).sortOptions();
			},null,true,"idModalPDV");
		},

        carregarTiposEstabelecimento:function(selected){
            carregarCombo(contextPath + "/cadastro/pdv/carregarTiposEstabelecimento", null,
                $("#selectTipoEstabelecimento", this.workspace), selected, "idModalPDV" );
        },

        carregarTiposLicencaMunicipal:function(selected){
            carregarCombo(contextPath + "/cadastro/pdv/carregarTiposLicencaMunicipal", null,
                $("#selectTipoLicenca", this.workspace), selected, "idModalPDV" );
        },

        carregarTiposPontoPdv:function(selected){
            carregarCombo(contextPath + "/cadastro/pdv/carregarTiposPontoPdv", null,
                $("#selectdTipoPonto", this.workspace), selected, "idModalPDV" );
        },

        carregarCaracteristicasPdv:function(selected){
            carregarCombo(contextPath + "/cadastro/pdv/carregarCaracteristicasPdv", null,
                $("#selectCaracteristica", this.workspace), selected, "idModalPDV" );
        },

        carregarAreasInfluenciaPdv:function(selected){
            carregarCombo(contextPath + "/cadastro/pdv/carregarAreasInfluenciaPdv", null,
                $("#selectAreainfluencia", this.workspace), selected, "idModalPDV" );
        },
		
		carregarGeradorFluxoNotIn:function(data){
			if (!data) {
                data = [];
            }
            data.push({name: 'modoTela', value: PDV.modoTela.value});
			$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxoNotIn",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selecTipoGeradorFluxo", this.workspace).html(combo);
							$("#selecTipoGeradorFluxo", this.workspace).sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarGeradorFluxo:function(data){
			if (!data) {
                data = [];
            }
            data.push({name: 'idPdv', value: PDV.idPdv});
			data.push({name: 'modoTela', value: PDV.modoTela.value});
			$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxo",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selecTipoGeradorFluxo", this.workspace).html(combo);
							$("#selecTipoGeradorFluxo", this.workspace).sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarGeradorFluxoSecundario:function(data){
            if (!data) {
                data = [];
            }
            data.push({name: 'idPdv', value: PDV.idPdv});
            data.push({name: 'modoTela', value: PDV.modoTela.value});
			$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxo",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectFluxoSecundario", this.workspace).html(combo);
							$("#selectFluxoSecundario", this.workspace).sortOptions();
			},null,true,"idModalPDV");
		},
		
		mostra_expositor:function(idChecked){
			
			if(PDV.isChecked(idChecked)){
				$(".tipoExpositor", this.workspace ).show();
			}
			else{
				$(".tipoExpositor", this.workspace ).hide();
				$("#tipoExpositor", this.workspace).val("");
			}
		},

		limparCamposTela:function(){
			
			$("#idImagem", this.workspace).attr("src",  contextPath + "/images/pdv/no_image.jpeg");
			$("#selectStatus", this.workspace).val(""); 
			$("#nomePDV", this.workspace).val("");
			$("#dataInicio", this.workspace).val( $('#dataAtual',this.workspace).val() );
			$("#contatoPDV", this.workspace).val("");
			$("#sitePDV", this.workspace).val("");
			$("#emailPDV", this.workspace).val("");
			$("#pontoReferenciaPDV", this.workspace).val("");
			$("#dentroOutroEstabelecimento", this.workspace).attr("checked",null);
			$("#arrendatario", this.workspace).attr("checked",null);
			$("#selectTipoEstabelecimento", this.workspace).val("");
			$("#selectTamanhoPDV", this.workspace).val("");
			$("#qntFuncionarios", this.workspace).val("");
			$("#sistemaIPV", this.workspace).attr("checked",null);
			$("#porcentagemFaturamento", this.workspace).val("");
			$("#selectTipoLicenca", this.workspace).val("");
			$("#numerolicenca", this.workspace).val("");
			$("#nomeLicenca", this.workspace).val("");
			$("#selectDiasFuncionamento", this.workspace).val("");
			$("#inicioHorario", this.workspace).val("");
			$("#fimHorario", this.workspace).val("");
			$("#divTipoEstabelecimento", this.workspace).hide();
			
			PDV.diasFuncionamento = [];
			
			PDV.montartabelaDiasFuncionamento();
			
			$("#ptoPrincipal", this.workspace).attr("checked",null);
            $("#balcaoCentral", this.workspace).attr("checked",null);
            $("#temComputador", this.workspace).attr("checked",null);
            $("#possuiCartao", this.workspace).attr("checked",null);
                        
            $("#luminoso", this.workspace).attr("checked",null);
            $("#textoLuminoso", this.workspace).val("");
            $("#selectdTipoPonto", this.workspace).val("");
            $("#selectCaracteristica", this.workspace).val("");
            $("#selectAreainfluencia", this.workspace).val("");
                     
            $("#selectFluxoSecundario option", this.workspace).remove();
            
            $("#selectMap option", this.workspace).remove();
            
            $("#hiddenGeradorFluxoPrincipal", this.workspace).val("");
            $("#txtGeradorFluxoPrincipal", this.workspace).val("");
            
            $("#idPDV", this.workspace).val("");
            
            $("#tabpdv", this.workspace).tabs('select', 0);
		},
		
		selecionarDiaFuncionamento: function() {
			
			if($("#selectDiasFuncionamento", this.workspace).val()=='VINTE_QUATRO_HORAS') {
				$("#inicioHorario", this.workspace).attr('disabled', 'desabled');
				$("#fimHorario", this.workspace).attr('disabled', 'desabled');
				
			} else {
				$("#inicioHorario", this.workspace).attr('disabled', null);
				$("#fimHorario", this.workspace).attr('disabled', null);
			}
		},
		
		inicializarBinds : function () {
			
			$("#btnSalvarPDV", this.workspace).keypress(function() {
				
				var keynum = 0;
			      
			    if(window.event) {

			        keynum = event.keyCode;
			    
			    } else if(event.which) {   

			    	keynum = event.which;
			    }

				if (keynum == 13) {
					
					if ($("#ptoPrincipal", this.workspace).attr("checked")){
						
						data = {idCota:PDV.idCota,idPdv:$("#idPDV").val()};
						
						$.postJSON(contextPath + "/cadastro/pdv/verificarPontoPrincipal", 
								data, 
								function(result){
									
									if (result){
										
										$("#dialog-confirmaPontoPrincipal", this.workspace).dialog({
											resizable : false,
											height : 200,
											width : 360,
											modal : true,
											buttons : {
												"Confirmar" : function() {
													
													$("#dialog-confirmaPontoPrincipal", this.workspace).dialog("close");
													PDV.fecharModalCadastroPDV = true;
													PDV.salvarPDV();
												},
												"Cancelar" : function() {
													
													$("#dialog-confirmaPontoPrincipal", this.workspace).dialog("close");
													PDV.fecharModalCadastroPDV = true;
												}

											},
											form: $("#workspaceCota", this.workspace)
										});
									} else {
										$("#dialog-confirmaPontoPrincipal", this.workspace).dialog("close");
										PDV.fecharModalCadastroPDV = true;
										PDV.salvarPDV();
									}
								},
								null,
								true,
								"idModalPDV"
						);
					} else {
						
						PDV.fecharModalCadastroPDV = true;
						PDV.salvarPDV();
					}
				}
			});

		},
		
		inicializarGrids : function() {
			
			$(".PDVsGrid", this.workspace).flexigrid({
				preProcess: PDV.executarPreProcessamento,
				dataType : 'json',
				colModel : [  {
					display : 'Nome PDV',
					name : 'nomePdv',
					width : 100,
					sortable : true,
					align : 'left'
				},{
					display : 'Tipo de Ponto',
					name : 'tipoPonto',
					width : 100,
					sortable : true,
					align : 'left'
				},{
					display : 'Contato',
					name : 'contato',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Endereço',
					name : 'endereco',
					width : 170,
					sortable : true,
					align : 'left'
				}, {
					display : 'Principal',
					name : 'principal',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Status',
					name : 'status',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : '% Fat.',
					name : 'faturamento',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 55,
					sortable : false,
					align : 'center'
				}],
				width : 880,
				height : 150,
				sortname : "nomePdv",
				sortorder : "asc"
			});

		},		

		tratarRetornoUploadImagem : function(data) {
			
			data = replaceAll(data, "<pre>", "");
			data = replaceAll(data, "</pre>", "");
			
			data = replaceAll(data, "<PRE>", "");
			data = replaceAll(data, "</PRE>", "");
			
			var responseJson = jQuery.parseJSON(data);
			
			var mensagens = responseJson.result[0];
			var status = responseJson.result[1];
			var pathArquivo = responseJson.result[2];
				
			if(pathArquivo) {
				$("#idImagem", this.workspace).attr("src", contextPath + "/" + pathArquivo);
			} else {
				$("#idImagem", this.workspace).attr("src", contextPath +  "/images/pdv/no_image.jpeg");
			}	
			
			if(mensagens!=null && mensagens.length!=0) {
				exibirMensagemDialog(status,mensagens);
			}
		}
		
}, BaseController);

$(function() {
	PDV.init();				
});

//@ sourceURL=pdv.js