package android.salesianostriana.com.a12_login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.salesianostriana.com.a12_login.retrofit.generator.ServiceGenerator;
import android.salesianostriana.com.a12_login.model.LoginResponse;
import android.salesianostriana.com.a12_login.retrofit.services.LoginService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    EditText email, password;
    Button btn_login, btn_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_registro = findViewById(R.id.btn_registro);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username_txt = email.getText().toString();
                String password_txt = password.getText().toString();

                String credentials = Credentials.basic(username_txt, password_txt);

                LoginService service = ServiceGenerator.createService(LoginService.class);
                Call<LoginResponse> call = service.doLogin(credentials);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.code() != 201) {
                          // error
                            Log.e("RequestError", response.message());
                            Toast.makeText(MainActivity.this, "Error de petición", Toast.LENGTH_SHORT).show();
                        } else {
                            // exito
                            // Toast.makeText(MainActivity.this, response.body().getToken(), Toast.LENGTH_LONG).show();
                            /*
                                Pasos:
                                    1) Almacenar el token donde corresponda.
                                    2) Lanzar el siguiente Activity.
                             */
                            // ServiceGenerator.jwtToken = response.body().getToken();
                            /*SharedPreferences sharedPreferences =
                                    getSharedPreferences(getString(R.string.sharedpreferences_filename),
                                            Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.jwt_key), response.body().getToken());
                            editor.commit();*/
                            UtilToken.setToken(MainActivity.this, response.body().getToken());

                            startActivity(new Intent(MainActivity.this, UserListActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("NetworkFailure", t.getMessage());
                        Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });

    }
}
