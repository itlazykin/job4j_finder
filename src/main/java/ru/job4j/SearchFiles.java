package ru.job4j;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Класс SearchFiles используется для обхода файловой системы и поиска файлов,
 * соответствующих определённому условию.
 * <p>
 * Наследуется от {@link SimpleFileVisitor} и переопределяет метод {@code visitFile}
 * для добавления файлов в список, если они соответствуют заданному условию.
 * </p>
 */
public class SearchFiles extends SimpleFileVisitor<Path> {
    private final Predicate<Path> condition;
    private final List<Path> pathsList = new ArrayList<>();

    /**
     * Конструктор класса, принимающий условие для фильтрации файлов.
     *
     * @param condition условие, которому должны соответствовать файлы.
     */
    public SearchFiles(Predicate<Path> condition) {
        this.condition = condition;
    }

    /**
     * Возвращает список файлов, которые соответствуют условию поиска.
     *
     * @return список файлов, удовлетворяющих условию.
     */
    public List<Path> getPaths() {
        return pathsList;
    }

    /**
     * Метод, вызываемый при посещении файла. Добавляет файл в список,
     * если он удовлетворяет условию.
     *
     * @param file       путь к файлу
     * @param attributes атрибуты файла
     * @return результат продолжения обхода файловой системы
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
        if (condition.test(file)) {
            pathsList.add(file);
        }
        return CONTINUE;
    }
}