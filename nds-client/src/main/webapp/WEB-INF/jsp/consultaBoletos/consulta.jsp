<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript">

	var pesquisaCotaConsultaBoletos = new PesquisaCota();

	$(function() {
		$(".boletosCotaGrid").flexigrid({
		    preProcess: getDataFromResult,
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
				width : 100,
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
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 90,
				sortable : true,
				align : 'right',
			}, {
				display : 'Tipo Baixa',
				name : 'tipoBaixa',
				width : 100,
				sortable : true,
				align : 'left',
			}, {
				display : 'Status',
				name : 'status',
				width : 80,
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
    });
		
	$(function() {
		$( "#dataDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#dataAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#numCota").numeric();
		$("#dataDe").mask("99/99/9999");
		$("#dataAte").mask("99/99/9999");
		
		$("#descricaoCota").autocomplete({source: ""});
		
    }); 
	
	function mostrarGridConsulta() {
		
		/*PASSAGEM DE PARAMETROS*/
		$(".boletosCotaGrid").flexOptions({
			
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/financeiro/boletos/consultaBoletos' />",
			params: [
			         {name:'numCota', value:$("#numCota").val()},
			         {name:'descricaoCota', value:$("#descricaoCota").val()},
			         {name:'dataDe', value:$("#dataDe").val()},
			         {name:'dataAte', value:$("#dataAte").val()},
			         {name:'status', value:$("#status").val()}
			        ] ,
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boletosCotaGrid").flexReload();
		
		$(".grids").show();
	}	
	
	function getDataFromResult(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
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
			         
		        	 linkImpressao = '<a href="javascript:;" onclick="imprimeBoleto(' + row.cell[0] + ');" style="cursor:pointer">' +
				 					 '<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0px" title="Imprime boleto" />' +
				 					 '</a>';
			         			 					     
			         linkEmail = '<a href="javascript:;" onclick="enviaBoleto(' + row.cell[0] + ');" style="cursor:pointer">' +
			                     '<img src="${pageContext.request.contextPath}/images/ico_email.png" hspace="5" border="0px" title="Envia boleto por e-mail" />' +
 					             '</a>';		 					 
									
				     row.cell[8] = linkImpressao + linkEmail;
		         }
		);
		
		return dadosPesquisa;
	}
		
	function enviaBoleto(nossoNumero) {
		var data = [
	   				   {
	   					   name: 'nossoNumero', value: nossoNumero
	   				   }
	   			   ];
		$.postJSON("<c:url value='/financeiro/boletos/enviaBoleto' />", data);
	}
	
	function imprimeBoleto(nossoNumero) {
		var data = [
	   				   {
	   					   name: 'nossoNumero', value: nossoNumero
	   				   }
	   			   ];
		$.postJSON("<c:url value='/financeiro/boletos/verificaBoleto' />", data, imprimirBoleto );
	}
	
	function imprimirBoleto(result){
		if (result!=''){
			document.location.assign("${pageContext.request.contextPath}/financeiro/boletos/imprimeBoleto?nossoNumero="+ result);
		}
	}
	
</script>

</head>

<body>

   
    <div class="container">
    
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Boletos por Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
            
              <td width="29">Cota:</td>
              <td width="105">
              	<input name="numCota" 
              		   id="numCota" 
              		   type="text"
              		   maxlength="11"
              		   style="width:80px; 
              		   float:left; margin-right:5px;"
              		   onchange="pesquisaCotaConsultaBoletos.pesquisarPorNumeroCota('#numCota', '#descricaoCota');" />
			  </td>
				
			  <td>
			      <input name="descricaoCota" 
			      		 id="descricaoCota" 
			      		 type="text" 
			      		 class="nome_jornaleiro" 
			      		 maxlength="255"
			      		 style="width:130px;"
			      		 onkeyup="pesquisaCotaConsultaBoletos.autoCompletarPorNome('#descricaoCota');" 
			      		 onblur="pesquisaCotaConsultaBoletos.pesquisarPorNomeCota('#numCota', '#descricaoCota');" />
			  </td>
              
              <td width="124">Data de Vencimento:</td>
              <td width="114"><input name="dataDe" id="dataDe" type="date" style="width:80px; float:left; margin-right:5px;" value="${dataDe}"/></td>
              <td width="25">Até:</td>
              <td width="110"><input name="dataAte" id="dataAte" type="date" style="width:80px; float:left; margin-right:5px;" value="${dataAte}"/></td>
              
              <td width="40">Status:</td>
              <td width="98">
                 <select name="status" id="status" style="width:100px;">
                    <c:forEach varStatus="counter" var="status" items="${listaStatusCombo}">
				       <option value="${status.key}">${status.value}</option>
				    </c:forEach>
                 </select>
              </td>
              
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
	       <fieldset class="classFieldset">
	       
	       	  <legend>Boletos Cadastrados</legend>

		       	<table class="boletosCotaGrid"></table>
		        
				<span class="bt_novos" title="Gerar Arquivo">
					<a href="${pageContext.request.contextPath}/financeiro/boletos/exportar?fileType=XLS">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a>
				</span>
				
				<span class="bt_novos" title="Imprimir">
					<a href="${pageContext.request.contextPath}/financeiro/boletos/exportar?fileType=PDF">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir
					</a>
				</span>
	      </fieldset>
	    </div>  
	  <div class="linha_separa_fields">&nbsp;</div>

  </div>

</body>