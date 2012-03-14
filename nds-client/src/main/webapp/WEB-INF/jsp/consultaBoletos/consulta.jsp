<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>

<script type="text/javascript">

	$(function() {
		$(".boletosCotaGrid").flexigrid({
		    preProcess: getDataFromResult,
		    dataType : 'json',
			colModel : [ {
				display : 'Nosso Numero',
				name : 'nossoNumero',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Emissao',
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
				sortable : true,
				align : 'left',
			}],
			sortname : "nossoNumero",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 360
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
		
		return dadosPesquisa;
	}
			
		
</script>

</head>

<body>

   
    <div class="container">
    
    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Baixa de Divida < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Boletos por Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
            
              <td width="29">Cota:</td>
              <td width="260"><input name="numCota" id="numCota" type="text" style="width:80px; float:left; margin-right:5px;" />
			      <span class="classPesquisar">
			          <a href="javascript:;">&nbsp;</a>
			      </span> - 
			      <input name="descricaoCota" id="descricaoCota" type="text" class="nome_jornaleiro" style="width:130px;"/>
			  
			  </td>
              
              <td width="124">Data de Vencimento:</td>
              <td width="114"><input name="dataDe" id="dataDe" type="text" id="dataDe" style="width:80px; float:left; margin-right:5px;" value="${dataDe}"/></td>
              <td width="25">Até:</td>
              <td width="110"><input name="dataAte" id="dataAte" type="text" id="dataAte" style="width:80px; float:left; margin-right:5px;" value="${dataAte}"/></td>
              
              <td width="40">Status:</td>
              <td width="98"><select name="status" id="status" style="width:100px;">
                <c:forEach varStatus="counter" var="status" items="${listaStatusCombo}">
				    <option value="${status.key}">${status.value}</option>
				</c:forEach>
              </select></td>
              
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
	       <fieldset class="classFieldset">
	       	  <legend>Boletos Cadastrados</legend>
	          <div class="grids" style="display:none;">
		       	  
		       	  <table class="boletosCotaGrid"></table>
		        
		          <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
				  <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
			  
			  </div>
	      </fieldset>
	      
	  <div class="linha_separa_fields">&nbsp;</div>

  </div>

</body>
</html>