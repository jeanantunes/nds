<script type="text/javascript" src="scripts/p3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>

<script>

	function submitForm(f){
		 var dateIni = $('#filtro_data_inicial_p3').val(),
			 dateFin = $('#filtro_data_final_p3').val();
		
		p3Controller.validarDados(dateIni, dateFin, f);		
	}
</script>

<div class="corpo">
	<div class="container">
		
		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados" width="950">
   	    <legend> Integração Fiscal P3 </legend>
   	    
   	    <form id="p3-form">
		<table width="520" border="0" class="filtro">
			<tr>
				<td>
					<input type="radio" value="p3" name="opcaoDeRelatorio" class='opcaoDeRelatorio'>Integração Fiscal P3
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" value="movCompleta" name="opcaoDeRelatorio" class='opcaoDeRelatorio'>P3 Movimentação Completa
				</td>
			</tr>
          </table> 
        <table width="520" border="0" class="filtro">   
            <tr>
              <td width="65">Data inicial: 
                <input type="text" name="dataInicial" id="filtro_data_inicial_p3" style="width: 100px;" readonly="true" />
              </td>
              <td width="65">Data final: 
                <input type="text" name="dataFinal" id="filtro_data_final_p3" style="width: 100px;" readonly="true" />
              </td>              
              <td width="45">
              	<span class="bt_novos" title="Gerar Arquivo">
              		<a href="javascript:submitForm('#p3-form');">
              			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
              			Arquivo
              		</a>
           		</span>
       		  </td>
            </tr>
          </table>
          </form>
          
      </fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
</div>

