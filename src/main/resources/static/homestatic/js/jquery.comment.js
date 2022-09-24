


    function crateCommentInfo(obj){

        if(typeof(obj.time) == "undefined" || obj.time == ""){
            obj.time = getNowDateFormat();
        }

        var el = "<div class='comment-info'><div class='logo'><img src='"+obj.user.logo+"'></div><div class='comment-right'><h3>"+obj.user.name+"</h3>"
            +"<div class='comment-content-header'><span><i class='glyphicon glyphicon-time'></i>"+obj.createtime+"</span>";

        if(typeof(obj.commentId) != "undefined" ){
            el =el+"<h2 style='display:none'>"+obj.commentId+"</h2>";
        }
        el = el+"</div><p class='content'>"+obj.context+"</p><div class='comment-content-footer'><div class='row'><div class='col-md-10'>";


        el = el + "</div><div  style='margin-top:10px; margin-bottom:10px;' class='col-md-2'><span class='reply-btn'>回复</span></div></div></div><div class='reply-list'>";
        if(obj.replyBody != "" && obj.replyBody.length > 0){
            var arr = obj.replyBody;
            for(var j=0;j<arr.length;j++){
                var replyObj = arr[j];
                el = el+createReplyComment(replyObj,2);

            }
        }
        el = el+"</div></div></div>";
        return el;
    }

    //返回每个回复体内容
    function createReplyComment(reply,sz){
   if(sz==2){
       var replyEl = "<div class='reply'><h2 style='display:none;'>"+reply.commentId+"</h2><div><a href='javascript:void(0)' class='replyname'>"+reply.user.name+"</a>:<a href='javascript:void(0)'>@"+reply.hfusername+"</a><span>"+reply.context+"</span></div>"
           + "<p style='margin:10px'><span>"+reply.createtime+"</span></p></div>";
   }else{
        var replyEl = "<div class='reply'><h2 style='display:none;'>"+reply.commentId+"</h2><div><a href='javascript:void(0)' class='replyname'>"+reply.user.name+"</a>:<a href='javascript:void(0)'>@"+reply.hfusername+"</a><span>"+reply.context+"</span></div>"
            + "<p style='margin:10px'><span>"+reply.createtime+"</span> <span   class='reply-list-btn'>回复</span></p></div>";
    }
    return replyEl;
    }
    function getNowDateFormat(){
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = filterNum(nowDate.getMonth()+1);
        var day = filterNum(nowDate.getDate());
        var hours = filterNum(nowDate.getHours());
        var min = filterNum(nowDate.getMinutes());
        var seconds = filterNum(nowDate.getSeconds());
        return year+"-"+month+"-"+day+" "+hours+":"+min+":"+seconds;
    }
    function filterNum(num){
        if(num < 10){
            return "0"+num;
        }else{
            return num;
        }
    }

    function replyClick(el){
        el.parent().parent().append("<div class='replybox'><textarea cols='80' rows='50'style='padding:10px'  placeholder='来说几句吧......' class='mytextarea' ></textarea><span class='send'>发送</span></div>")
            .find(".send").click(function(){
            var content = $(this).prev().val();
            if(content != ""){
                var parentEl = $(this).parent().parent().parent().parent();
                var obj = new Object();
                var user = new Object();
                user.name=chname;
                obj.user=user;
                if(el.parent().parent().hasClass("reply")){
                    obj.hfusername = el.parent().parent().find("a:first").text();
                }else{

                    obj.hfusername =parentEl.find("h3").text();
                }
                //评论ID
                obj.context=content;
                obj.createtime = getNowDateFormat();
                if(el.parent().parent().hasClass("reply")){
                    obj.parentid=el.parent().parent().find("h2").text();
                }else{
                    obj.parentid=parentEl.find("h2:first").text();

                }
                if(chname=='未登录'){
                    layer.msg("请先登录");
                    return;
                };

                var succ=true;
                if(obj.hfusername==chname){
                    layer.msg("不能回复自己");
                    succ=false;
                    return;
                }
                var replyString = createReplyComment(obj,2);
                $(".replybox").remove();
                parentEl.find(".reply-list").append(replyString).find(".reply-list-btn:last").click(function(){
                    layer.msg("不能回复自己");
                    succ=false;
                    return;
                });
                var i=0;
                if(succ){
                    i++;
                    if(i>1){return;}
                    console.log(obj)
                    $.ajax({
                        url: '/pingluninsert',
                        method: 'post',
                        dataType: 'json',
                        data: {articleid: thisarticle,hfusername:obj.hfusername, context:obj.context,parentid:obj.parentid},
                        success: function (data) {
                            if (data.code == 200) {
                                layer.msg(data.msg)

                            } else if (data.code == 500) {
                                layer.msg(data.msg)
                            } else if (data.hashMap.code == '990') {
                                layer.msg(data.hashMap.msg)
                            }
                            var succ=true;
                        }
                    })
                }


            }else{
                layer.msg("内容不能为空喔");
            }
        });
    }


    $.fn.addCommentList=function(options){

        var defaults = {
            data:[],
            add:""
        }
        var option = $.extend(defaults, options);
        //加载数据
        if(option.data.length > 0){
            var dataList = option.data;
            var totalString = "";
            for(var i=0;i<dataList.length;i++){
                var obj = dataList[i];
                var objString = crateCommentInfo(obj);
                totalString = totalString+objString;
            }
            $(this).append(totalString).find(".reply-btn").click(function(){
                if($(this).parent().parent().find(".replybox").length > 0){
                    $(".replybox").remove();
                }else{
                    $(".replybox").remove();
                    replyClick($(this));
                }
            });

            $(".reply-list-btn").click(function(){
                if($(this).parent().parent().find(".replybox").length > 0){
                    $(".replybox").remove();
                }else{
                    $(".replybox").remove();
                    replyClick($(this));
                }
            })
        }

        //添加新数据
        if(option.add != ""){
            obj = option.add;

            var str = crateCommentInfo(obj);
            $(this).prepend(str).find(".reply-btn").click(function(){
                replyClick($(this));
            });
        }
    }


