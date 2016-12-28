package com.example.android.flyingio;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener{

    public static final int DATEPICKER_ORIGIN = 0, DATEPICKER_DESTINATION = 1;
    public static final double ADULT_DISCOUNT = 0, CHILDREN_DISCOUNT = 0.10, BABIES_DISCOUNT = 0.30;
    // Constants.
    private static final String TAG = "MainActivity";
    // Database Connection and setup.
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbCiudades = database.getReference("ciudades");
    DatabaseReference dbClases = database.getReference("clases");
    DatabaseReference dbPrueba = database.getReference("prueba");
    Calendar now = Calendar.getInstance();
    Calendar disabledDestinationDate;
    private int datePickerId;
    // List to store db values.
    private List<City> cities = new ArrayList<>();
    private List<TicketClass> ticketsClasses = new ArrayList<>();
    private ArrayList<String> citiesList = new ArrayList<>();
    private ArrayList<String> ticketsClassesList = new ArrayList<>();

    // =========================
    // Inicializate UI elements.
    // =========================
    private ArrayList<String> numbersOfTicketsList = new ArrayList<>();
    private ArrayList<String> numbersOfAdultsTicketsList = new ArrayList<>();
    // Selects inputs for airports.
    private Spinner selectOriginView, selectDestinationView;
    // Select inputs for tickets numbers.
    private Spinner selectAdultsView, selectChildrenView, selectBabiesView;
    // Select input for classes.
    private Spinner selectClassView;
    // Calendar Buttons.
    private Button buttonFrom, buttonTo, buttonBuy;
    // TextView.
    private TextView textPrice;
    private DatePickerDialog datePickerDialogOrigin, datePickerDialogDestination;

    // ===========================
    // Price of the flight control
    // ===========================
    private String originName, destinationName, className;
    private double flightPrice, classPrice;
    private boolean dateOriginReady = false, dateDestiantionReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciate the select inputs of airports.
        selectOriginView = (Spinner) findViewById(R.id.select_origin);
        selectDestinationView = (Spinner) findViewById(R.id.select_destination);

        // Instanciate the select inputs of tickets numbers.
        selectAdultsView = (Spinner) findViewById(R.id.select_adults);
        selectChildrenView = (Spinner) findViewById(R.id.select_childrens);
        selectBabiesView = (Spinner) findViewById(R.id.select_babies);

        // Instanciate the ticket class select.
        selectClassView = (Spinner) findViewById(R.id.select_class);

        // Instanciate the calendar buttons.
        buttonFrom = (Button) findViewById(R.id.button_from);
        buttonTo = (Button) findViewById(R.id.button_to);
        buttonBuy = (Button) findViewById(R.id.button_buy);

        textPrice = (TextView) findViewById(R.id.text_price);

        for (int i = 0; i <= 6; i++) {
            String number = String.valueOf(i);
            numbersOfTicketsList.add(number);
        }

        for (int i = 1; i <= 6; i++) {
            String number = String.valueOf(i);
            numbersOfAdultsTicketsList.add(number);
        }

        // Set the ArrayAdapters for the selects
        final ArrayAdapter<String> dataAdapterOrigin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citiesList);
        final ArrayAdapter<String> dataAdapterDestination = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citiesList);
        final ArrayAdapter<String> dataAdapterClasses = new ArrayAdapter<String>(this, R.layout.spinner_layout_tickets, ticketsClassesList);
        final ArrayAdapter<String> dataAdapterAdults = new ArrayAdapter<String>(this, R.layout.spinner_layout_tickets, numbersOfAdultsTicketsList);
        final ArrayAdapter<String> dataAdapterChildren = new ArrayAdapter<String>(this, R.layout.spinner_layout_tickets, numbersOfTicketsList);
        final ArrayAdapter<String> dataAdapterBabies = new ArrayAdapter<String>(this, R.layout.spinner_layout_tickets, numbersOfTicketsList);

        dbCiudades.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String city = dataSnapshot.getValue(String.class);
                citiesList.add(city);
                dataAdapterOrigin.notifyDataSetChanged();
                dataAdapterDestination.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

         dbClases.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 TicketClass ticketClass = new TicketClass(dataSnapshot.getKey(), (String) dataSnapshot.getValue());
                 ticketsClasses.add(ticketClass);
                 ticketsClassesList.add(dataSnapshot.getKey());
                 dataAdapterClasses.notifyDataSetChanged();
             }

             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {}

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

             @Override
             public void onCancelled(DatabaseError databaseError) {}
         });

        dbPrueba.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                City cityValue = new City(dataSnapshot.getKey());
                for (DataSnapshot city: dataSnapshot.getChildren()){
                    Map<String, String> temp = new HashMap<String, String>();

                    String key = city.getKey();
                    String value = (String) city.getValue();

                    temp.put(key, value);
                    cityValue.setCity(temp);
                }
                cities.add(cityValue);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        dataAdapterOrigin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterClasses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterAdults.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterChildren.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterBabies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectOriginView.setAdapter(dataAdapterOrigin);
        selectDestinationView.setAdapter(dataAdapterDestination);
        selectClassView.setAdapter(dataAdapterClasses);
        selectAdultsView.setAdapter(dataAdapterAdults);
        selectChildrenView.setAdapter(dataAdapterChildren);
        selectBabiesView.setAdapter(dataAdapterBabies);

        // Callback que se ejecuta cuando se seleciona una ciudad de origen
        selectOriginView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                originName = selectOriginView.getSelectedItem().toString();

                if (originName.equals(destinationName)) {
                    buttonBuy.setEnabled(false);
                } else if(!originName.equals(destinationName) && dateOriginReady && dateDestiantionReady){
                    buttonBuy.setEnabled(true);
                }

                for (City city : cities) {
                    if (city.getCityName().equals(originName)) {
                        flightPrice = Double.parseDouble(city.getPrice(destinationName));
                        System.out.println(Double.parseDouble(city.getPrice(destinationName)));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Callback que se ejecuta cuando se seleciona una ciudad de origen
        selectDestinationView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destinationName = selectDestinationView.getSelectedItem().toString();

                if (originName.equals(destinationName)) {
                    buttonBuy.setEnabled(false);
                } else if(!originName.equals(destinationName) && dateOriginReady && dateDestiantionReady){
                    buttonBuy.setEnabled(true);
                }

                for (City city : cities) {
                    if (city.getCityName().equals(destinationName)) {
                        flightPrice = Double.parseDouble(city.getPrice(originName));
                        System.out.println(Double.parseDouble(city.getPrice(originName)));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Callback que se ejecuta cuando se seleciona una ciudad de origen
        selectClassView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                className = selectClassView.getSelectedItem().toString();

                for (TicketClass ticket : ticketsClasses) {
                    if (ticket.getName().equals(className)) {
                        classPrice = Double.parseDouble(ticket.getPrice());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // Callback que se ejecuta cuando se seleciona una fecha para la ciudad de origen.
        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            datePickerId = 0;
            datePickerDialogOrigin = createDialog(v);
            datePickerDialogOrigin.setMinDate(now);
            datePickerDialogOrigin.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        // Callback que se ejecuta cuando se seleciona una fecha para la ciudad de origen.
        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            datePickerId = 1;
            datePickerDialogDestination = createDialog(v);
            datePickerDialogDestination.setMinDate(disabledDestinationDate);
            datePickerDialogDestination.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfAdults = Integer.parseInt(selectAdultsView.getSelectedItem().toString());
                int numberOfChildrens = Integer.parseInt(selectChildrenView.getSelectedItem().toString());
                int numberOfBabies = Integer.parseInt(selectBabiesView.getSelectedItem().toString());

                Double oneTicketPrice = flightPrice + classPrice;
                Double oneTicketAdultsPrice = ((oneTicketPrice * numberOfAdults) - (oneTicketPrice * numberOfAdults * ADULT_DISCOUNT));
                Double oneTicketChildrenPrice = ((oneTicketPrice * numberOfChildrens) - (oneTicketPrice * numberOfChildrens * CHILDREN_DISCOUNT));
                Double oneTicketBabyPrice = ((oneTicketPrice * numberOfBabies) - (oneTicketPrice * numberOfBabies * BABIES_DISCOUNT));

                Double total = oneTicketAdultsPrice + oneTicketChildrenPrice + oneTicketBabyPrice;

                String message = "Costo del Boleto(" + flightPrice + ") + Clase(" + classPrice + "): " + oneTicketPrice;

                if (numberOfChildrens > 0) {
                    message += "\nBoleto de cada niño(Con 10% de descuento): $" + oneTicketPrice * (1 - CHILDREN_DISCOUNT);
                }

                if (numberOfBabies > 0) {
                    message += "\nBoleto de cada bebe(Con 30% de descuento): $" + oneTicketPrice * (1 - BABIES_DISCOUNT);
                }

                message += "\nTotal: $" + total;

                textPrice.setText(message);
            }
        });

    }

    /**
     * Create a new DatePickerDialog with defaults params.
     * @param v current View
     * @return DatePickerDialog
     */
    public DatePickerDialog createDialog(View v) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
            MainActivity.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );

        return dpd;
    }

    /**
     * Callback to run when a date is selected from a DatePickerCalendar,
     * this callback can identify what datepicker was selecetd based on
     * an id.
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        TextView editTextOrigin = (TextView) findViewById(R.id.edit_text_date_origin);
        TextView editTextDestination = (TextView) findViewById(R.id.edit_text_date_destination);

        String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

        switch ( datePickerId ) {
            case DATEPICKER_ORIGIN:
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                disabledDestinationDate = calendar;
                editTextOrigin.setText(date);
                dateOriginReady = true;
                if (originName.equals(destinationName)) {
                    buttonBuy.setEnabled(false);
                } else if(!originName.equals(destinationName) && dateOriginReady && dateDestiantionReady){
                    buttonBuy.setEnabled(true);
                }
                break;
            case DATEPICKER_DESTINATION:
                dateDestiantionReady = true;
                editTextDestination.setText(date);
                if (originName.equals(destinationName)) {
                    buttonBuy.setEnabled(false);
                } else if(!originName.equals(destinationName) && dateOriginReady && dateDestiantionReady){
                    buttonBuy.setEnabled(true);
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
