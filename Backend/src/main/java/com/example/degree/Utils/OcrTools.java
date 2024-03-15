package com.example.degree.Utils;


import com.example.degree.Entity.County;
import com.example.degree.Entity.Prefix;
import com.example.degree.Response.CompleteName;
import com.example.degree.Response.PartPatientInfo;
import com.example.degree.Response.PatientInfo;
import com.example.degree.Service.CountyService;
import com.example.degree.Service.PrefixeService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class OcrTools {

    @Autowired
    private CountyService countyService;

    @Autowired
    private PrefixeService prefixeService;

    static {
        OpenCV.loadShared();
    }
    public  String extractString(MultipartFile file) {
        try {
            Path tempFilePath = null;
            tempFilePath = Files.createTempFile("temp", file.getOriginalFilename());
            File tempFile = tempFilePath.toFile();
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            // Read the image data from the temporary file into a Mat object
            Mat image = Imgcodecs.imread(tempFile.getAbsolutePath());

            // Calculate the dimensions of the rectangle to be extracted
            int width = (int) (105 * image.cols() / 210 * 1.6); // Multiply by scaling factor of 1.1
            int height = (int) (75 * image.rows() / 297 * 1.6); // Multiply by scaling factor of 1.1

            // Define the ROI (Region of Interest) as a rectangle
            int x = (image.cols() - width) / 2;
            int y = (image.rows() - height) / 2;
            Rect roi = new Rect(x, y, width, height);

            // Extract the ROI from the original image
            Mat cropped_image = new Mat(image, roi);

            // Display the cropped image
            Imgcodecs.imwrite("cropped_image.jpeg", cropped_image);

            File imageFile = new File("cropped_image.jpeg");
            ITesseract tesseract = new Tesseract();

            try {
                // Set the path to the tessdata directory containing the language data files
                tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

                // Set the language to use for OCR (English in this example)
                tesseract.setLanguage("ron");

                // Perform OCR on the cropped image
                String result = tesseract.doOCR(imageFile);

                // Print the OCR result
                tempFile.delete();

                return result;
            } catch (TesseractException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Error";
    }
    public PatientInfo extractInfo(MultipartFile file){
        String textFromSocialId = extractString(file);
        String lastRows = subtractSection(textFromSocialId);
        if(lastRows != null){
            return analyzeInfo(lastRows);
        }
        return null;
    }

    private PatientInfo analyzeInfo(String lastRows) {
        if (Character.isLowerCase(lastRows.charAt(39))) {
            lastRows =  lastRows.substring(0, 39) + lastRows.substring(40);
        }

        System.out.println(lastRows);
        String[] rows = lastRows.split("\n");
        List<String> rowList = Arrays.asList(rows);
        CompleteName completeName =  extractName(rowList.get(0));

        PartPatientInfo partPatientInfo = extractPartialInfo(rowList.get(1));
        return PatientInfo
                .builder()
                .socialId(partPatientInfo.getSocialId())
                .age(partPatientInfo.getAge())
                .county(partPatientInfo.getCounty())
                .gender(partPatientInfo.getGender())
                .name(completeName.getName().substring(0,1).toUpperCase() + completeName.getName().substring(1).toLowerCase())
                .surname(completeName.getSurname().substring(0,1).toUpperCase() + completeName.getSurname().substring(1).toLowerCase())
                .build();
    }
    private  PartPatientInfo extractPartialInfo(String s) {
        String prefix = s.substring(0,2);

        String temp = s.substring(13);

        String birthYearSocialId = temp.substring(0,6);

        String yearString = birthYearSocialId.substring(0,3);

        int fullYear = calculateFullYear(yearString);
        temp = temp.substring(7);

        String gender = calculateGender(temp);

        String firstDigit = firstDigit(fullYear,gender);

        temp = temp.replaceAll("\\D", "");
        String last6DigitsSocialId = temp.substring(Math.max(temp.length() - 7, 0)).substring(0,6);


        String socialId = firstDigit + birthYearSocialId + last6DigitsSocialId;


        String age = calculateAge(birthYearSocialId);

        Prefix prefix1 = prefixeService.getPrefixFromName(prefix);

        County county = countyService.getCountyFromPrefix(prefix1);
        return PartPatientInfo
                .builder()
                .age(age)
                .county(county.getName())
                .socialId(socialId)
                .gender(gender)
                .build();
    }
    private  CompleteName extractName(String firstRow){
        String temp = firstRow.substring(5);
        int indexFirstName = 0 ;
        for (Character ch: temp.toCharArray()) {
            if(ch.equals('<')){
                indexFirstName = temp.indexOf(ch);
                break;
            }
        }
        String surname = temp.substring(0,indexFirstName);
        String temp1 = temp.substring(indexFirstName).replaceAll("[a-z]", "").replaceAll("<", " ");
        int index = 0;
        for (int i = 0; i < temp1.length(); i++) {
            if (!Character.isWhitespace(temp1.charAt(i))) {
                index = i;
                break;
            }
        }
        String name = temp1.substring(index, temp1.indexOf("  ", index)).trim();
        return CompleteName.builder().name(name).surname(surname).build();
    }
    private  String subtractSection(String textFromSocialId) {
        String pattern = "IDROU";
        int startIndex = textFromSocialId.indexOf(pattern);
        if (startIndex != -1) {
            return textFromSocialId.substring(startIndex); // Output: "ROUBALTEANU<<ANDREI <<< <ceceececeee MZ923228<1R0U000711 33M270113852267497"
        } else {
            return null;
        }
    }
    private String calculateAge(String birthYearSocialId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate birthDate = LocalDate.parse(birthYearSocialId, formatter);
        LocalDate currentDate = LocalDate.now();
        return String.valueOf(ChronoUnit.YEARS.between(birthDate, currentDate));
    }
    private String firstDigit(Integer fullYear, String gender){
        if (fullYear < 2000 && gender.equals("Male")) {
            return  "1";
        }
        else if(fullYear < 2000 && gender.equals("Female"))  {
            return  "2";
        }
        else if(fullYear >= 2000 && gender.equals("Female"))  {
            return  "6";
        }
        else {
            return "5";
        }
    }
    private int calculateFullYear(String yearString){
        int year = Integer.parseInt(yearString);
        if (year == 0) {
            return  2000; // special case for year 2000
        } else if (year <= 23) {
            return   2000 + year; // birth year in 21st century
        } else {
            return   1900 + year; // birth year in 20th century
        }
    }
    private String calculateGender(String temp){
        if(temp.charAt(0) == 'M'){
            return   "Male";
        }
        else {
            return  "Female";
        }
    }
}
