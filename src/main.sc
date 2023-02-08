patterns:
    $offers = (порекоменд*/посовет*/предл*/помо*)
    $wish = (жела*/хотел*/хоч*/помо*)
    $lunch = (обед*/ланч*/лэнч*/ленч*/суп*)
    $dish = (блюд*/еду/еды/еда/есть/кушать/поесть/покуш*/кухн*)
    $menu = (меню*/миню*)
    $fishMenu = (рыб*/поке)
    $cooking = (готовите/подаете/подаёте)
    $pronoun = (что[-то]/какое[-то]/какие[-то])
    $cheeseMenu = (сыр*/чизкейк*)
    $vegeterianMenu = (гриб*/овощ*/фрукт*/вьетнам*)
    $meatMenu = (мяс*/стейк*/остр*)
    $no = (не/нет/непереносимость/нельзя/без)
theme: /

    state: Start
        q!: $regex</start>
        a: Добро пожаловать в наш ресторан, я виртаульный помощник, готов помочь вам сделать заказ!
        
        
        
        state: Menu
            a: Что вы предпочитаете:\n
                - Кухня для вегетеранцев\n
                - Блюда из мяса\n
                - Супы\ланчи\n
                - Рыбное меню\n
                - Закуски из сыра
        
        state: Ans_vegeterian
            a: У нас есть вегетерианские блюда:\n
                - блюдо 1\n
                - блюдо 2\n
                - блюдо n
                
        state: Ans_meat
            a: У нас есть блюда из мяса:\n
                - блюдо 1\n
                - блюдо 2\n
                - блюдо n
                
        state: Ans_fish
            a: У нас есть рыбное меню:\n
                - блюдо 1\n
                - блюдо 2\n
                - блюдо n
                
        state: Ans_cheese
            a: У нас есть закуски из сыра:\n
                - блюдо 1\n
                - блюдо 2\n
                - блюдо n
                
        state: Ans_soup
            a: У нас есть богатое меню супов и ланчей:\n
                - блюдо 1\n
                - блюдо 2\n
                - блюдо n
                
        state: react_wantSomething
            # блюдо порекомендуйте пожалуйста
            # помоги выбрать что покушать
            # посоветуй что можно поесть
            # порекомендуй что-нибудь из меню
            q: * {[$offers | $wish] * ($dish | $menu)} *
            go!: /Start/Menu
            
        state: react_wantSoup
            # помоги выбрать обед для ребенка
            # посоветуй блюдо для ланча
            q: * {[$offers | $wish] * $lunch * [$dish | $menu | $cooking]} *
            # что из супов есть в меню?
            q: * {[$pronoun] * $lunch * [$menu | $cooking]} *
            go!: /Start/Ans_soup
            
        state: react_wantMeat
            # есть что-то острое?
            q: * {[$offers | $wish | $pronoun] * $meatMenu * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_meat
            
        state: react_wantFish
            # поке подаете?
            # рыбу хочется поесть
            # у вас с рыбой блюда есть?
            # хотелось бы что-то из красной рыбы
            q: * {[$offers | $wish] * $fishMenu * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_fish
            
        state: react_wantVegeterian
            # посоветуй еду из овощей
            # с грибами порекомендуйте что-нибудь
            # вьетнамская кухня есть?
            q: * {[$offers | $wish] * $vegeterianMenu * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_vegeterian
            
        state: react_wantCheese
            # хочу съесть какое-то блюдо с сыром
            q: * {[$offers | $wish | $pronoun] * $cheeseMenu * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_cheese
            
        state: react_noMeat
            # я не ем мяса
            q: {[$offers | $wish] * $no *  $meatMenu * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_vegeterian
        state: react_noFish
            # есть ли блюда без рыбы?
            # рыбу мне нельзя
            q: *  {[$offers | $wish] * $no *  $fishMenu * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_meat
        state: react_noCheese
            # у меня непереносимость лактозы
            q: *  {[$offers | $wish] * $no *  ($cheeseMenu/лактоз*) * [$dish | $menu | $cooking]} *
            go!: /Start/Ans_fish




