(function (window, undefined) {
    var PopupAdd = Base.getClass('main.component.popupAdd');
    var PopupMsg = Base.getClass('main.component.popupMsg');

    Base.ready({
        initialize: fInitialize,
        binds: {
            'click #top-add-question': fClickAdd,
            'click #top-nav-count-wrap': fClickMsg
        }
    });

    function fInitialize() {
        var that = this;
    }

    function fClickAdd() {
        var that = this;
        PopupAdd.show({
            ok: function () {
                window.location.replace("/");
            }
        });
    }

    function fClickMsg() {
            var that = this;
            PopupMsg.show({
                ok: function () {
                    window.location.replace("/msg/list");
                }
            });
        }

})(window);