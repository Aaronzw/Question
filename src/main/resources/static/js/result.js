/**

 @Name：搜索结果列表
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
        var that=this;
        that.totals=$("input[name='totals']").val()
        that.pageSize=10;
        laypage.render({
            elem: 'result-page',
            count: totals,
            curr: 1,
            limit: that.pageSize,
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
                                '                                <a style="color: #2D93CA;font-size: small"  class="msg-dtatil-link pull-right" href="/msg/detail?conversationId='+item.message.conversationId+'">查看会话</a>\n' +
                                '                                <a style="color: #2D93CA;font-size: small" class="pull-right msg-unread-cnt">共'+item.unReadCount+'条未读</a>\n' +
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
    })


    //输出msg_list接口
    exports('result', {});
});
