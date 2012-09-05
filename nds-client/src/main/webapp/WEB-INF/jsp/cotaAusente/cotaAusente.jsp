
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cotaAusente.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCotaCotaAusente = new PesquisaCota();

$(function(){
	cotaAusenteController.init();
});

</script>
<style>
.linha_1, .linha_2, .linha_3, .linha_4, .linha_5, .linha_6, .linha_21, .linha_22, .linha_31, .linha_32 {display:none;}
#dialog-suplementar fieldset{width:350px!important;}
#dialog-suplementar .linha_separa_fields{width:350px!important;}

</style>
</head>

<body>

<form id="form-confirm">
<div id="dialog-confirm" title="Suplementar">
	<p>Confirma Suplementar?</p>
</div>
</form>

<form id="form-excluir">
<div id="dialog-excluir" title="Cota Ausente">
	<p>Confirma a exclusão desse Cota Ausente?</p>
</div>
</form>


<form id="form-novo">
<div id="dialog-novo" title="Incluir Cota Ausente"> 

	<jsp:include page="../messagesDialog.jsp" />

    <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Cota:</td>
             
              <td width="446" colspan="3">
 <!-- NOVA COTA - NUM -->     
<input id="idNovaCota" name="idNovaCota" type="text" style="width:80px; float:left; margin-right:5px;" 
	onchange="pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idNovaCota', '#idNomeNovaCota',true);" />
	
<!-- PESQUISAR NOVA COTA -->           
	<label style="margin-left:10px;">
           			Nome:
           		
           		</label>
           		
 <!-- NOVA COTA - NOME -->
<input id="idNomeNovaCota" name="idNomeNovaCota" type="text" class="nome_jornaleiro" style="width:280px;" 
	onkeyup="pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeNovaCota');" 
		 	   onblur="pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idNovaCota', '#idNomeNovaCota',true);" />
		 	   
       			</td>
            
            </tr>
          </table>
    </div>
</form>
   
    <div class="container">
    
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Cotas Ausentes</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="35">Data:</td>
              <td colspan="3">
<!--DATA-->
<input id="idData" value="${data}" type="text" name="datepickerDe" style="width:80px;" /></td>
              
                <td width="38">Cota:</td>
                <td width="123">
<!-- COTA -->                
<input id="idCota" name="idCota" type="text" style="width:80px; float:left; margin-right:5px;" 
	onchange="pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idCota', '#idNomeCota');"/>
	
<!-- PESQUISAR NOME COTA -->
<td width="40">Nome:</td>
                <td width="296">
<!-- NOME -->            
<input id="idNomeCota" name="idNomeCota" type="text" class="nome_jornaleiro" style="width:280px;" 
	onkeyup="pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeCota');" 
		 	   onblur="pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idCota', '#idNomeCota');"
	/>
				</td>
                <td width="27">Box:</td>
                <td width="111">
<!-- BOX -->
<input id="idBox" type="text" name="textfield" id="textfield" style="width:80px;"/></td>
              <td width="114"><span class="bt_pesquisar">
<!-- PESQUISAR -->
<a href="javascript:;" onclick="cotaAusenteController.cliquePesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Cotas Ausentes Cadastradas</legend>
        <div class="grids" style="display:none;">
       	  <table class="ausentesGrid"></table>
          <br />
          <span class="bt_novos" title="Gerar Arquivo">
<!-- ARQUIVO -->
<a href="${pageContext.request.contextPath}/cotaAusente/exportar?fileType=XLS">
	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	Arquivo
</a></span>

	<span class="bt_novos" title="Imprimir">
<!-- IMPRIMIR -->	
<a href="${pageContext.request.contextPath}/cotaAusente/exportar?fileType=PDF">
	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
	Imprimir
</a></span>

        </div>
        <span class="bt_novos" title="Novo">
<!-- NOVO -->
<a href="javascript:;" onclick="cotaAusenteController.popupNovaCotaAusente();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 

<form id="formRateio">
	<jsp:include page="modalRateio.jsp"/>
</form>
</body>
