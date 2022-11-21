package Tramas;
public class SegundaTramaAsociacion {
    public String Asociacion2(String TramaAsociacion_1,String PKxB, String PKyB,String NonceB ){
        String seq1=TramaAsociacion_1.substring(0,4);
        String seq2= TramaAsociacion_1.substring(4,6).replace("00","01");
	String seq3= TramaAsociacion_1.substring(6,16).replace("0002030200","0003020200");
	String seq4= TramaAsociacion_1.substring(16,28).replace(TramaAsociacion_1.substring(16,28), TramaAsociacion_1.substring(28,40));
	String seq5= TramaAsociacion_1.substring(28,40).replace(TramaAsociacion_1.substring(28,40), TramaAsociacion_1.substring(16,28));
	String seq6= TramaAsociacion_1.substring(40,44);
	String seq7= TramaAsociacion_1.substring(44,45).replace("0", "1");
	String seq8= TramaAsociacion_1.substring(45,48);
	String MK="0000000000000000";
	String seq9= NonceB;
	String seq10= PKxB;
	String seq11= PKyB;
	String seq12= MK;
	String TramaAsociacion2= seq1.concat(seq2).concat(seq3).concat(seq4).concat(seq5).concat(seq6).concat(seq7).concat(seq8).concat(seq9).concat(seq10).concat(seq11).concat(seq12);
	String CalCRC = "0x" +TramaAsociacion2;
	String asociacion = CalCRC;
	CalculoCRC CRCTramaManagment=new CalculoCRC();
	String CRC=new String();
	CRC=CRCTramaManagment.Crc(asociacion);
        TramaAsociacion2=TramaAsociacion2.concat(CRC);
	return TramaAsociacion2;
    }
}
