# java-test-case
Конвертер документов Docbook в другие форматы

Данное приложение предназначено для преобразования документов Docbook версии 5 в другие форматы.

# Задание

1. Преобразовать приложение в web-сервис.
2. Добавить поддержку получения документов в формате Docbook из Git репозитория
(пример документа расположен в репозитории в папке с тестами)
3. Исправить ошибку, связанную с формированием нумерованных списков - в результирующем документе формата DOCX
все нумерованные списки продолжают сквозную нумерацию.
4. Добавить преобразование в PDF cпомощью XSLT трансформаций и Apache FOP процессора
(набор преобразований расположен в репозитории https://bitbucket.org/Lab50/espd-docbook5/src)
5. При загрузке репозитория с документом должна быть возможность выбора корневого файла.
6. Корневой файл не обязательно должен располагаться в корневой папке репозитория с документом
7. Должна поддерживаться возможность параллельного формирования нескольких документов одновременно
8. Должны поддерживаться инлайн изображения (изображения вставленные в строку с текстом)

# Бонусные задачи

1. Добавить возможность добавления новых шаблонов без перезапуска сервиса
