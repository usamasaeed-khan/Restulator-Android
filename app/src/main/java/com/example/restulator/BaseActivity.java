package com.example.restulator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

class BaseActivity extends AppCompatActivity {

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Signing Off", Toast.LENGTH_LONG).show();

                // Deleting all shared preferences data for the current user to logout.
                SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();


                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.unpaid_orders:
                startActivity(new Intent(this, UnpaidOrders.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
