# * В001 2017-09-22 Новое от нашей команды: *
 1. Применив springboot "переделали проект в Web-сервис".
 2. В работе, надо добавить обработку URL (сейчас принимаются файлы с дисков).
 3. Устранена проблема с нумерацией списков - но есть проблема с вложенными списками, скорее всего не поддерживается в таком виде, лучше переписать на docx4j, что потребует дополнительных затрат времени. 
 4. Документы преобразуются в PDF (FOP) - но на стандартных шаблонах ЕСПД возникает проблема с русскими буквами в парсере.
 5,5,6. в работе, есть вопрос - предполагается набор документов в куче папок вложенных, и где-то в глубине может быть корневой? мы их скачиваем все в приложение и потом собираем/парсим.
 7. Ничего пока.
 8. Ничего пока.
 б1. Ничего пока.
 б2. Ничего пока.


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
5. При загрузке репозитория с документом должна быть возможность выбора корневого файла документа.
5. При выборе корневого файла документа должна быть возможность выбрать шаблон преобразования.
6. Корневой файл не обязательно должен располагаться в корневой папке репозитория с документом
7. Должна поддерживаться возможность параллельного формирования нескольких документов одновременно
8. Должны поддерживаться инлайн изображения (изображения вставленные в строку с текстом)

# Бонусные задачи

1. Добавить возможность добавления новых шаблонов без перезапуска сервиса.
2. Повысить скорость преобразования документов. Сейчас преобразование больших документов занимает порядка 40 секунд.
