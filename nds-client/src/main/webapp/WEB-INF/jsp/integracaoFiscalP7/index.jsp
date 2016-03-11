<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<script type="text/javascript" src="scripts/p7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>

<script language="javascript" type="text/javascript">

$(function(){
	p7Controller.init();
});

function submitForm(f, tipo){
	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
    var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
	
    p7Controller.validarDados(month, year, f, tipo);		
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
    
    <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   	    <legend> Integra&ccedil;&atilde;o Fiscal P7</legend>
        <table border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr width="50">
          <td>M&ecirc;s/Ano:</td>

		   	<td>
				 <input class="campoDePesquisa" type="text" name="" id="datepickerMesAno" readonly="readonly" style="width:120px;" value="" />
				 
			   	<form id="formInventario">
				 <input type="hidden" name="mes" id="mesInput" value=""/>
				 <input type="hidden" name="ano" id="anoInput" value=""/>
				</form>
				
				<form id="formInventarioXLS">
				 <input type="hidden" name="mes" id="mesInputXLS" value=""/>
				 <input type="hidden" name="ano" id="anoInputXLS" value=""/>
				</form>
				
			 
		 	</td>
		   	        
        </tr>

		<tr>
            <td>
            	<span class="bt_novos" title="Gerar Arquivo">
					<a href="javascript:submitForm('#formInventario', 'txt');" rel="tipsy" title="Arquivo .TXT">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Arquivo
					</a>
				</span>
				<span class="bt_arq" id="gerarAquivoP7XLS">
				<a href="javascript:submitForm('#formInventarioXLS', 'xls');" rel="tipsy" title="Arquivo .XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>  
            </td>
		</tr>
            
      </table>
        
   </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
</div>     
      