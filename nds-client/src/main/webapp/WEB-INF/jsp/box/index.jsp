<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/box.js"></script>
<script type="text/javascript">

$(function(){
	boxController.init();
	
});
</script>
<style type="text/css">
	#dialog-box{display:none;}
	#dialog-box fieldset{width:570px!important;}
</style>

</head>

<body>
	<form action="/cadastro/box/" name="form1">
	<div id="dialog-box" title="Detalhes do Box">
		<fieldset>
	    	<legend>Cotas Associadas ao Box</legend>
	        <table class="boxCotaGrid"></table>
	    </fieldset>
	</div>
	</form>

	<form action="/cadastro/box/" name="form2">
	<div id="dialog-excluir" title="Excluir Box">		
		<p>Confirma a exclus&atilde;o deste Box?</p>
	</div>
	</form>
	
	<form action="/cadastro/box/" name="form3">
	<input type="hidden" name="box.id" id="boxId" style="width:80px;"/>
	<div id="dialog-novo" title="Incluir Novo Box">  
		<jsp:include page="../messagesDialog.jsp" />  
	    <table width="356" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="112">Box:</td>
	              <td width="233"><input type="text" name="box.codigo" id="boxCodigo" style="width:80px;"/></td>
	            </tr>
	            <tr>
	              <td>Nome:</td>
	              <td><input type="text" name="box.nome" id="boxNome" style="width:220px;"/></td>
	            </tr>
	            <tr>
	              <td>Tipo de Box:</td>
	              <td><select name="box.tipoBox" id="boxTipoBox" style="width:227px;">
	                  	<option selected="selected"> </option>
	                    <option value="ENCALHE">Encalhe</option>
	                    <option value="LANCAMENTO">Lan&ccedil;amento</option>
	                    <option value="POSTO_AVANCADO">Posto Avan&ccedilado</option>
	             	 </select>
	             </td>
	            </tr>	           
          </table>
    </div>
	</form>
	
	<form action="/cadastro/box/" name="form4">
	 <fieldset class="classFieldset">
   	    <legend> Pesquisar Box</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="30">Box:</td>
                <td><input type="text" name="box.codigo" id="pesquisaCodigoBox" style="width:130px;"/></td>
                <td width="76">Tipo de Box:</td>
                <td><select name="box.tipoBox" id="pesquisaTipoBox" style="width:180px;">
                		<option selected="selected"> </option>
	                    <option value="ENCALHE">Encalhe</option>
	                    <option value="LANCAMENTO">Lan&ccedil;amento</option>
	                    <option value="POSTO_AVANCADO">Posto Avan&ccedilado</option>
              </select></td>
              <td><span class="bt_pesquisar"><a href="javascript:;" id="btnPesquisar">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Boxes Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<table class="boxGrid"></table>
        </div>

           <span class="bt_novos" title="Novo"><a href="javascript:;" id="btnNovo"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
    </form>

</body>