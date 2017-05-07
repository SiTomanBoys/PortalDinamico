function CopyToClipboard(text)
{
	window.prompt("Copiar Xml al portapapeles: Ctrl+C, Enter", text);
}
function paginar(pag,acc)
{
	document.formulario.pagina.value=pag;
	document.formulario.accion.value=acc;
	document.formulario.submit();
}