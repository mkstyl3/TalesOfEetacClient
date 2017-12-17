package dsa.upc.edu.talesofeetacclient.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dsa.upc.edu.talesofeetacclient.Model.Main.User;
import dsa.upc.edu.talesofeetacclient.R;

/**
 * Created by root on 4/06/17.
 */

public class UserInfo extends Activity {

    private TextView idView;
    private TextView nameView;
    private TextView passwordView;
    private TextView emailView;
    private ListView lv;
    private User user;
    private ImageView userAvatarView;
    private ImageView itemAvatarView;
    private TextView itemNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        //activity_this
        idView = (TextView) findViewById(R.id.idView);
        nameView = (TextView) findViewById(R.id.nameView);
        passwordView = (TextView) findViewById(R.id.passwordView);
        emailView = (TextView) findViewById(R.id.emailView);
        userAvatarView = (ImageView) findViewById(R.id.userAvatarView);
        lv = (ListView) findViewById(R.id.itemListView);
        //activity_customlist
        itemAvatarView = (ImageView) findViewById(R.id.itemAvatarView);
        itemNameView = (TextView) findViewById(R.id.itemNameView);

        //Pick up the intent's data and load variables

        Intent intent = getIntent();
        user = intent.getParcelableExtra("profile");
        //Fill data
        idView.setText(String.valueOf(user.getId()));
        nameView.setText(user.getName());
        passwordView.setText(user.getPassword());
        emailView.setText(user.getEmail());
        //Picasso.with(getBaseContext()).load(user.getAvatarUrl()).into(userAvatarView);
        Picasso.with(getBaseContext()).load("http://i.imgur.com/DvpvklR.png").into(userAvatarView);
        ListAdapter adapter = new ListAdapter(
                getApplicationContext(), R.layout.activity_userinfo, user.getItems()
        );
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent = new Intent(getBaseContext(), ItemInfo.class);

                intent.putExtra("item", user.getItem(position));
                startActivityForResult(intent,3);
            }
        });
        lv.setAdapter(adapter);
    }
}
