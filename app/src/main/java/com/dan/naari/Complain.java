package com.dan.naari;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Complain extends Activity {

    private EditText incidentDateEditText;
    private EditText incidentTimeEditText;
    private EditText incidentLocationEditText;
    private EditText incidentDescriptionEditText;

    private CollectionReference complaintsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaintform);

        // Initialize Firestore
        // Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        complaintsCollection = firestore.collection("complaints");

        incidentDateEditText = findViewById(R.id.incident_date);
        incidentTimeEditText = findViewById(R.id.incident_time);
        incidentLocationEditText = findViewById(R.id.incident_location);
        incidentDescriptionEditText = findViewById(R.id.incident_description);
        Button uploadEvidenceButton = findViewById(R.id.upload_evidence_button);
        Button submitComplaintButton = findViewById(R.id.submit_complaint_button);

        uploadEvidenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your logic to handle evidence upload
                Toast.makeText(Complain.this, "Upload evidence clicked", Toast.LENGTH_SHORT).show();
            }
        });

        submitComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle complaint submission
                String incidentDate = incidentDateEditText.getText().toString();
                String incidentTime = incidentTimeEditText.getText().toString();
                String incidentLocation = incidentLocationEditText.getText().toString();
                String incidentDescription = incidentDescriptionEditText.getText().toString();

                // Check if any field is empty
                if (incidentDate.isEmpty() || incidentTime.isEmpty() || incidentLocation.isEmpty() || incidentDescription.isEmpty()) {
                    Toast.makeText(Complain.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a Map to store complaint data
                    Map<String, Object> complaint = new HashMap<>();
                    complaint.put("date", incidentDate);
                    complaint.put("time", incidentTime);
                    complaint.put("location", incidentLocation);
                    complaint.put("description", incidentDescription);

                    // Add complaint to Firestore
                    complaintsCollection.add(complaint)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Complain.this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Complain.this, "Failed to submit complaint", Toast.LENGTH_SHORT).show();
                                    }

                                    // Clear input fields
                                    incidentDateEditText.setText("");
                                    incidentTimeEditText.setText("");
                                    incidentLocationEditText.setText("");
                                    incidentDescriptionEditText.setText("");
                                }
                            });
                }
            }
        });
    }
}
