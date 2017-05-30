package com.company;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
	// write your code here

        try{
            MongoClient m = new MongoClient ("localhost", 27017);
            DB db = m.getDB("kfzWerkstatt");
            DBCollection coll = db.getCollection("Kunde");

            //beispielDaten(coll, db);
            alleAusgeben(coll);

            m.close();
        } catch (MongoException e){
            System.out.println(e);
        } /*catch (ParseException e) {
            e.printStackTrace();
        }*/

    }

    public static void alleAusgeben(DBCollection coll){
        DBCursor cur = coll.find();
        while(cur.hasNext()){
            System.out.println(cur.next());
        }

        System.out.println("Datensatz Mueller:");

        BasicDBObject query = new BasicDBObject();
        query.put("Name", "Mueller");
        cur = coll.find(query);

        while(cur.hasNext()){
            System.out.println(cur.next());
        }

        System.out.println("Reperatur Lack:");

        query = new BasicDBObject();
        query.put("Auto.Reperatur.Bezeichnung", "Lack");
        cur = coll.find(query);

        while(cur.hasNext()){
            System.out.println(cur.next());
        }

        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.put("Name", "Mueller");
        BasicDBObject updateCommand = new BasicDBObject();
        BasicDBObject neuesAuto = new BasicDBObject();
        neuesAuto.put("Name", "Tesla S");
        neuesAuto.put("Kennzeichen", "ERB-S 1337");
        neuesAuto.put("Baujahr", 2017);
        updateCommand.put("$push", new BasicDBObject("Autos", neuesAuto));
        coll.update(updateQuery, updateCommand, true, true);

        System.out.println("Datensatz Richter löschen:");

        query = new BasicDBObject("Name", "Richter");
        DBObject DKunde = coll.findOne(query);
        coll.remove(DKunde);

    }

    public static void beispielDaten(DBCollection coll, DB db) throws ParseException {
        ArrayList<BasicDBObject> autosKunde1 = new ArrayList<>();
        ArrayList<BasicDBObject> autosKunde2 = new ArrayList<>();
        ArrayList<BasicDBObject> reperaturenAuto1 = new ArrayList<>();
        ArrayList<BasicDBObject> reperaturenAuto2 = new ArrayList<>();

        BasicDBObject kunde = new BasicDBObject();
        kunde.put("Name", "Schmidt");
        kunde.put("TelNr", 061421141);
        kunde.put("Strasse", "Bahnhofstr. 1");
        kunde.put("PLZ", 65428);
        kunde.put("Ort", "Ruesselsheim");


        BasicDBObject kunde2 = new BasicDBObject();
        kunde2.put("Name", "Mueller");
        kunde2.put("TelNr", 0711566);
        kunde2.put("Strasse", "Alleeweg 7");
        kunde2.put("PLZ", 70173);
        kunde2.put("Ort", "Stuttgart");

        BasicDBObject kunde3 = new BasicDBObject();
        kunde3.put("Name", "Richter");
        kunde3.put("TelNr", 061421141);
        kunde3.put("Strasse", "Hauptstraße 6");
        kunde3.put("PLZ", 70173);
        kunde3.put("Ort", "Stuttgart");

        BasicDBObject auto1 = new BasicDBObject();
        auto1.put("Name", "Opel Admiral");
        auto1.put("Kennzeichen", "DA-OA 338");
        auto1.put("Baujahr", 1938);

        BasicDBObject auto2 = new BasicDBObject();
        auto2.put("Name", "Opel Manta");
        auto2.put("Kennzeichen", "DA-OM 447");
        auto2.put("Baujahr", 1972);

        BasicDBObject auto3 = new BasicDBObject();
        auto3.put("Name", "Mercedes SLK");
        auto3.put("Kennzeichen", "S-PG 12");
        auto3.put("Baujahr", 2005);

        BasicDBObject reperatur1 = new BasicDBObject();
        reperatur1.put("Bezeichnung", "Bremse");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = (Date) sdf.parseObject("2001-01-22 12:00");
        reperatur1.put("Datum", d);

        BasicDBObject reperatur2 = new BasicDBObject();
        reperatur2.put("Bezeichnung", "Lack");
        d = (Date) sdf.parseObject("2005-06-17 12:00");
        reperatur2.put("Datum", d);

        BasicDBObject reperatur3 = new BasicDBObject();
        reperatur3.put("Bezeichnung", "Scheibe");
        d = (Date) sdf.parseObject("1995-01-10 12:00");
        reperatur2.put("Datum", d);

        autosKunde1.add(auto1);
        autosKunde1.add(auto2);
        autosKunde2.add(auto3);
        kunde.put("Auto", autosKunde1);
        kunde2.put("Autos", autosKunde2);

        reperaturenAuto1.add(reperatur1);
        auto1.put("Reperatur", reperaturenAuto1);

        reperaturenAuto2.add(reperatur2);
        reperaturenAuto2.add(reperatur3);
        auto2.put("Reperatur", reperaturenAuto2);

        coll.insert(kunde);
        coll.insert(kunde2);
        coll.insert(kunde3);

        kunde.clear();
        kunde2.clear();
        kunde3.clear();

    }
}
