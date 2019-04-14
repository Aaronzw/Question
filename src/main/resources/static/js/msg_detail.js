/**

 @Name：站内信详情
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
        initMsgDetail()
        sendMsg();

    })
    function initMsgDetail(){
        var that=this;
        that.toUserId=$("input[name='toUserId']").val();
        that.conversationId=$("input[name='conversationId']").val();
        if(that.toUserId==undefined||that.conversationId==undefined){
            layer.msg("加载出错，请重新登录");
            return
        }
        that.pageSize=10;
        that.curPage=1;
        common.ajax("/msg/detail/request",{
            "limit":that.pageSize,
            "offset":that.curPage,
            "conversationId":that.conversationId
        },function (result) {
            console.log(result)
            if(result.code=="0"){
                laypage.render({
                    elem: 'msg-detail-page',
                    count: result.totals,
                    curr: that.curPage,
                    limit: that.pageSize,
                    jump: function(obj, first){
                        common.ajax("/msg/detail/request",{
                            "limit":that.pageSize,
                            "offset":obj.curr,
                            "conversationId":that.conversationId
                        },function (result) {
                            $(".js-msg-detail").html("");
                            $.each(result.data,function (index, item) {
                                console.log(item)
                                if(item.user.id==that.toUserId){
                                    var html='<li class="msg-left" data-id="'+item.message.id+'" >\n' ;
                                }else {
                                    var html='<li class="msg-right" data-id="'+item.message.id+'" >\n' ;
                                }
                                html=html+'<p class="pull-right msg-time" style="font-size: 10px"><span >'+item.message.createdDate+'</span></p>\n' +
                                    '                        <blockquote class="layui-elem-quote" >\n' +
                                    '                            <a href="/user/'+item.user.id+'">\n' +
                                    '                                <img src="'+item.user.headUrl+'"\n' +
                                    '                                     class="author-head-img"><cite class="msg-sender-name">'+item.user.name+'</cite>\n' +
                                    '                            </a>\n' +
                                    '                            <p class="msg-content">\n' +
                                    item.message.content+
                                    '                            </p>\n' +
                                    '                        </blockquote>\n' +
                                    '\n' +
                                    '                    </li>';
                                $(".js-msg-detail").append(html);
                            })
                        })
                    }
                });
            }else {
                layer.msg("请求数据失败！");
            }

        })
    }
    function sendMsg(){
        $(".js-sendMsg").on("click",function () {
            var content=$("textarea[name='addMsg']").val();
            var fromId=$("input[name='global-user-id']").val();
            var toId=$("input[name='toUserId']").val();
            console.log(content+fromId+toId);
            if(content==null||content==""){
                layer.msg("请输入消息内容！");
                return
            }
            if(fromId==undefined||toId==undefined){
                layer.msg("数据加载失败，请登陆后重试！");
                return
            }
            common.ajax("/msg/add",{
                "content":content,
                "fromId":fromId,
                "toId":toId,
            },function (result) {
                console.log(result);
                if(result.code=="0"){
                    layer.msg("发送成功");
                    window.location.reload();
                }else {
                    layer.msg("发送失败"+result.msg);
                }
            })
        })
    }
    //输出msg_list接口
    exports('msg_detail', {});
});
