package it.chiarani.meteotrentinoapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.database.entity.OpenWeatherDataEntity;
import it.chiarani.meteotrentinoapp.database.entity.WeatherForSlotEntity;
import it.chiarani.meteotrentinoapp.database.entity.WeatherReportEntity;
import it.chiarani.meteotrentinoapp.helper.WeatherIconDescriptor;
import it.chiarani.meteotrentinoapp.helper.WeatherTypes;
import it.chiarani.meteotrentinoapp.models.WeatherForDay;
import it.chiarani.meteotrentinoapp.xml_parser.XmlDatiOggi;

public class WeatherStationAdapter extends RecyclerView.Adapter<WeatherStationAdapter.ViewHolder> {

  // #region private fields
  private XmlDatiOggi report;
  // #endregion

  public WeatherStationAdapter(XmlDatiOggi report) {
    this.report = report;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView txt_data;
    TextView txt_temp;


    public ViewHolder(View v) {
      super(v);
      txt_data = v.findViewById(R.id.item_weather_station_data);
      txt_temp = v.findViewById(R.id.item_weather_station_ora);
    }
  }

  @Override
  public WeatherStationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_station, parent, false);
    return new ViewHolder(singleItemLayout);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    //Set data to the individual list item

    holder.txt_data.setText(report.getTemperature().get(0).getTemperature().get(position).getData() + "");
    holder.txt_temp.setText(report.getTemperature().get(0).getTemperature().get(position).getTemperatura() + " °");
  }

  @Override
  public int getItemCount() {
    //Return the number of items in your list
    return report.getTemperature().get(0).getTemperature().size();
  }
}
