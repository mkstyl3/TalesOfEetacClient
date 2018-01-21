package dsa.upc.edu.talesofeetacclient.Controller;

import java.util.List;

import dsa.upc.edu.talesofeetacclient.Model.Main.Relation.ChestItem;
import dsa.upc.edu.talesofeetacclient.Model.Main.Item;
import dsa.upc.edu.talesofeetacclient.Model.Main.Relation.UserItem;
import dsa.upc.edu.talesofeetacclient.Model.Main.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mike on 4/12/17.
 */

public interface ApiService {
    @POST("user/login")
    Call<User> getUserLoginService (@Body User user);
    @GET("chest/{id}/items/all")
    Call<List<Item>> getChestItemsService (@Path("id") int chestId);
    @GET("chest/{id}/items/deleteAll")
    Call<Boolean> deleteChestItemsService(@Path("id") int chestId);
    @POST("user/register")
    Call<User> getUserRegisterService (@Body User user);
    @POST("userItem/add")
    Call<Boolean> setUserItemService (@Body UserItem userItem);
}
