package davis.androidapp.somco.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import davis.androidapp.somco.R;
import davis.androidapp.somco.db.AppDatabase;
import davis.androidapp.somco.db.AppPref;
import davis.androidapp.somco.db.model.User;
import davis.androidapp.somco.networking.model.RegisterRequest;
import davis.androidapp.somco.ui.base.BaseActivity;
import davis.androidapp.somco.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.input_name)
    EditText inputName;

    @BindView(R.id.input_contact_phone)
    EditText inputContactPhone;

    @BindView(R.id.input_business_phone)
    EditText inputBusinessPhone;

    @BindView(R.id.input_contact_name)
    EditText inputContactName;

    @BindView(R.id.input_email)
    EditText inputEmail;

    @BindView(R.id.input_password)
    EditText inputPassword;

    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        hideToolbar();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.btn_register)
    void onRegisterClick() {
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String businessPhone = inputBusinessPhone.getText().toString();
        String contactName = inputContactName.getText().toString();
        String contactPhone = inputContactPhone.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(businessPhone) || TextUtils.isEmpty(contactName) || TextUtils.isEmpty(contactPhone)) {
            Toast.makeText(getApplicationContext(), getString(R.string.msg_fill_the_form), Toast.LENGTH_LONG).show();
            return;
        }

        loader.setVisibility(View.VISIBLE);
        RegisterRequest request = new RegisterRequest();
        request.name = name;
        request.businessPhone=businessPhone;
        request.contactName=contactName;
        request.contactPhone=contactPhone;
        request.email = email;
        request.password = password;
        request.confirmPassword = password;
        getApi().register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                loader.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    handleError(response.errorBody());
                    return;
                }

                AppDatabase.saveUser(response.body());
                AppPref.getInstance().saveAuthToken(response.body().token);
                launchSplash(RegisterActivity.this);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loader.setVisibility(View.INVISIBLE);
                handleError(t);
            }
        });
    }

    @OnClick(R.id.btn_login_account)
    void onCreateAccountClick() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
