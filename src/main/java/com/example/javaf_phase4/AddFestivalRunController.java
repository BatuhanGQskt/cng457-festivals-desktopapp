package com.example.javaf_phase4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Adding festival run using the JavaFX which
 * can add festival run to a festival.
 */
public class AddFestivalRunController {
    /**
     * used to get festivalRun
     */
    @FXML
    private ComboBox festivalRunComboBox;

    /**
     * used to get festival run id
     */
    @FXML
    private TextField fesivalRunIdTextField;

    /**
     * used to get duration from interface
     */
    @FXML
    private TextField durationTextField;

    /**
     * used to get date from interface
     */
    @FXML
    private TextField dateTextField;

    /**
     * Add festival run using the interface when the button has been clicked. Gets the event to proceed the
     * action performed by the interface.
     * @param event proceed the action performed
     * @throws IOException I/O handler for URL read write
     */
    public void addFestivalRun(ActionEvent event) throws IOException {
        //Establish connection
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/addfestivalrun").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setDoInput(true); //Set the DoInput flag to true if you intend to use the URL connection for input
        connection.setDoOutput(true);

        //Gets festival id to add the festival run to it.
        String festivalid = festivalRunComboBox.getValue().toString().split("-")[0];

        System.out.println(festivalid);

        JSONObject festivalRun = new JSONObject();

        //Put information from interface to JSONObject
        System.out.println(fesivalRunIdTextField.getText().toString());
        festivalRun.put("festivalRunId", Integer.parseInt(fesivalRunIdTextField.getText()));
        festivalRun.put("duration", Integer.parseInt(durationTextField.getText()));
        festivalRun.put("festivalDate", dateTextField.getText());

        JSONObject festival = new JSONObject();
        festival.put("festivalId", Integer.parseInt(festivalid));
        festivalRun.put("festival",festival);


        System.out.println(festivalRun.toJSONString());

        //Trying to write the JSONObject to the database.
        try(OutputStream os = connection.getOutputStream()){
            byte[] input = festivalRun.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        String response = "";
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
        }

        System.out.println(response);
    }

    /**
     * Going back to the start view.
     * @param event proceed the action performed
     * @throws IOException I/O handler for URL read write
     */
    public void backPressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setTitle("Hello View");
        s.setScene(new Scene(root, 500, 300));
        s.show();
    }


    /**
     * initializes the comboBox values using the URL connection from the DB.
     * @throws IOException I/O handler for URL read write
     * @throws ParseException
     */
    public void initialize() throws IOException, ParseException {
        System.out.println("I have called");

        String response = "";
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/getallfestivals").openConnection();
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
        JSONArray array = (JSONArray) parser.parse(response);
        for(int i=0; i<array.size(); i++) {
            try {
                JSONObject temp = (JSONObject) array.get(i);
                festivalRunComboBox.getItems().add(temp.get("festivalId") +  "-" + temp.get("festivalName"));
                System.out.println(temp.get("festivalName"));
            } catch(Exception e){
                String response2 = "";
                HttpURLConnection connection2 = (HttpURLConnection)new URL("http://localhost:8080/getfestival/" + array.get(i).toString()).openConnection();
                connection2.setRequestMethod("GET");
                int responsecode2 = connection2.getResponseCode();
                if(responsecode2 == 200){
                    Scanner scanner = new Scanner(connection2.getInputStream());
                    while(scanner.hasNextLine()){
                        response2 += scanner.nextLine();
                    }
                    scanner.close();
                }
                JSONObject object = (JSONObject) parser.parse(response2);
                festivalRunComboBox.getItems().add(object.get("festivalName"));
            }
        }

    }
}
