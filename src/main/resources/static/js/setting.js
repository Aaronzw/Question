/**

 @Name：设置界面
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
        updatePass();
    })
    function updatePass(){
        $(".js-CertainUpdate").on("click",function () {
            var nowpass=$("input[name='nowpass']").val();
            var pass=$("input[name='pass']").val();
            var repass=$("input[name='repass']").val();
            if(pass!=repass){
                layer.msg("新密码与确认密码不一致")
                return
            }
            common.ajax("/updatePassword",{
                "nowpass":nowpass,
                "pass":pass
            },function (result) {
                console.log(result);
                if(result.code=="0"){
                    layer.msg("修改密码成功")
                }else if(result.code=="1") {
                    layer.msg("修改密码失败");
                }else if(result.code=="999"){
                    layer.msg("请重新登录")
                }
            })
            console.log(nowpass+","+pass+","+repass);
        });
    };

    //输出msg_list接口
    exports('setting', {});
});
