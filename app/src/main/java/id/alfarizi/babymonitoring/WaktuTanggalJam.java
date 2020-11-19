package id.alfarizi.babymonitoring;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WaktuTanggalJam {


  public String HanyaTanggal (String tgl){
    String hasil_tgl = "";

    try {
      SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
      Date newDate = formatdate.parse(tgl);
      formatdate = new SimpleDateFormat("dd MMMM yyyy");
      hasil_tgl = formatdate.format(newDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return hasil_tgl;

  }

}
