package com.example.javaf_phase4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AddConcertController {

    @FXML
    private ComboBox comboBox1;

    @FXML
    private ComboBox comboBox2;

    @FXML
    private TextField event_id;
    @FXML
    private TextField description;
    @FXML
    private TextField duration;
    @FXML
    private TextField start_date;
    @FXML
    private TextField performanser_name;


    public void comboBox2Selected(ActionEvent event) throws IOException, ParseException {

        HttpURLConnection connection = (HttpURLConnection)(new URL("http://localhost:8080/addconcert")).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        String festival_id = this.comboBox1.getValue().toString().split(",")[0];
        String festival_name = this.comboBox1.getValue().toString().split(",")[1];
        String festival_place = this.comboBox1.getValue().toString().split(",")[2];

        JSONObject festival = new JSONObject();
        festival.put("festivalId", festival_id);
        festival.put("festivalName", festival_name);
        festival.put("festivalPlace",festival_place);
        System.out.println(festival.toJSONString());



        String festivalrun_id = this.comboBox2.getValue().toString().split(",")[0];
        String festivalrun_duration = this.comboBox2.getValue().toString().split(",")[2];
        String festivalrun_date = this.comboBox2.getValue().toString().split(",")[1];

        JSONObject festivalrun = new JSONObject();
        festivalrun.put("festivalRunId", festivalrun_id);
        festivalrun.put("duration", festivalrun_duration);
        festivalrun.put("festivalDate",festivalrun_date);
        festivalrun.put("festival",festival);
        System.out.println(festivalrun.toJSONString());


        JSONObject concert = new JSONObject();
        concert.put("event_id", this.event_id.getText());
        concert.put("description", this.description.getText());
        concert.put("duration",this.duration.getText());
        concert.put("start_date",this.start_date.getText());
        concert.put("performer_name",this.performanser_name.getText());
        concert.put("festivalRun",festivalrun);
        System.out.println(festivalrun.toJSONString());


        try {
            OutputStream os = connection.getOutputStream();
            Throwable var7 = null;

            try {
                byte[] input = concert.toJSONString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (Throwable var17) {
                var7 = var17;
                throw var17;
            } finally {
                if (os != null) {
                    if (var7 != null) {
                        try {
                            os.close();
                        } catch (Throwable var16) {
                            var7.addSuppressed(var16);
                        }
                    } else {
                        os.close();
                    }
                }

            }
        } catch (IOException var19) {
            var19.printStackTrace();
        }


        String response = "";
        int responsecode = connection.getResponseCode();
        if (responsecode == 200) {
            Scanner scanner;
            for(scanner = new Scanner(connection.getInputStream()); scanner.hasNextLine(); response = response + scanner.nextLine()) {
            }

            scanner.close();
        }

        System.out.println(response);


    }


    public void comboBox1Selected(ActionEvent event) throws IOException, ParseException {


        String str= (String) comboBox1.getValue();
        List<String> tempList = Arrays.asList(str.split(","));

        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/getallfestivalruns/"+tempList.get(0)+"").openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }




        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray)parser.parse(response);

        for (int i = 0; i < array.size(); i++) {
            String record="";


            try{
                JSONObject object = (JSONObject)array.get(i);
                record = record + object.get("festivalRunId") + "," + object.get("festivalDate") + "," + object.get("duration");


            }catch(Exception var14){

            }
            System.out.println(record);
            comboBox2.getItems().add(record);
        }




    }



    public void initialize() throws IOException, org.json.simple.parser.ParseException {

        String response = "";
        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/getallfestivals").openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }



        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray)parser.parse(response);

        for (int i = 0; i < array.size(); i++) {
            String record="";


            try{
                JSONObject object = (JSONObject)array.get(i);

                record = record + object.get("festivalId") + "," + object.get("festivalName") + "," + object.get("festivalPlace");

            }catch(Exception var14){

            }
            System.out.println(record);
            comboBox1.getItems().add(record);
        }

    }

}
