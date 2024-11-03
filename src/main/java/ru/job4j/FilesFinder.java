package ru.job4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

/**
 * Основной класс для поиска файлов по заданным параметрам, таких как имя, маска или регулярное выражение.
 * Также включает методы для записи результатов поиска в файл.
 */
public class FilesFinder {
    /**
     * Выполняет поиск файлов в указанной директории с использованием условия фильтрации.
     *
     * @param root      путь к директории, где будет происходить поиск.
     * @param condition условие, которому должны соответствовать файлы.
     * @return список найденных файлов, соответствующих условию.
     * @throws IOException если возникает ошибка ввода-вывода при обходе файловой системы.
     */
    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        SearchFiles searcher = new SearchFiles(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

    /**
     * Преобразует маску поиска файлов в регулярное выражение.
     *
     * @param string строка с маской (например, "*.txt").
     * @return регулярное выражение, соответствующее маске.
     */
    public static String convertMaskToRegex(String string) {
        string = string.replace(".", "\\.")
                .replace('?', '.')
                .replace("*", ".*");
        return string;
    }

    /**
     * Валидирует параметры, необходимые для поиска файлов.
     *
     * @param argsForProgram объект с аргументами, содержащими ключи и значения параметров.
     * @throws IllegalArgumentException если один из параметров неверен.
     */
    public static void validationParameters(ArgsName argsForProgram) {
        File file = new File(argsForProgram.get("d"));
        String needFindFile = argsForProgram.get("n");
        String howToSearch = argsForProgram.get("t");
        String fileToWrite = argsForProgram.get("o");
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Not exist %s", file.getAbsoluteFile()));
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(String.format("Not directory %s", file.getAbsoluteFile()));
        }
        if (!"regex".equals(howToSearch)
                && !"mask".equals(howToSearch)
                && !needFindFile.matches("[A-Za-z0-9]*\\.[a-z0-9]{2,}$")) {
            throw new IllegalArgumentException(String.format("Incorrect search file format: %s", needFindFile));
        }
        if ("mask".equals(howToSearch) && !needFindFile.matches("[A-Za-z0-9?*]*\\.[a-z0-9?*]{2,}$")) {
            throw new IllegalArgumentException(String.format("Incorrect search file format: %s", needFindFile));
        }
        if (!"mask".equals(howToSearch) && !"name".equals(howToSearch) && !"regex".equals(howToSearch)) {
            throw new IllegalArgumentException(String.format("Incorrect third parameter: %s. "
                    + "This must be: \"mask\", \"name\" or \"regex\".", howToSearch));
        }
        if (!fileToWrite.matches("^.*\\.[a-z0-9]{2,5}$")) {
            throw new IllegalArgumentException(String.format("Incorrect file format: %s", fileToWrite));
        }
    }

    /**
     * Записывает результаты поиска в файл.
     *
     * @param path     путь к файлу, в который будут записаны результаты.
     * @param pathList список путей, найденных в ходе поиска.
     * @throws IOException если возникает ошибка при записи в файл.
     */
    public static void writeResultsToFile(String path, List<Path> pathList) throws IOException {
        String newPath = "data\\" + path;
        try (PrintWriter writer = new PrintWriter(new FileWriter(newPath, false))) {
            for (Path onePath : pathList) {
                writer.println(onePath.toString());
                System.out.println(onePath);
            }
        }
    }
}