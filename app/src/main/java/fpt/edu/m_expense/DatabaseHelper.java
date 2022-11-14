package fpt.edu.m_expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.anychart.anychart.DataEntry;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fpt.edu.m_expense.entities.destination;
import fpt.edu.m_expense.entities.expense;
import fpt.edu.m_expense.entities.trip;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MExpense";
    //Trip
    private static final String TABLE_NAME_TRIP = "trip";
    private static final String TABLE_TRIP_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   name TEXT, " +
                    "   date TEXT, " +
                    "   rate INTEGER, " +
                    "   risk INTEGER, " +
                    "   description TEXT, " +
                    "   destinationFromId TEXT, " +
                    "   destinationToId TEXT, " +
                    "   totalCost TEXT, " +
                    "   tripStatus INTEGER, " +
                    "   tagId TEXT)",
            TABLE_NAME_TRIP);
    //Expense
    private static final String TABLE_NAME_EXPENSE = "expense";
    private static final String TABLE_EXPENSE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   time TEXT, " +
                    "   tripId INTEGER, " +
                    "   cost TEXT, " +
                    "   description TEXT, " +
                    "   payType TEXT, " +
                    "   expenseTypeId INTEGER)",
            TABLE_NAME_EXPENSE);
    //Destination
    private static final String TABLE_NAME_DESTINATION = "destination";
    private static final String TABLE_DESTINATION_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   location TEXT, " +
                    "   description TEXT, " +
                    "   gps TEXT)",
            TABLE_NAME_DESTINATION);
    //ExpenseType
    private static final String TABLE_NAME_EXPENSE_TYPE = "expenseType";
    private static final String TABLE_EXPENSE_TYPE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   type TEXT, " +
                    "   description TEXT)",
            TABLE_NAME_EXPENSE_TYPE);
    //Photo
    private static final String TABLE_NAME_PHOTO = "photo";
    private static final String TABLE_PHOTO_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   tripId INTEGER, " +
                    "   path TEXT, " +
                    "   description TEXT)",
            TABLE_NAME_PHOTO);

    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.e("TAG", TABLE_DESTINATION_CREATE );
        db.execSQL(TABLE_TRIP_CREATE);
        db.execSQL(TABLE_EXPENSE_TYPE_CREATE);
        db.execSQL(TABLE_DESTINATION_CREATE);
        db.execSQL(TABLE_PHOTO_CREATE);
        db.execSQL(TABLE_EXPENSE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PHOTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXPENSE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DESTINATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXPENSE);

        Log.v(this.getClass().getName(),  DATABASE_NAME+ " database upgrade to version " +
                newVersion + " - old data lost");
        onCreate(db);
    }


    public long insertTrip(String name, String date, String destinationToId, Integer risk, Integer tripStatus,String description, Integer rate,String destinationFromId) {
        ContentValues rowValues = new ContentValues();

        rowValues.put("name", name);
        rowValues.put("date", date);
        rowValues.put("destinationToId", destinationToId);
        rowValues.put("tripStatus", tripStatus);
        rowValues.put("risk", risk);
        rowValues.put("totalCost", "0");
        if(description != null){
            rowValues.put("description", description);
        }
        if(rate != null) {
            rowValues.put("rate", rate);
        }
        if(destinationFromId != null){
            rowValues.put("destinationFromId", destinationFromId);
        }

        return database.insertOrThrow(TABLE_NAME_TRIP, null, rowValues);
    }

    public long insertDestination(String location, String description, String gps) {
        ContentValues rowValues = new ContentValues();

        rowValues.put("location", location);
        if(description != null){
            rowValues.put("description", description);
        }
        if(gps != null) {
            rowValues.put("gps", gps);
        }

        return database.insertOrThrow(TABLE_NAME_DESTINATION, null, rowValues);
    }

    public long insertExpenseType(String type, String description) {
        ContentValues rowValues = new ContentValues();

        rowValues.put("type", type);
        if(description != null){
            rowValues.put("description", description);
        }

        return database.insertOrThrow(TABLE_NAME_EXPENSE_TYPE, null, rowValues);
    }

    public String[] getExpenseTypeList() {
        Cursor cursor = database.query(TABLE_NAME_EXPENSE_TYPE, new String[] {"type"},
                null, null, null, null, "type");

        String[] ExpenseType = new String[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String data = cursor.getString(0);
            ExpenseType[i] = data;
            cursor.moveToNext();
            i = i+1;
        }
        return ExpenseType;
    }
    public long insertExpense(String tripId, String time, String cost, String description, String payType,String expenseTypeId) {
        ContentValues rowValues = new ContentValues();

        rowValues.put("time", time);
        rowValues.put("cost", cost);
        rowValues.put("payType", payType);
        rowValues.put("expenseTypeId", expenseTypeId);
        rowValues.put("tripId", tripId);
        if (description != null) {
            rowValues.put("description", description);
        }

        return database.insertOrThrow(TABLE_NAME_EXPENSE, null, rowValues);
    }

    public void deleteTripDataById(int id){
        deleteTripById(id);
        deleteTripExpenseByTripId(id);
    }
    public void deleteAllTripData(){
        deleteAllTrip();
        deleteAllExpense();
    }

    private void deleteAllTrip()
    {
        database.execSQL("delete from "+ TABLE_NAME_TRIP);
    }
    private void deleteAllExpense()
    {
        database.execSQL("delete from "+ TABLE_NAME_EXPENSE);
    }
    private boolean deleteTripById(int id)
    {
        return database.delete(TABLE_NAME_TRIP,  "id = " + id, null) > 0;
    }
    private boolean deleteTripExpenseByTripId(int id)
    {
        return database.delete(TABLE_NAME_EXPENSE,  "tripId = " + id, null) > 0;
    }

    public ArrayList<trip> getTrip() {
        Cursor cursor = database.query(TABLE_NAME_TRIP, new String[] {"id", "name", "date","rate", "risk", "description", "destinationFromId", "destinationToId", "totalCost", "tripStatus"},
                null, null, null, null, "id");

        ArrayList<trip> result = new ArrayList<>();

        Log.e("Cursor: ", String.valueOf(cursor.getCount()) );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            Integer rate = Integer.parseInt(cursor.getString(3));
            Integer risk = Integer.parseInt(cursor.getString(4));
            String description = cursor.getString(5);
            String destinationFromId = cursor.getString(6);
            String destinationToId = cursor.getString(7);
            String totalCost = cursor.getString(8);
            Integer tripStatus = Integer.parseInt(cursor.getString(9));

            trip NewTrip = new trip();
            NewTrip.setDate(date);
            NewTrip.setId(id);
            NewTrip.setName(name);
            NewTrip.setDate(date);
            NewTrip.setRate(rate);
            NewTrip.setRisk(risk);
            NewTrip.setDescription(description);
            NewTrip.setDestinationFromId(destinationFromId);
            NewTrip.setDestinationToId(destinationToId);
            NewTrip.setTotalCost(totalCost);
            NewTrip.setTripStatus(tripStatus);

            cursor.moveToNext();

            result.add(NewTrip);

        }

        Log.e("List Count: ", result.toString() );
        return result;

    }
    public trip getTripById(int idArg) {
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME_TRIP+" WHERE TRIM(id) = '"+idArg+"'", null);

        trip NewTrip = new trip();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = idArg;
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            Integer rate = Integer.parseInt(cursor.getString(3));
            Integer risk = Integer.parseInt(cursor.getString(4));
            String description = cursor.getString(5);
            String destinationFromId = cursor.getString(6);
            String destinationToId = cursor.getString(7);
            String totalCost = cursor.getString(8);
            Integer tripStatus = Integer.parseInt(cursor.getString(9));


            NewTrip.setDate(date);
            NewTrip.setId(id);
            NewTrip.setName(name);
            NewTrip.setDate(date);
            NewTrip.setRate(rate);
            NewTrip.setRisk(risk);
            NewTrip.setDescription(description);
            NewTrip.setDestinationFromId(destinationFromId);
            NewTrip.setDestinationToId(destinationToId);
            NewTrip.setTotalCost(totalCost);
            NewTrip.setTripStatus(tripStatus);
            NewTrip.setTotalCost(totalCost);
            cursor.moveToNext();

        }
        return NewTrip;
    }

    public ArrayList<destination> getDestinationInfoList() {
        Cursor cursor = database.query(TABLE_NAME_DESTINATION, new String[] {"id", "location", "description","gps"},
                null, null, null, null, "id");

        ArrayList<destination> result = new ArrayList<>();

        Log.e("Cursor: ", String.valueOf(cursor.getCount()) );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String location = cursor.getString(1);
            String description = cursor.getString(2);
            String gps = cursor.getString(3);

            destination NewDes = new destination();
            NewDes.setId(id);
            NewDes.setLocation(location);
            NewDes.setDescription(description);
            NewDes.setGps(gps);

            cursor.moveToNext();

            result.add(NewDes);

        }

        Log.e("List Count: ", result.toString() );
        return result;
    }

    public String[] getDestinationList() {
        Cursor cursor = database.query(TABLE_NAME_DESTINATION, new String[] {"location"},
                null, null, null, null, "location");

        String[] Destination = new String[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String location = cursor.getString(0);
            Destination[i] = location;
            cursor.moveToNext();
            i = i+1;
        }
        return Destination;
    }

    public ArrayList<expense> getExpenseByTripId(int id) {
        String arg = String.valueOf(id);
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME_EXPENSE+" WHERE TRIM(tripId) = '"+arg.trim()+"'", null);

        ArrayList<expense> expenses = new ArrayList<expense>();
        Log.e("Here", "getDestinationList: ");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int idEx = cursor.getInt(0);
            String time = cursor.getString(1);
            String tripId = cursor.getString(2);
            String cost = cursor.getString(3);
            String description = cursor.getString(4);
            String payType = cursor.getString(5);
            String expenseTypeId = cursor.getString(6);
            cursor.moveToNext();
            expense NewEx = new expense();
            NewEx.setCost(cost);
            NewEx.setTime(time);
            NewEx.setDescription(description);
            NewEx.setPayType(payType);
            NewEx.setExpenseTypeId(expenseTypeId);
            NewEx.setId(idEx);
            NewEx.setTripId(tripId);
            expenses.add(NewEx);
        }
        return expenses;
    }

    public float getAllExpenseCostByTripId(int id){
        String arg = String.valueOf(id);
        Cursor cursor = database.rawQuery("SELECT cost FROM "+TABLE_NAME_EXPENSE+" WHERE TRIM(tripId) = '"+arg.trim()+"'", null);
        float expenseCost = 0;
        Log.e("getAllExpenseCostByTripId", "Here" );
        Log.e("getAllExpenseCostByTripId", String.valueOf(cursor.getCount()) );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            float cost = cursor.getFloat(0);
            Log.e("cost", String.valueOf(cost));
            expenseCost = expenseCost + cost;
            cursor.moveToNext();
        }
        Log.d("expenseCost", String.valueOf(expenseCost));
        return expenseCost;
    }



    public List<searchItem> getListSearchData(){
        List<searchItem> list = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME_TRIP, new String[] {"id", "name", "destinationFromId", "destinationToId", "date"},
                null, null, null, null, "id");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String destinationFromId = cursor.getString(2);
            String destinationToId = cursor.getString(3);
            String date = cursor.getString(4);
            list.add(new searchItem(String.valueOf(id),name, "Trip name: "+name+" | Matching Name"));
            list.add(new searchItem(String.valueOf(id),destinationFromId, "Trip name: "+name+" | Matching destination From: "+destinationFromId));
            list.add(new searchItem(String.valueOf(id),destinationToId, "Trip name: "+name+" | Matching destination To: "+destinationToId));
            list.add(new searchItem(String.valueOf(id),date, "Trip name: "+name+" | Matching Date: "+date));
            cursor.moveToNext();
        }
        return list;
    }

    public List<dataForChart> getDataForChart(){
        String listAdded = "";
        List<dataForChart> data = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME_EXPENSE, new String[] {"expenseTypeId", "cost"},
                null, null, null, null, "expenseTypeId");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String expenseType = cursor.getString(0);
            String cost = cursor.getString(1);
            if(!listAdded.contains(expenseType)){
                data.add(new dataForChart(expenseType, cost));
                listAdded = listAdded + expenseType;
            }else{
                for (dataForChart items: data){
                    if(items.getExpenseType().equals(expenseType)){
                        items.setTotalCost(String.valueOf(Float.parseFloat(items.getTotalCost()) + Float.parseFloat(cost)));
                    }
                }
            }
            cursor.moveToNext();
        }
        return data;
    }

    public  void calTotalExpense(int tripId){

        float totalCost = getAllExpenseCostByTripId(tripId);
        String strSQL = "UPDATE "+TABLE_NAME_TRIP+" SET totalCost = "+totalCost+" WHERE id = "+ tripId;
        database.execSQL(strSQL);
    }

    public dataForStatistic calTotalTrip(){
        float totalCost = 0;
        Cursor cursor = database.rawQuery("SELECT totalCost FROM "+TABLE_NAME_TRIP+"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        totalCost = totalCost + Float.parseFloat(cursor.getString(0));
        cursor.moveToNext();
        }
        dataForStatistic data =  new dataForStatistic(cursor.getCount(), totalCost);
        return data;
    }
    public trip getMostCostTrip(){
        int MaxCostID = 0;
        float MaxCost = 0;
        Cursor cursor = database.rawQuery("SELECT totalCost, id FROM "+TABLE_NAME_TRIP+"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(Float.parseFloat(cursor.getString(0)) >= MaxCost){
                MaxCost = Float.parseFloat(cursor.getString(0));
                MaxCostID = cursor.getInt(1);
            }
            cursor.moveToNext();
        }
        trip costTrip = getTripById(MaxCostID);
        return costTrip;
    }

    public int getTripExpenseCount(int id){
        Cursor cursor = database.rawQuery("SELECT cost FROM "+TABLE_NAME_EXPENSE+" WHERE TRIM(tripId) = '"+id+"'", null);
        return cursor.getCount();
    }
    public List<DetailList> getDataForUploadCloud(){
        List<DetailList> data = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME_TRIP, new String[] {"id", "name", "description"},
                null, null, null, null, "id");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            if(description.equals("")){
                description = "No description!";
            }
            data.add(new DetailList(name, description));
            cursor.moveToNext();
        }
        return  data;
    }

    public long insertImage(int tripId,String path) {
        ContentValues rowValues = new ContentValues();
        rowValues.put("tripId", tripId);
        rowValues.put("path", path);
        return database.insertOrThrow(TABLE_NAME_PHOTO, null, rowValues);
    }
    public String[] getListImageLink(int id) {
        Cursor cursor = database.rawQuery("SELECT path FROM "+TABLE_NAME_PHOTO+" WHERE TRIM(tripId) = '"+id+"'", null);

        String[] imageLink = new String[cursor.getCount()];
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String data = cursor.getString(0);
            imageLink[i] = data;
            cursor.moveToNext();
            i = i+1;
        }
        return imageLink;
    }

}
