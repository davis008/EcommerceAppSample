package davis.androidapp.somco.networking.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    public String name;
    public String businessPhone;
    public String contactName;
    public String contactPhone;
    public String email;
    public String password;
    @SerializedName("c_password")
    public String confirmPassword;
}
