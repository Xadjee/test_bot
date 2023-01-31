require:requirements.sc

theme: /

    state: Start
        q!: $regex</start>
        a: Добрый день! Я виртуальный помощник банка «Рога и Копыта»!<br/>Чем могу помочь?
        
    #ШАГ2
    state: cardsgoal
        q: * $card *
        a:  Позвольте уточнить, для каких целей вам нужна карта:<br/>
            - оплата в магазинах;<br/>
            - снятие наличных;<br/>
            - зачисление социальных выплат или пенсии;<br/>
            - зачисление заработной платы;
            
        #ШАГ3        
        state: socialpayments
            q: * ({$socialpayments * $card} | $socialpayments) *
            a: В соответствии с законодательством (161 ФЗ) выплаты должны осуществляться на карты национальной платежной системы "МИР".
               Банк «Рога и Копыта» предлагает клиентам карты системы (МИР) типов.<br/>
    
               Народная карта МИР. Ознакомиться с условиями можно по ссылке: https://www.example.com<br/>
    
               Универсальная карта МИР. Ознакомиться с условиями можно по ссылке: https://www.<br/>
               example.com    
            go!:/areyoureadytoordercard
            
        #ШАГ4
        state: pension
            q: * ({$pension * $card} | $pension) *
            a: В соответствии с законодательством (161 ФЗ) зачисление пенсии должно осуществляться на карты национальной платежной системы «МИР». Банк«Рога и Копыта» предлагает своим клиентам выгодные условия по Пенсионной карте «МИР». Ознакомиться с тарифами можно по ссылке: https://www.example.com

        #ШАГ5    
        state: whatisimportant
            q: * ({$whatisimportant * $card} | $whatisimportant) *
            a: Укажите, что для вас важнее:<br/>
               -кешбэк;<br/> 
               -начисление процентов на остаток;<br/>
               -повышенный лимит снятия наличных;<br/>
    
            #ШАГ6
            state: cashback
                q: * ({$cashback * $card} | $cashback) *
                a: Предлагаю рассмотреть Универсальную карту (MasterCard/ VISA). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                go!: /cardsgoal/whatisimportant/step7
                
            #ШАГ7
            state: step7
                a: Также предлагаю рассмотреть альтернативный вариант - Премиальная карта (MasterCard Premium/VISA Premium). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                go!: /areyoureadytoordercard
            
            #ШАГ8
            state: cashwithdrawal
                q: * ({$cashwithdrawal * $card} | $cashwithdrawal) *
                a: На каждом тарифном плане установлены лимиты на снятие собственных денежных средств без комиссии в месяц. Укажите, какие лимиты вас интересуют:<br/>
                   - снятие до 250 000 руб. в месяц без комиссии;<br/>
                   - снятие до 500 000 руб. в месяц без комиссии;<br/>
                   - снятие 1 000 000 руб. в месяц без комиссии.

                #ШАГ9
                state: 250
                    q: * {$250} *
                    a: Предлагаю рассмотреть Универсальную карту (MasterCard/ VISA). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                    go!: /cardsgoal/whatisimportant/cashwithdrawal/step10
                        
                #ШАГ10
                state: step10
                    a: Также предлагаю рассмотреть альтернативный вариант - Универсальная карта «МИР». Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                    go!: /areyoureadytoordercard
                    
                #ШАГ11
                state: 500
                    q: * {$500} *
                    a: Предлагаю рассмотреть Премиальную карту (MasterCard Premium/VISA Premium). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                    go!: /cardsgoal/whatisimportant/cashwithdrawal/step12
                    
                #ШАГ12
                state: step12
                    a: Также предлагаю рассмотреть альтернативный вариант - Эксклюзивная карта (VISA Platinum). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                    go!: /areyoureadytoordercard
                    
                #ШАГ13
                state: million
                    q: * {$million} *
                    a: Предлагаю рассмотреть Эксклюзивную карту (VISA Platinum). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                    go!: /cardsgoal/whatisimportant/cashwithdrawal/step14
                    
                #ШАГ14
                state: step14
                    a: ШАГ14<br/>Также предлагаю рассмотреть альтернативный вариант - Премиальная карта (MasterCard Premium/VISA Premium). Ознакомиться с условиями можно на нашем официальном сайте: https://www.example.com
                    go!: /areyoureadytoordercard    
                   
            #ШАГ15        
            state: freeservice
                q: * ({$freeservice * $card} | $freeservice) *
                a: Банк «Рога и Копыта» предлагает клиентам карты системы «МИР» нескольких типов:
                   - Народная карта «МИР». Ознакомиться с условиями можно по ссылке: https://www.example.com
                   - Универсальная карта «МИР». Ознакомиться с условиями можно по ссылке: https://www.example.com
                go!: /cardsgoal/whatisimportant/freeservice/step16 
                
                #ШАГ16        
                state: step16
                    a: ШАГ16<br/>Если вы являетесь пенсионером, то предлагаю рассмотреть Пенсионную карту «МИР». Ознакомиться с тарифами можно на сайте: https://www.example.com
                    go!: /areyoureadytoordercard   
    

        
    state: areyoureadytoordercard
        a: Вы готовы заказать карту?

        #ШАГ17      
        state: step17
            q: * (да|даа|дааа*|конечно*) *
            a: Я предлагаю оформить заявку на сайте нашего банка: https://www.example.com<br/>
               Альтернативным способом заказа является посещение офиса.<br/>
               Благодарим за обращение в банк «Рога и Копыта»!
            script:
                $jsapi.stopSession();
                
    
        state: questions
            q: * (не|нет|нету|неа) *
            a: У вас остались вопросы?
            
            state: operator
                q: * (да|даа|дааа*|конечно*) *
                a: Соединяю с оператором
                script:
                    $response.replies.push({
                        "type": "switch",
                        "phoneNumber": "79123456789",
                        "continueCall": true,
                        "continueRecording": true
                    });

        #ШАГ18     
        state: step18
            q!: * (не|нет|нету|неа) *
            a: Благодарим за обращение в банк «Рога и Копыта»!
            script:
                $jsapi.stopSession();
            
    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Не смог разобрать :( Попробуете сказать иначе?
            a: Простите, я не понял вас. Давайте попробуем еще раз?
            a: Извините, я не понял. Попробуйте сформулировать по-другому        