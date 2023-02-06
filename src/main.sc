require: requirements.sc
theme: /

    state: Start
        q!: $regex</start>
        a: Здравствуйте!
        # Похоже что при перезапуске бота сессия не очищается
        # да, мы с Ваней тоже обнаружили, что странно. возможно, это особенность виджета здесь, но точной причины не знаю
        script:
            $session = {}
    
    state: noMatch
        q!: *
        a: Извините, не могу ответить на ваш вопрос. Попробуйте его переформулировать.
    
    state: doctorScheduleRequest
        # Дополните паттерны так, чтобы в этот стейт попадали все вопросы из файла helpers.js
        #q!: * (расписани* | $doctors | $medics | $admissions) *
        q!: * {$when $admissions $medics}  *
        q!: * { расписан* ($doctors | $medics)}*

        script:
            if ($parseTree.text) {
                var tempSpeciality = doctorSpecialities($parseTree.text)
                if (tempSpeciality){
                    $session.doctorSpeciality = tempSpeciality;
                }
            }

        if: $session.authSuccess
            if: !$session.doctorSpeciality
                go!: /doctorScheduleRequest/doctorSpecialityRequest
            else:
                go!: /printShedule
        else:
            go!: /autorizationRequest

        state: doctorSpecialityRequest
            a: Уточните, пожалуйста, специальность врача, чтобы узнать его расписание.
            
            state: doctorSpecialitySpecified
                q: * $doctors *

                if: $parseTree.text
                    go!: /doctorScheduleRequest


    state: autorizationRequest
        a: Пожалуйста, назовите ваш идентификационный номер.

        state: authorizationInput
            q: * $numbers *
            script:
                if ($parseTree.text){
                    parseDigit($parseTree, $session, $temp);
                }
    
            if: $session.authSuccess
                if: !$session.doctorSpeciality
                    go!: /doctorScheduleRequest
                else:
                    go!: /printShedule
            else:
                if: $temp.authResult == "lessThan9"
                    go!: /autorizationRequest/authorizationInput/lessThan9Digits
                if: $temp.authResult == "moreThan9"
                    go!: /autorizationRequest/authorizationInput/moreThan9Digits
                if: $temp.authResult == "invalidId"
                    go!: /autorizationRequest/authorizationInput/invalidID
            
            state: lessThan9Digits
                a: Вы назвали меньше цифр, чем нужно. Попробуйте ещё раз.
                go: /autorizationRequest
            
            state: moreThan9Digits
                a: Вы назвали больше цифр, чем нужно. Попробуйте ещё раз.
                go: /autorizationRequest
                
            state: invalidID
                a: Перевод на оператора.


    state: printShedule
        script:
            $temp.answer = getShedule($session.doctorSpeciality);
            $temp.answer += "</br>";
            $temp.answer += getSheduleMorphology($session.doctorSpeciality);
            if ($temp.answer === "</br>"){
                $temp.answer = "К сожалению, у меня нет информации о расписании выбранного специалиста.";
            }
        a: {{$temp.answer}}