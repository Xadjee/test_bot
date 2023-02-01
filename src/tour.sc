theme: /Tour

    state: GetTour
        intent!: /getTour
        script:
            extractEntities($parseTree, $session, $client);
        a: Я помогу вам оформить заявку.
        go!: /Tour/TourRequest

    state: TourRequest
        if: !$session.geo
            go!: /Tour/GetCountry
        elseif: !$session.peopleNumber
            go!: /Tour/GetPeopleNumber
        elseif: !$session.childrenNumber
            go!: /Tour/GetChildrenNumber
        elseif: !$session.moneyAmount
            go!: /Tour/GetMoneyAmount
        elseif: !$session.startDate
            go!: /Tour/GetStartDate
        elseif: !$session.duration
            go!: /Tour/GetDuration
        elseif: !$session.hotelStars
            go!: /Tour/GetHotelStars
        elseif: !$client.name
            go!: /Tour/GetName
        elseif: !$client.phoneNumber
            go!: /Tour/GetPhoneNumber
        elseif: !$session.comment
            go!: /Tour/GetComment
        else:
            go!: /Tour/Email

    state: GetCountry
        a: В какой город или страну вы хотите поехать?
        
        state: Country
            q: * @geo *
            script: 
                $session.geo = capitalize($parseTree._geo);
            a: Отлично, едем в {{capitalize($nlp.inflect($session.geo, "accs"))}}!
            go!: /Tour/TourRequest
        
        state: DoNotKnow
            intent: /doNotKnow
            script: $session.geo = 'не определился(-лась)';
            a: Определиться с направлением вам поможет менеджер.
            go!: /Tour/TourRequest

        # state: NoCountry
        #     event: noMatch
        #     script: $session.geo = 'не определился(-лась)';
        #     a: Определиться с направлением вам поможет менеджер.
        #     go!: /Tour/TourRequest
    
    state: GetPeopleNumber
        a: Сколько человек планируют поездку?
        
        state: Number
            q: * @duckling.number *
            script: 
                $session.peopleNumber = $parseTree['_duckling.number'];
            if: $session.peopleNumber < 0
                script: $session.peopleNumber = 'не определился(-лась)';
            go!: /Tour/TourRequest
        
        state: DoNotKnow
            intent: /doNotKnow
            script: $session.peopleNumber = 'не определился(-лась)';
            go!: /Tour/TourRequest
        
        # state: NoNumber
        #     event: noMatch
        #     script: $session.peopleNumber = 'не определился(-лась)';
        #     go!: /Tour/TourRequest

    state: GetChildrenNumber
        a: Сколько поедет детей?

        state: Number
            q: * @duckling.number *
            script: 
                $session.childrenNumber = $parseTree['_duckling.number']
            if: $session.childrenNumber < 0
                script: $session.childrenNumber = 'не определился(-лась)';
            elseif: $session.childrenNumber == 0
                a: Детей в поездку не берем, хорошо.
            go!: /Tour/TourRequest

        state: DoNotKnow
            intent: /doNotKnow
            script: $session.childrenNumber = 'не определился(-лась)'
            go!: /Tour/TourRequest

    state: GetMoneyAmount
        a: На какой бюджет вы рассчитываете?
        
        state: MoneyAmount
            q: * (@duckling.amount-of-money|@duckling.number) *
            script:
                if ($parseTree["_duckling.amount-of-money"]) {
                    $session.moneyAmount = $parseTree["_duckling.amount-of-money"];
                } else if ($parseTree["_duckling.number"]) {
                    $session.moneyAmount = $parseTree["_duckling.number"];
                }
            a: Понял, зафиксировал.
            go!: /Tour/TourRequest

        state: DoNotKnow
            intent: /doNotKnow
            script: $session.moneyAmount = 'не определился(-лась)'
            go!: /Tour/TourRequest

    state: GetStartDate
        a: Назовите, пожалуйта, дату начала поездки.

        state: Date
            q: * @duckling.date *
            script: 
                $session.startDate = moment($parseTree['_duckling.date']['value']).format("DD.MM.YYYY");
            a: Отлично, подберем вам тур с {{ $session.startDate }}.
            go!: /Tour/TourRequest

        state: DoNotKnow
            intent: /doNotKnow
            script: $session.startDate = 'не определился(-лась)'
            go!: /Tour/TourRequest

    state: GetDuration
        a: А сколько продлится ваше путешествие?

        state: Duration
            q: * @duckling.duration *
            script: 
                //функция getDurationUnit возвращает название промежутка времени на русском языке
                $session.duration = $parseTree['_duckling.duration']['value'] + ' ' + $nlp.conform(getDurationUnit($parseTree['_duckling.duration']['unit']), $parseTree['_duckling.duration']['value']);
            a: {{ $session.duration }} - отличный план!
            go!: /Tour/TourRequest

        state: DoNotKnow
            intent: /doNotKnow
            script: $session.duration = 'не определился(-лась)'
            go!: /Tour/TourRequest

    state: GetHotelStars
        a: Какую звездность отеля предпочитаете?

        state: Number
            q: * @duckling.number *
            script: 
                $session.hotelStars = $parseTree['_duckling.number']
            if: $session.hotelStars > 5
                a: Таких отелей еще не придумали, но постараюсь найти самое лучшее для вас!
                script:
                    $session.hotelStars = 5
            elseif: $session.hotelStars <= 0
                a: Без звезд, хорошо, зафиксировано.
            else:
                a: {{$session.hotelStars}} {{ $nlp.conform("звезда", $session.hotelStars) }}, хорошо, записал.
            go!: /Tour/TourRequest

        state: DoNotKnow
            intent: /doNotKnow
            script: $session.hotelStars = 'не определился(-лась)'
            go!: /Tour/TourRequest

    state: GetName
        a: Осталось совсем немного! Подскажите, как вас зовут?

        state: Name
            q: * @pymorphy.name *
            script: 
                $client.name = $parseTree['_pymorphy.name'];
            a: Очень приятно, {{ capitalize($client.name) }}!
            go!: /Tour/TourRequest

        state: NoName
            event: noMatch
            a: Чтобы оформить заявку, обязательно нужно ваше имя. Представьтесь, пожалуйста!
            go: ..

    state: GetPhoneNumber
        a: По какому номеру мы можем связаться с вами?
        
        state: PhoneNumber
            q: * @duckling.phone-number *
            script:
                $client.phoneNumber = $parseTree['_duckling.phone-number'];
            a: Спасибо!
            go!: /Tour/TourRequest
        
        state: NoPhoneNumber
            event: noMatch
            a: Без номера телефона мы не сможем связаться с вами! Напишите его, пожалуйста.
            go: ..
    
    state: GetComment
        a: И последний шаг: вы можете оставить любой комментарий для вашего менеджера!
        
        state: NoComment
            intent: /noComment
            script: $session.comment = '-';
            a: Без комментариев так без комментариев 🙂
            go!: /Tour/TourRequest

        state: Comment
            event: noMatch
            script: 
                if (/[a-zа-я]/i.test($parseTree.text)){
                    $session.comment = $parseTree.text;
                    $reactions.answer('Обязательно учтем ваш комментарий!');
                } else {
                    $session.comment = '-';
                    $reactions.answer('Без комментариев так без комментариев 🙂');
                }
            go!: /Tour/TourRequest

    state: Email
        script:
            var subject = 'Заявка на подбор тура';
            var body = 'Имя: ' + capitalize($client.name) + '<br/>Телефон: ' + $client.phoneNumber + '<br/>Человек: ' + $session.peopleNumber + '<br/>Детей: ' + $session.childrenNumber + '<br/>Направление: ' + $session.geo + '<br/>Бюджет: ' + $session.moneyAmount + '<br/>Дата начала: ' + $session.startDate + '<br/>Длительность: ' + $session.duration + '<br/>Звездность отеля: ' + $session.hotelStars + '<br/>Комментарий: ' + $session.comment 
            var email = $mail.sendMessage('e.yankovskaya@forte-com.ru', subject, body);
            $temp.status = email.status;
        if: $temp.status == 'OK'
            a: Большое спасибо за вашу заявку! В ближайшее время с вами свяжется менеджер, чтобы обсудить детали вашего путешествия. Желаем отличного отдыха!
        else:
            a: К сожалению, при оформлении заявки возникла ошибка. Вы можете обратиться за подбором тура по телефону 8(812)000-00-00.
        go!: /Goodbye
