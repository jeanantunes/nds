<script type="text/javascript">
	$(function(){
		$(".sociosGrid").flexigrid({
			dataType : 'json',
			colModel : [  {
				display : 'Nome',
				name : 'nome',
				width : 580,
				sortable : true,
				align : 'left'
			}, {
				display : 'Principal',
				name : 'principal',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 770,
			height : 160
		});
	});
</script>

<jsp:include page="dadosCadastraisCpf.jsp"></jsp:include>

<br />
<span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span>
<br />
<br />
<br clear="all" />
<strong>Sócios Cadastrados</strong>
<br />
<table class="sociosGrid"></table>