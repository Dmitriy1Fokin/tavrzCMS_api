
function setVisible(data) {
    el=document.getElementById(data);
    if(el.style.display=="none"){
        el.style.display="block";
    }else {
        el.style.display="none";
    }
}

$(document).ready(function () {
    $("#inputSearchNumPA").submit(function (e) {
        e.preventDefault();
        var num = $("#numPA").val();
        $('#tBodyPA').html('');

        $.ajax({
            url : 'searchPA',
            type: 'POST',
            dataType: 'json',
            data : ({
                numPA: num
            }),
            success: function (pledgeAgreementList) {
                el=document.getElementById("searchResultPA");
                el.style.display="block";
                for (var index in pledgeAgreementList) {
                    var pa = pledgeAgreementList[index];
                    $('#tBodyPA').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ pa['pledgeAgreementId']+ "'></td><td>" + pa['numPA'] + "</td><td>" + pa['dateBeginPA'] + "</td></tr>");
                }
            }
        });
    });
});


function choise() {
    var checkBoxArray = document.getElementsByName('checkPA'),
        count = checkBoxArray.length-1,
        isDisabled=true;
    for(;count>=0;count--){
        if (checkBoxArray[count]['checked']==true){
            isDisabled=!isDisabled;
            break;
        }
    }
    document.getElementById('buttonInsert').disabled = isDisabled;

}


function insertPA() {
    var pledgeAgreementIdArray = [];
    $('#tBodyPA input:checkbox:checked').each(function () {
        pledgeAgreementIdArray.push($(this).val());
    });
    var loanAgreementId = $("#loanAgreementId").text();

    $.ajax({
        url: 'insertPA',
        type: 'POST',
        dataType: 'json',
        data: {
            pledgeAgreementIdArray: pledgeAgreementIdArray,
            loanAgreementId: loanAgreementId
        },
        success: function () {
            location.reload();
        },
        error: function () {
            alert("!!!!!!!!!!!!!!!!!");
        }
    });
}
