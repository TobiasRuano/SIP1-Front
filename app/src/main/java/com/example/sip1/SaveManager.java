package com.example.sip1;

import android.app.Activity;
import android.content.Context;
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

    public void saveExpenses(List<Expense> expenses, Activity activity) {//saves expenses into fileName (data.bin)
        ObjectOutputStream oos = null;
        try {
            File file = new File(activity.getFilesDir().toString(), "expenses.bin");
            file.createNewFile();
            FileOutputStream fos = activity.openFileOutput("expenses.bin", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(expenses);
            oos.close();
            fos.close();
        } catch (FileNotFoundException err) {
            Toast.makeText(activity, "Something went wrong while saving", Toast.LENGTH_SHORT).show();
        } catch (Exception abcd) {
            Toast.makeText(activity, "Something went wrong while saving 2.0", Toast.LENGTH_SHORT).show();
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
            File file = new File(activity.getFilesDir().toString()+"/"+"expenses.bin");
            if (file.exists()) {
                ois = new ObjectInputStream(new FileInputStream(file));
                List<Expense> temp = (List<Expense>)ois.readObject();
                ois.close();
                return temp;
            } else {
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
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

    public void saveOnboardingStatus(Boolean status, Activity activity) {//saves expenses into fileName (data.bin)
        ObjectOutputStream oos = null;
        try {
            File file = new File(activity.getFilesDir().toString(), "onboarding.bin");
            file.createNewFile();
            FileOutputStream fos = activity.openFileOutput("onboarding.bin", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(status);
            oos.close();
            fos.close();
        } catch (FileNotFoundException err) {
            Toast.makeText(activity, "Something went wrong while saving", Toast.LENGTH_SHORT).show();
        } catch (Exception abcd) {
            Toast.makeText(activity, "Something went wrong while saving 2.0", Toast.LENGTH_SHORT).show();
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

    public Boolean readOnboardingStatus(Activity activity) {
        ObjectInputStream ois = null;
        try {
            File file = new File(activity.getFilesDir().toString()+"/"+"onboarding.bin");
            if (file.exists()) {
                ois = new ObjectInputStream(new FileInputStream(file));
                Boolean temp = (Boolean)ois.readObject();
                ois.close();
                return temp;
            } else {
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
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
