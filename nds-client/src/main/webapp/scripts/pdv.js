
var PDV = {
		
		abaDadosBasico:"DADOS_BASICO",
		abaEndereco:"ENDERECO",
		abaTelefone:"TELEFONE",
		abaCaracteristica:"CARACTERISTICA",
		abaEspecialidade:"ESPECIALIDADE",
		abaGeradorFluxo:"GERADOR_FLUXO",
		abaMap:"MAP",
		
		abaSelecionada:"",
		
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
			
		},
		
		carregarAbaEndereco: function (result){
			
		},
		
		carregarAbaTelefone: function (result){
			
		},
		
		carregarAbaCaracteristica: function (result){
			
		},
		
		carregarAbaEspecialidade: function (result){
			
		},
		
		carregarAbaGeradorFluxo: function (result){
			
		},
		
		carregarAbaMap: function (result){
			
		},
		
		editarPDV:function (idPdv,idCota,index){
			
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
		},
		
		salvarPDV : function(){
	
			$.postJSON(contextPath + "/cadastro/pdv/salvar",
					this.getDadosBasico() +"&" +
					this.getDadosCaracteristica() +"&" + 
					this.getDadosEspecialidade() +"&" + 
					this.getDadosGeradorFluxo()  +"&" +
					this.getDadosMap(), function(result){
				
			},this.errorSalvarPDV,true);
			
		},
		
		errorSalvarPDV: function (){
			
		},
		
		getDadosBasico: function (){
			
			var dados = 
				"pdvDTO.statusPDV="  							+$("#selectStatus").val() + "&" +
				"pdvDTO.dataInicio="							+$("#dataInicio").val()+ "&" +
				"pdvDTO.nomePDV="								+$("#nomePDV").val()+ "&" +
				"pdvDTO.contato="								+$("#contatoPDV").val()+ "&" +
				"pdvDTO.site="									+$("#sitePDV").val()+ "&" +
				"pdvDTO.email="									+$("#emailPDV").val()+ "&" +
				"pdvDTO.pontoReferencia="						+$("#pontoReferenciaPDV").val()+ "&" +
				"pdvDTO.dentroOutroEstabelecimento="	 		+this.isChecked("#dentroOutroEstabelecimento") + "&" +
				"pdvDTO.tipoEstabelecimentoAssociacaoPDV.id="	+$("#selectTipoEstabelecimento").val()+ "&" +
				"pdvDTO.tamanhoPDV="							+$("#selectTamanhoPDV").val()+ "&" +
				"pdvDTO.qtdeFuncionarios="						+$("#qntFuncionarios").val()+ "&" +
				"pdvDTO.sistemaIPV="							+this.isChecked("#sistemaIPV")+ "&" +
				"pdvDTO.porcentagemFaturamento="				+$("#porcentagemFaturamento").val()+ "&" +
				"pdvDTO.tipoLicencaMunicipal.id="				+$("#selectTipoLicenca").val()+ "&" +
				"pdvDTO.numeroLicenca="							+$("#numerolicenca").val()+ "&" +
				"pdvDTO.nomeLicenca="							+$("#nomeLicenca").val();
			
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
			
			 $("#especialidades_options option").each(function () {
				 listaEspecialidades = listaEspecialidades + "pdvDTO.especialidades="+ $(this).val() +"&";
             });
   
			return listaEspecialidades;
		},
		
		getDadosGeradorFluxo: function (){
			
			 var listaFluxoSecundario ="";
			
			 $("#selectFluxoSecundario option").each(function () {
				 listaFluxoSecundario = listaFluxoSecundario + "pdvDTO.geradorFluxoSecundario="+ $(this).val() +"&";
			 });
			 
			 /**
			  * TODO definir como sera o fluxo principal 
			  */
			 //listaFluxoSecundario = listaFluxoSecundario + "pdvDTO.pdvDTO.geradorFluxoPrincipal="+ $("#").val() +"&";
			 
			 return listaFluxoSecundario;	
		},
		
		getDadosMap: function (){
			
			var listaMaps ="";
			
			 $("#selectMap option").each(function () {
				 listaMaps = listaMaps + "pdvDTO.maps="+ $(this).val() +"&";
            });
  
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
		
		montartabelaDiasFuncionamento: function(){
			
			/**
			 * TODO chamar metodo para validar a inclusao conforme EMS
			 */
			
			 var idDiasFuncionamento = $("#selectDiasFuncionamento").val();
			 var diasFuncionamento = $("#selectDiasFuncionamento option:selected").text();
			 var inicioHoratio = $("#inicioHorario").val();
			 var fimHorario = $("#fimHorario").val();
			 
			 var inputHidden = "<input type='hidden' value='"+idDiasFuncionamento +"' name='diasFuncionamento'/>";
			 
			 $('#listaDiasFuncionais tr');
			 
			 var tr = $('<tr class="class_linha_1"></tr>');
			
			 tr.append("<td width='138'>&nbsp; "+ inputHidden+"</td>");
			 tr.append("<td width='249' class='diasFunc'>"+ diasFuncionamento +"</td>");
			 tr.append("<td width='47'>&nbsp;</td>");
			 tr.append("<td width='100'>"+ inicioHoratio +" as "+ fimHorario +"</td>");
			 tr.append("<td width='227'>"+
			 			"<a onclick='PDV.removerDiasFuncionamento($(this).parent().parent());'" +
			 			" href='javascript:;'><img src='"+contextPath+"/images/ico_excluir.gif' alt='Excluir'" +
			 			"width='15' height='15' border='0'/></a></td>");
		      
			 $('#listaDiasFuncionais').append(tr);
		},
		
		removerDiasFuncionamento: function (linha){
			
			/**
			 * TODO chamar metodo para validar a exclusao, verificar com analista se tera regra para exclusao 
			 */
			
			linha.remove();
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
		
		popup_novoPdv:function() {
			
			$( "#dialog-pdv" ).dialog({
				resizable: false,
				height:600,
				width:890,
				modal: true,
				buttons: {
					"Confirmar": function() {
						PDV.salvarPDV();
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		}
		
};

