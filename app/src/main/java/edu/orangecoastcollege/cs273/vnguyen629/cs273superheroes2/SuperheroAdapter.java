package edu.orangecoastcollege.cs273.vnguyen629.cs273superheroes2;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Custom adapter to bind an ArrayList of Superheroes to a custom layout.
 */
public class SuperheroAdapter extends ArrayAdapter<Superhero> {
    private Context mContext;
    private int mResourceId;
    private List<Superhero> mAllHeroes;

    private ImageView listItemImageView;
    private TextView listItemTypeTextView;

    /**
     * Parameterized constructor for this custom adapter.
     * @param context The context from which the SuperheroAdapter was created.
     * @param resourceId The layout resource id
     * @param allHeroes The ArrayList containing all Superhero objects.
     */
    public SuperheroAdapter(Context context, int resourceId, ArrayList<Superhero> allHeroes)
    {
        super(context, resourceId, allHeroes);
        this.mResourceId = resourceId;
        this.mContext = context;
        this.mAllHeroes = allHeroes;
    }

    /**
     * Gets the view associated with the layout (sets ImageView and TextView content).
     * @param pos The position of the Superhero selected.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content (ImageView and TextView) set.
     */
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        Superhero superhero = mAllHeroes.get(pos);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        listItemImageView = (ImageView) view.findViewById(R.id.superheroImageView);
        listItemTypeTextView = (TextView) view.findViewById(R.id.guessPromptTextView);

        AssetManager am = mContext.getAssets();
        try {
            InputStream stream = am.open(superhero.getImageName());
            Drawable event = Drawable.createFromStream(stream, superhero.getImageName());
            listItemImageView.setImageDrawable(event);
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Error loading " + superhero.getImageName(), ex);
        }

        listItemTypeTextView.setText(R.string.quiz_type_text);

        return view;
    }
}
