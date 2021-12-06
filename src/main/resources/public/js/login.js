function cancelBgColor() {
    var obj = $('.ys');
    obj.removeClass("ys");
}
function applyBgColor() {
    var obj = $('#test');
    obj.addClass("ys");
}
function my_login() {
    $.ajax({
        type: "post",
        url: "/user/login",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        data:JSON.stringify({
            "account":$('#account').val(),
            "password":$('#pwd').val()
        }),
        success: function (data) {
            if(data.code == 200){
                const token = data.data;
                window.localStorage.setItem('wxy_token', token);
                window.location.href='/main_page';
            }
        },
        error: function (ret) {
            console.log(ret);
        }
    });
}

function get_data() {
    var token = window.localStorage.getItem("wxy_token");
    $.ajax({
        type: "get",
        headers:{
            "authenticatedToken":token
        },
        url: "/user/get_data",
        success: function (data) {
            console.log(data);
        },
        error: function (ret) {
            console.log(ret);
        }
    });
}