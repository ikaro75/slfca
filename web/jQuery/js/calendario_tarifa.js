function calendario_tarifa_init() {
  clave_tarifa = $(xml).find("registro").find("clave_tarifa")[0].firstChild.data;
  if (clave_tarifa!="9") {
      $("#td_cargo_fijo").parent().hide();
  }
  
  $("#clave_tarifa").change(function(){
      if ($(this).val()=="9"){
          $("#td_cargo_fijo").parent().show();
      } else {
          $("#td_cargo_fijo").parent().hide();
      }
  })
}