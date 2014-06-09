<div id="dialogCopiarCota" title="Copiar Cotas" style="display:none;">
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogCopiarCota" name="messageDialog"/>
	</jsp:include> 
	<fieldset>
    	<legend>Copiar Cotas</legend>
        
        <table id="cotasParaCopiaGrid" width="347" border="0" cellspacing="1" cellpadding="1"></table>
        <br clear="all" />
        <table width="347" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td>Rota Atual:</td>
            <td>
            	<input type="text" name="rotaAtual" id="rotaAtual" style="width:220px;" disabled="disabled" />
            </td>
          </tr>
          <tr>
            <td><strong>Nova Rota:</strong></td>
            <td>
            	<select name="selectNovasRotas" id="selectNovasRotas" style="width: 190px;">
			    </select>
			</td>
          </tr>
        </table>
  </fieldset>
</div>