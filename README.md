<strong>Tavrz CMS</strong> (collateral management system).
Система управления залоговым обеспечением предназначена для повышения качества работы залоговых подразделений кредитных организаций. В данной системе ведется учет предметов залога и всех необходимых объектов (Кредитные договоры, договоры залога, обременения и тд.)
Система (на текущий момент) состоит из трех микросервисов:
<ol>
<li><a href="https://github.com/Dmitriy1Fokin/tavrzCMS_api">TavrzCMS_api</a> – основной сервис, предоставляющий базовый функционал;</li>
<li><a href="https://github.com/Dmitriy1Fokin/tavrzCMS_auditCollateral">TavrzCMS_audit</a> – сервис, предоставляющий проведения аудита залогового портфеля;</li>
<li><a href="https://github.com/Dmitriy1Fokin/tavrzCMS_tl">TavrzCMS_tl</a> – простой веб интерфейс для работы с вышеуказанными сервисами;</li>
<li>...</li>
</ol>
Также для работы микросервисов необходим <a href="https://github.com/Dmitriy1Fokin/tavrzCMS_server">TavrzCMS_server</a>. Он служит для взаимодействия сервисов между собой (реестр сервисов).<br><br>
Стек технологий: Java 11, PostgreSQL 10, MongoDB, Spring(Boot, Security, Data Jpa, Data mongodb, Data jdbc, Web, Eureka), Thymeleaf, RabbitMQ, Flyway, Swagger.<br><br>
<strong>TavrzCMS_api</strong>:
Сервис для учета/хранения данных о залоговом портфеле. 
Основные сущности:
<ul>
<li>Сотрудник залогового подразделения;</li>
<li>Клиентский менеджер;</li>
<li>Клиент (Залогодатель/Заемщик);</li>
<li>Кредитный договор;</li>
<li>Договор залога;</li>
<li>Предмет залога (транспорт, оборудование, земельные участки, помещения/здания/сооружения, ТМЦ, ценные бумаги, судна;</li>
<li>Обременения предмета залога;</li>
<li>Договоры страхования;</li>
<li>История мониторингов;</li>
<li>История стоимостей.</li>
</ul>

Более подробную информацию можно посмотреть в сваггере после старта сервиса (http://localhost:8080/swagger-ui.html#/).

# Инструкция по запуску:
<ol>
<li>Запускаем <a href="https://github.com/Dmitriy1Fokin/tavrzCMS_server">TavrzCMS_server</a> (mvn package --> java –jar file.jar).</li>
<li>Запускаем базы данных и rabbitMQ (все на дефолтных портах).
<ol>
<li>Postgres: user: postgres, password: postgres, db name: tavrzdb, db name: tavrz_sec.</li>
<li>Mongo: db name: auditCollateralDB.</li></li>
</ol>
<li>Запускаем TavrzCMS_api (mvn package --> java –jar file.jar). При старте запустится flyway и инициализирует бд и данные в бд. </li>
<li>Запускаем <a href="https://github.com/Dmitriy1Fokin/tavrzCMS_auditCollateral">TavrzCMS_audit</a> (mvn package --> java –jar file.jar). </li>
<li>Запускаем <a href="https://github.com/Dmitriy1Fokin/tavrzCMS_tl">TavrzCMS_tl</a> (mvn package --> java –jar file.jar). При старте запустится flyway и инициализирует бд и данные в бд. <strong>ВАЖНО!</strong> Для работы в сервисе необходимо авторизоваться. Всего 12 пользователей: user1, user2 … user12. У всех пароль: pass.</li>
</ol>


Порты при запуске всех сервисов:<br>
TavrzCMS_server – 8761;<br>
TavrzCMS_api – 8080;<br>
TavrzCMS_audit – 7979;<br>
TavrzCMS_tl - 7878.<br>
