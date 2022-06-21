package com.example.javaf_phase4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class control statistic panel of gui
 */
public class StatisticsController {


    @FXML
    private CheckBox showlongestconcerts;
    @FXML
    private CheckBox showpopularfestivals;
    @FXML
    private  ListView<String> popularfestivalslist;
    @FXML
    private  ListView<String> longestconcertslist;

    /**
     * This is thread class of Popular Concerts
     */
    public class ConcertThread  implements Runnable {
        /**
         * This run method add longest concerts into concert list
         */
        @Override
        public void run()  {
            String response = "";
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL("http://localhost:8080/longestconcerts").openConnection();
                System.out.println("BOOOOOOOOOOOOOOOO");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
            int responsecode = 0;
            try {
                responsecode = connection.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(responsecode == 200){
                Scanner scanner = null;
                try {
                    scanner = new Scanner(connection.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                while(scanner.hasNextLine()){
                    response += scanner.nextLine();
                }
                scanner.close();
            }

            System.out.println(response);



            JSONParser parser = new JSONParser();
            JSONArray array = null;
            try {
                array = (JSONArray)parser.parse(response);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < array.size(); i++) {
                String record="";


                try{

                    if (array!=null) {
                        JSONObject object = (JSONObject) array.get(i);
                        record = record + object.get("event_id") + "," + object.get("description") + "," + object.get("duration") + "," + object.get("start_date") + "," +  object.get("performer_name") +"," + object.get("festivalRun")  ;
                        if (record != "" ){
                            longestconcertslist.getItems().add(record);
                        }
                    }



                }catch(Exception var14){

                }
                System.out.println(record);

            }
        }
    }

    /**
     * This is popular festival threa
     */
    public class FestivalThread  implements Runnable {
        /**
         * This run method add most popular festivals into concert list
         */
        @Override
        public void run()  {
            String response = "";
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL("http://localhost:8080/popularfestivals").openConnection();
                System.out.println("BOOOOOOOOOOOOOOOO1");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
            int responsecode = 0;
            try {
                responsecode = connection.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(responsecode == 200){
                Scanner scanner = null;
                try {
                    scanner = new Scanner(connection.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                while(scanner.hasNextLine()){
                    response += scanner.nextLine();
                }
                scanner.close();
            }

            System.out.println(response);



            JSONParser parser = new JSONParser();
            JSONArray array = null;
            try {
                array = (JSONArray)parser.parse(response);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < array.size(); i++) {
                String record="";


                try{
                    JSONObject object = (JSONObject)array.get(i);

                    record = record + object.get("festivalName") ;
                    if (record!=""){
                        popularfestivalslist.getItems().add(record);
                    }


                }catch(Exception var14){

                }
                System.out.println(record);

            }

        }
    }

    /**
     * When show button cliks this one peform
     * @param e
     * @throws IOException
     * @throws ParseException
     */
    public void showAction(ActionEvent e) throws IOException, ParseException {

        //Claring the lists
        longestconcertslist.getItems().clear();
        popularfestivalslist.getItems().clear();
        ExecutorService executorService = Executors.newFixedThreadPool(2);



        //This if statements control the checkboxes and do nessasary operations
        if (showlongestconcerts.isSelected()){
            // executorService.execute(new ConcertThread());

            executorService.execute(new ConcertThread());

        }
        if (showpopularfestivals.isSelected()){
            //executorService.execute(new FestivalThread());
            executorService.execute(new FestivalThread());

        }
        executorService.shutdown();
        while(!executorService.isTerminated()){
        }
    }

    /**
     * For going back screen
     * @param e
     * @throws IOException
     * @throws ParseException
     */
    public void backtomain(ActionEvent e) throws IOException, ParseException{
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setTitle("Hello View");
        s.setScene(new Scene(root, 500, 300));
        s.show();
    }



}
