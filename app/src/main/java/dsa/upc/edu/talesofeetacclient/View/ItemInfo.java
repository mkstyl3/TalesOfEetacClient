package dsa.upc.edu.talesofeetacclient.View;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dsa.upc.edu.talesofeetacclient.Model.Main.Item;
import dsa.upc.edu.talesofeetacclient.R;

public class ItemInfo extends Activity {

    private TextView id2Text;
    private TextView name2Text;
    private TextView type2Text;
    private TextView cost2Text;

    private TextView id2View;
    private TextView name2View;
    private TextView type2View;
    private TextView cost2View;

    private ImageView item2View;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminfo);

        id2View = (TextView) findViewById(R.id.id2View);
        name2View = (TextView) findViewById(R.id.name2View);
        type2View = (TextView) findViewById(R.id.type2View);
        cost2View = (TextView) findViewById(R.id.cost2View);

        item2View = (ImageView) findViewById(R.id.item2View);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        id2View.setText(String.valueOf(item.getId()));
        name2View.setText(item.getName());
        type2View.setText(item.getType());
        cost2View.setText(item.getCost());
        //Picasso.with(getBaseContext()).load(user.getAvatarUrl()).into(userAvatarView);
        Picasso.with(getBaseContext()).load("http://i.imgur.com/DvpvklR.png").into(item2View);
    }

}
