<%@ page contentType="text/html" pageEncoding="UTF-8" %>  

<script>

var month,year;
var dateInventario;
$(function(){
	$( "#datepickerMesAno").datepicker({
		changeMonth: true,
        changeYear: true,
		showOn: "button",
		dateFormat: 'MM/yy',
		buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly: true,
		onClose: function(dateText, inst) { 
	        month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
	        year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
	        dateInventario = new Date(year, month, 1);
	        
	        $("#mesInput").val(month);
	        $("#anoInput").val(year);
	        $(this).datepicker('setDate', dateInventario);
	    },
	    beforeShow: function() {
	    	$(".ui-datepicker-calendar").hide();
	    }
	});
	$( "#datepickerMesAno").datepicker($.datepicker.regional[ "pt-BR" ]);
});

function submitForm(f){
	
	if(month==null && year==null){
		exibirMensagem("WARNING", ["Escolha uma data válida"]);
	}else if(dateInventario.getTime() > new Date().getTime()){
		exibirMensagem("WARNING", ["Mês/Ano maior que o mês atual."]);
	}else{
		$.postJSON(contextPath + "/financeiro/integracaoFiscalP7/verificarExportar", 
				$(f).serialize(),
				function(result) {
						console.log(result);
						if(result.quantidadeGerada>0){
							$(f).submit();
						}else{
							exibirMensagem("WARNING", ["Nenhum dado encontrado para inventário."]);							
						}
			   	},
			   null,
			   true
		);
		
	}
}

</script>
<div>

<style type="text/css">
.ui-datepicker-calendar {
    display: none;
}​

</style>

<br clear="all"/>
    <br />
    
    <fieldset class="classFieldset">
   	    <legend> Integra&ccedil;&atilde;o Fiscal P7</legend>
        <table border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr width="50">
          <td>M&ecirc;s/Ano:</td>

<!-- <td width="50"> <input type="text" maxlength="6" /></td> -->
            
		   	<td>
				 <input class="campoDePesquisa" type="text" name="" id="datepickerMesAno" readonly="readonly" style="width:120px;" value="" />
				 
			   	<form method="POST" action="${pageContext.request.contextPath}/financeiro/integracaoFiscalP7/exportar" id="formInventario">
				 <input type="hidden" name="mes" id="mesInput" value=""/>
				 <input type="hidden" name="ano" id="anoInput" value=""/>
				</form>
			 
		 	</td>
		   	        
        </tr>

		<tr>
            <td>
            	<span class="bt_novos" title="Gerar Arquivo">
					<a href="javascript:submitForm('#formInventario');">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Arquivo
					</a>
				</span>  
            </td>
		</tr>
            
      </table>
        
   </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
</div>     
      