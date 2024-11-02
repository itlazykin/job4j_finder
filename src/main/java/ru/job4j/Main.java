package ru.job4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.job4j.FilesFinder.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ArgsName argsForProgram = ArgsName.of(args);
        validationParameters(argsForProgram);
        List<Path> results = switch (argsForProgram.get("t")) {
            case "regex" -> search(Paths.get(argsForProgram.get("d")), path -> path.toFile()
                    .getName()
                    .matches(argsForProgram.get("n")));
            case "mask" -> {
                String regex = convertMaskToRegex(argsForProgram.get("n"));
                yield search(Paths.get(argsForProgram.get("d")), path -> path.toFile().
                        getName()
                        .matches(regex));
            }
            case "name" -> search(Paths.get(argsForProgram.get("d")), path -> path.toFile()
                    .getName()
                    .endsWith(argsForProgram.get("n")));
            default -> new ArrayList<>();
        };
        if (!results.isEmpty()) {
            writeResultsToFile(argsForProgram.get("o"), results);
        }
        System.out.println(results.size());
    }
}
