package com.nerzyl.filehandling.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FileTasksTest {

    @Autowired
    private FileTasks underTest;

    @Value("${app.folder.name}")
    private String mainFolder;

    @BeforeEach
    void setUpFirstFile() throws IOException {
        String testFileName = "test1.csv";
        File testFile = new File(mainFolder + testFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
        testFile.createNewFile();
        bw.write("supply," + 2);
        bw.newLine();
        bw.write("buy," + 1);
        bw.newLine();
        bw.close();
    }

    @BeforeEach
    void setUpSecondFile() throws IOException {
        String testFileName = "test2.csv";
        File testFile = new File(mainFolder + testFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
        testFile.createNewFile();
        bw.write("supply," + 100);
        bw.newLine();
        bw.write("supply," + 200);
        bw.newLine();
        bw.write("supply," + 300);
        bw.newLine();
        bw.write("buy," + 50);
        bw.newLine();
        bw.write("buy," + 25);
        bw.newLine();
        bw.write("buy," + 25);
        bw.newLine();
        bw.close();
    }

    @BeforeEach
    void setUpThirdFile() throws IOException {
        String testFileName = "test3.csv";
        File testFile = new File(mainFolder + testFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
        testFile.createNewFile();
        bw.close();
    }

    @AfterEach
    void deleteFilesAfterTest() {
        String testFileName1 = "test1.csv";
        File testFile1 = new File(mainFolder + "processed\\" + testFileName1);
        File reportFile1 = new File(mainFolder + "reports\\report_" + testFileName1);

        String testFileName2 = "test2.csv";
        File testFile2 = new File(mainFolder + "processed\\" + testFileName2);
        File reportFile2 = new File(mainFolder + "reports\\report_" + testFileName2);

        String testFileName3 = "test3.csv";
        File testFile3 = new File(mainFolder + "processed\\" + testFileName3);
        File reportFile3 = new File(mainFolder + "reports\\report_" + testFileName3);

        testFile1.deleteOnExit();
        testFile2.deleteOnExit();
        testFile3.deleteOnExit();
        reportFile1.deleteOnExit();
        reportFile2.deleteOnExit();
        reportFile3.deleteOnExit();
    }

    @Test
    void itShouldProcessFiles() throws IOException {
        underTest.processFiles();

        String testFileName1 = "test1.csv";
        String testFileName2 = "test2.csv";
        String testFileName3 = "test3.csv";
        File reportFile1 = new File(mainFolder + "reports\\report_" + testFileName1);
        File reportFile2 = new File(mainFolder + "reports\\report_" + testFileName2);
        File reportFile3 = new File(mainFolder + "reports\\report_" + testFileName3);

        int result1 = findResultIntoFiles(reportFile1);
        int result2 = findResultIntoFiles(reportFile2);
        int result3 = findResultIntoFiles(reportFile3);

        assertThat(result1).isEqualTo(1);
        assertThat(result2).isEqualTo(500);
        assertThat(result3).isEqualTo(0);
    }

    private int findResultIntoFiles(File file) throws IOException {
        String line;
        int result = 0;
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if ("result".equals(data[0])) {
                result =  Integer.parseInt(data[1]);
            }
        }
        br.close();
        return result;
    }


}