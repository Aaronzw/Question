$(document).ready(function () {
    //init();
    function init(){
        var that = this;
        // var sUrl = '/msg/detail/request' ;
        var sUrl='/msg/detail/request';
        var conversationId=$("input[name='conversationId']").val();
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "conversationId":conversationId,
                "offset":1,
                "limit":10},
        }).done(function (result) {
            if(result.code=="0"){
                var totals=result.totals;
                $("#paginate").pagination({
                    pageSize:10,
                    total: totals,
                    pageIndex:0,
                });
                var data=result.data;
                $(".msg-detail-list").html("");
                if(data.length>0){
                    $.each(data,function (index,item) {
                        console.log(item.message)
                        console.log(item.user)
                        var html='<li class="msg-item" data-message-id="'+item.message.id+'">\n' +
                            '                <div class="msg-info">\n' +
                            '                    <span class="msg-time">'+item.message.createdDate+'</span>\n' +
                            '                    <a class="act-link js-report" data-msg-id="'+item.message.id+'">举报<span class="zu-bull">|</span></a>\n' +
                            '\n' +
                            '                    <a class="act-link js-delete-msg" data-msg-id="'+item.message.id+'">删除<span class="zu-bull">|</span></a>\n' +
                            '\n' +
                            '                </div>\n' +
                            '                <div class="chat-headbox">\n' +
                            '                    <a class="msg-head">\n' +
                            '                        <img alt="头像" class="msg-head-avatar" src="'+item.user.headUrl+'">\n' +
                            '                    </a>\n' +
                            '                </div>\n' +
                            '                <div class="msg-detail">\n' +
                            '                    <a title="通知" class="msg-name level-color-1" href="/user/'+item.user.id+'">\n' +
                            item.user.name+
                            '                    </a>\n' +
                            '                    <p class="msg-brief">\n' +
                            item.message.content +
                            '                    </p>\n' +
                            '                </div>\n' +
                            '\n' +
                            '            </li>'
                        $(".msg-detail-list").append(html);
                    })
                }
                $("#paginate").on("pageClicked", function (event, data) {
                    //分页按钮点击事件
                    requestDataForPage(data.pageIndex);
                    $(window).scrollTop(0);
                });
            }else {
                alert("请求数据出错 "+result.msg)
            }
        }).fail(function () {
            console.log("fail")
        }).always(function () {
            console.log("always")
        });
    }

    function requestDataForPage(offset) {
        var sUrl='/msg/detail/request';
        var conversationId=$("input[name='conversationId']").val();
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "conversationId":conversationId,
                "offset":offset+1,
                "limit":10},
        }).done(function (result) {
            if(result.code=="0"){
                var totals=result.totals;
                var data=result.data;
                $(".msg-detail-list").html("");
                if(data.length>0){
                    $.each(data,function (index,item) {
                        console.log(item.message)
                        console.log(item.user)
                        var html='<li class="msg-item" data-message-id="'+item.message.id+'">\n' +
                            '                <div class="msg-info">\n' +
                            '                    <span class="msg-time">'+item.message.createdDate+'</span>\n' +
                            '                    <a class="act-link js-report" data-msg-id="'+item.message.id+'">举报<span class="zu-bull">|</span></a>\n' +
                            '\n' +
                            '                    <a class="act-link js-delete-msg" data-msg-id="'+item.message.id+'">删除<span class="zu-bull">|</span></a>\n' +
                            '\n' +
                            '                </div>\n' +
                            '                <div class="chat-headbox">\n' +
                            '                    <a class="msg-head">\n' +
                            '                        <img alt="头像" class="msg-head-avatar" src="'+item.user.headUrl+'">\n' +
                            '                    </a>\n' +
                            '                </div>\n' +
                            '                <div class="msg-detail">\n' +
                            '                    <a title="通知" class="msg-name level-color-1" href="/user/'+item.user.id+'">\n' +
                            item.user.name+
                            '                    </a>\n' +
                            '                    <p class="msg-brief">\n' +
                            item.message.content +
                            '                    </p>\n' +
                            '                </div>\n' +
                            '\n' +
                            '            </li>'
                        $(".msg-detail-list").append(html);
                    })
                }
            }else {
                alert("请求数据出错 "+result.msg)
            }
        }).fail(function () {
            console.log("fail")
        }).always(function () {
            console.log("always")
        });
    }
    /*发送消息ajax post*/
    $(".js-sendMsg").on("click",function () {
        var content=$("textarea[name='MsgContent']").val();
        var conversationId=$('input[name="conversationId"]').val();
        var toId=$('input[name="targetUserId"]').val();
        var fromId=$('input[name="userIdVal"]').val();
        if(fromId==""){
            window.location.href="/reglogin";
            return;
        }
        if(content==""){
            alert("消息不能为空！");
            return
        }
        var flag=false;
        if(flag){
            return false;
        }
        flag=true;

        $.ajax({
            url: '/msg/add',
            type: 'post',
            data: {"content":content,
                "fromId":fromId,
                "toId":toId},
            dataType: 'json'
        }).done(function (oResult) {
            //alert("done")
            //未登陆，跳转到登陆页面
            console.log(oResult);
            if(oResult.code==0){
                alert("发送消息成功！");
                window.location.reload()
                $(window).scrollTop(0);
            }else {
                alert("发表回答失败！"+oResult.msg);
                window.location.reload()
            }
        }).fail(function () {
            console.log("fail");
            alert('出现错误，请重试');
            window.location.reload();
        }).always(function () {
            console.log("always");
            bSubmit = false;
        });
    });

});


