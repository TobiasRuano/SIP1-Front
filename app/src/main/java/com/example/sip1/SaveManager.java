package com.example.sip1;

import android.app.Activity;
import android.widget.Toast;

import com.example.sip1.models.Expense;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SaveManager {

    private static SaveManager instance = null;

    private SaveManager() {}

    public static SaveManager Shared() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    public Boolean saveExpenses(List<Expense> expenses, Activity activity) {//saves expenses into fileName (data.bin)
        ObjectOutputStream oos = null;
        try {
            File file = new File(activity.getFilesDir().toString(), "data.bin");
            file.createNewFile();
            FileOutputStream fos = activity.openFileOutput("data.bin", activity.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(expenses);
            oos.close();
            fos.close();
            return true;
        } catch (FileNotFoundException err) {
            Toast.makeText(activity, "Something went wrong while saving", Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception abcd) {
            Toast.makeText(activity, "Something went wrong while saving 2.0", Toast.LENGTH_SHORT).show();
            return false;
        }
        finally {//makes sure to close the ObjectOutputStream
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Expense> readExpenses(Activity activity) {
        ObjectInputStream ois = null;
        try {
            File file = new File(activity.getFilesDir().toString()+"/"+"data.bin");
            if (file.exists()) {
                ois = new ObjectInputStream(new FileInputStream(file));
                List<Expense> temp = (List<Expense>)ois.readObject();
                ois.close();
                return temp;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
