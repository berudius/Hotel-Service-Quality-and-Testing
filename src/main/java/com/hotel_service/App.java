package com.hotel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

// 1.Реєстрацію користувача 
// 2.Авторизацію користувача
// 3.Перегляд доступних готельних номерів, транспортних послуг та спа-процедур.
// 4.Перегляд деталей про кожну послугу
// 5.Бронювання доступних готельних номерів, транспортних послуг та спа-процедур, включаючи можливість обрати годину запису на спа та годину прибуття транспорту.
// 6.Карту відстеження транспорту:
// 7.Можливість оплати онлайн
// 8.Можливість переглянути історію бронювань попередніх послуг
// 9.Можливість збереження послуг у особийтий список уподобань
// 10.Персоналізовані пропозиції та знижки
// 11.Можливість обробки запитів клієнтів працівниками готелю
// 12.Модерацію контенту/послуг/акцій тощо адміністраторами та модераторами
// 13.Можливість залишити відгук
// 14.Можливість зв’язатися із тех-підтримкою
// 15.Наявність push-сповіщень
//
//Форма реєстрації користувача
//Форма входу користувача
//Валідація введених даних
//Механізм "Забули пароль"
//Обробка помилок автентифікації
//Вивід списку доступних готельних номерів
//Вивід списку транспортних послуг
//Вивід списку спа-процедур
//Вивід списку страв, пропонованих рестораном готелю
//Фільтрація та пошук по послугах
//Авторизація через соціальні мережі (Google, Facebook)
//Перегляд розширених деталей кожної послуги
//Форма бронювання послуги з вибором дати та часу
//Підтвердження бронювання
//Перегляд статусу бронювання
//Зміна чи скасування бронювання
//Вибір мови інтерфейсу
//Інтеграція з картою
//Виведення місця розташування транспорту
//Оновлення інформації в реальному часі
//Push-сповіщення про підтвердження бронювання зі сторони працівників
//Push-сповіщення про зміну статусу бронювання
//Історія оплат
//Збереження та відображення історії бронювань на сторінці користувача
//Збереження та відображення вподобаних бронювань
//
//Вивід блоку "Рекомендовано для вас"
//Знижки для постійних клієнтів
//Сповіщення про знижки чи нові пропозиції
//Можливість залишити відгук та оцінку послузі
//Перегляд відгуків інших користувачів
//Сортування відображених відгуків
//Можливість адміністратора відповісти на відгук
//Чат підтримки
//Контакти підтримки
//Інтеграція з платіжною системою
//Формування рекомендацій на основі пошуку, вподобаних послуг та історії бронювань
