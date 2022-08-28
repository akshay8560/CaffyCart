package com.example.caffycart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class OrderCreatingActivity extends AppCompatActivity {

    private int quantity = 1;
    RelativeLayout rl;
    boolean isSelectMode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creating);

        setupOzSpinner();
        setupCoffeeTypeSpinner();
        setupCoffeeList();
        Button showOrder = findViewById(R.id.showOrderSummary);


        CheckBox WhippedCreamCheckBox = findViewById(R.id.Whipped_cream_checkBox);
        boolean hasWhippedCream = WhippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = findViewById(R.id.Chocolate_checkBox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        Spinner coffeeTypeSpinner = findViewById(R.id.spinner_coffee_select);
        String coffeeType = coffeeTypeSpinner.getSelectedItem().toString();
        Spinner coffeeCupSpinner = findViewById(R.id.spinner_oz_select);
        String coffeeCup = coffeeCupSpinner.getSelectedItem().toString();

        String price = "" + calculatePrice(hasWhippedCream, hasChocolate, coffeeType, coffeeCup);

        showOrder.setOnClickListener(view -> {
            Intent summaryPage = new Intent(OrderCreatingActivity.this, OrderSummaryActivity.class);
            OrderValues summary = generateSummary();
            summaryPage.putExtra("Summary", summary);
            startActivity(summaryPage);
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(OrderCreatingActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OrderCreatingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void inc(View view) {
        if (quantity == 100) {
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    public void dec(View view) {
        if (quantity == 1) {
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    @SuppressLint("SetTextI18n")
    private void display(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void setupCoffeeList() {
        RecyclerView list = findViewById(R.id.recycler_view_coffee_list);

        CoffeeBucket[] coffees = {
                new CoffeeBucket("Affogato Coffee", "Price : 40 Rs", R.drawable.affogatocoffee),
                new CoffeeBucket("Americano Coffee", "Price : 50 Rs", R.drawable.americanocoffee),
                new CoffeeBucket("Black Coffee", "Price : 30 Rs", R.drawable.blackcofee),
                new CoffeeBucket("Cappuccino Coffee", "Price : 69 Rs", R.drawable.cappuccinocoffee),
                new CoffeeBucket("Cortado Coffee", "Price : 20 Rs", R.drawable.cortadocoffee),
                new CoffeeBucket("Doppio Coffee", "Price : 60 Rs", R.drawable.doppiocoffee),
                new CoffeeBucket("Espresso Coffee", "Price : 300 Rs", R.drawable.espressocoffee),
                new CoffeeBucket("Flat White Coffee", "Price : 25 Rs", R.drawable.flat_whitecoffee),
                new CoffeeBucket("Galão Coffee", "Price : 85 Rs", R.drawable.gal_ocoffee),
                new CoffeeBucket("Irish Coffee", "Price : 50 Rs", R.drawable.irishcoffee),
                new CoffeeBucket("Latte Coffee", "Price : 50 Rs", R.drawable.lattecofee),
                new CoffeeBucket("Lungo Coffee", "Price : 110 Rs", R.drawable.lungocoffee),
                new CoffeeBucket("Macchiato Coffee", "Price : 125 Rs", R.drawable.macchiatocoffee),
                new CoffeeBucket("Mocha Coffee", "Price : 80 Rs", R.drawable.mochacoffee),
                new CoffeeBucket("Red Eye Coffee", "Price : 60 Rs", R.drawable.red_eyecoffee)
        };

        BucketAdapter adapter = new BucketAdapter(coffees);

        list.setAdapter(adapter);



    }

    private void setupOzSpinner() {
        Spinner cupSpinner = findViewById(R.id.spinner_oz_select);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cup_size, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        cupSpinner.setAdapter(adapter);
    }

    private void setupCoffeeTypeSpinner() {
        Spinner cupSpinner = findViewById(R.id.spinner_coffee_select);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.coffee_type,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        cupSpinner.setAdapter(adapter);
    }


    private OrderValues generateSummary() {
        CheckBox WhippedCreamCheckBox = findViewById(R.id.Whipped_cream_checkBox);
        boolean hasWhippedCream = WhippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = findViewById(R.id.Chocolate_checkBox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText nameField = findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        Spinner coffeeTypeSpinner = findViewById(R.id.spinner_coffee_select);
        String coffeeType = coffeeTypeSpinner.getSelectedItem().toString();
        Spinner coffeeCupSpinner = findViewById(R.id.spinner_oz_select);
        String coffeeCup = coffeeCupSpinner.getSelectedItem().toString();

        String price = "" + calculatePrice(hasWhippedCream, hasChocolate, coffeeType, coffeeCup);
        return new OrderValues(name, hasWhippedCream, hasChocolate, coffeeType, coffeeCup, "" + quantity, price);
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate, String coffeeType, String cupSize) {
        int basePrice = 10;
        double cupPrice = 25;
        int coffeePrice = 50;
        switch (cupSize) {
            case "3oz":
                cupPrice = cupPrice + 30;
                break;
            case "4.5oz":
                cupPrice = cupPrice + 45;
                break;
            case "5.5oz":
                cupPrice = cupPrice + 55;
                break;
            case "12oz":
                cupPrice = cupPrice + 120;
                break;
            case "14oz":
                cupPrice = cupPrice + 140;
                break;
            case "16oz":
                cupPrice = cupPrice + 160;
                break;
            default:
                cupPrice = cupPrice + 250;
                break;
        }

        switch (coffeeType) {
            case "Affogato":
                coffeePrice = coffeePrice + 120;
                break;
            case "Americano":
                coffeePrice = coffeePrice + 80;
                break;
            case "Black":
                coffeePrice = coffeePrice + 80;
                break;
            case "Cappuccino":
                coffeePrice = coffeePrice + 99;
                break;
            case "Cortado":
                coffeePrice = coffeePrice + 90;
                break;
            case "Doppio":
                coffeePrice = coffeePrice + 70;
                break;

            case "Espresso":
                coffeePrice = coffeePrice + 100;
                break;
            case "Flat White":
                coffeePrice = coffeePrice + 75;
                break;
            case "Galão":
                coffeePrice = coffeePrice + 85;
                break;
            case "Irish":
                coffeePrice = coffeePrice + 50;
                break;
            case "Latte":
                coffeePrice = coffeePrice + 50;
                break;
            case "Lungo":
                coffeePrice = coffeePrice + 110;
                break;
            case "Macchiato":
                coffeePrice = coffeePrice + 125;
                break;
            case "Mocha":
                coffeePrice = coffeePrice + 80;
                break;
            default:
                coffeePrice = coffeePrice + 60;
                break;
        }


        if (addWhippedCream) {
            basePrice = basePrice + 35;
        }
        if (addChocolate) {
            basePrice = basePrice + 55;
        }

        return (int) ((quantity * (coffeePrice + cupPrice)) + basePrice);
    }


}