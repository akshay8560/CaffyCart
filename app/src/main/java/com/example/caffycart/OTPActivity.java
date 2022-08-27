package com.example.caffycart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import soup.neumorphism.NeumorphButton;

public class OTPActivity extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    NeumorphButton confirmOTP;
    TextView resend;
    String gettingBackendOTP, userMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        otp1 = findViewById(R.id.OTP1);
        otp2 = findViewById(R.id.OTP2);
        otp3 = findViewById(R.id.OTP3);
        otp4 = findViewById(R.id.OTP4);
        otp5 = findViewById(R.id.OTP5);
        otp6 = findViewById(R.id.OTP6);
        confirmOTP = findViewById(R.id.verifyOTP);
        resend = findViewById(R.id.resendOTP);
        gettingBackendOTP = getIntent().getStringExtra("backendOTP");
        userMobileNumber = getIntent().getStringExtra("mobileNo");

        confirmOTP.setOnClickListener(view -> {

            String code1 = otp1.getText().toString();
            String code2 = otp2.getText().toString();
            String code3 = otp3.getText().toString();
            String code4 = otp4.getText().toString();
            String code5 = otp5.getText().toString();
            String code6 = otp6.getText().toString();

            if (!TextUtils.isEmpty(code1) || !TextUtils.isEmpty(code2) || !TextUtils.isEmpty(code3) || !TextUtils.isEmpty(code4) || !TextUtils.isEmpty(code5) || !TextUtils.isEmpty(code6)) {
                String fullOTP = code1 + code2 + code3 + code4 + code5 + code6;
                if (gettingBackendOTP != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(gettingBackendOTP, fullOTP);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {
                        Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                        Toast.makeText(OTPActivity.this, "Verification successful \n User Registered", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> Toast.makeText(OTPActivity.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(OTPActivity.this, "Error! Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (TextUtils.isEmpty(code1)) {
                    otp1.setError("Please, Enter the OTP");
                    otp1.requestFocus();
                } else if (TextUtils.isEmpty(code2)) {
                    otp2.setError("Please, Enter the OTP");
                    otp2.requestFocus();
                } else if (TextUtils.isEmpty(code3)) {
                    otp3.setError("Please, Enter the OTP");
                    otp3.requestFocus();
                } else if (TextUtils.isEmpty(code4)) {
                    otp4.setError("Please, Enter the OTP");
                    otp4.requestFocus();
                } else if (TextUtils.isEmpty(code5)) {
                    otp5.setError("Please, Enter the OTP");
                    otp5.requestFocus();
                } else {
                    otp6.setError("Please, Enter the OTP");
                    otp6.requestFocus();
                }
                Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        });

        // on clicking resend OTP
        resend.setOnClickListener(view -> {

            String newMobileNumber = "+91" + userMobileNumber;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    newMobileNumber,
                    120,
                    TimeUnit.SECONDS,
                    OTPActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(OTPActivity.this, "Error! Code not sent " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String newBackendOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            gettingBackendOTP = newBackendOTP;
                            Toast.makeText(OTPActivity.this, "OTP resend successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        // Moving to next otp entry after filling the previous OTP code
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}