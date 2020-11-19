package id.alfarizi.babymonitoring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLog {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Hari")
    @Expose
    private String hari;
    @SerializedName("Jam")
    @Expose
    private String jam;
    @SerializedName("Tanggal")
    @Expose
    private String tanggal;

    public ModelLog() {
    }

    public ModelLog(String status, String hari, String jam, String tanggal) {
        this.status = status;
        this.hari = hari;
        this.jam = jam;
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}