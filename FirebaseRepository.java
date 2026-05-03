package com.example.smartbus.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartbus.data.models.*;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {
    private final FirebaseAuth auth;
    private final DatabaseReference database;

    public FirebaseRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    public String getCurrentUserId() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }

    public Task<String> getUserRole(String userId) {
        return database.child("Users").child(userId).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                return task.getResult().child("role").getValue(String.class);
            }
            return null;
        });
    }

    // --- Authentication & Registration ---

    public Task<Boolean> registerStudent(String email, String password, Student student) {
        return auth.createUserWithEmailAndPassword(email, password).continueWithTask(task -> {
            if (!task.isSuccessful()) return Tasks.forException(task.getException());
            
            String userId = task.getResult().getUser().getUid();
            
            User baseUser = new User(userId, student.getName(), email, "Student", student.getPhoneNumber());
            student.setId(userId);
            
            Task<Void> userTask = database.child("Users").child(userId).setValue(baseUser);
            Task<Void> studentTask = database.child("Students").child(userId).setValue(student);
            
            return Tasks.whenAll(userTask, studentTask).continueWith(t -> t.isSuccessful());
        });
    }

    public Task<Boolean> registerDriver(String email, String password, Driver driver) {
        return auth.createUserWithEmailAndPassword(email, password).continueWithTask(task -> {
            if (!task.isSuccessful()) return Tasks.forException(task.getException());
            
            String userId = task.getResult().getUser().getUid();
            
            User baseUser = new User(userId, driver.getName(), email, "Driver", driver.getPhoneNumber());
            driver.setId(userId);
            
            Task<Void> userTask = database.child("Users").child(userId).setValue(baseUser);
            Task<Void> driverTask = database.child("Drivers").child(userId).setValue(driver);
            
            return Tasks.whenAll(userTask, driverTask).continueWith(t -> t.isSuccessful());
        });
    }

    public Task<Boolean> registerFaculty(String email, String password, Faculty faculty) {
        return auth.createUserWithEmailAndPassword(email, password).continueWithTask(task -> {
            if (!task.isSuccessful()) return Tasks.forException(task.getException());
            
            String userId = task.getResult().getUser().getUid();
            
            User baseUser = new User(userId, faculty.getName(), email, "Faculty", faculty.getPhoneNumber());
            faculty.setId(userId);
            
            Task<Void> userTask = database.child("Users").child(userId).setValue(baseUser);
            Task<Void> facultyTask = database.child("Faculty").child(userId).setValue(faculty);
            
            return Tasks.whenAll(userTask, facultyTask).continueWith(t -> t.isSuccessful());
        });
    }

    public Task<String> loginUser(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password).continueWithTask(task -> {
            if (!task.isSuccessful()) return Tasks.forException(task.getException());
            
            String userId = task.getResult().getUser().getUid();
            return getUserRole(userId);
        });
    }

    public void logout() {
        auth.signOut();
    }

    // --- Admin Operations ---
    
    public Task<List<Student>> getAllStudents() {
        return database.child("Students").get().continueWith(task -> {
            List<Student> students = new ArrayList<>();
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot child : task.getResult().getChildren()) {
                    Student s = child.getValue(Student.class);
                    if (s != null) students.add(s);
                }
            }
            return students;
        });
    }

    public Task<List<Driver>> getAllDrivers() {
        return database.child("Drivers").get().continueWith(task -> {
            List<Driver> drivers = new ArrayList<>();
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot child : task.getResult().getChildren()) {
                    Driver d = child.getValue(Driver.class);
                    if (d != null) drivers.add(d);
                }
            }
            return drivers;
        });
    }

    public Task<List<Faculty>> getAllFaculty() {
        return database.child("Faculty").get().continueWith(task -> {
            List<Faculty> facultyList = new ArrayList<>();
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot child : task.getResult().getChildren()) {
                    Faculty f = child.getValue(Faculty.class);
                    if (f != null) facultyList.add(f);
                }
            }
            return facultyList;
        });
    }

    // --- Profile Management ---
    
    public Task<Student> getStudentProfile(String userId) {
        return database.child("Students").child(userId).get().continueWith(task -> 
            task.isSuccessful() ? task.getResult().getValue(Student.class) : null);
    }

    public Task<Driver> getDriverProfile(String userId) {
        return database.child("Drivers").child(userId).get().continueWith(task -> 
            task.isSuccessful() ? task.getResult().getValue(Driver.class) : null);
    }

    public Task<Faculty> getFacultyProfile(String userId) {
        return database.child("Faculty").child(userId).get().continueWith(task -> 
            task.isSuccessful() ? task.getResult().getValue(Faculty.class) : null);
    }

    public Task<Boolean> updateStudentProfile(Student student) {
        Task<Void> studentTask = database.child("Students").child(student.getId()).setValue(student);
        Task<Void> userNameTask = database.child("Users").child(student.getId()).child("name").setValue(student.getName());
        Task<Void> userPhoneTask = database.child("Users").child(student.getId()).child("phoneNumber").setValue(student.getPhoneNumber());
        
        return Tasks.whenAll(studentTask, userNameTask, userPhoneTask).continueWith(t -> t.isSuccessful());
    }

    public Task<Boolean> updateDriverProfile(Driver driver) {
        Task<Void> driverTask = database.child("Drivers").child(driver.getId()).setValue(driver);
        Task<Void> userNameTask = database.child("Users").child(driver.getId()).child("name").setValue(driver.getName());
        Task<Void> userPhoneTask = database.child("Users").child(driver.getId()).child("phoneNumber").setValue(driver.getPhoneNumber());
        
        return Tasks.whenAll(driverTask, userNameTask, userPhoneTask).continueWith(t -> t.isSuccessful());
    }

    public Task<Boolean> updateFacultyProfile(Faculty faculty) {
        Task<Void> facultyTask = database.child("Faculty").child(faculty.getId()).setValue(faculty);
        Task<Void> userNameTask = database.child("Users").child(faculty.getId()).child("name").setValue(faculty.getName());
        Task<Void> userPhoneTask = database.child("Users").child(faculty.getId()).child("phoneNumber").setValue(faculty.getPhoneNumber());
        
        return Tasks.whenAll(facultyTask, userNameTask, userPhoneTask).continueWith(t -> t.isSuccessful());
    }

    // --- Driver GPS ---
    public Task<Void> updateDriverLocation(LocationData locationData) {
        return database.child("LiveLocation").child(locationData.getDriverId()).setValue(locationData);
    }

    public LiveData<List<LocationData>> observeBusLocations() {
        MutableLiveData<List<LocationData>> locationsData = new MutableLiveData<>();
        database.child("LiveLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<LocationData> locations = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    LocationData loc = child.getValue(LocationData.class);
                    if (loc != null) locations.add(loc);
                }
                locationsData.setValue(locations);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
        return locationsData;
    }

    // --- Student Fees ---
    public Task<Student> getStudentFeeDetails(String studentId) {
        return database.child("Students").child(studentId).get().continueWith(task -> 
            task.isSuccessful() ? task.getResult().getValue(Student.class) : null);
    }
    
    public LiveData<String> observeStudentFeeStatus(String studentId) {
        MutableLiveData<String> statusData = new MutableLiveData<>();
        database.child("Students").child(studentId).child("feeStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);
                statusData.setValue(status != null ? status : "Pending");
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
        return statusData;
    }

    public Task<Boolean> payFee(String studentId, double amount) {
        Task<Void> feeTask = database.child("Students").child(studentId).child("feeStatus").setValue("Paid");
        
        String transactionId = database.child("FeeRecords").push().getKey();
        if (transactionId == null) return Tasks.forException(new Exception("Failed to generate key"));
        
        FeeRecord record = new FeeRecord(
            studentId,
            amount,
            System.currentTimeMillis(),
            transactionId,
            "Success"
        );
        Task<Void> recordTask = database.child("FeeRecords").child(transactionId).setValue(record);
        
        return Tasks.whenAll(feeTask, recordTask).continueWith(t -> t.isSuccessful());
    }
}
