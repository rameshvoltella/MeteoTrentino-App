package it.chiarani.meteotrentinoapp.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.API_endpoint;

/**
 * Adapter for allerte used in AllerteActivity
 */

public class AllerteListAdapter extends RecyclerView.Adapter<AllerteListAdapter.ViewHolder> {

  // #region private fields
  private ArrayList<String> allerte_list;
  // #endregion

  public AllerteListAdapter(ArrayList<String> data) {
    this.allerte_list = data;
  }

  /**
   * ViewHolder
   */
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * UI elements
     */
    TextView    txt_name;
    ImageButton btn_link;
    CardView    card;

    public ViewHolder(View v) {
      super(v);
      txt_name = v.findViewById(R.id.item_allerte_txt_name);
      btn_link = v.findViewById(R.id.item_allerte_btn_link);
      card     = v.findViewById(R.id.item_allerte_cardview);
      v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int pos = this.getLayoutPosition();
      String link = allerte_list.get(pos).split(";")[2];

      // Open link
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_endpoint.GOOGLE_DOCS_BASE + link));
      v.getContext().startActivity(browserIntent);
    }
  }

  @Override
  public AllerteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allerte, parent, false);
    return new ViewHolder(singleItemLayout);
  }

  /**
   * Single Item
   */
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    String[] allerta = allerte_list.get(position).split(";");
    String data   = allerta[1];
    String titolo = allerta[0];
    String link   = allerta[2];
    holder.txt_name.setText(String.format("%s \n %s", data, titolo));

    holder.btn_link.setOnClickListener(v -> {
      // open link
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_endpoint.GOOGLE_DOCS_BASE + link));
      v.getContext().startActivity(browserIntent);
    });
  }

  @Override
  public int getItemCount() {
    return allerte_list.size();
  }
}
