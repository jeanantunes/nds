
var PDV = {
		
		idCota:"",
		fecharModalCadastroPDV:false,
		diasFuncionamento:[],
		
		pesquisarPdvs: function (idCota){
			
			var param = [{name:"idCota",value:idCota}];
			
			$(".PDVsGrid").flexOptions({
				url: contextPath + "/cadastro/pdv/consultar",
				params: param
			});
			
			$(".PDVsGrid").flexReload();
		},
		
		executarPreProcessamento : function (resultado){

			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				
				var param = '\'' + row.cell.idPdv +'\','+'\''+ row.cell.idCota +'\'';
				
				var linkEdicao = '<a href="javascript:;" onclick="PDV.editarPDV('+ param +');" style="cursor:pointer">' +
					 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar PDV" />' +
					 '</a>';			
				 
				var linkExclusao ='<a href="javascript:;" onclick="PDV.exibirDialogExclusao('+param +' );" style="cursor:pointer">' +
	                 '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir PDV" />' +
	                  '</a>';		 					 
				
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
			
			if(result.pdvDTO.pathImagem) {
				$("#idImagem").attr("src",contextPath + "/" + result.pdvDTO.pathImagem);
			} else {
				$("#idImagem").attr("src",contextPath + "/images/pdv/no_image.jpeg");
			}
			
			$("#idBtnExcluir").click(function() {
				PDV.excluirImagem(result.pdvDTO.id);
			});
			
			$("#idPDV").val(result.pdvDTO.id);
			$("#idCotaImagem").val(result.pdvDTO.idCota);
			$("#selectStatus").val(result.pdvDTO.statusPDV);
			$("#dataInicio").val($.format.date(result.pdvDTO.dataInicio.$.substr(0,10) + "00:00:00.000","dd/MM/yyyy"));
			$("#nomePDV").val(result.pdvDTO.nomePDV);
			$("#contatoPDV").val(result.pdvDTO.contato);
			$("#sitePDV").val(result.pdvDTO.site);
			$("#emailPDV").val(result.pdvDTO.email);
			$("#pontoReferenciaPDV").val(result.pdvDTO.pontoReferencia);
			$("#selectTipoEstabelecimento").val(result.pdvDTO.tipoEstabelecimentoAssociacaoPDV.codigo);
			$("#selectTamanhoPDV").val(result.pdvDTO.tamanhoPDV);
			$("#qntFuncionarios").val(result.pdvDTO.qtdeFuncionarios);
			$("#sistemaIPV").attr("checked", result.pdvDTO.sistemaIPV ? "checked" : null);
			$("#porcentagemFaturamento").val(result.pdvDTO.porcentagemFaturamento);
			$("#selectTipoLicenca").val(result.pdvDTO.tipoLicencaMunicipal.codigo);
			$("#numerolicenca").val(result.pdvDTO.numeroLicenca);
			$("#nomeLicenca").val(result.pdvDTO.nomeLicenca);
			
			$("#dentroOutroEstabelecimento").attr(
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

				PDV.montartabelaDiasFuncionamento();
			}
	
		},
		
		carregarAbaEndereco: function (result){
			
		},
		
		carregarAbaTelefone: function (result){
			
		},
		
		carregarAbaCaracteristica: function (result){
			
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.pontoPrincipal,"#ptoPrincipal");
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.balcaoCentral,"#balcaoCentral");
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.temComputador,"#temComputador");
			PDV.atribuirValorChecked(result.pdvDTO.caracteristicaDTO.luminoso,"#luminoso");

			$("#textoLuminoso").val(result.pdvDTO.caracteristicaDTO.textoLuminoso);
	        $("#selectdTipoPonto").val(result.pdvDTO.caracteristicaDTO.tipoPonto);
	        $("#selectCaracteristica").val(result.pdvDTO.caracteristicaDTO.tipoCaracteristicaSegmentacaoPDV);
	        $("#selectAreainfluencia").val(result.pdvDTO.caracteristicaDTO.areaInfluencia);
	        $("#selectCluster").val(result.pdvDTO.caracteristicaDTO.cluster);
	        
	        if(this.isChecked("#luminoso")){
				$("#textoLuminoso").removeAttr("disabled");
			}
	        else{
	        	$("#textoLuminoso").attr("disabled","disabled");
	        }
	        
		},
		
		atribuirValorChecked:function(valor,idChecked){
			
			var cheked = (valor == true)?"checked":null;
			
			$(idChecked).attr("checked",cheked);
		},
		
		carregarAbaEspecialidade: function (result){
	
			var parametros = [];
			
			$.each(result.pdvDTO.especialidades, function(index, valor) {
				
				parametros.push({name:'codigos['+ index +']', value: valor});
		  	});
			
			if(parametros.length > 0){
				
				PDV.carregarEspecialidade(parametros);
				PDV.carregarEspecialidadesNotIn(parametros);
			}
			else{
				PDV.carregarCaracteristicaEspecialidade(null);
			}

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
				
				var parametro = [ {name:"codigos",value:result.pdvDTO.geradorFluxoPrincipal}];
				$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxo",
						parametro, 
						   function(result){
					$("#txtGeradorFluxoPrincipal").val(result[0].value.$);
					$("#hiddenGeradorFluxoPrincipal").val(result[0].key.$);	
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
			$("#tipoExpositor").val(result.pdvDTO.tipoExpositor); 
			
			PDV.mostra_expositor("#expositor");
		},
		
		editarPDV:function (idPdv,idCota,index){
			
			PDV.limparCamposTela();
			
			$.postJSON(contextPath + "/cadastro/pdv/editar",
					[{name:"idPdv",value:idPdv},
					 {name:"idCota",value:idCota}
					 ], function(result){
				
				PDV.carregarDadosEdicao(result);
				
			},PDV.errorEditarPDV,true); 
		},
		
		excluirPDV:function(idPdv,idCota){
			
			$.postJSON(contextPath + "/cadastro/pdv/excluir",
					[{name:"idPdv",value:idPdv},
					 {name:"idCota",value:idCota},
					 ], function(result){
				
				PDV.pesquisarPdvs(idCota);
				
			},PDV.errorExcluirPDV,true);
		},
		
		errorEditarPDV: function (){
			
		},
		
		errorExcluirPDV: function (){
			
		},
		carregarDadosEdicao:function (result){
			
			PDV.popup_novoPdv();
			PDV.carregarAbaDadosBasico(result);
			PDV.carregarAbaCaracteristica(result);
			PDV.carregarAbaEspecialidade(result);
			PDV.carregarAbaGeradorFluxo(result);
			PDV.carregarAbaMap(result);
		},
		
		salvarPDV : function(){
	
			$.postJSON(contextPath + "/cadastro/pdv/salvar",
					this.getDadosBasico() +"&" +
					this.getDadosCaracteristica() +"&" + 
					this.getDadosEspecialidade() +"&" + 
					this.getDadosGeradorFluxo()  +"&" +
					this.getDadosMap(), function(result){
				
				PDV.pesquisarPdvs(PDV.idCota);
				PDV.limparCamposTela();
				$("#dialog-pdv").dialog( "close" );
				
			},this.errorSalvarPDV,true,"idModalPDV");
			
		},
		
		errorSalvarPDV: function (result){
		},
		
		getDadosBasico: function (){
			
			var dados = 
				"pdvDTO.idCota="								+PDV.idCota + "&" +
				"pdvDTO.id="									+$("#idPDV").val() + "&" +
				"pdvDTO.statusPDV="  							+$("#selectStatus").val() + "&" +
				"pdvDTO.dataInicio="							+$("#dataInicio").val()+ "&" +
				"pdvDTO.nomePDV="								+$("#nomePDV").val()+ "&" +
				"pdvDTO.contato="								+$("#contatoPDV").val()+ "&" +
				"pdvDTO.site="									+$("#sitePDV").val()+ "&" +
				"pdvDTO.email="									+$("#emailPDV").val()+ "&" +
				"pdvDTO.pontoReferencia="						+$("#pontoReferenciaPDV").val()+ "&" +
				"pdvDTO.dentroOutroEstabelecimento="	 		+this.isChecked("#dentroOutroEstabelecimento") + "&" +
				"pdvDTO.tipoEstabelecimentoAssociacaoPDV.codigo="	+$("#selectTipoEstabelecimento").val()+ "&" +
				"pdvDTO.tamanhoPDV="							+$("#selectTamanhoPDV").val()+ "&" +
				"pdvDTO.qtdeFuncionarios="						+$("#qntFuncionarios").val()+ "&" +
				"pdvDTO.sistemaIPV="							+this.isChecked("#sistemaIPV")+ "&" +
				"pdvDTO.porcentagemFaturamento="				+$("#porcentagemFaturamento").val()+ "&" +
				"pdvDTO.tipoLicencaMunicipal.id="				+$("#selectTipoLicenca").val()+ "&" +
				"pdvDTO.numeroLicenca="							+$("#numerolicenca").val()+ "&" +
				"pdvDTO.nomeLicenca="							+$("#nomeLicenca").val();
			
			$.each(PDV.diasFuncionamento, function(index, diaFuncionamento) {
				
				dados += '&pdvDTO.periodosFuncionamentoDTO['+ index +'].tipoPeriodo=' + diaFuncionamento.tipoPeriodo +
				'&pdvDTO.periodosFuncionamentoDTO['+ index +'].inicio='+diaFuncionamento.inicio +
				'&pdvDTO.periodosFuncionamentoDTO['+ index +'].fim='+diaFuncionamento.fim;
		  	});
			
			return dados;
		},
		
				
		getDadosCaracteristica: function (){
			
			var dados =
	              "pdvDTO.caracteristicaDTO.pontoPrincipal=" + this.isChecked("#ptoPrincipal") +"&"+
	              "pdvDTO.caracteristicaDTO.balcaoCentral="  + this.isChecked("#balcaoCentral")+"&"+
	              "pdvDTO.caracteristicaDTO.temComputador="  + this.isChecked("#temComputador")+"&"+
	              "pdvDTO.caracteristicaDTO.luminoso="       + this.isChecked("#luminoso")+"&"+
	              "pdvDTO.caracteristicaDTO.textoLuminoso="  + $("#textoLuminoso").val()+"&"+
	              "pdvDTO.caracteristicaDTO.tipoPonto="      + $("#selectdTipoPonto").val()+"&"+
	              "pdvDTO.caracteristicaDTO.caracteristica=" + $("#selectCaracteristica").val()+"&"+
	              "pdvDTO.caracteristicaDTO.areaInfluencia=" + $("#selectAreainfluencia").val()+"&"+
	              "pdvDTO.caracteristicaDTO.cluster="        + $("#selectCluster").val();
						
			return dados;
		},

		getDadosEspecialidade: function (){
			
			var listaEspecialidades ="";
			
			 $("#especialidades_options option").each(function (index) {
				 listaEspecialidades = listaEspecialidades + "pdvDTO.especialidades["+index+"]="+ $(this).val() +"&";
             });
   
			return listaEspecialidades;
		},
		
		getDadosGeradorFluxo: function (){
			
			 var listaFluxoSecundario ="";
			
			 $("#selectFluxoSecundario option").each(function (index) {
				 listaFluxoSecundario = listaFluxoSecundario + "pdvDTO.geradorFluxoSecundario["+index+"]="+ $(this).val() +"&";
			 });
			 
			 listaFluxoSecundario = listaFluxoSecundario + "pdvDTO.geradorFluxoPrincipal="+ $("#hiddenGeradorFluxoPrincipal").val() +"&";
 
			 return listaFluxoSecundario;	
		},
		
		getDadosMap: function (){
			
			var listaMaps ="";
			
			 $("#selectMap option").each(function (index) {
				 listaMaps = listaMaps + "pdvDTO.maps["+index+"]="+ $(this).val() +"&";
            });
			 
			 listaMaps = listaMaps +  "pdvDTO.expositor="+PDV.isChecked("#expositor")+"&" 
			 					   +  "pdvDTO.tipoExpositor="+$("#tipoExpositor").val(); 
  
			return listaMaps;
		},
		
		isChecked : function (idCampo){	
			return ($(idCampo).attr("checked") == "checked");
		},
		
		opcaoEstabelecimento: function (idCampo){
			
			if(this.isChecked(idCampo)){
				$("#divTipoEstabelecimento").show();
			}
			else{
				$("#divTipoEstabelecimento").hide();
			}
		},
		
		opcaoTextoLuminoso: function (idCampo){
			
			if(this.isChecked(idCampo)){
				$("#textoLuminoso").removeAttr("disabled");
				$("#textoLuminoso").val("");
				$("#textoLuminoso").focus();

			}
			else{
				$("#textoLuminoso").attr("disabled","disabled");
				$("#textoLuminoso").val("");
			}
		},
		
		adicionarDiaFuncionamento: function() {
			
			var tipoPeriodo = $("#selectDiasFuncionamento").val();
			var inicioHorario = $("#inicioHorario").val();
			var fimHorario = $("#fimHorario").val();		
						
			var parametros = [];
			
			$.each(PDV.diasFuncionamento, function(index, diaFuncionamento) {
				
				parametros.push({name:'periodos['+ index +'].tipoPeriodo', value: diaFuncionamento.tipoPeriodo});
				parametros.push({name:'periodos['+ index +'].inicio', value: diaFuncionamento.inicio});
				parametros.push({name:'periodos['+ index +'].fim', value: diaFuncionamento.fim});
		  	});
						
			parametros.push({name:'novoPeriodo.tipoPeriodo', value: tipoPeriodo});
			parametros.push({name:'novoPeriodo.inicio', value: inicioHorario});
			parametros.push({name:'novoPeriodo.fim', value: fimHorario});
			
			
			$.postJSON(contextPath + "/cadastro/pdv/adicionarPeriodo",
					parametros, 
					function(result) {PDV.retornoAdicaoDiaFuncionamento(result);},
					null,
					true,
					"idModalPDV"
			);
		},
		
	
		
		retornoAdicaoDiaFuncionamento: 	function(result){
			
			var items = result[0];
			var mensagens = result[1];
			var status = result[2];
	
			if(status == "SUCCESS") {
				
				var tipoPeriodo = $("#selectDiasFuncionamento").val();
				var descTipoPeriodo = $("#selectDiasFuncionamento option:selected").text();
				var inicioHorario = $("#inicioHorario").val();
				var fimHorario = $("#fimHorario").val();
				
				var novoPeriodo = {tipoPeriodo:tipoPeriodo,descTipoPeriodo:descTipoPeriodo,inicio:inicioHorario,fim:fimHorario};
				
				PDV.diasFuncionamento.push(novoPeriodo);
				PDV.montartabelaDiasFuncionamento();
				
				var combo = $("#selectDiasFuncionamento");
				combo.clear();
				
				$.each(items, function(index, item) {
					var option = document.createElement("OPTION");
					option.innerHTML = item.value.$;
					option.value = item.key.$;
							
					combo.append(option);
			  	});
				
			} else {
				
				if(mensagens!=null && mensagens.length!=0) {
					exibirMensagem(status,mensagens);
				}
			}
		},
		
		montartabelaDiasFuncionamento: function(){
				 
			 $('#listaDiasFuncionais tr').remove();;
			 
			
			$.each(PDV.diasFuncionamento, function(index, row) {
					
				 var inputHidden = "<input type='hidden' value='"+row.tipoPeriodo +"' name='diasFuncionamento'/>";
					
				 var tr = $('<tr class="class_linha_1"></tr>');
				
				 tr.append("<td width='138'>&nbsp; "+ inputHidden+"</td>");
				 tr.append("<td width='249' class='diasFunc'>"+ row.descTipoPeriodo +"</td>");
				 tr.append("<td width='47'>&nbsp;</td>");
				
				 var as = (row.descTipoPeriodo == '24 horas' ? '':' as ');
				 tr.append("<td width='100'>"+ row.inicio + as + row.fim +"</td>");
								 
				 tr.append("<td width='227'>"+
				 			"<a onclick='PDV.removerDiasFuncionamento($(this).parent().parent(),"+index+");'" +
				 			" href='javascript:;'><img src='"+contextPath+"/images/ico_excluir.gif' alt='Excluir'" +
				 			"width='15' height='15' border='0'/></a></td>");
			      
				 $('#listaDiasFuncionais').append(tr);
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
			
			var combo = $("#selectDiasFuncionamento");
			combo.clear();
			
			$.each(items, function(index, item) {
				var option = document.createElement("OPTION");
				option.innerHTML = item.value.$;
				option.value = item.key.$;
						
				combo.append(option);
		  	});
			
		},
		
		exibirDialogExclusao:function(idPdv,idCota){
			
			$("#dialog-excluirPdv" ).dialog({
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
				}
			});
		},
		
		cancelarCadastro:function(){
			
			$("#dialog-cancelar-cadastro-pdv").dialog({
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
								
								$("#dialog-close").dialog("close");
								$("#dialog-cancelar-cadastro-pdv").dialog("close");
								$("#dialog-pdv").dialog("close");
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
						PDV.fecharModalCadastroPDV = false;
					}
				}
			});
		},
	
		
		poupNovoPDV:function(){
			
			PDV.limparCamposTela();
			PDV.carregarMaterialPromocional(null);
			PDV.carregarGeradorFluxo(null);
			PDV.carregarCaracteristicaEspecialidade(null);
			PDV.carregarPeriodosFuncionamento();
			
			$.postJSON(contextPath + "/cadastro/pdv/novo",
					"idCota="+PDV.idCota, 
					null
			);
						
			PDV.popup_novoPdv();
		},
		
		popup_novoPdv:function() {
			
			PDV.fecharModalCadastroPDV = false;
			
			$( "#dialog-pdv" ).dialog({
				resizable: false,
				height:600,
				width:890,
				modal: true,
				buttons: {
					"Confirmar": function() {
						PDV.salvarPDV();
					},
					"Cancelar": function() {
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
					
				}
			});

		},
		
		enviarFluxoPrincipal: function (){
			
			var txtGeradorFluxoPrincipal ="";
			var hiddenGeradorFluxoPrincipal=""; 
			
			if($("#txtGeradorFluxoPrincipal").val().length == 0){
				
				txtGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected").text();
				hiddenGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected").val();
				$("#selecTipoGeradorFluxo option:selected").remove();
			}
			else{
				
				txtGeradorFluxoPrincipal = $("#txtGeradorFluxoPrincipal").val();
				hiddenGeradorFluxoPrincipal=$("#hiddenGeradorFluxoPrincipal").val(); 
				var option = "<option value='" + hiddenGeradorFluxoPrincipal + "'>" + txtGeradorFluxoPrincipal + "</option>";
				
				$("#selecTipoGeradorFluxo").append(option);
				
				txtGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected").text();
				hiddenGeradorFluxoPrincipal = $("#selecTipoGeradorFluxo option:selected").val();
				
				$("#selecTipoGeradorFluxo option:selected").remove();
			}
			
			$("#txtGeradorFluxoPrincipal").val(txtGeradorFluxoPrincipal);
			$("#hiddenGeradorFluxoPrincipal").val(hiddenGeradorFluxoPrincipal);
			
			$("#selecTipoGeradorFluxo").sortOptions();

		}, 
		popup_img:function () {
			
			$( "#dialog-img" ).dialog({
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
				}
			});

		},
		
		excluirImagem :function (idPDV) {
			
			$.postJSON(contextPath + "/cadastro/pdv/excluirImagem",
					   "idPdv=" + idPDV, 
					   function(result){
				
				var mensagens = result[0];
				var status = result[1];
				
				$("#idImagem").attr("src", contextPath + "/images/pdv/no_image.jpeg");
							
				if(mensagens!=null && mensagens.length!=0) {
					exibirMensagem(status,mensagens);
				}
			});

		},
		validarEmail : function (email)	{
			er = /^[a-zA-Z0-9][a-zA-Z0-9\._-]+@([a-zA-Z0-9\._-]+\.)[a-zA-Z-0-9]{2}/;
			if(!er.exec(email)) {
				exibirMensagemDialog("WARNING",["N&atildeo &eacute um email v&aacutelido."],'idModalPDV');	
				$("#emailPDV").focus();
			}
		},
		carregarPeriodosFuncionamento:function(){
			$.postJSON(contextPath + "/cadastro/pdv/carregarPeriodoFuncionamento",
					   null, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectDiasFuncionamento").html(combo);
			},null,true,"idModalPDV");
		},
		
		carregarMaterialPromocional:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarMaterialPromocional",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectMaterialPromocional").html(combo);
							$("#selectMaterialPromocional").sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarMaterialPromocionalNotIn:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarMaterialPromocionalNotIn",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectMaterialPromocional").html(combo);
							$("#selectMaterialPromocional").sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarMaterialPromocionalSelecionado:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarMaterialPromocional",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectMap").html(combo);
							$("#selectMap").sortOptions();
			},null,true,"idModalPDV");
		},
			
		carregarEspecialidade:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarEspecialidades",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#especialidades_options").html(combo);
							$("#especialidades_options").sortOptions();
			},null,true,"idModalPDV");
		},
		
		
		carregarCaracteristicaEspecialidade:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarEspecialidades",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#caract_options").html(combo);
							$("#caract_options").sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarEspecialidadesNotIn:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarEspecialidadesNotIn",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#caract_options").html(combo);
							$("#caract_options").sortOptions();
			},null,true,"idModalPDV");
		},

		carregarGeradorFluxoNotIn:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxoNotIn",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selecTipoGeradorFluxo").html(combo);
							$("#selecTipoGeradorFluxo").sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarGeradorFluxo:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxo",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selecTipoGeradorFluxo").html(combo);
							$("#selecTipoGeradorFluxo").sortOptions();
			},null,true,"idModalPDV");
		},
		
		carregarGeradorFluxoSecundario:function(data){
			$.postJSON(contextPath + "/cadastro/pdv/carregarGeradorFluxo",
					   data, 
					   function(result){
							var combo =  montarComboBox(result, false);
							$("#selectFluxoSecundario").html(combo);
							$("#selectFluxoSecundario").sortOptions();
			},null,true,"idModalPDV");
		},
		
		mostra_expositor:function(idChecked){
			
			if(PDV.isChecked(idChecked)){
				$(".tipoExpositor" ).show();
			}
			else{
				$(".tipoExpositor" ).hide();
				$("#tipoExpositor").val("");
			}
		},

		limparCamposTela:function(){
			
			$("#idImagem").attr("src",  contextPath + "/images/pdv/no_image.jpeg");
			$("#selectStatus").val(""); 
			$("#nomePDV").val("");
			$("#contatoPDV").val("");
			$("#sitePDV").val("");
			$("#emailPDV").val("");
			$("#pontoReferenciaPDV").val("");
			$("#dentroOutroEstabelecimento").attr("checked",null);
			$("#selectTipoEstabelecimento").val("");
			$("#selectTamanhoPDV").val("");
			$("#qntFuncionarios").val("");
			$("#sistemaIPV").attr("checked",null);
			$("#porcentagemFaturamento").val("");
			$("#selectTipoLicenca").val("");
			$("#numerolicenca").val("");
			$("#nomeLicenca").val("");
			$("#selectDiasFuncionamento").val("");
			$("#inicioHorario").val("");
			$("#fimHorario").val("");
			$("#divTipoEstabelecimento").hide();
			
			PDV.diasFuncionamento = [];
			
			PDV.montartabelaDiasFuncionamento();
			
			$("#ptoPrincipal").attr("checked",null);
            $("#balcaoCentral").attr("checked",null);
            $("#temComputador").attr("checked",null);
            $("#luminoso").attr("checked",null);
            $("#textoLuminoso").val("");
            $("#selectdTipoPonto").val("");
            $("#selectCaracteristica").val("");
            $("#selectAreainfluencia").val("");
            $("#selectCluster").val("");
            
            $("#especialidades_options option").remove();
            
            $("#selectFluxoSecundario option").remove();
            
            $("#selectMap option").remove();
            
            $("#hiddenGeradorFluxoPrincipal").val("");
            $("#txtGeradorFluxoPrincipal").val("");
            
            $("#idPDV").val("");
            
            $("#tabpdv").tabs('select', 0);
		},
		
		selecionarDiaFuncionamento: function() {
			
			if($("#selectDiasFuncionamento").val()=='VINTE_QUATRO_HORAS') {
				$("#inicioHorario").attr('disabled', 'desabled');
				$("#fimHorario").attr('disabled', 'desabled');
				
			} else {
				$("#inicioHorario").attr('disabled', null);
				$("#fimHorario").attr('disabled', null);
			}
		}
		
};
