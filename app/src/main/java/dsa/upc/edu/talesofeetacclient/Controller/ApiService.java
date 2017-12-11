package dsa.upc.edu.talesofeetacclient.Controller;

import dsa.upc.edu.talesofeetacclient.Model.Main.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mike on 4/12/17.
 */

public interface ApiService {
    @POST("user/login")
    Call<User> getUserLoginService (@Body User user);
}
