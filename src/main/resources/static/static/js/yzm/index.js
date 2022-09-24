/**
 * Created by ppp on 2019/9/5.
 */
//    使input文本框随其中内容而变化长度的方法
$(function () {
    var count = 120; //间隔函数，1秒执行
    var timer; //timer变量，控制时间
    var curCount;//当前剩余秒数
    function sendMessage() {
        curCount = count;
        $("#btnSendCode").attr("disabled", "true");
        $("#btnSendCode").val(+curCount + "秒再获取");
        timer = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
    }
    function SetRemainTime() {
        if (curCount == 0) {
            window.clearInterval(timer);//停止计时器
            $("#btnSendCode").removeAttr("disabled");//启用按钮
            $("#btnSendCode").val("重新发送");
        }
        else {
            curCount--;
            $("#btnSendCode").val(+curCount + "秒再获取");
        }
    }


    $('#tel').bind('input propertychange', function () {
        var $this = $(this);
        var len = $this.val().length;//获取当前文本框的长度
        if (len == 11) {
            var tel = $this.val()
            $('.grayBg').addClass('blueBg')
            $('.grayBg').click(function () {
                console.log('code')
            })

        }
    });
    $('#code').bind('input propertychange', function () {
        var $this = $(this);
        var len = $this.val().length;//获取当前文本框的长度
        if (len > 0) {
            $('.btn').addClass('redBg')
        }
    });
    $('#btnSendCode').bind('click', function () {
        if(!$("#L_email").val().match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)){
            layer.msg("邮箱不正确")
            $("#email1").focus();
        }else {
           var ema=$("#L_email").val();
            $.ajax({
                url:'/admin/sendEmail/修改密码',
                method:'post',
                dataType:'json',
                data:{email:ema},
                success:function (data) {
                    if(data.code==200){
                        layer.msg(data.msg)
                    }else{
                        window.clearInterval(timer);//停止计时器
                        $("#btnSendCode").removeAttr("disabled");//启用按钮
                        $("#btnSendCode").val("重新发送");
                        layer.msg(data.msg)
                    }
                },error:function (data) {
                    layer.msg("网络错误");
                }
            })
            sendMessage()
        }
    })
})


