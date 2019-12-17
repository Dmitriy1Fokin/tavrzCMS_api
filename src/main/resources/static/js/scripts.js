
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
            alert("Ошибка при добавлении ДЗ!");
        }
    });
}


$(document).ready(function () {
    $("#inputSearchCadastralNum").submit(function (e) {
        e.preventDefault();
        var num = $("#cadastralNum").val();
        $('#tBodyPS').html('');

        $.ajax({
            url : 'searchPS',
            type: 'POST',
            dataType: 'json',
            data : ({
                cadastralNum: num
            }),
            success: function (pledgeSubjectList) {
                // console.log(pledgeSubjectList);
                el=document.getElementById("searchResultPS");
                el.style.display="block";
                for (var index in pledgeSubjectList) {
                    var ps = pledgeSubjectList[index];
                    if(ps['typeOfCollateral'] == 'BUILDING'){
                        var realty = ps['pledgeSubjectBuildingDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + realty['cadastralNum'] + "</td></tr>");
                    }else if(ps['typeOfCollateral'] == 'LAND_LEASE'){
                        var realty = ps['pledgeSubjectLandLeaseDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + realty['cadastralNum'] + "</td></tr>");
                    }else if(ps['typeOfCollateral'] == 'LAND_OWNERSHIP'){
                        var realty = ps['pledgeSubjectLandOwnershipDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + realty['cadastralNum'] + "</td></tr>");
                    }else if(ps['typeOfCollateral'] == 'PREMISE'){
                        var realty = ps['pledgeSubjectRoomDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + realty['cadastralNum'] + "</td></tr>");
                    }
                }
            }
        });
    });


    $("#inputSearchNamePS").submit(function (e) {
        e.preventDefault();
        var num = $("#namePS").val();
        $('#tBodyPS').html('');

        $.ajax({
            url : 'searchPS',
            type: 'POST',
            dataType: 'json',
            data : ({
                namePS: num
            }),
            success: function (pledgeSubjectList) {
                // console.log(pledgeSubjectList);
                el=document.getElementById("searchResultPS");
                el.style.display="block";
                for (var index in pledgeSubjectList) {
                    var ps = pledgeSubjectList[index];
                    if(ps["typeOfCollateral"] == "AUTO"){
                        var psIn = ps['pledgeSubjectAutoDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['vin'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "EQUIPMENT"){
                        var psIn = ps['pledgeSubjectEquipmentDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['serialNum'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "SECURITIES"){
                        var psIn = ps['pledgeSubjectSecuritiesDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['nominalValue'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "VESSEL"){
                        var psIn = ps['pledgeSubjectVesselDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['imo'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "TBO"){
                        var psIn = ps['pledgeSubjectTboDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['carryingAmount'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "BUILDING"){
                        var psIn = ps['pledgeSubjectBuildingDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['cadastralNum'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "LAND_LEASE"){
                        var psIn = ps['pledgeSubjectLandLeaseDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['cadastralNum'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "LAND_OWNERSHIP"){
                        var psIn = ps['pledgeSubjectLandOwnershipDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['cadastralNum'] + "</td></tr>");
                    }else if(ps["typeOfCollateral"] == "PREMISE"){
                        var psIn = ps['pledgeSubjectRoomDto'];
                        $('#tBodyPS').append("<tr><td><input type=\"checkbox\" onclick='choise()' name=\"checkPA\" value='"+ ps['pledgeSubjectId']+ "'></td><td>" + ps['name'] + "</td><td>" + psIn['cadastralNum'] + "</td></tr>");
                    }
                }
            }
        });
    });

});


function insertPS() {
    var pledgeSubjectsIdArray = [];
    $('#tBodyPS input:checkbox:checked').each(function () {
        pledgeSubjectsIdArray.push($(this).val());
    });
    var pledgeAgreementId = $("#pledgeAgreementId").text();

    $.ajax({
        url: 'insertPS',
        type: 'POST',
        dataType: 'json',
        data: {
            pledgeSubjectsIdArray: pledgeSubjectsIdArray,
            pledgeAgreementId: pledgeAgreementId
        },
        success: function () {
            location.reload();
        },
        error: function () {
            alert("!!!!!!!!!!!!!!!!!");
        }
    });
}

function withdrawFromDepositPledgeSubject(pledgeSubjectId, pledgeAgreementId) {
    $.ajax({
        url: 'withdrawFromDepositPledgeSubject',
        type: 'POST',
        dataType: 'json',
        data: {
            pledgeSubjectId: pledgeSubjectId,
            pledgeAgreementId: pledgeAgreementId
        },
        success: function (data) {
            console.log(data);

            location.reload();
        },
        error: function () {
            alert("!!!!!!!!!!!!!!!!!");
        }
    });

}
