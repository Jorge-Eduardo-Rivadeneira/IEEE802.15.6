package Tramas;

public class TerceraTramaAsociacion {
    public String Asociacion3(String TramaAsociacion_2,String MK_3B ){
        String seq1=TramaAsociacion_2.substring(0,4);
        String seq2= TramaAsociacion_2.substring(4,6).replace("01","02");
	String seq3= TramaAsociacion_2.substring(6,44);
	String seq4= TramaAsociacion_2.substring(44,45).replace("1", "2");
	String seq5= TramaAsociacion_2.substring(45,48);
	String NonceB=TramaAsociacion_2.substring(48,80);
	String PKxB=TramaAsociacion_2.substring(80,144);
	String PKyB=TramaAsociacion_2.substring(144,208);
	String seq6= NonceB;
	String seq7= PKxB;
	String seq8= PKyB;
	String seq9= MK_3B;
	String TramaAsociacion3= seq1.concat(seq2).concat(seq3).concat(seq4).concat(seq5).concat(seq6).concat(seq7).concat(seq8).concat(seq9);
	String CalCRC = "0x" +TramaAsociacion3;
	String asociacion = CalCRC;
	CalculoCRC CRCTramaManagment=new CalculoCRC();
	String CRC=new String();
	CRC=CRCTramaManagment.Crc(asociacion);
	TramaAsociacion3=TramaAsociacion3.concat(CRC);
	return TramaAsociacion3;
    }
}
