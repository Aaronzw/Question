/**

 @Name：关注的人列表
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
        that.pageSize=10;
        that.page=1;
        that.userId=$("input[name='cur_page_user']").val();
        if(that.userId==undefined){
            layer.msg("请重新登录");
            return
        }
        common.ajax("/request/followerList",{
            "userId":that.userId,
            "limit":that.pageSize,
            "offset":that.page
        },function (result) {
            if(result.code=="0"){
                that.totals=result.data.totals;
                initFollowerPage(that.totals,that.page,that.pageSize,that.userId);
            }else {
                layer.msg("请求数据失败");
            }
        })
        common.ajax("/request/followeeList",{
            "userId":that.userId,
            "limit":that.pageSize,
            "offset":that.page
        },function (result) {
            if(result.code=="0"){
                that.totals=result.data.totals;
                initFolloweePage(that.totals,that.page,that.pageSize,that.userId);
            }else {
                layer.msg("请求数据失败");
            }
        })
    });
    function initFollowerPage(totals,page,pageSize,userId){
        laypage.render({
            elem: 'follower-page',
            count: totals,
            curr: page,
            limit: pageSize,
            jump: function(obj, first){
                var question_id=$("input[name='questionId']").val();
                common.ajax("/request/followerList",{
                    "limit":pageSize,
                    "offset":obj.curr,
                    "userId":userId
                },function (res) {
                    if(res.code=="0"){
                        $(".follower-list").html("");
                        $.each(res.data.list,function (index, item) {
                            // console.log(item);
                            var html='<li class="item">\n' +
                                '                                <div class="letter-info">\n' +
                                '                                    <span class="l-time">\n' +
                                '                                        关注于\n' + item.followTime+
                                '                                    </span>\n' +
                                '                                    <div class="l-operate-bar">\n' +
                                '                                        <a class="layui-btn layui-btn-sm" data-sender-id="'+item.user.id+'" href="/sendMsgTo/'+item.user.id+'">联系TA</a>\n' +
                                '                                        <span class="js-replacebtn">';
                                if(item.followStatus=="0")
                                    html=html+'                                            <a class="layui-btn layui-btn-sm follow-btn js-follow" href="javascript:void(0);">\n' +
                                '                                            关注TA\n' +
                                '                                            </a>';
                                else
                                    html=html+'                                            <a class="layui-btn layui-btn-sm follow-btn js-cancel-follow" href="javascript:void(0);">\n' +
                                    '                                           取关TA\n' +
                                    '                                            </a>';
                                html=html+'                                        </span>\n' +
                                '                                    </div>\n' +
                                '                                </div>\n' +
                                '                                <div class="chat-headbox">\n' +
                                '                                    <a class="list-head" href="/user/'+item.user.id+'">\n' +
                                '                                        <img alt="头像" src="'+item.user.headUrl+'">\n' +
                                '                                    </a>\n' +
                                '                                </div>\n' +
                                '                                <div class="letter-detail">\n' +
                                '                                    <a href="/user/'+item.user.id+'" class="letter-name level-color-7">'+item.user.name+'</a>\n' +
                                '                                    <p class="letter-brief"></p>\n' +
                                '                                </div>\n' +
                                '                            </li>';
                            $(".follower-list").append(html);
                        });
                    }else {
                        layer.msg("ajax请求失败");
                    }
                })

            }
        });
    };
    function initFolloweePage(totals,page,pageSize,userId){
        laypage.render({
            elem: 'followee-page',
            count: totals,
            curr: page,
            limit: pageSize,
            jump: function(obj, first){
                var question_id=$("input[name='questionId']").val();
                common.ajax("/request/followeeList",{
                    "limit":pageSize,
                    "offset":obj.curr,
                    "userId":userId
                },function (res) {
                    if(res.code=="0"){
                        $(".followee-list").html("");
                        $.each(res.data.list,function (index, item) {
                            // console.log(item);
                            var html='<li class="item">\n' +
                                '                                <div class="letter-info">\n' +
                                '                                    <span class="l-time">\n' +
                                '                                        关注于\n' +
                                item.followTime+
                                '                                    </span>\n' +
                                '                                    <div class="l-operate-bar">\n' +
                                '                                        <a class="layui-btn layui-btn-sm" data-sender-id="'+item.user.id+'" href="/sendMsgTo/'+item.user.id+'">联系TA</a>\n' +
                                '                                        <span class="js-replacebtn">';
                                if(item.followStatus=="0")
                                    html=html+'                                            <a class="layui-btn layui-btn-sm follow-btn js-follow" href="javascript:void(0);">\n' +
                                        '                                            关注TA\n' +
                                        '                                            </a>';
                                else
                                    html=html+'                                            <a class="layui-btn layui-btn-sm follow-btn js-cancel-follow" href="javascript:void(0);">\n' +
                                        '                                           取关TA\n' +
                                        '                                            </a>';
                                html=html+'                                        </span>\n'+
                                    '                                    </div>\n' +
                                    '                                </div>\n' +
                                    '                                <div class="chat-headbox">\n' +
                                    '                                    <a class="list-head" href="/user/'+item.user.id+'">\n' +
                                    '                                        <img alt="头像" src="'+item.user.headUrl+'">\n' +
                                    '                                    </a>\n' +
                                    '                                </div>\n' +
                                    '                                <div class="letter-detail">\n' +
                                    '                                    <a href="/user/'+item.user.id+'" class="letter-name level-color-7">'+item.user.name+'</a>\n' +
                                    '                                    <p class="letter-brief"></p>\n' +
                                    '                                </div>\n' +
                                    '                            </li>';
                            $(".followee-list").append(html);
                        });
                    }else {
                        layer.msg("ajax请求失败");
                    }
                })

            }
        });
    };
    //输出msg_list接口
    exports('follow', {});
});
