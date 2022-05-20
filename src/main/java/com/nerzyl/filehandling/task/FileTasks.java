package com.nerzyl.filehandling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class FileTasks {
    @Autowired
    private FileTaskRepository repository;

    @Value("${app.folder.name}")
    private String mainFolder;

    @Scheduled(fixedRate = 5000)
    public void processFiles() throws IOException {
        File directory = new File(mainFolder);
        createFolders();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                Map<String, Integer> map = new HashMap<>();
                String fileName = file.getName();
                this.processDataFromFile(file, map);
                this.calculateResult(map);
                this.createReportFile(fileName, map);
                this.moveFileToProccesedFolder(fileName);
                this.saveDataToDatabase(map, fileName);
            }
        }
    }

    private void saveDataToDatabase(Map<String, Integer> map, String fileName) {
        FileTaskEntity taskEntity = FileTaskEntity
                .builder()
                .supply(map.get("supply") == null ? 0 : map.get("supply"))
                .buy(map.get("buy") == null ? 0 : map.get("buy"))
                .result(map.get("result"))
                .fileName(fileName)
                .build();
        log.info("Saving taskEntity to database {}", taskEntity);
        repository.save(taskEntity);
    }

    private void moveFileToProccesedFolder(String fileName) throws IOException {
        log.info("Processed file {} moving in {} folder", fileName, mainFolder + "processed\\");
        Files.move(
                Paths.get(mainFolder + fileName),
                Paths.get(mainFolder + "processed\\" + fileName),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private void calculateResult(Map<String, Integer> map) {
        int supply = map.get("supply") == null ? 0 : map.get("supply");
        int buy = map.get("buy") == null ? 0 : map.get("buy");
        int result = supply - buy;
        map.put("result", result);
        log.info("Calculated result: {}", result);
    }

    private void processDataFromFile(File file, Map<String, Integer> map) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (map.containsKey(data[0])) {
                map.put(data[0], map.get(data[0]) + Integer.parseInt(data[1]));
            } else {
                map.put(data[0], Integer.parseInt(data[1]));
            }
        }
        br.close();
        log.info("Processed data from file: {}", file.getName());
    }

    private void createReportFile(String fileName, Map<String, Integer> map) throws IOException {
        String reportFileName = "report_" + fileName;
        File report = new File(mainFolder + "reports\\" + reportFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(report));
        report.createNewFile();
        int supply = map.get("supply") == null ? 0 : map.get("supply");
        int buy = map.get("buy") == null ? 0 : map.get("buy");
        bw.write("supply," + supply);
        bw.newLine();
        bw.write("buy," + buy);
        bw.newLine();
        bw.write("result," + map.get("result"));
        bw.newLine();
        bw.close();
        log.info("Created report file with name = {}", reportFileName);
    }

    private void createFolders() throws IOException {
        log.info("Creating folders for reports and processed files");
        Path reportPath = Paths.get(mainFolder + "reports\\");
        Path processedPath = Paths.get(mainFolder + "processed\\");
        Files.createDirectories(reportPath);
        Files.createDirectories(processedPath);
    }
}
