package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView alsoKnownAsTextView;
    TextView ingredientsTextView;
    TextView placeOfOriginTextView;
    TextView descriptionTextView;
    ImageView ingredientsIv;

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        alsoKnownAsTextView = findViewById(R.id.also_known_tv);
        ingredientsTextView = findViewById(R.id.ingredients_tv);
        placeOfOriginTextView = findViewById(R.id.origin_tv);
        descriptionTextView = findViewById(R.id.description_tv);
        ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        /*if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }*/

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        StringBuilder alsoKnownStringBuilder = new StringBuilder();
        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        if (alsoKnownAsList.size() > 0) {
            for (int i = 0; i < alsoKnownAsList.size(); i++) {
                alsoKnownStringBuilder.append(alsoKnownAsList.get(i));
                if (i < alsoKnownAsList.size() - 1) {
                    alsoKnownStringBuilder.append(", ");
                }
            }
            alsoKnownAsTextView.setText(alsoKnownStringBuilder.toString());
        }

        StringBuilder ingredientsStringBuilder = new StringBuilder();
        List<String> ingredientsList = sandwich.getIngredients();
        for (int i = 0; i < ingredientsList.size(); i++) {
            ingredientsStringBuilder.append(ingredientsList.get(i));
            if (i < ingredientsList.size() - 1) {
                ingredientsStringBuilder.append("\n");
            }
        }

        ingredientsTextView.setText(ingredientsStringBuilder.toString());
        placeOfOriginTextView.setText(sandwich.getPlaceOfOrigin());
        descriptionTextView.setText(sandwich.getDescription());
    }
}
