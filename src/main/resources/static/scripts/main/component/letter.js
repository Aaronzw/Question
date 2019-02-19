$(document).ready(function () {
    init();
    function init(){
        var that = this;
        var sUrl = '/msg/list/request' ;
        var uid=$("input[name='userIdVal']").val();
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "userId":uid,
                "offset":1,
                "limit":5},
        }).done(function (result) {
            if(result.code=="0"){
                var totals=result.totals;
                $(".paginate").pagination({
                    pageSize:5,
                    total: totals,
                    pageIndex:0,
                });
                var data=result.data;
                $(".conversation-list").html("");
                if(data.length>0){
                    $.each(data,function (index,item) {
                        console.log(item.message)
                        console.log(item.user)
                        var html='<li class="conversation-item">\n' +
                            '                <a class="letter-link" href="/msg/detail?conversationId='+item.message.conversationId+'"></a>\n' +
                            '                <div class="letter-info">\n' +
                            '                    <span class="letter-time">'+item.message.createdDate+'</span>\n' +
                            '                </div>\n' +
                            '                <div class="operate-bar">\n' +
                            '                    <a href="/msg/detail?conversationId='+item.message.conversationId+'">共'+item.unReadCount+'条未读消息</a>\n' +
                            '                </div>\n' +
                            '                <div class="chat-headbox">\n' +
                            '                        <span class="msg-num">\n' +
                            item.unReadCount+
                            '                        </span>\n' +
                            '                    <a class="list-head">\n' +
                            '                        <img alt="头像" src="'+item.user.headUrl+'">\n' +
                            '                    </a>\n' +
                            '                </div>\n' +
                            '                <div class="letter-detail">\n' +
                            '                    <a title="通知" class="letter-name level-color-1" href="/user/'+item.user.id+'">\n' +
                            item.user.name +
                            '                    </a>\n' +
                            '                    <p class="letter-brief">\n' +
                            item.message.content+
                            '                    </p>\n' +
                            '                </div>\n' +
                            '            </li>'
                        $(".conversation-list").append(html);
                    })
                }
                $(".paginate").on("pageClicked", function (event, data) {
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
    function requestForPages(index) {
        var that = this;
        var sUrl = '/msg/list/request' ;
        var uid=$("input[name='userIdVal']").val();
        $.ajax({
            url: sUrl,
            dataType: 'json',
            type: 'post',
            data: { "userId":uid,
                "offset":index,
                "limit":5},
        }).done(function (result) {
            if(result.code=="0"){
                var totals=result.totals;
                var data=result.data;
                $(".conversation-list").html("");
                if(data.length>0){
                    $.each(data,function (index,item) {
                        console.log(item.message)
                        console.log(item.user)
                        var html='<li class="conversation-item">\n' +
                            '                <a class="letter-link" href="/msg/detail?conversationId='+item.message.conversationId+'"></a>\n' +
                            '                <div class="letter-info">\n' +
                            '                    <span class="letter-time">'+item.message.createdDate+'</span>\n' +
                            '                </div>\n' +
                            '                <div class="operate-bar">\n' +
                            '                    <a href="/msg/detail?conversationId='+item.message.conversationId+'">共'+item.unReadCount+'条未读消息</a>\n' +
                            '                </div>\n' +
                            '                <div class="chat-headbox">\n' +
                            '                        <span class="msg-num">\n' +
                            item.unReadCount+
                            '                        </span>\n' +
                            '                    <a class="list-head">\n' +
                            '                        <img alt="头像" src="'+item.user.headUrl+'">\n' +
                            '                    </a>\n' +
                            '                </div>\n' +
                            '                <div class="letter-detail">\n' +
                            '                    <a title="通知" class="letter-name level-color-1" href="/user/'+item.user.id+'">\n' +
                            item.user.name +
                            '                    </a>\n' +
                            '                    <p class="letter-brief">\n' +
                            item.message.content+
                            '                    </p>\n' +
                            '                </div>\n' +
                            '            </li>'
                        $(".conversation-list").append(html);
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
});


