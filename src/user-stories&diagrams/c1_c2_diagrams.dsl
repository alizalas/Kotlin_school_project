workspace {
    !identifiers hierarchical

    model {
        user = person "Пользователь" "Человек, который хочет саморазвиваться"
        admin = person "Администратор" "Контролирует выполнение запросов и управляет системой и пользователями"
        system = softwareSystem "Система" "Система, которая обрабатывает запросы" {
            tg = container "Телеграм бот"
            bd = container "База данных" {
                tags "Database"
            }
            server = container "Сервер" ""

        }
        api = softwareSystem "API сторонних сервисов" "Получение информации о событиях и обновления"
        #
        #C1
        #
        user -> system "Регистрация"
        system -> admin "Сохранение информации о пользователе"
        admin -> system " Подтверждение сохранения данных"
        system -> user " Подтверждение регистрации"

        user -> system "Вход в систему"
        system -> admin "Проверка введенных данных"
        admin -> system "Подтверждение подлинности данных"
        system -> user "Вход"
        #
        #C2
        #
        user -> system.tg "Отправка данных для регистрации"
        system.tg -> admin "Сохранение информации о пользователe"
        admin -> system.tg "Подтверждение сохранения данных"
        system.tg -> system.bd "Запись нового пользователя"
        system.tg -> user " Подтверждение регистрации"

        system.server -> api "Обновление тем"
        user -> system.tg "Выбор темы"
        system.tg -> system.server "Запрос данных о теме"
        system.server -> api "Получение информации о теме"
        api -> system.tg "Передача информации о теме"
        system.tg -> user "Отправка информации о теме"

        user -> system.tg "Выбор кнопки"
        system.tg -> user "Выберите действие: [Начать курс], [Получить советы], [Задать вопрос]"

        user -> system.tg "Начать курс"
        system.tg -> system.server "Запрос на начало курса"
        system.server -> api "Получение информации о курсе"
        api -> system.tg "Передача информации о курсе"
        system.tg -> user "Отправка информации о курсе"

        user -> system.tg "Задать вопрос"
        system.tg -> user "Введите ваш вопрос"
        user -> system.tg "Вопрос пользователя"
        system.tg -> system.server "Обработка вопроса"
        system.server -> api "Получение ответа"
        api -> system.tg "Передача ответа"
        system.tg -> user "Отправка ответа на вопрос"


    }

    views {
        systemContext system "Diagram1" {
            include *
            autolayout lr
        }

        container system "Diagram2" {
            include *
            autolayout lr

        }

        styles {
            element "Print" {
                color #000000
            }
            element "Person" {
                background #ffffff
                shape person
            }
            element "Software System" {
                shape cylinder
                background #ffffff
            }
            element "Container" {
                background #ffffff
            }
            element "Database" {
                shape cylinder
            }
        }
    }

    configuration {
        scope softwaresystem
    }
}