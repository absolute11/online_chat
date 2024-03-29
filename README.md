# Сетевой чат

## Сетевой чат на языке программирования Java

---
### **Описание проекта**
Проект "Сетевой чат" представляет собой систему для обмена текстовыми сообщениями между пользователями по сети с использованием консоли (терминала). Проект включает два основных приложения: сервер чата и клиент чата, которые обеспечивают подключение, отправку и получение сообщений, а также логирование чата в файл file.log.

### **Структура проекта**
**Server.java**

Server.java: Основной класс сервера, отвечает за ожидание подключений, обработку сообщений и управление чатом.

**Config.java**
Класс для работы с настройками сервера, загружаемыми из файла settings.txt. Включает методы для получения порта подключения и других настроек.

**Client.java**
Этот класс реализует клиентскую часть чата. Он подключается к серверу, отправляет и принимает сообщения. Каждое сообщение записывается в файл file.log

**ClientHandler**

Этот класс представляет обработчик клиента на сервере. Каждый экземпляр этого класса создается для обслуживания одного клиента. Он отвечает за чтение и отправку сообщений клиенту, обработку подключения и отключения клиента.

### **Система сборки**

Проект использует Maven в качестве системы сборки. Файл pom.xml содержит конфигурацию проекта, зависимости и плагины, включая необходимые для тестирования.

###  **Тестирование**
Для тестирования используются JUnit тесты. 

### **Инструкция по запуску**
1.Склонируйте репозиторий на свой компьютер.

2.Откройте терминал и перейдите в директорию проекта.

3.Запустите сервер

4.Запустите telnet и подключитесь к серверу.
   
5.Следуйте инструкциям на экране для ввода имени пользователя и подключения к чату.



