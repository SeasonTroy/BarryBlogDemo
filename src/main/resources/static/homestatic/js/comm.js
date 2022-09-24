$(function() {
    //nav
    var obj = null;
    var As = document.getElementById('starlist').getElementsByTagName('a');
    obj = As[0];

    for (i = 1; i < As.length; i++) {
        if(window.location.href.indexOf("/info") >= 0){
          obj=null;
        }else{
            if (window.location.href.indexOf(As[i].href) >= 0) obj = As[i];
        }

    }
    obj.id = 'selected';
    //nav
    $("#mnavh").click(function() {
        $("#starlist").toggle();
        $("#mnavh").toggleClass("opena");
    });
    //search
   /* $(".searchicon").click(function() {
        $(".search").toggleClass("opena");
    });
    //searchclose
    $(".searchcloses").click(function() {
        $(".search").toggleClass("opena");
    });*/
    //banner
    $('#banner').easyFader();
    //nav menu
    $(".menu").click(function(event) {
        $(this).children('.sub').slideToggle();
    });
    //tab
    $('.tab_buttons li').click(function() {
        $(this).addClass('newscurrent').siblings().removeClass('newscurrent');
        $('.newstab>div:eq(' + $(this).index() + ')').show().siblings().hide();
    });
});