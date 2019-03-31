/**

 @Name：站内信列表
 @Author：庄巍

 */
layui.define(['element', 'form','laypage','jquery','laytpl','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common
        ,layer=layui.layer;
    //statr 分页

    $(function () {
        var question_id=$("input[name='questionId']").val();
        var that=this;
        that.page=1;
        that.pageSize=7;
        that.userId=$("input[name='global-user-id']").val();
        if(that.userId==undefined){
            layer.msg("未登录，请登录后查看！")
        }
        common.ajax("/msg/list/request",{
            "limit":that.pageSize,
            "offset":that.page,
            "userId":that.userId
        },function (res) {
            if(res.code=="0"){
                that.totals=res.totals;
                initPages(that.totals,that.page,that.pageSize,that.userId)
            }
        })
    })
    function initPages(totals,cur,pageSize,userId){
        var that=this;
        laypage.render({
            elem: 'msg-list-page',
            count: totals,
            curr: cur,
            limit: pageSize,
            jump: function(obj, first){
                // console.log(obj.curr+""+obj.limit);
                var question_id=$("input[name='questionId']").val();
                common.ajax("/msg/list/request",{
                    "limit":pageSize,
                    "offset":obj.curr,
                    "userId":userId
                },function (res) {
                    if(res.code=="0"){
                        $(".js-msg-list").html("");
                        $.each(res.data,function (index, item) {
                            console.log(item);
                            var html='<li data-conversation-id="'+item.message.conversationId+'">\n' +
                                '                        <p><span>'+item.message.createdDate+'</span></p>\n' +
                                '                        <blockquote class="layui-elem-quote" >\n' +
                                '                            <img src="'+item.user.headUrl+'" class="author-head-img">\n' +
                                '                            <a href="/user/11"><cite>'+item.user.name+'</cite>\n' +
                                '                                <a class="msg-dtatil-link pull-right" href="/msg/detail/'+item.message.conversationId+'">查看会话</a>\n' +
                                '                                <a class="pull-right msg-unread-cnt">共'+item.unReadCount+'条未读</a>\n' +
                                '                                :\n' +
                                '                                <p class="msg-content">'+item.message.content+'</p>\n' +
                                '                            </a>\n' +
                                '                        </blockquote>\n' +
                                '                    </li>';
                            $(".js-msg-list").append(html);
                            // $(window).scrollTop(0);
                        });

                    }else {
                        layer.msg("ajax请求失败");
                    }
                })

            }
        });
    }

    //输出msg_list接口
    exports('msg_list', {});
});
