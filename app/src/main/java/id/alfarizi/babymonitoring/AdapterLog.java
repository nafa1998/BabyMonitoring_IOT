package id.alfarizi.babymonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterLog extends RecyclerView.Adapter<AdapterLog.AdapterLogViewHolder>{

    private Context ctx;
    private List<ModelLog> list;

    public AdapterLog(Context ctx, List<ModelLog> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_riwayat, parent, false);
        return new AdapterLogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLogViewHolder holder, int position) {
        final ModelLog data = list.get(position);

        String hari = new WaktuTanggalJam().HanyaTanggal(data.getTanggal());
        holder.waktu.setText(data.getHari() + ", " +hari +"\nJam "+data.getJam());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AdapterLogViewHolder extends RecyclerView.ViewHolder {
        TextView waktu;
        public AdapterLogViewHolder(@NonNull View itemView) {
            super(itemView);
            waktu = (TextView) itemView.findViewById(R.id.item_waktu);
        }
    }
}
