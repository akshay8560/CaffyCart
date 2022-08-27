package com.example.caffycart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import soup.neumorphism.NeumorphButton;

public class OrderSummaryActivity extends AppCompatActivity implements PaymentResultListener {

    NeumorphButton pay;
    TextView summaryList, orderId;
    CardView payOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Checkout.preload(getApplicationContext());
        pay = findViewById(R.id.confirm_order);
        orderId = findViewById(R.id.paymentOrderId);
        payOrder = findViewById(R.id.paymentCard);

        int randomReferenceNumber = (int) (Math.random() * 999998);
        String randomString = String.valueOf(randomReferenceNumber);

        OrderValues order = (OrderValues) getIntent().getSerializableExtra("Summary");
        summaryList = findViewById(R.id.order_summary_text_view);

        String summary = "Name: " + order.name;
        summary += "\nSelected Coffee Type: " + order.coffeeType;
        summary += "\nSelected Cup Size: " + order.cupSize;
        summary += "\nQuantity: " + order.quantity;
        summary += "\nTotal: " + order.price + " Rs";
        summary += "\nThank You :) for using Caffy Cart!";

        summaryList.setText(summary);

        String priceOfCart = order.price + "00";

        pay.setOnClickListener(view -> {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_Am86M3W8b09tLD");
            final Activity activity = OrderSummaryActivity.this;
            try {
                JSONObject options = new JSONObject();

                options.put("name", "Caffy Cart");
                options.put("description", "Reference No. #" + randomString);
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("theme.color", "#FFEBCE");
                checkout.setImage(R.drawable.caffy_cart_logo);
                options.put("currency", "INR");
                options.put("amount", priceOfCart);//pass amount in currency subunits
                options.put("prefill.email", "");
                options.put("prefill.contact", "");
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);
                checkout.open(activity, options);
            } catch (Exception ignored) {
            }
        });
    }

    private void createOrderSummaryAndPayment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show();
        payOrder.setVisibility(View.VISIBLE);
        orderId.setVisibility(View.VISIBLE);
        orderId.setText("Order ID: " + s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_LONG).show();
    }
}