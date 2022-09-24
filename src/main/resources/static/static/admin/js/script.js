layui.use('form', function () {
    var form = layui.form,
        layer = layui.layer;
});




$(document).ready(function() {
    //删除
    $(".del").click(function () {
        var url = $(this).attr("href");
        var id = $(this).attr("data-id");

        layer.confirm('你确定要删除么?', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.get(url, function (data) {
                if (data.code == 1) {
                    $(id).fadeOut();
                    layer.msg(data.msg, {icon: 1});
                } else {
                    layer.msg(data.msg, {icon: 2});
                }
            });
        }, function () {
            layer.msg("您取消了删除!");
        });
        return false;
    });
})


function delCache(){
    sessionStorage.clear();
    localStorage.clear();
}

function msg(code=1,msg='',url='',s=3) {
    if(typeof code == 'object') {
        msg = code.msg;
        url = code.url || '';
        s = code.s || 3;
        code = code.code;
    }
    code = code==1 ? 1 : 2;
    layer.msg(msg, {icon: code,offset: 't',shade: [0.4, '#000']});
    if(url){
        setTimeout(function () {
            window.location.href = url;
       },s*1000);
    }
}



