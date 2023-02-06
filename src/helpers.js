function doctorSpecialities(input) {
    var doctorSpeciality = "";
    if (input.match(/([^(физио)]терапевт|общ.*\s*практик)/gi)){
        doctorSpeciality = "Терапевт";
    }
    else if (input.match(/участков/gi)){
        doctorSpeciality = "Участковый";
    }
    else if (input.match(/(офтальмолог|окулист|глазно)/gi)){
        doctorSpeciality = "Офтальмолог";
    }
    else if (input.match(/(стоматолог|дантист|зубно|dentist)/gi)){
        doctorSpeciality = "Стоматолог";
    }
    else if (input.match(/(эндокринолог)/gi)){
        doctorSpeciality = "Эндокринолог";
    }
    else if (input.match(/(физиотерапевт)/gi)){
        doctorSpeciality = "Физиотерапевт";
    }
    
    return doctorSpeciality;
}

function findClient(id){
    return userIdArray.indexOf(id) > -1;   
}

function getShedule(speciality){
    for (var i=0; i < doctorSchedule.length; i++) {
        if (doctorSchedule[i].specialityName === speciality) {
            var text = doctorSchedule[i].specialityName + " принимает в следующие дни:";
            for (var j=0; j < doctorSchedule[i].weekSchedule.length; j++) {
                text += " " + doctorSchedule[i].weekSchedule[j].dayOfWeek + " - c " + doctorSchedule[i].weekSchedule[j].startTime + " до " + doctorSchedule[i].weekSchedule[j].endTime;
            
                if (j < doctorSchedule[i].weekSchedule.length - 1){
                    text += ",";
                }
                else{
                    text += ".";
                }
            }
            return text;
        }
    }
    return "";
}

function getSheduleMorphology(speciality){
    for (var i=0; i < doctorSchedule.length; i++) {
        if (doctorSchedule[i].specialityName === speciality) {
            var text = "К <b>" + doctorSchedule[i].specialityName.toLowerCase() + "у</b> можно прийти на приём:";
            for (var j=0; j < doctorSchedule[i].weekSchedule.length; j++) {
                text += " ";
                if (doctorSchedule[i].weekSchedule[j].dayOfWeek === "вторник"){
                    text += "<b>во ";
                }
                else{
                    text += "<b>в ";
                }
                
                if (doctorSchedule[i].weekSchedule[j].dayOfWeek === "среда" ||
                    doctorSchedule[i].weekSchedule[j].dayOfWeek === "пятница" ||
                    doctorSchedule[i].weekSchedule[j].dayOfWeek === "суббота"){
                    var temp = doctorSchedule[i].weekSchedule[j].dayOfWeek.replace(/.$/,"у")
                    text += temp;
                }
                else{
                    text += doctorSchedule[i].weekSchedule[j].dayOfWeek;
                }
                text += "</b> - c " + doctorSchedule[i].weekSchedule[j].startTime + " до " + doctorSchedule[i].weekSchedule[j].endTime;
            
                if (j < doctorSchedule[i].weekSchedule.length - 1){
                    text += ",";
                }
                else{
                    text += ".";
                }
            }
            return text;
        }
    }
    return "";
}

function parseDigit($parseTree, $session, $temp){
    var text = $parseTree.text;
    if (text.match(/\d+/)){
        var number = text.match(/\d+/)[0];
        if (number.length > 9){
            $temp.authResult = "moreThan9";
        }
        else if (number.length < 9){
            $temp.authResult = "lessThan9";
        }
        else{
            if (findClient(number)){
                $session.authSuccess = true;
            }
            else{
                $temp.authResult = "invalidId";
            }
        }
    }
}

/*
        - хотела бы уточнить расписание врача       V
        - подскажите расписание                     V
        - могу узнать когда принимает доктор        V
        - хочу узнать часы приёма врача             V
        - прием у доктора в какое время             V
        - расписание офтальмолога                   V
        - скажите расписание глазного врача         V
        - можно узнать когда принимает глазной      V
        - хочу узнать время приема окулиста         V
        - хотела бы узнать расписание терапевта     V
        - подскажи расписание участкового врача     V
        - напомните часы приёма участкового         V
        - когда работает врач общей практики        V
        - хотел бы уточнить расписание стоматолога  V
        - можно узнать когда принимает зубной врач  V
        - часы приёма дантиста                      V
        - прием у зубного в какие часы              V
*/