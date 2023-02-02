require: requirements.sc
theme: /
    
    state: Start
        q!: $regex</start>
        a: Здравствуйте!
        script:
            $session = {};
        
    state: checkDoctor
        q!: * ({$doctor * $medic} | $doctor) *
        script:
            parseDoctor($parseTree, $session);
        if: !$session.clientID 
            go!: /specifyId
        elseif: !$session.specialist
            a: /specifySpecialty
        else:
            go!: /printShedule
        
    state: checkRequest
        q!: * $medic *
        if: $session.clientID
            go!: /specifySpecialty
        else:
            go!: /specifyId
    
    state: authUser
        q!: * $digit *
        script:
            parseDidgit($parseTree, $session, $temp);
        if: $temp.lengthId > 9
            go!: /numberLong
        elseif: $temp.lengthId < 9
            go!: /numberShort
        elseif: $session.clientID && !$session.specialist
            script:
                log("BKSCCNJCNSCNNCJSCNSKCCKJNSC " + $session.clientID)
            go!: /specifySpecialty
        elseif: $session.clientID && $session.specialist
            go!: /printShedule
        elseif: !$session.clientID
            go!: /operator
    
    state: printShedule
        a: Расписание
        script:
            $jsapi.stopSession();
        
    state: specifySpecialty
        a: Уточните, пожалуйста, специальность врача, чтобы узнать расписание.
        
    state: specifyId
        a: Пожалуйста, назовите ваш идентификационный номер.
        
    state: numberLong
        a: Вы назвали больше цифр, чем нужно. Попробуйте ещё раз.
        
    state: numberShort
        a: Вы назвали меньше цифр, чем нужно. Попробуйте ещё раз.
        
    state: operator
        a: Соединяю с оператором
        script:
            $response.replies.push({
                "type": "switch",
                "phoneNumber": "79123456789",
                "continueCall": true,
                "continueRecording": true
            });
    
    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Не смог разобрать :( Попробуете сказать иначе?
            a: Простите, я не понял вас. Давайте попробуем еще раз?
            a: Извините, я не понял. Попробуйте сформулировать по-другому   