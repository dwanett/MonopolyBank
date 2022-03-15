# Описание
Программа MonopolyBank является собственным проектом в рамках обучения Java.
Она реализует банк для игры Монополия. Издание "Здесь и сейчас!".
# Как запустить MonopolyBank
Для работы программы необходимо установить `JDK 17` (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
<br/>
Можно запустить файл `MonopolyBank.jar` из репозитория или скомпилировать заново.<br/>
**Для компиляции**<br/>
Небходимо установить maven. Инструкция по установке (https://www.baeldung.com/install-maven-on-windows-linux-mac)
<br/>
В корне репозитория ввести
```
mvn install
```
# Возможности
# Выбор числа игроков<br/>
![](https://github.com/dwanett/MonopolyBank/blob/master/forreadme/number.png)<br/>
# Выбор имен игроков<br/>
![](https://github.com/dwanett/MonopolyBank/blob/master/forreadme/name.png)<br/>
# Покупать активы<br/>
![](https://github.com/dwanett/MonopolyBank/blob/master/forreadme/rootWindow.png)<br/>
# Покупать дома или отели<br/>
![](https://github.com/dwanett/MonopolyBank/blob/master/forreadme/buyHome.png)<br/>
# Обмениваться активами<br/>
![](https://github.com/dwanett/MonopolyBank/blob/master/forreadme/swap.png)<br/>
# Платить ренту<br/>
![](https://github.com/dwanett/MonopolyBank/blob/master/forreadme/payRent.png)<br/>
# Дополнительные функции
**1) Пополнение денег игрока**<br/>
**2) Снятие денег с игрока**<br/>
**3) Покупка за актива за введенное количество денег**<br/>
**4) Меню "Шанс"**<br/>
На данный момент имеет действие для одной карточки (Получить с каждого игрока по 100_000).<br/>
**5) Кнопака "Ход"**<br/>
На данный момент недоработана. При введенном количестве очков может купить улицу, на которую указывает ваша текущая позиция + выпавшее количество очков.
А также взимать ренту с игрока, если улица принадлежит другому игроку. (Возможно неопределенное поведение!!!)<br/>
